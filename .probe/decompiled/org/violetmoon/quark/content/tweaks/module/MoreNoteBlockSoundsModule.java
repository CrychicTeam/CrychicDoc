package org.violetmoon.quark.content.tweaks.module;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.play.ZPlayNoteBlock;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;

@ZetaLoadModule(category = "tweaks")
public class MoreNoteBlockSoundsModule extends ZetaModule {

    @Config(flag = "amethyst_note_block")
    public static boolean enableAmethystSound = true;

    @Hint("amethyst_note_block")
    Item amethyst_block = Items.AMETHYST_BLOCK;

    @PlayEvent
    public void noteBlockPlayed(ZPlayNoteBlock event) {
        LevelAccessor world = event.getLevel();
        BlockPos pos = event.getPos();
        if (world.m_8055_(pos).m_60734_() == Blocks.NOTE_BLOCK) {
            if (enableAmethystSound && event.getInstrument() == NoteBlockInstrument.HARP && world instanceof ServerLevel serverLevel && this.isAmethyst(world.m_8055_(pos.below()))) {
                event.setCanceled(true);
                int note = (Integer) event.getState().m_61143_(NoteBlock.NOTE);
                float pitch = (float) Math.pow(2.0, (double) (note - 12) / 12.0);
                world.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.RECORDS, 1.0F, pitch);
                serverLevel.sendParticles(ParticleTypes.NOTE, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 1.2, (double) pos.m_123343_() + 0.5, 1, 0.0, 0.0, 0.0, (double) note / 24.0);
            }
        }
    }

    private boolean isAmethyst(BlockState state) {
        return state.m_60827_() == SoundType.AMETHYST || state.m_60827_() == SoundType.AMETHYST_CLUSTER || state.m_60734_() == Blocks.LARGE_AMETHYST_BUD || state.m_60734_() == Blocks.MEDIUM_AMETHYST_BUD || state.m_60734_() == Blocks.SMALL_AMETHYST_BUD;
    }
}