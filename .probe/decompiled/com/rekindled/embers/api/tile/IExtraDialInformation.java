package com.rekindled.embers.api.tile;

import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

public interface IExtraDialInformation {

    void addDialInformation(Direction var1, List<Component> var2, String var3);

    default int getComparatorData(Direction facing, int data, String dialType) {
        return data;
    }
}