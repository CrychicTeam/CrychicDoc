package noppes.npcs.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import noppes.npcs.CustomEntities;
import noppes.npcs.CustomNpcs;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.roles.RoleFollower;

public class CmdNPC {

    public static final SuggestionProvider<CommandSourceStack> VISIBLE = SuggestionProviders.register(new ResourceLocation("visible"), (context, builder) -> SharedSuggestionProvider.suggest(new String[] { "true", "false", "semi" }, builder));

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) Commands.literal("npc").requires(source -> source.hasPermission(CustomNpcs.NoppesCommandOpOnly ? 4 : 2))).then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("npc", StringArgumentType.string()).then(Commands.literal("home").then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(context -> {
            String name = StringArgumentType.getString(context, "npc");
            List<EntityNPCInterface> npcs = CmdNoppes.getNpcsByName(((CommandSourceStack) context.getSource()).getLevel(), name);
            if (!npcs.isEmpty()) {
                ((EntityNPCInterface) npcs.get(0)).ais.setStartPos(BlockPosArgument.getLoadedBlockPos(context, "pos"));
            }
            return 1;
        })))).then(Commands.literal("visible").then(Commands.argument("visibility", StringArgumentType.word()).suggests(VISIBLE).executes(context -> {
            String name = StringArgumentType.getString(context, "npc");
            List<EntityNPCInterface> npcs = CmdNoppes.getNpcsByName(((CommandSourceStack) context.getSource()).getLevel(), name);
            String val = StringArgumentType.getString(context, "visibility");
            int vis = 0;
            if (val.equalsIgnoreCase("false")) {
                vis = 1;
            } else if (val.equalsIgnoreCase("semi")) {
                vis = 2;
            }
            for (EntityNPCInterface npc : npcs) {
                npc.display.setVisible(vis);
            }
            return 1;
        })))).then(Commands.literal("delete").executes(context -> {
            String name = StringArgumentType.getString(context, "npc");
            for (EntityNPCInterface npc : CmdNoppes.getNpcsByName(((CommandSourceStack) context.getSource()).getLevel(), name)) {
                npc.delete();
            }
            return 1;
        }))).then(((LiteralArgumentBuilder) Commands.literal("owner").executes(context -> {
            String name = StringArgumentType.getString(context, "npc");
            for (EntityNPCInterface npc : CmdNoppes.getNpcsByName(((CommandSourceStack) context.getSource()).getLevel(), name)) {
                LivingEntity owner = npc.getOwner();
                if (owner == null) {
                    ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.literal("No owner"), false);
                } else {
                    ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.literal("Owner is: " + owner.m_7755_()), false);
                }
            }
            return 1;
        })).then(Commands.argument("player", EntityArgument.player()).executes(context -> {
            String name = StringArgumentType.getString(context, "npc");
            List<EntityNPCInterface> npcs = CmdNoppes.getNpcsByName(((CommandSourceStack) context.getSource()).getLevel(), name);
            Player player = EntityArgument.getPlayer(context, "player");
            for (EntityNPCInterface npc : npcs) {
                if (npc.role instanceof RoleFollower) {
                    ((RoleFollower) npc.role).setOwner(player);
                }
                if (npc.role instanceof RoleCompanion) {
                    ((RoleCompanion) npc.role).setOwner(player);
                }
            }
            return 1;
        })))).then(Commands.literal("delete").then(Commands.argument("name", StringArgumentType.greedyString()).executes(context -> {
            List<EntityNPCInterface> npcs = CmdNoppes.getNpcsByName(((CommandSourceStack) context.getSource()).getLevel(), StringArgumentType.getString(context, "npc"));
            String name = StringArgumentType.getString(context, "name");
            for (EntityNPCInterface npc : npcs) {
                npc.display.setName(name);
                npc.updateClient = true;
            }
            return 1;
        })))).then(Commands.literal("reset").executes(context -> {
            String name = StringArgumentType.getString(context, "npc");
            for (EntityNPCInterface npc : CmdNoppes.getNpcsByName(((CommandSourceStack) context.getSource()).getLevel(), name)) {
                npc.reset();
            }
            return 1;
        }))).then(Commands.literal("create").executes(context -> {
            String name = StringArgumentType.getString(context, "npc");
            Level pw = ((CommandSourceStack) context.getSource()).getLevel();
            EntityCustomNpc npc = new EntityCustomNpc(CustomEntities.entityCustomNpc, pw);
            npc.display.setName(name);
            Vec3 pos = ((CommandSourceStack) context.getSource()).getPosition();
            npc.m_19890_(pos.x, pos.y, pos.z, 0.0F, 0.0F);
            npc.ais.setStartPos(new BlockPos((int) pos.x, (int) pos.y, (int) pos.z));
            pw.m_7967_(npc);
            npc.m_21153_(npc.m_21233_());
            return 1;
        })));
    }
}