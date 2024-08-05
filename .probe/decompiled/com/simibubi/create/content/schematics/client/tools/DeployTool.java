package com.simibubi.create.content.schematics.client.tools;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllKeys;
import com.simibubi.create.content.schematics.client.SchematicTransformation;
import com.simibubi.create.foundation.outliner.AABBOutline;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class DeployTool extends PlacementToolBase {

    @Override
    public void init() {
        super.init();
        this.selectionRange = -1;
    }

    @Override
    public void updateSelection() {
        if (this.schematicHandler.isActive() && this.selectionRange == -1) {
            this.selectionRange = (int) (this.schematicHandler.getBounds().getCenter().length() / 2.0);
            this.selectionRange = Mth.clamp(this.selectionRange, 1, 100);
        }
        this.selectIgnoreBlocks = AllKeys.ACTIVATE_TOOL.isPressed();
        super.updateSelection();
    }

    @Override
    public void renderTool(PoseStack ms, SuperRenderTypeBuffer buffer, Vec3 camera) {
        super.renderTool(ms, buffer, camera);
        if (this.selectedPos != null) {
            ms.pushPose();
            float pt = AnimationTickHolder.getPartialTicks();
            double x = Mth.lerp((double) pt, this.lastChasingSelectedPos.x, this.chasingSelectedPos.x);
            double y = Mth.lerp((double) pt, this.lastChasingSelectedPos.y, this.chasingSelectedPos.y);
            double z = Mth.lerp((double) pt, this.lastChasingSelectedPos.z, this.chasingSelectedPos.z);
            SchematicTransformation transformation = this.schematicHandler.getTransformation();
            AABB bounds = this.schematicHandler.getBounds();
            Vec3 center = bounds.getCenter();
            Vec3 rotationOffset = transformation.getRotationOffset(true);
            int centerX = (int) center.x;
            int centerZ = (int) center.z;
            double xOrigin = bounds.getXsize() / 2.0;
            double zOrigin = bounds.getZsize() / 2.0;
            Vec3 origin = new Vec3(xOrigin, 0.0, zOrigin);
            ms.translate(x - (double) centerX - camera.x, y - camera.y, z - (double) centerZ - camera.z);
            ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) TransformStack.cast(ms).translate(origin)).translate(rotationOffset)).rotateY((double) transformation.getCurrentRotation())).translateBack(rotationOffset)).translateBack(origin);
            AABBOutline outline = this.schematicHandler.getOutline();
            outline.render(ms, buffer, Vec3.ZERO, pt);
            outline.getParams().clearTextures();
            ms.popPose();
        }
    }

    @Override
    public boolean handleMouseWheel(double delta) {
        if (!this.selectIgnoreBlocks) {
            return super.handleMouseWheel(delta);
        } else {
            this.selectionRange = (int) ((double) this.selectionRange + delta);
            this.selectionRange = Mth.clamp(this.selectionRange, 1, 100);
            return true;
        }
    }

    @Override
    public boolean handleRightClick() {
        if (this.selectedPos == null) {
            return super.handleRightClick();
        } else {
            Vec3 center = this.schematicHandler.getBounds().getCenter();
            BlockPos target = this.selectedPos.offset(-((int) center.x), 0, -((int) center.z));
            ItemStack item = this.schematicHandler.getActiveSchematicItem();
            if (item != null) {
                item.getTag().putBoolean("Deployed", true);
                item.getTag().put("Anchor", NbtUtils.writeBlockPos(target));
                this.schematicHandler.getTransformation().startAt(target);
            }
            this.schematicHandler.getTransformation().moveTo(target);
            this.schematicHandler.markDirty();
            this.schematicHandler.deploy();
            return true;
        }
    }
}