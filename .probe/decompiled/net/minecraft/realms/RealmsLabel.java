package net.minecraft.realms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;

public class RealmsLabel implements Renderable {

    private final Component text;

    private final int x;

    private final int y;

    private final int color;

    public RealmsLabel(Component component0, int int1, int int2, int int3) {
        this.text = component0;
        this.x = int1;
        this.y = int2;
        this.color = int3;
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        guiGraphics0.drawCenteredString(Minecraft.getInstance().font, this.text, this.x, this.y, this.color);
    }

    public Component getText() {
        return this.text;
    }
}