package com.github.alexthe666.citadel.server;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.block.CitadelLecternBlock;
import com.github.alexthe666.citadel.server.block.CitadelLecternBlockEntity;
import com.github.alexthe666.citadel.server.block.LecternBooks;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CitadelEvents {

    private int updateTimer;

    @SubscribeEvent
    public void onEntityUpdateDebug(LivingEvent.LivingTickEvent event) {
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().getBlockState(event.getPos()).m_60713_(Blocks.LECTERN) && LecternBooks.isLecternBook(event.getItemStack())) {
            event.getEntity().getCooldowns().addCooldown(event.getItemStack().getItem(), 1);
            BlockState oldLectern = event.getLevel().getBlockState(event.getPos());
            if (event.getLevel().getBlockEntity(event.getPos()) instanceof LecternBlockEntity oldBe && !oldBe.hasBook()) {
                BlockState newLectern = (BlockState) ((BlockState) ((BlockState) Citadel.LECTERN.get().defaultBlockState().m_61124_(CitadelLecternBlock.f_54465_, (Direction) oldLectern.m_61143_(LecternBlock.FACING))).m_61124_(CitadelLecternBlock.f_54466_, (Boolean) oldLectern.m_61143_(LecternBlock.POWERED))).m_61124_(CitadelLecternBlock.f_54467_, true);
                event.getLevel().setBlockAndUpdate(event.getPos(), newLectern);
                CitadelLecternBlockEntity newBe = new CitadelLecternBlockEntity(event.getPos(), newLectern);
                ItemStack bookCopy = event.getItemStack().copy();
                bookCopy.setCount(1);
                newBe.setBook(bookCopy);
                if (!event.getEntity().isCreative()) {
                    event.getItemStack().shrink(1);
                }
                event.getLevel().setBlockEntity(newBe);
                event.getEntity().m_21011_(event.getHand(), true);
                event.getLevel().playSound((Player) null, event.getPos(), SoundEvents.BOOK_PUT, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getOriginal() != null && CitadelEntityData.getCitadelTag(event.getOriginal()) != null) {
            CitadelEntityData.setCitadelTag(event.getEntity(), CitadelEntityData.getCitadelTag(event.getOriginal()));
        }
    }
}