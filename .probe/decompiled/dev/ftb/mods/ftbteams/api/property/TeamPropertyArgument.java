package dev.ftb.mods.ftbteams.api.property;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.ftb.mods.ftbteams.api.event.TeamCollectPropertiesEvent;
import dev.ftb.mods.ftbteams.api.event.TeamEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TeamPropertyArgument implements ArgumentType<TeamProperty<?>> {

    private static final SimpleCommandExceptionType PROPERTY_NOT_FOUND = new SimpleCommandExceptionType(Component.translatable("ftbteams.property_not_found"));

    public static TeamPropertyArgument create() {
        return new TeamPropertyArgument();
    }

    public static TeamProperty<?> get(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return (TeamProperty<?>) context.getArgument(name, TeamProperty.class);
    }

    private TeamPropertyArgument() {
    }

    public TeamProperty<?> parse(StringReader reader) throws CommandSyntaxException {
        ResourceLocation id = ResourceLocation.read(reader);
        Map<ResourceLocation, TeamProperty<?>> map = new LinkedHashMap();
        TeamEvent.COLLECT_PROPERTIES.invoker().accept(new TeamCollectPropertiesEvent(propertyx -> map.put(propertyx.id, propertyx)));
        TeamProperty<?> property = (TeamProperty<?>) map.get(id);
        if (property != null) {
            return property;
        } else {
            throw PROPERTY_NOT_FOUND.create();
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        try {
            Map<String, TeamProperty<?>> map = new LinkedHashMap();
            TeamEvent.COLLECT_PROPERTIES.invoker().accept(new TeamCollectPropertiesEvent(property -> map.put(property.id.toString(), property)));
            return SharedSuggestionProvider.suggest(map.keySet(), builder);
        } catch (Exception var4) {
            return Suggestions.empty();
        }
    }

    public static class Info implements ArgumentTypeInfo<TeamPropertyArgument, TeamPropertyArgument.Info.Template> {

        public void serializeToNetwork(TeamPropertyArgument.Info.Template template, FriendlyByteBuf friendlyByteBuf) {
        }

        public TeamPropertyArgument.Info.Template deserializeFromNetwork(FriendlyByteBuf friendlyByteBuf) {
            return new TeamPropertyArgument.Info.Template();
        }

        public void serializeToJson(TeamPropertyArgument.Info.Template template, JsonObject jsonObject) {
        }

        public TeamPropertyArgument.Info.Template unpack(TeamPropertyArgument argumentType) {
            return new TeamPropertyArgument.Info.Template();
        }

        public final class Template implements ArgumentTypeInfo.Template<TeamPropertyArgument> {

            public TeamPropertyArgument instantiate(CommandBuildContext commandBuildContext) {
                return TeamPropertyArgument.create();
            }

            @Override
            public ArgumentTypeInfo<TeamPropertyArgument, ?> type() {
                return Info.this;
            }
        }
    }
}