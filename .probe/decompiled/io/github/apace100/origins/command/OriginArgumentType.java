package io.github.apace100.origins.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import io.github.edwinmindcraft.origins.api.origin.OriginLayer;
import io.github.edwinmindcraft.origins.api.registry.OriginsBuiltinRegistries;
import io.github.edwinmindcraft.origins.api.registry.OriginsDynamicRegistries;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class OriginArgumentType implements ArgumentType<ResourceLocation> {

    public static final DynamicCommandExceptionType ORIGIN_NOT_FOUND = new DynamicCommandExceptionType(p_208663_0_ -> Component.translatable("commands.origin.origin_not_found", p_208663_0_));

    public static OriginArgumentType origin() {
        return new OriginArgumentType();
    }

    public ResourceLocation parse(StringReader p_parse_1_) throws CommandSyntaxException {
        return ResourceLocation.read(p_parse_1_);
    }

    public static ResourceKey<Origin> getOrigin(CommandContext<CommandSourceStack> context, String argumentName) throws CommandSyntaxException {
        ResourceLocation id = (ResourceLocation) context.getArgument(argumentName, ResourceLocation.class);
        if (!OriginsAPI.getOriginsRegistry(((CommandSourceStack) context.getSource()).getServer()).containsKey(id)) {
            throw ORIGIN_NOT_FOUND.create(id);
        } else {
            return ResourceKey.create(OriginsDynamicRegistries.ORIGINS_REGISTRY, id);
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        List<ResourceLocation> availableOrigins = new ArrayList();
        try {
            ResourceLocation originLayerId = (ResourceLocation) context.getArgument("layer", ResourceLocation.class);
            OriginLayer originLayer = OriginsAPI.getLayersRegistry().get(originLayerId);
            availableOrigins.add(((IForgeRegistry) OriginsBuiltinRegistries.ORIGINS.get()).getKey(Origin.EMPTY));
            if (originLayer != null) {
                availableOrigins.addAll((Collection) originLayer.origins().stream().map(Holder::m_203543_).flatMap(Optional::stream).map(ResourceKey::m_135782_).collect(Collectors.toCollection(LinkedList::new)));
            }
        } catch (IllegalArgumentException var6) {
        }
        return SharedSuggestionProvider.suggestResource(availableOrigins.stream(), builder);
    }
}