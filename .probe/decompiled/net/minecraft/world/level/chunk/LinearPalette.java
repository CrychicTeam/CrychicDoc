package net.minecraft.world.level.chunk;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.IdMap;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.commons.lang3.Validate;

public class LinearPalette<T> implements Palette<T> {

    private final IdMap<T> registry;

    private final T[] values;

    private final PaletteResize<T> resizeHandler;

    private final int bits;

    private int size;

    private LinearPalette(IdMap<T> idMapT0, int int1, PaletteResize<T> paletteResizeT2, List<T> listT3) {
        this.registry = idMapT0;
        this.values = (T[]) (new Object[1 << int1]);
        this.bits = int1;
        this.resizeHandler = paletteResizeT2;
        Validate.isTrue(listT3.size() <= this.values.length, "Can't initialize LinearPalette of size %d with %d entries", new Object[] { this.values.length, listT3.size() });
        for (int $$4 = 0; $$4 < listT3.size(); $$4++) {
            this.values[$$4] = (T) listT3.get($$4);
        }
        this.size = listT3.size();
    }

    private LinearPalette(IdMap<T> idMapT0, T[] t1, PaletteResize<T> paletteResizeT2, int int3, int int4) {
        this.registry = idMapT0;
        this.values = t1;
        this.resizeHandler = paletteResizeT2;
        this.bits = int3;
        this.size = int4;
    }

    public static <A> Palette<A> create(int int0, IdMap<A> idMapA1, PaletteResize<A> paletteResizeA2, List<A> listA3) {
        return new LinearPalette<>(idMapA1, int0, paletteResizeA2, listA3);
    }

    @Override
    public int idFor(T t0) {
        for (int $$1 = 0; $$1 < this.size; $$1++) {
            if (this.values[$$1] == t0) {
                return $$1;
            }
        }
        int $$2 = this.size;
        if ($$2 < this.values.length) {
            this.values[$$2] = t0;
            this.size++;
            return $$2;
        } else {
            return this.resizeHandler.onResize(this.bits + 1, t0);
        }
    }

    @Override
    public boolean maybeHas(Predicate<T> predicateT0) {
        for (int $$1 = 0; $$1 < this.size; $$1++) {
            if (predicateT0.test(this.values[$$1])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public T valueFor(int int0) {
        if (int0 >= 0 && int0 < this.size) {
            return this.values[int0];
        } else {
            throw new MissingPaletteEntryException(int0);
        }
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf0) {
        this.size = friendlyByteBuf0.readVarInt();
        for (int $$1 = 0; $$1 < this.size; $$1++) {
            this.values[$$1] = this.registry.byIdOrThrow(friendlyByteBuf0.readVarInt());
        }
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.size);
        for (int $$1 = 0; $$1 < this.size; $$1++) {
            friendlyByteBuf0.writeVarInt(this.registry.getId(this.values[$$1]));
        }
    }

    @Override
    public int getSerializedSize() {
        int $$0 = FriendlyByteBuf.getVarIntSize(this.getSize());
        for (int $$1 = 0; $$1 < this.getSize(); $$1++) {
            $$0 += FriendlyByteBuf.getVarIntSize(this.registry.getId(this.values[$$1]));
        }
        return $$0;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public Palette<T> copy() {
        return new LinearPalette<>(this.registry, (T[]) ((Object[]) this.values.clone()), this.resizeHandler, this.bits, this.size);
    }
}