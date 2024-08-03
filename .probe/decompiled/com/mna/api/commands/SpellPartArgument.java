package com.mna.api.commands;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.spells.ISpellHelper;
import com.mna.api.spells.base.ISpellComponent;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SpellPartArgument implements ArgumentType<ISpellComponent> {

    private static final Collection<String> EXAMPLES = Arrays.asList("mna:shapes/touch", "mna:components/break", "mna:modifiers/damage");

    public static final DynamicCommandExceptionType PART_BAD_ID = new DynamicCommandExceptionType(p_208696_0_ -> Component.translatable("argument.item.id.invalid", p_208696_0_));

    public static SpellPartArgument spell() {
        return new SpellPartArgument();
    }

    public ISpellComponent parse(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();
        ResourceLocation resourcelocation = ResourceLocation.read(reader);
        ISpellHelper helper = ManaAndArtificeMod.getSpellHelper();
        if (helper.getShapeRegistry().containsKey(resourcelocation)) {
            return ManaAndArtificeMod.getShapeRegistry().getValue(resourcelocation);
        } else if (helper.getComponentRegistry().containsKey(resourcelocation)) {
            return ManaAndArtificeMod.getComponentRegistry().getValue(resourcelocation);
        } else if (helper.getModifierRegistry().containsKey(resourcelocation)) {
            return ManaAndArtificeMod.getModifierRegistry().getValue(resourcelocation);
        } else {
            reader.setCursor(i);
            throw PART_BAD_ID.createWithContext(reader, resourcelocation.toString());
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        List<ResourceLocation> all = new ArrayList();
        all.addAll(ManaAndArtificeMod.getShapeRegistry().getKeys());
        all.addAll(ManaAndArtificeMod.getComponentRegistry().getKeys());
        all.addAll(ManaAndArtificeMod.getModifierRegistry().getKeys());
        return SharedSuggestionProvider.suggestResource(all, builder);
    }

    public static <S> ISpellComponent getSpell(CommandContext<S> context, String name) {
        return (ISpellComponent) context.getArgument(name, ISpellComponent.class);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}