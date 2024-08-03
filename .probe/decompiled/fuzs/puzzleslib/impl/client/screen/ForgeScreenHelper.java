package fuzs.puzzleslib.impl.client.screen;

import fuzs.puzzleslib.api.client.screen.v2.ScreenHelper;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;

public final class ForgeScreenHelper implements ScreenHelper {

    @Override
    public Minecraft getMinecraft(Screen screen) {
        Objects.requireNonNull(screen, "screen is null");
        return screen.getMinecraft();
    }

    @Override
    public Font getFont(Screen screen) {
        Objects.requireNonNull(screen, "screen is null");
        return this.getMinecraft(screen).font;
    }

    @Override
    public int getImageWidth(AbstractContainerScreen<?> screen) {
        Objects.requireNonNull(screen, "screen is null");
        return screen.getXSize();
    }

    @Override
    public int getImageHeight(AbstractContainerScreen<?> screen) {
        Objects.requireNonNull(screen, "screen is null");
        return screen.getYSize();
    }

    @Override
    public int getLeftPos(AbstractContainerScreen<?> screen) {
        Objects.requireNonNull(screen, "screen is null");
        return screen.getGuiLeft();
    }

    @Override
    public int getTopPos(AbstractContainerScreen<?> screen) {
        Objects.requireNonNull(screen, "screen is null");
        return screen.getGuiTop();
    }

    @Nullable
    @Override
    public Slot getHoveredSlot(AbstractContainerScreen<?> screen) {
        Objects.requireNonNull(screen, "screen is null");
        return screen.getSlotUnderMouse();
    }
}