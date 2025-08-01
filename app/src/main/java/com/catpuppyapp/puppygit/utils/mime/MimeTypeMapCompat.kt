/*
 * Copyright (c) 2021 Hai Zhang <dreaming.in.code.zh@gmail.com>
 * All Rights Reserved.
 */

package com.catpuppyapp.puppygit.utils.mime

import android.webkit.MimeTypeMap

// Generated by mime/generate-extensions.sh and mime/generate-code.sh .
private val extensionToMimeTypeMap = mapOf(
    "%" to "application/x-trash",
    "323" to "text/h323",
    "3g2" to "video/3gpp2",
    "3ga" to "audio/3gpp",
    "3gp" to "video/3gpp",
    "3gp2" to "video/3gpp2",
    "3gpp" to "video/3gpp",
    "3gpp2" to "video/3gpp2",
    "7z" to "application/x-7z-compressed",
    "a52" to "audio/ac3",
    "aac" to "audio/aac",
    "abw" to "application/x-abiword",
    "ac3" to "audio/ac3",
    "adt" to "audio/aac",
    "adts" to "audio/aac",
    "ai" to "application/postscript",
    "aif" to "audio/x-aiff",
    "aifc" to "audio/x-aiff",
    "aiff" to "audio/x-aiff",
    "alc" to "chemical/x-alchemy",
    "amr" to "audio/amr",
    "anx" to "application/annodex",
    "apk" to "application/vnd.android.package-archive",
    "appcache" to "text/cache-manifest",
    "application" to "application/x-ms-application",
    "art" to "image/x-jg",
    "arw" to "image/x-sony-arw",
    "asc" to "text/plain",
    "asf" to "video/x-ms-asf",
    "asn" to "chemical/x-ncbi-asn1-spec",
    "aso" to "chemical/x-ncbi-asn1-binary",
    "asx" to "video/x-ms-asf",
    "atom" to "application/atom+xml",
    "atomcat" to "application/atomcat+xml",
    "atomsrv" to "application/atomserv+xml",
    "au" to "audio/basic",
    "avi" to "video/avi",
    "avif" to "image/avif",
    "awb" to "audio/amr-wb",
    "axa" to "audio/annodex",
    "axv" to "video/annodex",
    "b" to "chemical/x-molconn-Z",
    "bak" to "application/x-trash",
    "bat" to "application/x-msdos-program",
    "bcpio" to "application/x-bcpio",
    "bib" to "text/x-bibtex",
    "bin" to "application/octet-stream",
    "bmp" to "image/x-ms-bmp",
    "boo" to "text/x-boo",
    "book" to "application/x-maker",
    "brf" to "text/plain",
    "bsd" to "chemical/x-crossfire",
    "c" to "text/x-csrc",
    "c++" to "text/x-c++src",
    "c3d" to "chemical/x-chem3d",
    "cab" to "application/x-cab",
    "cac" to "chemical/x-cache",
    "cache" to "chemical/x-cache",
    "cap" to "application/vnd.tcpdump.pcap",
    "cascii" to "chemical/x-cactvs-binary",
    "cat" to "application/vnd.ms-pki.seccat",
    "cbin" to "chemical/x-cactvs-binary",
    "cbr" to "application/x-cbr",
    "cbz" to "application/x-cbz",
    "cc" to "text/x-c++src",
    "cda" to "application/x-cdf",
    "cdf" to "application/x-cdf",
    "cdr" to "image/x-coreldraw",
    "cdt" to "image/x-coreldrawtemplate",
    "cdx" to "chemical/x-cdx",
    "cdy" to "application/vnd.cinderella",
    "cef" to "chemical/x-cxf",
    "cer" to "application/pkix-cert",
    "chm" to "chemical/x-chemdraw",
    "chrt" to "application/x-kchart",
    "cif" to "chemical/x-cif",
    "class" to "application/java-vm",
    "cls" to "text/x-tex",
    "cmdf" to "chemical/x-cmdf",
    "cml" to "chemical/x-cml",
    "cod" to "application/vnd.rim.cod",
    "com" to "application/x-msdos-program",
    "cpa" to "chemical/x-compass",
    "cpio" to "application/x-cpio",
    "cpp" to "text/x-c++src",
    "cpt" to "image/x-corelphotopaint",
    "cr2" to "image/x-canon-cr2",
    "crl" to "application/x-pkcs7-crl",
    "crt" to "application/x-x509-ca-cert",
    "crw" to "image/x-canon-crw",
    "csd" to "audio/csound",
    "csf" to "chemical/x-cache-csf",
    "csh" to "text/x-csh",
    "csm" to "chemical/x-csml",
    "csml" to "chemical/x-csml",
    "css" to "text/css",
    "csv" to "text/comma-separated-values",
    "ctab" to "chemical/x-cactvs-binary",
    "ctx" to "chemical/x-ctx",
    "cu" to "application/cu-seeme",
    "cub" to "chemical/x-gaussian-cube",
    "cur" to "image/ico",
    "cxf" to "chemical/x-cxf",
    "cxx" to "text/x-c++src",
    "d" to "text/x-dsrc",
    "davmount" to "application/davmount+xml",
    "dcm" to "application/dicom",
    "dcr" to "application/x-director",
    "ddeb" to "application/vnd.debian.binary-package",
    "deb" to "application/x-debian-package",
    "deploy" to "application/octet-stream",
    "der" to "application/x-x509-ca-cert",
    "dfxp" to "application/ttml+xml",
    "dif" to "video/dv",
    "diff" to "text/plain",
    "dir" to "application/x-director",
    "djv" to "image/vnd.djvu",
    "djvu" to "image/vnd.djvu",
    "dl" to "video/dl",
    "dll" to "application/x-msdos-program",
    "dmg" to "application/x-apple-diskimage",
    "dms" to "application/x-dms",
    "dng" to "image/x-adobe-dng",
    "doc" to "application/msword",
    "docm" to "application/vnd.ms-word.document.macroEnabled.12",
    "docx" to "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
    "dot" to "application/msword",
    "dotm" to "application/vnd.ms-word.template.macroEnabled.12",
    "dotx" to "application/vnd.openxmlformats-officedocument.wordprocessingml.template",
    "dv" to "video/dv",
    "dvi" to "application/x-dvi",
    "dx" to "chemical/x-jcamp-dx",
    "dxr" to "application/x-director",
    "emb" to "chemical/x-embl-dl-nucleotide",
    "embl" to "chemical/x-embl-dl-nucleotide",
    "eml" to "message/rfc822",
    "ent" to "chemical/x-pdb",
    "eot" to "application/vnd.ms-fontobject",
    "eps" to "application/postscript",
    "eps2" to "application/postscript",
    "eps3" to "application/postscript",
    "epsf" to "application/postscript",
    "epsi" to "application/postscript",
    "epub" to "application/epub+zip",
    "erf" to "image/x-epson-erf",
    "es" to "application/ecmascript",
    "etx" to "text/x-setext",
    "exe" to "application/x-msdos-program",
    "ez" to "application/andrew-inset",
    "f4a" to "audio/mp4",
    "f4b" to "audio/mp4",
    "f4p" to "audio/mp4",
    "f4v" to "video/mp4",
    "fb" to "application/x-maker",
    "fbdoc" to "application/x-maker",
    "fch" to "chemical/x-gaussian-checkpoint",
    "fchk" to "chemical/x-gaussian-checkpoint",
    "fig" to "application/x-xfig",
    "fl" to "application/x-android-drm-fl",
    "flac" to "audio/flac",
    "fli" to "video/fli",
    "flv" to "video/x-flv",
    "fm" to "application/x-maker",
    "frame" to "application/x-maker",
    "frm" to "application/x-maker",
    "gal" to "chemical/x-gaussian-log",
    "gam" to "chemical/x-gamess-input",
    "gamin" to "chemical/x-gamess-input",
    "gan" to "application/x-ganttproject",
    "gau" to "chemical/x-gaussian-input",
    "gcd" to "text/x-pcs-gcd",
    "gcf" to "application/x-graphing-calculator",
    "gcg" to "chemical/x-gcg8-sequence",
    "gen" to "chemical/x-genbank",
    "gf" to "application/x-tex-gf",
    "gif" to "image/gif",
    "gjc" to "chemical/x-gaussian-input",
    "gjf" to "chemical/x-gaussian-input",
    "gl" to "video/gl",
    "gnumeric" to "application/x-gnumeric",
    "gpt" to "chemical/x-mopac-graph",
    "gsf" to "application/x-font",
    "gsm" to "audio/x-gsm",
    "gtar" to "application/x-gtar",
    "gz" to "application/gzip",
    "h" to "text/x-chdr",
    "h++" to "text/x-c++hdr",
    "hdf" to "application/x-hdf",
    "heic" to "image/heic",
    "heics" to "image/heic-sequence",
    "heif" to "image/heif",
    "heifs" to "image/heif-sequence",
    "hh" to "text/x-c++hdr",
    "hif" to "image/heif",
    "hin" to "chemical/x-hin",
    "hpp" to "text/x-c++hdr",
    "hqx" to "application/mac-binhex40",
    "hs" to "text/x-haskell",
    "hta" to "application/hta",
    "htc" to "text/x-component",
    "htm" to "text/html",
    "html" to "text/html",
    "hwp" to "application/x-hwp",
    "hxx" to "text/x-c++hdr",
    "ica" to "application/x-ica",
    "ice" to "x-conference/x-cooltalk",
    "ico" to "image/x-icon",
    "ics" to "text/calendar",
    "icz" to "text/calendar",
    "ief" to "image/ief",
    "iges" to "model/iges",
    "igs" to "model/iges",
    "iii" to "application/x-iphone",
    "imy" to "audio/imelody",
    "info" to "application/x-info",
    "inp" to "chemical/x-gamess-input",
    "ins" to "application/x-internet-signup",
    "iso" to "application/x-iso9660-image",
    "isp" to "application/x-internet-signup",
    "ist" to "chemical/x-isostar",
    "istr" to "chemical/x-isostar",
    "jad" to "text/vnd.sun.j2me.app-descriptor",
    "jam" to "application/x-jam",
    "jar" to "application/java-archive",
    "java" to "text/x-java",
    "jdx" to "chemical/x-jcamp-dx",
    "jmz" to "application/x-jmol",
    "jng" to "image/x-jng",
    "jnlp" to "application/x-java-jnlp-file",
    "jp2" to "image/jp2",
    "jpe" to "image/jpeg",
    "jpeg" to "image/jpeg",
    "jpf" to "image/jpx",
    "jpg" to "image/jpeg",
    "jpg2" to "image/jp2",
    "jpm" to "image/jpm",
    "jpx" to "image/jpx",
    "js" to "application/javascript",
    "json" to "application/json",
    "jsonld" to "application/ld+json",
    "kar" to "audio/midi",
    "key" to "application/pgp-keys",
    "kil" to "application/x-killustrator",
    "kin" to "chemical/x-kinemage",
    "kml" to "application/vnd.google-earth.kml+xml",
    "kmz" to "application/vnd.google-earth.kmz",
    "kpr" to "application/x-kpresenter",
    "kpt" to "application/x-kpresenter",
    "ksp" to "application/x-kspread",

    // START: I added
    "iml" to "text/xml",
    "mdown" to "text/markdown",
    "kt" to "application/x-kotlin",
    "kts" to "application/x-kotlin",
    "kotlin" to "application/x-kotlin",
    "groovy" to "application/x-groovy",
    "gradle" to "application/x-groovy",

    "go" to "application/x-go",
    "jsx" to "application/x-jsx",
    "tsx" to "application/x-tsx",
    "bat" to "application/x-batchfile",
    "ps1" to "application/x-powershell",

    "less" to "application/x-less",
    "scss" to "application/x-scss",
    "sass" to "application/x-scss",
    "rs" to "application/x-rust",
    "rust" to "application/x-rust",
    "coffee" to "application/x-coffee",
    "clj" to "application/x-clojure",
    "cljs" to "application/x-clojure",
    "lua" to "application/x-lua",
    "php" to "application/x-php",
    "r" to "application/x-r",
    "sql" to "application/x-sql",
    "swift" to "application/x-swift",
    "vb" to "application/x-vb",
    "pl6" to "application/x-raku",
    "raku" to "application/x-raku",
    "rakudoc" to "application/x-raku",
    "rakumod" to "application/x-raku",
    "toml" to "application/x-toml",
    "dart" to "application/x-dart",
    "dockerfile" to "application/x-dockerfile",
    "makefile" to "application/x-makefile",
    "vagrantfile" to "application/x-ruby",
    "zig" to "application/x-zig",
    "zon" to "application/x-zig",
    "pro" to "application/x-proguard",
    "proguard" to "application/x-proguard",
    "r8" to "application/x-proguard",
    "vue" to "application/x-vue",


    // END: I added

    "kwd" to "application/x-kword",
    "kwt" to "application/x-kword",
    "latex" to "application/x-latex",
    "lha" to "application/x-lha",
    "lhs" to "text/x-literate-haskell",
    "lin" to "application/bbolin",
    "lrc" to "application/lrc",
    "lsf" to "video/x-la-asf",
    "lsx" to "video/x-la-asf",
    "ltx" to "text/x-tex",
    "ly" to "text/x-lilypond",
    "lyx" to "application/x-lyx",
    "lzh" to "application/x-lzh",
    "lzx" to "application/x-lzx",
    "m1v" to "video/mpeg",
    "m2t" to "video/mpeg",
    "m2ts" to "video/mp2t",
    "m2v" to "video/mpeg",
    "m3g" to "application/m3g",
    "m3u" to "audio/x-mpegurl",
    "m3u8" to "audio/x-mpegurl",
    "m4a" to "audio/mpeg",
    "m4b" to "audio/mp4",
    "m4p" to "audio/mp4",
    "m4r" to "audio/mpeg",
    "m4v" to "video/mp4",
    "maker" to "application/x-maker",
    "man" to "application/x-troff-man",
    "manifest" to "application/x-ms-manifest",
    "markdown" to "text/markdown",
    "mbox" to "application/mbox",
    "mcif" to "chemical/x-mmcif",
    "mcm" to "chemical/x-macmolecule",
    "md" to "text/markdown",
    "mdb" to "application/msaccess",
    "me" to "application/x-troff-me",
    "mesh" to "model/mesh",
    "mid" to "audio/midi",
    "midi" to "audio/midi",
    "mif" to "application/x-mif",
    "mjs" to "application/javascript",
    "mka" to "audio/x-matroska",
    "mkv" to "video/x-matroska",
    "mm" to "application/x-freemind",
    "mmd" to "chemical/x-macromodel-input",
    "mmf" to "application/vnd.smaf",
    "mml" to "text/mathml",
    "mmod" to "chemical/x-macromodel-input",
    "mng" to "video/x-mng",
    "mobi" to "application/x-mobipocket-ebook",
    "moc" to "text/x-moc",
    "mol" to "chemical/x-mdl-molfile",
    "mol2" to "chemical/x-mol2",
    "moo" to "chemical/x-mopac-out",
    "mop" to "chemical/x-mopac-input",
    "mopcrt" to "chemical/x-mopac-input",
    "mov" to "video/quicktime",
    "movie" to "video/x-sgi-movie",
    "mp1" to "audio/mpeg",
    "mp1v" to "video/mpeg",
    "mp2" to "audio/mpeg",
    "mp2v" to "video/mpeg",
    "mp3" to "audio/mpeg",
    "mp4" to "video/mp4",
    "mp4v" to "video/mp4",
    "mpa" to "audio/mpeg",
    "mpc" to "chemical/x-mopac-input",
    "mpe" to "video/mpeg",
    "mpeg" to "video/mpeg",
    "mpeg1" to "video/mpeg",
    "mpeg2" to "video/mpeg",
    "mpeg4" to "video/mp4",
    "mpega" to "audio/mpeg",
    "mpg" to "video/mpeg",
    "mpga" to "audio/mpeg",
    "mph" to "application/x-comsol",
    "mpv" to "video/x-matroska",
    "mpv1" to "video/mpeg",
    "mpv2" to "video/mpeg",
    "ms" to "application/x-troff-ms",
    "msh" to "model/mesh",
    "msi" to "application/x-msi",
    "msp" to "application/octet-stream",
    "msu" to "application/octet-stream",
    "mts" to "video/mp2t",
    "mvb" to "chemical/x-mopac-vib",
    "mxf" to "application/mxf",
    "mxmf" to "audio/mobile-xmf",
    "mxu" to "video/vnd.mpegurl",
    "nb" to "application/mathematica",
    "nbp" to "application/mathematica",
    "nc" to "application/x-netcdf",
    "nef" to "image/x-nikon-nef",
    "nrw" to "image/x-nikon-nrw",
    "nwc" to "application/x-nwc",
    "o" to "application/x-object",
    "oda" to "application/oda",
    "odb" to "application/vnd.oasis.opendocument.database",
    "odc" to "application/vnd.oasis.opendocument.chart",
    "odf" to "application/vnd.oasis.opendocument.formula",
    "odg" to "application/vnd.oasis.opendocument.graphics",
    "odi" to "application/vnd.oasis.opendocument.image",
    "odm" to "application/vnd.oasis.opendocument.text-master",
    "odp" to "application/vnd.oasis.opendocument.presentation",
    "ods" to "application/vnd.oasis.opendocument.spreadsheet",
    "odt" to "application/vnd.oasis.opendocument.text",
    "oga" to "audio/ogg",
    "ogg" to "audio/ogg",
    "ogv" to "video/ogg",
    "ogx" to "application/ogg",
    "old" to "application/x-trash",
    "one" to "application/onenote",
    "onepkg" to "application/onenote",
    "onetmp" to "application/onenote",
    "onetoc2" to "application/onenote",
    "opf" to "application/oebps-package+xml",
    "opus" to "audio/ogg",
    "orc" to "audio/csound",
    "orf" to "image/x-olympus-orf",
    "ota" to "application/vnd.android.ota",
    "otf" to "font/ttf",
    "otg" to "application/vnd.oasis.opendocument.graphics-template",
    "oth" to "application/vnd.oasis.opendocument.text-web",
    "otp" to "application/vnd.oasis.opendocument.presentation-template",
    "ots" to "application/vnd.oasis.opendocument.spreadsheet-template",
    "ott" to "application/vnd.oasis.opendocument.text-template",
    "oza" to "application/x-oz-application",
    "p" to "text/x-pascal",
    "p12" to "application/x-pkcs12",
    "p7r" to "application/x-pkcs7-certreqresp",
    "pac" to "application/x-ns-proxy-autoconfig",
    "pas" to "text/x-pascal",
    "pat" to "image/x-coreldrawpattern",
    "patch" to "text/x-diff",
    "pbm" to "image/x-portable-bitmap",
    "pcap" to "application/vnd.tcpdump.pcap",
    "pcf" to "application/x-font",
    "pcf.Z" to "application/x-font-pcf",
    "pcx" to "image/pcx",
    "pdb" to "chemical/x-pdb",
    "pdf" to "application/pdf",
    "pef" to "image/x-pentax-pef",
    "pem" to "application/x-pem-file",
    "pfa" to "application/x-font",
    "pfb" to "application/x-font",
    "pfr" to "application/font-tdpfr",
    "pfx" to "application/x-pkcs12",
    "pgm" to "image/x-portable-graymap",
    "pgn" to "application/x-chess-pgn",
    "pgp" to "application/pgp-signature",
    "phps" to "text/text",
    "pk" to "application/x-tex-pk",
    "pl" to "text/x-perl",
    "pls" to "audio/x-scpls",
    "pm" to "text/x-perl",
    "png" to "image/png",
    "pnm" to "image/x-portable-anymap",
    "po" to "text/plain",
    "pot" to "application/vnd.ms-powerpoint",
    "potm" to "application/vnd.ms-powerpoint.template.macroEnabled.12",
    "potx" to "application/vnd.openxmlformats-officedocument.presentationml.template",
    "ppam" to "application/vnd.ms-powerpoint.addin.macroEnabled.12",
    "ppm" to "image/x-portable-pixmap",
    "pps" to "application/vnd.ms-powerpoint",
    "ppsm" to "application/vnd.ms-powerpoint.slideshow.macroEnabled.12",
    "ppsx" to "application/vnd.openxmlformats-officedocument.presentationml.slideshow",
    "ppt" to "application/vnd.ms-powerpoint",
    "pptm" to "application/vnd.ms-powerpoint.presentation.macroEnabled.12",
    "pptx" to "application/vnd.openxmlformats-officedocument.presentationml.presentation",
    "prc" to "application/x-mobipocket-ebook",
    "prf" to "application/pics-rules",
    "prt" to "chemical/x-ncbi-asn1-ascii",
    "ps" to "application/postscript",
    "psd" to "image/x-photoshop",
    "py" to "text/x-python",
    "pyc" to "application/x-python-code",
    "pyo" to "application/x-python-code",
    "qgs" to "application/x-qgis",
    "qt" to "video/quicktime",
    "qtl" to "application/x-quicktimeplayer",
    "ra" to "audio/x-pn-realaudio",
    "raf" to "image/x-fuji-raf",
    "ram" to "audio/x-pn-realaudio",
    "rar" to "application/rar",
    "ras" to "image/x-cmu-raster",
    "rb" to "application/x-ruby",
    "rd" to "chemical/x-mdl-rdfile",
    "rdf" to "application/rdf+xml",
    "rdp" to "application/x-rdp",
    "rgb" to "image/x-rgb",
    "rm" to "audio/x-pn-realaudio",
    "roff" to "application/x-troff",
    "ros" to "chemical/x-rosdal",
    "rpm" to "application/x-redhat-package-manager",
    "rss" to "application/rss+xml",
    "rtf" to "text/rtf",
    "rtttl" to "audio/midi",
    "rtx" to "audio/midi",
    "rw2" to "image/x-panasonic-rw2",
    "rxn" to "chemical/x-mdl-rxnfile",
    "scala" to "text/x-scala",
    "sce" to "application/x-scilab",
    "sci" to "application/x-scilab",
    "sco" to "audio/csound",
    "scr" to "application/x-silverlight",
    "sct" to "text/scriptlet",
    "sd" to "chemical/x-mdl-sdfile",
    "sd2" to "audio/x-sd2",
    "sda" to "application/vnd.stardivision.draw",
    "sdc" to "application/vnd.stardivision.calc",
    "sdd" to "application/vnd.stardivision.impress",
    "sdf" to "chemical/x-mdl-sdfile",
    "sdp" to "application/vnd.stardivision.impress",
    "sds" to "application/vnd.stardivision.chart",
    "sdw" to "application/vnd.stardivision.writer",
    "ser" to "application/java-serialized-object",
    "sfd" to "application/vnd.font-fontforge-sfd",
    "sfv" to "text/x-sfv",
    "sgf" to "application/x-go-sgf",
    "sgl" to "application/vnd.stardivision.writer-global",
    "sh" to "text/x-sh",
    "shar" to "application/x-shar",
    "shp" to "application/x-qgis",
    "shtml" to "text/html",
    "shx" to "application/x-qgis",
    "sid" to "audio/prs.sid",
    "sig" to "application/pgp-signature",
    "sik" to "application/x-trash",
    "silo" to "model/mesh",
    "sis" to "application/vnd.symbian.install",
    "sisx" to "x-epoc/x-sisx-app",
    "sit" to "application/x-stuffit",
    "sitx" to "application/x-stuffit",
    "skd" to "application/x-koan",
    "skm" to "application/x-koan",
    "skp" to "application/x-koan",
    "skt" to "application/x-koan",
    "sldm" to "application/vnd.ms-powerpoint.slide.macroEnabled.12",
    "sldx" to "application/vnd.openxmlformats-officedocument.presentationml.slide",
    "smf" to "audio/sp-midi",
    "smi" to "application/smil+xml",
    "smil" to "application/smil+xml",
    "snd" to "audio/basic",
    "spc" to "chemical/x-galactic-spc",
    "spl" to "application/x-futuresplash",
    "spx" to "audio/ogg",
    "sql" to "application/x-sql",
    "src" to "application/x-wais-source",
    "srt" to "application/x-subrip",
    "srw" to "image/x-samsung-srw",
    "stc" to "application/vnd.sun.xml.calc.template",
    "std" to "application/vnd.sun.xml.draw.template",
    "sti" to "application/vnd.sun.xml.impress.template",
    "stl" to "application/vnd.ms-pki.stl",
    "stw" to "application/vnd.sun.xml.writer.template",
    "sty" to "text/x-tex",
    "sv4cpio" to "application/x-sv4cpio",
    "sv4crc" to "application/x-sv4crc",
    "svg" to "image/svg+xml",
    "svgz" to "image/svg+xml",
    "sw" to "chemical/x-swissprot",
    "swf" to "application/x-shockwave-flash",
    "swfl" to "application/x-shockwave-flash",
    "sxc" to "application/vnd.sun.xml.calc",
    "sxd" to "application/vnd.sun.xml.draw",
    "sxg" to "application/vnd.sun.xml.writer.global",
    "sxi" to "application/vnd.sun.xml.impress",
    "sxm" to "application/vnd.sun.xml.math",
    "sxw" to "application/vnd.sun.xml.writer",
    "t" to "application/x-troff",
    "tar" to "application/x-tar",
    "taz" to "application/x-gtar-compressed",
    "tcl" to "text/x-tcl",
    "tex" to "text/x-tex",
    "texi" to "application/x-texinfo",
    "texinfo" to "application/x-texinfo",
    "text" to "text/plain",
    "tgf" to "chemical/x-mdl-tgf",
    "tgz" to "application/x-gtar-compressed",
    "thmx" to "application/vnd.ms-officetheme",
    "tif" to "image/tiff",
    "tiff" to "image/tiff",
    "tk" to "text/x-tcl",
    "tm" to "text/texmacs",
    "torrent" to "application/x-bittorrent",
    "tr" to "application/x-troff",
    "ts" to "video/mp2ts",
    "tsp" to "application/dsptype",
    "tsv" to "text/tab-separated-values",
    "ttc" to "font/collection",
    "ttf" to "font/ttf",
    "ttl" to "text/turtle",
    "ttml" to "application/ttml+xml",
    "txt" to "text/plain",
    "udeb" to "application/x-debian-package",
    "uls" to "text/iuls",
    "ustar" to "application/x-ustar",
    "val" to "chemical/x-ncbi-asn1-binary",
    "vcard" to "text/vcard",
    "vcd" to "application/x-cdlink",
    "vcf" to "text/x-vcard",
    "vcs" to "text/x-vcalendar",
    "vmd" to "chemical/x-vmd",
    "vms" to "chemical/x-vamas-iso14976",
    "vor" to "application/vnd.stardivision.writer",
    "vrm" to "x-world/x-vrml",
    "vrml" to "x-world/x-vrml",
    "vsd" to "application/vnd.visio",
    "vss" to "application/vnd.visio",
    "vst" to "application/vnd.visio",
    "vsw" to "application/vnd.visio",
    "wad" to "application/x-doom",
    "wasm" to "application/wasm",
    "wav" to "audio/x-wav",
    "wax" to "audio/x-ms-wax",
    "wbmp" to "image/vnd.wap.wbmp",
    "wbxml" to "application/vnd.wap.wbxml",
    "webarchive" to "application/x-webarchive",
    "webarchivexml" to "application/x-webarchive-xml",
    "webm" to "video/webm",
    "webp" to "image/webp",
    "wk" to "application/x-123",
    "wm" to "video/x-ms-wm",
    "wma" to "audio/x-ms-wma",
    "wmd" to "application/x-ms-wmd",
    "wml" to "text/vnd.wap.wml",
    "wmlc" to "application/vnd.wap.wmlc",
    "wmls" to "text/vnd.wap.wmlscript",
    "wmlsc" to "application/vnd.wap.wmlscriptc",
    "wmv" to "video/x-ms-wmv",
    "wmx" to "video/x-ms-wmx",
    "wmz" to "application/x-ms-wmz",
    "woff" to "font/woff",
    "woff2" to "font/woff2",
    "wp5" to "application/vnd.wordperfect5.1",
    "wpd" to "application/vnd.wordperfect",
    "wpl" to "application/vnd.ms-wpl",
    "wrf" to "video/x-webex",
    "wrl" to "x-world/x-vrml",
    "wsc" to "text/scriptlet",
    "wvx" to "video/x-ms-wvx",
    "wz" to "application/x-wingz",
    "x3d" to "model/x3d+xml",
    "x3db" to "model/x3d+binary",
    "x3dv" to "model/x3d+vrml",
    "xbm" to "image/x-xbitmap",
    "xcf" to "application/x-xcf",
    "xcos" to "application/x-scilab-xcos",
    "xht" to "application/xhtml+xml",
    "xhtml" to "application/xhtml+xml",
    "xlam" to "application/vnd.ms-excel.addin.macroEnabled.12",
    "xlb" to "application/vnd.ms-excel",
    "xls" to "application/vnd.ms-excel",
    "xlsb" to "application/vnd.ms-excel.sheet.binary.macroEnabled.12",
    "xlsm" to "application/vnd.ms-excel.sheet.macroEnabled.12",
    "xlsx" to "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    "xlt" to "application/vnd.ms-excel",
    "xltm" to "application/vnd.ms-excel.template.macroEnabled.12",
    "xltx" to "application/vnd.openxmlformats-officedocument.spreadsheetml.template",
    "xmf" to "audio/midi",
    "xml" to "text/xml",
    "xpi" to "application/x-xpinstall",
    "xpm" to "image/x-xpixmap",
    "xsd" to "application/xml",
    "xsl" to "application/xslt+xml",
    "xslt" to "application/xslt+xml",
    "xspf" to "application/xspf+xml",
    "xtel" to "chemical/x-xtel",
    "xul" to "application/vnd.mozilla.xul+xml",
    "xwd" to "image/x-xwindowdump",
    "xyz" to "chemical/x-xyz",
    "xz" to "application/x-xz",
    "yt" to "video/vnd.youtube.yt",
    "zip" to "application/zip",
    "zmt" to "chemical/x-mopac-input",
    "~" to "application/x-trash"
)

fun MimeTypeMap.getMimeTypeFromExtensionCompat(extension: String): String? {
    return extensionToMimeTypeMap[extension] ?: getMimeTypeFromExtension(extension)
}
