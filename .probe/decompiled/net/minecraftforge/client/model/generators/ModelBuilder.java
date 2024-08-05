package net.minecraftforge.client.model.generators;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.math.Transformation;
import com.mojang.serialization.JsonOps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockElementRotation;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.ForgeFaceData;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.util.TransformationHelper;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ModelBuilder<T extends ModelBuilder<T>> extends ModelFile {

    @Nullable
    protected ModelFile parent;

    protected final Map<String, String> textures = new LinkedHashMap();

    protected final ModelBuilder<T>.TransformsBuilder transforms = new ModelBuilder.TransformsBuilder();

    protected final ExistingFileHelper existingFileHelper;

    protected String renderType = null;

    protected boolean ambientOcclusion = true;

    protected BlockModel.GuiLight guiLight = null;

    protected final List<ModelBuilder<T>.ElementBuilder> elements = new ArrayList();

    protected CustomLoaderBuilder<T> customLoader = null;

    private final ModelBuilder<T>.RootTransformsBuilder rootTransforms = new ModelBuilder.RootTransformsBuilder();

    protected ModelBuilder(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper) {
        super(outputLocation);
        this.existingFileHelper = existingFileHelper;
    }

    private T self() {
        return (T) this;
    }

    @Override
    protected boolean exists() {
        return true;
    }

    public T parent(ModelFile parent) {
        Preconditions.checkNotNull(parent, "Parent must not be null");
        parent.assertExistence();
        this.parent = parent;
        return this.self();
    }

    public T texture(String key, String texture) {
        Preconditions.checkNotNull(key, "Key must not be null");
        Preconditions.checkNotNull(texture, "Texture must not be null");
        if (texture.charAt(0) == '#') {
            this.textures.put(key, texture);
            return this.self();
        } else {
            ResourceLocation asLoc;
            if (texture.contains(":")) {
                asLoc = new ResourceLocation(texture);
            } else {
                asLoc = new ResourceLocation(this.getLocation().getNamespace(), texture);
            }
            return this.texture(key, asLoc);
        }
    }

    public T texture(String key, ResourceLocation texture) {
        Preconditions.checkNotNull(key, "Key must not be null");
        Preconditions.checkNotNull(texture, "Texture must not be null");
        Preconditions.checkArgument(this.existingFileHelper.exists(texture, ModelProvider.TEXTURE), "Texture %s does not exist in any known resource pack", texture);
        this.textures.put(key, texture.toString());
        return this.self();
    }

    public T renderType(String renderType) {
        Preconditions.checkNotNull(renderType, "Render type must not be null");
        return this.renderType(new ResourceLocation(renderType));
    }

    public T renderType(ResourceLocation renderType) {
        Preconditions.checkNotNull(renderType, "Render type must not be null");
        this.renderType = renderType.toString();
        return this.self();
    }

    public ModelBuilder<T>.TransformsBuilder transforms() {
        return this.transforms;
    }

    public T ao(boolean ao) {
        this.ambientOcclusion = ao;
        return this.self();
    }

    public T guiLight(BlockModel.GuiLight light) {
        this.guiLight = light;
        return this.self();
    }

    public ModelBuilder<T>.ElementBuilder element() {
        Preconditions.checkState(this.customLoader == null, "Cannot use elements and custom loaders at the same time");
        ModelBuilder<T>.ElementBuilder ret = new ModelBuilder.ElementBuilder();
        this.elements.add(ret);
        return ret;
    }

    public ModelBuilder<T>.ElementBuilder element(int index) {
        Preconditions.checkState(this.customLoader == null, "Cannot use elements and custom loaders at the same time");
        Preconditions.checkElementIndex(index, this.elements.size(), "Element index");
        return (ModelBuilder.ElementBuilder) this.elements.get(index);
    }

    public int getElementCount() {
        return this.elements.size();
    }

    public <L extends CustomLoaderBuilder<T>> L customLoader(BiFunction<T, ExistingFileHelper, L> customLoaderFactory) {
        Preconditions.checkState(this.elements.size() == 0, "Cannot use elements and custom loaders at the same time");
        Preconditions.checkNotNull(customLoaderFactory, "customLoaderFactory must not be null");
        L customLoader = (L) customLoaderFactory.apply(this.self(), this.existingFileHelper);
        this.customLoader = customLoader;
        return customLoader;
    }

    public ModelBuilder<T>.RootTransformsBuilder rootTransforms() {
        return this.rootTransforms;
    }

    @VisibleForTesting
    public JsonObject toJson() {
        JsonObject root = new JsonObject();
        if (this.parent != null) {
            root.addProperty("parent", this.parent.getLocation().toString());
        }
        if (!this.ambientOcclusion) {
            root.addProperty("ambientocclusion", this.ambientOcclusion);
        }
        if (this.guiLight != null) {
            root.addProperty("gui_light", this.guiLight.getSerializedName());
        }
        if (this.renderType != null) {
            root.addProperty("render_type", this.renderType);
        }
        Map<ItemDisplayContext, ItemTransform> transforms = this.transforms.build();
        if (!transforms.isEmpty()) {
            JsonObject display = new JsonObject();
            for (Entry<ItemDisplayContext, ItemTransform> e : transforms.entrySet()) {
                JsonObject transform = new JsonObject();
                ItemTransform vec = (ItemTransform) e.getValue();
                if (!vec.equals(ItemTransform.NO_TRANSFORM)) {
                    boolean hasRightRotation = !vec.rightRotation.equals(ItemTransform.Deserializer.DEFAULT_ROTATION);
                    if (!vec.translation.equals(ItemTransform.Deserializer.DEFAULT_TRANSLATION)) {
                        transform.add("translation", this.serializeVector3f(((ItemTransform) e.getValue()).translation));
                    }
                    if (!vec.rotation.equals(ItemTransform.Deserializer.DEFAULT_ROTATION)) {
                        transform.add(hasRightRotation ? "left_rotation" : "rotation", this.serializeVector3f(vec.rotation));
                    }
                    if (!vec.scale.equals(ItemTransform.Deserializer.DEFAULT_SCALE)) {
                        transform.add("scale", this.serializeVector3f(((ItemTransform) e.getValue()).scale));
                    }
                    if (hasRightRotation) {
                        transform.add("right_rotation", this.serializeVector3f(vec.rightRotation));
                    }
                    display.add(((ItemDisplayContext) e.getKey()).getSerializedName(), transform);
                }
            }
            root.add("display", display);
        }
        if (!this.textures.isEmpty()) {
            JsonObject textures = new JsonObject();
            for (Entry<String, String> ex : this.textures.entrySet()) {
                textures.addProperty((String) ex.getKey(), this.serializeLocOrKey((String) ex.getValue()));
            }
            root.add("textures", textures);
        }
        if (!this.elements.isEmpty()) {
            JsonArray elements = new JsonArray();
            this.elements.stream().map(ModelBuilder.ElementBuilder::build).forEach(part -> {
                JsonObject partObj = new JsonObject();
                partObj.add("from", this.serializeVector3f(part.from));
                partObj.add("to", this.serializeVector3f(part.to));
                if (part.rotation != null) {
                    JsonObject rotation = new JsonObject();
                    rotation.add("origin", this.serializeVector3f(part.rotation.origin()));
                    rotation.addProperty("axis", part.rotation.axis().getSerializedName());
                    rotation.addProperty("angle", part.rotation.angle());
                    if (part.rotation.rescale()) {
                        rotation.addProperty("rescale", part.rotation.rescale());
                    }
                    partObj.add("rotation", rotation);
                }
                if (!part.shade) {
                    partObj.addProperty("shade", part.shade);
                }
                JsonObject faces = new JsonObject();
                for (Direction dir : Direction.values()) {
                    BlockElementFace face = (BlockElementFace) part.faces.get(dir);
                    if (face != null) {
                        JsonObject faceObj = new JsonObject();
                        faceObj.addProperty("texture", this.serializeLocOrKey(face.texture));
                        if (!Arrays.equals(face.uv.uvs, part.uvsByFace(dir))) {
                            faceObj.add("uv", new Gson().toJsonTree(face.uv.uvs));
                        }
                        if (face.cullForDirection != null) {
                            faceObj.addProperty("cullface", face.cullForDirection.getSerializedName());
                        }
                        if (face.uv.rotation != 0) {
                            faceObj.addProperty("rotation", face.uv.rotation);
                        }
                        if (face.tintIndex != -1) {
                            faceObj.addProperty("tintindex", face.tintIndex);
                        }
                        if (!face.getFaceData().equals(ForgeFaceData.DEFAULT)) {
                            faceObj.add("forge_data", (JsonElement) ForgeFaceData.CODEC.encodeStart(JsonOps.INSTANCE, face.getFaceData()).result().get());
                        }
                        faces.add(dir.getSerializedName(), faceObj);
                    }
                }
                if (!part.faces.isEmpty()) {
                    partObj.add("faces", faces);
                }
                elements.add(partObj);
            });
            root.add("elements", elements);
        }
        JsonObject transform = this.rootTransforms.toJson();
        if (transform.size() > 0) {
            root.add("transform", transform);
        }
        return this.customLoader != null ? this.customLoader.toJson(root) : root;
    }

    private String serializeLocOrKey(String tex) {
        return tex.charAt(0) == '#' ? tex : new ResourceLocation(tex).toString();
    }

    private JsonArray serializeVector3f(Vector3f vec) {
        JsonArray ret = new JsonArray();
        ret.add(this.serializeFloat(vec.x()));
        ret.add(this.serializeFloat(vec.y()));
        ret.add(this.serializeFloat(vec.z()));
        return ret;
    }

    private Number serializeFloat(float f) {
        return (Number) ((float) ((int) f) == f ? (int) f : f);
    }

    public class ElementBuilder {

        private Vector3f from = new Vector3f();

        private Vector3f to = new Vector3f(16.0F, 16.0F, 16.0F);

        private final Map<Direction, ModelBuilder<T>.ElementBuilder.FaceBuilder> faces = new LinkedHashMap();

        private ModelBuilder<T>.ElementBuilder.RotationBuilder rotation;

        private boolean shade = true;

        private int color = -1;

        private int blockLight = 0;

        private int skyLight = 0;

        private boolean hasAmbientOcclusion = true;

        private boolean calculateNormals = false;

        private void validateCoordinate(float coord, char name) {
            Preconditions.checkArgument(!(coord < -16.0F) && !(coord > 32.0F), "Position " + name + " out of range, must be within [-16, 32]. Found: %d", coord);
        }

        private void validatePosition(Vector3f pos) {
            this.validateCoordinate(pos.x(), 'x');
            this.validateCoordinate(pos.y(), 'y');
            this.validateCoordinate(pos.z(), 'z');
        }

        public ModelBuilder<T>.ElementBuilder from(float x, float y, float z) {
            this.from = new Vector3f(x, y, z);
            this.validatePosition(this.from);
            return this;
        }

        public ModelBuilder<T>.ElementBuilder to(float x, float y, float z) {
            this.to = new Vector3f(x, y, z);
            this.validatePosition(this.to);
            return this;
        }

        public ModelBuilder<T>.ElementBuilder.FaceBuilder face(Direction dir) {
            Preconditions.checkNotNull(dir, "Direction must not be null");
            return (ModelBuilder.ElementBuilder.FaceBuilder) this.faces.computeIfAbsent(dir, x$0 -> new ModelBuilder.ElementBuilder.FaceBuilder(x$0));
        }

        public ModelBuilder<T>.ElementBuilder.RotationBuilder rotation() {
            if (this.rotation == null) {
                this.rotation = new ModelBuilder.ElementBuilder.RotationBuilder();
            }
            return this.rotation;
        }

        public ModelBuilder<T>.ElementBuilder shade(boolean shade) {
            this.shade = shade;
            return this;
        }

        public ModelBuilder<T>.ElementBuilder allFaces(BiConsumer<Direction, ModelBuilder<T>.ElementBuilder.FaceBuilder> action) {
            Arrays.stream(Direction.values()).forEach(d -> action.accept(d, this.face(d)));
            return this;
        }

        public ModelBuilder<T>.ElementBuilder faces(BiConsumer<Direction, ModelBuilder<T>.ElementBuilder.FaceBuilder> action) {
            this.faces.entrySet().stream().forEach(e -> action.accept((Direction) e.getKey(), (ModelBuilder.ElementBuilder.FaceBuilder) e.getValue()));
            return this;
        }

        public ModelBuilder<T>.ElementBuilder textureAll(String texture) {
            return this.allFaces(this.addTexture(texture));
        }

        public ModelBuilder<T>.ElementBuilder texture(String texture) {
            return this.faces(this.addTexture(texture));
        }

        public ModelBuilder<T>.ElementBuilder cube(String texture) {
            return this.allFaces(this.addTexture(texture).andThen((dir, f) -> f.cullface(dir)));
        }

        public ModelBuilder<T>.ElementBuilder emissivity(int blockLight, int skyLight) {
            this.blockLight = blockLight;
            this.skyLight = skyLight;
            return this;
        }

        public ModelBuilder<T>.ElementBuilder color(int color) {
            this.color = color;
            return this;
        }

        public ModelBuilder<T>.ElementBuilder ao(boolean ao) {
            this.hasAmbientOcclusion = ao;
            return this;
        }

        public ModelBuilder<T>.ElementBuilder calculateNormals(boolean calc) {
            this.calculateNormals = calc;
            return this;
        }

        private BiConsumer<Direction, ModelBuilder<T>.ElementBuilder.FaceBuilder> addTexture(String texture) {
            return ($, f) -> f.texture(texture);
        }

        BlockElement build() {
            Map<Direction, BlockElementFace> faces = (Map<Direction, BlockElementFace>) this.faces.entrySet().stream().collect(Collectors.toMap(Entry::getKey, e -> ((ModelBuilder.ElementBuilder.FaceBuilder) e.getValue()).build(), (k1, k2) -> {
                throw new IllegalArgumentException();
            }, LinkedHashMap::new));
            return new BlockElement(this.from, this.to, faces, this.rotation == null ? null : this.rotation.build(), this.shade, new ForgeFaceData(this.color, this.blockLight, this.skyLight, this.hasAmbientOcclusion, this.calculateNormals));
        }

        public T end() {
            return ModelBuilder.this.self();
        }

        public class FaceBuilder {

            private Direction cullface;

            private int tintindex = -1;

            private String texture = MissingTextureAtlasSprite.getLocation().toString();

            private float[] uvs;

            private ModelBuilder.FaceRotation rotation = ModelBuilder.FaceRotation.ZERO;

            private int color = -1;

            private int blockLight = 0;

            private int skyLight = 0;

            private boolean hasAmbientOcclusion = true;

            private boolean calculateNormals = false;

            FaceBuilder(Direction dir) {
            }

            public ModelBuilder<T>.ElementBuilder.FaceBuilder cullface(@Nullable Direction dir) {
                this.cullface = dir;
                return this;
            }

            public ModelBuilder<T>.ElementBuilder.FaceBuilder tintindex(int index) {
                this.tintindex = index;
                return this;
            }

            public ModelBuilder<T>.ElementBuilder.FaceBuilder texture(String texture) {
                Preconditions.checkNotNull(texture, "Texture must not be null");
                this.texture = texture;
                return this;
            }

            public ModelBuilder<T>.ElementBuilder.FaceBuilder uvs(float u1, float v1, float u2, float v2) {
                this.uvs = new float[] { u1, v1, u2, v2 };
                return this;
            }

            public ModelBuilder<T>.ElementBuilder.FaceBuilder rotation(ModelBuilder.FaceRotation rot) {
                Preconditions.checkNotNull(rot, "Rotation must not be null");
                this.rotation = rot;
                return this;
            }

            public ModelBuilder<T>.ElementBuilder.FaceBuilder emissivity(int blockLight, int skyLight) {
                this.blockLight = blockLight;
                this.skyLight = skyLight;
                return this;
            }

            public ModelBuilder<T>.ElementBuilder.FaceBuilder color(int color) {
                this.color = color;
                return this;
            }

            public ModelBuilder<T>.ElementBuilder.FaceBuilder ao(boolean ao) {
                this.hasAmbientOcclusion = ao;
                return this;
            }

            public ModelBuilder<T>.ElementBuilder.FaceBuilder calculateNormals(boolean calc) {
                this.calculateNormals = calc;
                return this;
            }

            BlockElementFace build() {
                if (this.texture == null) {
                    throw new IllegalStateException("A model face must have a texture");
                } else {
                    return new BlockElementFace(this.cullface, this.tintindex, this.texture, new BlockFaceUV(this.uvs, this.rotation.rotation), new ForgeFaceData(this.color, this.blockLight, this.skyLight, this.hasAmbientOcclusion, this.calculateNormals));
                }
            }

            public ModelBuilder<T>.ElementBuilder end() {
                return ElementBuilder.this;
            }
        }

        public class RotationBuilder {

            private Vector3f origin;

            private Direction.Axis axis;

            private float angle;

            private boolean rescale;

            public ModelBuilder<T>.ElementBuilder.RotationBuilder origin(float x, float y, float z) {
                this.origin = new Vector3f(x, y, z);
                return this;
            }

            public ModelBuilder<T>.ElementBuilder.RotationBuilder axis(Direction.Axis axis) {
                Preconditions.checkNotNull(axis, "Axis must not be null");
                this.axis = axis;
                return this;
            }

            public ModelBuilder<T>.ElementBuilder.RotationBuilder angle(float angle) {
                Preconditions.checkArgument(angle == 0.0F || Mth.abs(angle) == 22.5F || Mth.abs(angle) == 45.0F, "Invalid rotation %f found, only -45/-22.5/0/22.5/45 allowed", angle);
                this.angle = angle;
                return this;
            }

            public ModelBuilder<T>.ElementBuilder.RotationBuilder rescale(boolean rescale) {
                this.rescale = rescale;
                return this;
            }

            BlockElementRotation build() {
                return new BlockElementRotation(this.origin, this.axis, this.angle, this.rescale);
            }

            public ModelBuilder<T>.ElementBuilder end() {
                return ElementBuilder.this;
            }
        }
    }

    public static enum FaceRotation {

        ZERO(0), CLOCKWISE_90(90), UPSIDE_DOWN(180), COUNTERCLOCKWISE_90(270);

        final int rotation;

        private FaceRotation(int rotation) {
            this.rotation = rotation;
        }
    }

    public class RootTransformsBuilder {

        private static final Vector3f ONE = new Vector3f(1.0F, 1.0F, 1.0F);

        private Vector3f translation = new Vector3f();

        private Quaternionf leftRotation = new Quaternionf();

        private Quaternionf rightRotation = new Quaternionf();

        private Vector3f scale = ONE;

        @Nullable
        private TransformationHelper.TransformOrigin origin;

        @Nullable
        private Vector3f originVec;

        RootTransformsBuilder() {
        }

        public ModelBuilder<T>.RootTransformsBuilder translation(Vector3f translation) {
            this.translation = (Vector3f) Preconditions.checkNotNull(translation, "Translation must not be null");
            return this;
        }

        public ModelBuilder<T>.RootTransformsBuilder translation(float x, float y, float z) {
            return this.translation(new Vector3f(x, y, z));
        }

        public ModelBuilder<T>.RootTransformsBuilder rotation(Quaternionf rotation) {
            this.leftRotation = (Quaternionf) Preconditions.checkNotNull(rotation, "Rotation must not be null");
            return this;
        }

        public ModelBuilder<T>.RootTransformsBuilder rotation(float x, float y, float z, boolean isDegrees) {
            return this.rotation(TransformationHelper.quatFromXYZ(x, y, z, isDegrees));
        }

        public ModelBuilder<T>.RootTransformsBuilder leftRotation(Quaternionf leftRotation) {
            return this.rotation(leftRotation);
        }

        public ModelBuilder<T>.RootTransformsBuilder leftRotation(float x, float y, float z, boolean isDegrees) {
            return this.leftRotation(TransformationHelper.quatFromXYZ(x, y, z, isDegrees));
        }

        public ModelBuilder<T>.RootTransformsBuilder rightRotation(Quaternionf rightRotation) {
            this.rightRotation = (Quaternionf) Preconditions.checkNotNull(rightRotation, "Rotation must not be null");
            return this;
        }

        public ModelBuilder<T>.RootTransformsBuilder rightRotation(float x, float y, float z, boolean isDegrees) {
            return this.rightRotation(TransformationHelper.quatFromXYZ(x, y, z, isDegrees));
        }

        public ModelBuilder<T>.RootTransformsBuilder postRotation(Quaternionf postRotation) {
            return this.rightRotation(postRotation);
        }

        public ModelBuilder<T>.RootTransformsBuilder postRotation(float x, float y, float z, boolean isDegrees) {
            return this.postRotation(TransformationHelper.quatFromXYZ(x, y, z, isDegrees));
        }

        public ModelBuilder<T>.RootTransformsBuilder scale(float scale) {
            return this.scale(new Vector3f(scale, scale, scale));
        }

        public ModelBuilder<T>.RootTransformsBuilder scale(float xScale, float yScale, float zScale) {
            return this.scale(new Vector3f(xScale, yScale, zScale));
        }

        public ModelBuilder<T>.RootTransformsBuilder scale(Vector3f scale) {
            this.scale = (Vector3f) Preconditions.checkNotNull(scale, "Scale must not be null");
            return this;
        }

        public ModelBuilder<T>.RootTransformsBuilder transform(Transformation transformation) {
            Preconditions.checkNotNull(transformation, "Transformation must not be null");
            this.translation = transformation.getTranslation();
            this.leftRotation = transformation.getLeftRotation();
            this.rightRotation = transformation.getRightRotation();
            this.scale = transformation.getScale();
            return this;
        }

        public ModelBuilder<T>.RootTransformsBuilder origin(Vector3f origin) {
            this.originVec = (Vector3f) Preconditions.checkNotNull(origin, "Origin must not be null");
            this.origin = null;
            return this;
        }

        public ModelBuilder<T>.RootTransformsBuilder origin(TransformationHelper.TransformOrigin origin) {
            this.origin = (TransformationHelper.TransformOrigin) Preconditions.checkNotNull(origin, "Origin must not be null");
            this.originVec = null;
            return this;
        }

        public ModelBuilder<T> end() {
            return ModelBuilder.this;
        }

        public JsonObject toJson() {
            JsonObject transform = new JsonObject();
            if (!this.translation.equals(0.0F, 0.0F, 0.0F)) {
                transform.add("translation", writeVec3(this.translation));
            }
            if (!this.scale.equals(ONE)) {
                transform.add("scale", writeVec3(this.scale));
            }
            if (!this.leftRotation.equals(0.0F, 0.0F, 0.0F, 1.0F)) {
                transform.add("rotation", writeQuaternion(this.leftRotation));
            }
            if (!this.rightRotation.equals(0.0F, 0.0F, 0.0F, 1.0F)) {
                transform.add("post_rotation", writeQuaternion(this.rightRotation));
            }
            if (this.origin != null) {
                transform.addProperty("origin", this.origin.getSerializedName());
            } else if (this.originVec != null && !this.originVec.equals(0.0F, 0.0F, 0.0F)) {
                transform.add("origin", writeVec3(this.originVec));
            }
            return transform;
        }

        private static JsonArray writeVec3(Vector3f vector) {
            JsonArray array = new JsonArray();
            array.add(vector.x());
            array.add(vector.y());
            array.add(vector.z());
            return array;
        }

        private static JsonArray writeQuaternion(Quaternionf quaternion) {
            JsonArray array = new JsonArray();
            array.add(quaternion.x());
            array.add(quaternion.y());
            array.add(quaternion.z());
            array.add(quaternion.w());
            return array;
        }
    }

    public class TransformsBuilder {

        private final Map<ItemDisplayContext, ModelBuilder<T>.TransformsBuilder.TransformVecBuilder> transforms = new LinkedHashMap();

        public ModelBuilder<T>.TransformsBuilder.TransformVecBuilder transform(ItemDisplayContext type) {
            Preconditions.checkNotNull(type, "Perspective cannot be null");
            return (ModelBuilder.TransformsBuilder.TransformVecBuilder) this.transforms.computeIfAbsent(type, x$0 -> new ModelBuilder.TransformsBuilder.TransformVecBuilder(x$0));
        }

        Map<ItemDisplayContext, ItemTransform> build() {
            return (Map<ItemDisplayContext, ItemTransform>) this.transforms.entrySet().stream().collect(Collectors.toMap(Entry::getKey, e -> ((ModelBuilder.TransformsBuilder.TransformVecBuilder) e.getValue()).build(), (k1, k2) -> {
                throw new IllegalArgumentException();
            }, LinkedHashMap::new));
        }

        public T end() {
            return ModelBuilder.this.self();
        }

        public class TransformVecBuilder {

            private Vector3f rotation = new Vector3f(ItemTransform.Deserializer.DEFAULT_ROTATION);

            private Vector3f translation = new Vector3f(ItemTransform.Deserializer.DEFAULT_TRANSLATION);

            private Vector3f scale = new Vector3f(ItemTransform.Deserializer.DEFAULT_SCALE);

            private Vector3f rightRotation = new Vector3f(ItemTransform.Deserializer.DEFAULT_ROTATION);

            TransformVecBuilder(ItemDisplayContext type) {
            }

            public ModelBuilder<T>.TransformsBuilder.TransformVecBuilder rotation(float x, float y, float z) {
                this.rotation = new Vector3f(x, y, z);
                return this;
            }

            public ModelBuilder<T>.TransformsBuilder.TransformVecBuilder leftRotation(float x, float y, float z) {
                return this.rotation(x, y, z);
            }

            public ModelBuilder<T>.TransformsBuilder.TransformVecBuilder translation(float x, float y, float z) {
                this.translation = new Vector3f(x, y, z);
                return this;
            }

            public ModelBuilder<T>.TransformsBuilder.TransformVecBuilder scale(float sc) {
                return this.scale(sc, sc, sc);
            }

            public ModelBuilder<T>.TransformsBuilder.TransformVecBuilder scale(float x, float y, float z) {
                this.scale = new Vector3f(x, y, z);
                return this;
            }

            public ModelBuilder<T>.TransformsBuilder.TransformVecBuilder rightRotation(float x, float y, float z) {
                this.rightRotation = new Vector3f(x, y, z);
                return this;
            }

            ItemTransform build() {
                return new ItemTransform(this.rotation, this.translation, this.scale, this.rightRotation);
            }

            public ModelBuilder<T>.TransformsBuilder end() {
                return TransformsBuilder.this;
            }
        }
    }
}