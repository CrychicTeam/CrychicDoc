package noppes.npcs.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import noppes.npcs.CustomNpcs;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.client.gui.player.GuiQuestLog;
import noppes.npcs.client.renderer.RenderNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketPlayerKeyPressed;
import noppes.npcs.packets.server.SPacketPlayerLeftClicked;
import noppes.npcs.packets.server.SPacketQuestCompletionCheckAll;
import noppes.npcs.packets.server.SPacketSceneReset;
import noppes.npcs.packets.server.SPacketSceneStart;

public class ClientTickHandler {

    private Level prevLevel;

    private boolean otherContainer = false;

    private final int[] ignoreKeys = new int[] { 341, 340, 342, 343, 345, 344, 346, 347 };

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || !(mc.player.f_36096_ instanceof InventoryMenu)) {
                this.otherContainer = true;
            } else if (this.otherContainer) {
                Packets.sendServer(new SPacketQuestCompletionCheckAll());
                this.otherContainer = false;
            }
            CustomNpcs.ticks++;
            RenderNPCInterface.LastTextureTick++;
            if (this.prevLevel != mc.level) {
                this.prevLevel = mc.level;
                MusicController.Instance.stopMusic();
            }
        }
    }

    @SubscribeEvent
    public void onKey(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc != null && mc.level != null && mc.getConnection() != null) {
            if (CustomNpcs.SceneButtonsEnabled) {
                if (ClientProxy.Scene1.isDown()) {
                    Packets.sendServer(new SPacketSceneStart(1));
                }
                if (ClientProxy.Scene2.isDown()) {
                    Packets.sendServer(new SPacketSceneStart(2));
                }
                if (ClientProxy.Scene3.isDown()) {
                    Packets.sendServer(new SPacketSceneStart(3));
                }
                if (ClientProxy.SceneReset.isDown()) {
                    Packets.sendServer(new SPacketSceneReset());
                }
            }
            if (ClientProxy.QuestLog.isDown()) {
                if (mc.screen == null) {
                    NoppesUtil.openGUI(mc.player, new GuiQuestLog(mc.player));
                } else if (mc.screen instanceof GuiQuestLog) {
                    mc.mouseHandler.grabMouse();
                }
            }
            if (event.getAction() == 1 || event.getAction() == 0) {
                boolean isCtrlPressed = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 341) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 345);
                boolean isShiftPressed = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 344);
                boolean isAltPressed = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 342) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 346);
                boolean isMetaPressed = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 343) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 347);
                String openGui = mc.screen == null ? "" : mc.screen.getClass().getName();
                Packets.sendServer(new SPacketPlayerKeyPressed(event.getKey(), isCtrlPressed, isShiftPressed, isAltPressed, isMetaPressed, event.getAction() == 0, openGui));
            }
        }
    }

    @SubscribeEvent
    public void invoke(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getHand() == InteractionHand.MAIN_HAND) {
            Packets.sendServer(new SPacketPlayerLeftClicked());
        }
    }

    private boolean isIgnoredKey(int key) {
        for (int i : this.ignoreKeys) {
            if (i == key) {
                return true;
            }
        }
        return false;
    }
}