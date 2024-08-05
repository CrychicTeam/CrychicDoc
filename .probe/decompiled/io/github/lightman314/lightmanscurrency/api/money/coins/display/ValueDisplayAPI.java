package io.github.lightman314.lightmanscurrency.api.money.coins.display;

import io.github.lightman314.lightmanscurrency.api.events.RegisterValueDisplayTypes;
import io.github.lightman314.lightmanscurrency.api.money.coins.display.builtin.CoinDisplay;
import io.github.lightman314.lightmanscurrency.api.money.coins.display.builtin.Null;
import io.github.lightman314.lightmanscurrency.api.money.coins.display.builtin.NumberDisplay;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "lightmanscurrency", bus = Bus.MOD)
public class ValueDisplayAPI {

    private static Map<ResourceLocation, ValueDisplaySerializer> REGISTRY = null;

    public static void Setup() {
        if (REGISTRY == null) {
            RegisterValueDisplayTypes event = new RegisterValueDisplayTypes();
            event.register(Null.SERIALIZER);
            event.register(CoinDisplay.SERIALIZER);
            event.register(NumberDisplay.SERIALIZER);
            ModLoader.get().postEvent(event);
            REGISTRY = event.getResults();
        }
    }

    @Nullable
    public static ValueDisplaySerializer get(@Nonnull ResourceLocation type) {
        return REGISTRY == null ? null : (ValueDisplaySerializer) REGISTRY.get(type);
    }
}