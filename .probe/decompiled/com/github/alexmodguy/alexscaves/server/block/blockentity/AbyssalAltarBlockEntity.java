package com.github.alexmodguy.alexscaves.server.block.blockentity;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.block.AbyssalAltarBlock;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import com.github.alexmodguy.alexscaves.server.message.WorldEventMessage;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class AbyssalAltarBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {

    LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN);

    private NonNullList<ItemStack> stacks = NonNullList.withSize(1, ItemStack.EMPTY);

    private ItemStack displayCopyStack = ItemStack.EMPTY;

    private float itemAngle = 0.0F;

    private float slideProgress;

    private float prevSlideProgress;

    private int popDelay = 0;

    private static final int[] slotsTop = new int[] { 0 };

    private ItemStack popStack;

    private LivingEntity lastInteracter;

    private UUID placingPlayer = null;

    private long lastInteractionTime = 0L;

    private boolean slideImpulse;

    public AbyssalAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ACBlockEntityRegistry.ABYSSAL_ALTAR.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AbyssalAltarBlockEntity entity) {
        if (level.isClientSide) {
            ItemStack itemStack = entity.getItem(0);
            if (!itemStack.equals(entity.displayCopyStack, false) || entity.slideImpulse) {
                if (entity.slideProgress > 0.0F) {
                    entity.prevSlideProgress = entity.slideProgress--;
                } else {
                    entity.prevSlideProgress = entity.slideProgress;
                    entity.slideImpulse = false;
                    entity.displayCopyStack = itemStack.copy();
                }
            }
        }
        if (entity.popStack != null && !level.isClientSide && entity.popDelay++ > 6) {
            ItemStack drop = entity.popStack.copy();
            Vec3 angleAdd = new Vec3(0.0, 0.0, 1.0).yRot(entity.itemAngle * (float) (Math.PI / 180.0));
            Vec3 vec3 = Vec3.atCenterOf(entity.f_58858_).add(0.0, 0.5, 0.0).add(angleAdd);
            ItemEntity itemEntity = new ItemEntity(level, vec3.x, vec3.y, vec3.z, drop);
            if (entity.lastInteracter != null) {
                itemEntity.setThrower(entity.lastInteracter.m_20148_());
                boolean kill = true;
                if (entity.lastInteracter instanceof Player player) {
                    boolean fullInv = !hasInventorySpaceFor(player.getInventory(), drop);
                    if (!player.addItem(drop.copy()) || player.getAbilities().instabuild && fullInv) {
                        kill = false;
                    }
                } else if (entity.lastInteracter instanceof DeepOneBaseEntity deepOne) {
                    deepOne.swapItemsForAnimation(itemEntity.getItem());
                    deepOne.m_21008_(InteractionHand.MAIN_HAND, itemEntity.getItem());
                    deepOne.setAnimation(deepOne.getTradingAnimation());
                    deepOne.m_216990_(deepOne.getAdmireSound());
                    if (entity.placingPlayer != null) {
                        deepOne.addReputation(entity.placingPlayer, 5);
                        entity.placingPlayer = null;
                    }
                    kill = true;
                }
                itemEntity.setItem(drop);
                if (kill) {
                    entity.lastInteracter.onItemPickup(itemEntity);
                    entity.lastInteracter.take(itemEntity, drop.getCount());
                    itemEntity.m_146870_();
                } else {
                    level.m_7967_(itemEntity);
                    itemEntity.setDefaultPickUpDelay();
                }
            }
            entity.popStack = null;
            entity.lastInteracter = null;
        }
    }

    private static boolean hasInventorySpaceFor(Inventory inventory, ItemStack drop) {
        return inventory.getSlotWithRemainingSpace(drop) != -1 || inventory.getFreeSlot() != -1;
    }

    public void onEntityInteract(LivingEntity entity, boolean flip) {
        this.displayCopyStack = this.getItem(0).copy();
        if (flip) {
            if ((Boolean) this.m_58900_().m_61143_(AbyssalAltarBlock.ACTIVE)) {
                this.f_58857_.setBlockAndUpdate(this.f_58858_, (BlockState) this.m_58900_().m_61124_(AbyssalAltarBlock.ACTIVE, false));
            }
        } else if (entity instanceof DeepOneBaseEntity) {
            this.f_58857_.setBlockAndUpdate(this.f_58858_, (BlockState) this.m_58900_().m_61124_(AbyssalAltarBlock.ACTIVE, true));
        }
        Vec3 vec3 = entity.m_20182_().subtract(Vec3.atCenterOf(this.f_58858_));
        this.itemAngle = Mth.wrapDegrees((float) (Mth.atan2(vec3.x, vec3.z) * 180.0F / (float) Math.PI));
        this.lastInteracter = entity;
        this.popDelay = 0;
        this.resetSlideAnimation();
        if (!this.f_58857_.isClientSide) {
            BlockPos blockPos = this.m_58899_();
            AlexsCaves.sendMSGToAll(new WorldEventMessage(6, blockPos.m_123341_(), blockPos.m_123342_(), blockPos.m_123343_()));
        }
        if (entity instanceof Player) {
            this.placingPlayer = entity.m_20148_();
        } else {
            this.lastInteractionTime = entity.m_9236_().getGameTime();
        }
    }

    public void resetSlideAnimation() {
        this.prevSlideProgress = 5.0F;
        this.slideProgress = 5.0F;
        this.slideImpulse = true;
    }

    public float getSlideProgress(float partialTick) {
        return (this.prevSlideProgress + (this.slideProgress - this.prevSlideProgress) * partialTick) * 0.2F;
    }

    public float getItemAngle() {
        return this.itemAngle;
    }

    public ItemStack getDisplayStack() {
        return this.displayCopyStack;
    }

    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        return new AABB(this.f_58858_, this.f_58858_.offset(1, 2, 1));
    }

    @Override
    public int getContainerSize() {
        return this.stacks.size();
    }

    @Override
    public ItemStack getItem(int index) {
        return this.stacks.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        if (!this.stacks.get(index).isEmpty()) {
            if (this.stacks.get(index).getCount() <= count) {
                ItemStack itemstack = this.stacks.get(index);
                this.stacks.set(index, ItemStack.EMPTY);
                return itemstack;
            } else {
                ItemStack itemstack = this.stacks.get(index).split(count);
                if (this.stacks.get(index).isEmpty()) {
                    this.stacks.set(index, ItemStack.EMPTY);
                }
                return itemstack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    public ItemStack getStackInSlotOnClosing(int index) {
        if (!this.stacks.get(index).isEmpty()) {
            ItemStack itemstack = this.stacks.get(index);
            this.stacks.set(index, itemstack);
            return itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (!stack.isEmpty() && ItemStack.isSameItemSameTags(stack, this.stacks.get(index))) {
            boolean var4 = true;
        } else {
            boolean var10000 = false;
        }
        this.stacks.set(index, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        this.markUpdated();
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, this.stacks);
        if (compound.contains("PopStack")) {
            this.popStack = ItemStack.of(compound.getCompound("PopStack"));
        }
        if (compound.hasUUID("PlayerUUID")) {
            this.placingPlayer = compound.getUUID("PlayerUUID");
        }
        this.slideProgress = compound.getFloat("SlideAmount");
        this.prevSlideProgress = compound.getFloat("PrevSlideAmount");
        this.itemAngle = compound.getFloat("Angle");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        ContainerHelper.saveAllItems(compound, this.stacks);
        if (this.popStack != null && !this.popStack.isEmpty()) {
            CompoundTag stackTag = new CompoundTag();
            this.popStack.save(stackTag);
            compound.put("PopStack", stackTag);
        }
        if (this.placingPlayer != null) {
            compound.putUUID("PlayerUUID", this.placingPlayer);
        }
        compound.putFloat("Angle", this.itemAngle);
        compound.putFloat("SlideAmount", this.slideProgress);
        compound.putFloat("PrevSlideAmount", this.prevSlideProgress);
    }

    @Override
    public void startOpen(Player player) {
    }

    @Override
    public void stopOpen(Player player) {
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.stacks.clear();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return slotsTop;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return true;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        if (packet != null && packet.getTag() != null) {
            this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
            ContainerHelper.loadAllItems(packet.getTag(), this.stacks);
            this.itemAngle = packet.getTag().getFloat("Angle");
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compoundtag = new CompoundTag();
        ContainerHelper.saveAllItems(compoundtag, this.stacks, true);
        compoundtag.putFloat("Angle", this.itemAngle);
        return compoundtag;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack lvt_2_1_ = this.stacks.get(index);
        if (lvt_2_1_.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.stacks.set(index, ItemStack.EMPTY);
            return lvt_2_1_;
        }
    }

    @Override
    public Component getDisplayName() {
        return this.getDefaultName();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.alexsmobs.capsid");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            if (!this.getItem(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (this.f_58859_ || facing == null || capability != ForgeCapabilities.ITEM_HANDLER) {
            return super.getCapability(capability, facing);
        } else {
            return facing == Direction.DOWN ? this.handlers[0].cast() : this.handlers[1].cast();
        }
    }

    private void markUpdated() {
        this.m_6596_();
        this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
    }

    public boolean queueItemDrop(ItemStack copy) {
        if (this.popStack != null) {
            return false;
        } else {
            this.popStack = copy;
            return true;
        }
    }

    public long getLastInteractionTime() {
        return this.lastInteractionTime;
    }
}