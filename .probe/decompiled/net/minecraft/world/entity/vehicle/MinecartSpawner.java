package net.minecraft.world.entity.vehicle;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MinecartSpawner extends AbstractMinecart {

    private final BaseSpawner spawner = new BaseSpawner() {

        @Override
        public void broadcastEvent(Level p_150342_, BlockPos p_150343_, int p_150344_) {
            p_150342_.broadcastEntityEvent(MinecartSpawner.this, (byte) p_150344_);
        }
    };

    private final Runnable ticker;

    public MinecartSpawner(EntityType<? extends MinecartSpawner> entityTypeExtendsMinecartSpawner0, Level level1) {
        super(entityTypeExtendsMinecartSpawner0, level1);
        this.ticker = this.createTicker(level1);
    }

    public MinecartSpawner(Level level0, double double1, double double2, double double3) {
        super(EntityType.SPAWNER_MINECART, level0, double1, double2, double3);
        this.ticker = this.createTicker(level0);
    }

    @Override
    protected Item getDropItem() {
        return Items.MINECART;
    }

    private Runnable createTicker(Level level0) {
        return level0 instanceof ServerLevel ? () -> this.spawner.serverTick((ServerLevel) level0, this.m_20183_()) : () -> this.spawner.clientTick(level0, this.m_20183_());
    }

    @Override
    public AbstractMinecart.Type getMinecartType() {
        return AbstractMinecart.Type.SPAWNER;
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return Blocks.SPAWNER.defaultBlockState();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.spawner.load(this.m_9236_(), this.m_20183_(), compoundTag0);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        this.spawner.save(compoundTag0);
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        this.spawner.onEventTriggered(this.m_9236_(), byte0);
    }

    @Override
    public void tick() {
        super.tick();
        this.ticker.run();
    }

    public BaseSpawner getSpawner() {
        return this.spawner;
    }

    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }
}