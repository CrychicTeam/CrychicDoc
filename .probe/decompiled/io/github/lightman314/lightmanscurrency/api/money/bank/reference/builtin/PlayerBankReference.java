package io.github.lightman314.lightmanscurrency.api.money.bank.reference.builtin;

import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.IBankAccount;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReferenceType;
import io.github.lightman314.lightmanscurrency.common.bank.BankSaveData;
import io.github.lightman314.lightmanscurrency.common.player.LCAdminMode;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class PlayerBankReference extends BankReference {

    public static final BankReferenceType TYPE = new PlayerBankReference.Type();

    public final UUID playerID;

    protected PlayerBankReference(@Nonnull UUID playerID) {
        super(TYPE);
        this.playerID = playerID;
    }

    public static BankReference of(@Nonnull UUID player) {
        return new PlayerBankReference(player);
    }

    @Nullable
    public static BankReference of(@Nullable PlayerReference player) {
        return player != null ? new PlayerBankReference(player.id) : null;
    }

    public static BankReference of(@Nonnull Player player) {
        return new PlayerBankReference(player.m_20148_()).flagAsClient(player.m_9236_().isClientSide);
    }

    @Nullable
    @Override
    public IBankAccount get() {
        return BankSaveData.GetBankAccount(this.isClient(), this.playerID);
    }

    @Override
    public boolean allowedAccess(@Nonnull Player player) {
        return LCAdminMode.isAdminPlayer(player) || this.playerID.equals(player.m_20148_());
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag) {
        tag.putUUID("PlayerID", this.playerID);
    }

    @Override
    protected void encodeAdditional(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeUUID(this.playerID);
    }

    @Override
    public boolean canPersist(@Nonnull Player player) {
        return this.playerID.equals(player.m_20148_());
    }

    private static final class Type extends BankReferenceType {

        Type() {
            super(new ResourceLocation("lightmanscurrency", "personal"));
        }

        @Override
        public BankReference load(CompoundTag tag) {
            return PlayerBankReference.of(tag.getUUID("PlayerID"));
        }

        @Override
        public BankReference decode(FriendlyByteBuf buffer) {
            return PlayerBankReference.of(buffer.readUUID());
        }
    }
}