package com.github.alexmodguy.alexscaves.server.item;

import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class MagicConchItem extends Item {

    public MagicConchItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity player, int useTimeLeft) {
        level.playSound(null, player, ACSoundRegistry.MAGIC_CONCH_CAST.get(), SoundSource.RECORDS, 16.0F, 1.0F);
        int i = this.getUseDuration(stack) - useTimeLeft;
        boolean hurtRelations = false;
        if (i > 25) {
            if (stack.getEnchantmentLevel(ACEnchantmentRegistry.TAXING_BELLOW.get()) > 0) {
                stack.setDamageValue(Math.min(0, stack.getDamageValue() - 1));
                hurtRelations = true;
            } else {
                stack.hurtAndBreak(1, player, player1 -> player1.broadcastBreakEvent(player1.getUsedItemHand()));
            }
            RandomSource randomSource = player.getRandom();
            int time = 1200 + stack.getEnchantmentLevel(ACEnchantmentRegistry.LASTING_MORALE.get()) * 400;
            if (!level.isClientSide) {
                int chartingLevel = stack.getEnchantmentLevel(ACEnchantmentRegistry.CHARTING_CALL.get());
                DeepOneBaseEntity lastSummonedDeepOne = null;
                int maxNormal = 3 + randomSource.nextInt(1);
                int maxKnights = 2 + randomSource.nextInt(1);
                int maxMage = 1 + randomSource.nextInt(1);
                if (chartingLevel > 0) {
                    maxNormal += randomSource.nextInt(Math.max(chartingLevel - 1, 2));
                    maxKnights += randomSource.nextInt(Math.max(chartingLevel - 2, 0));
                    maxMage += randomSource.nextInt(Math.max(chartingLevel - 3, 0));
                }
                int normal = 0;
                int knights = 0;
                int mage = 0;
                int tries = 0;
                while (normal < maxNormal && tries < 99) {
                    tries++;
                    DeepOneBaseEntity summoned = this.summonDeepOne(ACEntityRegistry.DEEP_ONE.get(), player, time);
                    if (summoned != null) {
                        normal++;
                        lastSummonedDeepOne = summoned;
                    }
                }
                tries = 0;
                while (knights < maxKnights && tries < 99) {
                    tries++;
                    DeepOneBaseEntity summoned = this.summonDeepOne(ACEntityRegistry.DEEP_ONE_KNIGHT.get(), player, time);
                    if (summoned != null) {
                        knights++;
                        lastSummonedDeepOne = summoned;
                    }
                }
                tries = 0;
                while (mage < maxMage && tries < 99) {
                    tries++;
                    DeepOneBaseEntity summoned = this.summonDeepOne(ACEntityRegistry.DEEP_ONE_MAGE.get(), player, time);
                    if (summoned != null) {
                        mage++;
                        lastSummonedDeepOne = summoned;
                    }
                }
                if (hurtRelations && lastSummonedDeepOne != null) {
                    lastSummonedDeepOne.addReputation(player.m_20148_(), -2);
                }
            }
            if (player instanceof Player realPlayer) {
                realPlayer.awardStat(Stats.ITEM_USED.get(this));
                realPlayer.getCooldowns().addCooldown(this, time);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        player.m_6672_(hand);
        level.m_214171_(GameEvent.INSTRUMENT_PLAY, player.m_20182_(), GameEvent.Context.of(player));
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return stack.getCount() == 1;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 1200;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }

    private DeepOneBaseEntity summonDeepOne(EntityType type, LivingEntity summoner, int time) {
        RandomSource random = summoner.getRandom();
        BlockPos randomPos = summoner.m_20183_().offset(random.nextInt(20) - 10, 7, random.nextInt(20) - 10);
        while ((summoner.m_9236_().getFluidState(randomPos).is(FluidTags.WATER) || summoner.m_9236_().m_46859_(randomPos)) && randomPos.m_123342_() > summoner.m_9236_().m_141937_()) {
            randomPos = randomPos.below();
        }
        BlockState state = summoner.m_9236_().getBlockState(randomPos);
        if (!state.m_60819_().is(FluidTags.WATER) && !state.m_60634_(summoner.m_9236_(), randomPos, summoner)) {
            return null;
        } else {
            Vec3 at = Vec3.atCenterOf(randomPos).add(0.0, 0.5, 0.0);
            if (type.create(summoner.m_9236_()) instanceof DeepOneBaseEntity deepOne) {
                float f = random.nextFloat() * 360.0F;
                deepOne.m_7678_(at.x, at.y, at.z, f, -60.0F);
                deepOne.f_20883_ = f;
                deepOne.m_5616_(f);
                deepOne.setSummonedBy(summoner, time);
                deepOne.m_6518_((ServerLevel) summoner.m_9236_(), summoner.m_9236_().getCurrentDifficultyAt(BlockPos.containing(at)), MobSpawnType.TRIGGERED, (SpawnGroupData) null, (CompoundTag) null);
                if (deepOne.checkSpawnObstruction(summoner.m_9236_())) {
                    summoner.m_9236_().m_7967_(deepOne);
                    deepOne.copyTarget(summoner);
                    return deepOne;
                }
            }
            return null;
        }
    }
}