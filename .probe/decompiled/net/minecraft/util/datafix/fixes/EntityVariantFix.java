package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.DSL.TypeReference;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import java.util.function.Function;
import java.util.function.IntFunction;

public class EntityVariantFix extends NamedEntityFix {

    private final String fieldName;

    private final IntFunction<String> idConversions;

    public EntityVariantFix(Schema schema0, String string1, TypeReference typeReference2, String string3, String string4, IntFunction<String> intFunctionString5) {
        super(schema0, false, string1, typeReference2, string3);
        this.fieldName = string4;
        this.idConversions = intFunctionString5;
    }

    private static <T> Dynamic<T> updateAndRename(Dynamic<T> dynamicT0, String string1, String string2, Function<Dynamic<T>, Dynamic<T>> functionDynamicTDynamicT3) {
        return dynamicT0.map(p_216646_ -> {
            DynamicOps<T> $$5 = dynamicT0.getOps();
            Function<T, T> $$6 = p_216656_ -> ((Dynamic) functionDynamicTDynamicT3.apply(new Dynamic($$5, p_216656_))).getValue();
            return $$5.get(p_216646_, string1).map(p_216652_ -> $$5.set(p_216646_, string2, $$6.apply(p_216652_))).result().orElse(p_216646_);
        });
    }

    @Override
    protected Typed<?> fix(Typed<?> typed0) {
        return typed0.update(DSL.remainderFinder(), p_216632_ -> updateAndRename(p_216632_, this.fieldName, "variant", p_216658_ -> (Dynamic) DataFixUtils.orElse(p_216658_.asNumber().map(p_216635_ -> p_216658_.createString((String) this.idConversions.apply(p_216635_.intValue()))).result(), p_216658_)));
    }
}