package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;

public class ChunkDeleteLightFix extends DataFix {

    public ChunkDeleteLightFix(Schema schema0) {
        super(schema0, false);
    }

    protected TypeRewriteRule makeRule() {
        Type<?> $$0 = this.getInputSchema().getType(References.CHUNK);
        OpticFinder<?> $$1 = $$0.findField("sections");
        return this.fixTypeEverywhereTyped("ChunkDeleteLightFix for " + this.getOutputSchema().getVersionKey(), $$0, p_285335_ -> {
            p_285335_ = p_285335_.update(DSL.remainderFinder(), p_284993_ -> p_284993_.remove("isLightOn"));
            return p_285335_.updateTyped($$1, p_285501_ -> p_285501_.update(DSL.remainderFinder(), p_285474_ -> p_285474_.remove("BlockLight").remove("SkyLight")));
        });
    }
}