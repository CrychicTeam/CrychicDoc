package mezz.jei.library.render;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientTypeWithSubtypes;
import mezz.jei.common.platform.IPlatformFluidHelperInternal;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.joml.Matrix4f;

public class FluidTankRenderer<T> implements IIngredientRenderer<T> {

    private static final NumberFormat nf = NumberFormat.getIntegerInstance();

    private static final int TEXTURE_SIZE = 16;

    private static final int MIN_FLUID_HEIGHT = 1;

    private final IPlatformFluidHelperInternal<T> fluidHelper;

    private final long capacity;

    private final FluidTankRenderer.TooltipMode tooltipMode;

    private final int width;

    private final int height;

    public FluidTankRenderer(IPlatformFluidHelperInternal<T> fluidHelper) {
        this(fluidHelper, fluidHelper.bucketVolume(), FluidTankRenderer.TooltipMode.ITEM_LIST, 16, 16);
    }

    public FluidTankRenderer(IPlatformFluidHelperInternal<T> fluidHelper, long capacity, boolean showCapacity, int width, int height) {
        this(fluidHelper, capacity, showCapacity ? FluidTankRenderer.TooltipMode.SHOW_AMOUNT_AND_CAPACITY : FluidTankRenderer.TooltipMode.SHOW_AMOUNT, width, height);
    }

    private FluidTankRenderer(IPlatformFluidHelperInternal<T> fluidHelper, long capacity, FluidTankRenderer.TooltipMode tooltipMode, int width, int height) {
        Preconditions.checkArgument(capacity > 0L, "capacity must be > 0");
        Preconditions.checkArgument(width > 0, "width must be > 0");
        Preconditions.checkArgument(height > 0, "height must be > 0");
        this.fluidHelper = fluidHelper;
        this.capacity = capacity;
        this.tooltipMode = tooltipMode;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(GuiGraphics guiGraphics, T fluidStack) {
        RenderSystem.enableBlend();
        this.drawFluid(guiGraphics, this.width, this.height, fluidStack);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }

    private void drawFluid(GuiGraphics guiGraphics, int width, int height, T fluidStack) {
        IIngredientTypeWithSubtypes<Fluid, T> type = this.fluidHelper.getFluidIngredientType();
        Fluid fluid = type.getBase(fluidStack);
        if (!fluid.isSame(Fluids.EMPTY)) {
            this.fluidHelper.getStillFluidSprite(fluidStack).ifPresent(fluidStillSprite -> {
                int fluidColor = this.fluidHelper.getColorTint(fluidStack);
                long amount = this.fluidHelper.getAmount(fluidStack);
                long scaledAmount = amount * (long) height / this.capacity;
                if (amount > 0L && scaledAmount < 1L) {
                    scaledAmount = 1L;
                }
                if (scaledAmount > (long) height) {
                    scaledAmount = (long) height;
                }
                drawTiledSprite(guiGraphics, width, height, fluidColor, scaledAmount, fluidStillSprite);
            });
        }
    }

    private static void drawTiledSprite(GuiGraphics guiGraphics, int tiledWidth, int tiledHeight, int color, long scaledAmount, TextureAtlasSprite sprite) {
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        Matrix4f matrix = guiGraphics.pose().last().pose();
        setGLColorFromInt(color);
        int xTileCount = tiledWidth / 16;
        int xRemainder = tiledWidth - xTileCount * 16;
        long yTileCount = scaledAmount / 16L;
        long yRemainder = scaledAmount - yTileCount * 16L;
        int yStart = tiledHeight;
        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            for (int yTile = 0; (long) yTile <= yTileCount; yTile++) {
                int width = xTile == xTileCount ? xRemainder : 16;
                long height = (long) yTile == yTileCount ? yRemainder : 16L;
                int x = xTile * 16;
                int y = yStart - (yTile + 1) * 16;
                if (width > 0 && height > 0L) {
                    long maskTop = 16L - height;
                    int maskRight = 16 - width;
                    drawTextureWithMasking(matrix, (float) x, (float) y, sprite, maskTop, (long) maskRight, 100.0F);
                }
            }
        }
    }

    private static void setGLColorFromInt(int color) {
        float red = (float) (color >> 16 & 0xFF) / 255.0F;
        float green = (float) (color >> 8 & 0xFF) / 255.0F;
        float blue = (float) (color & 0xFF) / 255.0F;
        float alpha = (float) (color >> 24 & 0xFF) / 255.0F;
        RenderSystem.setShaderColor(red, green, blue, alpha);
    }

    private static void drawTextureWithMasking(Matrix4f matrix, float xCoord, float yCoord, TextureAtlasSprite textureSprite, long maskTop, long maskRight, float zLevel) {
        float uMin = textureSprite.getU0();
        float uMax = textureSprite.getU1();
        float vMin = textureSprite.getV0();
        float vMax = textureSprite.getV1();
        uMax -= (float) maskRight / 16.0F * (uMax - uMin);
        vMax -= (float) maskTop / 16.0F * (vMax - vMin);
        RenderSystem.setShader(GameRenderer::m_172817_);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.m_252986_(matrix, xCoord, yCoord + 16.0F, zLevel).uv(uMin, vMax).endVertex();
        bufferBuilder.m_252986_(matrix, xCoord + 16.0F - (float) maskRight, yCoord + 16.0F, zLevel).uv(uMax, vMax).endVertex();
        bufferBuilder.m_252986_(matrix, xCoord + 16.0F - (float) maskRight, yCoord + (float) maskTop, zLevel).uv(uMax, vMin).endVertex();
        bufferBuilder.m_252986_(matrix, xCoord, yCoord + (float) maskTop, zLevel).uv(uMin, vMin).endVertex();
        tessellator.end();
    }

    @Override
    public List<Component> getTooltip(T fluidStack, TooltipFlag tooltipFlag) {
        IIngredientTypeWithSubtypes<Fluid, T> type = this.fluidHelper.getFluidIngredientType();
        Fluid fluidType = type.getBase(fluidStack);
        if (fluidType.isSame(Fluids.EMPTY)) {
            return new ArrayList();
        } else {
            List<Component> tooltip = this.fluidHelper.getTooltip(fluidStack, tooltipFlag);
            long amount = this.fluidHelper.getAmount(fluidStack);
            long milliBuckets = amount * 1000L / this.fluidHelper.bucketVolume();
            if (this.tooltipMode == FluidTankRenderer.TooltipMode.SHOW_AMOUNT_AND_CAPACITY) {
                MutableComponent amountString = Component.translatable("jei.tooltip.liquid.amount.with.capacity", nf.format(milliBuckets), nf.format(this.capacity));
                tooltip.add(amountString.withStyle(ChatFormatting.GRAY));
            } else if (this.tooltipMode == FluidTankRenderer.TooltipMode.SHOW_AMOUNT) {
                MutableComponent amountString = Component.translatable("jei.tooltip.liquid.amount", nf.format(milliBuckets));
                tooltip.add(amountString.withStyle(ChatFormatting.GRAY));
            }
            return tooltip;
        }
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    static enum TooltipMode {

        SHOW_AMOUNT, SHOW_AMOUNT_AND_CAPACITY, ITEM_LIST
    }
}