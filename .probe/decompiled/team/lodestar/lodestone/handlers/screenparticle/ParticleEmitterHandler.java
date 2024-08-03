package team.lodestar.lodestone.handlers.screenparticle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import team.lodestar.lodestone.helpers.DataHelper;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;

public class ParticleEmitterHandler {

    public static final Map<Item, List<ParticleEmitterHandler.ItemParticleSupplier>> EMITTERS = new HashMap();

    public static void registerParticleEmitters(FMLClientSetupEvent event) {
        DataHelper.getAll(ForgeRegistries.ITEMS.getValues(), i -> i instanceof ParticleEmitterHandler.ItemParticleSupplier).forEach(i -> {
            ParticleEmitterHandler.ItemParticleSupplier emitter = (ParticleEmitterHandler.ItemParticleSupplier) i;
            registerItemParticleEmitter(i, emitter);
        });
    }

    public static void registerItemParticleEmitter(Item item, ParticleEmitterHandler.ItemParticleSupplier emitter) {
        if (EMITTERS.containsKey(item)) {
            ((List) EMITTERS.get(item)).add(emitter);
        } else {
            EMITTERS.put(item, new ArrayList(List.of(emitter)));
        }
    }

    public static void registerItemParticleEmitter(ParticleEmitterHandler.ItemParticleSupplier emitter, Item... items) {
        for (Item item : items) {
            registerItemParticleEmitter(item, emitter);
        }
    }

    public interface ItemParticleSupplier {

        default void spawnEarlyParticles(ScreenParticleHolder target, Level level, float partialTick, ItemStack stack, float x, float y) {
        }

        default void spawnLateParticles(ScreenParticleHolder target, Level level, float partialTick, ItemStack stack, float x, float y) {
        }
    }
}