package net.minecraft.client.gui.screens.controls;

import com.mojang.blaze3d.platform.InputConstants;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class KeyBindsScreen extends OptionsSubScreen {

    @Nullable
    public KeyMapping selectedKey;

    public long lastKeySelection;

    private KeyBindsList keyBindsList;

    private Button resetButton;

    public KeyBindsScreen(Screen screen0, Options options1) {
        super(screen0, options1, Component.translatable("controls.keybinds.title"));
    }

    @Override
    protected void init() {
        this.keyBindsList = new KeyBindsList(this, this.f_96541_);
        this.m_7787_(this.keyBindsList);
        this.resetButton = (Button) this.m_142416_(Button.builder(Component.translatable("controls.resetAll"), p_269619_ -> {
            for (KeyMapping $$1 : this.f_96282_.keyMappings) {
                $$1.setKey($$1.getDefaultKey());
            }
            this.keyBindsList.resetMappingAndUpdateButtons();
        }).bounds(this.f_96543_ / 2 - 155, this.f_96544_ - 29, 150, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, p_280847_ -> this.f_96541_.setScreen(this.f_96281_)).bounds(this.f_96543_ / 2 - 155 + 160, this.f_96544_ - 29, 150, 20).build());
    }

    @Override
    public boolean mouseClicked(double double0, double double1, int int2) {
        if (this.selectedKey != null) {
            this.f_96282_.setKey(this.selectedKey, InputConstants.Type.MOUSE.getOrCreate(int2));
            this.selectedKey = null;
            this.keyBindsList.resetMappingAndUpdateButtons();
            return true;
        } else {
            return super.m_6375_(double0, double1, int2);
        }
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (this.selectedKey != null) {
            if (int0 == 256) {
                this.f_96282_.setKey(this.selectedKey, InputConstants.UNKNOWN);
            } else {
                this.f_96282_.setKey(this.selectedKey, InputConstants.getKey(int0, int1));
            }
            this.selectedKey = null;
            this.lastKeySelection = Util.getMillis();
            this.keyBindsList.resetMappingAndUpdateButtons();
            return true;
        } else {
            return super.m_7933_(int0, int1, int2);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        this.keyBindsList.m_88315_(guiGraphics0, int1, int2, float3);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 8, 16777215);
        boolean $$4 = false;
        for (KeyMapping $$5 : this.f_96282_.keyMappings) {
            if (!$$5.isDefault()) {
                $$4 = true;
                break;
            }
        }
        this.resetButton.f_93623_ = $$4;
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }
}