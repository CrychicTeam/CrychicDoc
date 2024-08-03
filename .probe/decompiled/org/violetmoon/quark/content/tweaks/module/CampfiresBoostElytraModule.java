package org.violetmoon.quark.content.tweaks.module;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.play.entity.player.ZPlayerTick;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;

@ZetaLoadModule(category = "tweaks")
public class CampfiresBoostElytraModule extends ZetaModule {

    @Config
    public double boostStrength = 0.5;

    @Config
    public double maxSpeed = 1.0;

    @Hint
    Item campfire = Items.CAMPFIRE;

    @Hint
    Item soul_campfire = Items.SOUL_CAMPFIRE;

    @PlayEvent
    public void onPlayerTick(ZPlayerTick.Start event) {
        Player player = event.getPlayer();
        if (player.m_21255_()) {
            Vec3 motion = player.m_20184_();
            if (motion.y() < this.maxSpeed) {
                BlockPos pos = player.m_20183_();
                Level world = player.m_9236_();
                int moves;
                for (moves = 0; world.m_46859_(pos) && world.isInWorldBounds(pos) && moves < 20; moves++) {
                    pos = pos.below();
                }
                BlockState state = world.getBlockState(pos);
                Block block = state.m_60734_();
                boolean isCampfire = state.m_204336_(BlockTags.CAMPFIRES);
                if (isCampfire && block instanceof CampfireBlock && (Boolean) state.m_61143_(CampfireBlock.LIT) && (Boolean) state.m_61143_(CampfireBlock.SIGNAL_FIRE)) {
                    double force = this.boostStrength;
                    if (moves > 16) {
                        force -= force * (1.0 - ((double) moves - 16.0) / 4.0);
                    }
                    if (block == Blocks.SOUL_CAMPFIRE) {
                        force *= -1.5;
                    }
                    player.m_20334_(motion.x(), Math.min(this.maxSpeed, motion.y() + force), motion.z());
                }
            }
        }
    }
}