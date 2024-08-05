package team.lodestar.lodestone.systems.rendering;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.helpers.RenderHelper;
import team.lodestar.lodestone.helpers.VecHelper;
import team.lodestar.lodestone.systems.rendering.trail.TrailPoint;
import team.lodestar.lodestone.systems.rendering.trail.TrailRenderPoint;

public class VFXBuilders {

    public static VFXBuilders.ScreenVFXBuilder createScreen() {
        return new VFXBuilders.ScreenVFXBuilder();
    }

    public static VFXBuilders.WorldVFXBuilder createWorld() {
        return new VFXBuilders.WorldVFXBuilder();
    }

    public static class ScreenVFXBuilder {

        float r = 1.0F;

        float g = 1.0F;

        float b = 1.0F;

        float a = 1.0F;

        int light = -1;

        float u0 = 0.0F;

        float v0 = 0.0F;

        float u1 = 1.0F;

        float v1 = 1.0F;

        float x0 = 0.0F;

        float y0 = 0.0F;

        float x1 = 1.0F;

        float y1 = 1.0F;

        int zLevel;

        VertexFormat format;

        Supplier<ShaderInstance> shader = GameRenderer::m_172817_;

        ResourceLocation texture;

        VFXBuilders.ScreenVFXBuilder.ScreenVertexPlacementSupplier supplier;

        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();

        public VFXBuilders.ScreenVFXBuilder setPosTexDefaultFormat() {
            this.supplier = (b, l, x, y, u, v) -> b.m_252986_(l, x, y, (float) this.zLevel).uv(u, v).endVertex();
            this.format = DefaultVertexFormat.POSITION_TEX;
            return this;
        }

        public VFXBuilders.ScreenVFXBuilder setPosColorDefaultFormat() {
            this.supplier = (b, l, x, y, u, v) -> b.m_252986_(l, x, y, (float) this.zLevel).color(this.r, this.g, this.b, this.a).endVertex();
            this.format = DefaultVertexFormat.POSITION_COLOR;
            return this;
        }

        public VFXBuilders.ScreenVFXBuilder setPosColorTexDefaultFormat() {
            this.supplier = (b, l, x, y, u, v) -> b.m_252986_(l, x, y, (float) this.zLevel).color(this.r, this.g, this.b, this.a).uv(u, v).endVertex();
            this.format = DefaultVertexFormat.POSITION_COLOR_TEX;
            return this;
        }

        public VFXBuilders.ScreenVFXBuilder setPosColorTexLightmapDefaultFormat() {
            this.supplier = (b, l, x, y, u, v) -> b.m_252986_(l, x, y, (float) this.zLevel).color(this.r, this.g, this.b, this.a).uv(u, v).uv2(this.light).endVertex();
            this.format = DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP;
            return this;
        }

        public VFXBuilders.ScreenVFXBuilder setFormat(VertexFormat format) {
            this.format = format;
            return this;
        }

        public VFXBuilders.ScreenVFXBuilder setShaderTexture(ResourceLocation texture) {
            this.texture = texture;
            return this;
        }

        public VFXBuilders.ScreenVFXBuilder setShader(Supplier<ShaderInstance> shader) {
            this.shader = shader;
            return this;
        }

        public VFXBuilders.ScreenVFXBuilder setShader(ShaderInstance shader) {
            this.shader = () -> shader;
            return this;
        }

        public VFXBuilders.ScreenVFXBuilder setVertexSupplier(VFXBuilders.ScreenVFXBuilder.ScreenVertexPlacementSupplier supplier) {
            this.supplier = supplier;
            return this;
        }

        public VFXBuilders.ScreenVFXBuilder overrideBufferBuilder(BufferBuilder builder) {
            this.bufferbuilder = builder;
            return this;
        }

        public VFXBuilders.ScreenVFXBuilder setLight(int light) {
            this.light = light;
            return this;
        }

        public VFXBuilders.ScreenVFXBuilder setColor(Color color) {
            return this.setColor((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue());
        }

        public VFXBuilders.ScreenVFXBuilder setColor(Color color, float a) {
            return this.setColor(color).setAlpha(a);
        }

        public VFXBuilders.ScreenVFXBuilder setColor(float r, float g, float b, float a) {
            return this.setColor(r, g, b).setAlpha(a);
        }

        public VFXBuilders.ScreenVFXBuilder setColor(float r, float g, float b) {
            this.r = r / 255.0F;
            this.g = g / 255.0F;
            this.b = b / 255.0F;
            return this;
        }

        public VFXBuilders.ScreenVFXBuilder setColorRaw(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
            return this;
        }

        public VFXBuilders.ScreenVFXBuilder setAlpha(float a) {
            this.a = a;
            return this;
        }

        public VFXBuilders.ScreenVFXBuilder setPositionWithWidth(float x, float y, float width, float height) {
            return this.setPosition(x, y, x + width, y + height);
        }

        public VFXBuilders.ScreenVFXBuilder setPosition(float x0, float y0, float x1, float y1) {
            this.x0 = x0;
            this.y0 = y0;
            this.x1 = x1;
            this.y1 = y1;
            return this;
        }

        public VFXBuilders.ScreenVFXBuilder setZLevel(int z) {
            this.zLevel = z;
            return this;
        }

        public VFXBuilders.ScreenVFXBuilder setUVWithWidth(float u, float v, float width, float height, float canvasSize) {
            return this.setUVWithWidth(u, v, width, height, canvasSize, canvasSize);
        }

        public VFXBuilders.ScreenVFXBuilder setUVWithWidth(float u, float v, float width, float height, float canvasSizeX, float canvasSizeY) {
            return this.setUVWithWidth(u / canvasSizeX, v / canvasSizeY, width / canvasSizeX, height / canvasSizeY);
        }

        public VFXBuilders.ScreenVFXBuilder setUVWithWidth(float u, float v, float width, float height) {
            this.u0 = u;
            this.v0 = v;
            this.u1 = u + width;
            this.v1 = v + height;
            return this;
        }

        public VFXBuilders.ScreenVFXBuilder setUV(float u0, float v0, float u1, float v1, float canvasSize) {
            return this.setUV(u0, v0, u1, v1, canvasSize, canvasSize);
        }

        public VFXBuilders.ScreenVFXBuilder setUV(float u0, float v0, float u1, float v1, float canvasSizeX, float canvasSizeY) {
            return this.setUV(u0 / canvasSizeX, v0 / canvasSizeY, u1 / canvasSizeX, v1 / canvasSizeY);
        }

        public VFXBuilders.ScreenVFXBuilder setUV(float u0, float v0, float u1, float v1) {
            this.u0 = u0;
            this.v0 = v0;
            this.u1 = u1;
            this.v1 = v1;
            return this;
        }

        public VFXBuilders.ScreenVFXBuilder blit(PoseStack stack) {
            Matrix4f last = stack.last().pose();
            RenderSystem.setShader(this.shader);
            if (this.texture != null) {
                RenderSystem.setShaderTexture(0, this.texture);
            }
            this.supplier.placeVertex(this.bufferbuilder, last, this.x0, this.y1, this.u0, this.v1);
            this.supplier.placeVertex(this.bufferbuilder, last, this.x1, this.y1, this.u1, this.v1);
            this.supplier.placeVertex(this.bufferbuilder, last, this.x1, this.y0, this.u1, this.v0);
            this.supplier.placeVertex(this.bufferbuilder, last, this.x0, this.y0, this.u0, this.v0);
            return this;
        }

        public VFXBuilders.ScreenVFXBuilder draw(PoseStack stack) {
            if (this.bufferbuilder.building()) {
                this.bufferbuilder.end();
            }
            this.begin();
            this.blit(stack);
            this.end();
            return this;
        }

        public VFXBuilders.ScreenVFXBuilder endAndProceed() {
            return this.end().begin();
        }

        public VFXBuilders.ScreenVFXBuilder begin() {
            this.bufferbuilder.begin(VertexFormat.Mode.QUADS, this.format);
            return this;
        }

        public VFXBuilders.ScreenVFXBuilder end() {
            BufferUploader.drawWithShader(this.bufferbuilder.end());
            return this;
        }

        private interface ScreenVertexPlacementSupplier {

            void placeVertex(BufferBuilder var1, Matrix4f var2, float var3, float var4, float var5, float var6);
        }
    }

    public static class WorldVFXBuilder {

        protected float r = 1.0F;

        protected float g = 1.0F;

        protected float b = 1.0F;

        protected float a = 1.0F;

        protected int light = 15728880;

        protected float u0 = 0.0F;

        protected float v0 = 0.0F;

        protected float u1 = 1.0F;

        protected float v1 = 1.0F;

        protected MultiBufferSource bufferSource = RenderHandler.DELAYED_RENDER.getTarget();

        protected RenderType renderType;

        protected VertexFormat format;

        protected VFXBuilders.WorldVFXBuilder.WorldVertexConsumerActor supplier;

        protected VertexConsumer vertexConsumer;

        protected HashMap<Object, Consumer<VFXBuilders.WorldVFXBuilder>> modularActors = new HashMap();

        protected int modularActorAddIndex;

        protected int modularActorGetIndex;

        public static final HashMap<VertexFormatElement, VFXBuilders.WorldVFXBuilder.WorldVertexConsumerActor> CONSUMER_INFO_MAP = new HashMap();

        public VFXBuilders.WorldVFXBuilder replaceBufferSource(RenderHandler.LodestoneRenderLayer renderLayer) {
            return this.replaceBufferSource(renderLayer.getTarget());
        }

        public VFXBuilders.WorldVFXBuilder replaceBufferSource(MultiBufferSource bufferSource) {
            this.bufferSource = bufferSource;
            return this;
        }

        public VFXBuilders.WorldVFXBuilder setRenderType(RenderType renderType) {
            return this.setRenderTypeRaw(renderType).setFormat(renderType.format()).setVertexConsumer(this.bufferSource.getBuffer(renderType));
        }

        public VFXBuilders.WorldVFXBuilder setRenderTypeRaw(RenderType renderType) {
            this.renderType = renderType;
            return this;
        }

        public VFXBuilders.WorldVFXBuilder setFormat(VertexFormat format) {
            ImmutableList<VertexFormatElement> elements = format.getElements();
            return this.setFormatRaw(format).setVertexSupplier((consumer, last, builder, x, y, z, u, v) -> {
                UnmodifiableIterator var10 = elements.iterator();
                while (var10.hasNext()) {
                    VertexFormatElement element = (VertexFormatElement) var10.next();
                    ((VFXBuilders.WorldVFXBuilder.WorldVertexConsumerActor) CONSUMER_INFO_MAP.get(element)).placeVertex(consumer, last, this, x, y, z, u, v);
                }
                consumer.endVertex();
            });
        }

        public VFXBuilders.WorldVFXBuilder setFormatRaw(VertexFormat format) {
            this.format = format;
            return this;
        }

        public VFXBuilders.WorldVFXBuilder setVertexSupplier(VFXBuilders.WorldVFXBuilder.WorldVertexConsumerActor supplier) {
            this.supplier = supplier;
            return this;
        }

        public VFXBuilders.WorldVFXBuilder setVertexConsumer(VertexConsumer vertexConsumer) {
            this.vertexConsumer = vertexConsumer;
            return this;
        }

        public VertexConsumer getVertexConsumer() {
            if (this.vertexConsumer == null) {
                this.setVertexConsumer(this.bufferSource.getBuffer(this.renderType));
            }
            return this.vertexConsumer;
        }

        public VFXBuilders.WorldVFXBuilder addModularActor(Consumer<VFXBuilders.WorldVFXBuilder> actor) {
            return this.addModularActor(this.modularActorAddIndex, actor);
        }

        public VFXBuilders.WorldVFXBuilder addModularActor(Object key, Consumer<VFXBuilders.WorldVFXBuilder> actor) {
            if (this.modularActors == null) {
                this.modularActors = new HashMap();
            }
            this.modularActors.put(key, actor);
            return this;
        }

        public Optional<HashMap<Object, Consumer<VFXBuilders.WorldVFXBuilder>>> getModularActors() {
            return Optional.ofNullable(this.modularActors);
        }

        public Optional<Consumer<VFXBuilders.WorldVFXBuilder>> getNextModularActor() {
            return Optional.ofNullable(this.modularActors).map(m -> (Consumer) m.get(this.modularActorGetIndex++));
        }

        public MultiBufferSource getBufferSource() {
            return this.bufferSource;
        }

        public RenderType getRenderType() {
            return this.renderType;
        }

        public VertexFormat getFormat() {
            return this.format;
        }

        public VFXBuilders.WorldVFXBuilder.WorldVertexConsumerActor getSupplier() {
            return this.supplier;
        }

        public VFXBuilders.WorldVFXBuilder setColor(Color color) {
            return this.setColor((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue());
        }

        public VFXBuilders.WorldVFXBuilder setColor(Color color, float a) {
            return this.setColor(color).setAlpha(a);
        }

        public VFXBuilders.WorldVFXBuilder setColor(float r, float g, float b, float a) {
            return this.setColor(r, g, b).setAlpha(a);
        }

        public VFXBuilders.WorldVFXBuilder setColor(float r, float g, float b) {
            this.r = r / 255.0F;
            this.g = g / 255.0F;
            this.b = b / 255.0F;
            return this;
        }

        public VFXBuilders.WorldVFXBuilder setColorRaw(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
            return this;
        }

        public VFXBuilders.WorldVFXBuilder setAlpha(float a) {
            this.a = a;
            return this;
        }

        public VFXBuilders.WorldVFXBuilder setLight(int light) {
            this.light = light;
            return this;
        }

        public VFXBuilders.WorldVFXBuilder setUV(float u0, float v0, float u1, float v1) {
            this.u0 = u0;
            this.v0 = v0;
            this.u1 = u1;
            this.v1 = v1;
            return this;
        }

        public VFXBuilders.WorldVFXBuilder renderBeam(Matrix4f last, BlockPos start, BlockPos end, float width) {
            return this.renderBeam(last, VecHelper.getCenterOf(start), VecHelper.getCenterOf(end), width);
        }

        public VFXBuilders.WorldVFXBuilder renderBeam(@Nullable Matrix4f last, Vec3 start, Vec3 end, float width) {
            Minecraft minecraft = Minecraft.getInstance();
            Vec3 cameraPosition = minecraft.getBlockEntityRenderDispatcher().camera.getPosition();
            return this.renderBeam(last, start, end, width, cameraPosition);
        }

        public VFXBuilders.WorldVFXBuilder renderBeam(@Nullable Matrix4f last, Vec3 start, Vec3 end, float width, Consumer<VFXBuilders.WorldVFXBuilder> consumer) {
            Minecraft minecraft = Minecraft.getInstance();
            Vec3 cameraPosition = minecraft.getBlockEntityRenderDispatcher().camera.getPosition();
            return this.renderBeam(last, start, end, width, cameraPosition, consumer);
        }

        public VFXBuilders.WorldVFXBuilder renderBeam(@Nullable Matrix4f last, Vec3 start, Vec3 end, float width, Vec3 cameraPosition) {
            return this.renderBeam(last, start, end, width, cameraPosition, builder -> {
            });
        }

        public VFXBuilders.WorldVFXBuilder renderBeam(@Nullable Matrix4f last, Vec3 start, Vec3 end, float width, Vec3 cameraPosition, Consumer<VFXBuilders.WorldVFXBuilder> consumer) {
            Vec3 delta = end.subtract(start);
            Vec3 normal = start.subtract(cameraPosition).cross(delta).normalize().multiply((double) (width / 2.0F), (double) (width / 2.0F), (double) (width / 2.0F));
            Vec3[] positions = new Vec3[] { start.subtract(normal), start.add(normal), end.add(normal), end.subtract(normal) };
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, (float) positions[0].x, (float) positions[0].y, (float) positions[0].z, this.u0, this.v1);
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, (float) positions[1].x, (float) positions[1].y, (float) positions[1].z, this.u1, this.v1);
            consumer.accept(this);
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, (float) positions[2].x, (float) positions[2].y, (float) positions[2].z, this.u1, this.v0);
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, (float) positions[3].x, (float) positions[3].y, (float) positions[3].z, this.u0, this.v0);
            return this;
        }

        public VFXBuilders.WorldVFXBuilder renderTrail(PoseStack stack, List<TrailPoint> trailSegments, float width) {
            return this.renderTrail(stack, trailSegments, f -> width, f -> {
            });
        }

        public VFXBuilders.WorldVFXBuilder renderTrail(PoseStack stack, List<TrailPoint> trailSegments, Function<Float, Float> widthFunc) {
            return this.renderTrail(stack, trailSegments, widthFunc, f -> {
            });
        }

        public VFXBuilders.WorldVFXBuilder renderTrail(PoseStack stack, List<TrailPoint> trailSegments, Function<Float, Float> widthFunc, Consumer<Float> vfxOperator) {
            return this.renderTrail(stack.last().pose(), trailSegments, widthFunc, vfxOperator);
        }

        public VFXBuilders.WorldVFXBuilder renderTrail(Matrix4f pose, List<TrailPoint> trailSegments, Function<Float, Float> widthFunc, Consumer<Float> vfxOperator) {
            if (trailSegments.size() < 2) {
                return this;
            } else {
                List<Vector4f> positions = trailSegments.stream().map(TrailPoint::getMatrixPosition).peek(p -> p.mul(pose)).toList();
                int count = trailSegments.size() - 1;
                float increment = 1.0F / (float) count;
                TrailRenderPoint[] points = new TrailRenderPoint[trailSegments.size()];
                for (int i = 1; i < count; i++) {
                    float width = (Float) widthFunc.apply(increment * (float) i);
                    Vector4f previous = (Vector4f) positions.get(i - 1);
                    Vector4f current = (Vector4f) positions.get(i);
                    Vector4f next = (Vector4f) positions.get(i + 1);
                    points[i] = new TrailRenderPoint(current, RenderHelper.perpendicularTrailPoints(previous, next, width));
                }
                points[0] = new TrailRenderPoint((Vector4f) positions.get(0), RenderHelper.perpendicularTrailPoints((Vector4f) positions.get(0), (Vector4f) positions.get(1), (Float) widthFunc.apply(0.0F)));
                points[count] = new TrailRenderPoint((Vector4f) positions.get(count), RenderHelper.perpendicularTrailPoints((Vector4f) positions.get(count - 1), (Vector4f) positions.get(count), (Float) widthFunc.apply(1.0F)));
                return this.renderPoints(points, this.u0, this.v0, this.u1, this.v1, vfxOperator);
            }
        }

        public VFXBuilders.WorldVFXBuilder renderPoints(TrailRenderPoint[] points, float u0, float v0, float u1, float v1, Consumer<Float> vfxOperator) {
            int count = points.length - 1;
            float increment = 1.0F / (float) count;
            vfxOperator.accept(0.0F);
            points[0].renderStart(this.getVertexConsumer(), this, u0, v0, u1, Mth.lerp(increment, v0, v1));
            for (int i = 1; i < count; i++) {
                float current = Mth.lerp((float) i * increment, v0, v1);
                vfxOperator.accept(current);
                points[i].renderMid(this.getVertexConsumer(), this, u0, current, u1, current);
            }
            vfxOperator.accept(1.0F);
            points[count].renderEnd(this.getVertexConsumer(), this, u0, Mth.lerp((float) count * increment, v0, v1), u1, v1);
            return this;
        }

        public VFXBuilders.WorldVFXBuilder renderQuad(PoseStack stack, float size) {
            return this.renderQuad(stack, size, size);
        }

        public VFXBuilders.WorldVFXBuilder renderQuad(PoseStack stack, float width, float height) {
            Vector3f[] positions = new Vector3f[] { new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F) };
            return this.renderQuad(stack, positions, width, height);
        }

        public VFXBuilders.WorldVFXBuilder renderQuad(PoseStack stack, Vector3f[] positions, float size) {
            return this.renderQuad(stack, positions, size, size);
        }

        public VFXBuilders.WorldVFXBuilder renderQuad(PoseStack stack, Vector3f[] positions, float width, float height) {
            for (Vector3f position : positions) {
                position.mul(width, height, width);
            }
            return this.renderQuad(stack.last().pose(), positions);
        }

        public VFXBuilders.WorldVFXBuilder renderQuad(Matrix4f last, Vector3f[] positions) {
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, positions[0].x(), positions[0].y(), positions[0].z(), this.u0, this.v1);
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, positions[1].x(), positions[1].y(), positions[1].z(), this.u1, this.v1);
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, positions[2].x(), positions[2].y(), positions[2].z(), this.u1, this.v0);
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, positions[3].x(), positions[3].y(), positions[3].z(), this.u0, this.v0);
            return this;
        }

        public VFXBuilders.WorldVFXBuilder renderSphere(PoseStack stack, float radius, int longs, int lats) {
            Matrix4f last = stack.last().pose();
            float startU = this.u0;
            float startV = this.v0;
            float endU = (float) (Math.PI * 2) * this.u1;
            float endV = (float) Math.PI * this.v1;
            float stepU = (endU - startU) / (float) longs;
            float stepV = (endV - startV) / (float) lats;
            for (int i = 0; i < longs; i++) {
                for (int j = 0; j < lats; j++) {
                    float u = (float) i * stepU + startU;
                    float v = (float) j * stepV + startV;
                    float un = i + 1 == longs ? endU : (float) (i + 1) * stepU + startU;
                    float vn = j + 1 == lats ? endV : (float) (j + 1) * stepV + startV;
                    Vector3f p0 = RenderHelper.parametricSphere(u, v, radius);
                    Vector3f p1 = RenderHelper.parametricSphere(u, vn, radius);
                    Vector3f p2 = RenderHelper.parametricSphere(un, v, radius);
                    Vector3f p3 = RenderHelper.parametricSphere(un, vn, radius);
                    float textureU = u / endU * radius;
                    float textureV = v / endV * radius;
                    float textureUN = un / endU * radius;
                    float textureVN = vn / endV * radius;
                    this.supplier.placeVertex(this.getVertexConsumer(), last, this, p0.x(), p0.y(), p0.z(), textureU, textureV);
                    this.supplier.placeVertex(this.getVertexConsumer(), last, this, p2.x(), p2.y(), p2.z(), textureUN, textureV);
                    this.supplier.placeVertex(this.getVertexConsumer(), last, this, p1.x(), p1.y(), p1.z(), textureU, textureVN);
                    this.supplier.placeVertex(this.getVertexConsumer(), last, this, p3.x(), p3.y(), p3.z(), textureUN, textureVN);
                    this.supplier.placeVertex(this.getVertexConsumer(), last, this, p1.x(), p1.y(), p1.z(), textureU, textureVN);
                    this.supplier.placeVertex(this.getVertexConsumer(), last, this, p2.x(), p2.y(), p2.z(), textureUN, textureV);
                }
            }
            return this;
        }

        static {
            CONSUMER_INFO_MAP.put(DefaultVertexFormat.ELEMENT_POSITION, (VFXBuilders.WorldVFXBuilder.WorldVertexConsumerActor) (consumer, last, builder, x, y, z, u, v) -> {
                if (last == null) {
                    consumer.vertex((double) x, (double) y, (double) z);
                } else {
                    consumer.vertex(last, x, y, z);
                }
            });
            CONSUMER_INFO_MAP.put(DefaultVertexFormat.ELEMENT_COLOR, (VFXBuilders.WorldVFXBuilder.WorldVertexConsumerActor) (consumer, last, builder, x, y, z, u, v) -> consumer.color(builder.r, builder.g, builder.b, builder.a));
            CONSUMER_INFO_MAP.put(DefaultVertexFormat.ELEMENT_UV0, (VFXBuilders.WorldVFXBuilder.WorldVertexConsumerActor) (consumer, last, builder, x, y, z, u, v) -> consumer.uv(u, v));
            CONSUMER_INFO_MAP.put(DefaultVertexFormat.ELEMENT_UV2, (VFXBuilders.WorldVFXBuilder.WorldVertexConsumerActor) (consumer, last, builder, x, y, z, u, v) -> consumer.uv2(builder.light));
        }

        public interface WorldVertexConsumerActor {

            void placeVertex(VertexConsumer var1, Matrix4f var2, VFXBuilders.WorldVFXBuilder var3, float var4, float var5, float var6, float var7, float var8);
        }
    }
}