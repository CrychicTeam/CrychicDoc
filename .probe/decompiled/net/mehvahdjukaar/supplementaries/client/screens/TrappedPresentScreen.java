package net.mehvahdjukaar.supplementaries.client.screens;

import net.mehvahdjukaar.supplementaries.common.block.tiles.TrappedPresentBlockTile;
import net.mehvahdjukaar.supplementaries.common.inventories.TrappedPresentContainerMenu;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.mehvahdjukaar.supplementaries.common.network.ServerBoundSetTrappedPresentPacket;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
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

public class TrappedPresentScreen extends AbstractContainerScreen<TrappedPresentContainerMenu> implements ContainerListener {

    private final TrappedPresentBlockTile tile;

    private TrappedPresentScreen.PackButton packButton;

    private boolean primed;

    private boolean needsInitialization = true;

    public TrappedPresentScreen(TrappedPresentContainerMenu menu, Inventory inventory, Component text) {
        super(menu, inventory, text);
        this.f_97726_ = 176;
        this.f_97727_ = 166;
        this.tile = (TrappedPresentBlockTile) menu.getContainer();
    }

    @Override
    public void init() {
        super.init();
        this.f_97728_ = (this.f_97726_ - this.f_96547_.width(this.f_96539_)) / 2;
        int i = (this.f_96543_ - this.f_97726_) / 2;
        int j = (this.f_96544_ - this.f_97727_) / 2;
        this.packButton = (TrappedPresentScreen.PackButton) this.m_142416_(new TrappedPresentScreen.PackButton(i + 60 + 33, j + 33));
        this.primed = this.tile.isPrimed();
        this.updateState();
        ((TrappedPresentContainerMenu) this.f_97732_).m_38893_(this);
    }

    private void pack() {
        this.updateStateAndTryToPack(true);
    }

    private void updateState() {
        this.updateStateAndTryToPack(false);
    }

    private void updateStateAndTryToPack(boolean tryToPack) {
        boolean hasItem = this.needsInitialization ? this.primed : ((TrappedPresentContainerMenu) this.f_97732_).m_38853_(0).hasItem();
        boolean hasChanged = false;
        if (this.primed && !hasItem) {
            this.primed = false;
            hasChanged = true;
        } else if (tryToPack && !this.primed && hasItem) {
            this.primed = true;
            hasChanged = true;
        }
        if (hasChanged) {
            ModNetwork.CHANNEL.sendToServer(new ServerBoundSetTrappedPresentPacket(this.tile.m_58899_(), this.primed));
            this.tile.updateState(this.primed);
            if (this.primed) {
                this.f_96541_.player.clientSideCloseContainer();
            }
        }
        this.packButton.setState(hasItem, this.primed);
    }

    @Override
    public void slotChanged(AbstractContainerMenu container, int slot, ItemStack stack) {
        if (slot == 0) {
            this.updateState();
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
            Slot slot = ((TrappedPresentContainerMenu) this.f_97732_).m_38853_(0);
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
        ((TrappedPresentContainerMenu) this.f_97732_).m_38943_(this);
    }

    public class PackButton extends AbstractButton {

        private static final Tooltip TOOLTIP = Tooltip.create(Component.translatable("gui.supplementaries.present.trapped"));

        private boolean packed;

        protected PackButton(int x, int y) {
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
            TrappedPresentScreen.this.pack();
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        }
    }
}