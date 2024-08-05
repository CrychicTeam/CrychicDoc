package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.settings.input;

import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.settings.SettingsSubTab;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import javax.annotation.Nonnull;

public abstract class InputTabAddon {

    public abstract void onOpen(@Nonnull SettingsSubTab var1, @Nonnull ScreenArea var2, boolean var3);

    public abstract void renderBG(@Nonnull SettingsSubTab var1, @Nonnull EasyGuiGraphics var2);

    public abstract void renderAfterWidgets(@Nonnull SettingsSubTab var1, @Nonnull EasyGuiGraphics var2);

    public abstract void tick(@Nonnull SettingsSubTab var1);

    public abstract void onClose(@Nonnull SettingsSubTab var1);
}