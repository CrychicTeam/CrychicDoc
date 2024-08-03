package com.mna.enchantments.framework;

import com.mna.effects.EffectInit;
import com.mna.enchantments.Beheading;
import com.mna.enchantments.Bouncing;
import com.mna.enchantments.Cloudstep;
import com.mna.enchantments.ExperienceToSouls;
import com.mna.enchantments.Fireproof;
import com.mna.enchantments.Gilded;
import com.mna.enchantments.ManaRepair;
import com.mna.enchantments.Returning;
import com.mna.enchantments.Soulbound;
import com.mna.enchantments.TransitoryStep;
import com.mna.enchantments.auras.Aura;
import com.mna.enchantments.staves.Bludgeoning;
import com.mna.enchantments.staves.DurationModifier;
import com.mna.enchantments.staves.RangeModifier;
import com.mna.enchantments.staves.SpeedModifier;
import com.mna.network.ClientMessageDispatcher;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = "mna", bus = Bus.FORGE)
public class EnchantmentInit {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, "mna");

    public static final RegistryObject<Aura> HEALING = ENCHANTMENTS.register("aura-healing", () -> new Aura(Enchantment.Rarity.VERY_RARE).withEffect(MobEffects.REGENERATION).withManaCost(10.0F).withPredicate(p -> p.m_21223_() <= p.m_21233_() * 0.75F && p.m_21124_(MobEffects.REGENERATION) == null));

    public static final RegistryObject<Aura> RUNNING = ENCHANTMENTS.register("aura-running", () -> new Aura(Enchantment.Rarity.COMMON).withEffect(MobEffects.MOVEMENT_SPEED).withMagnitude(3).withManaCost(0.075F));

    public static final RegistryObject<Aura> PILGRIM = ENCHANTMENTS.register("aura-pilgrim", () -> new Aura(Enchantment.Rarity.VERY_RARE).withEffect(EffectInit.PILGRIM).withMagnitude(0).withManaCost(0.0F));

    public static final RegistryObject<Aura> JUMPING = ENCHANTMENTS.register("aura-jumping", () -> new Aura(Enchantment.Rarity.COMMON).withEffect(MobEffects.JUMP).withMagnitude(5).withManaCost(0.075F));

    public static final RegistryObject<Aura> WATER_BREATHING = ENCHANTMENTS.register("aura-depths", () -> new Aura(Enchantment.Rarity.COMMON).withEffect(MobEffects.CONDUIT_POWER).withManaCost(0.075F).withPredicate(p -> p.m_20072_()));

    public static final RegistryObject<Aura> MINING_BOOST = ENCHANTMENTS.register("aura-mining", () -> new Aura(Enchantment.Rarity.UNCOMMON).withEffect(MobEffects.DIG_SPEED).withMagnitude(3).withManaCost(0.05F));

    public static final RegistryObject<Aura> BULWARK = ENCHANTMENTS.register("aura-bulwark", () -> new Aura(Enchantment.Rarity.UNCOMMON).withEffect(EffectInit.BULWARK).withMaxLevel(3).withManaCost(0.05F));

    public static final RegistryObject<Aura> REPAIR = ENCHANTMENTS.register("aura-repair", () -> new Aura(Enchantment.Rarity.VERY_RARE).withEffect(EffectInit.REPAIR).withManaCost(0.25F).withPredicate(p -> {
        for (int i = 0; i < p.getInventory().getContainerSize(); i++) {
            ItemStack stack = p.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.isRepairable() && stack.isDamaged()) {
                return true;
            }
        }
        return false;
    }));

    public static final RegistryObject<Bouncing> BOUNCING = ENCHANTMENTS.register("bouncing", () -> new Bouncing(Enchantment.Rarity.COMMON));

    public static final RegistryObject<Cloudstep> LEAPING = ENCHANTMENTS.register("cloudstep", () -> new Cloudstep(Enchantment.Rarity.COMMON));

    public static final RegistryObject<Beheading> BEHEADING = ENCHANTMENTS.register("beheading", () -> new Beheading());

    public static final RegistryObject<TransitoryStep> TRANSITORY_STEP = ENCHANTMENTS.register("transitorystep", () -> new TransitoryStep(Enchantment.Rarity.RARE));

    public static final RegistryObject<Gilded> GILDED = ENCHANTMENTS.register("gilded", () -> new Gilded());

    public static final RegistryObject<Fireproof> FIREPROOF = ENCHANTMENTS.register("fireproof", () -> new Fireproof());

    public static final RegistryObject<ManaRepair> MANA_REPAIR = ENCHANTMENTS.register("mana-repair", () -> new ManaRepair());

    public static final RegistryObject<Returning> RETURNING = ENCHANTMENTS.register("returning", () -> new Returning());

    public static final RegistryObject<Bludgeoning> BLUDGEONING = ENCHANTMENTS.register("bludgeoning", () -> new Bludgeoning());

    public static final RegistryObject<DurationModifier> DURATION_MOD = ENCHANTMENTS.register("duration_modifier", () -> new DurationModifier());

    public static final RegistryObject<RangeModifier> RANGE_MOD = ENCHANTMENTS.register("range_modifier", () -> new RangeModifier());

    public static final RegistryObject<SpeedModifier> SPEED_MOD = ENCHANTMENTS.register("speed_modifier", () -> new SpeedModifier());

    public static final RegistryObject<ExperienceToSouls> XP_TO_SOULS = ENCHANTMENTS.register("xp_to_souls", () -> new ExperienceToSouls());

    public static final RegistryObject<Soulbound> SOULBOUND = ENCHANTMENTS.register("soulbound", () -> new Soulbound());

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.m_9236_().getGameTime() % 5L == 0L) {
            tickAuras(event.player, event.player.m_9236_().isClientSide() || event.player.m_9236_().getGameTime() % 60L != 0L);
        }
        ItemStack boots = event.player.getItemBySlot(EquipmentSlot.FEET);
        if (boots.getEnchantmentLevel(TRANSITORY_STEP.get()) > 0) {
            TRANSITORY_STEP.get().apply(event.player, 1);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerFall(LivingFallEvent event) {
        if (!event.isCanceled()) {
            if (event.getEntity() instanceof Player && event.getDistance() > 2.0F) {
                Player player = (Player) event.getEntity();
                if (!player.m_6144_() && player.m_21124_(EffectInit.GRAVITY_WELL.get()) == null) {
                    ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
                    if (!boots.isEmpty() && boots.getEnchantmentLevel(BOUNCING.get()) > 0) {
                        event.setCanceled(true);
                        if (player.m_9236_().isClientSide()) {
                            float maxVelocity = 4.0F;
                            float bounceVelocity = (float) (player.m_20184_().y * -0.5);
                            Vec3 velocity = new Vec3(player.m_20184_().x, (double) Math.min(maxVelocity, bounceVelocity), player.m_20184_().z);
                            player.m_20334_(velocity.x, velocity.y, velocity.z);
                            ClientMessageDispatcher.sendPlayerBounce(velocity);
                        }
                    }
                }
            }
        }
    }

    private static void tickAuras(Player player, boolean manaOnly) {
        for (ItemStack armorStack : player.getArmorSlots()) {
            if (!armorStack.isEmpty()) {
                for (RegistryObject<Enchantment> enchantment : ENCHANTMENTS.getEntries()) {
                    if (enchantment.get() instanceof Aura) {
                        int level = armorStack.getEnchantmentLevel(enchantment.get());
                        if (level > 0) {
                            ((Aura) enchantment.get()).apply(player, level, manaOnly);
                        }
                    }
                }
            }
        }
    }
}