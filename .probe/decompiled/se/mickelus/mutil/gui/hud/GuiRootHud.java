package se.mickelus.mutil.gui.hud;

import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;

public class GuiRootHud extends GuiElement {

    public GuiRootHud() {
        super(0, 0, 0, 0);
    }

    public void draw(GuiGraphics graphics, Vec3 proj, BlockHitResult rayTrace, VoxelShape shape) {
        BlockPos blockPos = rayTrace.getBlockPos();
        Vec3 hitVec = rayTrace.m_82450_();
        this.draw(graphics, (double) blockPos.m_123341_() - proj.x, (double) blockPos.m_123342_() - proj.y, (double) blockPos.m_123343_() - proj.z, hitVec.x - (double) blockPos.m_123341_(), hitVec.y - (double) blockPos.m_123342_(), hitVec.z - (double) blockPos.m_123343_(), rayTrace.getDirection(), shape.bounds());
    }

    public void draw(GuiGraphics graphics, double x, double y, double z, double hitX, double hitY, double hitZ, Direction facing, AABB boundingBox) {
        this.activeAnimations.removeIf(keyframeAnimation -> !keyframeAnimation.isActive());
        this.activeAnimations.forEach(KeyframeAnimation::preDraw);
        graphics.pose().pushPose();
        graphics.pose().translate(x, y, z);
        int mouseX = 0;
        int mouseY = 0;
        float size = 64.0F;
        Vec3 magicOffset = Vec3.atLowerCornerOf(facing.getNormal()).scale(0.002F);
        graphics.pose().translate(magicOffset.x(), magicOffset.y(), magicOffset.z());
        switch(facing) {
            case NORTH:
                mouseX = (int) ((boundingBox.maxX - hitX) * (double) size);
                mouseY = (int) ((boundingBox.maxY - hitY) * (double) size);
                this.width = (int) ((boundingBox.maxX - boundingBox.minX) * (double) size);
                this.height = (int) ((boundingBox.maxY - boundingBox.minY) * (double) size);
                graphics.pose().translate(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
                graphics.pose().mulPose(Axis.YP.rotationDegrees(180.0F));
                break;
            case SOUTH:
                mouseX = (int) ((hitX - boundingBox.minX) * (double) size);
                mouseY = (int) ((boundingBox.maxY - hitY) * (double) size);
                this.width = (int) ((boundingBox.maxX - boundingBox.minX) * (double) size);
                this.height = (int) ((boundingBox.maxY - boundingBox.minY) * (double) size);
                graphics.pose().translate(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
                break;
            case EAST:
                mouseX = (int) ((boundingBox.maxZ - hitZ) * (double) size);
                mouseY = (int) ((boundingBox.maxY - hitY) * (double) size);
                this.width = (int) ((boundingBox.maxZ - boundingBox.minZ) * (double) size);
                this.height = (int) ((boundingBox.maxY - boundingBox.minY) * (double) size);
                graphics.pose().translate(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
                graphics.pose().mulPose(Axis.YP.rotationDegrees(90.0F));
                break;
            case WEST:
                mouseX = (int) ((hitZ - boundingBox.minZ) * (double) size);
                mouseY = (int) ((boundingBox.maxY - hitY) * (double) size);
                this.width = (int) ((boundingBox.maxZ - boundingBox.minZ) * (double) size);
                this.height = (int) ((boundingBox.maxY - boundingBox.minY) * (double) size);
                graphics.pose().translate(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
                graphics.pose().mulPose(Axis.YP.rotationDegrees(-90.0F));
                break;
            case UP:
                mouseX = (int) ((boundingBox.maxX - hitX) * (double) size);
                mouseY = (int) ((boundingBox.maxZ - hitZ) * (double) size);
                this.width = (int) ((boundingBox.maxX - boundingBox.minX) * (double) size);
                this.height = (int) ((boundingBox.maxZ - boundingBox.minZ) * (double) size);
                graphics.pose().translate(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
                graphics.pose().mulPose(Axis.XP.rotationDegrees(90.0F));
                graphics.pose().scale(-1.0F, 1.0F, 1.0F);
                break;
            case DOWN:
                mouseX = (int) ((hitX - boundingBox.minX) * (double) size);
                mouseY = (int) ((boundingBox.maxZ - hitZ) * (double) size);
                this.width = (int) ((boundingBox.maxX - boundingBox.minX) * (double) size);
                this.height = (int) ((boundingBox.maxZ - boundingBox.minZ) * (double) size);
                graphics.pose().translate(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
                graphics.pose().mulPose(Axis.XP.rotationDegrees(90.0F));
        }
        graphics.pose().scale(1.0F / size, -1.0F / size, 1.0F / size);
        graphics.pose().translate(0.0, 0.0, 0.02);
        this.updateFocusState(0, 0, mouseX, mouseY);
        this.drawChildren(graphics, 0, 0, this.width, this.height, mouseX, mouseY, 1.0F);
        graphics.pose().popPose();
    }
}