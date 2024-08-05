package net.minecraft.world.inventory;

public abstract class DataSlot {

    private int prevValue;

    public static DataSlot forContainer(final ContainerData containerData0, final int int1) {
        return new DataSlot() {

            @Override
            public int get() {
                return containerData0.get(int1);
            }

            @Override
            public void set(int p_39416_) {
                containerData0.set(int1, p_39416_);
            }
        };
    }

    public static DataSlot shared(final int[] int0, final int int1) {
        return new DataSlot() {

            @Override
            public int get() {
                return int0[int1];
            }

            @Override
            public void set(int p_39424_) {
                int0[int1] = p_39424_;
            }
        };
    }

    public static DataSlot standalone() {
        return new DataSlot() {

            private int value;

            @Override
            public int get() {
                return this.value;
            }

            @Override
            public void set(int p_39429_) {
                this.value = p_39429_;
            }
        };
    }

    public abstract int get();

    public abstract void set(int var1);

    public boolean checkAndClearUpdateFlag() {
        int $$0 = this.get();
        boolean $$1 = $$0 != this.prevValue;
        this.prevValue = $$0;
        return $$1;
    }
}