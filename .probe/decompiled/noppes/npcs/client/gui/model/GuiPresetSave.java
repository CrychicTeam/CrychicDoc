package noppes.npcs.client.gui.model;

import net.minecraft.client.gui.screens.Screen;
import noppes.npcs.ModelData;
import noppes.npcs.client.controllers.Preset;
import noppes.npcs.client.controllers.PresetController;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;

public class GuiPresetSave extends GuiBasic {

    private ModelData data;

    private Screen parent;

    public GuiPresetSave(Screen parent, ModelData data) {
        this.data = data;
        this.parent = parent;
        this.imageWidth = 200;
        this.drawDefaultBackground = true;
    }

    @Override
    public void init() {
        super.init();
        this.addTextField(new GuiTextFieldNop(0, this, this.guiLeft, this.guiTop + 70, 200, 20, ""));
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft, this.guiTop + 100, 98, 20, "Save"));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 100, this.guiTop + 100, 98, 20, "Cancel"));
    }

    @Override
    public void buttonEvent(GuiButtonNop btn) {
        if (btn.id == 0) {
            String name = this.getTextField(0).m_94155_().trim();
            if (name.isEmpty()) {
                return;
            }
            Preset preset = new Preset();
            preset.name = name;
            preset.data = this.data.copy();
            PresetController.instance.addPreset(preset);
        }
        this.close();
    }
}