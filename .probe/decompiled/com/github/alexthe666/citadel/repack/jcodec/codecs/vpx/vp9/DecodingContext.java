package com.github.alexthe666.citadel.repack.jcodec.codecs.vpx.vp9;

import com.github.alexthe666.citadel.repack.jcodec.codecs.vpx.VPXBooleanDecoder;
import com.github.alexthe666.citadel.repack.jcodec.common.ArrayUtil;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import java.nio.ByteBuffer;

public class DecodingContext {

    private int profile;

    private int showExistingFrame;

    private int frameToShowMapIdx;

    private int frameType;

    private int showFrame;

    private int errorResilientMode;

    private int refreshFrameFlags;

    private int frameIsIntra;

    private int intraOnly;

    private int resetFrameContext;

    private int colorSpace;

    int subsamplingX;

    int subsamplingY;

    int bitDepth;

    int frameWidth;

    int frameHeight;

    private int renderWidth;

    private int renderHeight;

    private int[] refFrameWidth = new int[4];

    private int[] refFrameHeight = new int[4];

    private int[] refFrameIdx = new int[3];

    private int[] refFrameSignBias = new int[3];

    private int allowHighPrecisionMv;

    int interpFilter;

    private int frameParallelDecodingMode;

    private int refreshFrameContext;

    private int frameContextIdx;

    private int[] loopFilterRefDeltas = new int[4];

    private int[] loopFilterModeDeltas = new int[2];

    private int baseQIdx;

    private int deltaQYDc;

    private int deltaQUvDc;

    private int deltaQUvAc;

    private boolean lossless;

    private boolean segmentationEnabled;

    private int[] segmentationTreeProbs = new int[7];

    private int[] segmentationPredProbs = new int[3];

    private int[][] featureEnabled = new int[8][4];

    private int[][] featureData = new int[8][4];

    private int tileRowsLog2;

    private int tileColsLog2;

    int txMode;

    private int compFixedRef;

    private int compVarRef0;

    private int compVarRef1;

    int refMode;

    int[][] tx8x8Probs = new int[2][1];

    int[][] tx16x16Probs = new int[2][2];

    int[][] tx32x32Probs = new int[2][3];

    int[][][][][][] coefProbs;

    private int[] skipProbs = new int[3];

    int[][] interModeProbs = new int[7][3];

    int[][] interpFilterProbs = new int[4][2];

    private int[] isInterProbs = new int[4];

    private int[] compModeProbs = new int[5];

    private int[][] singleRefProbs = new int[5][2];

    private int[] compRefProbs = new int[5];

    int[][] yModeProbs = new int[4][9];

    int[][] partitionProbs = new int[16][3];

    public int[][] uvModeProbs = new int[10][9];

    private int[] mvJointProbs = new int[3];

    private int[] mvSignProbs = new int[2];

    private int[][] mvClassProbs = new int[2][10];

    private int[] mvClass0BitProbs = new int[2];

    private int[][] mvBitsProbs = new int[2][10];

    private int[][][] mvClass0FrProbs = new int[2][2][3];

    private int[][] mvFrProbs = new int[2][3];

    private int[] mvClass0HpProb = new int[2];

    private int[] mvHpProbs = new int[2];

    private int filterLevel;

    private int sharpnessLevel;

    int[] leftPartitionSizes;

    int[] abovePartitionSizes;

    int tileHeight;

    int tileWidth;

    boolean[] leftSkipped;

    boolean[] aboveSkipped;

    int[][] aboveNonzeroContext;

    int[][] leftNonzeroContext;

    int[] aboveModes;

    int[] leftModes;

    private int colorRange;

    int[] aboveRefs;

    int[] leftRefs;

    int[] leftInterpFilters;

    int[] aboveInterpFilters;

    int miTileStartCol;

    int[] leftTxSizes;

    int[] aboveTxSizes;

    boolean[] leftCompound;

    boolean[] aboveCompound;

    private static final int[] defaultSkipProb = new int[] { 192, 128, 64 };

    private static final int[][] defaultTxProbs8x8 = new int[][] { { 100 }, { 66 } };

    private static final int[][] defaultTxProbs16x16 = new int[][] { { 20, 152 }, { 15, 101 } };

    private static final int[][] defaultTxProbs32x32 = new int[][] { { 3, 136, 37 }, { 5, 52, 13 } };

    public static final int[][][][][][] defaultCoefProbs = new int[][][][][][] { { { { { { 195, 29, 183 }, { 84, 49, 136 }, { 8, 42, 71 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }, { { 31, 107, 169 }, { 35, 99, 159 }, { 17, 82, 140 }, { 8, 66, 114 }, { 2, 44, 76 }, { 1, 19, 32 } }, { { 40, 132, 201 }, { 29, 114, 187 }, { 13, 91, 157 }, { 7, 75, 127 }, { 3, 58, 95 }, { 1, 28, 47 } }, { { 69, 142, 221 }, { 42, 122, 201 }, { 15, 91, 159 }, { 6, 67, 121 }, { 1, 42, 77 }, { 1, 17, 31 } }, { { 102, 148, 228 }, { 67, 117, 204 }, { 17, 82, 154 }, { 6, 59, 114 }, { 2, 39, 75 }, { 1, 15, 29 } }, { { 156, 57, 233 }, { 119, 57, 212 }, { 58, 48, 163 }, { 29, 40, 124 }, { 12, 30, 81 }, { 3, 12, 31 } } }, { { { 191, 107, 226 }, { 124, 117, 204 }, { 25, 99, 155 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }, { { 29, 148, 210 }, { 37, 126, 194 }, { 8, 93, 157 }, { 2, 68, 118 }, { 1, 39, 69 }, { 1, 17, 33 } }, { { 41, 151, 213 }, { 27, 123, 193 }, { 3, 82, 144 }, { 1, 58, 105 }, { 1, 32, 60 }, { 1, 13, 26 } }, { { 59, 159, 220 }, { 23, 126, 198 }, { 4, 88, 151 }, { 1, 66, 114 }, { 1, 38, 71 }, { 1, 18, 34 } }, { { 114, 136, 232 }, { 51, 114, 207 }, { 11, 83, 155 }, { 3, 56, 105 }, { 1, 33, 65 }, { 1, 17, 34 } }, { { 149, 65, 234 }, { 121, 57, 215 }, { 61, 49, 166 }, { 28, 36, 114 }, { 12, 25, 76 }, { 3, 16, 42 } } } }, { { { { 214, 49, 220 }, { 132, 63, 188 }, { 42, 65, 137 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }, { { 85, 137, 221 }, { 104, 131, 216 }, { 49, 111, 192 }, { 21, 87, 155 }, { 2, 49, 87 }, { 1, 16, 28 } }, { { 89, 163, 230 }, { 90, 137, 220 }, { 29, 100, 183 }, { 10, 70, 135 }, { 2, 42, 81 }, { 1, 17, 33 } }, { { 108, 167, 237 }, { 55, 133, 222 }, { 15, 97, 179 }, { 4, 72, 135 }, { 1, 45, 85 }, { 1, 19, 38 } }, { { 124, 146, 240 }, { 66, 124, 224 }, { 17, 88, 175 }, { 4, 58, 122 }, { 1, 36, 75 }, { 1, 18, 37 } }, { { 141, 79, 241 }, { 126, 70, 227 }, { 66, 58, 182 }, { 30, 44, 136 }, { 12, 34, 96 }, { 2, 20, 47 } } }, { { { 229, 99, 249 }, { 143, 111, 235 }, { 46, 109, 192 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }, { { 82, 158, 236 }, { 94, 146, 224 }, { 25, 117, 191 }, { 9, 87, 149 }, { 3, 56, 99 }, { 1, 33, 57 } }, { { 83, 167, 237 }, { 68, 145, 222 }, { 10, 103, 177 }, { 2, 72, 131 }, { 1, 41, 79 }, { 1, 20, 39 } }, { { 99, 167, 239 }, { 47, 141, 224 }, { 10, 104, 178 }, { 2, 73, 133 }, { 1, 44, 85 }, { 1, 22, 47 } }, { { 127, 145, 243 }, { 71, 129, 228 }, { 17, 93, 177 }, { 3, 61, 124 }, { 1, 41, 84 }, { 1, 21, 52 } }, { { 157, 78, 244 }, { 140, 72, 231 }, { 69, 58, 184 }, { 31, 44, 137 }, { 14, 38, 105 }, { 8, 23, 61 } } } } }, { { { { { 125, 34, 187 }, { 52, 41, 133 }, { 6, 31, 56 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }, { { 37, 109, 153 }, { 51, 102, 147 }, { 23, 87, 128 }, { 8, 67, 101 }, { 1, 41, 63 }, { 1, 19, 29 } }, { { 31, 154, 185 }, { 17, 127, 175 }, { 6, 96, 145 }, { 2, 73, 114 }, { 1, 51, 82 }, { 1, 28, 45 } }, { { 23, 163, 200 }, { 10, 131, 185 }, { 2, 93, 148 }, { 1, 67, 111 }, { 1, 41, 69 }, { 1, 14, 24 } }, { { 29, 176, 217 }, { 12, 145, 201 }, { 3, 101, 156 }, { 1, 69, 111 }, { 1, 39, 63 }, { 1, 14, 23 } }, { { 57, 192, 233 }, { 25, 154, 215 }, { 6, 109, 167 }, { 3, 78, 118 }, { 1, 48, 69 }, { 1, 21, 29 } } }, { { { 202, 105, 245 }, { 108, 106, 216 }, { 18, 90, 144 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }, { { 33, 172, 219 }, { 64, 149, 206 }, { 14, 117, 177 }, { 5, 90, 141 }, { 2, 61, 95 }, { 1, 37, 57 } }, { { 33, 179, 220 }, { 11, 140, 198 }, { 1, 89, 148 }, { 1, 60, 104 }, { 1, 33, 57 }, { 1, 12, 21 } }, { { 30, 181, 221 }, { 8, 141, 198 }, { 1, 87, 145 }, { 1, 58, 100 }, { 1, 31, 55 }, { 1, 12, 20 } }, { { 32, 186, 224 }, { 7, 142, 198 }, { 1, 86, 143 }, { 1, 58, 100 }, { 1, 31, 55 }, { 1, 12, 22 } }, { { 57, 192, 227 }, { 20, 143, 204 }, { 3, 96, 154 }, { 1, 68, 112 }, { 1, 42, 69 }, { 1, 19, 32 } } } }, { { { { 212, 35, 215 }, { 113, 47, 169 }, { 29, 48, 105 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }, { { 74, 129, 203 }, { 106, 120, 203 }, { 49, 107, 178 }, { 19, 84, 144 }, { 4, 50, 84 }, { 1, 15, 25 } }, { { 71, 172, 217 }, { 44, 141, 209 }, { 15, 102, 173 }, { 6, 76, 133 }, { 2, 51, 89 }, { 1, 24, 42 } }, { { 64, 185, 231 }, { 31, 148, 216 }, { 8, 103, 175 }, { 3, 74, 131 }, { 1, 46, 81 }, { 1, 18, 30 } }, { { 65, 196, 235 }, { 25, 157, 221 }, { 5, 105, 174 }, { 1, 67, 120 }, { 1, 38, 69 }, { 1, 15, 30 } }, { { 65, 204, 238 }, { 30, 156, 224 }, { 7, 107, 177 }, { 2, 70, 124 }, { 1, 42, 73 }, { 1, 18, 34 } } }, { { { 225, 86, 251 }, { 144, 104, 235 }, { 42, 99, 181 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }, { { 85, 175, 239 }, { 112, 165, 229 }, { 29, 136, 200 }, { 12, 103, 162 }, { 6, 77, 123 }, { 2, 53, 84 } }, { { 75, 183, 239 }, { 30, 155, 221 }, { 3, 106, 171 }, { 1, 74, 128 }, { 1, 44, 76 }, { 1, 17, 28 } }, { { 73, 185, 240 }, { 27, 159, 222 }, { 2, 107, 172 }, { 1, 75, 127 }, { 1, 42, 73 }, { 1, 17, 29 } }, { { 62, 190, 238 }, { 21, 159, 222 }, { 2, 107, 172 }, { 1, 72, 122 }, { 1, 40, 71 }, { 1, 18, 32 } }, { { 61, 199, 240 }, { 27, 161, 226 }, { 4, 113, 180 }, { 1, 76, 129 }, { 1, 46, 80 }, { 1, 23, 41 } } } } }, { { { { { 7, 27, 153 }, { 5, 30, 95 }, { 1, 16, 30 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }, { { 50, 75, 127 }, { 57, 75, 124 }, { 27, 67, 108 }, { 10, 54, 86 }, { 1, 33, 52 }, { 1, 12, 18 } }, { { 43, 125, 151 }, { 26, 108, 148 }, { 7, 83, 122 }, { 2, 59, 89 }, { 1, 38, 60 }, { 1, 17, 27 } }, { { 23, 144, 163 }, { 13, 112, 154 }, { 2, 75, 117 }, { 1, 50, 81 }, { 1, 31, 51 }, { 1, 14, 23 } }, { { 18, 162, 185 }, { 6, 123, 171 }, { 1, 78, 125 }, { 1, 51, 86 }, { 1, 31, 54 }, { 1, 14, 23 } }, { { 15, 199, 227 }, { 3, 150, 204 }, { 1, 91, 146 }, { 1, 55, 95 }, { 1, 30, 53 }, { 1, 11, 20 } } }, { { { 19, 55, 240 }, { 19, 59, 196 }, { 3, 52, 105 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }, { { 41, 166, 207 }, { 104, 153, 199 }, { 31, 123, 181 }, { 14, 101, 152 }, { 5, 72, 106 }, { 1, 36, 52 } }, { { 35, 176, 211 }, { 12, 131, 190 }, { 2, 88, 144 }, { 1, 60, 101 }, { 1, 36, 60 }, { 1, 16, 28 } }, { { 28, 183, 213 }, { 8, 134, 191 }, { 1, 86, 142 }, { 1, 56, 96 }, { 1, 30, 53 }, { 1, 12, 20 } }, { { 20, 190, 215 }, { 4, 135, 192 }, { 1, 84, 139 }, { 1, 53, 91 }, { 1, 28, 49 }, { 1, 11, 20 } }, { { 13, 196, 216 }, { 2, 137, 192 }, { 1, 86, 143 }, { 1, 57, 99 }, { 1, 32, 56 }, { 1, 13, 24 } } } }, { { { { 211, 29, 217 }, { 96, 47, 156 }, { 22, 43, 87 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }, { { 78, 120, 193 }, { 111, 116, 186 }, { 46, 102, 164 }, { 15, 80, 128 }, { 2, 49, 76 }, { 1, 18, 28 } }, { { 71, 161, 203 }, { 42, 132, 192 }, { 10, 98, 150 }, { 3, 69, 109 }, { 1, 44, 70 }, { 1, 18, 29 } }, { { 57, 186, 211 }, { 30, 140, 196 }, { 4, 93, 146 }, { 1, 62, 102 }, { 1, 38, 65 }, { 1, 16, 27 } }, { { 47, 199, 217 }, { 14, 145, 196 }, { 1, 88, 142 }, { 1, 57, 98 }, { 1, 36, 62 }, { 1, 15, 26 } }, { { 26, 219, 229 }, { 5, 155, 207 }, { 1, 94, 151 }, { 1, 60, 104 }, { 1, 36, 62 }, { 1, 16, 28 } } }, { { { 233, 29, 248 }, { 146, 47, 220 }, { 43, 52, 140 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }, { { 100, 163, 232 }, { 179, 161, 222 }, { 63, 142, 204 }, { 37, 113, 174 }, { 26, 89, 137 }, { 18, 68, 97 } }, { { 85, 181, 230 }, { 32, 146, 209 }, { 7, 100, 164 }, { 3, 71, 121 }, { 1, 45, 77 }, { 1, 18, 30 } }, { { 65, 187, 230 }, { 20, 148, 207 }, { 2, 97, 159 }, { 1, 68, 116 }, { 1, 40, 70 }, { 1, 14, 29 } }, { { 40, 194, 227 }, { 8, 147, 204 }, { 1, 94, 155 }, { 1, 65, 112 }, { 1, 39, 66 }, { 1, 14, 26 } }, { { 16, 208, 228 }, { 3, 151, 207 }, { 1, 98, 160 }, { 1, 67, 117 }, { 1, 41, 74 }, { 1, 17, 31 } } } } }, { { { { { 17, 38, 140 }, { 7, 34, 80 }, { 1, 17, 29 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }, { { 37, 75, 128 }, { 41, 76, 128 }, { 26, 66, 116 }, { 12, 52, 94 }, { 2, 32, 55 }, { 1, 10, 16 } }, { { 50, 127, 154 }, { 37, 109, 152 }, { 16, 82, 121 }, { 5, 59, 85 }, { 1, 35, 54 }, { 1, 13, 20 } }, { { 40, 142, 167 }, { 17, 110, 157 }, { 2, 71, 112 }, { 1, 44, 72 }, { 1, 27, 45 }, { 1, 11, 17 } }, { { 30, 175, 188 }, { 9, 124, 169 }, { 1, 74, 116 }, { 1, 48, 78 }, { 1, 30, 49 }, { 1, 11, 18 } }, { { 10, 222, 223 }, { 2, 150, 194 }, { 1, 83, 128 }, { 1, 48, 79 }, { 1, 27, 45 }, { 1, 11, 17 } } }, { { { 36, 41, 235 }, { 29, 36, 193 }, { 10, 27, 111 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }, { { 85, 165, 222 }, { 177, 162, 215 }, { 110, 135, 195 }, { 57, 113, 168 }, { 23, 83, 120 }, { 10, 49, 61 } }, { { 85, 190, 223 }, { 36, 139, 200 }, { 5, 90, 146 }, { 1, 60, 103 }, { 1, 38, 65 }, { 1, 18, 30 } }, { { 72, 202, 223 }, { 23, 141, 199 }, { 2, 86, 140 }, { 1, 56, 97 }, { 1, 36, 61 }, { 1, 16, 27 } }, { { 55, 218, 225 }, { 13, 145, 200 }, { 1, 86, 141 }, { 1, 57, 99 }, { 1, 35, 61 }, { 1, 13, 22 } }, { { 15, 235, 212 }, { 1, 132, 184 }, { 1, 84, 139 }, { 1, 57, 97 }, { 1, 34, 56 }, { 1, 14, 23 } } } }, { { { { 181, 21, 201 }, { 61, 37, 123 }, { 10, 38, 71 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }, { { 47, 106, 172 }, { 95, 104, 173 }, { 42, 93, 159 }, { 18, 77, 131 }, { 4, 50, 81 }, { 1, 17, 23 } }, { { 62, 147, 199 }, { 44, 130, 189 }, { 28, 102, 154 }, { 18, 75, 115 }, { 2, 44, 65 }, { 1, 12, 19 } }, { { 55, 153, 210 }, { 24, 130, 194 }, { 3, 93, 146 }, { 1, 61, 97 }, { 1, 31, 50 }, { 1, 10, 16 } }, { { 49, 186, 223 }, { 17, 148, 204 }, { 1, 96, 142 }, { 1, 53, 83 }, { 1, 26, 44 }, { 1, 11, 17 } }, { { 13, 217, 212 }, { 2, 136, 180 }, { 1, 78, 124 }, { 1, 50, 83 }, { 1, 29, 49 }, { 1, 14, 23 } } }, { { { 197, 13, 247 }, { 82, 17, 222 }, { 25, 17, 162 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }, { { 126, 186, 247 }, { 234, 191, 243 }, { 176, 177, 234 }, { 104, 158, 220 }, { 66, 128, 186 }, { 55, 90, 137 } }, { { 111, 197, 242 }, { 46, 158, 219 }, { 9, 104, 171 }, { 2, 65, 125 }, { 1, 44, 80 }, { 1, 17, 91 } }, { { 104, 208, 245 }, { 39, 168, 224 }, { 3, 109, 162 }, { 1, 79, 124 }, { 1, 50, 102 }, { 1, 43, 102 } }, { { 84, 220, 246 }, { 31, 177, 231 }, { 2, 115, 180 }, { 1, 79, 134 }, { 1, 55, 77 }, { 1, 60, 79 } }, { { 43, 243, 240 }, { 8, 180, 217 }, { 1, 115, 166 }, { 1, 84, 121 }, { 1, 51, 67 }, { 1, 16, 6 } } } } } };

    public static final int[] defaultMvJointProbs = new int[] { 32, 64, 96 };

    public static final int[][] defaultMvBitsProb = new int[][] { { 136, 140, 148, 160, 176, 192, 224, 234, 234, 240 }, { 136, 140, 148, 160, 176, 192, 224, 234, 234, 240 } };

    public static final int[] defaultMvClass0BitProb = new int[] { 216, 208 };

    public static final int[] defaultMvClass0HpProb = new int[] { 160, 160 };

    public static final int[] defaultMvSignProb = new int[] { 128, 128 };

    public static final int[][] defaultMvClassProbs = new int[][] { { 224, 144, 192, 168, 192, 176, 192, 198, 198, 245 }, { 216, 128, 176, 160, 176, 176, 192, 198, 198, 208 } };

    public static final int[][][] defaultMvClass0FrProbs = new int[][][] { { { 128, 128, 64 }, { 96, 112, 64 } }, { { 128, 128, 64 }, { 96, 112, 64 } } };

    public static final int[][] defaultMvFrProbs = new int[][] { { 64, 96, 64 }, { 64, 96, 64 } };

    public static final int[] defaultMvHpProb = new int[] { 128, 128 };

    public static final int[][] defaultInterModeProbs = new int[][] { { 2, 173, 34 }, { 7, 145, 85 }, { 7, 166, 63 }, { 7, 94, 66 }, { 8, 64, 46 }, { 17, 81, 31 }, { 25, 29, 30 } };

    public static final int[][] defaultInterpFilterProbs = new int[][] { { 235, 162 }, { 36, 255 }, { 34, 3 }, { 149, 144 } };

    public static final int[] defaultIsInterProbs = new int[] { 9, 102, 187, 225 };

    private static final int[][] defaultPartitionProbs = new int[][] { { 199, 122, 141 }, { 147, 63, 159 }, { 148, 133, 118 }, { 121, 104, 114 }, { 174, 73, 87 }, { 92, 41, 83 }, { 82, 99, 50 }, { 53, 39, 39 }, { 177, 58, 59 }, { 68, 26, 63 }, { 52, 79, 25 }, { 17, 14, 12 }, { 222, 34, 30 }, { 72, 16, 44 }, { 58, 32, 12 }, { 10, 7, 6 } };

    public static final int[][][] kfYmodeProbs = new int[][][] { { { 137, 30, 42, 148, 151, 207, 70, 52, 91 }, { 92, 45, 102, 136, 116, 180, 74, 90, 100 }, { 73, 32, 19, 187, 222, 215, 46, 34, 100 }, { 91, 30, 32, 116, 121, 186, 93, 86, 94 }, { 72, 35, 36, 149, 68, 206, 68, 63, 105 }, { 73, 31, 28, 138, 57, 124, 55, 122, 151 }, { 67, 23, 21, 140, 126, 197, 40, 37, 171 }, { 86, 27, 28, 128, 154, 212, 45, 43, 53 }, { 74, 32, 27, 107, 86, 160, 63, 134, 102 }, { 59, 67, 44, 140, 161, 202, 78, 67, 119 } }, { { 63, 36, 126, 146, 123, 158, 60, 90, 96 }, { 43, 46, 168, 134, 107, 128, 69, 142, 92 }, { 44, 29, 68, 159, 201, 177, 50, 57, 77 }, { 58, 38, 76, 114, 97, 172, 78, 133, 92 }, { 46, 41, 76, 140, 63, 184, 69, 112, 57 }, { 38, 32, 85, 140, 46, 112, 54, 151, 133 }, { 39, 27, 61, 131, 110, 175, 44, 75, 136 }, { 52, 30, 74, 113, 130, 175, 51, 64, 58 }, { 47, 35, 80, 100, 74, 143, 64, 163, 74 }, { 36, 61, 116, 114, 128, 162, 80, 125, 82 } }, { { 82, 26, 26, 171, 208, 204, 44, 32, 105 }, { 55, 44, 68, 166, 179, 192, 57, 57, 108 }, { 42, 26, 11, 199, 241, 228, 23, 15, 85 }, { 68, 42, 19, 131, 160, 199, 55, 52, 83 }, { 58, 50, 25, 139, 115, 232, 39, 52, 118 }, { 50, 35, 33, 153, 104, 162, 64, 59, 131 }, { 44, 24, 16, 150, 177, 202, 33, 19, 156 }, { 55, 27, 12, 153, 203, 218, 26, 27, 49 }, { 53, 49, 21, 110, 116, 168, 59, 80, 76 }, { 38, 72, 19, 168, 203, 212, 50, 50, 107 } }, { { 103, 26, 36, 129, 132, 201, 83, 80, 93 }, { 59, 38, 83, 112, 103, 162, 98, 136, 90 }, { 62, 30, 23, 158, 200, 207, 59, 57, 50 }, { 67, 30, 29, 84, 86, 191, 102, 91, 59 }, { 60, 32, 33, 112, 71, 220, 64, 89, 104 }, { 53, 26, 34, 130, 56, 149, 84, 120, 103 }, { 53, 21, 23, 133, 109, 210, 56, 77, 172 }, { 77, 19, 29, 112, 142, 228, 55, 66, 36 }, { 61, 29, 29, 93, 97, 165, 83, 175, 162 }, { 47, 47, 43, 114, 137, 181, 100, 99, 95 } }, { { 69, 23, 29, 128, 83, 199, 46, 44, 101 }, { 53, 40, 55, 139, 69, 183, 61, 80, 110 }, { 40, 29, 19, 161, 180, 207, 43, 24, 91 }, { 60, 34, 19, 105, 61, 198, 53, 64, 89 }, { 52, 31, 22, 158, 40, 209, 58, 62, 89 }, { 44, 31, 29, 147, 46, 158, 56, 102, 198 }, { 35, 19, 12, 135, 87, 209, 41, 45, 167 }, { 55, 25, 21, 118, 95, 215, 38, 39, 66 }, { 51, 38, 25, 113, 58, 164, 70, 93, 97 }, { 47, 54, 34, 146, 108, 203, 72, 103, 151 } }, { { 64, 19, 37, 156, 66, 138, 49, 95, 133 }, { 46, 27, 80, 150, 55, 124, 55, 121, 135 }, { 36, 23, 27, 165, 149, 166, 54, 64, 118 }, { 53, 21, 36, 131, 63, 163, 60, 109, 81 }, { 40, 26, 35, 154, 40, 185, 51, 97, 123 }, { 35, 19, 34, 179, 19, 97, 48, 129, 124 }, { 36, 20, 26, 136, 62, 164, 33, 77, 154 }, { 45, 18, 32, 130, 90, 157, 40, 79, 91 }, { 45, 26, 28, 129, 45, 129, 49, 147, 123 }, { 38, 44, 51, 136, 74, 162, 57, 97, 121 } }, { { 75, 17, 22, 136, 138, 185, 32, 34, 166 }, { 56, 39, 58, 133, 117, 173, 48, 53, 187 }, { 35, 21, 12, 161, 212, 207, 20, 23, 145 }, { 56, 29, 19, 117, 109, 181, 55, 68, 112 }, { 47, 29, 17, 153, 64, 220, 59, 51, 114 }, { 46, 16, 24, 136, 76, 147, 41, 64, 172 }, { 34, 17, 11, 108, 152, 187, 13, 15, 209 }, { 51, 24, 14, 115, 133, 209, 32, 26, 104 }, { 55, 30, 18, 122, 79, 179, 44, 88, 116 }, { 37, 49, 25, 129, 168, 164, 41, 54, 148 } }, { { 82, 22, 32, 127, 143, 213, 39, 41, 70 }, { 62, 44, 61, 123, 105, 189, 48, 57, 64 }, { 47, 25, 17, 175, 222, 220, 24, 30, 86 }, { 68, 36, 17, 106, 102, 206, 59, 74, 74 }, { 57, 39, 23, 151, 68, 216, 55, 63, 58 }, { 49, 30, 35, 141, 70, 168, 82, 40, 115 }, { 51, 25, 15, 136, 129, 202, 38, 35, 139 }, { 68, 26, 16, 111, 141, 215, 29, 28, 28 }, { 59, 39, 19, 114, 75, 180, 77, 104, 42 }, { 40, 61, 26, 126, 152, 206, 61, 59, 93 } }, { { 78, 23, 39, 111, 117, 170, 74, 124, 94 }, { 48, 34, 86, 101, 92, 146, 78, 179, 134 }, { 47, 22, 24, 138, 187, 178, 68, 69, 59 }, { 56, 25, 33, 105, 112, 187, 95, 177, 129 }, { 48, 31, 27, 114, 63, 183, 82, 116, 56 }, { 43, 28, 37, 121, 63, 123, 61, 192, 169 }, { 42, 17, 24, 109, 97, 177, 56, 76, 122 }, { 58, 18, 28, 105, 139, 182, 70, 92, 63 }, { 46, 23, 32, 74, 86, 150, 67, 183, 88 }, { 36, 38, 48, 92, 122, 165, 88, 137, 91 } }, { { 65, 70, 60, 155, 159, 199, 61, 60, 81 }, { 44, 78, 115, 132, 119, 173, 71, 112, 93 }, { 39, 38, 21, 184, 227, 206, 42, 32, 64 }, { 58, 47, 36, 124, 137, 193, 80, 82, 78 }, { 49, 50, 35, 144, 95, 205, 63, 78, 59 }, { 41, 53, 52, 148, 71, 142, 65, 128, 51 }, { 40, 36, 28, 143, 143, 202, 40, 55, 137 }, { 52, 34, 29, 129, 183, 227, 42, 35, 43 }, { 42, 44, 44, 104, 105, 164, 64, 130, 80 }, { 43, 81, 53, 140, 169, 204, 68, 84, 72 } } };

    public static final int[][] kfUvModeProbs = new int[][] { { 144, 11, 54, 157, 195, 130, 46, 58, 108 }, { 118, 15, 123, 148, 131, 101, 44, 93, 131 }, { 113, 12, 23, 188, 226, 142, 26, 32, 125 }, { 120, 11, 50, 123, 163, 135, 64, 77, 103 }, { 113, 9, 36, 155, 111, 157, 32, 44, 161 }, { 116, 9, 55, 176, 76, 96, 37, 61, 149 }, { 115, 9, 28, 141, 161, 167, 21, 25, 193 }, { 120, 12, 32, 145, 195, 142, 32, 38, 86 }, { 116, 12, 64, 120, 140, 125, 49, 115, 121 }, { 102, 19, 66, 162, 182, 122, 35, 59, 128 } };

    public static final int[][] defaultYModeProbs = new int[][] { { 65, 32, 18, 144, 162, 194, 41, 51, 98 }, { 132, 68, 18, 165, 217, 196, 45, 40, 78 }, { 173, 80, 19, 176, 240, 193, 64, 35, 46 }, { 221, 135, 38, 194, 248, 121, 96, 85, 29 } };

    public static final int[][] defaultUvModeProbs = new int[][] { { 120, 7, 76, 176, 208, 126, 28, 54, 103 }, { 48, 12, 154, 155, 139, 90, 34, 117, 119 }, { 67, 6, 25, 204, 243, 158, 13, 21, 96 }, { 97, 5, 44, 131, 176, 139, 48, 68, 97 }, { 83, 5, 42, 156, 111, 152, 26, 49, 152 }, { 80, 5, 58, 178, 74, 83, 33, 62, 145 }, { 86, 5, 32, 154, 192, 168, 14, 22, 163 }, { 85, 5, 32, 156, 216, 148, 19, 29, 73 }, { 77, 7, 64, 116, 132, 122, 37, 126, 120 }, { 101, 21, 107, 181, 192, 103, 19, 67, 125 } };

    public static final int[][] defaultSingleRefProb = new int[][] { { 33, 16 }, { 77, 74 }, { 142, 142 }, { 172, 170 }, { 238, 247 } };

    public static final int[] defaultCompRefProb = new int[] { 50, 126, 123, 221, 226 };

    public static DecodingContext createFromHeaders(ByteBuffer bb) {
        DecodingContext dc = new DecodingContext();
        int compressedHeaderSize = dc.readUncompressedHeader(bb);
        dc.readCompressedHeader(NIOUtils.read(bb, compressedHeaderSize));
        return dc;
    }

    protected DecodingContext() {
        ArrayUtil.copy1D(this.skipProbs, defaultSkipProb);
        ArrayUtil.copy2D(this.tx8x8Probs, defaultTxProbs8x8);
        ArrayUtil.copy2D(this.tx16x16Probs, defaultTxProbs16x16);
        ArrayUtil.copy2D(this.tx32x32Probs, defaultTxProbs32x32);
        this.coefProbs = new int[4][2][2][6][][];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    this.coefProbs[i][j][k][0] = new int[3][3];
                    for (int l = 1; l < 6; l++) {
                        this.coefProbs[i][j][k][l] = new int[6][3];
                    }
                }
            }
        }
        ArrayUtil.copy6D(this.coefProbs, defaultCoefProbs);
        ArrayUtil.copy1D(this.mvJointProbs, defaultMvJointProbs);
        ArrayUtil.copy1D(this.mvSignProbs, defaultMvSignProb);
        ArrayUtil.copy2D(this.mvClassProbs, defaultMvClassProbs);
        ArrayUtil.copy1D(this.mvClass0BitProbs, defaultMvClass0BitProb);
        ArrayUtil.copy2D(this.mvBitsProbs, defaultMvBitsProb);
        ArrayUtil.copy3D(this.mvClass0FrProbs, defaultMvClass0FrProbs);
        ArrayUtil.copy2D(this.mvFrProbs, defaultMvFrProbs);
        ArrayUtil.copy1D(this.mvClass0HpProb, defaultMvClass0HpProb);
        ArrayUtil.copy1D(this.mvHpProbs, defaultMvHpProb);
        ArrayUtil.copy2D(this.interModeProbs, defaultInterModeProbs);
        ArrayUtil.copy2D(this.interpFilterProbs, defaultInterpFilterProbs);
        ArrayUtil.copy1D(this.isInterProbs, defaultIsInterProbs);
        ArrayUtil.copy2D(this.singleRefProbs, defaultSingleRefProb);
        ArrayUtil.copy2D(this.yModeProbs, defaultYModeProbs);
        ArrayUtil.copy2D(this.uvModeProbs, defaultUvModeProbs);
        ArrayUtil.copy2D(this.partitionProbs, defaultPartitionProbs);
        ArrayUtil.copy1D(this.compRefProbs, defaultCompRefProb);
    }

    public boolean isKeyIntraFrame() {
        return false;
    }

    public boolean isSegmentationEnabled() {
        return this.segmentationEnabled;
    }

    public boolean isUpdateSegmentMap() {
        return false;
    }

    public boolean isSegmentFeatureActive(int segmentId, int segLvlSkip) {
        return false;
    }

    public boolean isSegmentMapConditionalUpdate() {
        return false;
    }

    public int getSegmentFeature(int segmentId, int segLvlRefFrame) {
        return 0;
    }

    public int getCompFixedRef() {
        return 0;
    }

    public int refFrameSignBias(int fixedRef) {
        return 0;
    }

    public int getInterpFilter() {
        return this.interpFilter;
    }

    public int getRefMode() {
        return this.refMode;
    }

    public long[][] getLeftMVs() {
        return (long[][]) null;
    }

    public long[][] getAboveMVs() {
        return (long[][]) null;
    }

    public long[][] getAboveLeftMVs() {
        return (long[][]) null;
    }

    public long[] getLeft4x4MVs() {
        return null;
    }

    public long[] getAbove4x4MVs() {
        return null;
    }

    public boolean[] getAboveCompound() {
        return this.aboveCompound;
    }

    public boolean[] getLeftCompound() {
        return this.leftCompound;
    }

    public boolean isAllowHpMv() {
        return false;
    }

    public boolean isUsePrevFrameMvs() {
        return false;
    }

    public long[][] getPrevFrameMv() {
        return (long[][]) null;
    }

    public int getMiFrameWidth() {
        return this.frameWidth + 7 >> 3;
    }

    public int getMiFrameHeight() {
        return this.frameHeight + 7 >> 3;
    }

    public int[] getLeftInterpFilters() {
        return this.leftInterpFilters;
    }

    public int[] getAboveInterpFilters() {
        return this.aboveInterpFilters;
    }

    public int getMiTileHeight() {
        return this.tileHeight;
    }

    public int getMiTileWidth() {
        return this.tileWidth;
    }

    public int getCompVarRef(int i) {
        return 0;
    }

    public int[] getAboveModes() {
        return this.aboveModes;
    }

    public int[] getLeftModes() {
        return this.leftModes;
    }

    public int getTxMode() {
        return this.txMode;
    }

    public boolean[] getAboveSegIdPredicted() {
        return null;
    }

    public boolean[] getLeftSegIdPredicted() {
        return null;
    }

    public int[][] getPrevSegmentIds() {
        return (int[][]) null;
    }

    public int getSubX() {
        return this.subsamplingX;
    }

    public int getSubY() {
        return this.subsamplingY;
    }

    public int getBitDepth() {
        return this.bitDepth;
    }

    public int[][] getAboveNonzeroContext() {
        return this.aboveNonzeroContext;
    }

    public int[][] getLeftNonzeroContext() {
        return this.leftNonzeroContext;
    }

    public int[] getLeftPartitionSizes() {
        return this.leftPartitionSizes;
    }

    public int[] getAbovePartitionSizes() {
        return this.abovePartitionSizes;
    }

    public boolean[] getLeftSkipped() {
        return this.leftSkipped;
    }

    public boolean[] getAboveSkipped() {
        return this.aboveSkipped;
    }

    protected int readUncompressedHeader(ByteBuffer bb) {
        BitReader br = BitReader.createBitReader(bb);
        int frame_marker = br.readNBit(2);
        this.profile = br.read1Bit() | br.read1Bit() << 1;
        if (this.profile == 3) {
            br.read1Bit();
        }
        this.showExistingFrame = br.read1Bit();
        if (this.showExistingFrame == 1) {
            this.frameToShowMapIdx = br.readNBit(3);
        }
        this.frameType = br.read1Bit();
        this.showFrame = br.read1Bit();
        this.errorResilientMode = br.read1Bit();
        if (this.frameType == 0) {
            frame_sync_code(br);
            this.readColorConfig(br);
            this.readFrameSize(br);
            this.readRenderSize(br);
            this.refreshFrameFlags = 255;
            this.frameIsIntra = 1;
        } else {
            this.intraOnly = 0;
            if (this.showFrame == 0) {
                this.intraOnly = br.read1Bit();
            }
            this.resetFrameContext = 0;
            if (this.errorResilientMode == 0) {
                this.resetFrameContext = br.readNBit(2);
            }
            if (this.intraOnly == 1) {
                frame_sync_code(br);
                if (this.profile > 0) {
                    this.readColorConfig(br);
                } else {
                    this.colorSpace = 1;
                    this.subsamplingX = 1;
                    this.subsamplingY = 1;
                    this.bitDepth = 8;
                }
                this.refreshFrameFlags = br.readNBit(8);
                this.readFrameSize(br);
                this.readRenderSize(br);
            } else {
                int refreshFrameFlags = br.readNBit(8);
                for (int i = 0; i < 3; i++) {
                    this.refFrameIdx[i] = br.readNBit(3);
                    this.refFrameSignBias[1 + i] = br.read1Bit();
                }
                this.readFrameSizeWithRefs(br);
                this.allowHighPrecisionMv = br.read1Bit();
                this.readInterpolationFilter(br);
            }
        }
        this.refreshFrameContext = 0;
        if (this.errorResilientMode == 0) {
            this.refreshFrameContext = br.read1Bit();
            this.frameParallelDecodingMode = br.read1Bit();
        }
        this.frameContextIdx = br.readNBit(2);
        this.readLoopFilterParams(br);
        this.readQuantizationParams(br);
        this.readSegmentationParams(br);
        this.readTileInfo(br);
        int headerSizeInBytes = br.readNBit(16);
        br.terminate();
        return headerSizeInBytes;
    }

    int calc_min_log2_tile_cols() {
        int sb64Cols = this.frameWidth + 63 >> 6;
        int minLog2 = 0;
        while (64 << minLog2 < sb64Cols) {
            minLog2++;
        }
        return minLog2;
    }

    int calc_max_log2_tile_cols() {
        int sb64Cols = this.frameWidth + 63 >> 6;
        int maxLog2 = 1;
        while (sb64Cols >> maxLog2 >= 4) {
            maxLog2++;
        }
        return maxLog2 - 1;
    }

    private void readTileInfo(BitReader br) {
        int minLog2TileCols = this.calc_min_log2_tile_cols();
        int maxLog2TileCols = this.calc_max_log2_tile_cols();
        for (this.tileColsLog2 = minLog2TileCols; this.tileColsLog2 < maxLog2TileCols; this.tileColsLog2++) {
            int increment_tile_cols_log2 = br.read1Bit();
            if (increment_tile_cols_log2 != 1) {
                break;
            }
        }
        this.tileRowsLog2 = br.read1Bit();
        if (this.tileRowsLog2 == 1) {
            int increment_tile_rows_log2 = br.read1Bit();
            this.tileRowsLog2 += increment_tile_rows_log2;
        }
    }

    private static int readProb(BitReader br) {
        return br.read1Bit() == 1 ? br.readNBit(8) : 255;
    }

    private void readSegmentationParams(BitReader br) {
        this.segmentationEnabled = br.read1Bit() == 1;
        if (this.segmentationEnabled) {
            if (br.read1Bit() == 1) {
                for (int i = 0; i < 7; i++) {
                    this.segmentationTreeProbs[i] = readProb(br);
                }
                int segmentationTemporalUpdate = br.read1Bit();
                for (int i = 0; i < 3; i++) {
                    this.segmentationPredProbs[i] = segmentationTemporalUpdate == 1 ? readProb(br) : 255;
                }
            }
            if (br.read1Bit() == 1) {
                int segmentationAbsOrDeltaUpdate = br.read1Bit();
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (br.read1Bit() == 1) {
                            this.featureEnabled[i][j] = 1;
                            int bits_to_read = Consts.SEGMENTATION_FEATURE_BITS[j];
                            int value = br.readNBit(bits_to_read);
                            if (Consts.SEGMENTATION_FEATURE_SIGNED[j] == 1 && br.read1Bit() == 1) {
                                value *= -1;
                            }
                            this.featureData[i][j] = value;
                        }
                    }
                }
            }
        }
    }

    private static int readDeltaQ(BitReader br) {
        int delta_coded = br.read1Bit();
        return delta_coded == 1 ? br.readNBitSigned(4) : 0;
    }

    private void readQuantizationParams(BitReader br) {
        this.baseQIdx = br.readNBit(8);
        this.deltaQYDc = readDeltaQ(br);
        this.deltaQUvDc = readDeltaQ(br);
        this.deltaQUvAc = readDeltaQ(br);
        this.lossless = this.baseQIdx == 0 && this.deltaQYDc == 0 && this.deltaQUvDc == 0 && this.deltaQUvAc == 0;
    }

    private void readLoopFilterParams(BitReader br) {
        this.filterLevel = br.readNBit(6);
        this.sharpnessLevel = br.readNBit(3);
        int modeRefDeltaEnabled = br.read1Bit();
        if (modeRefDeltaEnabled == 1) {
            int modeRefDeltaUpdate = br.read1Bit();
            if (modeRefDeltaUpdate == 1) {
                for (int i = 0; i < 4; i++) {
                    if (br.read1Bit() == 1) {
                        this.loopFilterRefDeltas[i] = br.readNBitSigned(6);
                    }
                }
                for (int ix = 0; ix < 2; ix++) {
                    if (br.read1Bit() == 1) {
                        this.loopFilterModeDeltas[ix] = br.readNBitSigned(6);
                    }
                }
            }
        }
    }

    private void readInterpolationFilter(BitReader br) {
        this.interpFilter = 3;
        if (br.read1Bit() == 0) {
            this.interpFilter = Consts.LITERAL_TO_FILTER_TYPE[br.readNBit(2)];
        }
    }

    private void readFrameSizeWithRefs(BitReader br) {
        int i;
        for (i = 0; i < 3; i++) {
            if (br.read1Bit() == 1) {
                this.frameWidth = this.refFrameWidth[this.refFrameIdx[i]];
                this.frameHeight = this.refFrameHeight[this.refFrameIdx[i]];
                break;
            }
        }
        if (i == 3) {
            this.readFrameSize(br);
        }
        this.readRenderSize(br);
    }

    private void readRenderSize(BitReader br) {
        if (br.read1Bit() == 1) {
            this.renderWidth = br.readNBit(16) + 1;
            this.renderHeight = br.readNBit(16) + 1;
        } else {
            this.renderWidth = this.frameWidth;
            this.renderHeight = this.frameHeight;
        }
    }

    private void readFrameSize(BitReader br) {
        this.frameWidth = br.readNBit(16) + 1;
        this.frameHeight = br.readNBit(16) + 1;
    }

    private void readColorConfig(BitReader br) {
        if (this.profile >= 2) {
            int ten_or_twelve_bit = br.read1Bit();
            this.bitDepth = ten_or_twelve_bit == 1 ? 12 : 10;
        } else {
            this.bitDepth = 8;
        }
        int colorSpace = br.readNBit(3);
        if (colorSpace != 7) {
            this.colorRange = br.read1Bit();
            if (this.profile != 1 && this.profile != 3) {
                this.subsamplingX = 1;
                this.subsamplingY = 1;
            } else {
                this.subsamplingX = br.read1Bit();
                this.subsamplingY = br.read1Bit();
                int var3 = br.read1Bit();
            }
        } else {
            this.colorRange = 1;
            if (this.profile == 1 || this.profile == 3) {
                this.subsamplingX = 0;
                this.subsamplingY = 0;
                int var5 = br.read1Bit();
            }
        }
    }

    private static void frame_sync_code(BitReader br) {
        int code = br.readNBit(24);
    }

    protected void readCompressedHeader(ByteBuffer compressedHeader) {
        VPXBooleanDecoder boolDec = new VPXBooleanDecoder(compressedHeader, 0);
        if (boolDec.readBitEq() != 0) {
            throw new RuntimeException("Invalid marker bit");
        } else {
            this.readTxMode(boolDec);
            if (this.txMode == 4) {
                this.readTxModeProbs(boolDec);
            }
            this.readCoefProbs(boolDec);
            this.readSkipProb(boolDec);
            if (this.frameIsIntra == 0) {
                this.readInterModeProbs(boolDec);
                if (this.interpFilter == 3) {
                    this.readInterpFilterProbs(boolDec);
                }
                this.readIsInterProbs(boolDec);
                this.frameReferenceMode(boolDec);
                this.frameReferenceModeProbs(boolDec);
                this.readYModeProbs(boolDec);
                this.readPartitionProbs(boolDec);
                this.mvProbs(boolDec);
            }
        }
    }

    private void readTxMode(VPXBooleanDecoder boolDec) {
        if (this.lossless) {
            this.txMode = 0;
        } else {
            this.txMode = boolDec.decodeInt(2);
            if (this.txMode == 3) {
                this.txMode = this.txMode + boolDec.decodeInt(1);
            }
        }
    }

    private void readTxModeProbs(VPXBooleanDecoder boolDec) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 1; j++) {
                this.tx8x8Probs[i][j] = this.diffUpdateProb(boolDec, this.tx8x8Probs[i][j]);
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                this.tx16x16Probs[i][j] = this.diffUpdateProb(boolDec, this.tx16x16Probs[i][j]);
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                this.tx32x32Probs[i][j] = this.diffUpdateProb(boolDec, this.tx32x32Probs[i][j]);
            }
        }
    }

    private int diffUpdateProb(VPXBooleanDecoder boolDec, int prob) {
        int update_prob = boolDec.readBit(252);
        if (update_prob == 1) {
            int deltaProb = this.decodeTermSubexp(boolDec);
            prob = this.invRemapProb(deltaProb, prob);
        }
        return prob;
    }

    private int decodeTermSubexp(VPXBooleanDecoder boolDec) {
        int bit = boolDec.readBitEq();
        if (bit == 0) {
            return boolDec.decodeInt(4);
        } else {
            bit = boolDec.readBitEq();
            if (bit == 0) {
                return boolDec.decodeInt(4) + 16;
            } else {
                bit = boolDec.readBitEq();
                if (bit == 0) {
                    return boolDec.decodeInt(5) + 32;
                } else {
                    int v = boolDec.decodeInt(7);
                    if (v < 65) {
                        return v + 64;
                    } else {
                        bit = boolDec.readBitEq();
                        return (v << 1) - 1 + bit;
                    }
                }
            }
        }
    }

    private int invRemapProb(int deltaProb, int prob) {
        int v = Consts.INV_REMAP_TABLE[deltaProb];
        int m = prob - 1;
        if (m << 1 <= 255) {
            m = 1 + this.invRecenterNonneg(v, m);
        } else {
            m = 255 - this.invRecenterNonneg(v, 254 - m);
        }
        return m;
    }

    private int invRecenterNonneg(int v, int m) {
        if (v > 2 * m) {
            return v;
        } else {
            return (v & 1) != 0 ? m - (v + 1 >> 1) : m + (v >> 1);
        }
    }

    private void readCoefProbs(VPXBooleanDecoder boolDec) {
        int maxTxSize = Consts.tx_mode_to_biggest_tx_size[this.txMode];
        for (int txSz = 0; txSz <= maxTxSize; txSz++) {
            int update_probs = boolDec.readBitEq();
            if (update_probs == 1) {
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        for (int k = 0; k < 6; k++) {
                            int maxL = k == 0 ? 3 : 6;
                            for (int l = 0; l < maxL; l++) {
                                for (int m = 0; m < 3; m++) {
                                    this.coefProbs[txSz][i][j][k][l][m] = this.diffUpdateProb(boolDec, this.coefProbs[txSz][i][j][k][l][m]);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void readSkipProb(VPXBooleanDecoder boolDec) {
        for (int i = 0; i < 3; i++) {
            this.skipProbs[i] = this.diffUpdateProb(boolDec, this.skipProbs[i]);
        }
    }

    private void readInterModeProbs(VPXBooleanDecoder boolDec) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 3; j++) {
                this.interModeProbs[i][j] = this.diffUpdateProb(boolDec, this.interModeProbs[i][j]);
            }
        }
    }

    private void readInterpFilterProbs(VPXBooleanDecoder boolDec) {
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 2; i++) {
                this.interpFilterProbs[j][i] = this.diffUpdateProb(boolDec, this.interpFilterProbs[j][i]);
            }
        }
    }

    private void readIsInterProbs(VPXBooleanDecoder boolDec) {
        for (int i = 0; i < 4; i++) {
            this.isInterProbs[i] = this.diffUpdateProb(boolDec, this.isInterProbs[i]);
        }
    }

    private void frameReferenceMode(VPXBooleanDecoder boolDec) {
        int compoundReferenceAllowed = 0;
        for (int i = 1; i < 3; i++) {
            if (this.refFrameSignBias[i] != this.refFrameSignBias[0]) {
                compoundReferenceAllowed = 1;
            }
        }
        if (compoundReferenceAllowed == 1) {
            int non_single_reference = boolDec.readBitEq();
            if (non_single_reference == 0) {
                this.refMode = 0;
            } else {
                int reference_select = boolDec.readBitEq();
                if (reference_select == 0) {
                    this.refMode = 1;
                } else {
                    this.refMode = 2;
                }
                this.setupCompoundReferenceMode();
            }
        } else {
            this.refMode = 0;
        }
    }

    private void frameReferenceModeProbs(VPXBooleanDecoder boolDec) {
        if (this.refMode == 2) {
            for (int i = 0; i < 5; i++) {
                this.compModeProbs[i] = this.diffUpdateProb(boolDec, this.compModeProbs[i]);
            }
        }
        if (this.refMode != 1) {
            for (int i = 0; i < 5; i++) {
                this.singleRefProbs[i][0] = this.diffUpdateProb(boolDec, this.singleRefProbs[i][0]);
                this.singleRefProbs[i][1] = this.diffUpdateProb(boolDec, this.singleRefProbs[i][1]);
            }
        }
        if (this.refMode != 0) {
            for (int i = 0; i < 5; i++) {
                this.compRefProbs[i] = this.diffUpdateProb(boolDec, this.compRefProbs[i]);
            }
        }
    }

    private void readYModeProbs(VPXBooleanDecoder boolDec) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 9; j++) {
                this.yModeProbs[i][j] = this.diffUpdateProb(boolDec, this.yModeProbs[i][j]);
            }
        }
    }

    private void readPartitionProbs(VPXBooleanDecoder boolDec) {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 3; j++) {
                this.partitionProbs[i][j] = this.diffUpdateProb(boolDec, this.partitionProbs[i][j]);
            }
        }
    }

    private void mvProbs(VPXBooleanDecoder boolDec) {
        for (int j = 0; j < 3; j++) {
            this.mvJointProbs[j] = this.updateMvProb(boolDec, this.mvJointProbs[j]);
        }
        for (int i = 0; i < 2; i++) {
            this.mvSignProbs[i] = this.updateMvProb(boolDec, this.mvSignProbs[i]);
            for (int j = 0; j < 10; j++) {
                this.mvClassProbs[i][j] = this.updateMvProb(boolDec, this.mvClassProbs[i][j]);
            }
            this.mvClass0BitProbs[i] = this.updateMvProb(boolDec, this.mvClass0BitProbs[i]);
            for (int j = 0; j < 10; j++) {
                this.mvBitsProbs[i][j] = this.updateMvProb(boolDec, this.mvBitsProbs[i][j]);
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 3; k++) {
                    this.mvClass0FrProbs[i][j][k] = this.updateMvProb(boolDec, this.mvClass0FrProbs[i][j][k]);
                }
            }
            for (int k = 0; k < 3; k++) {
                this.mvFrProbs[i][k] = this.updateMvProb(boolDec, this.mvFrProbs[i][k]);
            }
        }
        if (this.allowHighPrecisionMv == 1) {
            for (int i = 0; i < 2; i++) {
                this.mvClass0HpProb[i] = this.updateMvProb(boolDec, this.mvClass0HpProb[i]);
                this.mvHpProbs[i] = this.updateMvProb(boolDec, this.mvHpProbs[i]);
            }
        }
    }

    private int updateMvProb(VPXBooleanDecoder boolDec, int prob) {
        int update_mv_prob = boolDec.readBit(252);
        if (update_mv_prob == 1) {
            int mv_prob = boolDec.decodeInt(7);
            prob = mv_prob << 1 | 1;
        }
        return prob;
    }

    private void setupCompoundReferenceMode() {
        if (this.refFrameSignBias[1] == this.refFrameSignBias[3]) {
            this.compFixedRef = 2;
            this.compVarRef0 = 1;
            this.compVarRef1 = 3;
        } else if (this.refFrameSignBias[1] == this.refFrameSignBias[2]) {
            this.compFixedRef = 3;
            this.compVarRef0 = 1;
            this.compVarRef1 = 2;
        } else {
            this.compFixedRef = 1;
            this.compVarRef0 = 3;
            this.compVarRef1 = 2;
        }
    }

    public int getFrameContextIdx() {
        return this.frameContextIdx;
    }

    public int getTileColsLog2() {
        return this.tileColsLog2;
    }

    public int getTileRowsLog2() {
        return this.tileRowsLog2;
    }

    public int getFrameWidth() {
        return this.frameWidth;
    }

    public int getFrameHeight() {
        return this.frameHeight;
    }

    public int getBaseQIdx() {
        return this.baseQIdx;
    }

    public int getDeltaQYDc() {
        return this.deltaQYDc;
    }

    public int getDeltaQUvDc() {
        return this.deltaQUvDc;
    }

    public int getDeltaQUvAc() {
        return this.deltaQUvAc;
    }

    public int getFilterLevel() {
        return this.filterLevel;
    }

    public int getSharpnessLevel() {
        return this.sharpnessLevel;
    }

    public int[] getSkipProbs() {
        return this.skipProbs;
    }

    public int[][] getTx8x8Probs() {
        return this.tx8x8Probs;
    }

    public int[][] getTx16x16Probs() {
        return this.tx16x16Probs;
    }

    public int[][] getTx32x32Probs() {
        return this.tx32x32Probs;
    }

    public int[][][][][][] getCoefProbs() {
        return this.coefProbs;
    }

    public int[] getMvJointProbs() {
        return this.mvJointProbs;
    }

    public int[] getMvSignProb() {
        return this.mvSignProbs;
    }

    public int[][] getMvClassProbs() {
        return this.mvClassProbs;
    }

    public int[] getMvClass0BitProbs() {
        return this.mvClass0BitProbs;
    }

    public int[][] getMvBitsProb() {
        return this.mvBitsProbs;
    }

    public int[][][] getMvClass0FrProbs() {
        return this.mvClass0FrProbs;
    }

    public int[][] getMvFrProbs() {
        return this.mvFrProbs;
    }

    public int[] getMvClass0HpProbs() {
        return this.mvClass0HpProb;
    }

    public int[] getMvHpProbs() {
        return this.mvHpProbs;
    }

    public int[][] getInterModeProbs() {
        return this.interModeProbs;
    }

    public int[][] getInterpFilterProbs() {
        return this.interpFilterProbs;
    }

    public int[] getIsInterProbs() {
        return this.isInterProbs;
    }

    public int[][] getSingleRefProbs() {
        return this.singleRefProbs;
    }

    public int[][] getYModeProbs() {
        return this.yModeProbs;
    }

    public int[][] getPartitionProbs() {
        return this.partitionProbs;
    }

    public int[][] getUvModeProbs() {
        return this.uvModeProbs;
    }

    public int[] getCompRefProbs() {
        return this.compRefProbs;
    }

    public int[][][] getKfYModeProbs() {
        return kfYmodeProbs;
    }

    public int[][] getKfUVModeProbs() {
        return kfUvModeProbs;
    }

    public int[] getSegmentationTreeProbs() {
        return this.segmentationTreeProbs;
    }

    public int[] getSegmentationPredProbs() {
        return this.segmentationPredProbs;
    }

    public int[] getCompModeProb() {
        return this.compModeProbs;
    }

    public int[] getAboveRefs() {
        return this.aboveRefs;
    }

    public int[] getLeftRefs() {
        return this.leftRefs;
    }

    public int getMiTileStartCol() {
        return this.miTileStartCol;
    }

    public int[] getAboveTxSizes() {
        return this.aboveTxSizes;
    }

    public int[] getLeftTxSizes() {
        return this.leftTxSizes;
    }
}