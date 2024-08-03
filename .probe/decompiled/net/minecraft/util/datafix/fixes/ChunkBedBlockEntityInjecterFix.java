package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Streams;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.List.ListType;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ChunkBedBlockEntityInjecterFix extends DataFix {

    public ChunkBedBlockEntityInjecterFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    public TypeRewriteRule makeRule() {
        Type<?> $$0 = this.getOutputSchema().getType(References.CHUNK);
        Type<?> $$1 = $$0.findFieldType("Level");
        if (!($$1.findFieldType("TileEntities") instanceof ListType<?> $$3)) {
            throw new IllegalStateException("Tile entity type is not a list type.");
        } else {
            return this.cap($$1, $$3);
        }
    }

    private <TE> TypeRewriteRule cap(Type<?> type0, ListType<TE> listTypeTE1) {
        Type<TE> $$2 = listTypeTE1.getElement();
        OpticFinder<?> $$3 = DSL.fieldFinder("Level", type0);
        OpticFinder<List<TE>> $$4 = DSL.fieldFinder("TileEntities", listTypeTE1);
        int $$5 = 416;
        return TypeRewriteRule.seq(this.fixTypeEverywhere("InjectBedBlockEntityType", this.getInputSchema().findChoiceType(References.BLOCK_ENTITY), this.getOutputSchema().findChoiceType(References.BLOCK_ENTITY), p_184841_ -> p_184837_ -> p_184837_), this.fixTypeEverywhereTyped("BedBlockEntityInjecter", this.getOutputSchema().getType(References.CHUNK), p_274912_ -> {
            Typed<?> $$4x = p_274912_.getTyped($$3);
            Dynamic<?> $$5x = (Dynamic<?>) $$4x.get(DSL.remainderFinder());
            int $$6 = $$5x.get("xPos").asInt(0);
            int $$7 = $$5x.get("zPos").asInt(0);
            List<TE> $$8 = Lists.newArrayList((Iterable) $$4x.getOrCreate($$4));
            List<? extends Dynamic<?>> $$9 = $$5x.get("Sections").asList(Function.identity());
            for (int $$10 = 0; $$10 < $$9.size(); $$10++) {
                Dynamic<?> $$11 = (Dynamic<?>) $$9.get($$10);
                int $$12 = $$11.get("Y").asInt(0);
                Streams.mapWithIndex($$11.get("Blocks").asIntStream(), (p_274917_, p_274918_) -> {
                    if (416 == (p_274917_ & 0xFF) << 4) {
                        int $$6x = (int) p_274918_;
                        int $$7x = $$6x & 15;
                        int $$8x = $$6x >> 8 & 15;
                        int $$9x = $$6x >> 4 & 15;
                        Map<Dynamic<?>, Dynamic<?>> $$10x = Maps.newHashMap();
                        $$10x.put($$11.createString("id"), $$11.createString("minecraft:bed"));
                        $$10x.put($$11.createString("x"), $$11.createInt($$7x + ($$6 << 4)));
                        $$10x.put($$11.createString("y"), $$11.createInt($$8x + ($$12 << 4)));
                        $$10x.put($$11.createString("z"), $$11.createInt($$9x + ($$7 << 4)));
                        $$10x.put($$11.createString("color"), $$11.createShort((short) 14));
                        return $$10x;
                    } else {
                        return null;
                    }
                }).forEachOrdered(p_274922_ -> {
                    if (p_274922_ != null) {
                        $$8.add(((Pair) $$2.read($$11.createMap(p_274922_)).result().orElseThrow(() -> new IllegalStateException("Could not parse newly created bed block entity."))).getFirst());
                    }
                });
            }
            return !$$8.isEmpty() ? p_274912_.set($$3, $$4x.set($$4, $$8)) : p_274912_;
        }));
    }
}