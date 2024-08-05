package harmonised.pmmo.client.gui;

import harmonised.pmmo.client.utils.VeinTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class IndicatorsOverlayGUI implements IGuiOverlay {

    private static final ResourceLocation ICONS = new ResourceLocation("pmmo", "textures/gui/overlay_icons.png");

    private Minecraft mc;

    private BlockHitResult bhr;

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if (this.mc == null) {
            this.mc = Minecraft.getInstance();
        }
        if (this.mc.hitResult instanceof BlockHitResult) {
            this.bhr = (BlockHitResult) this.mc.hitResult;
            if (!this.mc.options.renderDebug && VeinTracker.isLookingAtVeinTarget(this.bhr) && !this.mc.player.m_9236_().getBlockState(this.bhr.getBlockPos()).m_60795_()) {
                float iconIndex = (float) (VeinTracker.mode.ordinal() * 16);
                guiGraphics.blit(ICONS, screenWidth / 2 - 16, screenHeight / 2 - 8, iconIndex, 0.0F, 16, 16, 48, 16);
            }
        }
    }
}