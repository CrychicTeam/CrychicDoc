package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12;

import com.github.alexthe666.citadel.repack.jcodec.common.io.VLC;
import com.github.alexthe666.citadel.repack.jcodec.common.io.VLCBuilder;

public class MPEGConst {

    public static final int PICTURE_START_CODE = 0;

    public static final int SLICE_START_CODE_FIRST = 1;

    public static final int SLICE_START_CODE_LAST = 175;

    public static final int USER_DATA_START_CODE = 178;

    public static final int SEQUENCE_HEADER_CODE = 179;

    public static final int SEQUENCE_ERROR_CODE = 180;

    public static final int EXTENSION_START_CODE = 181;

    public static final int SEQUENCE_END_CODE = 183;

    public static final int GROUP_START_CODE = 184;

    public static final VLC vlcAddressIncrement = VLC.createVLC(new String[] { "1", "011", "010", "0011", "0010", "00011", "00010", "0000111", "0000110", "00001011", "00001010", "00001001", "00001000", "00000111", "00000110", "0000010111", "0000010110", "0000010101", "0000010100", "0000010011", "0000010010", "00000100011", "00000100010", "00000100001", "00000100000", "00000011111", "00000011110", "00000011101", "00000011100", "00000011011", "00000011010", "00000011001", "00000011000" });

    public static final VLC vlcMBTypeI = VLC.createVLC(new String[] { "1", "01" });

    public static final MPEGConst.MBType[] mbTypeValI = new MPEGConst.MBType[] { new MPEGConst.MBType(0, 0, 0, 0, 1, 0, 0), new MPEGConst.MBType(1, 0, 0, 0, 1, 0, 0) };

    public static final VLC vlcMBTypeP = VLC.createVLC(new String[] { "1", "01", "001", "00011", "00010", "00001", "000001" });

    public static final MPEGConst.MBType[] mbTypeValP = new MPEGConst.MBType[] { new MPEGConst.MBType(0, 1, 0, 1, 0, 0, 0), new MPEGConst.MBType(0, 0, 0, 1, 0, 0, 0), new MPEGConst.MBType(0, 1, 0, 0, 0, 0, 0), new MPEGConst.MBType(0, 0, 0, 0, 1, 0, 0), new MPEGConst.MBType(1, 1, 0, 1, 0, 0, 0), new MPEGConst.MBType(1, 0, 0, 1, 0, 0, 0), new MPEGConst.MBType(1, 0, 0, 0, 1, 0, 0) };

    public static final VLC vlcMBTypeB = VLC.createVLC(new String[] { "10", "11", "010", "011", "0010", "0011", "00011", "00010", "000011", "000010", "000001" });

    public static final MPEGConst.MBType[] mbTypeValB = new MPEGConst.MBType[] { new MPEGConst.MBType(0, 1, 1, 0, 0, 0, 0), new MPEGConst.MBType(0, 1, 1, 1, 0, 0, 0), new MPEGConst.MBType(0, 0, 1, 0, 0, 0, 0), new MPEGConst.MBType(0, 0, 1, 1, 0, 0, 0), new MPEGConst.MBType(0, 1, 0, 0, 0, 0, 0), new MPEGConst.MBType(0, 1, 0, 1, 0, 0, 0), new MPEGConst.MBType(0, 0, 0, 0, 1, 0, 0), new MPEGConst.MBType(1, 1, 1, 1, 0, 0, 0), new MPEGConst.MBType(1, 1, 0, 1, 0, 0, 0), new MPEGConst.MBType(1, 0, 1, 1, 0, 0, 0), new MPEGConst.MBType(1, 0, 0, 0, 1, 0, 0) };

    public static final VLC vlcMBTypeISpat = VLC.createVLC(new String[] { "1", "01", "0011", "0010", "0001" });

    public static final MPEGConst.MBType[] mbTypeValISpat = new MPEGConst.MBType[] { new MPEGConst.MBType(0, 0, 0, 0, 1, 0, 0), new MPEGConst.MBType(1, 0, 0, 0, 1, 0, 0), new MPEGConst.MBType(0, 0, 0, 0, 1, 0, 0), new MPEGConst.MBType(1, 0, 0, 0, 1, 0, 0), new MPEGConst.MBType(0, 0, 0, 0, 0, 0, 0) };

    public static final VLC vlcMBTypePSpat = VLC.createVLC(new String[] { "10", "011", "0000100", "000111", "0010", "0000111", "0011", "010", "000100", "0000110", "11", "000101", "000110", "0000101", "0000010", "0000011" });

    public static final MPEGConst.MBType[] mbTypeValPSpat = new MPEGConst.MBType[] { new MPEGConst.MBType(0, 1, 0, 1, 0, 0, 0), new MPEGConst.MBType(0, 1, 0, 1, 0, 1, 0), new MPEGConst.MBType(0, 0, 0, 1, 0, 0, 0), new MPEGConst.MBType(0, 0, 0, 1, 0, 1, 0), new MPEGConst.MBType(0, 1, 0, 0, 0, 0, 0), new MPEGConst.MBType(0, 0, 0, 0, 1, 0, 0), new MPEGConst.MBType(0, 1, 0, 0, 0, 1, 0), new MPEGConst.MBType(1, 1, 0, 1, 0, 0, 0), new MPEGConst.MBType(1, 0, 0, 1, 0, 0, 0), new MPEGConst.MBType(1, 0, 0, 0, 1, 0, 0), new MPEGConst.MBType(1, 1, 0, 1, 0, 1, 0), new MPEGConst.MBType(1, 0, 0, 1, 0, 1, 0), new MPEGConst.MBType(0, 0, 0, 0, 0, 1, 0), new MPEGConst.MBType(0, 0, 0, 1, 0, 0, 0), new MPEGConst.MBType(1, 0, 0, 1, 0, 0, 0), new MPEGConst.MBType(0, 0, 0, 0, 0, 0, 0) };

    public static final VLC vlcMBTypeBSpat = VLC.createVLC(new String[] { "10", "11", "010", "011", "0010", "0011", "000110", "000111", "000100", "000101", "0000110", "0000111", "0000100", "0000101", "00000100", "00000101", "000001100", "000001110", "000001101", "000001111" });

    public static final MPEGConst.MBType[] mbTypeValBSpat = new MPEGConst.MBType[] { new MPEGConst.MBType(0, 1, 1, 0, 0, 0, 0), new MPEGConst.MBType(0, 1, 1, 1, 0, 0, 0), new MPEGConst.MBType(0, 0, 1, 0, 0, 0, 0), new MPEGConst.MBType(0, 0, 1, 1, 0, 0, 0), new MPEGConst.MBType(0, 1, 0, 0, 0, 0, 0), new MPEGConst.MBType(0, 1, 0, 1, 0, 0, 0), new MPEGConst.MBType(0, 0, 1, 0, 0, 1, 0), new MPEGConst.MBType(0, 0, 1, 1, 0, 1, 0), new MPEGConst.MBType(0, 1, 0, 0, 0, 1, 0), new MPEGConst.MBType(0, 1, 0, 1, 0, 1, 0), new MPEGConst.MBType(0, 0, 0, 0, 1, 0, 0), new MPEGConst.MBType(1, 1, 1, 1, 0, 0, 0), new MPEGConst.MBType(1, 1, 0, 1, 0, 0, 0), new MPEGConst.MBType(1, 0, 1, 1, 0, 0, 0), new MPEGConst.MBType(1, 0, 0, 0, 1, 0, 0), new MPEGConst.MBType(1, 1, 0, 1, 0, 1, 0), new MPEGConst.MBType(1, 0, 1, 1, 0, 1, 0), new MPEGConst.MBType(0, 0, 0, 0, 0, 0, 0), new MPEGConst.MBType(1, 0, 0, 1, 0, 0, 0), new MPEGConst.MBType(0, 0, 0, 1, 0, 0, 0) };

    public static final VLC vlcMBTypeSNR = VLC.createVLC(new String[] { "1", "01", "001" });

    public static final MPEGConst.MBType[] mbTypeValSNR = new MPEGConst.MBType[] { new MPEGConst.MBType(0, 0, 0, 1, 0, 0, 0), new MPEGConst.MBType(1, 0, 0, 1, 0, 0, 0), new MPEGConst.MBType(0, 0, 0, 0, 0, 0, 0) };

    public static final VLC vlcCBP = VLC.createVLC(new String[] { "000000001", "01011", "01001", "001101", "1101", "0010111", "0010011", "00011111", "1100", "0010110", "0010010", "00011110", "10011", "00011011", "00010111", "00010011", "1011", "0010101", "0010001", "00011101", "10001", "00011001", "00010101", "00010001", "001111", "00001111", "00001101", "000000011", "01111", "00001011", "00000111", "000000111", "1010", "0010100", "0010000", "00011100", "001110", "00001110", "00001100", "000000010", "10000", "00011000", "00010100", "00010000", "01110", "00001010", "00000110", "000000110", "10010", "00011010", "00010110", "00010010", "01101", "00001001", "00000101", "000000101", "01100", "00001000", "00000100", "000000100", "111", "01010", "01000", "001100" });

    public static final VLC vlcMotionCode = VLC.createVLC(new String[] { "1", "01", "001", "0001", "000011", "0000101", "0000100", "0000011", "000001011", "000001010", "000001001", "0000010001", "0000010000", "0000001111", "0000001110", "0000001101", "0000001100" });

    public static final VLC vlcDualPrime = VLC.createVLC(new String[] { "11", "0", "10" });

    public static final VLC vlcDCSizeLuma = VLC.createVLC(new String[] { "100", "00", "01", "101", "110", "1110", "11110", "111110", "1111110", "11111110", "111111110", "111111111" });

    public static final VLC vlcDCSizeChroma = VLC.createVLC(new String[] { "00", "01", "10", "110", "1110", "11110", "111110", "1111110", "11111110", "111111110", "1111111110", "1111111111" });

    public static final VLC vlcCoeff0;

    public static final VLC vlcCoeff1;

    public static final int CODE_ESCAPE = 2049;

    public static final int CODE_END = 2048;

    public static final int[] qScaleTab1 = new int[] { 0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52, 54, 56, 58, 60, 62 };

    public static final int[] qScaleTab2 = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 10, 12, 14, 16, 18, 20, 22, 24, 28, 32, 36, 40, 44, 48, 52, 56, 64, 72, 80, 88, 96, 104, 112 };

    public static final int[] defaultQMatIntra = new int[] { 8, 16, 19, 22, 26, 27, 29, 34, 16, 16, 22, 24, 27, 29, 34, 37, 19, 22, 26, 27, 29, 34, 34, 38, 22, 22, 26, 27, 29, 34, 37, 40, 22, 26, 27, 29, 32, 35, 40, 48, 26, 27, 29, 32, 35, 40, 48, 58, 26, 27, 29, 34, 38, 46, 56, 69, 27, 29, 35, 38, 46, 56, 69, 83 };

    public static final int[] defaultQMatInter = new int[] { 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16 };

    public static final int[][] scan = new int[][] { { 0, 1, 8, 16, 9, 2, 3, 10, 17, 24, 32, 25, 18, 11, 4, 5, 12, 19, 26, 33, 40, 48, 41, 34, 27, 20, 13, 6, 7, 14, 21, 28, 35, 42, 49, 56, 57, 50, 43, 36, 29, 22, 15, 23, 30, 37, 44, 51, 58, 59, 52, 45, 38, 31, 39, 46, 53, 60, 61, 54, 47, 55, 62, 63 }, { 0, 8, 16, 24, 1, 9, 2, 10, 17, 25, 32, 40, 48, 56, 57, 49, 41, 33, 26, 18, 3, 11, 4, 12, 19, 27, 34, 42, 50, 58, 35, 43, 51, 59, 20, 28, 5, 13, 6, 14, 21, 29, 36, 44, 52, 60, 37, 45, 53, 61, 22, 30, 7, 15, 23, 31, 38, 46, 54, 62, 39, 47, 55, 63 } };

    public static final int[] BLOCK_TO_CC = new int[] { 0, 0, 0, 0, 1, 2, 1, 2, 1, 2, 1, 2 };

    public static final int[] BLOCK_POS_X = new int[] { 0, 8, 0, 8, 0, 0, 0, 0, 8, 8, 8, 8, 0, 0, 0, 0, 0, 8, 0, 8, 0, 0, 0, 0, 8, 8, 8, 8 };

    public static final int[] BLOCK_POS_Y = new int[] { 0, 0, 8, 8, 0, 0, 8, 8, 0, 0, 8, 8, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1 };

    public static final int[] STEP_Y = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

    public static final int[] SQUEEZE_X = new int[] { 0, 1, 1, 0 };

    public static final int[] SQUEEZE_Y = new int[] { 0, 1, 0, 0 };

    public static final int IntraCoded = 1;

    public static final int PredictiveCoded = 2;

    public static final int BiPredictiveCoded = 3;

    static {
        VLCBuilder vlcCoeffBldr = new VLCBuilder();
        vlcCoeffBldr.set(2049, "000001");
        vlcCoeffBldr.set(2048, "10");
        vlcCoeffBldr.set(1, "11");
        vlcCoeffBldr.set(65, "011");
        vlcCoeffBldr.set(2, "0100");
        vlcCoeffBldr.set(129, "0101");
        vlcCoeffBldr.set(3, "00101");
        vlcCoeffBldr.set(193, "00111");
        vlcCoeffBldr.set(257, "00110");
        vlcCoeffBldr.set(66, "000110");
        vlcCoeffBldr.set(321, "000111");
        vlcCoeffBldr.set(385, "000101");
        vlcCoeffBldr.set(449, "000100");
        vlcCoeffBldr.set(4, "0000110");
        vlcCoeffBldr.set(130, "0000100");
        vlcCoeffBldr.set(513, "0000111");
        vlcCoeffBldr.set(577, "0000101");
        vlcCoeffBldr.set(5, "00100110");
        vlcCoeffBldr.set(6, "00100001");
        vlcCoeffBldr.set(67, "00100101");
        vlcCoeffBldr.set(194, "00100100");
        vlcCoeffBldr.set(641, "00100111");
        vlcCoeffBldr.set(705, "00100011");
        vlcCoeffBldr.set(769, "00100010");
        vlcCoeffBldr.set(833, "00100000");
        vlcCoeffBldr.set(7, "0000001010");
        vlcCoeffBldr.set(68, "0000001100");
        vlcCoeffBldr.set(131, "0000001011");
        vlcCoeffBldr.set(258, "0000001111");
        vlcCoeffBldr.set(322, "0000001001");
        vlcCoeffBldr.set(897, "0000001110");
        vlcCoeffBldr.set(961, "0000001101");
        vlcCoeffBldr.set(1025, "0000001000");
        vlcCoeffBldr.set(8, "000000011101");
        vlcCoeffBldr.set(9, "000000011000");
        vlcCoeffBldr.set(10, "000000010011");
        vlcCoeffBldr.set(11, "000000010000");
        vlcCoeffBldr.set(69, "000000011011");
        vlcCoeffBldr.set(132, "000000010100");
        vlcCoeffBldr.set(195, "000000011100");
        vlcCoeffBldr.set(259, "000000010010");
        vlcCoeffBldr.set(386, "000000011110");
        vlcCoeffBldr.set(450, "000000010101");
        vlcCoeffBldr.set(514, "000000010001");
        vlcCoeffBldr.set(1089, "000000011111");
        vlcCoeffBldr.set(1153, "000000011010");
        vlcCoeffBldr.set(1217, "000000011001");
        vlcCoeffBldr.set(1281, "000000010111");
        vlcCoeffBldr.set(1345, "000000010110");
        vlcCoeffBldr.set(12, "0000000011010");
        vlcCoeffBldr.set(13, "0000000011001");
        vlcCoeffBldr.set(14, "0000000011000");
        vlcCoeffBldr.set(15, "0000000010111");
        vlcCoeffBldr.set(70, "0000000010110");
        vlcCoeffBldr.set(71, "0000000010101");
        vlcCoeffBldr.set(133, "0000000010100");
        vlcCoeffBldr.set(196, "0000000010011");
        vlcCoeffBldr.set(323, "0000000010010");
        vlcCoeffBldr.set(578, "0000000010001");
        vlcCoeffBldr.set(642, "0000000010000");
        vlcCoeffBldr.set(1409, "0000000011111");
        vlcCoeffBldr.set(1473, "0000000011110");
        vlcCoeffBldr.set(1537, "0000000011101");
        vlcCoeffBldr.set(1601, "0000000011100");
        vlcCoeffBldr.set(1665, "0000000011011");
        vlcCoeffBldr.set(16, "00000000011111");
        vlcCoeffBldr.set(17, "00000000011110");
        vlcCoeffBldr.set(18, "00000000011101");
        vlcCoeffBldr.set(19, "00000000011100");
        vlcCoeffBldr.set(20, "00000000011011");
        vlcCoeffBldr.set(21, "00000000011010");
        vlcCoeffBldr.set(22, "00000000011001");
        vlcCoeffBldr.set(23, "00000000011000");
        vlcCoeffBldr.set(24, "00000000010111");
        vlcCoeffBldr.set(25, "00000000010110");
        vlcCoeffBldr.set(26, "00000000010101");
        vlcCoeffBldr.set(27, "00000000010100");
        vlcCoeffBldr.set(28, "00000000010011");
        vlcCoeffBldr.set(29, "00000000010010");
        vlcCoeffBldr.set(30, "00000000010001");
        vlcCoeffBldr.set(31, "00000000010000");
        vlcCoeffBldr.set(32, "000000000011000");
        vlcCoeffBldr.set(33, "000000000010111");
        vlcCoeffBldr.set(34, "000000000010110");
        vlcCoeffBldr.set(35, "000000000010101");
        vlcCoeffBldr.set(36, "000000000010100");
        vlcCoeffBldr.set(37, "000000000010011");
        vlcCoeffBldr.set(38, "000000000010010");
        vlcCoeffBldr.set(39, "000000000010001");
        vlcCoeffBldr.set(40, "000000000010000");
        vlcCoeffBldr.set(72, "000000000011111");
        vlcCoeffBldr.set(73, "000000000011110");
        vlcCoeffBldr.set(74, "000000000011101");
        vlcCoeffBldr.set(75, "000000000011100");
        vlcCoeffBldr.set(76, "000000000011011");
        vlcCoeffBldr.set(77, "000000000011010");
        vlcCoeffBldr.set(78, "000000000011001");
        vlcCoeffBldr.set(79, "0000000000010011");
        vlcCoeffBldr.set(80, "0000000000010010");
        vlcCoeffBldr.set(81, "0000000000010001");
        vlcCoeffBldr.set(82, "0000000000010000");
        vlcCoeffBldr.set(387, "0000000000010100");
        vlcCoeffBldr.set(706, "0000000000011010");
        vlcCoeffBldr.set(770, "0000000000011001");
        vlcCoeffBldr.set(834, "0000000000011000");
        vlcCoeffBldr.set(898, "0000000000010111");
        vlcCoeffBldr.set(962, "0000000000010110");
        vlcCoeffBldr.set(1026, "0000000000010101");
        vlcCoeffBldr.set(1729, "0000000000011111");
        vlcCoeffBldr.set(1793, "0000000000011110");
        vlcCoeffBldr.set(1857, "0000000000011101");
        vlcCoeffBldr.set(1921, "0000000000011100");
        vlcCoeffBldr.set(1985, "0000000000011011");
        vlcCoeff0 = vlcCoeffBldr.getVLC();
        vlcCoeffBldr = new VLCBuilder();
        vlcCoeffBldr.set(2049, "000001");
        vlcCoeffBldr.set(2048, "0110");
        vlcCoeffBldr.set(1, "10");
        vlcCoeffBldr.set(65, "010");
        vlcCoeffBldr.set(2, "110");
        vlcCoeffBldr.set(129, "00101");
        vlcCoeffBldr.set(3, "0111");
        vlcCoeffBldr.set(193, "00111");
        vlcCoeffBldr.set(257, "000110");
        vlcCoeffBldr.set(66, "00110");
        vlcCoeffBldr.set(321, "000111");
        vlcCoeffBldr.set(385, "0000110");
        vlcCoeffBldr.set(449, "0000100");
        vlcCoeffBldr.set(4, "11100");
        vlcCoeffBldr.set(130, "0000111");
        vlcCoeffBldr.set(513, "0000101");
        vlcCoeffBldr.set(577, "1111000");
        vlcCoeffBldr.set(5, "11101");
        vlcCoeffBldr.set(6, "000101");
        vlcCoeffBldr.set(67, "1111001");
        vlcCoeffBldr.set(194, "00100110");
        vlcCoeffBldr.set(641, "1111010");
        vlcCoeffBldr.set(705, "00100001");
        vlcCoeffBldr.set(769, "00100101");
        vlcCoeffBldr.set(833, "00100100");
        vlcCoeffBldr.set(7, "000100");
        vlcCoeffBldr.set(68, "00100111");
        vlcCoeffBldr.set(131, "11111100");
        vlcCoeffBldr.set(258, "11111101");
        vlcCoeffBldr.set(322, "000000100");
        vlcCoeffBldr.set(897, "000000101");
        vlcCoeffBldr.set(961, "000000111");
        vlcCoeffBldr.set(1025, "0000001101");
        vlcCoeffBldr.set(8, "1111011");
        vlcCoeffBldr.set(9, "1111100");
        vlcCoeffBldr.set(10, "00100011");
        vlcCoeffBldr.set(11, "00100010");
        vlcCoeffBldr.set(69, "00100000");
        vlcCoeffBldr.set(132, "0000001100");
        vlcCoeffBldr.set(195, "000000011100");
        vlcCoeffBldr.set(259, "000000010010");
        vlcCoeffBldr.set(386, "000000011110");
        vlcCoeffBldr.set(450, "000000010101");
        vlcCoeffBldr.set(514, "000000010001");
        vlcCoeffBldr.set(1089, "000000011111");
        vlcCoeffBldr.set(1153, "000000011010");
        vlcCoeffBldr.set(1217, "000000011001");
        vlcCoeffBldr.set(1281, "000000010111");
        vlcCoeffBldr.set(1345, "000000010110");
        vlcCoeffBldr.set(12, "11111010");
        vlcCoeffBldr.set(13, "11111011");
        vlcCoeffBldr.set(14, "11111110");
        vlcCoeffBldr.set(15, "11111111");
        vlcCoeffBldr.set(70, "0000000010110");
        vlcCoeffBldr.set(71, "0000000010101");
        vlcCoeffBldr.set(133, "0000000010100");
        vlcCoeffBldr.set(196, "0000000010011");
        vlcCoeffBldr.set(323, "0000000010010");
        vlcCoeffBldr.set(578, "0000000010001");
        vlcCoeffBldr.set(642, "0000000010000");
        vlcCoeffBldr.set(1409, "0000000011111");
        vlcCoeffBldr.set(1473, "0000000011110");
        vlcCoeffBldr.set(1537, "0000000011101");
        vlcCoeffBldr.set(1601, "0000000011100");
        vlcCoeffBldr.set(1665, "0000000011011");
        vlcCoeffBldr.set(16, "00000000011111");
        vlcCoeffBldr.set(17, "00000000011110");
        vlcCoeffBldr.set(18, "00000000011101");
        vlcCoeffBldr.set(19, "00000000011100");
        vlcCoeffBldr.set(20, "00000000011011");
        vlcCoeffBldr.set(21, "00000000011010");
        vlcCoeffBldr.set(22, "00000000011001");
        vlcCoeffBldr.set(23, "00000000011000");
        vlcCoeffBldr.set(24, "00000000010111");
        vlcCoeffBldr.set(25, "00000000010110");
        vlcCoeffBldr.set(26, "00000000010101");
        vlcCoeffBldr.set(27, "00000000010100");
        vlcCoeffBldr.set(28, "00000000010011");
        vlcCoeffBldr.set(29, "00000000010010");
        vlcCoeffBldr.set(30, "00000000010001");
        vlcCoeffBldr.set(31, "00000000010000");
        vlcCoeffBldr.set(32, "000000000011000");
        vlcCoeffBldr.set(33, "000000000010111");
        vlcCoeffBldr.set(34, "000000000010110");
        vlcCoeffBldr.set(35, "000000000010101");
        vlcCoeffBldr.set(36, "000000000010100");
        vlcCoeffBldr.set(37, "000000000010011");
        vlcCoeffBldr.set(38, "000000000010010");
        vlcCoeffBldr.set(39, "000000000010001");
        vlcCoeffBldr.set(40, "000000000010000");
        vlcCoeffBldr.set(72, "000000000011111");
        vlcCoeffBldr.set(73, "000000000011110");
        vlcCoeffBldr.set(74, "000000000011101");
        vlcCoeffBldr.set(75, "000000000011100");
        vlcCoeffBldr.set(76, "000000000011011");
        vlcCoeffBldr.set(77, "000000000011010");
        vlcCoeffBldr.set(78, "000000000011001");
        vlcCoeffBldr.set(79, "0000000000010011");
        vlcCoeffBldr.set(80, "0000000000010010");
        vlcCoeffBldr.set(81, "0000000000010001");
        vlcCoeffBldr.set(82, "0000000000010000");
        vlcCoeffBldr.set(387, "0000000000010100");
        vlcCoeffBldr.set(706, "0000000000011010");
        vlcCoeffBldr.set(770, "0000000000011001");
        vlcCoeffBldr.set(834, "0000000000011000");
        vlcCoeffBldr.set(898, "0000000000010111");
        vlcCoeffBldr.set(962, "0000000000010110");
        vlcCoeffBldr.set(1026, "0000000000010101");
        vlcCoeffBldr.set(1729, "0000000000011111");
        vlcCoeffBldr.set(1793, "0000000000011110");
        vlcCoeffBldr.set(1857, "0000000000011101");
        vlcCoeffBldr.set(1921, "0000000000011100");
        vlcCoeffBldr.set(1985, "0000000000011011");
        vlcCoeff1 = vlcCoeffBldr.getVLC();
    }

    public static class MBType {

        public int macroblock_quant;

        public int macroblock_motion_forward;

        public int macroblock_motion_backward;

        public int macroblock_pattern;

        public int macroblock_intra;

        public int spatial_temporal_weight_code_flag;

        public int permitted_spatial_temporal_weight_classes;

        MBType(int macroblock_quant, int macroblock_motion_forward, int macroblock_motion_backward, int macroblock_pattern, int macroblock_intra, int spatial_temporal_weight_code_flag, int permitted_spatial_temporal_weight_classes) {
            this.macroblock_quant = macroblock_quant;
            this.macroblock_motion_forward = macroblock_motion_forward;
            this.macroblock_motion_backward = macroblock_motion_backward;
            this.macroblock_pattern = macroblock_pattern;
            this.macroblock_intra = macroblock_intra;
            this.spatial_temporal_weight_code_flag = spatial_temporal_weight_code_flag;
            this.permitted_spatial_temporal_weight_classes = permitted_spatial_temporal_weight_classes;
        }
    }
}