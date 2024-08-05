package org.violetmoon.quark.content.tools.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.tools.entity.ParrotEgg;
import org.violetmoon.quark.content.tools.module.ParrotEggsModule;
import org.violetmoon.zeta.item.ZetaItem;
import org.violetmoon.zeta.module.ZetaModule;

public class ParrotEggItem extends ZetaItem {

    private final int variant;

    public ParrotEggItem(String suffix, int variant, ZetaModule module) {
        super("egg_parrot_" + suffix, module, new Item.Properties().stacksTo(16));
        this.variant = variant;
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        world.playSound(null, player.m_20185_(), player.m_20186_(), player.m_20189_(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!world.isClientSide) {
            ParrotEgg parrotEgg = new ParrotEgg(world, player);
            parrotEgg.m_37446_(stack);
            parrotEgg.setVariant(this.variant);
            parrotEgg.m_37251_(player, player.m_146909_(), player.m_146908_(), 0.0F, 1.5F, 1.0F);
            world.m_7967_(parrotEgg);
            if (player instanceof ServerPlayer sp) {
                ParrotEggsModule.throwParrotEggTrigger.trigger(sp);
            }
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
    }
}