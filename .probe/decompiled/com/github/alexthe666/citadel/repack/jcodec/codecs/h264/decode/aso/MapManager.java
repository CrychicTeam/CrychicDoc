package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.aso;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.PictureParameterSet;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SeqParameterSet;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceHeader;

public class MapManager {

    private SeqParameterSet sps;

    private PictureParameterSet pps;

    private MBToSliceGroupMap mbToSliceGroupMap;

    private int prevSliceGroupChangeCycle;

    public MapManager(SeqParameterSet sps, PictureParameterSet pps) {
        this.sps = sps;
        this.pps = pps;
        this.mbToSliceGroupMap = this.buildMap(sps, pps);
    }

    private MBToSliceGroupMap buildMap(SeqParameterSet sps, PictureParameterSet pps) {
        int numGroups = pps.numSliceGroupsMinus1 + 1;
        if (numGroups <= 1) {
            return null;
        } else {
            int picWidthInMbs = sps.picWidthInMbsMinus1 + 1;
            int picHeightInMbs = SeqParameterSet.getPicHeightInMbs(sps);
            int[] map;
            if (pps.sliceGroupMapType == 0) {
                int[] runLength = new int[numGroups];
                for (int i = 0; i < numGroups; i++) {
                    runLength[i] = pps.runLengthMinus1[i] + 1;
                }
                map = SliceGroupMapBuilder.buildInterleavedMap(picWidthInMbs, picHeightInMbs, runLength);
            } else if (pps.sliceGroupMapType == 1) {
                map = SliceGroupMapBuilder.buildDispersedMap(picWidthInMbs, picHeightInMbs, numGroups);
            } else if (pps.sliceGroupMapType == 2) {
                map = SliceGroupMapBuilder.buildForegroundMap(picWidthInMbs, picHeightInMbs, numGroups, pps.topLeft, pps.bottomRight);
            } else {
                if (pps.sliceGroupMapType >= 3 && pps.sliceGroupMapType <= 5) {
                    return null;
                }
                if (pps.sliceGroupMapType != 6) {
                    throw new RuntimeException("Unsupported slice group map type");
                }
                map = pps.sliceGroupId;
            }
            return this.buildMapIndices(map, numGroups);
        }
    }

    private MBToSliceGroupMap buildMapIndices(int[] map, int numGroups) {
        int[] ind = new int[numGroups];
        int[] indices = new int[map.length];
        for (int i = 0; i < map.length; i++) {
            indices[i] = (int) (ind[map[i]]++);
        }
        int[][] inverse = new int[numGroups][];
        for (int i = 0; i < numGroups; i++) {
            inverse[i] = new int[ind[i]];
        }
        ind = new int[numGroups];
        int i = 0;
        while (i < map.length) {
            int sliceGroup = map[i];
            inverse[sliceGroup][ind[sliceGroup]++] = i++;
        }
        return new MBToSliceGroupMap(map, indices, inverse);
    }

    private void updateMap(SliceHeader sh) {
        int mapType = this.pps.sliceGroupMapType;
        int numGroups = this.pps.numSliceGroupsMinus1 + 1;
        if (numGroups > 1 && mapType >= 3 && mapType <= 5 && (sh.sliceGroupChangeCycle != this.prevSliceGroupChangeCycle || this.mbToSliceGroupMap == null)) {
            this.prevSliceGroupChangeCycle = sh.sliceGroupChangeCycle;
            int picWidthInMbs = this.sps.picWidthInMbsMinus1 + 1;
            int picHeightInMbs = SeqParameterSet.getPicHeightInMbs(this.sps);
            int picSizeInMapUnits = picWidthInMbs * picHeightInMbs;
            int mapUnitsInSliceGroup0 = sh.sliceGroupChangeCycle * (this.pps.sliceGroupChangeRateMinus1 + 1);
            mapUnitsInSliceGroup0 = mapUnitsInSliceGroup0 > picSizeInMapUnits ? picSizeInMapUnits : mapUnitsInSliceGroup0;
            int sizeOfUpperLeftGroup = this.pps.sliceGroupChangeDirectionFlag ? picSizeInMapUnits - mapUnitsInSliceGroup0 : mapUnitsInSliceGroup0;
            int[] map;
            if (mapType == 3) {
                map = SliceGroupMapBuilder.buildBoxOutMap(picWidthInMbs, picHeightInMbs, this.pps.sliceGroupChangeDirectionFlag, mapUnitsInSliceGroup0);
            } else if (mapType == 4) {
                map = SliceGroupMapBuilder.buildRasterScanMap(picWidthInMbs, picHeightInMbs, sizeOfUpperLeftGroup, this.pps.sliceGroupChangeDirectionFlag);
            } else {
                map = SliceGroupMapBuilder.buildWipeMap(picWidthInMbs, picHeightInMbs, sizeOfUpperLeftGroup, this.pps.sliceGroupChangeDirectionFlag);
            }
            this.mbToSliceGroupMap = this.buildMapIndices(map, numGroups);
        }
    }

    public Mapper getMapper(SliceHeader sh) {
        this.updateMap(sh);
        int firstMBInSlice = sh.firstMbInSlice;
        return (Mapper) (this.pps.numSliceGroupsMinus1 > 0 ? new PrebuiltMBlockMapper(this.mbToSliceGroupMap, firstMBInSlice, this.sps.picWidthInMbsMinus1 + 1) : new FlatMBlockMapper(this.sps.picWidthInMbsMinus1 + 1, firstMBInSlice));
    }
}