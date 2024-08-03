package com.mna.interop.lootr;

import java.util.Set;
import java.util.UUID;
import net.minecraft.resources.ResourceLocation;

public interface ILootrBridge {

    long getLootSeed();

    UUID getTileId();

    ResourceLocation getLootTable();

    void setLootrOpened(boolean var1);

    Set<UUID> getOpeners();

    void updatePacketViaState();
}