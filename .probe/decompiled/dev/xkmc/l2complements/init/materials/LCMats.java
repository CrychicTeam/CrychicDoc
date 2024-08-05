package dev.xkmc.l2complements.init.materials;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2complements.content.item.equipments.EterniumArmor;
import dev.xkmc.l2complements.content.item.equipments.EterniumTool;
import dev.xkmc.l2complements.content.item.equipments.PoseiditeArmor;
import dev.xkmc.l2complements.content.item.equipments.PoseiditeTool;
import dev.xkmc.l2complements.content.item.equipments.SculkiumArmor;
import dev.xkmc.l2complements.content.item.equipments.SculkiumTool;
import dev.xkmc.l2complements.content.item.equipments.ShulkerateArmor;
import dev.xkmc.l2complements.content.item.equipments.ShulkerateTool;
import dev.xkmc.l2complements.content.item.equipments.TotemicArmor;
import dev.xkmc.l2complements.content.item.equipments.TotemicTool;
import dev.xkmc.l2complements.init.data.TagGen;
import dev.xkmc.l2complements.init.registrate.LCBlocks;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2damagetracker.contents.materials.api.ArmorConfig;
import dev.xkmc.l2damagetracker.contents.materials.api.ArmorMat;
import dev.xkmc.l2damagetracker.contents.materials.api.ArmorStats;
import dev.xkmc.l2damagetracker.contents.materials.api.IMatVanillaType;
import dev.xkmc.l2damagetracker.contents.materials.api.IToolStats;
import dev.xkmc.l2damagetracker.contents.materials.api.ToolConfig;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraArmorConfig;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2damagetracker.contents.materials.vanilla.GenItemVanillaType;
import dev.xkmc.l2damagetracker.contents.materials.vanilla.ToolStats;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeTier;

public enum LCMats implements IMatVanillaType {

    TOTEMIC_GOLD("totemic_gold", 3, SoundEvents.ARMOR_EQUIP_GOLD, new ToolStats(1000, 12, 7, 1.0F, 22), new ArmorStats(15, new int[] { 2, 5, 6, 2 }, 0.0F, 0.0F, 25), GenItemVanillaType.TOOL_GEN, GenItemVanillaType.ARMOR_GEN, new TotemicTool().setStick(e -> Items.EMERALD, true), new TotemicArmor(), ChatFormatting.YELLOW), POSEIDITE("poseidite", 4, SoundEvents.ARMOR_EQUIP_IRON, new ToolStats(1500, 8, 7, 1.0F, 14), new ArmorStats(33, new int[] { 3, 6, 8, 3 }, 2.0F, 0.0F, 9), GenItemVanillaType.TOOL_GEN, GenItemVanillaType.ARMOR_GEN, new PoseiditeTool().setStick(e -> Items.PRISMARINE_SHARD, false), new PoseiditeArmor(), ChatFormatting.AQUA), SHULKERATE("shulkerate", 4, SoundEvents.ARMOR_EQUIP_IRON, new ToolStats(4000, 8, 7, 1.0F, 14), new ArmorStats(400, new int[] { 3, 6, 8, 3 }, 2.0F, 0.0F, 9), GenItemVanillaType.TOOL_GEN, GenItemVanillaType.ARMOR_GEN, new ShulkerateTool().setStick(e -> Items.IRON_INGOT, false), new ShulkerateArmor(), ChatFormatting.LIGHT_PURPLE), SCULKIUM("sculkium", 5, SoundEvents.ARMOR_EQUIP_IRON, new ToolStats(2000, 8, 9, 1.2F, 15), new ArmorStats(100, new int[] { 5, 9, 10, 6 }, 4.0F, 1.0F, 15), GenItemVanillaType.TOOL_GEN, GenItemVanillaType.ARMOR_GEN, new SculkiumTool().setStick(e -> Items.NETHERITE_INGOT, false).setTier(e -> TagGen.REQUIRES_SCULK_TOOL), new SculkiumArmor(), ChatFormatting.DARK_AQUA), ETERNIUM("eternium", 5, SoundEvents.ARMOR_EQUIP_IRON, new ToolStats(9999, 8, 7, 1.0F, 1), new ArmorStats(9999, new int[] { 3, 6, 8, 3 }, 10.0F, 1.0F, 1), GenItemVanillaType.TOOL_GEN, GenItemVanillaType.ARMOR_GEN, new EterniumTool().damageChance(0.0).repairChance(1.0).setStick(e -> (Item) LCItems.EXPLOSION_SHARD.get(), false), new EterniumArmor().damageChance(0.0).repairChance(1.0), ChatFormatting.BLUE);

    final String id;

    final Tier tier;

    final ArmorMaterial mat;

    final ToolConfig tool_config;

    final ArmorConfig armor_config;

    final IToolStats tool_stats;

    final ExtraToolConfig tool_extra;

    final ExtraArmorConfig armor_extra;

    public final ChatFormatting trim_text_color;

    private LCMats(String name, int level, SoundEvent equip_sound, IToolStats tool, ArmorStats armor, ToolConfig tool_config, ArmorConfig armor_config, ExtraToolConfig tool_extra, ExtraArmorConfig armor_extra, ChatFormatting trimTextColor) {
        this.trim_text_color = trimTextColor;
        Supplier<Ingredient> ing = () -> Ingredient.of((ItemLike) LCItems.MAT_INGOTS[this.ordinal()].get());
        this.id = name;
        this.tier = new ForgeTier(level, tool.durability(), (float) tool.speed(), 0.0F, tool.enchant(), tool_extra.getTier(level), ing);
        this.mat = new ArmorMat(this.armorPrefix(), armor.durability(), armor.protection(), armor.enchant(), equip_sound, armor.tough(), armor.kb(), ing);
        this.tool_config = tool_config;
        this.armor_config = armor_config;
        this.tool_stats = tool;
        this.tool_extra = tool_extra;
        this.armor_extra = armor_extra;
    }

    public Item getIngot() {
        return (Item) LCItems.MAT_INGOTS[this.ordinal()].get();
    }

    public Item getNugget() {
        return (Item) LCItems.MAT_NUGGETS[this.ordinal()].get();
    }

    public Block getBlock() {
        return (Block) LCBlocks.GEN_BLOCK[this.ordinal()].get();
    }

    public String armorPrefix() {
        return "l2complements:" + this.id;
    }

    public String getID() {
        return this.id;
    }

    public ArmorConfig getArmorConfig() {
        return this.armor_config;
    }

    public ToolConfig getToolConfig() {
        return this.tool_config;
    }

    public IToolStats getToolStats() {
        return this.tool_stats;
    }

    public Tier getTier() {
        return this.tier;
    }

    public ExtraArmorConfig getExtraArmorConfig() {
        return this.armor_extra;
    }

    public ArmorMaterial getArmorMaterial() {
        return this.mat;
    }

    public ExtraToolConfig getExtraToolConfig() {
        return this.tool_extra;
    }

    public ItemEntry<Item>[][] getGenerated() {
        return LCItems.GEN_ITEM;
    }
}