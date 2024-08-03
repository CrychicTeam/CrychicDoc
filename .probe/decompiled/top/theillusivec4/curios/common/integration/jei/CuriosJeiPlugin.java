package top.theillusivec4.curios.common.integration.jei;

import javax.annotation.Nonnull;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.curios.client.gui.CuriosScreen;
import top.theillusivec4.curios.client.gui.CuriosScreenV2;

@JeiPlugin
public class CuriosJeiPlugin implements IModPlugin {

    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("curios", "curios");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(CuriosScreen.class, new CuriosContainerHandler());
        registration.addGuiContainerHandler(CuriosScreenV2.class, new CuriosContainerV2Handler());
    }
}