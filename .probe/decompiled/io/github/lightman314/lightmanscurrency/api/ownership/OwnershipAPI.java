package io.github.lightman314.lightmanscurrency.api.ownership;

import io.github.lightman314.lightmanscurrency.api.ownership.listing.IPotentialOwnerProvider;
import io.github.lightman314.lightmanscurrency.api.ownership.listing.PotentialOwner;
import io.github.lightman314.lightmanscurrency.common.impl.OwnershipAPIImpl;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public abstract class OwnershipAPI {

    public static final OwnershipAPI API = OwnershipAPIImpl.INSTANCE;

    public abstract void registerOwnerType(@Nonnull OwnerType var1);

    @Nullable
    public abstract OwnerType getOwnerType(@Nonnull ResourceLocation var1);

    public abstract void registerPotentialOwnerProvider(@Nonnull IPotentialOwnerProvider var1);

    public abstract List<PotentialOwner> getPotentialOwners(@Nonnull Player var1);
}