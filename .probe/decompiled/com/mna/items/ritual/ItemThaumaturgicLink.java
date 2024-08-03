package com.mna.items.ritual;

import com.mna.ManaAndArtifice;
import com.mna.tools.BiomeUtils;
import com.mna.tools.StructureUtils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ItemThaumaturgicLink extends Item {

    private static final String NBT_RECIPEID = "linked_position";

    private static final String NBT_LINK_TYPE = "link_type";

    public static final String LINK_TYPE_BIOME = "biome";

    public static final String LINK_TYPE_STRUCTURE = "structure";

    private static ArrayList<ResourceLocation> all_structure_locations;

    private static ArrayList<ResourceLocation> all_biome_locations;

    public ItemThaumaturgicLink() {
        super(new Item.Properties());
    }

    private ArrayList<ResourceLocation> getAllStructureLocations(ServerLevel world) {
        if (all_structure_locations == null) {
            all_structure_locations = new ArrayList();
            StructureUtils.getAllStructureIDs(world).forEach(s -> all_structure_locations.add(s));
        }
        return all_structure_locations;
    }

    private ArrayList<ResourceLocation> getAllBiomeLocations(ServerLevel world) {
        if (all_biome_locations == null) {
            all_biome_locations = new ArrayList();
            BiomeUtils.getAllBiomeIDs(world).forEach(s -> all_biome_locations.add(s));
        }
        return all_biome_locations;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        ResourceLocation recipe = this.getLocationKey(stack);
        if (recipe != null) {
            tooltip.add(Component.translatable(recipe.toString()));
        }
    }

    @Nullable
    public ResourceLocation getLocationKey(ItemStack stack) {
        if (stack.hasTag() && stack.getItem() == this) {
            CompoundTag nbt = stack.getTag();
            return !nbt.contains("linked_position") ? null : new ResourceLocation(nbt.get("linked_position").getAsString());
        } else {
            return null;
        }
    }

    public boolean isStructure(ItemStack stack) {
        if (stack.hasTag() && stack.getItem() == this) {
            CompoundTag nbt = stack.getTag();
            return !nbt.contains("link_type") ? false : nbt.getString("link_type") == "structure";
        } else {
            return false;
        }
    }

    public boolean isBiome(ItemStack stack) {
        if (stack.hasTag() && stack.getItem() == this) {
            CompoundTag nbt = stack.getTag();
            return !nbt.contains("link_type") ? false : nbt.getString("link_type") == "biome";
        } else {
            return false;
        }
    }

    public static void setBiomeKey(ServerLevel level, ItemStack stack, Holder<Biome> biome) {
        CompoundTag nbt = stack.getOrCreateTag();
        ResourceLocation key = BiomeUtils.getBiomeID(level, biome);
        if (key != null) {
            nbt.putString("linked_position", key.toString());
            nbt.putString("link_type", "biome");
        } else {
            ManaAndArtifice.LOGGER.error("Failed to look up biome key, not setting location.");
        }
    }

    public static void setStructureKey(ServerLevel level, ItemStack stack, Holder<Structure> structure) {
        CompoundTag nbt = stack.getOrCreateTag();
        ResourceLocation key = StructureUtils.getStructureID(level, structure);
        if (key != null) {
            nbt.putString("linked_position", key.toString());
            nbt.putString("link_type", "structure");
        } else {
            ManaAndArtifice.LOGGER.error("Failed to look up structure key, not setting location.");
        }
    }

    public ItemStack getRandomLink(ServerLevel world) {
        ItemStack output = new ItemStack(this);
        if (world.f_46441_.nextBoolean()) {
            output.getOrCreateTag().putString("linked_position", ((ResourceLocation) this.getAllBiomeLocations(world).get((int) (Math.random() * (double) this.getAllBiomeLocations(world).size()))).toString());
            output.getOrCreateTag().putString("link_type", "biome");
        } else {
            output.getOrCreateTag().putString("linked_position", ((ResourceLocation) this.getAllStructureLocations(world).get((int) (Math.random() * (double) this.getAllStructureLocations(world).size()))).toString());
            output.getOrCreateTag().putString("link_type", "structure");
        }
        return output;
    }
}