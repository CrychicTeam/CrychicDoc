package com.github.alexmodguy.alexscaves.server.item;

import java.util.function.Function;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ThrownProjectileItem extends Item {

    private final float throwAngle;

    private final float throwSpeed;

    private final float throwRandomness;

    private final Function<Player, ThrowableItemProjectile> projectileSupplier;

    public ThrownProjectileItem(Item.Properties properties, Function<Player, ThrowableItemProjectile> projectileSupplier, float throwAngle, float throwSpeed, float throwRandomness) {
        super(properties);
        this.throwAngle = throwAngle;
        this.throwSpeed = throwSpeed;
        this.throwRandomness = throwRandomness;
        this.projectileSupplier = projectileSupplier;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        level.playSound((Player) null, player.m_20185_(), player.m_20186_(), player.m_20189_(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5F, (level.getRandom().nextFloat() * 0.7F + 0.25F) * 0.5F);
        if (!level.isClientSide) {
            ThrowableItemProjectile brick = (ThrowableItemProjectile) this.projectileSupplier.apply(player);
            brick.setItem(itemstack);
            brick.m_37251_(player, player.m_146909_(), player.m_146908_(), this.throwAngle, this.throwSpeed, this.throwRandomness);
            level.m_7967_(brick);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}