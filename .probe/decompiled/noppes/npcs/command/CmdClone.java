package noppes.npcs.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import java.util.List;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.entity.EntityNPCInterface;

public class CmdClone {

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("clone");
        command.then(((LiteralArgumentBuilder) Commands.literal("list").requires(source -> source.hasPermission(2))).then(Commands.argument("tab", IntegerArgumentType.integer(0)).executes(context -> {
            int tab = IntegerArgumentType.getInteger(context, "tab");
            ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.literal("--- Stored NPCs --- (server side)"), false);
            for (String name : ServerCloneController.Instance.getClones(tab)) {
                ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.literal(name), false);
            }
            ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.literal("------------------------------------"), false);
            return 1;
        })));
        command.then(((LiteralArgumentBuilder) Commands.literal("add").requires(source -> source.hasPermission(4))).then(Commands.argument("npc", StringArgumentType.string()).then(((RequiredArgumentBuilder) Commands.argument("tab", IntegerArgumentType.integer(0)).executes(context -> {
            addClone(context, "");
            return 1;
        })).then(Commands.argument("name", StringArgumentType.string()).executes(context -> {
            addClone(context, StringArgumentType.getString(context, "name"));
            return 1;
        })))));
        command.then(((LiteralArgumentBuilder) Commands.literal("remove").requires(source -> source.hasPermission(4))).then(Commands.argument("npc", StringArgumentType.string()).then(Commands.argument("tab", IntegerArgumentType.integer(0)).executes(context -> {
            String nametodel = StringArgumentType.getString(context, "npc");
            int tab = IntegerArgumentType.getInteger(context, "tab");
            boolean deleted = false;
            for (String name : ServerCloneController.Instance.getClones(tab)) {
                if (nametodel.equalsIgnoreCase(name)) {
                    ServerCloneController.Instance.removeClone(name, tab);
                    deleted = true;
                    break;
                }
            }
            if (!deleted) {
                throw new CommandRuntimeException(Component.translatable("Npc '%s' wasn't found", nametodel));
            } else {
                return 1;
            }
        }))));
        command.then(((LiteralArgumentBuilder) Commands.literal("spawn").requires(source -> source.hasPermission(2))).then(Commands.argument("npc", StringArgumentType.string()).then(((RequiredArgumentBuilder) Commands.argument("tab", IntegerArgumentType.integer(0)).executes(context -> {
            spawnClone(context, new BlockPos((int) ((CommandSourceStack) context.getSource()).getPosition().x, (int) ((CommandSourceStack) context.getSource()).getPosition().y, (int) ((CommandSourceStack) context.getSource()).getPosition().z), "");
            return 1;
        })).then(((RequiredArgumentBuilder) Commands.argument("pos", BlockPosArgument.blockPos()).executes(context -> {
            spawnClone(context, BlockPosArgument.getLoadedBlockPos(context, "pos"), "");
            return 1;
        })).then(Commands.argument("display_name", StringArgumentType.string()).executes(context -> {
            spawnClone(context, BlockPosArgument.getLoadedBlockPos(context, "pos"), StringArgumentType.getString(context, "display_name"));
            return 1;
        }))))));
        command.then(((LiteralArgumentBuilder) Commands.literal("grid").requires(source -> source.hasPermission(2))).then(Commands.argument("npc", StringArgumentType.string()).then(Commands.argument("tab", IntegerArgumentType.integer(0)).then(Commands.argument("length", IntegerArgumentType.integer()).then(((RequiredArgumentBuilder) Commands.argument("width", IntegerArgumentType.integer()).executes(context -> {
            int length = IntegerArgumentType.getInteger(context, "length");
            int width = IntegerArgumentType.getInteger(context, "width");
            for (int x = 0; x < length; x++) {
                for (int z = 0; z < width; z++) {
                    spawnClone(context, new BlockPos((int) ((CommandSourceStack) context.getSource()).getPosition().x, (int) ((CommandSourceStack) context.getSource()).getPosition().y, (int) ((CommandSourceStack) context.getSource()).getPosition().z).offset(length, 0, width), "");
                }
            }
            return 1;
        })).then(((RequiredArgumentBuilder) Commands.argument("pos", BlockPosArgument.blockPos()).executes(context -> {
            int length = IntegerArgumentType.getInteger(context, "length");
            int width = IntegerArgumentType.getInteger(context, "width");
            for (int x = 0; x < length; x++) {
                for (int z = 0; z < width; z++) {
                    spawnClone(context, BlockPosArgument.getLoadedBlockPos(context, "pos").offset(length, 0, width), "");
                }
            }
            return 1;
        })).then(Commands.argument("display_name", StringArgumentType.string()).executes(context -> {
            int length = IntegerArgumentType.getInteger(context, "length");
            int width = IntegerArgumentType.getInteger(context, "width");
            for (int x = 0; x < length; x++) {
                for (int z = 0; z < width; z++) {
                    spawnClone(context, BlockPosArgument.getLoadedBlockPos(context, "pos").offset(length, 0, width), StringArgumentType.getString(context, "display_name"));
                }
            }
            return 1;
        }))))))));
        return command;
    }

    private static void addClone(CommandContext<CommandSourceStack> context, String newName) {
        String name = StringArgumentType.getString(context, "npc");
        if (newName.isEmpty()) {
            newName = name;
        }
        int tab = IntegerArgumentType.getInteger(context, "tab");
        List<EntityNPCInterface> list = CmdNoppes.getNpcsByName(((CommandSourceStack) context.getSource()).getLevel(), name);
        if (!list.isEmpty()) {
            EntityNPCInterface npc = (EntityNPCInterface) list.get(0);
            CompoundTag compound = new CompoundTag();
            if (npc.m_20086_(compound)) {
                ServerCloneController.Instance.addClone(compound, newName, tab);
            }
        }
    }

    private static void spawnClone(CommandContext<CommandSourceStack> context, BlockPos pos, String newName) {
        String name = StringArgumentType.getString(context, "npc").replaceAll("%", " ");
        int tab = IntegerArgumentType.getInteger(context, "tab");
        CompoundTag compound = ServerCloneController.Instance.getCloneData((CommandSourceStack) context.getSource(), name, tab);
        if (compound == null) {
            throw new CommandRuntimeException(Component.literal("Unknown npc"));
        } else if (pos == BlockPos.ZERO) {
            throw new CommandRuntimeException(Component.literal("Location needed"));
        } else {
            Level world = ((CommandSourceStack) context.getSource()).getLevel();
            Entity entity = (Entity) EntityType.create(compound, world).get();
            entity.setPos((double) pos.m_123341_() + 0.5, (double) (pos.m_123342_() + 1), (double) pos.m_123343_() + 0.5);
            if (entity instanceof EntityNPCInterface npc) {
                npc.ais.setStartPos(pos);
                if (!newName.isEmpty()) {
                    npc.display.setName(newName.replaceAll("%", " "));
                }
            }
            world.m_7967_(entity);
        }
    }
}