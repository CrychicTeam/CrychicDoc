package se.mickelus.tetra.blocks.forged.container;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.RegistryObject;
import se.mickelus.mutil.util.ItemHandlerWrapper;
import se.mickelus.mutil.util.TileEntityOptional;
import se.mickelus.tetra.blocks.salvage.BlockInteraction;

@ParametersAreNonnullByDefault
public class ForgedContainerBlockEntity extends BlockEntity implements MenuProvider {

    private static final String inventoryKey = "inv";

    private static final ResourceLocation lockLootTable = new ResourceLocation("tetra", "forged/lock_break");

    private static final ResourceLocation containerLootTable = new ResourceLocation("tetra", "forged/container_content");

    public static RegistryObject<BlockEntityType<ForgedContainerBlockEntity>> type;

    public static int lockIntegrityMax = 4;

    public static int lockCount = 4;

    public static int lidIntegrityMax = 5;

    public static int compartmentCount = 3;

    public static int compartmentSize = 54;

    private final int[] lockIntegrity;

    private final LazyOptional<ItemStackHandler> handler = LazyOptional.of(() -> new ItemStackHandler(compartmentSize * compartmentCount) {

        @Override
        protected void onContentsChanged(int slot) {
            ForgedContainerBlockEntity.this.m_6596_();
        }
    });

    public long openTime = -1L;

    private int lidIntegrity = 0;

    public ForgedContainerBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(type.get(), blockPos0, blockState1);
        this.lockIntegrity = new int[lockCount];
    }

    public static BlockState getUpdatedBlockState(BlockState blockState, int[] lockIntegrity, int lidIntegrity) {
        return blockState.m_61143_(ForgedContainerBlock.flippedProp) ? (BlockState) ((BlockState) ((BlockState) ((BlockState) blockState.m_61124_(ForgedContainerBlock.locked1Prop, lockIntegrity[2] > 0)).m_61124_(ForgedContainerBlock.locked2Prop, lockIntegrity[3] > 0)).m_61124_(ForgedContainerBlock.anyLockedProp, Arrays.stream(lockIntegrity).anyMatch(integrity -> integrity > 0))).m_61124_(ForgedContainerBlock.openProp, lidIntegrity <= 0) : (BlockState) ((BlockState) ((BlockState) ((BlockState) blockState.m_61124_(ForgedContainerBlock.locked1Prop, lockIntegrity[0] > 0)).m_61124_(ForgedContainerBlock.locked2Prop, lockIntegrity[1] > 0)).m_61124_(ForgedContainerBlock.anyLockedProp, Arrays.stream(lockIntegrity).anyMatch(integrity -> integrity > 0))).m_61124_(ForgedContainerBlock.openProp, lidIntegrity <= 0);
    }

    public static void writeLockData(CompoundTag compound, int[] lockIntegrity) {
        for (int i = 0; i < lockIntegrity.length; i++) {
            compound.putInt("lock_integrity" + i, lockIntegrity[i]);
        }
    }

    public static void writeLidData(CompoundTag compound, int lidIntegrity) {
        compound.putInt("lid_integrity", lidIntegrity);
    }

    public ForgedContainerBlockEntity getOrDelegate() {
        return this.f_58857_ != null && this.m_58900_().m_60734_() instanceof ForgedContainerBlock && this.isFlipped() ? (ForgedContainerBlockEntity) TileEntityOptional.from(this.f_58857_, this.f_58858_.relative(this.getFacing().getCounterClockWise()), ForgedContainerBlockEntity.class).orElse(null) : this;
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? this.getOrDelegate().handler.cast() : super.getCapability(cap, side);
    }

    public void open(@Nullable Player player) {
        if (this.lidIntegrity > 0) {
            this.lidIntegrity--;
            this.m_6596_();
            if (!this.f_58857_.isClientSide) {
                ServerLevel worldServer = (ServerLevel) this.f_58857_;
                if (this.lidIntegrity == 0) {
                    this.populateInventory(worldServer, (ServerPlayer) player);
                    this.causeOpeningEffects(worldServer);
                } else {
                    worldServer.m_5594_(null, this.f_58858_, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.PLAYERS, 0.5F, 1.3F);
                }
                Optional.ofNullable(player).filter(p -> !p.m_21023_(MobEffects.DAMAGE_BOOST)).ifPresent(p -> p.m_7292_(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 200, 5)));
            } else if (this.lidIntegrity == 0) {
                this.openTime = System.currentTimeMillis();
            }
            this.updateBlockState();
        }
    }

    private void populateInventory(ServerLevel serverWorld, @Nullable ServerPlayer player) {
        this.handler.ifPresent(handler -> {
            LootTable lootTable = serverWorld.getServer().getLootData().m_278676_(containerLootTable);
            LootParams.Builder builder = new LootParams.Builder(serverWorld).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.f_58858_));
            if (player != null) {
                CriteriaTriggers.GENERATE_LOOT.trigger(player, containerLootTable);
                builder = builder.withParameter(LootContextParams.THIS_ENTITY, player).withLuck(player.m_36336_());
            }
            lootTable.fill(new ItemHandlerWrapper(handler), builder.create(LootContextParamSets.CHEST), this.m_58900_().m_60726_(this.m_58899_()));
        });
    }

    private void causeOpeningEffects(ServerLevel worldServer) {
        Direction facing = (Direction) worldServer.m_8055_(this.f_58858_).m_61143_(HorizontalDirectionalBlock.FACING);
        Vec3 smokeDirection = Vec3.atLowerCornerOf(facing.getClockWise().getNormal());
        Random random = new Random();
        int smokeCount = 5 + random.nextInt(4);
        BlockPos smokeOrigin = this.f_58858_;
        if (Direction.SOUTH.equals(facing)) {
            smokeOrigin = smokeOrigin.offset(1, 0, 0);
        } else if (Direction.WEST.equals(facing)) {
            smokeOrigin = smokeOrigin.offset(1, 0, 1);
        } else if (Direction.NORTH.equals(facing)) {
            smokeOrigin = smokeOrigin.offset(0, 0, 1);
        }
        for (int i = 0; i < smokeCount; i++) {
            worldServer.sendParticles(ParticleTypes.SMOKE, (double) smokeOrigin.m_123341_() + smokeDirection.x * (double) i * 2.0 / (double) (smokeCount - 1), (double) smokeOrigin.m_123342_() + 0.8, (double) smokeOrigin.m_123343_() + smokeDirection.z * (double) i * 2.0 / (double) (smokeCount - 1), 1, 0.0, 0.0, 0.0, 0.0);
        }
        worldServer.m_5594_(null, this.f_58858_, SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.PLAYERS, 1.0F, 0.5F);
        worldServer.m_5594_(null, this.f_58858_, SoundEvents.LAVA_EXTINGUISH, SoundSource.PLAYERS, 0.2F, 0.8F);
    }

    private void updateBlockState() {
        this.f_58857_.setBlock(this.f_58858_, getUpdatedBlockState(this.m_58900_(), this.lockIntegrity, this.lidIntegrity), 3);
        BlockPos offsetPos = this.f_58858_.relative(this.getFacing().getClockWise());
        this.f_58857_.setBlock(offsetPos, getUpdatedBlockState(this.f_58857_.getBlockState(offsetPos), this.lockIntegrity, this.lidIntegrity), 3);
    }

    public Direction getFacing() {
        return (Direction) this.m_58900_().m_61143_(ForgedContainerBlock.facingProp);
    }

    public boolean isFlipped() {
        return (Boolean) this.m_58900_().m_61143_(ForgedContainerBlock.flippedProp);
    }

    public boolean isOpen() {
        return this.lidIntegrity <= 0;
    }

    public boolean isLocked(int index) {
        return this.lockIntegrity[index] > 0;
    }

    public Boolean[] isLocked() {
        return (Boolean[]) Arrays.stream(this.lockIntegrity).mapToObj(integrity -> integrity > 0).toArray(Boolean[]::new);
    }

    public void breakLock(@Nullable Player player, int index, @Nullable InteractionHand hand) {
        if (this.lockIntegrity[index] > 0) {
            this.lockIntegrity[index]--;
            if (this.lockIntegrity[index] == 0) {
                this.f_58857_.playSound(player, this.f_58858_, SoundEvents.SHIELD_BREAK, SoundSource.PLAYERS, 1.0F, 0.5F);
            } else {
                this.f_58857_.playSound(player, this.f_58858_, SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.PLAYERS, 1.0F, 0.5F);
            }
            if (!this.f_58857_.isClientSide && this.lockIntegrity[index] == 0) {
                if (player != null) {
                    BlockInteraction.dropLoot(lockLootTable, player, hand, (ServerLevel) this.f_58857_, this.m_58900_());
                } else {
                    BlockInteraction.dropLoot(lockLootTable, (ServerLevel) this.f_58857_, this.m_58899_(), this.m_58900_());
                }
            }
        }
        this.updateBlockState();
        this.m_6596_();
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("forged_container");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
        return new ForgedContainerMenu(windowId, this, playerInventory, playerEntity);
    }

    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.handler.ifPresent(handler -> handler.deserializeNBT(compound.getCompound("inv")));
        for (int i = 0; i < this.lockIntegrity.length; i++) {
            this.lockIntegrity[i] = compound.getInt("lock_integrity" + i);
        }
        this.lidIntegrity = compound.getInt("lid_integrity");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        this.handler.ifPresent(handler -> compound.put("inv", handler.serializeNBT()));
        writeLockData(compound, this.lockIntegrity);
        writeLidData(compound, this.lidIntegrity);
    }
}