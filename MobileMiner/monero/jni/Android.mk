LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := lib-cpuid
LOCAL_C_INCLUDES  += $(LOCAL_PATH)/3rdparty/libcpuid

LOCAL_SRC_FILES    := \
                    ./3rdparty/libcpuid/cpuid_main.c \
                    ./3rdparty/libcpuid/asm-bits.c \
                    ./3rdparty/libcpuid/recog_amd.c \
                    ./3rdparty/libcpuid/recog_intel.c \
                    ./3rdparty/libcpuid/libcpuid_util.c
LOCAL_CFLAGS  += -std=gnu99

include $(BUILD_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := lib-uv
LOCAL_EXPORT_C_INCLUDES    := $(LOCAL_PATH)/3rdparty/uv
LOCAL_LDLIBS    :=  $(LOCAL_PATH)/3rdparty/uv/libuv.so
include $(BUILD_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := lib-api
LOCAL_CPPFLAGS    := -std=c++11 -fexceptions -frtti -lpthread
LOCAL_C_INCLUDES    := $(LOCAL_PATH)/3rdparty/rapidjson \
                       $(LOCAL_PATH)/3rdparty/rapidjson/error \
                       $(LOCAL_PATH)/3rdparty/rapidjson/internal \
                       $(LOCAL_PATH)/3rdparty/rapidjson/msinttypes
LOCAL_SRC_FILES := \
    ./api/Api.cpp \
    ./api/ApiState.cpp \
    ./api/Httpd.cpp \
    ./api/NetworkState.cpp

LOCAL_STATIC_LIBRARIES := lib-uv

include $(BUILD_STATIC_LIBRARY)