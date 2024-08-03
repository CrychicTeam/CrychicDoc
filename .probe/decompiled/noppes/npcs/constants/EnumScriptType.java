package noppes.npcs.constants;

public enum EnumScriptType {

    INIT("init"),
    TICK("tick"),
    INTERACT("interact"),
    DIALOG("dialog"),
    DAMAGED("damaged"),
    DIED("died"),
    ATTACK_MELEE("meleeAttack"),
    TARGET("target"),
    COLLIDE("collide"),
    KILL("kill"),
    DIALOG_OPTION("dialogOption"),
    TARGET_LOST("targetLost"),
    ROLE("role"),
    RANGED_LAUNCHED("rangedLaunched"),
    CLICKED("clicked"),
    FALLEN_UPON("fallenUpon"),
    RAIN_FILLED("rainFilled"),
    BROKEN("broken"),
    HARVESTED("harvested"),
    EXPLODED("exploded"),
    NEIGHBOR_CHANGED("neighborChanged"),
    REDSTONE("redstone"),
    DOOR_TOGGLE("doorToggle"),
    TIMER("timer"),
    TOSS("toss"),
    CONTAINER_OPEN("containerOpen"),
    CONTAINER_CLOSED("containerClosed"),
    LOGIN("login"),
    LOGOUT("logout"),
    CHAT("chat"),
    DAMAGED_ENTITY("damagedEntity"),
    DIALOG_CLOSE("dialogClose"),
    SPAWN("spawn"),
    TOSSED("tossed"),
    PICKEDUP("pickedUp"),
    PICKUP("pickUp"),
    ATTACK("attack"),
    PROJECTILE_TICK("projectileTick"),
    PROJECTILE_IMPACT("projectileImpact"),
    FACTION_UPDATE("factionUpdate"),
    LEVEL_UP("levelUp"),
    QUEST_START("questStart"),
    QUEST_COMPLETED("questCompleted"),
    QUEST_TURNIN("questTurnIn"),
    KEY_PRESSED("keyPressed"),
    KEY_RELEASED("keyReleased"),
    SCRIPT_TRIGGER("trigger"),
    PLAY_SOUND("playSound"),
    CUSTOM_GUI_CLOSED("customGuiClosed"),
    CUSTOM_GUI_BUTTON("customGuiButton"),
    CUSTOM_GUI_SLOT("customGuiSlot"),
    CUSTOM_GUI_SCROLL("customGuiScroll"),
    CUSTOM_GUI_SLOT_CLICKED("customGuiSlotClicked");

    public static EnumScriptType[] npcScripts = new EnumScriptType[] { INIT, TICK, INTERACT, DIALOG, DIALOG_OPTION, DIALOG_CLOSE, SCRIPT_TRIGGER, DAMAGED, DIED, ATTACK_MELEE, RANGED_LAUNCHED, TARGET, TARGET_LOST, KILL, ROLE, COLLIDE, TIMER, PROJECTILE_TICK, PROJECTILE_IMPACT, CUSTOM_GUI_SCROLL, CUSTOM_GUI_SLOT, CUSTOM_GUI_CLOSED, CUSTOM_GUI_BUTTON, CUSTOM_GUI_SLOT_CLICKED };

    public static EnumScriptType[] blockScripts = new EnumScriptType[] { INIT, TICK, INTERACT, REDSTONE, FALLEN_UPON, BROKEN, SCRIPT_TRIGGER, EXPLODED, RAIN_FILLED, NEIGHBOR_CHANGED, CLICKED, HARVESTED, COLLIDE, TIMER, CUSTOM_GUI_SCROLL, CUSTOM_GUI_SLOT, CUSTOM_GUI_CLOSED, CUSTOM_GUI_BUTTON, CUSTOM_GUI_SLOT_CLICKED };

    public static EnumScriptType[] doorScripts = new EnumScriptType[] { INIT, TICK, INTERACT, REDSTONE, FALLEN_UPON, BROKEN, SCRIPT_TRIGGER, EXPLODED, RAIN_FILLED, NEIGHBOR_CHANGED, CLICKED, HARVESTED, COLLIDE, TIMER, DOOR_TOGGLE, CUSTOM_GUI_SCROLL, CUSTOM_GUI_SLOT, CUSTOM_GUI_CLOSED, CUSTOM_GUI_BUTTON, CUSTOM_GUI_SLOT_CLICKED };

    public static EnumScriptType[] playerScripts = new EnumScriptType[] { INIT, TICK, INTERACT, BROKEN, PICKUP, CONTAINER_OPEN, CONTAINER_CLOSED, DAMAGED, DIED, ATTACK, KILL, DAMAGED_ENTITY, RANGED_LAUNCHED, TIMER, LOGIN, LOGOUT, CHAT, FACTION_UPDATE, TOSS, DIALOG, DIALOG_OPTION, DIALOG_CLOSE, QUEST_START, QUEST_COMPLETED, QUEST_TURNIN, SCRIPT_TRIGGER, KEY_PRESSED, KEY_RELEASED, PROJECTILE_TICK, PROJECTILE_IMPACT, PLAY_SOUND, CUSTOM_GUI_SCROLL, CUSTOM_GUI_SLOT, CUSTOM_GUI_CLOSED, CUSTOM_GUI_BUTTON, CUSTOM_GUI_SLOT_CLICKED };

    public static EnumScriptType[] itemScripts = new EnumScriptType[] { INIT, TICK, INTERACT, ATTACK, TOSSED, SPAWN, PICKEDUP, CUSTOM_GUI_SCROLL, CUSTOM_GUI_SLOT, CUSTOM_GUI_CLOSED, CUSTOM_GUI_BUTTON, CUSTOM_GUI_SLOT_CLICKED };

    public String function;

    private EnumScriptType(String function) {
        this.function = function;
    }
}