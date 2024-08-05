package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import java.util.List;
import java.util.Optional;

public class OverreachingTickFix extends DataFix {

    public OverreachingTickFix(Schema schema0) {
        super(schema0, false);
    }

    protected TypeRewriteRule makeRule() {
        Type<?> $$0 = this.getInputSchema().getType(References.CHUNK);
        OpticFinder<?> $$1 = $$0.findField("block_ticks");
        return this.fixTypeEverywhereTyped("Handle ticks saved in the wrong chunk", $$0, p_207661_ -> {
            Optional<? extends Typed<?>> $$2 = p_207661_.getOptionalTyped($$1);
            Optional<? extends Dynamic<?>> $$3 = $$2.isPresent() ? ((Typed) $$2.get()).write().result() : Optional.empty();
            return p_207661_.update(DSL.remainderFinder(), p_207670_ -> {
                int $$2x = p_207670_.get("xPos").asInt(0);
                int $$3x = p_207670_.get("zPos").asInt(0);
                Optional<? extends Dynamic<?>> $$4 = p_207670_.get("fluid_ticks").get().result();
                p_207670_ = extractOverreachingTicks(p_207670_, $$2x, $$3x, $$3, "neighbor_block_ticks");
                return extractOverreachingTicks(p_207670_, $$2x, $$3x, $$4, "neighbor_fluid_ticks");
            });
        });
    }

    private static Dynamic<?> extractOverreachingTicks(Dynamic<?> dynamic0, int int1, int int2, Optional<? extends Dynamic<?>> optionalExtendsDynamic3, String string4) {
        if (optionalExtendsDynamic3.isPresent()) {
            List<? extends Dynamic<?>> $$5 = ((Dynamic) optionalExtendsDynamic3.get()).asStream().filter(p_207658_ -> {
                int $$3 = p_207658_.get("x").asInt(0);
                int $$4 = p_207658_.get("z").asInt(0);
                int $$5x = Math.abs(int1 - ($$3 >> 4));
                int $$6 = Math.abs(int2 - ($$4 >> 4));
                return ($$5x != 0 || $$6 != 0) && $$5x <= 1 && $$6 <= 1;
            }).toList();
            if (!$$5.isEmpty()) {
                dynamic0 = dynamic0.set("UpgradeData", dynamic0.get("UpgradeData").orElseEmptyMap().set(string4, dynamic0.createList($$5.stream())));
            }
        }
        return dynamic0;
    }
}