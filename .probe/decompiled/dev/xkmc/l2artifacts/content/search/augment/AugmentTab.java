package dev.xkmc.l2artifacts.content.search.augment;

import dev.xkmc.l2artifacts.content.search.tabs.FilterTabBase;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabToken;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2artifacts.network.SetFilterToServer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class AugmentTab extends FilterTabBase<AugmentTab> {

    public AugmentTab(int index, FilterTabToken<AugmentTab> token, FilterTabManager manager, ItemStack stack, Component title) {
        super(index, token, manager, stack, title);
    }

    @Override
    public void onTabClicked() {
        NetworkManager.HANDLER.toServer(new SetFilterToServer(this.manager.token, SetFilterToServer.Type.AUGMENT));
    }
}