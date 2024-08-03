package noppes.npcs.controllers;

import net.minecraft.world.entity.player.Player;
import noppes.npcs.api.event.CustomGuiEvent;
import noppes.npcs.api.wrapper.WrapperNpcAPI;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.containers.ContainerCustomGui;

public class CustomGuiController {

    static boolean checkGui(CustomGuiEvent event) {
        Player player = event.player.getMCEntity();
        return !(player.containerMenu instanceof ContainerCustomGui) ? false : ((ContainerCustomGui) player.containerMenu).customGui.getID() == event.gui.getID();
    }

    public static void onButton(CustomGuiEvent.ButtonEvent event) {
        Player player = event.player.getMCEntity();
        if (checkGui(event) && getOpenGui(player).getScriptHandler() != null) {
            ((CustomGuiWrapper) event.gui).getScriptHandler().run(EnumScriptType.CUSTOM_GUI_BUTTON, event);
        }
        WrapperNpcAPI.EVENT_BUS.post(event);
    }

    public static void onQuickCraft(CustomGuiEvent.SlotEvent event) {
        Player player = event.player.getMCEntity();
        if (checkGui(event) && getOpenGui(player).getScriptHandler() != null) {
            ((CustomGuiWrapper) event.gui).getScriptHandler().run(EnumScriptType.CUSTOM_GUI_SLOT, event);
        }
        WrapperNpcAPI.EVENT_BUS.post(event);
    }

    public static void onScrollClick(CustomGuiEvent.ScrollEvent event) {
        Player player = event.player.getMCEntity();
        if (checkGui(event) && getOpenGui(player).getScriptHandler() != null) {
            ((CustomGuiWrapper) event.gui).getScriptHandler().run(EnumScriptType.CUSTOM_GUI_SCROLL, event);
        }
        WrapperNpcAPI.EVENT_BUS.post(event);
    }

    public static boolean onSlotClick(CustomGuiEvent.SlotClickEvent event) {
        Player player = event.player.getMCEntity();
        if (checkGui(event) && getOpenGui(player).getScriptHandler() != null) {
            ((CustomGuiWrapper) event.gui).getScriptHandler().run(EnumScriptType.CUSTOM_GUI_SLOT_CLICKED, event);
        }
        return WrapperNpcAPI.EVENT_BUS.post(event);
    }

    public static void onClose(CustomGuiEvent.CloseEvent event) {
        Player player = event.player.getMCEntity();
        if (checkGui(event) && getOpenGui(player).getScriptHandler() != null) {
            ((CustomGuiWrapper) event.gui).getScriptHandler().run(EnumScriptType.CUSTOM_GUI_CLOSED, event);
        }
        WrapperNpcAPI.EVENT_BUS.post(event);
    }

    public static CustomGuiWrapper getOpenGui(Player player) {
        return player.containerMenu instanceof ContainerCustomGui ? ((ContainerCustomGui) player.containerMenu).customGui : null;
    }
}