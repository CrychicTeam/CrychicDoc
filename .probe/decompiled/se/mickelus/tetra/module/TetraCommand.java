package se.mickelus.tetra.module;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;
import se.mickelus.tetra.aspect.TetraEnchantmentHelper;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.data.ImprovementData;

@ParametersAreNonnullByDefault
public class TetraCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        LiteralArgumentBuilder<CommandSourceStack> command = (LiteralArgumentBuilder<CommandSourceStack>) Commands.literal("tetra").requires(player -> player.hasPermission(2));
        command.then(((LiteralArgumentBuilder) Commands.literal("hone").executes(ctx -> runHone(ctx, 100))).then(Commands.argument("progress", IntegerArgumentType.integer(0, 100)).executes(ctx -> runHone(ctx, IntegerArgumentType.getInteger(ctx, "progress")))));
        command.then(Commands.literal("module").then(((RequiredArgumentBuilder) Commands.argument("slot", StringArgumentType.string()).suggests(TetraCommand::getAllSlotSuggestions).then(Commands.literal("remove").executes(ctx -> runRemoveModule(ctx, StringArgumentType.getString(ctx, "slot"))))).then(Commands.argument("module", StringArgumentType.string()).suggests(TetraCommand::getModuleSuggestions).then(Commands.argument("variant", StringArgumentType.string()).suggests(TetraCommand::getVariantSuggestions).executes(ctx -> runAddModule(ctx, StringArgumentType.getString(ctx, "slot"), StringArgumentType.getString(ctx, "module"), StringArgumentType.getString(ctx, "variant")))))));
        command.then(Commands.literal("improvement").then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("slot", StringArgumentType.string()).suggests(TetraCommand::getMajorSlotSuggestions).then(Commands.literal("clear").executes(ctx -> runClearImprovements(ctx, StringArgumentType.getString(ctx, "slot"))))).then(Commands.literal("remove").then(Commands.argument("improvement", StringArgumentType.string()).suggests(TetraCommand::getCurrentImprovementSuggestion).executes(ctx -> runRemoveImprovement(ctx, StringArgumentType.getString(ctx, "slot"), StringArgumentType.getString(ctx, "improvement")))))).then(Commands.literal("add").then(Commands.argument("improvement", StringArgumentType.string()).suggests(TetraCommand::getAvailableImprovementSuggestion).then(Commands.argument("level", IntegerArgumentType.integer()).suggests(TetraCommand::getImprovementLevelSuggestion).executes(ctx -> runAddImprovement(ctx, StringArgumentType.getString(ctx, "slot"), StringArgumentType.getString(ctx, "improvement"), IntegerArgumentType.getInteger(ctx, "level"))))))));
        command.then(Commands.literal("enchantment").then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("slot", StringArgumentType.string()).suggests(TetraCommand::getMajorSlotSuggestions).then(Commands.literal("clear").executes(ctx -> runClearEnchantments(ctx, StringArgumentType.getString(ctx, "slot"))))).then(Commands.literal("remove").then(Commands.argument("enchantment", ResourceArgument.resource(context, Registries.ENCHANTMENT)).suggests(TetraCommand::getCurrentEnchantmentsSuggestion).executes(ctx -> runRemoveEnchantment(ctx, StringArgumentType.getString(ctx, "slot"), ResourceArgument.getEnchantment(ctx, "enchantment")))))).then(Commands.literal("add").then(Commands.argument("enchantment", ResourceArgument.resource(context, Registries.ENCHANTMENT)).then(Commands.argument("level", IntegerArgumentType.integer()).suggests(TetraCommand::getEnchantmentLevelSuggestion).executes(ctx -> runAddEnchantment(ctx, StringArgumentType.getString(ctx, "slot"), ResourceArgument.getEnchantment(ctx, "enchantment"), IntegerArgumentType.getInteger(ctx, "level"))))))));
        dispatcher.register(command);
    }

    private static int runHone(CommandContext<CommandSourceStack> context, int progress) throws CommandSyntaxException {
        ServerPlayer player = ((CommandSourceStack) context.getSource()).getPlayerOrException();
        ItemStack itemStack = player.m_21205_();
        if (itemStack.getItem() instanceof IModularItem item) {
            if (item.canGainHoneProgress(itemStack)) {
                item.setHoningProgress(itemStack, (int) Math.ceil((double) ((float) (100 - progress) / 100.0F * (float) item.getHoningLimit(itemStack))));
                ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.literal("Honing progression set to §e" + progress + "%§r for ").append(itemStack.getDisplayName()), true);
                return 1;
            }
            ((CommandSourceStack) context.getSource()).sendFailure(Component.literal("Item cannot be honed"));
        } else {
            ((CommandSourceStack) context.getSource()).sendFailure(Component.literal("Main hand item is not a modular item"));
        }
        return 0;
    }

    private static int runRemoveModule(CommandContext<CommandSourceStack> context, String slot) throws CommandSyntaxException {
        ServerPlayer player = ((CommandSourceStack) context.getSource()).getPlayerOrException();
        ItemStack itemStack = player.m_21205_();
        if (itemStack.getItem() instanceof IModularItem item) {
            ItemModule module = item.getModuleFromSlot(itemStack, slot);
            if (module != null) {
                String moduleIdentifiers = "'" + module.getKey() + "' (" + module.getVariantData(itemStack).key + ")";
                module.removeModule(itemStack);
                module.postRemove(itemStack, player);
                IModularItem.updateIdentifier(itemStack);
                ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.literal("Removed module " + moduleIdentifiers + " from slot '" + slot + "' in item ").append(itemStack.getDisplayName()), true);
                return 1;
            }
            ((CommandSourceStack) context.getSource()).sendFailure(Component.literal("The provided slot is already empty"));
        } else {
            ((CommandSourceStack) context.getSource()).sendFailure(Component.literal("Main hand item is not a modular item"));
        }
        return 0;
    }

    private static int runAddModule(CommandContext<CommandSourceStack> context, String slot, String moduleKey, String variantKey) throws CommandSyntaxException {
        ServerPlayer player = ((CommandSourceStack) context.getSource()).getPlayerOrException();
        ItemStack itemStack = player.m_21205_();
        if (itemStack.getItem() instanceof IModularItem item) {
            ItemModule module = ItemUpgradeRegistry.instance.getModule(moduleKey);
            if (module != null) {
                ItemModule previousModule = item.getModuleFromSlot(itemStack, slot);
                if (previousModule != null) {
                    previousModule.removeModule(itemStack);
                    previousModule.postRemove(itemStack, player);
                }
                module.addModule(itemStack, variantKey, player);
                IModularItem.updateIdentifier(itemStack);
                ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.literal("Added module " + moduleKey + " in slot '" + slot + "' in item ").append(itemStack.getDisplayName()), true);
                return 1;
            }
        } else {
            ((CommandSourceStack) context.getSource()).sendFailure(Component.literal("Main hand item is not a modular item"));
        }
        return 0;
    }

    private static int runClearImprovements(CommandContext<CommandSourceStack> context, String slot) throws CommandSyntaxException {
        ServerPlayer player = ((CommandSourceStack) context.getSource()).getPlayerOrException();
        ItemStack itemStack = player.m_21205_();
        if (itemStack.getItem() instanceof IModularItem item) {
            if (item.getModuleFromSlot(itemStack, slot) instanceof ItemModuleMajor module) {
                ImprovementData[] improvements = module.getImprovements(itemStack);
                Arrays.stream(improvements).forEach(improvement -> module.removeImprovement(itemStack, improvement.key));
                IModularItem.updateIdentifier(itemStack);
                ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.literal("Cleared " + improvements.length + " improvements from slot '" + slot + "' in item ").append(itemStack.getDisplayName()), true);
            } else {
                ((CommandSourceStack) context.getSource()).sendFailure(Component.literal("The provided slot is empty (or not a major module slot)"));
            }
            return 1;
        } else {
            ((CommandSourceStack) context.getSource()).sendFailure(Component.literal("Main hand item is not a modular item"));
            return 0;
        }
    }

    private static int runRemoveImprovement(CommandContext<CommandSourceStack> context, String slot, String improvementKey) throws CommandSyntaxException {
        ServerPlayer player = ((CommandSourceStack) context.getSource()).getPlayerOrException();
        ItemStack itemStack = player.m_21205_();
        if (itemStack.getItem() instanceof IModularItem item) {
            if (item.getModuleFromSlot(itemStack, slot) instanceof ItemModuleMajor module) {
                module.removeImprovement(itemStack, improvementKey);
                IModularItem.updateIdentifier(itemStack);
                ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.literal("Removed improvement '" + improvementKey + "' from slot '" + slot + "' in item ").append(itemStack.getDisplayName()), true);
            } else {
                ((CommandSourceStack) context.getSource()).sendFailure(Component.literal("The provided slot is empty (or not a major module slot)"));
            }
            return 1;
        } else {
            ((CommandSourceStack) context.getSource()).sendFailure(Component.literal("Main hand item is not a modular item"));
            return 0;
        }
    }

    private static int runAddImprovement(CommandContext<CommandSourceStack> context, String slot, String improvementKey, int level) throws CommandSyntaxException {
        ServerPlayer player = ((CommandSourceStack) context.getSource()).getPlayerOrException();
        ItemStack itemStack = player.m_21205_();
        if (itemStack.getItem() instanceof IModularItem item) {
            if (item.getModuleFromSlot(itemStack, slot) instanceof ItemModuleMajor module) {
                module.removeCollidingImprovements(itemStack, improvementKey, level);
                module.addImprovement(itemStack, improvementKey, level);
                IModularItem.updateIdentifier(itemStack);
                ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.literal("Added improvement '" + improvementKey + "' at level " + level + " from slot '" + slot + "' in item ").append(itemStack.getDisplayName()), true);
            } else {
                ((CommandSourceStack) context.getSource()).sendFailure(Component.literal("The provided slot is empty (or not a major module slot)"));
            }
            return 1;
        } else {
            ((CommandSourceStack) context.getSource()).sendFailure(Component.literal("Main hand item is not a modular item"));
            return 0;
        }
    }

    private static int runClearEnchantments(CommandContext<CommandSourceStack> context, String slot) throws CommandSyntaxException {
        ServerPlayer player = ((CommandSourceStack) context.getSource()).getPlayerOrException();
        ItemStack itemStack = player.m_21205_();
        if (itemStack.getItem() instanceof IModularItem item) {
            if (item.getModuleFromSlot(itemStack, slot) instanceof ItemModuleMajor module) {
                Map<Enchantment, Integer> enchantments = module.getEnchantments(itemStack);
                module.removeEnchantments(itemStack);
                IModularItem.updateIdentifier(itemStack);
                ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.literal("Cleared " + enchantments.size() + " enchantments from slot '" + slot + "' in item ").append(itemStack.getDisplayName()), true);
            } else {
                ((CommandSourceStack) context.getSource()).sendFailure(Component.literal("The provided slot is empty (or not a major module slot)"));
            }
            return 1;
        } else {
            ((CommandSourceStack) context.getSource()).sendFailure(Component.literal("Main hand item is not a modular item"));
            return 0;
        }
    }

    private static int runRemoveEnchantment(CommandContext<CommandSourceStack> context, String slot, Holder<Enchantment> enchantment) throws CommandSyntaxException {
        ServerPlayer player = ((CommandSourceStack) context.getSource()).getPlayerOrException();
        ItemStack itemStack = player.m_21205_();
        if (itemStack.getItem() instanceof IModularItem item) {
            if (item.getModuleFromSlot(itemStack, slot) instanceof ItemModuleMajor module) {
                String enchantmentId = ForgeRegistries.ENCHANTMENTS.getKey((Enchantment) enchantment.get()).toString();
                TetraEnchantmentHelper.removeEnchantment(itemStack, enchantmentId);
                IModularItem.updateIdentifier(itemStack);
                ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.literal("Removed enchantment '" + enchantmentId + "' from item ").append(itemStack.getDisplayName()), true);
            } else {
                ((CommandSourceStack) context.getSource()).sendFailure(Component.literal("The provided slot is empty (or not a major module slot)"));
            }
            return 1;
        } else {
            ((CommandSourceStack) context.getSource()).sendFailure(Component.literal("Main hand item is not a modular item"));
            return 0;
        }
    }

    private static int runAddEnchantment(CommandContext<CommandSourceStack> context, String slot, Holder<Enchantment> enchantmentHolder, int level) throws CommandSyntaxException {
        ServerPlayer player = ((CommandSourceStack) context.getSource()).getPlayerOrException();
        ItemStack itemStack = player.m_21205_();
        if (itemStack.getItem() instanceof IModularItem item) {
            if (item.getModuleFromSlot(itemStack, slot) instanceof ItemModuleMajor module) {
                Enchantment enchantment = (Enchantment) enchantmentHolder.get();
                String enchantmentId = ForgeRegistries.ENCHANTMENTS.getKey(enchantment).toString();
                int currentLevel = itemStack.getItem().getEnchantmentLevel(itemStack, enchantment);
                TetraEnchantmentHelper.removeEnchantment(itemStack, enchantmentId);
                TetraEnchantmentHelper.applyEnchantment(itemStack, module.getSlot(), enchantment, level);
                IModularItem.updateIdentifier(itemStack);
                if (currentLevel > 0) {
                    ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.literal("Updated enchantment '" + enchantmentId + "' to level " + level + " in slot '" + slot + "' in item ").append(itemStack.getDisplayName()), true);
                } else {
                    ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.literal("Added enchantment '" + enchantmentId + "' at level " + level + " for slot '" + slot + "' in item ").append(itemStack.getDisplayName()), true);
                }
            } else {
                ((CommandSourceStack) context.getSource()).sendFailure(Component.literal("The provided slot is empty (or not a major module slot)"));
            }
            return 1;
        } else {
            ((CommandSourceStack) context.getSource()).sendFailure(Component.literal("Main hand item is not a modular item"));
            return 0;
        }
    }

    private static CompletableFuture<Suggestions> getMajorSlotSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        ServerPlayer player = ((CommandSourceStack) context.getSource()).getPlayer();
        if (player != null) {
            ItemStack itemStack = player.m_21205_();
            if (itemStack.getItem() instanceof IModularItem item) {
                return SharedSuggestionProvider.suggest(Arrays.stream(item.getMajorModuleKeys(itemStack)).map(key -> "\"" + key + "\""), builder);
            }
        }
        return SharedSuggestionProvider.suggest(Collections.emptyList(), builder);
    }

    private static CompletableFuture<Suggestions> getAllSlotSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        ServerPlayer player = ((CommandSourceStack) context.getSource()).getPlayer();
        if (player != null) {
            ItemStack itemStack = player.m_21205_();
            if (itemStack.getItem() instanceof IModularItem item) {
                List<String> suggestions = Stream.concat(Arrays.stream(item.getMajorModuleKeys(itemStack)), Arrays.stream(item.getMinorModuleKeys(itemStack))).map(slot -> "\"" + slot + "\"").toList();
                return SharedSuggestionProvider.suggest(suggestions, builder);
            }
        }
        return SharedSuggestionProvider.suggest(Collections.emptyList(), builder);
    }

    private static CompletableFuture<Suggestions> getModuleSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        String slot = StringArgumentType.getString(context, "slot");
        return SharedSuggestionProvider.suggest((String[]) ItemUpgradeRegistry.instance.getAllModules().stream().filter(module -> slot == null || slot.equals(module.getSlot())).filter(module -> !module.perk).map(ItemModule::getKey).map(key -> "\"" + key + "\"").toArray(String[]::new), builder);
    }

    private static CompletableFuture<Suggestions> getVariantSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        ItemModule module = ItemUpgradeRegistry.instance.getModule(StringArgumentType.getString(context, "module"));
        return SharedSuggestionProvider.suggest(Arrays.stream(module.getVariantData()).map(data -> data.key).map(key -> "\"" + key + "\""), builder);
    }

    private static CompletableFuture<Suggestions> getAvailableImprovementSuggestion(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        String slot = StringArgumentType.getString(context, "slot");
        ServerPlayer player = ((CommandSourceStack) context.getSource()).getPlayer();
        if (player != null) {
            ItemStack itemStack = player.m_21205_();
            if (itemStack.getItem() instanceof IModularItem item && item.getModuleFromSlot(itemStack, slot) instanceof ItemModuleMajor module) {
                List<String> suggestions = Arrays.stream(module.improvements).map(improvement -> improvement.key).map(key -> "\"" + key + "\"").toList();
                return SharedSuggestionProvider.suggest(suggestions, builder);
            }
        }
        return SharedSuggestionProvider.suggest(Collections.emptyList(), builder);
    }

    private static CompletableFuture<Suggestions> getCurrentImprovementSuggestion(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        String slot = StringArgumentType.getString(context, "slot");
        ServerPlayer player = ((CommandSourceStack) context.getSource()).getPlayer();
        if (player != null) {
            ItemStack itemStack = player.m_21205_();
            if (itemStack.getItem() instanceof IModularItem item && item.getModuleFromSlot(itemStack, slot) instanceof ItemModuleMajor module) {
                List<String> suggestions = Arrays.stream(module.getImprovements(itemStack)).map(improvement -> improvement.key).map(key -> "\"" + key + "\"").toList();
                return SharedSuggestionProvider.suggest(suggestions, builder);
            }
        }
        return SharedSuggestionProvider.suggest(Collections.emptyList(), builder);
    }

    private static CompletableFuture<Suggestions> getImprovementLevelSuggestion(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        String slot = StringArgumentType.getString(context, "slot");
        String improvementKey = StringArgumentType.getString(context, "improvement");
        ServerPlayer player = ((CommandSourceStack) context.getSource()).getPlayer();
        if (player != null) {
            ItemStack itemStack = player.m_21205_();
            if (itemStack.getItem() instanceof IModularItem item && item.getModuleFromSlot(itemStack, slot) instanceof ItemModuleMajor module) {
                List<String> suggestions = Arrays.stream(module.improvements).filter(improvement -> improvement.key.equals(improvementKey)).map(improvement -> improvement.level).map(String::valueOf).toList();
                return SharedSuggestionProvider.suggest(suggestions, builder);
            }
        }
        return SharedSuggestionProvider.suggest(Collections.emptyList(), builder);
    }

    private static CompletableFuture<Suggestions> getCurrentEnchantmentsSuggestion(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        String slot = StringArgumentType.getString(context, "slot");
        ServerPlayer player = ((CommandSourceStack) context.getSource()).getPlayer();
        if (player != null) {
            ItemStack itemStack = player.m_21205_();
            if (itemStack.getItem() instanceof IModularItem item && item.getModuleFromSlot(itemStack, slot) instanceof ItemModuleMajor module) {
                List<String> suggestions = module.getEnchantments(itemStack).keySet().stream().map(ForgeRegistries.ENCHANTMENTS::getKey).filter(Objects::nonNull).map(ResourceLocation::toString).toList();
                return SharedSuggestionProvider.suggest(suggestions, builder);
            }
        }
        return SharedSuggestionProvider.suggest(Collections.emptyList(), builder);
    }

    private static CompletableFuture<Suggestions> getEnchantmentLevelSuggestion(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        Holder<Enchantment> enchantment = ResourceArgument.getEnchantment(context, "enchantment");
        List<String> suggestions = IntStream.rangeClosed(((Enchantment) enchantment.get()).getMinLevel(), ((Enchantment) enchantment.get()).getMaxLevel()).mapToObj(String::valueOf).toList();
        return SharedSuggestionProvider.suggest(suggestions, builder);
    }
}