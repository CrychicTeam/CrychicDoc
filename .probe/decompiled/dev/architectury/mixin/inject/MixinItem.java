package dev.architectury.mixin.inject;

import dev.architectury.extensions.injected.InjectedItemExtension;
import dev.architectury.impl.ItemPropertiesExtensionImpl;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredSupplier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Item.class })
public class MixinItem implements InjectedItemExtension {

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void init(Item.Properties properties, CallbackInfo ci) {
        CreativeModeTab tab = ((ItemPropertiesExtensionImpl) properties).arch$getTab();
        if (tab != null) {
            CreativeTabRegistry.appendBuiltin(tab, (Item) this);
        } else {
            DeferredSupplier<CreativeModeTab> tabSupplier = ((ItemPropertiesExtensionImpl) properties).arch$getTabSupplier();
            if (tabSupplier != null) {
                CreativeTabRegistry.append(tabSupplier, (Item) this);
            }
        }
    }
}