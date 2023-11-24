package com.pri.petcationbackend.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

@UtilityClass
public final class ImageUtils {

    public static final String RESOURCES_PATH = "/src/main/resources/images/";

    public static byte[] getImageFromPath(String path) {
        InputStream in = ImageUtils.class
                .getResourceAsStream(RESOURCES_PATH + path);
        if (in == null) return null;
        try {
            return IOUtils.toByteArray(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
