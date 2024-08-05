package top.theillusivec4.curios.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.util.Tuple;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.client.CuriosClientConfig;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.client.CPacketDestroy;

public class GuiEventHandler {

    @SubscribeEvent
    public void onInventoryGuiInit(ScreenEvent.Init.Post evt) {
        Screen screen = evt.getScreen();
        if (CuriosClientConfig.CLIENT.enableButton.get()) {
            if (screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen) {
                AbstractContainerScreen<?> gui = (AbstractContainerScreen<?>) screen;
                boolean isCreative = screen instanceof CreativeModeInventoryScreen;
                Tuple<Integer, Integer> offsets = CuriosScreen.getButtonOffset(isCreative);
                int x = offsets.getA();
                int y = offsets.getB();
                int size = isCreative ? 10 : 14;
                int textureOffsetX = isCreative ? 64 : 50;
                int yOffset = isCreative ? 68 : 83;
                evt.addListener(new CuriosButton(gui, gui.getGuiLeft() + x, gui.getGuiTop() + y + yOffset, size, size, textureOffsetX, 0, size, CuriosScreen.CURIO_INVENTORY));
            }
        }
    }

    @SubscribeEvent
    public void onInventoryGuiDrawBackground(ScreenEvent.Render.Pre evt) {
        if (evt.getScreen() instanceof InventoryScreen gui) {
            gui.xMouse = (float) evt.getMouseX();
            gui.yMouse = (float) evt.getMouseY();
        }
    }

    @SubscribeEvent
    public void onMouseClick(ScreenEvent.MouseButtonPressed.Pre evt) {
        long handle = Minecraft.getInstance().getWindow().getWindow();
        boolean isLeftShiftDown = InputConstants.isKeyDown(handle, 340);
        boolean isRightShiftDown = InputConstants.isKeyDown(handle, 344);
        boolean isShiftDown = isLeftShiftDown || isRightShiftDown;
        if (!(evt.getScreen() instanceof CreativeModeInventoryScreen gui) || !isShiftDown) {
            return;
        }
        if (gui.isInventoryOpen()) {
            Slot destroyItemSlot = gui.destroyItemSlot;
            Slot slot = gui.m_97744_(evt.getMouseX(), evt.getMouseY());
            if (destroyItemSlot != null && slot == destroyItemSlot) {
                NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CPacketDestroy());
            }
        }
    }
}