package io.github.lightman314.lightmanscurrency.api.money.bank.reference.builtin;

import io.github.lightman314.lightmanscurrency.api.money.bank.IBankAccount;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReferenceType;
import io.github.lightman314.lightmanscurrency.api.teams.ITeam;
import io.github.lightman314.lightmanscurrency.api.teams.TeamAPI;
import io.github.lightman314.lightmanscurrency.common.teams.TeamSaveData;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class TeamBankReference extends BankReference {

    public static final BankReferenceType TYPE = new TeamBankReference.Type();

    public final long teamID;

    protected TeamBankReference(long teamID) {
        super(TYPE);
        this.teamID = teamID;
    }

    public static BankReference of(long teamID) {
        return new TeamBankReference(teamID);
    }

    public static BankReference of(@Nonnull ITeam team) {
        return new TeamBankReference(team.getID());
    }

    @Nullable
    @Override
    public IBankAccount get() {
        ITeam team = TeamAPI.getTeam(this.isClient(), this.teamID);
        return team != null ? team.getBankAccount() : null;
    }

    @Override
    public boolean allowedAccess(@Nonnull Player player) {
        ITeam team = TeamSaveData.GetTeam(this.isClient(), this.teamID);
        return team != null ? team.canAccessBankAccount(player) : false;
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag) {
        tag.putLong("TeamID", this.teamID);
    }

    @Override
    protected void encodeAdditional(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeLong(this.teamID);
    }

    private static class Type extends BankReferenceType {

        protected Type() {
            super(new ResourceLocation("lightmanscurrency", "team_account"));
        }

        @Override
        public BankReference load(CompoundTag tag) {
            return TeamBankReference.of(tag.getLong("TeamID"));
        }

        @Override
        public BankReference decode(FriendlyByteBuf buffer) {
            return TeamBankReference.of(buffer.readLong());
        }
    }
}