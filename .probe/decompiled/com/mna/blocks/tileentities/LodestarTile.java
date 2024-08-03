package com.mna.blocks.tileentities;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.ConstructTask;
import com.mna.api.entities.construct.ai.IMutexManager;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.gui.containers.block.ContainerLodestar;
import com.mna.network.ClientMessageDispatcher;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.IForgeRegistry;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class LodestarTile extends BlockEntity implements Consumer<FriendlyByteBuf>, MenuProvider, GeoBlockEntity, IMutexManager {

    private CompoundTag logic;

    private ArrayList<ConstructAITask<?>> inflatedTasks;

    private List<Component> errors;

    private List<Component> warnings;

    private List<String> misconfiguredIDs;

    private AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    private HashMap<Class<?>, HashMap<IConstruct<?>, Long>> blockPositionMutex;

    private HashMap<Class<?>, HashMap<IConstruct<?>, Integer>> entityIDMutex;

    public LodestarTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.LODESTAR.get(), pos, state);
        this.logic = new CompoundTag();
        this.inflatedTasks = new ArrayList();
        this.errors = new ArrayList();
        this.warnings = new ArrayList();
        this.misconfiguredIDs = new ArrayList();
        this.blockPositionMutex = new HashMap();
        this.entityIDMutex = new HashMap();
    }

    public Optional<ConstructAITask<?>> getCommand(String ID) {
        return ID == null ? this.inflatedTasks.stream().filter(t -> t.isStart()).findFirst() : this.inflatedTasks.stream().filter(t -> t.getId().equals(ID)).findFirst();
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        compound.put("logic", this.logic);
    }

    @Override
    public void load(CompoundTag nbt) {
        if (nbt.contains("logic")) {
            this.logic = nbt.getCompound("logic");
            this.inflateLogic();
        }
        super.load(nbt);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187480_();
    }

    public void accept(FriendlyByteBuf t) {
        t.writeBlockPos(this.m_58899_());
        CompoundTag compound = this.m_187480_();
        t.writeNbt(compound);
    }

    public LodestarTile readFrom(FriendlyByteBuf data) {
        CompoundTag compound = data.readNbt();
        this.load(compound);
        return this;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new ContainerLodestar(id, playerInventory, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("");
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, state -> this.m_58904_().m_276867_(this.m_58899_()) ? state.setAndContinue(RawAnimation.begin().thenLoop("animation.lodestar_armature.inactive")) : state.setAndContinue(RawAnimation.begin().thenLoop("animation.lodestar_armature.active"))).transitionLength(20));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animCache;
    }

    public void setLogic(CompoundTag logic, boolean sync) {
        this.logic = logic;
        this.inflateLogic();
        if (this.m_58904_().isClientSide() && sync) {
            ClientMessageDispatcher.sendLodestarLogicMessage(this.m_58899_(), logic);
        } else {
            this.m_6596_();
        }
    }

    public CompoundTag getLogic() {
        return this.logic;
    }

    private void inflateLogic() {
        this.inflatedTasks.clear();
        if (this.logic.contains("commands")) {
            ListTag data = this.logic.getList("commands", 10);
            data.forEach(tag -> this.inflateLogicNode((CompoundTag) tag));
            this.analyzeLogic();
        }
    }

    private void inflateLogicNode(CompoundTag tag) {
        ResourceLocation taskID = new ResourceLocation(tag.getString("task"));
        ConstructTask action = (ConstructTask) ((IForgeRegistry) Registries.ConstructTasks.get()).getValue(taskID);
        if (action == null) {
            ManaAndArtifice.LOGGER.error("Failed to look up task (" + taskID.toString() + "); Node skipped.  This will likely break connections.");
        } else {
            if (tag.contains("id") && tag.contains("start")) {
                ConstructAITask<?> inst = action.instantiateTask(null);
                if (tag.contains("parameters", 9)) {
                    inst.loadParameterData(tag.getList("parameters", 10));
                }
                if (tag.contains("connections", 9)) {
                    inst.loadConnections(tag.getList("connections", 10));
                }
                inst.setIdAndIsStart(tag.getString("id"), tag.getBoolean("start"));
                inst.inflateParameters();
                this.inflatedTasks.add(inst);
            } else {
                ManaAndArtifice.LOGGER.error("Task (" + taskID.toString() + ") did not have the required parameters id and start from the Lodestar.  Skipping.");
            }
        }
    }

    private void analyzeLogic() {
        this.errors.clear();
        this.warnings.clear();
        this.misconfiguredIDs = this.inflatedTasks.stream().filter(task -> !task.isFullyConfigured()).map(task -> task.getId()).toList();
        boolean hasNoStart = this.inflatedTasks.stream().noneMatch(task -> task.isStart());
        boolean hasMisconfigured = this.misconfiguredIDs.size() > 0;
        List<String> visitedIDs = (List<String>) this.inflatedTasks.stream().map(task -> task.getConnectedIDs()).flatMap(Collection::stream).collect(Collectors.toList());
        boolean hasDisconnected = this.inflatedTasks.stream().anyMatch(task -> !visitedIDs.contains(task.getId()) && !task.isStart());
        if (hasNoStart && this.inflatedTasks.size() > 0) {
            this.errors.add(Component.translatable("gui.mna.lodestar.error.no_start"));
        }
        if (hasMisconfigured) {
            this.warnings.add(Component.translatable("gui.mna.lodestar.warning.misconfigured"));
        }
        if (hasDisconnected && this.inflatedTasks.size() > 0) {
            this.warnings.add(Component.translatable("gui.mna.lodestar.warning.unreachable"));
        }
    }

    public List<Component> getErrors() {
        return this.errors;
    }

    public List<Component> getWarnings() {
        return this.warnings;
    }

    public List<String> getMisconfiguredNodeIDs() {
        return this.misconfiguredIDs;
    }

    @Override
    public boolean claimMutex(BlockPos pos, IConstruct<?> construct, ConstructAITask<?> task) {
        long posLong = pos.asLong();
        HashMap<IConstruct<?>, Long> mutex = (HashMap<IConstruct<?>, Long>) this.blockPositionMutex.computeIfAbsent(task.getClass(), k -> new HashMap());
        if (mutex.containsValue(posLong)) {
            return mutex.containsKey(construct) && (Long) mutex.get(construct) == posLong;
        } else {
            mutex.put(construct, posLong);
            return true;
        }
    }

    @Override
    public void releaseMutex(BlockPos pos, IConstruct<?> construct, ConstructAITask<?> task) {
        HashMap<IConstruct<?>, Long> mutex = (HashMap<IConstruct<?>, Long>) this.blockPositionMutex.computeIfAbsent(task.getClass(), k -> new HashMap());
        mutex.remove(construct);
    }

    @Override
    public boolean claimMutex(Entity entity, IConstruct<?> construct, ConstructAITask<?> task) {
        if (entity == null) {
            return false;
        } else {
            int entityID = entity.getId();
            HashMap<IConstruct<?>, Integer> mutex = (HashMap<IConstruct<?>, Integer>) this.entityIDMutex.computeIfAbsent(task.getClass(), k -> new HashMap());
            if (mutex.containsValue(entityID)) {
                return mutex.containsKey(construct) && (Integer) mutex.get(construct) == entityID;
            } else {
                mutex.put(construct, entityID);
                return true;
            }
        }
    }

    @Override
    public void releaseMutex(Entity entity, IConstruct<?> construct, ConstructAITask<?> task) {
        HashMap<IConstruct<?>, Integer> mutex = (HashMap<IConstruct<?>, Integer>) this.entityIDMutex.computeIfAbsent(task.getClass(), k -> new HashMap());
        mutex.remove(construct);
    }

    @Override
    public void releaseAllMutexes(IConstruct<?> construct, ConstructAITask<?> task) {
        this.blockPositionMutex.computeIfPresent(task.getClass(), (k, v) -> {
            v.remove(construct);
            return v;
        });
        this.entityIDMutex.computeIfPresent(task.getClass(), (k, v) -> {
            v.remove(construct);
            return v;
        });
    }
}