package se.mickelus.tetra.blocks.scroll;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.Direction;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

@ParametersAreNonnullByDefault
public class QuadRenderer {

    public final QuadRenderer.Vertex[] vertexPositions;

    public final Vector3f normal;

    public QuadRenderer(float x, float y, float z, float w, float h, float u, float v, float texWidth, float texHeight, boolean mirror, Direction direction) {
        float u1 = u / texWidth;
        float u2 = (u + w) / texWidth;
        float v1 = v / texHeight;
        float v2 = (v + h) / texHeight;
        if (mirror) {
            float temp = u1;
            u1 = u2;
            u2 = temp;
        }
        switch(direction) {
            case DOWN:
            default:
                this.vertexPositions = new QuadRenderer.Vertex[] { new QuadRenderer.Vertex(x + 0.0F, y + 0.0F, z + 0.0F, u1, v1), new QuadRenderer.Vertex(x + w, y + 0.0F, z + 0.0F, u2, v1), new QuadRenderer.Vertex(x + w, y + 0.0F, z + h, u2, v2), new QuadRenderer.Vertex(x + 0.0F, y + 0.0F, z + h, u1, v2) };
                break;
            case UP:
                this.vertexPositions = new QuadRenderer.Vertex[] { new QuadRenderer.Vertex(x + w, y + 0.0F, z + 0.0F, u1, v1), new QuadRenderer.Vertex(x + 0.0F, y + 0.0F, z + 0.0F, u2, v1), new QuadRenderer.Vertex(x + 0.0F, y + 0.0F, z + h, u2, v2), new QuadRenderer.Vertex(x + w, y + 0.0F, z + h, u1, v2) };
                break;
            case WEST:
                this.vertexPositions = new QuadRenderer.Vertex[] { new QuadRenderer.Vertex(x + 0.0F, y + 0.0F, z + 0.0F, u1, v1), new QuadRenderer.Vertex(x + 0.0F, y + 0.0F, z + w, u2, v1), new QuadRenderer.Vertex(x + 0.0F, y + h, z + w, u2, v2), new QuadRenderer.Vertex(x + 0.0F, y + h, z + 0.0F, u1, v2) };
                break;
            case NORTH:
                this.vertexPositions = new QuadRenderer.Vertex[] { new QuadRenderer.Vertex(x + w, y + 0.0F, z + 0.0F, u1, v1), new QuadRenderer.Vertex(x + 0.0F, y + 0.0F, z + 0.0F, u2, v1), new QuadRenderer.Vertex(x + 0.0F, y + h, z + 0.0F, u2, v2), new QuadRenderer.Vertex(x + w, y + h, z + 0.0F, u1, v2) };
                break;
            case EAST:
                this.vertexPositions = new QuadRenderer.Vertex[] { new QuadRenderer.Vertex(x + 0.0F, y + 0.0F, z + w, u1, v1), new QuadRenderer.Vertex(x + 0.0F, y + 0.0F, z + 0.0F, u2, v1), new QuadRenderer.Vertex(x + 0.0F, y + h, z + 0.0F, u2, v2), new QuadRenderer.Vertex(x + 0.0F, y + h, z + w, u1, v2) };
                break;
            case SOUTH:
                this.vertexPositions = new QuadRenderer.Vertex[] { new QuadRenderer.Vertex(x + 0.0F, y + 0.0F, z + 0.0F, u1, v1), new QuadRenderer.Vertex(x + w, y + 0.0F, z + 0.0F, u2, v1), new QuadRenderer.Vertex(x + w, y + h, z + 0.0F, u2, v2), new QuadRenderer.Vertex(x + 0.0F, y + h, z + 0.0F, u1, v2) };
        }
        this.normal = direction.step();
        if (mirror) {
            this.normal.mul(-1.0F, 1.0F, 1.0F);
        }
    }

    public void render(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        matrixStack.pushPose();
        PoseStack.Pose last = matrixStack.last();
        Matrix4f matrix = last.pose();
        Matrix3f normal = last.normal();
        Vector3f vector3f = new Vector3f(this.normal);
        normal.transform(vector3f);
        float originX = vector3f.x();
        float originY = vector3f.y();
        float originZ = vector3f.z();
        for (QuadRenderer.Vertex vertex : this.vertexPositions) {
            Vector4f pos = new Vector4f(vertex.pos.x() / 16.0F, vertex.pos.y() / 16.0F, vertex.pos.z() / 16.0F, 1.0F);
            matrix.transform(pos);
            buffer.vertex(pos.x(), pos.y(), pos.z(), red, green, blue, alpha, vertex.u, vertex.v, packedOverlay, packedLight, originX, originY, originZ);
        }
        matrixStack.popPose();
    }

    static class Vertex {

        final Vector3f pos;

        final float u;

        final float v;

        public Vertex(float x, float y, float z, float texU, float texV) {
            this(new Vector3f(x, y, z), texU, texV);
        }

        public Vertex(Vector3f pos, float u, float v) {
            this.pos = pos;
            this.u = u;
            this.v = v;
        }
    }
}