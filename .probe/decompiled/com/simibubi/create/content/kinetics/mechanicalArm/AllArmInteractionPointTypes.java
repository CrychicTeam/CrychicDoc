package com.simibubi.create.content.kinetics.mechanicalArm;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.crafter.MechanicalCrafterBlock;
import com.simibubi.create.content.kinetics.crafter.MechanicalCrafterBlockEntity;
import com.simibubi.create.content.kinetics.deployer.DeployerBlock;
import com.simibubi.create.content.kinetics.saw.SawBlock;
import com.simibubi.create.content.logistics.chute.AbstractChuteBlock;
import com.simibubi.create.content.logistics.funnel.AbstractFunnelBlock;
import com.simibubi.create.content.logistics.funnel.BeltFunnelBlock;
import com.simibubi.create.content.logistics.funnel.FunnelBlock;
import com.simibubi.create.content.logistics.funnel.FunnelBlockEntity;
import com.simibubi.create.content.logistics.tunnel.BeltTunnelBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.InvManipulationBehaviour;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class AllArmInteractionPointTypes {

    public static final AllArmInteractionPointTypes.BasinType BASIN = register("basin", AllArmInteractionPointTypes.BasinType::new);

    public static final AllArmInteractionPointTypes.BeltType BELT = register("belt", AllArmInteractionPointTypes.BeltType::new);

    public static final AllArmInteractionPointTypes.BlazeBurnerType BLAZE_BURNER = register("blaze_burner", AllArmInteractionPointTypes.BlazeBurnerType::new);

    public static final AllArmInteractionPointTypes.ChuteType CHUTE = register("chute", AllArmInteractionPointTypes.ChuteType::new);

    public static final AllArmInteractionPointTypes.CrafterType CRAFTER = register("crafter", AllArmInteractionPointTypes.CrafterType::new);

    public static final AllArmInteractionPointTypes.CrushingWheelsType CRUSHING_WHEELS = register("crushing_wheels", AllArmInteractionPointTypes.CrushingWheelsType::new);

    public static final AllArmInteractionPointTypes.DeployerType DEPLOYER = register("deployer", AllArmInteractionPointTypes.DeployerType::new);

    public static final AllArmInteractionPointTypes.DepotType DEPOT = register("depot", AllArmInteractionPointTypes.DepotType::new);

    public static final AllArmInteractionPointTypes.FunnelType FUNNEL = register("funnel", AllArmInteractionPointTypes.FunnelType::new);

    public static final AllArmInteractionPointTypes.MillstoneType MILLSTONE = register("millstone", AllArmInteractionPointTypes.MillstoneType::new);

    public static final AllArmInteractionPointTypes.SawType SAW = register("saw", AllArmInteractionPointTypes.SawType::new);

    public static final AllArmInteractionPointTypes.CampfireType CAMPFIRE = register("campfire", AllArmInteractionPointTypes.CampfireType::new);

    public static final AllArmInteractionPointTypes.ComposterType COMPOSTER = register("composter", AllArmInteractionPointTypes.ComposterType::new);

    public static final AllArmInteractionPointTypes.JukeboxType JUKEBOX = register("jukebox", AllArmInteractionPointTypes.JukeboxType::new);

    public static final AllArmInteractionPointTypes.RespawnAnchorType RESPAWN_ANCHOR = register("respawn_anchor", AllArmInteractionPointTypes.RespawnAnchorType::new);

    private static <T extends ArmInteractionPointType> T register(String id, Function<ResourceLocation, T> factory) {
        T type = (T) factory.apply(Create.asResource(id));
        ArmInteractionPointType.register(type);
        return type;
    }

    public static void register() {
    }

    public static class BasinType extends ArmInteractionPointType {

        public BasinType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return AllBlocks.BASIN.has(state);
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new ArmInteractionPoint(this, level, pos, state);
        }
    }

    public static class BeltPoint extends AllArmInteractionPointTypes.DepotPoint {

        public BeltPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
        }

        @Override
        public void keepAlive() {
            super.keepAlive();
            BeltBlockEntity beltBE = BeltHelper.getSegmentBE(this.level, this.pos);
            if (beltBE != null) {
                TransportedItemStackHandlerBehaviour transport = beltBE.getBehaviour(TransportedItemStackHandlerBehaviour.TYPE);
                if (transport != null) {
                    MutableBoolean found = new MutableBoolean(false);
                    transport.handleProcessingOnAllItems(tis -> {
                        if (found.isTrue()) {
                            return TransportedItemStackHandlerBehaviour.TransportedResult.doNothing();
                        } else {
                            tis.lockedExternally = true;
                            found.setTrue();
                            return TransportedItemStackHandlerBehaviour.TransportedResult.doNothing();
                        }
                    });
                }
            }
        }
    }

    public static class BeltType extends ArmInteractionPointType {

        public BeltType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return AllBlocks.BELT.has(state) && !(level.getBlockState(pos.above()).m_60734_() instanceof BeltTunnelBlock);
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new AllArmInteractionPointTypes.BeltPoint(this, level, pos, state);
        }
    }

    public static class BlazeBurnerPoint extends AllArmInteractionPointTypes.DepositOnlyArmInteractionPoint {

        public BlazeBurnerPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
        }

        @Override
        public ItemStack insert(ItemStack stack, boolean simulate) {
            ItemStack input = stack.copy();
            InteractionResultHolder<ItemStack> res = BlazeBurnerBlock.tryInsert(this.cachedState, this.level, this.pos, input, false, false, simulate);
            ItemStack remainder = res.getObject();
            if (input.isEmpty()) {
                return remainder;
            } else {
                if (!simulate) {
                    Containers.dropItemStack(this.level, (double) this.pos.m_123341_(), (double) this.pos.m_123342_(), (double) this.pos.m_123343_(), remainder);
                }
                return input;
            }
        }
    }

    public static class BlazeBurnerType extends ArmInteractionPointType {

        public BlazeBurnerType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return AllBlocks.BLAZE_BURNER.has(state);
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new AllArmInteractionPointTypes.BlazeBurnerPoint(this, level, pos, state);
        }
    }

    public static class CampfirePoint extends AllArmInteractionPointTypes.DepositOnlyArmInteractionPoint {

        public CampfirePoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
        }

        @Override
        public ItemStack insert(ItemStack stack, boolean simulate) {
            if (!(this.level.getBlockEntity(this.pos) instanceof CampfireBlockEntity campfireBE)) {
                return stack;
            } else {
                Optional<CampfireCookingRecipe> recipe = campfireBE.getCookableRecipe(stack);
                if (recipe.isEmpty()) {
                    return stack;
                } else if (!simulate) {
                    ItemStack remainder = stack.copy();
                    campfireBE.placeFood(null, remainder, ((CampfireCookingRecipe) recipe.get()).m_43753_());
                    return remainder;
                } else {
                    boolean hasSpace = false;
                    for (ItemStack campfireStack : campfireBE.getItems()) {
                        if (campfireStack.isEmpty()) {
                            hasSpace = true;
                            break;
                        }
                    }
                    if (!hasSpace) {
                        return stack;
                    } else {
                        ItemStack remainder = stack.copy();
                        remainder.shrink(1);
                        return remainder;
                    }
                }
            }
        }
    }

    public static class CampfireType extends ArmInteractionPointType {

        public CampfireType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return state.m_60734_() instanceof CampfireBlock;
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new AllArmInteractionPointTypes.CampfirePoint(this, level, pos, state);
        }
    }

    public static class ChuteType extends ArmInteractionPointType {

        public ChuteType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return AbstractChuteBlock.isChute(state);
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new AllArmInteractionPointTypes.TopFaceArmInteractionPoint(this, level, pos, state);
        }
    }

    public static class ComposterPoint extends ArmInteractionPoint {

        public ComposterPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
        }

        @Override
        protected Vec3 getInteractionPositionVector() {
            return Vec3.atLowerCornerOf(this.pos).add(0.5, 0.8125, 0.5);
        }

        @Override
        public void updateCachedState() {
            BlockState oldState = this.cachedState;
            super.updateCachedState();
            if (oldState != this.cachedState) {
                this.cachedHandler.invalidate();
            }
        }

        @Nullable
        @Override
        protected IItemHandler getHandler() {
            return null;
        }

        protected WorldlyContainer getContainer() {
            ComposterBlock composterBlock = (ComposterBlock) Blocks.COMPOSTER;
            return composterBlock.getContainer(this.cachedState, this.level, this.pos);
        }

        @Override
        public ItemStack insert(ItemStack stack, boolean simulate) {
            IItemHandler handler = new SidedInvWrapper(this.getContainer(), Direction.UP);
            return ItemHandlerHelper.insertItem(handler, stack, simulate);
        }

        @Override
        public ItemStack extract(int slot, int amount, boolean simulate) {
            IItemHandler handler = new SidedInvWrapper(this.getContainer(), Direction.DOWN);
            return handler.extractItem(slot, amount, simulate);
        }

        @Override
        public int getSlotCount() {
            return 2;
        }
    }

    public static class ComposterType extends ArmInteractionPointType {

        public ComposterType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return state.m_60713_(Blocks.COMPOSTER);
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new AllArmInteractionPointTypes.ComposterPoint(this, level, pos, state);
        }
    }

    public static class CrafterPoint extends ArmInteractionPoint {

        public CrafterPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
        }

        @Override
        protected Direction getInteractionDirection() {
            return ((Direction) this.cachedState.m_61145_(MechanicalCrafterBlock.HORIZONTAL_FACING).orElse(Direction.SOUTH)).getOpposite();
        }

        @Override
        protected Vec3 getInteractionPositionVector() {
            return super.getInteractionPositionVector().add(Vec3.atLowerCornerOf(this.getInteractionDirection().getNormal()).scale(0.5));
        }

        @Override
        public void updateCachedState() {
            BlockState oldState = this.cachedState;
            super.updateCachedState();
            if (oldState != this.cachedState) {
                this.cachedAngles = null;
            }
        }

        @Override
        public ItemStack extract(int slot, int amount, boolean simulate) {
            if (!(this.level.getBlockEntity(this.pos) instanceof MechanicalCrafterBlockEntity crafter)) {
                return ItemStack.EMPTY;
            } else {
                SmartInventory inventory = crafter.getInventory();
                inventory.allowExtraction();
                ItemStack extract = super.extract(slot, amount, simulate);
                inventory.forbidExtraction();
                return extract;
            }
        }
    }

    public static class CrafterType extends ArmInteractionPointType {

        public CrafterType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return AllBlocks.MECHANICAL_CRAFTER.has(state);
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new AllArmInteractionPointTypes.CrafterPoint(this, level, pos, state);
        }
    }

    public static class CrushingWheelsType extends ArmInteractionPointType {

        public CrushingWheelsType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return AllBlocks.CRUSHING_WHEEL_CONTROLLER.has(state);
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new AllArmInteractionPointTypes.TopFaceArmInteractionPoint(this, level, pos, state);
        }
    }

    public static class DeployerPoint extends ArmInteractionPoint {

        public DeployerPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
        }

        @Override
        protected Direction getInteractionDirection() {
            return ((Direction) this.cachedState.m_61145_(DeployerBlock.FACING).orElse(Direction.UP)).getOpposite();
        }

        @Override
        protected Vec3 getInteractionPositionVector() {
            return super.getInteractionPositionVector().add(Vec3.atLowerCornerOf(this.getInteractionDirection().getNormal()).scale(0.65F));
        }

        @Override
        public void updateCachedState() {
            BlockState oldState = this.cachedState;
            super.updateCachedState();
            if (oldState != this.cachedState) {
                this.cachedAngles = null;
            }
        }
    }

    public static class DeployerType extends ArmInteractionPointType {

        public DeployerType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return AllBlocks.DEPLOYER.has(state);
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new AllArmInteractionPointTypes.DeployerPoint(this, level, pos, state);
        }
    }

    public static class DepositOnlyArmInteractionPoint extends ArmInteractionPoint {

        public DepositOnlyArmInteractionPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
        }

        @Override
        public void cycleMode() {
        }

        @Override
        public ItemStack extract(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotCount() {
            return 0;
        }
    }

    public static class DepotPoint extends ArmInteractionPoint {

        public DepotPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
        }

        @Override
        protected Vec3 getInteractionPositionVector() {
            return Vec3.atLowerCornerOf(this.pos).add(0.5, 0.875, 0.5);
        }
    }

    public static class DepotType extends ArmInteractionPointType {

        public DepotType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return AllBlocks.DEPOT.has(state) || AllBlocks.WEIGHTED_EJECTOR.has(state) || AllBlocks.TRACK_STATION.has(state);
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new AllArmInteractionPointTypes.DepotPoint(this, level, pos, state);
        }
    }

    public static class FunnelPoint extends AllArmInteractionPointTypes.DepositOnlyArmInteractionPoint {

        public FunnelPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
        }

        @Override
        protected Vec3 getInteractionPositionVector() {
            Direction funnelFacing = FunnelBlock.getFunnelFacing(this.cachedState);
            Vec3i normal = funnelFacing != null ? funnelFacing.getNormal() : Vec3i.ZERO;
            return VecHelper.getCenterOf(this.pos).add(Vec3.atLowerCornerOf(normal).scale(-0.15F));
        }

        @Override
        protected Direction getInteractionDirection() {
            Direction funnelFacing = FunnelBlock.getFunnelFacing(this.cachedState);
            return funnelFacing != null ? funnelFacing.getOpposite() : Direction.UP;
        }

        @Override
        public void updateCachedState() {
            BlockState oldState = this.cachedState;
            super.updateCachedState();
            if (oldState != this.cachedState) {
                this.cachedAngles = null;
            }
        }

        @Override
        public ItemStack insert(ItemStack stack, boolean simulate) {
            FilteringBehaviour filtering = BlockEntityBehaviour.get(this.level, this.pos, FilteringBehaviour.TYPE);
            InvManipulationBehaviour inserter = BlockEntityBehaviour.get(this.level, this.pos, InvManipulationBehaviour.TYPE);
            if ((Boolean) this.cachedState.m_61145_(BlockStateProperties.POWERED).orElse(false)) {
                return stack;
            } else if (inserter == null) {
                return stack;
            } else if (filtering != null && !filtering.test(stack)) {
                return stack;
            } else {
                if (simulate) {
                    inserter.simulate();
                }
                ItemStack insert = inserter.insert(stack);
                if (!simulate && insert.getCount() != stack.getCount() && this.level.getBlockEntity(this.pos) instanceof FunnelBlockEntity funnelBlockEntity) {
                    funnelBlockEntity.onTransfer(stack);
                    if (funnelBlockEntity.hasFlap()) {
                        funnelBlockEntity.flap(true);
                    }
                }
                return insert;
            }
        }
    }

    public static class FunnelType extends ArmInteractionPointType {

        public FunnelType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return state.m_60734_() instanceof AbstractFunnelBlock && (!state.m_61138_(FunnelBlock.EXTRACTING) || !(Boolean) state.m_61143_(FunnelBlock.EXTRACTING)) && (!state.m_61138_(BeltFunnelBlock.SHAPE) || state.m_61143_(BeltFunnelBlock.SHAPE) != BeltFunnelBlock.Shape.PUSHING);
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new AllArmInteractionPointTypes.FunnelPoint(this, level, pos, state);
        }
    }

    public static class JukeboxPoint extends AllArmInteractionPointTypes.TopFaceArmInteractionPoint {

        public JukeboxPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
        }

        @Override
        public int getSlotCount() {
            return 1;
        }

        @Override
        public ItemStack insert(ItemStack stack, boolean simulate) {
            Item item = stack.getItem();
            if (!(item instanceof RecordItem)) {
                return stack;
            } else if ((Boolean) this.cachedState.m_61145_(JukeboxBlock.HAS_RECORD).orElse(true)) {
                return stack;
            } else if (this.level.getBlockEntity(this.pos) instanceof JukeboxBlockEntity jukeboxBE) {
                if (!jukeboxBE.m_272036_().isEmpty()) {
                    return stack;
                } else {
                    ItemStack remainder = stack.copy();
                    ItemStack toInsert = remainder.split(1);
                    if (!simulate) {
                        jukeboxBE.m_272287_(toInsert);
                        this.level.setBlock(this.pos, (BlockState) this.cachedState.m_61124_(JukeboxBlock.HAS_RECORD, true), 2);
                        this.level.m_5898_(null, 1010, this.pos, Item.getId(item));
                    }
                    return remainder;
                }
            } else {
                return stack;
            }
        }

        @Override
        public ItemStack extract(int slot, int amount, boolean simulate) {
            if (!(Boolean) this.cachedState.m_61145_(JukeboxBlock.HAS_RECORD).orElse(false)) {
                return ItemStack.EMPTY;
            } else if (this.level.getBlockEntity(this.pos) instanceof JukeboxBlockEntity jukeboxBE) {
                ItemStack record = jukeboxBE.m_272036_();
                if (record.isEmpty()) {
                    return ItemStack.EMPTY;
                } else {
                    if (!simulate) {
                        this.level.m_46796_(1010, this.pos, 0);
                        jukeboxBE.m_6211_();
                        this.level.setBlock(this.pos, (BlockState) this.cachedState.m_61124_(JukeboxBlock.HAS_RECORD, false), 2);
                    }
                    return record;
                }
            } else {
                return ItemStack.EMPTY;
            }
        }
    }

    public static class JukeboxType extends ArmInteractionPointType {

        public JukeboxType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return state.m_60713_(Blocks.JUKEBOX);
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new AllArmInteractionPointTypes.JukeboxPoint(this, level, pos, state);
        }
    }

    public static class MillstoneType extends ArmInteractionPointType {

        public MillstoneType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return AllBlocks.MILLSTONE.has(state);
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new ArmInteractionPoint(this, level, pos, state);
        }
    }

    public static class RespawnAnchorPoint extends AllArmInteractionPointTypes.DepositOnlyArmInteractionPoint {

        public RespawnAnchorPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
        }

        @Override
        protected Vec3 getInteractionPositionVector() {
            return Vec3.atLowerCornerOf(this.pos).add(0.5, 1.0, 0.5);
        }

        @Override
        public ItemStack insert(ItemStack stack, boolean simulate) {
            if (!stack.is(Items.GLOWSTONE)) {
                return stack;
            } else if ((Integer) this.cachedState.m_61145_(RespawnAnchorBlock.CHARGE).orElse(4) == 4) {
                return stack;
            } else {
                if (!simulate) {
                    RespawnAnchorBlock.charge(null, this.level, this.pos, this.cachedState);
                }
                ItemStack remainder = stack.copy();
                remainder.shrink(1);
                return remainder;
            }
        }
    }

    public static class RespawnAnchorType extends ArmInteractionPointType {

        public RespawnAnchorType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return state.m_60713_(Blocks.RESPAWN_ANCHOR);
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new AllArmInteractionPointTypes.RespawnAnchorPoint(this, level, pos, state);
        }
    }

    public static class SawType extends ArmInteractionPointType {

        public SawType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return AllBlocks.MECHANICAL_SAW.has(state) && state.m_61143_(SawBlock.FACING) == Direction.UP && ((KineticBlockEntity) level.getBlockEntity(pos)).getSpeed() != 0.0F;
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new AllArmInteractionPointTypes.DepotPoint(this, level, pos, state);
        }
    }

    public static class TopFaceArmInteractionPoint extends ArmInteractionPoint {

        public TopFaceArmInteractionPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
        }

        @Override
        protected Vec3 getInteractionPositionVector() {
            return Vec3.atLowerCornerOf(this.pos).add(0.5, 1.0, 0.5);
        }
    }
}