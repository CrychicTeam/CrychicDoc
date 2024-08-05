package io.github.apace100.origins.badge;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.apace100.calio.data.SerializableData.Instance;
import io.github.apace100.origins.Origins;
import io.github.edwinmindcraft.origins.common.registry.OriginRegisters;
import java.util.function.Function;
import net.minecraftforge.registries.RegistryObject;

public final class BadgeFactories {

    public static final RegistryObject<BadgeFactory> SPRITE = register("sprite", new SerializableData().add("sprite", SerializableDataTypes.IDENTIFIER), SpriteBadge::new);

    public static final RegistryObject<BadgeFactory> TOOLTIP = register("tooltip", new SerializableData().add("sprite", SerializableDataTypes.IDENTIFIER).add("text", SerializableDataTypes.TEXT), TooltipBadge::new);

    public static final RegistryObject<BadgeFactory> KEYBIND = register("keybind", new SerializableData().add("sprite", SerializableDataTypes.IDENTIFIER).add("text", SerializableDataTypes.STRING), KeybindBadge::new);

    public static final RegistryObject<BadgeFactory> CRAFTING_RECIPE = register("crafting_recipe", new SerializableData().add("sprite", SerializableDataTypes.IDENTIFIER).add("recipe", SerializableDataTypes.RECIPE).add("prefix", SerializableDataTypes.TEXT, null).add("suffix", SerializableDataTypes.TEXT, null), CraftingRecipeBadge::new);

    public static void bootstrap() {
    }

    private static RegistryObject<BadgeFactory> register(String name, SerializableData data, Function<Instance, Badge> factory) {
        return OriginRegisters.BADGE_FACTORIES.register(name, () -> new BadgeFactory(Origins.identifier(name), data, factory));
    }
}