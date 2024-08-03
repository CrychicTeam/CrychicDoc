package com.mna.gui.containers.providers;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.gui.containers.item.ContainerGrimoire;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.common.util.LazyOptional;

public class NamedGrimoire implements MenuProvider {

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        LazyOptional<IPlayerMagic> magicContainer = player.getCapability(PlayerMagicProvider.MAGIC);
        return magicContainer.isPresent() ? new ContainerGrimoire(i, inventory, magicContainer.orElse(null).getGrimoireInventory()) : new ContainerGrimoire(i, inventory, (FriendlyByteBuf) null);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("");
    }
}