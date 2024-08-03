package net.minecraft.world.level.block.entity;

import java.util.List;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ShulkerBoxBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {

    public static final int COLUMNS = 9;

    public static final int ROWS = 3;

    public static final int CONTAINER_SIZE = 27;

    public static final int EVENT_SET_OPEN_COUNT = 1;

    public static final int OPENING_TICK_LENGTH = 10;

    public static final float MAX_LID_HEIGHT = 0.5F;

    public static final float MAX_LID_ROTATION = 270.0F;

    public static final String ITEMS_TAG = "Items";

    private static final int[] SLOTS = IntStream.range(0, 27).toArray();

    private NonNullList<ItemStack> itemStacks = NonNullList.withSize(27, ItemStack.EMPTY);

    private int openCount;

    private ShulkerBoxBlockEntity.AnimationStatus animationStatus = ShulkerBoxBlockEntity.AnimationStatus.CLOSED;

    private float progress;

    private float progressOld;

    @Nullable
    private final DyeColor color;

    public ShulkerBoxBlockEntity(@Nullable DyeColor dyeColor0, BlockPos blockPos1, BlockState blockState2) {
        super(BlockEntityType.SHULKER_BOX, blockPos1, blockState2);
        this.color = dyeColor0;
    }

    public ShulkerBoxBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.SHULKER_BOX, blockPos0, blockState1);
        this.color = ShulkerBoxBlock.getColorFromBlock(blockState1.m_60734_());
    }

    public static void tick(Level level0, BlockPos blockPos1, BlockState blockState2, ShulkerBoxBlockEntity shulkerBoxBlockEntity3) {
        shulkerBoxBlockEntity3.updateAnimation(level0, blockPos1, blockState2);
    }

    private void updateAnimation(Level level0, BlockPos blockPos1, BlockState blockState2) {
        this.progressOld = this.progress;
        switch(this.animationStatus) {
            case CLOSED:
                this.progress = 0.0F;
                break;
            case OPENING:
                this.progress += 0.1F;
                if (this.progress >= 1.0F) {
                    this.animationStatus = ShulkerBoxBlockEntity.AnimationStatus.OPENED;
                    this.progress = 1.0F;
                    doNeighborUpdates(level0, blockPos1, blockState2);
                }
                this.moveCollidedEntities(level0, blockPos1, blockState2);
                break;
            case CLOSING:
                this.progress -= 0.1F;
                if (this.progress <= 0.0F) {
                    this.animationStatus = ShulkerBoxBlockEntity.AnimationStatus.CLOSED;
                    this.progress = 0.0F;
                    doNeighborUpdates(level0, blockPos1, blockState2);
                }
                break;
            case OPENED:
                this.progress = 1.0F;
        }
    }

    public ShulkerBoxBlockEntity.AnimationStatus getAnimationStatus() {
        return this.animationStatus;
    }

    public AABB getBoundingBox(BlockState blockState0) {
        return Shulker.getProgressAabb((Direction) blockState0.m_61143_(ShulkerBoxBlock.FACING), 0.5F * this.getProgress(1.0F));
    }

    private void moveCollidedEntities(Level level0, BlockPos blockPos1, BlockState blockState2) {
        if (blockState2.m_60734_() instanceof ShulkerBoxBlock) {
            Direction $$3 = (Direction) blockState2.m_61143_(ShulkerBoxBlock.FACING);
            AABB $$4 = Shulker.getProgressDeltaAabb($$3, this.progressOld, this.progress).move(blockPos1);
            List<Entity> $$5 = level0.m_45933_(null, $$4);
            if (!$$5.isEmpty()) {
                for (int $$6 = 0; $$6 < $$5.size(); $$6++) {
                    Entity $$7 = (Entity) $$5.get($$6);
                    if ($$7.getPistonPushReaction() != PushReaction.IGNORE) {
                        $$7.move(MoverType.SHULKER_BOX, new Vec3(($$4.getXsize() + 0.01) * (double) $$3.getStepX(), ($$4.getYsize() + 0.01) * (double) $$3.getStepY(), ($$4.getZsize() + 0.01) * (double) $$3.getStepZ()));
                    }
                }
            }
        }
    }

    @Override
    public int getContainerSize() {
        return this.itemStacks.size();
    }

    @Override
    public boolean triggerEvent(int int0, int int1) {
        if (int0 == 1) {
            this.openCount = int1;
            if (int1 == 0) {
                this.animationStatus = ShulkerBoxBlockEntity.AnimationStatus.CLOSING;
                doNeighborUpdates(this.m_58904_(), this.f_58858_, this.m_58900_());
            }
            if (int1 == 1) {
                this.animationStatus = ShulkerBoxBlockEntity.AnimationStatus.OPENING;
                doNeighborUpdates(this.m_58904_(), this.f_58858_, this.m_58900_());
            }
            return true;
        } else {
            return super.m_7531_(int0, int1);
        }
    }

    private static void doNeighborUpdates(Level level0, BlockPos blockPos1, BlockState blockState2) {
        blockState2.m_60701_(level0, blockPos1, 3);
    }

    @Override
    public void startOpen(Player player0) {
        if (!this.f_58859_ && !player0.isSpectator()) {
            if (this.openCount < 0) {
                this.openCount = 0;
            }
            this.openCount++;
            this.f_58857_.blockEvent(this.f_58858_, this.m_58900_().m_60734_(), 1, this.openCount);
            if (this.openCount == 1) {
                this.f_58857_.m_142346_(player0, GameEvent.CONTAINER_OPEN, this.f_58858_);
                this.f_58857_.playSound(null, this.f_58858_, SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.5F, this.f_58857_.random.nextFloat() * 0.1F + 0.9F);
            }
        }
    }

    @Override
    public void stopOpen(Player player0) {
        if (!this.f_58859_ && !player0.isSpectator()) {
            this.openCount--;
            this.f_58857_.blockEvent(this.f_58858_, this.m_58900_().m_60734_(), 1, this.openCount);
            if (this.openCount <= 0) {
                this.f_58857_.m_142346_(player0, GameEvent.CONTAINER_CLOSE, this.f_58858_);
                this.f_58857_.playSound(null, this.f_58858_, SoundEvents.SHULKER_BOX_CLOSE, SoundSource.BLOCKS, 0.5F, this.f_58857_.random.nextFloat() * 0.1F + 0.9F);
            }
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.shulkerBox");
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.m_142466_(compoundTag0);
        this.loadFromTag(compoundTag0);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.m_183515_(compoundTag0);
        if (!this.m_59634_(compoundTag0)) {
            ContainerHelper.saveAllItems(compoundTag0, this.itemStacks, false);
        }
    }

    public void loadFromTag(CompoundTag compoundTag0) {
        this.itemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.m_59631_(compoundTag0) && compoundTag0.contains("Items", 9)) {
            ContainerHelper.loadAllItems(compoundTag0, this.itemStacks);
        }
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.itemStacks;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullListItemStack0) {
        this.itemStacks = nonNullListItemStack0;
    }

    @Override
    public int[] getSlotsForFace(Direction direction0) {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int int0, ItemStack itemStack1, @Nullable Direction direction2) {
        return !(Block.byItem(itemStack1.getItem()) instanceof ShulkerBoxBlock);
    }

    @Override
    public boolean canTakeItemThroughFace(int int0, ItemStack itemStack1, Direction direction2) {
        return true;
    }

    public float getProgress(float float0) {
        return Mth.lerp(float0, this.progressOld, this.progress);
    }

    @Nullable
    public DyeColor getColor() {
        return this.color;
    }

    @Override
    protected AbstractContainerMenu createMenu(int int0, Inventory inventory1) {
        return new ShulkerBoxMenu(int0, inventory1, this);
    }

    public boolean isClosed() {
        return this.animationStatus == ShulkerBoxBlockEntity.AnimationStatus.CLOSED;
    }

    public static enum AnimationStatus {

        CLOSED, OPENING, OPENED, CLOSING
    }
}