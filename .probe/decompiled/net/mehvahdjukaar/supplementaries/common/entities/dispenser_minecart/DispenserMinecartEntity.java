package net.mehvahdjukaar.supplementaries.common.entities.dispenser_minecart;

import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModEntities;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DispenserMinecartEntity extends Minecart implements Container, MenuProvider {

    private static final BlockState BLOCK_STATE = (BlockState) Blocks.DISPENSER.defaultBlockState().m_61124_(DispenserBlock.FACING, Direction.UP);

    private static final BlockState BLOCK_STATE_FRONT = (BlockState) Blocks.DISPENSER.defaultBlockState().m_61124_(DispenserBlock.FACING, Direction.NORTH);

    private final MovingDispenserBlockEntity dispenser;

    private boolean onActivator = false;

    private boolean powered = false;

    public DispenserMinecartEntity(Level level, double x, double y, double z) {
        this((EntityType<DispenserMinecartEntity>) ModEntities.DISPENSER_MINECART.get(), level);
        this.m_6034_(x, y, z);
        this.f_19854_ = x;
        this.f_19855_ = y;
        this.f_19856_ = z;
    }

    public DispenserMinecartEntity(EntityType<DispenserMinecartEntity> entityType, Level level) {
        super(entityType, level);
        BlockState state = CommonConfigs.Redstone.DISPENSER_MINECART_FRONT.get() ? BLOCK_STATE_FRONT : BLOCK_STATE;
        this.dispenser = new MovingDispenserBlockEntity(BlockEntityType.DISPENSER, BlockPos.ZERO, state, this);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.m_7380_(pCompound);
        pCompound.put("Dispenser", this.dispenser.m_187482_());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.m_7378_(pCompound);
        this.dispenser.m_142466_(pCompound.getCompound("Dispenser"));
    }

    @Override
    public ItemStack getPickResult() {
        return ((Item) ModRegistry.DISPENSER_MINECART_ITEM.get()).getDefaultInstance();
    }

    @Override
    protected Item getDropItem() {
        return (Item) ModRegistry.DISPENSER_MINECART_ITEM.get();
    }

    @Override
    public AbstractMinecart.Type getMinecartType() {
        return AbstractMinecart.Type.CHEST;
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return BLOCK_STATE;
    }

    @Override
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        InteractionResult ret = InteractionResult.PASS;
        if (ret.consumesAction()) {
            return ret;
        } else {
            pPlayer.openMenu(this);
            if (!pPlayer.m_9236_().isClientSide) {
                this.m_146852_(GameEvent.CONTAINER_OPEN, pPlayer);
                PiglinAi.angerNearbyPiglins(pPlayer, true);
                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    public int getContainerSize() {
        return this.dispenser.m_6643_();
    }

    @Override
    public boolean isEmpty() {
        return this.dispenser.m_7983_();
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return this.dispenser.m_8020_(pIndex);
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        return this.dispenser.m_7407_(pIndex, pCount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return this.dispenser.m_8016_(pIndex);
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {
        this.dispenser.m_6836_(pIndex, pStack);
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.m_213877_() ? false : pPlayer.m_20280_(this) <= 64.0;
    }

    @Override
    public void clearContent() {
        this.dispenser.m_6211_();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return this.dispenser.m_7208_(pContainerId, pInventory, pPlayer);
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        if (!this.m_9236_().isClientSide && reason.shouldDestroy()) {
            Containers.dropContents(this.m_9236_(), this, this);
        }
        super.m_142687_(reason);
    }

    @Override
    protected void applyNaturalSlowdown() {
        float f = 0.98F;
        int i = 15 - AbstractContainerMenu.getRedstoneSignalFromContainer(this);
        f += (float) i * 0.001F;
        if (this.m_20069_()) {
            f *= 0.95F;
        }
        this.m_20256_(this.m_20184_().multiply((double) f, 0.0, (double) f));
    }

    @Override
    public SlotAccess getSlot(int pSlot) {
        return pSlot >= 0 && pSlot < this.getContainerSize() ? new SlotAccess() {

            @Override
            public ItemStack get() {
                return DispenserMinecartEntity.this.dispenser.m_8020_(pSlot);
            }

            @Override
            public boolean set(ItemStack carried) {
                DispenserMinecartEntity.this.dispenser.m_6836_(pSlot, carried);
                return true;
            }
        } : super.m_141942_(pSlot);
    }

    @Override
    public void activateMinecart(int pX, int pY, int pZ, boolean pReceivingPower) {
        this.onActivator = true;
        if (!this.powered && pReceivingPower && this.m_9236_() instanceof ServerLevel serverLevel) {
            this.dispenseFrom(serverLevel, this.m_20183_());
        }
        this.powered = pReceivingPower;
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.dispenser.m_142339_(this.m_9236_());
        if (!this.m_9236_().isClientSide && this.m_6084_() && this.powered) {
            if (!this.onActivator) {
                this.powered = false;
            }
            this.onActivator = false;
        }
    }

    @Override
    public void teleportTo(double pX, double pY, double pZ) {
        super.m_6021_(pX, pY, pZ);
        this.m_9236_().broadcastEntityEvent(this, (byte) 46);
    }

    protected void dispenseFrom(ServerLevel pLevel, BlockPos pPos) {
        ((ILevelEventRedirect) pLevel).setRedirected(true, this.m_20182_());
        int i = this.dispenser.m_222761_(pLevel.m_213780_());
        if (i < 0) {
            pLevel.m_46796_(1001, pPos, 0);
            pLevel.m_142346_(this, GameEvent.BLOCK_ACTIVATE, pPos);
        } else {
            ItemStack itemstack = this.dispenser.m_8020_(i);
            try {
                DispenseItemBehavior dispenseitembehavior = ((DispenserBlock) Blocks.DISPENSER).getDispenseMethod(itemstack);
                if (dispenseitembehavior != DispenseItemBehavior.NOOP) {
                    MovingBlockSource<?> blockSource = new MovingBlockSource<>(this, this.dispenser);
                    this.dispenser.m_6836_(i, dispenseitembehavior.dispense(blockSource, itemstack));
                }
            } catch (Exception var7) {
                Supplementaries.LOGGER.warn("Failed to execute Dispenser Minecart behavior for item {}", itemstack.getItem());
            }
        }
        ((ILevelEventRedirect) pLevel).setRedirected(false, Vec3.ZERO);
    }
}