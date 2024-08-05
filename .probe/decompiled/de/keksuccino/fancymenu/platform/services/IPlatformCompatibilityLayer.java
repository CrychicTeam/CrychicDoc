package de.keksuccino.fancymenu.platform.services;

import de.keksuccino.fancymenu.customization.deep.layers.titlescreen.TitleScreenLayer;
import java.util.List;
import net.minecraft.network.chat.Component;

public interface IPlatformCompatibilityLayer {

    List<Component> getTitleScreenBrandingLines();

    void registerTitleScreenDeepCustomizationLayerElements(TitleScreenLayer var1);
}