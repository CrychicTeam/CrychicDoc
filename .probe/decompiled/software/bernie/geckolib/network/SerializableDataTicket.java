package software.bernie.geckolib.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.object.DataTicket;

public abstract class SerializableDataTicket<D> extends DataTicket<D> {

    public SerializableDataTicket(String id, Class<? extends D> objectType) {
        super(id, objectType);
    }

    public abstract void encode(D var1, FriendlyByteBuf var2);

    public abstract D decode(FriendlyByteBuf var1);

    public static SerializableDataTicket<Double> ofDouble(ResourceLocation id) {
        return new SerializableDataTicket<Double>(id.toString(), Double.class) {

            public void encode(Double data, FriendlyByteBuf buffer) {
                buffer.writeDouble(data);
            }

            public Double decode(FriendlyByteBuf buffer) {
                return buffer.readDouble();
            }
        };
    }

    public static SerializableDataTicket<Float> ofFloat(ResourceLocation id) {
        return new SerializableDataTicket<Float>(id.toString(), Float.class) {

            public void encode(Float data, FriendlyByteBuf buffer) {
                buffer.writeFloat(data);
            }

            public Float decode(FriendlyByteBuf buffer) {
                return buffer.readFloat();
            }
        };
    }

    public static SerializableDataTicket<Boolean> ofBoolean(ResourceLocation id) {
        return new SerializableDataTicket<Boolean>(id.toString(), Boolean.class) {

            public void encode(Boolean data, FriendlyByteBuf buffer) {
                buffer.writeBoolean(data);
            }

            public Boolean decode(FriendlyByteBuf buffer) {
                return buffer.readBoolean();
            }
        };
    }

    public static SerializableDataTicket<Integer> ofInt(ResourceLocation id) {
        return new SerializableDataTicket<Integer>(id.toString(), Integer.class) {

            public void encode(Integer data, FriendlyByteBuf buffer) {
                buffer.writeVarInt(data);
            }

            public Integer decode(FriendlyByteBuf buffer) {
                return buffer.readVarInt();
            }
        };
    }

    public static SerializableDataTicket<String> ofString(ResourceLocation id) {
        return new SerializableDataTicket<String>(id.toString(), String.class) {

            public void encode(String data, FriendlyByteBuf buffer) {
                buffer.writeUtf(data);
            }

            public String decode(FriendlyByteBuf buffer) {
                return buffer.readUtf();
            }
        };
    }

    public static <E extends Enum<E>> SerializableDataTicket<E> ofEnum(ResourceLocation id, final Class<E> enumClass) {
        return new SerializableDataTicket<E>(id.toString(), enumClass) {

            public void encode(E data, FriendlyByteBuf buffer) {
                buffer.writeUtf(data.toString());
            }

            public E decode(FriendlyByteBuf buffer) {
                return (E) Enum.valueOf(enumClass, buffer.readUtf());
            }
        };
    }
}