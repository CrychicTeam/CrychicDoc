package com.github.alexmodguy.alexscaves.server.entity.util;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.block.poi.ACPOIRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.AbstractMovingBlockEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.MovingMetalBlockEntity;
import com.github.alexmodguy.alexscaves.server.message.PlayerJumpFromMagnetMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.google.common.base.Predicates;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MagnetUtil {

    private static Stream<BlockPos> getNearbyAttractingMagnets(BlockPos blockpos, ServerLevel world, int range) {
        PoiManager pointofinterestmanager = world.getPoiManager();
        return pointofinterestmanager.findAll(poiTypeHolder -> poiTypeHolder.is(ACPOIRegistry.ATTRACTING_MAGNETS.getKey()), Predicates.alwaysTrue(), blockpos, range, PoiManager.Occupancy.ANY);
    }

    private static Stream<BlockPos> getNearbyRepellingMagnets(BlockPos blockpos, ServerLevel world, int range) {
        PoiManager pointofinterestmanager = world.getPoiManager();
        return pointofinterestmanager.findAll(poiTypeHolder -> poiTypeHolder.is(ACPOIRegistry.REPELLING_MAGNETS.getKey()), Predicates.alwaysTrue(), blockpos, range, PoiManager.Occupancy.ANY);
    }

    public static void tickMagnetism(Entity entity) {
        if (!entity.level().isClientSide && entity.level() instanceof ServerLevel serverLevel) {
            int range = 5;
            Stream<BlockPos> attracts = getNearbyAttractingMagnets(entity.blockPosition(), serverLevel, range);
            Stream<BlockPos> repels = getNearbyRepellingMagnets(entity.blockPosition(), serverLevel, range);
            attracts.forEach(magnet -> {
                Vec3 center = Vec3.atCenterOf(magnet);
                double distance = Mth.clamp(Math.sqrt(entity.distanceToSqr(center)) / (double) range, 0.0, 1.0);
                Vec3 pull = Vec3.atCenterOf(magnet).subtract(entity.position());
                Vec3 pullNorm = pull.length() < 1.0 ? pull : pull.normalize();
                Vec3 pullScale = pullNorm.scale((1.0 - distance) * 0.25);
                setEntityMagneticDelta(entity, getEntityMagneticDelta(entity).scale(0.9).add(pullScale));
            });
            repels.forEach(magnet -> {
                Vec3 center = Vec3.atCenterOf(magnet);
                double distance = Mth.clamp(Math.sqrt(entity.distanceToSqr(center)) / (double) range, 0.0, 1.0);
                Vec3 pull = entity.position().subtract(Vec3.atCenterOf(magnet));
                Vec3 pullNorm = pull.length() < 1.0 ? pull : pull.normalize();
                Vec3 pullScale = pullNorm.scale((1.0 - distance) * 0.25);
                setEntityMagneticDelta(entity, getEntityMagneticDelta(entity).scale(0.9).add(pullScale));
            });
        }
        Vec3 vec3 = getEntityMagneticDelta(entity);
        Direction dir = getEntityMagneticDirection(entity);
        MagneticEntityAccessor magneticAccessor = (MagneticEntityAccessor) entity;
        boolean attatchesToMagnets = AlexsCaves.COMMON_CONFIG.walkingOnMagnets.get() && attachesToMagnets(entity);
        float progress = magneticAccessor.getAttachmentProgress(1.0F);
        if (vec3 != Vec3.ZERO) {
            Direction standingOnDirection = getStandingOnMagnetSurface(entity);
            float overrideByWalking = 1.0F;
            if (entity instanceof LivingEntity living) {
                if (living.jumping && standingOnDirection == dir) {
                    if (living.m_9236_().isClientSide) {
                        AlexsCaves.sendMSGToServer(new PlayerJumpFromMagnetMessage(living.m_19879_(), living.jumping));
                    }
                    magneticAccessor.postMagnetJump();
                }
                float detract = living.xxa * living.xxa + living.yya * living.yya + living.zza * living.zza;
                overrideByWalking = (float) ((double) overrideByWalking - Math.min(1.0, Math.sqrt((double) detract) * 0.7F));
            }
            if (!isEntityOnMovingMetal(entity)) {
                if (!attatchesToMagnets) {
                    entity.setDeltaMovement(entity.getDeltaMovement().add(vec3));
                } else {
                    if (dir == Direction.DOWN && standingOnDirection == null) {
                        Vec3 vec31 = vec3.multiply((double) overrideByWalking, (double) overrideByWalking, (double) overrideByWalking);
                        entity.setDeltaMovement(entity.getDeltaMovement().add(vec31));
                        entity.refreshDimensions();
                    } else {
                        magneticAccessor.stepOnMagnetBlock(getSamplePosForDirection(entity, dir, 0.5F));
                        float f1 = (float) Math.abs(dir.getStepX());
                        float f2 = (float) Math.abs(dir.getStepY());
                        float f3 = (float) Math.abs(dir.getStepZ());
                        Vec3 vec31 = vec3.multiply((double) (overrideByWalking * f1), (double) (overrideByWalking * f2), (double) (overrideByWalking * f3));
                        if (entity.getPose() == Pose.SWIMMING) {
                            entity.setPose(Pose.STANDING);
                        }
                        if (entity instanceof LivingEntity living) {
                            vec31 = processMovementControls(0.0F, living, dir);
                        }
                        entity.setDeltaMovement(vec31);
                    }
                    Direction closest = calculateClosestDirection(entity);
                    if (closest != null && closest != Direction.DOWN) {
                        entity.fallDistance = 0.0F;
                    }
                    if (closest != dir && magneticAccessor.canChangeDirection() && (progress == 1.0F || closest == Direction.UP)) {
                        entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, 0.4F, 0.0));
                        setEntityMagneticDirection(entity, closest);
                        entity.refreshDimensions();
                        entity.setPose(Pose.STANDING);
                    }
                }
            }
            setEntityMagneticDelta(entity, vec3.scale(0.08F));
        }
        if (!attatchesToMagnets && dir != Direction.DOWN) {
            setEntityMagneticDirection(entity, Direction.DOWN);
            entity.refreshDimensions();
            entity.setPose(Pose.STANDING);
        }
    }

    public static boolean isEntityOnMovingMetal(Entity entity) {
        return !(entity instanceof MovingMetalBlockEntity) && !entity.level().m_45976_(MovingMetalBlockEntity.class, entity.getBoundingBox().inflate(0.4F)).isEmpty();
    }

    private static Vec3 processMovementControls(float dist, LivingEntity living, Direction dir) {
        double dSpeed = living.getAttributeValue(Attributes.MOVEMENT_SPEED);
        float jump = living.jumping && getStandingOnMagnetSurface(living) != null ? 0.75F : -0.1F;
        if (dir == Direction.UP) {
            return new Vec3(living.m_20184_().x * 0.98, -living.m_20184_().y - (double) jump, living.m_20184_().z * 0.98);
        } else if (dir == Direction.NORTH) {
            return new Vec3((double) (-living.xxa) * dSpeed * 0.6F, (double) living.zza * dSpeed, (double) jump);
        } else if (dir == Direction.SOUTH) {
            return new Vec3((double) living.xxa * dSpeed * 0.6F, (double) living.zza * dSpeed, (double) (-jump));
        } else if (dir == Direction.EAST) {
            return new Vec3((double) (-jump), (double) living.zza * dSpeed, (double) (-living.xxa) * dSpeed * 0.6F);
        } else {
            return dir == Direction.WEST ? new Vec3((double) jump, (double) living.zza * dSpeed, (double) living.xxa * dSpeed * 0.6F) : living.m_20184_();
        }
    }

    public static Vec3 getEntityMagneticDelta(Entity entity) {
        if (entity instanceof MagneticEntityAccessor magnetic) {
            float f1 = magnetic.getMagneticDeltaX();
            float f2 = magnetic.getMagneticDeltaY();
            float f3 = magnetic.getMagneticDeltaZ();
            if ((double) f1 != 0.0 || (double) f2 != 0.0 || (double) f3 != 0.0) {
                return new Vec3((double) f1, (double) f2, (double) f3);
            }
        }
        return Vec3.ZERO;
    }

    public static void setEntityMagneticDelta(Entity entity, Vec3 vec3) {
        if (entity instanceof MagneticEntityAccessor magnetic) {
            magnetic.setMagneticDeltaX((float) vec3.x);
            magnetic.setMagneticDeltaY((float) vec3.y);
            magnetic.setMagneticDeltaZ((float) vec3.z);
        }
    }

    public static Direction getEntityMagneticDirection(Entity entity) {
        if (entity instanceof MagneticEntityAccessor magnetic && AlexsCaves.COMMON_CONFIG.walkingOnMagnets.get()) {
            return magnetic.getMagneticAttachmentFace();
        }
        return Direction.DOWN;
    }

    public static void setEntityMagneticDirection(Entity entity, Direction direction) {
        if (entity instanceof MagneticEntityAccessor magnetic) {
            magnetic.setMagneticAttachmentFace(direction);
        }
    }

    private static Direction getStandingOnMagnetSurface(Entity entity) {
        if (entity.isShiftKeyDown()) {
            return Direction.DOWN;
        } else {
            for (Direction dir : Direction.values()) {
                BlockPos offsetPos = getSamplePosForDirection(entity, dir, 0.1F);
                BlockState blockState = entity.level().getBlockState(offsetPos);
                if (blockState.m_204336_(ACTagRegistry.MAGNETIC_ATTACHABLES)) {
                    return dir;
                }
            }
            return null;
        }
    }

    private static Direction calculateClosestDirection(Entity entity) {
        Direction closestDirection = Direction.DOWN;
        if (entity.isShiftKeyDown()) {
            return Direction.DOWN;
        } else {
            double closestDistance = (double) (entity.getBbHeight() + entity.getBbWidth());
            Vec3 sampleCenter = new Vec3(entity.getX(), entity.getY(0.5), entity.getZ());
            for (Direction dir : Direction.values()) {
                BlockPos offsetPos = getSamplePosForDirection(entity, dir, 0.50001F);
                BlockState blockState = entity.level().getBlockState(offsetPos);
                Vec3 offset = Vec3.atCenterOf(offsetPos);
                double dist = sampleCenter.distanceTo(offset);
                if ((closestDistance > dist || dir == Direction.UP) && blockState.m_204336_(ACTagRegistry.MAGNETIC_ATTACHABLES)) {
                    closestDistance = dist;
                    closestDirection = dir;
                    if (dir == Direction.UP) {
                        break;
                    }
                }
            }
            return closestDirection;
        }
    }

    private static BlockPos getSamplePosForDirection(Entity entity, Direction direction, float expand) {
        switch(direction) {
            case DOWN:
                return BlockPos.containing(entity.getX(), entity.getBoundingBox().minY - (double) expand, entity.getZ());
            case UP:
                return BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY + (double) expand, entity.getZ());
            case EAST:
                return BlockPos.containing(entity.getBoundingBox().maxX + (double) expand, entity.getY(), entity.getZ());
            case WEST:
                return BlockPos.containing(entity.getBoundingBox().minX - (double) expand, entity.getY(), entity.getZ());
            case NORTH:
                return BlockPos.containing(entity.getX(), entity.getY(), entity.getBoundingBox().minZ - (double) expand);
            case SOUTH:
                return BlockPos.containing(entity.getX(), entity.getY(), entity.getBoundingBox().maxZ + (double) expand);
            default:
                return entity.blockPosition();
        }
    }

    private static boolean isDynamicallyMagnetic(LivingEntity entity, boolean legsOnly) {
        if (entity.hasEffect(ACEffectRegistry.MAGNETIZING.get())) {
            return true;
        } else if (legsOnly) {
            return entity.getItemBySlot(EquipmentSlot.FEET).is(ACTagRegistry.MAGNETIC_ITEMS);
        } else {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (entity.getItemBySlot(slot).is(ACTagRegistry.MAGNETIC_ITEMS)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static boolean isSpectatorPlayer(Entity entity) {
        if (entity instanceof Player player && player.isSpectator()) {
            return true;
        }
        return false;
    }

    public static boolean isPulledByMagnets(Entity entity) {
        if (entity instanceof ItemEntity item) {
            return item.getItem() != null && item.getItem().is(ACTagRegistry.MAGNETIC_ITEMS);
        } else {
            if (entity instanceof LivingEntity living && isDynamicallyMagnetic(living, false) && !isSpectatorPlayer(entity)) {
                return true;
            }
            return !(entity instanceof FallingBlockEntity block) ? entity.getType().is(ACTagRegistry.MAGNETIC_ENTITIES) : block.getBlockState() != null && block.getBlockState().m_204336_(ACTagRegistry.MAGNETIC_BLOCKS);
        }
    }

    public static boolean attachesToMagnets(Entity entity) {
        if (entity instanceof LivingEntity living && isDynamicallyMagnetic(living, true) && !living.m_6144_()) {
            return true;
        }
        return entity.getType().is(ACTagRegistry.MAGNETIC_ENTITIES);
    }

    public static void rotateHead(LivingEntity entity) {
        Direction direction = getEntityMagneticDirection(entity);
        if (direction == Direction.UP) {
            float f = entity.getYHeadRot() - entity.yBodyRot;
            float f1 = entity.yHeadRotO - entity.yBodyRotO;
            entity.setYHeadRot(entity.yBodyRot - f);
            entity.yHeadRotO = entity.yBodyRotO - f1;
            entity.m_146926_(180.0F - entity.m_146909_());
            entity.f_19860_ = 180.0F - entity.f_19860_;
        } else if (direction != Direction.DOWN) {
            float f = entity instanceof Player ? 90.0F : 0.0F;
            entity.m_146926_(entity.m_146909_() + f);
            entity.f_19860_ += f;
        }
    }

    public static Vec3 getEyePositionForAttachment(Entity entity, Direction face, float partialTicks) {
        float progress = ((MagneticEntityAccessor) entity).getAttachmentProgress(partialTicks);
        double eyeHeight = (double) entity.getEyeHeight(Pose.STANDING);
        double d0 = Mth.lerp((double) partialTicks, entity.xo, entity.getX());
        double d1 = Mth.lerp((double) partialTicks, entity.yo, entity.getY());
        double d2 = Mth.lerp((double) partialTicks, entity.zo, entity.getZ());
        Vec3 offset = new Vec3((double) (-face.getStepX()) * eyeHeight, (double) (-face.getStepY()) * eyeHeight, (double) (-face.getStepZ()) * eyeHeight);
        Vec3 from = new Vec3(d0, d1 + eyeHeight, d2);
        Vec3 to = new Vec3(d0, d1, d2).add(offset);
        return from.add(to.subtract(from).scale((double) progress));
    }

    public static List<VoxelShape> getMovingBlockCollisions(@Nullable Entity entity, AABB aabb) {
        if (aabb.getSize() < 1.0E-7) {
            return List.of();
        } else {
            List<AbstractMovingBlockEntity> list = entity.level().m_6443_(AbstractMovingBlockEntity.class, aabb.inflate(1.0E-7), AbstractMovingBlockEntity::movesEntities);
            if (list.isEmpty()) {
                return List.of();
            } else {
                List<VoxelShape> shapes = new ArrayList();
                for (AbstractMovingBlockEntity metalEntity : list) {
                    if (metalEntity != entity) {
                        shapes.add(metalEntity.getShape());
                    }
                }
                return shapes;
            }
        }
    }

    public static void turnEntityOnMagnet(Entity entity, double xBy, double yBy, Direction magneticAttachmentFace) {
        float progress = ((MagneticEntityAccessor) entity).getAttachmentProgress(AlexsCaves.PROXY.getPartialTicks());
        float f = (float) xBy * 0.15F;
        float f1 = (float) yBy * 0.15F * (magneticAttachmentFace == Direction.UP ? -1.0F : 1.0F);
        float magnetOffset = (float) (magneticAttachmentFace == Direction.UP ? -180 : -90) * progress;
        if ((double) progress > 0.0 && progress < 1.0F) {
            entity.setXRot(magnetOffset);
        } else {
            entity.setXRot(entity.getXRot() + f);
        }
        entity.setYRot(entity.getYRot() + f1);
        entity.setXRot(Mth.clamp(entity.getXRot(), -90.0F + magnetOffset, 90.0F + magnetOffset));
        entity.xRotO += f;
        entity.yRotO += f1;
        entity.xRotO = Mth.clamp(entity.xRotO, -90.0F + magnetOffset, 90.0F + magnetOffset);
    }

    public static AABB rotateBoundingBox(EntityDimensions dimensions, Direction dir, Vec3 position) {
        float usualWidth = dimensions.width * 0.5F;
        switch(dir) {
            case EAST:
                return new AABB((double) (-dimensions.height + usualWidth), (double) (dimensions.width * -0.5F), (double) (dimensions.width * -0.5F), (double) usualWidth, (double) (dimensions.width * 0.5F), (double) (dimensions.width * 0.5F)).move(position);
            case WEST:
                return new AABB((double) (-usualWidth), (double) (dimensions.width * -0.5F), (double) (dimensions.width * -0.5F), (double) (dimensions.height - usualWidth), (double) (dimensions.width * 0.5F), (double) (dimensions.width * 0.5F)).move(position);
            case NORTH:
                return new AABB((double) (dimensions.width * -0.5F), (double) (dimensions.width * -0.5F), (double) (-usualWidth), (double) (dimensions.width * 0.5F), (double) (dimensions.width * 0.5F), (double) (dimensions.height - usualWidth)).move(position);
            case SOUTH:
                return new AABB((double) (dimensions.width * -0.5F), (double) (dimensions.width * -0.5F), (double) (-dimensions.height + usualWidth), (double) (dimensions.width * 0.5F), (double) (dimensions.width * 0.5F), (double) usualWidth).move(position);
            default:
                return dimensions.makeBoundingBox(position);
        }
    }
}