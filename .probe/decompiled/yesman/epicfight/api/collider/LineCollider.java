package yesman.epicfight.api.collider;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.EpicFightRenderTypes;

public class LineCollider extends Collider {

    protected Vec3 modelVec;

    protected Vec3 worldVec;

    public LineCollider(double posX, double posY, double posZ, double vecX, double vecY, double vecZ) {
        this(getInitialAABB(posX, posY, posZ, vecX, vecY, vecZ), posX, posY, posZ, vecX, vecY, vecZ);
    }

    protected LineCollider(AABB outerAABB, double posX, double posY, double posZ, double vecX, double vecY, double vecZ) {
        super(new Vec3(posX, posY, posZ), outerAABB);
        this.modelVec = new Vec3(vecX, vecY, vecZ);
        this.worldVec = new Vec3(0.0, 0.0, 0.0);
    }

    static AABB getInitialAABB(double posX, double posY, double posZ, double vecX, double vecY, double vecZ) {
        Vec3 start = new Vec3(posX, posY, posZ);
        Vec3 end = new Vec3(vecX + posX, vecY + posY, vecZ + posZ);
        double length = Math.max(start.length(), end.length());
        return new AABB(length, length, length, -length, -length, -length);
    }

    @Override
    public void transform(OpenMatrix4f mat) {
        this.worldVec = OpenMatrix4f.transform(mat.removeTranslation(), this.modelVec);
        super.transform(mat);
    }

    @Override
    public boolean isCollide(Entity entity) {
        AABB opponent = entity.getBoundingBox();
        if (this.worldVec.x != 0.0 || !(this.worldCenter.x < opponent.minX) && !(this.worldCenter.x > opponent.maxX)) {
            double startX = Mth.clamp((opponent.minX + this.worldCenter.x) / -this.worldVec.x, 0.0, 1.0);
            double endX = Mth.clamp((opponent.maxX + this.worldCenter.x) / -this.worldVec.x, 0.0, 1.0);
            if (startX > endX) {
                double temp = startX;
                startX = endX;
                endX = temp;
            }
            if (endX == startX) {
                return false;
            } else if (this.worldVec.y != 0.0 || !(this.worldCenter.y < opponent.minY) && !(this.worldCenter.y > opponent.maxY)) {
                double startY = Mth.clamp((double) ((float) (opponent.minY - this.worldCenter.y)) / this.worldVec.y, 0.0, 1.0);
                double endY = Mth.clamp((double) ((float) (opponent.maxY - this.worldCenter.y)) / this.worldVec.y, 0.0, 1.0);
                if (startY > endY) {
                    double temp = startY;
                    startY = endY;
                    endY = temp;
                }
                double maxStart = startX < startY ? startY : startX;
                double minEnd = endX > endY ? endY : endX;
                if (maxStart >= minEnd) {
                    return false;
                } else if (this.worldVec.z != 0.0 || !(this.worldCenter.z < opponent.minZ) && !(this.worldCenter.z > opponent.maxZ)) {
                    double startZ = Mth.clamp((double) ((float) (opponent.minZ + this.worldCenter.z)) / -this.worldVec.z, 0.0, 1.0);
                    double endZ = Mth.clamp((double) ((float) (opponent.maxZ + this.worldCenter.z)) / -this.worldVec.z, 0.0, 1.0);
                    if (startZ > endZ) {
                        double temp = startZ;
                        startZ = endZ;
                        endZ = temp;
                    }
                    maxStart = maxStart < startZ ? startZ : maxStart;
                    minEnd = minEnd > endZ ? endZ : minEnd;
                    return !(maxStart >= minEnd);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public LineCollider deepCopy() {
        return new LineCollider(this.modelCenter.x, this.modelCenter.y, this.modelCenter.z, this.modelVec.x, this.modelVec.y, this.modelVec.z);
    }

    @Override
    public void drawInternal(PoseStack matrixStackIn, MultiBufferSource buffer, OpenMatrix4f pose, boolean red) {
        VertexConsumer vertexBuilder = buffer.getBuffer(EpicFightRenderTypes.debugCollider());
        OpenMatrix4f transpose = new OpenMatrix4f();
        OpenMatrix4f.transpose(pose, transpose);
        MathUtils.translateStack(matrixStackIn, pose);
        MathUtils.rotateStack(matrixStackIn, transpose);
        Matrix4f matrix = matrixStackIn.last().pose();
        float startX = (float) this.modelCenter.x;
        float startY = (float) this.modelCenter.y;
        float startZ = (float) this.modelCenter.z;
        float endX = (float) (this.modelCenter.x + this.modelVec.x);
        float endY = (float) (this.modelCenter.y + this.modelVec.y);
        float endZ = (float) (this.modelCenter.z + this.modelVec.z);
        float color = red ? 0.0F : 1.0F;
        vertexBuilder.vertex(matrix, startX, startY, startZ).color(1.0F, color, color, 1.0F).endVertex();
        vertexBuilder.vertex(matrix, endX, endY, endZ).color(1.0F, color, color, 1.0F).endVertex();
    }
}