package se.mickelus.tetra.items.modular.impl.toolbelt.gui.screen;

import java.util.List;
import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import se.mickelus.mutil.gui.DisabledSlot;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.impl.GuiHorizontalLayoutGroup;
import se.mickelus.tetra.client.keymap.TetraKeyMappings;
import se.mickelus.tetra.gui.GuiKeybinding;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.items.modular.impl.toolbelt.ToolbeltContainer;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class ToolbeltScreen extends AbstractContainerScreen<ToolbeltContainer> {

    private static ToolbeltScreen instance;

    private final GuiElement defaultGui;

    private final GuiElement keybindGui;

    public ToolbeltScreen(ToolbeltContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.f_97726_ = 179;
        this.f_97727_ = 240;
        int numQuickslots = container.getQuickslotInventory().m_6643_();
        int numStorageSlots = container.getStorageInventory().m_6643_();
        int numPotionSlots = container.getPotionInventory().m_6643_();
        int numQuiverSlots = container.getQuiverInventory().m_6643_();
        int offset = 0;
        this.defaultGui = new GuiElement(0, 0, this.f_97726_, this.f_97727_);
        this.defaultGui.addChild(new GuiTexture(0, 129, 179, 91, GuiTextures.playerInventory));
        if (numStorageSlots > 0) {
            GuiStorageBackdrop storageBackdrop = new GuiStorageBackdrop(0, 130 - offset, numStorageSlots, container.getStorageInventory().getSlotEffects());
            this.defaultGui.addChild(storageBackdrop);
            offset += storageBackdrop.getHeight() + 2;
        }
        if (numQuiverSlots > 0) {
            GuiQuiverBackdrop quiverBackdrop = new GuiQuiverBackdrop(0, 130 - offset, numQuiverSlots, container.getQuiverInventory().getSlotEffects());
            this.defaultGui.addChild(quiverBackdrop);
            offset += quiverBackdrop.getHeight() + 2;
        }
        if (numPotionSlots > 0) {
            PotionBackdropGui potionsBackdrop = new PotionBackdropGui(0, 130 - offset, numPotionSlots, container.getPotionInventory().getSlotEffects());
            this.defaultGui.addChild(potionsBackdrop);
            offset += potionsBackdrop.getHeight() + 2;
        }
        if (numQuickslots > 0) {
            GuiQuickSlotBackdrop quickSlotBackdrop = new GuiQuickSlotBackdrop(0, 130 - offset, numQuickslots, container.getQuickslotInventory().getSlotEffects());
            this.defaultGui.addChild(quickSlotBackdrop);
            offset += quickSlotBackdrop.getHeight() + 2;
        }
        this.keybindGui = new GuiElement(0, 0, 3840, 23);
        this.keybindGui.addChild(new GuiRect(0, 0, 3840, 23, -872415232).setAttachment(GuiAttachment.bottomCenter));
        this.keybindGui.addChild(new GuiRect(0, -21, 3840, 1, 4210752).setAttachment(GuiAttachment.bottomCenter));
        GuiHorizontalLayoutGroup keybindGroup = new GuiHorizontalLayoutGroup(0, -5, 11, 8);
        keybindGroup.setAttachment(GuiAttachment.bottomCenter);
        this.keybindGui.addChild(keybindGroup);
        keybindGroup.addChild(new GuiKeybinding(0, 0, TetraKeyMappings.accessBinding));
        keybindGroup.addChild(new GuiRect(0, -1, 1, 13, 4210752));
        keybindGroup.addChild(new GuiKeybinding(0, 0, TetraKeyMappings.restockBinding));
        keybindGroup.addChild(new GuiRect(0, -1, 1, 13, 4210752));
        keybindGroup.addChild(new GuiKeybinding(0, 0, TetraKeyMappings.openBinding));
        instance = this;
    }

    @Override
    protected void slotClicked(Slot slot, int slotIndex, int barIndex, ClickType clickType) {
        if (!(slot instanceof DisabledSlot)) {
            super.slotClicked(slot, slotIndex, barIndex, clickType);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int x = (this.f_96543_ - this.f_97726_) / 2;
        int y = (this.f_96544_ - this.f_97727_) / 2;
        this.keybindGui.setWidth(this.f_96543_);
        this.keybindGui.updateFocusState(0, this.f_96544_ - this.keybindGui.getHeight(), mouseX, mouseY);
        this.keybindGui.draw(graphics, 0, this.f_96544_ - this.keybindGui.getHeight(), this.f_96543_, this.f_96544_, mouseX, mouseY, 1.0F);
        this.defaultGui.updateFocusState(x, y, mouseX, mouseY);
        this.defaultGui.draw(graphics, x, y, this.f_96543_, this.f_96544_, mouseX, mouseY, 1.0F);
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderTooltip(graphics, mouseX, mouseY);
        List<Component> tooltipLines = this.defaultGui.getTooltipLines();
        if (tooltipLines != null) {
            graphics.renderTooltip(this.f_96547_, tooltipLines, Optional.empty(), mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int x, int y) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}