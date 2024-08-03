package dev.architectury.registry.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.MenuProvider;

public interface ExtendedMenuProvider extends MenuProvider {

    void saveExtraData(FriendlyByteBuf var1);
}