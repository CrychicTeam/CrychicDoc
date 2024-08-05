package lio.playeranimatorapi.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import lio.playeranimatorapi.ModInit;
import lio.playeranimatorapi.API.PlayerAnimAPI;
import lio.playeranimatorapi.data.PlayerAnimationData;
import lio.playeranimatorapi.data.PlayerParts;
import lio.playeranimatorapi.modifier.CommonModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayPlayerAnimationCommand {

    private static final Logger logger = LogManager.getLogger(ModInit.class);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("playPlayerAnimation").requires(commandSourceStack -> commandSourceStack.hasPermission(2))).then(Commands.argument("player", EntityArgument.players()).then(((RequiredArgumentBuilder) Commands.argument("animationID", ResourceLocationArgument.id()).executes(context -> execute(context, CommandState.Minimal))).then(Commands.argument("fadeLength", IntegerArgumentType.integer()).then(Commands.argument("easeID", IntegerArgumentType.integer()).then(Commands.argument("firstPersonEnabled", BoolArgumentType.bool()).then(((RequiredArgumentBuilder) Commands.argument("important", BoolArgumentType.bool()).executes(context -> execute(context, CommandState.Advanced))).then(Commands.argument("playerParts", StringArgumentType.string()).then(Commands.argument("modifiers", StringArgumentType.string()).executes(context -> execute(context, CommandState.Complete)))))))))));
    }

    private static int execute(CommandContext<CommandSourceStack> command, CommandState state) {
        try {
            switch(state) {
                case Minimal:
                    for (ServerPlayer player : EntityArgument.getPlayers(command, "player")) {
                        PlayerAnimAPI.playPlayerAnim((ServerLevel) player.m_9236_(), player, ResourceLocationArgument.getId(command, "animationID"));
                    }
                    break;
                case Advanced:
                    for (ServerPlayer player : EntityArgument.getPlayers(command, "player")) {
                        PlayerAnimationData data = new PlayerAnimationData(player.m_20148_(), ResourceLocationArgument.getId(command, "animationID"), null, null, IntegerArgumentType.getInteger(command, "fadeLength"), IntegerArgumentType.getInteger(command, "easeID"), BoolArgumentType.getBool(command, "firstPersonEnabled"), BoolArgumentType.getBool(command, "important"));
                        PlayerAnimAPI.playPlayerAnim(((CommandSourceStack) command.getSource()).getLevel(), player, data);
                    }
                    break;
                case Complete:
                    for (ServerPlayer player : EntityArgument.getPlayers(command, "player")) {
                        PlayerAnimationData data = new PlayerAnimationData(player.m_20148_(), ResourceLocationArgument.getId(command, "animationID"), PlayerParts.fromBigInteger(playerPartsIntFromString(StringArgumentType.getString(command, "playerParts"))), modifierList(StringArgumentType.getString(command, "modifiers")), IntegerArgumentType.getInteger(command, "fadeLength"), IntegerArgumentType.getInteger(command, "easeID"), BoolArgumentType.getBool(command, "firstPersonEnabled"), BoolArgumentType.getBool(command, "important"));
                        PlayerAnimAPI.playPlayerAnim(((CommandSourceStack) command.getSource()).getLevel(), player, data);
                    }
            }
        } catch (CommandSyntaxException var5) {
            Player player = ((CommandSourceStack) command.getSource()).getPlayer();
            if (player != null) {
                player.m_213846_(Component.literal(var5.getMessage()).withStyle(ChatFormatting.RED));
            }
        }
        return 1;
    }

    public static List<CommonModifier> modifierList(String input) {
        List<CommonModifier> list = new ArrayList();
        for (String str : input.split(",")) {
            list.add(new CommonModifier(new ResourceLocation(str), null));
        }
        return list;
    }

    public static BigInteger playerPartsIntFromString(String string) {
        try {
            return new BigInteger(string, 36);
        } catch (NumberFormatException var2) {
            return new BigInteger("axq5j8k4e1uiyz27", 36);
        }
    }
}