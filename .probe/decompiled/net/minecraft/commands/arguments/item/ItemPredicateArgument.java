package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.datafixers.util.Either;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemPredicateArgument implements ArgumentType<ItemPredicateArgument.Result> {

    private static final Collection<String> EXAMPLES = Arrays.asList("stick", "minecraft:stick", "#stick", "#stick{foo=bar}");

    private final HolderLookup<Item> items;

    public ItemPredicateArgument(CommandBuildContext commandBuildContext0) {
        this.items = commandBuildContext0.holderLookup(Registries.ITEM);
    }

    public static ItemPredicateArgument itemPredicate(CommandBuildContext commandBuildContext0) {
        return new ItemPredicateArgument(commandBuildContext0);
    }

    public ItemPredicateArgument.Result parse(StringReader stringReader0) throws CommandSyntaxException {
        Either<ItemParser.ItemResult, ItemParser.TagResult> $$1 = ItemParser.parseForTesting(this.items, stringReader0);
        return (ItemPredicateArgument.Result) $$1.map(p_235356_ -> createResult(p_235359_ -> p_235359_ == p_235356_.item(), p_235356_.nbt()), p_235361_ -> createResult(p_235361_.tag()::m_203333_, p_235361_.nbt()));
    }

    public static Predicate<ItemStack> getItemPredicate(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
        return (Predicate<ItemStack>) commandContextCommandSourceStack0.getArgument(string1, ItemPredicateArgument.Result.class);
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContextS0, SuggestionsBuilder suggestionsBuilder1) {
        return ItemParser.fillSuggestions(this.items, suggestionsBuilder1, true);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    private static ItemPredicateArgument.Result createResult(Predicate<Holder<Item>> predicateHolderItem0, @Nullable CompoundTag compoundTag1) {
        return compoundTag1 != null ? p_235371_ -> p_235371_.is(predicateHolderItem0) && NbtUtils.compareNbt(compoundTag1, p_235371_.getTag(), true) : p_235364_ -> p_235364_.is(predicateHolderItem0);
    }

    public interface Result extends Predicate<ItemStack> {
    }
}