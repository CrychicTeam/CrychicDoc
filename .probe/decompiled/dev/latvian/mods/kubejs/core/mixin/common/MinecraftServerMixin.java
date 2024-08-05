package dev.latvian.mods.kubejs.core.mixin.common;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.bindings.event.ServerEvents;
import dev.latvian.mods.kubejs.core.MinecraftServerKJS;
import dev.latvian.mods.kubejs.gui.chest.CustomChestMenu;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.server.ScheduledServerEvent;
import dev.latvian.mods.kubejs.server.ServerEventJS;
import dev.latvian.mods.kubejs.util.AttachedData;
import dev.latvian.mods.kubejs.util.KubeJSPlugins;
import dev.latvian.mods.kubejs.util.ScheduledEvents;
import dev.latvian.mods.rhino.util.RemapForJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@RemapPrefixForJS("kjs$")
@Mixin({ MinecraftServer.class })
public abstract class MinecraftServerMixin implements MinecraftServerKJS {

    @Unique
    private final CompoundTag kjs$persistentData = new CompoundTag();

    @Unique
    private ScheduledEvents kjs$scheduledEvents;

    @Unique
    private ServerLevel kjs$overworld;

    @Unique
    private AttachedData<MinecraftServer> kjs$attachedData;

    @Unique
    private final Map<UUID, Map<Integer, ItemStack>> kjs$restoreInventories = new HashMap(1);

    @Shadow
    protected abstract boolean initServer() throws IOException;

    @Shadow
    public abstract void invalidateStatus();

    @Accessor("resources")
    @Override
    public abstract MinecraftServer.ReloadableResources kjs$getReloadableResources();

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void kjs$init(CallbackInfo ci) {
        CompletableFuture.runAsync(() -> this.kjs$afterResourcesLoaded(false), this.kjs$self());
    }

    @Override
    public CompoundTag kjs$getPersistentData() {
        return this.kjs$persistentData;
    }

    @Override
    public AttachedData<MinecraftServer> kjs$getData() {
        if (this.kjs$attachedData == null) {
            this.kjs$attachedData = new AttachedData<>(this.kjs$self());
            KubeJSPlugins.forEachPlugin(this.kjs$attachedData, KubeJSPlugin::attachServerData);
        }
        return this.kjs$attachedData;
    }

    @Override
    public ServerLevel kjs$getOverworld() {
        if (this.kjs$overworld == null) {
            this.kjs$overworld = this.kjs$self().overworld();
        }
        return this.kjs$overworld;
    }

    @Inject(method = { "tickServer" }, at = { @At("RETURN") })
    private void kjs$postTickServer(BooleanSupplier booleanSupplier, CallbackInfo ci) {
        if (this.kjs$scheduledEvents != null) {
            this.kjs$scheduledEvents.tickAll(this.kjs$getOverworld().m_46467_());
        }
        if (!this.kjs$restoreInventories.isEmpty()) {
            for (ServerPlayer player : this.kjs$self().getPlayerList().getPlayers()) {
                Map<Integer, ItemStack> map = (Map<Integer, ItemStack>) this.kjs$restoreInventories.get(player.m_20148_());
                if (map != null && player.m_6084_() && !player.hasDisconnected() && !(player.f_36096_ instanceof CustomChestMenu)) {
                    this.kjs$restoreInventories.remove(player.m_20148_());
                    NonNullList<ItemStack> playerItems = player.m_150109_().items;
                    for (int i = 0; i < playerItems.size(); i++) {
                        playerItems.set(i, (ItemStack) map.getOrDefault(i, ItemStack.EMPTY));
                    }
                }
            }
        }
        if (ServerEvents.TICK.hasListeners()) {
            ServerEvents.TICK.post(ScriptType.SERVER, new ServerEventJS(this.kjs$self()));
        }
    }

    @Override
    public ScheduledEvents kjs$getScheduledEvents() {
        if (this.kjs$scheduledEvents == null) {
            this.kjs$scheduledEvents = ScheduledServerEvent.make(this.kjs$self());
        }
        return this.kjs$scheduledEvents;
    }

    @Override
    public Map<UUID, Map<Integer, ItemStack>> kjs$restoreInventories() {
        return this.kjs$restoreInventories;
    }

    @Shadow
    @RemapForJS("isDedicated")
    public abstract boolean isDedicatedServer();

    @Shadow
    @RemapForJS("stop")
    public abstract void stopServer();

    @Inject(method = { "reloadResources" }, at = { @At("TAIL") })
    private void kjs$endResourceReload(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        CompletableFuture.runAsync(() -> this.kjs$afterResourcesLoaded(true), this.kjs$self());
    }
}