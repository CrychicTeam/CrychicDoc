package de.keksuccino.konkrete.gui.content.scrollarea;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.konkrete.input.MouseInput;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ScrollArea {

    public Color backgroundColor = new Color(0, 0, 0, 240);

    public Color grabberColorNormal = Color.LIGHT_GRAY;

    public Color grabberColorHover = Color.GRAY;

    public ResourceLocation grabberTextureNormal = null;

    public ResourceLocation grabberTextureHover = null;

    public int x;

    public int y;

    public int width;

    public int height;

    public int grabberheight = 20;

    public int grabberwidth = 10;

    public boolean enableScrolling = true;

    private List<ScrollAreaEntry> entries = new ArrayList();

    private boolean grabberHovered = false;

    private boolean grabberPressed = false;

    private int scrollpos = 0;

    private int entryheight = 0;

    private int startY = 0;

    private int startPos = 0;

    public ScrollArea(int x, int y, int width, int height) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void render(GuiGraphics graphics) {
        RenderSystem.enableBlend();
        this.renderBackground(graphics);
        this.renderScrollbar(graphics);
        Window win = Minecraft.getInstance().getWindow();
        double scale = win.getGuiScale();
        int sciBottom = this.height + this.y;
        RenderSystem.enableScissor((int) ((double) this.x * scale), (int) ((double) win.getHeight() - (double) sciBottom * scale), (int) ((double) this.width * scale), (int) ((double) this.height * scale));
        int i = 0;
        for (ScrollAreaEntry e : this.entries) {
            int i2 = this.height - this.grabberheight;
            if (i2 == 0) {
                i2 = 1;
            }
            int scroll = this.scrollpos * (this.entryheight / i2);
            e.x = this.x;
            e.y = this.y + i - scroll;
            e.render(graphics);
            i += e.getHeight();
        }
        RenderSystem.disableScissor();
    }

    protected void renderScrollbar(GuiGraphics graphics) {
        if (this.height < this.entryheight) {
            int mouseX = MouseInput.getMouseX();
            int mouseY = MouseInput.getMouseY();
            if (this.x + this.width <= mouseX && this.x + this.width + this.grabberwidth >= mouseX && this.y + this.scrollpos <= mouseY && this.y + this.scrollpos + this.grabberheight >= mouseY) {
                this.grabberHovered = true;
            } else {
                this.grabberHovered = false;
            }
            if (this.isGrabberHovered() && MouseInput.isLeftMouseDown()) {
                this.grabberPressed = true;
            }
            if (!MouseInput.isLeftMouseDown()) {
                this.grabberPressed = false;
            }
            if (this.enableScrolling) {
                RenderSystem.enableBlend();
                int scrollXStart = this.x + this.width;
                int scrollYStart = this.y + this.scrollpos;
                int scrollXEnd = this.x + this.width + this.grabberwidth;
                int scrollYEnd = this.y + this.scrollpos + this.grabberheight;
                if (!this.isGrabberHovered()) {
                    if (this.grabberTextureNormal == null) {
                        graphics.fill(scrollXStart, scrollYStart, scrollXEnd, scrollYEnd, this.grabberColorNormal.getRGB());
                    } else {
                        graphics.blit(this.grabberTextureNormal, scrollXStart, scrollYStart, 0.0F, 0.0F, this.grabberwidth, this.grabberheight, this.grabberwidth, this.grabberheight);
                    }
                } else if (this.grabberTextureHover == null) {
                    graphics.fill(scrollXStart, scrollYStart, scrollXEnd, scrollYEnd, this.grabberColorHover.getRGB());
                } else {
                    graphics.blit(this.grabberTextureHover, scrollXStart, scrollYStart, 0.0F, 0.0F, this.grabberwidth, this.grabberheight, this.grabberwidth, this.grabberheight);
                }
            }
            if (this.enableScrolling) {
                if (this.isGrabberPressed()) {
                    this.handleGrabberScrolling();
                } else {
                    this.startY = MouseInput.getMouseY();
                    this.startPos = this.scrollpos;
                }
            } else {
                this.scrollpos = 0;
            }
        }
    }

    public boolean isAreaHovered() {
        int mouseX = MouseInput.getMouseX();
        int mouseY = MouseInput.getMouseY();
        return this.x <= mouseX && this.x + this.width + this.grabberwidth >= mouseX && this.y <= mouseY && this.y + this.height >= mouseY;
    }

    protected void handleGrabberScrolling() {
        int i = this.startY - MouseInput.getMouseY();
        int scroll = this.startPos - i;
        if (scroll < 0) {
            this.scrollpos = 0;
        } else if (scroll > this.height - this.grabberheight) {
            this.scrollpos = this.height - this.grabberheight;
        } else {
            this.scrollpos = scroll;
        }
    }

    protected void renderBackground(GuiGraphics graphics) {
        graphics.fill(this.x, this.y, this.x + this.width, this.y + this.height, this.backgroundColor.getRGB());
    }

    public void addEntry(ScrollAreaEntry e) {
        this.entries.add(e);
        this.scrollpos = 0;
        this.entryheight = this.entryheight + e.getHeight();
    }

    public void removeEntry(ScrollAreaEntry e) {
        if (this.entries.contains(e)) {
            this.entries.remove(e);
            this.scrollpos = 0;
            this.entryheight = this.entryheight - e.getHeight();
        }
    }

    public List<ScrollAreaEntry> getEntries() {
        return this.entries;
    }

    public int getStackedEntryHeight() {
        return this.entryheight;
    }

    public boolean isGrabberHovered() {
        return this.grabberHovered;
    }

    public boolean isGrabberPressed() {
        return this.grabberPressed;
    }

    @SubscribeEvent
    public void onMouseScrollPre(ScreenEvent.MouseScrolled.Pre e) {
        if (this.isAreaHovered()) {
            int scroll = this.scrollpos - (int) e.getScrollDelta() * 7;
            if (scroll < 0) {
                this.scrollpos = 0;
            } else if (scroll > this.height - this.grabberheight) {
                this.scrollpos = this.height - this.grabberheight;
            } else {
                this.scrollpos = scroll;
            }
        }
    }
}