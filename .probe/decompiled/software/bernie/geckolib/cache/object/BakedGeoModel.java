package software.bernie.geckolib.cache.object;

import java.util.List;
import java.util.Optional;
import software.bernie.geckolib.core.animatable.model.CoreBakedGeoModel;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.loading.json.raw.ModelProperties;

public record BakedGeoModel(List<GeoBone> topLevelBones, ModelProperties properties) implements CoreBakedGeoModel {

    @Override
    public List<? extends CoreGeoBone> getBones() {
        return this.topLevelBones;
    }

    @Override
    public Optional<GeoBone> getBone(String name) {
        for (GeoBone bone : this.topLevelBones) {
            CoreGeoBone childBone = this.searchForChildBone(bone, name);
            if (childBone != null) {
                return Optional.of((GeoBone) childBone);
            }
        }
        return Optional.empty();
    }
}