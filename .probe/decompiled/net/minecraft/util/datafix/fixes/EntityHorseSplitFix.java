package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import java.util.Objects;

public class EntityHorseSplitFix extends EntityRenameFix {

    public EntityHorseSplitFix(Schema schema0, boolean boolean1) {
        super("EntityHorseSplitFix", schema0, boolean1);
    }

    @Override
    protected Pair<String, Typed<?>> fix(String string0, Typed<?> typed1) {
        Dynamic<?> $$2 = (Dynamic<?>) typed1.get(DSL.remainderFinder());
        if (Objects.equals("EntityHorse", string0)) {
            int $$3 = $$2.get("Type").asInt(0);
            String $$8 = switch($$3) {
                default ->
                    "Horse";
                case 1 ->
                    "Donkey";
                case 2 ->
                    "Mule";
                case 3 ->
                    "ZombieHorse";
                case 4 ->
                    "SkeletonHorse";
            };
            $$2.remove("Type");
            Type<?> $$9 = (Type<?>) this.getOutputSchema().findChoiceType(References.ENTITY).types().get($$8);
            return Pair.of($$8, (Typed) ((Pair) typed1.write().flatMap($$9::readTyped).result().orElseThrow(() -> new IllegalStateException("Could not parse the new horse"))).getFirst());
        } else {
            return Pair.of(string0, typed1);
        }
    }
}