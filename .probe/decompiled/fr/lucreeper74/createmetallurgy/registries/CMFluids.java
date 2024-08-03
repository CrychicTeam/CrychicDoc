package fr.lucreeper74.createmetallurgy.registries;

import com.simibubi.create.AllTags;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import fr.lucreeper74.createmetallurgy.CreateMetallurgy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class CMFluids {

    public static final ResourceLocation MOLTEN_IRON_STILL_RL = CreateMetallurgy.genRL("fluid/iron/molten_iron_still");

    public static final ResourceLocation MOLTEN_IRON_FLOW_RL = CreateMetallurgy.genRL("fluid/iron/molten_iron_flow");

    public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_IRON = ((FluidBuilder) CreateMetallurgy.REGISTRATE.fluid("molten_iron", MOLTEN_IRON_STILL_RL, MOLTEN_IRON_FLOW_RL).properties(b -> b.viscosity(2000).density(1400).lightLevel(10).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).canHydrate(false).canDrown(false).canSwim(false)).fluidProperties(p -> p.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(100.0F)).tag(new TagKey[] { AllTags.forgeFluidTag("molten_iron"), AllTags.forgeFluidTag("molten_materials") }).source(ForgeFlowingFluid.Source::new).bucket().build()).register();

    public static final ResourceLocation MOLTEN_GOLD_STILL_RL = CreateMetallurgy.genRL("fluid/gold/molten_gold_still");

    public static final ResourceLocation MOLTEN_GOLD_FLOW_RL = CreateMetallurgy.genRL("fluid/gold/molten_gold_flow");

    public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_GOLD = ((FluidBuilder) CreateMetallurgy.REGISTRATE.fluid("molten_gold", MOLTEN_GOLD_STILL_RL, MOLTEN_GOLD_FLOW_RL).properties(b -> b.viscosity(2000).density(1400).lightLevel(10).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).canHydrate(false).canDrown(false).canSwim(false)).fluidProperties(p -> p.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(100.0F)).tag(new TagKey[] { AllTags.forgeFluidTag("molten_gold"), AllTags.forgeFluidTag("molten_materials") }).source(ForgeFlowingFluid.Source::new).bucket().build()).register();

    public static final ResourceLocation MOLTEN_COPPER_STILL_RL = CreateMetallurgy.genRL("fluid/copper/molten_copper_still");

    public static final ResourceLocation MOLTEN_COPPER_FLOW_RL = CreateMetallurgy.genRL("fluid/copper/molten_copper_flow");

    public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_COPPER = ((FluidBuilder) CreateMetallurgy.REGISTRATE.fluid("molten_copper", MOLTEN_COPPER_STILL_RL, MOLTEN_COPPER_FLOW_RL).properties(b -> b.viscosity(2000).density(1400).lightLevel(10).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).canHydrate(false).canDrown(false).canSwim(false)).fluidProperties(p -> p.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(100.0F)).tag(new TagKey[] { AllTags.forgeFluidTag("molten_copper"), AllTags.forgeFluidTag("molten_materials") }).source(ForgeFlowingFluid.Source::new).bucket().build()).register();

    public static final ResourceLocation MOLTEN_ZINC_STILL_RL = CreateMetallurgy.genRL("fluid/zinc/molten_zinc_still");

    public static final ResourceLocation MOLTEN_ZINC_FLOW_RL = CreateMetallurgy.genRL("fluid/zinc/molten_zinc_flow");

    public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_ZINC = ((FluidBuilder) CreateMetallurgy.REGISTRATE.fluid("molten_zinc", MOLTEN_ZINC_STILL_RL, MOLTEN_ZINC_FLOW_RL).properties(b -> b.viscosity(2000).density(1400).lightLevel(10).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).canHydrate(false).canDrown(false).canSwim(false)).fluidProperties(p -> p.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(100.0F)).tag(new TagKey[] { AllTags.forgeFluidTag("molten_zinc"), AllTags.forgeFluidTag("molten_materials") }).source(ForgeFlowingFluid.Source::new).bucket().build()).register();

    public static final ResourceLocation MOLTEN_BRASS_STILL_RL = CreateMetallurgy.genRL("fluid/brass/molten_brass_still");

    public static final ResourceLocation MOLTEN_BRASS_FLOW_RL = CreateMetallurgy.genRL("fluid/brass/molten_brass_flow");

    public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_BRASS = ((FluidBuilder) CreateMetallurgy.REGISTRATE.fluid("molten_brass", MOLTEN_BRASS_STILL_RL, MOLTEN_BRASS_FLOW_RL).properties(b -> b.viscosity(2000).density(1400).lightLevel(10).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).canHydrate(false).canDrown(false).canSwim(false)).fluidProperties(p -> p.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(100.0F)).tag(new TagKey[] { AllTags.forgeFluidTag("molten_brass"), AllTags.forgeFluidTag("molten_materials") }).source(ForgeFlowingFluid.Source::new).bucket().build()).register();

    public static final ResourceLocation MOLTEN_TUNGSTEN_STILL_RL = CreateMetallurgy.genRL("fluid/tungsten/molten_tungsten_still");

    public static final ResourceLocation MOLTEN_TUNGSTEN_FLOW_RL = CreateMetallurgy.genRL("fluid/tungsten/molten_tungsten_flow");

    public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_TUNGSTEN = ((FluidBuilder) CreateMetallurgy.REGISTRATE.fluid("molten_tungsten", MOLTEN_TUNGSTEN_STILL_RL, MOLTEN_TUNGSTEN_FLOW_RL).properties(b -> b.viscosity(2000).density(1400).lightLevel(10).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).canHydrate(false).canDrown(false).canSwim(false)).fluidProperties(p -> p.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(100.0F)).tag(new TagKey[] { AllTags.forgeFluidTag("molten_tungsten"), AllTags.forgeFluidTag("molten_materials") }).source(ForgeFlowingFluid.Source::new).bucket().build()).register();

    public static final ResourceLocation MOLTEN_STEEL_STILL_RL = CreateMetallurgy.genRL("fluid/steel/molten_steel_still");

    public static final ResourceLocation MOLTEN_STEEL_FLOW_RL = CreateMetallurgy.genRL("fluid/steel/molten_steel_flow");

    public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_STEEL = ((FluidBuilder) CreateMetallurgy.REGISTRATE.fluid("molten_steel", MOLTEN_STEEL_STILL_RL, MOLTEN_STEEL_FLOW_RL).properties(b -> b.viscosity(2000).density(1400).lightLevel(10).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).canHydrate(false).canDrown(false).canSwim(false)).fluidProperties(p -> p.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(100.0F)).tag(new TagKey[] { AllTags.forgeFluidTag("molten_steel"), AllTags.forgeFluidTag("molten_materials") }).source(ForgeFlowingFluid.Source::new).bucket().build()).register();

    public static void register() {
    }
}