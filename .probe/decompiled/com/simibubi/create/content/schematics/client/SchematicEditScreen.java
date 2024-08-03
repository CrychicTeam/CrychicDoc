package com.simibubi.create.content.schematics.client;

import com.simibubi.create.AllItems;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.widget.AbstractSimiWidget;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.gui.widget.SelectionScrollInput;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

public class SchematicEditScreen extends AbstractSimiScreen {

    private final List<Component> rotationOptions = Lang.translatedOptions("schematic.rotation", "none", "cw90", "cw180", "cw270");

    private final List<Component> mirrorOptions = Lang.translatedOptions("schematic.mirror", "none", "leftRight", "frontBack");

    private final Component rotationLabel = Lang.translateDirect("schematic.rotation");

    private final Component mirrorLabel = Lang.translateDirect("schematic.mirror");

    private AllGuiTextures background = AllGuiTextures.SCHEMATIC;

    private EditBox xInput;

    private EditBox yInput;

    private EditBox zInput;

    private IconButton confirmButton;

    private ScrollInput rotationArea;

    private ScrollInput mirrorArea;

    private SchematicHandler handler = CreateClient.SCHEMATIC_HANDLER;

    @Override
    protected void init() {
        this.setWindowSize(this.background.width, this.background.height);
        this.setWindowOffset(-6, 0);
        super.init();
        int x = this.guiLeft;
        int y = this.guiTop;
        this.xInput = new EditBox(this.f_96547_, x + 50, y + 26, 34, 10, Components.immutableEmpty());
        this.yInput = new EditBox(this.f_96547_, x + 90, y + 26, 34, 10, Components.immutableEmpty());
        this.zInput = new EditBox(this.f_96547_, x + 130, y + 26, 34, 10, Components.immutableEmpty());
        BlockPos anchor = this.handler.getTransformation().getAnchor();
        if (this.handler.isDeployed()) {
            this.xInput.setValue(anchor.m_123341_() + "");
            this.yInput.setValue(anchor.m_123342_() + "");
            this.zInput.setValue(anchor.m_123343_() + "");
        } else {
            BlockPos alt = this.f_96541_.player.m_20183_();
            this.xInput.setValue(alt.m_123341_() + "");
            this.yInput.setValue(alt.m_123342_() + "");
            this.zInput.setValue(alt.m_123343_() + "");
        }
        for (EditBox widget : new EditBox[] { this.xInput, this.yInput, this.zInput }) {
            widget.setMaxLength(6);
            widget.setBordered(false);
            widget.setTextColor(16777215);
            widget.setFocused(false);
            widget.m_6375_(0.0, 0.0, 0);
            widget.setFilter(s -> {
                if (!s.isEmpty() && !s.equals("-")) {
                    try {
                        Integer.parseInt(s);
                        return true;
                    } catch (NumberFormatException var2x) {
                        return false;
                    }
                } else {
                    return true;
                }
            });
        }
        StructurePlaceSettings settings = this.handler.getTransformation().toSettings();
        Label labelR = new Label(x + 50, y + 48, Components.immutableEmpty()).withShadow();
        this.rotationArea = new SelectionScrollInput(x + 45, y + 43, 118, 18).forOptions(this.rotationOptions).titled(this.rotationLabel.plainCopy()).setState(settings.getRotation().ordinal()).writingTo(labelR);
        Label labelM = new Label(x + 50, y + 70, Components.immutableEmpty()).withShadow();
        this.mirrorArea = new SelectionScrollInput(x + 45, y + 65, 118, 18).forOptions(this.mirrorOptions).titled(this.mirrorLabel.plainCopy()).setState(settings.getMirror().ordinal()).writingTo(labelM);
        this.addRenderableWidgets(new EditBox[] { this.xInput, this.yInput, this.zInput });
        this.addRenderableWidgets(new AbstractSimiWidget[] { labelR, labelM, this.rotationArea, this.mirrorArea });
        this.confirmButton = new IconButton(x + this.background.width - 33, y + this.background.height - 24, AllIcons.I_CONFIRM);
        this.confirmButton.withCallback(() -> this.m_7379_());
        this.m_142416_(this.confirmButton);
    }

    @Override
    public boolean keyPressed(int code, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (m_96630_(code)) {
            String coords = this.f_96541_.keyboardHandler.getClipboard();
            if (coords != null && !coords.isEmpty()) {
                coords.replaceAll(" ", "");
                String[] split = coords.split(",");
                if (split.length == 3) {
                    boolean valid = true;
                    for (String s : split) {
                        try {
                            Integer.parseInt(s);
                        } catch (NumberFormatException var12) {
                            valid = false;
                        }
                    }
                    if (valid) {
                        this.xInput.setValue(split[0]);
                        this.yInput.setValue(split[1]);
                        this.zInput.setValue(split[2]);
                        return true;
                    }
                }
            }
        }
        return super.keyPressed(code, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int x = this.guiLeft;
        int y = this.guiTop;
        this.background.render(graphics, x, y);
        String title = this.handler.getCurrentSchematicName();
        graphics.drawCenteredString(this.f_96547_, title, x + (this.background.width - 8) / 2, y + 3, 16777215);
        GuiGameElement.of(AllItems.SCHEMATIC.asStack()).<GuiGameElement.GuiRenderBuilder>at((float) (x + this.background.width + 6), (float) (y + this.background.height - 40), -200.0F).scale(3.0).render(graphics);
    }

    @Override
    public void removed() {
        boolean validCoords = true;
        BlockPos newLocation = null;
        try {
            newLocation = new BlockPos(Integer.parseInt(this.xInput.getValue()), Integer.parseInt(this.yInput.getValue()), Integer.parseInt(this.zInput.getValue()));
        } catch (NumberFormatException var5) {
            validCoords = false;
        }
        StructurePlaceSettings settings = new StructurePlaceSettings();
        settings.setRotation(Rotation.values()[this.rotationArea.getState()]);
        settings.setMirror(Mirror.values()[this.mirrorArea.getState()]);
        if (validCoords && newLocation != null) {
            ItemStack item = this.handler.getActiveSchematicItem();
            if (item != null) {
                item.getTag().putBoolean("Deployed", true);
                item.getTag().put("Anchor", NbtUtils.writeBlockPos(newLocation));
            }
            this.handler.getTransformation().init(newLocation, settings, this.handler.getBounds());
            this.handler.markDirty();
            this.handler.deploy();
        }
    }
}