package team.lodestar.lodestone.registry.common;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(bus = Bus.MOD)
public class LodestoneAttributeRegistry {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, "lodestone");

    public static final HashMap<RegistryObject<Attribute>, UUID> UUIDS = new HashMap();

    public static final RegistryObject<Attribute> MAGIC_RESISTANCE = registerAttribute(ATTRIBUTES, "lodestone", "magic_resistance", id -> new RangedAttribute(id, 0.0, 0.0, 2048.0).m_22084_(true));

    public static final RegistryObject<Attribute> MAGIC_PROFICIENCY = registerAttribute(ATTRIBUTES, "lodestone", "magic_proficiency", id -> new RangedAttribute(id, 0.0, 0.0, 2048.0).m_22084_(true));

    public static final RegistryObject<Attribute> MAGIC_DAMAGE = registerAttribute(ATTRIBUTES, "lodestone", "magic_damage", id -> new RangedAttribute(id, 0.0, 0.0, 2048.0).m_22084_(true));

    public static RegistryObject<Attribute> registerAttribute(DeferredRegister<Attribute> registry, String modId, String name, Function<String, Attribute> attribute) {
        RegistryObject<Attribute> registryObject = registry.register(name, () -> (Attribute) attribute.apply("attribute.name." + modId + "." + name));
        UUIDS.put(registryObject, UUID.randomUUID());
        return registryObject;
    }

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(e -> {
            event.add(e, MAGIC_RESISTANCE.get());
            event.add(e, MAGIC_PROFICIENCY.get());
            event.add(e, MAGIC_DAMAGE.get());
        });
    }
}