package dev.shadowsoffire.placebo.menu;

import dev.shadowsoffire.placebo.cap.ModifiableEnergyStorage;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import net.minecraft.world.inventory.DataSlot;

public class SimpleDataSlots {

    protected List<DataSlot> slots = new ArrayList();

    public void addSlot(DataSlot slot) {
        this.slots.add(slot);
    }

    public void addData(IntSupplier getter, IntConsumer setter) {
        this.addSlot(new SimpleDataSlots.LambdaDataSlot(getter, setter));
    }

    public void addData(BooleanSupplier getter, BooleanConsumer setter) {
        this.addData((IntSupplier) (() -> getter.getAsBoolean() ? 1 : 0), (IntConsumer) (v -> setter.accept(v == 1)));
    }

    public List<DataSlot> getSlots() {
        return this.slots;
    }

    public void register(Consumer<DataSlot> consumer) {
        this.slots.forEach(consumer);
    }

    public void addEnergy(ModifiableEnergyStorage energy) {
        this.addSlot(new SimpleDataSlots.EnergyDataSlot(energy, false));
        this.addSlot(new SimpleDataSlots.EnergyDataSlot(energy, true));
    }

    public class EnergyDataSlot extends SimpleDataSlots.LambdaDataSlot {

        public EnergyDataSlot(ModifiableEnergyStorage energy, boolean upper) {
            super(() -> MenuUtil.split(energy.getEnergyStored(), upper), v -> energy.setEnergy(MenuUtil.merge(energy.getEnergyStored(), v, upper)));
        }
    }

    public interface IDataAutoRegister {

        void registerSlots(Consumer<DataSlot> var1);
    }

    public class LambdaDataSlot extends DataSlot {

        private final IntSupplier getter;

        private final IntConsumer setter;

        public LambdaDataSlot(IntSupplier getter, IntConsumer setter) {
            this.getter = getter;
            this.setter = setter;
        }

        @Override
        public int get() {
            return this.getter.getAsInt();
        }

        @Override
        public void set(int pValue) {
            this.setter.accept(pValue);
        }
    }
}