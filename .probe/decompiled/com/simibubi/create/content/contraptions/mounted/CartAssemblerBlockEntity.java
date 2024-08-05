package com.simibubi.create.content.contraptions.mounted;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.IDisplayAssemblyExceptions;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import com.simibubi.create.content.contraptions.minecart.CouplingHandler;
import com.simibubi.create.content.contraptions.minecart.capability.CapabilityMinecartController;
import com.simibubi.create.content.contraptions.minecart.capability.MinecartController;
import com.simibubi.create.content.redstone.rail.ControllerRailBlock;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.CenteredSideValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class CartAssemblerBlockEntity extends SmartBlockEntity implements IDisplayAssemblyExceptions {

    private static final int assemblyCooldown = 8;

    protected ScrollOptionBehaviour<CartAssemblerBlockEntity.CartMovementMode> movementMode;

    private int ticksSinceMinecartUpdate = 8;

    protected AssemblyException lastException;

    protected AbstractMinecart cartToAssemble;

    public CartAssemblerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.ticksSinceMinecartUpdate < 8) {
            this.ticksSinceMinecartUpdate++;
        }
        this.tryAssemble(this.cartToAssemble);
        this.cartToAssemble = null;
    }

    public void tryAssemble(AbstractMinecart cart) {
        if (cart != null) {
            if (this.isMinecartUpdateValid()) {
                this.resetTicksSinceMinecartUpdate();
                BlockState state = this.f_58857_.getBlockState(this.f_58858_);
                if (AllBlocks.CART_ASSEMBLER.has(state)) {
                    CartAssemblerBlock block = (CartAssemblerBlock) state.m_60734_();
                    CartAssemblerBlock.CartAssemblerAction action = CartAssemblerBlock.getActionForCart(state, cart);
                    if (action.shouldAssemble()) {
                        this.assemble(this.f_58857_, this.f_58858_, cart);
                    }
                    if (action.shouldDisassemble()) {
                        this.disassemble(this.f_58857_, this.f_58858_, cart);
                    }
                    if (action == CartAssemblerBlock.CartAssemblerAction.ASSEMBLE_ACCELERATE && cart.m_20184_().length() > 0.0078125) {
                        Direction facing = cart.getMotionDirection();
                        RailShape railShape = (RailShape) state.m_61143_(CartAssemblerBlock.RAIL_SHAPE);
                        for (Direction d : Iterate.directionsInAxis(railShape == RailShape.EAST_WEST ? Direction.Axis.X : Direction.Axis.Z)) {
                            if (this.f_58857_.getBlockState(this.f_58858_.relative(d)).m_60796_(this.f_58857_, this.f_58858_.relative(d))) {
                                facing = d.getOpposite();
                            }
                        }
                        float speed = block.getRailMaxSpeed(state, this.f_58857_, this.f_58858_, cart);
                        cart.m_20334_((double) ((float) facing.getStepX() * speed), (double) ((float) facing.getStepY() * speed), (double) ((float) facing.getStepZ() * speed));
                    }
                    if (action == CartAssemblerBlock.CartAssemblerAction.ASSEMBLE_ACCELERATE_DIRECTIONAL) {
                        Vec3i accelerationVector = ControllerRailBlock.getAccelerationVector((BlockState) ((BlockState) AllBlocks.CONTROLLER_RAIL.getDefaultState().m_61124_(ControllerRailBlock.SHAPE, (RailShape) state.m_61143_(CartAssemblerBlock.RAIL_SHAPE))).m_61124_(ControllerRailBlock.BACKWARDS, (Boolean) state.m_61143_(CartAssemblerBlock.BACKWARDS)));
                        float speed = block.getRailMaxSpeed(state, this.f_58857_, this.f_58858_, cart);
                        cart.m_20256_(Vec3.atLowerCornerOf(accelerationVector).scale((double) speed));
                    }
                    if (action == CartAssemblerBlock.CartAssemblerAction.DISASSEMBLE_BRAKE) {
                        Vec3 diff = VecHelper.getCenterOf(this.f_58858_).subtract(cart.m_20182_());
                        cart.m_20334_(diff.x / 16.0, 0.0, diff.z / 16.0);
                    }
                }
            }
        }
    }

    protected void assemble(Level world, BlockPos pos, AbstractMinecart cart) {
        if (cart.m_20197_().isEmpty()) {
            LazyOptional<MinecartController> optional = cart.getCapability(CapabilityMinecartController.MINECART_CONTROLLER_CAPABILITY);
            if (!optional.isPresent() || !optional.orElse(null).isCoupledThroughContraption()) {
                CartAssemblerBlockEntity.CartMovementMode mode = CartAssemblerBlockEntity.CartMovementMode.values()[this.movementMode.value];
                MountedContraption contraption = new MountedContraption(mode);
                try {
                    if (!contraption.assemble(world, pos)) {
                        return;
                    }
                    this.lastException = null;
                    this.sendData();
                } catch (AssemblyException var11) {
                    this.lastException = var11;
                    this.sendData();
                    return;
                }
                boolean couplingFound = contraption.connectedCart != null;
                Direction initialOrientation = CartAssemblerBlock.getHorizontalDirection(this.m_58900_());
                if (couplingFound) {
                    cart.m_6034_((double) ((float) pos.m_123341_() + 0.5F), (double) pos.m_123342_(), (double) ((float) pos.m_123343_() + 0.5F));
                    if (!CouplingHandler.tryToCoupleCarts(null, world, cart.m_19879_(), contraption.connectedCart.m_19879_())) {
                        return;
                    }
                }
                contraption.removeBlocksFromWorld(world, BlockPos.ZERO);
                contraption.startMoving(world);
                contraption.expandBoundsAroundAxis(Direction.Axis.Y);
                if (couplingFound) {
                    Vec3 diff = contraption.connectedCart.m_20182_().subtract(cart.m_20182_());
                    initialOrientation = Direction.fromYRot(Mth.atan2(diff.z, diff.x) * 180.0 / Math.PI);
                }
                OrientedContraptionEntity entity = OrientedContraptionEntity.create(world, contraption, initialOrientation);
                if (couplingFound) {
                    entity.setCouplingId(cart.m_20148_());
                }
                entity.m_6034_((double) pos.m_123341_() + 0.5, (double) pos.m_123342_(), (double) pos.m_123343_() + 0.5);
                world.m_7967_(entity);
                entity.m_20329_(cart);
                if (cart instanceof MinecartFurnace) {
                    CompoundTag nbt = cart.serializeNBT();
                    nbt.putDouble("PushZ", 0.0);
                    nbt.putDouble("PushX", 0.0);
                    cart.deserializeNBT(nbt);
                }
                if (contraption.containsBlockBreakers()) {
                    this.award(AllAdvancements.CONTRAPTION_ACTORS);
                }
            }
        }
    }

    protected void disassemble(Level world, BlockPos pos, AbstractMinecart cart) {
        if (!cart.m_20197_().isEmpty()) {
            Entity entity = (Entity) cart.m_20197_().get(0);
            if (entity instanceof OrientedContraptionEntity contraption) {
                UUID couplingId = contraption.getCouplingId();
                if (couplingId == null) {
                    contraption.yaw = CartAssemblerBlock.getHorizontalDirection(this.m_58900_()).toYRot();
                    this.disassembleCart(cart);
                } else {
                    Couple<MinecartController> coupledCarts = contraption.getCoupledCartsIfPresent();
                    if (coupledCarts != null) {
                        for (boolean current : Iterate.trueAndFalse) {
                            MinecartController minecartController = coupledCarts.get(current);
                            if (minecartController.cart() != cart) {
                                BlockPos otherPos = minecartController.cart().m_20183_();
                                BlockState blockState = world.getBlockState(otherPos);
                                if (!AllBlocks.CART_ASSEMBLER.has(blockState)) {
                                    return;
                                }
                                if (!CartAssemblerBlock.getActionForCart(blockState, minecartController.cart()).shouldDisassemble()) {
                                    return;
                                }
                            }
                        }
                        for (boolean currentx : Iterate.trueAndFalse) {
                            coupledCarts.get(currentx).removeConnection(currentx);
                        }
                        this.disassembleCart(cart);
                    }
                }
            }
        }
    }

    protected void disassembleCart(AbstractMinecart cart) {
        cart.m_20153_();
        if (cart instanceof MinecartFurnace) {
            CompoundTag nbt = cart.serializeNBT();
            nbt.putDouble("PushZ", cart.m_20184_().x);
            nbt.putDouble("PushX", cart.m_20184_().z);
            cart.deserializeNBT(nbt);
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        this.movementMode = new ScrollOptionBehaviour(CartAssemblerBlockEntity.CartMovementMode.class, Lang.translateDirect("contraptions.cart_movement_mode"), this, this.getMovementModeSlot());
        behaviours.add(this.movementMode);
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.CONTRAPTION_ACTORS });
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        AssemblyException.write(compound, this.lastException);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        this.lastException = AssemblyException.read(compound);
        super.read(compound, clientPacket);
    }

    @Override
    public AssemblyException getLastAssemblyException() {
        return this.lastException;
    }

    protected ValueBoxTransform getMovementModeSlot() {
        return new CartAssemblerBlockEntity.CartAssemblerValueBoxTransform();
    }

    public void resetTicksSinceMinecartUpdate() {
        this.ticksSinceMinecartUpdate = 0;
    }

    public void assembleNextTick(AbstractMinecart cart) {
        if (this.cartToAssemble == null) {
            this.cartToAssemble = cart;
        }
    }

    public boolean isMinecartUpdateValid() {
        return this.ticksSinceMinecartUpdate >= 8;
    }

    private class CartAssemblerValueBoxTransform extends CenteredSideValueBoxTransform {

        public CartAssemblerValueBoxTransform() {
            super((state, d) -> {
                if (d.getAxis().isVertical()) {
                    return false;
                } else if (!state.m_61138_(CartAssemblerBlock.RAIL_SHAPE)) {
                    return false;
                } else {
                    RailShape railShape = (RailShape) state.m_61143_(CartAssemblerBlock.RAIL_SHAPE);
                    return d.getAxis() == Direction.Axis.X == (railShape == RailShape.NORTH_SOUTH);
                }
            });
        }

        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8.0, 7.0, 17.5);
        }
    }

    public static enum CartMovementMode implements INamedIconOptions {

        ROTATE(AllIcons.I_CART_ROTATE), ROTATE_PAUSED(AllIcons.I_CART_ROTATE_PAUSED), ROTATION_LOCKED(AllIcons.I_CART_ROTATE_LOCKED);

        private String translationKey;

        private AllIcons icon;

        private CartMovementMode(AllIcons icon) {
            this.icon = icon;
            this.translationKey = "contraptions.cart_movement_mode." + Lang.asId(this.name());
        }

        @Override
        public AllIcons getIcon() {
            return this.icon;
        }

        @Override
        public String getTranslationKey() {
            return this.translationKey;
        }
    }
}