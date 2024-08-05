package fr.frinn.custommachinery.client.integration.jei;

import com.mojang.datafixers.util.Pair;
import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import fr.frinn.custommachinery.api.integration.jei.IDisplayInfo;
import fr.frinn.custommachinery.common.machine.CustomMachine;
import fr.frinn.custommachinery.impl.util.TextureSizeHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class RequirementDisplayInfo implements IDisplayInfo {

    private static final RequirementDisplayInfo.IDisplayInfoRenderer DEFAULT_RENDERER = new RequirementDisplayInfo.Texture(new ResourceLocation("custommachinery", "textures/gui/creation/create_icon.png"), 10, 10, 0, 0);

    private RequirementDisplayInfo.IDisplayInfoRenderer renderer = DEFAULT_RENDERER;

    private final List<Pair<Component, IDisplayInfo.TooltipPredicate>> tooltips = new ArrayList();

    private IDisplayInfo.ClickAction clickAction;

    public RequirementDisplayInfo setTextureIcon(ResourceLocation icon, int width, int height, int u, int v) {
        this.renderer = new RequirementDisplayInfo.Texture(icon, width, height, u, v);
        return this;
    }

    @Override
    public IDisplayInfo setSpriteIcon(ResourceLocation atlas, ResourceLocation sprite) {
        this.renderer = new RequirementDisplayInfo.Sprite(atlas, sprite);
        return this;
    }

    public RequirementDisplayInfo setItemIcon(ItemStack stack) {
        this.renderer = new RequirementDisplayInfo.Item(stack);
        return this;
    }

    public RequirementDisplayInfo addTooltip(Component tooltip, IDisplayInfo.TooltipPredicate predicate) {
        this.tooltips.add(Pair.of(tooltip, predicate));
        return this;
    }

    @Override
    public void setClickAction(IDisplayInfo.ClickAction clickAction) {
        this.clickAction = clickAction;
    }

    public void renderIcon(GuiGraphics graphics, int size) {
        this.renderer.render(graphics, size);
    }

    public List<Pair<Component, IDisplayInfo.TooltipPredicate>> getTooltips() {
        return this.tooltips;
    }

    public boolean hasClickAction() {
        return this.clickAction != null;
    }

    public boolean handleClick(CustomMachine machine, IMachineRecipe recipe, int button) {
        if (this.hasClickAction()) {
            this.clickAction.handleClick(machine, recipe, button);
            return true;
        } else {
            return false;
        }
    }

    public boolean shouldRender() {
        return this.renderer != DEFAULT_RENDERER || !this.tooltips.isEmpty() || this.hasClickAction();
    }

    public interface IDisplayInfoRenderer {

        void render(GuiGraphics var1, int var2);
    }

    private static record Item(ItemStack stack) implements RequirementDisplayInfo.IDisplayInfoRenderer {

        @Override
        public void render(GuiGraphics graphics, int size) {
            graphics.pose().scale((float) size / 16.0F, (float) size / 16.0F, 1.0F);
            graphics.renderItem(this.stack, 0, 0);
        }
    }

    private static record Sprite(ResourceLocation atlas, ResourceLocation sprite) implements RequirementDisplayInfo.IDisplayInfoRenderer {

        @Override
        public void render(GuiGraphics graphics, int size) {
            graphics.blit(0, 0, 0, size, size, (TextureAtlasSprite) Minecraft.getInstance().getTextureAtlas(this.atlas).apply(this.sprite));
        }
    }

    private static record Texture(ResourceLocation icon, int width, int height, int u, int v) implements RequirementDisplayInfo.IDisplayInfoRenderer {

        @Override
        public void render(GuiGraphics graphics, int size) {
            int textureWidth = TextureSizeHelper.getTextureWidth(this.icon);
            int textureHeight = TextureSizeHelper.getTextureHeight(this.icon);
            graphics.blit(this.icon, 0, 0, size, size, (float) this.u, (float) this.v, textureWidth, textureHeight, textureWidth, textureHeight);
        }
    }
}