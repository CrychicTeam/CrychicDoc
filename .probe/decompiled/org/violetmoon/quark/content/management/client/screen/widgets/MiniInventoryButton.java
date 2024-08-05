package org.violetmoon.quark.content.management.client.screen.widgets;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.client.handler.ClientUtil;
import org.violetmoon.quark.base.client.handler.InventoryButtonHandler;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class MiniInventoryButton extends Button {

    private final Supplier<List<Component>> tooltip;

    private InventoryButtonHandler.ButtonTargetType type = InventoryButtonHandler.ButtonTargetType.CONTAINER_INVENTORY;

    private final int spriteType;

    private final AbstractContainerScreen<?> parent;

    private final int startX;

    private final int startY;

    private BooleanSupplier shiftTexture = BooleanSuppliers.FALSE;

    @Deprecated(forRemoval = true)
    public MiniInventoryButton(AbstractContainerScreen<?> parent, int spriteType, int x, int y, Consumer<List<String>> legacyTooltip, Button.OnPress onPress) {
        this(parent, spriteType, x, y, (Supplier<List<Component>>) (() -> {
            List<String> toConsume = new ArrayList();
            legacyTooltip.accept(toConsume);
            return toConsume.stream().map(z -> Component.translatable(z)).toList();
        }), onPress);
    }

    public MiniInventoryButton(AbstractContainerScreen<?> parent, int spriteType, int x, int y, Supplier<List<Component>> tooltip, Button.OnPress onPress) {
        super(new Button.Builder(Component.literal(""), onPress).size(10, 10).pos(parent.getGuiLeft() + x, parent.getGuiTop() + y));
        this.parent = parent;
        this.spriteType = spriteType;
        this.tooltip = tooltip;
        this.startX = x;
        this.startY = y;
    }

    public MiniInventoryButton(AbstractContainerScreen<?> parent, int spriteType, int x, int y, Component tooltip, Button.OnPress onPress) {
        this(parent, spriteType, x, y, (Supplier<List<Component>>) (() -> List.of(tooltip)), onPress);
    }

    public MiniInventoryButton(AbstractContainerScreen<?> parent, int spriteType, int x, int y, String tooltipKey, Button.OnPress onPress) {
        this(parent, spriteType, x, y, Component.translatable(tooltipKey), onPress);
    }

    public MiniInventoryButton setType(InventoryButtonHandler.ButtonTargetType type) {
        this.type = type;
        return this;
    }

    public MiniInventoryButton setTextureShift(BooleanSupplier func) {
        this.shiftTexture = func;
        return this;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        int targetX = this.startX + (Integer) this.type.offX.get() + this.parent.getGuiLeft();
        int targetY = this.startY + (Integer) this.type.offY.get() + this.parent.getGuiTop();
        this.m_252865_(targetX);
        this.m_253211_(targetY);
        super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        int u = this.spriteType * this.f_93618_;
        int v = 25 + (this.f_93622_ ? this.f_93619_ : 0);
        if (this.shiftTexture.getAsBoolean()) {
            v += this.f_93619_ * 2;
        }
        guiGraphics.blit(ClientUtil.GENERAL_ICONS, this.m_252754_(), this.m_252907_(), u, v, this.f_93618_, this.f_93619_);
        if (this.f_93622_) {
            guiGraphics.renderComponentTooltip(Minecraft.getInstance().font, (List<Component>) this.tooltip.get(), mouseX, mouseY);
        }
    }

    @NotNull
    @Override
    protected MutableComponent createNarrationMessage() {
        List<Component> resolvedTooltip = (List<Component>) this.tooltip.get();
        return resolvedTooltip.isEmpty() ? Component.literal("") : Component.translatable("gui.narrate.button", resolvedTooltip.get(0));
    }
}