package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;

public abstract class SimpleEntityRenameFix extends EntityRenameFix {

    public SimpleEntityRenameFix(String string0, Schema schema1, boolean boolean2) {
        super(string0, schema1, boolean2);
    }

    @Override
    protected Pair<String, Typed<?>> fix(String string0, Typed<?> typed1) {
        Pair<String, Dynamic<?>> $$2 = this.getNewNameAndTag(string0, (Dynamic<?>) typed1.getOrCreate(DSL.remainderFinder()));
        return Pair.of((String) $$2.getFirst(), typed1.set(DSL.remainderFinder(), (Dynamic) $$2.getSecond()));
    }

    protected abstract Pair<String, Dynamic<?>> getNewNameAndTag(String var1, Dynamic<?> var2);
}