package yesman.epicfight.api.utils;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import yesman.epicfight.api.utils.math.QuaternionUtils;
import yesman.epicfight.api.utils.math.Vec2i;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPFracture;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.damagesource.EpicFightDamageSources;
import yesman.epicfight.world.damagesource.EpicFightDamageType;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.level.block.FractureBlock;
import yesman.epicfight.world.level.block.FractureBlockState;

public class LevelUtil {

    private static final Vec3 IMPACT_DIRECTION = new Vec3(0.0, -1.0, 0.0);

    public static int calculateLivingEntityFallDamage(LivingEntity livingEntity, float distance, float modifier) {
        if (livingEntity.m_6095_().is(EntityTypeTags.FALL_DAMAGE_IMMUNE)) {
            return 0;
        } else {
            MobEffectInstance mobeffectinstance = livingEntity.getEffect(MobEffects.JUMP);
            float f = mobeffectinstance == null ? 0.0F : (float) (mobeffectinstance.getAmplifier() + 1);
            return Mth.ceil((distance - 3.0F - f) * modifier);
        }
    }

    public static void spreadShockwave(Level level, Vec3 center, Vec3 direction, double length, int edgeX, int edgeZ, List<Entity> entityBeingHit) {
        Vec3 edgeOfShockwave = center.add(direction.normalize().scale((double) ((float) length)));
        int xFrom = (int) Math.min(Math.floor(center.x), (double) edgeX);
        int xTo = (int) Math.max(Math.floor(center.x), (double) edgeX);
        int zFrom = (int) Math.min(Math.floor(center.z), (double) edgeZ);
        int zTo = (int) Math.max(Math.floor(center.z), (double) edgeZ);
        List<Vec2i> affectedBlocks = Lists.newArrayList();
        List<Entity> entitiesInArea = level.isClientSide ? null : level.m_45933_(null, new AABB((double) xFrom, center.y - length, (double) zFrom, (double) xTo, center.y + length, (double) zTo));
        double bounceExponentCoef = Math.min(1.0 / (length * length), 0.1);
        for (int k = zFrom; k <= zTo; k++) {
            for (int l = xFrom; l <= xTo; l++) {
                Vec2i blockCoord = new Vec2i(l, k);
                if (isBlockOverlapLine(blockCoord, center, edgeOfShockwave)) {
                    affectedBlocks.add(blockCoord);
                }
            }
        }
        affectedBlocks.sort((v1, v2) -> {
            double v1DistSqr = Math.pow((double) v1.x - center.x, 2.0) + Math.pow((double) v1.y - center.z, 2.0);
            double v2DistSqr = Math.pow((double) v2.x - center.x, 2.0) + Math.pow((double) v2.y - center.z, 2.0);
            if (v1DistSqr > v2DistSqr) {
                return 1;
            } else {
                return v1DistSqr == v2DistSqr ? 0 : -1;
            }
        });
        double y = center.y;
        for (Vec2i block : affectedBlocks) {
            BlockPos bp = new BlockPos.MutableBlockPos((double) block.x, y, (double) block.y);
            BlockState bs = level.getBlockState(bp);
            BlockPos aboveBp = bp.above();
            BlockState aboveState = level.getBlockState(aboveBp);
            if (canTransferShockWave(level, aboveBp, aboveState)) {
                BlockPos aboveTwoBp = aboveBp.above();
                BlockState aboveTwoState = level.getBlockState(aboveTwoBp);
                if (canTransferShockWave(level, aboveTwoBp, aboveTwoState)) {
                    break;
                }
                y++;
                bp = aboveBp;
                bs = aboveState;
            } else if (!level.isClientSide && aboveState.m_60742_(level, aboveBp, CollisionContext.empty()).isEmpty() && level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                level.m_46961_(aboveBp, level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS));
            }
            if (!canTransferShockWave(level, bp, bs)) {
                BlockPos belowBp = bp.below();
                BlockState belowState = level.getBlockState(belowBp);
                if (!canTransferShockWave(level, belowBp, belowState)) {
                    break;
                }
                y--;
                bp = belowBp;
                bs = belowState;
            }
            Vec3 blockCenter = new Vec3((double) bp.m_123341_() + 0.5, (double) bp.m_123342_(), (double) bp.m_123343_() + 0.5);
            Vec3 centerToBlock = blockCenter.subtract(center);
            double distance = centerToBlock.horizontalDistance();
            if (!(length < distance)) {
                if (level.isClientSide) {
                    if (canTransferShockWave(level, bp, bs) && !(bs instanceof FractureBlockState)) {
                        Vec3 rotAxis = IMPACT_DIRECTION.cross(centerToBlock).normalize();
                        Vector3f axis = new Vector3f((float) rotAxis.x, (float) rotAxis.y, (float) rotAxis.z);
                        Vector3f translator = new Vector3f(0.0F, Math.max(0.0F, (float) (distance / length) - 0.5F) * 0.5F, 0.0F);
                        Quaternionf rotator = QuaternionUtils.rotationDegrees(axis, (float) (distance / length) * 15.0F + level.random.nextFloat() * 10.0F - 5.0F);
                        rotator.mul(QuaternionUtils.XP.rotationDegrees(level.random.nextFloat() * 15.0F - 7.5F));
                        rotator.mul(QuaternionUtils.YP.rotationDegrees(level.random.nextFloat() * 40.0F - 20.0F));
                        rotator.mul(QuaternionUtils.ZP.rotationDegrees(level.random.nextFloat() * 15.0F - 7.5F));
                        int lifeTime = 30 + level.random.nextInt((int) length * 80);
                        double bouncing = Math.pow(distance, 2.0) * bounceExponentCoef;
                        FractureBlockState fractureBlockState = FractureBlock.getDefaultFractureBlockState(null);
                        fractureBlockState.setFractureInfo(bp, bs, translator, rotator, bouncing, lifeTime);
                        level.setBlock(bp, fractureBlockState, 0);
                        createParticle(level, bp, bs);
                    }
                } else {
                    for (Entity entity : entitiesInArea) {
                        boolean inSameY = (double) (bp.m_123342_() + 1) >= entity.getY() && (double) bp.m_123342_() <= entity.getY();
                        if (bp.m_123341_() == entity.getBlockX() && inSameY && bp.m_123343_() == entity.getBlockZ() && !entityBeingHit.contains(entity)) {
                            entityBeingHit.add(entity);
                        }
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void createParticle(Level level, BlockPos bp, BlockState bs) {
        int i = 0;
        while (i < 4) {
            double x = (double) (bp.m_123341_() + i % 2);
            double z = (double) (bp.m_123343_() + 1 - i % 2);
            TerrainParticle blockParticle = new TerrainParticle((ClientLevel) level, x, (double) (bp.m_123342_() + 1), z, 0.0, 0.0, 0.0, bs, bp);
            blockParticle.m_172260_((Math.random() - 0.5) * 0.3, Math.random() * 0.5, (Math.random() - 0.5) * 0.3);
            blockParticle.m_107257_(10 + new Random().nextInt(60));
            Minecraft mc = Minecraft.getInstance();
            mc.particleEngine.add(blockParticle);
            i += level.getRandom().nextInt(4);
        }
    }

    public static boolean circleSlamFracture(@Nullable LivingEntity caster, Level level, Vec3 center, double radius) {
        return circleSlamFracture(caster, level, center, radius, false, false, true);
    }

    public static boolean circleSlamFracture(@Nullable LivingEntity caster, Level level, Vec3 center, double radius, boolean hurtEntities) {
        return circleSlamFracture(caster, level, center, radius, false, false, hurtEntities);
    }

    public static boolean circleSlamFracture(@Nullable LivingEntity caster, Level level, Vec3 center, double radius, boolean noSound, boolean noParticle) {
        return circleSlamFracture(caster, level, center, radius, noSound, noParticle, true);
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean circleSlamFracture(@Nullable LivingEntity caster, ClientLevel level, Vec3 center, double radius, boolean noSound, boolean noParticle) {
        return circleSlamFracture(caster, level, center, radius, noSound, noParticle, true);
    }

    public static boolean circleSlamFracture(@Nullable LivingEntity caster, Level level, Vec3 center, double radius, boolean noSound, boolean noParticle, boolean hurtEntities) {
        Vec3 closestEdge = new Vec3((double) Math.round(center.x), Math.floor(center.y), (double) Math.round(center.z));
        Vec3 centerOfBlock = new Vec3(Math.floor(center.x) + 0.5, Math.floor(center.y), Math.floor(center.z) + 0.5);
        if (closestEdge.distanceToSqr(center) < centerOfBlock.distanceToSqr(center)) {
            center = closestEdge;
        } else {
            center = centerOfBlock;
        }
        BlockPos blockPos = new BlockPos.MutableBlockPos(center.x, center.y, center.z);
        BlockState originBlockState = level.getBlockState(blockPos);
        if (!canTransferShockWave(level, blockPos, originBlockState)) {
            return false;
        } else {
            radius = Math.max(0.5, radius);
            if (!level.isClientSide) {
                EpicFightNetworkManager.sendToAllPlayerTrackingThisChunkWithSelf(new SPFracture(center, radius, noSound, noParticle), level.getChunkAt(blockPos));
            }
            int xFrom = (int) Math.floor(center.x - radius);
            int xTo = (int) Math.ceil(center.x + radius);
            int zFrom = (int) Math.floor(center.z - radius);
            int zTo = (int) Math.ceil(center.z + radius);
            List<Entity> entityBeingHit = Lists.newArrayList();
            for (int i = zFrom; i <= zTo; i++) {
                for (int j = xFrom; j <= xTo; j += i != zFrom && i != zTo ? xTo - xFrom : 1) {
                    Vec3 direction = new Vec3((double) j - center.x + 0.1, 0.0, (double) i - center.z);
                    spreadShockwave(level, center, direction, radius, j, i, entityBeingHit);
                }
            }
            if (!level.isClientSide && hurtEntities) {
                for (Entity entity : entityBeingHit) {
                    if (!entity.is(caster)) {
                        double damageInflict = 1.0 - (entity.position().distanceTo(center) - radius * 0.5) / radius;
                        float damage = (float) (radius * 2.0 * Math.min(damageInflict, 1.0));
                        EpicFightDamageSources damageSources = EpicFightDamageSources.of(entity.level());
                        entity.hurt(damageSources.shockwave(caster).setAnimation(Animations.DUMMY_ANIMATION).setInitialPosition(center).addRuntimeTag(EpicFightDamageType.FINISHER).setStunType(StunType.KNOCKDOWN).addRuntimeTag(DamageTypes.EXPLOSION), damage);
                    }
                }
            } else {
                boolean smallSlam = radius < 1.5;
                if (!noSound) {
                    level.playLocalSound(center.x, center.y, center.z, smallSlam ? EpicFightSounds.GROUND_SLAM_SMALL.get() : EpicFightSounds.GROUND_SLAM.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
                }
                if (!smallSlam && !noParticle) {
                    level.addParticle(EpicFightParticles.GROUND_SLAM.get(), center.x, center.y, center.z, 1.0, radius * 10.0, 0.5);
                }
            }
            return true;
        }
    }

    public static boolean canTransferShockWave(Level level, BlockPos blockPos, BlockState blockState) {
        return Block.isFaceFull(blockState.m_60742_(level, blockPos, CollisionContext.empty()), Direction.DOWN) || blockState instanceof FractureBlockState;
    }

    private static boolean isBlockOverlapLine(Vec2i vec2, Vec3 from, Vec3 to) {
        return isLinesCross((double) vec2.x, (double) vec2.y, (double) (vec2.x + 1), (double) vec2.y, from.x, from.z, to.x, to.z) || isLinesCross((double) vec2.x, (double) vec2.y, (double) vec2.x, (double) (vec2.y + 1), from.x, from.z, to.x, to.z) || isLinesCross((double) (vec2.x + 1), (double) vec2.y, (double) (vec2.x + 1), (double) (vec2.y + 1), from.x, from.z, to.x, to.z) || isLinesCross((double) vec2.x, (double) (vec2.y + 1), (double) (vec2.x + 1), (double) (vec2.y + 1), from.x, from.z, to.x, to.z);
    }

    private static boolean isLinesCross(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double u = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / ((x2 - x1) * (y4 - y3) - (x4 - x3) * (y2 - y1));
        double v = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / ((x2 - x1) * (y4 - y3) - (x4 - x3) * (y2 - y1));
        return 0.0 < u && u < 1.0 && 0.0 < v && v < 1.0;
    }
}