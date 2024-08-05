package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import java.util.Objects;

public class EntityElderGuardianSplitFix extends SimpleEntityRenameFix {

    public EntityElderGuardianSplitFix(Schema schema0, boolean boolean1) {
        super("EntityElderGuardianSplitFix", schema0, boolean1);
    }

    @Override
    protected Pair<String, Dynamic<?>> getNewNameAndTag(String string0, Dynamic<?> dynamic1) {
        return Pair.of(Objects.equals(string0, "Guardian") && dynamic1.get("Elder").asBoolean(false) ? "ElderGuardian" : string0, dynamic1);
    }
}