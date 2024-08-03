package noppes.npcs.controllers.data;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import noppes.npcs.api.entity.data.IMark;
import noppes.npcs.api.handler.data.IAvailability;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketMarkData;

public class MarkData implements ICapabilityProvider {

    public static Capability<MarkData> MARKDATA_CAPABILITY = CapabilityManager.get(new CapabilityToken<MarkData>() {
    });

    private static final String NBTKEY = "cnpcmarkdata";

    private static final ResourceLocation CAPKEY = new ResourceLocation("customnpcs", "markdata");

    private LivingEntity entity;

    private LazyOptional<MarkData> instance = LazyOptional.of(() -> this);

    public List<MarkData.Mark> marks = new ArrayList();

    private static MarkData backup = new MarkData();

    public void setNBT(CompoundTag compound) {
        List<MarkData.Mark> marks = new ArrayList();
        ListTag list = compound.getList("marks", 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag c = list.getCompound(i);
            MarkData.Mark m = new MarkData.Mark();
            m.type = c.getInt("type");
            m.color = c.getInt("color");
            m.availability.load(c.getCompound("availability"));
            marks.add(m);
        }
        this.marks = marks;
    }

    public CompoundTag getNBT() {
        CompoundTag compound = new CompoundTag();
        ListTag list = new ListTag();
        for (MarkData.Mark m : this.marks) {
            CompoundTag c = new CompoundTag();
            c.putInt("type", m.type);
            c.putInt("color", m.color);
            c.put("availability", m.availability.save(new CompoundTag()));
            list.add(c);
        }
        compound.put("marks", list);
        return compound;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
        return capability == MARKDATA_CAPABILITY ? this.instance.cast() : LazyOptional.empty();
    }

    public static void register(AttachCapabilitiesEvent<Entity> event) {
        event.addCapability(CAPKEY, new MarkData());
    }

    public void save() {
        this.entity.getPersistentData().put("cnpcmarkdata", this.getNBT());
    }

    public IMark addMark(int type) {
        MarkData.Mark m = new MarkData.Mark();
        m.type = type;
        this.marks.add(m);
        if (!this.entity.m_9236_().isClientSide) {
            this.syncClients();
        }
        return m;
    }

    public IMark addMark(int type, int color) {
        MarkData.Mark m = new MarkData.Mark();
        m.type = type;
        m.color = color;
        this.marks.add(m);
        if (!this.entity.m_9236_().isClientSide) {
            this.syncClients();
        }
        return m;
    }

    public static MarkData get(LivingEntity entity) {
        MarkData data = entity.getCapability(MARKDATA_CAPABILITY, null).orElse(backup);
        if (data.entity == null) {
            data.entity = entity;
            data.setNBT(entity.getPersistentData().getCompound("cnpcmarkdata"));
        }
        return data;
    }

    public void syncClients() {
        Packets.sendAll(new PacketMarkData(this.entity.m_19879_(), this.getNBT()));
    }

    public class Mark implements IMark {

        public int type = 0;

        public Availability availability = new Availability();

        public int color = 16772433;

        @Override
        public IAvailability getAvailability() {
            return this.availability;
        }

        @Override
        public int getColor() {
            return this.color;
        }

        @Override
        public void setColor(int color) {
            this.color = color;
        }

        @Override
        public int getType() {
            return this.type;
        }

        @Override
        public void setType(int type) {
            this.type = type;
        }

        @Override
        public void update() {
            MarkData.this.syncClients();
        }
    }
}