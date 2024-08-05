package com.simibubi.create.content.contraptions.minecart;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.simibubi.create.content.contraptions.minecart.capability.CapabilityMinecartController;
import com.simibubi.create.content.contraptions.minecart.capability.MinecartController;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;

public class MinecartSim2020 {

    private static final Map<RailShape, Pair<Vec3i, Vec3i>> MATRIX = Util.make(Maps.newEnumMap(RailShape.class), map -> {
        Vec3i west = Direction.WEST.getNormal();
        Vec3i east = Direction.EAST.getNormal();
        Vec3i north = Direction.NORTH.getNormal();
        Vec3i south = Direction.SOUTH.getNormal();
        map.put(RailShape.NORTH_SOUTH, Pair.of(north, south));
        map.put(RailShape.EAST_WEST, Pair.of(west, east));
        map.put(RailShape.ASCENDING_EAST, Pair.of(west.below(), east));
        map.put(RailShape.ASCENDING_WEST, Pair.of(west, east.below()));
        map.put(RailShape.ASCENDING_NORTH, Pair.of(north, south.below()));
        map.put(RailShape.ASCENDING_SOUTH, Pair.of(north.below(), south));
        map.put(RailShape.SOUTH_EAST, Pair.of(south, east));
        map.put(RailShape.SOUTH_WEST, Pair.of(south, west));
        map.put(RailShape.NORTH_WEST, Pair.of(north, west));
        map.put(RailShape.NORTH_EAST, Pair.of(north, east));
    });

    public static Vec3 predictNextPositionOf(AbstractMinecart cart) {
        Vec3 position = cart.m_20182_();
        Vec3 motion = VecHelper.clamp(cart.m_20184_(), 1.0F);
        return position.add(motion);
    }

    public static boolean canAddMotion(AbstractMinecart c) {
        if (!(c instanceof MinecartFurnace)) {
            LazyOptional<MinecartController> capability = c.getCapability(CapabilityMinecartController.MINECART_CONTROLLER_CAPABILITY);
            return !capability.isPresent() || !capability.orElse(null).isStalled();
        } else {
            return Mth.equal(((MinecartFurnace) c).xPush, 0.0) && Mth.equal(((MinecartFurnace) c).zPush, 0.0);
        }
    }

    public static void moveCartAlongTrack(AbstractMinecart cart, Vec3 forcedMovement, BlockPos cartPos, BlockState trackState) {
        if (!forcedMovement.equals(Vec3.ZERO)) {
            Vec3 previousMotion = cart.m_20184_();
            cart.f_19789_ = 0.0F;
            double x = cart.m_20185_();
            double y = cart.m_20186_();
            double z = cart.m_20189_();
            Vec3 actualVec = cart.getPos(x, y, z);
            double actualY = (double) (cartPos.m_123342_() + 1);
            BaseRailBlock abstractrailblock = (BaseRailBlock) trackState.m_60734_();
            RailShape railshape = abstractrailblock.getRailDirection(trackState, cart.m_9236_(), cartPos, cart);
            switch(railshape) {
                case ASCENDING_EAST:
                    forcedMovement = forcedMovement.add(-1.0 * cart.getSlopeAdjustment(), 0.0, 0.0);
                    actualY++;
                    break;
                case ASCENDING_WEST:
                    forcedMovement = forcedMovement.add(cart.getSlopeAdjustment(), 0.0, 0.0);
                    actualY++;
                    break;
                case ASCENDING_NORTH:
                    forcedMovement = forcedMovement.add(0.0, 0.0, cart.getSlopeAdjustment());
                    actualY++;
                    break;
                case ASCENDING_SOUTH:
                    forcedMovement = forcedMovement.add(0.0, 0.0, -1.0 * cart.getSlopeAdjustment());
                    actualY++;
            }
            Pair<Vec3i, Vec3i> pair = (Pair<Vec3i, Vec3i>) MATRIX.get(railshape);
            Vec3i Vector3i = (Vec3i) pair.getFirst();
            Vec3i Vector3i1 = (Vec3i) pair.getSecond();
            double d4 = (double) (Vector3i1.getX() - Vector3i.getX());
            double d5 = (double) (Vector3i1.getZ() - Vector3i.getZ());
            double d7 = forcedMovement.x * d4 + forcedMovement.z * d5;
            if (d7 < 0.0) {
                d4 = -d4;
                d5 = -d5;
            }
            double d23 = (double) cartPos.m_123341_() + 0.5 + (double) Vector3i.getX() * 0.5;
            double d10 = (double) cartPos.m_123343_() + 0.5 + (double) Vector3i.getZ() * 0.5;
            double d12 = (double) cartPos.m_123341_() + 0.5 + (double) Vector3i1.getX() * 0.5;
            double d13 = (double) cartPos.m_123343_() + 0.5 + (double) Vector3i1.getZ() * 0.5;
            d4 = d12 - d23;
            d5 = d13 - d10;
            double d14;
            if (d4 == 0.0) {
                d14 = z - (double) cartPos.m_123343_();
            } else if (d5 == 0.0) {
                d14 = x - (double) cartPos.m_123341_();
            } else {
                double d15 = x - d23;
                double d16 = z - d10;
                d14 = (d15 * d4 + d16 * d5) * 2.0;
            }
            double actualX = d23 + d4 * d14;
            double actualZ = d10 + d5 * d14;
            cart.m_6034_(actualX, actualY, actualZ);
            cart.m_20256_(forcedMovement);
            cart.moveMinecartOnRail(cartPos);
            x = cart.m_20185_();
            y = cart.m_20186_();
            z = cart.m_20189_();
            if (Vector3i.getY() != 0 && Mth.floor(x) - cartPos.m_123341_() == Vector3i.getX() && Mth.floor(z) - cartPos.m_123343_() == Vector3i.getZ()) {
                cart.m_6034_(x, y + (double) Vector3i.getY(), z);
            } else if (Vector3i1.getY() != 0 && Mth.floor(x) - cartPos.m_123341_() == Vector3i1.getX() && Mth.floor(z) - cartPos.m_123343_() == Vector3i1.getZ()) {
                cart.m_6034_(x, y + (double) Vector3i1.getY(), z);
            }
            x = cart.m_20185_();
            y = cart.m_20186_();
            z = cart.m_20189_();
            Vec3 Vector3d3 = cart.getPos(x, y, z);
            if (Vector3d3 != null && actualVec != null) {
                double d17 = (actualVec.y - Vector3d3.y) * 0.05;
                Vec3 Vector3d4 = cart.m_20184_();
                double d18 = Math.sqrt(Vector3d4.horizontalDistanceSqr());
                if (d18 > 0.0) {
                    cart.m_20256_(Vector3d4.multiply((d18 + d17) / d18, 1.0, (d18 + d17) / d18));
                }
                cart.m_6034_(x, Vector3d3.y, z);
            }
            x = cart.m_20185_();
            y = cart.m_20186_();
            z = cart.m_20189_();
            int j = Mth.floor(x);
            int i = Mth.floor(z);
            if (j != cartPos.m_123341_() || i != cartPos.m_123343_()) {
                Vec3 Vector3d5 = cart.m_20184_();
                double d26 = Math.sqrt(Vector3d5.horizontalDistanceSqr());
                cart.m_20334_(d26 * (double) (j - cartPos.m_123341_()), Vector3d5.y, d26 * (double) (i - cartPos.m_123343_()));
            }
            cart.m_20256_(previousMotion);
        }
    }

    public static Vec3 getRailVec(RailShape shape) {
        switch(shape) {
            case ASCENDING_EAST:
            case ASCENDING_WEST:
            case EAST_WEST:
                return new Vec3(1.0, 0.0, 0.0);
            case ASCENDING_NORTH:
            case ASCENDING_SOUTH:
            case NORTH_SOUTH:
                return new Vec3(0.0, 0.0, 1.0);
            case NORTH_EAST:
            case SOUTH_WEST:
                return new Vec3(1.0, 0.0, 1.0).normalize();
            case NORTH_WEST:
            case SOUTH_EAST:
                return new Vec3(1.0, 0.0, -1.0).normalize();
            default:
                return new Vec3(0.0, 1.0, 0.0);
        }
    }
}