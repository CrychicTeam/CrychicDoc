package harmonised.pmmo.events;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.events.EnchantEvent;
import harmonised.pmmo.api.events.FurnaceBurnEvent;
import harmonised.pmmo.config.Config;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.events.impl.AnvilRepairHandler;
import harmonised.pmmo.events.impl.BreakHandler;
import harmonised.pmmo.events.impl.BreakSpeedHandler;
import harmonised.pmmo.events.impl.BreedHandler;
import harmonised.pmmo.events.impl.CraftHandler;
import harmonised.pmmo.events.impl.CropGrowHandler;
import harmonised.pmmo.events.impl.DamageDealtHandler;
import harmonised.pmmo.events.impl.DamageReceivedHandler;
import harmonised.pmmo.events.impl.DeathHandler;
import harmonised.pmmo.events.impl.DimensionTravelHandler;
import harmonised.pmmo.events.impl.EnchantHandler;
import harmonised.pmmo.events.impl.EntityInteractHandler;
import harmonised.pmmo.events.impl.ExplosionHandler;
import harmonised.pmmo.events.impl.FishHandler;
import harmonised.pmmo.events.impl.FoodEatHandler;
import harmonised.pmmo.events.impl.FurnaceHandler;
import harmonised.pmmo.events.impl.JumpHandler;
import harmonised.pmmo.events.impl.LoginHandler;
import harmonised.pmmo.events.impl.MountHandler;
import harmonised.pmmo.events.impl.PistonHandler;
import harmonised.pmmo.events.impl.PlaceHandler;
import harmonised.pmmo.events.impl.PlayerClickHandler;
import harmonised.pmmo.events.impl.PlayerDeathHandler;
import harmonised.pmmo.events.impl.PlayerTickHandler;
import harmonised.pmmo.events.impl.PotionHandler;
import harmonised.pmmo.events.impl.ShieldBlockHandler;
import harmonised.pmmo.events.impl.SleepHandler;
import harmonised.pmmo.events.impl.TameHandler;
import harmonised.pmmo.events.impl.TradeHandler;
import harmonised.pmmo.features.party.PartyUtils;
import harmonised.pmmo.util.Reference;
import harmonised.pmmo.util.TagBuilder;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.TradeWithVillagerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.event.level.PistonEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "pmmo", bus = Bus.FORGE)
public class EventHandler {

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        LoginHandler.handle(event);
    }

    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        PartyUtils.removeFromParty(event.getEntity());
    }

    @SubscribeEvent
    public static void onGamemodeChange(PlayerEvent.PlayerChangeGameModeEvent event) {
        if (event.getNewGameMode().isCreative()) {
            AttributeInstance reachAttribute = event.getEntity().m_21051_(ForgeMod.BLOCK_REACH.get());
            if (reachAttribute.getModifier(Reference.CREATIVE_REACH_ATTRIBUTE) == null || reachAttribute.getModifier(Reference.CREATIVE_REACH_ATTRIBUTE).getAmount() != Config.CREATIVE_REACH.get()) {
                reachAttribute.removeModifier(Reference.CREATIVE_REACH_ATTRIBUTE);
                reachAttribute.addPermanentModifier(new AttributeModifier(Reference.CREATIVE_REACH_ATTRIBUTE, "PMMO Creative Reach Bonus", Config.CREATIVE_REACH.get(), AttributeModifier.Operation.ADDITION));
            }
        } else {
            event.getEntity().m_21051_(ForgeMod.BLOCK_REACH.get()).removeModifier(Reference.CREATIVE_REACH_ATTRIBUTE);
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Core core = Core.get(event.getEntity().m_9236_());
        core.getPerkRegistry().executePerk(EventType.SKILL_UP, event.getEntity(), TagBuilder.start().withString("skill", "respawn").build());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onSleep(SleepFinishedTimeEvent event) {
        SleepHandler.handle(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPistonMove(PistonEvent.Pre event) {
        if (!event.isCanceled()) {
            PistonHandler.handle(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void tickPerks(TickEvent.LevelTickEvent event) {
        Core.get(event.level).getPerkRegistry().executePerkTicks(event);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void filterExplosions(ExplosionEvent.Detonate event) {
        if (!event.isCanceled()) {
            ExplosionHandler.handle(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!event.isCanceled()) {
            BreakHandler.handle(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (!event.isCanceled()) {
            PlaceHandler.handle(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (!event.isCanceled()) {
            BreakSpeedHandler.handle(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onCraft(PlayerEvent.ItemCraftedEvent event) {
        if (!event.isCanceled()) {
            CraftHandler.handle(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDamage(LivingHurtEvent event) {
        if (!event.isCanceled()) {
            DamageReceivedHandler.handle(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDealDamage(LivingAttackEvent event) {
        if (!event.isCanceled()) {
            DamageDealtHandler.handle(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDealDamage(LivingDamageEvent event) {
        if (!event.isCanceled()) {
            DamageDealtHandler.handle(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!event.isCanceled()) {
            EntityInteractHandler.handle(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        if (!event.isCanceled()) {
            JumpHandler.handle(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDeath(LivingDeathEvent event) {
        if (!event.isCanceled()) {
            if (event.getEntity() instanceof Player) {
                PlayerDeathHandler.handle(event);
            }
            DeathHandler.handle(event);
        }
    }

    @SubscribeEvent
    public static void onPotionCollect(PlayerBrewedPotionEvent event) {
        PotionHandler.handle(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBreed(BabyEntitySpawnEvent event) {
        if (!event.isCanceled()) {
            BreedHandler.handle(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onTame(AnimalTameEvent event) {
        if (!event.isCanceled()) {
            TameHandler.handle(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onFish(ItemFishedEvent event) {
        if (!event.isCanceled()) {
            FishHandler.handle(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onCropGrow(BlockEvent.CropGrowEvent.Post event) {
        CropGrowHandler.handle(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onMount(EntityMountEvent event) {
        if (!event.isCanceled()) {
            MountHandler.handle(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onShieldBlock(ShieldBlockEvent event) {
        if (!event.isCanceled()) {
            ShieldBlockHandler.handle(event);
        }
    }

    @SubscribeEvent
    public static void onAnvilRepar(AnvilRepairEvent event) {
        AnvilRepairHandler.handle(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockHit(PlayerInteractEvent.LeftClickBlock event) {
        PlayerClickHandler.leftClickBlock(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockActivate(PlayerInteractEvent.RightClickBlock event) {
        PlayerClickHandler.rightClickBlock(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemActivate(PlayerInteractEvent.RightClickItem event) {
        PlayerClickHandler.rightClickItem(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerTickHandler.handle(event);
    }

    @SubscribeEvent
    public static void onPlayerDimTravel(EntityTravelToDimensionEvent event) {
        DimensionTravelHandler.handle(event);
    }

    @SubscribeEvent
    public static void onFoodEat(LivingEntityUseItemEvent.Finish event) {
        FoodEatHandler.handle(event);
    }

    @SubscribeEvent
    public static void onSmelt(FurnaceBurnEvent event) {
        FurnaceHandler.handle(event);
    }

    @SubscribeEvent
    public static void onEnchant(EnchantEvent event) {
        EnchantHandler.handle(event);
    }

    @SubscribeEvent
    public static void onTrade(TradeWithVillagerEvent event) {
        TradeHandler.handle(event);
    }
}