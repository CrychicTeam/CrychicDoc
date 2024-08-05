package net.minecraft.world.level.chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.IdMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;

public class HashMapPalette<T> implements Palette<T> {

    private final IdMap<T> registry;

    private final CrudeIncrementalIntIdentityHashBiMap<T> values;

    private final PaletteResize<T> resizeHandler;

    private final int bits;

    public HashMapPalette(IdMap<T> idMapT0, int int1, PaletteResize<T> paletteResizeT2, List<T> listT3) {
        this(idMapT0, int1, paletteResizeT2);
        listT3.forEach(this.values::m_13569_);
    }

    public HashMapPalette(IdMap<T> idMapT0, int int1, PaletteResize<T> paletteResizeT2) {
        this(idMapT0, int1, paletteResizeT2, CrudeIncrementalIntIdentityHashBiMap.create(1 << int1));
    }

    private HashMapPalette(IdMap<T> idMapT0, int int1, PaletteResize<T> paletteResizeT2, CrudeIncrementalIntIdentityHashBiMap<T> crudeIncrementalIntIdentityHashBiMapT3) {
        this.registry = idMapT0;
        this.bits = int1;
        this.resizeHandler = paletteResizeT2;
        this.values = crudeIncrementalIntIdentityHashBiMapT3;
    }

    public static <A> Palette<A> create(int int0, IdMap<A> idMapA1, PaletteResize<A> paletteResizeA2, List<A> listA3) {
        return new HashMapPalette<>(idMapA1, int0, paletteResizeA2, listA3);
    }

    @Override
    public int idFor(T t0) {
        int $$1 = this.values.getId(t0);
        if ($$1 == -1) {
            $$1 = this.values.add(t0);
            if ($$1 >= 1 << this.bits) {
                $$1 = this.resizeHandler.onResize(this.bits + 1, t0);
            }
        }
        return $$1;
    }

    @Override
    public boolean maybeHas(Predicate<T> predicateT0) {
        for (int $$1 = 0; $$1 < this.getSize(); $$1++) {
            if (predicateT0.test(this.values.byId($$1))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public T valueFor(int int0) {
        T $$1 = this.values.byId(int0);
        if ($$1 == null) {
            throw new MissingPaletteEntryException(int0);
        } else {
            return $$1;
        }
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf0) {
        this.values.clear();
        int $$1 = friendlyByteBuf0.readVarInt();
        for (int $$2 = 0; $$2 < $$1; $$2++) {
            this.values.add(this.registry.byIdOrThrow(friendlyByteBuf0.readVarInt()));
        }
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        int $$1 = this.getSize();
        friendlyByteBuf0.writeVarInt($$1);
        for (int $$2 = 0; $$2 < $$1; $$2++) {
            friendlyByteBuf0.writeVarInt(this.registry.getId(this.values.byId($$2)));
        }
    }

    @Override
    public int getSerializedSize() {
        int $$0 = FriendlyByteBuf.getVarIntSize(this.getSize());
        for (int $$1 = 0; $$1 < this.getSize(); $$1++) {
            $$0 += FriendlyByteBuf.getVarIntSize(this.registry.getId(this.values.byId($$1)));
        }
        return $$0;
    }

    public List<T> getEntries() {
        ArrayList<T> $$0 = new ArrayList();
        this.values.iterator().forEachRemaining($$0::add);
        return $$0;
    }

    @Override
    public int getSize() {
        return this.values.size();
    }

    @Override
    public Palette<T> copy() {
        return new HashMapPalette<>(this.registry, this.bits, this.resizeHandler, this.values.copy());
    }
}