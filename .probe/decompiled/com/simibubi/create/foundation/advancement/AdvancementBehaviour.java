package com.simibubi.create.foundation.advancement;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;

public class AdvancementBehaviour extends BlockEntityBehaviour {

    public static final BehaviourType<AdvancementBehaviour> TYPE = new BehaviourType<>();

    private UUID playerId;

    private Set<CreateAdvancement> advancements = new HashSet();

    public AdvancementBehaviour(SmartBlockEntity be, CreateAdvancement... advancements) {
        super(be);
        this.add(advancements);
    }

    public void add(CreateAdvancement... advancements) {
        for (CreateAdvancement advancement : advancements) {
            this.advancements.add(advancement);
        }
    }

    public boolean isOwnerPresent() {
        return this.playerId != null;
    }

    public void setPlayer(UUID id) {
        Player player = this.getWorld().m_46003_(id);
        if (player != null) {
            this.playerId = id;
            this.removeAwarded();
            this.blockEntity.m_6596_();
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        this.removeAwarded();
    }

    private void removeAwarded() {
        Player player = this.getPlayer();
        if (player != null) {
            this.advancements.removeIf(c -> c.isAlreadyAwardedTo(player));
            if (this.advancements.isEmpty()) {
                this.playerId = null;
                this.blockEntity.m_6596_();
            }
        }
    }

    public void awardPlayerIfNear(CreateAdvancement advancement, int maxDistance) {
        Player player = this.getPlayer();
        if (player != null) {
            if (!(player.m_20238_(Vec3.atCenterOf(this.getPos())) > (double) (maxDistance * maxDistance))) {
                this.award(advancement, player);
            }
        }
    }

    public void awardPlayer(CreateAdvancement advancement) {
        Player player = this.getPlayer();
        if (player != null) {
            this.award(advancement, player);
        }
    }

    private void award(CreateAdvancement advancement, Player player) {
        if (this.advancements.contains(advancement)) {
            advancement.awardTo(player);
        }
        this.removeAwarded();
    }

    private Player getPlayer() {
        return this.playerId == null ? null : this.getWorld().m_46003_(this.playerId);
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        if (this.playerId != null) {
            nbt.putUUID("Owner", this.playerId);
        }
    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        super.read(nbt, clientPacket);
        if (nbt.contains("Owner")) {
            this.playerId = nbt.getUUID("Owner");
        }
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    public static void tryAward(BlockGetter reader, BlockPos pos, CreateAdvancement advancement) {
        AdvancementBehaviour behaviour = BlockEntityBehaviour.get(reader, pos, TYPE);
        if (behaviour != null) {
            behaviour.awardPlayer(advancement);
        }
    }

    public static void setPlacedBy(Level worldIn, BlockPos pos, LivingEntity placer) {
        AdvancementBehaviour behaviour = BlockEntityBehaviour.get(worldIn, pos, TYPE);
        if (behaviour != null) {
            if (!(placer instanceof FakePlayer)) {
                if (placer instanceof ServerPlayer) {
                    behaviour.setPlayer(placer.m_20148_());
                }
            }
        }
    }
}