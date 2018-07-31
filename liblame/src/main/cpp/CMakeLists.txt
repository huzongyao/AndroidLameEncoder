# Sets the minimum version of CMake required to build the native
# library. You should either keep the default value or only pass a
# value of 3.4.0 or lower.
# by huzongyao

cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall -DHAVE_CONFIG_H")
# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds it for you.
# Gradle automatically packages shared libraries with your APK.

include_directories(liblame)

set(NATIVE_SRCS "lame_encoder.c"
        "liblame/bitstream.c"  
        "liblame/id3tag.c" 
        "liblame/presets.c" 
        "liblame/reservoir.c" 
        "liblame/util.c" 
        "liblame/encoder.c" 
        "liblame/lame.c" 
        "liblame/psymodel.c" 
        "liblame/set_get.c" 
        "liblame/vbrquantize.c" 
        "liblame/fft.c" 
        "liblame/quantize.c" 
        "liblame/tables.c" 
        "liblame/VbrTag.c" 
        "liblame/gain_analysis.c" 
        "liblame/newmdct.c" 
        "liblame/quantize_pvt.c" 
        "liblame/takehiro.c" 
        "liblame/version.c"
        "liblame/vector/xmm_quantize_sub.c")

add_library(lame SHARED ${NATIVE_SRCS})

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in the
# build script, prebuilt third-party libraries, or system libraries.
target_link_libraries(lame log)
