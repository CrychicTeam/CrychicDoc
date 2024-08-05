package net.mehvahdjukaar.supplementaries.common.block;

import java.util.UUID;
import net.mehvahdjukaar.moonlight.api.client.IScreenProvider;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface IOnePlayerGui extends IScreenProvider {

    void setPlayerWhoMayEdit(@Nullable UUID var1);

    UUID getPlayerWhoMayEdit();

    default void validatePlayerWhoMayEdit(Level level, BlockPos pos) {
        UUID uuid = this.getPlayerWhoMayEdit();
        if (uuid != null && this.playerIsTooFarAwayToEdit(level, pos, uuid)) {
            this.setPlayerWhoMayEdit(null);
        }
    }

    default boolean playerIsTooFarAwayToEdit(Level level, BlockPos pos, UUID uUID) {
        Player player = level.m_46003_(uUID);
        return player == null || player.m_20275_((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_()) > 64.0;
    }

    default boolean isOtherPlayerEditing(Player player) {
        UUID uuid = this.getPlayerWhoMayEdit();
        return uuid != null && !uuid.equals(player.m_20148_());
    }

    default boolean tryOpeningEditGui(ServerPlayer player, BlockPos pos) {
        if (Utils.mayBuild(player, pos) && !this.isOtherPlayerEditing(player)) {
            this.setPlayerWhoMayEdit(player.m_20148_());
            if (this.shouldUseContainerMenu() && this instanceof MenuProvider mp) {
                PlatHelper.openCustomMenu(player, mp, pos);
            } else {
                this.sendOpenGuiPacket(player.m_9236_(), pos, player);
            }
            return true;
        } else {
            return false;
        }
    }

    default boolean shouldUseContainerMenu() {
        return false;
    }
}