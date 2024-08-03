package net.minecraftforge.client.model.pipeline;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Transformation;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class TransformingVertexPipeline extends VertexConsumerWrapper {

    private final Transformation transformation;

    public TransformingVertexPipeline(VertexConsumer parent, Transformation transformation) {
        super(parent);
        this.transformation = transformation;
    }

    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        Vector4f vec = new Vector4f((float) x, (float) y, (float) z, 1.0F);
        this.transformation.transformPosition(vec);
        vec.div(vec.w);
        return super.vertex((double) vec.x(), (double) vec.y(), (double) vec.z());
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        Vector3f vec = new Vector3f(x, y, z);
        this.transformation.transformNormal(vec);
        vec.normalize();
        return super.normal(vec.x(), vec.y(), vec.z());
    }
}