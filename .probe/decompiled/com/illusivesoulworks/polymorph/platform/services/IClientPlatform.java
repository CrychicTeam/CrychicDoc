package com.illusivesoulworks.polymorph.platform.services;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public interface IClientPlatform {

    int getScreenTop(AbstractContainerScreen<?> var1);

    int getScreenLeft(AbstractContainerScreen<?> var1);
}