package software.bernie.geckolib.loading.object;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.cache.object.GeoQuad;
import software.bernie.geckolib.cache.object.GeoVertex;
import software.bernie.geckolib.loading.json.raw.Bone;
import software.bernie.geckolib.loading.json.raw.Cube;
import software.bernie.geckolib.loading.json.raw.FaceUV;
import software.bernie.geckolib.loading.json.raw.ModelProperties;
import software.bernie.geckolib.loading.json.raw.UVUnion;
import software.bernie.geckolib.util.RenderUtils;

public interface BakedModelFactory {

    Map<String, BakedModelFactory> FACTORIES = new Object2ObjectOpenHashMap(1);

    BakedModelFactory DEFAULT_FACTORY = new BakedModelFactory.Builtin();

    BakedGeoModel constructGeoModel(GeometryTree var1);

    GeoBone constructBone(BoneStructure var1, ModelProperties var2, @Nullable GeoBone var3);

    GeoCube constructCube(Cube var1, ModelProperties var2, GeoBone var3);

    default GeoQuad[] buildQuads(UVUnion uvUnion, BakedModelFactory.VertexSet vertices, Cube cube, float textureWidth, float textureHeight, boolean mirror) {
        return new GeoQuad[] { this.buildQuad(vertices, cube, uvUnion, textureWidth, textureHeight, mirror, Direction.WEST), this.buildQuad(vertices, cube, uvUnion, textureWidth, textureHeight, mirror, Direction.EAST), this.buildQuad(vertices, cube, uvUnion, textureWidth, textureHeight, mirror, Direction.NORTH), this.buildQuad(vertices, cube, uvUnion, textureWidth, textureHeight, mirror, Direction.SOUTH), this.buildQuad(vertices, cube, uvUnion, textureWidth, textureHeight, mirror, Direction.UP), this.buildQuad(vertices, cube, uvUnion, textureWidth, textureHeight, mirror, Direction.DOWN) };
    }

    default GeoQuad buildQuad(BakedModelFactory.VertexSet vertices, Cube cube, UVUnion uvUnion, float textureWidth, float textureHeight, boolean mirror, Direction direction) {
        if (!uvUnion.isBoxUV()) {
            FaceUV faceUV = uvUnion.faceUV().fromDirection(direction);
            return faceUV == null ? null : GeoQuad.build(vertices.verticesForQuad(direction, false, mirror || cube.mirror() == Boolean.TRUE), faceUV.uv(), faceUV.uvSize(), textureWidth, textureHeight, mirror, direction);
        } else {
            double[] uv = cube.uv().boxUVCoords();
            double[] uvSize = cube.size();
            Vec3 uvSizeVec = new Vec3(Math.floor(uvSize[0]), Math.floor(uvSize[1]), Math.floor(uvSize[2]));
            double[][] uvData = switch(direction) {
                case WEST ->
                    new double[][] { { uv[0] + uvSizeVec.z + uvSizeVec.x, uv[1] + uvSizeVec.z }, { uvSizeVec.z, uvSizeVec.y } };
                case EAST ->
                    new double[][] { { uv[0], uv[1] + uvSizeVec.z }, { uvSizeVec.z, uvSizeVec.y } };
                case NORTH ->
                    new double[][] { { uv[0] + uvSizeVec.z, uv[1] + uvSizeVec.z }, { uvSizeVec.x, uvSizeVec.y } };
                case SOUTH ->
                    new double[][] { { uv[0] + uvSizeVec.z + uvSizeVec.x + uvSizeVec.z, uv[1] + uvSizeVec.z }, { uvSizeVec.x, uvSizeVec.y } };
                case UP ->
                    new double[][] { { uv[0] + uvSizeVec.z, uv[1] }, { uvSizeVec.x, uvSizeVec.z } };
                case DOWN ->
                    new double[][] { { uv[0] + uvSizeVec.z + uvSizeVec.x, uv[1] + uvSizeVec.z }, { uvSizeVec.x, -uvSizeVec.z } };
            };
            return GeoQuad.build(vertices.verticesForQuad(direction, true, mirror || cube.mirror() == Boolean.TRUE), uvData[0], uvData[1], textureWidth, textureHeight, mirror, direction);
        }
    }

    static BakedModelFactory getForNamespace(String namespace) {
        return (BakedModelFactory) FACTORIES.getOrDefault(namespace, DEFAULT_FACTORY);
    }

    static void register(String namespace, BakedModelFactory factory) {
        FACTORIES.put(namespace, factory);
    }

    public static final class Builtin implements BakedModelFactory {

        @Override
        public BakedGeoModel constructGeoModel(GeometryTree geometryTree) {
            List<GeoBone> bones = new ObjectArrayList();
            for (BoneStructure boneStructure : geometryTree.topLevelBones().values()) {
                bones.add(this.constructBone(boneStructure, geometryTree.properties(), null));
            }
            return new BakedGeoModel(bones, geometryTree.properties());
        }

        @Override
        public GeoBone constructBone(BoneStructure boneStructure, ModelProperties properties, GeoBone parent) {
            Bone bone = boneStructure.self();
            GeoBone newBone = new GeoBone(parent, bone.name(), bone.mirror(), bone.inflate(), bone.neverRender(), bone.reset());
            Vec3 rotation = RenderUtils.arrayToVec(bone.rotation());
            Vec3 pivot = RenderUtils.arrayToVec(bone.pivot());
            newBone.updateRotation((float) Math.toRadians(-rotation.x), (float) Math.toRadians(-rotation.y), (float) Math.toRadians(rotation.z));
            newBone.updatePivot((float) (-pivot.x), (float) pivot.y, (float) pivot.z);
            for (Cube cube : bone.cubes()) {
                newBone.getCubes().add(this.constructCube(cube, properties, newBone));
            }
            for (BoneStructure child : boneStructure.children().values()) {
                newBone.getChildBones().add(this.constructBone(child, properties, newBone));
            }
            return newBone;
        }

        @Override
        public GeoCube constructCube(Cube cube, ModelProperties properties, GeoBone bone) {
            boolean mirror = cube.mirror() == Boolean.TRUE;
            double inflate = cube.inflate() != null ? cube.inflate() / 16.0 : (bone.getInflate() == null ? 0.0 : bone.getInflate() / 16.0);
            Vec3 size = RenderUtils.arrayToVec(cube.size());
            Vec3 origin = RenderUtils.arrayToVec(cube.origin());
            Vec3 rotation = RenderUtils.arrayToVec(cube.rotation());
            Vec3 pivot = RenderUtils.arrayToVec(cube.pivot());
            origin = new Vec3(-(origin.x + size.x) / 16.0, origin.y / 16.0, origin.z / 16.0);
            Vec3 vertexSize = size.multiply(0.0625, 0.0625, 0.0625);
            pivot = pivot.multiply(-1.0, 1.0, 1.0);
            rotation = new Vec3(Math.toRadians(-rotation.x), Math.toRadians(-rotation.y), Math.toRadians(rotation.z));
            GeoQuad[] quads = this.buildQuads(cube.uv(), new BakedModelFactory.VertexSet(origin, vertexSize, inflate), cube, (float) properties.textureWidth(), (float) properties.textureHeight(), mirror);
            return new GeoCube(quads, pivot, rotation, size, inflate, mirror);
        }
    }

    public static record VertexSet(GeoVertex bottomLeftBack, GeoVertex bottomRightBack, GeoVertex topLeftBack, GeoVertex topRightBack, GeoVertex topLeftFront, GeoVertex topRightFront, GeoVertex bottomLeftFront, GeoVertex bottomRightFront) {

        public VertexSet(Vec3 origin, Vec3 vertexSize, double inflation) {
            this(new GeoVertex(origin.x - inflation, origin.y - inflation, origin.z - inflation), new GeoVertex(origin.x - inflation, origin.y - inflation, origin.z + vertexSize.z + inflation), new GeoVertex(origin.x - inflation, origin.y + vertexSize.y + inflation, origin.z - inflation), new GeoVertex(origin.x - inflation, origin.y + vertexSize.y + inflation, origin.z + vertexSize.z + inflation), new GeoVertex(origin.x + vertexSize.x + inflation, origin.y + vertexSize.y + inflation, origin.z - inflation), new GeoVertex(origin.x + vertexSize.x + inflation, origin.y + vertexSize.y + inflation, origin.z + vertexSize.z + inflation), new GeoVertex(origin.x + vertexSize.x + inflation, origin.y - inflation, origin.z - inflation), new GeoVertex(origin.x + vertexSize.x + inflation, origin.y - inflation, origin.z + vertexSize.z + inflation));
        }

        public GeoVertex[] quadWest() {
            return new GeoVertex[] { this.topRightBack, this.topLeftBack, this.bottomLeftBack, this.bottomRightBack };
        }

        public GeoVertex[] quadEast() {
            return new GeoVertex[] { this.topLeftFront, this.topRightFront, this.bottomRightFront, this.bottomLeftFront };
        }

        public GeoVertex[] quadNorth() {
            return new GeoVertex[] { this.topLeftBack, this.topLeftFront, this.bottomLeftFront, this.bottomLeftBack };
        }

        public GeoVertex[] quadSouth() {
            return new GeoVertex[] { this.topRightFront, this.topRightBack, this.bottomRightBack, this.bottomRightFront };
        }

        public GeoVertex[] quadUp() {
            return new GeoVertex[] { this.topRightBack, this.topRightFront, this.topLeftFront, this.topLeftBack };
        }

        public GeoVertex[] quadDown() {
            return new GeoVertex[] { this.bottomLeftBack, this.bottomLeftFront, this.bottomRightFront, this.bottomRightBack };
        }

        public GeoVertex[] verticesForQuad(Direction direction, boolean boxUv, boolean mirror) {
            return switch(direction) {
                case WEST ->
                    mirror ? this.quadEast() : this.quadWest();
                case EAST ->
                    mirror ? this.quadWest() : this.quadEast();
                case NORTH ->
                    this.quadNorth();
                case SOUTH ->
                    this.quadSouth();
                case UP ->
                    mirror && !boxUv ? this.quadDown() : this.quadUp();
                case DOWN ->
                    mirror && !boxUv ? this.quadUp() : this.quadDown();
            };
        }
    }
}