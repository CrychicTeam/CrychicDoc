package dev.xkmc.l2backpack.events;

import dev.xkmc.l2backpack.content.quickswap.common.IQuickSwapToken;
import dev.xkmc.l2backpack.content.quickswap.common.QuickSwapOverlay;
import dev.xkmc.l2backpack.content.quickswap.type.QuickSwapManager;
import dev.xkmc.l2backpack.init.data.BackpackConfig;
import dev.xkmc.l2itemselector.init.data.L2Keys;
import dev.xkmc.l2itemselector.select.ISelectionListener;
import dev.xkmc.l2itemselector.select.SetSelectedToServer;
import java.util.function.BooleanSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class BackpackSel implements ISelectionListener {

    public static final BackpackSel INSTANCE = new BackpackSel();

    public static final int UP = -1;

    public static final int DOWN = -2;

    public static final int SWAP = -3;

    private static final ResourceLocation ID = new ResourceLocation("l2backpack", "backpack");

    public ResourceLocation getID() {
        return ID;
    }

    public boolean isClientActive(Player player) {
        if (Minecraft.getInstance().screen != null) {
            return false;
        } else {
            IQuickSwapToken<?> token = QuickSwapManager.getToken(player, QuickSwapOverlay.hasAltDown());
            return token != null;
        }
    }

    public void handleServerSetSelection(SetSelectedToServer packet, Player player) {
        IQuickSwapToken<?> token = QuickSwapManager.getToken(player, packet.isAltDown);
        if (token != null) {
            if (packet.slot == -3) {
                token.swap(player);
            } else {
                token.setSelected(packet.slot);
            }
        }
    }

    public boolean handleClientScroll(int i, Player player) {
        if (BackpackConfig.CLIENT.reverseScroll.get()) {
            i = -i;
        }
        if (i > 0) {
            this.toServer(-1);
        } else if (i < 0) {
            this.toServer(-2);
        }
        return true;
    }

    public void handleClientKey(L2Keys key, Player player) {
        if (QuickSwapOverlay.INSTANCE.isScreenOn()) {
            if (key == L2Keys.SWAP) {
                this.toServer(-3);
            } else if (key == L2Keys.UP) {
                this.toServer(-1);
            } else if (key == L2Keys.DOWN) {
                this.toServer(-2);
            }
        }
    }

    public boolean handleClientNumericKey(int i, BooleanSupplier click) {
        if (!QuickSwapOverlay.hasShiftDown()) {
            return false;
        } else if (click.getAsBoolean()) {
            this.toServer(i);
            return true;
        } else {
            return false;
        }
    }
}