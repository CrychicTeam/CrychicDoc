package dev.xkmc.l2library.util;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.server.ServerLifecycleHooks;

public class Proxy {

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public static LocalPlayer getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    @Deprecated
    @Nullable
    public static Player getPlayer() {
        return (Player) DistExecutor.unsafeRunForDist(() -> Proxy::getClientPlayer, () -> () -> null);
    }

    @Deprecated
    @Nullable
    public static Level getWorld() {
        return (Level) DistExecutor.unsafeRunForDist(() -> Proxy::getClientWorld, () -> () -> (Level) getServer().map(MinecraftServer::m_129783_).orElse(null));
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public static ClientLevel getClientWorld() {
        return Minecraft.getInstance().level;
    }

    public static Optional<MinecraftServer> getServer() {
        return Optional.ofNullable(ServerLifecycleHooks.getCurrentServer());
    }
}