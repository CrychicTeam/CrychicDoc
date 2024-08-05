package io.github.lightman314.lightmanscurrency.common.notifications.types.settings;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.notifications.Notification;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationCategory;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationType;
import io.github.lightman314.lightmanscurrency.common.notifications.categories.NullCategory;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class ChangeNameNotification extends Notification {

    public static final NotificationType<ChangeNameNotification> TYPE = new NotificationType<>(new ResourceLocation("lightmanscurrency", "changed_name"), ChangeNameNotification::new);

    private PlayerReference player;

    private String oldName;

    private String newName;

    private ChangeNameNotification() {
    }

    public ChangeNameNotification(PlayerReference player, String newName, String oldName) {
        this.player = player;
        this.newName = newName;
        this.oldName = oldName;
    }

    @Nonnull
    @Override
    protected NotificationType<ChangeNameNotification> getType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public NotificationCategory getCategory() {
        return NullCategory.INSTANCE;
    }

    @Nonnull
    @Override
    public MutableComponent getMessage() {
        if (this.oldName.isBlank()) {
            return LCText.NOTIFICATION_SETTINGS_CHANGE_NAME_SET.get(this.player.getName(true), this.newName);
        } else {
            return this.newName.isBlank() ? LCText.NOTIFICATION_SETTINGS_CHANGE_NAME_RESET.get(this.player.getName(true), this.oldName) : LCText.NOTIFICATION_SETTINGS_CHANGE_NAME.get(this.player.getName(true), this.oldName, this.newName);
        }
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag compound) {
        compound.put("Player", this.player.save());
        compound.putString("OldName", this.oldName);
        compound.putString("NewName", this.newName);
    }

    @Override
    protected void loadAdditional(@Nonnull CompoundTag compound) {
        this.player = PlayerReference.load(compound.getCompound("Player"));
        this.oldName = compound.getString("OldName");
        this.newName = compound.getString("NewName");
    }

    @Override
    protected boolean canMerge(@Nonnull Notification other) {
        return !(other instanceof ChangeNameNotification n) ? false : n.player.is(this.player) && n.newName.equals(this.newName) && n.oldName.equals(this.oldName);
    }
}