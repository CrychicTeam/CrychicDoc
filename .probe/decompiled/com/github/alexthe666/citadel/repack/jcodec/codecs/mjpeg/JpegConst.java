package com.github.alexthe666.citadel.repack.jcodec.codecs.mjpeg;

import com.github.alexthe666.citadel.repack.jcodec.common.io.VLC;
import com.github.alexthe666.citadel.repack.jcodec.common.io.VLCBuilder;

public class JpegConst {

    public static final int[] naturalOrder = new int[] { 0, 1, 8, 16, 9, 2, 3, 10, 17, 24, 32, 25, 18, 11, 4, 5, 12, 19, 26, 33, 40, 48, 41, 34, 27, 20, 13, 6, 7, 14, 21, 28, 35, 42, 49, 56, 57, 50, 43, 36, 29, 22, 15, 23, 30, 37, 44, 51, 58, 59, 52, 45, 38, 31, 39, 46, 53, 60, 61, 54, 47, 55, 62, 63 };

    public static final VLC YDC_DEFAULT;

    public static final VLC YAC_DEFAULT;

    public static final VLC CDC_DEFAULT;

    public static final VLC CAC_DEFAULT;

    private static final String[] names = new String[256];

    public static final int SOF0 = 192;

    public static final int SOF1 = 193;

    public static final int SOF2 = 194;

    public static final int SOF3 = 195;

    public static final int DHT = 196;

    public static final int DQT = 219;

    public static final int SOS = 218;

    public static final int EOI = 217;

    public static final int SOI = 216;

    public static final int APP0 = 224;

    public static final int APP1 = 225;

    public static final int APP2 = 226;

    public static final int APP3 = 227;

    public static final int APP4 = 228;

    public static final int APP5 = 229;

    public static final int APP6 = 230;

    public static final int APP7 = 231;

    public static final int APP8 = 232;

    public static final int APP9 = 233;

    public static final int APPA = 234;

    public static final int APPB = 235;

    public static final int APPC = 236;

    public static final int APPD = 237;

    public static final int APPE = 238;

    public static final int APPF = 239;

    public static final int RST0 = 208;

    public static final int RST1 = 209;

    public static final int RST2 = 210;

    public static final int RST3 = 211;

    public static final int RST4 = 212;

    public static final int RST5 = 213;

    public static final int RST6 = 214;

    public static final int RST7 = 215;

    public static final int COM = 254;

    public static final int DRI = 221;

    public static int[] DEFAULT_QUANT_LUMA;

    public static int[] DEFAULT_QUANT_CHROMA;

    public static String markerToString(int marker) {
        return names[marker];
    }

    static {
        VLCBuilder bldr1 = new VLCBuilder();
        bldr1.set(0, "00");
        bldr1.set(1, "010");
        bldr1.set(2, "011");
        bldr1.set(3, "100");
        bldr1.set(4, "101");
        bldr1.set(5, "110");
        bldr1.set(6, "1110");
        bldr1.set(7, "11110");
        bldr1.set(8, "111110");
        bldr1.set(9, "1111110");
        bldr1.set(10, "11111110");
        bldr1.set(11, "111111110");
        YDC_DEFAULT = bldr1.getVLC();
        VLCBuilder bldr2 = new VLCBuilder();
        bldr2.set(0, "00");
        bldr2.set(1, "01");
        bldr2.set(2, "10");
        bldr2.set(3, "110");
        bldr2.set(4, "1110");
        bldr2.set(5, "11110");
        bldr2.set(6, "111110");
        bldr2.set(7, "1111110");
        bldr2.set(8, "11111110");
        bldr2.set(9, "111111110");
        bldr2.set(10, "1111111110");
        bldr2.set(11, "11111111110");
        CDC_DEFAULT = bldr2.getVLC();
        VLCBuilder bldr3 = new VLCBuilder();
        bldr3.set(0, "1010");
        bldr3.set(1, "00");
        bldr3.set(2, "01");
        bldr3.set(3, "100");
        bldr3.set(4, "1011");
        bldr3.set(5, "11010");
        bldr3.set(6, "1111000");
        bldr3.set(7, "11111000");
        bldr3.set(8, "1111110110");
        bldr3.set(9, "1111111110000010");
        bldr3.set(10, "1111111110000011");
        bldr3.set(17, "1100");
        bldr3.set(18, "11011");
        bldr3.set(19, "1111001");
        bldr3.set(20, "111110110");
        bldr3.set(21, "11111110110");
        bldr3.set(22, "1111111110000100");
        bldr3.set(23, "1111111110000101");
        bldr3.set(24, "1111111110000110");
        bldr3.set(25, "1111111110000111");
        bldr3.set(26, "1111111110001000");
        bldr3.set(33, "11100");
        bldr3.set(34, "11111001");
        bldr3.set(35, "1111110111");
        bldr3.set(36, "111111110100");
        bldr3.set(37, "1111111110001001");
        bldr3.set(38, "1111111110001010");
        bldr3.set(39, "1111111110001011");
        bldr3.set(40, "1111111110001100");
        bldr3.set(41, "1111111110001101");
        bldr3.set(42, "1111111110001110");
        bldr3.set(49, "111010");
        bldr3.set(50, "111110111");
        bldr3.set(51, "111111110101");
        bldr3.set(52, "1111111110001111");
        bldr3.set(53, "1111111110010000");
        bldr3.set(54, "1111111110010001");
        bldr3.set(55, "1111111110010010");
        bldr3.set(56, "1111111110010011");
        bldr3.set(57, "1111111110010100");
        bldr3.set(58, "1111111110010101");
        bldr3.set(65, "111011");
        bldr3.set(66, "1111111000");
        bldr3.set(67, "1111111110010110");
        bldr3.set(68, "1111111110010111");
        bldr3.set(69, "1111111110011000");
        bldr3.set(70, "1111111110011001");
        bldr3.set(71, "1111111110011010");
        bldr3.set(72, "1111111110011011");
        bldr3.set(73, "1111111110011100");
        bldr3.set(74, "1111111110011101");
        bldr3.set(81, "1111010");
        bldr3.set(82, "11111110111");
        bldr3.set(83, "1111111110011110");
        bldr3.set(84, "1111111110011111");
        bldr3.set(85, "1111111110100000");
        bldr3.set(86, "1111111110100001");
        bldr3.set(87, "1111111110100010");
        bldr3.set(88, "1111111110100011");
        bldr3.set(89, "1111111110100100");
        bldr3.set(90, "1111111110100101");
        bldr3.set(97, "1111011");
        bldr3.set(98, "111111110110");
        bldr3.set(99, "1111111110100110");
        bldr3.set(100, "1111111110100111");
        bldr3.set(101, "1111111110101000");
        bldr3.set(102, "1111111110101001");
        bldr3.set(103, "1111111110101010");
        bldr3.set(104, "1111111110101011");
        bldr3.set(105, "1111111110101100");
        bldr3.set(106, "1111111110101101");
        bldr3.set(113, "11111010");
        bldr3.set(114, "111111110111");
        bldr3.set(115, "1111111110101110");
        bldr3.set(116, "1111111110101111");
        bldr3.set(117, "1111111110110000");
        bldr3.set(118, "1111111110110001");
        bldr3.set(119, "1111111110110010");
        bldr3.set(120, "1111111110110011");
        bldr3.set(121, "1111111110110100");
        bldr3.set(122, "1111111110110101");
        bldr3.set(129, "111111000");
        bldr3.set(130, "111111111000000");
        bldr3.set(131, "1111111110110110");
        bldr3.set(132, "1111111110110111");
        bldr3.set(133, "1111111110111000");
        bldr3.set(134, "1111111110111001");
        bldr3.set(135, "1111111110111010");
        bldr3.set(136, "1111111110111011");
        bldr3.set(137, "1111111110111100");
        bldr3.set(138, "1111111110111101");
        bldr3.set(145, "111111001");
        bldr3.set(146, "1111111110111110");
        bldr3.set(147, "1111111110111111");
        bldr3.set(148, "1111111111000000");
        bldr3.set(149, "1111111111000001");
        bldr3.set(150, "1111111111000010");
        bldr3.set(151, "1111111111000011");
        bldr3.set(152, "1111111111000100");
        bldr3.set(153, "1111111111000101");
        bldr3.set(154, "1111111111000110");
        bldr3.set(161, "111111010");
        bldr3.set(162, "1111111111000111");
        bldr3.set(163, "1111111111001000");
        bldr3.set(164, "1111111111001001");
        bldr3.set(165, "1111111111001010");
        bldr3.set(166, "1111111111001011");
        bldr3.set(167, "1111111111001100");
        bldr3.set(168, "1111111111001101");
        bldr3.set(169, "1111111111001110");
        bldr3.set(170, "1111111111001111");
        bldr3.set(177, "1111111001");
        bldr3.set(178, "1111111111010000");
        bldr3.set(179, "1111111111010001");
        bldr3.set(180, "1111111111010010");
        bldr3.set(181, "1111111111010011");
        bldr3.set(182, "1111111111010100");
        bldr3.set(183, "1111111111010101");
        bldr3.set(184, "1111111111010110");
        bldr3.set(185, "1111111111010111");
        bldr3.set(186, "1111111111011000");
        bldr3.set(193, "1111111010");
        bldr3.set(194, "1111111111011001");
        bldr3.set(195, "1111111111011010");
        bldr3.set(196, "1111111111011011");
        bldr3.set(197, "1111111111011100");
        bldr3.set(198, "1111111111011101");
        bldr3.set(199, "1111111111011110");
        bldr3.set(200, "1111111111011111");
        bldr3.set(201, "1111111111100000");
        bldr3.set(202, "1111111111100001");
        bldr3.set(209, "11111111000");
        bldr3.set(210, "1111111111100010");
        bldr3.set(211, "1111111111100011");
        bldr3.set(212, "1111111111100100");
        bldr3.set(213, "1111111111100101");
        bldr3.set(214, "1111111111100110");
        bldr3.set(215, "1111111111100111");
        bldr3.set(216, "1111111111101000");
        bldr3.set(217, "1111111111101001");
        bldr3.set(218, "1111111111101010");
        bldr3.set(225, "1111111111101011");
        bldr3.set(226, "1111111111101100");
        bldr3.set(227, "1111111111101101");
        bldr3.set(228, "1111111111101110");
        bldr3.set(229, "1111111111101111");
        bldr3.set(230, "1111111111110000");
        bldr3.set(231, "1111111111110001");
        bldr3.set(232, "1111111111110010");
        bldr3.set(233, "1111111111110011");
        bldr3.set(234, "1111111111110100");
        bldr3.set(240, "11111111001");
        bldr3.set(241, "1111111111110101");
        bldr3.set(242, "1111111111110110");
        bldr3.set(243, "1111111111110111");
        bldr3.set(244, "1111111111111000");
        bldr3.set(245, "1111111111111001");
        bldr3.set(246, "1111111111111010");
        bldr3.set(247, "1111111111111011");
        bldr3.set(248, "1111111111111100");
        bldr3.set(249, "1111111111111101");
        bldr3.set(250, "1111111111111110");
        YAC_DEFAULT = bldr3.getVLC();
        VLCBuilder bldr4 = new VLCBuilder();
        bldr4.set(0, "00");
        bldr4.set(1, "01");
        bldr4.set(2, "100");
        bldr4.set(3, "1010");
        bldr4.set(4, "11000");
        bldr4.set(5, "11001");
        bldr4.set(6, "111000");
        bldr4.set(7, "1111000");
        bldr4.set(8, "111110100");
        bldr4.set(9, "1111110110");
        bldr4.set(10, "111111110100");
        bldr4.set(17, "1011");
        bldr4.set(18, "111001");
        bldr4.set(19, "11110110");
        bldr4.set(20, "111110101");
        bldr4.set(21, "11111110110");
        bldr4.set(22, "111111110101");
        bldr4.set(23, "1111111110001000");
        bldr4.set(24, "1111111110001001");
        bldr4.set(25, "1111111110001010");
        bldr4.set(26, "1111111110001011");
        bldr4.set(33, "11010");
        bldr4.set(34, "11110111");
        bldr4.set(35, "1111110111");
        bldr4.set(36, "111111110110");
        bldr4.set(37, "111111111000010");
        bldr4.set(38, "1111111110001100");
        bldr4.set(39, "1111111110001101");
        bldr4.set(40, "1111111110001110");
        bldr4.set(41, "1111111110001111");
        bldr4.set(42, "1111111110010000");
        bldr4.set(49, "11011");
        bldr4.set(50, "11111000");
        bldr4.set(51, "1111111000");
        bldr4.set(52, "111111110111");
        bldr4.set(53, "1111111110010001");
        bldr4.set(54, "1111111110010010");
        bldr4.set(55, "1111111110010011");
        bldr4.set(56, "1111111110010100");
        bldr4.set(57, "1111111110010101");
        bldr4.set(58, "1111111110010110");
        bldr4.set(65, "111010");
        bldr4.set(66, "111110110");
        bldr4.set(67, "1111111110010111");
        bldr4.set(68, "1111111110011000");
        bldr4.set(69, "1111111110011001");
        bldr4.set(70, "1111111110011010");
        bldr4.set(71, "1111111110011011");
        bldr4.set(72, "1111111110011100");
        bldr4.set(73, "1111111110011101");
        bldr4.set(74, "1111111110011110");
        bldr4.set(81, "111011");
        bldr4.set(82, "1111111001");
        bldr4.set(83, "1111111110011111");
        bldr4.set(84, "1111111110100000");
        bldr4.set(85, "1111111110100001");
        bldr4.set(86, "1111111110100010");
        bldr4.set(87, "1111111110100011");
        bldr4.set(88, "1111111110100100");
        bldr4.set(89, "1111111110100101");
        bldr4.set(90, "1111111110100110");
        bldr4.set(97, "1111001");
        bldr4.set(98, "11111110111");
        bldr4.set(99, "1111111110100111");
        bldr4.set(100, "1111111110101000");
        bldr4.set(101, "1111111110101001");
        bldr4.set(102, "1111111110101010");
        bldr4.set(103, "1111111110101011");
        bldr4.set(104, "1111111110101100");
        bldr4.set(105, "1111111110101101");
        bldr4.set(106, "1111111110101110");
        bldr4.set(113, "1111010");
        bldr4.set(114, "11111111000");
        bldr4.set(115, "1111111110101111");
        bldr4.set(116, "1111111110110000");
        bldr4.set(117, "1111111110110001");
        bldr4.set(118, "1111111110110010");
        bldr4.set(119, "1111111110110011");
        bldr4.set(120, "1111111110110100");
        bldr4.set(121, "1111111110110101");
        bldr4.set(122, "1111111110110110");
        bldr4.set(129, "11111001");
        bldr4.set(130, "1111111110110111");
        bldr4.set(131, "1111111110111000");
        bldr4.set(132, "1111111110111001");
        bldr4.set(133, "1111111110111010");
        bldr4.set(134, "1111111110111011");
        bldr4.set(135, "1111111110111100");
        bldr4.set(136, "1111111110111101");
        bldr4.set(137, "1111111110111110");
        bldr4.set(138, "1111111110111111");
        bldr4.set(145, "111110111");
        bldr4.set(146, "1111111111000000");
        bldr4.set(147, "1111111111000001");
        bldr4.set(148, "1111111111000010");
        bldr4.set(149, "1111111111000011");
        bldr4.set(150, "1111111111000100");
        bldr4.set(151, "1111111111000101");
        bldr4.set(152, "1111111111000110");
        bldr4.set(153, "1111111111000111");
        bldr4.set(154, "1111111111001000");
        bldr4.set(161, "111111000");
        bldr4.set(162, "1111111111001001");
        bldr4.set(163, "1111111111001010");
        bldr4.set(164, "1111111111001011");
        bldr4.set(165, "1111111111001100");
        bldr4.set(166, "1111111111001101");
        bldr4.set(167, "1111111111001110");
        bldr4.set(168, "1111111111001111");
        bldr4.set(169, "1111111111010000");
        bldr4.set(170, "1111111111010001");
        bldr4.set(177, "111111001");
        bldr4.set(178, "1111111111010010");
        bldr4.set(179, "1111111111010011");
        bldr4.set(180, "1111111111010100");
        bldr4.set(181, "1111111111010101");
        bldr4.set(182, "1111111111010110");
        bldr4.set(183, "1111111111010111");
        bldr4.set(184, "1111111111011000");
        bldr4.set(185, "1111111111011001");
        bldr4.set(186, "1111111111011010");
        bldr4.set(193, "111111010");
        bldr4.set(194, "1111111111011011");
        bldr4.set(195, "1111111111011100");
        bldr4.set(196, "1111111111011101");
        bldr4.set(197, "1111111111011110");
        bldr4.set(198, "1111111111011111");
        bldr4.set(199, "1111111111100000");
        bldr4.set(200, "1111111111100001");
        bldr4.set(201, "1111111111100010");
        bldr4.set(202, "1111111111100011");
        bldr4.set(209, "11111111001");
        bldr4.set(210, "1111111111100100");
        bldr4.set(211, "1111111111100101");
        bldr4.set(212, "1111111111100110");
        bldr4.set(213, "1111111111100111");
        bldr4.set(214, "1111111111101000");
        bldr4.set(215, "1111111111101001");
        bldr4.set(216, "1111111111101010");
        bldr4.set(217, "1111111111101011");
        bldr4.set(218, "1111111111101100");
        bldr4.set(225, "11111111100000");
        bldr4.set(226, "1111111111101101");
        bldr4.set(227, "1111111111101110");
        bldr4.set(228, "1111111111101111");
        bldr4.set(229, "1111111111110000");
        bldr4.set(230, "1111111111110001");
        bldr4.set(231, "1111111111110010");
        bldr4.set(232, "1111111111110011");
        bldr4.set(233, "1111111111110100");
        bldr4.set(234, "1111111111110101");
        bldr4.set(240, "1111111010");
        bldr4.set(241, "111111111000011");
        bldr4.set(242, "1111111111110110");
        bldr4.set(243, "1111111111110111");
        bldr4.set(244, "1111111111111000");
        bldr4.set(245, "1111111111111001");
        bldr4.set(246, "1111111111111010");
        bldr4.set(247, "1111111111111011");
        bldr4.set(248, "1111111111111100");
        bldr4.set(249, "1111111111111101");
        bldr4.set(250, "1111111111111110");
        CAC_DEFAULT = bldr4.getVLC();
        for (int i = 0; i < names.length; i++) {
            names[i] = "(0x" + Integer.toHexString(i) + ")";
        }
        names[192] = "SOF0";
        names[193] = "SOF1";
        names[194] = "SOF2";
        names[195] = "SOF3";
        names[196] = "DHT";
        names[219] = "DQT";
        names[218] = "SOS";
        names[217] = "EOI";
        names[216] = "SOI";
        names[224] = "APP0";
        names[225] = "APP1";
        names[226] = "APP2";
        names[227] = "APP3";
        names[228] = "APP4";
        names[229] = "APP5";
        names[230] = "APP6";
        names[231] = "APP7";
        names[232] = "APP8";
        names[233] = "APP9";
        names[234] = "APPA";
        names[235] = "APPB";
        names[236] = "APPC";
        names[237] = "APPD";
        names[238] = "APPE";
        names[239] = "APPF";
        names[208] = "RST0";
        names[209] = "RST1";
        names[210] = "RST2";
        names[211] = "RST3";
        names[212] = "RST4";
        names[213] = "RST5";
        names[214] = "RST6";
        names[215] = "RST7";
        names[221] = "DRI";
        DEFAULT_QUANT_LUMA = new int[] { 16, 11, 12, 14, 12, 10, 16, 14, 13, 14, 18, 17, 16, 19, 24, 40, 26, 24, 22, 22, 24, 49, 36, 37, 29, 40, 58, 51, 61, 60, 57, 51, 56, 55, 64, 72, 92, 78, 64, 68, 87, 69, 55, 56, 80, 109, 81, 87, 95, 62, 103, 104, 103, 98, 77, 113, 121, 112, 100, 120, 92, 101, 103, 99 };
        DEFAULT_QUANT_CHROMA = new int[] { 17, 18, 18, 24, 21, 24, 47, 26, 26, 47, 99, 66, 56, 66, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99 };
    }
}