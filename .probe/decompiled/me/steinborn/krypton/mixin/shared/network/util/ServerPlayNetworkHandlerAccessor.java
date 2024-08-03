package me.steinborn.krypton.mixin.shared.network.util;

import net.minecraft.network.Connection;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ ServerGamePacketListenerImpl.class })
public interface ServerPlayNetworkHandlerAccessor {

    @Accessor
    Connection getConnection();
}