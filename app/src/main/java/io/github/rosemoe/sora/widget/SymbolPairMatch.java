/*
 *    sora-editor - the awesome code editor for Android
 *    https://github.com/Rosemoe/sora-editor
 *    Copyright (C) 2020-2024  Rosemoe
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 *
 *     Please contact Rosemoe by email 2073412493@qq.com if you need
 *     additional information or have any questions
 */
package io.github.rosemoe.sora.widget;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.rosemoe.sora.lang.analysis.AnalyzeManager;
import io.github.rosemoe.sora.text.CharPosition;
import io.github.rosemoe.sora.text.Content;
import io.github.rosemoe.sora.text.ContentLine;

/**
 * Define symbol pairs to complete them automatically when the user
 * enters the first character of pair.
 *
 * @author Rosemoe
 */
public class SymbolPairMatch {

    private final Map<Character, SymbolPair> singleCharPairMaps = new HashMap<>();

    private final Map<Character, List<SymbolPair>> multipleCharByEndPairMaps = new HashMap<>();

    private SymbolPairMatch parent;

    public SymbolPairMatch() {
        this(null);
    }

    public SymbolPairMatch(SymbolPairMatch parent) {
        setParent(parent);
    }

    protected void setParent(SymbolPairMatch parent) {
        this.parent = parent;
    }

    /**
     * Put a pair of symbol completion
     * When the user types the {@param singleCharacter}, it will be replaced by {@param symbolPair}
     * SymbolPair maybe null to disable completion for this character.
     *
     * @see SymbolPair
     */
    public void putPair(char singleCharacter, SymbolPair symbolPair) {
        singleCharPairMaps.put(singleCharacter, symbolPair);
    }

    /**
     * Put a pair of symbol completion
     * When the user types the {@param charArray}, it will be replaced by {@param symbolPair}
     * SymbolPair maybe null to disable completion for this character.
     *
     * @see SymbolPair
     */
    public void putPair(char[] charArray, SymbolPair symbolPair) {
        char endChar = charArray[charArray.length - 1];
        var list = multipleCharByEndPairMaps.get(endChar);

        if (list == null) {
            list = new ArrayList<>();
        }

        list.add(symbolPair);
        multipleCharByEndPairMaps.put(endChar, list);
    }

    /**
     * Put a pair of symbol completion
     * When the user types the {@param openString}, it will be replaced by {@param symbolPair}
     * SymbolPair maybe null to disable completion for this character.
     *
     * @see #putPair(char[], SymbolPair)
     */
    public void putPair(String openString, SymbolPair symbolPair) {
        putPair(openString.toCharArray(), symbolPair);
    }


    @Nullable
    public final SymbolPair matchBestPairBySingleChar(char editChar) {
        var pair = singleCharPairMaps.get(editChar);
        if (pair == null && parent != null) {
            return parent.matchBestPairBySingleChar(editChar);
        }
        return pair;
    }

    public final List<SymbolPair> matchBestPairList(char editChar) {
        var result = multipleCharByEndPairMaps.get(editChar);

        if (result == null && parent != null) {
            var parentResult = parent.matchBestPairList(editChar);
            result = new ArrayList<>(parentResult);
        }

        return result == null ? Collections.emptyList() : result;
    }



    public void removeAllPairs() {
        singleCharPairMaps.clear();
        multipleCharByEndPairMaps.clear();
    }

    /**
     * Defines a replacement of input
     */
    public static class SymbolPair {

        /**
         * Defines that this character does not have to be replaced
         */
        public final static SymbolPair EMPTY_SYMBOL_PAIR = new SymbolPair("", "");

        public final String open;

        public final String close;

        private SymbolPairEx symbolPairEx;

        private int cursorOffset;

        private int insertOffset;


        /**
         * If your {@param open} string and  {@param close} string are both ', it makes a pair of single quotes.
         * This will replace the entered character with a pair of single quotes,
         * and will move the cursor to the middle of the pair.
         * This class defines these symbol pairs
         */
        public SymbolPair(String open, String close) {
            this.open = open;
            this.close = close;
        }

        public SymbolPair(String open, String close, SymbolPairEx symbolPairEx) {
            this(open, close);
            this.symbolPairEx = symbolPairEx;
        }




        protected boolean shouldDoAutoSurround(Content content) {
            if (symbolPairEx == null) {
                return false;
            }
            return symbolPairEx.shouldDoAutoSurround(content);
        }

        protected void measureCursorPosition(int offsetIndex) {
            cursorOffset = offsetIndex + open.length();
            insertOffset = offsetIndex;
        }

        protected int getCursorOffset() {
            return cursorOffset;
        }

        public int getInsertOffset() {
            return insertOffset;
        }

        public interface SymbolPairEx {
            /**
             * The method will be called
             * to decide whether to perform the replacement or not.
             * It may be same as vscode language-configuration Auto-closing 'notIn'.
             * also see <a href="https://code.visualstudio.com/api/language-extensions/language-configuration-guide#autoclosing">this</a>
             * If not implemented, always return true
             *
             * @param editor      The current edit content,
             *                    sometimes you may need to get the analyzed data from {@link AnalyzeManager}
             *                    (e.g. token with tags) and use editor to get more information,
             *                    such as the line of the cursor.
             * @param currentLine The current line edit in the editor,quick analysis it to decide whether to replaced
             * @param leftColumn  return current cursor column
             */


            /**
             * when before the replaced and select a range,surrounds the selected content with return ture.
             * If not implemented, always return false
             * also see <a href="https://code.visualstudio.com/api/language-extensions/language-configuration-guide#autosurrounding">this</a>
             */
            default boolean shouldDoAutoSurround(Content content) {
                return false;
            }

        }

    }

    public final static class DefaultSymbolPairs extends SymbolPairMatch {

        public DefaultSymbolPairs() {
            super.putPair('{', new SymbolPair("{", "}"));
            super.putPair('(', new SymbolPair("(", ")"));
            super.putPair('[', new SymbolPair("[", "]"));
            super.putPair('"', new SymbolPair("\"", "\"", new SymbolPair.SymbolPairEx() {
                @Override
                public boolean shouldDoAutoSurround(Content content) {
                    return content.getCursor().isSelected();
                }
            }));
            super.putPair('\'', new SymbolPair("'", "'", new SymbolPair.SymbolPairEx() {
                @Override
                public boolean shouldDoAutoSurround(Content content) {
                    return content.getCursor().isSelected();
                }
            }));
        }

    }

}
