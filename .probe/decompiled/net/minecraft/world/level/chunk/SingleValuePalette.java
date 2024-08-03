package net.minecraft.world.level.chunk;

import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.IdMap;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.commons.lang3.Validate;

public class SingleValuePalette<T> implements Palette<T> {

    private final IdMap<T> registry;

    @Nullable
    private T value;

    private final PaletteResize<T> resizeHandler;

    public SingleValuePalette(IdMap<T> idMapT0, PaletteResize<T> paletteResizeT1, List<T> listT2) {
        this.registry = idMapT0;
        this.resizeHandler = paletteResizeT1;
        if (listT2.size() > 0) {
            Validate.isTrue(listT2.size() <= 1, "Can't initialize SingleValuePalette with %d values.", (long) listT2.size());
            this.value = (T) listT2.get(0);
        }
    }

    public static <A> Palette<A> create(int int0, IdMap<A> idMapA1, PaletteResize<A> paletteResizeA2, List<A> listA3) {
        return new SingleValuePalette<>(idMapA1, paletteResizeA2, listA3);
    }

    @Override
    public int idFor(T t0) {
        if (this.value != null && this.value != t0) {
            return this.resizeHandler.onResize(1, t0);
        } else {
            this.value = t0;
            return 0;
        }
    }

    @Override
    public boolean maybeHas(Predicate<T> predicateT0) {
        if (this.value == null) {
            throw new IllegalStateException("Use of an uninitialized palette");
        } else {
            return predicateT0.test(this.value);
        }
    }

    @Override
    public T valueFor(int int0) {
        if (this.value != null && int0 == 0) {
            return this.value;
        } else {
            throw new IllegalStateException("Missing Palette entry for id " + int0 + ".");
        }
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf0) {
        this.value = this.registry.byIdOrThrow(friendlyByteBuf0.readVarInt());
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        if (this.value == null) {
            throw new IllegalStateException("Use of an uninitialized palette");
        } else {
            friendlyByteBuf0.writeVarInt(this.registry.getId(this.value));
        }
    }

    @Override
    public int getSerializedSize() {
        if (this.value == null) {
            throw new IllegalStateException("Use of an uninitialized palette");
        } else {
            return FriendlyByteBuf.getVarIntSize(this.registry.getId(this.value));
        }
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public Palette<T> copy() {
        if (this.value == null) {
            throw new IllegalStateException("Use of an uninitialized palette");
        } else {
            return this;
        }
    }
}