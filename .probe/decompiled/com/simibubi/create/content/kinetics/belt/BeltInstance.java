package com.simibubi.create.content.kinetics.belt;

import com.jozufozu.flywheel.api.InstanceData;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.FlatLit;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.BeltData;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.ArrayList;
import java.util.function.Supplier;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.LightLayer;
import org.joml.Quaternionf;

public class BeltInstance extends KineticBlockEntityInstance<BeltBlockEntity> {

    boolean upward;

    boolean diagonal;

    boolean sideways;

    boolean vertical;

    boolean alongX;

    boolean alongZ;

    BeltSlope beltSlope;

    Direction facing;

    protected ArrayList<BeltData> keys;

    protected RotatingData pulleyKey;

    public BeltInstance(MaterialManager materialManager, BeltBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        if (AllBlocks.BELT.has(this.blockState)) {
            this.keys = new ArrayList(2);
            this.beltSlope = (BeltSlope) this.blockState.m_61143_(BeltBlock.SLOPE);
            this.facing = (Direction) this.blockState.m_61143_(BeltBlock.HORIZONTAL_FACING);
            this.upward = this.beltSlope == BeltSlope.UPWARD;
            this.diagonal = this.beltSlope.isDiagonal();
            this.sideways = this.beltSlope == BeltSlope.SIDEWAYS;
            this.vertical = this.beltSlope == BeltSlope.VERTICAL;
            this.alongX = this.facing.getAxis() == Direction.Axis.X;
            this.alongZ = this.facing.getAxis() == Direction.Axis.Z;
            BeltPart part = (BeltPart) this.blockState.m_61143_(BeltBlock.PART);
            boolean start = part == BeltPart.START;
            boolean end = part == BeltPart.END;
            DyeColor color = (DyeColor) blockEntity.color.orElse(null);
            for (boolean bottom : Iterate.trueAndFalse) {
                PartialModel beltPartial = BeltRenderer.getBeltPartial(this.diagonal, start, end, bottom);
                SpriteShiftEntry spriteShift = BeltRenderer.getSpriteShiftEntry(color, this.diagonal, bottom);
                Instancer<BeltData> beltModel = materialManager.defaultSolid().material(AllMaterialSpecs.BELTS).getModel(beltPartial, this.blockState);
                this.keys.add(this.setup((BeltData) beltModel.createInstance(), bottom, spriteShift));
                if (this.diagonal) {
                    break;
                }
            }
            if (blockEntity.hasPulley()) {
                Instancer<RotatingData> pulleyModel = this.getPulleyModel();
                this.pulleyKey = this.setup((RotatingData) pulleyModel.createInstance());
            }
        }
    }

    public void update() {
        DyeColor color = (DyeColor) ((BeltBlockEntity) this.blockEntity).color.orElse(null);
        boolean bottom = true;
        for (BeltData key : this.keys) {
            SpriteShiftEntry spriteShiftEntry = BeltRenderer.getSpriteShiftEntry(color, this.diagonal, bottom);
            key.setScrollTexture(spriteShiftEntry).setColor((KineticBlockEntity) this.blockEntity).setRotationalSpeed(this.getScrollSpeed());
            bottom = false;
        }
        if (this.pulleyKey != null) {
            this.updateRotation(this.pulleyKey);
        }
    }

    public void updateLight() {
        this.relight(this.pos, this.keys.stream());
        if (this.pulleyKey != null) {
            this.relight(this.pos, new FlatLit[] { this.pulleyKey });
        }
    }

    public void remove() {
        this.keys.forEach(InstanceData::delete);
        this.keys.clear();
        if (this.pulleyKey != null) {
            this.pulleyKey.delete();
        }
        this.pulleyKey = null;
    }

    private float getScrollSpeed() {
        float speed = ((BeltBlockEntity) this.blockEntity).getSpeed();
        if (this.facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE ^ this.upward ^ (this.alongX && !this.diagonal || this.alongZ && this.diagonal)) {
            speed = -speed;
        }
        if (this.sideways && (this.facing == Direction.SOUTH || this.facing == Direction.WEST) || this.vertical && this.facing == Direction.EAST) {
            speed = -speed;
        }
        return speed;
    }

    private Instancer<RotatingData> getPulleyModel() {
        Direction dir = this.getOrientation();
        Direction.Axis axis = dir.getAxis();
        Supplier<PoseStack> ms = () -> {
            PoseStack modelTransform = new PoseStack();
            TransformStack msr = TransformStack.cast(modelTransform);
            msr.centre();
            if (axis == Direction.Axis.X) {
                msr.rotateY(90.0);
            }
            if (axis == Direction.Axis.Y) {
                msr.rotateX(90.0);
            }
            msr.rotateX(90.0);
            msr.unCentre();
            return modelTransform;
        };
        return this.getRotatingMaterial().getModel(AllPartialModels.BELT_PULLEY, this.blockState, dir, ms);
    }

    private Direction getOrientation() {
        Direction dir = ((Direction) this.blockState.m_61143_(BeltBlock.HORIZONTAL_FACING)).getClockWise();
        if (this.beltSlope == BeltSlope.SIDEWAYS) {
            dir = Direction.UP;
        }
        return dir;
    }

    private BeltData setup(BeltData key, boolean bottom, SpriteShiftEntry spriteShift) {
        boolean downward = this.beltSlope == BeltSlope.DOWNWARD;
        float rotX = (float) ((!this.diagonal && this.beltSlope != BeltSlope.HORIZONTAL ? 90 : 0) + (downward ? 180 : 0) + (this.sideways ? 90 : 0) + (this.vertical && this.alongZ ? 180 : 0));
        float rotY = this.facing.toYRot() + (float) (this.diagonal ^ this.alongX && !downward ? 180 : 0) + (float) (this.sideways && this.alongZ ? 180 : 0) + (float) (this.vertical && this.alongX ? 90 : 0);
        float rotZ = (float) ((this.sideways ? 90 : 0) + (this.vertical && this.alongX ? 90 : 0));
        Quaternionf q = new Quaternionf().rotationXYZ(rotX * (float) (Math.PI / 180.0), rotY * (float) (Math.PI / 180.0), rotZ * (float) (Math.PI / 180.0));
        key.setScrollTexture(spriteShift).setScrollMult(this.diagonal ? 0.375F : 0.5F).setRotation(q).setRotationalSpeed(this.getScrollSpeed()).setRotationOffset(bottom ? 0.5F : 0.0F).setColor((KineticBlockEntity) this.blockEntity).setPosition(this.getInstancePosition()).setBlockLight(this.world.m_45517_(LightLayer.BLOCK, this.pos)).setSkyLight(this.world.m_45517_(LightLayer.SKY, this.pos));
        return key;
    }
}