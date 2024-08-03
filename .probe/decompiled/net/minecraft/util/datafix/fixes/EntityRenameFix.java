package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.TaggedChoice.TaggedChoiceType;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DynamicOps;
import java.util.Locale;

public abstract class EntityRenameFix extends DataFix {

    protected final String name;

    public EntityRenameFix(String string0, Schema schema1, boolean boolean2) {
        super(schema1, boolean2);
        this.name = string0;
    }

    public TypeRewriteRule makeRule() {
        TaggedChoiceType<String> $$0 = this.getInputSchema().findChoiceType(References.ENTITY);
        TaggedChoiceType<String> $$1 = this.getOutputSchema().findChoiceType(References.ENTITY);
        return this.fixTypeEverywhere(this.name, $$0, $$1, p_15624_ -> p_145311_ -> {
            String $$4 = (String) p_145311_.getFirst();
            Type<?> $$5 = (Type<?>) $$0.types().get($$4);
            Pair<String, Typed<?>> $$6 = this.fix($$4, this.getEntity(p_145311_.getSecond(), p_15624_, $$5));
            Type<?> $$7 = (Type<?>) $$1.types().get($$6.getFirst());
            if (!$$7.equals(((Typed) $$6.getSecond()).getType(), true, true)) {
                throw new IllegalStateException(String.format(Locale.ROOT, "Dynamic type check failed: %s not equal to %s", $$7, ((Typed) $$6.getSecond()).getType()));
            } else {
                return Pair.of((String) $$6.getFirst(), ((Typed) $$6.getSecond()).getValue());
            }
        });
    }

    private <A> Typed<A> getEntity(Object object0, DynamicOps<?> dynamicOps1, Type<A> typeA2) {
        return new Typed(typeA2, dynamicOps1, object0);
    }

    protected abstract Pair<String, Typed<?>> fix(String var1, Typed<?> var2);
}