package com.rekindled.embers.datagen;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.compat.curios.CuriosCompat;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.minecraftforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class EmbersItemModels extends ItemModelProvider {

    public EmbersItemModels(PackOutput generator, ExistingFileHelper existingFileHelper) {
        super(generator, "embers", existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (RegistryManager.FluidStuff fluid : RegistryManager.fluidList) {
            this.bucketModel(fluid.FLUID_BUCKET, fluid.FLUID.get());
        }
        this.itemWithModel(RegistryManager.TINKER_HAMMER, "item/handheld");
        this.basicItem(RegistryManager.TINKER_LENS.get());
        this.basicItem(RegistryManager.SMOKY_TINKER_LENS.get());
        this.basicItem(RegistryManager.ANCIENT_CODEX.get());
        this.layeredItem(RegistryManager.EMBER_JAR, "item/generated", "ember_jar_glass", "ember_jar_glass_shine", "ember_jar");
        this.layeredItem(RegistryManager.EMBER_CARTRIDGE, "item/generated", "ember_cartridge_glass", "ember_cartridge_glass_shine", "ember_cartridge");
        this.layeredItem(CuriosCompat.EMBER_BULB, "item/generated", "ember_bulb_glass", "ember_bulb_glass_shine", "ember_bulb");
        this.itemWithTexture(RegistryManager.MUSIC_DISC_7F_PATTERNS, "music_disc_ember");
        this.basicItem(RegistryManager.ALCHEMICAL_WASTE.get());
        ((SeparateTransformsModelBuilder) this.withExistingParent(RegistryManager.ALCHEMICAL_NOTE.getId().getPath(), new ResourceLocation("forge", "item/default")).customLoader(SeparateTransformsModelBuilder::begin)).base(this.basicItem(new ResourceLocation("embers", "alchemical_note_item"))).perspective(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, this.nested().parent(new ModelFile.UncheckedModelFile(new ResourceLocation("builtin/entity")))).perspective(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, this.nested().parent(new ModelFile.UncheckedModelFile(new ResourceLocation("builtin/entity")))).perspective(ItemDisplayContext.FIXED, this.nested().parent(new ModelFile.UncheckedModelFile(new ResourceLocation("builtin/entity"))));
        this.basicItem(RegistryManager.CODEBREAKING_SLATE.get());
        this.layeredItem(RegistryManager.TYRFING, "item/handheld", "tyrfing", "tyrfing_gem");
        this.withExistingParent(RegistryManager.INFLICTOR_GEM.getId().getPath(), new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation("embers", "item/inflictor_gem")).override().predicate(new ResourceLocation("embers", "charged"), 1.0F).model(this.singleTexture("inflictor_gem_charged", new ResourceLocation("item/generated"), "layer0", new ResourceLocation("embers", "item/inflictor_gem_charged")));
        this.basicItem(RegistryManager.ASHEN_GOGGLES.get());
        this.basicItem(RegistryManager.ASHEN_CLOAK.get());
        this.basicItem(RegistryManager.ASHEN_LEGGINGS.get());
        this.basicItem(RegistryManager.ASHEN_BOOTS.get());
        this.basicItem(RegistryManager.GLIMMER_CRYSTAL.get());
        this.basicItem(RegistryManager.GLIMMER_LAMP.get());
        this.basicItem(RegistryManager.EMBER_CRYSTAL.get());
        this.basicItem(RegistryManager.EMBER_SHARD.get());
        this.basicItem(RegistryManager.EMBER_GRIT.get());
        this.basicItem(RegistryManager.CAMINITE_BLEND.get());
        this.basicItem(RegistryManager.CAMINITE_BRICK.get());
        this.basicItem(RegistryManager.ARCHAIC_BRICK.get());
        this.basicItem(RegistryManager.ANCIENT_MOTIVE_CORE.get());
        this.basicItem(RegistryManager.ASH.get());
        this.basicItem(RegistryManager.ASHEN_FABRIC.get());
        this.basicItem(RegistryManager.EMBER_CRYSTAL_CLUSTER.get());
        this.basicItem(RegistryManager.WILDFIRE_CORE.get());
        this.basicItem(RegistryManager.ISOLATED_MATERIA.get());
        this.basicItem(RegistryManager.ADHESIVE.get());
        this.basicItem(RegistryManager.ARCHAIC_CIRCUIT.get());
        this.basicItem(RegistryManager.SUPERHEATER.get());
        this.basicItem(RegistryManager.CINDER_JET.get());
        this.basicItem(RegistryManager.BLASTING_CORE.get());
        this.basicItem(RegistryManager.CASTER_ORB.get());
        this.basicItem(RegistryManager.RESONATING_BELL.get());
        this.basicItem(RegistryManager.FLAME_BARRIER.get());
        this.basicItem(RegistryManager.ELDRITCH_INSIGNIA.get());
        this.basicItem(RegistryManager.INTELLIGENT_APPARATUS.get());
        this.basicItem(RegistryManager.DIFFRACTION_BARREL.get());
        this.basicItem(RegistryManager.FOCAL_LENS.get());
        this.basicItem(RegistryManager.SHIFTING_SCALES.get());
        this.basicItem(RegistryManager.WINDING_GEARS.get());
        this.itemWithTexture(RegistryManager.RAW_CAMINITE_PLATE, "plate_caminite_raw");
        this.itemWithTexture(RegistryManager.RAW_FLAT_STAMP, "flat_stamp_raw");
        this.itemWithTexture(RegistryManager.RAW_INGOT_STAMP, "ingot_stamp_raw");
        this.itemWithTexture(RegistryManager.RAW_NUGGET_STAMP, "nugget_stamp_raw");
        this.itemWithTexture(RegistryManager.RAW_PLATE_STAMP, "plate_stamp_raw");
        this.itemWithTexture(RegistryManager.RAW_GEAR_STAMP, "gear_stamp_raw");
        this.itemWithTexture(RegistryManager.CAMINITE_PLATE, "plate_caminite");
        this.basicItem(RegistryManager.FLAT_STAMP.get());
        this.basicItem(RegistryManager.INGOT_STAMP.get());
        this.basicItem(RegistryManager.NUGGET_STAMP.get());
        this.basicItem(RegistryManager.PLATE_STAMP.get());
        this.basicItem(RegistryManager.GEAR_STAMP.get());
        this.itemWithTexture(RegistryManager.IRON_ASPECTUS, "aspectus_iron");
        this.itemWithTexture(RegistryManager.COPPER_ASPECTUS, "aspectus_copper");
        this.itemWithTexture(RegistryManager.LEAD_ASPECTUS, "aspectus_lead");
        this.itemWithTexture(RegistryManager.SILVER_ASPECTUS, "aspectus_silver");
        this.itemWithTexture(RegistryManager.DAWNSTONE_ASPECTUS, "aspectus_dawnstone");
        this.itemWithTexture(RegistryManager.IRON_PLATE, "plate_iron");
        this.itemWithTexture(RegistryManager.COPPER_PLATE, "plate_copper");
        this.itemWithTexture(RegistryManager.COPPER_NUGGET, "nugget_copper");
        this.basicItem(RegistryManager.RAW_LEAD.get());
        this.itemWithTexture(RegistryManager.LEAD_INGOT, "ingot_lead");
        this.itemWithTexture(RegistryManager.LEAD_NUGGET, "nugget_lead");
        this.itemWithTexture(RegistryManager.LEAD_PLATE, "plate_lead");
        this.basicItem(RegistryManager.RAW_SILVER.get());
        this.itemWithTexture(RegistryManager.SILVER_INGOT, "ingot_silver");
        this.itemWithTexture(RegistryManager.SILVER_NUGGET, "nugget_silver");
        this.itemWithTexture(RegistryManager.SILVER_PLATE, "plate_silver");
        this.itemWithTexture(RegistryManager.DAWNSTONE_INGOT, "ingot_dawnstone");
        this.itemWithTexture(RegistryManager.DAWNSTONE_NUGGET, "nugget_dawnstone");
        this.itemWithTexture(RegistryManager.DAWNSTONE_PLATE, "plate_dawnstone");
        this.toolModels(RegistryManager.LEAD_TOOLS);
        this.toolModels(RegistryManager.SILVER_TOOLS);
        this.toolModels(RegistryManager.DAWNSTONE_TOOLS);
        this.basicItem(CuriosCompat.EMBER_RING.get());
        this.basicItem(CuriosCompat.EMBER_BELT.get());
        this.basicItem(CuriosCompat.EMBER_AMULET.get());
        this.basicItem(CuriosCompat.DAWNSTONE_MAIL.get());
        this.basicItem(CuriosCompat.ASHEN_AMULET.get());
        this.basicItem(CuriosCompat.NONBELEIVER_AMULET.get());
        this.basicItem(CuriosCompat.EXPLOSION_CHARM.get());
        this.spawnEgg(RegistryManager.ANCIENT_GOLEM_SPAWN_EGG);
        this.buttonInventory(RegistryManager.CAMINITE_BUTTON_ITEM.getId().getPath(), new ResourceLocation("embers", "block/caminite_button"));
    }

    public void itemWithModel(RegistryObject<? extends Item> registryObject, String model) {
        ResourceLocation id = registryObject.getId();
        ResourceLocation textureLocation = new ResourceLocation(id.getNamespace(), "item/" + id.getPath());
        this.singleTexture(id.getPath(), new ResourceLocation(model), "layer0", textureLocation);
    }

    public void spawnEgg(RegistryObject<? extends Item> registryObject) {
        this.withExistingParent(registryObject.getId().getPath(), "item/template_spawn_egg");
    }

    public void itemWithTexture(RegistryObject<? extends Item> registryObject, String texture) {
        this.itemWithTexture(registryObject, "item/generated", texture);
    }

    public void itemWithTexture(RegistryObject<? extends Item> registryObject, String model, String texture) {
        ResourceLocation id = registryObject.getId();
        ResourceLocation textureLocation = new ResourceLocation(id.getNamespace(), "item/" + texture);
        this.singleTexture(id.getPath(), new ResourceLocation(model), "layer0", textureLocation);
    }

    public void layeredItem(RegistryObject<? extends Item> registryObject, String model, String... textures) {
        ResourceLocation id = registryObject.getId();
        ModelBuilder<?> builder = this.withExistingParent(id.getPath(), new ResourceLocation(model));
        for (int i = 0; i < textures.length; i++) {
            builder.texture("layer" + i, new ResourceLocation(id.getNamespace(), "item/" + textures[i]));
        }
    }

    public void bucketModel(RegistryObject<? extends BucketItem> registryObject, Fluid fluid) {
        ModelBuilder<ItemModelBuilder> builder = this.withExistingParent(registryObject.getId().getPath(), new ResourceLocation("embers", "item/bucket_fluid"));
        builder.<DynamicFluidContainerModelBuilder>customLoader(DynamicFluidContainerModelBuilder::begin).fluid(fluid).coverIsMask(false).flipGas(true).end();
    }

    public void toolModels(RegistryManager.ToolSet set) {
        this.itemWithTexture(set.SWORD, "item/handheld", "sword_" + set.name);
        this.itemWithTexture(set.SHOVEL, "item/handheld", "shovel_" + set.name);
        this.itemWithTexture(set.PICKAXE, "item/handheld", "pickaxe_" + set.name);
        this.itemWithTexture(set.AXE, "item/handheld", "axe_" + set.name);
        this.itemWithTexture(set.HOE, "item/handheld", "hoe_" + set.name);
    }
}