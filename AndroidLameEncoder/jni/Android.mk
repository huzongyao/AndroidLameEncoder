JNI_PATH := $(call my-dir)
LOCAL_PATH := $(JNI_PATH)/liblame-3.99.5

include $(CLEAR_VARS)

LOCAL_MODULE    := lame
LOCAL_SRC_FILES := \
	bitstream.c  \
	id3tag.c \
	presets.c \
	reservoir.c \
	util.c \
	encoder.c \
	lame.c \
	psymodel.c \
	set_get.c \
	vbrquantize.c \
	fft.c \
	quantize.c \
	tables.c \
	VbrTag.c \
	gain_analysis.c \
	newmdct.c \
	quantize_pvt.c \
	takehiro.c \
	version.c
	
LOCAL_SRC_FILES += \
	../lame_encoder.c
	

include $(BUILD_SHARED_LIBRARY)
