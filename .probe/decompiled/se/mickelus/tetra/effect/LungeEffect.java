package se.mickelus.tetra.effect;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.TetraMod;
import se.mickelus.tetra.effect.potion.ExhaustedPotionEffect;
import se.mickelus.tetra.effect.potion.StunPotionEffect;
import se.mickelus.tetra.effect.revenge.RevengeTracker;
import se.mickelus.tetra.items.modular.ItemModularHandheld;

@ParametersAreNonnullByDefault
public class LungeEffect extends ChargedAbilityEffect {

    public static final LungeEffect instance = new LungeEffect();

    private static final Cache<Integer, LungeEffect.LungeData> activeCache = CacheBuilder.newBuilder().maximumSize(20L).expireAfterWrite(30L, TimeUnit.SECONDS).build();

    LungeEffect() {
        super(5, 0.5, 60, 6.5, ItemEffect.lunge, ChargedAbilityEffect.TargetRequirement.none, UseAnim.SPEAR, "raised");
    }

    public static void onPlayerTick(Player player) {
        LungeEffect.LungeData data = (LungeEffect.LungeData) activeCache.getIfPresent(getIdentifier(player));
        if (data != null && !player.m_20159_()) {
            if (!player.m_20096_()) {
                AABB axisalignedbb = player.m_20191_().inflate(0.2, 0.0, 0.2).move(player.m_20184_());
                player.m_9236_().m_45976_(LivingEntity.class, axisalignedbb).stream().filter(Entity::m_6084_).filter(Entity::m_6087_).filter(Entity::m_6097_).filter(entity -> !player.equals(entity)).findAny().ifPresent(entity -> onEntityImpact(player, entity, data));
            } else {
                activeCache.invalidate(getIdentifier(player));
                if (data.exhaustDuration > 0.0F) {
                    player.m_7292_(new MobEffectInstance(ExhaustedPotionEffect.instance, (int) (data.exhaustDuration * 20.0F), 4, false, true));
                }
            }
        }
    }

    private static void onEntityImpact(Player player, LivingEntity target, LungeEffect.LungeData data) {
        ItemStack itemStack = data.itemStack;
        ItemModularHandheld item = (ItemModularHandheld) CastOptional.cast(itemStack.getItem(), ItemModularHandheld.class).orElse(null);
        if (item == null) {
            activeCache.invalidate(getIdentifier(player));
        } else {
            Vec3 bounceVector = player.m_20182_().subtract(target.m_20182_()).normalize().scale(0.1F);
            player.m_20256_(bounceVector);
            player.f_19864_ = true;
            float cooldownMultiplier = data.hitCooldown;
            int momentumLevel = item.getEffectLevel(itemStack, ItemEffect.abilityMomentum);
            if (RevengeTracker.canRevenge(player) && RevengeTracker.canRevenge(player, target)) {
                cooldownMultiplier = 0.0F;
                RevengeTracker.removeEnemy(player, target);
            }
            if (!player.m_9236_().isClientSide) {
                double bonusDamage = 0.0;
                if (momentumLevel > 0) {
                    bonusDamage = (double) Math.min((float) momentumLevel, player.f_19789_);
                }
                int lungeLevel = item.getEffectLevel(itemStack, ItemEffect.lunge);
                AbilityUseResult result = item.hitEntity(itemStack, player, target, (double) (data.damageMultiplierOffset + (float) lungeLevel / 100.0F), bonusDamage, 0.5F, 0.5F);
                if (result != AbilityUseResult.fail && momentumLevel > 0) {
                    int duration = 10 + (int) (Math.min((float) momentumLevel, player.f_19789_) * item.getEffectEfficiency(itemStack, ItemEffect.abilityMomentum) * 20.0F);
                    target.addEffect(new MobEffectInstance(StunPotionEffect.instance, duration, 0, false, false));
                    spawnMomentumParticles(target, bonusDamage);
                }
                target.m_20193_().playSound(null, target.m_20183_(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, SoundSource.PLAYERS, 1.0F, 0.8F);
                player.m_21011_(InteractionHand.MAIN_HAND, true);
            }
            if (momentumLevel > 0) {
                player.f_19789_ = Math.max(0.0F, player.f_19789_ - (float) momentumLevel);
            }
            item.tickProgression(player, itemStack, 2);
            item.applyDamage(2, itemStack, player);
            player.getCooldowns().addCooldown(item, (int) ((float) instance.getCooldown(item, itemStack) * cooldownMultiplier));
            activeCache.invalidate(getIdentifier(player));
        }
    }

    private static void spawnMomentumParticles(LivingEntity target, double bonus) {
        BlockPos pos = BlockPos.containing(target.m_20185_(), target.m_20186_() - 0.2, target.m_20189_());
        BlockState blockState = target.m_9236_().getBlockState(pos);
        if (!blockState.m_60795_()) {
            ((ServerLevel) target.m_9236_()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, blockState), target.m_20185_(), target.m_20186_(), target.m_20189_(), (int) (bonus * 8.0) + 20, 0.0, 0.0, 0.0, 0.15);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onRightClick(LocalPlayer player) {
        LungeEffect.LungeData data = (LungeEffect.LungeData) activeCache.getIfPresent(getIdentifier(player));
        if (data != null && data.echoCount > 0) {
            TetraMod.packetHandler.sendToServer(new LungeEchoPacket());
            echo(player, data, false);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onJump(LocalPlayer player) {
        LungeEffect.LungeData data = (LungeEffect.LungeData) activeCache.getIfPresent(getIdentifier(player));
        if (data != null && data.echoCount > 0) {
            TetraMod.packetHandler.sendToServer(new LungeEchoPacket(true));
            echo(player, data, true);
        }
    }

    public static void receiveEchoPacket(Player player, boolean isVertical) {
        LungeEffect.LungeData data = (LungeEffect.LungeData) activeCache.getIfPresent(getIdentifier(player));
        if (data != null && data.echoCount > 0) {
            echo(player, data, isVertical);
        }
    }

    public static void echo(Player entity, LungeEffect.LungeData data, boolean isVertical) {
        Vec3 lookVector = entity.m_20154_();
        entity.m_20256_(lookVector.scale(entity.m_20184_().dot(lookVector) / lookVector.dot(lookVector)));
        double strength = data.echoStrength;
        if (isVertical) {
            strength *= 0.3;
        }
        entity.m_5997_(lookVector.x * strength, lookVector.y * strength, lookVector.z * strength);
        if (entity.m_6047_()) {
            entity.m_20256_(entity.m_20184_().scale(-0.8));
        }
        if (isVertical) {
            Vec3 motion = entity.m_20184_();
            double vertical = motion.y > 0.0 ? motion.y + strength * 0.5 : strength * 0.5;
            entity.m_20334_(motion.x, vertical, motion.z);
        }
        if (entity.m_20184_().y > 0.0) {
            entity.f_19789_ = 0.0F;
        }
        entity.f_19864_ = true;
        entity.m_20193_().playSound(entity, BlockPos.containing(entity.m_20182_().add(entity.m_20184_())), SoundEvents.UI_TOAST_IN, SoundSource.PLAYERS, 1.0F, 1.3F);
        if (!entity.m_9236_().isClientSide) {
            RandomSource rand = entity.m_217043_();
            ((ServerLevel) entity.m_9236_()).sendParticles(ParticleTypes.WITCH, entity.m_20185_() + (rand.nextGaussian() - 0.5) * 0.5, entity.m_20186_(), entity.m_20189_() + (rand.nextGaussian() - 0.5) * 0.5, 5, (double) rand.nextFloat() * 0.2, -0.2 + (double) rand.nextFloat() * -0.6, (double) rand.nextFloat() * 0.2, 0.0);
        }
        data.echoCount--;
    }

    private static int getIdentifier(Player entity) {
        return entity.m_9236_().isClientSide ? -entity.m_19879_() : entity.m_19879_();
    }

    @Override
    public int getChargeTime(Player attacker, ItemModularHandheld item, ItemStack itemStack) {
        return ComboPoints.canSpend(item, itemStack) ? (int) ((double) super.getChargeTime(attacker, item, itemStack) * (1.0 - (double) item.getEffectLevel(itemStack, ItemEffect.abilityCombo) / 100.0 * (double) ComboPoints.get(attacker))) : super.getChargeTime(attacker, item, itemStack);
    }

    @Override
    public void perform(Player attacker, InteractionHand hand, ItemModularHandheld item, ItemStack itemStack, @Nullable LivingEntity target, @Nullable BlockPos targetPos, @Nullable Vec3 hitVec, int chargedTicks) {
        if (attacker.m_20096_()) {
            float damageMultiplierOffset = 0.0F;
            float strength = 1.0F + (float) EnchantmentHelper.getItemEnchantmentLevel(Enchantments.KNOCKBACK, itemStack) * 0.5F;
            Vec3 lookVector = attacker.m_20154_();
            double verticalVelocityFactor = 0.8;
            float hitCooldown = 0.7F;
            float exhaustDuration = 0.0F;
            double echoStrength = 0.0;
            if (this.canOvercharge(item, itemStack)) {
                int overcharge = this.getOverchargeBonus(item, itemStack, chargedTicks);
                strength += (float) (overcharge * item.getEffectLevel(itemStack, ItemEffect.abilityOvercharge)) / 100.0F;
                damageMultiplierOffset += item.getEffectEfficiency(itemStack, ItemEffect.abilityOvercharge) / 100.0F;
            }
            double comboEfficiency = (double) item.getEffectEfficiency(itemStack, ItemEffect.abilityCombo);
            if (comboEfficiency > 0.0) {
                hitCooldown = (float) ((double) hitCooldown - comboEfficiency * (double) ComboPoints.get(attacker) / 100.0);
            }
            int momentumLevel = item.getEffectLevel(itemStack, ItemEffect.abilityMomentum);
            if (momentumLevel > 0) {
                verticalVelocityFactor = 1.2;
            }
            int overextendLevel = item.getEffectLevel(itemStack, ItemEffect.abilityOverextend);
            if (overextendLevel > 0 && !attacker.getFoodData().needsFood()) {
                damageMultiplierOffset = (float) ((double) damageMultiplierOffset + (double) overextendLevel / 100.0);
                strength += item.getEffectEfficiency(itemStack, ItemEffect.abilityOverextend);
                verticalVelocityFactor += 0.1;
            }
            int exhilarateLevel = item.getEffectLevel(itemStack, ItemEffect.abilityExhilaration);
            if (exhilarateLevel > 0) {
                damageMultiplierOffset += (float) exhilarateLevel / 100.0F;
                strength += item.getEffectEfficiency(itemStack, ItemEffect.abilityExhilaration);
                exhaustDuration += 15.0F;
            }
            int echoLevel = item.getEffectLevel(itemStack, ItemEffect.abilityEcho);
            if (echoLevel > 0) {
                echoStrength = (double) item.getEffectEfficiency(itemStack, ItemEffect.abilityEcho);
            }
            if (this.isDefensive(item, itemStack, hand)) {
                lookVector = lookVector.multiply(-1.2, 0.0, -1.2).add(0.0, 0.4, 0.0);
            } else {
                activeCache.put(getIdentifier(attacker), new LungeEffect.LungeData(itemStack, damageMultiplierOffset, hitCooldown, exhaustDuration, echoLevel, echoStrength));
            }
            attacker.m_20256_(lookVector.scale(attacker.m_20184_().dot(lookVector) / lookVector.dot(lookVector)));
            attacker.m_5997_(lookVector.x * (double) strength, Mth.clamp(lookVector.y * (double) strength * verticalVelocityFactor + 0.3, 0.3, verticalVelocityFactor), lookVector.z * (double) strength);
            attacker.f_19864_ = true;
            attacker.m_6478_(MoverType.SELF, new Vec3(0.0, 0.4, 0.0));
            attacker.causeFoodExhaustion(overextendLevel > 0 ? 6.0F : 1.0F);
            attacker.getCooldowns().addCooldown(item, this.getCooldown(item, itemStack));
            attacker.m_9236_().playSound(attacker, BlockPos.containing(attacker.m_20182_().add(attacker.m_20184_())), SoundEvents.UI_TOAST_IN, SoundSource.PLAYERS, 1.0F, 1.3F);
        }
        if (ComboPoints.canSpend(item, itemStack)) {
            ComboPoints.reset(attacker);
        }
    }

    static class LungeData {

        ItemStack itemStack;

        float damageMultiplierOffset;

        float hitCooldown;

        float exhaustDuration;

        int echoCount;

        double echoStrength;

        public LungeData(ItemStack itemStack, float damageMultiplierOffset, float hitCooldown, float exhaustDuration, int echoCount, double echoStrength) {
            this.itemStack = itemStack;
            this.damageMultiplierOffset = damageMultiplierOffset;
            this.hitCooldown = hitCooldown;
            this.exhaustDuration = exhaustDuration;
            this.echoCount = echoCount;
            this.echoStrength = echoStrength;
        }
    }
}