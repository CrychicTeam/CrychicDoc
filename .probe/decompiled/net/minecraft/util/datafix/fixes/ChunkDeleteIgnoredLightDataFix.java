package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;

public class ChunkDeleteIgnoredLightDataFix extends DataFix {

    public ChunkDeleteIgnoredLightDataFix(Schema schema0) {
        super(schema0, true);
    }

    protected TypeRewriteRule makeRule() {
        Type<?> $$0 = this.getInputSchema().getType(References.CHUNK);
        OpticFinder<?> $$1 = $$0.findField("sections");
        return this.fixTypeEverywhereTyped("ChunkDeleteIgnoredLightDataFix", $$0, p_216575_ -> {
            boolean $$2 = ((Dynamic) p_216575_.get(DSL.remainderFinder())).get("isLightOn").asBoolean(false);
            return !$$2 ? p_216575_.updateTyped($$1, p_216577_ -> p_216577_.update(DSL.remainderFinder(), p_216579_ -> p_216579_.remove("BlockLight").remove("SkyLight"))) : p_216575_;
        });
    }
}