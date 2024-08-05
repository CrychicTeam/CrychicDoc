package dev.latvian.mods.kubejs.core;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import dev.latvian.mods.kubejs.gui.KubeJSGUI;
import dev.latvian.mods.kubejs.gui.KubeJSMenu;
import dev.latvian.mods.kubejs.gui.chest.ChestMenuData;
import dev.latvian.mods.kubejs.gui.chest.CustomChestMenu;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.net.NotificationMessage;
import dev.latvian.mods.kubejs.net.PaintMessage;
import dev.latvian.mods.kubejs.net.SendDataFromServerMessage;
import dev.latvian.mods.kubejs.player.AdvancementJS;
import dev.latvian.mods.kubejs.player.PlayerStatsJS;
import dev.latvian.mods.kubejs.util.NotificationBuilder;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Consumer;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import org.jetbrains.annotations.Nullable;

@RemapPrefixForJS("kjs$")
public interface ServerPlayerKJS extends PlayerKJS {

    default ServerPlayer kjs$self() {
        return (ServerPlayer) this;
    }

    @Override
    default void kjs$sendData(String channel, @Nullable CompoundTag data) {
        if (!channel.isEmpty()) {
            new SendDataFromServerMessage(channel, data).sendTo(this.kjs$self());
        }
    }

    @Override
    default void kjs$paint(CompoundTag renderer) {
        new PaintMessage(renderer).sendTo(this.kjs$self());
    }

    @Override
    default PlayerStatsJS kjs$getStats() {
        return new PlayerStatsJS(this.kjs$self(), this.kjs$self().getStats());
    }

    @Override
    default boolean kjs$isMiningBlock() {
        return this.kjs$self().gameMode.isDestroyingBlock;
    }

    @Override
    default void kjs$setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
        PlayerKJS.super.kjs$setPositionAndRotation(x, y, z, yaw, pitch);
        this.kjs$self().connection.teleport(x, y, z, yaw, pitch);
    }

    default void kjs$setCreativeMode(boolean mode) {
        this.kjs$self().setGameMode(mode ? GameType.CREATIVE : GameType.SURVIVAL);
    }

    default boolean kjs$isOp() {
        return this.kjs$self().server.getPlayerList().isOp(this.kjs$self().m_36316_());
    }

    default void kjs$kick(Component reason) {
        this.kjs$self().connection.disconnect(reason);
    }

    default void kjs$kick() {
        this.kjs$kick(Component.translatable("multiplayer.disconnect.kicked"));
    }

    default void kjs$ban(String banner, String reason, long expiresInMillis) {
        Date date = new Date();
        UserBanListEntry userlistbansentry = new UserBanListEntry(this.kjs$self().m_36316_(), date, banner, new Date(date.getTime() + (expiresInMillis <= 0L ? 315569260000L : expiresInMillis)), reason);
        this.kjs$self().server.getPlayerList().getBans().m_11381_(userlistbansentry);
        this.kjs$kick(Component.translatable("multiplayer.disconnect.banned"));
    }

    default boolean kjs$isAdvancementDone(ResourceLocation id) {
        AdvancementJS a = this.kjs$self().server.kjs$getAdvancement(id);
        return a != null && this.kjs$self().getAdvancements().getOrStartProgress(a.advancement).isDone();
    }

    default void kjs$unlockAdvancement(ResourceLocation id) {
        AdvancementJS a = this.kjs$self().server.kjs$getAdvancement(id);
        if (a != null) {
            AdvancementProgress advancementprogress = this.kjs$self().getAdvancements().getOrStartProgress(a.advancement);
            for (String s : advancementprogress.getRemainingCriteria()) {
                this.kjs$self().getAdvancements().award(a.advancement, s);
            }
        }
    }

    default void kjs$revokeAdvancement(ResourceLocation id) {
        AdvancementJS a = this.kjs$self().server.kjs$getAdvancement(id);
        if (a != null) {
            AdvancementProgress advancementprogress = this.kjs$self().getAdvancements().getOrStartProgress(a.advancement);
            if (advancementprogress.hasProgress()) {
                for (String s : advancementprogress.getCompletedCriteria()) {
                    this.kjs$self().getAdvancements().revoke(a.advancement, s);
                }
            }
        }
    }

    @Override
    default void kjs$setSelectedSlot(int index) {
        int p = this.kjs$getSelectedSlot();
        PlayerKJS.super.kjs$setSelectedSlot(index);
        int n = this.kjs$getSelectedSlot();
        if (p != n && this.kjs$self().connection != null) {
            this.kjs$self().connection.send(new ClientboundSetCarriedItemPacket(n));
        }
    }

    @Override
    default void kjs$setMouseItem(ItemStack item) {
        PlayerKJS.super.kjs$setMouseItem(item);
        if (this.kjs$self().connection != null) {
            this.kjs$self().f_36095_.m_38946_();
        }
    }

    @Nullable
    default BlockContainerJS kjs$getSpawnLocation() {
        BlockPos pos = this.kjs$self().getRespawnPosition();
        return pos == null ? null : new BlockContainerJS(this.kjs$getLevel(), pos);
    }

    default void kjs$setSpawnLocation(BlockContainerJS c) {
        this.kjs$self().setRespawnPosition(c.minecraftLevel.dimension(), c.getPos(), 0.0F, true, false);
    }

    @Override
    default void kjs$notify(NotificationBuilder builder) {
        new NotificationMessage(builder).sendTo(this.kjs$self());
    }

    default void kjs$openGUI(Consumer<KubeJSGUI> gui) {
        final KubeJSGUI data = new KubeJSGUI();
        gui.accept(data);
        MenuRegistry.openExtendedMenu(this.kjs$self(), new ExtendedMenuProvider() {

            @Override
            public void saveExtraData(FriendlyByteBuf buf) {
                data.write(buf);
            }

            @Override
            public Component getDisplayName() {
                return data.title;
            }

            @Override
            public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return new KubeJSMenu(i, inventory, data);
            }
        });
    }

    default void kjs$openInventoryGUI(InventoryKJS inventory, Component title) {
        this.kjs$openGUI(gui -> {
            gui.title = title;
            gui.setInventory(inventory);
        });
    }

    default Container kjs$captureInventory(boolean autoRestore) {
        NonNullList<ItemStack> playerItems = this.kjs$self().m_150109_().items;
        SimpleContainer captured = new SimpleContainer(playerItems.size());
        HashMap<Integer, ItemStack> map = new HashMap();
        for (int i = 0; i < playerItems.size(); i++) {
            ItemStack c = playerItems.set(i, ItemStack.EMPTY);
            if (!c.isEmpty()) {
                if (autoRestore) {
                    map.put(i, c);
                }
                captured.setItem(i, c.copy());
            }
        }
        if (autoRestore && !map.isEmpty()) {
            this.kjs$self().m_20194_().kjs$restoreInventories().put(this.kjs$self().m_20148_(), map);
        }
        return captured;
    }

    default void kjs$openChestGUI(Component title, int rows, Consumer<ChestMenuData> gui) {
        final ChestMenuData data = new ChestMenuData(this.kjs$self(), title, Mth.clamp(rows, 1, 6));
        gui.accept(data);
        if (this.kjs$self().f_36096_ instanceof CustomChestMenu open) {
            data.capturedInventory = open.data.capturedInventory;
        } else {
            data.capturedInventory = this.kjs$captureInventory(true);
        }
        if (this.kjs$self().f_36096_ instanceof CustomChestMenu open && open.data.rows == data.rows && open.data.title.equals(title)) {
            open.data = data;
            data.sync();
            return;
        }
        data.sync();
        this.kjs$self().openMenu(new MenuProvider() {

            @Override
            public Component getDisplayName() {
                return title;
            }

            @Override
            public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return new CustomChestMenu(i, data);
            }
        });
    }
}