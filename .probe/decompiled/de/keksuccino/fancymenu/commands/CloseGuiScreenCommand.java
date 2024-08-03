package de.keksuccino.fancymenu.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import de.keksuccino.fancymenu.networking.PacketHandler;
import de.keksuccino.fancymenu.networking.packets.commands.closegui.CloseGuiCommandPacket;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CloseGuiScreenCommand {

    public static void register(CommandDispatcher<CommandSourceStack> d) {
        d.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) net.minecraft.commands.Commands.literal("closeguiscreen").executes(stack -> closeGui((CommandSourceStack) stack.getSource(), null))).then(((RequiredArgumentBuilder) net.minecraft.commands.Commands.argument("target_players", EntityArgument.players()).requires(stack -> stack.hasPermission(2))).executes(stack -> closeGui((CommandSourceStack) stack.getSource(), EntityArgument.getPlayers(stack, "target_players")))));
    }

    private static int closeGui(CommandSourceStack stack, @Nullable Collection<ServerPlayer> targets) {
        try {
            if (targets == null) {
                ServerPlayer sender = stack.getPlayerOrException();
                CloseGuiCommandPacket packet = new CloseGuiCommandPacket();
                PacketHandler.sendToClient(sender, packet);
            } else {
                for (ServerPlayer target : targets) {
                    CloseGuiCommandPacket packet = new CloseGuiCommandPacket();
                    PacketHandler.sendToClient(target, packet);
                }
            }
        } catch (Exception var5) {
            stack.sendFailure(Component.literal("Error while executing command!"));
            var5.printStackTrace();
        }
        return 1;
    }
}