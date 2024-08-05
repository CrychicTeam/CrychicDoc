package net.mehvahdjukaar.moonlight.api.block;

import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public interface IOwnerProtected {

    @Nullable
    UUID getOwner();

    void setOwner(@Nullable UUID var1);

    default void saveOwner(CompoundTag tag) {
        UUID owner = this.getOwner();
        if (owner != null) {
            tag.putUUID("Owner", owner);
        }
    }

    default void loadOwner(CompoundTag tag) {
        if (tag.contains("Owner")) {
            this.setOwner(tag.getUUID("Owner"));
        }
    }

    default boolean isOwnedBy(Player player) {
        UUID id = this.getOwner();
        return id != null && id.equals(player.m_20148_());
    }

    default boolean isPublic() {
        return this.getOwner() == null;
    }

    default boolean isAccessibleBy(Player player) {
        return this.isPublic() || this.isOwnedBy(player);
    }

    default boolean isNotOwnedBy(Player player) {
        UUID id = this.getOwner();
        return id != null && !id.equals(player.m_20148_());
    }
}