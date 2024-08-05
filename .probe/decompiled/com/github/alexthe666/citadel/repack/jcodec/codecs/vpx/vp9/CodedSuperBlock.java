package com.github.alexthe666.citadel.repack.jcodec.codecs.vpx.vp9;

import com.github.alexthe666.citadel.repack.jcodec.codecs.vpx.VPXBooleanDecoder;
import java.util.ArrayList;
import java.util.List;

public class CodedSuperBlock {

    private CodedBlock[] codedBlocks;

    public CodedSuperBlock(CodedBlock[] codedBlocks) {
        this.codedBlocks = codedBlocks;
    }

    protected CodedSuperBlock() {
    }

    public CodedBlock[] getCodedBlocks() {
        return this.codedBlocks;
    }

    public static CodedSuperBlock read(int miCol, int miRow, VPXBooleanDecoder decoder, DecodingContext c) {
        List<CodedBlock> blocks = new ArrayList();
        CodedSuperBlock result = new CodedSuperBlock();
        result.readSubPartition(miCol, miRow, 3, decoder, c, blocks);
        result.codedBlocks = (CodedBlock[]) blocks.toArray(CodedBlock.EMPTY_ARR);
        return result;
    }

    protected CodedBlock readBlock(int miCol, int miRow, int blSz, VPXBooleanDecoder decoder, DecodingContext c) {
        return CodedBlock.read(miCol, miRow, blSz, decoder, c);
    }

    protected void readSubPartition(int miCol, int miRow, int logBlkSize, VPXBooleanDecoder decoder, DecodingContext c, List<CodedBlock> blocks) {
        int part = readPartition(miCol, miRow, logBlkSize, decoder, c);
        int nextBlkSize = 1 << logBlkSize >> 1;
        if (logBlkSize > 0) {
            if (part == 0) {
                CodedBlock blk = this.readBlock(miCol, miRow, Consts.blSizeLookup[1 + logBlkSize][1 + logBlkSize], decoder, c);
                blocks.add(blk);
                saveAboveSizes(miCol, 1 + logBlkSize, c);
                saveLeftSizes(miRow, 1 + logBlkSize, c);
            } else if (part == 1) {
                CodedBlock blk = this.readBlock(miCol, miRow, Consts.blSizeLookup[1 + logBlkSize][logBlkSize], decoder, c);
                blocks.add(blk);
                saveAboveSizes(miCol, 1 + logBlkSize, c);
                saveLeftSizes(miRow, logBlkSize, c);
                if (miRow + nextBlkSize < c.getMiTileHeight()) {
                    blk = this.readBlock(miCol, miRow + nextBlkSize, Consts.blSizeLookup[1 + logBlkSize][logBlkSize], decoder, c);
                    blocks.add(blk);
                    saveLeftSizes(miRow + nextBlkSize, logBlkSize, c);
                }
            } else if (part == 2) {
                CodedBlock blk = this.readBlock(miCol, miRow, Consts.blSizeLookup[logBlkSize][1 + logBlkSize], decoder, c);
                blocks.add(blk);
                saveLeftSizes(miRow, 1 + logBlkSize, c);
                saveAboveSizes(miCol, logBlkSize, c);
                if (miCol + nextBlkSize < c.getMiTileWidth()) {
                    blk = this.readBlock(miCol + nextBlkSize, miRow, Consts.blSizeLookup[logBlkSize][1 + logBlkSize], decoder, c);
                    blocks.add(blk);
                    saveAboveSizes(miCol + nextBlkSize, logBlkSize, c);
                }
            } else {
                this.readSubPartition(miCol, miRow, logBlkSize - 1, decoder, c, blocks);
                if (miCol + nextBlkSize < c.getMiTileWidth()) {
                    this.readSubPartition(miCol + nextBlkSize, miRow, logBlkSize - 1, decoder, c, blocks);
                }
                if (miRow + nextBlkSize < c.getMiTileHeight()) {
                    this.readSubPartition(miCol, miRow + nextBlkSize, logBlkSize - 1, decoder, c, blocks);
                }
                if (miCol + nextBlkSize < c.getMiTileWidth() && miRow + nextBlkSize < c.getMiTileHeight()) {
                    this.readSubPartition(miCol + nextBlkSize, miRow + nextBlkSize, logBlkSize - 1, decoder, c, blocks);
                }
            }
        } else {
            int subBlSz = Consts.sub8x8PartitiontoBlockType[part];
            CodedBlock blk = this.readBlock(miCol, miRow, subBlSz, decoder, c);
            blocks.add(blk);
            saveAboveSizes(miCol, 1 + logBlkSize - (subBlSz != 0 && subBlSz != 1 ? 0 : 1), c);
            saveLeftSizes(miRow, 1 + logBlkSize - (subBlSz != 0 && subBlSz != 2 ? 0 : 1), c);
        }
    }

    private static void saveLeftSizes(int miRow, int blkSize4x4, DecodingContext c) {
        int blkSize8x8 = blkSize4x4 == 0 ? 0 : blkSize4x4 - 1;
        int miBlkSize = 1 << blkSize8x8;
        int[] leftSizes = c.getLeftPartitionSizes();
        for (int i = 0; i < miBlkSize; i++) {
            leftSizes[miRow % 8 + i] = blkSize4x4;
        }
    }

    private static void saveAboveSizes(int miCol, int blkSize4x4, DecodingContext c) {
        int blkSize8x8 = blkSize4x4 == 0 ? 0 : blkSize4x4 - 1;
        int miBlkSize = 1 << blkSize8x8;
        int[] aboveSizes = c.getAbovePartitionSizes();
        for (int i = 0; i < miBlkSize; i++) {
            aboveSizes[miCol + i] = blkSize4x4;
        }
    }

    protected static int readPartition(int miCol, int miRow, int blkSize, VPXBooleanDecoder decoder, DecodingContext c) {
        int ctx = calcPartitionContext(miCol, miRow, blkSize, c);
        int[] probs = c.getPartitionProbs()[ctx];
        int halfBlk = 1 << blkSize >> 1;
        boolean rightEdge = miCol + halfBlk >= c.getMiTileWidth();
        boolean bottomEdge = miRow + halfBlk >= c.getMiTileHeight();
        if (rightEdge && bottomEdge) {
            return 3;
        } else if (rightEdge) {
            return decoder.readBit(probs[2]) == 1 ? 3 : 2;
        } else if (bottomEdge) {
            return decoder.readBit(probs[1]) == 1 ? 3 : 1;
        } else {
            return decoder.readTree(Consts.TREE_PARTITION, probs);
        }
    }

    private static int calcPartitionContext(int miCol, int miRow, int blkSize, DecodingContext c) {
        boolean left = false;
        boolean above = false;
        int[] aboveSizes = c.getAbovePartitionSizes();
        above = aboveSizes[miCol] <= blkSize;
        int[] leftSizes = c.getLeftPartitionSizes();
        left |= leftSizes[miRow % 8] <= blkSize;
        return blkSize * 4 + (left ? 2 : 0) + (above ? 1 : 0);
    }
}