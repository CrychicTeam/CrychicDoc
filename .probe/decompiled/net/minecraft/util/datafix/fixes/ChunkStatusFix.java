package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import java.util.Objects;

public class ChunkStatusFix extends DataFix {

    public ChunkStatusFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    protected TypeRewriteRule makeRule() {
        Type<?> $$0 = this.getInputSchema().getType(References.CHUNK);
        Type<?> $$1 = $$0.findFieldType("Level");
        OpticFinder<?> $$2 = DSL.fieldFinder("Level", $$1);
        return this.fixTypeEverywhereTyped("ChunkStatusFix", $$0, this.getOutputSchema().getType(References.CHUNK), p_15251_ -> p_15251_.updateTyped($$2, p_145230_ -> {
            Dynamic<?> $$1x = (Dynamic<?>) p_145230_.get(DSL.remainderFinder());
            String $$2x = $$1x.get("Status").asString("empty");
            if (Objects.equals($$2x, "postprocessed")) {
                $$1x = $$1x.set("Status", $$1x.createString("fullchunk"));
            }
            return p_145230_.set(DSL.remainderFinder(), $$1x);
        }));
    }
}