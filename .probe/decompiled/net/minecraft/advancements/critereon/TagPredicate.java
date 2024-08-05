package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;

public class TagPredicate<T> {

    private final TagKey<T> tag;

    private final boolean expected;

    public TagPredicate(TagKey<T> tagKeyT0, boolean boolean1) {
        this.tag = tagKeyT0;
        this.expected = boolean1;
    }

    public static <T> TagPredicate<T> is(TagKey<T> tagKeyT0) {
        return new TagPredicate<>(tagKeyT0, true);
    }

    public static <T> TagPredicate<T> isNot(TagKey<T> tagKeyT0) {
        return new TagPredicate<>(tagKeyT0, false);
    }

    public boolean matches(Holder<T> holderT0) {
        return holderT0.is(this.tag) == this.expected;
    }

    public JsonElement serializeToJson() {
        JsonObject $$0 = new JsonObject();
        $$0.addProperty("id", this.tag.location().toString());
        $$0.addProperty("expected", this.expected);
        return $$0;
    }

    public static <T> TagPredicate<T> fromJson(@Nullable JsonElement jsonElement0, ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT1) {
        if (jsonElement0 == null) {
            throw new JsonParseException("Expected a tag predicate");
        } else {
            JsonObject $$2 = GsonHelper.convertToJsonObject(jsonElement0, "Tag Predicate");
            ResourceLocation $$3 = new ResourceLocation(GsonHelper.getAsString($$2, "id"));
            boolean $$4 = GsonHelper.getAsBoolean($$2, "expected");
            return new TagPredicate<>(TagKey.create(resourceKeyExtendsRegistryT1, $$3), $$4);
        }
    }
}