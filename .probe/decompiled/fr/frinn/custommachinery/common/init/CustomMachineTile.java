package fr.frinn.custommachinery.common.init;

import fr.frinn.custommachinery.CustomMachinery;
import fr.frinn.custommachinery.api.component.IMachineComponent;
import fr.frinn.custommachinery.api.crafting.ComponentNotFoundException;
import fr.frinn.custommachinery.api.crafting.IProcessor;
import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.api.machine.IMachineAppearance;
import fr.frinn.custommachinery.api.machine.MachineAppearanceProperty;
import fr.frinn.custommachinery.api.machine.MachineStatus;
import fr.frinn.custommachinery.api.machine.MachineTile;
import fr.frinn.custommachinery.api.network.ISyncable;
import fr.frinn.custommachinery.api.network.ISyncableStuff;
import fr.frinn.custommachinery.common.component.DummyComponentManager;
import fr.frinn.custommachinery.common.component.MachineComponentManager;
import fr.frinn.custommachinery.common.crafting.DummyProcessor;
import fr.frinn.custommachinery.common.crafting.UpgradeManager;
import fr.frinn.custommachinery.common.machine.CustomMachine;
import fr.frinn.custommachinery.common.machine.MachineAppearance;
import fr.frinn.custommachinery.common.network.SRefreshCustomMachineTilePacket;
import fr.frinn.custommachinery.common.network.SUpdateMachineAppearancePacket;
import fr.frinn.custommachinery.common.network.SUpdateMachineGuiElementsPacket;
import fr.frinn.custommachinery.common.network.SUpdateMachineStatusPacket;
import fr.frinn.custommachinery.common.network.syncable.StringSyncable;
import fr.frinn.custommachinery.common.util.MachineList;
import fr.frinn.custommachinery.common.util.SoundManager;
import fr.frinn.custommachinery.impl.util.TextComponentUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class CustomMachineTile extends MachineTile implements ISyncableStuff {

    public static final ResourceLocation DUMMY = new ResourceLocation("custommachinery", "dummy");

    private ResourceLocation id = DUMMY;

    private boolean paused = false;

    private IProcessor processor = new DummyProcessor(this);

    private MachineComponentManager componentManager = new DummyComponentManager(this);

    private final UpgradeManager upgradeManager = new UpgradeManager(this);

    private SoundManager soundManager;

    private MachineStatus status = MachineStatus.IDLE;

    private Component errorMessage = Component.empty();

    @Nullable
    private MachineAppearance customAppearance = null;

    @Nullable
    private List<IGuiElement> customGuiElements = null;

    @Nullable
    private Component ownerName;

    @Nullable
    private UUID ownerID;

    private List<WeakReference<ServerPlayer>> players = new ArrayList();

    private boolean unloaded = false;

    public CustomMachineTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) Registration.CUSTOM_MACHINE_TILE.get(), pos, state);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public void setId(ResourceLocation id) {
        this.id = id;
        this.processor = this.getMachine().getProcessorTemplate().build(this);
        this.componentManager = new MachineComponentManager(this.getMachine().getComponentTemplates(), this);
        this.componentManager.getComponents().values().forEach(IMachineComponent::init);
    }

    public CustomMachine getMachine() {
        CustomMachine machine = (CustomMachine) CustomMachinery.MACHINES.get(this.getId());
        return machine != null ? machine : CustomMachine.DUMMY;
    }

    @Override
    public MachineStatus getStatus() {
        return this.isPaused() ? MachineStatus.PAUSED : this.status;
    }

    @Override
    public Component getMessage() {
        return this.errorMessage;
    }

    @Override
    public void setStatus(MachineStatus status, Component message) {
        if (this.status != status) {
            this.componentManager.getComponents().values().forEach(component -> component.onStatusChanged(this.status, status, message));
            this.status = status;
            this.errorMessage = message;
            this.m_6596_();
            if (this.m_58904_() != null && !this.m_58904_().isClientSide()) {
                BlockPos pos = this.m_58899_();
                new SUpdateMachineStatusPacket(pos, this.status).sendToChunkListeners(this.m_58904_().getChunkAt(pos));
            }
        }
    }

    @Override
    public void refreshMachine(@Nullable ResourceLocation id) {
        if (this.f_58857_ != null && !this.f_58857_.isClientSide()) {
            CompoundTag craftingManagerNBT = this.processor.serialize();
            CompoundTag componentManagerNBT = this.componentManager.serializeNBT();
            this.componentManager.getComponents().values().forEach(IMachineComponent::onRemoved);
            if (id == null) {
                id = this.getId();
            }
            this.id = id;
            this.processor = this.getMachine().getProcessorTemplate().build(this);
            this.componentManager = new MachineComponentManager(this.getMachine().getComponentTemplates(), this);
            this.processor.deserialize(craftingManagerNBT);
            this.componentManager.deserializeNBT(componentManagerNBT);
            this.componentManager.getComponents().values().forEach(IMachineComponent::init);
            new SRefreshCustomMachineTilePacket(this.f_58858_, id).sendToChunkListeners(this.f_58857_.getChunkAt(this.f_58858_));
        }
    }

    @Override
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    @Override
    public boolean isPaused() {
        return this.paused;
    }

    @Override
    public void resetProcess() {
        if (this.f_58857_ != null && !this.f_58857_.isClientSide()) {
            this.processor.reset();
        }
    }

    public MachineComponentManager getComponentManager() {
        return this.componentManager;
    }

    public UpgradeManager getUpgradeManager() {
        return this.upgradeManager;
    }

    @Override
    public IProcessor getProcessor() {
        return this.processor;
    }

    public MachineAppearance getAppearance() {
        return this.customAppearance != null ? this.customAppearance : this.getMachine().getAppearance(this.getStatus());
    }

    @Override
    public void setCustomAppearance(@Nullable IMachineAppearance customAppearance) {
        if (this.customAppearance != customAppearance) {
            this.customAppearance = (MachineAppearance) customAppearance;
            if (this.m_58904_() != null && !this.m_58904_().isClientSide()) {
                BlockPos pos = this.m_58899_();
                new SUpdateMachineAppearancePacket(pos, this.customAppearance).sendToChunkListeners(this.m_58904_().getChunkAt(pos));
            }
        }
    }

    @Override
    public List<IGuiElement> getGuiElements() {
        return this.customGuiElements != null && !this.customGuiElements.isEmpty() ? this.customGuiElements : this.getMachine().getGuiElements();
    }

    @Override
    public void setCustomGuiElements(@Nullable List<IGuiElement> customGuiElements) {
        if (this.customGuiElements != customGuiElements && (this.customGuiElements == null || customGuiElements == null || customGuiElements.isEmpty() || !new HashSet(this.customGuiElements).containsAll(customGuiElements))) {
            this.customGuiElements = customGuiElements;
            if (this.m_58904_() != null && !this.m_58904_().isClientSide()) {
                BlockPos pos = this.m_58899_();
                new SUpdateMachineGuiElementsPacket(pos, this.customGuiElements).sendToChunkListeners(this.m_58904_().getChunkAt(pos));
                Iterator<WeakReference<ServerPlayer>> iterator = this.players.iterator();
                while (iterator.hasNext()) {
                    ServerPlayer player = (ServerPlayer) ((WeakReference) iterator.next()).get();
                    if (player != null && player.f_36096_ instanceof CustomMachineContainer container && container.getTile() == this) {
                        container.init();
                    } else {
                        iterator.remove();
                    }
                }
            }
        }
    }

    @Override
    public void setOwner(LivingEntity entity) {
        if (entity != null) {
            this.ownerName = entity.m_7755_();
            this.ownerID = entity.m_20148_();
        }
    }

    @Nullable
    @Override
    public UUID getOwnerId() {
        return this.ownerID;
    }

    @Nullable
    @Override
    public Component getOwnerName() {
        return this.ownerName;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CustomMachineTile tile) {
        if (tile.componentManager != null && tile.processor != null) {
            level.getProfiler().push("Component tick");
            tile.componentManager.serverTick();
            level.getProfiler().pop();
            if (!tile.isPaused()) {
                level.getProfiler().push("Crafting Manager tick");
                try {
                    tile.processor.tick();
                } catch (ComponentNotFoundException var5) {
                    CustomMachinery.LOGGER.error(var5.getMessage());
                    tile.setPaused(true);
                }
                level.getProfiler().pop();
            }
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, CustomMachineTile tile) {
        if (tile.componentManager != null && tile.processor != null) {
            tile.componentManager.clientTick();
            if (tile.soundManager == null) {
                tile.soundManager = new SoundManager(pos);
            }
            if (!tile.getAppearance().getAmbientSound().getLocation().equals(tile.soundManager.getSoundID())) {
                if (tile.getAppearance().getAmbientSound() == ((MachineAppearanceProperty) Registration.AMBIENT_SOUND_PROPERTY.get()).getDefaultValue()) {
                    tile.soundManager.setSound(null);
                } else {
                    tile.soundManager.setSound(tile.getAppearance().getAmbientSound());
                }
            }
            if (!tile.soundManager.isPlaying()) {
                tile.soundManager.play();
            }
        }
    }

    @Override
    public void setLevel(Level level) {
        super.m_142339_(level);
        MachineList.addMachine(this);
        this.componentManager.getComponents().values().forEach(IMachineComponent::init);
    }

    @Override
    public void setRemoved() {
        if (this.f_58857_ != null && this.f_58857_.isClientSide() && this.soundManager != null) {
            this.soundManager.stop();
        }
        if (this.f_58857_ != null && !this.f_58857_.isClientSide()) {
            this.componentManager.getComponents().values().forEach(IMachineComponent::onRemoved);
        }
        super.m_7651_();
    }

    public void unload() {
        this.unloaded = true;
    }

    @Override
    public boolean isUnloaded() {
        return this.unloaded;
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.m_183515_(nbt);
        nbt.putString("machineID", this.id.toString());
        nbt.put("craftingManager", this.processor.serialize());
        nbt.put("componentManager", this.componentManager.serializeNBT());
        nbt.putString("status", this.status.toString());
        nbt.putString("message", TextComponentUtils.toJsonString(this.errorMessage));
        if (this.ownerID != null) {
            nbt.putString("ownerID", this.ownerID.toString());
        }
        if (this.ownerName != null) {
            nbt.putString("ownerName", TextComponentUtils.toJsonString(this.ownerName));
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.m_142466_(nbt);
        if (nbt.contains("machineID", 8) && this.getMachine() == CustomMachine.DUMMY) {
            this.setId(new ResourceLocation(nbt.getString("machineID")));
        }
        if (nbt.contains("craftingManager", 10)) {
            this.processor.deserialize(nbt.getCompound("craftingManager"));
        }
        if (nbt.contains("componentManager", 10)) {
            this.componentManager.deserializeNBT(nbt.getCompound("componentManager"));
        }
        if (nbt.contains("status", 8)) {
            this.setStatus(MachineStatus.value(nbt.getString("status")));
        }
        if (nbt.contains("message", 8)) {
            this.errorMessage = TextComponentUtils.fromJsonString(nbt.getString("message"));
        }
        if (nbt.contains("ownerID", 8)) {
            this.ownerID = UUID.fromString(nbt.getString("ownerID"));
        }
        if (nbt.contains("ownerName", 8)) {
            this.ownerName = TextComponentUtils.fromJsonString(nbt.getString("ownerName"));
        }
        if (nbt.contains("appearance", 10)) {
            this.customAppearance = (MachineAppearance) MachineAppearance.CODEC.read(NbtOps.INSTANCE, nbt.get("appearance")).result().map(MachineAppearance::new).orElse(null);
        }
        if (nbt.contains("gui", 9)) {
            this.customGuiElements = (List<IGuiElement>) IGuiElement.CODEC.listOf().read(NbtOps.INSTANCE, nbt.getList("gui", 10)).result().orElse(Collections.emptyList());
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.m_5995_();
        nbt.putString("machineID", this.getId().toString());
        nbt.putString("status", this.status.toString());
        nbt.putString("message", TextComponentUtils.toJsonString(this.errorMessage));
        if (this.ownerID != null) {
            nbt.putString("ownerID", this.ownerID.toString());
        }
        if (this.ownerName != null) {
            nbt.putString("ownerName", TextComponentUtils.toJsonString(this.ownerName));
        }
        if (this.customAppearance != null) {
            MachineAppearance.CODEC.encodeStart(NbtOps.INSTANCE, this.customAppearance.getProperties()).result().ifPresent(appearance -> nbt.put("appearance", appearance));
        }
        if (this.customGuiElements != null && !this.customGuiElements.isEmpty()) {
            IGuiElement.CODEC.listOf().encodeStart(NbtOps.INSTANCE, this.customGuiElements).result().ifPresent(elements -> nbt.put("gui", elements));
        }
        return nbt;
    }

    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void getStuffToSync(Consumer<ISyncable<?, ?>> container) {
        if (this.processor instanceof ISyncableStuff syncableProcessor) {
            syncableProcessor.getStuffToSync(container);
        }
        this.componentManager.getStuffToSync(container);
        container.accept(StringSyncable.create(() -> this.status.toString(), status -> this.status = MachineStatus.value(status)));
        container.accept(StringSyncable.create(() -> Component.Serializer.toJson(this.errorMessage), errorMessage -> this.errorMessage = Component.Serializer.fromJson(errorMessage)));
    }

    public void startInteracting(ServerPlayer player) {
        if (this.players.stream().noneMatch(ref -> ref.get() == player)) {
            this.players.add(new WeakReference(player));
        }
    }

    public void stopInteracting(ServerPlayer player) {
        Iterator<WeakReference<ServerPlayer>> iterator = this.players.iterator();
        while (iterator.hasNext()) {
            ServerPlayer ref = (ServerPlayer) ((WeakReference) iterator.next()).get();
            if (ref == null || ref == player || !(ref.f_36096_ instanceof CustomMachineContainer)) {
                iterator.remove();
            }
        }
    }
}