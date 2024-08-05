package net.minecraft.world.level.block.entity;

import com.google.common.annotations.VisibleForTesting;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.ticks.ContainerSingleItem;

public class JukeboxBlockEntity extends BlockEntity implements Clearable, ContainerSingleItem {

    private static final int SONG_END_PADDING = 20;

    private final NonNullList<ItemStack> items = NonNullList.withSize(this.m_6643_(), ItemStack.EMPTY);

    private int ticksSinceLastEvent;

    private long tickCount;

    private long recordStartedTick;

    private boolean isPlaying;

    public JukeboxBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.JUKEBOX, blockPos0, blockState1);
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.load(compoundTag0);
        if (compoundTag0.contains("RecordItem", 10)) {
            this.items.set(0, ItemStack.of(compoundTag0.getCompound("RecordItem")));
        }
        this.isPlaying = compoundTag0.getBoolean("IsPlaying");
        this.recordStartedTick = compoundTag0.getLong("RecordStartTick");
        this.tickCount = compoundTag0.getLong("TickCount");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        if (!this.m_272036_().isEmpty()) {
            compoundTag0.put("RecordItem", this.m_272036_().save(new CompoundTag()));
        }
        compoundTag0.putBoolean("IsPlaying", this.isPlaying);
        compoundTag0.putLong("RecordStartTick", this.recordStartedTick);
        compoundTag0.putLong("TickCount", this.tickCount);
    }

    public boolean isRecordPlaying() {
        return !this.m_272036_().isEmpty() && this.isPlaying;
    }

    private void setHasRecordBlockState(@Nullable Entity entity0, boolean boolean1) {
        if (this.f_58857_.getBlockState(this.m_58899_()) == this.m_58900_()) {
            this.f_58857_.setBlock(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(JukeboxBlock.HAS_RECORD, boolean1), 2);
            this.f_58857_.m_220407_(GameEvent.BLOCK_CHANGE, this.m_58899_(), GameEvent.Context.of(entity0, this.m_58900_()));
        }
    }

    @VisibleForTesting
    public void startPlaying() {
        this.recordStartedTick = this.tickCount;
        this.isPlaying = true;
        this.f_58857_.updateNeighborsAt(this.m_58899_(), this.m_58900_().m_60734_());
        this.f_58857_.m_5898_(null, 1010, this.m_58899_(), Item.getId(this.m_272036_().getItem()));
        this.m_6596_();
    }

    private void stopPlaying() {
        this.isPlaying = false;
        this.f_58857_.m_220407_(GameEvent.JUKEBOX_STOP_PLAY, this.m_58899_(), GameEvent.Context.of(this.m_58900_()));
        this.f_58857_.updateNeighborsAt(this.m_58899_(), this.m_58900_().m_60734_());
        this.f_58857_.m_46796_(1011, this.m_58899_(), 0);
        this.m_6596_();
    }

    private void tick(Level level0, BlockPos blockPos1, BlockState blockState2) {
        this.ticksSinceLastEvent++;
        if (this.isRecordPlaying() && this.m_272036_().getItem() instanceof RecordItem $$3) {
            if (this.shouldRecordStopPlaying($$3)) {
                this.stopPlaying();
            } else if (this.shouldSendJukeboxPlayingEvent()) {
                this.ticksSinceLastEvent = 0;
                level0.m_220407_(GameEvent.JUKEBOX_PLAY, blockPos1, GameEvent.Context.of(blockState2));
                this.spawnMusicParticles(level0, blockPos1);
            }
        }
        this.tickCount++;
    }

    private boolean shouldRecordStopPlaying(RecordItem recordItem0) {
        return this.tickCount >= this.recordStartedTick + (long) recordItem0.getLengthInTicks() + 20L;
    }

    private boolean shouldSendJukeboxPlayingEvent() {
        return this.ticksSinceLastEvent >= 20;
    }

    @Override
    public ItemStack getItem(int int0) {
        return this.items.get(int0);
    }

    @Override
    public ItemStack removeItem(int int0, int int1) {
        ItemStack $$2 = (ItemStack) Objects.requireNonNullElse(this.items.get(int0), ItemStack.EMPTY);
        this.items.set(int0, ItemStack.EMPTY);
        if (!$$2.isEmpty()) {
            this.setHasRecordBlockState(null, false);
            this.stopPlaying();
        }
        return $$2;
    }

    @Override
    public void setItem(int int0, ItemStack itemStack1) {
        if (itemStack1.is(ItemTags.MUSIC_DISCS) && this.f_58857_ != null) {
            this.items.set(int0, itemStack1);
            this.setHasRecordBlockState(null, true);
            this.startPlaying();
        }
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean stillValid(Player player0) {
        return Container.stillValidBlockEntity(this, player0);
    }

    @Override
    public boolean canPlaceItem(int int0, ItemStack itemStack1) {
        return itemStack1.is(ItemTags.MUSIC_DISCS) && this.getItem(int0).isEmpty();
    }

    @Override
    public boolean canTakeItem(Container container0, int int1, ItemStack itemStack2) {
        return container0.hasAnyMatching(ItemStack::m_41619_);
    }

    private void spawnMusicParticles(Level level0, BlockPos blockPos1) {
        if (level0 instanceof ServerLevel $$2) {
            Vec3 $$3 = Vec3.atBottomCenterOf(blockPos1).add(0.0, 1.2F, 0.0);
            float $$4 = (float) level0.getRandom().nextInt(4) / 24.0F;
            $$2.sendParticles(ParticleTypes.NOTE, $$3.x(), $$3.y(), $$3.z(), 0, (double) $$4, 0.0, 0.0, 1.0);
        }
    }

    public void popOutRecord() {
        if (this.f_58857_ != null && !this.f_58857_.isClientSide) {
            BlockPos $$0 = this.m_58899_();
            ItemStack $$1 = this.m_272036_();
            if (!$$1.isEmpty()) {
                this.m_272108_();
                Vec3 $$2 = Vec3.atLowerCornerWithOffset($$0, 0.5, 1.01, 0.5).offsetRandom(this.f_58857_.random, 0.7F);
                ItemStack $$3 = $$1.copy();
                ItemEntity $$4 = new ItemEntity(this.f_58857_, $$2.x(), $$2.y(), $$2.z(), $$3);
                $$4.setDefaultPickUpDelay();
                this.f_58857_.m_7967_($$4);
            }
        }
    }

    public static void playRecordTick(Level level0, BlockPos blockPos1, BlockState blockState2, JukeboxBlockEntity jukeboxBlockEntity3) {
        jukeboxBlockEntity3.tick(level0, blockPos1, blockState2);
    }

    @VisibleForTesting
    public void setRecordWithoutPlaying(ItemStack itemStack0) {
        this.items.set(0, itemStack0);
        this.f_58857_.updateNeighborsAt(this.m_58899_(), this.m_58900_().m_60734_());
        this.m_6596_();
    }
}