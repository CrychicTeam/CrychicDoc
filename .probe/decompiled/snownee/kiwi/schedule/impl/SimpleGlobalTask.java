package snownee.kiwi.schedule.impl;

import java.util.function.IntPredicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.LogicalSide;
import snownee.kiwi.schedule.Task;

public class SimpleGlobalTask extends Task<GlobalTicker> implements INBTSerializable<CompoundTag> {

    protected int tick = 0;

    protected LogicalSide side;

    protected TickEvent.Phase phase;

    protected IntPredicate function;

    public SimpleGlobalTask() {
    }

    public SimpleGlobalTask(LogicalSide side, TickEvent.Phase phase, IntPredicate function) {
        this.side = side;
        this.phase = phase;
        this.function = function;
    }

    public boolean tick(GlobalTicker ticker) {
        return this.function.test(++this.tick);
    }

    public GlobalTicker ticker() {
        return GlobalTicker.get(this.side, this.phase);
    }

    @Override
    public boolean shouldSave() {
        return this.getClass() != SimpleGlobalTask.class;
    }

    public CompoundTag serializeNBT() {
        CompoundTag data = new CompoundTag();
        data.putInt("tick", this.tick);
        data.putBoolean("client", this.side.isClient());
        data.putBoolean("start", this.phase == TickEvent.Phase.START);
        return data;
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.tick = nbt.getInt("tick");
        this.side = nbt.getBoolean("client") ? LogicalSide.CLIENT : LogicalSide.SERVER;
        this.phase = nbt.getBoolean("start") ? TickEvent.Phase.START : TickEvent.Phase.END;
    }
}