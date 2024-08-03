package com.mna.entities.models.constructs;

import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.entities.constructs.animated.Construct;
import java.util.HashMap;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ConstructModel extends GeoModel<Construct> {

    private static final ResourceLocation modelFile = new ResourceLocation("mna", "geo/construct.geo.json");

    private static final ResourceLocation animFile = new ResourceLocation("mna", "animations/construct.animation.json");

    private ResourceLocation curLayerTexture;

    private HashMap<ConstructSlot, Integer> mutices = new HashMap();

    private UUID owner;

    public ResourceLocation getAnimationResource(Construct arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(Construct arg0) {
        return modelFile;
    }

    public void setActiveMaterial(ConstructMaterial material) {
        this.curLayerTexture = material.getTexture();
    }

    public ResourceLocation getTextureResource(Construct arg0) {
        return this.curLayerTexture != null ? this.curLayerTexture : ConstructMaterial.WOOD.getTexture();
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    @Nullable
    public UUID getOwner() {
        return this.owner;
    }

    public int getMutex(ConstructSlot slot) {
        return (Integer) this.mutices.getOrDefault(slot, 0);
    }

    public void setVisibleParts(UUID owner, int headMutex, int torsoMutex, int legsMutex, int leftArmMutex, int rightArmMutex) {
        this.setOwner(owner);
        this.setMutexVisibility(ConstructSlot.HEAD, headMutex);
        this.setMutexVisibility(ConstructSlot.TORSO, torsoMutex);
        this.setMutexVisibility(ConstructSlot.LEGS, legsMutex);
        this.setMutexVisibility(ConstructSlot.LEFT_ARM, leftArmMutex);
        this.setMutexVisibility(ConstructSlot.RIGHT_ARM, rightArmMutex);
    }

    public void resetMutexVisibility() {
        this.setVisibleParts(null, 0, 0, 0, 0, 0);
    }

    public void setMutexVisibility(ConstructSlot slot, int mutex) {
        this.mutices.put(slot, mutex);
    }
}