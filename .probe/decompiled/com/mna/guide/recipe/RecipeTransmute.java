package com.mna.guide.recipe;

import com.mna.api.guidebook.RecipeRendererBase;
import com.mna.api.tools.MATags;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.GuiTextures;
import com.mna.network.ClientMessageDispatcher;
import com.mna.recipes.manaweaving.TransmutationRecipe;
import com.mna.tools.loot.LootDrop;
import com.mna.tools.loot.LootTableCache;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.apache.commons.lang3.mutable.MutableInt;

public class RecipeTransmute extends RecipeRendererBase {

    private TransmutationRecipe recipe;

    private List<ItemStack> targetBlock;

    private List<ItemStack> replaceBlock;

    private List<LootDrop> drops;

    private boolean waitingForServer = false;

    private ResourceLocation lootTableID = null;

    public RecipeTransmute(int xIn, int yIn) {
        super(xIn, yIn);
    }

    @Override
    protected void drawForeground(GuiGraphics pGuiGraphics, int x, int y, int mouseX, int mouseY, float partialTicks) {
        if (this.recipe != null && (this.targetBlock != null || this.drops != null)) {
            if (this.targetBlock != null && this.targetBlock.size() > 0) {
                this.renderItemStack(pGuiGraphics, this.targetBlock, (int) ((float) this.m_252754_() / this.scale + 33.0F), (int) ((float) this.m_252907_() / this.scale + 119.0F), 1.05F);
            }
            if (this.replaceBlock != null && this.replaceBlock.size() > 0) {
                this.renderReplaceBlock(pGuiGraphics);
            } else if (this.waitingForServer || this.drops != null && this.drops.size() > 0) {
                this.renderDrops(pGuiGraphics, x, y);
            }
            int tier = this.recipe.getTier();
            MutableInt playerTier = new MutableInt(0);
            this.minecraft.player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> playerTier.setValue(p.getTier()));
            int color = tier <= playerTier.getValue() ? FastColor.ARGB32.color(255, 0, 128, 0) : FastColor.ARGB32.color(255, 255, 0, 0);
            Component name = Component.translatable(this.recipe.getResultItem().getDescriptionId().toString());
            Component tierPrompt = Component.translatable("gui.mna.item-tier", tier);
            Component tooltipPrompt = Component.translatable("cantrip.mna.transmute.jei");
            int stringWidth = this.minecraft.font.width(tooltipPrompt);
            int textX = x + this.f_93618_ / 2 - stringWidth / 2;
            int textY = y + 25;
            pGuiGraphics.drawString(this.minecraft.font, tooltipPrompt, textX, textY, color, false);
            stringWidth = this.minecraft.font.width(name);
            textX = x + this.f_93618_ / 2 - stringWidth / 2;
            textY = y + 5;
            pGuiGraphics.drawString(this.minecraft.font, name, textX, textY, FastColor.ARGB32.color(255, 255, 255, 255), false);
            pGuiGraphics.drawString(this.minecraft.font, tierPrompt, x + this.f_93618_ / 2 - this.minecraft.font.width(tierPrompt) / 2, y + 15, color, false);
            if (this.recipe.getFactionRequirement() != null) {
                int xPadding = 3;
                this.renderFactionIcon(pGuiGraphics, this.recipe.getFactionRequirement(), textX + stringWidth + xPadding, textY);
            }
        }
    }

    private void renderReplaceBlock(GuiGraphics pGuiGraphics) {
        this.renderItemStack(pGuiGraphics, this.replaceBlock, (int) ((float) this.m_252754_() / this.scale + 168.0F), (int) ((float) this.m_252907_() / this.scale + 119.0F), 1.05F);
    }

    private void renderDrops(GuiGraphics pGuiGraphics, int x, int y) {
        int xPos = 32;
        int yPos = 176;
        if (this.waitingForServer) {
            MutableComponent c = Component.translatable("gui.mna.waiting-for-server");
            pGuiGraphics.drawString(this.minecraft.font, c, (int) ((float) this.m_252754_() / this.scale + (float) (this.m_5711_() / 2) - (float) (this.minecraft.font.width(c) / 2)), (int) ((float) this.m_252907_() / this.scale + (float) yPos + 3.0F), 16711680, false);
            if (this.minecraft.level.m_46467_() % 20L == 0L) {
                List<LootDrop> drops = LootTableCache.getLoot(this.recipe.getLootTable());
                if (drops != null) {
                    this.drops = drops;
                    this.waitingForServer = false;
                }
            }
        } else {
            for (LootDrop drop : this.drops) {
                if (drop.minDrop == drop.maxDrop) {
                    this.renderItemStack(pGuiGraphics, drop.item, (int) ((float) this.m_252754_() / this.scale + (float) xPos), (int) ((float) this.m_252907_() / this.scale + (float) yPos), 0.0F, 0.0F, 1.05F, drop.getTooltipText());
                    String s = drop.toString();
                    int width = this.minecraft.font.width(s);
                    pGuiGraphics.drawString(this.minecraft.font, s, x + xPos - width / 2 + 10, y + yPos + 14, 4210752, false);
                } else {
                    this.renderItemStack(pGuiGraphics, drop.item, (int) ((float) this.m_252754_() / this.scale + (float) xPos), (int) ((float) this.m_252907_() / this.scale + (float) yPos), 0.0F, 0.0F, 1.05F, drop.getTooltipText());
                    pGuiGraphics.drawString(this.minecraft.font, drop.toString(), (int) ((float) this.m_252754_() + (float) xPos * this.scale), (int) ((float) this.m_252907_() + (float) yPos * this.scale), 4210752, false);
                }
                xPos += 20;
                if (xPos >= 180) {
                    xPos = 32;
                    yPos += 24;
                }
            }
        }
    }

    @Override
    protected ResourceLocation backgroundTexture() {
        return this.replaceBlock != null ? GuiTextures.Recipe.TRANSMUTATION_SINGLE : GuiTextures.Recipe.TRANSMUTATION_MULTI;
    }

    @Override
    public void init_internal(ResourceLocation recipeLocation) {
        Optional<? extends Recipe<?>> pattern = this.minecraft.level.getRecipeManager().byKey(recipeLocation);
        if (pattern.isPresent() && pattern.get() instanceof TransmutationRecipe) {
            this.recipe = (TransmutationRecipe) pattern.get();
            if (this.recipe != null) {
                this.targetBlock = (List<ItemStack>) MATags.smartLookupItem(this.recipe.getTargetBlock()).stream().map(i -> new ItemStack(i)).collect(Collectors.toList());
                if (this.recipe.getReplaceBlock() != null) {
                    this.replaceBlock = (List<ItemStack>) MATags.smartLookupItem(this.recipe.getReplaceBlock()).stream().map(i -> new ItemStack(i)).collect(Collectors.toList());
                } else if (this.recipe.getLootTable() != null) {
                    List<LootDrop> cached = LootTableCache.getLoot(this.recipe.getLootTable());
                    if (cached == null) {
                        this.waitingForServer = true;
                        ClientMessageDispatcher.requestLootTableDrops(this.recipe.getLootTable());
                    } else {
                        this.drops = cached;
                    }
                }
            }
        }
    }

    @Override
    public int getTier() {
        return this.recipe != null ? this.recipe.getTier() : 1;
    }
}