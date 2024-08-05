package yesman.epicfight.server.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import yesman.epicfight.api.data.reloader.SkillManager;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.skill.SkillSlot;

public class SkillArgument implements ArgumentType<Skill> {

    private static final Collection<String> EXAMPLES = Arrays.asList("epicfight:dodge");

    private static final DynamicCommandExceptionType ERROR_UNKNOWN_SKILL = new DynamicCommandExceptionType(obj -> Component.literal("epicfight.skillNotFound"));

    public static SkillArgument skill() {
        return new SkillArgument();
    }

    public static Skill getSkill(CommandContext<CommandSourceStack> commandContext, String name) {
        return (Skill) commandContext.getArgument(name, Skill.class);
    }

    public Skill parse(StringReader stringReader0) throws CommandSyntaxException {
        ResourceLocation resourcelocation = ResourceLocation.read(stringReader0);
        Skill skill = SkillManager.getSkill(resourcelocation.toString());
        if (skill != null && !skill.getCategory().learnable()) {
            skill = null;
        }
        return (Skill) Optional.ofNullable(skill).orElseThrow(() -> ERROR_UNKNOWN_SKILL.create(resourcelocation));
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContext, SuggestionsBuilder suggestionsBuilder) {
        SkillCategory skillCategory = ((ParsedCommandNode) commandContext.getNodes().get(4)).getNode() instanceof LiteralCommandNode<?> literalNode ? nullParam(SkillSlot.ENUM_MANAGER.get(literalNode.getLiteral())) : null;
        return SharedSuggestionProvider.suggestResource(SkillManager.getLearnableSkillNames(skillBuilder -> skillBuilder.isLearnable() && (skillCategory == null || skillBuilder.hasCategory(skillCategory))), suggestionsBuilder);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    private static SkillCategory nullParam(SkillSlot slot) {
        return slot == null ? null : slot.category();
    }
}