package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;

public class OptionsAccessibilityOnboardFix extends DataFix {

    public OptionsAccessibilityOnboardFix(Schema schema0) {
        super(schema0, false);
    }

    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("OptionsAccessibilityOnboardFix", this.getInputSchema().getType(References.OPTIONS), p_265152_ -> p_265152_.update(DSL.remainderFinder(), p_265786_ -> p_265786_.set("onboardAccessibility", p_265786_.createBoolean(false))));
    }
}