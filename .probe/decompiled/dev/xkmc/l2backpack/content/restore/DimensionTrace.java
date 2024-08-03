package dev.xkmc.l2backpack.content.restore;

import dev.xkmc.l2backpack.content.remote.common.StorageContainer;
import dev.xkmc.l2backpack.content.remote.common.WorldStorage;
import dev.xkmc.l2backpack.content.remote.worldchest.SimpleStorageMenuPvd;
import dev.xkmc.l2backpack.init.registrate.BackpackMenus;
import dev.xkmc.l2screentracker.screen.base.LayerPopType;
import dev.xkmc.l2screentracker.screen.track.TrackedEntryType;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.NetworkHooks;

public class DimensionTrace extends TrackedEntryType<DimensionTraceData> {

    public LayerPopType restoreMenuNotifyClient(ServerPlayer player, DimensionTraceData data, @Nullable Component comp) {
        if (comp == null) {
            comp = Component.translatable(BackpackMenus.getLangKey((MenuType<?>) BackpackMenus.MT_WORLD_CHEST.get()));
        }
        Optional<StorageContainer> op = WorldStorage.get((ServerLevel) player.m_9236_()).getStorageWithoutPassword(data.uuid(), data.color());
        if (op.isPresent()) {
            NetworkHooks.openScreen(player, new SimpleStorageMenuPvd(comp, (StorageContainer) op.get()));
            return LayerPopType.REMAIN;
        } else {
            return LayerPopType.FAIL;
        }
    }
}