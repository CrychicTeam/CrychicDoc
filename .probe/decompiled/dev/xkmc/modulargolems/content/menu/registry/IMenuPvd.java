package dev.xkmc.modulargolems.content.menu.registry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.MenuProvider;

public interface IMenuPvd extends MenuProvider {

    void writeBuffer(FriendlyByteBuf var1);
}