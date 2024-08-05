package io.github.lightman314.lightmanscurrency.api.taxes;

import io.github.lightman314.lightmanscurrency.api.misc.player.OwnerData;
import io.github.lightman314.lightmanscurrency.api.misc.world.WorldArea;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.common.util.IClientTracker;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

public interface ITaxCollector extends IClientTracker {

    long getID();

    boolean isServerEntry();

    @Nonnull
    WorldArea getArea();

    int getTaxRate();

    @Nonnull
    MutableComponent getName();

    @Nonnull
    OwnerData getOwner();

    boolean canAccess(@Nonnull Player var1);

    boolean ShouldTax(@Nonnull ITaxable var1);

    boolean IsInArea(@Nonnull ITaxable var1);

    void AcceptTaxable(@Nonnull ITaxable var1);

    void TaxableWasRemoved(@Nonnull ITaxable var1);

    @Nonnull
    MoneyValue CalculateAndPayTaxes(@Nonnull ITaxable var1, @Nonnull MoneyValue var2);
}