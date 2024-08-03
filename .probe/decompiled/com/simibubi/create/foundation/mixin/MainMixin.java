package com.simibubi.create.foundation.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestRegistry;
import net.minecraft.gametest.framework.GameTestRunner;
import net.minecraft.gametest.framework.GameTestServer;
import net.minecraft.server.Main;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({ Main.class })
public class MainMixin {

    @ModifyVariable(method = { "lambda$main$5" }, at = @At(value = "STORE", ordinal = 0), require = 0)
    private static MinecraftServer create$correctlyInitializeGametestServer(MinecraftServer original) {
        return (MinecraftServer) (original instanceof GameTestServer && !Boolean.getBoolean("create.useOriginalGametestServer") ? GameTestServer.create(original.getRunningThread(), original.storageSource, original.getPackRepository(), GameTestRunner.groupTestsIntoBatches(GameTestRegistry.getAllTestFunctions()), BlockPos.ZERO) : original);
    }
}