package software.bernie.geckolib.core.animatable.model;

import java.util.List;
import java.util.Optional;

public interface CoreBakedGeoModel {

    List<? extends CoreGeoBone> getBones();

    Optional<? extends CoreGeoBone> getBone(String var1);

    default CoreGeoBone searchForChildBone(CoreGeoBone parent, String name) {
        if (parent.getName().equals(name)) {
            return parent;
        } else {
            for (CoreGeoBone bone : parent.getChildBones()) {
                if (bone.getName().equals(name)) {
                    return bone;
                }
                CoreGeoBone subChildBone = this.searchForChildBone(bone, name);
                if (subChildBone != null) {
                    return subChildBone;
                }
            }
            return null;
        }
    }
}