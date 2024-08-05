package com.mna.items.artifact;

import com.mna.api.items.IShowHud;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.entities.boss.attacks.ThrownAllfatherAxe;
import com.mna.items.ItemInit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class AllfatherAxe extends AxeItem implements IShowHud {

    private static final float THROW_MANA_COST = 50.0F;

    public AllfatherAxe() {
        super(Tiers.NETHERITE, 10.0F, 1.0F, new Item.Properties().rarity(Rarity.EPIC));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        MutableBoolean didThrow = new MutableBoolean(false);
        player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
            if (m.getCastingResource().hasEnough(player, 50.0F)) {
                m.getCastingResource().consume(player, 50.0F);
                didThrow.setTrue();
                if (!world.isClientSide) {
                    ItemStack playerStack = player.m_21120_(hand);
                    ThrownAllfatherAxe thrown = new ThrownAllfatherAxe(player, world, playerStack.getTag());
                    Vec3 look = player.m_20154_();
                    thrown.shoot(look.x(), look.y, look.z, 2.0F, 0.0F);
                    world.m_7967_(thrown);
                    ItemStack control = new ItemStack(ItemInit.ALLFATHER_AXE_CONTROL.get());
                    AllfatherAxeControl.setAxe(control, thrown);
                    player.getCooldowns().addCooldown(ItemInit.ALLFATHER_AXE_CONTROL.get(), 10);
                    player.m_21008_(hand, control);
                }
            }
        });
        return didThrow.getValue() ? InteractionResultHolder.success(player.m_21120_(hand)) : super.m_7203_(world, player, hand);
    }

    public boolean isDamageable(ItemStack stack) {
        return false;
    }
}