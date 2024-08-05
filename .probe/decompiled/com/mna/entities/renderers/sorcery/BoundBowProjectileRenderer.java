package com.mna.entities.renderers.sorcery;

import com.mna.api.affinity.Affinity;
import com.mna.entities.sorcery.targeting.BoundBowProjectile;
import com.mna.tools.render.WorldRenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import java.util.HashMap;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BoundBowProjectileRenderer extends EntityRenderer<BoundBowProjectile> {

    private static final HashMap<Affinity, Pair<int[], int[]>> affinityColors = new HashMap();

    private static final Pair<int[], int[]> _default = new Pair(new int[] { 255, 0, 255 }, new int[] { 138, 0, 255 });

    public BoundBowProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(BoundBowProjectile entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        Pair<int[], int[]> colors = (Pair<int[], int[]>) affinityColors.getOrDefault(entityIn.getAffinity(), _default);
        WorldRenderUtils.renderRadiantWithDirection(entityIn, matrixStackIn, bufferIn, (int[]) colors.getFirst(), (int[]) colors.getSecond(), 255, 1.0F);
    }

    public ResourceLocation getTextureLocation(BoundBowProjectile entity) {
        return null;
    }

    static {
        affinityColors.put(Affinity.ARCANE, new Pair(new int[] { 255, 0, 255 }, new int[] { 138, 0, 255 }));
        affinityColors.put(Affinity.WATER, new Pair(new int[] { 0, 255, 255 }, new int[] { 78, 78, 153 }));
        affinityColors.put(Affinity.ICE, new Pair(new int[] { 0, 255, 255 }, new int[] { 30, 150, 150 }));
        affinityColors.put(Affinity.FIRE, new Pair(new int[] { 255, 255, 0 }, new int[] { 255, 120, 0 }));
        affinityColors.put(Affinity.HELLFIRE, new Pair(new int[] { 76, 138, 76 }, new int[] { 26, 54, 26 }));
        affinityColors.put(Affinity.WIND, new Pair(new int[] { 200, 204, 204 }, new int[] { 96, 97, 97 }));
        affinityColors.put(Affinity.EARTH, new Pair(new int[] { 171, 135, 99 }, new int[] { 94, 63, 32 }));
        affinityColors.put(Affinity.ENDER, new Pair(new int[] { 67, 16, 115 }, new int[] { 32, 17, 46 }));
    }
}