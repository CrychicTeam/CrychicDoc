package net.blay09.mods.waystones.network.message;

import java.util.UUID;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.Waystone;
import net.blay09.mods.waystones.core.WaystoneEditPermissions;
import net.blay09.mods.waystones.core.WaystoneProxy;
import net.blay09.mods.waystones.menu.ModMenus;
import net.blay09.mods.waystones.menu.WaystoneSettingsMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class RequestEditWaystoneMessage {

    private final UUID waystoneUid;

    public RequestEditWaystoneMessage(UUID waystoneUid) {
        this.waystoneUid = waystoneUid;
    }

    public static void encode(RequestEditWaystoneMessage message, FriendlyByteBuf buf) {
        buf.writeUUID(message.waystoneUid);
    }

    public static RequestEditWaystoneMessage decode(FriendlyByteBuf buf) {
        UUID waystoneUid = buf.readUUID();
        return new RequestEditWaystoneMessage(waystoneUid);
    }

    public static void handle(ServerPlayer player, RequestEditWaystoneMessage message) {
        final WaystoneProxy waystone = new WaystoneProxy(player.server, message.waystoneUid);
        WaystoneEditPermissions permissions = PlayerWaystoneManager.mayEditWaystone(player, player.m_9236_(), waystone);
        if (permissions == WaystoneEditPermissions.ALLOW) {
            BlockPos pos = waystone.getPos();
            if (!(player.m_20275_((double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 0.5F), (double) ((float) pos.m_123343_() + 0.5F)) > 64.0)) {
                BalmMenuProvider containerProvider = new BalmMenuProvider() {

                    @Override
                    public Component getDisplayName() {
                        return Component.translatable("container.waystones.waystone_settings");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
                        return new WaystoneSettingsMenu(ModMenus.waystoneSettings.get(), waystone, i);
                    }

                    @Override
                    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                        Waystone.write(buf, waystone);
                    }
                };
                Balm.getNetworking().openGui(player, containerProvider);
            }
        }
    }
}