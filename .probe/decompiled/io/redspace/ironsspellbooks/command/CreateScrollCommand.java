package io.redspace.ironsspellbooks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;

public class CreateScrollCommand {

    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.irons_spellbooks.create_scroll.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
        pDispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("createScroll").requires(p_138819_ -> p_138819_.hasPermission(2))).then(Commands.argument("spell", SpellArgument.spellArgument()).then(Commands.argument("level", IntegerArgumentType.integer(1)).executes(commandContext -> createScroll((CommandSourceStack) commandContext.getSource(), (String) commandContext.getArgument("spell", String.class), IntegerArgumentType.getInteger(commandContext, "level"))))));
    }

    private static int createScroll(CommandSourceStack source, String spell, int spellLevel) throws CommandSyntaxException {
        if (!spell.contains(":")) {
            spell = "irons_spellbooks:" + spell;
        }
        AbstractSpell abstractSpell = (AbstractSpell) ((IForgeRegistry) SpellRegistry.REGISTRY.get()).getValue(new ResourceLocation(spell));
        if (abstractSpell == null || abstractSpell == SpellRegistry.none()) {
            throw ERROR_FAILED.create();
        } else if (spellLevel > abstractSpell.getMaxLevel()) {
            throw new SimpleCommandExceptionType(Component.translatable("commands.irons_spellbooks.create_spell.failed_max_level", abstractSpell.getSpellName(), abstractSpell.getMaxLevel())).create();
        } else {
            ServerPlayer serverPlayer = source.getPlayer();
            if (serverPlayer != null) {
                ItemStack itemStack = new ItemStack(ItemRegistry.SCROLL.get());
                ISpellContainer.createScrollContainer(abstractSpell, spellLevel, itemStack);
                if (serverPlayer.m_150109_().add(itemStack)) {
                    return 1;
                }
            }
            throw ERROR_FAILED.create();
        }
    }
}