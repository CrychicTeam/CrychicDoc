package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EntityVariantPredicate<V> {

    private static final String VARIANT_KEY = "variant";

    final Codec<V> variantCodec;

    final Function<Entity, Optional<V>> getter;

    final EntitySubPredicate.Type type;

    public static <V> EntityVariantPredicate<V> create(Registry<V> registryV0, Function<Entity, Optional<V>> functionEntityOptionalV1) {
        return new EntityVariantPredicate<>(registryV0.byNameCodec(), functionEntityOptionalV1);
    }

    public static <V> EntityVariantPredicate<V> create(Codec<V> codecV0, Function<Entity, Optional<V>> functionEntityOptionalV1) {
        return new EntityVariantPredicate<>(codecV0, functionEntityOptionalV1);
    }

    private EntityVariantPredicate(Codec<V> codecV0, Function<Entity, Optional<V>> functionEntityOptionalV1) {
        this.variantCodec = codecV0;
        this.getter = functionEntityOptionalV1;
        this.type = p_262519_ -> {
            JsonElement $$2 = p_262519_.get("variant");
            if ($$2 == null) {
                throw new JsonParseException("Missing variant field");
            } else {
                V $$3 = (V) Util.getOrThrow(codecV0.decode(new Dynamic(JsonOps.INSTANCE, $$2)), JsonParseException::new).getFirst();
                return this.createPredicate($$3);
            }
        };
    }

    public EntitySubPredicate.Type type() {
        return this.type;
    }

    public EntitySubPredicate createPredicate(final V v0) {
        return new EntitySubPredicate() {

            @Override
            public boolean matches(Entity p_219105_, ServerLevel p_219106_, @Nullable Vec3 p_219107_) {
                return ((Optional) EntityVariantPredicate.this.getter.apply(p_219105_)).filter(p_219110_ -> p_219110_.equals(v0)).isPresent();
            }

            @Override
            public JsonObject serializeCustomData() {
                JsonObject $$0 = new JsonObject();
                $$0.add("variant", Util.getOrThrow(EntityVariantPredicate.this.variantCodec.encodeStart(JsonOps.INSTANCE, v0), p_262521_ -> new JsonParseException("Can't serialize variant " + v0 + ", message " + p_262521_)));
                return $$0;
            }

            @Override
            public EntitySubPredicate.Type type() {
                return EntityVariantPredicate.this.type;
            }
        };
    }
}