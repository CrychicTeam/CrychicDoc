package net.minecraftforge.common;

import com.google.common.collect.Sets;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ToolActions {

    public static final ToolAction AXE_DIG = ToolAction.get("axe_dig");

    public static final ToolAction PICKAXE_DIG = ToolAction.get("pickaxe_dig");

    public static final ToolAction SHOVEL_DIG = ToolAction.get("shovel_dig");

    public static final ToolAction HOE_DIG = ToolAction.get("hoe_dig");

    public static final ToolAction SWORD_DIG = ToolAction.get("sword_dig");

    public static final ToolAction SHEARS_DIG = ToolAction.get("shears_dig");

    public static final ToolAction AXE_STRIP = ToolAction.get("axe_strip");

    public static final ToolAction AXE_SCRAPE = ToolAction.get("axe_scrape");

    public static final ToolAction AXE_WAX_OFF = ToolAction.get("axe_wax_off");

    public static final ToolAction SHOVEL_FLATTEN = ToolAction.get("shovel_flatten");

    public static final ToolAction SWORD_SWEEP = ToolAction.get("sword_sweep");

    public static final ToolAction SHEARS_HARVEST = ToolAction.get("shears_harvest");

    public static final ToolAction SHEARS_CARVE = ToolAction.get("shears_carve");

    public static final ToolAction SHEARS_DISARM = ToolAction.get("shears_disarm");

    public static final ToolAction HOE_TILL = ToolAction.get("till");

    public static final ToolAction SHIELD_BLOCK = ToolAction.get("shield_block");

    public static final ToolAction FISHING_ROD_CAST = ToolAction.get("fishing_rod_cast");

    public static final Set<ToolAction> DEFAULT_AXE_ACTIONS = of(AXE_DIG, AXE_STRIP, AXE_SCRAPE, AXE_WAX_OFF);

    public static final Set<ToolAction> DEFAULT_HOE_ACTIONS = of(HOE_DIG, HOE_TILL);

    public static final Set<ToolAction> DEFAULT_SHOVEL_ACTIONS = of(SHOVEL_DIG, SHOVEL_FLATTEN);

    public static final Set<ToolAction> DEFAULT_PICKAXE_ACTIONS = of(PICKAXE_DIG);

    public static final Set<ToolAction> DEFAULT_SWORD_ACTIONS = of(SWORD_DIG, SWORD_SWEEP);

    public static final Set<ToolAction> DEFAULT_SHEARS_ACTIONS = of(SHEARS_DIG, SHEARS_HARVEST, SHEARS_CARVE, SHEARS_DISARM);

    public static final Set<ToolAction> DEFAULT_SHIELD_ACTIONS = of(SHIELD_BLOCK);

    public static final Set<ToolAction> DEFAULT_FISHING_ROD_ACTIONS = of(FISHING_ROD_CAST);

    private static Set<ToolAction> of(ToolAction... actions) {
        return (Set<ToolAction>) Stream.of(actions).collect(Collectors.toCollection(Sets::newIdentityHashSet));
    }
}