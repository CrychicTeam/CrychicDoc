package se.mickelus.tetra.items.modular.impl.toolbelt.booster;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import se.mickelus.mutil.gui.GuiRoot;

@ParametersAreNonnullByDefault
public class OverlayGuiBooster extends GuiRoot {

    private final GuiBarBooster barElement = new GuiBarBooster(50, 100, 0, 0);

    public OverlayGuiBooster(Minecraft mc) {
        super(mc);
        this.addChild(this.barElement);
    }

    public void setFuel(float fuel) {
        this.barElement.setFuel(fuel);
    }
}