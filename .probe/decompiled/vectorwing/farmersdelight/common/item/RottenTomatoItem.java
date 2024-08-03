package vectorwing.farmersdelight.common.item;

import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.entity.RottenTomatoEntity;
import vectorwing.farmersdelight.common.registry.ModSounds;

public class RottenTomatoItem extends Item {

    public RottenTomatoItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack heldStack = player.m_21120_(hand);
        level.playSound(null, player.m_20185_(), player.m_20186_(), player.m_20189_(), ModSounds.ENTITY_ROTTEN_TOMATO_THROW.get(), SoundSource.NEUTRAL, 0.5F, 0.4F / (level.random.nextFloat() * 0.4F + 0.8F));
        if (!level.isClientSide) {
            RottenTomatoEntity projectile = new RottenTomatoEntity(level, player);
            projectile.m_37446_(heldStack);
            projectile.m_37251_(player, player.m_146909_(), player.m_146908_(), 0.0F, 1.5F, 1.0F);
            level.m_7967_(projectile);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            heldStack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(heldStack, level.isClientSide());
    }
}