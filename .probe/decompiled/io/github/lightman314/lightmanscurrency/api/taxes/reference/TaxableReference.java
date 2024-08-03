package io.github.lightman314.lightmanscurrency.api.taxes.reference;

import io.github.lightman314.lightmanscurrency.api.taxes.ITaxable;
import io.github.lightman314.lightmanscurrency.api.taxes.TaxAPI;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public abstract class TaxableReference {

    public final TaxReferenceType type;

    protected TaxableReference(@Nonnull TaxReferenceType type) {
        this.type = type;
    }

    @Nullable
    public abstract ITaxable getTaxable(boolean var1);

    public final boolean stillValid(boolean isClient) {
        return this.getTaxable(isClient) != null;
    }

    @Nonnull
    public final CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        tag.putString("Type", this.type.typeID.toString());
        return tag;
    }

    protected abstract void saveAdditional(@Nonnull CompoundTag var1);

    @Nullable
    public static TaxableReference load(@Nonnull CompoundTag tag) {
        ResourceLocation type = new ResourceLocation(tag.getString("Type"));
        TaxReferenceType t = TaxAPI.getReferenceType(type);
        return t != null ? t.load(tag) : null;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj instanceof TaxableReference reference ? this.matches(reference) : false;
        }
    }

    protected abstract boolean matches(@Nonnull TaxableReference var1);
}