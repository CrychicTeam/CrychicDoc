package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Lists;
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
import com.mojang.serialization.Dynamic;
import java.util.List;
import java.util.Optional;

public class FurnaceRecipeFix extends DataFix {

    public FurnaceRecipeFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    protected TypeRewriteRule makeRule() {
        return this.cap(this.getOutputSchema().getTypeRaw(References.RECIPE));
    }

    private <R> TypeRewriteRule cap(Type<R> typeR0) {
        Type<Pair<Either<Pair<List<Pair<R, Integer>>, Dynamic<?>>, Unit>, Dynamic<?>>> $$1 = DSL.and(DSL.optional(DSL.field("RecipesUsed", DSL.and(DSL.compoundList(typeR0, DSL.intType()), DSL.remainderType()))), DSL.remainderType());
        OpticFinder<?> $$2 = DSL.namedChoice("minecraft:furnace", this.getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:furnace"));
        OpticFinder<?> $$3 = DSL.namedChoice("minecraft:blast_furnace", this.getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:blast_furnace"));
        OpticFinder<?> $$4 = DSL.namedChoice("minecraft:smoker", this.getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:smoker"));
        Type<?> $$5 = this.getOutputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:furnace");
        Type<?> $$6 = this.getOutputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:blast_furnace");
        Type<?> $$7 = this.getOutputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:smoker");
        Type<?> $$8 = this.getInputSchema().getType(References.BLOCK_ENTITY);
        Type<?> $$9 = this.getOutputSchema().getType(References.BLOCK_ENTITY);
        return this.fixTypeEverywhereTyped("FurnaceRecipesFix", $$8, $$9, p_15848_ -> p_15848_.updateTyped($$2, $$5, p_145372_ -> this.updateFurnaceContents(typeR0, $$1, p_145372_)).updateTyped($$3, $$6, p_145368_ -> this.updateFurnaceContents(typeR0, $$1, p_145368_)).updateTyped($$4, $$7, p_145364_ -> this.updateFurnaceContents(typeR0, $$1, p_145364_)));
    }

    private <R> Typed<?> updateFurnaceContents(Type<R> typeR0, Type<Pair<Either<Pair<List<Pair<R, Integer>>, Dynamic<?>>, Unit>, Dynamic<?>>> typePairEitherPairListPairRIntegerDynamicUnitDynamic1, Typed<?> typed2) {
        Dynamic<?> $$3 = (Dynamic<?>) typed2.getOrCreate(DSL.remainderFinder());
        int $$4 = $$3.get("RecipesUsedSize").asInt(0);
        $$3 = $$3.remove("RecipesUsedSize");
        List<Pair<R, Integer>> $$5 = Lists.newArrayList();
        for (int $$6 = 0; $$6 < $$4; $$6++) {
            String $$7 = "RecipeLocation" + $$6;
            String $$8 = "RecipeAmount" + $$6;
            Optional<? extends Dynamic<?>> $$9 = $$3.get($$7).result();
            int $$10 = $$3.get($$8).asInt(0);
            if ($$10 > 0) {
                $$9.ifPresent(p_15859_ -> {
                    Optional<? extends Pair<R, ? extends Dynamic<?>>> $$4x = typeR0.read(p_15859_).result();
                    $$4x.ifPresent(p_145360_ -> $$5.add(Pair.of(p_145360_.getFirst(), $$10)));
                });
            }
            $$3 = $$3.remove($$7).remove($$8);
        }
        return typed2.set(DSL.remainderFinder(), typePairEitherPairListPairRIntegerDynamicUnitDynamic1, Pair.of(Either.left(Pair.of($$5, $$3.emptyMap())), $$3));
    }
}