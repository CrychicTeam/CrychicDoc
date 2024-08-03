package org.embeddedt.modernfix.platform.forge;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mojang.brigadier.CommandDispatcher;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.CreativeModeTabSearchRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.embeddedt.modernfix.core.ModernFixMixinPlugin;
import org.embeddedt.modernfix.forge.classloading.ATInjector;
import org.embeddedt.modernfix.forge.classloading.FastAccessTransformerList;
import org.embeddedt.modernfix.forge.config.NightConfigFixer;
import org.embeddedt.modernfix.forge.config.NightConfigWatchThrottler;
import org.embeddedt.modernfix.forge.init.ModernFixForge;
import org.embeddedt.modernfix.forge.packet.PacketHandler;
import org.embeddedt.modernfix.platform.ModernFixPlatformHooks;
import org.embeddedt.modernfix.spark.SparkLaunchProfiler;
import org.embeddedt.modernfix.util.CommonModUtil;
import org.embeddedt.modernfix.util.DummyList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;

public class ModernFixPlatformHooksImpl implements ModernFixPlatformHooks {

    private static final String verString = (String) Optional.ofNullable(ModernFixMixinPlugin.class.getPackage().getImplementationVersion()).orElse("[unknown]");

    private static Multimap<String, String> modOptions;

    @Override
    public boolean isClient() {
        return FMLLoader.getDist() == Dist.CLIENT;
    }

    @Override
    public boolean isDedicatedServer() {
        return FMLLoader.getDist().isDedicatedServer();
    }

    @Override
    public String getVersionString() {
        return verString;
    }

    @Override
    public boolean modPresent(String modId) {
        return FMLLoader.getLoadingModList().getModFileById(modId) != null;
    }

    @Override
    public boolean isDevEnv() {
        return !FMLLoader.isProduction();
    }

    @Override
    public MinecraftServer getCurrentServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    @Override
    public boolean isEarlyLoadingNormally() {
        return LoadingModList.get().getErrors().isEmpty();
    }

    @Override
    public boolean isLoadingNormally() {
        return this.isEarlyLoadingNormally() && ModLoader.isLoadingStateValid();
    }

    @Override
    public Path getGameDirectory() {
        return FMLPaths.GAMEDIR.get();
    }

    @Override
    public void sendPacket(ServerPlayer player, Object packet) {
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    @Override
    public void injectPlatformSpecificHacks() {
        if (!this.isEarlyLoadingNormally() && ModernFixMixinPlugin.instance.isOptionEnabled("bugfix.forge_at_inject_error.ATInjector")) {
            ATInjector.injectModATs();
        }
        FastAccessTransformerList.attemptReplace();
        try {
            Field groupMembersField = InjectorGroupInfo.class.getDeclaredField("members");
            groupMembersField.setAccessible(true);
            Field noGroupField = org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo.Map.class.getDeclaredField("NO_GROUP");
            noGroupField.setAccessible(true);
            InjectorGroupInfo noGroup = (InjectorGroupInfo) noGroupField.get(null);
            groupMembersField.set(noGroup, new DummyList());
        } catch (NoSuchFieldException var4) {
        } catch (ReflectiveOperationException | RuntimeException var5) {
            ModernFixMixinPlugin.instance.logger.error("Failed to patch mixin memory leak", var5);
        }
        if (ModernFixMixinPlugin.instance.isOptionEnabled("feature.spark_profile_launch.OnForge")) {
            CommonModUtil.runWithoutCrash(() -> SparkLaunchProfiler.start("launch"), "Failed to start profiler");
        }
        NightConfigFixer.monitorFileWatcher();
        NightConfigWatchThrottler.throttle();
    }

    @Override
    public void applyASMTransformers(String mixinClassName, ClassNode targetClass) {
    }

    @Override
    public void onServerCommandRegister(Consumer<CommandDispatcher<CommandSourceStack>> handler) {
        MinecraftForge.EVENT_BUS.addListener(event -> handler.accept(event.getDispatcher()));
    }

    @Override
    public Multimap<String, String> getCustomModOptions() {
        if (modOptions == null) {
            modOptions = ArrayListMultimap.create();
            for (ModInfo meta : LoadingModList.get().getMods()) {
                meta.getConfigElement(new String[] { "modernfix:integration" }).ifPresent(optionsObj -> {
                    if (optionsObj instanceof Map<Object, Object> options) {
                        options.forEach((key, value) -> {
                            if (key instanceof String && value instanceof String) {
                                modOptions.put((String) key, (String) value);
                            }
                        });
                    }
                });
            }
        }
        return modOptions;
    }

    @Override
    public void registerCreativeSearchTrees(SearchRegistry registry, SearchRegistry.TreeBuilderSupplier<ItemStack> nameSupplier, SearchRegistry.TreeBuilderSupplier<ItemStack> tagSupplier, BiConsumer<SearchRegistry.Key<ItemStack>, List<ItemStack>> populator) {
        for (SearchRegistry.Key<ItemStack> nameKey : CreativeModeTabSearchRegistry.getNameSearchKeys().values()) {
            registry.register(nameKey, nameSupplier);
        }
        for (SearchRegistry.Key<ItemStack> tagKey : CreativeModeTabSearchRegistry.getTagSearchKeys().values()) {
            registry.register(tagKey, tagSupplier);
        }
        Map<CreativeModeTab, SearchRegistry.Key<ItemStack>> tagSearchKeys = CreativeModeTabSearchRegistry.getTagSearchKeys();
        CreativeModeTabSearchRegistry.getNameSearchKeys().forEach((tab, nameSearchKey) -> {
            SearchRegistry.Key<ItemStack> tagSearchKey = (SearchRegistry.Key<ItemStack>) tagSearchKeys.get(tab);
            tab.setSearchTreeBuilder(contents -> {
                populator.accept(nameSearchKey, contents);
                populator.accept(tagSearchKey, contents);
            });
        });
    }

    @Override
    public void onLaunchComplete() {
        if (ModernFixMixinPlugin.instance.isOptionEnabled("feature.spark_profile_launch.OnForge")) {
            CommonModUtil.runWithoutCrash(() -> SparkLaunchProfiler.stop("launch"), "Failed to stop profiler");
        }
        ModernFixForge.launchDone = true;
    }

    @Override
    public String getPlatformName() {
        return "Forge";
    }
}