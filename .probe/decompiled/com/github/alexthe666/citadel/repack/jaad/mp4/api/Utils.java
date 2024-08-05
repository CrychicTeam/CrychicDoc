package com.github.alexthe666.citadel.repack.jaad.mp4.api;

import java.util.Date;

class Utils {

    private static final long DATE_OFFSET = 2082850791998L;

    static Date getDate(long time) {
        return new Date(time * 1000L - 2082850791998L);
    }
}