package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import java.util.Map;
import java.util.stream.Collectors;

public class OptionsKeyTranslationFix extends DataFix {

    public OptionsKeyTranslationFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("OptionsKeyTranslationFix", this.getInputSchema().getType(References.OPTIONS), p_16648_ -> p_16648_.update(DSL.remainderFinder(), p_145582_ -> (Dynamic) p_145582_.getMapValues().map(p_145588_ -> p_145582_.createMap((Map) p_145588_.entrySet().stream().map(p_145585_ -> {
            if (((Dynamic) p_145585_.getKey()).asString("").startsWith("key_")) {
                String $$2 = ((Dynamic) p_145585_.getValue()).asString("");
                if (!$$2.startsWith("key.mouse") && !$$2.startsWith("scancode.")) {
                    return Pair.of((Dynamic) p_145585_.getKey(), p_145582_.createString("key.keyboard." + $$2.substring("key.".length())));
                }
            }
            return Pair.of((Dynamic) p_145585_.getKey(), (Dynamic) p_145585_.getValue());
        }).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)))).result().orElse(p_145582_)));
    }
}