package fuzs.puzzleslib.api.client.screen.v2;

import fuzs.puzzleslib.impl.client.core.ClientFactories;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;

public interface ScreenHelper {

    ScreenHelper INSTANCE = ClientFactories.INSTANCE.getScreenHelper();

    Minecraft getMinecraft(Screen var1);

    Font getFont(Screen var1);

    int getImageWidth(AbstractContainerScreen<?> var1);

    int getImageHeight(AbstractContainerScreen<?> var1);

    int getLeftPos(AbstractContainerScreen<?> var1);

    int getTopPos(AbstractContainerScreen<?> var1);

    @Nullable
    Slot getHoveredSlot(AbstractContainerScreen<?> var1);

    default int getMouseX(Screen screen) {
        Objects.requireNonNull(screen, "screen is null");
        return this.getMouseX(this.getMinecraft(screen));
    }

    default int getMouseX(Minecraft minecraft) {
        Objects.requireNonNull(minecraft, "minecraft is null");
        return (int) (minecraft.mouseHandler.xpos() * (double) minecraft.getWindow().getGuiScaledWidth() / (double) minecraft.getWindow().getScreenWidth());
    }

    default int getMouseY(Screen screen) {
        Objects.requireNonNull(screen, "screen is null");
        return this.getMouseY(this.getMinecraft(screen));
    }

    default int getMouseY(Minecraft minecraft) {
        Objects.requireNonNull(minecraft, "minecraft is null");
        return (int) (minecraft.mouseHandler.ypos() * (double) minecraft.getWindow().getGuiScaledHeight() / (double) minecraft.getWindow().getScreenHeight());
    }

    @Nullable
    default Slot findSlot(AbstractContainerScreen<?> screen, double mouseX, double mouseY) {
        for (Slot slot : screen.getMenu().slots) {
            if (slot.isActive() && this.isHovering(screen, slot, mouseX, mouseY)) {
                return slot;
            }
        }
        return null;
    }

    default boolean isHovering(AbstractContainerScreen<?> screen, Slot slot, double mouseX, double mouseY) {
        mouseX -= (double) this.getLeftPos(screen);
        mouseY -= (double) this.getTopPos(screen);
        return this.isHovering(slot.x - 1, slot.y - 1, 18, 18, mouseX, mouseY);
    }

    default boolean isHovering(int posX, int posY, int width, int height, double mouseX, double mouseY) {
        return mouseX >= (double) posX && mouseX < (double) (posX + width) && mouseY >= (double) posY && mouseY < (double) (posY + height);
    }
}