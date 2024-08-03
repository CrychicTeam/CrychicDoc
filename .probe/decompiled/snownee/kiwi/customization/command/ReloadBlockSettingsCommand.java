package snownee.kiwi.customization.command;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Sets;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Set;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import snownee.kiwi.Kiwi;
import snownee.kiwi.customization.CustomizationHooks;
import snownee.kiwi.customization.block.BlockFundamentals;
import snownee.kiwi.customization.block.KBlockSettings;
import snownee.kiwi.customization.block.loader.KBlockDefinition;
import snownee.kiwi.util.resource.OneTimeLoader;

public class ReloadBlockSettingsCommand {

    public static void register(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.then(Commands.literal("block_settings").executes(ctx -> reload((CommandSourceStack) ctx.getSource())));
    }

    private static int reload(CommandSourceStack source) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        OneTimeLoader.Context context = new OneTimeLoader.Context();
        BlockFundamentals fundamentals = BlockFundamentals.reload(CustomizationHooks.collectKiwiPacks(), context, false);
        long parseTime = stopwatch.elapsed().toMillis();
        stopwatch.reset().start();
        Set<Block> set = Sets.newHashSet();
        BuiltInRegistries.BLOCK.m_203611_().forEach(holder -> {
            KBlockDefinition definition = (KBlockDefinition) fundamentals.blocks().get(holder.key().location());
            if (definition != null && set.add((Block) holder.value())) {
                KBlockSettings.Builder builder = definition.createSettings(holder.key().location(), fundamentals.shapes());
                ((Block) holder.value()).f_60439_ = builder.get();
                KBlockDefinition.setConfiguringShape((Block) holder.value());
            }
        });
        Blocks.rebuildCache();
        ReloadSlotsCommand.reload(fundamentals);
        long attachTime = stopwatch.elapsed().toMillis();
        Kiwi.LOGGER.info("Parse time %dms + Attach time %dms = %dms".formatted(parseTime, attachTime, parseTime + attachTime));
        source.sendSuccess(() -> Component.literal("%d Block settings reloaded".formatted(set.size())), false);
        return 1;
    }
}