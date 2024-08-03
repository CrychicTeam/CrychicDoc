package com.mna.items.artifice.charms;

import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.events.EnderfeatherCharmUsedEvent;
import com.mna.items.ItemInit;
import com.mna.tools.TeleportHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

public class ItemTeleportToBedCharm extends CharmBaseItem {

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (world.isClientSide) {
            return InteractionResultHolder.success(player.m_21120_(hand));
        } else {
            ServerPlayer spe = (ServerPlayer) player;
            ItemStack stack = player.m_21120_(hand);
            if (!world.dimension().equals(spe.getRespawnDimension())) {
                player.m_213846_(Component.translatable("item.mna.bed_charm.dimension-wrong"));
                return InteractionResultHolder.fail(stack);
            } else {
                BlockPos bedPos = spe.getRespawnPosition();
                if (bedPos == null) {
                    player.m_213846_(Component.translatable("item.mna.bed_charm.no-bed"));
                    return InteractionResultHolder.fail(stack);
                } else if (this.consume(spe, hand)) {
                    EnderfeatherCharmUsedEvent event = new EnderfeatherCharmUsedEvent(spe);
                    MinecraftForge.EVENT_BUS.post(event);
                    if (event.isCanceled()) {
                        spe.m_36356_(new ItemStack(this));
                        return InteractionResultHolder.fail(stack);
                    } else {
                        double dist = spe.m_20182_().distanceTo(new Vec3((double) bedPos.m_123341_(), (double) bedPos.m_123342_(), (double) bedPos.m_123343_()));
                        if (dist >= 9999.0) {
                            CustomAdvancementTriggers.USE_CHARM.trigger((ServerPlayer) player, new ItemStack(this));
                        }
                        player.awardStat(Stats.ITEM_USED.get(ItemInit.BED_CHARM.get()));
                        CustomAdvancementTriggers.SHAMAN.trigger((ServerPlayer) player);
                        world.broadcastEntityEvent(spe, (byte) 46);
                        world.playSound(null, spe.m_20185_(), spe.m_20186_(), spe.m_20189_(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 0.9F + (float) Math.random() * 0.2F);
                        TeleportHelper.teleportEntity(spe, world.dimension(), new Vec3((double) bedPos.m_123341_() + 0.5, (double) bedPos.m_123342_(), (double) bedPos.m_123343_() + 0.5));
                        world.playSound(null, (double) bedPos.m_123341_(), (double) bedPos.m_123342_(), (double) bedPos.m_123343_(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 0.9F + (float) Math.random() * 0.2F);
                        world.broadcastEntityEvent(spe, (byte) 46);
                        return InteractionResultHolder.success(stack);
                    }
                } else {
                    player.m_213846_(Component.translatable("item.mna.bed_charm.consume-failed"));
                    return InteractionResultHolder.fail(stack);
                }
            }
        }
    }
}