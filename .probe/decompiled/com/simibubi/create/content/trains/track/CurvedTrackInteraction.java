package com.simibubi.create.content.trains.track;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllPackets;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.InputEvent;

public class CurvedTrackInteraction {

    static final int breakerId = new Object().hashCode();

    static int breakTicks;

    static int breakTimeout;

    static float breakProgress;

    static BlockPos breakPos;

    public static void clientTick() {
        TrackBlockOutline.BezierPointSelection result = TrackBlockOutline.result;
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        if (player.m_150110_().mayBuild) {
            if (mc.options.keyAttack.isDown() && result != null) {
                breakPos = result.blockEntity().m_58899_();
                BlockState blockState = level.m_8055_(breakPos);
                if (blockState.m_60795_()) {
                    resetBreakProgress();
                } else {
                    if ((float) breakTicks % 4.0F == 0.0F) {
                        SoundType soundtype = blockState.getSoundType(level, breakPos, player);
                        mc.getSoundManager().play(new SimpleSoundInstance(soundtype.getHitSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 8.0F, soundtype.getPitch() * 0.5F, level.f_46441_, BlockPos.containing(result.vec())));
                    }
                    boolean creative = player.m_150110_().instabuild;
                    breakTicks++;
                    breakTimeout = 2;
                    breakProgress = breakProgress + (creative ? 0.125F : blockState.m_60625_(player, level, breakPos) / 8.0F);
                    Vec3 vec = VecHelper.offsetRandomly(result.vec(), level.f_46441_, 0.25F);
                    level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), vec.x, vec.y, vec.z, 0.0, 0.0, 0.0);
                    int progress = (int) (breakProgress * 10.0F) - 1;
                    level.destroyBlockProgress(player.m_19879_(), breakPos, progress);
                    player.swing(InteractionHand.MAIN_HAND);
                    if (breakProgress >= 1.0F) {
                        AllPackets.getChannel().sendToServer(new CurvedTrackDestroyPacket(breakPos, result.loc().curveTarget(), BlockPos.containing(result.vec()), false));
                        resetBreakProgress();
                    }
                }
            } else if (breakTimeout != 0) {
                if (--breakTimeout <= 0) {
                    resetBreakProgress();
                }
            }
        }
    }

    private static void resetBreakProgress() {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (breakPos != null && level != null) {
            level.destroyBlockProgress(mc.player.m_19879_(), breakPos, -1);
        }
        breakProgress = 0.0F;
        breakTicks = 0;
        breakPos = null;
    }

    public static boolean onClickInput(InputEvent.InteractionKeyMappingTriggered event) {
        TrackBlockOutline.BezierPointSelection result = TrackBlockOutline.result;
        if (result == null) {
            return false;
        } else {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            ClientLevel level = mc.level;
            if (player != null && level != null) {
                if (event.isUseItem()) {
                    ItemStack heldItem = player.m_21205_();
                    Item item = heldItem.getItem();
                    if (AllTags.AllBlockTags.TRACKS.matches(heldItem)) {
                        player.displayClientMessage(Lang.translateDirect("track.turn_start").withStyle(ChatFormatting.RED), true);
                        player.swing(InteractionHand.MAIN_HAND);
                        return true;
                    }
                    if (item instanceof TrackTargetingBlockItem ttbi && ttbi.useOnCurve(result, heldItem)) {
                        player.swing(InteractionHand.MAIN_HAND);
                        return true;
                    }
                    if (AllItems.WRENCH.isIn(heldItem) && player.isShiftKeyDown()) {
                        AllPackets.getChannel().sendToServer(new CurvedTrackDestroyPacket(result.blockEntity().m_58899_(), result.loc().curveTarget(), BlockPos.containing(result.vec()), true));
                        resetBreakProgress();
                        player.swing(InteractionHand.MAIN_HAND);
                        return true;
                    }
                }
                return event.isAttack();
            } else {
                return false;
            }
        }
    }
}