package dev.shadowsoffire.placebo.util;

import java.util.function.Function;
import java.util.function.ToIntFunction;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public final class CachedObject<T> {

    public static final int HAS_NEVER_BEEN_INITIALIZED = -2;

    public static final int EMPTY_NBT = -1;

    protected final ResourceLocation id;

    protected final Function<ItemStack, T> deserializer;

    protected final ToIntFunction<ItemStack> hasher;

    protected volatile T data = (T) null;

    protected volatile int lastNbtHash = -2;

    public CachedObject(ResourceLocation id, Function<ItemStack, T> deserializer, ToIntFunction<ItemStack> hasher) {
        this.id = id;
        this.deserializer = deserializer;
        this.hasher = hasher;
    }

    public CachedObject(ResourceLocation id, Function<ItemStack, T> deserializer) {
        this(id, deserializer, CachedObject::defaultHash);
    }

    @Nullable
    public T get(ItemStack stack) {
        if (this.lastNbtHash == -2) {
            this.compute(stack);
            return this.data;
        } else {
            if (this.hasher.applyAsInt(stack) != this.lastNbtHash) {
                this.compute(stack);
            }
            return this.data;
        }
    }

    public void reset() {
        this.data = null;
        this.lastNbtHash = -2;
    }

    protected void compute(ItemStack stack) {
        synchronized (this) {
            this.data = (T) this.deserializer.apply(stack);
            this.lastNbtHash = this.hasher.applyAsInt(stack);
        }
    }

    public static int defaultHash(ItemStack stack) {
        return stack.hasTag() ? stack.getTag().hashCode() : -1;
    }

    public static ToIntFunction<ItemStack> hashSubkey(String subkey) {
        return stack -> stack.getTagElement(subkey) != null ? stack.getTagElement(subkey).hashCode() : -1;
    }

    public interface CachedObjectSource {

        <T> T getOrCreate(ResourceLocation var1, Function<ItemStack, T> var2, ToIntFunction<ItemStack> var3);

        default <T> T getOrCreate(ResourceLocation id, Function<ItemStack, T> deserializer) {
            return this.getOrCreate(id, deserializer, CachedObject::defaultHash);
        }

        static <T> T getOrCreate(ItemStack stack, ResourceLocation id, Function<ItemStack, T> deserializer, ToIntFunction<ItemStack> hasher) {
            return ((CachedObject.CachedObjectSource) stack).getOrCreate(id, deserializer, hasher);
        }

        static <T> T getOrCreate(ItemStack stack, ResourceLocation id, Function<ItemStack, T> deserializer) {
            return ((CachedObject.CachedObjectSource) stack).getOrCreate(id, deserializer);
        }
    }
}