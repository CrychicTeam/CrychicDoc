package io.github.lightman314.lightmanscurrency.api.taxes;

import io.github.lightman314.lightmanscurrency.api.misc.world.WorldPosition;
import io.github.lightman314.lightmanscurrency.api.notifications.Notification;
import io.github.lightman314.lightmanscurrency.api.taxes.reference.TaxableReference;
import io.github.lightman314.lightmanscurrency.common.util.IClientTracker;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.common.util.NonNullSupplier;

public interface ITaxable extends IClientTracker {

    @Nonnull
    MutableComponent getName();

    @Nonnull
    TaxableReference getReference();

    @Nonnull
    WorldPosition getWorldPosition();

    void pushNotification(@Nonnull NonNullSupplier<Notification> var1);
}