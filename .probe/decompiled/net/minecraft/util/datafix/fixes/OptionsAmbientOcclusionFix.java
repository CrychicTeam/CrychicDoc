package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;

public class OptionsAmbientOcclusionFix extends DataFix {

    public OptionsAmbientOcclusionFix(Schema schema0) {
        super(schema0, false);
    }

    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("OptionsAmbientOcclusionFix", this.getInputSchema().getType(References.OPTIONS), p_263493_ -> p_263493_.update(DSL.remainderFinder(), p_263531_ -> (Dynamic) DataFixUtils.orElse(p_263531_.get("ao").asString().map(p_263546_ -> p_263531_.set("ao", p_263531_.createString(updateValue(p_263546_)))).result(), p_263531_)));
    }

    private static String updateValue(String string0) {
        return switch(string0) {
            case "0" ->
                "false";
            case "1", "2" ->
                "true";
            default ->
                string0;
        };
    }
}