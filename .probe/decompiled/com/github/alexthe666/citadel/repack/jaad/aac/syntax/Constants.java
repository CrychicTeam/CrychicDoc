package com.github.alexthe666.citadel.repack.jaad.aac.syntax;

import java.util.logging.Logger;

public interface Constants {

    Logger LOGGER = Logger.getLogger("jaad");

    int MAX_ELEMENTS = 16;

    int BYTE_MASK = 255;

    int MIN_INPUT_SIZE = 768;

    int WINDOW_LEN_LONG = 1024;

    int WINDOW_LEN_SHORT = 128;

    int WINDOW_SMALL_LEN_LONG = 960;

    int WINDOW_SMALL_LEN_SHORT = 120;

    int ELEMENT_SCE = 0;

    int ELEMENT_CPE = 1;

    int ELEMENT_CCE = 2;

    int ELEMENT_LFE = 3;

    int ELEMENT_DSE = 4;

    int ELEMENT_PCE = 5;

    int ELEMENT_FIL = 6;

    int ELEMENT_END = 7;

    int MAX_WINDOW_COUNT = 8;

    int MAX_WINDOW_GROUP_COUNT = 8;

    int MAX_LTP_SFB = 40;

    int MAX_SECTIONS = 120;

    int MAX_MS_MASK = 128;

    float SQRT2 = 1.4142135F;
}