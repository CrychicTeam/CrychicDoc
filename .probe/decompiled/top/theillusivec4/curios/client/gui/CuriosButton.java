package top.theillusivec4.curios.client.gui;

import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.client.ICuriosScreen;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.client.CPacketOpenCurios;
import top.theillusivec4.curios.common.network.client.CPacketOpenVanilla;

public class CuriosButton extends ImageButton {

    private final AbstractContainerScreen<?> parentGui;

    CuriosButton(AbstractContainerScreen<?> parentGui, int xIn, int yIn, int widthIn, int heightIn, int textureOffsetX, int textureOffsetY, int yDiffText, ResourceLocation resource) {
        super(xIn, yIn, widthIn, heightIn, textureOffsetX, textureOffsetY, yDiffText, resource, button -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                ItemStack stack = mc.player.f_36096_.getCarried();
                mc.player.f_36096_.setCarried(ItemStack.EMPTY);
                if (parentGui instanceof ICuriosScreen) {
                    InventoryScreen inventory = new InventoryScreen(mc.player);
                    mc.setScreen(inventory);
                    mc.player.f_36096_.setCarried(stack);
                    NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CPacketOpenVanilla(stack));
                } else {
                    if (parentGui instanceof InventoryScreen inventory) {
                        RecipeBookComponent recipeBookGui = inventory.getRecipeBookComponent();
                        if (recipeBookGui.isVisible()) {
                            recipeBookGui.toggleVisibility();
                        }
                    }
                    NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CPacketOpenCurios(stack));
                }
            }
        });
        this.parentGui = parentGui;
    }

    @Override
    public void renderWidget(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        Tuple<Integer, Integer> offsets = CuriosScreen.getButtonOffset(this.parentGui instanceof CreativeModeInventoryScreen);
        this.m_252865_(this.parentGui.getGuiLeft() + offsets.getA());
        int yOffset = this.parentGui instanceof CreativeModeInventoryScreen ? 68 : 83;
        this.m_253211_(this.parentGui.getGuiTop() + offsets.getB() + yOffset);
        if (this.parentGui instanceof CreativeModeInventoryScreen gui) {
            boolean isInventoryTab = gui.isInventoryOpen();
            this.f_93623_ = isInventoryTab;
            if (!isInventoryTab) {
                return;
            }
        }
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);
    }
}