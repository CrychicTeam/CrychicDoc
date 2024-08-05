package noppes.npcs.client.gui.player;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.controllers.RecipeController;
import noppes.npcs.controllers.data.RecipeCarpentry;
import noppes.npcs.shared.client.gui.components.GuiButtonNextPage;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;

@OnlyIn(Dist.CLIENT)
public class GuiRecipes extends GuiNPCInterface {

    private static final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/slot.png");

    private int page = 0;

    private boolean npcRecipes = true;

    private GuiLabel label;

    private GuiButtonNop left;

    private GuiButtonNop right;

    private List<Recipe> recipes = new ArrayList();

    public GuiRecipes() {
        this.imageHeight = 182;
        this.imageWidth = 256;
        this.setBackground("recipes.png");
        this.recipes.addAll(RecipeController.instance.anvilRecipes.values());
    }

    @Override
    public void init() {
        super.m_7856_();
        this.addLabel(new GuiLabel(0, "Recipe List", this.guiLeft + 5, this.guiTop + 5));
        this.addLabel(this.label = new GuiLabel(1, "", this.guiLeft + 5, this.guiTop + 168));
        this.addButton(this.left = new GuiButtonNextPage(this, 1, this.guiLeft + 150, this.guiTop + 164, true, b -> {
            this.page++;
            this.updateButton();
        }));
        this.addButton(this.right = new GuiButtonNextPage(this, 2, this.guiLeft + 80, this.guiTop + 164, false, b -> {
            this.page--;
            this.updateButton();
        }));
        this.updateButton();
    }

    private void updateButton() {
        this.right.f_93624_ = this.right.f_93623_ = this.page > 0;
        this.left.f_93624_ = this.left.f_93623_ = this.page + 1 < Mth.ceil((float) this.recipes.size() / 4.0F);
    }

    @Override
    public void render(GuiGraphics graphics, int xMouse, int yMouse, float f) {
        super.m_88315_(graphics, xMouse, yMouse, f);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, resource);
        this.label.m_93666_(Component.literal(this.page + 1 + "/" + Mth.ceil((float) this.recipes.size() / 4.0F)));
        this.label.m_252865_(this.guiLeft + (256 - Minecraft.getInstance().font.width(this.label.m_6035_())) / 2);
        for (int i = 0; i < 4; i++) {
            int index = i + this.page * 4;
            if (index >= this.recipes.size()) {
                break;
            }
            Recipe irecipe = (Recipe) this.recipes.get(index);
            if (!irecipe.getResultItem(this.player.m_9236_().registryAccess()).isEmpty()) {
                int x = this.guiLeft + 5 + i / 2 * 126;
                int y = this.guiTop + 15 + i % 2 * 76;
                this.drawItem(graphics, irecipe.getResultItem(this.player.m_9236_().registryAccess()), x + 98, y + 28, xMouse, yMouse);
                if (irecipe instanceof RecipeCarpentry recipe) {
                    x += (72 - recipe.getRecipeWidth() * 18) / 2;
                    y += (72 - recipe.getRecipeHeight() * 18) / 2;
                    for (int j = 0; j < recipe.getRecipeWidth(); j++) {
                        for (int k = 0; k < recipe.getRecipeHeight(); k++) {
                            RenderSystem.setShader(GameRenderer::m_172817_);
                            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                            RenderSystem.setShaderTexture(0, resource);
                            graphics.blit(resource, x + j * 18, y + k * 18, 0, 0, 18, 18);
                            ItemStack item = recipe.getCraftingItem(j + k * recipe.getRecipeWidth());
                            if (!item.isEmpty()) {
                                this.drawItem(graphics, item, x + j * 18 + 1, y + k * 18 + 1, xMouse, yMouse);
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            int indexx = i + this.page * 4;
            if (indexx >= this.recipes.size()) {
                break;
            }
            Recipe irecipe = (Recipe) this.recipes.get(indexx);
            if (irecipe instanceof RecipeCarpentry recipe && !recipe.getResultItem(this.player.m_9236_().registryAccess()).isEmpty()) {
                int x = this.guiLeft + 5 + i / 2 * 126;
                int y = this.guiTop + 15 + i % 2 * 76;
                this.drawOverlay(graphics, recipe.getResultItem(this.player.m_9236_().registryAccess()), x + 98, y + 22, xMouse, yMouse);
                x += (72 - recipe.getRecipeWidth() * 18) / 2;
                y += (72 - recipe.getRecipeHeight() * 18) / 2;
                for (int j = 0; j < recipe.getRecipeWidth(); j++) {
                    for (int kx = 0; kx < recipe.getRecipeHeight(); kx++) {
                        ItemStack item = recipe.getCraftingItem(j + kx * recipe.getRecipeWidth());
                        if (!item.isEmpty()) {
                            this.drawOverlay(graphics, item, x + j * 18 + 1, y + kx * 18 + 1, xMouse, yMouse);
                        }
                    }
                }
            }
        }
    }

    private void drawItem(GuiGraphics graphics, ItemStack item, int x, int y, int xMouse, int yMouse) {
        graphics.pose().pushPose();
        graphics.pose().translate(0.0, 0.0, 100.0);
        graphics.renderItem(item, x, y);
        graphics.renderItemDecorations(this.f_96547_, item, x, y);
        graphics.pose().popPose();
    }

    private void drawOverlay(GuiGraphics graphics, ItemStack item, int x, int y, int xMouse, int yMouse) {
        if (this.func_146978_c(x - this.guiLeft, y - this.guiTop, 16, 16, xMouse, yMouse)) {
            graphics.renderTooltip(this.f_96547_, item, xMouse, yMouse);
        }
    }

    protected boolean func_146978_c(int p_146978_1_, int p_146978_2_, int p_146978_3_, int p_146978_4_, int p_146978_5_, int p_146978_6_) {
        int k1 = this.guiLeft;
        int l1 = this.guiTop;
        p_146978_5_ -= k1;
        p_146978_6_ -= l1;
        return p_146978_5_ >= p_146978_1_ - 1 && p_146978_5_ < p_146978_1_ + p_146978_3_ + 1 && p_146978_6_ >= p_146978_2_ - 1 && p_146978_6_ < p_146978_2_ + p_146978_4_ + 1;
    }

    @Override
    public void save() {
    }
}