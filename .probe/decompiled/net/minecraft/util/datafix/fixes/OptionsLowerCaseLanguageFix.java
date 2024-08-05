package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import java.util.Locale;
import java.util.Optional;

public class OptionsLowerCaseLanguageFix extends DataFix {

    public OptionsLowerCaseLanguageFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("OptionsLowerCaseLanguageFix", this.getInputSchema().getType(References.OPTIONS), p_16662_ -> p_16662_.update(DSL.remainderFinder(), p_145590_ -> {
            Optional<String> $$1 = p_145590_.get("lang").asString().result();
            return $$1.isPresent() ? p_145590_.set("lang", p_145590_.createString(((String) $$1.get()).toLowerCase(Locale.ROOT))) : p_145590_;
        }));
    }
}