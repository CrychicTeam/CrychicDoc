package com.simibubi.create.content.equipment.potatoCannon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.registries.ForgeRegistries;

public class PotatoCannonProjectileType {

    private List<Supplier<Item>> items = new ArrayList();

    private int reloadTicks = 10;

    private int damage = 1;

    private int split = 1;

    private float knockback = 1.0F;

    private float drag = 0.99F;

    private float velocityMultiplier = 1.0F;

    private float gravityMultiplier = 1.0F;

    private float soundPitch = 1.0F;

    private boolean sticky = false;

    private PotatoProjectileRenderMode renderMode = PotatoProjectileRenderMode.Billboard.INSTANCE;

    private Predicate<EntityHitResult> preEntityHit = e -> false;

    private Predicate<EntityHitResult> onEntityHit = e -> false;

    private BiPredicate<LevelAccessor, BlockHitResult> onBlockHit = (w, ray) -> false;

    protected PotatoCannonProjectileType() {
    }

    public List<Supplier<Item>> getItems() {
        return this.items;
    }

    public int getReloadTicks() {
        return this.reloadTicks;
    }

    public int getDamage() {
        return this.damage;
    }

    public int getSplit() {
        return this.split;
    }

    public float getKnockback() {
        return this.knockback;
    }

    public float getDrag() {
        return this.drag;
    }

    public float getVelocityMultiplier() {
        return this.velocityMultiplier;
    }

    public float getGravityMultiplier() {
        return this.gravityMultiplier;
    }

    public float getSoundPitch() {
        return this.soundPitch;
    }

    public boolean isSticky() {
        return this.sticky;
    }

    public PotatoProjectileRenderMode getRenderMode() {
        return this.renderMode;
    }

    public boolean preEntityHit(EntityHitResult ray) {
        return this.preEntityHit.test(ray);
    }

    public boolean onEntityHit(EntityHitResult ray) {
        return this.onEntityHit.test(ray);
    }

    public boolean onBlockHit(LevelAccessor world, BlockHitResult ray) {
        return this.onBlockHit.test(world, ray);
    }

    public static PotatoCannonProjectileType fromJson(JsonObject object) {
        PotatoCannonProjectileType type = new PotatoCannonProjectileType();
        try {
            JsonElement itemsElement = object.get("items");
            if (itemsElement != null && itemsElement.isJsonArray()) {
                for (JsonElement element : itemsElement.getAsJsonArray()) {
                    if (element.isJsonPrimitive()) {
                        JsonPrimitive primitive = element.getAsJsonPrimitive();
                        if (primitive.isString()) {
                            try {
                                Optional<Holder.Reference<Item>> reference = ForgeRegistries.ITEMS.getDelegate(new ResourceLocation(primitive.getAsString()));
                                if (reference.isPresent()) {
                                    type.items.add((Supplier) reference.get());
                                }
                            } catch (ResourceLocationException var7) {
                            }
                        }
                    }
                }
            }
            parseJsonPrimitive(object, "reload_ticks", JsonPrimitive::isNumber, primitivex -> type.reloadTicks = primitivex.getAsInt());
            parseJsonPrimitive(object, "damage", JsonPrimitive::isNumber, primitivex -> type.damage = primitivex.getAsInt());
            parseJsonPrimitive(object, "split", JsonPrimitive::isNumber, primitivex -> type.split = primitivex.getAsInt());
            parseJsonPrimitive(object, "knockback", JsonPrimitive::isNumber, primitivex -> type.knockback = primitivex.getAsFloat());
            parseJsonPrimitive(object, "drag", JsonPrimitive::isNumber, primitivex -> type.drag = primitivex.getAsFloat());
            parseJsonPrimitive(object, "velocity_multiplier", JsonPrimitive::isNumber, primitivex -> type.velocityMultiplier = primitivex.getAsFloat());
            parseJsonPrimitive(object, "gravity_multiplier", JsonPrimitive::isNumber, primitivex -> type.gravityMultiplier = primitivex.getAsFloat());
            parseJsonPrimitive(object, "sound_pitch", JsonPrimitive::isNumber, primitivex -> type.soundPitch = primitivex.getAsFloat());
            parseJsonPrimitive(object, "sticky", JsonPrimitive::isBoolean, primitivex -> type.sticky = primitivex.getAsBoolean());
        } catch (Exception var8) {
        }
        return type;
    }

    private static void parseJsonPrimitive(JsonObject object, String key, Predicate<JsonPrimitive> predicate, Consumer<JsonPrimitive> consumer) {
        JsonElement element = object.get(key);
        if (element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (predicate.test(primitive)) {
                consumer.accept(primitive);
            }
        }
    }

    public static void toBuffer(PotatoCannonProjectileType type, FriendlyByteBuf buffer) {
        buffer.writeVarInt(type.items.size());
        for (Supplier<Item> delegate : type.items) {
            buffer.writeResourceLocation(RegisteredObjects.getKeyOrThrow((Item) delegate.get()));
        }
        buffer.writeInt(type.reloadTicks);
        buffer.writeInt(type.damage);
        buffer.writeInt(type.split);
        buffer.writeFloat(type.knockback);
        buffer.writeFloat(type.drag);
        buffer.writeFloat(type.velocityMultiplier);
        buffer.writeFloat(type.gravityMultiplier);
        buffer.writeFloat(type.soundPitch);
        buffer.writeBoolean(type.sticky);
    }

    public static PotatoCannonProjectileType fromBuffer(FriendlyByteBuf buffer) {
        PotatoCannonProjectileType type = new PotatoCannonProjectileType();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++) {
            Optional<Holder.Reference<Item>> reference = ForgeRegistries.ITEMS.getDelegate(buffer.readResourceLocation());
            if (reference.isPresent()) {
                type.items.add((Supplier) reference.get());
            }
        }
        type.reloadTicks = buffer.readInt();
        type.damage = buffer.readInt();
        type.split = buffer.readInt();
        type.knockback = buffer.readFloat();
        type.drag = buffer.readFloat();
        type.velocityMultiplier = buffer.readFloat();
        type.gravityMultiplier = buffer.readFloat();
        type.soundPitch = buffer.readFloat();
        type.sticky = buffer.readBoolean();
        return type;
    }

    public static class Builder {

        protected ResourceLocation id;

        protected PotatoCannonProjectileType result;

        public Builder(ResourceLocation id) {
            this.id = id;
            this.result = new PotatoCannonProjectileType();
        }

        public PotatoCannonProjectileType.Builder reloadTicks(int reload) {
            this.result.reloadTicks = reload;
            return this;
        }

        public PotatoCannonProjectileType.Builder damage(int damage) {
            this.result.damage = damage;
            return this;
        }

        public PotatoCannonProjectileType.Builder splitInto(int split) {
            this.result.split = split;
            return this;
        }

        public PotatoCannonProjectileType.Builder knockback(float knockback) {
            this.result.knockback = knockback;
            return this;
        }

        public PotatoCannonProjectileType.Builder drag(float drag) {
            this.result.drag = drag;
            return this;
        }

        public PotatoCannonProjectileType.Builder velocity(float velocity) {
            this.result.velocityMultiplier = velocity;
            return this;
        }

        public PotatoCannonProjectileType.Builder gravity(float modifier) {
            this.result.gravityMultiplier = modifier;
            return this;
        }

        public PotatoCannonProjectileType.Builder soundPitch(float pitch) {
            this.result.soundPitch = pitch;
            return this;
        }

        public PotatoCannonProjectileType.Builder sticky() {
            this.result.sticky = true;
            return this;
        }

        public PotatoCannonProjectileType.Builder renderMode(PotatoProjectileRenderMode renderMode) {
            this.result.renderMode = renderMode;
            return this;
        }

        public PotatoCannonProjectileType.Builder renderBillboard() {
            this.renderMode(PotatoProjectileRenderMode.Billboard.INSTANCE);
            return this;
        }

        public PotatoCannonProjectileType.Builder renderTumbling() {
            this.renderMode(PotatoProjectileRenderMode.Tumble.INSTANCE);
            return this;
        }

        public PotatoCannonProjectileType.Builder renderTowardMotion(int spriteAngle, float spin) {
            this.renderMode(new PotatoProjectileRenderMode.TowardMotion(spriteAngle, spin));
            return this;
        }

        public PotatoCannonProjectileType.Builder preEntityHit(Predicate<EntityHitResult> callback) {
            this.result.preEntityHit = callback;
            return this;
        }

        public PotatoCannonProjectileType.Builder onEntityHit(Predicate<EntityHitResult> callback) {
            this.result.onEntityHit = callback;
            return this;
        }

        public PotatoCannonProjectileType.Builder onBlockHit(BiPredicate<LevelAccessor, BlockHitResult> callback) {
            this.result.onBlockHit = callback;
            return this;
        }

        public PotatoCannonProjectileType.Builder addItems(ItemLike... items) {
            for (ItemLike provider : items) {
                this.result.items.add(ForgeRegistries.ITEMS.getDelegateOrThrow(provider.asItem()));
            }
            return this;
        }

        public PotatoCannonProjectileType register() {
            PotatoProjectileTypeManager.registerBuiltinType(this.id, this.result);
            return this.result;
        }

        public PotatoCannonProjectileType registerAndAssign(ItemLike... items) {
            this.addItems(items);
            this.register();
            return this.result;
        }
    }
}