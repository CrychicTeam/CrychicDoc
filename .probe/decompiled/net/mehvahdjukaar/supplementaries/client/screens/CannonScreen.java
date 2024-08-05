package net.mehvahdjukaar.supplementaries.client.screens;

import net.mehvahdjukaar.supplementaries.common.block.tiles.CannonBlockTile;
import net.mehvahdjukaar.supplementaries.common.inventories.CannonContainerMenu;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CannonScreen extends AbstractContainerScreen<CannonContainerMenu> implements ContainerListener {

    private final CannonBlockTile tile;

    private CannonScreen.TargetButton targetButton;

    private TextFieldHelper pitchSelector;

    private TextFieldHelper yawSelector;

    private boolean primed;

    private boolean needsInitialization = true;

    public CannonScreen(CannonContainerMenu menu, Inventory inventory, Component text) {
        super(menu, inventory, text);
        this.f_97726_ = 176;
        this.f_97727_ = 166;
        this.tile = menu.getContainer();
    }

    @Override
    public void init() {
        super.init();
        this.f_97728_ = (this.f_97726_ - this.f_96547_.width(this.f_96539_)) / 2;
        int i = (this.f_96543_ - this.f_97726_) / 2;
        int j = (this.f_96544_ - this.f_97727_) / 2;
        this.targetButton = (CannonScreen.TargetButton) this.m_142416_(new CannonScreen.TargetButton(i + 60 + 33, j + 33));
        this.yawSelector = new TextFieldHelper(() -> this.getYaw(), h -> this.setYaw(h), TextFieldHelper.createClipboardGetter(this.f_96541_), TextFieldHelper.createClipboardSetter(this.f_96541_), s -> this.isValidAngle(s));
        ((CannonContainerMenu) this.f_97732_).m_38893_(this);
    }

    private void setYaw(String h) {
    }

    private String getYaw() {
        return "0";
    }

    private boolean isValidAngle(String s) {
        this.f_96541_.font.width(s);
        return true;
    }

    private void pack() {
    }

    @Override
    public void slotChanged(AbstractContainerMenu container, int slot, ItemStack stack) {
        if (slot == 0) {
        }
    }

    @Override
    public void dataChanged(AbstractContainerMenu container, int dataSlotIndex, int value) {
        this.slotChanged(container, 0, container.getSlot(0).getItem());
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        this.m_280273_(graphics);
        int k = (this.f_96543_ - this.f_97726_) / 2;
        int l = (this.f_96544_ - this.f_97727_) / 2;
        graphics.blit(ModTextures.TRAPPED_PRESENT_GUI_TEXTURE, k, l, 0, 0, this.f_97726_, this.f_97727_);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        if (this.primed) {
            int k = (this.f_96543_ - this.f_97726_) / 2;
            int l = (this.f_96544_ - this.f_97727_) / 2;
            Slot slot = ((CannonContainerMenu) this.f_97732_).m_38853_(0);
            graphics.blit(ModTextures.TRAPPED_PRESENT_GUI_TEXTURE, k + slot.x, l + slot.y, 400, 12.0F, 232.0F, 16, 16, 256, 256);
        }
        this.m_280072_(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int x, int y) {
        super.renderLabels(graphics, x, y);
    }

    @Override
    public boolean keyPressed(int key, int a, int b) {
        if (key == 256) {
            this.f_96541_.player.closeContainer();
        }
        return super.keyPressed(key, a, b);
    }

    @Override
    public void containerTick() {
        this.needsInitialization = false;
        super.containerTick();
    }

    @Override
    public void removed() {
        super.removed();
        ((CannonContainerMenu) this.f_97732_).m_38943_(this);
    }

    public class TargetButton extends AbstractButton {

        private static final Tooltip TOOLTIP = Tooltip.create(Component.translatable("gui.supplementaries.cannon.maneuver"));

        private boolean packed;

        protected TargetButton(int x, int y) {
            super(x, y, 22, 22, CommonComponents.EMPTY);
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            int i = 198;
            int j = 0;
            if (!this.f_93623_) {
                j += this.f_93618_ * 2;
            } else if (this.packed) {
                j += this.f_93618_ * 1;
            } else if (this.f_93622_) {
                j += this.f_93618_ * 3;
            }
            graphics.blit(ModTextures.TRAPPED_PRESENT_GUI_TEXTURE, this.m_252754_(), this.m_252907_(), j, i, this.f_93618_, this.f_93619_);
        }

        public void setState(boolean hasItem, boolean packed) {
            this.packed = packed;
            this.f_93623_ = hasItem;
            this.m_257544_(!packed ? TOOLTIP : null);
        }

        @Override
        protected ClientTooltipPositioner createTooltipPositioner() {
            return DefaultTooltipPositioner.INSTANCE;
        }

        @Override
        public void onPress() {
            CannonScreen.this.pack();
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        }
    }
}