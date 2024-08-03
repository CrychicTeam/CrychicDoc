package com.simibubi.create.content.schematics.table;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.schematics.client.ClientSchematicLoader;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.gui.widget.SelectionScrollInput;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class SchematicTableScreen extends AbstractSimiContainerScreen<SchematicTableMenu> {

    private final Component uploading = Lang.translateDirect("gui.schematicTable.uploading");

    private final Component finished = Lang.translateDirect("gui.schematicTable.finished");

    private final Component refresh = Lang.translateDirect("gui.schematicTable.refresh");

    private final Component folder = Lang.translateDirect("gui.schematicTable.open_folder");

    private final Component noSchematics = Lang.translateDirect("gui.schematicTable.noSchematics");

    private final Component availableSchematicsTitle = Lang.translateDirect("gui.schematicTable.availableSchematics");

    protected AllGuiTextures background;

    private ScrollInput schematicsArea;

    private IconButton confirmButton;

    private IconButton folderButton;

    private IconButton refreshButton;

    private Label schematicsLabel;

    private float progress;

    private float chasingProgress;

    private float lastChasingProgress;

    private final ItemStack renderedItem = AllBlocks.SCHEMATIC_TABLE.asStack();

    private List<Rect2i> extraAreas = Collections.emptyList();

    public SchematicTableScreen(SchematicTableMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.background = AllGuiTextures.SCHEMATIC_TABLE;
    }

    @Override
    protected void init() {
        this.setWindowSize(this.background.width, this.background.height + 4 + AllGuiTextures.PLAYER_INVENTORY.height);
        this.setWindowOffset(-11, 8);
        super.init();
        CreateClient.SCHEMATIC_SENDER.refresh();
        List<Component> availableSchematics = CreateClient.SCHEMATIC_SENDER.getAvailableSchematics();
        int x = this.f_97735_;
        int y = this.f_97736_;
        this.schematicsLabel = new Label(x + 49, y + 26, Components.immutableEmpty()).withShadow();
        this.schematicsLabel.text = Components.immutableEmpty();
        if (!availableSchematics.isEmpty()) {
            this.schematicsArea = new SelectionScrollInput(x + 45, y + 21, 139, 18).forOptions(availableSchematics).titled(this.availableSchematicsTitle.plainCopy()).writingTo(this.schematicsLabel);
            this.m_142416_(this.schematicsArea);
            this.m_142416_(this.schematicsLabel);
        }
        this.confirmButton = new IconButton(x + 44, y + 56, AllIcons.I_CONFIRM);
        this.confirmButton.withCallback(() -> {
            if (((SchematicTableMenu) this.f_97732_).canWrite() && this.schematicsArea != null) {
                ClientSchematicLoader schematicSender = CreateClient.SCHEMATIC_SENDER;
                this.lastChasingProgress = this.chasingProgress = this.progress = 0.0F;
                List<Component> availableSchematics1 = schematicSender.getAvailableSchematics();
                Component schematic = (Component) availableSchematics1.get(this.schematicsArea.getState());
                schematicSender.startNewUpload(schematic.getString());
            }
        });
        this.folderButton = new IconButton(x + 21, y + 21, AllIcons.I_OPEN_FOLDER);
        this.folderButton.withCallback(() -> Util.getPlatform().openFile(Paths.get("schematics/").toFile()));
        this.folderButton.setToolTip(this.folder);
        this.refreshButton = new IconButton(x + 207, y + 21, AllIcons.I_REFRESH);
        this.refreshButton.withCallback(() -> {
            ClientSchematicLoader schematicSender = CreateClient.SCHEMATIC_SENDER;
            schematicSender.refresh();
            List<Component> availableSchematics1 = schematicSender.getAvailableSchematics();
            this.m_169411_(this.schematicsArea);
            if (!availableSchematics1.isEmpty()) {
                this.schematicsArea = new SelectionScrollInput(this.f_97735_ + 45, this.f_97736_ + 21, 139, 18).forOptions(availableSchematics1).titled(this.availableSchematicsTitle.plainCopy()).writingTo(this.schematicsLabel);
                this.schematicsArea.onChanged();
                this.m_142416_(this.schematicsArea);
            } else {
                this.schematicsArea = null;
                this.schematicsLabel.text = Components.immutableEmpty();
            }
        });
        this.refreshButton.setToolTip(this.refresh);
        this.m_142416_(this.confirmButton);
        this.m_142416_(this.folderButton);
        this.m_142416_(this.refreshButton);
        this.extraAreas = ImmutableList.of(new Rect2i(x + this.background.width, y + this.background.height - 40, 48, 48), new Rect2i(this.refreshButton.m_252754_(), this.refreshButton.m_252907_(), this.refreshButton.m_5711_(), this.refreshButton.m_93694_()));
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int invX = this.getLeftOfCentered(AllGuiTextures.PLAYER_INVENTORY.width);
        int invY = this.f_97736_ + this.background.height + 4;
        this.renderPlayerInventory(graphics, invX, invY);
        int x = this.f_97735_;
        int y = this.f_97736_;
        this.background.render(graphics, x, y);
        Component titleText;
        if (((SchematicTableMenu) this.f_97732_).contentHolder.isUploading) {
            titleText = this.uploading;
        } else if (((SchematicTableMenu) this.f_97732_).m_38853_(1).hasItem()) {
            titleText = this.finished;
        } else {
            titleText = this.f_96539_;
        }
        graphics.drawCenteredString(this.f_96547_, titleText, x + (this.background.width - 8) / 2, y + 3, 16777215);
        if (this.schematicsArea == null) {
            graphics.drawString(this.f_96547_, this.noSchematics, x + 54, y + 26, 13882323);
        }
        GuiGameElement.of(this.renderedItem).<GuiGameElement.GuiRenderBuilder>at((float) (x + this.background.width), (float) (y + this.background.height - 40), -200.0F).scale(3.0).render(graphics);
        int width = (int) ((float) AllGuiTextures.SCHEMATIC_TABLE_PROGRESS.width * Mth.lerp(partialTicks, this.lastChasingProgress, this.chasingProgress));
        int height = AllGuiTextures.SCHEMATIC_TABLE_PROGRESS.height;
        graphics.blit(AllGuiTextures.SCHEMATIC_TABLE_PROGRESS.location, x + 70, y + 57, AllGuiTextures.SCHEMATIC_TABLE_PROGRESS.startX, AllGuiTextures.SCHEMATIC_TABLE_PROGRESS.startY, width, height);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        boolean finished = ((SchematicTableMenu) this.f_97732_).m_38853_(1).hasItem();
        if (!((SchematicTableMenu) this.f_97732_).contentHolder.isUploading && !finished) {
            this.progress = 0.0F;
            this.chasingProgress = this.lastChasingProgress = 0.0F;
            this.confirmButton.f_93623_ = true;
            if (this.schematicsLabel != null) {
                this.schematicsLabel.colored(16777215);
            }
            if (this.schematicsArea != null) {
                this.schematicsArea.writingTo(this.schematicsLabel);
                this.schematicsArea.f_93624_ = true;
            }
        } else {
            if (finished) {
                this.chasingProgress = this.lastChasingProgress = this.progress = 1.0F;
            } else {
                this.lastChasingProgress = this.chasingProgress;
                this.progress = ((SchematicTableMenu) this.f_97732_).contentHolder.uploadingProgress;
                this.chasingProgress = this.chasingProgress + (this.progress - this.chasingProgress) * 0.5F;
            }
            this.confirmButton.f_93623_ = false;
            if (this.schematicsLabel != null) {
                this.schematicsLabel.colored(13426175);
                String uploadingSchematic = ((SchematicTableMenu) this.f_97732_).contentHolder.uploadingSchematic;
                this.schematicsLabel.text = uploadingSchematic == null ? null : Components.literal(uploadingSchematic);
            }
            if (this.schematicsArea != null) {
                this.schematicsArea.f_93624_ = false;
            }
        }
    }

    @Override
    public List<Rect2i> getExtraAreas() {
        return this.extraAreas;
    }
}