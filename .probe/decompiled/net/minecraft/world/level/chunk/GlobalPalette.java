package net.minecraft.world.level.chunk;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.IdMap;
import net.minecraft.network.FriendlyByteBuf;

public class GlobalPalette<T> implements Palette<T> {

    private final IdMap<T> registry;

    public GlobalPalette(IdMap<T> idMapT0) {
        this.registry = idMapT0;
    }

    public static <A> Palette<A> create(int int0, IdMap<A> idMapA1, PaletteResize<A> paletteResizeA2, List<A> listA3) {
        return new GlobalPalette<>(idMapA1);
    }

    @Override
    public int idFor(T t0) {
        int $$1 = this.registry.getId(t0);
        return $$1 == -1 ? 0 : $$1;
    }

    @Override
    public boolean maybeHas(Predicate<T> predicateT0) {
        return true;
    }

    @Override
    public T valueFor(int int0) {
        T $$1 = this.registry.byId(int0);
        if ($$1 == null) {
            throw new MissingPaletteEntryException(int0);
        } else {
            return $$1;
        }
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf0) {
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
    }

    @Override
    public int getSerializedSize() {
        return FriendlyByteBuf.getVarIntSize(0);
    }

    @Override
    public int getSize() {
        return this.registry.size();
    }

    @Override
    public Palette<T> copy() {
        return this;
    }
}