package com.mna.api.guidebook;

import com.google.common.collect.UnmodifiableIterator;
import com.mna.api.ManaAndArtificeMod;
import com.mna.api.faction.IFaction;
import com.mna.gui.GuiTextures;
import com.mna.guide.recipe.ICyclingRecipeRenderer;
import com.mna.recipes.AMRecipeBase;
import com.mna.recipes.RecipeByproduct;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

public abstract class RecipeRendererBase extends AbstractWidget {

    private static final int CYCLE_TICKS = 40;

    protected Minecraft minecraft;

    private ResourceLocation[] rLocs;

    private boolean initialized = false;

    private ResourceLocation registryName;

    private int curMouseX = 0;

    private int curMouseY = 0;

    private static final int ITEMSTACK_WIDTH = 16;

    private Consumer<List<Component>> tooltipFunction;

    public int lockIndex = -1;

    protected float scale = 1.0F;

    private boolean disablePaperBackground = false;

    public RecipeRendererBase(int xIn, int yIn) {
        this(xIn, yIn, 218, 256);
    }

    public RecipeRendererBase(int xIn, int yIn, int xSize, int ySize) {
        super(xIn, yIn, xSize, ySize, Component.literal(""));
        this.minecraft = Minecraft.getInstance();
        this.rLocs = new ResourceLocation[0];
    }

    public final ResourceLocation getRegistryName() {
        return this.registryName;
    }

    public final ResourceLocation[] getRecipeIds() {
        return this.rLocs;
    }

    public final void setTooltipFunction(Consumer<List<Component>> tooltipFunction) {
        this.tooltipFunction = tooltipFunction;
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.f_93625_);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(this.scale, this.scale, this.scale);
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        int i = (int) ((float) this.m_252754_() / this.scale);
        int j = (int) ((float) this.m_252907_() / this.scale);
        if (!this.disablePaperBackground) {
            pGuiGraphics.blit(GuiTextures.Items.SPELL_RECIPE, i + 3, j, 0.0F, 0.0F, this.f_93618_, this.f_93619_, 256, 256);
        }
        if (this.backgroundTexture() != null) {
            pGuiGraphics.blit(this.backgroundTexture(), i, j, 0.0F, 0.0F, this.f_93618_, this.f_93619_, 218, 256);
        }
        this.curMouseX = (int) ((float) mouseX / this.scale);
        this.curMouseY = (int) ((float) mouseY / this.scale);
        this.drawForeground(pGuiGraphics, i, j, this.curMouseX, this.curMouseY, partialTicks);
        pGuiGraphics.pose().popPose();
        RenderSystem.disableBlend();
    }

    @Override
    protected boolean isValidClickButton(int p_isValidClickButton_1_) {
        return false;
    }

    protected abstract void drawForeground(GuiGraphics var1, int var2, int var3, int var4, int var5, float var6);

    protected final void renderItemStack(GuiGraphics pGuiGraphics, ItemStack stackToRender, int x, int y) {
        this.renderItemStack(pGuiGraphics, Arrays.asList(stackToRender), x, y, 1.0F);
    }

    protected final void renderItemStack(GuiGraphics pGuiGraphics, ItemStack stackToRender, int x, int y, float scale) {
        this.renderItemStack(pGuiGraphics, Arrays.asList(stackToRender), x, y, scale);
    }

    protected final void renderItemStack(GuiGraphics pGuiGraphics, List<ItemStack> stackToRender, int x, int y) {
        this.renderItemStack(pGuiGraphics, stackToRender, x, y, 1.0F);
    }

    protected final void renderItemStack(GuiGraphics pGuiGraphics, List<ItemStack> stackToRender, int x, int y, float scale) {
        this.renderItemStack(pGuiGraphics, stackToRender, x, y, 0.0F, 0.0F, scale, Arrays.asList());
    }

    protected final void renderItemStack(GuiGraphics pGuiGraphics, ItemStack stackToRender, int x, int y, float nudgeX, float nudgeY, float scale, List<Component> additionalTooltip) {
        this.renderItemStack(pGuiGraphics, Arrays.asList(stackToRender), x, y, nudgeX, nudgeY, scale, additionalTooltip);
    }

    protected final void renderItemStack(GuiGraphics pGuiGraphics, List<ItemStack> stackToRender, int x, int y, float nudgeX, float nudgeY, float scale, List<Component> additionalTooltip) {
        if (stackToRender != null && stackToRender.size() != 0) {
            ItemStack stack = this.getCurrentIndex(stackToRender);
            if (stack.getItem() == Items.POTION && stack.getTag() == null) {
                stack = PotionUtils.setPotion(stack, Potions.WATER);
            }
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().scale(scale, scale, scale);
            pGuiGraphics.pose().translate(nudgeX, nudgeY, 0.0F);
            pGuiGraphics.renderItem(stack, (int) ((float) x / scale), (int) ((float) y / scale));
            if (stack.getCount() > 1) {
                pGuiGraphics.drawString(this.minecraft.font, "x" + stack.getCount(), x + 16, y + 16 - 9, 4210752, false);
            }
            pGuiGraphics.pose().popPose();
            int adjustedX = (int) ((float) x + 16.0F * scale);
            int adjustedY = (int) ((float) y + 16.0F * scale);
            if (this.tooltipFunction != null && adjustedX >= this.curMouseX && (float) adjustedX <= (float) this.curMouseX + 16.0F * scale && adjustedY >= this.curMouseY && (float) adjustedY <= (float) this.curMouseY + 16.0F * scale) {
                List<Component> toolTip = stack.getTooltipLines(null, TooltipFlag.Default.f_256752_);
                ArrayList<Component> tt = new ArrayList();
                tt.addAll(toolTip);
                tt.addAll(additionalTooltip);
                this.tooltipFunction.accept(tt);
            }
        }
    }

    protected final void renderByproducts(GuiGraphics pGuiGraphics, int posX, int posY, AMRecipeBase recipe) {
        this.renderByproducts(pGuiGraphics, posX, posY, recipe, false);
    }

    protected final void renderByproducts(GuiGraphics pGuiGraphics, int posX, int posY, AMRecipeBase recipe, boolean plusOnLeft) {
        if (recipe.getByproducts().size() != 0) {
            RenderSystem.enableBlend();
            this.m_280322_(pGuiGraphics, GuiTextures.Widgets.GUIDE_WIDGETS, (int) ((float) this.m_252754_() / this.scale + (float) posX), (int) ((float) this.m_252907_() / this.scale + (float) posY), 39, 0, 0, 50, 50, 256, 256);
            if (plusOnLeft) {
                pGuiGraphics.drawString(this.minecraft.font, "+", (float) this.m_252754_() / this.scale - 5.0F + (float) posX - (float) (this.minecraft.font.width("+") / 2), (float) this.m_252907_() / this.scale + 30.0F + (float) posY - 9.0F, -12105913, false);
            } else {
                pGuiGraphics.drawString(this.minecraft.font, "+", (float) this.m_252754_() / this.scale + (float) posX + 25.0F - (float) (this.minecraft.font.width("+") / 2), (float) this.m_252907_() / this.scale + (float) posY - 9.0F, -12105913, false);
            }
            int x = 0;
            int y = 0;
            int count = 0;
            int step = 24;
            float scale_text = 0.7F;
            float scale_item = 0.7F;
            int item_offset_x = (int) (7.142857F * this.scale);
            UnmodifiableIterator var13 = recipe.getByproducts().iterator();
            while (var13.hasNext()) {
                RecipeByproduct byproduct = (RecipeByproduct) var13.next();
                pGuiGraphics.pose().pushPose();
                pGuiGraphics.pose().scale(0.7F, 0.7F, 0.7F);
                pGuiGraphics.renderItem(byproduct.stack, (int) (((float) this.m_252754_() / this.scale + (float) posX + (float) x + (float) item_offset_x) / 0.7F), (int) (((float) this.m_252907_() / this.scale + (float) posY + (float) y + 2.0F) / 0.7F));
                pGuiGraphics.pose().popPose();
                pGuiGraphics.pose().pushPose();
                pGuiGraphics.pose().scale(0.7F, 0.7F, 0.7F);
                pGuiGraphics.drawString(this.minecraft.font, String.format("%.0f%%", byproduct.chance * 100.0F), (int) (((float) this.m_252754_() / this.scale + (float) posX + (float) x + 22.0F) / 0.7F), (int) (((float) this.m_252907_() / this.scale + (float) posY + (float) y + 15.0F - 9.0F) / 0.7F), -12105913, false);
                pGuiGraphics.pose().popPose();
                y += 24;
                if (++count > 6) {
                    break;
                }
            }
            RenderSystem.disableBlend();
        }
    }

    private ItemStack getCurrentIndex(List<ItemStack> stacks) {
        return stacks.size() == 1 ? (ItemStack) stacks.get(0) : (ItemStack) stacks.get((int) (this.minecraft.level.m_46467_() / 40L % (long) stacks.size()));
    }

    protected abstract ResourceLocation backgroundTexture();

    protected abstract void init_internal(ResourceLocation var1);

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void disablePaperBackground() {
        this.disablePaperBackground = true;
    }

    public void init(ResourceLocation[] recipeLocations) {
        if (!this.initialized) {
            this.registryName = recipeLocations[0];
            this.rLocs = recipeLocations;
            if (this instanceof ICyclingRecipeRenderer) {
                ((ICyclingRecipeRenderer) this).init_cycling(this.rLocs);
            } else {
                this.init_internal(this.rLocs[0]);
            }
        }
    }

    public RecipeRendererBase clone(int x, int y, boolean lock_index) {
        Class<? extends RecipeRendererBase> clazz = this.getClass();
        try {
            RecipeRendererBase inst = (RecipeRendererBase) clazz.getDeclaredConstructor(int.class, int.class).newInstance(x, y);
            inst.init(this.rLocs);
            if (lock_index && this instanceof ICyclingRecipeRenderer) {
                inst.lockIndex = ((ICyclingRecipeRenderer) this).getIndex();
            }
            return inst;
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof RecipeRendererBase other) {
            if (this.rLocs.length != other.rLocs.length) {
                return false;
            } else {
                for (int i = 0; i < this.rLocs.length; i++) {
                    if (!this.rLocs[i].equals(other.rLocs[i])) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    public abstract int getTier();

    public void renderFactionIcon(GuiGraphics pGuiGraphics, IFaction faction, int x, int y) {
        ManaAndArtificeMod.getGuiRenderHelper().renderFactionIcon(pGuiGraphics, faction, x, y);
    }

    public Consumer<List<Component>> getTooltipFunction() {
        return this.tooltipFunction;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
    }
}