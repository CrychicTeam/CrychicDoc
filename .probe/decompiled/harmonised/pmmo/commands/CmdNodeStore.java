package harmonised.pmmo.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import harmonised.pmmo.config.SkillsConfig;
import harmonised.pmmo.config.codecs.SkillData;
import harmonised.pmmo.core.Core;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraftforge.fml.LogicalSide;

public class CmdNodeStore {

    private static final String TARGET_ARG = "Target";

    private static final String SKILL_ARG = "Skill Name";

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return ((LiteralArgumentBuilder) Commands.literal("store").requires(p -> p.hasPermission(2))).then(Commands.argument("Target", EntityArgument.players()).then(Commands.argument("Skill Name", StringArgumentType.word()).suggests((ctx, builder) -> SharedSuggestionProvider.suggest(SkillsConfig.SKILLS.get().keySet(), builder)).executes(ctx -> store(ctx))));
    }

    public static int store(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "Target");
        String skillName = StringArgumentType.getString(ctx, "Skill Name");
        MinecraftServer server = ((CommandSourceStack) ctx.getSource()).getServer();
        for (ServerPlayer player : players) {
            int skillLevel = getSkillLevel(skillName, player.m_20148_());
            server.getScoreboard().m_83471_(player.m_6302_(), getOrCreate(server, skillName)).setScore(skillLevel);
        }
        return 0;
    }

    private static Objective getOrCreate(MinecraftServer server, String objective) {
        Objective obtainedObjective = server.getScoreboard().m_83477_(objective);
        if (obtainedObjective == null) {
            obtainedObjective = server.getScoreboard().m_83436_(objective, ObjectiveCriteria.DUMMY, Component.translatable("pmmo." + objective), ObjectiveCriteria.RenderType.INTEGER);
        }
        return obtainedObjective;
    }

    private static int getSkillLevel(String skill, UUID pid) {
        Core core = Core.get(LogicalSide.SERVER);
        SkillData skillData = (SkillData) SkillsConfig.SKILLS.get().get(skill);
        if (skillData == null) {
            return 0;
        } else if (!skillData.isSkillGroup()) {
            return core.getData().getPlayerSkillLevel(skill, pid);
        } else {
            int groupLevel = 0;
            double proportionModifier = skillData.getUseTotalLevels() ? 1.0 : (Double) ((Map) skillData.groupedSkills().get()).values().stream().collect(Collectors.summingDouble(Double::doubleValue));
            for (Entry<String, Double> portion : ((Map) skillData.groupedSkills().get()).entrySet()) {
                groupLevel = (int) ((double) groupLevel + (double) core.getData().getPlayerSkillLevel((String) portion.getKey(), pid) * ((Double) portion.getValue() / proportionModifier));
            }
            return groupLevel;
        }
    }
}