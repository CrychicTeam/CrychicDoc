package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;

public class FilteredSignsFix extends NamedEntityFix {

    public FilteredSignsFix(Schema schema0) {
        super(schema0, false, "Remove filtered text from signs", References.BLOCK_ENTITY, "minecraft:sign");
    }

    @Override
    protected Typed<?> fix(Typed<?> typed0) {
        return typed0.update(DSL.remainderFinder(), p_216670_ -> p_216670_.remove("FilteredText1").remove("FilteredText2").remove("FilteredText3").remove("FilteredText4"));
    }
}