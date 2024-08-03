package com.mna.gui.containers.providers;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.gui.containers.item.ContainerRoteBook;
import com.mna.items.sorcery.ItemBookOfRote;
import com.mna.items.sorcery.ItemSpellBook;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

public class NamedRoteBook implements MenuProvider {

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        ItemStack curStack = player.m_21205_();
        int curIndex = 0;
        if (curStack.getItem() instanceof ItemBookOfRote) {
            curIndex = ItemSpellBook.getActiveSpellSlot(curStack);
        }
        LazyOptional<IPlayerMagic> magicContainer = player.getCapability(PlayerMagicProvider.MAGIC);
        return magicContainer.isPresent() ? new ContainerRoteBook(i, inventory, magicContainer.orElse(null).getRoteInventory(), curIndex) : new ContainerRoteBook(i, inventory, null);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("");
    }
}