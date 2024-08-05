package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class ItemVacuumBlockEntity extends BlockEntity {

    public ItemStackHandler inventory = new ItemStackHandler(1) {

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return false;
        }
    };

    public LazyOptional<IItemHandler> holder = LazyOptional.of(() -> this.inventory);

    public ItemVacuumBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.ITEM_VACUUM_ENTITY.get(), pPos, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ItemVacuumBlockEntity blockEntity) {
        Direction facing = (Direction) state.m_61143_(BlockStateProperties.FACING);
        BlockEntity tile = level.getBlockEntity(pos.relative(facing.getOpposite()));
        if (level.m_276867_(pos) && tile != null) {
            IItemHandler inventory = (IItemHandler) tile.getCapability(ForgeCapabilities.ITEM_HANDLER, facing).orElse(null);
            if (inventory != null) {
                Vec3i vec = facing.getNormal();
                AABB suckBB = new AABB((double) (pos.m_123341_() - 6 + vec.getX() * 7), (double) (pos.m_123342_() - 6 + vec.getY() * 7), (double) (pos.m_123343_() - 6 + vec.getZ() * 7), (double) (pos.m_123341_() + 7 + vec.getX() * 7), (double) (pos.m_123342_() + 7 + vec.getY() * 7), (double) (pos.m_123343_() + 7 + vec.getZ() * 7));
                List<ItemEntity> items = level.m_6443_(ItemEntity.class, suckBB, entity -> getInsertedSlot(entity.getItem(), inventory) != -1);
                if (items.size() > 0) {
                    for (ItemEntity item : items) {
                        Vec3 v = new Vec3(item.m_20185_() - ((double) pos.m_123341_() + 0.5), item.m_20186_() - ((double) pos.m_123342_() + 0.5), item.m_20189_() - ((double) pos.m_123343_() + 0.5));
                        double factor = 1.0 / v.length();
                        double speed = factor * 0.4 + 0.06;
                        factor++;
                        v = v.normalize().multiply(speed, speed, speed);
                        item.m_20334_(-v.x + item.m_20184_().x / factor, -v.y + item.m_20184_().y / factor, -v.z + item.m_20184_().z / factor);
                    }
                }
                if (!level.isClientSide) {
                    List<ItemEntity> nearestItems = level.m_45976_(ItemEntity.class, new AABB((double) pos.m_123341_() + 0.24 + (double) vec.getX() * 0.25, (double) pos.m_123342_() + 0.24 + (double) vec.getY() * 0.25, (double) pos.m_123343_() + 0.24 + (double) vec.getZ() * 0.25, (double) pos.m_123341_() + 0.76 + (double) vec.getX() * 0.25, (double) pos.m_123342_() + 0.76 + (double) vec.getY() * 0.25, (double) pos.m_123343_() + 0.76 + (double) vec.getZ() * 0.25));
                    if (nearestItems.size() > 0) {
                        for (ItemEntity item : nearestItems) {
                            if (!item.m_213877_()) {
                                int slot = getInsertedSlot(item.getItem(), inventory);
                                if (slot != -1) {
                                    item.setItem(inventory.insertItem(slot, item.getItem(), false));
                                    if (item.getItem().isEmpty()) {
                                        item.m_146870_();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        Direction facing = (Direction) this.f_58857_.getBlockState(this.f_58858_).m_61143_(BlockStateProperties.FACING);
        return !this.f_58859_ && cap == ForgeCapabilities.ITEM_HANDLER && side == facing.getOpposite() ? ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, this.holder) : super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.holder.invalidate();
    }

    static int getInsertedSlot(ItemStack stack, IItemHandler inventory) {
        int slot = -1;
        for (int j = 0; j < inventory.getSlots() && slot == -1; j++) {
            if (inventory.isItemValid(j, stack)) {
                ItemStack added = inventory.insertItem(j, stack, true);
                if (added.getCount() < stack.getCount() || !ItemStack.isSameItemSameTags(stack, added)) {
                    slot = j;
                }
            }
        }
        return slot;
    }
}