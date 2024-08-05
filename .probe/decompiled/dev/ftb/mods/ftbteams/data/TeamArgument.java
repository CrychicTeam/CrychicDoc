package dev.ftb.mods.ftbteams.data;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class TeamArgument implements ArgumentType<TeamArgumentProvider> {

    public static final SimpleCommandExceptionType ALREADY_IN_PARTY = new SimpleCommandExceptionType(Component.translatable("ftbteams.already_in_party"));

    public static final DynamicCommandExceptionType PLAYER_IN_PARTY = new DynamicCommandExceptionType(object -> Component.translatable("ftbteams.player_already_in_party", object));

    public static final SimpleCommandExceptionType NOT_IN_PARTY = new SimpleCommandExceptionType(Component.translatable("ftbteams.not_in_party"));

    public static final DynamicCommandExceptionType TEAM_NOT_FOUND = new DynamicCommandExceptionType(object -> Component.translatable("ftbteams.team_not_found", object));

    public static final DynamicCommandExceptionType CANT_EDIT = new DynamicCommandExceptionType(object -> Component.translatable("ftbteams.cant_edit", object));

    public static final Dynamic2CommandExceptionType NOT_MEMBER = new Dynamic2CommandExceptionType((a, b) -> Component.translatable("ftbteams.not_member", a, b));

    public static final Dynamic2CommandExceptionType NOT_OFFICER = new Dynamic2CommandExceptionType((a, b) -> Component.translatable("ftbteams.not_officer", a, b));

    public static final DynamicCommandExceptionType NOT_INVITED = new DynamicCommandExceptionType(object -> Component.translatable("ftbteams.not_invited", object));

    public static final SimpleCommandExceptionType OWNER_CANT_LEAVE = new SimpleCommandExceptionType(Component.translatable("ftbteams.owner_cant_leave"));

    public static final SimpleCommandExceptionType CANT_KICK_OWNER = new SimpleCommandExceptionType(Component.translatable("ftbteams.cant_kick_owner"));

    public static final SimpleCommandExceptionType API_OVERRIDE = new SimpleCommandExceptionType(Component.translatable("ftbteams.party_api_only"));

    public static final SimpleCommandExceptionType NAME_TOO_SHORT = new SimpleCommandExceptionType(Component.translatable("ftbteams.name_too_short"));

    public static final SimpleCommandExceptionType NO_PERMISSION = new SimpleCommandExceptionType(Component.translatable("ftbteams.server_permissions_prevent"));

    public static TeamArgument create() {
        return new TeamArgument();
    }

    public static Team get(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return ((TeamArgumentProvider) context.getArgument(name, TeamArgumentProvider.class)).getTeam((CommandSourceStack) context.getSource());
    }

    private TeamArgument() {
    }

    public TeamArgumentProvider parse(StringReader reader) throws CommandSyntaxException {
        if (reader.canRead() && reader.peek() == '@') {
            EntitySelector selector = new EntitySelectorParser(reader).parse();
            if (selector.includesEntities()) {
                throw EntityArgument.ERROR_ONLY_PLAYERS_ALLOWED.create();
            } else {
                return new TeamArgument.SelectorProvider(selector);
            }
        } else {
            int i = reader.getCursor();
            while (reader.canRead() && reader.peek() != ' ') {
                reader.skip();
            }
            return new TeamArgument.IDProvider(reader.getString().substring(i, reader.getCursor()));
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContext, SuggestionsBuilder builder) {
        if (!(commandContext.getSource() instanceof SharedSuggestionProvider)) {
            return Suggestions.empty();
        } else {
            LinkedHashSet<String> list = new LinkedHashSet();
            FTBTeamsAPI.API api = FTBTeamsAPI.api();
            if (commandContext.getSource() instanceof CommandSourceStack) {
                if (api.isManagerLoaded()) {
                    for (Team team : api.getManager().getTeams()) {
                        if (!team.isPlayerTeam()) {
                            list.add(team.getShortName());
                        }
                    }
                }
            } else if (api.isClientManagerLoaded()) {
                for (Team teamx : api.getClientManager().getTeams()) {
                    if (!teamx.isPlayerTeam()) {
                        list.add(teamx.getShortName());
                    }
                }
            }
            list.addAll(((SharedSuggestionProvider) commandContext.getSource()).getOnlinePlayerNames());
            return SharedSuggestionProvider.suggest(list, builder);
        }
    }

    private static class IDProvider implements TeamArgumentProvider {

        private final String id;

        private IDProvider(String s) {
            this.id = s;
        }

        private CommandSyntaxException error() {
            return TeamArgument.TEAM_NOT_FOUND.create(this.id);
        }

        @Override
        public Team getTeam(CommandSourceStack source) throws CommandSyntaxException {
            Optional<Team> t = FTBTeamsAPI.api().getManager().getTeamByName(this.id);
            return t.isPresent() ? (Team) t.get() : (Team) ((Optional) source.getServer().getProfileCache().get(this.id).map(GameProfile::getId).map(FTBTeamsAPI.api().getManager()::getTeamForPlayerID).orElseThrow()).orElseThrow(this::error);
        }
    }

    public static class Info implements ArgumentTypeInfo<TeamArgument, TeamArgument.Info.Template> {

        public void serializeToNetwork(TeamArgument.Info.Template template, FriendlyByteBuf friendlyByteBuf) {
        }

        public TeamArgument.Info.Template deserializeFromNetwork(FriendlyByteBuf friendlyByteBuf) {
            return new TeamArgument.Info.Template();
        }

        public void serializeToJson(TeamArgument.Info.Template template, JsonObject jsonObject) {
        }

        public TeamArgument.Info.Template unpack(TeamArgument argumentType) {
            return new TeamArgument.Info.Template();
        }

        public final class Template implements ArgumentTypeInfo.Template<TeamArgument> {

            public TeamArgument instantiate(CommandBuildContext commandBuildContext) {
                return TeamArgument.create();
            }

            @Override
            public ArgumentTypeInfo<TeamArgument, ?> type() {
                return Info.this;
            }
        }
    }

    private static class SelectorProvider implements TeamArgumentProvider {

        private final EntitySelector selector;

        private SelectorProvider(EntitySelector s) {
            this.selector = s;
        }

        @Override
        public Team getTeam(CommandSourceStack source) throws CommandSyntaxException {
            ServerPlayer player = this.selector.findSinglePlayer(source);
            return (Team) FTBTeamsAPI.api().getManager().getTeamForPlayer(player).orElseThrow(() -> TeamArgument.TEAM_NOT_FOUND.create(player.m_20148_()));
        }
    }
}