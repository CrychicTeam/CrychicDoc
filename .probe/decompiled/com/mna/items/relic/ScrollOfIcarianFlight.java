package com.mna.items.relic;

import com.mna.api.items.IRelic;
import com.mna.api.items.TieredItem;
import com.mna.api.spells.ICanContainSpell;
import com.mna.api.spells.SpellCastingResult;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.collections.Components;
import com.mna.api.spells.collections.Shapes;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.items.ItemInit;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ScrollOfIcarianFlight extends TieredItem implements ICanContainSpell, IRelic {

    public ScrollOfIcarianFlight() {
        super(new Item.Properties().stacksTo(3));
    }

    public ScrollOfIcarianFlight(Item.Properties properties) {
        super(properties);
    }

    public static ItemStack create() {
        ItemStack spellStack = new ItemStack(ItemInit.ICARIAN_FLIGHT.get());
        if (Math.random() < 0.05F) {
            spellStack.setHoverName(Component.literal("Scroll of Icarian Fart"));
        }
        spellStack.setCount(3);
        return spellStack;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> lines, TooltipFlag flags) {
        super.m_7373_(stack, world, lines, flags);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack held = player.m_21120_(hand);
        if (!world.isClientSide) {
            SpellRecipe recipe = new SpellRecipe();
            recipe.setShape(Shapes.SELF);
            recipe.addComponent(Components.ICARIAN_FLIGHT);
            recipe.getComponent(0).setValue(Attribute.DURATION, 5.0F);
            recipe.writeToNBT(held.getOrCreateTag());
            SpellCastingResult res = SpellCaster.Affect(held, recipe, world, new SpellSource(player, hand));
            if (res.getCode().isConsideredSuccess() && !world.isClientSide && player.m_21211_() != held) {
                held.shrink(1);
                if (held.isEmpty() && !player.isCreative()) {
                    player.m_21166_(hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                }
            }
        }
        return InteractionResultHolder.sidedSuccess(held, world.isClientSide);
    }

    protected boolean shouldConsumeMana(ItemStack stack) {
        return false;
    }

    protected boolean shouldConsumeChanneledMana(Player player, ItemStack stack) {
        return false;
    }
}