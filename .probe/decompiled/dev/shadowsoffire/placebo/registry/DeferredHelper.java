package dev.shadowsoffire.placebo.registry;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

public class DeferredHelper {

    protected final String modid;

    protected final Map<ResourceKey<? extends Registry<?>>, List<DeferredHelper.Registrar<?>>> objects;

    private static MethodHandle RO_updateReference;

    public static DeferredHelper create(String modid) {
        DeferredHelper helper = new DeferredHelper(modid);
        FMLJavaModLoadingContext.get().getModEventBus().register(helper);
        return helper;
    }

    protected DeferredHelper(String modid) {
        this.modid = modid;
        this.objects = new IdentityHashMap();
    }

    public <T extends Block> RegistryObject<T> block(String path, Supplier<T> factory) {
        return this.create(path, Registries.BLOCK, factory);
    }

    public <T extends Fluid> RegistryObject<T> fluid(String path, Supplier<T> factory) {
        return this.create(path, Registries.FLUID, factory);
    }

    public <T extends Item> RegistryObject<T> item(String path, Supplier<T> factory) {
        return this.create(path, Registries.ITEM, factory);
    }

    public <T extends MobEffect> RegistryObject<T> effect(String path, Supplier<T> factory) {
        return this.create(path, Registries.MOB_EFFECT, factory);
    }

    public <T extends SoundEvent> RegistryObject<T> sound(String path, Supplier<T> factory) {
        return this.create(path, Registries.SOUND_EVENT, factory);
    }

    public RegistryObject<SoundEvent> sound(String path) {
        return this.sound(path, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(this.modid, path)));
    }

    public <T extends Potion> RegistryObject<T> potion(String path, Supplier<T> factory) {
        return this.create(path, Registries.POTION, factory);
    }

    public <T extends Enchantment> RegistryObject<T> enchant(String path, Supplier<T> factory) {
        return this.create(path, Registries.ENCHANTMENT, factory);
    }

    public <U extends Entity, T extends EntityType<U>> RegistryObject<T> entity(String path, Supplier<T> factory) {
        return this.create(path, Registries.ENTITY_TYPE, factory);
    }

    public <U extends BlockEntity, T extends BlockEntityType<U>> RegistryObject<T> blockEntity(String path, Supplier<T> factory) {
        return this.create(path, Registries.BLOCK_ENTITY_TYPE, factory);
    }

    public <U extends ParticleOptions, T extends ParticleType<U>> RegistryObject<T> particle(String path, Supplier<T> factory) {
        return this.create(path, Registries.PARTICLE_TYPE, factory);
    }

    public <U extends AbstractContainerMenu, T extends MenuType<U>> RegistryObject<T> menu(String path, Supplier<T> factory) {
        return this.create(path, Registries.MENU, factory);
    }

    public <T extends PaintingVariant> RegistryObject<T> painting(String path, Supplier<T> factory) {
        return this.create(path, Registries.PAINTING_VARIANT, factory);
    }

    public <C extends Container, U extends Recipe<C>, T extends RecipeType<U>> RegistryObject<T> recipe(String path, Supplier<T> factory) {
        return this.create(path, Registries.RECIPE_TYPE, factory);
    }

    public <C extends Container, U extends Recipe<C>, T extends RecipeSerializer<U>> RegistryObject<T> recipeSerializer(String path, Supplier<T> factory) {
        return this.create(path, Registries.RECIPE_SERIALIZER, factory);
    }

    public <T extends Attribute> RegistryObject<T> attribute(String path, Supplier<T> factory) {
        return this.create(path, Registries.ATTRIBUTE, factory);
    }

    public <S, U extends StatType<S>, T extends StatType<U>> RegistryObject<T> stat(String path, Supplier<T> factory) {
        return this.create(path, Registries.STAT_TYPE, factory);
    }

    public RegistryObject<ResourceLocation> customStat(String path, StatFormatter formatter) {
        return this.create(path, Registries.CUSTOM_STAT, () -> {
            ResourceLocation id = new ResourceLocation(this.modid, path);
            Stats.CUSTOM.get(id, formatter);
            return id;
        });
    }

    public <U extends FeatureConfiguration, T extends Feature<U>> RegistryObject<T> feature(String path, Supplier<T> factory) {
        return this.create(path, Registries.FEATURE, factory);
    }

    public <T extends CreativeModeTab> RegistryObject<T> tab(String path, Supplier<T> factory) {
        return this.create(path, Registries.CREATIVE_MODE_TAB, factory);
    }

    public <P, T extends P> RegistryObject<T> custom(String path, ResourceKey<Registry<P>> registry, Supplier<T> factory) {
        return this.create(path, registry, factory);
    }

    protected <P, T extends P> RegistryObject<T> create(String path, ResourceKey<Registry<P>> regKey, Supplier<T> factory) {
        List<DeferredHelper.Registrar<?>> registrars = (List<DeferredHelper.Registrar<?>>) this.objects.computeIfAbsent(regKey, k -> new ArrayList());
        ResourceLocation id = new ResourceLocation(this.modid, path);
        RegistryObject<T> obj = RegistryObject.create(id, regKey, this.modid);
        registrars.add(new DeferredHelper.Registrar<>(id, obj, factory));
        return obj;
    }

    @SubscribeEvent
    public void register(RegisterEvent e) {
        ((List) this.objects.getOrDefault(e.getRegistryKey(), Collections.emptyList())).forEach(registrar -> {
            e.register(e.getRegistryKey(), registrar.id, registrar.factory);
            if (RO_updateReference != null) {
                try {
                    RO_updateReference.invoke(registrar.obj, e);
                } catch (Throwable var3) {
                    throw new RuntimeException(var3);
                }
            }
        });
    }

    static {
        try {
            Method m = RegistryObject.class.getDeclaredMethod("updateReference", RegisterEvent.class);
            m.setAccessible(true);
            RO_updateReference = MethodHandles.lookup().unreflect(m);
        } catch (Exception var1) {
        }
    }

    protected static record Registrar<T>(ResourceLocation id, RegistryObject<T> obj, Supplier<T> factory) {
    }
}