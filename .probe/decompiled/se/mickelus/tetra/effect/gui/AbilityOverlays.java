package se.mickelus.tetra.effect.gui;

import com.mojang.blaze3d.platform.Window;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.mutil.gui.GuiRoot;

@ParametersAreNonnullByDefault
public class AbilityOverlays extends GuiRoot implements IGuiOverlay {

    public static AbilityOverlays instance;

    private final ChargeBarGui chargeBar = new ChargeBarGui();

    private final ComboPointGui comboPoints;

    private final RevengeGui revengeIndicator;

    private final FocusGui focusIndicator;

    public AbilityOverlays(Minecraft mc) {
        super(mc);
        this.addChild(this.chargeBar);
        this.comboPoints = new ComboPointGui();
        this.addChild(this.comboPoints);
        this.revengeIndicator = new RevengeGui();
        this.addChild(this.revengeIndicator);
        this.focusIndicator = new FocusGui();
        this.addChild(this.focusIndicator);
        instance = this;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (TickEvent.Phase.START == event.phase && this.mc.player != null) {
            this.chargeBar.update(this.mc.player);
            this.comboPoints.update(this.mc.player);
            this.revengeIndicator.update(this.mc.player, this.mc.hitResult);
            this.focusIndicator.update(this.mc.player);
        }
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if (this.isVisible()) {
            Window window = this.mc.getWindow();
            int width = window.getGuiScaledWidth();
            int height = window.getGuiScaledHeight();
            this.drawChildren(guiGraphics, Math.round((float) width / 2.0F), Math.round((float) height / 2.0F), 0, 0, 0, 0, 1.0F);
        }
    }
}