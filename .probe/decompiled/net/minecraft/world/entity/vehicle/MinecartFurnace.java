package net.minecraft.world.entity.vehicle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class MinecartFurnace extends AbstractMinecart {

    private static final EntityDataAccessor<Boolean> DATA_ID_FUEL = SynchedEntityData.defineId(MinecartFurnace.class, EntityDataSerializers.BOOLEAN);

    private int fuel;

    public double xPush;

    public double zPush;

    private static final Ingredient INGREDIENT = Ingredient.of(Items.COAL, Items.CHARCOAL);

    public MinecartFurnace(EntityType<? extends MinecartFurnace> entityTypeExtendsMinecartFurnace0, Level level1) {
        super(entityTypeExtendsMinecartFurnace0, level1);
    }

    public MinecartFurnace(Level level0, double double1, double double2, double double3) {
        super(EntityType.FURNACE_MINECART, level0, double1, double2, double3);
    }

    @Override
    public AbstractMinecart.Type getMinecartType() {
        return AbstractMinecart.Type.FURNACE;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DATA_ID_FUEL, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.m_9236_().isClientSide()) {
            if (this.fuel > 0) {
                this.fuel--;
            }
            if (this.fuel <= 0) {
                this.xPush = 0.0;
                this.zPush = 0.0;
            }
            this.setHasFuel(this.fuel > 0);
        }
        if (this.hasFuel() && this.f_19796_.nextInt(4) == 0) {
            this.m_9236_().addParticle(ParticleTypes.LARGE_SMOKE, this.m_20185_(), this.m_20186_() + 0.8, this.m_20189_(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected double getMaxSpeed() {
        return (this.m_20069_() ? 3.0 : 4.0) / 20.0;
    }

    @Override
    protected Item getDropItem() {
        return Items.FURNACE_MINECART;
    }

    @Override
    protected void moveAlongTrack(BlockPos blockPos0, BlockState blockState1) {
        double $$2 = 1.0E-4;
        double $$3 = 0.001;
        super.moveAlongTrack(blockPos0, blockState1);
        Vec3 $$4 = this.m_20184_();
        double $$5 = $$4.horizontalDistanceSqr();
        double $$6 = this.xPush * this.xPush + this.zPush * this.zPush;
        if ($$6 > 1.0E-4 && $$5 > 0.001) {
            double $$7 = Math.sqrt($$5);
            double $$8 = Math.sqrt($$6);
            this.xPush = $$4.x / $$7 * $$8;
            this.zPush = $$4.z / $$7 * $$8;
        }
    }

    @Override
    protected void applyNaturalSlowdown() {
        double $$0 = this.xPush * this.xPush + this.zPush * this.zPush;
        if ($$0 > 1.0E-7) {
            $$0 = Math.sqrt($$0);
            this.xPush /= $$0;
            this.zPush /= $$0;
            Vec3 $$1 = this.m_20184_().multiply(0.8, 0.0, 0.8).add(this.xPush, 0.0, this.zPush);
            if (this.m_20069_()) {
                $$1 = $$1.scale(0.1);
            }
            this.m_20256_($$1);
        } else {
            this.m_20256_(this.m_20184_().multiply(0.98, 0.0, 0.98));
        }
        super.applyNaturalSlowdown();
    }

    @Override
    public InteractionResult interact(Player player0, InteractionHand interactionHand1) {
        ItemStack $$2 = player0.m_21120_(interactionHand1);
        if (INGREDIENT.test($$2) && this.fuel + 3600 <= 32000) {
            if (!player0.getAbilities().instabuild) {
                $$2.shrink(1);
            }
            this.fuel += 3600;
        }
        if (this.fuel > 0) {
            this.xPush = this.m_20185_() - player0.m_20185_();
            this.zPush = this.m_20189_() - player0.m_20189_();
        }
        return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putDouble("PushX", this.xPush);
        compoundTag0.putDouble("PushZ", this.zPush);
        compoundTag0.putShort("Fuel", (short) this.fuel);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.xPush = compoundTag0.getDouble("PushX");
        this.zPush = compoundTag0.getDouble("PushZ");
        this.fuel = compoundTag0.getShort("Fuel");
    }

    protected boolean hasFuel() {
        return this.f_19804_.get(DATA_ID_FUEL);
    }

    protected void setHasFuel(boolean boolean0) {
        this.f_19804_.set(DATA_ID_FUEL, boolean0);
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return (BlockState) ((BlockState) Blocks.FURNACE.defaultBlockState().m_61124_(FurnaceBlock.f_48683_, Direction.NORTH)).m_61124_(FurnaceBlock.f_48684_, this.hasFuel());
    }
}