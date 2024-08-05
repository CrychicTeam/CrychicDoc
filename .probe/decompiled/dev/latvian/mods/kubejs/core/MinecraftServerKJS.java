package dev.latvian.mods.kubejs.core;

import dev.latvian.mods.kubejs.CommonProperties;
import dev.latvian.mods.kubejs.net.SendDataFromServerMessage;
import dev.latvian.mods.kubejs.player.AdvancementJS;
import dev.latvian.mods.kubejs.player.EntityArrayList;
import dev.latvian.mods.kubejs.server.DataExport;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import java.util.Map;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@RemapPrefixForJS("kjs$")
public interface MinecraftServerKJS extends WithAttachedData<MinecraftServer>, WithPersistentData, DataSenderKJS, MinecraftEnvironmentKJS {

    default MinecraftServer kjs$self() {
        return (MinecraftServer) this;
    }

    MinecraftServer.ReloadableResources kjs$getReloadableResources();

    ServerLevel kjs$getOverworld();

    @Override
    default Component kjs$getName() {
        return Component.literal(this.kjs$self().m_7326_());
    }

    @Override
    default void kjs$tell(Component message) {
        this.kjs$self().sendSystemMessage(message);
        for (ServerPlayer player : this.kjs$self().getPlayerList().getPlayers()) {
            player.kjs$tell(message);
        }
    }

    @Override
    default void kjs$setStatusMessage(Component message) {
        for (ServerPlayer player : this.kjs$self().getPlayerList().getPlayers()) {
            player.kjs$setStatusMessage(message);
        }
    }

    @Override
    default int kjs$runCommand(String command) {
        return this.kjs$self().getCommands().performPrefixedCommand(this.kjs$self().createCommandSourceStack(), command);
    }

    @Override
    default int kjs$runCommandSilent(String command) {
        return this.kjs$self().getCommands().performPrefixedCommand(this.kjs$self().createCommandSourceStack().withSuppressedOutput(), command);
    }

    default ServerLevel kjs$getLevel(ResourceLocation dimension) {
        return this.kjs$self().getLevel(ResourceKey.create(Registries.DIMENSION, dimension));
    }

    @Nullable
    default ServerPlayer kjs$getPlayer(PlayerSelector selector) {
        return selector.getPlayer(this.kjs$self());
    }

    default EntityArrayList kjs$getPlayers() {
        return new EntityArrayList(this.kjs$self().overworld(), this.kjs$self().getPlayerList().getPlayers());
    }

    default EntityArrayList kjs$getEntities() {
        EntityArrayList list = new EntityArrayList(this.kjs$self().overworld(), 10);
        for (ServerLevel level : this.kjs$self().getAllLevels()) {
            list.addAllIterable(level.getAllEntities());
        }
        return list;
    }

    @Nullable
    default AdvancementJS kjs$getAdvancement(ResourceLocation id) {
        Advancement a = this.kjs$self().getAdvancements().getAdvancement(id);
        return a == null ? null : new AdvancementJS(a);
    }

    @Override
    default void kjs$sendData(String channel, @Nullable CompoundTag data) {
        new SendDataFromServerMessage(channel, data).sendToAll(this.kjs$self());
    }

    @HideFromJS
    default void kjs$afterResourcesLoaded(boolean reload) {
        if (reload) {
            DataExport.exportData();
        }
        if (reload && CommonProperties.get().announceReload && !CommonProperties.get().hideServerScriptErrors) {
            if (ConsoleJS.SERVER.errors.isEmpty()) {
                this.kjs$tell(Component.literal("Reloaded with no KubeJS errors!").withStyle(ChatFormatting.GREEN));
            } else {
                this.kjs$tell(ConsoleJS.SERVER.errorsComponent("/kubejs errors server"));
            }
        }
        ConsoleJS.SERVER.setCapturingErrors(false);
        ConsoleJS.SERVER.info("Server resource reload complete!");
    }

    default Map<UUID, Map<Integer, ItemStack>> kjs$restoreInventories() {
        throw new NoMixinException();
    }
}