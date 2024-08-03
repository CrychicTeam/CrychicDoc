package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.util.IDragonFlute;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class ItemDragonFlute extends Item {

    public ItemDragonFlute() {
        super(new Item.Properties().stacksTo(1));
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, @NotNull InteractionHand hand) {
        ItemStack itemStackIn = player.m_21120_(hand);
        player.getCooldowns().addCooldown(this, 60);
        float chunksize = (float) (16 * IafConfig.dragonFluteDistance);
        List<Entity> list = worldIn.m_45933_(player, new AABB(player.m_20185_(), player.m_20186_(), player.m_20189_(), player.m_20185_() + 1.0, player.m_20186_() + 1.0, player.m_20189_() + 1.0).inflate((double) chunksize, 256.0, (double) chunksize));
        Collections.sort(list, new ItemDragonFlute.Sorter(player));
        List<IDragonFlute> dragons = new ArrayList();
        for (Entity entity : list) {
            if (entity instanceof IDragonFlute) {
                dragons.add((IDragonFlute) entity);
            }
        }
        for (IDragonFlute dragon : dragons) {
            dragon.onHearFlute(player);
        }
        worldIn.playSound(player, player.m_20183_(), IafSoundRegistry.DRAGONFLUTE, SoundSource.NEUTRAL, 1.0F, 1.75F);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStackIn);
    }

    public static class Sorter implements Comparator<Entity> {

        private final Entity theEntity;

        public Sorter(Entity theEntityIn) {
            this.theEntity = theEntityIn;
        }

        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            double d0 = this.theEntity.distanceToSqr(p_compare_1_);
            double d1 = this.theEntity.distanceToSqr(p_compare_2_);
            return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
        }
    }
}