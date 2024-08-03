package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.OptionalDynamic;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.SectionPos;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class BlendingDataFix extends DataFix {

    private final String name;

    private static final Set<String> STATUSES_TO_SKIP_BLENDING = Set.of("minecraft:empty", "minecraft:structure_starts", "minecraft:structure_references", "minecraft:biomes");

    public BlendingDataFix(Schema schema0) {
        super(schema0, false);
        this.name = "Blending Data Fix v" + schema0.getVersionKey();
    }

    protected TypeRewriteRule makeRule() {
        Type<?> $$0 = this.getOutputSchema().getType(References.CHUNK);
        return this.fixTypeEverywhereTyped(this.name, $$0, p_216563_ -> p_216563_.update(DSL.remainderFinder(), p_240248_ -> updateChunkTag(p_240248_, p_240248_.get("__context"))));
    }

    private static Dynamic<?> updateChunkTag(Dynamic<?> dynamic0, OptionalDynamic<?> optionalDynamic1) {
        dynamic0 = dynamic0.remove("blending_data");
        boolean $$2 = "minecraft:overworld".equals(optionalDynamic1.get("dimension").asString().result().orElse(""));
        Optional<? extends Dynamic<?>> $$3 = dynamic0.get("Status").result();
        if ($$2 && $$3.isPresent()) {
            String $$4 = NamespacedSchema.ensureNamespaced(((Dynamic) $$3.get()).asString("empty"));
            Optional<? extends Dynamic<?>> $$5 = dynamic0.get("below_zero_retrogen").result();
            if (!STATUSES_TO_SKIP_BLENDING.contains($$4)) {
                dynamic0 = updateBlendingData(dynamic0, 384, -64);
            } else if ($$5.isPresent()) {
                Dynamic<?> $$6 = (Dynamic<?>) $$5.get();
                String $$7 = NamespacedSchema.ensureNamespaced($$6.get("target_status").asString("empty"));
                if (!STATUSES_TO_SKIP_BLENDING.contains($$7)) {
                    dynamic0 = updateBlendingData(dynamic0, 256, 0);
                }
            }
        }
        return dynamic0;
    }

    private static Dynamic<?> updateBlendingData(Dynamic<?> dynamic0, int int1, int int2) {
        return dynamic0.set("blending_data", dynamic0.createMap(Map.of(dynamic0.createString("min_section"), dynamic0.createInt(SectionPos.blockToSectionCoord(int2)), dynamic0.createString("max_section"), dynamic0.createInt(SectionPos.blockToSectionCoord(int2 + int1)))));
    }
}