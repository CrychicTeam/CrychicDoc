package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import java.util.function.Function;

public class AdvancementsRenameFix extends DataFix {

    private final String name;

    private final Function<String, String> renamer;

    public AdvancementsRenameFix(Schema schema0, boolean boolean1, String string2, Function<String, String> functionStringString3) {
        super(schema0, boolean1);
        this.name = string2;
        this.renamer = functionStringString3;
    }

    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped(this.name, this.getInputSchema().getType(References.ADVANCEMENTS), p_14657_ -> p_14657_.update(DSL.remainderFinder(), p_145063_ -> p_145063_.updateMapValues(p_145066_ -> {
            String $$2 = ((Dynamic) p_145066_.getFirst()).asString("");
            return p_145066_.mapFirst(p_145070_ -> p_145063_.createString((String) this.renamer.apply($$2)));
        })));
    }
}