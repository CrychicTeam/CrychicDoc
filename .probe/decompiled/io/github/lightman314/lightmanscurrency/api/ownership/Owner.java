package io.github.lightman314.lightmanscurrency.api.ownership;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReference;
import io.github.lightman314.lightmanscurrency.api.notifications.Notification;
import io.github.lightman314.lightmanscurrency.api.stats.StatKey;
import io.github.lightman314.lightmanscurrency.common.util.IClientTracker;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.NonNullSupplier;

public abstract class Owner implements IClientTracker {

    public static final Owner NULL = new Owner.NullOwner();

    public static final OwnerType NULL_TYPE = OwnerType.create(new ResourceLocation("lightmanscurrency", "null"), t -> NULL);

    private boolean isClient = false;

    @Override
    public final boolean isClient() {
        return this.isClient;
    }

    public final Owner flagAsClient() {
        this.isClient = true;
        return this;
    }

    public final void flagAsClient(boolean isClient) {
        this.isClient = isClient;
    }

    @Nonnull
    public abstract MutableComponent getName();

    @Nonnull
    public abstract MutableComponent getCommandLabel();

    public abstract boolean stillValid();

    public boolean alwaysValid() {
        return false;
    }

    public final boolean isNull() {
        return this instanceof Owner.NullOwner;
    }

    public abstract boolean isOnline();

    public abstract boolean isAdmin(@Nonnull PlayerReference var1);

    public abstract boolean isMember(@Nonnull PlayerReference var1);

    @Nonnull
    public abstract PlayerReference asPlayerReference();

    @Nullable
    public abstract BankReference asBankReference();

    public boolean hasNotificationLevels() {
        return false;
    }

    @Nonnull
    public static MutableComponent getOwnerLevelBlurb(int notificationLevel) {
        return switch(notificationLevel) {
            case 0 ->
                LCText.BLURB_OWNERSHIP_MEMBERS.get();
            case 1 ->
                LCText.BLURB_OWNERSHIP_ADMINS.get();
            default ->
                LCText.BLURB_OWNERSHIP_OWNER.get();
        };
    }

    public abstract void pushNotification(@Nonnull NonNullSupplier<? extends Notification> var1, int var2, boolean var3);

    public <T> void incrementStat(@Nonnull StatKey<?, T> key, @Nonnull T addValue) {
    }

    @Nonnull
    public abstract OwnerType getType();

    @Nonnull
    public final CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Type", this.getType().getID().toString());
        this.saveAdditional(tag);
        return tag;
    }

    protected abstract void saveAdditional(@Nonnull CompoundTag var1);

    @Nullable
    public static Owner load(@Nonnull CompoundTag tag) {
        ResourceLocation id = new ResourceLocation(tag.getString("Type"));
        OwnerType type = OwnershipAPI.API.getOwnerType(id);
        if (type != null) {
            return type.load(tag);
        } else {
            LightmansCurrency.LogError("No owner type " + id + " is registered!\nCould not load the owner!");
            return null;
        }
    }

    public boolean equals(Object obj) {
        return obj instanceof Owner o ? this.matches(o) : false;
    }

    public abstract boolean matches(@Nonnull Owner var1);

    private static class NullOwner extends Owner {

        @Nonnull
        @Override
        public MutableComponent getName() {
            return LCText.GUI_OWNER_NULL.get();
        }

        @Nonnull
        @Override
        public MutableComponent getCommandLabel() {
            return LCText.COMMAND_LCADMIN_DATA_OWNER_CUSTOM.get(this.getName());
        }

        @Override
        public boolean stillValid() {
            return false;
        }

        @Override
        public boolean isOnline() {
            return false;
        }

        @Override
        public boolean isAdmin(@Nonnull PlayerReference player) {
            return false;
        }

        @Override
        public boolean isMember(@Nonnull PlayerReference player) {
            return false;
        }

        @Nonnull
        @Override
        public PlayerReference asPlayerReference() {
            return PlayerReference.NULL;
        }

        @Nullable
        @Override
        public BankReference asBankReference() {
            return null;
        }

        @Override
        public void pushNotification(@Nonnull NonNullSupplier<? extends Notification> notificationSource, int notificationLevel, boolean sendToChat) {
        }

        @Nonnull
        @Override
        public OwnerType getType() {
            return NULL_TYPE;
        }

        @Override
        protected void saveAdditional(@Nonnull CompoundTag tag) {
        }

        @Override
        public boolean matches(@Nonnull Owner other) {
            return other.isNull();
        }
    }
}