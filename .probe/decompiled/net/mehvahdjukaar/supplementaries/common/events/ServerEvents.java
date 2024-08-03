package net.mehvahdjukaar.supplementaries.common.events;

import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.events.IFireConsumeBlockEvent;
import net.mehvahdjukaar.moonlight.api.fluids.BuiltInSoftFluids;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidRegistry;
import net.mehvahdjukaar.supplementaries.SuppPlatformStuff;
import net.mehvahdjukaar.supplementaries.api.IQuiverEntity;
import net.mehvahdjukaar.supplementaries.common.block.IRopeConnection;
import net.mehvahdjukaar.supplementaries.common.block.blocks.AshLayerBlock;
import net.mehvahdjukaar.supplementaries.common.block.hourglass.HourglassTimesManager;
import net.mehvahdjukaar.supplementaries.common.entities.goals.EatFodderGoal;
import net.mehvahdjukaar.supplementaries.common.entities.goals.EvokerRedMerchantWololooSpellGoal;
import net.mehvahdjukaar.supplementaries.common.events.overrides.InteractEventsHandler;
import net.mehvahdjukaar.supplementaries.common.items.AbstractMobContainerItem;
import net.mehvahdjukaar.supplementaries.common.items.CandyItem;
import net.mehvahdjukaar.supplementaries.common.items.FluteItem;
import net.mehvahdjukaar.supplementaries.common.items.QuiverItem;
import net.mehvahdjukaar.supplementaries.common.items.SoapItem;
import net.mehvahdjukaar.supplementaries.common.misc.MapLightHandler;
import net.mehvahdjukaar.supplementaries.common.misc.globe.GlobeData;
import net.mehvahdjukaar.supplementaries.common.misc.mob_container.CapturedMobHandler;
import net.mehvahdjukaar.supplementaries.common.misc.songs.SongsManager;
import net.mehvahdjukaar.supplementaries.common.worldgen.WaySignStructure;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModSetup;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public class ServerEvents {

    private static final boolean FODDER_ENABLED = (Boolean) CommonConfigs.Functional.FODDER_ENABLED.get();

    public static void onFireConsume(IFireConsumeBlockEvent event) {
        if (event.getState().m_60734_() instanceof IRopeConnection) {
            LevelAccessor level = event.getLevel();
            BlockPos pos = event.getPos();
            level.m_7471_(pos, false);
            if (BaseFireBlock.canBePlacedAt((Level) level, pos, Direction.DOWN)) {
                BlockState state = BaseFireBlock.getState(level, pos);
                if (state.m_61138_(FireBlock.AGE)) {
                    event.setFinalState((BlockState) state.m_61124_(FireBlock.AGE, 8));
                }
                level.scheduleTick(pos, Blocks.FIRE, 2 + ((Level) level).random.nextInt(1));
            }
        } else {
            AshLayerBlock.tryConvertToAsh(event);
        }
    }

    public static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        return !player.isSpectator() ? InteractEventsHandler.onItemUsedOnBlock(player, level, player.m_21120_(hand), hand, hitResult) : InteractionResult.PASS;
    }

    public static InteractionResult onRightClickBlockHP(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        return !player.isSpectator() ? InteractEventsHandler.onItemUsedOnBlockHP(player, level, player.m_21120_(hand), hand, hitResult) : InteractionResult.PASS;
    }

    public static InteractionResultHolder<ItemStack> onUseItem(Player player, Level level, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        return !player.isSpectator() ? InteractEventsHandler.onItemUse(player, level, hand, stack) : InteractionResultHolder.pass(stack);
    }

    public static void onPlayerLoggedIn(ServerPlayer player) {
    }

    public static InteractionResult onRightClickEntity(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult entityHitResult) {
        if (player.isSpectator()) {
            return InteractionResult.PASS;
        } else {
            ItemStack stack = player.m_21120_(hand);
            Item item = stack.getItem();
            if (item instanceof FluteItem) {
                if (FluteItem.interactWithPet(stack, player, entity, hand)) {
                    return InteractionResult.SUCCESS;
                }
            } else if (item instanceof AbstractMobContainerItem containerItem) {
                if (!containerItem.isFull(stack)) {
                    InteractionResult res = containerItem.doInteract(stack, player, entity, hand);
                    if (res.consumesAction()) {
                        return InteractionResult.SUCCESS;
                    }
                }
            } else if (item == ModRegistry.SOAP.get() && SoapItem.interactWithEntity(stack, player, entity, hand)) {
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }
    }

    public static void onDataSyncToPlayer(ServerPlayer player, boolean joined) {
        SongsManager.sendDataToClient(player);
        CapturedMobHandler.sendDataToClient(player);
        GlobeData.sendDataToClient(player);
        HourglassTimesManager.sendDataToClient(player);
        MapLightHandler.sendDataToClient(player);
    }

    public static void onCommonTagUpdate(RegistryAccess registryAccess, boolean client) {
        ModSetup.tagDependantSetup(registryAccess);
        if (!client) {
            WaySignStructure.recomputeValidStructureCache(registryAccess);
            try {
                SoftFluidRegistry.getRegistry(registryAccess).get(BuiltInSoftFluids.EMPTY.getID());
            } catch (Exception var3) {
                throw new RuntimeException("Failed to get empty soft fluid from datapack. How?", var3);
            }
        }
    }

    public static void onEntityLoad(Entity entity, ServerLevel serverLevel) {
        if (FODDER_ENABLED && entity instanceof Animal animal) {
            EntityType<?> type = entity.getType();
            if (type.is(ModTags.EATS_FODDER)) {
                animal.f_21345_.addGoal(3, new EatFodderGoal(animal, 1.0, 8, 2, 30));
            }
        } else {
            if (entity.getType() == EntityType.EVOKER) {
                ((Evoker) entity).f_21345_.addGoal(6, new EvokerRedMerchantWololooSpellGoal((Evoker) entity));
            }
        }
    }

    public static void serverPlayerTick(Player player) {
        CandyItem.checkSweetTooth(player);
    }

    public static void clientPlayerTick(Player player) {
        if (player instanceof IQuiverEntity) {
        }
    }

    public static boolean onItemPickup(ItemEntity itemEntity, Player player) {
        ItemStack stack = itemEntity.getItem();
        if (!itemEntity.hasPickUpDelay() && (Boolean) CommonConfigs.Tools.QUIVER_PICKUP.get() && QuiverItem.canAcceptItem(stack) && (itemEntity.getOwner() == null || SuppPlatformStuff.getItemLifeSpawn(itemEntity) - itemEntity.getAge() <= 200 || itemEntity.getOwner().equals(player))) {
            ItemStack old = stack.copy();
            if (takeArrow(itemEntity, player, stack)) {
                SuppPlatformStuff.onItemPickup(player, itemEntity, old);
                player.m_21053_(itemEntity);
                player.awardStat(Stats.ITEM_PICKED_UP.get(stack.getItem()), old.getCount() - stack.getCount());
                return true;
            }
        }
        return false;
    }

    public static boolean onArrowPickup(AbstractArrow arrow, Player player, Supplier<ItemStack> pickup) {
        if ((Boolean) CommonConfigs.Tools.QUIVER_PICKUP.get()) {
            ItemStack stack = (ItemStack) pickup.get();
            return takeArrow(arrow, player, stack);
        } else {
            return false;
        }
    }

    private static boolean takeArrow(Entity itemEntity, Player player, ItemStack stack) {
        ItemStack quiverItem = QuiverItem.getQuiver(player);
        if (!quiverItem.isEmpty()) {
            QuiverItem.Data data = QuiverItem.getQuiverData(quiverItem);
            if (data != null) {
                ItemStack copy = stack.copy();
                int count = copy.getCount();
                int newCount = data.tryAdding(copy, true).getCount();
                if (count != newCount) {
                    player.m_7938_(itemEntity, count);
                    stack.setCount(newCount);
                    if (stack.isEmpty()) {
                        itemEntity.discard();
                    }
                    return true;
                }
            }
        }
        return false;
    }
}