package com.simibubi.create.content.kinetics.belt;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.utility.VecHelper;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class BeltHelper {

    public static Map<Item, Boolean> uprightCache = new Object2BooleanOpenHashMap();

    public static final ResourceManagerReloadListener LISTENER = resourceManager -> uprightCache.clear();

    public static boolean isItemUpright(ItemStack stack) {
        return (Boolean) uprightCache.computeIfAbsent(stack.getItem(), item -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent() || AllTags.AllItemTags.UPRIGHT_ON_BELT.matches(stack));
    }

    public static BeltBlockEntity getSegmentBE(LevelAccessor world, BlockPos pos) {
        if (world instanceof Level l && !l.isLoaded(pos)) {
            return null;
        }
        BlockEntity blockEntity = world.m_7702_(pos);
        return !(blockEntity instanceof BeltBlockEntity) ? null : (BeltBlockEntity) blockEntity;
    }

    public static BeltBlockEntity getControllerBE(LevelAccessor world, BlockPos pos) {
        BeltBlockEntity segment = getSegmentBE(world, pos);
        if (segment == null) {
            return null;
        } else {
            BlockPos controllerPos = segment.controller;
            return controllerPos == null ? null : getSegmentBE(world, controllerPos);
        }
    }

    public static BeltBlockEntity getBeltForOffset(BeltBlockEntity controller, float offset) {
        return getBeltAtSegment(controller, (int) Math.floor((double) offset));
    }

    public static BeltBlockEntity getBeltAtSegment(BeltBlockEntity controller, int segment) {
        BlockPos pos = getPositionForOffset(controller, segment);
        BlockEntity be = controller.m_58904_().getBlockEntity(pos);
        return be != null && be instanceof BeltBlockEntity ? (BeltBlockEntity) be : null;
    }

    public static BlockPos getPositionForOffset(BeltBlockEntity controller, int offset) {
        BlockPos pos = controller.m_58899_();
        Vec3i vec = controller.getBeltFacing().getNormal();
        BeltSlope slope = (BeltSlope) controller.m_58900_().m_61143_(BeltBlock.SLOPE);
        int verticality = slope == BeltSlope.DOWNWARD ? -1 : (slope == BeltSlope.UPWARD ? 1 : 0);
        return pos.offset(offset * vec.getX(), Mth.clamp(offset, 0, controller.beltLength - 1) * verticality, offset * vec.getZ());
    }

    public static Vec3 getVectorForOffset(BeltBlockEntity controller, float offset) {
        BeltSlope slope = (BeltSlope) controller.m_58900_().m_61143_(BeltBlock.SLOPE);
        int verticality = slope == BeltSlope.DOWNWARD ? -1 : (slope == BeltSlope.UPWARD ? 1 : 0);
        float verticalMovement = (float) verticality;
        if ((double) offset < 0.5) {
            verticalMovement = 0.0F;
        }
        verticalMovement *= Math.min(offset, (float) controller.beltLength - 0.5F) - 0.5F;
        Vec3 vec = VecHelper.getCenterOf(controller.m_58899_());
        Vec3 horizontalMovement = Vec3.atLowerCornerOf(controller.getBeltFacing().getNormal()).scale((double) (offset - 0.5F));
        if (slope == BeltSlope.VERTICAL) {
            horizontalMovement = Vec3.ZERO;
        }
        return vec.add(horizontalMovement).add(0.0, (double) verticalMovement, 0.0);
    }

    public static Vec3 getBeltVector(BlockState state) {
        BeltSlope slope = (BeltSlope) state.m_61143_(BeltBlock.SLOPE);
        int verticality = slope == BeltSlope.DOWNWARD ? -1 : (slope == BeltSlope.UPWARD ? 1 : 0);
        Vec3 horizontalMovement = Vec3.atLowerCornerOf(((Direction) state.m_61143_(BeltBlock.HORIZONTAL_FACING)).getNormal());
        return slope == BeltSlope.VERTICAL ? new Vec3(0.0, (double) ((Direction) state.m_61143_(BeltBlock.HORIZONTAL_FACING)).getAxisDirection().getStep(), 0.0) : new Vec3(0.0, (double) verticality, 0.0).add(horizontalMovement);
    }
}