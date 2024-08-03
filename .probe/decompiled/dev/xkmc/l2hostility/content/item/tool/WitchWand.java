package dev.xkmc.l2hostility.content.item.tool;

import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.base.TargetEffectTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.base.effects.EffectBuilder;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class WitchWand extends Item {

    private static MobEffectInstance getRandom(int maxRank, RandomSource source) {
        List<MobTrait> list = LHTraits.TRAITS.get().getValues().stream().filter(e -> e instanceof TargetEffectTrait).toList();
        TargetEffectTrait trait = (TargetEffectTrait) list.get(source.nextInt(list.size()));
        int rank = Math.min(maxRank, trait.getConfig().max_rank);
        MobEffectInstance ans = (MobEffectInstance) trait.func.apply(rank);
        return new EffectBuilder(ans).setDuration(ans.getDuration() * LHConfig.COMMON.witchWandFactor.get()).ins;
    }

    public WitchWand(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        level.playSound(player, player.m_20185_(), player.m_20186_(), player.m_20189_(), SoundEvents.LINGERING_POTION_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (player.m_217043_().nextFloat() * 0.4F + 0.8F));
        ItemStack stack = player.m_21120_(hand);
        if (!level.isClientSide) {
            ThrownPotion entity = new ThrownPotion(level, player);
            ItemStack potion = new ItemStack(Items.SPLASH_POTION);
            int maxRank = PlayerDifficulty.HOLDER.get(player).maxRankKilled;
            MobEffectInstance ins = getRandom(maxRank, player.m_217043_());
            PotionUtils.setCustomEffects(potion, List.of(ins));
            potion.getOrCreateTag().putInt("CustomPotionColor", ins.getEffect().getColor());
            entity.m_37446_(potion);
            entity.m_37251_(player, player.m_146909_(), player.m_146908_(), -20.0F, 0.5F, 1.0F);
            level.m_7967_(entity);
            if (!player.getAbilities().instabuild) {
                stack.hurtAndBreak(1, player, e -> e.m_21190_(hand));
            }
        }
        player.getCooldowns().addCooldown(this, 60);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(LangData.ITEM_WITCH_WAND.get().withStyle(ChatFormatting.GOLD));
    }
}