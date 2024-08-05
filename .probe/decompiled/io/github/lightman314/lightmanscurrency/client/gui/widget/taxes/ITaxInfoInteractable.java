package io.github.lightman314.lightmanscurrency.client.gui.widget.taxes;

import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import javax.annotation.Nullable;

public interface ITaxInfoInteractable {

    @Nullable
    TraderData getTrader();

    boolean canPlayerForceIgnore();

    void AcceptTaxCollector(long var1);

    void ForceIgnoreTaxCollector(long var1);

    void PardonIgnoredTaxCollector(long var1);
}