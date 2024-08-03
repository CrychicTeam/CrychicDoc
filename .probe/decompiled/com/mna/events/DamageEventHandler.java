package com.mna.events;

import com.mna.ManaAndArtifice;
import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.capabilities.resource.CastingResourceIDs;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.entities.DamageHelper;
import com.mna.api.entities.IFactionEnemy;
import com.mna.api.events.BonefeatherCharmUsedEvent;
import com.mna.api.events.CoalfeatherCharmUsedEvent;
import com.mna.api.events.ReedfeatherCharmUsedEvent;
import com.mna.api.sound.SFX;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.config.GeneralConfig;
import com.mna.effects.EffectInit;
import com.mna.entities.boss.WitherLich;
import com.mna.entities.constructs.animated.Construct;
import com.mna.entities.utility.DisplayDamage;
import com.mna.factions.Factions;
import com.mna.interop.CuriosInterop;
import com.mna.items.ItemInit;
import com.mna.items.armor.CouncilArmorItem;
import com.mna.items.armor.FeyArmorItem;
import com.mna.items.artifice.charms.ItemContingencyCharm;
import com.mna.items.sorcery.ItemCrystalPhylactery;
import com.mna.items.sorcery.ItemSpell;
import com.mna.items.sorcery.PhylacteryStaffItem;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.BlockUtils;
import com.mna.tools.EntityUtil;
import com.mna.tools.InventoryUtilities;
import com.mna.tools.ProjectileHelper;
import com.mna.tools.SummonUtils;
import com.mna.tools.TeleportHelper;
import com.mna.tools.math.MathUtils;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.items.wrapper.InvWrapper;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.SlotTypePreset;

@EventBusSubscriber(modid = "mna", bus = Bus.FORGE)
public class DamageEventHandler {

    @SubscribeEvent(receiveCanceled = true)
    public static void onLivingFallAlways(LivingFallEvent event) {
        event.getEntity().getPersistentData().remove("mna:flung");
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        LivingEntity living = event.getEntity();
        if (living.hasEffect(EffectInit.MIST_FORM.get())) {
            event.setCanceled(true);
        } else {
            if (living.hasEffect(EffectInit.PILGRIM.get()) || living.hasEffect(EffectInit.ENLARGE.get()) || living.hasEffect(EffectInit.REDUCE.get())) {
                event.setDistance(event.getDistance() - 2.0F);
            }
            if (living instanceof Player player) {
                if (ItemInit.DEMON_ARMOR_BOOTS.get().handlePlayerMeteorJumpImpact(player, event.getDistance())) {
                    event.setCanceled(true);
                    return;
                }
                if (player.m_21124_(EffectInit.GRAVITY_WELL.get()) != null) {
                    event.setDistance(event.getDistance() * 2.0F);
                }
                if (player.getInventory().getItem(EquipmentSlot.CHEST.getIndex()).getItem() == ItemInit.SPECTRAL_ELYTRA.get()) {
                    event.setDistance(event.getDistance() - 5.0F);
                }
                if (!event.isCanceled()) {
                    checkAndConsumeBonefeather(event, player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        DamageSource source = event.getSource();
        LivingEntity living = event.getEntity();
        if (living != null && source != null) {
            if (!source.is(DamageTypes.IN_WALL) || !living.hasEffect(EffectInit.MIST_FORM.get()) && !living.hasEffect(EffectInit.ELDRIN_FLIGHT.get())) {
                if (living instanceof Player) {
                    if (checkAndConsumeReedfeather(event, (Player) living)) {
                        return;
                    }
                    if (checkAndConsumeCoalfeather(event, (Player) living)) {
                        return;
                    }
                }
                if (living.hasEffect(EffectInit.FORTIFICATION.get()) && !source.is(DamageTypeTags.IS_FALL) && !source.is(DamageTypes.IN_WALL) && !source.is(DamageTypes.FELL_OUT_OF_WORLD) && !source.is(DamageTypeTags.BYPASSES_RESISTANCE)) {
                    event.setCanceled(true);
                    living.hurt(DamageHelper.createSourcedType(DamageTypes.FELL_OUT_OF_WORLD, living.m_9236_().registryAccess(), source.getEntity()), (float) GeneralConfigValues.FossilizeDamage);
                } else {
                    if (source.getDirectEntity() instanceof Projectile) {
                        boolean shouldReflect = false;
                        boolean shouldReflectAtShooter = false;
                        Entity sourceEntity = source.getEntity();
                        if (living.hasEffect(EffectInit.WIND_WALL.get()) && (sourceEntity == null || sourceEntity.canChangeDimensions())) {
                            shouldReflect = true;
                            MobEffectInstance windWall = living.getEffect(EffectInit.WIND_WALL.get());
                            if (windWall != null) {
                                shouldReflectAtShooter = windWall.getAmplifier() > 0;
                            }
                        } else if (event.getEntity() instanceof Player) {
                            Player player = (Player) event.getEntity();
                            if (ItemInit.COUNCIL_ARMOR__CHEST.get().isSetEquipped(living)) {
                                shouldReflect = CouncilArmorItem.consumeReflectCharget(player);
                                shouldReflectAtShooter = true;
                            } else if (ItemInit.FEY_ARMOR_CHEST.get().isSetEquipped(living)) {
                                shouldReflect = FeyArmorItem.randomReflectChance();
                                shouldReflectAtShooter = true;
                            }
                        }
                        if (shouldReflect) {
                            event.setCanceled(true);
                            ProjectileHelper.ReflectProjectile(living, (Projectile) source.getDirectEntity(), shouldReflectAtShooter, 10.0F);
                            return;
                        }
                    }
                    if (!source.is(DamageTypeTags.IS_PROJECTILE) && source.getEntity() != null && source.getEntity() instanceof LivingEntity && source.getEntity().distanceToSqr(event.getEntity()) < GeneralConfigValues.MeleeDistance && ItemInit.FEY_ARMOR_CHEST.get().isSetEquipped(living) && FeyArmorItem.randomTeleportChance() && TeleportHelper.randomTeleport((LivingEntity) source.getEntity(), 16.0F, 10)) {
                        event.setCanceled(true);
                    } else {
                        if (source.is(DamageTypeTags.IS_FIRE)) {
                            if (ItemInit.DEMON_ARMOR_CHEST.get().isSetEquipped(living)) {
                                event.setCanceled(true);
                                return;
                            }
                            if (ItemInit.EMBERGLOW_BRACELET.get().isEquippedAndHasMana(living, 1.0F, true)) {
                                event.setCanceled(true);
                                return;
                            }
                        }
                        if (source.is(DamageTypeTags.IS_EXPLOSION) && ItemInit.DEMON_ARMOR_CHEST.get().isSetEquipped(living)) {
                            event.setCanceled(true);
                        }
                    }
                }
            } else {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Entity source = event.getSource().getEntity();
        LivingEntity living = event.getEntity();
        if (source != null && source instanceof LivingEntity sourceLiving) {
            if (event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO)) {
                if (living.hasEffect(EffectInit.AMPLIFY_MAGIC.get())) {
                    int amplifier = living.getEffect(EffectInit.AMPLIFY_MAGIC.get()).getAmplifier() + 1;
                    event.setAmount(event.getAmount() * (1.0F + (float) amplifier * 0.1F));
                }
                if (living.hasEffect(EffectInit.DAMPEN_MAGIC.get())) {
                    int amplifier = living.getEffect(EffectInit.DAMPEN_MAGIC.get()).getAmplifier() + 1;
                    event.setAmount(event.getAmount() * (1.0F - (float) amplifier * 0.1F));
                }
            }
            if (living.hasEffect(EffectInit.FRAILTY.get())) {
                int amplifier = living.getEffect(EffectInit.FRAILTY.get()).getAmplifier() + 1;
                event.setAmount(event.getAmount() * (1.0F + (float) amplifier * 0.2F));
            }
            double dist = living.m_20280_(sourceLiving);
            boolean retaliate = dist <= 25.0;
            if (retaliate && living.getUseItem().getItem() == ItemInit.BOUND_SHIELD.get() && canEntityBlock(event.getSource(), living)) {
                SpellRecipe recipe = ItemInit.BOUND_SHIELD.get().getRecipe(living.getUseItem());
                if (living instanceof Player player) {
                    if (!player.getCooldowns().isOnCooldown(ItemInit.BOUND_SHIELD.get())) {
                        ItemInit.BOUND_SHIELD.get().affectTarget(recipe, living, sourceLiving, living.getUsedItemHand());
                        player.getCooldowns().addCooldown(ItemInit.BOUND_SHIELD.get(), recipe.getCooldown(player));
                    }
                } else {
                    ItemInit.BOUND_SHIELD.get().affectTarget(recipe, living, sourceLiving, living.getUsedItemHand());
                }
            }
        }
        boolean reduce = !event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !event.getSource().is(DamageTypeTags.IS_FALL) && !event.getSource().is(DamageTypes.IN_WALL) && !event.getSource().is(DamageTypes.STARVE) && !event.getSource().is(DamageTypeTags.IS_DROWNING) && !event.getSource().is(DamageTypes.CRAMMING) && !event.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE);
        MobEffectInstance mana_shield = living.getEffect(EffectInit.MANA_SHIELD.get());
        if (mana_shield != null && reduce) {
            Player mana_shield_source = resolveManaShieldSource(living);
            if (mana_shield_source != null) {
                float reduction = event.getAmount() * 0.2F * (float) MathUtils.clamp(mana_shield.getAmplifier() + 1, 1, 5);
                float reductionCost = EffectInit.MANA_SHIELD.get().getReductionCost(mana_shield.getAmplifier() + 1);
                mana_shield_source.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                    float amountReduced = reduction;
                    if (m.getCastingResource().getAmount() <= reduction * reductionCost) {
                        amountReduced = m.getCastingResource().getAmount() / reductionCost;
                    }
                    m.getCastingResource().consume(mana_shield_source, amountReduced * reductionCost);
                    m.forceSync(1);
                    if (m.getCastingResource().getAmount() <= 0.0F) {
                        living.removeEffect(EffectInit.MANA_SHIELD.get());
                    }
                    event.setAmount(event.getAmount() - amountReduced);
                });
            } else {
                living.removeEffect(EffectInit.MANA_SHIELD.get());
            }
            if (event.getAmount() == 0.0F) {
                event.setCanceled(true);
                return;
            }
        }
        if (source instanceof Player && !event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO) && !event.getSource().is(DamageTypeTags.IS_PROJECTILE) && !event.getSource().is(DamageTypeTags.IS_FIRE) && !event.getSource().is(DamageTypeTags.IS_EXPLOSION) && !event.getSource().is(DamageTypeTags.IS_FALL) && (double) source.distanceTo(living) <= GeneralConfigValues.MeleeDistance) {
            Player sourcePlayer = (Player) source;
            ItemStack mainHand = sourcePlayer.m_21205_();
            ItemStack offHand = sourcePlayer.m_21206_();
            if ((mainHand.getItem() instanceof SwordItem && offHand.getItem() instanceof ItemSpell || offHand.getItem() instanceof SwordItem && mainHand.getItem() instanceof ItemSpell) && CuriosInterop.IsItemInCurioSlot(ItemInit.BATTLEMAGE_AMULET.get(), sourcePlayer, SlotTypePreset.NECKLACE)) {
                IPlayerMagic magic = (IPlayerMagic) sourcePlayer.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
                if (magic != null) {
                    float pct = 1.0F - magic.getCastingResource().getAmount() / magic.getCastingResource().getMaxAmount();
                    event.setAmount(event.getAmount() + 5.0F * pct);
                }
            }
        }
        if (event.getAmount() > 0.0F) {
            if (living instanceof Player) {
                MobEffectInstance lev = living.getEffect(EffectInit.LEVITATION.get());
                if (lev != null) {
                    Player target = (Player) event.getEntity();
                    boolean wasFlying = target.getAbilities().flying;
                    int amplifier = lev.getAmplifier();
                    int duration = lev.isInfiniteDuration() ? 1200 : lev.getDuration();
                    target.m_21195_(EffectInit.LEVITATION.get());
                    duration /= 2;
                    if (duration > 100) {
                        target.m_7292_(new MobEffectInstance(EffectInit.LEVITATION.get(), duration, amplifier));
                        ManaAndArtifice.instance.proxy.setFlightEnabled(target, true);
                        target.getAbilities().flying = wasFlying;
                    }
                }
            }
            if (living.hasEffect(EffectInit.BIND_WOUNDS.get())) {
                living.removeEffect(EffectInit.BIND_WOUNDS.get());
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        MobEffectInstance fire_shield = event.getEntity().getEffect(EffectInit.FIRE_SHIELD.get());
        MobEffectInstance briar_shield = event.getEntity().getEffect(EffectInit.BRIARTHORN_BARRIER.get());
        MobEffectInstance life_tap = event.getEntity().getEffect(EffectInit.LIFE_TAP.get());
        boolean reduce = !event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !event.getSource().is(DamageTypeTags.IS_FALL) && !event.getSource().is(DamageTypes.IN_WALL) && !event.getSource().is(DamageTypes.STARVE) && !event.getSource().is(DamageTypeTags.IS_DROWNING) && !event.getSource().is(DamageTypes.CRAMMING) && !event.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE);
        if (event.getSource().is(DamageTypes.WITHER) && ItemInit.WITHERGUARD_AMULET.get().isEquippedAndHasMana(event.getEntity(), 1.0F, true)) {
            event.setCanceled(true);
        } else {
            if (event.getSource().getEntity() instanceof Player && ((Player) event.getSource().getEntity()).getPersistentData().getBoolean("hellfiretrident")) {
                if (((Player) event.getSource().getEntity()).f_20938_ > 0 && event.getSource().getMsgId() == "player") {
                    event.setAmount(event.getAmount() * 2.0F);
                }
                ((Player) event.getSource().getEntity()).getPersistentData().putBoolean("hellfiretrident", false);
            }
            if (reduce && event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO) && ItemInit.WARDING_AMULET.get().isEquippedAndHasMana(event.getEntity(), 1.0F, true)) {
                event.setAmount(event.getAmount() - event.getAmount() * 0.2F);
            }
            if (event.getSource().is(DamageTypeTags.IS_FIRE) && fire_shield != null && reduce) {
                float reduction = event.getAmount() * 0.1F * (float) (fire_shield.getAmplifier() + 1);
                event.setAmount(event.getAmount() - reduction);
            }
            Entity source = event.getSource().getEntity();
            if (source != null && source instanceof LivingEntity && source != event.getEntity()) {
                LivingEntity living = event.getEntity();
                LivingEntity sourceLiving = (LivingEntity) source;
                double dist = living.m_20280_(sourceLiving);
                boolean retaliate = dist <= 25.0;
                if (retaliate && event.getEntity().getUseItem().getItem() == ItemInit.BOUND_SHIELD.get() && canEntityBlock(event.getSource(), living)) {
                    SpellRecipe recipe = ItemInit.BOUND_SHIELD.get().getRecipe(living.getUseItem());
                    if (living instanceof Player player) {
                        if (!player.getCooldowns().isOnCooldown(ItemInit.BOUND_SHIELD.get())) {
                            ItemInit.BOUND_SHIELD.get().affectTarget(recipe, living, sourceLiving, living.getUsedItemHand());
                            player.getCooldowns().addCooldown(ItemInit.BOUND_SHIELD.get(), recipe.getCooldown(player));
                        }
                    } else {
                        ItemInit.BOUND_SHIELD.get().affectTarget(recipe, living, sourceLiving, living.getUsedItemHand());
                    }
                }
                if (briar_shield != null) {
                    if (event.getSource().getMsgId().equals("thorns") || event.getSource().getMsgId().equals("poison") || event.getSource().getMsgId().equals("sting") || event.getSource().getMsgId().equals("cactus") || event.getSource().getMsgId().equals("sweetBerryBush")) {
                        float reduction = event.getAmount() * 0.1F * (float) (briar_shield.getAmplifier() + 1);
                        event.setAmount(event.getAmount() - reduction);
                    }
                    if (retaliate) {
                        float damage = (float) ((briar_shield.getAmplifier() + 1) * 2);
                        sourceLiving.hurt(DamageHelper.forType(DamageHelper.BRIARS, sourceLiving.m_9236_().registryAccess()), damage * GeneralConfig.getDamageMultiplier());
                    }
                }
                if (fire_shield != null && retaliate) {
                    float damage = (float) ((fire_shield.getAmplifier() + 1) * 2);
                    sourceLiving.hurt(sourceLiving.m_269291_().inFire(), damage * GeneralConfig.getDamageMultiplier());
                }
                if (life_tap != null) {
                    float pct = 0.05F;
                    if (sourceLiving.getPersistentData().contains("life_tap_pct")) {
                        pct = sourceLiving.getPersistentData().getFloat("life_tap_pct");
                    }
                    float healing = event.getAmount() * pct * (float) (life_tap.getAmplifier() + 1);
                    sourceLiving.heal(healing);
                }
                if (source instanceof Player sourcePlayer) {
                    ItemStack mhStack = sourcePlayer.m_21205_();
                    ItemStack ohStack = sourcePlayer.m_21206_();
                    if (!event.getSource().is(DamageTypeTags.IS_PROJECTILE)) {
                        CuriosApi.getCuriosHelper().findFirstCurio(sourcePlayer, ItemInit.EMBERGLOW_BRACELET.get()).ifPresent(t -> {
                            if (living.m_20280_(source) < 4.0) {
                                living.m_20254_(4);
                            }
                            t.stack().hurtAndBreak(1, living, damager -> CuriosApi.getCuriosHelper().onBrokenCurio(t.slotContext()));
                        });
                    }
                    if (sourcePlayer.m_6144_() && sourcePlayer.m_21205_().isEmpty()) {
                        CuriosApi.getCuriosHelper().findFirstCurio(sourcePlayer, ItemInit.TRICKERY_BRACELET.get()).ifPresent(t -> {
                            living.knockback(5.0, sourcePlayer.m_20185_() - living.m_20185_(), sourcePlayer.m_20189_() - living.m_20189_());
                            t.stack().hurtAndBreak(1, living, damager -> CuriosApi.getCuriosHelper().onBrokenCurio(t.slotContext()));
                        });
                    }
                    sourcePlayer.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
                        if (p.getAlliedFaction() == Factions.UNDEAD) {
                            if (sourcePlayer.getPersistentData().getBoolean("bone_armor_set_bonus")) {
                                float souls = EntityUtil.getSoulsRestored(sourcePlayer, living);
                                if (souls > 0.0F) {
                                    sourcePlayer.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                                        m.getCastingResource().restore(souls * 0.1F);
                                        m.getCastingResource().setNeedsSync();
                                    });
                                }
                            }
                            if (CuriosApi.getCuriosHelper().findFirstCurio(sourcePlayer, ItemInit.COWL_OF_CONSUMPTION.get()) != null && event.getAmount() >= 10.0F) {
                                sourcePlayer.getFoodData().eat(1, 0.1F);
                            }
                        }
                        if (p.getAlliedFaction() == Factions.FEY) {
                            int mhThorns = mhStack.getEnchantmentLevel(Enchantments.THORNS);
                            int ohThorns = ohStack.getEnchantmentLevel(Enchantments.THORNS);
                            int appliedThorns = Math.max(mhThorns, ohThorns);
                            if (appliedThorns > 0) {
                                living.addEffect(new MobEffectInstance(EffectInit.WRITHING_BRAMBLES.get(), 100, appliedThorns));
                            }
                        }
                    });
                    if (living instanceof Mob) {
                        int mhLure = mhStack.getEnchantmentLevel(Enchantments.FISHING_SPEED);
                        int ohLure = ohStack.getEnchantmentLevel(Enchantments.FISHING_SPEED);
                        int appliedLure = Math.max(mhLure, ohLure);
                        if (Math.random() < 0.15 * (double) appliedLure) {
                            ((Mob) living).setTarget(sourcePlayer);
                        }
                    }
                }
            }
            if (event.getAmount() <= 0.0F) {
                event.setCanceled(true);
            } else if (event.getEntity().getPersistentData().getBoolean("bone_armor_set_bonus") && !event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) && event.getEntity().getHealth() > 1.0F && event.getAmount() > event.getEntity().getHealth()) {
                event.getEntity().addEffect(new MobEffectInstance(EffectInit.MIST_FORM.get(), 200, 0, true, true));
                event.getEntity().setHealth(1.0F);
                event.setCanceled(true);
            }
            if (event.getEntity() instanceof ServerPlayer && !event.isCanceled()) {
                ServerPlayer playerx = (ServerPlayer) event.getEntity();
                if (playerx.m_21223_() <= playerx.m_21233_() * 0.25F) {
                    ItemContingencyCharm.CheckAndConsumeCharmCharge(playerx, ItemContingencyCharm.ContingencyEvent.LOW_HEALTH);
                } else if (!event.getSource().is(DamageTypes.IN_WALL) && !event.getSource().is(DamageTypeTags.IS_DROWNING)) {
                    ItemContingencyCharm.CheckAndConsumeCharmCharge(playerx, ItemContingencyCharm.ContingencyEvent.DAMAGE);
                } else {
                    ItemContingencyCharm.CheckAndConsumeCharmCharge(playerx, ItemContingencyCharm.ContingencyEvent.SUFFOCATION);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDeath(LivingDeathEvent event) {
        Entity causeOfDeath = event.getSource().getEntity();
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Optional<SlotResult> curio = CuriosApi.getCuriosHelper().findFirstCurio(player, ItemInit.CRIT_IMMUNITY_AMULET.get());
            if (curio.isPresent() && !player.getCooldowns().isOnCooldown(ItemInit.CRIT_IMMUNITY_AMULET.get()) && ItemInit.CRIT_IMMUNITY_AMULET.get().consumeMana(((SlotResult) curio.get()).stack(), 250.0F, player)) {
                player.m_9236_().playSound(null, player.m_20185_(), player.m_20186_(), player.m_20189_(), SFX.Spell.Buff.ARCANE, SoundSource.PLAYERS, 1.0F, 1.0F);
                player.getCooldowns().addCooldown(ItemInit.CRIT_IMMUNITY_AMULET.get(), 12000);
                player.m_7292_(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 4));
                if (player.m_21223_() < 1.0F) {
                    player.m_21153_(1.0F);
                }
                event.setCanceled(true);
                return;
            }
            if (player.m_21124_(EffectInit.CHRONO_ANCHOR.get()) != null) {
                player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                    if (m.getChronoAnchorData().canRevert(player)) {
                        event.setCanceled(true);
                        player.m_21195_(EffectInit.CHRONO_ANCHOR.get());
                    }
                });
            } else {
                SummonUtils.getSummons(player).forEach(s -> s.m_146870_());
            }
            if (player.m_21023_(EffectInit.LIFT.get())) {
                player.m_21195_(EffectInit.LIFT.get());
            }
            if (player.m_21023_(EffectInit.MIND_VISION.get())) {
                player.m_21195_(EffectInit.MIND_VISION.get());
            }
            if (player.m_21023_(EffectInit.POSSESSION.get())) {
                player.m_21195_(EffectInit.POSSESSION.get());
            }
            if (player instanceof ServerPlayer) {
                ItemContingencyCharm.CheckAndConsumeCharmCharge((ServerPlayer) player, ItemContingencyCharm.ContingencyEvent.DEATH);
            }
            CompoundTag deathData = new CompoundTag();
            deathData.putLong("position", player.m_20183_().above().asLong());
            deathData.putString("dimension", player.m_9236_().dimension().location().toString());
            player.getPersistentData().put("mna_last_death_data", deathData);
        } else if (event.getEntity() instanceof Mob) {
            boolean isSummon = SummonUtils.isSummon(event.getEntity());
            if (isSummon) {
                event.setCanceled(true);
                event.getEntity().setHealth(1.0F);
                event.getEntity().m_146870_();
                return;
            }
            if (causeOfDeath != null && causeOfDeath instanceof Player) {
                if (!isSummon && !(event.getEntity() instanceof Construct)) {
                    ItemStack mainHand = ((Player) causeOfDeath).m_21205_();
                    ItemStack offHand = ((Player) causeOfDeath).m_21206_();
                    boolean added = false;
                    EntityType<? extends Mob> type = event.getEntity().m_6095_();
                    int amount = ((Player) causeOfDeath).isCreative() ? 100 : 1;
                    Level level = causeOfDeath.level();
                    IPlayerProgression progression = (IPlayerProgression) ((Player) causeOfDeath).getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
                    if (progression != null && progression.getAlliedFaction() == Factions.UNDEAD) {
                        amount *= 5;
                    }
                    if (mainHand.getItem() == ItemInit.STAFF_PHYLACTERY.get()) {
                        added = PhylacteryStaffItem.addToPhylactery(mainHand, type, (float) amount, level);
                    }
                    if (!added && offHand.getItem() == ItemInit.STAFF_PHYLACTERY.get()) {
                        added = PhylacteryStaffItem.addToPhylactery(offHand, type, (float) amount, level);
                    }
                    if (!added) {
                        ItemStack backPhylactery = ItemStack.EMPTY;
                        for (SlotResult sr : CuriosApi.getCuriosHelper().findCurios((Player) causeOfDeath, is -> is.getItem() == ItemInit.STAFF_PHYLACTERY.get() || is.getItem() == ItemInit.CRYSTAL_PHYLACTERY.get())) {
                            if (!sr.stack().isEmpty()) {
                                backPhylactery = sr.stack();
                                break;
                            }
                        }
                        if (!backPhylactery.isEmpty()) {
                            added = PhylacteryStaffItem.addToPhylactery(backPhylactery, type, (float) amount, level);
                        }
                        if (!added) {
                            ItemCrystalPhylactery.addToPhylactery(((Player) causeOfDeath).getInventory(), type, (float) amount, level, true);
                        }
                    }
                }
                if (GeneralConfigValues.FactionMobKillIre && event.getEntity() instanceof IFactionEnemy) {
                    ((Player) causeOfDeath).getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> p.incrementFactionAggro(((IFactionEnemy) event.getEntity()).getFaction(), 0.025F, 0.05F));
                }
                SummonUtils.getSummons(event.getEntity()).forEach(s -> s.m_146870_());
                if (SummonUtils.isSummon(event.getEntity())) {
                    LivingEntity summoner = SummonUtils.getSummoner(event.getEntity());
                    if (summoner != null && summoner instanceof WitherLich) {
                        ((WitherLich) summoner).decrementSummons();
                    }
                }
            }
        }
        Player soulRecipient = causeOfDeath instanceof Player ? (Player) causeOfDeath : (causeOfDeath instanceof LivingEntity && SummonUtils.isSummon(causeOfDeath) ? SummonUtils.getSummonerAsPlayer((LivingEntity) causeOfDeath) : null);
        if (soulRecipient != null) {
            soulRecipient.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                if (m.getCastingResource().getRegistryName().equals(CastingResourceIDs.SOULS)) {
                    m.getCastingResource().restore(EntityUtil.getSoulsRestored(soulRecipient, event.getEntity()));
                    m.getCastingResource().setNeedsSync();
                }
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerAttackTarget(AttackEntityEvent event) {
        if (event.getEntity().m_21023_(EffectInit.MIST_FORM.get())) {
            event.setCanceled(true);
            event.setResult(Result.DENY);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDamage_Insight(LivingDamageEvent event) {
        float insightRange = 16.0F;
        if (!event.getEntity().m_9236_().isClientSide()) {
            boolean shouldSpawn = event.getEntity().m_9236_().m_45955_(TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting().range((double) insightRange).selector(le -> le.hasEffect(EffectInit.INSIGHT.get())), event.getEntity(), event.getEntity().m_20191_().inflate((double) insightRange)).stream().map(p -> (ServerPlayer) p).toList().size() > 0;
            if (shouldSpawn) {
                Vec3 pos = event.getEntity().m_146892_().add(new Vec3(-0.5 + Math.random(), 0.0, -0.5 + Math.random()));
                float amount = Math.max(event.getAmount(), 0.0F);
                DisplayDamage edd = new DisplayDamage(event.getEntity().m_9236_(), event.getSource(), amount);
                edd.m_146884_(pos);
                event.getEntity().m_9236_().m_7967_(edd);
            }
        }
    }

    public static void onCriticalHit(CriticalHitEvent event) {
        if (event.getTarget() instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) event.getTarget();
            Optional<SlotResult> curio = CuriosApi.getCuriosHelper().findFirstCurio(living, ItemInit.CRIT_IMMUNITY_AMULET.get());
            if (curio.isPresent()) {
                event.setResult(Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public static void onShieldBlock(ShieldBlockEvent event) {
        if (event.getEntity() instanceof Construct) {
            ((Construct) event.getEntity()).onShieldBlock();
        }
    }

    private static boolean canEntityBlock(DamageSource damageSource, LivingEntity living) {
        Entity entity = damageSource.getDirectEntity();
        boolean flag = false;
        if (entity instanceof AbstractArrow abstractarrowentity && abstractarrowentity.getPierceLevel() > 0) {
            flag = true;
        }
        if (!damageSource.is(DamageTypeTags.BYPASSES_ARMOR) && living.isBlocking() && !flag) {
            Vec3 vector3d2 = damageSource.getSourcePosition();
            if (vector3d2 != null) {
                Vec3 vector3d = living.m_20252_(1.0F);
                Vec3 vector3d1 = vector3d2.vectorTo(living.m_20182_()).normalize();
                vector3d1 = new Vec3(vector3d1.x, 0.0, vector3d1.z);
                if (vector3d1.dot(vector3d) < 0.0) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int calculateFallDamage(LivingEntity entity, float distance, float damageMult) {
        MobEffectInstance effectinstance = entity.getEffect(MobEffects.JUMP);
        float f = effectinstance == null ? 0.0F : (float) (effectinstance.getAmplifier() + 1);
        return Mth.ceil((distance - 3.0F - f) * damageMult);
    }

    private static void checkAndConsumeBonefeather(LivingFallEvent event, Player player) {
        if (!player.isCreative() && !player.isSpectator()) {
            int damage = calculateFallDamage(player, event.getDistance(), event.getDamageMultiplier());
            if ((float) damage > player.m_21223_()) {
                ItemStack charmStack = new ItemStack(ItemInit.FALL_CHARM.get());
                boolean consumed_fall_charm = false;
                if (CuriosInterop.IsItemInCurioSlot(ItemInit.FALL_CHARM.get(), player, SlotTypePreset.CHARM)) {
                    consumed_fall_charm = true;
                    CuriosInterop.DamageCurioInSlot(ItemInit.FALL_CHARM.get(), player, SlotTypePreset.CHARM, 999);
                } else if (InventoryUtilities.removeItemFromInventory(charmStack, true, true, new InvWrapper(player.getInventory()))) {
                    consumed_fall_charm = true;
                }
                if (consumed_fall_charm) {
                    player.m_9236_().playSound(null, player.m_20185_(), player.m_20186_(), player.m_20189_(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                    event.setCanceled(true);
                    BonefeatherCharmUsedEvent bfe = new BonefeatherCharmUsedEvent(player);
                    MinecraftForge.EVENT_BUS.post(bfe);
                    if (player instanceof ServerPlayer) {
                        CustomAdvancementTriggers.USE_CHARM.trigger((ServerPlayer) player, charmStack);
                        player.awardStat(Stats.ITEM_USED.get(ItemInit.FALL_CHARM.get()));
                        CustomAdvancementTriggers.SHAMAN.trigger((ServerPlayer) player);
                    }
                }
            }
        }
    }

    private static boolean checkAndConsumeReedfeather(LivingAttackEvent event, Player player) {
        if (!player.isCreative() && !player.isSpectator()) {
            if (event.getSource().is(DamageTypeTags.IS_DROWNING)) {
                ItemStack charmStack = new ItemStack(ItemInit.DROWN_CHARM.get());
                boolean consumed_charm = false;
                if (CuriosInterop.IsItemInCurioSlot(ItemInit.DROWN_CHARM.get(), player, SlotTypePreset.CHARM)) {
                    consumed_charm = true;
                    CuriosInterop.DamageCurioInSlot(ItemInit.DROWN_CHARM.get(), player, SlotTypePreset.CHARM, 999);
                } else if (InventoryUtilities.removeItemFromInventory(charmStack, true, true, new InvWrapper(player.getInventory()))) {
                    consumed_charm = true;
                }
                if (consumed_charm) {
                    player.m_9236_().playSound(null, player.m_20185_(), player.m_20186_(), player.m_20189_(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                    event.setCanceled(true);
                    ReedfeatherCharmUsedEvent bfe = new ReedfeatherCharmUsedEvent(player);
                    MinecraftForge.EVENT_BUS.post(bfe);
                    if (player instanceof ServerPlayer) {
                        CustomAdvancementTriggers.USE_CHARM.trigger((ServerPlayer) player, charmStack);
                        player.awardStat(Stats.ITEM_USED.get(ItemInit.DROWN_CHARM.get()));
                        CustomAdvancementTriggers.SHAMAN.trigger((ServerPlayer) player);
                    }
                    player.m_20301_(player.m_6062_());
                    if (!player.m_9236_().isClientSide()) {
                        player.m_7292_(new MobEffectInstance(MobEffects.WATER_BREATHING, 1, 40));
                    }
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    private static boolean checkAndConsumeCoalfeather(LivingAttackEvent event, Player player) {
        if (!player.isCreative() && !player.isSpectator()) {
            if (event.getSource().is(DamageTypeTags.IS_FIRE) || event.getSource().is(DamageTypes.LAVA)) {
                boolean consumed_charm = false;
                ItemStack charmStack = new ItemStack(ItemInit.BURN_CHARM.get());
                if (CuriosInterop.IsItemInCurioSlot(ItemInit.BURN_CHARM.get(), player, SlotTypePreset.CHARM)) {
                    consumed_charm = true;
                    CuriosInterop.DamageCurioInSlot(ItemInit.BURN_CHARM.get(), player, SlotTypePreset.CHARM, 999);
                } else if (InventoryUtilities.removeItemFromInventory(charmStack, true, true, new InvWrapper(player.getInventory()))) {
                    consumed_charm = true;
                }
                if (consumed_charm) {
                    player.m_9236_().playSound(null, player.m_20185_(), player.m_20186_(), player.m_20189_(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                    event.setCanceled(true);
                    CoalfeatherCharmUsedEvent bfe = new CoalfeatherCharmUsedEvent(player);
                    MinecraftForge.EVENT_BUS.post(bfe);
                    if (player instanceof ServerPlayer) {
                        CustomAdvancementTriggers.USE_CHARM.trigger((ServerPlayer) player, charmStack);
                        player.awardStat(Stats.ITEM_USED.get(ItemInit.BURN_CHARM.get()));
                        CustomAdvancementTriggers.SHAMAN.trigger((ServerPlayer) player);
                    }
                    player.m_20095_();
                    int radius = 3;
                    BlockUtils.IterateBlocksInCube(radius, player.m_20183_(), pos -> {
                        BlockState state = player.m_9236_().getBlockState(pos);
                        if (state.m_60734_() instanceof BaseFireBlock) {
                            player.m_9236_().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                        } else if (state.m_60819_().is(Fluids.LAVA)) {
                            player.m_9236_().setBlock(pos, Blocks.OBSIDIAN.defaultBlockState(), 3);
                        } else if (state.m_60819_().is(Fluids.FLOWING_LAVA)) {
                            player.m_9236_().setBlock(pos, Blocks.COBBLESTONE.defaultBlockState(), 3);
                        }
                    });
                    player.m_9236_().playSound(null, player.m_20185_(), player.m_20186_(), player.m_20189_(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1.0F, 1.0F);
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Nullable
    private static Player resolveManaShieldSource(Entity target) {
        int id = target.getPersistentData().getInt("mana_shield_source_id");
        Entity res = target.level().getEntity(id);
        if (res != null && res instanceof Player) {
            float maxDist = target.getPersistentData().getFloat("mana_shield_range");
            if (res.distanceTo(target) <= maxDist) {
                return (Player) res;
            }
        }
        return null;
    }
}