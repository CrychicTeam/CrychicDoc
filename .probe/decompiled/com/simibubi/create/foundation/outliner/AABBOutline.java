package com.simibubi.create.foundation.outliner;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllSpecialTextures;
import com.simibubi.create.foundation.render.RenderTypes;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import java.util.Optional;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class AABBOutline extends Outline {

    protected AABB bb;

    protected final Vector3f minPosTemp1 = new Vector3f();

    protected final Vector3f maxPosTemp1 = new Vector3f();

    protected final Vector4f colorTemp1 = new Vector4f();

    protected final Vector3f pos0Temp = new Vector3f();

    protected final Vector3f pos1Temp = new Vector3f();

    protected final Vector3f pos2Temp = new Vector3f();

    protected final Vector3f pos3Temp = new Vector3f();

    protected final Vector3f normalTemp = new Vector3f();

    protected final Vector3f originTemp = new Vector3f();

    public AABBOutline(AABB bb) {
        this.setBounds(bb);
    }

    public AABB getBounds() {
        return this.bb;
    }

    public void setBounds(AABB bb) {
        this.bb = bb;
    }

    @Override
    public void render(PoseStack ms, SuperRenderTypeBuffer buffer, Vec3 camera, float pt) {
        this.params.loadColor(this.colorTemp);
        Vector4f color = this.colorTemp;
        int lightmap = this.params.lightmap;
        boolean disableLineNormals = this.params.disableLineNormals;
        this.renderBox(ms, buffer, camera, this.bb, color, lightmap, disableLineNormals);
    }

    protected void renderBox(PoseStack ms, SuperRenderTypeBuffer buffer, Vec3 camera, AABB box, Vector4f color, int lightmap, boolean disableLineNormals) {
        Vector3f minPos = this.minPosTemp1;
        Vector3f maxPos = this.maxPosTemp1;
        boolean cameraInside = box.contains(camera);
        boolean cull = !cameraInside && !this.params.disableCull;
        float inflate = cameraInside ? -0.0078125F : 0.0078125F;
        box = box.move(camera.scale(-1.0));
        minPos.set((float) box.minX - inflate, (float) box.minY - inflate, (float) box.minZ - inflate);
        maxPos.set((float) box.maxX + inflate, (float) box.maxY + inflate, (float) box.maxZ + inflate);
        this.renderBoxFaces(ms, buffer, cull, this.params.getHighlightedFace(), minPos, maxPos, color, lightmap);
        float lineWidth = this.params.getLineWidth();
        if (lineWidth != 0.0F) {
            VertexConsumer consumer = buffer.getBuffer(RenderTypes.getOutlineSolid());
            this.renderBoxEdges(ms, consumer, minPos, maxPos, lineWidth, color, lightmap, disableLineNormals);
        }
    }

    protected void renderBoxFaces(PoseStack ms, SuperRenderTypeBuffer buffer, boolean cull, Direction highlightedFace, Vector3f minPos, Vector3f maxPos, Vector4f color, int lightmap) {
        PoseStack.Pose pose = ms.last();
        this.renderBoxFace(pose, buffer, cull, highlightedFace, minPos, maxPos, Direction.DOWN, color, lightmap);
        this.renderBoxFace(pose, buffer, cull, highlightedFace, minPos, maxPos, Direction.UP, color, lightmap);
        this.renderBoxFace(pose, buffer, cull, highlightedFace, minPos, maxPos, Direction.NORTH, color, lightmap);
        this.renderBoxFace(pose, buffer, cull, highlightedFace, minPos, maxPos, Direction.SOUTH, color, lightmap);
        this.renderBoxFace(pose, buffer, cull, highlightedFace, minPos, maxPos, Direction.WEST, color, lightmap);
        this.renderBoxFace(pose, buffer, cull, highlightedFace, minPos, maxPos, Direction.EAST, color, lightmap);
    }

    protected void renderBoxFace(PoseStack.Pose pose, SuperRenderTypeBuffer buffer, boolean cull, Direction highlightedFace, Vector3f minPos, Vector3f maxPos, Direction face, Vector4f color, int lightmap) {
        boolean highlighted = face == highlightedFace;
        Optional<AllSpecialTextures> optionalFaceTexture = this.params.faceTexture;
        if (optionalFaceTexture.isPresent()) {
            AllSpecialTextures faceTexture = (AllSpecialTextures) optionalFaceTexture.get();
            RenderType renderType = RenderTypes.getOutlineTranslucent(faceTexture.getLocation(), cull);
            VertexConsumer consumer = buffer.getLateBuffer(renderType);
            float alphaMult = highlighted ? 1.0F : 0.5F;
            this.colorTemp1.set(color.x(), color.y(), color.z(), color.w() * alphaMult);
            color = this.colorTemp1;
            this.renderBoxFace(pose, consumer, minPos, maxPos, face, color, lightmap);
        }
    }

    protected void renderBoxFace(PoseStack.Pose pose, VertexConsumer consumer, Vector3f minPos, Vector3f maxPos, Direction face, Vector4f color, int lightmap) {
        Vector3f pos0 = this.pos0Temp;
        Vector3f pos1 = this.pos1Temp;
        Vector3f pos2 = this.pos2Temp;
        Vector3f pos3 = this.pos3Temp;
        Vector3f normal = this.normalTemp;
        float minX = minPos.x();
        float minY = minPos.y();
        float minZ = minPos.z();
        float maxX = maxPos.x();
        float maxY = maxPos.y();
        float maxZ = maxPos.z();
        float maxU;
        float maxV;
        switch(face) {
            case DOWN:
                pos0.set(minX, minY, maxZ);
                pos1.set(minX, minY, minZ);
                pos2.set(maxX, minY, minZ);
                pos3.set(maxX, minY, maxZ);
                maxU = maxX - minX;
                maxV = maxZ - minZ;
                normal.set(0.0F, -1.0F, 0.0F);
                break;
            case UP:
                pos0.set(minX, maxY, minZ);
                pos1.set(minX, maxY, maxZ);
                pos2.set(maxX, maxY, maxZ);
                pos3.set(maxX, maxY, minZ);
                maxU = maxX - minX;
                maxV = maxZ - minZ;
                normal.set(0.0F, 1.0F, 0.0F);
                break;
            case NORTH:
                pos0.set(maxX, maxY, minZ);
                pos1.set(maxX, minY, minZ);
                pos2.set(minX, minY, minZ);
                pos3.set(minX, maxY, minZ);
                maxU = maxX - minX;
                maxV = maxY - minY;
                normal.set(0.0F, 0.0F, -1.0F);
                break;
            case SOUTH:
                pos0.set(minX, maxY, maxZ);
                pos1.set(minX, minY, maxZ);
                pos2.set(maxX, minY, maxZ);
                pos3.set(maxX, maxY, maxZ);
                maxU = maxX - minX;
                maxV = maxY - minY;
                normal.set(0.0F, 0.0F, 1.0F);
                break;
            case WEST:
                pos0.set(minX, maxY, minZ);
                pos1.set(minX, minY, minZ);
                pos2.set(minX, minY, maxZ);
                pos3.set(minX, maxY, maxZ);
                maxU = maxZ - minZ;
                maxV = maxY - minY;
                normal.set(-1.0F, 0.0F, 0.0F);
                break;
            case EAST:
                pos0.set(maxX, maxY, maxZ);
                pos1.set(maxX, minY, maxZ);
                pos2.set(maxX, minY, minZ);
                pos3.set(maxX, maxY, minZ);
                maxU = maxZ - minZ;
                maxV = maxY - minY;
                normal.set(1.0F, 0.0F, 0.0F);
                break;
            default:
                maxU = 1.0F;
                maxV = 1.0F;
        }
        this.bufferQuad(pose, consumer, pos0, pos1, pos2, pos3, color, 0.0F, 0.0F, maxU, maxV, lightmap, normal);
    }

    protected void renderBoxEdges(PoseStack ms, VertexConsumer consumer, Vector3f minPos, Vector3f maxPos, float lineWidth, Vector4f color, int lightmap, boolean disableNormals) {
        Vector3f origin = this.originTemp;
        PoseStack.Pose pose = ms.last();
        float lineLengthX = maxPos.x() - minPos.x();
        float lineLengthY = maxPos.y() - minPos.y();
        float lineLengthZ = maxPos.z() - minPos.z();
        origin.set(minPos);
        this.bufferCuboidLine(pose, consumer, origin, Direction.EAST, lineLengthX, lineWidth, color, lightmap, disableNormals);
        this.bufferCuboidLine(pose, consumer, origin, Direction.UP, lineLengthY, lineWidth, color, lightmap, disableNormals);
        this.bufferCuboidLine(pose, consumer, origin, Direction.SOUTH, lineLengthZ, lineWidth, color, lightmap, disableNormals);
        origin.set(maxPos.x(), minPos.y(), minPos.z());
        this.bufferCuboidLine(pose, consumer, origin, Direction.UP, lineLengthY, lineWidth, color, lightmap, disableNormals);
        this.bufferCuboidLine(pose, consumer, origin, Direction.SOUTH, lineLengthZ, lineWidth, color, lightmap, disableNormals);
        origin.set(minPos.x(), maxPos.y(), minPos.z());
        this.bufferCuboidLine(pose, consumer, origin, Direction.EAST, lineLengthX, lineWidth, color, lightmap, disableNormals);
        this.bufferCuboidLine(pose, consumer, origin, Direction.SOUTH, lineLengthZ, lineWidth, color, lightmap, disableNormals);
        origin.set(minPos.x(), minPos.y(), maxPos.z());
        this.bufferCuboidLine(pose, consumer, origin, Direction.EAST, lineLengthX, lineWidth, color, lightmap, disableNormals);
        this.bufferCuboidLine(pose, consumer, origin, Direction.UP, lineLengthY, lineWidth, color, lightmap, disableNormals);
        origin.set(minPos.x(), maxPos.y(), maxPos.z());
        this.bufferCuboidLine(pose, consumer, origin, Direction.EAST, lineLengthX, lineWidth, color, lightmap, disableNormals);
        origin.set(maxPos.x(), minPos.y(), maxPos.z());
        this.bufferCuboidLine(pose, consumer, origin, Direction.UP, lineLengthY, lineWidth, color, lightmap, disableNormals);
        origin.set(maxPos.x(), maxPos.y(), minPos.z());
        this.bufferCuboidLine(pose, consumer, origin, Direction.SOUTH, lineLengthZ, lineWidth, color, lightmap, disableNormals);
    }
}