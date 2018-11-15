LOCAL_PATH := device/atc/smartauto/products/c8/themes

theme_target_file_path := system/etc/themes

# copy themes.json
PRODUCT_COPY_FILES += \
        $(LOCAL_PATH)/themes.json:$(theme_target_file_path)/themes.json \

theme_path := 100

# copy theme1 skin
theme_files := $(shell ls $(LOCAL_PATH)/$(theme_path) )
PRODUCT_COPY_FILES += \
        $(LOCAL_PATH)/$(theme_path)/$(theme_path).skin:$(theme_target_file_path)/$(theme_path)/$(theme_path).skin \

# preview path
preview_path := preview

#copy theme1 preview drawable
theme_preview_files := $(shell ls $(LOCAL_PATH)/$(theme_path)/$(preview_path) )
PRODUCT_COPY_FILES += $(foreach file, $(theme_preview_files), \
        $(LOCAL_PATH)/$(theme_path)/$(preview_path)/$(file):$(theme_target_file_path)/$(theme_path)/$(preview_path)/$(file))

theme_path := 101

# copy theme1 skin
theme_files := $(shell ls $(LOCAL_PATH)/$(theme_path) )
PRODUCT_COPY_FILES += \
        $(LOCAL_PATH)/$(theme_path)/$(theme_path).skin:$(theme_target_file_path)/$(theme_path)/$(theme_path).skin \

# preview path
preview_path := preview

#copy theme1 preview drawable
theme_preview_files := $(shell ls $(LOCAL_PATH)/$(theme_path)/$(preview_path) )
PRODUCT_COPY_FILES += $(foreach file, $(theme_preview_files), \
        $(LOCAL_PATH)/$(theme_path)/$(preview_path)/$(file):$(theme_target_file_path)/$(theme_path)/$(preview_path)/$(file))


theme_path := 102

# copy theme1 skin
theme_files := $(shell ls $(LOCAL_PATH)/$(theme_path) )
PRODUCT_COPY_FILES += \
        $(LOCAL_PATH)/$(theme_path)/$(theme_path).skin:$(theme_target_file_path)/$(theme_path)/$(theme_path).skin \

# preview path
preview_path := preview

#copy theme1 preview drawable
theme_preview_files := $(shell ls $(LOCAL_PATH)/$(theme_path)/$(preview_path) )
PRODUCT_COPY_FILES += $(foreach file, $(theme_preview_files), \
        $(LOCAL_PATH)/$(theme_path)/$(preview_path)/$(file):$(theme_target_file_path)/$(theme_path)/$(preview_path)/$(file))




