package com.mna.api.commands;

import com.mna.api.ManaAndArtificeMod;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class FactionArgument implements ArgumentType<ResourceLocation> {

    private static final Collection<String> EXAMPLES = new ArrayList();

    public static final DynamicCommandExceptionType PART_BAD_ID = new DynamicCommandExceptionType(p_208696_0_ -> Component.translatable("argument.item.id.invalid", p_208696_0_));

    public static final FactionArgument faction() {
        return new FactionArgument();
    }

    public ResourceLocation parse(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();
        String inputString = reader.readString();
        try {
            return new ResourceLocation(inputString);
        } catch (Exception var5) {
            reader.setCursor(i);
            throw PART_BAD_ID.createWithContext(reader, inputString.toString());
        }
    }

    private Collection<String> populateAndGetExamples() {
        if (EXAMPLES.size() == 0) {
            EXAMPLES.add("\"mna:none\"");
            ManaAndArtificeMod.getFactionsRegistry().getKeys().forEach(r -> EXAMPLES.add("\"" + r.toString() + "\""));
        }
        return EXAMPLES;
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        List<String> all = new ArrayList();
        all.addAll(this.populateAndGetExamples());
        return SharedSuggestionProvider.suggest(all, builder);
    }

    public static <S> ResourceLocation getFaction(CommandContext<S> context, String name) {
        return (ResourceLocation) context.getArgument(name, ResourceLocation.class);
    }

    public Collection<String> getExamples() {
        return this.populateAndGetExamples();
    }
}