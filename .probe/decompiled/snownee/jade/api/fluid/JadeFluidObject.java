package snownee.jade.api.fluid;

import java.util.Objects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class JadeFluidObject {

    private final Fluid type;

    private final long amount;

    @Nullable
    private final CompoundTag tag;

    public static long bucketVolume() {
        return 1000L;
    }

    public static long blockVolume() {
        return 1000L;
    }

    public static JadeFluidObject empty() {
        return of(Fluids.EMPTY, 0L);
    }

    public static JadeFluidObject of(Fluid fluid) {
        return of(fluid, blockVolume());
    }

    public static JadeFluidObject of(Fluid fluid, long amount) {
        return of(fluid, amount, null);
    }

    public static JadeFluidObject of(Fluid fluid, long amount, CompoundTag tag) {
        return new JadeFluidObject(fluid, amount, tag);
    }

    private JadeFluidObject(Fluid type, long amount, @Nullable CompoundTag tag) {
        this.type = type;
        this.amount = amount;
        this.tag = tag;
        Objects.requireNonNull(type);
    }

    public Fluid getType() {
        return this.type;
    }

    public long getAmount() {
        return this.amount;
    }

    @Nullable
    public CompoundTag getTag() {
        return this.tag;
    }

    public boolean isEmpty() {
        return this.getType() == Fluids.EMPTY || this.getAmount() == 0L;
    }
}