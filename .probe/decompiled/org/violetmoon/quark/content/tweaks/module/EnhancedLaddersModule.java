package org.violetmoon.quark.content.tweaks.module;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.violetmoon.zeta.client.event.play.ZInputUpdate;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.event.play.entity.player.ZPlayerTick;
import org.violetmoon.zeta.event.play.entity.player.ZRightClickBlock;
import org.violetmoon.zeta.event.play.loading.ZGatherHints;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.RegistryUtil;

@ZetaLoadModule(category = "tweaks")
public class EnhancedLaddersModule extends ZetaModule {

    @Config.Max(0.0)
    @Config
    public double fallSpeed = -0.2;

    @Config
    public static boolean allowFreestanding = true;

    @Config
    public static boolean allowDroppingDown = true;

    @Config
    public static boolean allowSliding = true;

    @Config
    public static boolean allowInventorySneak = true;

    private static boolean staticEnabled;

    private static TagKey<Item> laddersTag;

    private static TagKey<Block> laddersBlockTag;

    @LoadEvent
    public final void setup(ZCommonSetup event) {
        laddersTag = ItemTags.create(new ResourceLocation("quark", "ladders"));
        laddersBlockTag = BlockTags.create(new ResourceLocation("quark", "ladders"));
    }

    @LoadEvent
    public final void configChanged(ZConfigChanged event) {
        staticEnabled = this.enabled;
    }

    @PlayEvent
    public void addAdditionalHints(ZGatherHints event) {
        if (allowFreestanding || allowDroppingDown || allowSliding || allowInventorySneak) {
            MutableComponent comp = Component.empty();
            String pad = "";
            if (allowDroppingDown) {
                comp = comp.append(pad).append(Component.translatable("quark.jei.hint.ladder_dropping"));
                pad = " ";
            }
            if (allowFreestanding) {
                comp = comp.append(pad).append(Component.translatable("quark.jei.hint.ladder_freestanding"));
                pad = " ";
            }
            if (allowSliding) {
                comp = comp.append(pad).append(Component.translatable("quark.jei.hint.ladder_sliding"));
                pad = " ";
            }
            if (allowInventorySneak) {
                comp = comp.append(pad).append(Component.translatable("quark.jei.hint.ladder_sneak"));
            }
            for (Item item : RegistryUtil.getTagValues(event.getRegistryAccess(), laddersTag)) {
                event.accept(item, comp);
            }
        }
    }

    private static boolean canAttachTo(BlockState state, Block ladder, LevelReader world, BlockPos pos, Direction facing) {
        if (ladder instanceof LadderBlock) {
            if (allowFreestanding) {
                return canLadderSurvive(state, world, pos);
            } else {
                BlockPos offset = pos.relative(facing);
                BlockState blockstate = world.m_8055_(offset);
                return !blockstate.m_60803_() && blockstate.m_60783_(world, offset, facing);
            }
        } else {
            return false;
        }
    }

    public static boolean canLadderSurvive(BlockState state, LevelReader world, BlockPos pos) {
        if (!staticEnabled || !allowFreestanding) {
            return false;
        } else if (!state.m_204336_(laddersBlockTag)) {
            return false;
        } else {
            Direction facing = (Direction) state.m_61143_(LadderBlock.FACING);
            Direction opposite = facing.getOpposite();
            BlockPos oppositePos = pos.relative(opposite);
            BlockState oppositeState = world.m_8055_(oppositePos);
            boolean solid = oppositeState.m_60783_(world, oppositePos, facing) && !(oppositeState.m_60734_() instanceof LadderBlock);
            BlockState topState = world.m_8055_(pos.above());
            return solid || topState.m_60734_() instanceof LadderBlock && (facing.getAxis() == Direction.Axis.Y || topState.m_61143_(LadderBlock.FACING) == facing);
        }
    }

    @PlayEvent
    public void onInteract(ZRightClickBlock event) {
        if (allowDroppingDown) {
            Player player = event.getPlayer();
            InteractionHand hand = event.getHand();
            ItemStack stack = player.m_21120_(hand);
            if (!stack.isEmpty() && stack.is(laddersTag)) {
                Block block = Block.byItem(stack.getItem());
                Level world = event.getLevel();
                BlockPos pos = event.getPos();
                while (world.getBlockState(pos).m_60734_() == block) {
                    event.setCanceled(true);
                    BlockPos posDown = pos.below();
                    if (world.m_151570_(posDown)) {
                        break;
                    }
                    BlockState stateDown = world.getBlockState(posDown);
                    if (stateDown.m_60734_() != block) {
                        boolean water = stateDown.m_60734_() == Blocks.WATER;
                        if (water || stateDown.m_60795_()) {
                            BlockState copyState = world.getBlockState(pos);
                            Direction facing = (Direction) copyState.m_61143_(LadderBlock.FACING);
                            if (canAttachTo(copyState, block, world, posDown, facing.getOpposite())) {
                                world.setBlockAndUpdate(posDown, (BlockState) copyState.m_61124_(BlockStateProperties.WATERLOGGED, water));
                                world.playSound(null, (double) posDown.m_123341_(), (double) posDown.m_123342_(), (double) posDown.m_123343_(), SoundEvents.LADDER_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                                if (!player.getAbilities().instabuild) {
                                    stack.shrink(1);
                                    if (stack.getCount() <= 0) {
                                        player.m_21008_(hand, ItemStack.EMPTY);
                                    }
                                }
                                event.setCancellationResult(InteractionResult.sidedSuccess(world.isClientSide));
                            }
                        }
                        break;
                    }
                    pos = posDown;
                }
            }
        }
    }

    protected boolean isScaffolding(BlockState state, LivingEntity entity) {
        return this.zeta.blockExtensions.get(state).isScaffoldingZeta(state, entity.m_9236_(), entity.m_20183_(), entity);
    }

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends EnhancedLaddersModule {

        @PlayEvent
        public void onInput(ZInputUpdate event) {
            if (allowInventorySneak) {
                Player player = event.getEntity();
                if (player.m_6147_() && !player.getAbilities().flying && !this.isScaffolding(player.m_9236_().getBlockState(player.m_20183_()), player) && Minecraft.getInstance().screen != null && (player.f_20902_ != 0.0F || !(player.m_146909_() > 70.0F)) && !player.m_20096_()) {
                    Input input = event.getInput();
                    if (input != null) {
                        input.shiftKeyDown = true;
                    }
                }
            }
        }

        @PlayEvent
        public void onPlayerTick(ZPlayerTick.Start event) {
            if (allowSliding) {
                Player player = event.getPlayer();
                if (player.m_6147_() && player.m_9236_().isClientSide) {
                    BlockPos playerPos = player.m_20183_();
                    BlockPos downPos = playerPos.below();
                    boolean scaffold = this.isScaffolding(player.m_9236_().getBlockState(playerPos), player);
                    if (player.m_6047_() == scaffold && player.f_20902_ == 0.0F && player.f_20901_ <= 0.0F && player.f_20900_ == 0.0F && player.m_146909_() > 70.0F && !player.f_20899_ && !player.getAbilities().flying && player.m_9236_().getBlockState(downPos).isLadder(player.m_9236_(), downPos, player)) {
                        Vec3 move = new Vec3(0.0, this.fallSpeed, 0.0);
                        AABB target = player.m_20191_().move(move);
                        Iterable<VoxelShape> collisions = player.m_9236_().m_186434_(player, target);
                        if (!collisions.iterator().hasNext()) {
                            player.m_20011_(target);
                            player.m_6478_(MoverType.SELF, move);
                        }
                    }
                }
            }
        }
    }
}