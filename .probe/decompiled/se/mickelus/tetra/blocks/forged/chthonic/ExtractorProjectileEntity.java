package se.mickelus.tetra.blocks.forged.chthonic;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.mutil.util.RotationHelper;
import se.mickelus.tetra.ServerScheduler;

@ParametersAreNonnullByDefault
public class ExtractorProjectileEntity extends AbstractArrow implements IEntityAdditionalSpawnData {

    public static final String unlocalizedName = "extractor_projectile";

    public static final String damageKey = "dmg";

    public static final String heatKey = "heat";

    @ObjectHolder(registryName = "entity_type", value = "tetra:extractor_projectile")
    public static EntityType<ExtractorProjectileEntity> type;

    private int damage;

    private int heat;

    private boolean extinguishing = false;

    public ExtractorProjectileEntity(Level world, LivingEntity shooter, ItemStack itemStack) {
        super(type, shooter, world);
        this.damage = itemStack.getDamageValue();
        this.m_36740_(SoundEvents.NETHERITE_BLOCK_HIT);
        this.m_36781_(0.5);
        this.m_36735_(3);
        this.m_36767_((byte) 127);
    }

    public ExtractorProjectileEntity(EntityType<? extends ExtractorProjectileEntity> type, Level worldIn) {
        super(type, worldIn);
        this.m_36781_(0.5);
        this.m_36735_(3);
        this.m_36767_((byte) 127);
    }

    @OnlyIn(Dist.CLIENT)
    public ExtractorProjectileEntity(Level worldIn, double x, double y, double z) {
        super(type, x, y, z, worldIn);
        this.m_36781_(0.5);
        this.m_36735_(3);
        this.m_36767_((byte) 127);
    }

    public ExtractorProjectileEntity(PlayMessages.SpawnEntity packet, Level worldIn) {
        super(type, worldIn);
        this.m_36781_(0.5);
        this.m_36735_(3);
        this.m_36767_((byte) 127);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    protected void onHit(HitResult rayTraceResult) {
        if (!this.m_9236_().isClientSide && rayTraceResult.getType() == HitResult.Type.BLOCK && this.m_20184_().lengthSqr() > 0.95) {
            ServerPlayer shooter = (ServerPlayer) CastOptional.cast(this.m_19749_(), ServerPlayer.class).orElse(null);
            BlockPos pos = ((BlockHitResult) rayTraceResult).getBlockPos();
            if (shooter != null && this.breakBlock(this.m_9236_(), pos, shooter)) {
                this.breakAround(this.m_9236_(), pos, ((BlockHitResult) rayTraceResult).getDirection(), shooter);
                this.m_20256_(this.m_20184_().scale(0.95F));
                this.hitAdditional();
                return;
            }
        }
        super.m_6532_(rayTraceResult);
    }

    private void hitAdditional() {
        Vec3 position = this.m_20182_();
        Vec3 target = position.add(this.m_20184_());
        HitResult rayTraceResult = this.m_9236_().m_45547_(new ClipContext(position, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (rayTraceResult.getType() == HitResult.Type.BLOCK && !ForgeEventFactory.onProjectileImpact(this, rayTraceResult)) {
            this.onHit(rayTraceResult);
        }
    }

    private void breakAround(Level world, BlockPos pos, Direction face, ServerPlayer shooter) {
        Vec3i axis1 = RotationHelper.shiftAxis(face.getNormal());
        Vec3i axis2 = RotationHelper.shiftAxis(axis1);
        ServerScheduler.schedule(2, () -> {
            this.breakBlock(world, pos.offset(axis1), shooter);
            this.breakBlock(world, pos.subtract(axis1), shooter);
        });
        ServerScheduler.schedule(4, () -> {
            this.breakBlock(world, pos.offset(axis2), shooter);
            this.breakBlock(world, pos.subtract(axis2), shooter);
        });
        ServerScheduler.schedule(6, () -> {
            this.breakBlock(world, pos.offset(axis1).offset(axis2), shooter);
            this.breakBlock(world, pos.subtract(axis1).subtract(axis2), shooter);
        });
        ServerScheduler.schedule(8, () -> {
            this.breakBlock(world, pos.offset(axis1).subtract(axis2), shooter);
            this.breakBlock(world, pos.subtract(axis1).offset(axis2), shooter);
        });
    }

    private boolean breakBlock(Level world, BlockPos pos, ServerPlayer shooter) {
        ServerLevel serverWorld = (ServerLevel) world;
        GameType gameType = shooter.gameMode.getGameModeForPlayer();
        BlockState blockState = world.getBlockState(pos);
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (blockState.m_60800_(world, pos) != -1.0F && this.m_6084_() && !shooter.m_36187_(world, pos, gameType) && blockState.m_204336_(FracturedBedrockTile.extractorBreakable) && blockState.m_60734_().onDestroyedByPlayer(blockState, world, pos, shooter, true, world.getFluidState(pos)) && ForgeHooks.onBlockBreakEvent(world, gameType, shooter, pos) != -1) {
            blockState.m_60734_().playerDestroy(world, shooter, pos, blockState, tileEntity, ItemStack.EMPTY);
            blockState.m_60734_().destroy(world, pos, blockState);
            world.m_5898_(null, 2001, pos, Block.getId(blockState));
            this.damage++;
            this.heat += 10;
            int exp = blockState.getExpDrop(world, world.getRandom(), pos, 0, 0);
            if (exp > 0) {
                blockState.m_60734_().popExperience(serverWorld, pos, exp);
            }
            if (this.damage > 1024) {
                this.destroyExtractor();
            }
            return true;
        } else {
            return false;
        }
    }

    private void destroyExtractor() {
        this.m_146870_();
        this.m_9236_().explode(this.m_19749_(), this.m_20185_(), this.m_20186_(), this.m_20189_(), 4.0F, true, Level.ExplosionInteraction.TNT);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.m_9236_().isClientSide) {
            if (this.onGround() && this.heat > 0) {
                int cooldown = this.m_20069_() ? 10 : 1;
                if (this.f_19797_ % 10 == 0) {
                    Vec3 pos = this.m_20182_().add(this.m_20154_().scale(-Math.random()));
                    ((ServerLevel) this.m_9236_()).sendParticles(ParticleTypes.LARGE_SMOKE, pos.x, pos.y, pos.z, cooldown, 0.0, 0.01, 0.0, 0.01);
                    ((ServerLevel) this.m_9236_()).sendParticles(ParticleTypes.FLAME, pos.x, pos.y + 0.1, pos.z, 1, 0.0, 0.01, 0.0, 0.01);
                }
                if (cooldown > 1 && !this.extinguishing) {
                    this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 0.2F, 0.9F);
                    ((ServerLevel) this.m_9236_()).sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, this.m_20185_(), this.m_20186_(), this.m_20189_(), 12, 0.0, 0.01, 0.0, 0.01);
                    this.extinguishing = true;
                }
                this.heat -= cooldown;
            } else if (this.f_19797_ % 40 == 0) {
                Vec3 pos = this.m_20182_().add(this.m_20154_().scale(-Math.random()));
                ((ServerLevel) this.m_9236_()).sendParticles(ParticleTypes.LARGE_SMOKE, pos.x, pos.y, pos.z, 1, 0.0, 0.01, 0.0, 0.01);
            }
        }
    }

    @Override
    public boolean onGround() {
        return this.f_36704_ > 0;
    }

    @Override
    protected ItemStack getPickupItem() {
        if (this.damage == 0) {
            return new ItemStack(ChthonicExtractorBlock.item);
        } else {
            ItemStack itemStack = new ItemStack(ChthonicExtractorBlock.usedItem);
            itemStack.setDamageValue(this.damage);
            return itemStack;
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult rayTraceResult) {
        super.onHitBlock(rayTraceResult);
        this.m_36740_(SoundEvents.NETHERITE_BLOCK_HIT);
    }

    @Override
    protected void onHitEntity(EntityHitResult rayTraceResult) {
        super.onHitEntity(rayTraceResult);
        this.m_20256_(this.m_20184_().normalize().scale(-0.1));
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return !type.equals(entity.getType()) && super.canHitEntity(entity);
    }

    @Override
    public void playerTouch(Player player) {
        if (this.f_36703_) {
            super.playerTouch(player);
            if (!this.m_6084_()) {
                this.ignitePlayer(player);
            }
        }
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        if (!this.m_9236_().isClientSide && this.onGround() && this.m_6084_() && this.f_36705_ == AbstractArrow.Pickup.ALLOWED) {
            ItemStack itemStack = this.getPickupItem();
            boolean success = false;
            if (player.m_21205_().isEmpty()) {
                player.m_21008_(InteractionHand.MAIN_HAND, itemStack);
                success = true;
            } else if (player.m_21206_().isEmpty()) {
                player.m_21008_(InteractionHand.OFF_HAND, itemStack);
                success = true;
            } else if (player.getInventory().add(itemStack)) {
                success = true;
            }
            if (success) {
                player.m_7938_(this, 1);
                this.ignitePlayer(player);
                this.m_146870_();
                return InteractionResult.SUCCESS;
            }
        }
        return super.m_7111_(player, vec, hand);
    }

    private void ignitePlayer(Player player) {
        if (!this.m_6084_() && this.heat > 10) {
            player.m_20254_(3 + this.heat / 20);
        }
    }

    @Override
    public void tickDespawn() {
        if (this.f_36705_ != AbstractArrow.Pickup.ALLOWED) {
            super.tickDespawn();
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.damage = compound.getInt("dmg");
        this.heat = compound.getInt("heat");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("dmg", this.damage);
        compound.putInt("heat", this.heat);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(this.damage);
        buffer.writeInt(this.heat);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        this.damage = buffer.readInt();
        this.heat = buffer.readInt();
    }
}