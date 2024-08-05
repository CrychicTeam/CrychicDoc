package me.fengming.renderjs.core;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Transformation;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import java.util.Arrays;
import me.fengming.renderjs.core.objects.BlocksDisplay;
import me.fengming.renderjs.core.objects.IconsDisplay;
import me.fengming.renderjs.core.objects.ItemsDisplay;
import me.fengming.renderjs.core.objects.Lines;
import me.fengming.renderjs.core.objects.Quads;
import me.fengming.renderjs.core.objects.Triangles;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Display;
import org.joml.Quaternionf;

@RemapPrefixForJS("rjs$")
public abstract class RenderObject {

    public static Minecraft mc = Minecraft.getInstance();

    public static Camera camera = mc.gameRenderer.getMainCamera();

    protected boolean broken;

    protected PoseStack poseStack = null;

    protected RenderObject.ObjectType type;

    protected float[] vertices;

    protected boolean enableBlend = true;

    protected boolean enableDepthTest = true;

    protected boolean enableCull = false;

    protected Display.BillboardConstraints billboard = Display.BillboardConstraints.FIXED;

    protected Transformation transformation = new Transformation(null);

    private final float[] innerOffsets = new float[300];

    private int innerOffsetsLength = 0;

    private final float[] offsets = new float[300];

    private int offsetsLength = 0;

    public RenderObject(float[] vertices, RenderObject.ObjectType type) {
        this.vertices = vertices;
        this.type = type;
    }

    public abstract void loadInner(CompoundTag var1);

    public void load(CompoundTag object) {
        if (object.contains("options")) {
            CompoundTag options = object.getCompound("options");
            if (options.contains("blend")) {
                this.enableBlend = options.getBoolean("blend");
            }
            if (options.contains("depth_test")) {
                this.enableDepthTest = options.getBoolean("depth_test");
            }
            if (options.contains("cull")) {
                this.enableCull = options.getBoolean("cull");
            }
            if (options.contains("billboard")) {
                this.billboard = Display.BillboardConstraints.valueOf(options.getString("billboard").toUpperCase());
            }
            if (options.contains("transformation")) {
                this.rjs$setTransformation(options.get("transformation"));
            }
        }
        this.loadInner(object);
    }

    public static RenderObject loadFromNbt(CompoundTag object) {
        if (!object.contains("type")) {
            ConsoleJS.CLIENT.error("Missing a necessary key: type");
            return null;
        } else {
            String type = object.getString("type");
            if (!Arrays.stream(RenderObject.ObjectType.values()).map(Enum::toString).toList().contains(type.toUpperCase())) {
                ConsoleJS.CLIENT.error("Type " + type + " does not exist");
                return null;
            } else {
                RenderObject.ObjectType objectType = RenderObject.ObjectType.valueOf(type.toUpperCase());
                if (!object.contains("vertices")) {
                    ConsoleJS.CLIENT.error("Missing a necessary key: vertices");
                    return null;
                } else {
                    ListTag verticesList = object.getList("vertices", 6);
                    float[] vertices = new float[verticesList.size()];
                    for (int i = 0; i < verticesList.size(); i++) {
                        vertices[i] = (float) verticesList.getDouble(i);
                    }
                    RenderObject renderObject = null;
                    switch(objectType) {
                        case LINES:
                        case LINE_STRIP:
                            renderObject = new Lines(vertices, objectType);
                            break;
                        case TRIANGLES:
                        case TRIANGLE_STRIP:
                        case TRIANGLE_FAN:
                            renderObject = new Triangles(vertices, objectType);
                            break;
                        case QUADS:
                        case RECTANGLES:
                            renderObject = new Quads(vertices, objectType);
                            break;
                        case BLOCKS:
                            renderObject = new BlocksDisplay(vertices, objectType);
                            break;
                        case ITEMS:
                            renderObject = new ItemsDisplay(vertices, objectType);
                            break;
                        case ICONS:
                            renderObject = new IconsDisplay(vertices, objectType);
                    }
                    renderObject.load(object);
                    return renderObject;
                }
            }
        }
    }

    public void rjs$setPoseStack(PoseStack poseStack) {
        this.poseStack = poseStack;
    }

    @Info("Directly modify vertices to new ones\n")
    public void rjs$setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    public void setTransformation(Transformation transformation) {
        this.transformation = transformation;
    }

    @Info("Transform vertex matrix.\n")
    public void rjs$setTransformation(Tag tag) {
        Transformation.EXTENDED_CODEC.decode(NbtOps.INSTANCE, tag).result().ifPresent(pair -> this.setTransformation((Transformation) pair.getFirst()));
    }

    @Info("Directly modify the value of a given vertex.\n")
    public void rjs$modifyVertices(int index, float value) {
        this.vertices[index] = value;
    }

    @Info(value = "Offset vertices by given values.\n", params = { @Param(name = "i", value = "The specified offset index starts from 0. If multiple offsets are to be applied, the index value is increased sequentially. Max to 100."), @Param(name = "x", value = "Offset in the x-direction."), @Param(name = "y", value = "Offset in the y-direction."), @Param(name = "z", value = "Offset in the z-direction.") })
    public void rjs$addOffset(int i, float x, float y, float z) {
        i *= 3;
        this.offsets[i] = x;
        this.offsets[i + 1] = y;
        this.offsets[i + 2] = z;
        this.offsetsLength = i + 3;
    }

    public void addInnerOffsets(int i, float x, float y, float z) {
        i *= 3;
        this.innerOffsets[i] = x;
        this.innerOffsets[i + 1] = y;
        this.innerOffsets[i + 2] = z;
        this.innerOffsetsLength = i + 3;
    }

    @Info("Get this the type of this object.\n")
    public RenderObject.ObjectType rjs$getType() {
        return this.type;
    }

    public abstract void renderInner();

    @Info("Render this object.\n")
    public void rjs$render() {
        if (!this.broken) {
            if (this.enableBlend) {
                RenderSystem.enableBlend();
            }
            if (this.enableDepthTest) {
                RenderSystem.enableDepthTest();
                RenderSystem.depthMask(true);
            }
            if (this.enableCull) {
                RenderSystem.enableCull();
            } else {
                RenderSystem.disableCull();
            }
            this.poseStack.pushPose();
            for (int i = 0; i < this.innerOffsetsLength; i += 3) {
                this.poseStack.translate(this.innerOffsets[i], this.innerOffsets[i + 1], this.innerOffsets[i + 2]);
            }
            for (int i = 0; i < this.offsetsLength; i += 3) {
                this.poseStack.translate(this.offsets[i], this.offsets[i + 1], this.offsets[i + 2]);
            }
            switch(this.billboard) {
                case HORIZONTAL:
                    this.poseStack.mulPose(new Quaternionf().rotationYXZ(0.0F, (float) (-Math.PI / 180.0) * camera.getXRot(), 0.0F));
                    break;
                case VERTICAL:
                    this.poseStack.mulPose(new Quaternionf().rotationYXZ((float) Math.PI - (float) (Math.PI / 180.0) * camera.getYRot(), (float) (Math.PI / 180.0), 0.0F));
                    break;
                case CENTER:
                    this.poseStack.mulPose(new Quaternionf().rotationYXZ((float) Math.PI - (float) (Math.PI / 180.0) * camera.getYRot(), (float) (-Math.PI / 180.0) * camera.getXRot(), 0.0F));
            }
            this.poseStack.mulPoseMatrix(this.transformation.getMatrix());
            this.poseStack.last().normal().rotate(this.transformation.getLeftRotation()).rotate(this.transformation.getRightRotation());
            this.renderInner();
            this.poseStack.popPose();
        }
    }

    public static enum ObjectType {

        LINES,
        LINE_STRIP,
        TRIANGLES,
        TRIANGLE_STRIP,
        TRIANGLE_FAN,
        QUADS,
        RECTANGLES,
        BLOCKS,
        ITEMS,
        ICONS,
        OVERLAYS,
        MODELS;

        private final VertexFormat.Mode[] modes = new VertexFormat.Mode[] { VertexFormat.Mode.LINES, VertexFormat.Mode.LINE_STRIP, VertexFormat.Mode.TRIANGLES, VertexFormat.Mode.TRIANGLE_STRIP, VertexFormat.Mode.TRIANGLE_FAN, VertexFormat.Mode.QUADS };

        public VertexFormat.Mode getMode() {
            return this.modes[this.ordinal()];
        }
    }
}