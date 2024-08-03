package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.DSL.TypeReference;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import java.util.Map;

public class VariantRenameFix extends NamedEntityFix {

    private final Map<String, String> renames;

    public VariantRenameFix(Schema schema0, String string1, TypeReference typeReference2, String string3, Map<String, String> mapStringString4) {
        super(schema0, false, string1, typeReference2, string3);
        this.renames = mapStringString4;
    }

    @Override
    protected Typed<?> fix(Typed<?> typed0) {
        return typed0.update(DSL.remainderFinder(), p_216750_ -> p_216750_.update("variant", p_216755_ -> (Dynamic) DataFixUtils.orElse(p_216755_.asString().map(p_216753_ -> p_216755_.createString((String) this.renames.getOrDefault(p_216753_, p_216753_))).result(), p_216755_)));
    }
}