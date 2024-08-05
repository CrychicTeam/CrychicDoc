package se.mickelus.tetra.items.modular.impl.toolbelt.booster;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.items.modular.impl.toolbelt.ToolbeltHelper;

@ParametersAreNonnullByDefault
public class OverlayBooster implements IGuiOverlay {

    private final Minecraft mc;

    private final OverlayGuiBooster gui;

    public OverlayBooster(Minecraft mc) {
        this.mc = mc;
        this.gui = new OverlayGuiBooster(mc);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (TickEvent.Phase.END == event.phase && this.mc.player != null) {
            float fuelPercent = 0.0F;
            ItemStack itemStack = ToolbeltHelper.findToolbelt(this.mc.player);
            if (UtilBooster.canBoost(itemStack)) {
                fuelPercent = UtilBooster.getFuelPercent(itemStack.getTag());
            }
            this.gui.setFuel(fuelPercent);
        }
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        this.gui.draw(graphics);
    }
}