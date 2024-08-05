package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;

public class EntityGoatMissingStateFix extends NamedEntityFix {

    public EntityGoatMissingStateFix(Schema schema0) {
        super(schema0, false, "EntityGoatMissingStateFix", References.ENTITY, "minecraft:goat");
    }

    @Override
    protected Typed<?> fix(Typed<?> typed0) {
        return typed0.update(DSL.remainderFinder(), p_238370_ -> p_238370_.set("HasLeftHorn", p_238370_.createBoolean(true)).set("HasRightHorn", p_238370_.createBoolean(true)));
    }
}