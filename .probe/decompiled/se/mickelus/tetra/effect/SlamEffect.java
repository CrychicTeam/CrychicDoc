package se.mickelus.tetra.effect;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.ServerScheduler;
import se.mickelus.tetra.effect.potion.SmallStrengthPotionEffect;
import se.mickelus.tetra.effect.potion.StunPotionEffect;
import se.mickelus.tetra.effect.revenge.RevengeTracker;
import se.mickelus.tetra.items.modular.ItemModularHandheld;

@ParametersAreNonnullByDefault
public class SlamEffect extends ChargedAbilityEffect {

    public static final SlamEffect instance = new SlamEffect();

    SlamEffect() {
        super(10, 1.0, 40, 6.0, ItemEffect.slam, ChargedAbilityEffect.TargetRequirement.either, UseAnim.SPEAR, "raised");
    }

    private static void groundSlamEntity(Player attacker, LivingEntity target, ItemModularHandheld item, ItemStack itemStack, Vec3 origin, double damageMultiplier, int slowDuration, double momentumEfficiency, int revengeLevel) {
        ServerScheduler.schedule(target.m_20183_().m_123333_(BlockPos.containing(origin)) - 3, () -> {
            float knockback = momentumEfficiency > 0.0 ? 0.1F : 0.5F;
            AbilityUseResult result = item.hitEntity(itemStack, attacker, target, damageMultiplier, knockback, knockback);
            if (momentumEfficiency > 0.0) {
                target.m_20193_().playSound(null, target.m_20183_(), SoundEvents.GENERIC_BIG_FALL, SoundSource.PLAYERS, 1.0F, 0.7F);
            } else {
                target.m_20193_().playSound(null, target.m_20183_(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS, 1.0F, 0.9F);
            }
            if (result != AbilityUseResult.fail) {
                if (slowDuration > 0) {
                    target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, slowDuration, 1, false, true));
                }
                if (momentumEfficiency > 0.0) {
                    double rand = momentumEfficiency * (1.0 - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    target.m_5997_(0.0, rand, 0.0);
                    target.addEffect(new MobEffectInstance(StunPotionEffect.instance, 40, 0, false, false));
                }
                if (revengeLevel > 0 && RevengeTracker.canRevenge(attacker, target)) {
                    target.addEffect(new MobEffectInstance(StunPotionEffect.instance, revengeLevel, 0, false, false));
                    RevengeTracker.removeEnemySynced((ServerPlayer) attacker, target);
                }
            }
            if (result == AbilityUseResult.crit) {
                RandomSource rand = target.getRandom();
                CastOptional.cast(target.m_9236_(), ServerLevel.class).ifPresent(world -> world.sendParticles(ParticleTypes.CRIT, target.m_20185_(), target.m_20186_(), target.m_20189_(), 10, rand.nextGaussian() * 0.3, rand.nextGaussian() * 0.5, rand.nextGaussian() * 0.3, 0.1F));
            }
        });
    }

    @Override
    public int getChargeTime(Player attacker, ItemModularHandheld item, ItemStack itemStack) {
        return ComboPoints.canSpend(item, itemStack) ? (int) ((double) super.getChargeTime(attacker, item, itemStack) * (1.0 - (double) item.getEffectLevel(itemStack, ItemEffect.abilityCombo) / 100.0 * (double) ComboPoints.get(attacker))) : super.getChargeTime(attacker, item, itemStack);
    }

    @Override
    public void perform(Player attacker, InteractionHand hand, ItemModularHandheld item, ItemStack itemStack, LivingEntity target, Vec3 hitVec, int chargedTicks) {
        AbilityUseResult result = this.directSlam(attacker, hand, item, itemStack, target, hitVec, chargedTicks);
        double overextendLevel = (double) item.getEffectLevel(itemStack, ItemEffect.abilityOverextend);
        attacker.causeFoodExhaustion(overextendLevel > 0.0 ? 6.0F : 1.0F);
        attacker.m_21011_(hand, false);
        attacker.getCooldowns().addCooldown(item, Math.round((float) this.getCooldown(item, itemStack) * 1.5F));
        int revengeLevel = item.getEffectLevel(itemStack, ItemEffect.abilityRevenge);
        if (revengeLevel > 0) {
            RevengeTracker.removeEnemy(attacker, target);
        }
        item.tickProgression(attacker, itemStack, result == AbilityUseResult.fail ? 1 : 2);
        item.applyDamage(2, itemStack, attacker);
        int echoLevel = item.getEffectLevel(itemStack, ItemEffect.abilityEcho);
        if (echoLevel > 0) {
            this.echoTarget(attacker, hand, item, itemStack, target, hitVec, chargedTicks);
        }
    }

    public AbilityUseResult directSlam(Player attacker, InteractionHand hand, ItemModularHandheld item, ItemStack itemStack, LivingEntity target, Vec3 hitVec, int chargedTicks) {
        int stunDuration = 0;
        double damageMultiplier = (double) item.getEffectLevel(itemStack, ItemEffect.slam) * 1.5 / 100.0;
        float knockbackBase = item.getEffectEfficiency(itemStack, ItemEffect.slam);
        float knockbackMultiplier = 1.0F;
        boolean isDefensive = this.isDefensive(item, itemStack, hand);
        int overchargeBonus = this.canOvercharge(item, itemStack) ? this.getOverchargeBonus(item, itemStack, chargedTicks) : 0;
        int revengeLevel = item.getEffectLevel(itemStack, ItemEffect.abilityRevenge);
        if (isDefensive) {
            damageMultiplier -= 0.3;
            stunDuration = item.getEffectLevel(itemStack, ItemEffect.abilityDefensive);
        }
        if (overchargeBonus > 0) {
            double bonus = (double) (overchargeBonus * item.getEffectLevel(itemStack, ItemEffect.abilityOvercharge)) / 100.0;
            damageMultiplier += bonus;
            knockbackMultiplier = (float) ((double) knockbackMultiplier + bonus);
        }
        int momentumLevel = item.getEffectLevel(itemStack, ItemEffect.abilityMomentum);
        if (momentumLevel > 0) {
            stunDuration = momentumLevel;
        }
        double momentumEfficiency = (double) item.getEffectEfficiency(itemStack, ItemEffect.abilityMomentum);
        if (momentumEfficiency > 0.0) {
            knockbackMultiplier = 0.4F;
        }
        double overextendLevel = (double) item.getEffectLevel(itemStack, ItemEffect.abilityOverextend);
        if (overextendLevel > 0.0 && !attacker.getFoodData().needsFood()) {
            damageMultiplier += overextendLevel / 100.0;
        }
        AbilityUseResult result = item.hitEntity(itemStack, attacker, target, damageMultiplier, knockbackMultiplier * knockbackBase, knockbackMultiplier / 2.0F);
        if (result != AbilityUseResult.fail) {
            if (stunDuration > 0) {
                target.addEffect(new MobEffectInstance(StunPotionEffect.instance, stunDuration, 0, false, false));
            }
            if (momentumEfficiency > 0.0) {
                double exhilarationEfficiency = momentumEfficiency * (1.0 - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                target.m_5997_(0.0, exhilarationEfficiency, 0.0);
            }
            if (revengeLevel > 0 && RevengeTracker.canRevenge(attacker, target)) {
                target.addEffect(new MobEffectInstance(StunPotionEffect.instance, revengeLevel, 0, false, false));
            }
            double exhilarationEfficiency = (double) item.getEffectEfficiency(itemStack, ItemEffect.abilityExhilaration);
            if (exhilarationEfficiency > 0.0) {
                this.knockbackExhilaration(attacker, attacker.m_20182_(), target, target.m_9236_().getGameTime() + 200L, exhilarationEfficiency);
            }
            RandomSource rand = target.getRandom();
            CastOptional.cast(target.m_9236_(), ServerLevel.class).ifPresent(world -> world.sendParticles(ParticleTypes.CRIT, hitVec.x, hitVec.y, hitVec.z, 10, rand.nextGaussian() * 0.3, rand.nextGaussian() * (double) target.m_20206_() * 0.8, rand.nextGaussian() * 0.3, 0.1F));
            target.m_20193_().playSound(attacker, target.m_20183_(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS, 1.0F, 0.7F);
        } else {
            target.m_20193_().playSound(attacker, target.m_20183_(), SoundEvents.PLAYER_ATTACK_WEAK, SoundSource.PLAYERS, 1.0F, 0.7F);
        }
        return result;
    }

    private void knockbackExhilaration(Player attacker, Vec3 origin, LivingEntity target, long timeLimit, double multiplier) {
        ServerScheduler.schedule(20, () -> {
            if (target.m_20096_()) {
                double distance = Math.min(20.0, origin.distanceTo(target.m_20182_()));
                int amplifier = (int) (distance * multiplier) - 1;
                if (amplifier >= 0) {
                    attacker.m_7292_(new MobEffectInstance(SmallStrengthPotionEffect.instance, 200, amplifier, false, true));
                }
            } else if (target.m_9236_().getGameTime() < timeLimit) {
                this.knockbackExhilaration(attacker, origin, target, timeLimit, multiplier);
            }
        });
    }

    @Override
    public void perform(Player attacker, InteractionHand hand, ItemModularHandheld item, ItemStack itemStack, BlockPos targetPos, Vec3 hitVec, int chargedTicks) {
        if (!attacker.m_9236_().isClientSide) {
            int overchargeBonus = this.canOvercharge(item, itemStack) ? this.getOverchargeBonus(item, itemStack, chargedTicks) : 0;
            int slowDuration = this.isDefensive(item, itemStack, hand) ? (int) (item.getEffectEfficiency(itemStack, ItemEffect.abilityDefensive) * 20.0F) : 0;
            double momentumEfficiency = (double) item.getEffectEfficiency(itemStack, ItemEffect.abilityMomentum);
            int revengeLevel = item.getEffectLevel(itemStack, ItemEffect.abilityRevenge);
            double overextendLevel = (double) item.getEffectLevel(itemStack, ItemEffect.abilityOverextend);
            double range = this.getAoeRange(attacker, item, itemStack, overchargeBonus);
            Vec3 direction = hitVec.subtract(attacker.m_20182_()).multiply(1.0, 0.0, 1.0).normalize();
            double yaw = Mth.atan2(direction.x, direction.z);
            AABB boundingBox = new AABB(hitVec, hitVec).inflate(range + 1.0, 4.0, range + 1.0).move(direction.scale(range / 2.0));
            List<LivingEntity> targets = (List<LivingEntity>) attacker.m_9236_().m_45976_(LivingEntity.class, boundingBox).stream().filter(Entity::m_6084_).filter(Entity::m_6097_).filter(entity -> !attacker.equals(entity)).filter(entity -> this.inRange(hitVec, entity, yaw, range)).collect(Collectors.toList());
            double damageMultiplier = this.getAoeDamageMultiplier(attacker, item, itemStack, slowDuration > 0, overchargeBonus, targets);
            targets.forEach(entity -> groundSlamEntity(attacker, entity, item, itemStack, hitVec, damageMultiplier, slowDuration, momentumEfficiency, revengeLevel));
            this.spawnGroundParticles(attacker.m_9236_(), hitVec, direction, yaw, range);
            attacker.causeFoodExhaustion(overextendLevel > 0.0 ? 6.0F : 1.0F);
            int echoLevel = item.getEffectLevel(itemStack, ItemEffect.abilityEcho);
            if (echoLevel > 0) {
                this.echoGround(attacker, item, itemStack, hitVec, direction, yaw, range, damageMultiplier * (double) echoLevel / 100.0, slowDuration, momentumEfficiency, revengeLevel);
            }
        }
        attacker.m_21011_(hand, false);
        attacker.getCooldowns().addCooldown(item, this.getCooldown(item, itemStack));
        item.tickProgression(attacker, itemStack, 2);
        item.applyDamage(2, itemStack, attacker);
    }

    private void echoGround(Player attacker, ItemModularHandheld item, ItemStack itemStack, Vec3 hitVec, Vec3 direction, double yaw, double range, double damageMultiplier, int slowDuration, double momentumEfficiency, int revengeLevel) {
        EchoHelper.echo(attacker, 60, () -> {
            AABB boundingBox = new AABB(hitVec, hitVec).inflate(range + 1.0, 4.0, range + 1.0).move(direction.scale(range / 2.0));
            List<LivingEntity> targets = (List<LivingEntity>) attacker.m_9236_().m_45976_(LivingEntity.class, boundingBox).stream().filter(Entity::m_6084_).filter(Entity::m_6097_).filter(entity -> this.inRange(hitVec, entity, yaw, range)).collect(Collectors.toList());
            targets.forEach(entity -> groundSlamEntity(attacker, entity, item, itemStack, hitVec, damageMultiplier, slowDuration, momentumEfficiency, revengeLevel));
            this.spawnGroundParticles(attacker.m_9236_(), hitVec, direction, yaw, range);
        });
    }

    private void echoTarget(Player attacker, InteractionHand hand, ItemModularHandheld item, ItemStack itemStack, LivingEntity target, Vec3 hitVec, int chargedTicks) {
        if (!attacker.m_9236_().isClientSide) {
            EchoHelper.echo(attacker, 60, () -> {
                this.directSlam(attacker, hand, item, itemStack, target, hitVec, chargedTicks);
                int revengeLevel = item.getEffectLevel(itemStack, ItemEffect.abilityRevenge);
                if (revengeLevel > 0) {
                    RevengeTracker.removeEnemySynced((ServerPlayer) attacker, target);
                }
            });
        }
    }

    private double getAoeDamageMultiplier(Player attacker, ItemModularHandheld item, ItemStack itemStack, boolean isDefensive, int overchargeBonus, List<LivingEntity> targets) {
        double damageMultiplier = (double) ((float) item.getEffectLevel(itemStack, ItemEffect.slam) / 100.0F);
        if (isDefensive) {
            damageMultiplier -= 0.3;
        }
        if (overchargeBonus > 0) {
            damageMultiplier += (double) (overchargeBonus * item.getEffectLevel(itemStack, ItemEffect.abilityOvercharge)) / 100.0;
        }
        double overextendLevel = (double) item.getEffectLevel(itemStack, ItemEffect.abilityOverextend);
        if (overextendLevel > 0.0 && !attacker.getFoodData().needsFood()) {
            damageMultiplier += overextendLevel / 100.0;
        }
        int exhilarationLevel = item.getEffectLevel(itemStack, ItemEffect.abilityExhilaration);
        if (exhilarationLevel > 0) {
            damageMultiplier += (double) (targets.size() * exhilarationLevel) / 100.0;
        }
        return damageMultiplier;
    }

    private double getAoeRange(Player attacker, ItemModularHandheld item, ItemStack itemStack, int overchargeBonus) {
        double range = 8.0;
        if (overchargeBonus > 0) {
            range += (double) ((float) overchargeBonus * item.getEffectEfficiency(itemStack, ItemEffect.abilityOvercharge));
        }
        double overextendEfficiency = (double) item.getEffectEfficiency(itemStack, ItemEffect.abilityOverextend);
        if (overextendEfficiency > 0.0 && !attacker.getFoodData().needsFood()) {
            range += overextendEfficiency;
        }
        return range;
    }

    private void spawnGroundParticles(Level world, Vec3 origin, Vec3 direction, double yaw, double range) {
        RandomSource rand = world.getRandom();
        BlockState originState = world.getBlockState(BlockPos.containing(origin));
        ((ServerLevel) world).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, originState), origin.x(), origin.y(), origin.z(), 8, 0.0, rand.nextGaussian() * 0.1, 0.0, 0.1);
        world.playSound(null, BlockPos.containing(origin), originState.m_60827_().getBreakSound(), SoundSource.PLAYERS, 1.5F, 0.5F);
        int bound = (int) Math.ceil(range / 2.0);
        BlockPos center = BlockPos.containing(origin.add(direction.scale(range / 2.0)));
        origin = origin.add(direction.scale(-1.0));
        BlockPos.MutableBlockPos targetPos = new BlockPos.MutableBlockPos(0, 0, 0);
        for (int x = -bound; x <= bound; x++) {
            for (int z = -bound; z <= bound; z++) {
                targetPos.setWithOffset(center, x, 0, z);
                if (this.compareAngle(origin, targetPos, yaw)) {
                    for (int y = -2; y < 2; y++) {
                        targetPos.setWithOffset(center, x, y, z);
                        BlockState targetState = world.getBlockState(targetPos);
                        if (targetState.m_60815_() && !world.getBlockState(targetPos.m_7494_()).m_60815_()) {
                            targetPos.m_7495_();
                            double distance = targetPos.m_203198_(origin.x, origin.y, origin.z);
                            if (distance < range * range) {
                                double yOffset = targetState.m_60808_(world, targetPos).bounds().maxY;
                                BlockPos particlePos = targetPos.immutable();
                                ServerScheduler.schedule(particlePos.m_123333_(BlockPos.containing(origin)) - 3, () -> {
                                    ((ServerLevel) world).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, targetState), (double) particlePos.m_123341_() + 0.5, (double) particlePos.m_123342_() + yOffset, (double) particlePos.m_123343_() + 0.5, 3, 0.0, rand.nextGaussian() * 0.1, 0.0, 0.1);
                                    if (rand.nextFloat() < 0.3F) {
                                        world.playSound(null, particlePos, targetState.m_60827_().getFallSound(), SoundSource.PLAYERS, 1.0F, 0.5F);
                                    }
                                });
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean inRange(Vec3 origin, Entity entity, double originYaw, double range) {
        if (origin.closerThan(entity.position(), range)) {
            Vec3 direction = entity.position().subtract(origin);
            double entityYaw = Mth.atan2(direction.x, direction.z);
            double yawDiff = Math.abs((originYaw - entityYaw + (Math.PI * 3)) % (Math.PI * 2) - Math.PI);
            return yawDiff < Math.PI / 6;
        } else {
            return false;
        }
    }

    private boolean compareAngle(Vec3 originPos, BlockPos.MutableBlockPos offsetPos, double originYaw) {
        Vec3 direction = Vec3.atBottomCenterOf(offsetPos).subtract(originPos);
        double offsetYaw = Mth.atan2(direction.x(), direction.z());
        double yawDiff = Math.abs((originYaw - offsetYaw + (Math.PI * 3)) % (Math.PI * 2) - Math.PI);
        return yawDiff < Math.PI / 6;
    }
}