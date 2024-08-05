package net.blay09.mods.waystones.client.gui.screen;

import java.util.Objects;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WaystoneTypes;
import net.blay09.mods.waystones.menu.WaystoneSettingsMenu;
import net.blay09.mods.waystones.network.message.EditWaystoneMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class WaystoneSettingsScreen extends AbstractContainerScreen<WaystoneSettingsMenu> {

    private final MutableComponent isGlobalText = Component.translatable("gui.waystones.waystone_settings.is_global");

    private EditBox textField;

    private Button doneButton;

    private Checkbox isGlobalCheckbox;

    private boolean focusTextFieldNextTick;

    public WaystoneSettingsScreen(WaystoneSettingsMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.f_97726_ = 270;
        this.f_97727_ = 200;
    }

    @Override
    public void init() {
        this.f_97726_ = this.f_96543_;
        super.init();
        IWaystone waystone = ((WaystoneSettingsMenu) this.f_97732_).getWaystone();
        String oldText = waystone.getName();
        if (this.textField != null) {
            oldText = this.textField.getValue();
        }
        int var10004 = this.f_96543_ / 2 - 100;
        int var10005 = this.f_96544_ / 2 - 20;
        this.textField = new EditBox(Minecraft.getInstance().font, var10004, var10005, 200, 20, this.textField, Component.empty());
        this.textField.setMaxLength(128);
        this.textField.setValue(oldText);
        this.m_142416_(this.textField);
        this.m_264313_(this.textField);
        this.doneButton = Button.builder(Component.translatable("gui.done"), button -> {
            if (this.textField.getValue().isEmpty()) {
                this.focusTextFieldNextTick = true;
            } else {
                Balm.getNetworking().sendToServer(new EditWaystoneMessage(waystone.getWaystoneUid(), this.textField.getValue(), this.isGlobalCheckbox.selected()));
            }
        }).pos(this.f_96543_ / 2, this.f_96544_ / 2 + 10).size(100, 20).build();
        this.m_142416_(this.doneButton);
        this.isGlobalCheckbox = new Checkbox(this.f_96543_ / 2 - 100, this.f_96544_ / 2 + 10, 20, 20, Component.empty(), waystone.isGlobal());
        this.isGlobalCheckbox.f_93624_ = waystone.getWaystoneType().equals(WaystoneTypes.WAYSTONE) && PlayerWaystoneManager.mayEditGlobalWaystones((Player) Objects.requireNonNull(Minecraft.getInstance().player));
        this.m_142416_(this.isGlobalCheckbox);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int chkGlobalLabelX = this.f_96543_ / 2 - 100 + 25;
        int chkGlobalLabelY = this.f_96544_ / 2 + 16;
        int chkGlobalLabelWidth = this.f_96541_.font.width(I18n.get("gui.waystones.waystone_settings.is_global"));
        if (mouseX >= (double) chkGlobalLabelX && mouseX < (double) (chkGlobalLabelX + chkGlobalLabelWidth) && mouseY >= (double) chkGlobalLabelY && mouseY < (double) (chkGlobalLabelY + 9)) {
            this.isGlobalCheckbox.onPress();
            return true;
        } else if (this.textField.isMouseOver(mouseX, mouseY) && button == 1) {
            this.textField.setValue("");
            return true;
        } else {
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 257) {
            this.doneButton.onPress();
            return true;
        } else if (!this.textField.keyPressed(keyCode, scanCode, modifiers) && !this.textField.m_93696_()) {
            return super.keyPressed(keyCode, scanCode, modifiers);
        } else {
            if (keyCode == 256) {
                ((LocalPlayer) Objects.requireNonNull(this.f_96541_.player)).closeContainer();
            }
            return true;
        }
    }

    @Override
    protected void containerTick() {
        this.textField.tick();
        if (this.focusTextFieldNextTick) {
            this.m_264313_(this.textField);
            this.focusTextFieldNextTick = false;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.drawString(this.f_96547_, this.m_96636_(), this.f_96543_ / 2 - 100, this.f_96544_ / 2 - 35, 16777215);
        if (this.isGlobalCheckbox.f_93624_) {
            guiGraphics.drawString(this.f_96547_, this.isGlobalText, this.f_96543_ / 2 - 100 + 25, this.f_96544_ / 2 + 16, 16777215);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }
}