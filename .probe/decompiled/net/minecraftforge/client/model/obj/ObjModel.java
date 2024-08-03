package net.minecraftforge.client.model.obj;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Transformation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import joptsimple.internal.Strings;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.model.IModelBuilder;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.SimpleUnbakedGeometry;
import net.minecraftforge.client.model.geometry.UnbakedGeometryHelper;
import net.minecraftforge.client.model.pipeline.QuadBakingVertexConsumer;
import net.minecraftforge.client.model.renderable.CompositeRenderable;
import net.minecraftforge.client.textures.UnitTextureAtlasSprite;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;

public class ObjModel extends SimpleUnbakedGeometry<ObjModel> {

    private static final Vector4f COLOR_WHITE = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);

    private static final Vec2[] DEFAULT_COORDS = new Vec2[] { new Vec2(0.0F, 0.0F), new Vec2(0.0F, 1.0F), new Vec2(1.0F, 1.0F), new Vec2(1.0F, 0.0F) };

    private final Map<String, ObjModel.ModelGroup> parts = Maps.newLinkedHashMap();

    private final Set<String> rootComponentNames = Collections.unmodifiableSet(this.parts.keySet());

    private Set<String> allComponentNames;

    private final List<Vector3f> positions = Lists.newArrayList();

    private final List<Vec2> texCoords = Lists.newArrayList();

    private final List<Vector3f> normals = Lists.newArrayList();

    private final List<Vector4f> colors = Lists.newArrayList();

    public final boolean automaticCulling;

    public final boolean shadeQuads;

    public final boolean flipV;

    public final boolean emissiveAmbient;

    @Nullable
    public final String mtlOverride;

    public final ResourceLocation modelLocation;

    private ObjModel(ObjModel.ModelSettings settings) {
        this.modelLocation = settings.modelLocation;
        this.automaticCulling = settings.automaticCulling;
        this.shadeQuads = settings.shadeQuads;
        this.flipV = settings.flipV;
        this.emissiveAmbient = settings.emissiveAmbient;
        this.mtlOverride = settings.mtlOverride;
    }

    public static ObjModel parse(ObjTokenizer tokenizer, ObjModel.ModelSettings settings) throws IOException {
        ResourceLocation modelLocation = settings.modelLocation;
        String materialLibraryOverrideLocation = settings.mtlOverride;
        ObjModel model = new ObjModel(settings);
        String modelDomain = modelLocation.getNamespace();
        String modelPath = modelLocation.getPath();
        int lastSlash = modelPath.lastIndexOf(47);
        if (lastSlash >= 0) {
            modelPath = modelPath.substring(0, lastSlash + 1);
        } else {
            modelPath = "";
        }
        ObjMaterialLibrary mtllib = ObjMaterialLibrary.EMPTY;
        ObjMaterialLibrary.Material currentMat = null;
        String currentSmoothingGroup = null;
        ObjModel.ModelGroup currentGroup = null;
        ObjModel.ModelObject currentObject = null;
        ObjModel.ModelMesh currentMesh = null;
        boolean objAboveGroup = false;
        if (materialLibraryOverrideLocation != null) {
            if (materialLibraryOverrideLocation.contains(":")) {
                mtllib = ObjLoader.INSTANCE.loadMaterialLibrary(new ResourceLocation(materialLibraryOverrideLocation));
            } else {
                mtllib = ObjLoader.INSTANCE.loadMaterialLibrary(new ResourceLocation(modelDomain, modelPath + materialLibraryOverrideLocation));
            }
        }
        String[] line;
        while ((line = tokenizer.readAndSplitLine(true)) != null) {
            String var16 = line[0];
            switch(var16) {
                case "mtllib":
                    if (materialLibraryOverrideLocation == null) {
                        String lib = line[1];
                        if (lib.contains(":")) {
                            mtllib = ObjLoader.INSTANCE.loadMaterialLibrary(new ResourceLocation(lib));
                        } else {
                            mtllib = ObjLoader.INSTANCE.loadMaterialLibrary(new ResourceLocation(modelDomain, modelPath + lib));
                        }
                    }
                    break;
                case "usemtl":
                    String mat = Strings.join((String[]) Arrays.copyOfRange(line, 1, line.length), " ");
                    ObjMaterialLibrary.Material newMat = mtllib.getMaterial(mat);
                    if (Objects.equals(newMat, currentMat)) {
                        break;
                    }
                    currentMat = newMat;
                    if (currentMesh != null && currentMesh.mat == null && currentMesh.faces.size() == 0) {
                        currentMesh.mat = newMat;
                        break;
                    }
                    currentMesh = null;
                    break;
                case "v":
                    model.positions.add(parseVector4To3(line));
                    break;
                case "vt":
                    model.texCoords.add(parseVector2(line));
                    break;
                case "vn":
                    model.normals.add(parseVector3(line));
                    break;
                case "vc":
                    model.colors.add(parseVector4(line));
                    break;
                case "f":
                    if (currentMesh == null) {
                        currentMesh = model.new ModelMesh(currentMat, currentSmoothingGroup);
                        if (currentObject != null) {
                            currentObject.meshes.add(currentMesh);
                        } else {
                            if (currentGroup == null) {
                                currentGroup = model.new ModelGroup("");
                                model.parts.put("", currentGroup);
                            }
                            currentGroup.meshes.add(currentMesh);
                        }
                    }
                    int[][] vertices = new int[line.length - 1][];
                    for (int i = 0; i < vertices.length; i++) {
                        String vertexData = line[i + 1];
                        String[] vertexParts = vertexData.split("/");
                        int[] vertex = Arrays.stream(vertexParts).mapToInt(num -> Strings.isNullOrEmpty(num) ? 0 : Integer.parseInt(num)).toArray();
                        if (vertex[0] < 0) {
                            vertex[0] += model.positions.size();
                        } else {
                            vertex[0]--;
                        }
                        if (vertex.length > 1) {
                            if (vertex[1] < 0) {
                                vertex[1] += model.texCoords.size();
                            } else {
                                vertex[1]--;
                            }
                            if (vertex.length > 2) {
                                if (vertex[2] < 0) {
                                    vertex[2] += model.normals.size();
                                } else {
                                    vertex[2]--;
                                }
                                if (vertex.length > 3) {
                                    if (vertex[3] < 0) {
                                        vertex[3] += model.colors.size();
                                    } else {
                                        vertex[3]--;
                                    }
                                }
                            }
                        }
                        vertices[i] = vertex;
                    }
                    currentMesh.faces.add(vertices);
                    break;
                case "s":
                    String smoothingGroup = "off".equals(line[1]) ? null : line[1];
                    if (Objects.equals(currentSmoothingGroup, smoothingGroup)) {
                        break;
                    }
                    currentSmoothingGroup = smoothingGroup;
                    if (currentMesh != null && currentMesh.smoothingGroup == null && currentMesh.faces.size() == 0) {
                        currentMesh.smoothingGroup = smoothingGroup;
                        break;
                    }
                    currentMesh = null;
                    break;
                case "g":
                    String name = line[1];
                    if (objAboveGroup) {
                        currentObject = model.new ModelObject(currentGroup.name() + "/" + name);
                        currentGroup.parts.put(name, currentObject);
                    } else {
                        currentGroup = model.new ModelGroup(name);
                        model.parts.put(name, currentGroup);
                        currentObject = null;
                    }
                    currentMesh = null;
                    break;
                case "o":
                    String name = line[1];
                    if (!objAboveGroup && currentGroup != null) {
                        currentObject = model.new ModelObject(currentGroup.name() + "/" + name);
                        currentGroup.parts.put(name, currentObject);
                    } else {
                        objAboveGroup = true;
                        currentGroup = model.new ModelGroup(name);
                        model.parts.put(name, currentGroup);
                        currentObject = null;
                    }
                    currentMesh = null;
            }
        }
        return model;
    }

    private static Vector3f parseVector4To3(String[] line) {
        Vector4f vec4 = parseVector4(line);
        return new Vector3f(vec4.x() / vec4.w(), vec4.y() / vec4.w(), vec4.z() / vec4.w());
    }

    private static Vec2 parseVector2(String[] line) {
        return switch(line.length) {
            case 1 ->
                new Vec2(0.0F, 0.0F);
            case 2 ->
                new Vec2(Float.parseFloat(line[1]), 0.0F);
            default ->
                new Vec2(Float.parseFloat(line[1]), Float.parseFloat(line[2]));
        };
    }

    private static Vector3f parseVector3(String[] line) {
        return switch(line.length) {
            case 1 ->
                new Vector3f();
            case 2 ->
                new Vector3f(Float.parseFloat(line[1]), 0.0F, 0.0F);
            case 3 ->
                new Vector3f(Float.parseFloat(line[1]), Float.parseFloat(line[2]), 0.0F);
            default ->
                new Vector3f(Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3]));
        };
    }

    static Vector4f parseVector4(String[] line) {
        return switch(line.length) {
            case 1 ->
                new Vector4f();
            case 2 ->
                new Vector4f(Float.parseFloat(line[1]), 0.0F, 0.0F, 1.0F);
            case 3 ->
                new Vector4f(Float.parseFloat(line[1]), Float.parseFloat(line[2]), 0.0F, 1.0F);
            case 4 ->
                new Vector4f(Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3]), 1.0F);
            default ->
                new Vector4f(Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3]), Float.parseFloat(line[4]));
        };
    }

    @Override
    protected void addQuads(IGeometryBakingContext owner, IModelBuilder<?> modelBuilder, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ResourceLocation modelLocation) {
        this.parts.values().stream().filter(part -> owner.isComponentVisible(part.name(), true)).forEach(part -> part.addQuads(owner, modelBuilder, baker, spriteGetter, modelTransform, modelLocation));
    }

    public Set<String> getRootComponentNames() {
        return this.rootComponentNames;
    }

    @Override
    public Set<String> getConfigurableComponentNames() {
        if (this.allComponentNames != null) {
            return this.allComponentNames;
        } else {
            HashSet<String> names = new HashSet();
            for (ObjModel.ModelGroup group : this.parts.values()) {
                group.addNamesRecursively(names);
            }
            return this.allComponentNames = Collections.unmodifiableSet(names);
        }
    }

    private org.apache.commons.lang3.tuple.Pair<BakedQuad, Direction> makeQuad(int[][] indices, int tintIndex, Vector4f colorTint, Vector4f ambientColor, TextureAtlasSprite texture, Transformation transform) {
        boolean needsNormalRecalculation = false;
        for (int[] ints : indices) {
            needsNormalRecalculation |= ints.length < 3;
        }
        Vector3f faceNormal = new Vector3f();
        if (needsNormalRecalculation) {
            Vector3f a = (Vector3f) this.positions.get(indices[0][0]);
            Vector3f ab = (Vector3f) this.positions.get(indices[1][0]);
            Vector3f ac = (Vector3f) this.positions.get(indices[2][0]);
            Vector3f abs = new Vector3f(ab);
            abs.sub(a);
            Vector3f acs = new Vector3f(ac);
            acs.sub(a);
            abs.cross(acs);
            abs.normalize();
            faceNormal = abs;
        }
        QuadBakingVertexConsumer.Buffered quadBaker = new QuadBakingVertexConsumer.Buffered();
        quadBaker.setSprite(texture);
        quadBaker.setTintIndex(tintIndex);
        int uv2 = 0;
        if (this.emissiveAmbient) {
            int fakeLight = (int) ((ambientColor.x() + ambientColor.y() + ambientColor.z()) * 15.0F / 3.0F);
            uv2 = LightTexture.pack(fakeLight, fakeLight);
            quadBaker.setShade(fakeLight == 0 && this.shadeQuads);
        } else {
            quadBaker.setShade(this.shadeQuads);
        }
        boolean hasTransform = !transform.isIdentity();
        Transformation transformation = hasTransform ? transform.blockCenterToCorner() : transform;
        Vector4f[] pos = new Vector4f[4];
        Vector3f[] norm = new Vector3f[4];
        for (int i = 0; i < 4; i++) {
            int[] index = indices[Math.min(i, indices.length - 1)];
            Vector4f position = new Vector4f((Vector3fc) this.positions.get(index[0]), 1.0F);
            Vec2 texCoord = index.length >= 2 && this.texCoords.size() > 0 ? (Vec2) this.texCoords.get(index[1]) : DEFAULT_COORDS[i];
            Vector3f norm0 = !needsNormalRecalculation && index.length >= 3 && this.normals.size() > 0 ? (Vector3f) this.normals.get(index[2]) : faceNormal;
            Vector3f normal = norm0;
            Vector4f color = index.length >= 4 && this.colors.size() > 0 ? (Vector4f) this.colors.get(index[3]) : COLOR_WHITE;
            if (hasTransform) {
                normal = new Vector3f(norm0);
                transformation.transformPosition(position);
                transformation.transformNormal(normal);
            }
            Vector4f tintedColor = new Vector4f(color.x() * colorTint.x(), color.y() * colorTint.y(), color.z() * colorTint.z(), color.w() * colorTint.w());
            quadBaker.m_5483_((double) position.x(), (double) position.y(), (double) position.z());
            quadBaker.m_85950_(tintedColor.x(), tintedColor.y(), tintedColor.z(), tintedColor.w());
            quadBaker.m_7421_(texture.getU((double) (texCoord.x * 16.0F)), texture.getV((double) ((this.flipV ? 1.0F - texCoord.y : texCoord.y) * 16.0F)));
            quadBaker.m_85969_(uv2);
            quadBaker.m_5601_(normal.x(), normal.y(), normal.z());
            if (i == 0) {
                quadBaker.setDirection(Direction.getNearest(normal.x(), normal.y(), normal.z()));
            }
            quadBaker.m_5752_();
            pos[i] = position;
            norm[i] = normal;
        }
        Direction cull = null;
        if (this.automaticCulling) {
            if (Mth.equal(pos[0].x(), 0.0F) && Mth.equal(pos[1].x(), 0.0F) && Mth.equal(pos[2].x(), 0.0F) && Mth.equal(pos[3].x(), 0.0F) && norm[0].x() < 0.0F) {
                cull = Direction.WEST;
            } else if (Mth.equal(pos[0].x(), 1.0F) && Mth.equal(pos[1].x(), 1.0F) && Mth.equal(pos[2].x(), 1.0F) && Mth.equal(pos[3].x(), 1.0F) && norm[0].x() > 0.0F) {
                cull = Direction.EAST;
            } else if (Mth.equal(pos[0].z(), 0.0F) && Mth.equal(pos[1].z(), 0.0F) && Mth.equal(pos[2].z(), 0.0F) && Mth.equal(pos[3].z(), 0.0F) && norm[0].z() < 0.0F) {
                cull = Direction.NORTH;
            } else if (Mth.equal(pos[0].z(), 1.0F) && Mth.equal(pos[1].z(), 1.0F) && Mth.equal(pos[2].z(), 1.0F) && Mth.equal(pos[3].z(), 1.0F) && norm[0].z() > 0.0F) {
                cull = Direction.SOUTH;
            } else if (Mth.equal(pos[0].y(), 0.0F) && Mth.equal(pos[1].y(), 0.0F) && Mth.equal(pos[2].y(), 0.0F) && Mth.equal(pos[3].y(), 0.0F) && norm[0].y() < 0.0F) {
                cull = Direction.DOWN;
            } else if (Mth.equal(pos[0].y(), 1.0F) && Mth.equal(pos[1].y(), 1.0F) && Mth.equal(pos[2].y(), 1.0F) && Mth.equal(pos[3].y(), 1.0F) && norm[0].y() > 0.0F) {
                cull = Direction.UP;
            }
        }
        return org.apache.commons.lang3.tuple.Pair.of(quadBaker.getQuad(), cull);
    }

    public CompositeRenderable bakeRenderable(IGeometryBakingContext configuration) {
        CompositeRenderable.Builder builder = CompositeRenderable.builder();
        for (Entry<String, ObjModel.ModelGroup> entry : this.parts.entrySet()) {
            String name = (String) entry.getKey();
            ObjModel.ModelGroup part = (ObjModel.ModelGroup) entry.getValue();
            part.bake(builder.child(name), configuration);
        }
        return builder.get();
    }

    public class ModelGroup extends ObjModel.ModelObject {

        final Map<String, ObjModel.ModelObject> parts = Maps.newLinkedHashMap();

        ModelGroup(String name) {
            super(name);
        }

        @Override
        public void addQuads(IGeometryBakingContext owner, IModelBuilder<?> modelBuilder, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ResourceLocation modelLocation) {
            super.addQuads(owner, modelBuilder, baker, spriteGetter, modelTransform, modelLocation);
            this.parts.values().stream().filter(part -> owner.isComponentVisible(part.name(), true)).forEach(part -> part.addQuads(owner, modelBuilder, baker, spriteGetter, modelTransform, modelLocation));
        }

        @Override
        public void bake(CompositeRenderable.PartBuilder<?> builder, IGeometryBakingContext configuration) {
            super.bake(builder, configuration);
            for (Entry<String, ObjModel.ModelObject> entry : this.parts.entrySet()) {
                String name = (String) entry.getKey();
                ObjModel.ModelObject part = (ObjModel.ModelObject) entry.getValue();
                part.bake(builder.child(name), configuration);
            }
        }

        @Override
        public Collection<Material> getTextures(IGeometryBakingContext owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
            Set<Material> combined = Sets.newHashSet();
            combined.addAll(super.getTextures(owner, modelGetter, missingTextureErrors));
            for (ObjModel.ModelObject part : this.parts.values()) {
                combined.addAll(part.getTextures(owner, modelGetter, missingTextureErrors));
            }
            return combined;
        }

        @Override
        protected void addNamesRecursively(Set<String> names) {
            super.addNamesRecursively(names);
            for (ObjModel.ModelObject object : this.parts.values()) {
                object.addNamesRecursively(names);
            }
        }
    }

    private class ModelMesh {

        @Nullable
        public ObjMaterialLibrary.Material mat;

        @Nullable
        public String smoothingGroup;

        public final List<int[][]> faces = Lists.newArrayList();

        public ModelMesh(@Nullable ObjMaterialLibrary.Material currentMat, @Nullable String currentSmoothingGroup) {
            this.mat = currentMat;
            this.smoothingGroup = currentSmoothingGroup;
        }

        public void addQuads(IGeometryBakingContext owner, IModelBuilder<?> modelBuilder, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform) {
            if (this.mat != null) {
                TextureAtlasSprite texture = (TextureAtlasSprite) spriteGetter.apply(UnbakedGeometryHelper.resolveDirtyMaterial(this.mat.diffuseColorMap, owner));
                int tintIndex = this.mat.diffuseTintIndex;
                Vector4f colorTint = this.mat.diffuseColor;
                Transformation rootTransform = owner.getRootTransform();
                Transformation transform = rootTransform.isIdentity() ? modelTransform.getRotation() : modelTransform.getRotation().compose(rootTransform);
                for (int[][] face : this.faces) {
                    org.apache.commons.lang3.tuple.Pair<BakedQuad, Direction> quad = ObjModel.this.makeQuad(face, tintIndex, colorTint, this.mat.ambientColor, texture, transform);
                    if (quad.getRight() == null) {
                        modelBuilder.addUnculledFace((BakedQuad) quad.getLeft());
                    } else {
                        modelBuilder.addCulledFace((Direction) quad.getRight(), (BakedQuad) quad.getLeft());
                    }
                }
            }
        }

        public void bake(CompositeRenderable.PartBuilder<?> builder, IGeometryBakingContext configuration) {
            ObjMaterialLibrary.Material mat = this.mat;
            if (mat != null) {
                int tintIndex = mat.diffuseTintIndex;
                Vector4f colorTint = mat.diffuseColor;
                List<BakedQuad> quads = new ArrayList();
                for (int[][] face : this.faces) {
                    org.apache.commons.lang3.tuple.Pair<BakedQuad, Direction> pair = ObjModel.this.makeQuad(face, tintIndex, colorTint, mat.ambientColor, UnitTextureAtlasSprite.INSTANCE, Transformation.identity());
                    quads.add((BakedQuad) pair.getLeft());
                }
                ResourceLocation textureLocation = UnbakedGeometryHelper.resolveDirtyMaterial(mat.diffuseColorMap, configuration).texture();
                ResourceLocation texturePath = new ResourceLocation(textureLocation.getNamespace(), "textures/" + textureLocation.getPath() + ".png");
                builder.addMesh(texturePath, quads);
            }
        }
    }

    public class ModelObject {

        public final String name;

        List<ObjModel.ModelMesh> meshes = Lists.newArrayList();

        ModelObject(String name) {
            this.name = name;
        }

        public String name() {
            return this.name;
        }

        public void addQuads(IGeometryBakingContext owner, IModelBuilder<?> modelBuilder, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ResourceLocation modelLocation) {
            for (ObjModel.ModelMesh mesh : this.meshes) {
                mesh.addQuads(owner, modelBuilder, spriteGetter, modelTransform);
            }
        }

        public void bake(CompositeRenderable.PartBuilder<?> builder, IGeometryBakingContext configuration) {
            for (ObjModel.ModelMesh mesh : this.meshes) {
                mesh.bake(builder, configuration);
            }
        }

        public Collection<Material> getTextures(IGeometryBakingContext owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
            return (Collection<Material>) this.meshes.stream().flatMap(mesh -> mesh.mat != null ? Stream.of(UnbakedGeometryHelper.resolveDirtyMaterial(mesh.mat.diffuseColorMap, owner)) : Stream.of()).collect(Collectors.toSet());
        }

        protected void addNamesRecursively(Set<String> names) {
            names.add(this.name());
        }
    }

    public static record ModelSettings(@NotNull ResourceLocation modelLocation, boolean automaticCulling, boolean shadeQuads, boolean flipV, boolean emissiveAmbient, @Nullable String mtlOverride) {
    }
}