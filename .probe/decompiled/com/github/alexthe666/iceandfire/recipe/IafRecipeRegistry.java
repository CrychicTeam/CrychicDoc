package com.github.alexthe666.iceandfire.recipe;

import com.github.alexthe666.iceandfire.entity.EntityAmphithereArrow;
import com.github.alexthe666.iceandfire.entity.EntityCockatriceEgg;
import com.github.alexthe666.iceandfire.entity.EntityDeathWormEgg;
import com.github.alexthe666.iceandfire.entity.EntityDragonArrow;
import com.github.alexthe666.iceandfire.entity.EntityHippogryphEgg;
import com.github.alexthe666.iceandfire.entity.EntityHydraArrow;
import com.github.alexthe666.iceandfire.entity.EntitySeaSerpentArrow;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianArrow;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(bus = Bus.MOD)
public class IafRecipeRegistry {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE = DeferredRegister.create(Registries.RECIPE_TYPE, "iceandfire");

    public static final RegistryObject<RecipeType<DragonForgeRecipe>> DRAGON_FORGE_TYPE = RECIPE_TYPE.register("dragonforge", () -> RecipeType.simple(new ResourceLocation("iceandfire", "dragonforge")));

    @SubscribeEvent
    public static void preInit(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DispenserBlock.registerBehavior(IafItemRegistry.STYMPHALIAN_ARROW.get(), new AbstractProjectileDispenseBehavior() {

                @NotNull
                @Override
                protected Projectile getProjectile(@NotNull Level worldIn, @NotNull Position position, @NotNull ItemStack stackIn) {
                    EntityStymphalianArrow entityarrow = new EntityStymphalianArrow(IafEntityRegistry.STYMPHALIAN_ARROW.get(), worldIn, position.x(), position.y(), position.z());
                    entityarrow.f_36705_ = AbstractArrow.Pickup.ALLOWED;
                    return entityarrow;
                }
            });
            DispenserBlock.registerBehavior(IafItemRegistry.AMPHITHERE_ARROW.get(), new AbstractProjectileDispenseBehavior() {

                @NotNull
                @Override
                protected Projectile getProjectile(@NotNull Level worldIn, @NotNull Position position, @NotNull ItemStack stackIn) {
                    EntityAmphithereArrow entityarrow = new EntityAmphithereArrow(IafEntityRegistry.AMPHITHERE_ARROW.get(), worldIn, position.x(), position.y(), position.z());
                    entityarrow.f_36705_ = AbstractArrow.Pickup.ALLOWED;
                    return entityarrow;
                }
            });
            DispenserBlock.registerBehavior(IafItemRegistry.SEA_SERPENT_ARROW.get(), new AbstractProjectileDispenseBehavior() {

                @NotNull
                @Override
                protected Projectile getProjectile(@NotNull Level worldIn, @NotNull Position position, @NotNull ItemStack stackIn) {
                    EntitySeaSerpentArrow entityarrow = new EntitySeaSerpentArrow(IafEntityRegistry.SEA_SERPENT_ARROW.get(), worldIn, position.x(), position.y(), position.z());
                    entityarrow.f_36705_ = AbstractArrow.Pickup.ALLOWED;
                    return entityarrow;
                }
            });
            DispenserBlock.registerBehavior(IafItemRegistry.DRAGONBONE_ARROW.get(), new AbstractProjectileDispenseBehavior() {

                @NotNull
                @Override
                protected Projectile getProjectile(@NotNull Level worldIn, @NotNull Position position, @NotNull ItemStack stackIn) {
                    EntityDragonArrow entityarrow = new EntityDragonArrow(IafEntityRegistry.DRAGON_ARROW.get(), position.x(), position.y(), position.z(), worldIn);
                    entityarrow.f_36705_ = AbstractArrow.Pickup.ALLOWED;
                    return entityarrow;
                }
            });
            DispenserBlock.registerBehavior(IafItemRegistry.HYDRA_ARROW.get(), new AbstractProjectileDispenseBehavior() {

                @NotNull
                @Override
                protected Projectile getProjectile(@NotNull Level worldIn, @NotNull Position position, @NotNull ItemStack stackIn) {
                    EntityHydraArrow entityarrow = new EntityHydraArrow(IafEntityRegistry.HYDRA_ARROW.get(), worldIn, position.x(), position.y(), position.z());
                    entityarrow.f_36705_ = AbstractArrow.Pickup.ALLOWED;
                    return entityarrow;
                }
            });
            DispenserBlock.registerBehavior(IafItemRegistry.HIPPOGRYPH_EGG.get(), new AbstractProjectileDispenseBehavior() {

                @NotNull
                @Override
                protected Projectile getProjectile(@NotNull Level worldIn, @NotNull Position position, @NotNull ItemStack stackIn) {
                    return new EntityHippogryphEgg(IafEntityRegistry.HIPPOGRYPH_EGG.get(), worldIn, position.x(), position.y(), position.z(), stackIn);
                }
            });
            DispenserBlock.registerBehavior(IafItemRegistry.ROTTEN_EGG.get(), new AbstractProjectileDispenseBehavior() {

                @NotNull
                @Override
                protected Projectile getProjectile(@NotNull Level worldIn, @NotNull Position position, @NotNull ItemStack stackIn) {
                    return new EntityCockatriceEgg(IafEntityRegistry.COCKATRICE_EGG.get(), position.x(), position.y(), position.z(), worldIn);
                }
            });
            DispenserBlock.registerBehavior(IafItemRegistry.DEATHWORM_EGG.get(), new AbstractProjectileDispenseBehavior() {

                @NotNull
                @Override
                protected Projectile getProjectile(@NotNull Level worldIn, @NotNull Position position, @NotNull ItemStack stackIn) {
                    return new EntityDeathWormEgg(IafEntityRegistry.DEATH_WORM_EGG.get(), position.x(), position.y(), position.z(), worldIn, false);
                }
            });
            DispenserBlock.registerBehavior(IafItemRegistry.DEATHWORM_EGG_GIGANTIC.get(), new AbstractProjectileDispenseBehavior() {

                @NotNull
                @Override
                protected Projectile getProjectile(@NotNull Level worldIn, @NotNull Position position, @NotNull ItemStack stackIn) {
                    return new EntityDeathWormEgg(IafEntityRegistry.DEATH_WORM_EGG.get(), position.x(), position.y(), position.z(), worldIn, true);
                }
            });
            BrewingRecipeRegistry.addRecipe(Ingredient.of(createPotion(Potions.WATER).getItem()), Ingredient.of(IafItemRegistry.SHINY_SCALES.get()), createPotion(Potions.WATER_BREATHING));
        });
    }

    public static ItemStack createPotion(Potion potion) {
        return PotionUtils.setPotion(new ItemStack(Items.POTION), potion);
    }
}