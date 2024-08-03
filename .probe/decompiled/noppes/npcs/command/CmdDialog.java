package noppes.npcs.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Collection;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.SyncController;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogOption;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.entity.EntityDialogNpc;

public class CmdDialog {

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("dialog");
        command.then(((LiteralArgumentBuilder) Commands.literal("reload").requires(source -> source.hasPermission(4))).executes(context -> {
            new DialogController().load();
            SyncController.syncAllDialogs();
            return 1;
        }));
        command.then(((LiteralArgumentBuilder) Commands.literal("read").requires(source -> source.hasPermission(2))).then(Commands.argument("players", EntityArgument.players()).then(Commands.argument("dialog", IntegerArgumentType.integer(0)).executes(context -> {
            Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "players");
            if (players.isEmpty()) {
                return 1;
            } else {
                Dialog dialog = (Dialog) DialogController.instance.dialogs.get(IntegerArgumentType.getInteger(context, "dialog"));
                if (dialog == null) {
                    throw new CommandRuntimeException(Component.literal("Unknown DialogID"));
                } else {
                    for (ServerPlayer player : players) {
                        PlayerData data = PlayerData.get(player);
                        if (!data.dialogData.dialogsRead.contains(dialog.id)) {
                            data.dialogData.dialogsRead.add(dialog.id);
                            data.save(true);
                        }
                    }
                    return 1;
                }
            }
        }))));
        command.then(((LiteralArgumentBuilder) Commands.literal("unread").requires(source -> source.hasPermission(2))).then(Commands.argument("players", EntityArgument.players()).then(Commands.argument("dialog", IntegerArgumentType.integer(0)).executes(context -> {
            Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "players");
            if (players.isEmpty()) {
                return 1;
            } else {
                Dialog dialog = (Dialog) DialogController.instance.dialogs.get(IntegerArgumentType.getInteger(context, "dialog"));
                if (dialog == null) {
                    throw new CommandRuntimeException(Component.literal("Unknown DialogID"));
                } else {
                    for (ServerPlayer player : players) {
                        PlayerData data = PlayerData.get(player);
                        if (data.dialogData.dialogsRead.contains(dialog.id)) {
                            data.dialogData.dialogsRead.remove(dialog.id);
                            data.save(true);
                        }
                    }
                    return 1;
                }
            }
        }))));
        command.then(((LiteralArgumentBuilder) Commands.literal("show").requires(source -> source.hasPermission(2))).then(Commands.argument("players", EntityArgument.players()).then(Commands.argument("dialog", IntegerArgumentType.integer(0)).then(Commands.argument("name", StringArgumentType.string()).executes(context -> {
            Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "players");
            if (players.isEmpty()) {
                return 1;
            } else {
                Dialog dialog = (Dialog) DialogController.instance.dialogs.get(IntegerArgumentType.getInteger(context, "dialog"));
                if (dialog == null) {
                    throw new CommandRuntimeException(Component.literal("Unknown DialogID"));
                } else {
                    EntityDialogNpc npc = new EntityDialogNpc(((CommandSourceStack) context.getSource()).getLevel());
                    DialogOption option = new DialogOption();
                    option.dialogId = dialog.id;
                    option.title = dialog.title;
                    npc.dialogs.put(0, option);
                    npc.display.setName(StringArgumentType.getString(context, "name"));
                    for (ServerPlayer player : players) {
                        EntityUtil.Copy(player, npc);
                        NoppesUtilServer.openDialog(player, npc, dialog);
                    }
                    return 1;
                }
            }
        })))));
        return command;
    }
}