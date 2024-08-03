package net.mehvahdjukaar.supplementaries.client.screens;

import java.util.UUID;
import net.mehvahdjukaar.supplementaries.client.screens.widgets.MultiLineEditBoxWidget;
import net.mehvahdjukaar.supplementaries.client.screens.widgets.PlayerSuggestionBoxWidget;
import net.mehvahdjukaar.supplementaries.common.block.tiles.PresentBlockTile;
import net.mehvahdjukaar.supplementaries.common.inventories.PresentContainerMenu;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.mehvahdjukaar.supplementaries.common.network.ServerBoundSetPresentPacket;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PresentScreen extends AbstractContainerScreen<PresentContainerMenu> implements ContainerListener {

    private static final int DESCRIPTION_BOX_X = 53;

    private static final int DESCRIPTION_BOX_Y = 33;

    private static final int DESCRIPTION_BOX_H = 36;

    private static final int DESCRIPTION_BOX_W = 105;

    private static final int SUGGESTION_BOX_Y = 19;

    private static final int SUGGESTION_BOX_W = 99;

    private static final int SUGGESTION_BOX_H = 12;

    private final PresentBlockTile tile;

    private PresentScreen.PackButton packButton;

    private PlayerSuggestionBoxWidget recipient;

    private MultiLineEditBoxWidget descriptionBox;

    private boolean packed;

    private boolean needsInitialization = true;

    public PresentScreen(PresentContainerMenu menu, Inventory inventory, Component text) {
        super(menu, inventory, text);
        this.f_97726_ = 176;
        this.f_97727_ = 166;
        this.tile = (PresentBlockTile) menu.getContainer();
    }

    @Override
    public void init() {
        super.init();
        this.f_97728_ = (this.f_97726_ - this.f_96547_.width(this.f_96539_)) / 2;
        int i = (this.f_96543_ - this.f_97726_) / 2;
        int j = (this.f_96544_ - this.f_97727_) / 2;
        this.packButton = (PresentScreen.PackButton) this.m_142416_(new PresentScreen.PackButton(i + 14, j + 45));
        this.recipient = (PlayerSuggestionBoxWidget) this.m_142416_(new PlayerSuggestionBoxWidget(this.f_96541_, i + 53, j + 19, 99, 12));
        this.recipient.setOutOfBoundResponder(up -> {
            if (!up) {
                this.m_7522_(this.descriptionBox);
            }
        });
        this.descriptionBox = (MultiLineEditBoxWidget) this.m_142416_(new MultiLineEditBoxWidget(this.f_96541_, i + 53, j + 33, 105, 36));
        this.descriptionBox.setOutOfBoundResponder(up -> {
            if (up) {
                this.m_7522_(this.recipient);
            }
        });
        this.m_7522_(this.recipient);
        this.recipient.setText(this.tile.getRecipient());
        this.descriptionBox.setText(this.tile.getDescription());
        this.packed = this.tile.isPacked();
        this.updateState();
        ((PresentContainerMenu) this.f_97732_).m_38893_(this);
    }

    public void onAddPlayer(PlayerInfo info) {
        this.recipient.addPlayer(info);
    }

    public void onRemovePlayer(UUID uuid) {
        this.recipient.removePlayer(uuid);
    }

    private void pack() {
        this.updateStateAndTryToPack(true);
    }

    private void updateState() {
        this.updateStateAndTryToPack(false);
    }

    private void updateStateAndTryToPack(boolean tryToPack) {
        boolean hasItem = this.needsInitialization ? this.packed : ((PresentContainerMenu) this.f_97732_).m_38853_(0).hasItem();
        boolean hasChanged = false;
        if (this.packed && !hasItem) {
            this.packed = false;
            hasChanged = true;
        } else if (tryToPack && !this.packed && hasItem) {
            this.packed = true;
            hasChanged = true;
        }
        if (hasChanged) {
            String sender = Minecraft.getInstance().player.m_7755_().getString();
            String recipient = this.recipient.getText();
            String description = this.descriptionBox.getText();
            ModNetwork.CHANNEL.sendToServer(new ServerBoundSetPresentPacket(this.tile.m_58899_(), this.packed, recipient, sender, description));
            this.tile.updateState(this.packed, recipient, sender, description);
            if (this.packed) {
                this.f_96541_.player.clientSideCloseContainer();
            }
        }
        this.recipient.setState(hasItem, this.packed);
        this.packButton.setState(hasItem, this.packed);
        this.descriptionBox.setState(hasItem, this.packed);
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
        ResourceLocation presentGuiTexture = ((PresentContainerMenu) this.f_97732_).m_38853_(0).getItem().isEmpty() ? ModTextures.PRESENT_EMPTY_GUI_TEXTURE : ModTextures.PRESENT_GUI_TEXTURE;
        graphics.blit(presentGuiTexture, k, l, 0, 0, this.f_97726_, this.f_97727_);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        if (this.packed) {
            int k = (this.f_96543_ - this.f_97726_) / 2;
            int l = (this.f_96544_ - this.f_97727_) / 2;
            Slot slot = ((PresentContainerMenu) this.f_97732_).m_38853_(0);
            graphics.blit(ModTextures.PRESENT_GUI_TEXTURE, k + slot.x, l + slot.y, 300, 12.0F, 232.0F, 16, 16, 256, 256);
        }
        this.m_280072_(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics poseStack, int x, int y) {
        super.renderLabels(poseStack, x, y);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int key) {
        this.recipient.m_93692_(false);
        this.descriptionBox.m_93692_(false);
        return super.mouseClicked(mouseX, mouseY, key);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return this.recipient.m_6050_(mouseX, mouseY, amount) || this.descriptionBox.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(int key, int a, int b) {
        if (key == 256) {
            this.f_96541_.player.closeContainer();
        }
        return this.recipient.keyPressed(key, a, b) || this.recipient.canConsumeInput() || this.descriptionBox.keyPressed(key, a, b) || this.descriptionBox.canConsumeInput() || super.keyPressed(key, a, b);
    }

    @Override
    public boolean mouseDragged(double dx, double dy, int key, double mouseX, double mouseY) {
        return key == 0 && this.descriptionBox.m_7979_(dx, dy, key, mouseX, mouseY) ? true : super.mouseDragged(dx, dy, key, mouseX, mouseY);
    }

    @Override
    public void containerTick() {
        this.needsInitialization = false;
        super.containerTick();
        this.recipient.tick();
        this.descriptionBox.tick();
    }

    @Override
    public void removed() {
        super.removed();
        ((PresentContainerMenu) this.f_97732_).m_38943_(this);
    }

    public class PackButton extends AbstractButton {

        private static final Tooltip TOOLTIP = Tooltip.create(Component.translatable("gui.supplementaries.present.pack"));

        private boolean packed;

        protected PackButton(int x, int y) {
            super(x, y, 22, 22, CommonComponents.EMPTY);
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int i1, int i2, float v) {
            int i = 198;
            int j = 0;
            if (!this.f_93623_) {
                j += this.f_93618_ * 2;
            } else if (this.packed) {
                j += this.f_93618_ * 1;
            } else if (this.f_93622_) {
                j += this.f_93618_ * 3;
            }
            graphics.blit(ModTextures.PRESENT_GUI_TEXTURE, this.m_252754_(), this.m_252907_(), j, i, this.f_93618_, this.f_93619_);
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
            PresentScreen.this.pack();
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        }
    }
}