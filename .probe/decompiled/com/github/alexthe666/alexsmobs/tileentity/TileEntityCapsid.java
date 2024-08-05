package com.github.alexthe666.alexsmobs.tileentity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.block.BlockCapsid;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityEnderiophage;
import com.github.alexthe666.alexsmobs.message.MessageUpdateCapsid;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.CapsidRecipe;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndRodBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class TileEntityCapsid extends BaseContainerBlockEntity implements WorldlyContainer {

    private static final int[] slotsTop = new int[] { 0 };

    public int ticksExisted;

    public float prevFloatUpProgress;

    public float floatUpProgress;

    public float prevYawSwitchProgress;

    public float yawSwitchProgress;

    public boolean vibratingThisTick = false;

    LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN);

    private float yawTarget = 0.0F;

    private int transformTime = 0;

    private boolean fnaf = false;

    private CapsidRecipe lastRecipe = null;

    private NonNullList<ItemStack> stacks = NonNullList.withSize(1, ItemStack.EMPTY);

    public TileEntityCapsid(BlockPos pos, BlockState state) {
        super(AMTileEntityRegistry.CAPSID.get(), pos, state);
    }

    public static void commonTick(Level level, BlockPos pos, BlockState state, TileEntityCapsid entity) {
        entity.tick();
    }

    public void tick() {
        this.prevFloatUpProgress = this.floatUpProgress;
        this.prevYawSwitchProgress = this.yawSwitchProgress;
        this.ticksExisted++;
        this.vibratingThisTick = false;
        if (!this.getItem(0).isEmpty()) {
            BlockEntity up = this.f_58857_.getBlockEntity(this.f_58858_.above());
            if (up instanceof Container) {
                if (this.floatUpProgress >= 1.0F) {
                    LazyOptional<IItemHandler> handler = this.f_58857_.getBlockEntity(this.f_58858_.above()).getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.UP);
                    if (handler.orElse(null) != null && ItemHandlerHelper.insertItem(handler.orElse(null), this.getItem(0), true).isEmpty()) {
                        ItemHandlerHelper.insertItem(handler.orElse(null), this.getItem(0).copy(), false);
                        this.setItem(0, ItemStack.EMPTY);
                    }
                    this.yawTarget = 0.0F;
                    this.floatUpProgress = 0.0F;
                    this.yawSwitchProgress = 0.0F;
                } else {
                    if (up instanceof TileEntityCapsid) {
                        this.yawTarget = Mth.wrapDegrees(((TileEntityCapsid) up).getBlockAngle() - this.getBlockAngle());
                    } else {
                        this.yawTarget = 0.0F;
                    }
                    if (this.yawTarget < this.yawSwitchProgress) {
                        this.yawSwitchProgress = this.yawSwitchProgress + this.yawTarget * 0.1F;
                    } else if (this.yawTarget > this.yawSwitchProgress) {
                        this.yawSwitchProgress = this.yawSwitchProgress + this.yawTarget * 0.1F;
                    }
                    this.floatUpProgress += 0.05F;
                }
            } else {
                this.floatUpProgress = 0.0F;
            }
            if (this.getItem(0).getItem() == Items.ENDER_EYE && this.f_58857_.getBlockState(this.m_58899_().below()).m_60734_() == Blocks.END_ROD && ((Direction) this.f_58857_.getBlockState(this.m_58899_().below()).m_61143_(EndRodBlock.f_52588_)).getAxis() == Direction.Axis.Y) {
                this.vibratingThisTick = true;
                if (this.transformTime > 20) {
                    this.setItem(0, ItemStack.EMPTY);
                    this.f_58857_.m_46961_(this.m_58899_(), false);
                    this.f_58857_.m_46961_(this.m_58899_().below(), false);
                    EntityEnderiophage phage = AMEntityRegistry.ENDERIOPHAGE.get().create(this.f_58857_);
                    phage.m_6034_((double) ((float) this.m_58899_().m_123341_() + 0.5F), (double) ((float) this.m_58899_().m_123342_() - 1.0F), (double) ((float) this.m_58899_().m_123343_() + 0.5F));
                    phage.setVariant(0);
                    if (!this.f_58857_.isClientSide) {
                        this.f_58857_.m_7967_(phage);
                    }
                }
            } else if (!this.getItem(0).isEmpty() && this.f_58857_.getBlockState(this.m_58899_().above()).m_60734_() != this.m_58900_().m_60734_() && this.lastRecipe != null && this.lastRecipe.matches(this.getItem(0))) {
                this.floatUpProgress = 0.0F;
                this.vibratingThisTick = true;
                if (this.transformTime == 1 && (AlexsMobs.isAprilFools() || new Random().nextInt(100) == 0)) {
                    this.fnaf = true;
                    this.f_58857_.playSound(null, this.m_58899_(), AMSoundRegistry.MOSQUITO_CAPSID_CONVERT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                if (this.transformTime > (this.fnaf ? Math.max(160, this.lastRecipe.getTime()) : this.lastRecipe.getTime())) {
                    ItemStack current = this.getItem(0).copy();
                    current.shrink(1);
                    this.fnaf = false;
                    if (!current.isEmpty()) {
                        ItemEntity itemEntity = new ItemEntity(this.f_58857_, (double) ((float) this.m_58899_().m_123341_() + 0.5F), (double) ((float) this.m_58899_().m_123342_() + 0.5F), (double) ((float) this.m_58899_().m_123343_() + 0.5F), current);
                        if (!this.f_58857_.isClientSide) {
                            this.f_58857_.m_7967_(itemEntity);
                        }
                    }
                    this.setItem(0, this.lastRecipe.getResult().copy());
                }
            }
        }
        if (!this.vibratingThisTick) {
            this.transformTime = 0;
        } else {
            this.transformTime++;
        }
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
        this.lastRecipe = AlexsMobs.PROXY.getCapsidRecipeManager().getRecipeFor(stack);
        this.saveAdditional(this.getUpdateTag());
        if (!this.f_58857_.isClientSide) {
            AlexsMobs.sendMSGToAll(new MessageUpdateCapsid(this.m_58899_().asLong(), this.stacks.get(0)));
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, this.stacks);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        ContainerHelper.saveAllItems(compound, this.stacks);
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
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
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

    public float getBlockAngle() {
        if (this.m_58900_().m_60734_() instanceof BlockCapsid) {
            Direction dir = (Direction) this.m_58900_().m_61143_(BlockCapsid.HORIZONTAL_FACING);
            return dir.toYRot();
        } else {
            return 0.0F;
        }
    }

    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (this.f_58859_ || facing == null || capability != ForgeCapabilities.ITEM_HANDLER) {
            return super.getCapability(capability, facing);
        } else {
            return facing == Direction.DOWN ? this.handlers[0].cast() : this.handlers[1].cast();
        }
    }
}