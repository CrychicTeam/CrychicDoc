package org.violetmoon.quark.content.tools.module;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.world.block.CorundumClusterBlock;
import org.violetmoon.quark.content.world.module.CorundumModule;
import org.violetmoon.zeta.advancement.ManualTrigger;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.loading.ZGatherHints;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;

@ZetaLoadModule(category = "tools")
public class BeaconRedirectionModule extends ZetaModule {

    private static final TagKey<Block> BEACON_TRANSPARENT = BlockTags.create(new ResourceLocation("quark:beacon_transparent"));

    @Config
    public static int horizontalMoveLimit = 64;

    @Config(flag = "tinted_glass_dims")
    public static boolean allowTintedGlassTransparency = true;

    @Hint("tinted_glass_dims")
    Item tinted_glass = Items.TINTED_GLASS;

    public static boolean staticEnabled;

    public static ManualTrigger redirectTrigger;

    @LoadEvent
    public final void register(ZRegister event) {
        redirectTrigger = event.getAdvancementModifierRegistry().registerManualTrigger("redirect_beacon");
    }

    @LoadEvent
    public final void configChanged(ZConfigChanged event) {
        staticEnabled = this.enabled;
    }

    @PlayEvent
    public void addAdditionalHints(ZGatherHints event) {
        String redirectHint = "beacon_redirect_item";
        String type = "amethyst";
        if (!Quark.ZETA.modules.isEnabled(CorundumModule.class)) {
            event.hintItem(this.zeta, Items.AMETHYST_CLUSTER, "beacon_redirect_item");
        } else {
            type = "corundum";
        }
        Component comp = Component.translatable("quark.jei.hint.beacon_redirection", Component.translatable("quark.jei.hint.beacon_" + type));
        event.accept(Items.BEACON, comp);
    }

    public static int tickBeacon(BeaconBlockEntity beacon, int original) {
        if (!staticEnabled) {
            return original;
        } else {
            Level world = beacon.m_58904_();
            BlockPos beaconPos = beacon.m_58899_();
            BlockPos currPos = beaconPos;
            int horizontalMoves = horizontalMoveLimit;
            int targetHeight = world.getHeight(Heightmap.Types.WORLD_SURFACE, beaconPos.m_123341_(), beaconPos.m_123343_());
            boolean broke = false;
            boolean didRedirection = false;
            beacon.checkingBeamSections.clear();
            float[] currColor = new float[] { 1.0F, 1.0F, 1.0F };
            float alpha = 1.0F;
            Direction lastDir = null;
            BeaconRedirectionModule.ExtendedBeamSegment currSegment = new BeaconRedirectionModule.ExtendedBeamSegment(Direction.UP, Vec3i.ZERO, currColor, alpha);
            Collection<BlockPos> seenPositions = new HashSet();
            boolean check = true;
            boolean hardColorSet = false;
            while (world.isInWorldBounds(currPos) && horizontalMoves > 0) {
                if (currSegment.dir == Direction.UP && currSegment.dir != lastDir) {
                    int heightmapVal = world.getHeight(Heightmap.Types.WORLD_SURFACE, currPos.m_123341_(), currPos.m_123343_());
                    if (heightmapVal == currPos.m_123342_() + 1) {
                        currSegment.setHeight(heightmapVal + 1000);
                        break;
                    }
                    lastDir = currSegment.dir;
                }
                currPos = currPos.relative(currSegment.dir);
                if (currSegment.dir.getAxis().isHorizontal()) {
                    horizontalMoves--;
                } else {
                    horizontalMoves = horizontalMoveLimit;
                }
                BlockState blockstate = world.getBlockState(currPos);
                Block block = blockstate.m_60734_();
                float[] targetColor = blockstate.getBeaconColorMultiplier(world, currPos, beaconPos);
                float targetAlpha = -1.0F;
                if (allowTintedGlassTransparency && block == Blocks.TINTED_GLASS) {
                    targetAlpha = alpha < 0.3F ? 0.0F : alpha / 2.0F;
                }
                if (isRedirectingBlock(block)) {
                    Direction dir = (Direction) blockstate.m_61143_(BlockStateProperties.FACING);
                    if (dir == currSegment.dir) {
                        currSegment.increaseHeight();
                    } else {
                        check = true;
                        beacon.checkingBeamSections.add(currSegment);
                        targetColor = getTargetColor(block);
                        if (targetColor[0] == 1.0F && targetColor[1] == 1.0F && targetColor[2] == 1.0F) {
                            targetColor = currColor;
                        }
                        float[] mixedColor = new float[] { (currColor[0] + targetColor[0] * 3.0F) / 4.0F, (currColor[1] + targetColor[1] * 3.0F) / 4.0F, (currColor[2] + targetColor[2] * 3.0F) / 4.0F };
                        currColor = mixedColor;
                        alpha = 1.0F;
                        didRedirection = true;
                        lastDir = currSegment.dir;
                        currSegment = new BeaconRedirectionModule.ExtendedBeamSegment(dir, currPos.subtract(beaconPos), mixedColor, alpha);
                    }
                } else if (targetColor == null && targetAlpha == -1.0F) {
                    boolean bedrock = blockstate.m_204336_(BEACON_TRANSPARENT);
                    if (!bedrock && blockstate.m_60739_(world, currPos) >= 15) {
                        broke = true;
                        break;
                    }
                    currSegment.increaseHeight();
                    if (bedrock) {
                        continue;
                    }
                } else if (Arrays.equals(targetColor, currColor) && targetAlpha == alpha) {
                    currSegment.increaseHeight();
                } else {
                    check = true;
                    beacon.checkingBeamSections.add(currSegment);
                    float[] mixedColor = currColor;
                    if (targetColor != null) {
                        mixedColor = new float[] { (currColor[0] + targetColor[0]) / 2.0F, (currColor[1] + targetColor[1]) / 2.0F, (currColor[2] + targetColor[2]) / 2.0F };
                        if (!hardColorSet) {
                            mixedColor = targetColor;
                            hardColorSet = true;
                        }
                        currColor = mixedColor;
                    }
                    if (targetAlpha != -1.0F) {
                        alpha = targetAlpha;
                    }
                    lastDir = currSegment.dir;
                    currSegment = new BeaconRedirectionModule.ExtendedBeamSegment(currSegment.dir, currPos.subtract(beaconPos), mixedColor, alpha);
                }
                if (check) {
                    boolean added = seenPositions.add(currPos);
                    if (!added) {
                        broke = true;
                        break;
                    }
                }
            }
            if (horizontalMoves == 0 || currPos.m_123342_() <= world.m_141937_()) {
                broke = true;
            }
            String tag = "quark:redirected";
            if (!broke) {
                beacon.checkingBeamSections.add(currSegment);
                beacon.lastCheckY = targetHeight + 1;
            } else {
                beacon.getPersistentData().putBoolean("quark:redirected", false);
                beacon.checkingBeamSections.clear();
                beacon.lastCheckY = targetHeight;
            }
            if (!beacon.getPersistentData().getBoolean("quark:redirected") && didRedirection && !beacon.checkingBeamSections.isEmpty()) {
                beacon.getPersistentData().putBoolean("quark:redirected", true);
                int i = beaconPos.m_123341_();
                int j = beaconPos.m_123342_();
                int k = beaconPos.m_123343_();
                for (ServerPlayer serverplayer : beacon.m_58904_().m_45976_(ServerPlayer.class, new AABB((double) i, (double) j, (double) k, (double) i, (double) (j - 4), (double) k).inflate(10.0, 5.0, 10.0))) {
                    redirectTrigger.trigger(serverplayer);
                }
            }
            return Integer.MAX_VALUE;
        }
    }

    private static boolean isRedirectingBlock(Block block) {
        return CorundumModule.staticEnabled ? block instanceof CorundumClusterBlock : block == Blocks.AMETHYST_CLUSTER;
    }

    private static float[] getTargetColor(Block block) {
        return block instanceof CorundumClusterBlock cc ? cc.base.colorComponents : new float[] { 1.0F, 1.0F, 1.0F };
    }

    public static class ExtendedBeamSegment extends BeaconBlockEntity.BeaconBeamSection {

        public final Direction dir;

        public final Vec3i offset;

        public final float alpha;

        private boolean isTurn = false;

        public ExtendedBeamSegment(Direction dir, Vec3i offset, float[] colorsIn, float alpha) {
            super(colorsIn);
            this.offset = offset;
            this.dir = dir;
            this.alpha = alpha;
        }

        public void makeTurn() {
            this.isTurn = true;
        }

        public boolean isTurn() {
            return this.isTurn;
        }

        @Override
        public void increaseHeight() {
            super.increaseHeight();
        }

        public void setHeight(int target) {
            this.f_58716_ = target;
        }
    }
}