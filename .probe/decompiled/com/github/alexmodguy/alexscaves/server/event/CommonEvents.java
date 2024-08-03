package com.github.alexmodguy.alexscaves.server.event;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.config.BiomeGenerationConfig;
import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACFrogRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.SeekingArrowEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.RaycatEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.VallumraptorEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.WatcherEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.EntityDropChanceAccessor;
import com.github.alexmodguy.alexscaves.server.entity.util.FlyingMount;
import com.github.alexmodguy.alexscaves.server.entity.util.MagneticEntityAccessor;
import com.github.alexmodguy.alexscaves.server.entity.util.VillagerUndergroundCabinMapTrade;
import com.github.alexmodguy.alexscaves.server.entity.util.WatcherPossessionAccessor;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.AlwaysCombinableOnAnvil;
import com.github.alexmodguy.alexscaves.server.item.ExtinctionSpearItem;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRarity;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.github.alexmodguy.alexscaves.server.potion.DarknessIncarnateEffect;
import com.github.alexthe666.citadel.server.event.EventReplaceBiome;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public class CommonEvents {

    @SubscribeEvent
    public void resizeEntity(EntityEvent.Size event) {
        if (event.getEntity() instanceof MagneticEntityAccessor magnet && event.getEntity().getEntityData().isDirty()) {
            Direction dir = magnet.getMagneticAttachmentFace();
            float defaultHeight = event.getOldSize().height;
            float defaultEyeHeight = event.getEntity().getEyeHeightAccess(event.getPose(), event.getOldSize());
            if (dir == Direction.DOWN && event.getEntity() instanceof Player && event.getEntity().getPose() == Pose.STANDING) {
                event.setNewSize(event.getNewSize(), true);
            } else if (dir == Direction.UP) {
                event.setNewEyeHeight(defaultHeight - defaultEyeHeight);
            } else if (dir.getAxis() != Direction.Axis.Y) {
                event.setNewEyeHeight(0.0F);
            }
        }
    }

    @SubscribeEvent
    public void livingDie(LivingDeathEvent event) {
        if (event.getEntity().m_6095_() == EntityType.MAGMA_CUBE && event.getSource() != null && event.getSource().getEntity() instanceof Frog frog && frog.getVariant() == ACFrogRegistry.PRIMORDIAL.get()) {
            event.getEntity().m_19983_(new ItemStack(ACBlockRegistry.CARMINE_FROGLIGHT.get()));
        }
        if (!event.getEntity().m_9236_().isClientSide && event.getEntity() instanceof Mob mob && event.getSource() != null && event.getSource().getDirectEntity() instanceof LivingEntity directSource && directSource.getItemInHand(InteractionHand.MAIN_HAND).is(ACItemRegistry.PRIMITIVE_CLUB.get()) && directSource.getItemInHand(InteractionHand.MAIN_HAND).getEnchantmentLevel(ACEnchantmentRegistry.BONKING.get()) > 0 && event.getEntity().m_9236_().random.nextFloat() < 0.33F) {
            Creeper fakeCreeperForSkullDrop = EntityType.CREEPER.create(mob.m_9236_());
            if (fakeCreeperForSkullDrop != null) {
                if (event.getEntity().m_9236_() instanceof ServerLevel serverLevel) {
                    LightningBolt fakeThunder = EntityType.LIGHTNING_BOLT.create(serverLevel);
                    if (fakeThunder != null) {
                        fakeThunder.setVisualOnly(true);
                        fakeCreeperForSkullDrop.thunderHit(serverLevel, fakeThunder);
                    }
                }
                DamageSource fakeCreeperDamage = mob.m_9236_().damageSources().mobAttack(fakeCreeperForSkullDrop);
                HashMap<EquipmentSlot, Float> prevLootDropChances = new HashMap();
                EntityDropChanceAccessor dropChanceAccessor = (EntityDropChanceAccessor) mob;
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    prevLootDropChances.put(slot, dropChanceAccessor.ac_getEquipmentDropChance(slot));
                    dropChanceAccessor.ac_setDropChance(slot, 0.0F);
                }
                dropChanceAccessor.ac_dropCustomDeathLoot(fakeCreeperDamage, 0, false);
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    dropChanceAccessor.ac_setDropChance(slot, (Float) prevLootDropChances.get(slot));
                }
            }
        }
        if (event.getEntity() instanceof Player) {
            if (event.getEntity().m_20148_().toString().equals("71363abe-fd03-49c9-940d-aae8b8209b7c")) {
                event.getEntity().m_19983_(new ItemStack(ACItemRegistry.GREEN_SOYLENT.get(), 1 + event.getEntity().getRandom().nextInt(9)));
            }
            if (event.getEntity().m_20148_().toString().equals("4a463319-625c-4b86-a4e7-8b700f023a60")) {
                event.getEntity().m_19983_(new ItemStack(ACItemRegistry.STINKY_FISH.get(), 1));
            }
        }
    }

    @SubscribeEvent
    public void livingHeal(LivingHealEvent event) {
        if (event.getEntity().hasEffect(ACEffectRegistry.IRRADIATED.get()) && !event.getEntity().m_6095_().is(ACTagRegistry.RESISTS_RADIATION)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void playerEntityInteract(PlayerInteractEvent.EntityInteract event) {
        ItemStack stack = event.getItemStack();
        if (stack.is(ACItemRegistry.HOLOCODER.get()) && event.getTarget() instanceof LivingEntity && !(event.getTarget() instanceof ArmorStand) && event.getTarget().isAlive()) {
            CompoundTag tag = stack.getOrCreateTag();
            tag.putUUID("BoundEntityUUID", event.getTarget().getUUID());
            CompoundTag entityTag = event.getTarget() instanceof Player ? new CompoundTag() : event.getTarget().serializeNBT();
            entityTag.putString("id", ForgeRegistries.ENTITY_TYPES.getKey(event.getTarget().getType()).toString());
            if (event.getTarget() instanceof Player) {
                entityTag.putUUID("UUID", event.getTarget().getUUID());
            }
            tag.put("BoundEntityTag", entityTag);
            ItemStack stackReplacement = new ItemStack(ACItemRegistry.HOLOCODER.get());
            stack.shrink(1);
            stackReplacement.setTag(tag);
            event.getEntity().m_6674_(event.getHand());
            if (!event.getEntity().addItem(stackReplacement)) {
                ItemEntity itementity = event.getEntity().drop(stackReplacement, false);
                if (itementity != null) {
                    itementity.setNoPickUpDelay();
                    itementity.setThrower(event.getEntity().m_20148_());
                }
            }
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }

    @SubscribeEvent
    public void livingFindTarget(LivingChangeTargetEvent event) {
        if (event.getEntity() instanceof Mob mob && event.getNewTarget() instanceof VallumraptorEntity vallumraptor && vallumraptor.getHideFor() > 0) {
            mob.setTarget(null);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void livingHurt(LivingDamageEvent event) {
        if (event.getEntity().m_20159_() && event.getEntity() instanceof FlyingMount && (event.getSource().is(DamageTypes.IN_WALL) || event.getSource().is(DamageTypes.FALL) || event.getSource().is(DamageTypes.FLY_INTO_WALL))) {
            event.setCanceled(true);
        }
        if (event.getEntity() instanceof WatcherPossessionAccessor possessed && possessed.isPossessedByWatcher() && !event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !(event.getSource().getEntity() instanceof WatcherEntity)) {
            event.setCanceled(true);
        }
        if (event.getEntity() instanceof Player player && player.m_21211_().is(ACItemRegistry.EXTINCTION_SPEAR.get()) && ExtinctionSpearItem.killGrottoGhostsFor(player, true)) {
            event.setCanceled(true);
            player.m_216990_(SoundEvents.SHIELD_BLOCK);
        }
    }

    @SubscribeEvent
    public void livingAttack(LivingAttackEvent event) {
        if (event.getSource().getDirectEntity() instanceof AbstractArrow arrow && event.getEntity().isBlocking() && event.getEntity().getUseItem().is(ACItemRegistry.RESISTOR_SHIELD.get())) {
            ItemStack shield = event.getEntity().getUseItem();
            if (shield.getEnchantmentLevel(ACEnchantmentRegistry.ARROW_INDUCTING.get()) > 0 && arrow.m_6095_() != ACEntityRegistry.SEEKING_ARROW.get()) {
                SeekingArrowEntity seekingArrowEntity = new SeekingArrowEntity(event.getEntity().m_9236_(), event.getEntity());
                seekingArrowEntity.m_20359_(arrow);
                seekingArrowEntity.m_20256_(arrow.m_20184_().scale(-0.4));
                seekingArrowEntity.m_146922_(arrow.m_146908_() + 180.0F);
                event.getEntity().m_9236_().m_7967_(seekingArrowEntity);
                arrow.m_146870_();
            }
        }
        if (event.getSource() != null && event.getSource().getDirectEntity() instanceof LivingEntity directSource && directSource.hasEffect(ACEffectRegistry.STUNNED.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void playerAttack(AttackEntityEvent event) {
        if (event.getTarget() instanceof DinosaurEntity && event.getEntity().m_20365_(event.getTarget())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void livingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().hasEffect(ACEffectRegistry.BUBBLED.get()) && event.getEntity().isInFluidType()) {
            event.getEntity().removeEffect(ACEffectRegistry.BUBBLED.get());
        }
        if (event.getEntity().hasEffect(ACEffectRegistry.DARKNESS_INCARNATE.get()) && event.getEntity().f_19797_ % 5 == 0 && DarknessIncarnateEffect.isInLight(event.getEntity(), 11)) {
            event.getEntity().removeEffect(ACEffectRegistry.DARKNESS_INCARNATE.get());
        }
        if (event.getEntity().getItemBySlot(EquipmentSlot.HEAD).is(ACItemRegistry.DIVING_HELMET.get()) && (!event.getEntity().m_204029_(FluidTags.WATER) || event.getEntity().m_20202_() instanceof SubmarineEntity)) {
            event.getEntity().addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 810, 0, false, false, true));
        }
        if (!event.getEntity().m_9236_().isClientSide && event.getEntity() instanceof Mob mob && mob.getTarget() instanceof VallumraptorEntity vallumraptor && vallumraptor.getHideFor() > 0) {
            mob.setTarget(null);
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(MobSpawnEvent.FinalizeSpawn event) {
        try {
            if (event.getEntity() instanceof Creeper creeper) {
                creeper.f_21346_.addGoal(3, new AvoidEntityGoal(creeper, RaycatEntity.class, 10.0F, 1.0, 1.2));
            }
            if (event.getEntity() instanceof Drowned drowned && drowned.m_9236_().m_204166_(drowned.m_20183_()).is(ACBiomeRegistry.ABYSSAL_CHASM) && drowned.m_6844_(EquipmentSlot.FEET).isEmpty() && drowned.m_6844_(EquipmentSlot.LEGS).isEmpty() && drowned.m_6844_(EquipmentSlot.CHEST).isEmpty() && drowned.m_6844_(EquipmentSlot.HEAD).isEmpty()) {
                if ((double) drowned.m_217043_().nextFloat() < 0.2) {
                    drowned.m_8061_(EquipmentSlot.HEAD, new ItemStack(ACItemRegistry.DIVING_HELMET.get()));
                    drowned.m_21409_(EquipmentSlot.HEAD, 0.5F);
                }
                if ((double) drowned.m_217043_().nextFloat() < 0.2) {
                    drowned.m_8061_(EquipmentSlot.CHEST, new ItemStack(ACItemRegistry.DIVING_CHESTPLATE.get()));
                    drowned.m_21409_(EquipmentSlot.CHEST, 0.5F);
                }
                if ((double) drowned.m_217043_().nextFloat() < 0.2) {
                    drowned.m_8061_(EquipmentSlot.LEGS, new ItemStack(ACItemRegistry.DIVING_LEGGINGS.get()));
                    drowned.m_21409_(EquipmentSlot.LEGS, 0.5F);
                }
                if ((double) drowned.m_217043_().nextFloat() < 0.2) {
                    drowned.m_8061_(EquipmentSlot.FEET, new ItemStack(ACItemRegistry.DIVING_BOOTS.get()));
                    drowned.m_21409_(EquipmentSlot.FEET, 0.5F);
                }
            }
        } catch (Exception var4) {
            AlexsCaves.LOGGER.warn("Tried to add unique behaviors to vanilla mobs and encountered an error");
        }
    }

    @SubscribeEvent
    public void livingRemoveEffect(MobEffectEvent.Remove event) {
        if (event.getEffect() instanceof DarknessIncarnateEffect darknessIncarnateEffect) {
            darknessIncarnateEffect.toggleFlight(event.getEntity(), false);
            event.getEntity().m_216990_(ACSoundRegistry.DARKNESS_INCARNATE_EXIT.get());
        }
    }

    @SubscribeEvent
    public void livingAddEffect(MobEffectEvent.Added event) {
        if (event.getEffectInstance().getEffect() instanceof DarknessIncarnateEffect) {
            event.getEntity().m_216990_(ACSoundRegistry.DARKNESS_INCARNATE_ENTER.get());
        }
    }

    @SubscribeEvent
    public void livingExpireEffect(MobEffectEvent.Expired event) {
        if (event.getEffectInstance().getEffect() instanceof DarknessIncarnateEffect darknessIncarnateEffect) {
            darknessIncarnateEffect.toggleFlight(event.getEntity(), false);
            event.getEntity().m_216990_(ACSoundRegistry.DARKNESS_INCARNATE_EXIT.get());
        }
    }

    @SubscribeEvent
    public void onServerAboutToStart(ServerAboutToStartEvent event) {
        ACBiomeRarity.init();
    }

    @SubscribeEvent
    public void onReplaceBiome(EventReplaceBiome event) {
        ResourceKey<Biome> biome = BiomeGenerationConfig.getBiomeForEvent(event);
        if (biome != null) {
            Holder<Biome> biomeHolder = (Holder<Biome>) event.getBiomeSource().getResourceKeyMap().get(biome);
            if (biomeHolder != null) {
                event.setResult(Result.ALLOW);
                event.setBiomeToGenerate(biomeHolder);
            }
        }
    }

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.isCreative()) {
            if (event.player.m_21120_(InteractionHand.MAIN_HAND).is(ACTagRegistry.RESTRICTED_BIOME_LOCATORS)) {
                checkAndDestroyExploitItem(event.player, EquipmentSlot.MAINHAND);
            }
            if (event.player.m_21120_(InteractionHand.OFF_HAND).is(ACTagRegistry.RESTRICTED_BIOME_LOCATORS)) {
                checkAndDestroyExploitItem(event.player, EquipmentSlot.OFFHAND);
            }
        }
    }

    @SubscribeEvent
    public void onVillagerTradeSetup(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.CARTOGRAPHER && AlexsCaves.COMMON_CONFIG.cartographersSellCabinMaps.get()) {
            int level = 2;
            List<VillagerTrades.ItemListing> list = (List<VillagerTrades.ItemListing>) event.getTrades().get(level);
            list.add(new VillagerUndergroundCabinMapTrade(5, 10, 6));
            event.getTrades().put(level, list);
        }
    }

    @SubscribeEvent
    public void onWanderingTradeSetup(WandererTradesEvent event) {
        if (AlexsCaves.COMMON_CONFIG.wanderingTradersSellCabinMaps.get()) {
            event.getGenericTrades().add(new VillagerUndergroundCabinMapTrade(8, 1, 10));
        }
    }

    @SubscribeEvent
    public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (AlexsCaves.COMMON_CONFIG.warnGenerationIncompatibility.get() && !AlexsCaves.MOD_GENERATION_CONFLICTS.isEmpty() && event.getEntity().m_9236_().isClientSide) {
            for (String modid : AlexsCaves.MOD_GENERATION_CONFLICTS) {
                if (ModList.get().isLoaded(modid)) {
                    event.getEntity().m_213846_(Component.translatable("alexscaves.startup_warning.generation_incompatible", modid).withStyle(ChatFormatting.RED));
                }
            }
        }
    }

    private static void checkAndDestroyExploitItem(Player player, EquipmentSlot slot) {
        ItemStack itemInHand = player.getItemBySlot(slot);
        if (itemInHand.is(ACTagRegistry.RESTRICTED_BIOME_LOCATORS)) {
            CompoundTag tag = itemInHand.getTag();
            if (tag != null && (itemTagContainsAC(tag, "BiomeKey", false) || itemTagContainsAC(tag, "Structure", true) || itemTagContainsAC(tag, "structurecompass:structureName", true) || itemTagContainsAC(tag, "StructureKey", true))) {
                itemInHand.shrink(1);
                player.m_21166_(slot);
                player.m_216990_(ACSoundRegistry.DISAPPOINTMENT.get());
                if (!player.m_9236_().isClientSide) {
                    player.displayClientMessage(Component.translatable("item.alexscaves.natures_compass_warning"), true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onUpdateAnvil(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof AlwaysCombinableOnAnvil && event.getLeft().getItem() == event.getRight().getItem() && !event.getLeft().getAllEnchantments().isEmpty() && !event.getRight().getAllEnchantments().isEmpty()) {
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(event.getLeft());
            Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(event.getRight());
            boolean canCombine = true;
            int i = 0;
            for (Enchantment enchantment1 : map1.keySet()) {
                if (enchantment1 != null) {
                    int i2 = (Integer) map.getOrDefault(enchantment1, 0);
                    int j2 = (Integer) map1.get(enchantment1);
                    j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
                    for (Enchantment enchantment : map.keySet()) {
                        if (enchantment != enchantment1 && !enchantment1.isCompatibleWith(enchantment)) {
                            canCombine = false;
                            i++;
                        }
                    }
                    if (canCombine) {
                        if (j2 > enchantment1.getMaxLevel()) {
                            j2 = enchantment1.getMaxLevel();
                        }
                        map.put(enchantment1, j2);
                        int k3 = 0;
                        switch(enchantment1.getRarity()) {
                            case COMMON:
                                k3 = 1;
                                break;
                            case UNCOMMON:
                                k3 = 2;
                                break;
                            case RARE:
                                k3 = 4;
                                break;
                            case VERY_RARE:
                                k3 = 8;
                        }
                        i += k3 * j2;
                    }
                }
            }
            event.setCost(i);
            ItemStack copy = event.getLeft().copy();
            EnchantmentHelper.setEnchantments(map, copy);
            event.setOutput(copy);
        }
    }

    private static boolean itemTagContainsAC(CompoundTag tag, String tagID, boolean allowUndergroundCabin) {
        if (tag.contains(tagID)) {
            String resourceLocation = tag.getString(tagID);
            if (resourceLocation.contains("alexscaves:") && (!allowUndergroundCabin || !resourceLocation.contains("underground_cabin"))) {
                return true;
            }
        }
        return false;
    }
}