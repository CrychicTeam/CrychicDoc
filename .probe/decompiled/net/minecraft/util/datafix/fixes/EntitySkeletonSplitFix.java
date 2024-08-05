package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import java.util.Objects;

public class EntitySkeletonSplitFix extends SimpleEntityRenameFix {

    public EntitySkeletonSplitFix(Schema schema0, boolean boolean1) {
        super("EntitySkeletonSplitFix", schema0, boolean1);
    }

    @Override
    protected Pair<String, Dynamic<?>> getNewNameAndTag(String string0, Dynamic<?> dynamic1) {
        if (Objects.equals(string0, "Skeleton")) {
            int $$2 = dynamic1.get("SkeletonType").asInt(0);
            if ($$2 == 1) {
                string0 = "WitherSkeleton";
            } else if ($$2 == 2) {
                string0 = "Stray";
            }
        }
        return Pair.of(string0, dynamic1);
    }
}