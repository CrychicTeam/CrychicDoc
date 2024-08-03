package se.mickelus.tetra.blocks.forged.container;

import java.util.stream.IntStream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.AnimationChain;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.gui.VerticalTabGroupGui;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class ForgedContainerScreen extends AbstractContainerScreen<ForgedContainerMenu> {

    private static final ResourceLocation containerTexture = new ResourceLocation("tetra", "textures/gui/forged-container.png");

    private final ForgedContainerBlockEntity tileEntity;

    private final ForgedContainerMenu container;

    private final GuiElement guiRoot;

    private final AnimationChain slotTransition;

    private final VerticalTabGroupGui compartmentButtons;

    public ForgedContainerScreen(ForgedContainerMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.f_97726_ = 179;
        this.f_97727_ = 176;
        this.tileEntity = container.getTile();
        this.container = container;
        this.guiRoot = new GuiElement(0, 0, this.f_97726_, this.f_97727_);
        this.guiRoot.addChild(new GuiTexture(0, -13, 179, 128, containerTexture));
        this.guiRoot.addChild(new GuiTexture(0, 103, 179, 106, GuiTextures.playerInventory));
        this.compartmentButtons = new VerticalTabGroupGui(10, 26, this::changeCompartment, containerTexture, 0, 128, (String[]) IntStream.range(0, ForgedContainerBlockEntity.compartmentCount).mapToObj(i -> I18n.get("tetra.forged_container.compartment_" + i)).toArray(String[]::new));
        this.guiRoot.addChild(this.compartmentButtons);
        GuiRect slotTransitionElement = new GuiRect(12, 0, 152, 101, 0);
        slotTransitionElement.setOpacity(0.0F);
        this.guiRoot.addChild(slotTransitionElement);
        this.slotTransition = new AnimationChain(new KeyframeAnimation(30, slotTransitionElement).applyTo(new Applier.Opacity(0.3F)), new KeyframeAnimation(50, slotTransitionElement).applyTo(new Applier.Opacity(0.0F)));
    }

    private void changeCompartment(int index) {
        this.container.changeCompartment(index);
        this.compartmentButtons.setActive(index);
        this.slotTransition.stop();
        this.slotTransition.start();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        return this.guiRoot.onMouseClick((int) mouseX, (int) mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        this.guiRoot.onMouseRelease((int) mouseX, (int) mouseY, button);
        return true;
    }

    @Override
    public boolean charTyped(char typecChar, int keyCode) {
        this.compartmentButtons.keyTyped(typecChar);
        return false;
    }

    @Override
    public void containerTick() {
        super.containerTick();
        int size = ForgedContainerBlockEntity.compartmentSize;
        this.tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(itemHandler -> {
            for (int i = 0; i < ForgedContainerBlockEntity.compartmentCount; i++) {
                boolean hasContent = false;
                for (int j = 0; j < size; j++) {
                    if (!itemHandler.getStackInSlot(i * size + j).isEmpty()) {
                        hasContent = true;
                        break;
                    }
                }
                this.compartmentButtons.setHasContent(i, hasContent);
            }
        });
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.m_280273_(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.m_280072_(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.f_96543_ - this.f_97726_) / 2;
        int y = (this.f_96544_ - this.f_97727_) / 2;
        this.guiRoot.updateFocusState(x, y, mouseX, mouseY);
        this.guiRoot.draw(graphics, x, y, this.f_96543_, this.f_96544_, mouseX, mouseY, 1.0F);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    }
}