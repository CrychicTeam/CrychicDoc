package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;

public class WorldGenSettingsDisallowOldCustomWorldsFix extends DataFix {

    public WorldGenSettingsDisallowOldCustomWorldsFix(Schema schema0) {
        super(schema0, false);
    }

    protected TypeRewriteRule makeRule() {
        Type<?> $$0 = this.getInputSchema().getType(References.WORLD_GEN_SETTINGS);
        OpticFinder<?> $$1 = $$0.findField("dimensions");
        return this.fixTypeEverywhereTyped("WorldGenSettingsDisallowOldCustomWorldsFix_" + this.getOutputSchema().getVersionKey(), $$0, p_185160_ -> p_185160_.updateTyped($$1, p_185162_ -> {
            p_185162_.write().map(p_185164_ -> p_185164_.getMapValues().map(p_185169_ -> {
                p_185169_.forEach((p_185166_, p_185167_) -> {
                    if (p_185167_.get("type").asString().result().isEmpty()) {
                        throw new IllegalStateException("Unable load old custom worlds.");
                    }
                });
                return p_185169_;
            }));
            return p_185162_;
        }));
    }
}