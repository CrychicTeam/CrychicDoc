package net.minecraft.client.model;

import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public abstract class EntityModel<T extends Entity> extends Model {

    public float attackTime;

    public boolean riding;

    public boolean young = true;

    protected EntityModel() {
        this(RenderType::m_110458_);
    }

    protected EntityModel(Function<ResourceLocation, RenderType> functionResourceLocationRenderType0) {
        super(functionResourceLocationRenderType0);
    }

    public abstract void setupAnim(T var1, float var2, float var3, float var4, float var5, float var6);

    public void prepareMobModel(T t0, float float1, float float2, float float3) {
    }

    public void copyPropertiesTo(EntityModel<T> entityModelT0) {
        entityModelT0.attackTime = this.attackTime;
        entityModelT0.riding = this.riding;
        entityModelT0.young = this.young;
    }
}