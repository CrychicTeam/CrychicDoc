package software.bernie.geckolib.loading.object;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import software.bernie.geckolib.loading.json.raw.Bone;
import software.bernie.geckolib.loading.json.raw.MinecraftGeometry;
import software.bernie.geckolib.loading.json.raw.Model;
import software.bernie.geckolib.loading.json.raw.ModelProperties;

public record GeometryTree(Map<String, BoneStructure> topLevelBones, ModelProperties properties) {

    public static GeometryTree fromModel(Model model) {
        Map<String, BoneStructure> topLevelBones = new Object2ObjectOpenHashMap();
        MinecraftGeometry geometry = model.minecraftGeometry()[0];
        Bone[] bones = geometry.bones();
        Map<String, BoneStructure> lookup = new Object2ObjectOpenHashMap(bones.length);
        for (Bone bone : bones) {
            BoneStructure boneStructure = new BoneStructure(bone);
            lookup.put(bone.name(), boneStructure);
            if (bone.parent() == null) {
                topLevelBones.put(bone.name(), boneStructure);
            }
        }
        for (Bone bonex : bones) {
            String parentName = bonex.parent();
            if (parentName != null) {
                String boneName = bonex.name();
                if (parentName.equals(boneName)) {
                    throw new IllegalArgumentException("Invalid model definition. Bone has defined itself as its own parent: " + boneName);
                }
                BoneStructure parentStructure = (BoneStructure) lookup.get(parentName);
                if (parentStructure == null) {
                    throw new IllegalArgumentException("Invalid model definition. Found bone with undefined parent (child -> parent): " + boneName + " -> " + parentName);
                }
                parentStructure.children().put(boneName, (BoneStructure) lookup.get(boneName));
            }
        }
        return new GeometryTree(topLevelBones, geometry.modelProperties());
    }

    @Deprecated(forRemoval = true)
    private static BoneStructure findBoneStructureInTree(Map<String, BoneStructure> bones, String boneName) {
        for (BoneStructure entry : bones.values()) {
            if (boneName.equals(entry.self().name())) {
                return entry;
            }
            BoneStructure subStructure = findBoneStructureInTree(entry.children(), boneName);
            if (subStructure != null) {
                return subStructure;
            }
        }
        return null;
    }
}