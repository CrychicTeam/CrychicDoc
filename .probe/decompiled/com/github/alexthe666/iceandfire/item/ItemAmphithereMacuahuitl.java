package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemAmphithereMacuahuitl extends SwordItem {

    public ItemAmphithereMacuahuitl() {
        super(IafItemRegistry.AMPHITHERE_SWORD_TOOL_MATERIAL, 3, -2.4F, new Item.Properties());
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, LivingEntity targetEntity, LivingEntity attacker) {
        targetEntity.m_5496_(IafSoundRegistry.AMPHITHERE_GUST, 1.0F, 1.0F);
        targetEntity.m_5496_(SoundEvents.SHIELD_BLOCK, 1.0F, 1.0F);
        targetEntity.f_19812_ = true;
        double xRatio = (double) (-Mth.sin(attacker.m_146908_() * (float) (Math.PI / 180.0)));
        double zRatio = (double) Mth.cos(attacker.m_146908_() * (float) (Math.PI / 180.0));
        float strength = -0.6F;
        float f = Mth.sqrt((float) (xRatio * xRatio + zRatio * zRatio));
        targetEntity.m_20334_(targetEntity.m_20184_().x / 2.0 - xRatio / (double) f * (double) strength, 0.8, targetEntity.m_20184_().z / 2.0 - zRatio / (double) f * (double) strength);
        Random rand = new Random();
        for (int i = 0; i < 20; i++) {
            double d0 = rand.nextGaussian() * 0.02;
            double d1 = rand.nextGaussian() * 0.02;
            double d2 = rand.nextGaussian() * 0.02;
            double d3 = 10.0;
            targetEntity.m_9236_().addParticle(ParticleTypes.CLOUD, targetEntity.m_20185_() + (double) (rand.nextFloat() * targetEntity.m_20205_() * 5.0F) - (double) targetEntity.m_20205_() - d0 * 10.0, targetEntity.m_20186_() + (double) (rand.nextFloat() * targetEntity.m_20206_()) - d1 * 10.0, targetEntity.m_20189_() + (double) (rand.nextFloat() * targetEntity.m_20205_() * 5.0F) - (double) targetEntity.m_20205_() - d2 * 10.0, d0, d1, d2);
        }
        return super.hurtEnemy(stack, targetEntity, attacker);
    }

    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return true;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.iceandfire.legendary_weapon.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.amphithere_macuahuitl.desc_0").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.amphithere_macuahuitl.desc_1").withStyle(ChatFormatting.GRAY));
    }
}