package snownee.kiwi.schedule.impl;

import java.util.function.IntPredicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.TickEvent;
import snownee.kiwi.Kiwi;
import snownee.kiwi.schedule.Task;

public class SimpleLevelTask extends Task<LevelTicker> implements INBTSerializable<CompoundTag> {

    protected int tick = 0;

    protected ResourceKey<Level> dimension;

    protected TickEvent.Phase phase;

    protected IntPredicate function;

    public SimpleLevelTask() {
    }

    public SimpleLevelTask(Level world, TickEvent.Phase phase, IntPredicate function) {
        this(world.dimension(), phase, function);
    }

    public SimpleLevelTask(ResourceKey<Level> dimensionType, TickEvent.Phase phase, IntPredicate function) {
        this.dimension = dimensionType;
        this.phase = phase;
        this.function = function;
    }

    public boolean tick(LevelTicker ticker) {
        return this.function.test(++this.tick);
    }

    public LevelTicker ticker() {
        return LevelTicker.get(this.dimension, this.phase);
    }

    @Override
    public boolean shouldSave() {
        return this.getClass() != SimpleLevelTask.class;
    }

    public CompoundTag serializeNBT() {
        CompoundTag data = new CompoundTag();
        data.putInt("tick", this.tick);
        Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, this.dimension).resultOrPartial(Kiwi.LOGGER::error).ifPresent(nbt -> data.put("world", nbt));
        data.putBoolean("start", this.phase == TickEvent.Phase.START);
        return data;
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.dimension = (ResourceKey<Level>) Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, nbt.get("world")).resultOrPartial(Kiwi.LOGGER::error).orElse(Level.OVERWORLD);
        this.tick = nbt.getInt("tick");
        this.phase = nbt.getBoolean("start") ? TickEvent.Phase.START : TickEvent.Phase.END;
    }
}