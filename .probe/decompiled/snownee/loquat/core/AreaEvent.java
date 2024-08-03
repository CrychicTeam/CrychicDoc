package snownee.loquat.core;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import snownee.loquat.LoquatRegistries;
import snownee.loquat.core.area.Area;

public abstract class AreaEvent {

    public int ticksExisted;

    protected boolean isFinished;

    protected final Area area;

    protected AreaEvent(Area area) {
        this.area = area;
    }

    public abstract void tick(ServerLevel var1);

    public abstract AreaEvent.Type<?> getType();

    public static AreaEvent deserialize(AreaManager manager, CompoundTag data) {
        Area area = manager.get(data.getUUID("Area"));
        if (area == null) {
            return null;
        } else {
            AreaEvent.Type<?> type = LoquatRegistries.AREA_EVENT.get(new ResourceLocation(data.getString("Type")));
            AreaEvent event = type.deserialize(area, data);
            event.ticksExisted = data.getInt("Ticks");
            return event;
        }
    }

    public final CompoundTag serialize(CompoundTag data) {
        data.putString("Type", LoquatRegistries.AREA_EVENT.getKey(this.getType()).toString());
        ((AreaEvent.Type<AreaEvent>) this.getType()).serialize(data, this);
        data.putUUID("Area", this.area.getUuid());
        data.putInt("Ticks", this.ticksExisted);
        return data;
    }

    public boolean isFinished() {
        return this.isFinished;
    }

    public Area getArea() {
        return this.area;
    }

    public abstract static class Type<T extends AreaEvent> {

        public abstract T deserialize(Area var1, CompoundTag var2);

        public abstract CompoundTag serialize(CompoundTag var1, T var2);
    }
}