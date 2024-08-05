package com.simibubi.create;

import com.simibubi.create.compat.jei.ConversionRecipe;
import com.simibubi.create.content.equipment.sandPaper.SandPaperPolishingRecipe;
import com.simibubi.create.content.equipment.toolbox.ToolboxDyeingRecipe;
import com.simibubi.create.content.fluids.transfer.EmptyingRecipe;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.crafter.MechanicalCraftingRecipe;
import com.simibubi.create.content.kinetics.crusher.CrushingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.deployer.ManualApplicationRecipe;
import com.simibubi.create.content.kinetics.fan.processing.HauntingRecipe;
import com.simibubi.create.content.kinetics.fan.processing.SplashingRecipe;
import com.simibubi.create.content.kinetics.millstone.MillingRecipe;
import com.simibubi.create.content.kinetics.mixer.CompactingRecipe;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.kinetics.saw.CuttingRecipe;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.Lang;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public enum AllRecipeTypes implements IRecipeTypeInfo {

    CONVERSION(ConversionRecipe::new),
    CRUSHING(CrushingRecipe::new),
    CUTTING(CuttingRecipe::new),
    MILLING(MillingRecipe::new),
    BASIN(BasinRecipe::new),
    MIXING(MixingRecipe::new),
    COMPACTING(CompactingRecipe::new),
    PRESSING(PressingRecipe::new),
    SANDPAPER_POLISHING(SandPaperPolishingRecipe::new),
    SPLASHING(SplashingRecipe::new),
    HAUNTING(HauntingRecipe::new),
    DEPLOYING(DeployerApplicationRecipe::new),
    FILLING(FillingRecipe::new),
    EMPTYING(EmptyingRecipe::new),
    ITEM_APPLICATION(ManualApplicationRecipe::new),
    MECHANICAL_CRAFTING(MechanicalCraftingRecipe.Serializer::new),
    SEQUENCED_ASSEMBLY(SequencedAssemblyRecipeSerializer::new),
    TOOLBOX_DYEING(() -> new SimpleCraftingRecipeSerializer(ToolboxDyeingRecipe::new), () -> RecipeType.CRAFTING, false);

    private final ResourceLocation id;

    private final RegistryObject<RecipeSerializer<?>> serializerObject;

    @Nullable
    private final RegistryObject<RecipeType<?>> typeObject;

    private final Supplier<RecipeType<?>> type;

    private AllRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier, Supplier<RecipeType<?>> typeSupplier, boolean registerType) {
        String name = Lang.asId(this.name());
        this.id = Create.asResource(name);
        this.serializerObject = AllRecipeTypes.Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        if (registerType) {
            this.typeObject = AllRecipeTypes.Registers.TYPE_REGISTER.register(name, typeSupplier);
            this.type = this.typeObject;
        } else {
            this.typeObject = null;
            this.type = typeSupplier;
        }
    }

    private AllRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier) {
        String name = Lang.asId(this.name());
        this.id = Create.asResource(name);
        this.serializerObject = AllRecipeTypes.Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        this.typeObject = AllRecipeTypes.Registers.TYPE_REGISTER.register(name, () -> RecipeType.simple(this.id));
        this.type = this.typeObject;
    }

    private AllRecipeTypes(ProcessingRecipeBuilder.ProcessingRecipeFactory<?> processingFactory) {
        this(() -> new ProcessingRecipeSerializer<>(processingFactory));
    }

    public static void register(IEventBus modEventBus) {
        ShapedRecipe.setCraftingSize(9, 9);
        AllRecipeTypes.Registers.SERIALIZER_REGISTER.register(modEventBus);
        AllRecipeTypes.Registers.TYPE_REGISTER.register(modEventBus);
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public <T extends RecipeSerializer<?>> T getSerializer() {
        return (T) this.serializerObject.get();
    }

    @Override
    public <T extends RecipeType<?>> T getType() {
        return (T) this.type.get();
    }

    public <C extends Container, T extends Recipe<C>> Optional<T> find(C inv, Level world) {
        return world.getRecipeManager().getRecipeFor(this.getType(), inv, world);
    }

    public static boolean shouldIgnoreInAutomation(Recipe<?> recipe) {
        RecipeSerializer<?> serializer = recipe.getSerializer();
        return serializer != null && AllTags.AllRecipeSerializerTags.AUTOMATION_IGNORE.matches(serializer) ? true : recipe.getId().getPath().endsWith("_manual_only");
    }

    private static class Registers {

        private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "create");

        private static final DeferredRegister<RecipeType<?>> TYPE_REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, "create");
    }
}