package net.minecraft.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Shulker;

public class ShulkerModel<T extends Shulker> extends ListModel<T> {

    private static final String LID = "lid";

    private static final String BASE = "base";

    private final ModelPart base;

    private final ModelPart lid;

    private final ModelPart head;

    public ShulkerModel(ModelPart modelPart0) {
        super(RenderType::m_110464_);
        this.lid = modelPart0.getChild("lid");
        this.base = modelPart0.getChild("base");
        this.head = modelPart0.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 12.0F, 16.0F), PartPose.offset(0.0F, 24.0F, 0.0F));
        $$1.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 28).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 8.0F, 16.0F), PartPose.offset(0.0F, 24.0F, 0.0F));
        $$1.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 52).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 6.0F, 6.0F), PartPose.offset(0.0F, 12.0F, 0.0F));
        return LayerDefinition.create($$0, 64, 64);
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        float $$6 = float3 - (float) t0.f_19797_;
        float $$7 = (0.5F + t0.getClientPeekAmount($$6)) * (float) Math.PI;
        float $$8 = -1.0F + Mth.sin($$7);
        float $$9 = 0.0F;
        if ($$7 > (float) Math.PI) {
            $$9 = Mth.sin(float3 * 0.1F) * 0.7F;
        }
        this.lid.setPos(0.0F, 16.0F + Mth.sin($$7) * 8.0F + $$9, 0.0F);
        if (t0.getClientPeekAmount($$6) > 0.3F) {
            this.lid.yRot = $$8 * $$8 * $$8 * $$8 * (float) Math.PI * 0.125F;
        } else {
            this.lid.yRot = 0.0F;
        }
        this.head.xRot = float5 * (float) (Math.PI / 180.0);
        this.head.yRot = (t0.f_20885_ - 180.0F - t0.f_20883_) * (float) (Math.PI / 180.0);
    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(this.base, this.lid);
    }

    public ModelPart getLid() {
        return this.lid;
    }

    public ModelPart getHead() {
        return this.head;
    }
}