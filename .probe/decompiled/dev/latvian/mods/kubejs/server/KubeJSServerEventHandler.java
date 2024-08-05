package dev.latvian.mods.kubejs.server;

import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.CommandPerformEvent;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.latvian.mods.kubejs.bindings.event.LevelEvents;
import dev.latvian.mods.kubejs.bindings.event.ServerEvents;
import dev.latvian.mods.kubejs.command.CommandRegistryEventJS;
import dev.latvian.mods.kubejs.command.KubeJSCommands;
import dev.latvian.mods.kubejs.level.SimpleLevelEventJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.Util;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;

public class KubeJSServerEventHandler {

    private static final LevelResource PERSISTENT_DATA = new LevelResource("kubejs_persistent_data.nbt");

    public static void init() {
        LifecycleEvent.SERVER_BEFORE_START.register(KubeJSServerEventHandler::serverBeforeStart);
        CommandRegistrationEvent.EVENT.register(KubeJSServerEventHandler::registerCommands);
        LifecycleEvent.SERVER_STARTING.register(KubeJSServerEventHandler::serverStarting);
        LifecycleEvent.SERVER_STOPPING.register(KubeJSServerEventHandler::serverStopping);
        LifecycleEvent.SERVER_STOPPED.register(KubeJSServerEventHandler::serverStopped);
        LifecycleEvent.SERVER_LEVEL_SAVE.register(KubeJSServerEventHandler::serverLevelSaved);
        LifecycleEvent.SERVER_LEVEL_LOAD.register(KubeJSServerEventHandler::serverLevelLoaded);
        CommandPerformEvent.EVENT.register(KubeJSServerEventHandler::command);
    }

    public static void serverBeforeStart(MinecraftServer server) {
        UtilsJS.staticServer = server;
        UtilsJS.staticRegistryAccess = server.registryAccess();
        Path p = server.getWorldPath(PERSISTENT_DATA);
        if (Files.exists(p, new LinkOption[0])) {
            try {
                CompoundTag tag = NbtIo.readCompressed(p.toFile());
                if (tag != null) {
                    CompoundTag t = tag.getCompound("__restore_inventories");
                    if (!t.isEmpty()) {
                        tag.remove("__restore_inventories");
                        Map<UUID, Map<Integer, ItemStack>> playerMap = server.kjs$restoreInventories();
                        for (String key : t.getAllKeys()) {
                            ListTag list = t.getList(key, 10);
                            Map<Integer, ItemStack> map = (Map<Integer, ItemStack>) playerMap.computeIfAbsent(UUID.fromString(key), k -> new HashMap());
                            for (Tag tag2 : list) {
                                short slot = ((CompoundTag) tag2).getShort("Slot");
                                ItemStack stack = ItemStack.of((CompoundTag) tag2);
                                map.put(Integer.valueOf(slot), stack);
                            }
                        }
                    }
                    server.kjs$getPersistentData().merge(tag);
                }
            } catch (Exception var13) {
                var13.printStackTrace();
            }
        }
    }

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection selection) {
        KubeJSCommands.register(dispatcher);
        if (ServerEvents.COMMAND_REGISTRY.hasListeners()) {
            ServerEvents.COMMAND_REGISTRY.post(ScriptType.SERVER, new CommandRegistryEventJS(dispatcher, registry, selection));
        }
    }

    private static void serverStarting(MinecraftServer server) {
        ServerEvents.LOADED.post(ScriptType.SERVER, new ServerEventJS(server));
    }

    private static void serverStopping(MinecraftServer server) {
        ServerEvents.UNLOADED.post(ScriptType.SERVER, new ServerEventJS(server));
    }

    private static void serverStopped(MinecraftServer server) {
        UtilsJS.staticServer = null;
        UtilsJS.staticRegistryAccess = RegistryAccess.EMPTY;
    }

    private static void serverLevelLoaded(ServerLevel level) {
        if (LevelEvents.LOADED.hasListeners()) {
            LevelEvents.LOADED.post(new SimpleLevelEventJS(level), level.m_46472_().location());
        }
    }

    private static void serverLevelSaved(ServerLevel level) {
        if (level.m_46472_() == Level.OVERWORLD) {
            CompoundTag serverData = level.getServer().kjs$getPersistentData().copy();
            Path p = level.getServer().getWorldPath(PERSISTENT_DATA);
            Map<UUID, Map<Integer, ItemStack>> playerMap = level.getServer().kjs$restoreInventories();
            if (!playerMap.isEmpty()) {
                CompoundTag nbt = new CompoundTag();
                for (Entry<UUID, Map<Integer, ItemStack>> entry : playerMap.entrySet()) {
                    ListTag list = new ListTag();
                    for (Entry<Integer, ItemStack> entry2 : ((Map) entry.getValue()).entrySet()) {
                        CompoundTag tag = new CompoundTag();
                        tag.putShort("Slot", ((Integer) entry2.getKey()).shortValue());
                        ((ItemStack) entry2.getValue()).save(tag);
                        list.add(tag);
                    }
                    nbt.put(((UUID) entry.getKey()).toString(), list);
                }
                serverData.put("__restore_inventories", nbt);
            }
            Util.ioPool().execute(() -> {
                try {
                    NbtIo.writeCompressed(serverData, p.toFile());
                } catch (Exception var3x) {
                    var3x.printStackTrace();
                }
            });
        }
    }

    public static EventResult command(CommandPerformEvent event) {
        if (ServerEvents.COMMAND.hasListeners()) {
            CommandEventJS e = new CommandEventJS(event);
            return ServerEvents.COMMAND.post(e, e.getCommandName()).arch();
        } else {
            return EventResult.pass();
        }
    }
}