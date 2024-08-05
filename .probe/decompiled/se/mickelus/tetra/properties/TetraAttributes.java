package se.mickelus.tetra.properties;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@ParametersAreNonnullByDefault
public class TetraAttributes {

    public static final DeferredRegister<Attribute> registry = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, "tetra");

    public static RegistryObject<Attribute> drawStrength = registry.register("draw_strength", () -> new RangedAttribute("tetra.attribute.draw_strength.name", 0.0, 0.0, 2048.0));

    public static RegistryObject<Attribute> drawSpeed = registry.register("draw_speed", () -> new RangedAttribute("tetra.attribute.draw_speed.name", 0.0, 0.0, 2048.0));

    public static RegistryObject<Attribute> abilityCooldown = registry.register("ability_cooldown", () -> new RangedAttribute("tetra.attribute.ability_cooldown.name", 0.0, 0.0, 2048.0));

    public static RegistryObject<Attribute> abilityDamage = registry.register("ability_damage", () -> new RangedAttribute("tetra.attribute.ability_damage.name", 0.0, 0.0, 2048.0));
}