package org.embeddedt.modernfix.platform;

import com.google.common.collect.Multimap;
import com.mojang.brigadier.CommandDispatcher;
import java.nio.file.Path;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.tree.ClassNode;

public interface ModernFixPlatformHooks {

    ModernFixPlatformHooks INSTANCE = PlatformHookLoader.findInstance();

    boolean isClient();

    boolean isDedicatedServer();

    String getVersionString();

    boolean modPresent(String var1);

    boolean isDevEnv();

    void injectPlatformSpecificHacks();

    void applyASMTransformers(String var1, ClassNode var2);

    MinecraftServer getCurrentServer();

    boolean isEarlyLoadingNormally();

    boolean isLoadingNormally();

    Path getGameDirectory();

    void sendPacket(ServerPlayer var1, Object var2);

    Multimap<String, String> getCustomModOptions();

    void onServerCommandRegister(Consumer<CommandDispatcher<CommandSourceStack>> var1);

    void onLaunchComplete();

    void registerCreativeSearchTrees(SearchRegistry var1, SearchRegistry.TreeBuilderSupplier<ItemStack> var2, SearchRegistry.TreeBuilderSupplier<ItemStack> var3, BiConsumer<SearchRegistry.Key<ItemStack>, List<ItemStack>> var4);

    String getPlatformName();
}