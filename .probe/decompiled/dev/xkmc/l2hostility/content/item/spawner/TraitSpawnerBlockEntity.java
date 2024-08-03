package dev.xkmc.l2hostility.content.item.spawner;

import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

@SerialClass
public abstract class TraitSpawnerBlockEntity extends BaseBlockEntity implements TickableBlockEntity {

    @SerialField
    public final TraitSpawnerData data = new TraitSpawnerData();

    @Nullable
    protected CustomBossEvent event;

    public TraitSpawnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void tick() {
        if (this.f_58857_ != null) {
            if (!this.f_58857_.isClientSide()) {
                if (this.m_58900_().m_61143_(TraitSpawnerBlock.STATE) == TraitSpawnerBlock.State.ACTIVATED) {
                    this.data.init(this.f_58857_);
                    TraitSpawnerBlock.State next = this.data.tick();
                    if (next == TraitSpawnerBlock.State.FAILED) {
                        this.stop();
                        this.f_58857_.setBlockAndUpdate(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(TraitSpawnerBlock.STATE, TraitSpawnerBlock.State.FAILED));
                        return;
                    }
                    if (next == TraitSpawnerBlock.State.CLEAR) {
                        this.clearStage();
                        this.stop();
                        this.f_58857_.setBlockAndUpdate(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(TraitSpawnerBlock.STATE, TraitSpawnerBlock.State.CLEAR));
                        return;
                    }
                    if (this.event == null) {
                        this.event = this.createBossEvent();
                    }
                    int max = this.data.getMax();
                    int alive = this.data.getAlive();
                    this.event.setMax(max);
                    this.event.setValue(alive);
                    this.event.m_6456_(LangData.BOSS_EVENT.get(max - alive, max).withStyle(ChatFormatting.GOLD));
                    Set<ServerPlayer> set = new HashSet();
                    for (ServerPlayer e : this.event.m_8324_()) {
                        if (e.m_20238_(Vec3.atCenterOf(this.m_58899_())) > 1024.0) {
                            set.add(e);
                        }
                    }
                    for (ServerPlayer ex : set) {
                        this.event.removePlayer(ex);
                    }
                }
            }
        }
    }

    public void activate() {
        if (this.f_58857_ != null && !this.f_58857_.isClientSide()) {
            this.data.stop();
            if (this.event != null) {
                this.event.removeAllPlayers();
            }
            this.event = this.createBossEvent();
            this.generate(this.data);
            this.f_58857_.setBlockAndUpdate(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(TraitSpawnerBlock.STATE, TraitSpawnerBlock.State.ACTIVATED));
        }
    }

    @Override
    public void setRemoved() {
        if (this.event != null) {
            this.event.removeAllPlayers();
        }
        super.m_7651_();
    }

    public void stop() {
        if (this.event != null) {
            this.event.removeAllPlayers();
            this.event = null;
        }
        this.data.stop();
    }

    public void deactivate() {
        if (this.f_58857_ != null && !this.f_58857_.isClientSide()) {
            this.stop();
            this.f_58857_.setBlockAndUpdate(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(TraitSpawnerBlock.STATE, TraitSpawnerBlock.State.IDLE));
        }
    }

    protected abstract void generate(TraitSpawnerData var1);

    protected abstract void clearStage();

    protected abstract CustomBossEvent createBossEvent();

    public void track(Player player) {
        if (this.event != null && player instanceof ServerPlayer sp) {
            this.event.addPlayer(sp);
        }
    }
}