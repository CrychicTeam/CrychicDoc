package com.simibubi.create.content.kinetics.deployer;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.mounted.MountedContraption;
import com.simibubi.create.content.contraptions.render.ActorInstance;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.contraptions.render.ContraptionRenderDispatcher;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import com.simibubi.create.content.schematics.SchematicInstances;
import com.simibubi.create.content.schematics.SchematicWorld;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.tuple.Pair;

public class DeployerMovementBehaviour implements MovementBehaviour {

    @Override
    public Vec3 getActiveAreaOffset(MovementContext context) {
        return Vec3.atLowerCornerOf(((Direction) context.state.m_61143_(DeployerBlock.FACING)).getNormal()).scale(2.0);
    }

    @Override
    public void visitNewPosition(MovementContext context, BlockPos pos) {
        if (!context.world.isClientSide) {
            this.tryGrabbingItem(context);
            DeployerFakePlayer player = this.getPlayer(context);
            DeployerBlockEntity.Mode mode = this.getMode(context);
            if (mode != DeployerBlockEntity.Mode.USE || DeployerHandler.shouldActivate(player.m_21205_(), context.world, pos, null)) {
                this.activate(context, pos, player, mode);
                this.tryDisposeOfExcess(context);
                context.stall = player.blockBreakingProgress != null;
            }
        }
    }

    public void activate(MovementContext context, BlockPos pos, DeployerFakePlayer player, DeployerBlockEntity.Mode mode) {
        Level world = context.world;
        FilterItemStack filter = context.getFilterFromBE();
        if (AllItems.SCHEMATIC.isIn(filter.item())) {
            this.activateAsSchematicPrinter(context, pos, player, world, filter.item());
        }
        Vec3 facingVec = Vec3.atLowerCornerOf(((Direction) context.state.m_61143_(DeployerBlock.FACING)).getNormal());
        facingVec = (Vec3) context.rotation.apply(facingVec);
        Vec3 vec = context.position.subtract(facingVec.scale(2.0));
        float xRot = AbstractContraptionEntity.pitchFromVector(facingVec) - 90.0F;
        if (Math.abs(xRot) > 89.0F) {
            Vec3 initial = new Vec3(0.0, 0.0, 1.0);
            if (context.contraption.entity instanceof OrientedContraptionEntity oce) {
                initial = VecHelper.rotate(initial, (double) oce.getInitialYaw(), Direction.Axis.Y);
            }
            if (context.contraption.entity instanceof CarriageContraptionEntity cce) {
                initial = VecHelper.rotate(initial, 90.0, Direction.Axis.Y);
            }
            facingVec = (Vec3) context.rotation.apply(initial);
        }
        player.m_146922_(AbstractContraptionEntity.yawFromVector(facingVec));
        player.m_146926_(xRot);
        player.placedTracks = false;
        DeployerHandler.activate(player, vec, pos, facingVec, mode);
        if ((context.contraption instanceof MountedContraption || context.contraption instanceof CarriageContraption) && player.placedTracks && context.blockEntityData != null && context.blockEntityData.contains("Owner")) {
            AllAdvancements.SELF_DEPLOYING.awardTo(world.m_46003_(context.blockEntityData.getUUID("Owner")));
        }
    }

    protected void activateAsSchematicPrinter(MovementContext context, BlockPos pos, DeployerFakePlayer player, Level world, ItemStack filter) {
        if (filter.hasTag()) {
            if (world.getBlockState(pos).m_247087_()) {
                CompoundTag tag = filter.getTag();
                if (tag.getBoolean("Deployed")) {
                    SchematicWorld schematicWorld = SchematicInstances.get(world, filter);
                    if (schematicWorld != null) {
                        if (schematicWorld.getBounds().isInside(pos.subtract(schematicWorld.anchor))) {
                            BlockState blockState = schematicWorld.getBlockState(pos);
                            ItemRequirement requirement = ItemRequirement.of(blockState, schematicWorld.getBlockEntity(pos));
                            if (!requirement.isInvalid() && !requirement.isEmpty()) {
                                if (!AllBlocks.BELT.has(blockState)) {
                                    List<ItemRequirement.StackRequirement> requiredItems = requirement.getRequiredItems();
                                    ItemStack contextStack = requiredItems.isEmpty() ? ItemStack.EMPTY : ((ItemRequirement.StackRequirement) requiredItems.get(0)).stack;
                                    if (!context.contraption.hasUniversalCreativeCrate) {
                                        IItemHandler itemHandler = context.contraption.getSharedInventory();
                                        for (ItemRequirement.StackRequirement required : requiredItems) {
                                            ItemStack stack = ItemHelper.extract(itemHandler, required::matches, ItemHelper.ExtractionCountMode.EXACTLY, required.stack.getCount(), true);
                                            if (stack.isEmpty()) {
                                                return;
                                            }
                                        }
                                        for (ItemRequirement.StackRequirement requiredx : requiredItems) {
                                            contextStack = ItemHelper.extract(itemHandler, requiredx::matches, ItemHelper.ExtractionCountMode.EXACTLY, requiredx.stack.getCount(), false);
                                        }
                                    }
                                    CompoundTag data = BlockHelper.prepareBlockEntityData(blockState, schematicWorld.getBlockEntity(pos));
                                    BlockSnapshot blocksnapshot = BlockSnapshot.create(world.dimension(), world, pos);
                                    BlockHelper.placeSchematicBlock(world, blockState, pos, contextStack, data);
                                    if (ForgeEventFactory.onBlockPlace(player, blocksnapshot, Direction.UP)) {
                                        blocksnapshot.restore(true, false);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void tick(MovementContext context) {
        if (!context.world.isClientSide) {
            if (context.stall) {
                DeployerFakePlayer player = this.getPlayer(context);
                DeployerBlockEntity.Mode mode = this.getMode(context);
                Pair<BlockPos, Float> blockBreakingProgress = player.blockBreakingProgress;
                if (blockBreakingProgress != null) {
                    int timer = context.data.getInt("Timer");
                    if (timer < 20) {
                        context.data.putInt("Timer", ++timer);
                        return;
                    }
                    context.data.remove("Timer");
                    this.activate(context, (BlockPos) blockBreakingProgress.getKey(), player, mode);
                    this.tryDisposeOfExcess(context);
                }
                context.stall = player.blockBreakingProgress != null;
            }
        }
    }

    @Override
    public void cancelStall(MovementContext context) {
        if (!context.world.isClientSide) {
            MovementBehaviour.super.cancelStall(context);
            DeployerFakePlayer player = this.getPlayer(context);
            if (player != null) {
                if (player.blockBreakingProgress != null) {
                    context.world.destroyBlockProgress(player.m_19879_(), (BlockPos) player.blockBreakingProgress.getKey(), -1);
                    player.blockBreakingProgress = null;
                }
            }
        }
    }

    @Override
    public void stopMoving(MovementContext context) {
        if (!context.world.isClientSide) {
            DeployerFakePlayer player = this.getPlayer(context);
            if (player != null) {
                this.cancelStall(context);
                context.blockEntityData.put("Inventory", player.m_150109_().save(new ListTag()));
                player.m_146870_();
            }
        }
    }

    private void tryGrabbingItem(MovementContext context) {
        DeployerFakePlayer player = this.getPlayer(context);
        if (player != null) {
            if (player.m_21205_().isEmpty()) {
                FilterItemStack filter = context.getFilterFromBE();
                if (AllItems.SCHEMATIC.isIn(filter.item())) {
                    return;
                }
                ItemStack held = ItemHelper.extract(context.contraption.getSharedInventory(), stack -> filter.test(context.world, stack), 1, false);
                player.m_21008_(InteractionHand.MAIN_HAND, held);
            }
        }
    }

    private void tryDisposeOfExcess(MovementContext context) {
        DeployerFakePlayer player = this.getPlayer(context);
        if (player != null) {
            Inventory inv = player.m_150109_();
            FilterItemStack filter = context.getFilterFromBE();
            for (List<ItemStack> list : Arrays.asList(inv.armor, inv.offhand, inv.items)) {
                for (int i = 0; i < list.size(); i++) {
                    ItemStack itemstack = (ItemStack) list.get(i);
                    if (!itemstack.isEmpty() && (list != inv.items || i != inv.selected || !filter.test(context.world, itemstack))) {
                        this.dropItem(context, itemstack);
                        list.set(i, ItemStack.EMPTY);
                    }
                }
            }
        }
    }

    @Override
    public void writeExtraData(MovementContext context) {
        DeployerFakePlayer player = this.getPlayer(context);
        if (player != null) {
            context.data.put("HeldItem", player.m_21205_().serializeNBT());
        }
    }

    private DeployerFakePlayer getPlayer(MovementContext context) {
        if (!(context.temporaryData instanceof DeployerFakePlayer) && context.world instanceof ServerLevel) {
            UUID owner = context.blockEntityData.contains("Owner") ? context.blockEntityData.getUUID("Owner") : null;
            DeployerFakePlayer deployerFakePlayer = new DeployerFakePlayer((ServerLevel) context.world, owner);
            deployerFakePlayer.onMinecartContraption = context.contraption instanceof MountedContraption;
            deployerFakePlayer.m_150109_().load(context.blockEntityData.getList("Inventory", 10));
            if (context.data.contains("HeldItem")) {
                deployerFakePlayer.m_21008_(InteractionHand.MAIN_HAND, ItemStack.of(context.data.getCompound("HeldItem")));
            }
            context.blockEntityData.remove("Inventory");
            context.temporaryData = deployerFakePlayer;
        }
        return (DeployerFakePlayer) context.temporaryData;
    }

    private DeployerBlockEntity.Mode getMode(MovementContext context) {
        return NBTHelper.readEnum(context.blockEntityData, "Mode", DeployerBlockEntity.Mode.class);
    }

    @Override
    public void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource buffers) {
        if (!ContraptionRenderDispatcher.canInstance()) {
            DeployerRenderer.renderInContraption(context, renderWorld, matrices, buffers);
        }
    }

    @Override
    public boolean hasSpecialInstancedRendering() {
        return true;
    }

    @Nullable
    @Override
    public ActorInstance createInstance(MaterialManager materialManager, VirtualRenderWorld simulationWorld, MovementContext context) {
        return new DeployerActorInstance(materialManager, simulationWorld, context);
    }
}