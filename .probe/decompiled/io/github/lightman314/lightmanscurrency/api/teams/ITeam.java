package io.github.lightman314.lightmanscurrency.api.teams;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.IBankAccount;
import io.github.lightman314.lightmanscurrency.api.stats.StatTracker;
import io.github.lightman314.lightmanscurrency.common.util.IClientTracker;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;

public interface ITeam extends IClientTracker {

    long getID();

    @Nonnull
    String getName();

    @Nonnull
    StatTracker getStats();

    @Nonnull
    PlayerReference getOwner();

    @Nonnull
    List<PlayerReference> getAdmins();

    @Nonnull
    List<PlayerReference> getMembers();

    @Nonnull
    default List<PlayerReference> getAdminsAndOwner() {
        List<PlayerReference> result = new ArrayList(this.getAdmins());
        result.add(this.getOwner());
        return ImmutableList.copyOf(result);
    }

    @Nonnull
    default List<PlayerReference> getAllMembers() {
        List<PlayerReference> result = new ArrayList();
        result.addAll(this.getMembers());
        result.addAll(this.getAdmins());
        result.add(this.getOwner());
        return ImmutableList.copyOf(result);
    }

    default int getMemberCount() {
        return this.getMembers().size() + this.getAdmins().size() + 1;
    }

    boolean hasBankAccount();

    boolean canAccessBankAccount(@Nonnull Player var1);

    @Nullable
    IBankAccount getBankAccount();

    boolean isOwner(@Nonnull Player var1);

    default boolean isOwner(@Nonnull PlayerReference player) {
        return this.isOwner(player.id);
    }

    boolean isOwner(@Nonnull UUID var1);

    boolean isAdmin(@Nonnull Player var1);

    default boolean isAdmin(@Nonnull PlayerReference player) {
        return this.isAdmin(player.id);
    }

    boolean isAdmin(@Nonnull UUID var1);

    boolean isMember(@Nonnull Player var1);

    default boolean isMember(@Nonnull PlayerReference player) {
        return this.isMember(player.id);
    }

    boolean isMember(@Nonnull UUID var1);
}