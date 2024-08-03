package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.aso;

public class PrebuiltMBlockMapper implements Mapper {

    private MBToSliceGroupMap map;

    private int firstMBInSlice;

    private int groupId;

    private int picWidthInMbs;

    private int indexOfFirstMb;

    public PrebuiltMBlockMapper(MBToSliceGroupMap map, int firstMBInSlice, int picWidthInMbs) {
        this.map = map;
        this.firstMBInSlice = firstMBInSlice;
        this.groupId = map.getGroups()[firstMBInSlice];
        this.picWidthInMbs = picWidthInMbs;
        this.indexOfFirstMb = map.getIndices()[firstMBInSlice];
    }

    @Override
    public int getAddress(int mbIndex) {
        return this.map.getInverse()[this.groupId][mbIndex + this.indexOfFirstMb];
    }

    @Override
    public boolean leftAvailable(int mbIndex) {
        int mbAddr = this.map.getInverse()[this.groupId][mbIndex + this.indexOfFirstMb];
        int leftMBAddr = mbAddr - 1;
        return leftMBAddr >= this.firstMBInSlice && mbAddr % this.picWidthInMbs != 0 && this.map.getGroups()[leftMBAddr] == this.groupId;
    }

    @Override
    public boolean topAvailable(int mbIndex) {
        int mbAddr = this.map.getInverse()[this.groupId][mbIndex + this.indexOfFirstMb];
        int topMBAddr = mbAddr - this.picWidthInMbs;
        return topMBAddr >= this.firstMBInSlice && this.map.getGroups()[topMBAddr] == this.groupId;
    }

    @Override
    public int getMbX(int index) {
        return this.getAddress(index) % this.picWidthInMbs;
    }

    @Override
    public int getMbY(int index) {
        return this.getAddress(index) / this.picWidthInMbs;
    }

    @Override
    public boolean topRightAvailable(int mbIndex) {
        int mbAddr = this.map.getInverse()[this.groupId][mbIndex + this.indexOfFirstMb];
        int topRMBAddr = mbAddr - this.picWidthInMbs + 1;
        return topRMBAddr >= this.firstMBInSlice && (mbAddr + 1) % this.picWidthInMbs != 0 && this.map.getGroups()[topRMBAddr] == this.groupId;
    }

    @Override
    public boolean topLeftAvailable(int mbIndex) {
        int mbAddr = this.map.getInverse()[this.groupId][mbIndex + this.indexOfFirstMb];
        int topLMBAddr = mbAddr - this.picWidthInMbs - 1;
        return topLMBAddr >= this.firstMBInSlice && mbAddr % this.picWidthInMbs != 0 && this.map.getGroups()[topLMBAddr] == this.groupId;
    }
}