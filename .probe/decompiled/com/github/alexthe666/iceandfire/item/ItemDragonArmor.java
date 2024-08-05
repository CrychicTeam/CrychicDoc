package com.github.alexthe666.iceandfire.item;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class ItemDragonArmor extends Item {

    public ItemDragonArmor.DragonArmorType type;

    public int dragonSlot;

    public String name;

    private Pattern baseName = Pattern.compile("[a-z]+_[a-z]+");

    public ItemDragonArmor(ItemDragonArmor.DragonArmorType type, int dragonSlot) {
        super(new Item.Properties().stacksTo(1));
        this.type = type;
        this.dragonSlot = dragonSlot;
        if (type == ItemDragonArmor.DragonArmorType.FIRE || type == ItemDragonArmor.DragonArmorType.ICE || type == ItemDragonArmor.DragonArmorType.LIGHTNING) {
            this.baseName = Pattern.compile("[a-z]+_[a-z]+_[a-z]+");
        }
    }

    @NotNull
    @Override
    public String getDescriptionId() {
        String fullName = ForgeRegistries.ITEMS.getKey(this).getPath();
        Matcher matcher = this.baseName.matcher(fullName);
        this.name = matcher.find() ? matcher.group() : fullName;
        return "item.iceandfire." + this.name;
    }

    static String getNameForSlot(int slot) {
        return switch(slot) {
            case 0 ->
                "head";
            case 1 ->
                "neck";
            case 2 ->
                "body";
            case 3 ->
                "tail";
            default ->
                "";
        };
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        String words = switch(this.dragonSlot) {
            case 1 ->
                "dragon.armor_neck";
            case 2 ->
                "dragon.armor_body";
            case 3 ->
                "dragon.armor_tail";
            default ->
                "dragon.armor_head";
        };
        tooltip.add(Component.translatable(words).withStyle(ChatFormatting.GRAY));
    }

    public static enum DragonArmorType {

        IRON,
        GOLD,
        DIAMOND,
        SILVER,
        FIRE,
        ICE,
        COPPER,
        LIGHTNING
    }
}