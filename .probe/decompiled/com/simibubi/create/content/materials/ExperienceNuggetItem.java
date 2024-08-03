package com.simibubi.create.content.materials;

import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ExperienceNuggetItem extends Item {

    public ExperienceNuggetItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemInHand = pPlayer.m_21120_(pUsedHand);
        if (pLevel.isClientSide) {
            pLevel.playSound(pPlayer, pPlayer.m_20183_(), SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.PLAYERS, 0.5F, 1.0F);
            return InteractionResultHolder.consume(itemInHand);
        } else {
            int amountUsed = pPlayer.m_6144_() ? 1 : itemInHand.getCount();
            int total = Mth.ceil(3.0F * (float) amountUsed);
            int maxOrbs = amountUsed == 1 ? 1 : 5;
            int valuePer = Math.max(1, 1 + total / maxOrbs);
            for (int i = 0; i < maxOrbs; i++) {
                int value = Math.min(valuePer, total - i * valuePer);
                if (value != 0) {
                    Vec3 offset = VecHelper.offsetRandomly(Vec3.ZERO, pLevel.random, 1.0F).normalize();
                    Vec3 look = pPlayer.m_20154_();
                    Vec3 motion = look.scale(0.2).add(0.0, 0.2, 0.0).add(offset.scale(0.1));
                    Vec3 cross = look.cross(VecHelper.rotate(new Vec3(-0.75, 0.0, 0.0), (double) (-pPlayer.m_146908_()), Direction.Axis.Y));
                    Vec3 global = offset.add(pPlayer.m_20318_(1.0F));
                    global = pPlayer.m_146892_().add(look.scale(0.5)).add(cross);
                    ExperienceOrb xp = new ExperienceOrb(pLevel, global.x, global.y, global.z, value);
                    xp.m_20256_(motion);
                    pLevel.m_7967_(xp);
                }
            }
            itemInHand.shrink(amountUsed);
            if (!itemInHand.isEmpty()) {
                return InteractionResultHolder.success(itemInHand);
            } else {
                pPlayer.m_21008_(pUsedHand, ItemStack.EMPTY);
                return InteractionResultHolder.consume(itemInHand);
            }
        }
    }
}