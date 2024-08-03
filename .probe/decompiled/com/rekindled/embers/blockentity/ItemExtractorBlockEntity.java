package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.filter.FilterAny;
import com.rekindled.embers.api.filter.IFilter;
import com.rekindled.embers.api.tile.IOrderDestination;
import com.rekindled.embers.api.tile.IOrderSource;
import com.rekindled.embers.api.tile.OrderStack;
import com.rekindled.embers.particle.VaporParticleOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class ItemExtractorBlockEntity extends ItemPipeBlockEntityBase implements IOrderDestination {

    Random random = new Random();

    IItemHandler[] sideHandlers;

    boolean active;

    List<OrderStack> orders = new ArrayList();

    public static IFilter FILTER_ANY = new FilterAny();

    public ItemExtractorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.ITEM_EXTRACTOR_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected void initInventory() {
        super.initInventory();
        this.sideHandlers = new IItemHandler[Direction.values().length];
        for (final Direction facing : Direction.values()) {
            this.sideHandlers[facing.get3DDataValue()] = new IItemHandler() {

                @Override
                public int getSlots() {
                    return ItemExtractorBlockEntity.this.inventory.getSlots();
                }

                @Nonnull
                @Override
                public ItemStack getStackInSlot(int slot) {
                    return ItemExtractorBlockEntity.this.inventory.getStackInSlot(slot);
                }

                @Nonnull
                @Override
                public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                    if (ItemExtractorBlockEntity.this.active) {
                        return stack;
                    } else {
                        if (!simulate) {
                            ItemExtractorBlockEntity.this.setFrom(facing, true);
                        }
                        return ItemExtractorBlockEntity.this.inventory.insertItem(slot, stack, simulate);
                    }
                }

                @Nonnull
                @Override
                public ItemStack extractItem(int slot, int amount, boolean simulate) {
                    return ItemExtractorBlockEntity.this.inventory.extractItem(slot, amount, simulate);
                }

                @Override
                public int getSlotLimit(int slot) {
                    return ItemExtractorBlockEntity.this.inventory.getSlotLimit(slot);
                }

                @Override
                public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                    return true;
                }
            };
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("orders")) {
            ListTag tagOrders = nbt.getList("orders", 10);
            this.orders.clear();
            for (Tag tagOrder : tagOrders) {
                this.orders.add(new OrderStack((CompoundTag) tagOrder));
            }
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        ListTag tagOrders = new ListTag();
        for (OrderStack order : this.orders) {
            tagOrders.add(order.writeToNBT(new CompoundTag()));
        }
        nbt.put("orders", tagOrders);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ItemExtractorBlockEntity blockEntity) {
        if (level instanceof ServerLevel && blockEntity.clogged && blockEntity.isAnySideUnclogged()) {
            Random posRand = new Random(pos.asLong());
            double angleA = posRand.nextDouble() * Math.PI * 2.0;
            double angleB = posRand.nextDouble() * Math.PI * 2.0;
            float xOffset = (float) (Math.cos(angleA) * Math.cos(angleB));
            float yOffset = (float) (Math.sin(angleA) * Math.cos(angleB));
            float zOffset = (float) Math.sin(angleB);
            float speed = 0.1F;
            float vx = xOffset * speed + posRand.nextFloat() * speed * 0.3F;
            float vy = yOffset * speed + posRand.nextFloat() * speed * 0.3F;
            float vz = zOffset * speed + posRand.nextFloat() * speed * 0.3F;
            ((ServerLevel) level).sendParticles(new VaporParticleOptions(new Vector3f(0.2509804F, 0.2509804F, 0.2509804F), new Vec3((double) vx, (double) vy, (double) vz), 1.0F), (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, 4, 0.0, 0.0, 0.0, 1.0);
        }
        blockEntity.cleanupOrders();
        blockEntity.active = level.m_276867_(pos);
        OrderStack currentOrder = blockEntity.orders.isEmpty() ? null : (OrderStack) blockEntity.orders.get(0);
        IFilter filter = FILTER_ANY;
        if (blockEntity.active) {
            currentOrder = null;
        } else if (currentOrder != null) {
            filter = currentOrder.getFilter();
        }
        IItemHandler invDest = null;
        if (currentOrder != null) {
            IOrderSource destination = currentOrder.getSource(level);
            if (destination != null) {
                invDest = destination.getItemHandler();
            }
        }
        for (Direction facing : Direction.values()) {
            if (blockEntity.getConnection(facing).transfer) {
                BlockEntity tile = level.getBlockEntity(pos.relative(facing));
                if (tile != null && !(tile instanceof ItemPipeBlockEntityBase)) {
                    IItemHandler handler = (IItemHandler) tile.getCapability(ForgeCapabilities.ITEM_HANDLER, facing.getOpposite()).orElse(null);
                    if (handler != null && (blockEntity.active || currentOrder != null && currentOrder.getSize() > 0)) {
                        int slot = -1;
                        for (int j = 0; j < handler.getSlots() && slot == -1; j++) {
                            ItemStack extracted = handler.extractItem(j, 1, true);
                            if (!extracted.isEmpty() && filter.acceptsItem(extracted, invDest)) {
                                slot = j;
                            }
                        }
                        if (slot != -1) {
                            ItemStack extracted = handler.extractItem(slot, 1, true);
                            if (blockEntity.inventory.insertItem(0, extracted, true).isEmpty()) {
                                handler.extractItem(slot, 1, false);
                                blockEntity.inventory.insertItem(0, extracted, false);
                                if (currentOrder != null) {
                                    currentOrder.deplete(extracted.getCount());
                                }
                            }
                        }
                        blockEntity.setFrom(facing, true);
                    } else {
                        blockEntity.setFrom(facing, false);
                    }
                }
            }
        }
        ItemPipeBlockEntityBase.serverTick(level, pos, state, blockEntity);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.f_58859_ && cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, this.holder);
            }
            if (this.getConnection(side).transfer) {
                return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, LazyOptional.of(() -> this.sideHandlers[side.get3DDataValue()]));
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public int getCapacity() {
        return 4;
    }

    @Override
    public void order(BlockEntity source, IFilter filter, int orderSize) {
        OrderStack order = this.getOrder(source);
        if (order == null) {
            this.orders.add(new OrderStack(source.getBlockPos(), filter, orderSize));
        } else if (Objects.equals(order.getFilter(), filter)) {
            order.increment(orderSize);
        } else {
            order.reset(filter, orderSize);
        }
    }

    @Override
    public void resetOrder(BlockEntity source) {
        this.orders.removeIf(order -> order.getPos().equals(source.getBlockPos()));
    }

    public OrderStack getOrder(BlockEntity source) {
        for (OrderStack order : this.orders) {
            if (order.getPos().equals(source.getBlockPos())) {
                return order;
            }
        }
        return null;
    }

    private void cleanupOrders() {
        this.orders.removeIf(this::isOrderInvalid);
    }

    private boolean isOrderInvalid(OrderStack order) {
        return order.getSize() <= 0 || order.getSource(this.f_58857_) == null;
    }
}