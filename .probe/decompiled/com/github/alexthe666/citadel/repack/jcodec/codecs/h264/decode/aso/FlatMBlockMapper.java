package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.aso;

public class FlatMBlockMapper implements Mapper {

    private int frameWidthInMbs;

    private int firstMBAddr;

    public FlatMBlockMapper(int frameWidthInMbs, int firstMBAddr) {
        this.frameWidthInMbs = frameWidthInMbs;
        this.firstMBAddr = firstMBAddr;
    }

    @Override
    public boolean leftAvailable(int index) {
        int mbAddr = index + this.firstMBAddr;
        boolean atTheBorder = mbAddr % this.frameWidthInMbs == 0;
        return !atTheBorder && mbAddr > this.firstMBAddr;
    }

    @Override
    public boolean topAvailable(int index) {
        int mbAddr = index + this.firstMBAddr;
        return mbAddr - this.frameWidthInMbs >= this.firstMBAddr;
    }

    @Override
    public int getAddress(int index) {
        return this.firstMBAddr + index;
    }

    @Override
    public int getMbX(int index) {
        return this.getAddress(index) % this.frameWidthInMbs;
    }

    @Override
    public int getMbY(int index) {
        return this.getAddress(index) / this.frameWidthInMbs;
    }

    @Override
    public boolean topRightAvailable(int index) {
        int mbAddr = index + this.firstMBAddr;
        boolean atTheBorder = (mbAddr + 1) % this.frameWidthInMbs == 0;
        return !atTheBorder && mbAddr - this.frameWidthInMbs + 1 >= this.firstMBAddr;
    }

    @Override
    public boolean topLeftAvailable(int index) {
        int mbAddr = index + this.firstMBAddr;
        boolean atTheBorder = mbAddr % this.frameWidthInMbs == 0;
        return !atTheBorder && mbAddr - this.frameWidthInMbs - 1 >= this.firstMBAddr;
    }
}