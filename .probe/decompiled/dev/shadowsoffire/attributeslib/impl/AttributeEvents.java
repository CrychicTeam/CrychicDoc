package dev.shadowsoffire.attributeslib.impl;

import dev.shadowsoffire.attributeslib.ALConfig;
import dev.shadowsoffire.attributeslib.AttributesLib;
import dev.shadowsoffire.attributeslib.api.ALObjects;
import dev.shadowsoffire.attributeslib.api.AttributeChangedValueEvent;
import dev.shadowsoffire.attributeslib.api.AttributeHelper;
import dev.shadowsoffire.attributeslib.api.IFormattableAttribute;
import dev.shadowsoffire.attributeslib.packet.CritParticleMessage;
import dev.shadowsoffire.attributeslib.util.AttributesUtil;
import dev.shadowsoffire.attributeslib.util.IFlying;
import dev.shadowsoffire.placebo.network.PacketDistro;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AttributeEvents {

    private static boolean noRecurse = false;

    private static Random dodgeRand = new Random();

    @SubscribeEvent
    public void fixChangedAttributes(PlayerEvent.PlayerLoggedInEvent e) {
        AttributeMap map = e.getEntity().m_21204_();
        map.getInstance(ForgeMod.STEP_HEIGHT_ADDITION.get()).setBaseValue(0.6);
    }

    private boolean canBenefitFromDrawSpeed(ItemStack stack) {
        return stack.getItem() instanceof ProjectileWeaponItem || stack.getItem() instanceof TridentItem;
    }

    @SubscribeEvent
    public void drawSpeed(LivingEntityUseItemEvent.Tick e) {
        if (e.getEntity() instanceof Player player) {
            double t = player.m_21051_(ALObjects.Attributes.DRAW_SPEED.get()).getValue() - 1.0;
            if (t == 0.0 || !this.canBenefitFromDrawSpeed(e.getItem())) {
                return;
            }
            int offset = -1;
            if (t < 0.0) {
                offset = 1;
                t = -t;
            }
            while (t > 1.0) {
                e.setDuration(e.getDuration() + offset);
                t--;
            }
            if (t > 0.5) {
                if (e.getEntity().f_19797_ % 2 == 0) {
                    e.setDuration(e.getDuration() + offset);
                }
                t -= 0.5;
            }
            int mod = (int) Math.floor(1.0 / Math.min(1.0, t));
            if (e.getEntity().f_19797_ % mod == 0) {
                e.setDuration(e.getDuration() + offset);
            }
            t--;
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void lifeStealOverheal(LivingHurtEvent e) {
        if (e.getSource().getDirectEntity() instanceof LivingEntity attacker && AttributesUtil.isPhysicalDamage(e.getSource())) {
            float lifesteal = (float) attacker.getAttributeValue(ALObjects.Attributes.LIFE_STEAL.get());
            float dmg = Math.min(e.getAmount(), e.getEntity().getHealth());
            if ((double) lifesteal > 0.001) {
                attacker.heal(dmg * lifesteal);
            }
            float overheal = (float) attacker.getAttributeValue(ALObjects.Attributes.OVERHEAL.get());
            float maxOverheal = attacker.getMaxHealth() * 0.5F;
            if (overheal > 0.0F && attacker.getAbsorptionAmount() < maxOverheal) {
                attacker.setAbsorptionAmount(Math.min(maxOverheal, attacker.getAbsorptionAmount() + dmg * overheal));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void meleeDamageAttributes(LivingAttackEvent e) {
        if (!e.getEntity().m_9236_().isClientSide && !e.getEntity().isDeadOrDying()) {
            if (!noRecurse) {
                noRecurse = true;
                if (e.getSource().getDirectEntity() instanceof LivingEntity attacker && AttributesUtil.isPhysicalDamage(e.getSource())) {
                    float hpDmg = (float) attacker.getAttributeValue(ALObjects.Attributes.CURRENT_HP_DAMAGE.get());
                    float fireDmg = (float) attacker.getAttributeValue(ALObjects.Attributes.FIRE_DAMAGE.get());
                    float coldDmg = (float) attacker.getAttributeValue(ALObjects.Attributes.COLD_DAMAGE.get());
                    LivingEntity target = e.getEntity();
                    int time = target.f_19802_;
                    target.f_19802_ = 0;
                    if ((double) hpDmg > 0.001 && AttributesLib.localAtkStrength >= 0.85F) {
                        target.hurt(src(ALObjects.DamageTypes.CURRENT_HP_DAMAGE, attacker), AttributesLib.localAtkStrength * hpDmg * target.getHealth());
                    }
                    target.f_19802_ = 0;
                    if ((double) fireDmg > 0.001 && AttributesLib.localAtkStrength >= 0.55F) {
                        target.hurt(src(ALObjects.DamageTypes.FIRE_DAMAGE, attacker), AttributesLib.localAtkStrength * fireDmg);
                        target.m_7311_(target.m_20094_() + (int) (10.0F * fireDmg));
                    }
                    target.f_19802_ = 0;
                    if ((double) coldDmg > 0.001 && AttributesLib.localAtkStrength >= 0.55F) {
                        target.hurt(src(ALObjects.DamageTypes.COLD_DAMAGE, attacker), AttributesLib.localAtkStrength * coldDmg);
                        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (int) (15.0F * coldDmg), Mth.floor(coldDmg / 5.0F)));
                    }
                    target.f_19802_ = time;
                    if (target.isDeadOrDying()) {
                        target.getPersistentData().putBoolean("apoth.killed_by_aux_dmg", true);
                    }
                }
                noRecurse = false;
            }
        }
    }

    private static DamageSource src(ResourceKey<DamageType> type, LivingEntity entity) {
        return entity.m_9236_().damageSources().source(type, entity);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void apothCriticalStrike(LivingHurtEvent e) {
        LivingEntity attacker = e.getSource().getEntity() instanceof LivingEntity le ? le : null;
        if (attacker != null) {
            double critChance = attacker.getAttributeValue(ALObjects.Attributes.CRIT_CHANCE.get());
            float critDmg = (float) attacker.getAttributeValue(ALObjects.Attributes.CRIT_DAMAGE.get());
            RandomSource rand = e.getEntity().getRandom();
            float critMult;
            for (critMult = 1.0F; (double) rand.nextFloat() <= critChance && critDmg > 1.0F; critDmg *= 0.85F) {
                critChance--;
                critMult *= critDmg;
            }
            e.setAmount(e.getAmount() * critMult);
            if (critMult > 1.0F && !attacker.m_9236_().isClientSide) {
                PacketDistro.sendToTracking(AttributesLib.CHANNEL, new CritParticleMessage(e.getEntity().m_19879_()), (ServerLevel) attacker.m_9236_(), e.getEntity().m_20183_());
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void vanillaCritDmg(CriticalHitEvent e) {
        float critDmg = (float) e.getEntity().m_21133_(ALObjects.Attributes.CRIT_DAMAGE.get());
        if (e.isVanillaCritical()) {
            e.setDamageModifier(Math.max(e.getDamageModifier(), critDmg));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void breakSpd(PlayerEvent.BreakSpeed e) {
        e.setNewSpeed(e.getNewSpeed() * (float) e.getEntity().m_21133_(ALObjects.Attributes.MINING_SPEED.get()));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void blockBreak(BlockEvent.BreakEvent e) {
        double xpMult = e.getPlayer().m_21133_(ALObjects.Attributes.EXPERIENCE_GAINED.get());
        e.setExpToDrop((int) ((double) e.getExpToDrop() * xpMult));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void mobXp(LivingExperienceDropEvent e) {
        Player player = e.getAttackingPlayer();
        if (player != null) {
            double xpMult = e.getAttackingPlayer().m_21133_(ALObjects.Attributes.EXPERIENCE_GAINED.get());
            e.setDroppedExperience((int) ((double) e.getDroppedExperience() * xpMult));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void heal(LivingHealEvent e) {
        float factor = (float) e.getEntity().getAttributeValue(ALObjects.Attributes.HEALING_RECEIVED.get());
        e.setAmount(e.getAmount() * factor);
        if (e.getAmount() <= 0.0F) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void arrow(EntityJoinLevelEvent e) {
        if (e.getEntity() instanceof AbstractArrow arrow) {
            if (arrow.m_9236_().isClientSide || arrow.getPersistentData().getBoolean("attributeslib.arrow.done")) {
                return;
            }
            if (arrow.m_19749_() instanceof LivingEntity le) {
                arrow.setBaseDamage(arrow.getBaseDamage() * le.getAttributeValue(ALObjects.Attributes.ARROW_DAMAGE.get()));
                arrow.m_20256_(arrow.m_20184_().scale(le.getAttributeValue(ALObjects.Attributes.ARROW_VELOCITY.get())));
            }
            arrow.getPersistentData().putBoolean("attributeslib.arrow.done", true);
        }
    }

    private static double getAttackReachSqr(Entity attacker, LivingEntity pAttackTarget) {
        return (double) (attacker.getBbWidth() * 2.0F * attacker.getBbWidth() * 2.0F + pAttackTarget.m_20205_());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void dodge(LivingAttackEvent e) {
        LivingEntity target = e.getEntity();
        if (!target.m_9236_().isClientSide) {
            Entity attacker = e.getSource().getDirectEntity();
            if (attacker instanceof LivingEntity) {
                double dodgeChance = target.getAttributeValue(ALObjects.Attributes.DODGE_CHANCE.get());
                double atkRangeSqr = attacker instanceof Player p ? p.getEntityReach() * p.getEntityReach() : getAttackReachSqr(attacker, target);
                dodgeRand.setSeed((long) target.f_19797_);
                if (attacker.distanceToSqr(target) <= atkRangeSqr && (double) dodgeRand.nextFloat() <= dodgeChance) {
                    this.onDodge(target);
                    e.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void dodge(ProjectileImpactEvent e) {
        Entity target = e.getRayTraceResult() instanceof EntityHitResult entRes ? entRes.getEntity() : null;
        if (target instanceof LivingEntity lvTarget) {
            double dodgeChance = lvTarget.getAttributeValue(ALObjects.Attributes.DODGE_CHANCE.get());
            dodgeRand.setSeed((long) target.tickCount);
            if ((double) dodgeRand.nextFloat() <= dodgeChance) {
                this.onDodge(lvTarget);
                e.setCanceled(true);
            }
        }
    }

    private void onDodge(LivingEntity target) {
        target.m_9236_().playSound(null, target, ALObjects.Sounds.DODGE.get(), SoundSource.NEUTRAL, 1.0F, 0.7F + target.getRandom().nextFloat() * 0.3F);
        if (target.m_9236_() instanceof ServerLevel sl) {
            double height = (double) target.m_20206_();
            double width = (double) target.m_20205_();
            sl.sendParticles(ParticleTypes.LARGE_SMOKE, target.m_20185_() - width / 4.0, target.m_20186_(), target.m_20189_() - width / 4.0, 6, -width / 4.0, height / 8.0, -width / 4.0, 0.0);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void fixMCF9370(ProjectileImpactEvent e) {
        if (e.isCanceled()) {
            Entity target = e.getRayTraceResult() instanceof EntityHitResult entRes ? entRes.getEntity() : null;
            if (target != null && e.getProjectile() instanceof AbstractArrow arrow && arrow.getPierceLevel() > 0) {
                if (arrow.piercingIgnoreEntityIds == null) {
                    arrow.piercingIgnoreEntityIds = new IntOpenHashSet(arrow.getPierceLevel());
                }
                arrow.piercingIgnoreEntityIds.add(target.getId());
            }
        }
    }

    @SubscribeEvent
    public void affixModifiers(ItemAttributeModifierEvent e) {
        boolean hasBaseAD = e.getModifiers().get(Attributes.ATTACK_DAMAGE).stream().filter(m -> ((IFormattableAttribute) Attributes.ATTACK_DAMAGE).getBaseUUID().equals(m.getId())).findAny().isPresent();
        if (hasBaseAD) {
            boolean hasBaseAR = e.getModifiers().get(ForgeMod.ENTITY_REACH.get()).stream().filter(m -> ((IFormattableAttribute) ForgeMod.ENTITY_REACH.get()).getBaseUUID().equals(m.getId())).findAny().isPresent();
            if (!hasBaseAR) {
                e.addModifier(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(AttributeHelper.BASE_ENTITY_REACH, (Supplier<String>) (() -> "attributeslib:fake_base_range"), 0.0, AttributeModifier.Operation.ADDITION));
            }
        }
        if (e.getSlotType() == EquipmentSlot.CHEST && e.getItemStack().getItem() instanceof ElytraItem && !e.getModifiers().containsKey(ALObjects.Attributes.ELYTRA_FLIGHT.get())) {
            e.addModifier(ALObjects.Attributes.ELYTRA_FLIGHT.get(), new AttributeModifier(AttributeHelper.ELYTRA_FLIGHT_UUID, (Supplier<String>) (() -> "attributeslib:elytra_item_flight"), 1.0, AttributeModifier.Operation.ADDITION));
        }
    }

    @SubscribeEvent
    public void trackCooldown(AttackEntityEvent e) {
        Player p = e.getEntity();
        AttributesLib.localAtkStrength = p.getAttackStrengthScale(0.5F);
    }

    @SubscribeEvent
    public void valueChanged(AttributeChangedValueEvent e) {
        if (e.getAttributeInstance().getAttribute() == ALObjects.Attributes.CREATIVE_FLIGHT.get() && e.getEntity() instanceof ServerPlayer player) {
            boolean changed = false;
            if (((IFlying) player).getAndDestroyFlyingCache()) {
                player.m_150110_().flying = true;
                changed = true;
            }
            if (e.getNewValue() > 0.0) {
                player.m_150110_().mayfly = true;
                changed = true;
            } else if (e.getOldValue() > 0.0 && e.getNewValue() <= 0.0) {
                player.m_150110_().mayfly = false;
                player.m_150110_().flying = false;
                changed = true;
            }
            if (changed) {
                player.onUpdateAbilities();
            }
        }
    }

    public static void applyCreativeFlightModifier(Player player, GameType newType) {
        AttributeInstance inst = player.m_21051_(ALObjects.Attributes.CREATIVE_FLIGHT.get());
        if (newType != GameType.CREATIVE && newType != GameType.SPECTATOR) {
            inst.removeModifier(AttributeHelper.CREATIVE_FLIGHT_UUID);
        } else if (inst.getModifier(AttributeHelper.CREATIVE_FLIGHT_UUID) == null) {
            inst.addTransientModifier(new AttributeModifier(AttributeHelper.CREATIVE_FLIGHT_UUID, (Supplier<String>) (() -> "attributeslib:creative_flight"), 1.0, AttributeModifier.Operation.ADDITION));
        }
    }

    @SubscribeEvent
    public void reloads(AddReloadListenerEvent e) {
        e.addListener(ALConfig.makeReloader());
    }
}