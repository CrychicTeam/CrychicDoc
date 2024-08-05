package noppes.npcs.constants;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public enum EnumCompanionTalent {

    INVENTORY(Item.byBlock(Blocks.CRAFTING_TABLE)),
    ARMOR(Items.IRON_CHESTPLATE),
    SWORD(Items.DIAMOND_SWORD),
    RANGED(Items.BOW),
    ACROBATS(Items.LEATHER_BOOTS),
    INTEL(Items.BOOK);

    public ItemStack item;

    private EnumCompanionTalent(Item item) {
        this.item = new ItemStack(item);
    }
}