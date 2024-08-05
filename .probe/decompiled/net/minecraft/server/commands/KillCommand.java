package net.minecraft.server.commands;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

public class KillCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("kill").requires(p_137812_ -> p_137812_.hasPermission(2))).executes(p_137817_ -> kill((CommandSourceStack) p_137817_.getSource(), ImmutableList.of(((CommandSourceStack) p_137817_.getSource()).getEntityOrException())))).then(Commands.argument("targets", EntityArgument.entities()).executes(p_137810_ -> kill((CommandSourceStack) p_137810_.getSource(), EntityArgument.getEntities(p_137810_, "targets")))));
    }

    private static int kill(CommandSourceStack commandSourceStack0, Collection<? extends Entity> collectionExtendsEntity1) {
        for (Entity $$2 : collectionExtendsEntity1) {
            $$2.kill();
        }
        if (collectionExtendsEntity1.size() == 1) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.kill.success.single", ((Entity) collectionExtendsEntity1.iterator().next()).getDisplayName()), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.kill.success.multiple", collectionExtendsEntity1.size()), true);
        }
        return collectionExtendsEntity1.size();
    }
}