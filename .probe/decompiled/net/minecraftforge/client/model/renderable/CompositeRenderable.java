package net.minecraftforge.client.model.renderable;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class CompositeRenderable implements IRenderable<CompositeRenderable.Transforms> {

    private final List<CompositeRenderable.Component> components = new ArrayList();

    private CompositeRenderable() {
    }

    public void render(PoseStack poseStack, MultiBufferSource bufferSource, ITextureRenderTypeLookup textureRenderTypeLookup, int lightmap, int overlay, float partialTick, CompositeRenderable.Transforms context) {
        for (CompositeRenderable.Component component : this.components) {
            component.render(poseStack, bufferSource, textureRenderTypeLookup, lightmap, overlay, context);
        }
    }

    public static CompositeRenderable.Builder builder() {
        return new CompositeRenderable.Builder();
    }

    public static class Builder {

        private final CompositeRenderable renderable = new CompositeRenderable();

        private Builder() {
        }

        public CompositeRenderable.PartBuilder<CompositeRenderable.Builder> child(String name) {
            CompositeRenderable.Component child = new CompositeRenderable.Component(name);
            this.renderable.components.add(child);
            return new CompositeRenderable.PartBuilder<>(this, child);
        }

        public CompositeRenderable get() {
            return this.renderable;
        }
    }

    private static class Component {

        private final String name;

        private final List<CompositeRenderable.Component> children = new ArrayList();

        private final List<CompositeRenderable.Mesh> meshes = new ArrayList();

        public Component(String name) {
            this.name = name;
        }

        public void render(PoseStack poseStack, MultiBufferSource bufferSource, ITextureRenderTypeLookup textureRenderTypeLookup, int lightmap, int overlay, CompositeRenderable.Transforms context) {
            Matrix4f matrix = context.getTransform(this.name);
            if (matrix != null) {
                poseStack.pushPose();
                poseStack.mulPoseMatrix(matrix);
            }
            for (CompositeRenderable.Component part : this.children) {
                part.render(poseStack, bufferSource, textureRenderTypeLookup, lightmap, overlay, context);
            }
            for (CompositeRenderable.Mesh mesh : this.meshes) {
                mesh.render(poseStack, bufferSource, textureRenderTypeLookup, lightmap, overlay);
            }
            if (matrix != null) {
                poseStack.popPose();
            }
        }
    }

    private static class Mesh {

        private final ResourceLocation texture;

        private final List<BakedQuad> quads = new ArrayList();

        public Mesh(ResourceLocation texture) {
            this.texture = texture;
        }

        public void render(PoseStack poseStack, MultiBufferSource bufferSource, ITextureRenderTypeLookup textureRenderTypeLookup, int lightmap, int overlay) {
            VertexConsumer consumer = bufferSource.getBuffer(textureRenderTypeLookup.get(this.texture));
            for (BakedQuad quad : this.quads) {
                consumer.putBulkData(poseStack.last(), quad, 1.0F, 1.0F, 1.0F, 1.0F, lightmap, overlay, true);
            }
        }
    }

    public static class PartBuilder<T> {

        private final T parent;

        private final CompositeRenderable.Component component;

        private PartBuilder(T parent, CompositeRenderable.Component component) {
            this.parent = parent;
            this.component = component;
        }

        public CompositeRenderable.PartBuilder<CompositeRenderable.PartBuilder<T>> child(String name) {
            CompositeRenderable.Component child = new CompositeRenderable.Component(this.component.name + "/" + name);
            this.component.children.add(child);
            return new CompositeRenderable.PartBuilder<>(this, child);
        }

        public CompositeRenderable.PartBuilder<T> addMesh(ResourceLocation texture, List<BakedQuad> quads) {
            CompositeRenderable.Mesh mesh = new CompositeRenderable.Mesh(texture);
            mesh.quads.addAll(quads);
            this.component.meshes.add(mesh);
            return this;
        }

        public T end() {
            return this.parent;
        }
    }

    public static class Transforms {

        public static final CompositeRenderable.Transforms EMPTY = new CompositeRenderable.Transforms(ImmutableMap.of());

        private final ImmutableMap<String, Matrix4f> parts;

        public static CompositeRenderable.Transforms of(ImmutableMap<String, Matrix4f> parts) {
            return new CompositeRenderable.Transforms(parts);
        }

        private Transforms(ImmutableMap<String, Matrix4f> parts) {
            this.parts = parts;
        }

        @Nullable
        public Matrix4f getTransform(String part) {
            return (Matrix4f) this.parts.get(part);
        }
    }
}