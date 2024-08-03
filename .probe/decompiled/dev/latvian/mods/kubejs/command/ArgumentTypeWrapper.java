package dev.latvian.mods.kubejs.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;

public interface ArgumentTypeWrapper {

    ArgumentType<?> create(CommandRegistryEventJS var1);

    Object getResult(CommandContext<CommandSourceStack> var1, String var2) throws CommandSyntaxException;
}