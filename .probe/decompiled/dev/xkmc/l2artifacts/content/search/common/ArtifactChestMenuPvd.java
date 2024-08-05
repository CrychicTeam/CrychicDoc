package dev.xkmc.l2artifacts.content.search.common;

import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;

public record ArtifactChestMenuPvd(ArtifactChestMenuPvd.Factory fac, ServerPlayer player, int slot, ItemStack stack) implements MenuProvider {

    @Override
    public Component getDisplayName() {
        return this.stack.getHoverName();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return this.fac.apply(id, inventory, ArtifactChestToken.of(player, this.slot));
    }

    public void writeBuffer(FriendlyByteBuf buf) {
        buf.writeInt(this.slot);
    }

    public void open() {
        NetworkHooks.openScreen(this.player, this, this::writeBuffer);
    }

    public interface Factory {

        AbstractContainerMenu apply(int var1, Inventory var2, ArtifactChestToken var3);
    }
}