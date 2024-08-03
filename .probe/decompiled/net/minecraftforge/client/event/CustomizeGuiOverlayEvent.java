package net.minecraftforge.client.event;

import com.mojang.blaze3d.platform.Window;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class CustomizeGuiOverlayEvent extends Event {

    private final Window window;

    private final GuiGraphics guiGraphics;

    private final float partialTick;

    @Internal
    protected CustomizeGuiOverlayEvent(Window window, GuiGraphics guiGraphics, float partialTick) {
        this.window = window;
        this.guiGraphics = guiGraphics;
        this.partialTick = partialTick;
    }

    public Window getWindow() {
        return this.window;
    }

    public GuiGraphics getGuiGraphics() {
        return this.guiGraphics;
    }

    public float getPartialTick() {
        return this.partialTick;
    }

    @Cancelable
    public static class BossEventProgress extends CustomizeGuiOverlayEvent {

        private final LerpingBossEvent bossEvent;

        private final int x;

        private final int y;

        private int increment;

        @Internal
        public BossEventProgress(Window window, GuiGraphics guiGraphics, float partialTick, LerpingBossEvent bossEvent, int x, int y, int increment) {
            super(window, guiGraphics, partialTick);
            this.bossEvent = bossEvent;
            this.x = x;
            this.y = y;
            this.increment = increment;
        }

        public LerpingBossEvent getBossEvent() {
            return this.bossEvent;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public int getIncrement() {
            return this.increment;
        }

        public void setIncrement(int increment) {
            this.increment = increment;
        }
    }

    public static class Chat extends CustomizeGuiOverlayEvent {

        private int posX;

        private int posY;

        @Internal
        public Chat(Window window, GuiGraphics guiGraphics, float partialTick, int posX, int posY) {
            super(window, guiGraphics, partialTick);
            this.setPosX(posX);
            this.setPosY(posY);
        }

        public int getPosX() {
            return this.posX;
        }

        public void setPosX(int posX) {
            this.posX = posX;
        }

        public int getPosY() {
            return this.posY;
        }

        public void setPosY(int posY) {
            this.posY = posY;
        }
    }

    public static class DebugText extends CustomizeGuiOverlayEvent {

        private final ArrayList<String> left;

        private final ArrayList<String> right;

        @Internal
        public DebugText(Window window, GuiGraphics guiGraphics, float partialTick, ArrayList<String> left, ArrayList<String> right) {
            super(window, guiGraphics, partialTick);
            this.left = left;
            this.right = right;
        }

        public ArrayList<String> getLeft() {
            return this.left;
        }

        public ArrayList<String> getRight() {
            return this.right;
        }
    }
}