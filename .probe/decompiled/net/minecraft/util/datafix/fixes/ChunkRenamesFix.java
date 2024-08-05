package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import java.util.function.Function;

public class ChunkRenamesFix extends DataFix {

    public ChunkRenamesFix(Schema schema0) {
        super(schema0, true);
    }

    protected TypeRewriteRule makeRule() {
        Type<?> $$0 = this.getInputSchema().getType(References.CHUNK);
        OpticFinder<?> $$1 = $$0.findField("Level");
        OpticFinder<?> $$2 = $$1.type().findField("Structures");
        Type<?> $$3 = this.getOutputSchema().getType(References.CHUNK);
        Type<?> $$4 = $$3.findFieldType("structures");
        return this.fixTypeEverywhereTyped("Chunk Renames; purge Level-tag", $$0, $$3, p_199427_ -> {
            Typed<?> $$4x = p_199427_.getTyped($$1);
            Typed<?> $$5 = appendChunkName($$4x);
            $$5 = $$5.set(DSL.remainderFinder(), mergeRemainders(p_199427_, (Dynamic) $$4x.get(DSL.remainderFinder())));
            $$5 = renameField($$5, "TileEntities", "block_entities");
            $$5 = renameField($$5, "TileTicks", "block_ticks");
            $$5 = renameField($$5, "Entities", "entities");
            $$5 = renameField($$5, "Sections", "sections");
            $$5 = $$5.updateTyped($$2, $$4, p_185128_ -> renameField(p_185128_, "Starts", "starts"));
            $$5 = renameField($$5, "Structures", "structures");
            return $$5.update(DSL.remainderFinder(), p_199429_ -> p_199429_.remove("Level"));
        });
    }

    private static Typed<?> renameField(Typed<?> typed0, String string1, String string2) {
        return renameFieldHelper(typed0, string1, string2, typed0.getType().findFieldType(string1)).update(DSL.remainderFinder(), p_199439_ -> p_199439_.remove(string1));
    }

    private static <A> Typed<?> renameFieldHelper(Typed<?> typed0, String string1, String string2, Type<A> typeA3) {
        Type<Either<A, Unit>> $$4 = DSL.optional(DSL.field(string1, typeA3));
        Type<Either<A, Unit>> $$5 = DSL.optional(DSL.field(string2, typeA3));
        return typed0.update($$4.finder(), $$5, Function.identity());
    }

    private static <A> Typed<Pair<String, A>> appendChunkName(Typed<A> typedA0) {
        return new Typed(DSL.named("chunk", typedA0.getType()), typedA0.getOps(), Pair.of("chunk", typedA0.getValue()));
    }

    private static <T> Dynamic<T> mergeRemainders(Typed<?> typed0, Dynamic<T> dynamicT1) {
        DynamicOps<T> $$2 = dynamicT1.getOps();
        Dynamic<T> $$3 = ((Dynamic) typed0.get(DSL.remainderFinder())).convert($$2);
        DataResult<T> $$4 = $$2.getMap(dynamicT1.getValue()).flatMap(p_199433_ -> $$2.mergeToMap($$3.getValue(), p_199433_));
        return (Dynamic<T>) $$4.result().map(p_199436_ -> new Dynamic($$2, p_199436_)).orElse(dynamicT1);
    }
}