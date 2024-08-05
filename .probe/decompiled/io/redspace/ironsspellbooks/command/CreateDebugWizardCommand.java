package io.redspace.ironsspellbooks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.entity.mobs.debug_wizard.DebugWizard;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class CreateDebugWizardCommand {

    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.irons_spellbooks.create_debug_wizard.failed"));

    private static final SimpleCommandExceptionType ERROR_FAILED_MAX_LEVEL = new SimpleCommandExceptionType(Component.translatable("commands.irons_spellbooks.create_debug_wizard.failed_max_level"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
        pDispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("createDebugWizard").requires(commandSourceStack -> commandSourceStack.hasPermission(2))).then(Commands.argument("spell", SpellArgument.spellArgument()).then(Commands.argument("spellLevel", IntegerArgumentType.integer(1)).then(Commands.argument("targetsPlayer", BoolArgumentType.bool()).then(Commands.argument("cancelAfterTicks", IntegerArgumentType.integer(0)).executes(ctx -> createDebugWizard((CommandSourceStack) ctx.getSource(), (String) ctx.getArgument("spell", String.class), IntegerArgumentType.getInteger(ctx, "spellLevel"), BoolArgumentType.getBool(ctx, "targetsPlayer"), IntegerArgumentType.getInteger(ctx, "cancelAfterTicks"))))))));
    }

    private static int createDebugWizard(CommandSourceStack source, String spellId, int spellLevel, boolean targetsPlayer, int cancelAfterTicks) throws CommandSyntaxException {
        if (!spellId.contains(":")) {
            spellId = "irons_spellbooks:" + spellId;
        }
        AbstractSpell spell = SpellRegistry.getSpell(spellId);
        if (spellLevel > spell.getMaxLevel()) {
            throw new SimpleCommandExceptionType(Component.translatable("commands.irons_spellbooks.create_spell.failed_max_level", spell.getSpellName(), spell.getMaxLevel())).create();
        } else {
            ServerPlayer serverPlayer = source.getPlayer();
            if (serverPlayer != null) {
                DebugWizard debugWizard = new DebugWizard(EntityRegistry.DEBUG_WIZARD.get(), serverPlayer.f_19853_, spell, spellLevel, targetsPlayer, cancelAfterTicks);
                debugWizard.m_146884_(serverPlayer.m_20182_());
                if (serverPlayer.f_19853_.m_7967_(debugWizard)) {
                    return 1;
                }
            }
            throw ERROR_FAILED.create();
        }
    }
}