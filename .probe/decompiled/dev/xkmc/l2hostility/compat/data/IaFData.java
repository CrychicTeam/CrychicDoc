package dev.xkmc.l2hostility.compat.data;

import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import java.util.List;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.RegistryObject;

public class IaFData {

    public static void genConfig(ConfigDataProvider.Collector collector) {
        addEntity(collector, 100, 50, IafEntityRegistry.FIRE_DRAGON, List.of(EntityConfig.trait((MobTrait) LHTraits.ADAPTIVE.get(), 1, 2), EntityConfig.trait((MobTrait) LHTraits.REGEN.get(), 2, 3), EntityConfig.trait((MobTrait) LHTraits.SOUL_BURNER.get(), 2, 3)), List.of((MobTrait) LHTraits.TANK.get()));
        addEntity(collector, 100, 50, IafEntityRegistry.ICE_DRAGON, List.of(EntityConfig.trait((MobTrait) LHTraits.ADAPTIVE.get(), 1, 2), EntityConfig.trait((MobTrait) LHTraits.REGEN.get(), 2, 3), EntityConfig.trait((MobTrait) LHTraits.FREEZING.get(), 2, 3)), List.of((MobTrait) LHTraits.TANK.get()));
        addEntity(collector, 100, 50, IafEntityRegistry.LIGHTNING_DRAGON, List.of(EntityConfig.trait((MobTrait) LHTraits.ADAPTIVE.get(), 1, 2), EntityConfig.trait((MobTrait) LHTraits.REGEN.get(), 2, 3), EntityConfig.trait((MobTrait) LHTraits.REFLECT.get(), 2, 3)), List.of((MobTrait) LHTraits.TANK.get()));
        addEntity(collector, 30, 10, IafEntityRegistry.GHOST, List.of(EntityConfig.trait((MobTrait) LHTraits.DEMENTOR.get(), 0, 1)), List.of((MobTrait) LHTraits.DISPELL.get()));
        addEntity(collector, 30, 10, IafEntityRegistry.SIREN, List.of(EntityConfig.trait((MobTrait) LHTraits.CONFUSION.get(), 1, 1), EntityConfig.trait((MobTrait) LHTraits.DRAIN.get(), 0, 1)), List.of());
    }

    public static <T extends LivingEntity> void addEntity(ConfigDataProvider.Collector collector, int min, int base, RegistryObject<EntityType<T>> obj, List<EntityConfig.TraitBase> traits, List<MobTrait> ban) {
        EntityConfig config = new EntityConfig();
        config.putEntityAndItem(min, base, 0.0, 0.0, List.of(obj.get()), traits, List.of());
        ((EntityConfig.Config) config.list.get(0)).blacklist().addAll(ban);
        collector.add(L2Hostility.ENTITY, obj.getId(), config);
    }
}