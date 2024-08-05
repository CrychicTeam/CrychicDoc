package io.github.lightman314.lightmanscurrency.api.ownership.listing;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.api.misc.player.OwnerData;
import io.github.lightman314.lightmanscurrency.api.ownership.Owner;
import io.github.lightman314.lightmanscurrency.api.ownership.OwnershipAPI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Player;

public class PotentialOwnerList {

    private final Player player;

    private final Supplier<OwnerData> currentOwner;

    private Owner oldOwner;

    private String lastSearch = "";

    private List<PotentialOwner> allOwners = null;

    private List<PotentialOwner> cache = new ArrayList();

    public PotentialOwnerList(@Nonnull Player player, @Nonnull Supplier<OwnerData> currentOwner) {
        this.player = player;
        this.currentOwner = currentOwner;
        this.updateCache("");
    }

    public void tick() {
        OwnerData data = (OwnerData) this.currentOwner.get();
        if (data != null) {
            if (this.oldOwner == null || !this.oldOwner.matches(data.getValidOwner())) {
                this.updateCache(this.lastSearch);
            }
        }
    }

    public void updateCache(@Nonnull String searchFilter) {
        if (this.allOwners == null) {
            this.allOwners = ImmutableList.copyOf(OwnershipAPI.API.getPotentialOwners(this.player));
        }
        this.lastSearch = searchFilter;
        List<PotentialOwner> temp = new ArrayList(this.allOwners);
        OwnerData data = (OwnerData) this.currentOwner.get();
        if (data != null) {
            this.oldOwner = data.getValidOwner();
            Owner owner = this.oldOwner;
            temp.forEach(po -> po.setAsCurrentOwner(po.asOwner().matches(owner)));
            temp.sort(Comparator.comparingInt(PotentialOwner::sortingPriority));
            if (!searchFilter.isBlank()) {
                temp.removeIf(po -> po.failedFilter(searchFilter));
            }
            this.cache = ImmutableList.copyOf(temp);
        }
    }

    @Nonnull
    public List<PotentialOwner> getOwners() {
        return this.cache;
    }
}