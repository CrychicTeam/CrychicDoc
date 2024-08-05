package io.github.lightman314.lightmanscurrency.api.taxes;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.taxes.reference.TaxReferenceType;
import io.github.lightman314.lightmanscurrency.common.taxes.TaxSaveData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;

public final class TaxAPI {

    private static final Map<ResourceLocation, TaxReferenceType> REFERENCE_TYPES = new HashMap();

    private TaxAPI() {
    }

    public static void registerReferenceType(@Nonnull TaxReferenceType type) {
        ResourceLocation id = type.typeID;
        if (REFERENCE_TYPES.containsKey(id)) {
            LightmansCurrency.LogWarning("Attempted to register the TaxReferenceType '" + id + "' twice!");
        } else {
            REFERENCE_TYPES.put(id, type);
            LightmansCurrency.LogDebug("Registered TaxReferenceType '" + id + "'!");
        }
    }

    @Nullable
    public static TaxReferenceType getReferenceType(@Nonnull ResourceLocation type) {
        return (TaxReferenceType) REFERENCE_TYPES.get(type);
    }

    @Nonnull
    public static List<ITaxCollector> GetActiveTaxCollectorsFor(@Nonnull ITaxable taxable) {
        return TaxSaveData.GetAllTaxEntries(taxable.isClient()).stream().filter(e -> e.ShouldTax(taxable)).map(e -> e).toList();
    }

    @Nonnull
    public static List<ITaxCollector> GetPossibleTaxCollectorsFor(@Nonnull ITaxable taxable) {
        return TaxSaveData.GetAllTaxEntries(taxable.isClient()).stream().filter(e -> e.IsInArea(taxable)).map(e -> e).toList();
    }
}