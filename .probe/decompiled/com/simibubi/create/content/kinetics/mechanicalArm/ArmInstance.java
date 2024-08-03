package com.simibubi.create.content.kinetics.mechanicalArm;

import com.google.common.collect.Lists;
import com.jozufozu.flywheel.api.InstanceData;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class ArmInstance extends SingleRotatingInstance<ArmBlockEntity> implements DynamicInstance {

    final ModelData base;

    final ModelData lowerBody;

    final ModelData upperBody;

    ModelData claw;

    private final ArrayList<ModelData> clawGrips;

    private final ArrayList<ModelData> models;

    private final Boolean ceiling;

    private boolean firstRender = true;

    private float baseAngle = Float.NaN;

    private float lowerArmAngle = Float.NaN;

    private float upperArmAngle = Float.NaN;

    private float headAngle = Float.NaN;

    public ArmInstance(MaterialManager materialManager, ArmBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        Material<ModelData> mat = this.getTransformMaterial();
        this.base = (ModelData) mat.getModel(AllPartialModels.ARM_BASE, this.blockState).createInstance();
        this.lowerBody = (ModelData) mat.getModel(AllPartialModels.ARM_LOWER_BODY, this.blockState).createInstance();
        this.upperBody = (ModelData) mat.getModel(AllPartialModels.ARM_UPPER_BODY, this.blockState).createInstance();
        this.claw = (ModelData) mat.getModel(blockEntity.goggles ? AllPartialModels.ARM_CLAW_BASE_GOGGLES : AllPartialModels.ARM_CLAW_BASE, this.blockState).createInstance();
        ModelData clawGrip1 = (ModelData) mat.getModel(AllPartialModels.ARM_CLAW_GRIP_UPPER, this.blockState).createInstance();
        ModelData clawGrip2 = (ModelData) mat.getModel(AllPartialModels.ARM_CLAW_GRIP_LOWER, this.blockState).createInstance();
        this.clawGrips = Lists.newArrayList(new ModelData[] { clawGrip1, clawGrip2 });
        this.models = Lists.newArrayList(new ModelData[] { this.base, this.lowerBody, this.upperBody, this.claw, clawGrip1, clawGrip2 });
        this.ceiling = (Boolean) this.blockState.m_61143_(ArmBlock.CEILING);
        this.animateArm(false);
    }

    public void beginFrame() {
        if (((ArmBlockEntity) this.blockEntity).phase == ArmBlockEntity.Phase.DANCING && ((ArmBlockEntity) this.blockEntity).getSpeed() != 0.0F) {
            this.animateArm(true);
            this.firstRender = true;
        } else {
            float pt = AnimationTickHolder.getPartialTicks();
            float baseAngleNow = ((ArmBlockEntity) this.blockEntity).baseAngle.getValue(pt);
            float lowerArmAngleNow = ((ArmBlockEntity) this.blockEntity).lowerArmAngle.getValue(pt);
            float upperArmAngleNow = ((ArmBlockEntity) this.blockEntity).upperArmAngle.getValue(pt);
            float headAngleNow = ((ArmBlockEntity) this.blockEntity).headAngle.getValue(pt);
            boolean settled = Mth.equal(this.baseAngle, baseAngleNow) && Mth.equal(this.lowerArmAngle, lowerArmAngleNow) && Mth.equal(this.upperArmAngle, upperArmAngleNow) && Mth.equal(this.headAngle, headAngleNow);
            this.baseAngle = baseAngleNow;
            this.lowerArmAngle = lowerArmAngleNow;
            this.upperArmAngle = upperArmAngleNow;
            this.headAngle = headAngleNow;
            if (!settled || this.firstRender) {
                this.animateArm(false);
            }
            if (this.firstRender) {
                this.firstRender = false;
            }
        }
    }

    private void animateArm(boolean rave) {
        float baseAngle;
        float lowerArmAngle;
        float upperArmAngle;
        float headAngle;
        int color;
        if (rave) {
            float renderTick = AnimationTickHolder.getRenderTime(((ArmBlockEntity) this.blockEntity).m_58904_()) + (float) (((ArmBlockEntity) this.blockEntity).hashCode() % 64);
            baseAngle = renderTick * 10.0F % 360.0F;
            lowerArmAngle = Mth.lerp((Mth.sin(renderTick / 4.0F) + 1.0F) / 2.0F, -45.0F, 15.0F);
            upperArmAngle = Mth.lerp((Mth.sin(renderTick / 8.0F) + 1.0F) / 4.0F, -45.0F, 95.0F);
            headAngle = -lowerArmAngle;
            color = Color.rainbowColor(AnimationTickHolder.getTicks() * 100).getRGB();
        } else {
            baseAngle = this.baseAngle;
            lowerArmAngle = this.lowerArmAngle - 135.0F;
            upperArmAngle = this.upperArmAngle - 90.0F;
            headAngle = this.headAngle;
            color = 16777215;
        }
        PoseStack msLocal = new PoseStack();
        TransformStack msr = TransformStack.cast(msLocal);
        msr.translate(this.getInstancePosition());
        msr.centre();
        if (this.ceiling) {
            msr.rotateX(180.0);
        }
        ArmRenderer.transformBase(msr, baseAngle);
        this.base.setTransform(msLocal);
        ArmRenderer.transformLowerArm(msr, lowerArmAngle);
        this.lowerBody.setTransform(msLocal).setColor(color);
        ArmRenderer.transformUpperArm(msr, upperArmAngle);
        this.upperBody.setTransform(msLocal).setColor(color);
        ArmRenderer.transformHead(msr, headAngle);
        if (this.ceiling && ((ArmBlockEntity) this.blockEntity).goggles) {
            msr.rotateZ(180.0);
        }
        this.claw.setTransform(msLocal);
        if (this.ceiling && ((ArmBlockEntity) this.blockEntity).goggles) {
            msr.rotateZ(180.0);
        }
        ItemStack item = ((ArmBlockEntity) this.blockEntity).heldItem;
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        boolean hasItem = !item.isEmpty();
        boolean isBlockItem = hasItem && item.getItem() instanceof BlockItem && itemRenderer.getModel(item, Minecraft.getInstance().level, null, 0).isGui3d();
        for (int index : Iterate.zeroAndOne) {
            msLocal.pushPose();
            int flip = index * 2 - 1;
            ArmRenderer.transformClawHalf(msr, hasItem, isBlockItem, flip);
            ((ModelData) this.clawGrips.get(index)).setTransform(msLocal);
            msLocal.popPose();
        }
    }

    @Override
    public void update() {
        super.update();
        this.models.remove(this.claw);
        this.claw.delete();
        this.claw = (ModelData) this.getTransformMaterial().getModel(((ArmBlockEntity) this.blockEntity).goggles ? AllPartialModels.ARM_CLAW_BASE_GOGGLES : AllPartialModels.ARM_CLAW_BASE, this.blockState).createInstance();
        this.models.add(this.claw);
        this.updateLight();
        this.animateArm(false);
    }

    @Override
    public void updateLight() {
        super.updateLight();
        this.relight(this.pos, this.models.stream());
    }

    @Override
    protected Instancer<RotatingData> getModel() {
        return this.getRotatingMaterial().getModel(AllPartialModels.ARM_COG, ((ArmBlockEntity) this.blockEntity).m_58900_());
    }

    @Override
    public void remove() {
        super.remove();
        this.models.forEach(InstanceData::delete);
    }
}