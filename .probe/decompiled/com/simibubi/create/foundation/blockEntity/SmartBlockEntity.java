package com.simibubi.create.foundation.blockEntity;

import com.simibubi.create.api.event.BlockEntityBehaviourEvent;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.IInteractionChecker;
import com.simibubi.create.foundation.utility.IPartialSafeNBT;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public abstract class SmartBlockEntity extends CachedRenderBBBlockEntity implements IPartialSafeNBT, IInteractionChecker, ISpecialBlockEntityItemRequirement {

    private final Map<BehaviourType<?>, BlockEntityBehaviour> behaviours = new HashMap();

    private boolean initialized = false;

    private boolean firstNbtRead = true;

    protected int lazyTickRate;

    protected int lazyTickCounter;

    private boolean chunkUnloaded;

    private boolean virtualMode;

    public SmartBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.setLazyTickRate(10);
        ArrayList<BlockEntityBehaviour> list = new ArrayList();
        this.addBehaviours(list);
        list.forEach(b -> this.behaviours.put(b.getType(), b));
    }

    public abstract void addBehaviours(List<BlockEntityBehaviour> var1);

    public void addBehavioursDeferred(List<BlockEntityBehaviour> behaviours) {
    }

    public void initialize() {
        if (this.firstNbtRead) {
            this.firstNbtRead = false;
            MinecraftForge.EVENT_BUS.post(new BlockEntityBehaviourEvent<SmartBlockEntity>(this, this.behaviours));
        }
        this.forEachBehaviour(BlockEntityBehaviour::initialize);
        this.lazyTick();
    }

    public void tick() {
        if (!this.initialized && this.m_58898_()) {
            this.initialize();
            this.initialized = true;
        }
        if (this.lazyTickCounter-- <= 0) {
            this.lazyTickCounter = this.lazyTickRate;
            this.lazyTick();
        }
        this.forEachBehaviour(BlockEntityBehaviour::tick);
    }

    public void lazyTick() {
    }

    protected void write(CompoundTag tag, boolean clientPacket) {
        super.m_183515_(tag);
        this.forEachBehaviour(tb -> tb.write(tag, clientPacket));
    }

    @Override
    public void writeSafe(CompoundTag tag) {
        super.m_183515_(tag);
        this.forEachBehaviour(tb -> {
            if (tb.isSafeNBT()) {
                tb.write(tag, false);
            }
        });
    }

    protected void read(CompoundTag tag, boolean clientPacket) {
        if (this.firstNbtRead) {
            this.firstNbtRead = false;
            ArrayList<BlockEntityBehaviour> list = new ArrayList();
            this.addBehavioursDeferred(list);
            list.forEach(b -> this.behaviours.put(b.getType(), b));
            MinecraftForge.EVENT_BUS.post(new BlockEntityBehaviourEvent<SmartBlockEntity>(this, this.behaviours));
        }
        super.m_142466_(tag);
        this.forEachBehaviour(tb -> tb.read(tag, clientPacket));
    }

    @Override
    public final void load(CompoundTag tag) {
        this.read(tag, false);
    }

    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        this.chunkUnloaded = true;
    }

    @Override
    public final void setRemoved() {
        super.m_7651_();
        if (!this.chunkUnloaded) {
            this.remove();
        }
        this.invalidate();
    }

    public void invalidate() {
        this.forEachBehaviour(BlockEntityBehaviour::unload);
    }

    public void remove() {
    }

    public void destroy() {
        this.forEachBehaviour(BlockEntityBehaviour::destroy);
    }

    @Override
    public final void saveAdditional(CompoundTag tag) {
        this.write(tag, false);
    }

    @Override
    public final void readClient(CompoundTag tag) {
        this.read(tag, true);
    }

    @Override
    public final CompoundTag writeClient(CompoundTag tag) {
        this.write(tag, true);
        return tag;
    }

    public <T extends BlockEntityBehaviour> T getBehaviour(BehaviourType<T> type) {
        return (T) this.behaviours.get(type);
    }

    public void forEachBehaviour(Consumer<BlockEntityBehaviour> action) {
        this.getAllBehaviours().forEach(action);
    }

    public Collection<BlockEntityBehaviour> getAllBehaviours() {
        return this.behaviours.values();
    }

    protected void attachBehaviourLate(BlockEntityBehaviour behaviour) {
        this.behaviours.put(behaviour.getType(), behaviour);
        behaviour.initialize();
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        return (ItemRequirement) this.getAllBehaviours().stream().reduce(ItemRequirement.NONE, (r, b) -> r.union(b.getRequiredItems()), (r, r1) -> r.union(r1));
    }

    protected void removeBehaviour(BehaviourType<?> type) {
        BlockEntityBehaviour remove = (BlockEntityBehaviour) this.behaviours.remove(type);
        if (remove != null) {
            remove.unload();
        }
    }

    public void setLazyTickRate(int slowTickRate) {
        this.lazyTickRate = slowTickRate;
        this.lazyTickCounter = slowTickRate;
    }

    public void markVirtual() {
        this.virtualMode = true;
    }

    public boolean isVirtual() {
        return this.virtualMode;
    }

    public boolean isChunkUnloaded() {
        return this.chunkUnloaded;
    }

    @Override
    public boolean canPlayerUse(Player player) {
        return this.f_58857_ != null && this.f_58857_.getBlockEntity(this.f_58858_) == this ? player.m_20275_((double) this.f_58858_.m_123341_() + 0.5, (double) this.f_58858_.m_123342_() + 0.5, (double) this.f_58858_.m_123343_() + 0.5) <= 64.0 : false;
    }

    public void sendToMenu(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.m_58899_());
        buffer.writeNbt(this.m_5995_());
    }

    public void refreshBlockState() {
        this.m_155250_(this.m_58904_().getBlockState(this.m_58899_()));
    }

    protected boolean isItemHandlerCap(Capability<?> cap) {
        return cap == ForgeCapabilities.ITEM_HANDLER;
    }

    protected boolean isFluidHandlerCap(Capability<?> cap) {
        return cap == ForgeCapabilities.FLUID_HANDLER;
    }

    public void registerAwardables(List<BlockEntityBehaviour> behaviours, CreateAdvancement... advancements) {
        for (BlockEntityBehaviour behaviour : behaviours) {
            if (behaviour instanceof AdvancementBehaviour ab) {
                ab.add(advancements);
                return;
            }
        }
        behaviours.add(new AdvancementBehaviour(this, advancements));
    }

    public void award(CreateAdvancement advancement) {
        AdvancementBehaviour behaviour = this.getBehaviour(AdvancementBehaviour.TYPE);
        if (behaviour != null) {
            behaviour.awardPlayer(advancement);
        }
    }

    public void awardIfNear(CreateAdvancement advancement, int range) {
        AdvancementBehaviour behaviour = this.getBehaviour(AdvancementBehaviour.TYPE);
        if (behaviour != null) {
            behaviour.awardPlayerIfNear(advancement, range);
        }
    }
}