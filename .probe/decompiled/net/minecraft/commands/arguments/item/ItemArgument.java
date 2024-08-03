package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public class ItemArgument implements ArgumentType<ItemInput> {

    private static final Collection<String> EXAMPLES = Arrays.asList("stick", "minecraft:stick", "stick{foo=bar}");

    private final HolderLookup<Item> items;

    public ItemArgument(CommandBuildContext commandBuildContext0) {
        this.items = commandBuildContext0.holderLookup(Registries.ITEM);
    }

    public static ItemArgument item(CommandBuildContext commandBuildContext0) {
        return new ItemArgument(commandBuildContext0);
    }

    public ItemInput parse(StringReader stringReader0) throws CommandSyntaxException {
        ItemParser.ItemResult $$1 = ItemParser.parseForItem(this.items, stringReader0);
        return new ItemInput($$1.item(), $$1.nbt());
    }

    public static <S> ItemInput getItem(CommandContext<S> commandContextS0, String string1) {
        return (ItemInput) commandContextS0.getArgument(string1, ItemInput.class);
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContextS0, SuggestionsBuilder suggestionsBuilder1) {
        return ItemParser.fillSuggestions(this.items, suggestionsBuilder1, false);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}