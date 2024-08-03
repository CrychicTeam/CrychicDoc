package snownee.kiwi.customization.command;

import com.google.common.base.Stopwatch;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import snownee.kiwi.Kiwi;
import snownee.kiwi.customization.CustomizationHooks;
import snownee.kiwi.customization.block.BlockFundamentals;
import snownee.kiwi.customization.block.loader.KBlockDefinition;
import snownee.kiwi.customization.placement.PlaceChoices;
import snownee.kiwi.customization.placement.PlaceSlot;
import snownee.kiwi.util.resource.OneTimeLoader;

public class ReloadSlotsCommand {

    public static void register(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.then(Commands.literal("placement_system").executes(ctx -> reload((CommandSourceStack) ctx.getSource())));
    }

    private static int reload(CommandSourceStack source) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        OneTimeLoader.Context context = new OneTimeLoader.Context();
        BlockFundamentals fundamentals = BlockFundamentals.reload(CustomizationHooks.collectKiwiPacks(), context, false);
        long parseTime = stopwatch.elapsed().toMillis();
        stopwatch.reset().start();
        int choicesCount = reload(fundamentals);
        long attachTime = stopwatch.elapsed().toMillis();
        Kiwi.LOGGER.info("Parse time %dms + Attach time %dms = %dms".formatted(parseTime, attachTime, parseTime + attachTime));
        source.sendSuccess(() -> Component.literal("Slots in %d blocks, %d block states have been reloaded, using %d providers".formatted(PlaceSlot.blockCount(), fundamentals.slotProviders().slots().size(), fundamentals.slotProviders().providers().size())), false);
        source.sendSuccess(() -> Component.literal("Place choices in %d blocks have been reloaded, using %d providers".formatted(choicesCount, fundamentals.placeChoices().choices().size())), false);
        return 1;
    }

    public static int reload(BlockFundamentals fundamentals) {
        AtomicInteger choicesCounter = new AtomicInteger();
        BuiltInRegistries.BLOCK.m_203611_().forEach(holder -> {
            PlaceChoices.setTo((Block) holder.value(), null);
            KBlockDefinition definition = (KBlockDefinition) fundamentals.blocks().get(holder.key().location());
            if (definition != null) {
                fundamentals.slotProviders().attachSlotsA((Block) holder.value(), definition);
                if (fundamentals.placeChoices().attachChoicesA((Block) holder.value(), definition)) {
                    choicesCounter.incrementAndGet();
                }
            }
        });
        fundamentals.slotProviders().attachSlotsB();
        choicesCounter.addAndGet(fundamentals.placeChoices().attachChoicesB());
        fundamentals.slotLinks().finish();
        return choicesCounter.get();
    }
}