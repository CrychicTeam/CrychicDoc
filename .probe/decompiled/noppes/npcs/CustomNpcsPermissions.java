package noppes.npcs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;
import org.apache.logging.log4j.LogManager;

public class CustomNpcsPermissions {

    public static final PermissionNode<Boolean> NPC_DELETE = new PermissionNode<>("customnpcs", "npc.delete", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> NPC_CREATE = new PermissionNode<>("customnpcs", "npc.create", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> NPC_GUI = new PermissionNode<>("customnpcs", "npc.gui", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> NPC_FREEZE = new PermissionNode<>("customnpcs", "npc.freeze", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> NPC_RESET = new PermissionNode<>("customnpcs", "npc.reset", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> NPC_AI = new PermissionNode<>("customnpcs", "npc.ai", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> NPC_ADVANCED = new PermissionNode<>("customnpcs", "npc.advanced", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> NPC_DISPLAY = new PermissionNode<>("customnpcs", "npc.display", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> NPC_INVENTORY = new PermissionNode<>("customnpcs", "npc.inventory", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> NPC_STATS = new PermissionNode<>("customnpcs", "npc.stats", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> NPC_CLONE = new PermissionNode<>("customnpcs", "npc.clone", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> GLOBAL_LINKED = new PermissionNode<>("customnpcs", "global.linked", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> GLOBAL_PLAYERDATA = new PermissionNode<>("customnpcs", "global.playerdata", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> GLOBAL_BANK = new PermissionNode<>("customnpcs", "global.bank", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> GLOBAL_DIALOG = new PermissionNode<>("customnpcs", "global.dialog", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> GLOBAL_QUEST = new PermissionNode<>("customnpcs", "global.quest", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> GLOBAL_FACTION = new PermissionNode<>("customnpcs", "global.faction", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> GLOBAL_TRANSPORT = new PermissionNode<>("customnpcs", "global.transport", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> GLOBAL_RECIPE = new PermissionNode<>("customnpcs", "global.recipe", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> GLOBAL_NATURALSPAWN = new PermissionNode<>("customnpcs", "global.naturalspawn", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> SPAWNER_MOB = new PermissionNode<>("customnpcs", "spawner.mob", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> SPAWNER_CREATE = new PermissionNode<>("customnpcs", "spawner.create", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> TOOL_MOUNTER = new PermissionNode<>("customnpcs", "tool.mounter", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> TOOL_PATHER = new PermissionNode<>("customnpcs", "tool.pather", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> TOOL_SCRIPTER = new PermissionNode<>("customnpcs", "tool.scripter", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> TOOL_NBTBOOK = new PermissionNode<>("customnpcs", "tool.nbtbook", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> EDIT_VILLAGER = new PermissionNode<>("customnpcs", "edit.villager", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> EDIT_BLOCKS = new PermissionNode<>("customnpcs", "edit.blocks", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    public static final PermissionNode<Boolean> SOULSTONE_ALL = new PermissionNode<>("customnpcs", "soulstone.all", PermissionTypes.BOOLEAN, (player, id, context) -> false);

    public static final PermissionNode<Boolean> SCENES = new PermissionNode<>("customnpcs", "scenes", PermissionTypes.BOOLEAN, (player, id, context) -> true);

    @SubscribeEvent
    public void registerNodes(PermissionGatherEvent.Nodes event) {
        if (!CustomNpcs.DisablePermissions) {
            List<PermissionNode<Boolean>> nodes = Arrays.asList(NPC_DELETE, NPC_CREATE, NPC_GUI, NPC_FREEZE, NPC_RESET, NPC_AI, NPC_ADVANCED, NPC_DISPLAY, NPC_INVENTORY, NPC_STATS, NPC_CLONE, GLOBAL_LINKED, GLOBAL_PLAYERDATA, GLOBAL_BANK, GLOBAL_DIALOG, GLOBAL_QUEST, GLOBAL_FACTION, GLOBAL_TRANSPORT, GLOBAL_RECIPE, GLOBAL_NATURALSPAWN, SPAWNER_MOB, SPAWNER_CREATE, TOOL_MOUNTER, TOOL_PATHER, TOOL_SCRIPTER, TOOL_NBTBOOK, EDIT_VILLAGER, EDIT_BLOCKS, SOULSTONE_ALL, SCENES);
            LogManager.getLogger(CustomNpcs.class).info("CustomNPC PermissionNode<Boolean>s available:");
            Collections.sort(nodes, (o1, o2) -> o1.getNodeName().compareToIgnoreCase(o2.getNodeName()));
            for (PermissionNode<Boolean> p : nodes) {
                event.addNodes(p);
                LogManager.getLogger(CustomNpcs.class).info(p.getNodeName());
            }
        }
    }

    public static boolean hasPermission(ServerPlayer player, PermissionNode<Boolean> permission) {
        if (CustomNpcs.OpsOnly) {
            return player.m_20310_(4);
        } else {
            return CustomNpcs.DisablePermissions ? permission.getDefaultResolver().resolve(player, player.m_20148_()) : PermissionAPI.getPermission(player, permission);
        }
    }
}