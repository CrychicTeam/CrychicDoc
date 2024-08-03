package com.simibubi.create.content.contraptions.minecart.capability;

import com.simibubi.create.AllPackets;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import com.simibubi.create.content.contraptions.minecart.CouplingHandler;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class MinecartController implements INBTSerializable<CompoundTag> {

    public static MinecartController EMPTY;

    private boolean needsEntryRefresh;

    private WeakReference<AbstractMinecart> weakRef;

    private Couple<Optional<MinecartController.StallData>> stallData;

    private Couple<Optional<MinecartController.CouplingData>> couplings;

    public MinecartController(AbstractMinecart minecart) {
        this.weakRef = new WeakReference(minecart);
        this.stallData = Couple.create(Optional::empty);
        this.couplings = Couple.create(Optional::empty);
        this.needsEntryRefresh = true;
    }

    public void tick() {
        AbstractMinecart cart = this.cart();
        Level world = this.getWorld();
        if (this.needsEntryRefresh) {
            CapabilityMinecartController.queuedAdditions.get(world).add(cart);
            this.needsEntryRefresh = false;
        }
        this.stallData.forEach(opt -> opt.ifPresent(sd -> sd.tick(cart)));
        MutableBoolean internalStall = new MutableBoolean(false);
        this.couplings.forEachWithContext((opt, main) -> opt.ifPresent(cd -> {
            UUID idOfOther = cd.idOfCart(!main);
            MinecartController otherCart = CapabilityMinecartController.getIfPresent(world, idOfOther);
            internalStall.setValue(internalStall.booleanValue() || otherCart == null || !otherCart.isPresent() || otherCart.isStalled(false));
        }));
        if (!world.isClientSide) {
            this.setStalled(internalStall.booleanValue(), true);
            this.disassemble(cart);
        }
    }

    private void disassemble(AbstractMinecart cart) {
        if (!(cart instanceof Minecart)) {
            List<Entity> passengers = cart.m_20197_();
            if (!passengers.isEmpty() && passengers.get(0) instanceof AbstractContraptionEntity) {
                Level world = cart.m_9236_();
                int i = Mth.floor(cart.m_20185_());
                int j = Mth.floor(cart.m_20186_());
                int k = Mth.floor(cart.m_20189_());
                if (world.getBlockState(new BlockPos(i, j - 1, k)).m_204336_(BlockTags.RAILS)) {
                    j--;
                }
                BlockPos blockpos = new BlockPos(i, j, k);
                BlockState blockstate = world.getBlockState(blockpos);
                if (cart.canUseRail() && blockstate.m_204336_(BlockTags.RAILS) && blockstate.m_60734_() instanceof PoweredRailBlock && ((PoweredRailBlock) blockstate.m_60734_()).isActivatorRail()) {
                    if (cart.m_20160_()) {
                        cart.m_20153_();
                    }
                    if (cart.getHurtTime() == 0) {
                        cart.setHurtDir(-cart.getHurtDir());
                        cart.setHurtTime(10);
                        cart.setDamage(50.0F);
                        cart.f_19864_ = true;
                    }
                }
            }
        }
    }

    public boolean isFullyCoupled() {
        return this.isLeadingCoupling() && this.isConnectedToCoupling();
    }

    public boolean isLeadingCoupling() {
        return this.couplings.get(true).isPresent();
    }

    public boolean isConnectedToCoupling() {
        return this.couplings.get(false).isPresent();
    }

    public boolean isCoupledThroughContraption() {
        for (boolean current : Iterate.trueAndFalse) {
            if (this.hasContraptionCoupling(current)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasContraptionCoupling(boolean current) {
        Optional<MinecartController.CouplingData> optional = this.couplings.get(current);
        return optional.isPresent() && ((MinecartController.CouplingData) optional.get()).contraption;
    }

    public float getCouplingLength(boolean leading) {
        Optional<MinecartController.CouplingData> optional = this.couplings.get(leading);
        return optional.isPresent() ? ((MinecartController.CouplingData) optional.get()).length : 0.0F;
    }

    public void decouple() {
        this.couplings.forEachWithContext((opt, main) -> opt.ifPresent(cd -> {
            UUID idOfOther = cd.idOfCart(!main);
            MinecartController otherCart = CapabilityMinecartController.getIfPresent(this.getWorld(), idOfOther);
            if (otherCart != null) {
                this.removeConnection(main);
                otherCart.removeConnection(!main);
            }
        }));
    }

    public void removeConnection(boolean main) {
        if (this.hasContraptionCoupling(main) && !this.getWorld().isClientSide) {
            List<Entity> passengers = this.cart().m_20197_();
            if (!passengers.isEmpty()) {
                Entity entity = (Entity) passengers.get(0);
                if (entity instanceof AbstractContraptionEntity) {
                    ((AbstractContraptionEntity) entity).disassemble();
                }
            }
        }
        this.couplings.set(main, Optional.empty());
        this.needsEntryRefresh |= main;
        this.sendData();
    }

    public void prepareForCoupling(boolean isLeading) {
        if (isLeading && this.isLeadingCoupling() || !isLeading && this.isConnectedToCoupling()) {
            List<MinecartController> cartsToFlip = new ArrayList();
            MinecartController current = this;
            boolean forward = this.isLeadingCoupling();
            int safetyCount = 1000;
            do {
                if (safetyCount-- <= 0) {
                    Create.LOGGER.warn("Infinite loop in coupling iteration");
                    return;
                }
                cartsToFlip.add(current);
                current = CouplingHandler.getNextInCouplingChain(this.getWorld(), current, forward);
            } while (current != null && current != EMPTY);
            for (MinecartController minecartController : cartsToFlip) {
                minecartController.couplings.forEachWithContext((opt, leading) -> opt.ifPresent(cd -> {
                    cd.flip();
                    if (cd.contraption) {
                        List<Entity> passengers = minecartController.cart().m_20197_();
                        if (!passengers.isEmpty()) {
                            Entity entity = (Entity) passengers.get(0);
                            if (entity instanceof OrientedContraptionEntity contraption) {
                                UUID couplingId = contraption.getCouplingId();
                                if (couplingId == cd.mainCartID) {
                                    contraption.setCouplingId(cd.connectedCartID);
                                } else if (couplingId == cd.connectedCartID) {
                                    contraption.setCouplingId(cd.mainCartID);
                                }
                            }
                        }
                    }
                }));
                minecartController.couplings = minecartController.couplings.swap();
                minecartController.needsEntryRefresh = true;
                if (minecartController != this) {
                    minecartController.sendData();
                }
            }
        }
    }

    public void coupleWith(boolean isLeading, UUID coupled, float length, boolean contraption) {
        UUID mainID = isLeading ? this.cart().m_20148_() : coupled;
        UUID connectedID = isLeading ? coupled : this.cart().m_20148_();
        this.couplings.set(isLeading, Optional.of(new MinecartController.CouplingData(mainID, connectedID, length, contraption)));
        this.needsEntryRefresh |= isLeading;
        this.sendData();
    }

    @Nullable
    public UUID getCoupledCart(boolean asMain) {
        Optional<MinecartController.CouplingData> optional = this.couplings.get(asMain);
        if (!optional.isPresent()) {
            return null;
        } else {
            MinecartController.CouplingData couplingData = (MinecartController.CouplingData) optional.get();
            return asMain ? couplingData.connectedCartID : couplingData.mainCartID;
        }
    }

    public boolean isStalled() {
        return this.isStalled(true) || this.isStalled(false);
    }

    private boolean isStalled(boolean internal) {
        return this.stallData.get(internal).isPresent();
    }

    public void setStalledExternally(boolean stall) {
        this.setStalled(stall, false);
    }

    private void setStalled(boolean stall, boolean internal) {
        if (this.isStalled(internal) != stall) {
            AbstractMinecart cart = this.cart();
            if (stall) {
                this.stallData.set(internal, Optional.of(new MinecartController.StallData(cart)));
                this.sendData();
            } else {
                if (!this.isStalled(!internal)) {
                    ((MinecartController.StallData) this.stallData.get(internal).get()).release(cart);
                }
                this.stallData.set(internal, Optional.empty());
                this.sendData();
            }
        }
    }

    public void sendData() {
        if (!this.getWorld().isClientSide) {
            AllPackets.getChannel().send(PacketDistributor.TRACKING_ENTITY.with(this::cart), new MinecartControllerUpdatePacket(this));
        }
    }

    public CompoundTag serializeNBT() {
        CompoundTag compoundNBT = new CompoundTag();
        this.stallData.forEachWithContext((opt, internal) -> opt.ifPresent(sd -> compoundNBT.put(internal ? "InternalStallData" : "StallData", sd.serialize())));
        this.couplings.forEachWithContext((opt, main) -> opt.ifPresent(cd -> compoundNBT.put(main ? "MainCoupling" : "ConnectedCoupling", cd.serialize())));
        return compoundNBT;
    }

    public void deserializeNBT(CompoundTag nbt) {
        Optional<MinecartController.StallData> internalSD = Optional.empty();
        Optional<MinecartController.StallData> externalSD = Optional.empty();
        Optional<MinecartController.CouplingData> mainCD = Optional.empty();
        Optional<MinecartController.CouplingData> connectedCD = Optional.empty();
        if (nbt.contains("InternalStallData")) {
            internalSD = Optional.of(MinecartController.StallData.read(nbt.getCompound("InternalStallData")));
        }
        if (nbt.contains("StallData")) {
            externalSD = Optional.of(MinecartController.StallData.read(nbt.getCompound("StallData")));
        }
        if (nbt.contains("MainCoupling")) {
            mainCD = Optional.of(MinecartController.CouplingData.read(nbt.getCompound("MainCoupling")));
        }
        if (nbt.contains("ConnectedCoupling")) {
            connectedCD = Optional.of(MinecartController.CouplingData.read(nbt.getCompound("ConnectedCoupling")));
        }
        this.stallData = Couple.create(internalSD, externalSD);
        this.couplings = Couple.create(mainCD, connectedCD);
        this.needsEntryRefresh = true;
    }

    public boolean isPresent() {
        return this.weakRef.get() != null && this.cart().m_6084_();
    }

    public AbstractMinecart cart() {
        return (AbstractMinecart) this.weakRef.get();
    }

    public static MinecartController empty() {
        return EMPTY != null ? EMPTY : (EMPTY = new MinecartController(null));
    }

    private Level getWorld() {
        return this.cart().m_20193_();
    }

    private static class CouplingData {

        private UUID mainCartID;

        private UUID connectedCartID;

        private float length;

        private boolean contraption;

        public CouplingData(UUID mainCartID, UUID connectedCartID, float length, boolean contraption) {
            this.mainCartID = mainCartID;
            this.connectedCartID = connectedCartID;
            this.length = length;
            this.contraption = contraption;
        }

        void flip() {
            UUID swap = this.mainCartID;
            this.mainCartID = this.connectedCartID;
            this.connectedCartID = swap;
        }

        CompoundTag serialize() {
            CompoundTag nbt = new CompoundTag();
            nbt.put("Main", NbtUtils.createUUID(this.mainCartID));
            nbt.put("Connected", NbtUtils.createUUID(this.connectedCartID));
            nbt.putFloat("Length", this.length);
            nbt.putBoolean("Contraption", this.contraption);
            return nbt;
        }

        static MinecartController.CouplingData read(CompoundTag nbt) {
            UUID mainCartID = NbtUtils.loadUUID(NBTHelper.getINBT(nbt, "Main"));
            UUID connectedCartID = NbtUtils.loadUUID(NBTHelper.getINBT(nbt, "Connected"));
            float length = nbt.getFloat("Length");
            boolean contraption = nbt.getBoolean("Contraption");
            return new MinecartController.CouplingData(mainCartID, connectedCartID, length, contraption);
        }

        public UUID idOfCart(boolean main) {
            return main ? this.mainCartID : this.connectedCartID;
        }
    }

    private static class StallData {

        Vec3 position;

        Vec3 motion;

        float yaw;

        float pitch;

        private StallData() {
        }

        StallData(AbstractMinecart entity) {
            this.position = entity.m_20182_();
            this.motion = entity.m_20184_();
            this.yaw = entity.m_146908_();
            this.pitch = entity.m_146909_();
            this.tick(entity);
        }

        void tick(AbstractMinecart entity) {
            entity.m_20256_(Vec3.ZERO);
            entity.m_146922_(this.yaw);
            entity.m_146926_(this.pitch);
        }

        void release(AbstractMinecart entity) {
            entity.m_20256_(this.motion);
        }

        CompoundTag serialize() {
            CompoundTag nbt = new CompoundTag();
            nbt.put("Pos", VecHelper.writeNBT(this.position));
            nbt.put("Motion", VecHelper.writeNBT(this.motion));
            nbt.putFloat("Yaw", this.yaw);
            nbt.putFloat("Pitch", this.pitch);
            return nbt;
        }

        static MinecartController.StallData read(CompoundTag nbt) {
            MinecartController.StallData stallData = new MinecartController.StallData();
            stallData.position = VecHelper.readNBT(nbt.getList("Pos", 6));
            stallData.motion = VecHelper.readNBT(nbt.getList("Motion", 6));
            stallData.yaw = nbt.getFloat("Yaw");
            stallData.pitch = nbt.getFloat("Pitch");
            return stallData;
        }
    }
}