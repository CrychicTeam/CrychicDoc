package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;

public class OptionsRenameFieldFix extends DataFix {

    private final String fixName;

    private final String fieldFrom;

    private final String fieldTo;

    public OptionsRenameFieldFix(Schema schema0, boolean boolean1, String string2, String string3, String string4) {
        super(schema0, boolean1);
        this.fixName = string2;
        this.fieldFrom = string3;
        this.fieldTo = string4;
    }

    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped(this.fixName, this.getInputSchema().getType(References.OPTIONS), p_16676_ -> p_16676_.update(DSL.remainderFinder(), p_145592_ -> (Dynamic) DataFixUtils.orElse(p_145592_.get(this.fieldFrom).result().map(p_145595_ -> p_145592_.set(this.fieldTo, p_145595_).remove(this.fieldFrom)), p_145592_)));
    }
}