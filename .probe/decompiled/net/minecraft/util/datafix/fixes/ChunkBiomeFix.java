package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

public class ChunkBiomeFix extends DataFix {

    public ChunkBiomeFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    protected TypeRewriteRule makeRule() {
        Type<?> $$0 = this.getInputSchema().getType(References.CHUNK);
        OpticFinder<?> $$1 = $$0.findField("Level");
        return this.fixTypeEverywhereTyped("Leaves fix", $$0, p_15018_ -> p_15018_.updateTyped($$1, p_145204_ -> p_145204_.update(DSL.remainderFinder(), p_145206_ -> {
            Optional<IntStream> $$1x = p_145206_.get("Biomes").asIntStreamOpt().result();
            if ($$1x.isEmpty()) {
                return p_145206_;
            } else {
                int[] $$2 = ((IntStream) $$1x.get()).toArray();
                if ($$2.length != 256) {
                    return p_145206_;
                } else {
                    int[] $$3 = new int[1024];
                    for (int $$4 = 0; $$4 < 4; $$4++) {
                        for (int $$5 = 0; $$5 < 4; $$5++) {
                            int $$6 = ($$5 << 2) + 2;
                            int $$7 = ($$4 << 2) + 2;
                            int $$8 = $$7 << 4 | $$6;
                            $$3[$$4 << 2 | $$5] = $$2[$$8];
                        }
                    }
                    for (int $$9 = 1; $$9 < 64; $$9++) {
                        System.arraycopy($$3, 0, $$3, $$9 * 16, 16);
                    }
                    return p_145206_.set("Biomes", p_145206_.createIntList(Arrays.stream($$3)));
                }
            }
        })));
    }
}