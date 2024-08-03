package com.mna.items.artifice.curio;

import com.mna.api.faction.IFaction;
import com.mna.api.items.ChargeableItem;
import com.mna.factions.Factions;
import java.util.UUID;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;

public class ItemTrickeryBracelet extends ChargeableItem implements IPreEnchantedItem<ItemTrickeryBracelet> {

    private static final float MAX_IRE = 1.6666666E-4F;

    private static final AttributeModifier TRICKERY_SPEED_BOOST = new AttributeModifier(UUID.fromString("4c9ebd60-cb05-4ad3-99db-0f2d99b32013"), "Trickery Speed Boost", 0.25, AttributeModifier.Operation.ADDITION);

    public ItemTrickeryBracelet() {
        super(new Item.Properties().setNoRepair().rarity(Rarity.UNCOMMON), 6000.0F);
    }

    @Override
    public float getMaxIre() {
        return 1.6666666E-4F;
    }

    @Override
    protected boolean tickEffect(ItemStack stack, Player player, Level world, int slot, float mana, boolean selected) {
        if (player.m_6047_()) {
            CuriosApi.getCuriosHelper().findFirstCurio(player, this).ifPresent(t -> t.stack().hurtAndBreak(1, player, damager -> CuriosApi.getCuriosHelper().onBrokenCurio(t.slotContext())));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public IFaction getFaction() {
        return Factions.FEY;
    }

    public static void ApplyMoveSpeed(Player player) {
        AttributeInstance attr = player.m_21051_(Attributes.MOVEMENT_SPEED);
        if (!attr.hasModifier(TRICKERY_SPEED_BOOST)) {
            attr.addTransientModifier(TRICKERY_SPEED_BOOST);
        }
    }

    public static void RemoveMoveSpeed(Player player) {
        AttributeInstance attr = player.m_21051_(Attributes.MOVEMENT_SPEED);
        if (attr.hasModifier(TRICKERY_SPEED_BOOST)) {
            attr.removeModifier(TRICKERY_SPEED_BOOST);
        }
    }
}