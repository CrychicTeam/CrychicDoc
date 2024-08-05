package me.jellysquid.mods.sodium.mixin.core;

import com.mojang.realmsclient.client.RealmsClient;
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import java.util.concurrent.CompletableFuture;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.compatibility.checks.ResourcePackScanner;
import me.jellysquid.mods.sodium.client.gui.screen.ConfigCorruptedScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.main.GameConfig;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.lwjgl.opengl.GL32C;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ Minecraft.class })
public class MinecraftClientMixin {

    @Shadow
    @Final
    private ReloadableResourceManager resourceManager;

    @Unique
    private final LongArrayFIFOQueue fences = new LongArrayFIFOQueue();

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void postInit(GameConfig args, CallbackInfo ci) {
        if (SodiumClientMod.options().isReadOnly()) {
            Screen parent = Minecraft.getInstance().screen;
            Minecraft.getInstance().setScreen(new ConfigCorruptedScreen(() -> parent));
        }
    }

    @Inject(method = { "runTick" }, at = { @At("HEAD") })
    private void preRender(boolean tick, CallbackInfo ci) {
        ProfilerFiller profiler = Minecraft.getInstance().getProfiler();
        profiler.push("wait_for_gpu");
        while (this.fences.size() > SodiumClientMod.options().advanced.cpuRenderAheadLimit) {
            long fence = this.fences.dequeueLong();
            GL32C.glClientWaitSync(fence, 1, Long.MAX_VALUE);
            GL32C.glDeleteSync(fence);
        }
        profiler.pop();
    }

    @Inject(method = { "runTick" }, at = { @At("RETURN") })
    private void postRender(boolean tick, CallbackInfo ci) {
        long fence = GL32C.glFenceSync(37143, 0);
        if (fence == 0L) {
            throw new RuntimeException("Failed to create fence object");
        } else {
            this.fences.enqueue(fence);
        }
    }

    @Inject(method = { "setInitialScreen" }, at = { @At("TAIL") })
    private void postInit(RealmsClient realms, ReloadInstance reload, GameConfig.QuickPlayData quickPlay, CallbackInfo ci) {
        ResourcePackScanner.checkIfCoreShaderLoaded(this.resourceManager);
    }

    @Inject(method = { "reloadResourcePacks()Ljava/util/concurrent/CompletableFuture;" }, at = { @At("TAIL") })
    private void postResourceReload(CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        ResourcePackScanner.checkIfCoreShaderLoaded(this.resourceManager);
    }
}