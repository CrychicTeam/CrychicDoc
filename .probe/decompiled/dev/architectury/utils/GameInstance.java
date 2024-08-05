package dev.architectury.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.architectury.utils.forge.GameInstanceImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public final class GameInstance {

    @OnlyIn(Dist.CLIENT)
    public static Minecraft getClient() {
        return Minecraft.getInstance();
    }

    @Nullable
    @ExpectPlatform
    @Transformed
    public static MinecraftServer getServer() {
        return GameInstanceImpl.getServer();
    }
}