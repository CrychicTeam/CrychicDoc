package com.simibubi.create.content.kinetics.belt.transport;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltPart;
import com.simibubi.create.content.kinetics.belt.BeltSlope;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BeltMovementHandler {

    public static boolean canBeTransported(Entity entity) {
        return !entity.isAlive() ? false : !(entity instanceof Player) || !((Player) entity).m_6144_();
    }

    public static void transportEntity(BeltBlockEntity beltBE, Entity entityIn, BeltMovementHandler.TransportedEntityInfo info) {
        BlockPos pos = info.lastCollidedPos;
        Level world = beltBE.m_58904_();
        BlockEntity be = world.getBlockEntity(pos);
        BlockEntity blockEntityBelowPassenger = world.getBlockEntity(entityIn.blockPosition());
        BlockState blockState = info.lastCollidedState;
        Direction movementFacing = Direction.fromAxisAndDirection(((Direction) blockState.m_61143_(BlockStateProperties.HORIZONTAL_FACING)).getAxis(), beltBE.getSpeed() < 0.0F ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE);
        boolean collidedWithBelt = be instanceof BeltBlockEntity;
        boolean betweenBelts = blockEntityBelowPassenger instanceof BeltBlockEntity && blockEntityBelowPassenger != be;
        if (collidedWithBelt && !betweenBelts) {
            boolean notHorizontal = beltBE.m_58900_().m_61143_(BeltBlock.SLOPE) != BeltSlope.HORIZONTAL;
            if (!(Math.abs(beltBE.getSpeed()) < 1.0F)) {
                if (!(entityIn.getY() - 0.25 < (double) pos.m_123342_())) {
                    boolean isPlayer = entityIn instanceof Player;
                    if (entityIn instanceof LivingEntity && !isPlayer) {
                        ((LivingEntity) entityIn).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 1, false, false));
                    }
                    Direction beltFacing = (Direction) blockState.m_61143_(BlockStateProperties.HORIZONTAL_FACING);
                    BeltSlope slope = (BeltSlope) blockState.m_61143_(BeltBlock.SLOPE);
                    Direction.Axis axis = beltFacing.getAxis();
                    float movementSpeed = beltBE.getBeltMovementSpeed();
                    Direction movementDirection = Direction.get(axis == Direction.Axis.X ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE, axis);
                    Vec3i centeringDirection = Direction.get(Direction.AxisDirection.POSITIVE, beltFacing.getClockWise().getAxis()).getNormal();
                    Vec3 movement = Vec3.atLowerCornerOf(movementDirection.getNormal()).scale((double) movementSpeed);
                    double diffCenter = axis == Direction.Axis.Z ? (double) ((float) pos.m_123341_() + 0.5F) - entityIn.getX() : (double) ((float) pos.m_123343_() + 0.5F) - entityIn.getZ();
                    if (!(Math.abs(diffCenter) > 0.75)) {
                        BeltPart part = (BeltPart) blockState.m_61143_(BeltBlock.PART);
                        float top = 0.8125F;
                        boolean onSlope = notHorizontal && (part == BeltPart.MIDDLE || part == BeltPart.PULLEY || part == (slope == BeltSlope.UPWARD ? BeltPart.END : BeltPart.START) && entityIn.getY() - (double) pos.m_123342_() < (double) top || part == (slope == BeltSlope.UPWARD ? BeltPart.START : BeltPart.END) && entityIn.getY() - (double) pos.m_123342_() > (double) top);
                        boolean movingDown = onSlope && slope == (movementFacing == beltFacing ? BeltSlope.DOWNWARD : BeltSlope.UPWARD);
                        boolean movingUp = onSlope && slope == (movementFacing == beltFacing ? BeltSlope.UPWARD : BeltSlope.DOWNWARD);
                        if (beltFacing.getAxis() == Direction.Axis.Z) {
                            boolean b = movingDown;
                            movingDown = movingUp;
                            movingUp = b;
                        }
                        if (movingUp) {
                            movement = movement.add(0.0, Math.abs(axis.choose(movement.x, movement.y, movement.z)), 0.0);
                        }
                        if (movingDown) {
                            movement = movement.add(0.0, -Math.abs(axis.choose(movement.x, movement.y, movement.z)), 0.0);
                        }
                        Vec3 centering = Vec3.atLowerCornerOf(centeringDirection).scale(diffCenter * (double) Math.min(Math.abs(movementSpeed), 0.1F) * 4.0);
                        if (!(entityIn instanceof LivingEntity) || ((LivingEntity) entityIn).zza == 0.0F && ((LivingEntity) entityIn).xxa == 0.0F) {
                            movement = movement.add(centering);
                        }
                        float step = entityIn.maxUpStep();
                        if (!isPlayer) {
                            entityIn.setMaxUpStep(1.0F);
                        }
                        if (Math.abs(movementSpeed) < 0.5F) {
                            Vec3 checkDistance = movement.normalize().scale(0.5);
                            AABB bb = entityIn.getBoundingBox();
                            AABB checkBB = new AABB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
                            checkBB = checkBB.move(checkDistance).inflate(-Math.abs(checkDistance.x), -Math.abs(checkDistance.y), -Math.abs(checkDistance.z));
                            List<Entity> list = world.m_45933_(entityIn, checkBB);
                            list.removeIf(e -> shouldIgnoreBlocking(entityIn, e));
                            if (!list.isEmpty()) {
                                entityIn.setDeltaMovement(0.0, 0.0, 0.0);
                                info.ticksSinceLastCollision--;
                                return;
                            }
                        }
                        entityIn.fallDistance = 0.0F;
                        if (movingUp) {
                            float minVelocity = 0.13F;
                            float yMovement = (float) (-Math.max(Math.abs(movement.y), (double) minVelocity));
                            entityIn.move(MoverType.SELF, new Vec3(0.0, (double) yMovement, 0.0));
                            entityIn.move(MoverType.SELF, movement.multiply(1.0, 0.0, 1.0));
                        } else if (movingDown) {
                            entityIn.move(MoverType.SELF, movement.multiply(1.0, 0.0, 1.0));
                            entityIn.move(MoverType.SELF, movement.multiply(0.0, 1.0, 0.0));
                        } else {
                            entityIn.move(MoverType.SELF, movement);
                        }
                        entityIn.setOnGround(true);
                        if (!isPlayer) {
                            entityIn.setMaxUpStep(step);
                        }
                        boolean movedPastEndingSlope = onSlope && (AllBlocks.BELT.has(world.getBlockState(entityIn.blockPosition())) || AllBlocks.BELT.has(world.getBlockState(entityIn.blockPosition().below())));
                        if (movedPastEndingSlope && !movingDown && Math.abs(movementSpeed) > 0.0F) {
                            entityIn.setPos(entityIn.getX(), entityIn.getY() + movement.y, entityIn.getZ());
                        }
                        if (movedPastEndingSlope) {
                            entityIn.setDeltaMovement(movement);
                            entityIn.hurtMarked = true;
                        }
                    }
                }
            }
        }
    }

    public static boolean shouldIgnoreBlocking(Entity me, Entity other) {
        if (other instanceof HangingEntity) {
            return true;
        } else {
            return other.getPistonPushReaction() == PushReaction.IGNORE ? true : isRidingOrBeingRiddenBy(me, other);
        }
    }

    public static boolean isRidingOrBeingRiddenBy(Entity me, Entity other) {
        for (Entity entity : me.getPassengers()) {
            if (entity.equals(other)) {
                return true;
            }
            if (isRidingOrBeingRiddenBy(entity, other)) {
                return true;
            }
        }
        return false;
    }

    public static class TransportedEntityInfo {

        int ticksSinceLastCollision;

        BlockPos lastCollidedPos;

        BlockState lastCollidedState;

        public TransportedEntityInfo(BlockPos collision, BlockState belt) {
            this.refresh(collision, belt);
        }

        public void refresh(BlockPos collision, BlockState belt) {
            this.ticksSinceLastCollision = 0;
            this.lastCollidedPos = new BlockPos(collision).immutable();
            this.lastCollidedState = belt;
        }

        public BeltMovementHandler.TransportedEntityInfo tick() {
            this.ticksSinceLastCollision++;
            return this;
        }

        public int getTicksSinceLastCollision() {
            return this.ticksSinceLastCollision;
        }
    }
}