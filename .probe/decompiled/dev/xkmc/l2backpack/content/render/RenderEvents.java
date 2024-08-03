package dev.xkmc.l2backpack.content.render;

import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class RenderEvents {

    private static final String REG_NAME = "backpack";

    public static final ModelLayerLocation BACKPACK_LAYER = new ModelLayerLocation(new ResourceLocation("l2backpack", "backpack"), "main");

    public static void registerBackpackLayer() {
        EntityRenderDispatcher renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
        Map<String, EntityRenderer<? extends Player>> skinMap = renderManager.getSkinMap();
        for (EntityRenderer<? extends Player> renderer : skinMap.values()) {
            if (renderer instanceof LivingEntityRenderer ler) {
                addLayer(renderManager, ler);
            }
        }
        renderManager.renderers.forEach((e, r) -> {
            if (r instanceof LivingEntityRenderer lerx && lerx.getModel() instanceof HumanoidModel) {
                addLayer(renderManager, lerx);
            }
        });
    }

    private static <T extends LivingEntity, M extends HumanoidModel<T>> void addLayer(EntityRenderDispatcher manager, LivingEntityRenderer<T, M> ler) {
        Minecraft mc = Minecraft.getInstance();
        ler.addLayer(new BackpackLayerRenderer<>(ler, mc.getEntityModels()));
        ler.addLayer(new ItemOnBackLayerRenderer<>(ler, mc.getEntityModels(), manager.getItemInHandRenderer()));
    }
}