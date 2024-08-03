package noobanidus.mods.lootr.util;

import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraftforge.network.PacketDistributor;
import noobanidus.mods.lootr.advancement.GenericTrigger;
import noobanidus.mods.lootr.api.IHasOpeners;
import noobanidus.mods.lootr.api.blockentity.ILootBlockEntity;
import noobanidus.mods.lootr.block.LootrShulkerBlock;
import noobanidus.mods.lootr.block.entities.LootrInventoryBlockEntity;
import noobanidus.mods.lootr.config.ConfigManager;
import noobanidus.mods.lootr.data.DataStorage;
import noobanidus.mods.lootr.entity.LootrChestMinecartEntity;
import noobanidus.mods.lootr.init.ModAdvancements;
import noobanidus.mods.lootr.init.ModStats;
import noobanidus.mods.lootr.network.CloseCart;
import noobanidus.mods.lootr.network.PacketHandler;
import noobanidus.mods.lootr.network.UpdateModelData;

public class ChestUtil {

    public static void handleLootSneak(Block block, Level level, BlockPos pos, Player player) {
        if (!level.isClientSide() && !player.isSpectator()) {
            BlockEntity te = level.getBlockEntity(pos);
            if (te instanceof ILootBlockEntity tile && tile.getOpeners().remove(player.m_20148_())) {
                te.setChanged();
                tile.updatePacketViaForce(te);
                UpdateModelData packet = new UpdateModelData(pos);
                PacketHandler.sendToInternal(packet, (ServerPlayer) player);
            }
        }
    }

    public static void handleLootCartSneak(Level level, LootrChestMinecartEntity cart, Player player) {
        if (!level.isClientSide() && !player.isSpectator()) {
            cart.getOpeners().remove(player.m_20148_());
            CloseCart open = new CloseCart(cart.m_19879_());
            PacketHandler.sendInternal(PacketDistributor.TRACKING_ENTITY.with(() -> cart), open);
        }
    }

    public static Style getInvalidStyle() {
        return ConfigManager.DISABLE_MESSAGE_STYLES.get() ? Style.EMPTY : Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.RED)).withBold(true);
    }

    public static Style getDecayStyle() {
        return ConfigManager.DISABLE_MESSAGE_STYLES.get() ? Style.EMPTY : Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.RED)).withBold(true);
    }

    public static Style getRefreshStyle() {
        return ConfigManager.DISABLE_MESSAGE_STYLES.get() ? Style.EMPTY : Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.BLUE)).withBold(true);
    }

    public static Component getInvalidTable(ResourceLocation lootTable) {
        return Component.translatable("lootr.message.invalid_table", lootTable.getNamespace(), lootTable.toString()).setStyle(ConfigManager.DISABLE_MESSAGE_STYLES.get() ? Style.EMPTY : Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.DARK_RED)).withBold(true));
    }

    public static void handleLootChest(Block block, Level level, BlockPos pos, Player player) {
        if (!level.isClientSide() && !player.isSpectator()) {
            BlockEntity te = level.getBlockEntity(pos);
            if (te instanceof ILootBlockEntity tile) {
                UUID tileId = tile.getTileId();
                if (tileId == null) {
                    player.displayClientMessage(Component.translatable("lootr.message.invalid_block").setStyle(getInvalidStyle()), true);
                    return;
                }
                if (te instanceof BaseContainerBlockEntity bce && !bce.canOpen(player)) {
                    return;
                }
                if (DataStorage.isDecayed(tileId)) {
                    level.m_46961_(pos, true);
                    notifyDecay(player, tileId);
                    return;
                }
                int decayValue = DataStorage.getDecayValue(tileId);
                if (decayValue > 0 && ConfigManager.shouldNotify(decayValue)) {
                    player.displayClientMessage(Component.translatable("lootr.message.decay_in", decayValue / 20).setStyle(getDecayStyle()), true);
                } else if (decayValue == -1 && ConfigManager.isDecaying((ServerLevel) level, tile)) {
                    startDecay(player, tileId, decayValue);
                }
                GenericTrigger<UUID> trigger = ModAdvancements.CHEST_PREDICATE;
                if (block instanceof BarrelBlock) {
                    trigger = ModAdvancements.BARREL_PREDICATE;
                } else if (block instanceof LootrShulkerBlock) {
                    trigger = ModAdvancements.SHULKER_PREDICATE;
                }
                trigger.trigger((ServerPlayer) player, tileId);
                if (DataStorage.isRefreshed(tileId)) {
                    DataStorage.refreshInventory(level, pos, tileId, (ServerPlayer) player);
                    notifyRefresh(player, tileId);
                }
                int refreshValue = DataStorage.getRefreshValue(tileId);
                if (refreshValue > 0 && ConfigManager.shouldNotify(refreshValue)) {
                    player.displayClientMessage(Component.translatable("lootr.message.refresh_in", refreshValue / 20).setStyle(getRefreshStyle()), true);
                } else if (refreshValue == -1 && ConfigManager.isRefreshing((ServerLevel) level, tile)) {
                    startRefresh(player, tileId, refreshValue);
                }
                MenuProvider provider = DataStorage.getInventory(level, tileId, pos, (ServerPlayer) player, (RandomizableContainerBlockEntity) te, tile::unpackLootTable);
                if (provider == null) {
                    return;
                }
                checkScore((ServerPlayer) player, tileId);
                if (addOpener(tile, player)) {
                    te.setChanged();
                    tile.updatePacketViaState();
                }
                player.openMenu(provider);
                PiglinAi.angerNearbyPiglins(player, true);
            }
        } else {
            if (player.isSpectator()) {
                player.openMenu(null);
            }
        }
    }

    private static boolean addOpener(IHasOpeners openable, Player player) {
        return openable.getOpeners().add(player.m_20148_());
    }

    public static void handleLootCart(Level level, LootrChestMinecartEntity cart, Player player) {
        if (!level.isClientSide() && !player.isSpectator()) {
            ModAdvancements.CART_PREDICATE.trigger((ServerPlayer) player, cart.m_20148_());
            UUID tileId = cart.m_20148_();
            if (DataStorage.isDecayed(tileId)) {
                cart.destroy(cart.m_269291_().fellOutOfWorld());
                notifyDecay(player, tileId);
            } else {
                int decayValue = DataStorage.getDecayValue(tileId);
                if (decayValue > 0 && ConfigManager.shouldNotify(decayValue)) {
                    player.displayClientMessage(Component.translatable("lootr.message.decay_in", decayValue / 20).setStyle(getDecayStyle()), true);
                } else if (decayValue == -1 && ConfigManager.isDecaying((ServerLevel) level, cart)) {
                    startDecay(player, tileId, decayValue);
                }
                addOpener(cart, player);
                checkScore((ServerPlayer) player, cart.m_20148_());
                if (DataStorage.isRefreshed(tileId)) {
                    DataStorage.refreshInventory(level, cart, (ServerPlayer) player);
                    notifyRefresh(player, tileId);
                }
                decayValue = DataStorage.getRefreshValue(tileId);
                if (decayValue > 0 && ConfigManager.shouldNotify(decayValue)) {
                    player.displayClientMessage(Component.translatable("lootr.message.refresh_in", decayValue / 20).setStyle(getRefreshStyle()), true);
                } else if (decayValue == -1 && ConfigManager.isRefreshing((ServerLevel) level, cart)) {
                    startRefresh(player, tileId, decayValue);
                }
                MenuProvider provider = DataStorage.getInventory(level, cart, (ServerPlayer) player, cart::addLoot);
                if (provider != null) {
                    player.openMenu(provider);
                }
            }
        } else {
            if (player.isSpectator()) {
                player.openMenu(null);
            }
        }
    }

    public static void handleLootInventory(Block block, Level level, BlockPos pos, Player player) {
        if (!level.isClientSide() && !player.isSpectator()) {
            BlockEntity te = level.getBlockEntity(pos);
            if (te instanceof LootrInventoryBlockEntity tile) {
                ModAdvancements.CHEST_PREDICATE.trigger((ServerPlayer) player, tile.getTileId());
                NonNullList<ItemStack> stacks = null;
                if (tile.getCustomInventory() != null) {
                    stacks = copyItemList(tile.getCustomInventory());
                }
                UUID tileId = tile.getTileId();
                if (DataStorage.isRefreshed(tileId)) {
                    DataStorage.refreshInventory(level, pos, tile.getTileId(), stacks, (ServerPlayer) player);
                    notifyRefresh(player, tileId);
                }
                int refreshValue = DataStorage.getRefreshValue(tileId);
                if (refreshValue > 0 && ConfigManager.shouldNotify(refreshValue)) {
                    player.displayClientMessage(Component.translatable("lootr.message.refresh_in", refreshValue / 20).setStyle(getRefreshStyle()), true);
                } else if (refreshValue == -1 && ConfigManager.isRefreshing((ServerLevel) level, tile)) {
                    startRefresh(player, tileId, refreshValue);
                }
                MenuProvider provider = DataStorage.getInventory(level, tile.getTileId(), stacks, (ServerPlayer) player, pos, tile);
                if (provider == null) {
                    return;
                }
                checkScore((ServerPlayer) player, tile.getTileId());
                if (addOpener(tile, player)) {
                    te.setChanged();
                    tile.updatePacketViaState();
                }
                player.openMenu(provider);
                PiglinAi.angerNearbyPiglins(player, true);
            }
        } else {
            if (player.isSpectator()) {
                player.openMenu(null);
            }
        }
    }

    public static NonNullList<ItemStack> copyItemList(NonNullList<ItemStack> reference) {
        NonNullList<ItemStack> contents = NonNullList.withSize(reference.size(), ItemStack.EMPTY);
        for (int i = 0; i < reference.size(); i++) {
            contents.set(i, reference.get(i).copy());
        }
        return contents;
    }

    private static void checkScore(ServerPlayer player, UUID tileId) {
        if (!DataStorage.isScored(player.m_20148_(), tileId)) {
            player.m_36246_(ModStats.LOOTED_STAT);
            ModAdvancements.SCORE_PREDICATE.trigger(player, null);
            DataStorage.score(player.m_20148_(), tileId);
        }
    }

    private static void notifyDecay(Player player, UUID tileId) {
        player.displayClientMessage(Component.translatable("lootr.message.decayed").setStyle(getDecayStyle()), true);
        DataStorage.removeDecayed(tileId);
    }

    private static void notifyRefresh(Player player, UUID tileId) {
        DataStorage.removeRefreshed(tileId);
        player.displayClientMessage(Component.translatable("lootr.message.refreshed").setStyle(getRefreshStyle()), true);
    }

    private static void startDecay(Player player, UUID tileId, int decayValue) {
        DataStorage.setDecaying(tileId, ConfigManager.DECAY_VALUE.get());
        player.displayClientMessage(Component.translatable("lootr.message.decay_start", ConfigManager.DECAY_VALUE.get() / 20).setStyle(getDecayStyle()), true);
    }

    private static void startRefresh(Player player, UUID tileId, int refreshValue) {
        DataStorage.setRefreshing(tileId, ConfigManager.REFRESH_VALUE.get());
        player.displayClientMessage(Component.translatable("lootr.message.refresh_start", ConfigManager.REFRESH_VALUE.get() / 20).setStyle(getRefreshStyle()), true);
    }
}