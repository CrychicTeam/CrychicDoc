package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EntityRidingToPassengersFix extends DataFix {

    public EntityRidingToPassengersFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    public TypeRewriteRule makeRule() {
        Schema $$0 = this.getInputSchema();
        Schema $$1 = this.getOutputSchema();
        Type<?> $$2 = $$0.getTypeRaw(References.ENTITY_TREE);
        Type<?> $$3 = $$1.getTypeRaw(References.ENTITY_TREE);
        Type<?> $$4 = $$0.getTypeRaw(References.ENTITY);
        return this.cap($$0, $$1, $$2, $$3, $$4);
    }

    private <OldEntityTree, NewEntityTree, Entity> TypeRewriteRule cap(Schema schema0, Schema schema1, Type<OldEntityTree> typeOldEntityTree2, Type<NewEntityTree> typeNewEntityTree3, Type<Entity> typeEntity4) {
        Type<Pair<String, Pair<Either<OldEntityTree, Unit>, Entity>>> $$5 = DSL.named(References.ENTITY_TREE.typeName(), DSL.and(DSL.optional(DSL.field("Riding", typeOldEntityTree2)), typeEntity4));
        Type<Pair<String, Pair<Either<List<NewEntityTree>, Unit>, Entity>>> $$6 = DSL.named(References.ENTITY_TREE.typeName(), DSL.and(DSL.optional(DSL.field("Passengers", DSL.list(typeNewEntityTree3))), typeEntity4));
        Type<?> $$7 = schema0.getType(References.ENTITY_TREE);
        Type<?> $$8 = schema1.getType(References.ENTITY_TREE);
        if (!Objects.equals($$7, $$5)) {
            throw new IllegalStateException("Old entity type is not what was expected.");
        } else if (!$$8.equals($$6, true, true)) {
            throw new IllegalStateException("New entity type is not what was expected.");
        } else {
            OpticFinder<Pair<String, Pair<Either<OldEntityTree, Unit>, Entity>>> $$9 = DSL.typeFinder($$5);
            OpticFinder<Pair<String, Pair<Either<List<NewEntityTree>, Unit>, Entity>>> $$10 = DSL.typeFinder($$6);
            OpticFinder<NewEntityTree> $$11 = DSL.typeFinder(typeNewEntityTree3);
            Type<?> $$12 = schema0.getType(References.PLAYER);
            Type<?> $$13 = schema1.getType(References.PLAYER);
            return TypeRewriteRule.seq(this.fixTypeEverywhere("EntityRidingToPassengerFix", $$5, $$6, p_15653_ -> p_145320_ -> {
                Optional<Pair<String, Pair<Either<List<NewEntityTree>, Unit>, Entity>>> $$7x = Optional.empty();
                Pair<String, Pair<Either<OldEntityTree, Unit>, Entity>> $$8x = p_145320_;
                while (true) {
                    Either<List<NewEntityTree>, Unit> $$9x = (Either<List<NewEntityTree>, Unit>) DataFixUtils.orElse($$7x.map(p_145326_ -> {
                        Typed<NewEntityTree> $$5x = (Typed<NewEntityTree>) typeNewEntityTree3.pointTyped(p_15653_).orElseThrow(() -> new IllegalStateException("Could not create new entity tree"));
                        NewEntityTree $$6x = (NewEntityTree) $$5x.set($$10, p_145326_).getOptional($$11).orElseThrow(() -> new IllegalStateException("Should always have an entity tree here"));
                        return Either.left(ImmutableList.of($$6x));
                    }), Either.right(DSL.unit()));
                    $$7x = Optional.of(Pair.of(References.ENTITY_TREE.typeName(), Pair.of($$9x, ((Pair) $$8x.getSecond()).getSecond())));
                    Optional<OldEntityTree> $$10x = ((Either) ((Pair) $$8x.getSecond()).getFirst()).left();
                    if (!$$10x.isPresent()) {
                        return (Pair) $$7x.orElseThrow(() -> new IllegalStateException("Should always have an entity tree here"));
                    }
                    $$8x = (Pair<String, Pair<Either<OldEntityTree, Unit>, Entity>>) new Typed(typeOldEntityTree2, p_15653_, $$10x.get()).getOptional($$9).orElseThrow(() -> new IllegalStateException("Should always have an entity here"));
                }
            }), this.writeAndRead("player RootVehicle injecter", $$12, $$13));
        }
    }
}