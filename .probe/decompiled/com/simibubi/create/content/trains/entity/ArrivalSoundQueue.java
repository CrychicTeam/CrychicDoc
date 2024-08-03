package com.simibubi.create.content.trains.entity;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class ArrivalSoundQueue {

    public int offset;

    int min;

    int max;

    Multimap<Integer, BlockPos> sources = Multimaps.newMultimap(new HashMap(), ArrayList::new);

    public ArrivalSoundQueue() {
        this.min = Integer.MAX_VALUE;
        this.max = Integer.MIN_VALUE;
    }

    @Nullable
    public Integer firstTick() {
        return this.sources.isEmpty() ? null : this.min + this.offset;
    }

    @Nullable
    public Integer lastTick() {
        return this.sources.isEmpty() ? null : this.max + this.offset;
    }

    public boolean tick(CarriageContraptionEntity entity, int tick, boolean backwards) {
        tick -= this.offset;
        if (!this.sources.containsKey(tick)) {
            return backwards ? tick > this.min : tick < this.max;
        } else {
            Contraption contraption = entity.getContraption();
            for (BlockPos blockPos : this.sources.get(tick)) {
                play(entity, (StructureTemplate.StructureBlockInfo) contraption.getBlocks().get(blockPos));
            }
            return backwards ? tick > this.min : tick < this.max;
        }
    }

    public Pair<Boolean, Integer> getFirstWhistle(CarriageContraptionEntity entity) {
        Integer firstTick = this.firstTick();
        Integer lastTick = this.lastTick();
        if (firstTick != null && lastTick != null && firstTick <= lastTick) {
            for (int i = firstTick; i <= lastTick; i++) {
                if (this.sources.containsKey(i - this.offset)) {
                    Contraption contraption = entity.getContraption();
                    for (BlockPos blockPos : this.sources.get(i - this.offset)) {
                        StructureTemplate.StructureBlockInfo info = (StructureTemplate.StructureBlockInfo) contraption.getBlocks().get(blockPos);
                        if (info != null) {
                            BlockState state = info.state();
                            if (state.m_60734_() instanceof WhistleBlock && info.nbt() != null) {
                                int pitch = info.nbt().getInt("Pitch");
                                WhistleBlock.WhistleSize size = (WhistleBlock.WhistleSize) state.m_61143_(WhistleBlock.SIZE);
                                return Pair.of(size == WhistleBlock.WhistleSize.LARGE, (size == WhistleBlock.WhistleSize.SMALL ? 12 : 0) - pitch);
                            }
                        }
                    }
                }
            }
            return null;
        } else {
            return null;
        }
    }

    public void serialize(CompoundTag tagIn) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Offset", this.offset);
        tag.put("Sources", NBTHelper.writeCompoundList(this.sources.entries(), e -> {
            CompoundTag c = new CompoundTag();
            c.putInt("Tick", (Integer) e.getKey());
            c.put("Pos", NbtUtils.writeBlockPos((BlockPos) e.getValue()));
            return c;
        }));
        tagIn.put("SoundQueue", tag);
    }

    public void deserialize(CompoundTag tagIn) {
        CompoundTag tag = tagIn.getCompound("SoundQueue");
        this.offset = tag.getInt("Offset");
        NBTHelper.iterateCompoundList(tag.getList("Sources", 10), c -> this.add(c.getInt("Tick"), NbtUtils.readBlockPos(c.getCompound("Pos"))));
    }

    public void add(int offset, BlockPos localPos) {
        this.sources.put(offset, localPos);
        this.min = Math.min(offset, this.min);
        this.max = Math.max(offset, this.max);
    }

    public static boolean isPlayable(BlockState state) {
        if (state.m_60734_() instanceof BellBlock) {
            return true;
        } else {
            return state.m_60734_() instanceof NoteBlock ? true : state.m_60734_() instanceof WhistleBlock;
        }
    }

    public static void play(CarriageContraptionEntity entity, StructureTemplate.StructureBlockInfo info) {
        if (info != null) {
            BlockState state = info.state();
            if (state.m_60734_() instanceof BellBlock) {
                if (AllBlocks.HAUNTED_BELL.has(state)) {
                    playSimple(entity, AllSoundEvents.HAUNTED_BELL_USE.getMainEvent(), 1.0F, 1.0F);
                } else {
                    playSimple(entity, SoundEvents.BELL_BLOCK, 1.0F, 1.0F);
                }
            }
            if (state.m_60734_() instanceof NoteBlock nb) {
                float f = (float) Math.pow(2.0, (double) ((Integer) state.m_61143_(NoteBlock.NOTE) - 12) / 12.0);
                playSimple(entity, (SoundEvent) ((NoteBlockInstrument) state.m_61143_(NoteBlock.INSTRUMENT)).getSoundEvent().get(), 1.0F, f);
            }
            if (state.m_60734_() instanceof WhistleBlock && info.nbt() != null) {
                int pitch = info.nbt().getInt("Pitch");
                WhistleBlock.WhistleSize size = (WhistleBlock.WhistleSize) state.m_61143_(WhistleBlock.SIZE);
                float f = (float) Math.pow(2.0, (double) ((size == WhistleBlock.WhistleSize.SMALL ? 12 : 0) - pitch) / 12.0);
                playSimple(entity, (size == WhistleBlock.WhistleSize.LARGE ? AllSoundEvents.WHISTLE_TRAIN_LOW : AllSoundEvents.WHISTLE_TRAIN).getMainEvent(), 1.0F, f);
            }
        }
    }

    private static void playSimple(CarriageContraptionEntity entity, SoundEvent event, float volume, float pitch) {
        entity.m_9236_().playSound(null, entity, event, SoundSource.NEUTRAL, 5.0F * volume, pitch);
    }
}