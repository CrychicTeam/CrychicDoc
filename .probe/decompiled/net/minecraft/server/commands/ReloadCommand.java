package net.minecraft.server.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.logging.LogUtils;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.WorldData;
import org.slf4j.Logger;

public class ReloadCommand {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static void reloadPacks(Collection<String> collectionString0, CommandSourceStack commandSourceStack1) {
        commandSourceStack1.getServer().reloadResources(collectionString0).exceptionally(p_138234_ -> {
            LOGGER.warn("Failed to execute reload", p_138234_);
            commandSourceStack1.sendFailure(Component.translatable("commands.reload.failure"));
            return null;
        });
    }

    private static Collection<String> discoverNewPacks(PackRepository packRepository0, WorldData worldData1, Collection<String> collectionString2) {
        packRepository0.reload();
        Collection<String> $$3 = Lists.newArrayList(collectionString2);
        Collection<String> $$4 = worldData1.getDataConfiguration().dataPacks().getDisabled();
        for (String $$5 : packRepository0.getAvailableIds()) {
            if (!$$4.contains($$5) && !$$3.contains($$5)) {
                $$3.add($$5);
            }
        }
        return $$3;
    }

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("reload").requires(p_138231_ -> p_138231_.hasPermission(2))).executes(p_288528_ -> {
            CommandSourceStack $$1 = (CommandSourceStack) p_288528_.getSource();
            MinecraftServer $$2 = $$1.getServer();
            PackRepository $$3 = $$2.getPackRepository();
            WorldData $$4 = $$2.getWorldData();
            Collection<String> $$5 = $$3.getSelectedIds();
            Collection<String> $$6 = discoverNewPacks($$3, $$4, $$5);
            $$1.sendSuccess(() -> Component.translatable("commands.reload.success"), true);
            reloadPacks($$6, $$1);
            return 0;
        }));
    }
}