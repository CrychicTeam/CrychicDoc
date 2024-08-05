package io.github.lightman314.lightmanscurrency.api.money.types;

import io.github.lightman314.lightmanscurrency.api.capability.money.IMoneyHandler;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Player;

public interface IPlayerMoneyHandler extends IMoneyHandler {

    void updatePlayer(@Nonnull Player var1);
}