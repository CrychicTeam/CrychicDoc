package net.minecraft.world.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class ShieldItem extends Item implements Equipable {

    public static final int EFFECTIVE_BLOCK_DELAY = 5;

    public static final float MINIMUM_DURABILITY_DAMAGE = 3.0F;

    public static final String TAG_BASE_COLOR = "Base";

    public ShieldItem(Item.Properties itemProperties0) {
        super(itemProperties0);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public String getDescriptionId(ItemStack itemStack0) {
        return BlockItem.getBlockEntityData(itemStack0) != null ? this.m_5524_() + "." + getColor(itemStack0).getName() : super.getDescriptionId(itemStack0);
    }

    @Override
    public void appendHoverText(ItemStack itemStack0, @Nullable Level level1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
        BannerItem.appendHoverTextFromBannerBlockEntityTag(itemStack0, listComponent2);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack0) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack itemStack0) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        ItemStack $$3 = player1.m_21120_(interactionHand2);
        player1.m_6672_(interactionHand2);
        return InteractionResultHolder.consume($$3);
    }

    @Override
    public boolean isValidRepairItem(ItemStack itemStack0, ItemStack itemStack1) {
        return itemStack1.is(ItemTags.PLANKS) || super.isValidRepairItem(itemStack0, itemStack1);
    }

    public static DyeColor getColor(ItemStack itemStack0) {
        CompoundTag $$1 = BlockItem.getBlockEntityData(itemStack0);
        return $$1 != null ? DyeColor.byId($$1.getInt("Base")) : DyeColor.WHITE;
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.OFFHAND;
    }
}