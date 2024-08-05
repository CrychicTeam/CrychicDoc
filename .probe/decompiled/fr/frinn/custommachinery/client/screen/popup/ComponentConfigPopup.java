package fr.frinn.custommachinery.client.screen.popup;

import fr.frinn.custommachinery.client.screen.MachineConfigScreen;
import fr.frinn.custommachinery.client.screen.widget.config.AutoIOModeButtonWidget;
import fr.frinn.custommachinery.client.screen.widget.config.SideModeButtonWidget;
import fr.frinn.custommachinery.impl.component.config.RelativeSide;
import fr.frinn.custommachinery.impl.component.config.SideConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ComponentConfigPopup extends PopupScreen {

    private static final ResourceLocation EXIT_TEXTURE = new ResourceLocation("custommachinery", "textures/gui/config/exit_button.png");

    private static final Component TITLE = Component.translatable("custommachinery.gui.config.component");

    private final SideConfig config;

    public ComponentConfigPopup(MachineConfigScreen parent, SideConfig config) {
        super(parent, 96, 96);
        this.config = config;
    }

    @Override
    protected void init() {
        super.init();
        this.m_142416_(new SideModeButtonWidget(this.x + 41, this.y + 25, this.config, RelativeSide.TOP));
        this.m_142416_(new SideModeButtonWidget(this.x + 25, this.y + 41, this.config, RelativeSide.LEFT));
        this.m_142416_(new SideModeButtonWidget(this.x + 41, this.y + 41, this.config, RelativeSide.FRONT));
        this.m_142416_(new SideModeButtonWidget(this.x + 57, this.y + 41, this.config, RelativeSide.RIGHT));
        this.m_142416_(new SideModeButtonWidget(this.x + 25, this.y + 57, this.config, RelativeSide.BACK));
        this.m_142416_(new SideModeButtonWidget(this.x + 41, this.y + 57, this.config, RelativeSide.BOTTOM));
        this.m_142416_(new AutoIOModeButtonWidget(this.x + 18, this.y + 75, this.config, true));
        this.m_142416_(new AutoIOModeButtonWidget(this.x + 50, this.y + 75, this.config, false));
        this.m_142416_(new ImageButton(this.x + 5, this.y + 5, 9, 9, 0, 0, 9, EXIT_TEXTURE, 9, 18, button -> this.parent.closePopup(this)));
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        blankBackground(graphics, this.x, this.y, this.xSize, this.ySize);
        graphics.drawString(Minecraft.getInstance().font, TITLE, (int) ((float) this.x + (float) this.xSize / 2.0F - (float) this.f_96547_.width(TITLE) / 2.0F), this.y + 5, 0, false);
    }
}