package org.violetmoon.quark.content.tweaks.module;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.base.network.message.DoubleDoorMessage;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.bus.ZResult;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import org.violetmoon.zeta.event.play.entity.player.ZRightClickBlock;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;

@ZetaLoadModule(category = "tweaks", antiOverlap = { "utilitix" })
public class DoubleDoorOpeningModule extends ZetaModule {

    @Config(flag = "doors_open_together")
    public static boolean enableDoors = true;

    @Config(flag = "fence_gates_open_together")
    public static boolean enableFenceGates = true;

    public static TagKey<Block> nonDoubleDoorTag;

    private static boolean handling = false;

    @Hint(key = "doors_open_together", value = "doors_open_together")
    TagKey<Item> doors = ItemTags.DOORS;

    @Hint(key = "fence_gates_open_together", value = "fence_gates_open_together")
    TagKey<Block> fence_gates = BlockTags.FENCE_GATES;

    @LoadEvent
    public void setup(ZCommonSetup e) {
        nonDoubleDoorTag = BlockTags.create(new ResourceLocation("quark", "non_double_door"));
    }

    public boolean openBlock(Level world, Player player, BlockPos pos) {
        if (this.enabled && world != null) {
            BlockState state = world.getBlockState(pos);
            if (state.m_204336_(nonDoubleDoorTag)) {
                return false;
            } else if (enableDoors && state.m_60734_() instanceof DoorBlock) {
                return this.openDoor(pos, world, player, state);
            } else {
                return enableFenceGates && state.m_60734_() instanceof FenceGateBlock ? this.openFenceGate(pos, world, player, state) : false;
            }
        } else {
            return false;
        }
    }

    private boolean openDoor(BlockPos pos, Level level, Player player, BlockState state) {
        Direction direction = (Direction) state.m_61143_(HorizontalDirectionalBlock.FACING);
        boolean isOpen = (Boolean) state.m_61143_(BlockStateProperties.OPEN);
        DoorHingeSide isMirrored = (DoorHingeSide) state.m_61143_(DoorBlock.HINGE);
        BlockPos mirrorPos = pos.relative(isMirrored == DoorHingeSide.RIGHT ? direction.getCounterClockWise() : direction.getClockWise());
        BlockPos doorPos = state.m_61143_(DoorBlock.HALF) == DoubleBlockHalf.LOWER ? mirrorPos : mirrorPos.below();
        return this.tryOpen(level, player, state, doorPos, direction, isOpen, test -> test.m_61143_(DoorBlock.HINGE) != isMirrored);
    }

    private boolean openFenceGate(BlockPos pos, Level level, Player player, BlockState state) {
        Direction direction = (Direction) state.m_61143_(FenceGateBlock.f_54117_);
        boolean isOpen = (Boolean) state.m_61143_(BlockStateProperties.OPEN);
        return this.tryOpen(level, player, state, pos.below(), direction, isOpen, Predicates.alwaysTrue()) ? true : this.tryOpen(level, player, state, pos.above(), direction, isOpen, Predicates.alwaysTrue());
    }

    private boolean tryOpen(Level level, Player player, BlockState state, BlockPos otherPos, Direction direction, boolean isOpen, Predicate<BlockState> pred) {
        BlockState other;
        boolean var10000;
        label35: {
            other = level.getBlockState(otherPos);
            if (state.m_60734_() instanceof DoorBlock doorBlock && doorBlock.type().canOpenByHand()) {
                var10000 = true;
                break label35;
            }
            var10000 = false;
        }
        boolean doorCheck = var10000;
        boolean fenceGateCheck = state.m_60734_() instanceof FenceGateBlock;
        if ((doorCheck || fenceGateCheck) && other.m_60734_() == state.m_60734_() && other.m_61143_(HorizontalDirectionalBlock.FACING) == direction && (Boolean) other.m_61143_(BlockStateProperties.OPEN) == isOpen && pred.apply(other)) {
            BlockHitResult res = new BlockHitResult(new Vec3((double) otherPos.m_123341_() + 0.5, (double) otherPos.m_123342_() + 0.5, (double) otherPos.m_123343_() + 0.5), direction, otherPos, false);
            if (res.getType() == HitResult.Type.BLOCK) {
                InteractionResult interaction = other.m_60664_(level, player, InteractionHand.MAIN_HAND, res);
                return interaction != InteractionResult.PASS;
            }
        }
        return false;
    }

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends DoubleDoorOpeningModule {

        @PlayEvent
        public void onPlayerInteract(ZRightClickBlock.Low event) {
            Player player = event.getPlayer();
            if (event.getLevel().isClientSide && !player.m_20163_() && !event.isCanceled() && event.getResult() != ZResult.DENY && event.getUseBlock() != ZResult.DENY && !DoubleDoorOpeningModule.handling) {
                Level world = event.getLevel();
                BlockPos pos = event.getPos();
                if (Quark.FLAN_INTEGRATION.canInteract(player, pos)) {
                    DoubleDoorOpeningModule.handling = true;
                    boolean opened = this.openBlock(world, player, pos);
                    DoubleDoorOpeningModule.handling = false;
                    if (opened) {
                        QuarkClient.ZETA_CLIENT.sendToServer(new DoubleDoorMessage(pos));
                    }
                }
            }
        }
    }
}