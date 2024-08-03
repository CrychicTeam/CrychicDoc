package com.github.alexmodguy.alexscaves.client.render.entity.layer;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

@OnlyIn(Dist.CLIENT)
public class ClientLayerRegistry {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        List<EntityType<? extends LivingEntity>> entityTypes = ImmutableList.copyOf((Collection) ForgeRegistries.ENTITY_TYPES.getValues().stream().filter(DefaultAttributes::m_22301_).map(entityType -> entityType).collect(Collectors.toList()));
        entityTypes.forEach(entityType -> addLayerIfApplicable(entityType, event));
        for (String skinType : event.getSkins()) {
            event.getSkin(skinType).addLayer(new ACPotionEffectLayer(event.getSkin(skinType)));
        }
    }

    private static void addLayerIfApplicable(EntityType<? extends LivingEntity> entityType, EntityRenderersEvent.AddLayers event) {
        LivingEntityRenderer renderer = null;
        if (entityType != EntityType.ENDER_DRAGON) {
            try {
                renderer = event.getRenderer(entityType);
            } catch (Exception var4) {
                AlexsCaves.LOGGER.warn("Could not apply radiation glow layer to " + ForgeRegistries.ENTITY_TYPES.getKey(entityType) + ", has custom renderer that is not LivingEntityRenderer.");
            }
            if (renderer != null) {
                renderer.addLayer(new ACPotionEffectLayer(renderer));
            }
        }
    }
}