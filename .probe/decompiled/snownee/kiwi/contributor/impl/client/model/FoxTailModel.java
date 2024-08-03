package snownee.kiwi.contributor.impl.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FoxTailModel<T extends LivingEntity> extends AgeableListModel<T> {

    private PlayerModel<AbstractClientPlayer> playerModel;

    private ModelPart tail;

    private ModelPart ear1;

    private ModelPart ear2;

    public FoxTailModel(PlayerModel<AbstractClientPlayer> playerModel, LayerDefinition definition) {
        this.playerModel = playerModel;
        ModelPart root = definition.bakeRoot();
        this.ear1 = root.getChild("right_ear");
        this.ear2 = root.getChild("left_ear");
        this.tail = root.getChild("tail");
    }

    public static LayerDefinition create() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition root = meshdefinition.getRoot();
        root.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(8, 1).addBox(-4.0F, -10.0F, -4.0F, 2.0F, 2.0F, 1.0F), PartPose.ZERO);
        root.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(15, 1).addBox(2.0F, -10.0F, -4.0F, 2.0F, 2.0F, 1.0F), PartPose.ZERO);
        root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(30, 0).addBox(0.0F, 0.0F, 0.0F, 4.0F, 9.0F, 5.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 48, 32);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.ear1, this.ear2);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.tail);
    }

    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.ear1.copyFrom(this.playerModel.f_102808_);
        this.ear2.copyFrom(this.playerModel.f_102808_);
        if (ageInTicks % 60.0F < 2.0F) {
            this.ear1.yRot += 0.05F;
            this.ear2.yRot -= 0.05F;
        }
        float delta = Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        if (entityIn.m_6047_()) {
            this.tail.setPos(-2.0F, 14.0F, 5.5F);
            this.tail.xRot = 1.25F + delta;
        } else {
            this.tail.setPos(-2.0F, 10.0F, 0.5F);
            this.tail.xRot = 0.85F + delta;
        }
    }
}