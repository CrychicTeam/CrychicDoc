package io.github.lightman314.lightmanscurrency.common;

import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.capability.money.IMoneyHandler;
import io.github.lightman314.lightmanscurrency.api.config.ConfigFile;
import io.github.lightman314.lightmanscurrency.api.config.SyncedConfigFile;
import io.github.lightman314.lightmanscurrency.api.events.WalletDropEvent;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.IOwnableBlock;
import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.api.money.coins.CoinAPI;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyView;
import io.github.lightman314.lightmanscurrency.api.money.value.builtin.CoinValue;
import io.github.lightman314.lightmanscurrency.common.advancements.date.DateTrigger;
import io.github.lightman314.lightmanscurrency.common.capability.CurrencyCapabilities;
import io.github.lightman314.lightmanscurrency.common.capability.event_unlocks.CapabilityEventUnlocks;
import io.github.lightman314.lightmanscurrency.common.capability.event_unlocks.IEventUnlocks;
import io.github.lightman314.lightmanscurrency.common.capability.wallet.IWalletHandler;
import io.github.lightman314.lightmanscurrency.common.capability.wallet.WalletCapability;
import io.github.lightman314.lightmanscurrency.common.gamerule.ModGameRules;
import io.github.lightman314.lightmanscurrency.common.items.WalletItem;
import io.github.lightman314.lightmanscurrency.common.menus.wallet.WalletMenuBase;
import io.github.lightman314.lightmanscurrency.network.LightmansCurrencyPacketHandler;
import io.github.lightman314.lightmanscurrency.network.message.event.SPacketSyncEventUnlocks;
import io.github.lightman314.lightmanscurrency.network.message.wallet.SPacketPlayPickupSound;
import io.github.lightman314.lightmanscurrency.network.message.walletslot.SPacketSyncWallet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.SaplingGrowTreeEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber
public class EventHandler {

    @SubscribeEvent
    public static void pickupItem(EntityItemPickupEvent event) {
        ItemStack pickupItem = event.getItem().getItem();
        if (CoinAPI.API.IsCoin(pickupItem, false)) {
            Player player = event.getEntity();
            ItemStack coinStack = event.getItem().getItem();
            WalletMenuBase activeContainer = null;
            if (player.containerMenu instanceof WalletMenuBase container && container.isEquippedWallet()) {
                activeContainer = container;
            }
            boolean cancelEvent = false;
            ItemStack wallet = CoinAPI.API.getEquippedWallet(player);
            if (!wallet.isEmpty()) {
                WalletItem walletItem = (WalletItem) wallet.getItem();
                if (WalletItem.CanPickup(walletItem)) {
                    cancelEvent = true;
                    if (activeContainer != null) {
                        coinStack = activeContainer.PickupCoins(coinStack);
                    } else {
                        coinStack = WalletItem.PickupCoin(wallet, coinStack);
                    }
                }
            }
            if (event.isCancelable() && cancelEvent) {
                event.getItem().setItem(ItemStack.EMPTY);
                if (!coinStack.isEmpty()) {
                    ItemHandlerHelper.giveItemToPlayer(player, coinStack);
                }
                if (!player.m_9236_().isClientSide) {
                    SPacketPlayPickupSound.INSTANCE.sendTo(player);
                }
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!LCConfig.SERVER.anarchyMode.get()) {
            LevelAccessor level = event.getLevel();
            BlockState state = event.getState();
            if (state.m_60734_() instanceof IOwnableBlock block && !block.canBreak(event.getPlayer(), level, event.getPos(), state)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void blockBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (!LCConfig.SERVER.anarchyMode.get()) {
            Level level = event.getEntity().m_9236_();
            BlockState state = event.getState();
            if (event.getState().m_60734_() instanceof IOwnableBlock block) {
                event.getPosition().ifPresent(pos -> {
                    if (!block.canBreak(event.getEntity(), level, pos, state)) {
                        event.setCanceled(true);
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void attachEntitiesCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            event.addCapability(CurrencyCapabilities.ID_WALLET, WalletCapability.createProvider(player));
            event.addCapability(CurrencyCapabilities.ID_EVENT_TRACKER, CapabilityEventUnlocks.createProvider(player));
        }
    }

    @SubscribeEvent
    public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().m_9236_().isClientSide) {
            sendWalletUpdatePacket(event.getEntity());
            sendEventUpdatePacket(event.getEntity());
            if (event.getEntity() instanceof ServerPlayer player) {
                SyncedConfigFile.playerJoined(player);
            }
        }
    }

    @SubscribeEvent
    public static void playerStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        Player player = event.getEntity();
        sendWalletUpdatePacket(target, LightmansCurrencyPacketHandler.getTarget(player));
    }

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone event) {
        Player player = event.getEntity();
        if (!player.m_9236_().isClientSide) {
            Player oldPlayer = event.getOriginal();
            oldPlayer.revive();
            IWalletHandler oldHandler = WalletCapability.lazyGetWalletHandler(oldPlayer);
            IWalletHandler newHandler = WalletCapability.lazyGetWalletHandler(event.getEntity());
            if (oldHandler != null && newHandler != null) {
                newHandler.setWallet(oldHandler.getWallet());
                newHandler.setVisible(oldHandler.visible());
            }
            IEventUnlocks oldEventHandler = CapabilityEventUnlocks.getCapability(oldPlayer);
            IEventUnlocks newEventHandler = CapabilityEventUnlocks.getCapability(event.getEntity());
            if (oldEventHandler != null && newEventHandler != null) {
                newEventHandler.sync(oldEventHandler.getUnlockedList());
            }
            oldPlayer.invalidateCaps();
        }
    }

    @SubscribeEvent
    public static void playerChangedDimensions(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        if (!player.m_9236_().isClientSide) {
            sendWalletUpdatePacket(player);
            sendEventUpdatePacket(player);
        }
    }

    private static void sendWalletUpdatePacket(Entity entity, PacketDistributor.PacketTarget target) {
        if (!entity.level().isClientSide) {
            IWalletHandler walletHandler = WalletCapability.lazyGetWalletHandler(entity);
            if (walletHandler != null) {
                new SPacketSyncWallet(entity.getId(), walletHandler.getWallet(), walletHandler.visible()).sendToTarget(target);
            }
        }
    }

    private static void sendWalletUpdatePacket(Player player) {
        sendWalletUpdatePacket(player, LightmansCurrencyPacketHandler.getTarget(player));
    }

    private static void sendEventUpdatePacket(Player player) {
        if (!player.m_9236_().isClientSide) {
            IEventUnlocks eventUnlocks = CapabilityEventUnlocks.getCapability(player);
            if (eventUnlocks != null) {
                new SPacketSyncEventUnlocks(eventUnlocks.getUnlockedList()).sendTo(player);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void playerDrops(LivingDropsEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (!livingEntity.m_9236_().isClientSide) {
            if (!livingEntity.m_5833_()) {
                IWalletHandler walletHandler = WalletCapability.lazyGetWalletHandler(livingEntity);
                if (walletHandler != null) {
                    ItemStack walletStack = walletHandler.getWallet();
                    if (walletStack.isEmpty()) {
                        return;
                    }
                    List<ItemStack> drops = new ArrayList();
                    if (livingEntity instanceof Player player) {
                        boolean keepWallet = ModGameRules.safeGetCustomBool(player.m_9236_(), ModGameRules.KEEP_WALLET, false);
                        if (!LightmansCurrency.isCuriosValid(player) && player.m_9236_().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                            keepWallet = true;
                        }
                        int coinDropPercent = ModGameRules.safeGetCustomInt(player.m_9236_(), ModGameRules.COIN_DROP_PERCENT, 0);
                        SimpleContainer walletInventory = WalletItem.getWalletInventory(walletStack);
                        WalletDropEvent wde = new WalletDropEvent(player, walletHandler, walletInventory, event.getSource(), keepWallet, coinDropPercent);
                        if (MinecraftForge.EVENT_BUS.post(wde)) {
                            return;
                        }
                        drops = wde.getDrops();
                        walletHandler.setWallet(wde.getWalletStack());
                    } else {
                        drops.add(walletStack);
                        walletHandler.setWallet(ItemStack.EMPTY);
                    }
                    event.getDrops().addAll(turnIntoEntities(livingEntity, drops));
                }
            }
        }
    }

    private static List<ItemEntity> turnIntoEntities(@Nonnull LivingEntity entity, @Nonnull List<ItemStack> list) {
        List<ItemEntity> result = new ArrayList();
        for (ItemStack stack : list) {
            result.add(new ItemEntity(entity.m_9236_(), entity.m_20182_().x, entity.m_20182_().y, entity.m_20182_().z, stack));
        }
        return result;
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onWalletDrop(@Nonnull WalletDropEvent event) {
        if (event.keepWallet) {
            event.addDrops(getWalletDrops(event, event.coinDropPercent));
        } else if (!LightmansCurrency.isCuriosValid(event.getEntity())) {
            event.addDrop(event.getWalletStack());
            event.setWalletStack(ItemStack.EMPTY);
        }
    }

    private static List<ItemStack> getWalletDrops(@Nonnull WalletDropEvent event, int coinDropPercent) {
        if (coinDropPercent <= 0) {
            return new ArrayList();
        } else {
            List<ItemStack> drops = new ArrayList();
            Container walletInventory = event.getWalletInventory();
            IMoneyHandler walletHandler = MoneyAPI.API.GetContainersMoneyHandler(walletInventory, drops::add);
            MoneyView walletFunds = walletHandler.getStoredMoney();
            for (MoneyValue value : walletFunds.allValues()) {
                if (value instanceof CoinValue) {
                    MoneyValue takeAmount = value.percentageOfValue(coinDropPercent);
                    if (!takeAmount.isEmpty() && takeAmount instanceof CoinValue) {
                        CoinValue coinsToDrop = (CoinValue) takeAmount;
                        if (walletHandler.extractMoney(takeAmount, true).isEmpty()) {
                            walletHandler.extractMoney(takeAmount, false);
                            drops.addAll(coinsToDrop.getAsSeperatedItemList());
                        }
                    }
                }
            }
            return drops;
        }
    }

    @SubscribeEvent
    public static void entityTick(LivingEvent.LivingTickEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (!livingEntity.m_9236_().isClientSide) {
            IWalletHandler walletHandler = WalletCapability.lazyGetWalletHandler(livingEntity);
            if (walletHandler != null) {
                walletHandler.tick();
                if (walletHandler.isDirty()) {
                    new SPacketSyncWallet(livingEntity.m_19879_(), walletHandler.getWallet(), walletHandler.visible()).sendToTarget(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> livingEntity));
                    walletHandler.clean();
                }
            }
            if (livingEntity instanceof Player player) {
                IEventUnlocks eventHandler = CapabilityEventUnlocks.getCapability(player);
                if (eventHandler != null && eventHandler.isDirty()) {
                    sendEventUpdatePacket(player);
                    eventHandler.clean();
                }
            }
        }
    }

    @SubscribeEvent
    public static void serverStart(ServerStartedEvent event) {
        ConfigFile.loadServerFiles(ConfigFile.LoadPhase.GAME_START);
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event) {
        if (event.haveTime()) {
            for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
                DateTrigger.INSTANCE.trigger(player);
            }
        }
    }

    @SubscribeEvent
    public static void treeGrowEvent(SaplingGrowTreeEvent event) {
        if (((ConfiguredFeature) event.getFeature().get()).feature() instanceof AbstractHugeMushroomFeature) {
            LevelAccessor level = event.getLevel();
            BlockPos center = event.getPos();
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
            int radius = 3;
            int height = 13;
            for (int y = 0; y <= 13; y++) {
                for (int x = -3; x <= 3; x++) {
                    for (int z = -3; z <= 3; z++) {
                        pos.setWithOffset(center, x, y, z);
                        BlockState state = level.m_8055_(pos);
                        if (ForgeRegistries.BLOCKS.getKey(state.m_60734_()).getNamespace().equals("lightmanscurrency")) {
                            LightmansCurrency.LogDebug("LC block detected at " + pos.m_123344_() + " which is within the potential growth area of a Huge Mushroom attempting to grow at " + center.m_123344_() + ". Mushroom growth will be cancelled.");
                            event.setResult(Result.DENY);
                            return;
                        }
                    }
                }
            }
        }
    }
}