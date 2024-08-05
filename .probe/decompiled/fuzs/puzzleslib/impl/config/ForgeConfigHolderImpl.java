package fuzs.puzzleslib.impl.config;

import fuzs.puzzleslib.api.core.v1.ModContainerHelper;
import fuzs.puzzleslib.impl.config.core.ForgeModConfig;
import java.util.Optional;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.config.ModConfig;

public class ForgeConfigHolderImpl extends ConfigHolderImpl {

    public ForgeConfigHolderImpl(String modId) {
        super(modId);
    }

    @Override
    void bake(ConfigDataHolderImpl<?> holder, String modId) {
        Optional<IEventBus> optional = ModContainerHelper.getOptionalModEventBus(modId);
        optional.ifPresent(eventBus -> eventBus.addListener(evt -> holder.onModConfig(evt.getConfig(), false)));
        optional.ifPresent(eventBus -> eventBus.addListener(evt -> holder.onModConfig(evt.getConfig(), true)));
        holder.register((type, spec, fileName) -> {
            ModContainer modContainer = ModContainerHelper.getModContainer(modId);
            ModConfig modConfig = new ForgeModConfig(type, spec, modContainer, (String) fileName.apply(modId));
            modContainer.addConfig(modConfig);
            return modConfig;
        });
    }
}