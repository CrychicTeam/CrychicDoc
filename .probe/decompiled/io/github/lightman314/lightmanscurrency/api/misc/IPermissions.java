package io.github.lightman314.lightmanscurrency.api.misc;

import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Player;

public interface IPermissions {

    default boolean hasPermission(@Nonnull Player player, @Nonnull String permission) {
        return this.getPermissionLevel(player, permission) > 0;
    }

    default boolean hasPermission(@Nonnull PlayerReference player, @Nonnull String permission) {
        return this.getPermissionLevel(player, permission) > 0;
    }

    int getPermissionLevel(@Nonnull Player var1, @Nonnull String var2);

    int getPermissionLevel(@Nonnull PlayerReference var1, @Nonnull String var2);
}