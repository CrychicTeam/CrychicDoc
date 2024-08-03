package com.github.alexthe666.citadel.repack.jaad.aac.sbr;

interface SBRConstants {

    int[] startMinTable = new int[] { 7, 7, 10, 11, 12, 16, 16, 17, 24, 32, 35, 48 };

    int[] offsetIndexTable = new int[] { 5, 5, 4, 4, 4, 3, 2, 1, 0, 6, 6, 6 };

    int[][] OFFSET = new int[][] { { -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7 }, { -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 9, 11, 13 }, { -5, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 9, 11, 13, 16 }, { -6, -4, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 9, 11, 13, 16 }, { -4, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 9, 11, 13, 16, 20 }, { -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 9, 11, 13, 16, 20, 24 }, { 0, 1, 2, 3, 4, 5, 6, 7, 9, 11, 13, 16, 20, 24, 28, 33 } };

    int EXTENSION_ID_PS = 2;

    int MAX_NTSRHFG = 40;

    int MAX_NTSR = 32;

    int MAX_M = 49;

    int MAX_L_E = 5;

    int EXT_SBR_DATA = 13;

    int EXT_SBR_DATA_CRC = 14;

    int FIXFIX = 0;

    int FIXVAR = 1;

    int VARFIX = 2;

    int VARVAR = 3;

    int LO_RES = 0;

    int HI_RES = 1;

    int NO_TIME_SLOTS_960 = 15;

    int NO_TIME_SLOTS = 16;

    int RATE = 2;

    int NOISE_FLOOR_OFFSET = 6;

    int T_HFGEN = 8;

    int T_HFADJ = 2;
}