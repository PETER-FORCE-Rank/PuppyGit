SET(CMAKE_SYSTEM_NAME Android)
SET(CMAKE_SYSTEM_VERSION $ENV{android_target_abi})
SET(CMAKE_ANDROID_ARCH_ABI x86)

SET(CMAKE_C_COMPILER   $ENV{ANDROID_TOOLCHAIN_ROOT}/bin/i686-linux-android$ENV{android_target_abi}-clang)
SET(CMAKE_CXX_COMPILER $ENV{ANDROID_TOOLCHAIN_ROOT}/bin/i686-linux-android$ENV{android_target_abi}-clang++)
SET(CMAKE_FIND_ROOT_PATH $ENV{ANDROID_TOOLCHAIN_ROOT}/sysroot/)

SET(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)
SET(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY)
SET(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY)


