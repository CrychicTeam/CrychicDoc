package net.blay09.mods.waystones.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Objects;
import java.util.function.Supplier;
import net.blay09.mods.balm.mixin.AbstractContainerScreenAccessor;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class WaystoneInventoryButton extends Button {

    private static final ResourceLocation INVENTORY_BUTTON_TEXTURE = new ResourceLocation("waystones", "textures/gui/inventory_button.png");

    private final AbstractContainerScreen<?> parentScreen;

    private final ItemStack iconItem;

    private final ItemStack iconItemHovered;

    private final Supplier<Boolean> visiblePredicate;

    private final Supplier<Integer> xPosition;

    private final Supplier<Integer> yPosition;

    public WaystoneInventoryButton(AbstractContainerScreen<?> parentScreen, Button.OnPress pressable, Supplier<Boolean> visiblePredicate, Supplier<Integer> xPosition, Supplier<Integer> yPosition) {
        super(0, 0, 16, 16, Component.empty(), pressable, Button.DEFAULT_NARRATION);
        this.parentScreen = parentScreen;
        this.visiblePredicate = visiblePredicate;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.iconItem = new ItemStack(ModItems.boundScroll);
        this.iconItemHovered = new ItemStack(ModItems.warpScroll);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.f_93624_ = (Boolean) this.visiblePredicate.get();
        super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (this.f_93624_) {
            this.m_252865_(((AbstractContainerScreenAccessor) this.parentScreen).getLeftPos() + (Integer) this.xPosition.get());
            this.m_253211_(((AbstractContainerScreenAccessor) this.parentScreen).getTopPos() + (Integer) this.yPosition.get());
            this.f_93622_ = mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.f_93618_ && mouseY < this.m_252907_() + this.f_93619_;
            Player player = Minecraft.getInstance().player;
            if (PlayerWaystoneManager.canUseInventoryButton((Player) Objects.requireNonNull(player))) {
                ItemStack icon = this.f_93622_ ? this.iconItemHovered : this.iconItem;
                guiGraphics.renderItem(icon, this.m_252754_(), this.m_252907_());
                guiGraphics.renderItemDecorations(Minecraft.getInstance().font, icon, this.m_252754_(), this.m_252907_());
            } else {
                RenderSystem.enableBlend();
                guiGraphics.setColor(1.0F, 1.0F, 1.0F, 0.5F);
                guiGraphics.blit(INVENTORY_BUTTON_TEXTURE, this.m_252754_(), this.m_252907_(), 0.0F, 0.0F, 16, 16, 16, 16);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.disableBlend();
            }
        }
    }
}