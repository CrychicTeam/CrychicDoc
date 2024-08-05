package software.bernie.geckolib.loading.object;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import software.bernie.geckolib.loading.json.raw.Bone;

public record BoneStructure(Bone self, Map<String, BoneStructure> children) {

    public BoneStructure(Bone self) {
        this(self, new Object2ObjectOpenHashMap());
    }
}