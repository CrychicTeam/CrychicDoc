package vazkii.patchouli.api;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;

public class BookDrawScreenEvent extends Event {

    private final ResourceLocation book;

    private final Screen screen;

    private final int mouseX;

    private final int mouseY;

    private final float partialTicks;

    private final GuiGraphics graphics;

    public BookDrawScreenEvent(ResourceLocation book, Screen screen, int mouseX, int mouseY, float partialTicks, GuiGraphics graphics) {
        this.book = book;
        this.screen = screen;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.partialTicks = partialTicks;
        this.graphics = graphics;
    }

    public ResourceLocation getBook() {
        return this.book;
    }

    public Screen getScreen() {
        return this.screen;
    }

    public int getMouseX() {
        return this.mouseX;
    }

    public int getMouseY() {
        return this.mouseY;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public GuiGraphics getGraphics() {
        return this.graphics;
    }
}