package com.simibubi.create.content.logistics.funnel;

import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import com.simibubi.create.foundation.item.ItemHelper;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;

public class FunnelMovementBehaviour implements MovementBehaviour {

    private final boolean hasFilter;

    public static FunnelMovementBehaviour andesite() {
        return new FunnelMovementBehaviour(false);
    }

    public static FunnelMovementBehaviour brass() {
        return new FunnelMovementBehaviour(true);
    }

    private FunnelMovementBehaviour(boolean hasFilter) {
        this.hasFilter = hasFilter;
    }

    @Override
    public Vec3 getActiveAreaOffset(MovementContext context) {
        Direction facing = FunnelBlock.getFunnelFacing(context.state);
        Vec3 vec = Vec3.atLowerCornerOf(facing.getNormal());
        return facing != Direction.UP ? vec.scale(context.state.m_61143_(FunnelBlock.EXTRACTING) ? 0.15 : 0.65) : vec.scale(0.65);
    }

    @Override
    public void visitNewPosition(MovementContext context, BlockPos pos) {
        MovementBehaviour.super.visitNewPosition(context, pos);
        if ((Boolean) context.state.m_61143_(FunnelBlock.EXTRACTING)) {
            this.extract(context, pos);
        } else {
            this.succ(context, pos);
        }
    }

    private void extract(MovementContext context, BlockPos pos) {
        Level world = context.world;
        Vec3 entityPos = context.position;
        if (context.state.m_61143_(FunnelBlock.FACING) != Direction.DOWN) {
            entityPos = entityPos.add(0.0, -0.5, 0.0);
        }
        if (world.getBlockState(pos).m_60812_(world, pos).isEmpty()) {
            if (world.m_45976_(ItemEntity.class, new AABB(BlockPos.containing(entityPos))).isEmpty()) {
                FilterItemStack filter = context.getFilterFromBE();
                int filterAmount = context.blockEntityData.getInt("FilterAmount");
                boolean upTo = context.blockEntityData.getBoolean("UpTo");
                if (filterAmount <= 0) {
                    filterAmount = this.hasFilter ? 64 : 1;
                }
                ItemStack extract = ItemHelper.extract(context.contraption.getSharedInventory(), s -> filter.test(world, s), upTo ? ItemHelper.ExtractionCountMode.UPTO : ItemHelper.ExtractionCountMode.EXACTLY, filterAmount, false);
                if (!extract.isEmpty()) {
                    if (!world.isClientSide) {
                        ItemEntity entity = new ItemEntity(world, entityPos.x, entityPos.y, entityPos.z, extract);
                        entity.m_20256_(Vec3.ZERO);
                        entity.setPickUpDelay(5);
                        world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.0625F, 0.1F);
                        world.m_7967_(entity);
                    }
                }
            }
        }
    }

    private void succ(MovementContext context, BlockPos pos) {
        Level world = context.world;
        List<ItemEntity> items = world.m_45976_(ItemEntity.class, new AABB(pos));
        FilterItemStack filter = context.getFilterFromBE();
        for (ItemEntity item : items) {
            if (item.m_6084_()) {
                ItemStack toInsert = item.getItem();
                if (filter.test(context.world, toInsert)) {
                    ItemStack remainder = ItemHandlerHelper.insertItemStacked(context.contraption.getSharedInventory(), toInsert, false);
                    if (remainder.getCount() != toInsert.getCount()) {
                        if (remainder.isEmpty()) {
                            item.setItem(ItemStack.EMPTY);
                            item.m_146870_();
                        } else {
                            item.setItem(remainder);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean renderAsNormalBlockEntity() {
        return true;
    }
}