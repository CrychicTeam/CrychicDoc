package noppes.npcs.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import noppes.npcs.CustomEntities;
import noppes.npcs.entity.EntityNPCInterface;

public class CmdNoppes {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("noppes").requires(p_198816_0_ -> p_198816_0_.hasPermission(2))).then(CmdClone.register())).then(CmdConfig.register())).then(CmdDialog.register())).then(CmdFaction.register())).then(CmdMark.register())).then(CmdNPC.register())).then(CmdQuest.register())).then(CmdScene.register())).then(CmdSchematics.register())).then(CmdScript.register())).then(CmdSlay.register()));
    }

    public static List<EntityNPCInterface> getNpcsByName(ServerLevel level, String name) {
        return level.getEntities(CustomEntities.entityCustomNpc, npc -> npc.display.getName().equalsIgnoreCase(name));
    }

    public static <T extends Entity> List<T> getEntities(EntityType<T> type, ServerLevel level) {
        return (List<T>) level.getEntities(type, entity -> true);
    }
}