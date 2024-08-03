package net.minecraft.network.syncher;

import java.util.Optional;
import net.minecraft.core.IdMap;
import net.minecraft.network.FriendlyByteBuf;

public interface EntityDataSerializer<T> {

    void write(FriendlyByteBuf var1, T var2);

    T read(FriendlyByteBuf var1);

    default EntityDataAccessor<T> createAccessor(int int0) {
        return new EntityDataAccessor<>(int0, this);
    }

    T copy(T var1);

    static <T> EntityDataSerializer<T> simple(final FriendlyByteBuf.Writer<T> friendlyByteBufWriterT0, final FriendlyByteBuf.Reader<T> friendlyByteBufReaderT1) {
        return new EntityDataSerializer.ForValueType<T>() {

            @Override
            public void write(FriendlyByteBuf p_238109_, T p_238110_) {
                friendlyByteBufWriterT0.accept(p_238109_, p_238110_);
            }

            @Override
            public T read(FriendlyByteBuf p_238107_) {
                return (T) friendlyByteBufReaderT1.apply(p_238107_);
            }
        };
    }

    static <T> EntityDataSerializer<Optional<T>> optional(FriendlyByteBuf.Writer<T> friendlyByteBufWriterT0, FriendlyByteBuf.Reader<T> friendlyByteBufReaderT1) {
        return simple(friendlyByteBufWriterT0.asOptional(), friendlyByteBufReaderT1.asOptional());
    }

    static <T extends Enum<T>> EntityDataSerializer<T> simpleEnum(Class<T> classT0) {
        return simple(FriendlyByteBuf::m_130068_, p_238094_ -> p_238094_.readEnum(classT0));
    }

    static <T> EntityDataSerializer<T> simpleId(IdMap<T> idMapT0) {
        return simple((p_238088_, p_238089_) -> p_238088_.writeId(idMapT0, (T) p_238089_), p_238085_ -> p_238085_.readById(idMapT0));
    }

    public interface ForValueType<T> extends EntityDataSerializer<T> {

        @Override
        default T copy(T t0) {
            return t0;
        }
    }
}