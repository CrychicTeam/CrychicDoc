package com.github.alexthe666.alexsmobs.item;

import com.github.alexthe666.alexsmobs.entity.util.RainbowUtil;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class ItemRainbowJelly extends Item {

    public ItemRainbowJelly(Item.Properties tab) {
        super(tab);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        int i = RainbowUtil.getRainbowTypeFromStack(stack);
        if (RainbowUtil.getRainbowType(target) == i) {
            return InteractionResult.PASS;
        } else {
            RainbowUtil.setRainbowType(target, i);
            RandomSource random = playerIn.m_217043_();
            for (int j = 0; j < 6 + random.nextInt(3); j++) {
                double d2 = random.nextGaussian() * 0.02;
                double d0 = random.nextGaussian() * 0.02;
                double d1 = random.nextGaussian() * 0.02;
                playerIn.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), target.m_20185_() + (double) (random.nextFloat() * target.m_20205_()) - (double) target.m_20205_() * 0.5, target.m_20186_() + (double) (target.m_20206_() * 0.5F) + (double) (random.nextFloat() * target.m_20206_() * 0.5F), target.m_20189_() + (double) (random.nextFloat() * target.m_20205_()) - (double) target.m_20205_() * 0.5, d0, d1, d2);
            }
            target.m_146850_(GameEvent.ITEM_INTERACT_START);
            target.m_5496_(SoundEvents.SLIME_SQUISH_SMALL, 1.0F, target.getVoicePitch());
            if (!playerIn.isCreative()) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack st, Level level, LivingEntity e) {
        RainbowUtil.setRainbowType(e, RainbowUtil.getRainbowTypeFromStack(st));
        return this.m_41472_() ? e.eat(level, st) : st;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return stack.getItem().isEdible() ? 64 : 0;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return super.isFoil(stack) || RainbowUtil.getRainbowTypeFromStack(stack) > 1;
    }

    public static enum RainbowType {

        RAINBOW,
        TRANS,
        NONBI,
        BI,
        ACE,
        WEEZER,
        BRAZIL;

        public static ItemRainbowJelly.RainbowType getFromString(String name) {
            if (name.contains("nonbi") || name.contains("non-bi")) {
                return NONBI;
            } else if (name.contains("trans")) {
                return TRANS;
            } else if (name.contains("bi")) {
                return BI;
            } else if (name.contains("asexual") || name.contains("ace")) {
                return ACE;
            } else if (name.contains("weezer")) {
                return WEEZER;
            } else {
                return name.contains("brazil") ? BRAZIL : RAINBOW;
            }
        }
    }
}