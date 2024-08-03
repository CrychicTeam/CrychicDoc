package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;

public class ChunkLightRemoveFix extends DataFix {

    public ChunkLightRemoveFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    protected TypeRewriteRule makeRule() {
        Type<?> $$0 = this.getInputSchema().getType(References.CHUNK);
        Type<?> $$1 = $$0.findFieldType("Level");
        OpticFinder<?> $$2 = DSL.fieldFinder("Level", $$1);
        return this.fixTypeEverywhereTyped("ChunkLightRemoveFix", $$0, this.getOutputSchema().getType(References.CHUNK), p_15029_ -> p_15029_.updateTyped($$2, p_145208_ -> p_145208_.update(DSL.remainderFinder(), p_145210_ -> p_145210_.remove("isLightOn"))));
    }
}