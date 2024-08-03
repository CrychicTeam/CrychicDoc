package snownee.kiwi;

import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class KiwiGO<T> implements Supplier<T> {

    private Supplier<T> factory;

    private ResourceLocation key;

    private T value;

    public KiwiGO(Supplier<T> factory) {
        this.factory = factory;
    }

    public T get() {
        Objects.requireNonNull(this.value);
        return this.value;
    }

    public T getOrCreate() {
        if (this.value == null) {
            this.value = (T) this.factory.get();
            this.factory = null;
        }
        return this.get();
    }

    public void setKey(ResourceLocation key) {
        Objects.requireNonNull(key);
        if (this.key != null) {
            throw new IllegalStateException("Key already set");
        } else {
            this.key = key;
        }
    }

    public boolean is(Object value) {
        return Objects.equals(this.value, value);
    }

    public boolean is(ItemStack stack) {
        return stack.is(((ItemLike) this.value).asItem());
    }

    public boolean is(BlockState state) {
        return state.m_60713_((Block) this.value);
    }

    public BlockState defaultBlockState() {
        return ((Block) this.value).defaultBlockState();
    }

    public ItemStack itemStack() {
        return this.itemStack(1);
    }

    public ItemStack itemStack(int amount) {
        ItemStack stack = ((ItemLike) this.value).asItem().getDefaultInstance();
        if (!stack.isEmpty()) {
            stack.setCount(amount);
        }
        return stack;
    }

    public ResourceLocation key() {
        return this.key;
    }

    @Nullable
    public Object registry() {
        return Kiwi.registryLookup.findRegistry(this.value);
    }

    public static class RegistrySpecified<T> extends KiwiGO<T> {

        final Supplier<Object> registry;

        public RegistrySpecified(Supplier<T> factory, Supplier<Object> registry) {
            super(factory);
            this.registry = registry;
        }

        @Override
        public Object registry() {
            return this.registry.get();
        }
    }
}