package net.minecraft.network.syncher;

public class EntityDataAccessor<T> {

    private final int id;

    private final EntityDataSerializer<T> serializer;

    public EntityDataAccessor(int int0, EntityDataSerializer<T> entityDataSerializerT1) {
        this.id = int0;
        this.serializer = entityDataSerializerT1;
    }

    public int getId() {
        return this.id;
    }

    public EntityDataSerializer<T> getSerializer() {
        return this.serializer;
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else if (object0 != null && this.getClass() == object0.getClass()) {
            EntityDataAccessor<?> $$1 = (EntityDataAccessor<?>) object0;
            return this.id == $$1.id;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.id;
    }

    public String toString() {
        return "<entity data: " + this.id + ">";
    }
}