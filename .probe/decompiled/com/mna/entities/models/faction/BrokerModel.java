package com.mna.entities.models.faction;

import com.mna.api.tools.RLoc;
import com.mna.entities.faction.Broker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class BrokerModel extends GeoModel<Broker> {

    private static final ResourceLocation modelFile = RLoc.create("geo/the_broker.geo.json");

    private static final ResourceLocation animFile = RLoc.create("animations/the_broker.anim.json");

    private static final ResourceLocation texFile = RLoc.create("textures/entity/the_broker.png");

    public ResourceLocation getAnimationResource(Broker arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(Broker arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(Broker arg0) {
        return texFile;
    }

    public void setCustomAnimations(Broker animatable, long instanceId, AnimationState<Broker> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        float f = Mth.rotLerp(animationState.getPartialTick(), animatable.f_20884_, animatable.f_20883_);
        float f1 = Mth.rotLerp(animationState.getPartialTick(), animatable.f_20886_, animatable.f_20885_);
        float f2 = f1 - f;
        float f6 = Mth.lerp(animationState.getPartialTick(), animatable.f_19860_, animatable.m_146909_());
        head.setRotY((float) ((double) (360.0F - f2) * Math.PI / 180.0));
        head.setRotX((float) ((double) f6 * Math.PI / 180.0));
    }
}