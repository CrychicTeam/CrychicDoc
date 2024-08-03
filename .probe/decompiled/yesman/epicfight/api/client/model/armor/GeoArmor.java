package yesman.epicfight.api.client.model.armor;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.Map;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.cache.object.GeoQuad;
import software.bernie.geckolib.cache.object.GeoVertex;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.RenderUtils;
import yesman.epicfight.api.client.forgeevent.AnimatedArmorTextureEvent;
import yesman.epicfight.api.client.model.AnimatedMesh;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.client.model.SingleVertex;
import yesman.epicfight.api.utils.math.Vec2f;
import yesman.epicfight.api.utils.math.Vec3f;

@OnlyIn(Dist.CLIENT)
public class GeoArmor extends ArmorModelTransformer {

    static final ArmorModelTransformer.PartTransformer<GeoCube> HEAD = new GeoArmor.SimpleTransformer(9);

    static final ArmorModelTransformer.PartTransformer<GeoCube> LEFT_FEET = new GeoArmor.SimpleTransformer(5);

    static final ArmorModelTransformer.PartTransformer<GeoCube> RIGHT_FEET = new GeoArmor.SimpleTransformer(2);

    static final ArmorModelTransformer.PartTransformer<GeoCube> LEFT_ARM = new GeoArmor.LimbPartTransformer(16, 17, 19, 1.125F, false, AABB.ofSize(new Vec3(-0.375, 1.125, 0.0), 0.5, 0.85, 0.5));

    static final ArmorModelTransformer.PartTransformer<GeoCube> RIGHT_ARM = new GeoArmor.LimbPartTransformer(11, 12, 14, 1.125F, false, AABB.ofSize(new Vec3(0.375, 1.125, 0.0), 0.5, 0.85, 0.5));

    static final ArmorModelTransformer.PartTransformer<GeoCube> LEFT_LEG = new GeoArmor.LimbPartTransformer(4, 5, 6, 0.375F, true, AABB.ofSize(new Vec3(-0.15, 0.375, 0.0), 0.5, 0.85, 0.5));

    static final ArmorModelTransformer.PartTransformer<GeoCube> RIGHT_LEG = new GeoArmor.LimbPartTransformer(1, 2, 3, 0.375F, true, AABB.ofSize(new Vec3(0.15, 0.375, 0.0), 0.5, 0.85, 0.5));

    static final ArmorModelTransformer.PartTransformer<GeoCube> CHEST = new GeoArmor.ChestPartTransformer(8, 7, 1.125F, AABB.ofSize(new Vec3(0.0, 1.125, 0.0), 0.9, 0.85, 0.45));

    static int indexCount = 0;

    public static void getGeoArmorTexturePath(AnimatedArmorTextureEvent event) {
        IClientItemExtensions customRenderProperties = IClientItemExtensions.of(event.getItemstack());
        if (customRenderProperties != null && customRenderProperties.getHumanoidArmorModel(event.getLivingEntity(), event.getItemstack(), event.getEquipmentSlot(), event.getOriginalModel()) instanceof GeoArmorRenderer geoArmorRenderer && event.getItemstack().getItem() instanceof GeoAnimatable geoAnimatable) {
            event.setResultLocation(geoArmorRenderer.getTextureLocation(geoAnimatable));
        }
    }

    @Override
    public AnimatedMesh transformModel(HumanoidModel<?> model, ArmorItem armorItem, EquipmentSlot slot, boolean debuggingMode) {
        if (armorItem instanceof GeoItem && model instanceof GeoArmorRenderer geoModel) {
            List<GeoArmor.GeoModelPartition> boxes = Lists.newArrayList();
            GeoBone headBone = geoModel.getHeadBone();
            GeoBone bodyBone = geoModel.getBodyBone();
            GeoBone rightArmBone = geoModel.getRightArmBone();
            GeoBone leftArmBone = geoModel.getLeftArmBone();
            GeoBone rightLegBone = geoModel.getRightLegBone();
            GeoBone leftLegBone = geoModel.getLeftLegBone();
            GeoBone rightBootBone = geoModel.getRightBootBone();
            GeoBone leftBootBone = geoModel.getLeftBootBone();
            if (headBone != null) {
                headBone.setRotX(0.0F);
                headBone.setRotY(0.0F);
                headBone.setRotZ(0.0F);
            }
            if (bodyBone != null) {
                bodyBone.setRotX(0.0F);
                bodyBone.setRotY(0.0F);
                bodyBone.setRotZ(0.0F);
            }
            if (rightArmBone != null) {
                rightArmBone.setRotX(0.0F);
                rightArmBone.setRotY(0.0F);
                rightArmBone.setRotZ(0.0F);
            }
            if (leftArmBone != null) {
                leftArmBone.setRotX(0.0F);
                leftArmBone.setRotY(0.0F);
                leftArmBone.setRotZ(0.0F);
            }
            if (rightLegBone != null) {
                rightLegBone.setRotX(0.0F);
                rightLegBone.setRotY(0.0F);
                rightLegBone.setRotZ(0.0F);
            }
            if (leftLegBone != null) {
                leftLegBone.setRotX(0.0F);
                leftLegBone.setRotY(0.0F);
                leftLegBone.setRotZ(0.0F);
            }
            if (rightBootBone != null) {
                rightBootBone.setRotX(0.0F);
                rightBootBone.setRotY(0.0F);
                rightBootBone.setRotZ(0.0F);
            }
            if (leftBootBone != null) {
                leftBootBone.setRotX(0.0F);
                leftBootBone.setRotY(0.0F);
                leftBootBone.setRotZ(0.0F);
            }
            switch(slot) {
                case HEAD:
                    boxes.add(new GeoArmor.GeoModelPartition(HEAD, headBone));
                    break;
                case CHEST:
                    boxes.add(new GeoArmor.GeoModelPartition(CHEST, bodyBone));
                    boxes.add(new GeoArmor.GeoModelPartition(RIGHT_ARM, rightArmBone));
                    boxes.add(new GeoArmor.GeoModelPartition(LEFT_ARM, leftArmBone));
                    break;
                case LEGS:
                    boxes.add(new GeoArmor.GeoModelPartition(CHEST, bodyBone));
                    boxes.add(new GeoArmor.GeoModelPartition(LEFT_LEG, leftLegBone));
                    boxes.add(new GeoArmor.GeoModelPartition(RIGHT_LEG, rightLegBone));
                    break;
                case FEET:
                    boxes.add(new GeoArmor.GeoModelPartition(LEFT_FEET, leftBootBone));
                    boxes.add(new GeoArmor.GeoModelPartition(RIGHT_FEET, rightBootBone));
                    break;
                default:
                    return null;
            }
            ResourceLocation rl = new ResourceLocation(ForgeRegistries.ITEMS.getKey(armorItem).getNamespace(), "armor/" + ForgeRegistries.ITEMS.getKey(armorItem).getPath());
            AnimatedMesh armorModelMesh = bakeMeshFromCubes(boxes, debuggingMode);
            Meshes.addMesh(rl, armorModelMesh);
            return armorModelMesh;
        } else {
            return null;
        }
    }

    private static AnimatedMesh bakeMeshFromCubes(List<GeoArmor.GeoModelPartition> partitions, boolean debuggingMode) {
        List<SingleVertex> vertices = Lists.newArrayList();
        Map<String, List<Integer>> indices = Maps.newHashMap();
        PoseStack poseStack = new PoseStack();
        indexCount = 0;
        for (GeoArmor.GeoModelPartition modelpartition : partitions) {
            bake(poseStack, modelpartition, modelpartition.geoBone, vertices, indices, debuggingMode);
        }
        return SingleVertex.loadVertexInformation(vertices, indices);
    }

    private static void bake(PoseStack poseStack, GeoArmor.GeoModelPartition modelpartition, GeoBone geoBone, List<SingleVertex> vertices, Map<String, List<Integer>> indices, boolean debuggingMode) {
        if (geoBone != null) {
            poseStack.pushPose();
            if (!geoBone.isHidden()) {
                RenderUtils.prepMatrixForBone(poseStack, geoBone);
                for (GeoCube cube : geoBone.getCubes()) {
                    poseStack.pushPose();
                    RenderUtils.translateToPivotPoint(poseStack, cube);
                    RenderUtils.rotateMatrixAroundCube(poseStack, cube);
                    RenderUtils.translateAwayFromPivotPoint(poseStack, cube);
                    modelpartition.partTransformer.bakeCube(poseStack, geoBone.getName(), cube, vertices, indices);
                    poseStack.popPose();
                }
            }
            if (!geoBone.isHidingChildren()) {
                for (GeoBone childBone : geoBone.getChildBones()) {
                    bake(poseStack, modelpartition, childBone, vertices, indices, debuggingMode);
                }
            }
            poseStack.popPose();
        }
    }

    static Direction getDirectionFromVector(Vector3f directionVec) {
        for (Direction direction : Direction.values()) {
            Vector3f direcVec = new Vector3f(Float.compare(directionVec.x(), -0.0F) == 0 ? 0.0F : directionVec.x(), directionVec.y(), directionVec.z());
            if (direcVec.equals(direction.step())) {
                return direction;
            }
        }
        return null;
    }

    static Vec3 getCenterOfCube(PoseStack poseStack, GeoCube cube) {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double minZ = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        double maxZ = Double.MIN_VALUE;
        Matrix4f matrix = poseStack.last().pose();
        for (GeoQuad quad : cube.quads()) {
            for (GeoVertex v : quad.vertices()) {
                Vector4f translatedPosition = new Vector4f(v.position(), 1.0F);
                translatedPosition.mul(matrix);
                if (minX > (double) translatedPosition.x()) {
                    minX = (double) translatedPosition.x();
                }
                if (minY > (double) translatedPosition.y()) {
                    minY = (double) translatedPosition.y();
                }
                if (minZ > (double) translatedPosition.z()) {
                    minZ = (double) translatedPosition.z();
                }
                if (maxX < (double) translatedPosition.x()) {
                    maxX = (double) translatedPosition.x();
                }
                if (maxY < (double) translatedPosition.y()) {
                    maxY = (double) translatedPosition.y();
                }
                if (maxZ < (double) translatedPosition.z()) {
                    maxZ = (double) translatedPosition.z();
                }
            }
        }
        return new Vec3(minX + (maxX - minX) * 0.5, minY + (maxY - minY) * 0.5, minZ + (maxZ - minZ) * 0.5);
    }

    static Vector3f getClipPoint(Vector3f pos1, Vector3f pos2, float yClip) {
        Vector3f direct = new Vector3f(pos2);
        direct.sub(pos1);
        direct.mul((yClip - pos1.y()) / (pos2.y() - pos1.y()));
        Vector3f clipPoint = new Vector3f(pos1);
        clipPoint.add(direct);
        return clipPoint;
    }

    static ModelPart.Vertex getTranslatedVertex(GeoVertex original, Matrix4f matrix) {
        Vector4f translatedPosition = new Vector4f(original.position(), 1.0F);
        translatedPosition.mul(matrix);
        return new ModelPart.Vertex(translatedPosition.x(), translatedPosition.y(), translatedPosition.z(), original.texU(), original.texV());
    }

    @OnlyIn(Dist.CLIENT)
    static class AnimatedPolygon {

        public final GeoArmor.AnimatedVertex[] animatedVertexPositions;

        public final Vector3f normal;

        public AnimatedPolygon(GeoArmor.AnimatedVertex[] positionsIn, Direction directionIn) {
            this.animatedVertexPositions = positionsIn;
            this.normal = directionIn.step();
        }

        public AnimatedPolygon(GeoArmor.AnimatedVertex[] positionsIn, float cor, Direction directionIn) {
            this.animatedVertexPositions = positionsIn;
            positionsIn[0] = new GeoArmor.AnimatedVertex(positionsIn[0], positionsIn[0].f_104372_, positionsIn[0].f_104373_ + cor, positionsIn[0].jointId, positionsIn[0].weight);
            positionsIn[1] = new GeoArmor.AnimatedVertex(positionsIn[1], positionsIn[1].f_104372_, positionsIn[1].f_104373_ + cor, positionsIn[1].jointId, positionsIn[1].weight);
            positionsIn[2] = new GeoArmor.AnimatedVertex(positionsIn[2], positionsIn[2].f_104372_, positionsIn[2].f_104373_ - cor, positionsIn[2].jointId, positionsIn[2].weight);
            positionsIn[3] = new GeoArmor.AnimatedVertex(positionsIn[3], positionsIn[3].f_104372_, positionsIn[3].f_104373_ - cor, positionsIn[3].jointId, positionsIn[3].weight);
            this.normal = directionIn.step();
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class AnimatedVertex extends ModelPart.Vertex {

        final Vec3i jointId;

        final Vec3f weight;

        public AnimatedVertex(ModelPart.Vertex posTexVertx, int jointId) {
            this(posTexVertx, jointId, 0, 0, 1.0F, 0.0F, 0.0F);
        }

        public AnimatedVertex(ModelPart.Vertex posTexVertx, int jointId1, int jointId2, int jointId3, float weight1, float weight2, float weight3) {
            this(posTexVertx, new Vec3i(jointId1, jointId2, jointId3), new Vec3f(weight1, weight2, weight3));
        }

        public AnimatedVertex(ModelPart.Vertex posTexVertx, Vec3i ids, Vec3f weights) {
            this(posTexVertx, posTexVertx.u, posTexVertx.v, ids, weights);
        }

        public AnimatedVertex(ModelPart.Vertex posTexVertx, float u, float v, Vec3i ids, Vec3f weights) {
            super(posTexVertx.pos.x(), posTexVertx.pos.y(), posTexVertx.pos.z(), u, v);
            this.jointId = ids;
            this.weight = weights;
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class ChestPartTransformer extends ArmorModelTransformer.PartTransformer<GeoCube> {

        static final float X_PLANE = 0.0F;

        static final GeoArmor.ChestPartTransformer.VertexWeight[] WEIGHT_ALONG_Y = new GeoArmor.ChestPartTransformer.VertexWeight[] { new GeoArmor.ChestPartTransformer.VertexWeight(13.6666F, 0.23F, 0.77F), new GeoArmor.ChestPartTransformer.VertexWeight(15.8333F, 0.254F, 0.746F), new GeoArmor.ChestPartTransformer.VertexWeight(18.0F, 0.5F, 0.5F), new GeoArmor.ChestPartTransformer.VertexWeight(20.1666F, 0.744F, 0.256F), new GeoArmor.ChestPartTransformer.VertexWeight(22.3333F, 0.77F, 0.23F) };

        final GeoArmor.SimpleTransformer upperAttachmentTransformer;

        final GeoArmor.SimpleTransformer lowerAttachmentTransformer;

        final AABB noneAttachmentArea;

        final float yClipCoord;

        public ChestPartTransformer(int upperJoint, int lowerJoint, float yBasis, AABB noneAttachmentArea) {
            this.noneAttachmentArea = noneAttachmentArea;
            this.upperAttachmentTransformer = new GeoArmor.SimpleTransformer(upperJoint);
            this.lowerAttachmentTransformer = new GeoArmor.SimpleTransformer(lowerJoint);
            this.yClipCoord = yBasis;
        }

        public void bakeCube(PoseStack poseStack, String partName, GeoCube cube, List<SingleVertex> vertices, Map<String, List<Integer>> indices) {
            Vec3 centerOfCube = GeoArmor.getCenterOfCube(poseStack, cube);
            if (!this.noneAttachmentArea.contains(centerOfCube)) {
                if (centerOfCube.y < (double) this.yClipCoord) {
                    this.lowerAttachmentTransformer.bakeCube(poseStack, partName, cube, vertices, indices);
                } else {
                    this.upperAttachmentTransformer.bakeCube(poseStack, partName, cube, vertices, indices);
                }
            } else {
                List<GeoArmor.AnimatedPolygon> xClipPolygons = Lists.newArrayList();
                List<GeoArmor.AnimatedPolygon> xyClipPolygons = Lists.newArrayList();
                for (GeoQuad polygon : cube.quads()) {
                    Matrix4f matrix = poseStack.last().pose();
                    ModelPart.Vertex pos0 = GeoArmor.getTranslatedVertex(polygon.vertices()[0], matrix);
                    ModelPart.Vertex pos1 = GeoArmor.getTranslatedVertex(polygon.vertices()[1], matrix);
                    ModelPart.Vertex pos2 = GeoArmor.getTranslatedVertex(polygon.vertices()[2], matrix);
                    ModelPart.Vertex pos3 = GeoArmor.getTranslatedVertex(polygon.vertices()[3], matrix);
                    Direction direction = GeoArmor.getDirectionFromVector(polygon.normal());
                    GeoArmor.ChestPartTransformer.VertexWeight pos0Weight = getYClipWeight(pos0.pos.y());
                    GeoArmor.ChestPartTransformer.VertexWeight pos1Weight = getYClipWeight(pos1.pos.y());
                    GeoArmor.ChestPartTransformer.VertexWeight pos2Weight = getYClipWeight(pos2.pos.y());
                    GeoArmor.ChestPartTransformer.VertexWeight pos3Weight = getYClipWeight(pos3.pos.y());
                    if (pos1.pos.x() > 0.0F != pos2.pos.x() > 0.0F) {
                        float distance = pos2.pos.x() - pos1.pos.x();
                        float textureU = pos1.u + (pos2.u - pos1.u) * ((0.0F - pos1.pos.x()) / distance);
                        ModelPart.Vertex pos4 = new ModelPart.Vertex(0.0F, pos0.pos.y(), pos0.pos.z(), textureU, pos0.v);
                        ModelPart.Vertex pos5 = new ModelPart.Vertex(0.0F, pos1.pos.y(), pos1.pos.z(), textureU, pos1.v);
                        xClipPolygons.add(new GeoArmor.AnimatedPolygon(new GeoArmor.AnimatedVertex[] { new GeoArmor.AnimatedVertex(pos0, 8, 7, 0, pos0Weight.chestWeight, pos0Weight.torsoWeight, 0.0F), new GeoArmor.AnimatedVertex(pos4, 8, 7, 0, pos0Weight.chestWeight, pos0Weight.torsoWeight, 0.0F), new GeoArmor.AnimatedVertex(pos5, 8, 7, 0, pos1Weight.chestWeight, pos1Weight.torsoWeight, 0.0F), new GeoArmor.AnimatedVertex(pos3, 8, 7, 0, pos3Weight.chestWeight, pos3Weight.torsoWeight, 0.0F) }, direction));
                        xClipPolygons.add(new GeoArmor.AnimatedPolygon(new GeoArmor.AnimatedVertex[] { new GeoArmor.AnimatedVertex(pos4, 8, 7, 0, pos0Weight.chestWeight, pos0Weight.torsoWeight, 0.0F), new GeoArmor.AnimatedVertex(pos1, 8, 7, 0, pos1Weight.chestWeight, pos1Weight.torsoWeight, 0.0F), new GeoArmor.AnimatedVertex(pos2, 8, 7, 0, pos2Weight.chestWeight, pos2Weight.torsoWeight, 0.0F), new GeoArmor.AnimatedVertex(pos5, 8, 7, 0, pos1Weight.chestWeight, pos1Weight.torsoWeight, 0.0F) }, direction));
                    } else {
                        xClipPolygons.add(new GeoArmor.AnimatedPolygon(new GeoArmor.AnimatedVertex[] { new GeoArmor.AnimatedVertex(pos0, 8, 7, 0, pos0Weight.chestWeight, pos0Weight.torsoWeight, 0.0F), new GeoArmor.AnimatedVertex(pos1, 8, 7, 0, pos1Weight.chestWeight, pos1Weight.torsoWeight, 0.0F), new GeoArmor.AnimatedVertex(pos2, 8, 7, 0, pos2Weight.chestWeight, pos2Weight.torsoWeight, 0.0F), new GeoArmor.AnimatedVertex(pos3, 8, 7, 0, pos3Weight.chestWeight, pos3Weight.torsoWeight, 0.0F) }, direction));
                    }
                }
                for (GeoArmor.AnimatedPolygon polygonx : xClipPolygons) {
                    boolean upsideDown = polygonx.animatedVertexPositions[1].f_104371_.y() > polygonx.animatedVertexPositions[2].f_104371_.y();
                    GeoArmor.AnimatedVertex pos0 = upsideDown ? polygonx.animatedVertexPositions[2] : polygonx.animatedVertexPositions[0];
                    GeoArmor.AnimatedVertex pos1 = upsideDown ? polygonx.animatedVertexPositions[3] : polygonx.animatedVertexPositions[1];
                    GeoArmor.AnimatedVertex pos2 = upsideDown ? polygonx.animatedVertexPositions[0] : polygonx.animatedVertexPositions[2];
                    GeoArmor.AnimatedVertex pos3 = upsideDown ? polygonx.animatedVertexPositions[1] : polygonx.animatedVertexPositions[3];
                    Direction direction = GeoArmor.getDirectionFromVector(polygonx.normal);
                    List<GeoArmor.ChestPartTransformer.VertexWeight> vertexWeights = getMiddleYClipWeights(pos1.f_104371_.y(), pos2.f_104371_.y());
                    List<GeoArmor.AnimatedVertex> animatedVertices = Lists.newArrayList();
                    animatedVertices.add(pos0);
                    animatedVertices.add(pos1);
                    if (vertexWeights.size() > 0) {
                        for (GeoArmor.ChestPartTransformer.VertexWeight vertexWeight : vertexWeights) {
                            float distance = pos2.f_104371_.y() - pos1.f_104371_.y();
                            float textureV = pos1.f_104373_ + (pos2.f_104373_ - pos1.f_104373_) * ((vertexWeight.yClipCoord - pos1.f_104371_.y()) / distance);
                            Vector3f clipPos1 = GeoArmor.getClipPoint(pos1.f_104371_, pos2.f_104371_, vertexWeight.yClipCoord);
                            Vector3f clipPos2 = GeoArmor.getClipPoint(pos0.f_104371_, pos3.f_104371_, vertexWeight.yClipCoord);
                            ModelPart.Vertex pos4 = new ModelPart.Vertex(clipPos2, pos0.f_104372_, textureV);
                            ModelPart.Vertex pos5 = new ModelPart.Vertex(clipPos1, pos1.f_104372_, textureV);
                            animatedVertices.add(new GeoArmor.AnimatedVertex(pos4, 8, 7, 0, vertexWeight.chestWeight, vertexWeight.torsoWeight, 0.0F));
                            animatedVertices.add(new GeoArmor.AnimatedVertex(pos5, 8, 7, 0, vertexWeight.chestWeight, vertexWeight.torsoWeight, 0.0F));
                        }
                    }
                    animatedVertices.add(pos3);
                    animatedVertices.add(pos2);
                    for (int i = 0; i < (animatedVertices.size() - 2) / 2; i++) {
                        int start = i * 2;
                        GeoArmor.AnimatedVertex p0 = (GeoArmor.AnimatedVertex) animatedVertices.get(start);
                        GeoArmor.AnimatedVertex p1 = (GeoArmor.AnimatedVertex) animatedVertices.get(start + 1);
                        GeoArmor.AnimatedVertex p2 = (GeoArmor.AnimatedVertex) animatedVertices.get(start + 3);
                        GeoArmor.AnimatedVertex p3 = (GeoArmor.AnimatedVertex) animatedVertices.get(start + 2);
                        xyClipPolygons.add(new GeoArmor.AnimatedPolygon(new GeoArmor.AnimatedVertex[] { new GeoArmor.AnimatedVertex(p0, 8, 7, 0, p0.weight.x, p0.weight.y, 0.0F), new GeoArmor.AnimatedVertex(p1, 8, 7, 0, p1.weight.x, p1.weight.y, 0.0F), new GeoArmor.AnimatedVertex(p2, 8, 7, 0, p2.weight.x, p2.weight.y, 0.0F), new GeoArmor.AnimatedVertex(p3, 8, 7, 0, p3.weight.x, p3.weight.y, 0.0F) }, direction));
                    }
                }
                for (GeoArmor.AnimatedPolygon polygonx : xyClipPolygons) {
                    Vector3f norm = new Vector3f(polygonx.normal);
                    norm.mul(poseStack.last().normal());
                    for (GeoArmor.AnimatedVertex vertex : polygonx.animatedVertexPositions) {
                        Vector4f pos = new Vector4f(vertex.f_104371_, 1.0F);
                        float weight1 = vertex.weight.x;
                        float weight2 = vertex.weight.y;
                        int joint1 = vertex.jointId.getX();
                        int joint2 = vertex.jointId.getY();
                        int count = weight1 > 0.0F && weight2 > 0.0F ? 2 : 1;
                        if (weight1 <= 0.0F) {
                            joint1 = joint2;
                            weight1 = weight2;
                        }
                        vertices.add(new SingleVertex().setPosition(new Vec3f(pos.x(), pos.y(), pos.z())).setNormal(new Vec3f(norm.x(), norm.y(), norm.z())).setTextureCoordinate(new Vec2f(vertex.f_104372_, vertex.f_104373_)).setEffectiveJointIDs(new Vec3f((float) joint1, (float) joint2, 0.0F)).setEffectiveJointWeights(new Vec3f(weight1, weight2, 0.0F)).setEffectiveJointNumber(count));
                    }
                    this.putIndexCount(indices, partName, GeoArmor.indexCount);
                    this.putIndexCount(indices, partName, GeoArmor.indexCount + 1);
                    this.putIndexCount(indices, partName, GeoArmor.indexCount + 3);
                    this.putIndexCount(indices, partName, GeoArmor.indexCount + 3);
                    this.putIndexCount(indices, partName, GeoArmor.indexCount + 1);
                    this.putIndexCount(indices, partName, GeoArmor.indexCount + 2);
                    GeoArmor.indexCount += 4;
                }
            }
        }

        static GeoArmor.ChestPartTransformer.VertexWeight getYClipWeight(float y) {
            if (y < WEIGHT_ALONG_Y[0].yClipCoord) {
                return new GeoArmor.ChestPartTransformer.VertexWeight(y, 0.0F, 1.0F);
            } else {
                int index = -1;
                int i = 0;
                while (i < WEIGHT_ALONG_Y.length) {
                    i++;
                }
                if (index > 0) {
                    GeoArmor.ChestPartTransformer.VertexWeight pair = WEIGHT_ALONG_Y[index];
                    return new GeoArmor.ChestPartTransformer.VertexWeight(y, pair.chestWeight, pair.torsoWeight);
                } else {
                    return new GeoArmor.ChestPartTransformer.VertexWeight(y, 1.0F, 0.0F);
                }
            }
        }

        static List<GeoArmor.ChestPartTransformer.VertexWeight> getMiddleYClipWeights(float minY, float maxY) {
            List<GeoArmor.ChestPartTransformer.VertexWeight> cutYs = Lists.newArrayList();
            for (GeoArmor.ChestPartTransformer.VertexWeight vertexWeight : WEIGHT_ALONG_Y) {
                if (vertexWeight.yClipCoord > minY && maxY >= vertexWeight.yClipCoord) {
                    cutYs.add(vertexWeight);
                }
            }
            return cutYs;
        }

        static class VertexWeight {

            final float yClipCoord;

            final float chestWeight;

            final float torsoWeight;

            public VertexWeight(float yClipCoord, float chestWeight, float torsoWeight) {
                this.yClipCoord = yClipCoord;
                this.chestWeight = chestWeight;
                this.torsoWeight = torsoWeight;
            }
        }
    }

    static class GeoModelPartition {

        final ArmorModelTransformer.PartTransformer<GeoCube> partTransformer;

        final GeoBone geoBone;

        private GeoModelPartition(ArmorModelTransformer.PartTransformer<GeoCube> partTransformer, GeoBone geoBone) {
            this.partTransformer = partTransformer;
            this.geoBone = geoBone;
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class LimbPartTransformer extends ArmorModelTransformer.PartTransformer<GeoCube> {

        final int upperJoint;

        final int lowerJoint;

        final int middleJoint;

        final boolean bendInFront;

        final GeoArmor.SimpleTransformer upperAttachmentTransformer;

        final GeoArmor.SimpleTransformer lowerAttachmentTransformer;

        final AABB noneAttachmentArea;

        final float yClipCoord;

        public LimbPartTransformer(int upperJoint, int lowerJoint, int middleJoint, float yClipCoord, boolean bendInFront, AABB noneAttachmentArea) {
            this.upperJoint = upperJoint;
            this.lowerJoint = lowerJoint;
            this.middleJoint = middleJoint;
            this.bendInFront = bendInFront;
            this.upperAttachmentTransformer = new GeoArmor.SimpleTransformer(upperJoint);
            this.lowerAttachmentTransformer = new GeoArmor.SimpleTransformer(lowerJoint);
            this.noneAttachmentArea = noneAttachmentArea;
            this.yClipCoord = yClipCoord;
        }

        public void bakeCube(PoseStack poseStack, String partName, GeoCube cube, List<SingleVertex> vertices, Map<String, List<Integer>> indices) {
            Vec3 centerOfCube = GeoArmor.getCenterOfCube(poseStack, cube);
            if (!this.noneAttachmentArea.contains(centerOfCube)) {
                if (centerOfCube.y < (double) this.yClipCoord) {
                    this.lowerAttachmentTransformer.bakeCube(poseStack, partName, cube, vertices, indices);
                } else {
                    this.upperAttachmentTransformer.bakeCube(poseStack, partName, cube, vertices, indices);
                }
            } else {
                List<GeoArmor.AnimatedPolygon> polygons = Lists.newArrayList();
                for (GeoQuad quad : cube.quads()) {
                    Matrix4f matrix = poseStack.last().pose();
                    ModelPart.Vertex pos0 = GeoArmor.getTranslatedVertex(quad.vertices()[0], matrix);
                    ModelPart.Vertex pos1 = GeoArmor.getTranslatedVertex(quad.vertices()[1], matrix);
                    ModelPart.Vertex pos2 = GeoArmor.getTranslatedVertex(quad.vertices()[2], matrix);
                    ModelPart.Vertex pos3 = GeoArmor.getTranslatedVertex(quad.vertices()[3], matrix);
                    Direction direction = GeoArmor.getDirectionFromVector(quad.normal());
                    if (pos1.pos.y() > this.yClipCoord != pos2.pos.y() > this.yClipCoord) {
                        float distance = pos2.pos.y() - pos1.pos.y();
                        float textureV = pos1.v + (pos2.v - pos1.v) * ((this.yClipCoord - pos1.pos.y()) / distance);
                        Vector3f clipPos1 = GeoArmor.getClipPoint(pos1.pos, pos2.pos, this.yClipCoord);
                        Vector3f clipPos2 = GeoArmor.getClipPoint(pos0.pos, pos3.pos, this.yClipCoord);
                        ModelPart.Vertex pos4 = new ModelPart.Vertex(clipPos2, pos0.u, textureV);
                        ModelPart.Vertex pos5 = new ModelPart.Vertex(clipPos1, pos1.u, textureV);
                        int upperId;
                        int lowerId;
                        if (distance > 0.0F) {
                            upperId = this.lowerJoint;
                            lowerId = this.upperJoint;
                        } else {
                            upperId = this.upperJoint;
                            lowerId = this.lowerJoint;
                        }
                        polygons.add(new GeoArmor.AnimatedPolygon(new GeoArmor.AnimatedVertex[] { new GeoArmor.AnimatedVertex(pos0, upperId), new GeoArmor.AnimatedVertex(pos1, upperId), new GeoArmor.AnimatedVertex(pos5, upperId), new GeoArmor.AnimatedVertex(pos4, upperId) }, direction));
                        polygons.add(new GeoArmor.AnimatedPolygon(new GeoArmor.AnimatedVertex[] { new GeoArmor.AnimatedVertex(pos4, lowerId), new GeoArmor.AnimatedVertex(pos5, lowerId), new GeoArmor.AnimatedVertex(pos2, lowerId), new GeoArmor.AnimatedVertex(pos3, lowerId) }, direction));
                        boolean hasSameZ = pos4.pos.z() < 0.0F == pos5.pos.z() < 0.0F;
                        boolean isFront = hasSameZ && pos4.pos.z() < 0.0F == this.bendInFront;
                        if (isFront) {
                            polygons.add(new GeoArmor.AnimatedPolygon(new GeoArmor.AnimatedVertex[] { new GeoArmor.AnimatedVertex(pos4, this.middleJoint), new GeoArmor.AnimatedVertex(pos5, this.middleJoint), new GeoArmor.AnimatedVertex(pos5, this.upperJoint), new GeoArmor.AnimatedVertex(pos4, this.upperJoint) }, 0.001F, direction));
                            polygons.add(new GeoArmor.AnimatedPolygon(new GeoArmor.AnimatedVertex[] { new GeoArmor.AnimatedVertex(pos4, this.lowerJoint), new GeoArmor.AnimatedVertex(pos5, this.lowerJoint), new GeoArmor.AnimatedVertex(pos5, this.middleJoint), new GeoArmor.AnimatedVertex(pos4, this.middleJoint) }, 0.001F, direction));
                        } else if (!hasSameZ) {
                            boolean startFront = pos4.pos.z() > 0.0F;
                            int firstJoint = this.lowerJoint;
                            int secondJoint = this.lowerJoint;
                            int thirdJoint = startFront ? this.upperJoint : this.middleJoint;
                            int fourthJoint = startFront ? this.middleJoint : this.upperJoint;
                            int fifthJoint = this.upperJoint;
                            int sixthJoint = this.upperJoint;
                            polygons.add(new GeoArmor.AnimatedPolygon(new GeoArmor.AnimatedVertex[] { new GeoArmor.AnimatedVertex(pos4, firstJoint), new GeoArmor.AnimatedVertex(pos5, secondJoint), new GeoArmor.AnimatedVertex(pos5, thirdJoint), new GeoArmor.AnimatedVertex(pos4, fourthJoint) }, 0.001F, direction));
                            polygons.add(new GeoArmor.AnimatedPolygon(new GeoArmor.AnimatedVertex[] { new GeoArmor.AnimatedVertex(pos4, fourthJoint), new GeoArmor.AnimatedVertex(pos5, thirdJoint), new GeoArmor.AnimatedVertex(pos5, fifthJoint), new GeoArmor.AnimatedVertex(pos4, sixthJoint) }, 0.001F, direction));
                        }
                    } else {
                        int jointId = pos0.pos.y() > this.yClipCoord ? this.upperJoint : this.lowerJoint;
                        polygons.add(new GeoArmor.AnimatedPolygon(new GeoArmor.AnimatedVertex[] { new GeoArmor.AnimatedVertex(pos0, jointId), new GeoArmor.AnimatedVertex(pos1, jointId), new GeoArmor.AnimatedVertex(pos2, jointId), new GeoArmor.AnimatedVertex(pos3, jointId) }, direction));
                    }
                }
                for (GeoArmor.AnimatedPolygon quadx : polygons) {
                    Vector3f norm = new Vector3f(quadx.normal);
                    norm.mul(poseStack.last().normal());
                    for (GeoArmor.AnimatedVertex vertex : quadx.animatedVertexPositions) {
                        Vector4f pos = new Vector4f(vertex.f_104371_, 1.0F);
                        vertices.add(new SingleVertex().setPosition(new Vec3f(pos.x(), pos.y(), pos.z())).setNormal(new Vec3f(norm.x(), norm.y(), norm.z())).setTextureCoordinate(new Vec2f(vertex.f_104372_, vertex.f_104373_)).setEffectiveJointIDs(new Vec3f((float) vertex.jointId.getX(), 0.0F, 0.0F)).setEffectiveJointWeights(new Vec3f(1.0F, 0.0F, 0.0F)).setEffectiveJointNumber(1));
                    }
                    this.putIndexCount(indices, partName, GeoArmor.indexCount);
                    this.putIndexCount(indices, partName, GeoArmor.indexCount + 1);
                    this.putIndexCount(indices, partName, GeoArmor.indexCount + 3);
                    this.putIndexCount(indices, partName, GeoArmor.indexCount + 3);
                    this.putIndexCount(indices, partName, GeoArmor.indexCount + 1);
                    this.putIndexCount(indices, partName, GeoArmor.indexCount + 2);
                    GeoArmor.indexCount += 4;
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class SimpleTransformer extends ArmorModelTransformer.PartTransformer<GeoCube> {

        final int jointId;

        public SimpleTransformer(int jointId) {
            this.jointId = jointId;
        }

        public void bakeCube(PoseStack poseStack, String partName, GeoCube cube, List<SingleVertex> vertices, Map<String, List<Integer>> indices) {
            for (GeoQuad quad : cube.quads()) {
                if (quad != null) {
                    Vector3f norm = new Vector3f(quad.normal());
                    norm.mul(poseStack.last().normal());
                    for (GeoVertex vertex : quad.vertices()) {
                        Vector4f pos = new Vector4f(vertex.position(), 1.0F);
                        pos.mul(poseStack.last().pose());
                        vertices.add(new SingleVertex().setPosition(new Vec3f(pos.x(), pos.y(), pos.z())).setNormal(new Vec3f(norm.x(), norm.y(), norm.z())).setTextureCoordinate(new Vec2f(vertex.texU(), vertex.texV())).setEffectiveJointIDs(new Vec3f((float) this.jointId, 0.0F, 0.0F)).setEffectiveJointWeights(new Vec3f(1.0F, 0.0F, 0.0F)).setEffectiveJointNumber(1));
                    }
                    this.putIndexCount(indices, partName, GeoArmor.indexCount);
                    this.putIndexCount(indices, partName, GeoArmor.indexCount + 1);
                    this.putIndexCount(indices, partName, GeoArmor.indexCount + 3);
                    this.putIndexCount(indices, partName, GeoArmor.indexCount + 3);
                    this.putIndexCount(indices, partName, GeoArmor.indexCount + 1);
                    this.putIndexCount(indices, partName, GeoArmor.indexCount + 2);
                    GeoArmor.indexCount += 4;
                }
            }
        }
    }
}