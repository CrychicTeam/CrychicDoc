package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL.TypeReference;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.Const.PrimitiveType;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import net.minecraft.resources.ResourceLocation;

public class NamespacedSchema extends Schema {

    public static final PrimitiveCodec<String> NAMESPACED_STRING_CODEC = new PrimitiveCodec<String>() {

        public <T> DataResult<String> read(DynamicOps<T> p_17321_, T p_17322_) {
            return p_17321_.getStringValue(p_17322_).map(NamespacedSchema::m_17311_);
        }

        public <T> T write(DynamicOps<T> p_17318_, String p_17319_) {
            return (T) p_17318_.createString(p_17319_);
        }

        public String toString() {
            return "NamespacedString";
        }
    };

    private static final Type<String> NAMESPACED_STRING = new PrimitiveType(NAMESPACED_STRING_CODEC);

    public NamespacedSchema(int int0, Schema schema1) {
        super(int0, schema1);
    }

    public static String ensureNamespaced(String string0) {
        ResourceLocation $$1 = ResourceLocation.tryParse(string0);
        return $$1 != null ? $$1.toString() : string0;
    }

    public static Type<String> namespacedString() {
        return NAMESPACED_STRING;
    }

    public Type<?> getChoiceType(TypeReference typeReference0, String string1) {
        return super.getChoiceType(typeReference0, ensureNamespaced(string1));
    }
}