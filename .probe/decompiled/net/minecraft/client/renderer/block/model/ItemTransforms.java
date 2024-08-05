package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.world.item.ItemDisplayContext;

public class ItemTransforms {

    public static final ItemTransforms NO_TRANSFORMS = new ItemTransforms();

    public final ItemTransform thirdPersonLeftHand;

    public final ItemTransform thirdPersonRightHand;

    public final ItemTransform firstPersonLeftHand;

    public final ItemTransform firstPersonRightHand;

    public final ItemTransform head;

    public final ItemTransform gui;

    public final ItemTransform ground;

    public final ItemTransform fixed;

    private ItemTransforms() {
        this(ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM);
    }

    public ItemTransforms(ItemTransforms itemTransforms0) {
        this.thirdPersonLeftHand = itemTransforms0.thirdPersonLeftHand;
        this.thirdPersonRightHand = itemTransforms0.thirdPersonRightHand;
        this.firstPersonLeftHand = itemTransforms0.firstPersonLeftHand;
        this.firstPersonRightHand = itemTransforms0.firstPersonRightHand;
        this.head = itemTransforms0.head;
        this.gui = itemTransforms0.gui;
        this.ground = itemTransforms0.ground;
        this.fixed = itemTransforms0.fixed;
    }

    public ItemTransforms(ItemTransform itemTransform0, ItemTransform itemTransform1, ItemTransform itemTransform2, ItemTransform itemTransform3, ItemTransform itemTransform4, ItemTransform itemTransform5, ItemTransform itemTransform6, ItemTransform itemTransform7) {
        this.thirdPersonLeftHand = itemTransform0;
        this.thirdPersonRightHand = itemTransform1;
        this.firstPersonLeftHand = itemTransform2;
        this.firstPersonRightHand = itemTransform3;
        this.head = itemTransform4;
        this.gui = itemTransform5;
        this.ground = itemTransform6;
        this.fixed = itemTransform7;
    }

    public ItemTransform getTransform(ItemDisplayContext itemDisplayContext0) {
        return switch(itemDisplayContext0) {
            case THIRD_PERSON_LEFT_HAND ->
                this.thirdPersonLeftHand;
            case THIRD_PERSON_RIGHT_HAND ->
                this.thirdPersonRightHand;
            case FIRST_PERSON_LEFT_HAND ->
                this.firstPersonLeftHand;
            case FIRST_PERSON_RIGHT_HAND ->
                this.firstPersonRightHand;
            case HEAD ->
                this.head;
            case GUI ->
                this.gui;
            case GROUND ->
                this.ground;
            case FIXED ->
                this.fixed;
            default ->
                ItemTransform.NO_TRANSFORM;
        };
    }

    public boolean hasTransform(ItemDisplayContext itemDisplayContext0) {
        return this.getTransform(itemDisplayContext0) != ItemTransform.NO_TRANSFORM;
    }

    protected static class Deserializer implements JsonDeserializer<ItemTransforms> {

        public ItemTransforms deserialize(JsonElement jsonElement0, Type type1, JsonDeserializationContext jsonDeserializationContext2) throws JsonParseException {
            JsonObject $$3 = jsonElement0.getAsJsonObject();
            ItemTransform $$4 = this.getTransform(jsonDeserializationContext2, $$3, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND);
            ItemTransform $$5 = this.getTransform(jsonDeserializationContext2, $$3, ItemDisplayContext.THIRD_PERSON_LEFT_HAND);
            if ($$5 == ItemTransform.NO_TRANSFORM) {
                $$5 = $$4;
            }
            ItemTransform $$6 = this.getTransform(jsonDeserializationContext2, $$3, ItemDisplayContext.FIRST_PERSON_RIGHT_HAND);
            ItemTransform $$7 = this.getTransform(jsonDeserializationContext2, $$3, ItemDisplayContext.FIRST_PERSON_LEFT_HAND);
            if ($$7 == ItemTransform.NO_TRANSFORM) {
                $$7 = $$6;
            }
            ItemTransform $$8 = this.getTransform(jsonDeserializationContext2, $$3, ItemDisplayContext.HEAD);
            ItemTransform $$9 = this.getTransform(jsonDeserializationContext2, $$3, ItemDisplayContext.GUI);
            ItemTransform $$10 = this.getTransform(jsonDeserializationContext2, $$3, ItemDisplayContext.GROUND);
            ItemTransform $$11 = this.getTransform(jsonDeserializationContext2, $$3, ItemDisplayContext.FIXED);
            return new ItemTransforms($$5, $$4, $$7, $$6, $$8, $$9, $$10, $$11);
        }

        private ItemTransform getTransform(JsonDeserializationContext jsonDeserializationContext0, JsonObject jsonObject1, ItemDisplayContext itemDisplayContext2) {
            String $$3 = itemDisplayContext2.getSerializedName();
            return jsonObject1.has($$3) ? (ItemTransform) jsonDeserializationContext0.deserialize(jsonObject1.get($$3), ItemTransform.class) : ItemTransform.NO_TRANSFORM;
        }
    }
}