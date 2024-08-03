package io.github.lightman314.lightmanscurrency.api.ownership.listing;

import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Player;

public interface IPotentialOwnerProvider {

    @Nonnull
    List<PotentialOwner> collectPotentialOwners(@Nonnull Player var1);
}