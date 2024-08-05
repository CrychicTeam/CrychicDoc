package net.minecraftforge.common.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.network.chat.Component;

public interface IEntitySelectorType {

    EntitySelector build(EntitySelectorParser var1) throws CommandSyntaxException;

    Component getSuggestionTooltip();
}