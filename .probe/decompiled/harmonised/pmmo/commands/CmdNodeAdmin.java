package harmonised.pmmo.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import harmonised.pmmo.api.enums.ObjectType;
import harmonised.pmmo.config.SkillsConfig;
import harmonised.pmmo.config.codecs.DataSource;
import harmonised.pmmo.config.codecs.PlayerData;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.core.IDataStorage;
import harmonised.pmmo.network.Networking;
import harmonised.pmmo.network.clientpackets.CP_SyncData;
import harmonised.pmmo.network.clientpackets.CP_SyncData_ClearXp;
import harmonised.pmmo.setup.datagen.LangProvider;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.LogicalSide;

public class CmdNodeAdmin {

    private static final String SKILL_ARG = "Skill Name";

    private static final String TARGET_ARG = "Target";

    private static final String TYPE_ARG = "Change Type";

    private static final String VALUE_ARG = "New Value";

    private static SuggestionProvider<CommandSourceStack> SKILL_SUGGESTIONS = new SuggestionProvider<CommandSourceStack>() {

        public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            return SharedSuggestionProvider.suggest(SkillsConfig.SKILLS.get().keySet(), builder);
        }
    };

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return ((LiteralArgumentBuilder) Commands.literal("admin").requires(p -> p.hasPermission(2))).then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("Target", EntityArgument.players()).then(Commands.literal("set").then(Commands.argument("Skill Name", StringArgumentType.word()).suggests(SKILL_SUGGESTIONS).then(Commands.argument("Change Type", StringArgumentType.word()).suggests((ctx, builder) -> SharedSuggestionProvider.suggest(List.of("level", "xp"), builder)).then(Commands.argument("New Value", LongArgumentType.longArg()).executes(ctx -> adminSetOrAdd(ctx, true))))))).then(Commands.literal("add").then(Commands.argument("Skill Name", StringArgumentType.word()).suggests(SKILL_SUGGESTIONS).then(Commands.argument("Change Type", StringArgumentType.word()).suggests((ctx, builder) -> SharedSuggestionProvider.suggest(List.of("level", "xp"), builder)).then(Commands.argument("New Value", LongArgumentType.longArg()).executes(ctx -> adminSetOrAdd(ctx, false))))))).then(Commands.literal("clear").executes(CmdNodeAdmin::adminClear))).then(Commands.literal("ignoreReqs").executes(CmdNodeAdmin::exemptAdmin))).then(Commands.literal("adminBonus").then(Commands.argument("Skill Name", StringArgumentType.word()).suggests(SKILL_SUGGESTIONS).then(Commands.argument("New Value", DoubleArgumentType.doubleArg(0.0)).executes(CmdNodeAdmin::adminBonuses))))).executes(ctx -> displayPlayer(ctx)));
    }

    public static int adminSetOrAdd(CommandContext<CommandSourceStack> ctx, boolean isSet) throws CommandSyntaxException {
        Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "Target");
        String skillName = StringArgumentType.getString(ctx, "Skill Name");
        boolean isLevel = StringArgumentType.getString(ctx, "Change Type").equalsIgnoreCase("level");
        Long value = LongArgumentType.getLong(ctx, "New Value");
        IDataStorage data = Core.get(LogicalSide.SERVER).getData();
        for (ServerPlayer player : players) {
            if (isSet) {
                if (isLevel) {
                    data.setPlayerSkillLevel(skillName, player.m_20148_(), value.intValue());
                    ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> LangProvider.SET_LEVEL.asComponent(skillName, value, player.m_7755_()), true);
                } else {
                    data.setXpRaw(player.m_20148_(), skillName, value);
                    ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> LangProvider.SET_XP.asComponent(skillName, value, player.m_7755_()), true);
                }
            } else if (isLevel) {
                data.changePlayerSkillLevel(skillName, player.m_20148_(), value.intValue());
                ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> LangProvider.ADD_LEVEL.asComponent(skillName, value, player.m_7755_()), true);
            } else {
                data.setXpDiff(player.m_20148_(), skillName, value);
                ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> LangProvider.ADD_XP.asComponent(skillName, value, player.m_7755_()), true);
            }
        }
        return 0;
    }

    public static int adminClear(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        IDataStorage data = Core.get(LogicalSide.SERVER).getData();
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "Target")) {
            data.setXpMap(player.m_20148_(), new HashMap());
            Networking.sendToClient(new CP_SyncData_ClearXp(), player);
        }
        return 0;
    }

    public static int displayPlayer(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        IDataStorage data = Core.get(LogicalSide.SERVER).getData();
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "Target")) {
            ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> player.m_7755_(), false);
            for (Entry<String, Long> skillMap : data.getXpMap(player.m_20148_()).entrySet()) {
                int level = data.getLevelFromXP((Long) skillMap.getValue());
                ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> Component.literal((String) skillMap.getKey() + ": " + level + " | " + skillMap.getValue()), false);
            }
        }
        return 0;
    }

    public static int exemptAdmin(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Core core = Core.get(((CommandSourceStack) ctx.getSource()).getLevel());
        ServerPlayer player = EntityArgument.getPlayer(ctx, "Target");
        ResourceLocation playerID = new ResourceLocation(player.m_20148_().toString());
        PlayerData existing = (PlayerData) core.getLoader().PLAYER_LOADER.getData().get(playerID);
        boolean exists = existing != null;
        PlayerData updated = new PlayerData(true, exists ? !existing.ignoreReq() : true, exists ? existing.bonuses() : Map.of());
        core.getLoader().PLAYER_LOADER.getData().put(playerID, updated);
        Networking.sendToClient(new CP_SyncData(ObjectType.PLAYER, (Map<ResourceLocation, ? extends DataSource<?>>) core.getLoader().PLAYER_LOADER.getData()), player);
        return 0;
    }

    public static int adminBonuses(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        double bonus = DoubleArgumentType.getDouble(ctx, "New Value");
        String skill = StringArgumentType.getString(ctx, "Skill Name");
        Core core = Core.get(((CommandSourceStack) ctx.getSource()).getLevel());
        ServerPlayer player = EntityArgument.getPlayer(ctx, "Target");
        ResourceLocation playerID = new ResourceLocation(player.m_20148_().toString());
        PlayerData existing = (PlayerData) core.getLoader().PLAYER_LOADER.getData().get(playerID);
        boolean exists = existing != null;
        Map<String, Double> bonuses = exists ? new HashMap(existing.bonuses()) : new HashMap();
        bonuses.put(skill, bonus);
        if (skill.equals("clear")) {
            bonuses.clear();
        }
        PlayerData updated = new PlayerData(true, exists ? !existing.ignoreReq() : true, bonuses);
        core.getLoader().PLAYER_LOADER.getData().put(playerID, updated);
        Networking.sendToClient(new CP_SyncData(ObjectType.PLAYER, (Map<ResourceLocation, ? extends DataSource<?>>) core.getLoader().PLAYER_LOADER.getData()), player);
        return 0;
    }
}