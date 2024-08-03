package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.List.ListType;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import java.util.List;
import java.util.stream.LongStream;
import net.minecraft.util.Mth;

public class BitStorageAlignFix extends DataFix {

    private static final int BIT_TO_LONG_SHIFT = 6;

    private static final int SECTION_WIDTH = 16;

    private static final int SECTION_HEIGHT = 16;

    private static final int SECTION_SIZE = 4096;

    private static final int HEIGHTMAP_BITS = 9;

    private static final int HEIGHTMAP_SIZE = 256;

    public BitStorageAlignFix(Schema schema0) {
        super(schema0, false);
    }

    protected TypeRewriteRule makeRule() {
        Type<?> $$0 = this.getInputSchema().getType(References.CHUNK);
        Type<?> $$1 = $$0.findFieldType("Level");
        OpticFinder<?> $$2 = DSL.fieldFinder("Level", $$1);
        OpticFinder<?> $$3 = $$2.type().findField("Sections");
        Type<?> $$4 = ((ListType) $$3.type()).getElement();
        OpticFinder<?> $$5 = DSL.typeFinder($$4);
        Type<Pair<String, Dynamic<?>>> $$6 = DSL.named(References.BLOCK_STATE.typeName(), DSL.remainderType());
        OpticFinder<List<Pair<String, Dynamic<?>>>> $$7 = DSL.fieldFinder("Palette", DSL.list($$6));
        return this.fixTypeEverywhereTyped("BitStorageAlignFix", $$0, this.getOutputSchema().getType(References.CHUNK), p_14749_ -> p_14749_.updateTyped($$2, p_145120_ -> this.updateHeightmaps(updateSections($$3, $$5, $$7, p_145120_))));
    }

    private Typed<?> updateHeightmaps(Typed<?> typed0) {
        return typed0.update(DSL.remainderFinder(), p_14765_ -> p_14765_.update("Heightmaps", p_145113_ -> p_145113_.updateMapValues(p_145110_ -> p_145110_.mapSecond(p_145123_ -> updateBitStorage(p_14765_, p_145123_, 256, 9)))));
    }

    private static Typed<?> updateSections(OpticFinder<?> opticFinder0, OpticFinder<?> opticFinder1, OpticFinder<List<Pair<String, Dynamic<?>>>> opticFinderListPairStringDynamic2, Typed<?> typed3) {
        return typed3.updateTyped(opticFinder0, p_14758_ -> p_14758_.updateTyped(opticFinder1, p_145103_ -> {
            int $$2 = (Integer) p_145103_.getOptional(opticFinderListPairStringDynamic2).map(p_145115_ -> Math.max(4, DataFixUtils.ceillog2(p_145115_.size()))).orElse(0);
            return $$2 != 0 && !Mth.isPowerOfTwo($$2) ? p_145103_.update(DSL.remainderFinder(), p_145100_ -> p_145100_.update("BlockStates", p_145107_ -> updateBitStorage(p_145100_, p_145107_, 4096, $$2))) : p_145103_;
        }));
    }

    private static Dynamic<?> updateBitStorage(Dynamic<?> dynamic0, Dynamic<?> dynamic1, int int2, int int3) {
        long[] $$4 = dynamic1.asLongStream().toArray();
        long[] $$5 = addPadding(int2, int3, $$4);
        return dynamic0.createLongList(LongStream.of($$5));
    }

    public static long[] addPadding(int int0, int int1, long[] long2) {
        int $$3 = long2.length;
        if ($$3 == 0) {
            return long2;
        } else {
            long $$4 = (1L << int1) - 1L;
            int $$5 = 64 / int1;
            int $$6 = (int0 + $$5 - 1) / $$5;
            long[] $$7 = new long[$$6];
            int $$8 = 0;
            int $$9 = 0;
            long $$10 = 0L;
            int $$11 = 0;
            long $$12 = long2[0];
            long $$13 = $$3 > 1 ? long2[1] : 0L;
            for (int $$14 = 0; $$14 < int0; $$14++) {
                int $$15 = $$14 * int1;
                int $$16 = $$15 >> 6;
                int $$17 = ($$14 + 1) * int1 - 1 >> 6;
                int $$18 = $$15 ^ $$16 << 6;
                if ($$16 != $$11) {
                    $$12 = $$13;
                    $$13 = $$16 + 1 < $$3 ? long2[$$16 + 1] : 0L;
                    $$11 = $$16;
                }
                long $$19;
                if ($$16 == $$17) {
                    $$19 = $$12 >>> $$18 & $$4;
                } else {
                    int $$20 = 64 - $$18;
                    $$19 = ($$12 >>> $$18 | $$13 << $$20) & $$4;
                }
                int $$22 = $$9 + int1;
                if ($$22 >= 64) {
                    $$7[$$8++] = $$10;
                    $$10 = $$19;
                    $$9 = int1;
                } else {
                    $$10 |= $$19 << $$9;
                    $$9 = $$22;
                }
            }
            if ($$10 != 0L) {
                $$7[$$8] = $$10;
            }
            return $$7;
        }
    }
}