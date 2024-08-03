package com.frikinjay.lmd.mixin;

import com.frikinjay.lmd.command.LetMeDespawnCommands;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ MinecraftServer.class })
public class MinecraftServerMixin {

    @Inject(method = { "loadLevel" }, at = { @At("HEAD") })
    private void onLoadLevel(CallbackInfo ci) {
        CommandDispatcher<CommandSourceStack> dispatcher = ((MinecraftServer) this).getCommands().getDispatcher();
        LetMeDespawnCommands.register(dispatcher);
    }
}