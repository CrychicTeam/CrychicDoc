package com.simibubi.create.content.equipment.extendoGrip;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllPackets;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ExtendoGripItem extends Item {

    public static final int MAX_DAMAGE = 200;

    public static final AttributeModifier singleRangeAttributeModifier = new AttributeModifier(UUID.fromString("7f7dbdb2-0d0d-458a-aa40-ac7633691f66"), "Range modifier", 3.0, AttributeModifier.Operation.ADDITION);

    public static final AttributeModifier doubleRangeAttributeModifier = new AttributeModifier(UUID.fromString("8f7dbdb2-0d0d-458a-aa40-ac7633691f66"), "Range modifier", 5.0, AttributeModifier.Operation.ADDITION);

    private static final Supplier<Multimap<Attribute, AttributeModifier>> rangeModifier = Suppliers.memoize(() -> ImmutableMultimap.of(ForgeMod.BLOCK_REACH.get(), singleRangeAttributeModifier));

    private static final Supplier<Multimap<Attribute, AttributeModifier>> doubleRangeModifier = Suppliers.memoize(() -> ImmutableMultimap.of(ForgeMod.BLOCK_REACH.get(), doubleRangeAttributeModifier));

    private static DamageSource lastActiveDamageSource;

    public static final String EXTENDO_MARKER = "createExtendo";

    public static final String DUAL_EXTENDO_MARKER = "createDualExtendo";

    public ExtendoGripItem(Item.Properties properties) {
        super(properties.defaultDurability(200));
    }

    @SubscribeEvent
    public static void holdingExtendoGripIncreasesRange(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            CompoundTag persistentData = player.getPersistentData();
            boolean inOff = AllItems.EXTENDO_GRIP.isIn(player.m_21206_());
            boolean inMain = AllItems.EXTENDO_GRIP.isIn(player.m_21205_());
            boolean holdingDualExtendo = inOff && inMain;
            boolean holdingExtendo = inOff ^ inMain;
            holdingExtendo &= !holdingDualExtendo;
            boolean wasHoldingExtendo = persistentData.contains("createExtendo");
            boolean wasHoldingDualExtendo = persistentData.contains("createDualExtendo");
            if (holdingExtendo != wasHoldingExtendo) {
                if (!holdingExtendo) {
                    player.m_21204_().removeAttributeModifiers((Multimap<Attribute, AttributeModifier>) rangeModifier.get());
                    persistentData.remove("createExtendo");
                } else {
                    AllAdvancements.EXTENDO_GRIP.awardTo(player);
                    player.m_21204_().addTransientAttributeModifiers((Multimap<Attribute, AttributeModifier>) rangeModifier.get());
                    persistentData.putBoolean("createExtendo", true);
                }
            }
            if (holdingDualExtendo != wasHoldingDualExtendo) {
                if (!holdingDualExtendo) {
                    player.m_21204_().removeAttributeModifiers((Multimap<Attribute, AttributeModifier>) doubleRangeModifier.get());
                    persistentData.remove("createDualExtendo");
                } else {
                    AllAdvancements.EXTENDO_GRIP_DUAL.awardTo(player);
                    player.m_21204_().addTransientAttributeModifiers((Multimap<Attribute, AttributeModifier>) doubleRangeModifier.get());
                    persistentData.putBoolean("createDualExtendo", true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void addReachToJoiningPlayersHoldingExtendo(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        CompoundTag persistentData = player.getPersistentData();
        if (persistentData.contains("createDualExtendo")) {
            player.m_21204_().addTransientAttributeModifiers((Multimap<Attribute, AttributeModifier>) doubleRangeModifier.get());
        } else if (persistentData.contains("createExtendo")) {
            player.m_21204_().addTransientAttributeModifiers((Multimap<Attribute, AttributeModifier>) rangeModifier.get());
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void dontMissEntitiesWhenYouHaveHighReachDistance(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (mc.level != null && player != null) {
            if (isHoldingExtendoGrip(player)) {
                if (!(mc.hitResult instanceof BlockHitResult) || mc.hitResult.getType() == HitResult.Type.MISS) {
                    double d0 = player.m_21051_(ForgeMod.BLOCK_REACH.get()).getValue();
                    if (!player.m_7500_()) {
                        d0 -= 0.5;
                    }
                    Vec3 Vector3d = player.m_20299_(AnimationTickHolder.getPartialTicks());
                    Vec3 Vector3d1 = player.m_20252_(1.0F);
                    Vec3 Vector3d2 = Vector3d.add(Vector3d1.x * d0, Vector3d1.y * d0, Vector3d1.z * d0);
                    AABB AABB = player.m_20191_().expandTowards(Vector3d1.scale(d0)).inflate(1.0, 1.0, 1.0);
                    EntityHitResult entityraytraceresult = ProjectileUtil.getEntityHitResult(player, Vector3d, Vector3d2, AABB, e -> !e.isSpectator() && e.isPickable(), d0 * d0);
                    if (entityraytraceresult != null) {
                        Entity entity1 = entityraytraceresult.getEntity();
                        Vec3 Vector3d3 = entityraytraceresult.m_82450_();
                        double d2 = Vector3d.distanceToSqr(Vector3d3);
                        if (d2 < d0 * d0 || mc.hitResult == null || mc.hitResult.getType() == HitResult.Type.MISS) {
                            mc.hitResult = entityraytraceresult;
                            if (entity1 instanceof LivingEntity || entity1 instanceof ItemFrame) {
                                mc.crosshairPickEntity = entity1;
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void consumeDurabilityOnBlockBreak(BlockEvent.BreakEvent event) {
        findAndDamageExtendoGrip(event.getPlayer());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void consumeDurabilityOnPlace(BlockEvent.EntityPlaceEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            findAndDamageExtendoGrip((Player) entity);
        }
    }

    private static void findAndDamageExtendoGrip(Player player) {
        if (player != null) {
            if (!player.m_9236_().isClientSide) {
                InteractionHand hand = InteractionHand.MAIN_HAND;
                ItemStack extendo = player.m_21205_();
                if (!AllItems.EXTENDO_GRIP.isIn(extendo)) {
                    extendo = player.m_21206_();
                    hand = InteractionHand.OFF_HAND;
                }
                if (AllItems.EXTENDO_GRIP.isIn(extendo)) {
                    InteractionHand h = hand;
                    if (!BacktankUtil.canAbsorbDamage(player, maxUses())) {
                        extendo.hurtAndBreak(1, player, p -> p.m_21190_(h));
                    }
                }
            }
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return BacktankUtil.isBarVisible(stack, maxUses());
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return BacktankUtil.getBarWidth(stack, maxUses());
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return BacktankUtil.getBarColor(stack, maxUses());
    }

    private static int maxUses() {
        return AllConfigs.server().equipment.maxExtendoGripActions.get();
    }

    @SubscribeEvent
    public static void bufferLivingAttackEvent(LivingAttackEvent event) {
        lastActiveDamageSource = event.getSource();
        DamageSource source = event.getSource();
        if (source != null) {
            Entity trueSource = source.getEntity();
            if (trueSource instanceof Player) {
                findAndDamageExtendoGrip((Player) trueSource);
            }
        }
    }

    @SubscribeEvent
    public static void attacksByExtendoGripHaveMoreKnockback(LivingKnockBackEvent event) {
        if (lastActiveDamageSource != null) {
            if (lastActiveDamageSource.getDirectEntity() instanceof Player player) {
                if (isHoldingExtendoGrip(player)) {
                    event.setStrength(event.getStrength() + 2.0F);
                }
            }
        }
    }

    private static boolean isUncaughtClientInteraction(Entity entity, Entity target) {
        if (entity.distanceToSqr(target) < 36.0) {
            return false;
        } else {
            return !entity.level().isClientSide ? false : entity instanceof Player;
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void notifyServerOfLongRangeAttacks(AttackEntityEvent event) {
        Entity entity = event.getEntity();
        Entity target = event.getTarget();
        if (isUncaughtClientInteraction(entity, target)) {
            Player player = (Player) entity;
            if (isHoldingExtendoGrip(player)) {
                AllPackets.getChannel().sendToServer(new ExtendoGripInteractionPacket(target));
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void notifyServerOfLongRangeInteractions(PlayerInteractEvent.EntityInteract event) {
        Entity entity = event.getEntity();
        Entity target = event.getTarget();
        if (isUncaughtClientInteraction(entity, target)) {
            Player player = (Player) entity;
            if (isHoldingExtendoGrip(player)) {
                AllPackets.getChannel().sendToServer(new ExtendoGripInteractionPacket(target, event.getHand()));
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void notifyServerOfLongRangeSpecificInteractions(PlayerInteractEvent.EntityInteractSpecific event) {
        Entity entity = event.getEntity();
        Entity target = event.getTarget();
        if (isUncaughtClientInteraction(entity, target)) {
            Player player = (Player) entity;
            if (isHoldingExtendoGrip(player)) {
                AllPackets.getChannel().sendToServer(new ExtendoGripInteractionPacket(target, event.getHand(), event.getLocalPos()));
            }
        }
    }

    public static boolean isHoldingExtendoGrip(Player player) {
        boolean inOff = AllItems.EXTENDO_GRIP.isIn(player.m_21206_());
        boolean inMain = AllItems.EXTENDO_GRIP.isIn(player.m_21205_());
        return inOff || inMain;
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new ExtendoGripItemRenderer()));
    }
}