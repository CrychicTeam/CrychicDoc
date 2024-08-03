package net.minecraft.world.item;

import net.minecraft.tags.BlockTags;

public class PickaxeItem extends DiggerItem {

    protected PickaxeItem(Tier tier0, int int1, float float2, Item.Properties itemProperties3) {
        super((float) int1, float2, tier0, BlockTags.MINEABLE_WITH_PICKAXE, itemProperties3);
    }
}