package io.redspace.ironsspellbooks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class LearnCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> command = dispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("learnSpell").requires(p -> p.hasPermission(2))).then(Commands.literal("forget").executes(context -> forget((CommandSourceStack) context.getSource())))).then(Commands.literal("learn").then(Commands.argument("spell", SpellArgument.spellArgument()).executes(commandContext -> learn((CommandSourceStack) commandContext.getSource(), (String) commandContext.getArgument("spell", String.class))))));
    }

    private static int forget(CommandSourceStack source) {
        MagicData.getPlayerMagicData(source.getPlayer()).getSyncedData().forgetAllSpells();
        return 1;
    }

    private static int learn(CommandSourceStack source, String spellId) {
        if (!spellId.contains(":")) {
            spellId = "irons_spellbooks:" + spellId;
        }
        AbstractSpell spell = SpellRegistry.getSpell(spellId);
        MagicData.getPlayerMagicData(source.getPlayer()).getSyncedData().learnSpell(spell);
        return 1;
    }
}