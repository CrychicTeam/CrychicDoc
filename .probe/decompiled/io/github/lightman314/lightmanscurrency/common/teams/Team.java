package io.github.lightman314.lightmanscurrency.common.teams;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.IBankAccount;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.builtin.TeamBankReference;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.notifications.Notification;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationSaveData;
import io.github.lightman314.lightmanscurrency.api.stats.StatTracker;
import io.github.lightman314.lightmanscurrency.api.teams.ITeam;
import io.github.lightman314.lightmanscurrency.common.bank.BankAccount;
import io.github.lightman314.lightmanscurrency.common.player.LCAdminMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.NonNullSupplier;
import org.jetbrains.annotations.Range;

public class Team implements ITeam {

    public static final int MAX_NAME_LENGTH = 32;

    private final long id;

    PlayerReference owner;

    String teamName;

    private boolean isClient = false;

    List<PlayerReference> admins = new ArrayList();

    List<PlayerReference> members = new ArrayList();

    int bankAccountLimit = 2;

    BankAccount bankAccount = null;

    private final StatTracker statTracker = new StatTracker(this::markDirty, this);

    @Override
    public long getID() {
        return this.id;
    }

    @Nonnull
    @Override
    public PlayerReference getOwner() {
        return this.owner;
    }

    @Nonnull
    @Override
    public String getName() {
        return this.teamName;
    }

    @Override
    public boolean isClient() {
        return this.isClient;
    }

    public Team flagAsClient() {
        this.isClient = true;
        return this;
    }

    @Nonnull
    @Override
    public List<PlayerReference> getAdmins() {
        return ImmutableList.copyOf(this.admins);
    }

    @Nonnull
    @Override
    public List<PlayerReference> getMembers() {
        return ImmutableList.copyOf(this.members);
    }

    @Range(from = 0L, to = 2L)
    public int getBankLimit() {
        return this.bankAccountLimit;
    }

    @Override
    public boolean hasBankAccount() {
        return this.bankAccount != null;
    }

    @Override
    public boolean canAccessBankAccount(@Nonnull Player player) {
        if (this.bankAccountLimit < 1) {
            return this.isMember(player);
        } else {
            return this.bankAccountLimit < 2 ? this.isAdmin(player) : this.isOwner(player);
        }
    }

    @Nullable
    @Override
    public IBankAccount getBankAccount() {
        return this.bankAccount;
    }

    public BankReference getBankReference() {
        return this.hasBankAccount() ? TeamBankReference.of(this.id).flagAsClient(this.isClient) : null;
    }

    @Nonnull
    @Override
    public StatTracker getStats() {
        return this.statTracker;
    }

    @Override
    public boolean isOwner(@Nonnull Player player) {
        return this.owner != null && this.owner.is(player) || LCAdminMode.isAdminPlayer(player);
    }

    @Override
    public boolean isOwner(@Nonnull UUID playerID) {
        return this.owner != null && this.owner.is(playerID);
    }

    @Override
    public boolean isAdmin(@Nonnull Player player) {
        return PlayerReference.isInList(this.admins, player) || this.isOwner(player);
    }

    @Override
    public boolean isAdmin(@Nonnull UUID playerID) {
        return PlayerReference.isInList(this.admins, playerID) || this.isOwner(playerID);
    }

    @Override
    public boolean isMember(@Nonnull Player player) {
        return PlayerReference.isInList(this.members, player) || this.isAdmin(player);
    }

    @Override
    public boolean isMember(@Nonnull UUID playerID) {
        return PlayerReference.isInList(this.members, playerID) || this.isAdmin(playerID);
    }

    public void changeAddMember(Player requestor, String name) {
        if (this.isAdmin(requestor)) {
            PlayerReference player = PlayerReference.of(false, name);
            if (player != null) {
                if (!this.isMember(player.id)) {
                    this.members.add(player);
                    this.markDirty();
                }
            }
        }
    }

    public void changeAddAdmin(Player requestor, String name) {
        if (this.isAdmin(requestor)) {
            PlayerReference player = PlayerReference.of(false, name);
            if (player != null) {
                if (this.isAdmin(player.id)) {
                    if (this.isOwner(player.id)) {
                        return;
                    }
                    if (player.is(requestor) || this.isOwner(requestor)) {
                        PlayerReference.removeFromList(this.admins, player);
                        this.members.add(player);
                        this.markDirty();
                    }
                } else if (this.isOwner(requestor)) {
                    if (this.isMember(player.id)) {
                        PlayerReference.removeFromList(this.members, player);
                    }
                    this.admins.add(player);
                    this.markDirty();
                }
            }
        }
    }

    public void changeRemoveMember(Player requestor, String name) {
        PlayerReference player = PlayerReference.of(false, name);
        if (player != null) {
            if (this.isAdmin(requestor) || player.is(requestor)) {
                if (this.isMember(player.id)) {
                    if (!this.isAdmin(player.id) || this.isOwner(requestor) || PlayerReference.of(requestor).is(player)) {
                        if (!this.isOwner(player.id)) {
                            if (this.isAdmin(player.id)) {
                                PlayerReference.removeFromList(this.admins, player);
                            } else {
                                PlayerReference.removeFromList(this.members, player);
                            }
                            this.markDirty();
                        }
                    }
                }
            }
        }
    }

    public void changeOwner(Player requestor, String name) {
        if (this.isOwner(requestor)) {
            PlayerReference player = PlayerReference.of(false, name);
            if (player != null) {
                if (!this.owner.is(player)) {
                    this.admins.add(this.owner);
                    this.owner = player;
                    PlayerReference.removeFromList(this.admins, player);
                    PlayerReference.removeFromList(this.members, player);
                    this.markDirty();
                }
            }
        }
    }

    public void changeName(Player requestor, String newName) {
        if (this.isAdmin(requestor)) {
            this.teamName = newName;
            if (this.bankAccount != null) {
                this.bankAccount.updateOwnersName(this.teamName);
            }
            this.markDirty();
        }
    }

    public void createBankAccount(Player requestor) {
        if (!this.hasBankAccount() && this.isOwner(requestor)) {
            this.bankAccount = new BankAccount(this::markDirty);
            this.bankAccount.updateOwnersName(this.teamName);
            this.bankAccount.setNotificationConsumer(this::notificationSender);
            this.markDirty();
        }
    }

    private void notificationSender(NonNullSupplier<Notification> notification) {
        List<PlayerReference> sendTo = new ArrayList();
        if (this.bankAccountLimit < 1) {
            sendTo.addAll(this.members);
        }
        if (this.bankAccountLimit < 2) {
            sendTo.addAll(this.admins);
        }
        sendTo.add(this.owner);
        for (PlayerReference player : sendTo) {
            if (player != null && player.id != null) {
                NotificationSaveData.PushNotification(player.id, notification.get());
            }
        }
    }

    public void changeBankLimit(Player requestor, int newLimit) {
        if (this.isOwner(requestor) && this.bankAccountLimit != newLimit) {
            this.bankAccountLimit = newLimit;
            this.markDirty();
        }
    }

    public static int NextBankLimit(int currentLimit) {
        int result = currentLimit - 1;
        if (result < 0) {
            result = 2;
        }
        return result;
    }

    public void clearStats(Player requestor) {
        if (this.isAdmin(requestor)) {
            this.statTracker.clear();
        }
    }

    public final void HandleEditRequest(@Nonnull ServerPlayer requestor, @Nonnull LazyPacketData request) {
        if (request.contains("Disband") && this.isOwner(requestor)) {
            TeamSaveData.RemoveTeam(this.getID());
        }
        if (request.contains("ChangeOwner", (byte) 6)) {
            this.changeOwner(requestor, request.getString("ChangeOwner"));
        }
        if (request.contains("AddMember", (byte) 6)) {
            this.changeAddMember(requestor, request.getString("AddMember"));
        }
        if (request.contains("AddAdmin", (byte) 6)) {
            this.changeAddAdmin(requestor, request.getString("AddAdmin"));
        }
        if (request.contains("RemoveMember", (byte) 6)) {
            this.changeRemoveMember(requestor, request.getString("RemoveMember"));
        }
        if (request.contains("ChangeName", (byte) 6)) {
            this.changeName(requestor, request.getString("ChangeName"));
        }
        if (request.contains("CreateBankAccount")) {
            this.createBankAccount(requestor);
        }
        if (request.contains("ChangeBankLimit", (byte) 2)) {
            this.changeBankLimit(requestor, request.getInt("ChangeBankLimit"));
        }
        if (request.contains("ClearStats")) {
            this.clearStats(requestor);
        }
    }

    private Team(long teamID, @Nonnull PlayerReference owner, @Nonnull String name) {
        this.id = teamID;
        this.owner = owner;
        this.teamName = name;
    }

    public void markDirty() {
        if (!this.isClient) {
            TeamSaveData.MarkTeamDirty(this.id);
        }
    }

    @Nonnull
    public CompoundTag save() {
        CompoundTag compound = new CompoundTag();
        compound.putLong("ID", this.id);
        if (this.owner != null) {
            compound.put("Owner", this.owner.save());
        }
        compound.putString("Name", this.teamName);
        PlayerReference.saveList(compound, this.members, "Members");
        PlayerReference.saveList(compound, this.admins, "Admins");
        if (this.bankAccount != null) {
            compound.put("BankAccount", this.bankAccount.save());
            compound.putInt("BankLimit", this.bankAccountLimit);
        }
        compound.put("Stats", this.statTracker.save());
        return compound;
    }

    public static Team load(@Nonnull CompoundTag compound) {
        PlayerReference owner = null;
        long id = -1L;
        if (compound.contains("ID")) {
            id = compound.getLong("ID");
        }
        if (compound.contains("Owner", 10)) {
            owner = PlayerReference.load(compound.getCompound("Owner"));
        }
        String name = compound.getString("Name");
        if (owner != null) {
            Team team = of(id, owner, name);
            team.admins = PlayerReference.loadList(compound, "Admins");
            team.members = PlayerReference.loadList(compound, "Members");
            if (compound.contains("BankAccount", 10)) {
                team.bankAccount = new BankAccount(team::markDirty, compound.getCompound("BankAccount"));
                if (compound.contains("BankLimit", 3)) {
                    team.bankAccountLimit = compound.getInt("BankLimit");
                }
                team.bankAccount.updateOwnersName(team.teamName);
                team.bankAccount.setNotificationConsumer(team::notificationSender);
            }
            if (compound.contains("Stats")) {
                team.statTracker.load(compound.getCompound("Stats"));
            }
            return team;
        } else {
            return null;
        }
    }

    public static Team of(long id, @Nonnull PlayerReference owner, @Nonnull String name) {
        return new Team(id, owner, name);
    }

    public static Comparator<ITeam> sorterFor(Player player) {
        return new Team.TeamSorter(player);
    }

    private static record TeamSorter(Player player) implements Comparator<ITeam> {

        public int compare(ITeam o1, ITeam o2) {
            if (o1.isOwner(this.player) && !o2.isOwner(this.player)) {
                return -1;
            } else if (!o1.isOwner(this.player) && o2.isOwner(this.player)) {
                return 1;
            } else if (o1.isAdmin(this.player) && !o2.isAdmin(this.player)) {
                return -1;
            } else {
                return !o1.isAdmin(this.player) && o2.isAdmin(this.player) ? 1 : o1.getName().compareToIgnoreCase(o2.getName());
            }
        }
    }
}