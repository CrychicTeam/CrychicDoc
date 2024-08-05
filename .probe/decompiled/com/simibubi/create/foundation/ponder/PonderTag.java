package com.simibubi.create.foundation.ponder;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PonderTag implements ScreenElement {

    public static final PonderTag HIGHLIGHT_ALL = new PonderTag(Create.asResource("_all"));

    private final ResourceLocation id;

    private ResourceLocation icon;

    private ItemStack itemIcon = ItemStack.EMPTY;

    private ItemStack mainItem = ItemStack.EMPTY;

    public PonderTag(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public ItemStack getMainItem() {
        return this.mainItem;
    }

    public String getTitle() {
        return PonderLocalization.getTag(this.id);
    }

    public String getDescription() {
        return PonderLocalization.getTagDescription(this.id);
    }

    public PonderTag defaultLang(String title, String description) {
        PonderLocalization.registerTag(this.id, title, description);
        return this;
    }

    public PonderTag addToIndex() {
        PonderRegistry.TAGS.listTag(this);
        return this;
    }

    public PonderTag icon(ResourceLocation location) {
        this.icon = new ResourceLocation(location.getNamespace(), "textures/ponder/tag/" + location.getPath() + ".png");
        return this;
    }

    public PonderTag icon(String location) {
        this.icon = new ResourceLocation(this.id.getNamespace(), "textures/ponder/tag/" + location + ".png");
        return this;
    }

    public PonderTag idAsIcon() {
        return this.icon(this.id);
    }

    public PonderTag item(ItemLike item, boolean useAsIcon, boolean useAsMainItem) {
        if (useAsIcon) {
            this.itemIcon = new ItemStack(item);
        }
        if (useAsMainItem) {
            this.mainItem = new ItemStack(item);
        }
        return this;
    }

    public PonderTag item(ItemLike item) {
        return this.item(item, true, false);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(GuiGraphics graphics, int x, int y) {
        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate((float) x, (float) y, 0.0F);
        if (this.icon != null) {
            ms.scale(0.25F, 0.25F, 1.0F);
            graphics.blit(this.icon, 0, 0, 0, 0.0F, 0.0F, 64, 64, 64, 64);
        } else if (!this.itemIcon.isEmpty()) {
            ms.translate(-2.0F, -2.0F, 0.0F);
            ms.scale(1.25F, 1.25F, 1.25F);
            GuiGameElement.of(this.itemIcon).render(graphics);
        }
        ms.popPose();
    }
}