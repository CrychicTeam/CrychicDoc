package software.bernie.example;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import software.bernie.example.registry.EntityRegistry;

@EventBusSubscriber(modid = "geckolib", bus = Bus.MOD)
public final class CommonListener {

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        if (GeckoLibMod.shouldRegisterExamples()) {
            AttributeSupplier.Builder genericAttribs = PathfinderMob.m_21552_().add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.MAX_HEALTH, 1.0);
            AttributeSupplier.Builder genericMovingAttribs = PathfinderMob.m_21552_().add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.MAX_HEALTH, 1.0).add(Attributes.MOVEMENT_SPEED, 0.25);
            AttributeSupplier.Builder genericMonsterAttribs = Monster.m_21552_().add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.MAX_HEALTH, 1.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.ATTACK_KNOCKBACK, 0.1);
            event.put(EntityRegistry.BIKE.get(), genericAttribs.build());
            event.put(EntityRegistry.RACE_CAR.get(), genericAttribs.build());
            event.put(EntityRegistry.BAT.get(), genericAttribs.build());
            event.put(EntityRegistry.MUTANT_ZOMBIE.get(), genericAttribs.build());
            event.put(EntityRegistry.GREMLIN.get(), genericAttribs.build());
            event.put(EntityRegistry.COOL_KID.get(), genericMovingAttribs.build());
            event.put(EntityRegistry.FAKE_GLASS.get(), genericMovingAttribs.build());
            event.put(EntityRegistry.PARASITE.get(), genericMonsterAttribs.build());
        }
    }
}