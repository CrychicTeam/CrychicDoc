package com.mna.items.constructs;

import com.mna.ManaAndArtifice;
import com.mna.api.events.GenericProgressionEvent;
import com.mna.api.events.ProgressionEventIDs;
import com.mna.api.items.TieredItem;
import com.mna.api.tools.MATags;
import com.mna.api.tools.RLoc;
import com.mna.blocks.BlockInit;
import com.mna.blocks.artifice.LodestarBlock;
import com.mna.blocks.tileentities.OffsetBlockTile;
import com.mna.blocks.utility.FillerBlock;
import com.mna.entities.constructs.ai.ConstructChop;
import com.mna.entities.constructs.ai.ConstructCollectFluid;
import com.mna.entities.constructs.ai.ConstructCommandFollowAndGuard;
import com.mna.entities.constructs.ai.ConstructCommandFollowLodestar;
import com.mna.entities.constructs.ai.ConstructCommandReturnToTable;
import com.mna.entities.constructs.ai.ConstructHarvest;
import com.mna.entities.constructs.ai.ConstructWearItem;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import com.mna.entities.constructs.animated.Construct;
import com.mna.items.base.IRadialInventorySelect;
import java.util.Arrays;
import java.util.List;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;

public class BellOfBidding extends TieredItem implements IRadialInventorySelect {

    private static final String NBT_COMMANDING_ENTITY = "commanding_entity";

    private static final String NBT_COMMAND_TYPE = "command";

    public BellOfBidding() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        Vec3 playerPos = player.m_146892_();
        double range = player.m_21133_(ForgeMod.BLOCK_REACH.get());
        range *= range;
        Vec3 viewVec = player.m_20252_(1.0F);
        Vec3 clipEnd = playerPos.add(viewVec.x * range, viewVec.y * range, viewVec.z * range);
        AABB box = player.m_20191_().expandTowards(viewVec.scale(range)).inflate(1.0, 1.0, 1.0);
        EntityHitResult res = ProjectileUtil.getEntityHitResult(player, playerPos, clipEnd, box, ex -> ex.isAlive(), range);
        if (res != null && res.getType() == HitResult.Type.ENTITY) {
            Entity e = res.getEntity();
            BellOfBidding.Commands cmd = this.getCommand(stack);
            if (e instanceof Construct construct) {
                if (player.m_6047_()) {
                    this.setCommandingEntity(construct, stack);
                    if (!player.m_9236_().isClientSide()) {
                        player.m_213846_(Component.translatable("item.mna.bell_of_bidding.bound"));
                    }
                    return InteractionResultHolder.success(stack);
                }
                if (cmd == BellOfBidding.Commands.DIAGNOSTICS) {
                    return this.issueDiagnosticsCommand(stack, player, construct, hand);
                }
                if (cmd == BellOfBidding.Commands.STAY) {
                    return this.issueStayCommand(stack, player, construct, hand);
                }
                if (cmd == BellOfBidding.Commands.FOLLOW) {
                    return this.issueFollowCommand(stack, player, construct, hand, false);
                }
                if (cmd == BellOfBidding.Commands.GUARD) {
                    return this.issueFollowCommand(stack, player, construct, hand, true);
                }
                if (cmd == BellOfBidding.Commands.EAT) {
                    return this.issueEatCommand(stack, player, construct, hand);
                }
            } else if (e instanceof LivingEntity living) {
                if (cmd == BellOfBidding.Commands.ATTACK) {
                    return this.issueAttackCommand(stack, player, living, hand);
                }
                if (cmd == BellOfBidding.Commands.GUARD) {
                    return this.issueFollowCommand(stack, player, living, hand, true);
                }
            }
        }
        return InteractionResultHolder.fail(stack);
    }

    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level world = context.getLevel();
        if (context.getPlayer().m_6047_()) {
            if (world.isClientSide) {
                return InteractionResult.PASS;
            } else {
                this.clearCommandingEntity(stack);
                context.getPlayer().m_213846_(Component.translatable("item.mna.bell_of_bidding.cleared"));
                return InteractionResult.SUCCESS;
            }
        } else {
            BellOfBidding.Commands cmd = this.getCommand(stack);
            if (cmd == BellOfBidding.Commands.MOVE) {
                return this.issueMoveCommand(stack, context);
            } else if (cmd == BellOfBidding.Commands.INTERACT) {
                return this.issueInteractCommand(stack, context);
            } else {
                return cmd == BellOfBidding.Commands.WEAR ? this.issueWearCommand(stack, context) : InteractionResult.FAIL;
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack0, Level level1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
        super.m_7373_(itemStack0, level1, listComponent2, tooltipFlag3);
        IRadialInventorySelect.super.appendHoverText(itemStack0, level1, listComponent2, tooltipFlag3);
    }

    private BellOfBidding.Commands getCommand(ItemStack stack) {
        return BellOfBidding.Commands.values()[this.getIndex(stack)];
    }

    public static void setCommand(ItemStack stack, BellOfBidding.Commands option) {
        if (stack.getItem() instanceof BellOfBidding) {
            stack.getOrCreateTag().putInt("command", option.ordinal());
        }
    }

    private void setCommandingEntity(Construct construct, ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("commanding_entity", construct != null ? construct.m_19879_() : -1);
        stack.setTag(tag);
    }

    private void clearCommandingEntity(ItemStack stack) {
        this.setCommandingEntity(null, stack);
    }

    private Construct getCommandedEntity(Level world, ItemStack stack) {
        Entity e = world.getEntity(stack.getOrCreateTag().getInt("commanding_entity"));
        return e instanceof Construct ? (Construct) e : null;
    }

    private List<Construct> getCommandingEntities(Level world, Player player, ItemStack stack) {
        Construct commandedEntity = this.getCommandedEntity(world, stack);
        return commandedEntity != null ? Arrays.asList(commandedEntity) : world.m_45976_(Construct.class, player.m_20191_().inflate(32.0)).stream().filter(c -> c.isFollowing(player)).toList();
    }

    private void ding(Player player) {
        if (!player.m_9236_().isClientSide()) {
            player.m_9236_().playSound(null, player.m_20185_(), player.m_20186_(), player.m_20189_(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    private InteractionResultHolder<ItemStack> issueWearCommand(ItemStack stack, Player player, ItemEntity target, InteractionHand hand) {
        this.getCommandingEntities(player.m_9236_(), player, stack).forEach(construct -> {
            ConstructWearItem task = (ConstructWearItem) ConstructTasks.WEAR_ITEM.instantiateTask(construct);
            task.setTarget(target);
            task.setOneOff(player);
            if (construct.setCurrentCommand(player, task)) {
                this.ding(player);
            }
        });
        this.ding(player);
        return InteractionResultHolder.sidedSuccess(stack, !player.m_9236_().isClientSide());
    }

    private InteractionResultHolder<ItemStack> issueAttackCommand(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        this.getCommandingEntities(player.m_9236_(), player, stack).forEach(construct -> construct.setTarget(target));
        this.ding(player);
        return InteractionResultHolder.sidedSuccess(stack, !player.m_9236_().isClientSide());
    }

    private InteractionResultHolder<ItemStack> issueEatCommand(ItemStack stack, Player player, Construct target, InteractionHand hand) {
        if (!player.m_9236_().isClientSide()) {
            target.eatItem(player);
        }
        this.ding(player);
        return InteractionResultHolder.sidedSuccess(stack, !player.m_9236_().isClientSide());
    }

    private InteractionResultHolder<ItemStack> issuePickupCommand(ItemStack stack, Player player, BlockPos target, InteractionHand hand) {
        this.getCommandingEntities(player.m_9236_(), player, stack).forEach(construct -> construct.pickupItem(target, player));
        return InteractionResultHolder.sidedSuccess(stack, !player.m_9236_().isClientSide());
    }

    private InteractionResultHolder<ItemStack> issueDiagnosticsCommand(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (player.m_9236_().isClientSide() && target instanceof Construct) {
            ManaAndArtifice.instance.proxy.openConstructDiagnostics((Construct) target);
        }
        this.ding(player);
        return InteractionResultHolder.success(stack);
    }

    private InteractionResultHolder<ItemStack> issueFollowCommand(ItemStack stack, Player player, LivingEntity target, InteractionHand hand, boolean guard) {
        if (player.m_9236_().isClientSide()) {
            return InteractionResultHolder.consume(stack);
        } else if (target instanceof Construct construct) {
            ConstructCommandFollowAndGuard task = (ConstructCommandFollowAndGuard) ConstructTasks.FOLLOW_DEFEND.instantiateTask(construct);
            task.setFollowTarget(player);
            task.setShouldGuard(guard);
            if (construct.setCurrentCommand(player, task)) {
                this.ding(player);
            }
            return InteractionResultHolder.success(stack);
        } else {
            this.getCommandingEntities(player.m_9236_(), player, stack).forEach(commandedEntity -> {
                ConstructCommandFollowAndGuard taskx = (ConstructCommandFollowAndGuard) ConstructTasks.FOLLOW_DEFEND.instantiateTask(commandedEntity);
                taskx.setFollowTarget(target);
                taskx.setShouldGuard(guard);
                commandedEntity.setCurrentCommand(player, taskx);
            });
            this.ding(player);
            return InteractionResultHolder.success(stack);
        }
    }

    private InteractionResultHolder<ItemStack> issueChopCommand(ItemStack stack, Player player, LivingEntity target, BlockPos pos, InteractionHand hand) {
        if (player.m_9236_().isClientSide()) {
            return InteractionResultHolder.consume(stack);
        } else if (target instanceof Construct construct) {
            ConstructChop task = (ConstructChop) ConstructTasks.CHOP.instantiateTask(construct);
            task.setAreaManually(pos, pos);
            task.setOneOff(player);
            if (construct.setCurrentCommand(player, task)) {
                this.ding(player);
            }
            return InteractionResultHolder.success(stack);
        } else {
            return InteractionResultHolder.fail(stack);
        }
    }

    private InteractionResultHolder<ItemStack> issueHarvestCommand(ItemStack stack, Player player, LivingEntity target, BlockPos pos, InteractionHand hand) {
        if (player.m_9236_().isClientSide()) {
            return InteractionResultHolder.consume(stack);
        } else if (target instanceof Construct construct) {
            ConstructHarvest task = (ConstructHarvest) ConstructTasks.HARVEST.instantiateTask(construct);
            task.setAreaManually(pos, pos);
            task.setOneOff(player);
            if (construct.setCurrentCommand(player, task)) {
                this.ding(player);
            }
            return InteractionResultHolder.success(stack);
        } else {
            return InteractionResultHolder.fail(stack);
        }
    }

    private InteractionResultHolder<ItemStack> issueCollectFluidCommand(ItemStack stack, Player player, LivingEntity target, BlockPos pos, InteractionHand hand) {
        if (player.m_9236_().isClientSide()) {
            return InteractionResultHolder.consume(stack);
        } else if (target instanceof Construct construct) {
            ConstructCollectFluid task = (ConstructCollectFluid) ConstructTasks.GATHER_FLUID.instantiateTask(construct);
            task.setAreaManually(pos, pos);
            task.setOneOff(player);
            if (construct.setCurrentCommand(player, task)) {
                this.ding(player);
            }
            return InteractionResultHolder.success(stack);
        } else {
            return InteractionResultHolder.fail(stack);
        }
    }

    private InteractionResultHolder<ItemStack> issueStayCommand(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (player.m_9236_().isClientSide()) {
            return InteractionResultHolder.consume(stack);
        } else if (target instanceof Construct construct) {
            if (construct.setCurrentCommand(player, ConstructTasks.STAY.instantiateTask(construct))) {
                this.ding(player);
            }
            return InteractionResultHolder.success(stack);
        } else {
            return InteractionResultHolder.fail(stack);
        }
    }

    private InteractionResult issueInteractCommand(ItemStack stack, UseOnContext context) {
        BlockPos clickPos = context.getClickedPos();
        BlockPos offsetPos = clickPos.offset(context.getClickedFace().getNormal());
        BlockState block = context.getLevel().getBlockState(context.getClickedPos());
        BlockState offsetBlock = context.getLevel().getBlockState(offsetPos);
        if (block.m_60734_() instanceof FillerBlock) {
            BlockEntity be = context.getLevel().getBlockEntity(context.getClickedPos());
            if (be != null && be instanceof OffsetBlockTile) {
                clickPos = context.getClickedPos().offset(((OffsetBlockTile) be).getOffset());
                offsetPos = clickPos.offset(context.getClickedFace().getNormal());
                block = context.getLevel().getBlockState(clickPos);
                offsetBlock = context.getLevel().getBlockState(offsetPos);
            }
        }
        BlockState adjBlock = block;
        BlockState adjOffsetBlock = offsetBlock;
        BlockPos adjOffsetPos = offsetPos;
        BlockPos adjClickPos = clickPos;
        this.getCommandingEntities(context.getLevel(), context.getPlayer(), stack).forEach(commandedEntity -> {
            if (adjBlock.m_60734_() == BlockInit.CONSTRUCT_WORKBENCH.get()) {
                ConstructCommandReturnToTable modify = (ConstructCommandReturnToTable) ConstructTasks.MODIFY.instantiateTask(commandedEntity);
                modify.setTablePos(adjClickPos);
                commandedEntity.setCurrentCommand(context.getPlayer(), modify);
            } else if (adjBlock.m_60734_() instanceof LodestarBlock) {
                ConstructCommandFollowLodestar lodestar = (ConstructCommandFollowLodestar) ConstructTasks.LODESTAR.instantiateTask(commandedEntity);
                lodestar.setTileEntity(adjClickPos, Direction.DOWN);
                if (commandedEntity.setCurrentCommand(context.getPlayer(), lodestar)) {
                    MinecraftForge.EVENT_BUS.post(new GenericProgressionEvent(context.getPlayer(), ProgressionEventIDs.CONSTRUCT_LODESTAR_ASSIGNED));
                    if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
                        CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, adjClickPos, stack);
                    }
                }
            } else if (context.getLevel().m_45976_(ItemEntity.class, new AABB(adjClickPos.above())).size() > 0) {
                this.issuePickupCommand(stack, context.getPlayer(), adjClickPos.above(), context.getHand());
            } else if (!adjOffsetBlock.m_60819_().isEmpty()) {
                this.issueCollectFluidCommand(stack, context.getPlayer(), commandedEntity, adjOffsetPos, context.getHand());
            } else if (MATags.isBlockIn(adjBlock.m_60734_(), new ResourceLocation("logs"))) {
                this.issueChopCommand(stack, context.getPlayer(), commandedEntity, adjClickPos, context.getHand());
            } else if (ConstructHarvest.validBlock(adjBlock)) {
                this.issueHarvestCommand(stack, context.getPlayer(), commandedEntity, adjClickPos, context.getHand());
            }
        });
        this.ding(context.getPlayer());
        return InteractionResult.SUCCESS;
    }

    private InteractionResult issueWearCommand(ItemStack stack, UseOnContext context) {
        BlockPos clickPos = context.getClickedPos();
        BlockState block = context.getLevel().getBlockState(context.getClickedPos());
        if (block.m_60734_() instanceof FillerBlock) {
            BlockEntity be = context.getLevel().getBlockEntity(context.getClickedPos());
            if (be != null && be instanceof OffsetBlockTile) {
                clickPos = context.getClickedPos().offset(((OffsetBlockTile) be).getOffset());
                block = context.getLevel().getBlockState(clickPos);
            }
        }
        BlockPos adjClickPos = clickPos;
        this.getCommandingEntities(context.getLevel(), context.getPlayer(), stack).forEach(commandedEntity -> {
            List<ItemEntity> entities = context.getLevel().m_45976_(ItemEntity.class, new AABB(adjClickPos.above()));
            if (entities.size() > 0) {
                this.issueWearCommand(stack, context.getPlayer(), (ItemEntity) entities.get(0), context.getHand());
            }
        });
        this.ding(context.getPlayer());
        return InteractionResult.SUCCESS;
    }

    private InteractionResult issueMoveCommand(ItemStack stack, UseOnContext context) {
        Level world = context.getLevel();
        if (world.isClientSide) {
            return InteractionResult.PASS;
        } else {
            this.getCommandingEntities(world, context.getPlayer(), stack).forEach(construct -> construct.moveTo(context.getClickLocation(), context.getPlayer()));
            this.ding(context.getPlayer());
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public int capacity() {
        return 0;
    }

    @Override
    public int getIndex(ItemStack stack) {
        if (stack.hasTag() && stack.getItem() instanceof BellOfBidding) {
            int type = stack.getTag().getInt("index");
            BellOfBidding.Commands[] values = BellOfBidding.Commands.values();
            return type >= 0 && type < values.length ? values[type].ordinal() : 0;
        } else {
            return 0;
        }
    }

    @Override
    public void setIndex(ItemStack stack, int index) {
        if (stack.getItem() instanceof BellOfBidding) {
            stack.getOrCreateTag().putInt("index", index);
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("commanding_entity") && stack.getTag().getInt("commanding_entity") != -1;
    }

    public static enum Commands {

        FOLLOW,
        GUARD,
        INTERACT,
        MOVE,
        STAY,
        DIAGNOSTICS,
        EAT,
        ATTACK,
        WEAR;

        private final ResourceLocation iconTexture = RLoc.create("textures/gui/construct/command_icons/" + this.toString().toLowerCase() + ".png");

        public ResourceLocation getIcon() {
            return this.iconTexture;
        }
    }
}