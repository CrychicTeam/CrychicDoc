package dev.xkmc.l2artifacts.events;

import dev.xkmc.l2artifacts.content.swap.ArtifactSwapData;
import dev.xkmc.l2artifacts.content.swap.ArtifactSwapItem;
import dev.xkmc.l2itemselector.init.data.L2Keys;
import dev.xkmc.l2itemselector.select.ISelectionListener;
import dev.xkmc.l2itemselector.select.SetSelectedToServer;
import it.unimi.dsi.fastutil.Pair;
import java.util.function.BooleanSupplier;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ArtifactSel implements ISelectionListener {

    public static final ArtifactSel INSTANCE = new ArtifactSel();

    public static final int UP = -1;

    public static final int DOWN = -2;

    public static final int SWAP = -3;

    private static final ResourceLocation ID = new ResourceLocation("l2artifacts", "swap");

    public ResourceLocation getID() {
        return ID;
    }

    public boolean isClientActive(Player player) {
        return Minecraft.getInstance().screen != null ? false : getData(player) != null;
    }

    public void handleServerSetSelection(SetSelectedToServer packet, Player player) {
        Pair<ItemStack, ArtifactSwapData> token = getData(player);
        if (token != null) {
            if (packet.slot == -3) {
                ((ArtifactSwapData) token.second()).swap(player);
                ArtifactSwapItem.setData((ItemStack) token.first(), (ArtifactSwapData) token.second());
            } else {
                int s = packet.slot;
                if (packet.slot < 0) {
                    s = ((ArtifactSwapData) token.second()).select;
                    if (packet.slot == -1) {
                        s--;
                    } else {
                        s++;
                    }
                    s = (s + 9) % 9;
                }
                ((ArtifactSwapData) token.second()).select = s;
                ArtifactSwapItem.setData((ItemStack) token.first(), (ArtifactSwapData) token.second());
            }
        }
    }

    public boolean handleClientScroll(int i, Player player) {
        if (i > 0) {
            this.toServer(-1);
        } else if (i < 0) {
            this.toServer(-2);
        }
        return true;
    }

    public void handleClientKey(L2Keys key, Player player) {
        if (key == L2Keys.SWAP) {
            this.toServer(-3);
        } else if (key == L2Keys.UP) {
            this.toServer(-1);
        } else if (key == L2Keys.DOWN) {
            this.toServer(-2);
        }
    }

    public boolean handleClientNumericKey(int i, BooleanSupplier click) {
        if (!Minecraft.getInstance().options.keyShift.isDown()) {
            return false;
        } else if (click.getAsBoolean()) {
            this.toServer(i);
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    public static Pair<ItemStack, ArtifactSwapData> getData(Player player) {
        ItemStack main = player.m_21205_();
        if (main.getItem() instanceof ArtifactSwapItem) {
            return Pair.of(main, ArtifactSwapItem.getData(main));
        } else {
            ItemStack off = player.m_21206_();
            return off.getItem() instanceof ArtifactSwapItem ? Pair.of(off, ArtifactSwapItem.getData(off)) : null;
        }
    }
}