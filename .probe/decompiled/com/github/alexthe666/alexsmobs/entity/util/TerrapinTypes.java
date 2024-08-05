package com.github.alexthe666.alexsmobs.entity.util;

import java.util.Locale;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

public enum TerrapinTypes {

    GREEN(new ResourceLocation("alexsmobs:textures/entity/terrapin/terrapin_green.png"), 8.0F),
    BLACK(new ResourceLocation("alexsmobs:textures/entity/terrapin/terrapin_black.png"), 11.0F),
    BROWN(new ResourceLocation("alexsmobs:textures/entity/terrapin/terrapin_brown.png"), 10.0F),
    KOOPA(new ResourceLocation("alexsmobs:textures/entity/terrapin/terrapin_koopa.png"), 0.05F),
    PAINTED(new ResourceLocation("alexsmobs:textures/entity/terrapin/terrapin_painted.png"), 8.0F),
    RED_EARED(new ResourceLocation("alexsmobs:textures/entity/terrapin/terrapin_red_eared.png"), 13.0F),
    OVERLAY(new ResourceLocation("alexsmobs:textures/entity/terrapin/overlay/terrapin_with_overlays.png"), 9.0F);

    private final ResourceLocation texture;

    private final float weight;

    private static final int[] DEFAULT_COLORS = new int[] { 11225397, 12756521, 3552563, 15460833, 6333298, 12753778 };

    private TerrapinTypes(ResourceLocation texture, float weight) {
        this.texture = texture;
        this.weight = weight;
    }

    public static TerrapinTypes getRandomType(RandomSource random) {
        float totalWeight = 0.0F;
        for (TerrapinTypes type : values()) {
            totalWeight += type.weight;
        }
        int randomIndex = -1;
        double randomWeightSample = random.nextDouble() * (double) totalWeight;
        for (int i = 0; i < values().length; i++) {
            randomWeightSample -= (double) values()[i].weight;
            if (randomWeightSample <= 0.0) {
                randomIndex = i;
                break;
            }
        }
        return values()[randomIndex];
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }

    public static int generateRandomColor(RandomSource random) {
        return DEFAULT_COLORS[random.nextInt(DEFAULT_COLORS.length) % DEFAULT_COLORS.length];
    }

    public String getTranslationName() {
        return "entity.alexsmobs.terrapin.variant_" + this.name().toLowerCase(Locale.ROOT);
    }
}