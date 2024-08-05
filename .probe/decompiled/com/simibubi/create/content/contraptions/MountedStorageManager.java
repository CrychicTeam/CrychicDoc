package com.simibubi.create.content.contraptions;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.NBTHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class MountedStorageManager {

    protected Contraption.ContraptionInvWrapper inventory;

    protected Contraption.ContraptionInvWrapper fuelInventory;

    protected CombinedTankWrapper fluidInventory;

    protected Map<BlockPos, MountedStorage> storage = new HashMap();

    protected Map<BlockPos, MountedFluidStorage> fluidStorage = new HashMap();

    public void entityTick(AbstractContraptionEntity entity) {
        this.fluidStorage.forEach((pos, mfs) -> mfs.tick(entity, pos, entity.m_9236_().isClientSide));
    }

    public void createHandlers() {
        Collection<MountedStorage> itemHandlers = this.storage.values();
        this.inventory = this.wrapItems(itemHandlers.stream().map(MountedStorage::getItemHandler).toList(), false);
        this.fuelInventory = this.wrapItems(itemHandlers.stream().filter(MountedStorage::canUseForFuel).map(MountedStorage::getItemHandler).toList(), true);
        this.fluidInventory = this.wrapFluids((Collection<IFluidHandler>) this.fluidStorage.values().stream().map(MountedFluidStorage::getFluidHandler).collect(Collectors.toList()));
    }

    protected Contraption.ContraptionInvWrapper wrapItems(Collection<IItemHandlerModifiable> list, boolean fuel) {
        return new Contraption.ContraptionInvWrapper((IItemHandlerModifiable[]) Arrays.copyOf(list.toArray(), list.size(), IItemHandlerModifiable[].class));
    }

    protected CombinedTankWrapper wrapFluids(Collection<IFluidHandler> list) {
        return new CombinedTankWrapper((IFluidHandler[]) Arrays.copyOf(list.toArray(), list.size(), IFluidHandler[].class));
    }

    public void addBlock(BlockPos localPos, BlockEntity be) {
        if (be != null && MountedStorage.canUseAsStorage(be)) {
            this.storage.put(localPos, new MountedStorage(be));
        }
        if (be != null && MountedFluidStorage.canUseAsStorage(be)) {
            this.fluidStorage.put(localPos, new MountedFluidStorage(be));
        }
    }

    public void read(CompoundTag nbt, Map<BlockPos, BlockEntity> presentBlockEntities, boolean clientPacket) {
        this.storage.clear();
        NBTHelper.iterateCompoundList(nbt.getList("Storage", 10), c -> this.storage.put(NbtUtils.readBlockPos(c.getCompound("Pos")), MountedStorage.deserialize(c.getCompound("Data"))));
        this.fluidStorage.clear();
        NBTHelper.iterateCompoundList(nbt.getList("FluidStorage", 10), c -> this.fluidStorage.put(NbtUtils.readBlockPos(c.getCompound("Pos")), MountedFluidStorage.deserialize(c.getCompound("Data"))));
        if (clientPacket && presentBlockEntities != null) {
            this.bindTanks(presentBlockEntities);
        }
        List<IItemHandlerModifiable> handlers = new ArrayList();
        List<IItemHandlerModifiable> fuelHandlers = new ArrayList();
        for (MountedStorage mountedStorage : this.storage.values()) {
            IItemHandlerModifiable itemHandler = mountedStorage.getItemHandler();
            handlers.add(itemHandler);
            if (mountedStorage.canUseForFuel()) {
                fuelHandlers.add(itemHandler);
            }
        }
        this.inventory = this.wrapItems(handlers, false);
        this.fuelInventory = this.wrapItems(fuelHandlers, true);
        this.fluidInventory = this.wrapFluids(this.fluidStorage.values().stream().map(MountedFluidStorage::getFluidHandler).toList());
    }

    public void bindTanks(Map<BlockPos, BlockEntity> presentBlockEntities) {
        this.fluidStorage.forEach((pos, mfs) -> {
            BlockEntity blockEntity = (BlockEntity) presentBlockEntities.get(pos);
            if (blockEntity instanceof FluidTankBlockEntity tank) {
                IFluidTank tankInventory = tank.getTankInventory();
                if (tankInventory instanceof FluidTank) {
                    ((FluidTank) tankInventory).setFluid(mfs.tank.getFluid());
                }
                tank.getFluidLevel().startWithValue((double) tank.getFillState());
                mfs.assignBlockEntity(tank);
            }
        });
    }

    public void write(CompoundTag nbt, boolean clientPacket) {
        ListTag storageNBT = new ListTag();
        if (!clientPacket) {
            for (BlockPos pos : this.storage.keySet()) {
                CompoundTag c = new CompoundTag();
                MountedStorage mountedStorage = (MountedStorage) this.storage.get(pos);
                if (mountedStorage.isValid()) {
                    c.put("Pos", NbtUtils.writeBlockPos(pos));
                    c.put("Data", mountedStorage.serialize());
                    storageNBT.add(c);
                }
            }
        }
        ListTag fluidStorageNBT = new ListTag();
        for (BlockPos posx : this.fluidStorage.keySet()) {
            CompoundTag c = new CompoundTag();
            MountedFluidStorage mountedStorage = (MountedFluidStorage) this.fluidStorage.get(posx);
            if (mountedStorage.isValid()) {
                c.put("Pos", NbtUtils.writeBlockPos(posx));
                c.put("Data", mountedStorage.serialize());
                fluidStorageNBT.add(c);
            }
        }
        nbt.put("Storage", storageNBT);
        nbt.put("FluidStorage", fluidStorageNBT);
    }

    public void removeStorageFromWorld() {
        this.storage.values().forEach(MountedStorage::removeStorageFromWorld);
        this.fluidStorage.values().forEach(MountedFluidStorage::removeStorageFromWorld);
    }

    public void addStorageToWorld(StructureTemplate.StructureBlockInfo block, BlockEntity blockEntity) {
        if (this.storage.containsKey(block.pos())) {
            MountedStorage mountedStorage = (MountedStorage) this.storage.get(block.pos());
            if (mountedStorage.isValid()) {
                mountedStorage.addStorageToWorld(blockEntity);
            }
        }
        if (this.fluidStorage.containsKey(block.pos())) {
            MountedFluidStorage mountedStorage = (MountedFluidStorage) this.fluidStorage.get(block.pos());
            if (mountedStorage.isValid()) {
                mountedStorage.addStorageToWorld(blockEntity);
            }
        }
    }

    public void clear() {
        for (int i = 0; i < this.inventory.getSlots(); i++) {
            if (!this.inventory.isSlotExternal(i)) {
                this.inventory.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
        for (int ix = 0; ix < this.fluidInventory.getTanks(); ix++) {
            this.fluidInventory.drain(this.fluidInventory.getFluidInTank(ix), IFluidHandler.FluidAction.EXECUTE);
        }
    }

    public void updateContainedFluid(BlockPos localPos, FluidStack containedFluid) {
        MountedFluidStorage mountedFluidStorage = (MountedFluidStorage) this.fluidStorage.get(localPos);
        if (mountedFluidStorage != null) {
            mountedFluidStorage.updateFluid(containedFluid);
        }
    }

    public void attachExternal(IItemHandlerModifiable externalStorage) {
        this.inventory = new Contraption.ContraptionInvWrapper(externalStorage, this.inventory);
        this.fuelInventory = new Contraption.ContraptionInvWrapper(externalStorage, this.fuelInventory);
    }

    public IItemHandlerModifiable getItems() {
        return this.inventory;
    }

    public IItemHandlerModifiable getFuelItems() {
        return this.fuelInventory;
    }

    public IFluidHandler getFluids() {
        return this.fluidInventory;
    }

    public boolean handlePlayerStorageInteraction(Contraption contraption, Player player, BlockPos localPos) {
        if (player.m_9236_().isClientSide()) {
            BlockEntity localBE = (BlockEntity) contraption.presentBlockEntities.get(localPos);
            return MountedStorage.canUseAsStorage(localBE);
        } else {
            MountedStorageManager storageManager = contraption.getStorageForSpawnPacket();
            MountedStorage storage = (MountedStorage) storageManager.storage.get(localPos);
            if (storage != null && storage.getItemHandler() != null) {
                IItemHandlerModifiable handler = storage.getItemHandler();
                StructureTemplate.StructureBlockInfo info = (StructureTemplate.StructureBlockInfo) contraption.getBlocks().get(localPos);
                if (info != null && info.state().m_61138_(ChestBlock.TYPE)) {
                    ChestType chestType = (ChestType) info.state().m_61143_(ChestBlock.TYPE);
                    Direction facing = (Direction) info.state().m_61145_(ChestBlock.FACING).orElse(Direction.SOUTH);
                    Direction connectedDirection = chestType == ChestType.LEFT ? facing.getClockWise() : facing.getCounterClockWise();
                    if (chestType != ChestType.SINGLE) {
                        MountedStorage storage2 = (MountedStorage) storageManager.storage.get(localPos.relative(connectedDirection));
                        if (storage2 != null && storage2.getItemHandler() != null) {
                            handler = chestType == ChestType.RIGHT ? new CombinedInvWrapper(handler, storage2.getItemHandler()) : new CombinedInvWrapper(storage2.getItemHandler(), handler);
                        }
                    }
                }
                int slotCount = handler.getSlots();
                if (slotCount == 0) {
                    return false;
                } else if (slotCount % 9 != 0) {
                    return false;
                } else {
                    Supplier<Boolean> stillValid = () -> contraption.entity.m_6084_() && player.m_20238_(contraption.entity.toGlobalVector(Vec3.atCenterOf(localPos), 0.0F)) < 64.0;
                    Component name = info != null ? info.state().m_60734_().getName() : Components.literal("Container");
                    player.openMenu(MountedStorageInteraction.createMenuProvider(name, handler, slotCount, stillValid));
                    Vec3 soundPos = contraption.entity.toGlobalVector(Vec3.atCenterOf(localPos), 0.0F);
                    player.m_9236_().playSound(null, BlockPos.containing(soundPos), SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, 0.75F, 1.0F);
                    return true;
                }
            } else {
                return false;
            }
        }
    }
}