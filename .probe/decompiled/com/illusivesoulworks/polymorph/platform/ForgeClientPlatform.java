package com.illusivesoulworks.polymorph.platform;

import com.illusivesoulworks.polymorph.platform.services.IClientPlatform;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public class ForgeClientPlatform implements IClientPlatform {

    @Override
    public int getScreenTop(AbstractContainerScreen<?> screen) {
        return screen.getGuiTop();
    }

    @Override
    public int getScreenLeft(AbstractContainerScreen<?> screen) {
        return screen.getGuiLeft();
    }
}