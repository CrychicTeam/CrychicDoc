package com.mrcrayfish.catalogue.platform.services;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public interface IComponentHelper {

    MutableComponent createTitle();

    MutableComponent createVersion(String var1);

    MutableComponent createFormatted(String var1, String var2);

    Component createFilterUpdates();

    String getCreditsKey();
}