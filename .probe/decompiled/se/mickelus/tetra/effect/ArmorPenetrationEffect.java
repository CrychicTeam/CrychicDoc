package se.mickelus.tetra.effect;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

@ParametersAreNonnullByDefault
public class ArmorPenetrationEffect {

    private static final UUID uuid = UUID.fromString("a43e0407-f070-4e2f-8813-a5e16328f1a5");

    public static void onLivingHurt(LivingHurtEvent event, int effectLevel) {
        Optional.of(event.getEntity()).map(LivingEntity::m_21204_).filter(manager -> manager.hasAttribute(Attributes.ARMOR)).map(manager -> manager.getInstance(Attributes.ARMOR)).filter(instance -> instance.getModifier(uuid) == null).ifPresent(instance -> instance.addTransientModifier(new AttributeModifier(uuid, "tetra_armor_pen", (double) effectLevel * -0.01, AttributeModifier.Operation.MULTIPLY_TOTAL)));
    }

    public static void onLivingDamage(LivingDamageEvent event) {
        Optional.of(event.getEntity()).map(LivingEntity::m_21204_).filter(manager -> manager.hasAttribute(Attributes.ARMOR)).map(manager -> manager.getInstance(Attributes.ARMOR)).ifPresent(instance -> instance.removeModifier(uuid));
    }
}