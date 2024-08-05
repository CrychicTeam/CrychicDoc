package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import java.util.Optional;
import java.util.stream.Stream;

public class MobSpawnerEntityIdentifiersFix extends DataFix {

    public MobSpawnerEntityIdentifiersFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    private Dynamic<?> fix(Dynamic<?> dynamic0) {
        if (!"MobSpawner".equals(dynamic0.get("id").asString(""))) {
            return dynamic0;
        } else {
            Optional<String> $$1 = dynamic0.get("EntityId").asString().result();
            if ($$1.isPresent()) {
                Dynamic<?> $$2 = (Dynamic<?>) DataFixUtils.orElse(dynamic0.get("SpawnData").result(), dynamic0.emptyMap());
                $$2 = $$2.set("id", $$2.createString(((String) $$1.get()).isEmpty() ? "Pig" : (String) $$1.get()));
                dynamic0 = dynamic0.set("SpawnData", $$2);
                dynamic0 = dynamic0.remove("EntityId");
            }
            Optional<? extends Stream<? extends Dynamic<?>>> $$3 = dynamic0.get("SpawnPotentials").asStreamOpt().result();
            if ($$3.isPresent()) {
                dynamic0 = dynamic0.set("SpawnPotentials", dynamic0.createList(((Stream) $$3.get()).map(p_16459_ -> {
                    Optional<String> $$1x = p_16459_.get("Type").asString().result();
                    if ($$1x.isPresent()) {
                        Dynamic<?> $$2 = ((Dynamic) DataFixUtils.orElse(p_16459_.get("Properties").result(), p_16459_.emptyMap())).set("id", p_16459_.createString((String) $$1x.get()));
                        return p_16459_.set("Entity", $$2).remove("Type").remove("Properties");
                    } else {
                        return p_16459_;
                    }
                })));
            }
            return dynamic0;
        }
    }

    public TypeRewriteRule makeRule() {
        Type<?> $$0 = this.getOutputSchema().getType(References.UNTAGGED_SPAWNER);
        return this.fixTypeEverywhereTyped("MobSpawnerEntityIdentifiersFix", this.getInputSchema().getType(References.UNTAGGED_SPAWNER), $$0, p_16455_ -> {
            Dynamic<?> $$2 = (Dynamic<?>) p_16455_.get(DSL.remainderFinder());
            $$2 = $$2.set("id", $$2.createString("MobSpawner"));
            DataResult<? extends Pair<? extends Typed<?>, ?>> $$3 = $$0.readTyped(this.fix($$2));
            return !$$3.result().isPresent() ? p_16455_ : (Typed) ((Pair) $$3.result().get()).getFirst();
        });
    }
}