package com.simibubi.create.content.contraptions.minecart;

import com.simibubi.create.content.contraptions.minecart.capability.MinecartController;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.extensions.IForgeAbstractMinecart;

public class CouplingPhysics {

    public static void tick(Level world) {
        CouplingHandler.forEachLoadedCoupling(world, c -> tickCoupling(world, c));
    }

    public static void tickCoupling(Level world, Couple<MinecartController> c) {
        Couple<AbstractMinecart> carts = c.map(MinecartController::cart);
        float couplingLength = c.getFirst().getCouplingLength(true);
        softCollisionStep(world, carts, (double) couplingLength);
        if (!world.isClientSide) {
            hardCollisionStep(world, carts, (double) couplingLength);
        }
    }

    public static void hardCollisionStep(Level world, Couple<AbstractMinecart> carts, double couplingLength) {
        if (!MinecartSim2020.canAddMotion(carts.get(false)) && MinecartSim2020.canAddMotion(carts.get(true))) {
            carts = carts.swap();
        }
        Couple<Vec3> corrections = Couple.create(null, null);
        Couple<Float> maxSpeed = carts.map(IForgeAbstractMinecart::getMaxCartSpeedOnRail);
        boolean firstLoop = true;
        for (boolean current : new boolean[] { true, false, true }) {
            AbstractMinecart cart = carts.get(current);
            AbstractMinecart otherCart = carts.get(!current);
            float stress = (float) (couplingLength - cart.m_20182_().distanceTo(otherCart.m_20182_()));
            if (!(Math.abs(stress) < 0.125F)) {
                RailShape shape = null;
                BlockPos railPosition = cart.getCurrentRailPosition();
                BlockState railState = world.getBlockState(railPosition.above());
                if (railState.m_60734_() instanceof BaseRailBlock) {
                    BaseRailBlock block = (BaseRailBlock) railState.m_60734_();
                    shape = block.getRailDirection(railState, world, railPosition, cart);
                }
                Vec3 correction = Vec3.ZERO;
                Vec3 pos = cart.m_20182_();
                Vec3 link = otherCart.m_20182_().subtract(pos);
                float correctionMagnitude = firstLoop ? -stress / 2.0F : -stress;
                if (!MinecartSim2020.canAddMotion(cart)) {
                    correctionMagnitude /= 2.0F;
                }
                correction = shape != null ? followLinkOnRail(link, pos, correctionMagnitude, MinecartSim2020.getRailVec(shape)).subtract(pos) : link.normalize().scale((double) correctionMagnitude);
                float maxResolveSpeed = 1.75F;
                correction = VecHelper.clamp(correction, Math.min(maxResolveSpeed, maxSpeed.get(current)));
                if (corrections.get(current) == null) {
                    corrections.set(current, correction);
                }
                if (shape != null) {
                    MinecartSim2020.moveCartAlongTrack(cart, correction, railPosition, railState);
                } else {
                    cart.m_6478_(MoverType.SELF, correction);
                    cart.m_20256_(cart.m_20184_().scale(0.95F));
                }
                firstLoop = false;
            }
        }
    }

    public static void softCollisionStep(Level world, Couple<AbstractMinecart> carts, double couplingLength) {
        Couple<Float> maxSpeed = carts.map(IForgeAbstractMinecart::getMaxCartSpeedOnRail);
        Couple<Boolean> canAddmotion = carts.map(MinecartSim2020::canAddMotion);
        Couple<Vec3> motions = carts.map(Entity::m_20184_);
        motions.replaceWithParams(VecHelper::clamp, Couple.create(1.0F, 1.0F));
        Couple<Vec3> nextPositions = carts.map(MinecartSim2020::predictNextPositionOf);
        Couple<RailShape> shapes = carts.mapWithContext((minecart, currentx) -> {
            Vec3 vec = nextPositions.get(currentx);
            int x = Mth.floor(vec.x());
            int y = Mth.floor(vec.y());
            int z = Mth.floor(vec.z());
            BlockPos posx = new BlockPos(x, y - 1, z);
            if (minecart.m_9236_().getBlockState(posx).m_204336_(BlockTags.RAILS)) {
                posx = posx.below();
            }
            BlockState railState = world.getBlockState(posx.above());
            if (!(railState.m_60734_() instanceof BaseRailBlock)) {
                return null;
            } else {
                BaseRailBlock block = (BaseRailBlock) railState.m_60734_();
                return block.getRailDirection(railState, world, posx, minecart);
            }
        });
        float futureStress = (float) (couplingLength - nextPositions.getFirst().distanceTo(nextPositions.getSecond()));
        if (!Mth.equal((double) futureStress, 0.0)) {
            for (boolean current : Iterate.trueAndFalse) {
                Vec3 correction = Vec3.ZERO;
                Vec3 pos = nextPositions.get(current);
                Vec3 link = nextPositions.get(!current).subtract(pos);
                float correctionMagnitude = -futureStress / 2.0F;
                if (canAddmotion.get(current) != canAddmotion.get(!current)) {
                    correctionMagnitude = !canAddmotion.get(current) ? 0.0F : correctionMagnitude * 2.0F;
                }
                if (canAddmotion.get(current)) {
                    RailShape shape = shapes.get(current);
                    if (shape != null) {
                        Vec3 railVec = MinecartSim2020.getRailVec(shape);
                        correction = followLinkOnRail(link, pos, correctionMagnitude, railVec).subtract(pos);
                    } else {
                        correction = link.normalize().scale((double) correctionMagnitude);
                    }
                    correction = VecHelper.clamp(correction, maxSpeed.get(current));
                    motions.set(current, motions.get(current).add(correction));
                }
            }
            motions.replaceWithParams(VecHelper::clamp, maxSpeed);
            carts.forEachWithParams(Entity::m_20256_, motions);
        }
    }

    public static Vec3 followLinkOnRail(Vec3 link, Vec3 cart, float diffToReduce, Vec3 railAxis) {
        double dotProduct = railAxis.dot(link);
        if (!Double.isNaN(dotProduct) && dotProduct != 0.0 && diffToReduce != 0.0F) {
            Vec3 axis = railAxis.scale(-Math.signum(dotProduct));
            Vec3 center = cart.add(link);
            double radius = link.length() - (double) diffToReduce;
            Vec3 intersectSphere = VecHelper.intersectSphere(cart, axis, center, radius);
            return intersectSphere == null ? cart.add(VecHelper.project(link, axis)) : intersectSphere;
        } else {
            return cart;
        }
    }
}