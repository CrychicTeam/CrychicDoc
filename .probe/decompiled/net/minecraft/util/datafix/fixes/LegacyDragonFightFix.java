package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.OptionalDynamic;

public class LegacyDragonFightFix extends DataFix {

    public LegacyDragonFightFix(Schema schema0) {
        super(schema0, false);
    }

    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("LegacyDragonFightFix", this.getInputSchema().getType(References.LEVEL), p_289787_ -> p_289787_.update(DSL.remainderFinder(), p_289763_ -> {
            OptionalDynamic<?> $$1 = p_289763_.get("DragonFight");
            if ($$1.result().isPresent()) {
                return p_289763_;
            } else {
                Dynamic<?> $$2 = p_289763_.get("DimensionData").get("1").get("DragonFight").orElseEmptyMap();
                return p_289763_.set("DragonFight", $$2);
            }
        }));
    }
}