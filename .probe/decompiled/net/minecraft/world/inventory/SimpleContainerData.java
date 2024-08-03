package net.minecraft.world.inventory;

public class SimpleContainerData implements ContainerData {

    private final int[] ints;

    public SimpleContainerData(int int0) {
        this.ints = new int[int0];
    }

    @Override
    public int get(int int0) {
        return this.ints[int0];
    }

    @Override
    public void set(int int0, int int1) {
        this.ints[int0] = int1;
    }

    @Override
    public int getCount() {
        return this.ints.length;
    }
}