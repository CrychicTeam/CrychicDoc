package com.simibubi.create.content.schematics.client.tools;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public interface ISchematicTool {

    void init();

    void updateSelection();

    boolean handleRightClick();

    boolean handleMouseWheel(double var1);

    void renderTool(PoseStack var1, SuperRenderTypeBuffer var2, Vec3 var3);

    void renderOverlay(ForgeGui var1, GuiGraphics var2, float var3, int var4, int var5);

    void renderOnSchematic(PoseStack var1, SuperRenderTypeBuffer var2);
}