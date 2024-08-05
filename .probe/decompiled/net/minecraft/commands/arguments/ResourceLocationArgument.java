package net.minecraft.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Arrays;
import java.util.Collection;
import net.minecraft.advancements.Advancement;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class ResourceLocationArgument implements ArgumentType<ResourceLocation> {

    private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo:bar", "012");

    private static final DynamicCommandExceptionType ERROR_UNKNOWN_ADVANCEMENT = new DynamicCommandExceptionType(p_107010_ -> Component.translatable("advancement.advancementNotFound", p_107010_));

    private static final DynamicCommandExceptionType ERROR_UNKNOWN_RECIPE = new DynamicCommandExceptionType(p_107005_ -> Component.translatable("recipe.notFound", p_107005_));

    private static final DynamicCommandExceptionType ERROR_UNKNOWN_PREDICATE = new DynamicCommandExceptionType(p_106998_ -> Component.translatable("predicate.unknown", p_106998_));

    private static final DynamicCommandExceptionType ERROR_UNKNOWN_ITEM_MODIFIER = new DynamicCommandExceptionType(p_106991_ -> Component.translatable("item_modifier.unknown", p_106991_));

    public static ResourceLocationArgument id() {
        return new ResourceLocationArgument();
    }

    public static Advancement getAdvancement(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        ResourceLocation $$2 = getId(commandContextCommandSourceStack0, string1);
        Advancement $$3 = ((CommandSourceStack) commandContextCommandSourceStack0.getSource()).getServer().getAdvancements().getAdvancement($$2);
        if ($$3 == null) {
            throw ERROR_UNKNOWN_ADVANCEMENT.create($$2);
        } else {
            return $$3;
        }
    }

    public static Recipe<?> getRecipe(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        RecipeManager $$2 = ((CommandSourceStack) commandContextCommandSourceStack0.getSource()).getServer().getRecipeManager();
        ResourceLocation $$3 = getId(commandContextCommandSourceStack0, string1);
        return (Recipe<?>) $$2.byKey($$3).orElseThrow(() -> ERROR_UNKNOWN_RECIPE.create($$3));
    }

    public static LootItemCondition getPredicate(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        ResourceLocation $$2 = getId(commandContextCommandSourceStack0, string1);
        LootDataManager $$3 = ((CommandSourceStack) commandContextCommandSourceStack0.getSource()).getServer().getLootData();
        LootItemCondition $$4 = (LootItemCondition) $$3.m_278789_(LootDataType.PREDICATE, $$2);
        if ($$4 == null) {
            throw ERROR_UNKNOWN_PREDICATE.create($$2);
        } else {
            return $$4;
        }
    }

    public static LootItemFunction getItemModifier(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        ResourceLocation $$2 = getId(commandContextCommandSourceStack0, string1);
        LootDataManager $$3 = ((CommandSourceStack) commandContextCommandSourceStack0.getSource()).getServer().getLootData();
        LootItemFunction $$4 = (LootItemFunction) $$3.m_278789_(LootDataType.MODIFIER, $$2);
        if ($$4 == null) {
            throw ERROR_UNKNOWN_ITEM_MODIFIER.create($$2);
        } else {
            return $$4;
        }
    }

    public static ResourceLocation getId(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
        return (ResourceLocation) commandContextCommandSourceStack0.getArgument(string1, ResourceLocation.class);
    }

    public ResourceLocation parse(StringReader stringReader0) throws CommandSyntaxException {
        return ResourceLocation.read(stringReader0);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}