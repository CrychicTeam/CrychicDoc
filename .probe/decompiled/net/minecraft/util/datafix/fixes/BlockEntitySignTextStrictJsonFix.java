package net.minecraft.util.datafix.fixes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import java.lang.reflect.Type;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.GsonHelper;
import org.apache.commons.lang3.StringUtils;

public class BlockEntitySignTextStrictJsonFix extends NamedEntityFix {

    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(Component.class, new JsonDeserializer<Component>() {

        public MutableComponent deserialize(JsonElement p_14875_, Type p_14876_, JsonDeserializationContext p_14877_) throws JsonParseException {
            if (p_14875_.isJsonPrimitive()) {
                return Component.literal(p_14875_.getAsString());
            } else if (p_14875_.isJsonArray()) {
                JsonArray $$3 = p_14875_.getAsJsonArray();
                MutableComponent $$4 = null;
                for (JsonElement $$5 : $$3) {
                    MutableComponent $$6 = this.deserialize($$5, $$5.getClass(), p_14877_);
                    if ($$4 == null) {
                        $$4 = $$6;
                    } else {
                        $$4.append($$6);
                    }
                }
                return $$4;
            } else {
                throw new JsonParseException("Don't know how to turn " + p_14875_ + " into a Component");
            }
        }
    }).create();

    public BlockEntitySignTextStrictJsonFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1, "BlockEntitySignTextStrictJsonFix", References.BLOCK_ENTITY, "Sign");
    }

    private Dynamic<?> updateLine(Dynamic<?> dynamic0, String string1) {
        String $$2 = dynamic0.get(string1).asString("");
        Component $$3 = null;
        if (!"null".equals($$2) && !StringUtils.isEmpty($$2)) {
            if ($$2.charAt(0) == '"' && $$2.charAt($$2.length() - 1) == '"' || $$2.charAt(0) == '{' && $$2.charAt($$2.length() - 1) == '}') {
                try {
                    $$3 = GsonHelper.fromNullableJson(GSON, $$2, Component.class, true);
                    if ($$3 == null) {
                        $$3 = CommonComponents.EMPTY;
                    }
                } catch (Exception var8) {
                }
                if ($$3 == null) {
                    try {
                        $$3 = Component.Serializer.fromJson($$2);
                    } catch (Exception var7) {
                    }
                }
                if ($$3 == null) {
                    try {
                        $$3 = Component.Serializer.fromJsonLenient($$2);
                    } catch (Exception var6) {
                    }
                }
                if ($$3 == null) {
                    $$3 = Component.literal($$2);
                }
            } else {
                $$3 = Component.literal($$2);
            }
        } else {
            $$3 = CommonComponents.EMPTY;
        }
        return dynamic0.set(string1, dynamic0.createString(Component.Serializer.toJson($$3)));
    }

    @Override
    protected Typed<?> fix(Typed<?> typed0) {
        return typed0.update(DSL.remainderFinder(), p_14869_ -> {
            p_14869_ = this.updateLine(p_14869_, "Text1");
            p_14869_ = this.updateLine(p_14869_, "Text2");
            p_14869_ = this.updateLine(p_14869_, "Text3");
            return this.updateLine(p_14869_, "Text4");
        });
    }
}