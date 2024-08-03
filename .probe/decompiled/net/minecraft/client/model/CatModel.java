package net.minecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.Cat;

public class CatModel<T extends Cat> extends OcelotModel<T> {

    private float lieDownAmount;

    private float lieDownAmountTail;

    private float relaxStateOneAmount;

    public CatModel(ModelPart modelPart0) {
        super(modelPart0);
    }

    public void prepareMobModel(T t0, float float1, float float2, float float3) {
        this.lieDownAmount = t0.getLieDownAmount(float3);
        this.lieDownAmountTail = t0.getLieDownAmountTail(float3);
        this.relaxStateOneAmount = t0.getRelaxStateOneAmount(float3);
        if (this.lieDownAmount <= 0.0F) {
            this.f_103135_.xRot = 0.0F;
            this.f_103135_.zRot = 0.0F;
            this.f_170755_.xRot = 0.0F;
            this.f_170755_.zRot = 0.0F;
            this.f_170756_.xRot = 0.0F;
            this.f_170756_.zRot = 0.0F;
            this.f_170756_.x = -1.2F;
            this.f_170753_.xRot = 0.0F;
            this.f_170754_.xRot = 0.0F;
            this.f_170754_.zRot = 0.0F;
            this.f_170754_.x = -1.1F;
            this.f_170754_.y = 18.0F;
        }
        super.prepareMobModel(t0, float1, float2, float3);
        if (t0.m_21825_()) {
            this.f_103136_.xRot = (float) (Math.PI / 4);
            this.f_103136_.y += -4.0F;
            this.f_103136_.z += 5.0F;
            this.f_103135_.y += -3.3F;
            this.f_103135_.z++;
            this.f_103133_.y += 8.0F;
            this.f_103133_.z += -2.0F;
            this.f_103134_.y += 2.0F;
            this.f_103134_.z += -0.8F;
            this.f_103133_.xRot = 1.7278761F;
            this.f_103134_.xRot = 2.670354F;
            this.f_170755_.xRot = (float) (-Math.PI / 20);
            this.f_170755_.y = 16.1F;
            this.f_170755_.z = -7.0F;
            this.f_170756_.xRot = (float) (-Math.PI / 20);
            this.f_170756_.y = 16.1F;
            this.f_170756_.z = -7.0F;
            this.f_170753_.xRot = (float) (-Math.PI / 2);
            this.f_170753_.y = 21.0F;
            this.f_170753_.z = 1.0F;
            this.f_170754_.xRot = (float) (-Math.PI / 2);
            this.f_170754_.y = 21.0F;
            this.f_170754_.z = 1.0F;
            this.f_103137_ = 3;
        }
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        super.setupAnim(t0, float1, float2, float3, float4, float5);
        if (this.lieDownAmount > 0.0F) {
            this.f_103135_.zRot = ModelUtils.rotlerpRad(this.f_103135_.zRot, -1.2707963F, this.lieDownAmount);
            this.f_103135_.yRot = ModelUtils.rotlerpRad(this.f_103135_.yRot, 1.2707963F, this.lieDownAmount);
            this.f_170755_.xRot = -1.2707963F;
            this.f_170756_.xRot = -0.47079635F;
            this.f_170756_.zRot = -0.2F;
            this.f_170756_.x = -0.2F;
            this.f_170753_.xRot = -0.4F;
            this.f_170754_.xRot = 0.5F;
            this.f_170754_.zRot = -0.5F;
            this.f_170754_.x = -0.3F;
            this.f_170754_.y = 20.0F;
            this.f_103133_.xRot = ModelUtils.rotlerpRad(this.f_103133_.xRot, 0.8F, this.lieDownAmountTail);
            this.f_103134_.xRot = ModelUtils.rotlerpRad(this.f_103134_.xRot, -0.4F, this.lieDownAmountTail);
        }
        if (this.relaxStateOneAmount > 0.0F) {
            this.f_103135_.xRot = ModelUtils.rotlerpRad(this.f_103135_.xRot, -0.58177644F, this.relaxStateOneAmount);
        }
    }
}