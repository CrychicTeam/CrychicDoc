package fuzs.puzzleslib.api.init.v3;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import fuzs.puzzleslib.api.core.v1.ModLoader;
import fuzs.puzzleslib.api.init.v2.builder.ExtendedMenuSupplier;
import fuzs.puzzleslib.impl.core.ModContext;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;

public interface RegistryManager {

    static RegistryManager from(String modId) {
        return ModContext.get(modId).getRegistryManagerV3();
    }

    default <T> ResourceKey<T> makeResourceKey(ResourceKey<? extends Registry<? super T>> registryKey, String path) {
        return ResourceKey.create((ResourceKey<? extends Registry<T>>) registryKey, this.makeKey(path));
    }

    ResourceLocation makeKey(String var1);

    RegistryManager whenOn(ModLoader... var1);

    default RegistryManager whenNotOn(ModLoader... forbiddenModLoaders) {
        Objects.checkIndex(0, forbiddenModLoaders.length);
        return this.whenOn((ModLoader[]) EnumSet.complementOf(Sets.newEnumSet(Arrays.asList(forbiddenModLoaders), ModLoader.class)).toArray(ModLoader[]::new));
    }

    <T> Holder.Reference<T> getHolder(ResourceKey<? extends Registry<? super T>> var1, String var2);

    <T> Holder.Reference<T> register(ResourceKey<? extends Registry<? super T>> var1, String var2, Supplier<T> var3);

    default Holder.Reference<Block> registerBlock(String path, Supplier<Block> entry) {
        return this.register(Registries.BLOCK, path, entry);
    }

    default Holder.Reference<Item> registerItem(String path, Supplier<Item> entry) {
        return this.register(Registries.ITEM, path, entry);
    }

    default Holder.Reference<Item> registerBlockItem(Holder.Reference<Block> blockReference) {
        return this.registerBlockItem(blockReference, new Item.Properties());
    }

    default Holder.Reference<Item> registerBlockItem(Holder.Reference<Block> blockReference, Item.Properties itemProperties) {
        return this.registerItem(blockReference.key().location().getPath(), () -> new BlockItem(blockReference.value(), itemProperties));
    }

    default Holder.Reference<Item> registerSpawnEggItem(Holder.Reference<? extends EntityType<? extends Mob>> entityTypeReference, int backgroundColor, int highlightColor) {
        return this.registerSpawnEggItem(entityTypeReference, backgroundColor, highlightColor, new Item.Properties());
    }

    Holder.Reference<Item> registerSpawnEggItem(Holder.Reference<? extends EntityType<? extends Mob>> var1, int var2, int var3, Item.Properties var4);

    default Holder.Reference<Fluid> registerFluid(String path, Supplier<Fluid> entry) {
        return this.register(Registries.FLUID, path, entry);
    }

    default Holder.Reference<MobEffect> registerMobEffect(String path, Supplier<MobEffect> entry) {
        return this.register(Registries.MOB_EFFECT, path, entry);
    }

    default Holder.Reference<SoundEvent> registerSoundEvent(String path) {
        return this.register(Registries.SOUND_EVENT, path, () -> SoundEvent.createVariableRangeEvent(this.makeKey(path)));
    }

    default Holder.Reference<Potion> registerPotion(String path, Supplier<Potion> entry) {
        return this.register(Registries.POTION, path, entry);
    }

    default Holder.Reference<Enchantment> registerEnchantment(String path, Supplier<Enchantment> entry) {
        return this.register(Registries.ENCHANTMENT, path, entry);
    }

    default <T extends Entity> Holder.Reference<EntityType<T>> registerEntityType(String path, Supplier<EntityType.Builder<T>> entry) {
        return this.register(Registries.ENTITY_TYPE, path, () -> ((EntityType.Builder) entry.get()).build(path));
    }

    default <T extends BlockEntity> Holder.Reference<BlockEntityType<T>> registerBlockEntityType(String path, Supplier<BlockEntityType.Builder<T>> entry) {
        return this.register(Registries.BLOCK_ENTITY_TYPE, path, () -> ((BlockEntityType.Builder) entry.get()).build(null));
    }

    default <T extends AbstractContainerMenu> Holder.Reference<MenuType<T>> registerMenuType(String path, Supplier<MenuType.MenuSupplier<T>> entry) {
        return this.register(Registries.MENU, path, () -> new MenuType((MenuType.MenuSupplier<T>) entry.get(), FeatureFlags.DEFAULT_FLAGS));
    }

    <T extends AbstractContainerMenu> Holder.Reference<MenuType<T>> registerExtendedMenuType(String var1, Supplier<ExtendedMenuSupplier<T>> var2);

    default Holder.Reference<PoiType> registerPoiType(String path, Supplier<Block> block) {
        return this.registerPoiType(path, () -> ImmutableSet.copyOf(((Block) block.get()).getStateDefinition().getPossibleStates()), 0, 1);
    }

    Holder.Reference<PoiType> registerPoiType(String var1, Supplier<Set<BlockState>> var2, int var3, int var4);

    default <T extends Recipe<?>> Holder.Reference<RecipeType<T>> registerRecipeType(String path) {
        return this.register(Registries.RECIPE_TYPE, path, () -> new RecipeType<T>() {

            private final String id = RegistryManager.this.makeKey(path).toString();

            public String toString() {
                return this.id;
            }
        });
    }

    default Holder.Reference<GameEvent> registerGameEvent(String path, int notificationRadius) {
        return this.register(Registries.GAME_EVENT, path, () -> new GameEvent(path, notificationRadius));
    }

    default Holder.Reference<SimpleParticleType> registerParticleType(String path) {
        return this.register(Registries.PARTICLE_TYPE, path, () -> new SimpleParticleType(false));
    }

    default ResourceKey<DamageType> registerDamageType(String path) {
        return this.makeResourceKey(Registries.DAMAGE_TYPE, path);
    }
}