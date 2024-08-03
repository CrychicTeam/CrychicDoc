package dev.xkmc.l2archery.content.entity;

import dev.xkmc.l2archery.content.item.GenericArrowItem;
import dev.xkmc.l2archery.init.registrate.ArcheryItems;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class GenericArrowRenderer extends ArrowRenderer<GenericArrowEntity> {

    public GenericArrowRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    public ResourceLocation getTextureLocation(GenericArrowEntity entity) {
        GenericArrowItem arrow;
        if (entity.data.arrow().item() instanceof GenericArrowItem gen) {
            arrow = gen;
        } else {
            arrow = (GenericArrowItem) ArcheryItems.STARTER_ARROW.get();
        }
        ResourceLocation rl = ForgeRegistries.ITEMS.getKey(arrow);
        return new ResourceLocation(rl.getNamespace(), "textures/entity/arrow/arrow.png");
    }
}