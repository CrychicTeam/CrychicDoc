package net.minecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.Monster;

public abstract class AbstractZombieModel<T extends Monster> extends HumanoidModel<T> {

    protected AbstractZombieModel(ModelPart modelPart0) {
        super(modelPart0);
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        super.setupAnim(t0, float1, float2, float3, float4, float5);
        AnimationUtils.animateZombieArms(this.f_102812_, this.f_102811_, this.isAggressive(t0), this.f_102608_, float3);
    }

    public abstract boolean isAggressive(T var1);
}