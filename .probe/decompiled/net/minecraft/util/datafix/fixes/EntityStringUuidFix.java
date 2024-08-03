package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import java.util.Optional;
import java.util.UUID;

public class EntityStringUuidFix extends DataFix {

    public EntityStringUuidFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("EntityStringUuidFix", this.getInputSchema().getType(References.ENTITY), p_15697_ -> p_15697_.update(DSL.remainderFinder(), p_145331_ -> {
            Optional<String> $$1 = p_145331_.get("UUID").asString().result();
            if ($$1.isPresent()) {
                UUID $$2 = UUID.fromString((String) $$1.get());
                return p_145331_.remove("UUID").set("UUIDMost", p_145331_.createLong($$2.getMostSignificantBits())).set("UUIDLeast", p_145331_.createLong($$2.getLeastSignificantBits()));
            } else {
                return p_145331_;
            }
        }));
    }
}