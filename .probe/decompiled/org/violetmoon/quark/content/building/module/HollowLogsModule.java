package org.violetmoon.quark.content.building.module;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.violetmoon.quark.api.ICrawlSpaceBlock;
import org.violetmoon.quark.content.building.block.HollowLogBlock;
import org.violetmoon.zeta.advancement.ManualTrigger;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.entity.player.ZPlayerTick;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;
import org.violetmoon.zeta.util.VanillaWoods;

@ZetaLoadModule(category = "building")
public class HollowLogsModule extends ZetaModule {

    private static final String TAG_TRYING_TO_CRAWL = "quark:trying_crawl";

    public static ManualTrigger crawlTrigger;

    @Config(flag = "hollow_log_auto_crawl")
    public static boolean enableAutoCrawl = true;

    @Hint(key = "hollow_logs", value = "hollow_log_auto_crawl")
    public static TagKey<Block> hollowLogsTag;

    public static boolean staticEnabled;

    public static Map<Block, Block> logMap = new HashMap();

    @LoadEvent
    public final void configChanged(ZConfigChanged event) {
        staticEnabled = this.enabled;
    }

    @LoadEvent
    public final void register(ZRegister event) {
        for (VanillaWoods.Wood wood : VanillaWoods.ALL_WITH_LOGS) {
            new HollowLogBlock(wood.log(), this, !wood.nether());
        }
        crawlTrigger = event.getAdvancementModifierRegistry().registerManualTrigger("hollow_log_crawl");
    }

    @LoadEvent
    public final void setup(ZCommonSetup event) {
        hollowLogsTag = BlockTags.create(new ResourceLocation("quark", "hollow_logs"));
    }

    @PlayEvent
    public void playerTick(ZPlayerTick.Start event) {
        if (enableAutoCrawl) {
            Player player = event.getPlayer();
            BlockPos playerPos = player.m_20183_();
            boolean isTrying = player.m_20143_() || player.m_6047_() && !player.m_20039_(playerPos, player.m_9236_().getBlockState(playerPos));
            boolean wasTrying = player.getPersistentData().getBoolean("quark:trying_crawl");
            if (!player.m_20143_() && isTrying && !wasTrying) {
                Direction dir = player.m_6350_();
                Direction opp = dir.getOpposite();
                if (dir.getAxis() != Direction.Axis.Y) {
                    BlockPos pos = playerPos.relative(dir);
                    if (!this.tryClimb(player, opp, playerPos) && !this.tryClimb(player, opp, playerPos.above()) && !this.tryClimb(player, dir, pos)) {
                        this.tryClimb(player, dir, pos.above());
                    }
                }
            }
            if (isTrying != wasTrying) {
                player.getPersistentData().putBoolean("quark:trying_crawl", isTrying);
            }
        }
    }

    private boolean tryClimb(Player player, Direction dir, BlockPos pos) {
        BlockState state = player.m_9236_().getBlockState(pos);
        if (state.m_60734_() instanceof ICrawlSpaceBlock crawlSpace && crawlSpace.canCrawl(player.m_9236_(), state, pos, dir)) {
            player.m_20124_(Pose.SWIMMING);
            player.m_20282_(true);
            double x = (double) pos.m_123341_() + 0.5 - (double) dir.getStepX() * 0.3;
            double y = (double) pos.m_123342_() + crawlSpace.crawlHeight(player.m_9236_(), state, pos, dir);
            double z = (double) pos.m_123343_() + 0.5 - (double) dir.getStepZ() * 0.3;
            player.m_6034_(x, y, z);
            if (player instanceof ServerPlayer sp && crawlSpace.isLog(sp, state, pos, dir)) {
                crawlTrigger.trigger(sp);
            }
            return true;
        }
        return false;
    }
}