package dev.xkmc.l2backpack.init.advancement;

import net.minecraft.resources.ResourceLocation;

public class BackpackTriggers {

    public static final SlotClickTrigger SLOT_CLICK = new SlotClickTrigger(new ResourceLocation("l2backpack", "slot_click"));

    public static final BagInteractTrigger INTERACT = new BagInteractTrigger(new ResourceLocation("l2backpack", "bag_interact"));

    public static final DrawerInteractTrigger DRAWER = new DrawerInteractTrigger(new ResourceLocation("l2backpack", "drawer_interact"));

    public static final RemoteHopperTrigger REMOTE = new RemoteHopperTrigger(new ResourceLocation("l2backpack", "remote_hopper"));

    public static final AnalogSignalTrigger ANALOG = new AnalogSignalTrigger(new ResourceLocation("l2backpack", "analog_signal"));

    public static final SharedDriveTrigger SHARE = new SharedDriveTrigger(new ResourceLocation("l2backpack", "shared_drive"));

    public static void register() {
    }
}