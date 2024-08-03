package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;

public class GossipUUIDFix extends NamedEntityFix {

    public GossipUUIDFix(Schema schema0, String string1) {
        super(schema0, false, "Gossip for for " + string1, References.ENTITY, string1);
    }

    @Override
    protected Typed<?> fix(Typed<?> typed0) {
        return typed0.update(DSL.remainderFinder(), p_15883_ -> p_15883_.update("Gossips", p_145376_ -> (Dynamic) DataFixUtils.orElse(p_145376_.asStreamOpt().result().map(p_145374_ -> p_145374_.map(p_145378_ -> (Dynamic) AbstractUUIDFix.replaceUUIDLeastMost(p_145378_, "Target", "Target").orElse(p_145378_))).map(p_145376_::createList), p_145376_)));
    }
}