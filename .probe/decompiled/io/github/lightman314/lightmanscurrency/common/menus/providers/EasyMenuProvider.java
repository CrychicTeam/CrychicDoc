package io.github.lightman314.lightmanscurrency.common.menus.providers;

import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;

public interface EasyMenuProvider extends MenuProvider {

    @Nonnull
    @Override
    default Component getDisplayName() {
        return EasyText.empty();
    }
}