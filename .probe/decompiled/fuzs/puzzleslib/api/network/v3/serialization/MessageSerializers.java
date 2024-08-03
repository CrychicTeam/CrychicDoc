package fuzs.puzzleslib.api.network.v3.serialization;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import fuzs.puzzleslib.impl.network.serialization.RecordSerializer;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.IntFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.Rotations;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.joml.Vector3f;

public final class MessageSerializers {

    private static final Map<Class<?>, MessageSerializer<?>> SERIALIZERS = Collections.synchronizedMap(Maps.newIdentityHashMap());

    private static final Map<Class<?>, Function<Type[], MessageSerializer<?>>> CONTAINER_PROVIDERS = Collections.synchronizedMap(Maps.newLinkedHashMap());

    private MessageSerializers() {
    }

    public static <T> void registerSerializer(Class<T> type, FriendlyByteBuf.Writer<T> writer, FriendlyByteBuf.Reader<T> reader) {
        registerSerializer(type, new MessageSerializers.MessageSerializerImpl<>(writer, reader));
    }

    public static <T> void registerSerializer(Class<? super T> type, ResourceKey<Registry<T>> resourceKey) {
        Registry<T> registry;
        if (BuiltInRegistries.REGISTRY.containsKey(resourceKey.location())) {
            registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(resourceKey.location());
        } else {
            registry = null;
        }
        Objects.requireNonNull(registry, "Registry for key %s not found".formatted(resourceKey.location()));
        registerSerializer((Class<T>) type, (friendlyByteBuf, t) -> friendlyByteBuf.writeVarInt(registry.getId((T) t)), friendlyByteBuf -> registry.m_7942_(friendlyByteBuf.readVarInt()));
    }

    private static <T> void registerSerializer(Class<T> type, MessageSerializer<T> value) {
        if (SERIALIZERS.put(type, value) != null) {
            throw new IllegalStateException("Duplicate serializer registered for type %s".formatted(type));
        }
    }

    private static <T> void registerSerializer(Class<T> type, EntityDataSerializer<T> entityDataSerializer) {
        registerSerializer(type, entityDataSerializer::m_6856_, entityDataSerializer::m_6709_);
    }

    public static <T> void registerContainerProvider(Class<T> type, Function<Type[], MessageSerializer<? extends T>> factory) {
        if (CONTAINER_PROVIDERS.put(type, factory) != null) {
            throw new IllegalStateException("Duplicate collection provider registered for type %s".formatted(type));
        }
    }

    public static <T> MessageSerializer<T> findByType(Class<T> type) {
        MessageSerializer<T> serializer = (MessageSerializer<T>) SERIALIZERS.get(type);
        if (serializer == null) {
            serializer = computeIfAbsent(type);
            SERIALIZERS.put(type, serializer);
        }
        return serializer;
    }

    private static <T, E extends Enum<E>> MessageSerializer<T> computeIfAbsent(Class<T> clazz) {
        if (Record.class.isAssignableFrom(clazz)) {
            return RecordSerializer.createRecordSerializer(clazz);
        } else if (clazz.isArray()) {
            return (MessageSerializer<T>) createArraySerializer(clazz.getComponentType());
        } else if (clazz.isEnum()) {
            return createEnumSerializer(clazz);
        } else {
            throw new RuntimeException("Missing serializer for type %s".formatted(clazz));
        }
    }

    @Internal
    public static MessageSerializer<?> findByGenericType(Type type) {
        if (type instanceof Class<?> clazz) {
            return findByType(clazz);
        } else {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Class<?> clazz = (Class<?>) parameterizedType.getRawType();
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            for (Entry<Class<?>, Function<Type[], MessageSerializer<?>>> entry : CONTAINER_PROVIDERS.entrySet()) {
                if (((Class) entry.getKey()).isAssignableFrom(clazz)) {
                    return (MessageSerializer<?>) ((Function) entry.getValue()).apply(typeArguments);
                }
            }
            return Collection.class.isAssignableFrom(clazz) ? createCollectionSerializer(typeArguments, Sets::newLinkedHashSetWithExpectedSize) : findByType(clazz);
        }
    }

    private static <K, V> MessageSerializer<Map<K, V>> createMapSerializer(Type[] typeArguments) {
        MessageSerializer<K> keySerializer = findByType((Class<K>) typeArguments[0]);
        MessageSerializer<V> valueSerializer = findByType((Class<V>) typeArguments[1]);
        return new MessageSerializers.MessageSerializerImpl<>((friendlyByteBuf, o) -> friendlyByteBuf.writeMap(o, keySerializer::write, valueSerializer::write), friendlyByteBuf -> friendlyByteBuf.readMap(keySerializer::read, valueSerializer::read));
    }

    private static <T, C extends Collection<T>> MessageSerializer<C> createCollectionSerializer(Type[] typeArguments, IntFunction<C> factory) {
        MessageSerializer<T> serializer = findByType((Class<T>) typeArguments[0]);
        return new MessageSerializers.MessageSerializerImpl((friendlyByteBuf, o) -> friendlyByteBuf.writeCollection(o, serializer::write), friendlyByteBuf -> friendlyByteBuf.readCollection(factory, serializer::read));
    }

    private static <T> MessageSerializer<Optional<T>> createOptionalSerializer(Type[] typeArguments) {
        MessageSerializer<T> serializer = findByType((Class<T>) typeArguments[0]);
        return new MessageSerializers.MessageSerializerImpl<>((friendlyByteBuf, o) -> friendlyByteBuf.writeOptional(o, serializer::write), friendlyByteBuf -> friendlyByteBuf.readOptional(serializer::read));
    }

    private static <L, R> MessageSerializer<Either<L, R>> createEitherSerializer(Type[] typeArguments) {
        MessageSerializer<L> leftSerializer = findByType((Class<L>) typeArguments[0]);
        MessageSerializer<R> rightSerializer = findByType((Class<R>) typeArguments[1]);
        return new MessageSerializers.MessageSerializerImpl<>((friendlyByteBuf, o) -> friendlyByteBuf.writeEither(o, leftSerializer::write, rightSerializer::write), friendlyByteBuf -> friendlyByteBuf.readEither(leftSerializer::read, rightSerializer::read));
    }

    private static MessageSerializer<?> createArraySerializer(Class<?> clazz) {
        MessageSerializer<Object> serializer = findByType((Class<Object>) clazz);
        return new MessageSerializers.MessageSerializerImpl((buf, t) -> {
            int length = Array.getLength(t);
            buf.writeVarInt(length);
            for (int i = 0; i < length; i++) {
                serializer.write(buf, Array.get(t, i));
            }
        }, buf -> {
            int length = buf.readVarInt();
            Object array = Array.newInstance(clazz, length);
            for (int i = 0; i < length; i++) {
                Array.set(array, i, serializer.read(buf));
            }
            return array;
        });
    }

    private static <E extends Enum<E>> MessageSerializer<E> createEnumSerializer(Class<E> clazz) {
        return new MessageSerializers.MessageSerializerImpl<>(FriendlyByteBuf::m_130068_, buf -> buf.readEnum(clazz));
    }

    static {
        registerSerializer(boolean.class, FriendlyByteBuf::writeBoolean, FriendlyByteBuf::readBoolean);
        registerSerializer(Boolean.class, FriendlyByteBuf::writeBoolean, FriendlyByteBuf::readBoolean);
        registerSerializer(int.class, FriendlyByteBuf::m_130130_, FriendlyByteBuf::m_130242_);
        registerSerializer(Integer.class, FriendlyByteBuf::m_130130_, FriendlyByteBuf::m_130242_);
        registerSerializer(long.class, FriendlyByteBuf::m_130103_, FriendlyByteBuf::m_130258_);
        registerSerializer(Long.class, FriendlyByteBuf::m_130103_, FriendlyByteBuf::m_130258_);
        registerSerializer(float.class, FriendlyByteBuf::writeFloat, FriendlyByteBuf::readFloat);
        registerSerializer(Float.class, FriendlyByteBuf::writeFloat, FriendlyByteBuf::readFloat);
        registerSerializer(double.class, FriendlyByteBuf::writeDouble, FriendlyByteBuf::readDouble);
        registerSerializer(Double.class, FriendlyByteBuf::writeDouble, FriendlyByteBuf::readDouble);
        registerSerializer(byte.class, FriendlyByteBuf::writeByte, FriendlyByteBuf::readByte);
        registerSerializer(Byte.class, FriendlyByteBuf::writeByte, FriendlyByteBuf::readByte);
        registerSerializer(short.class, FriendlyByteBuf::writeShort, FriendlyByteBuf::readShort);
        registerSerializer(Short.class, FriendlyByteBuf::writeShort, FriendlyByteBuf::readShort);
        registerSerializer(char.class, FriendlyByteBuf::writeChar, FriendlyByteBuf::readChar);
        registerSerializer(Character.class, FriendlyByteBuf::writeChar, FriendlyByteBuf::readChar);
        registerSerializer(String.class, EntityDataSerializers.STRING);
        registerSerializer(Date.class, FriendlyByteBuf::m_130075_, FriendlyByteBuf::m_130282_);
        registerSerializer(Instant.class, FriendlyByteBuf::m_236826_, FriendlyByteBuf::m_236873_);
        registerSerializer(UUID.class, FriendlyByteBuf::m_130077_, FriendlyByteBuf::m_130259_);
        registerSerializer(Component.class, EntityDataSerializers.COMPONENT);
        registerSerializer(ItemStack.class, EntityDataSerializers.ITEM_STACK);
        registerSerializer(Rotations.class, EntityDataSerializers.ROTATIONS);
        registerSerializer(BlockPos.class, EntityDataSerializers.BLOCK_POS);
        registerSerializer(Direction.class, EntityDataSerializers.DIRECTION);
        registerSerializer(CompoundTag.class, EntityDataSerializers.COMPOUND_TAG);
        registerSerializer(ParticleOptions.class, EntityDataSerializers.PARTICLE);
        registerSerializer(VillagerData.class, EntityDataSerializers.VILLAGER_DATA);
        registerSerializer(Pose.class, EntityDataSerializers.POSE);
        registerSerializer(ChunkPos.class, FriendlyByteBuf::m_178341_, FriendlyByteBuf::m_178383_);
        registerSerializer(ResourceLocation.class, FriendlyByteBuf::m_130085_, FriendlyByteBuf::m_130281_);
        registerSerializer(BlockHitResult.class, FriendlyByteBuf::m_130062_, FriendlyByteBuf::m_130283_);
        registerSerializer(BitSet.class, FriendlyByteBuf::m_178350_, FriendlyByteBuf::m_178384_);
        registerSerializer(GameProfile.class, FriendlyByteBuf::m_236803_, FriendlyByteBuf::m_236875_);
        registerSerializer(Vec3.class, (friendlyByteBuf, vec3) -> {
            friendlyByteBuf.writeDouble(vec3.x());
            friendlyByteBuf.writeDouble(vec3.y());
            friendlyByteBuf.writeDouble(vec3.z());
        }, friendlyByteBuf -> new Vec3(friendlyByteBuf.readDouble(), friendlyByteBuf.readDouble(), friendlyByteBuf.readDouble()));
        registerSerializer(Vector3f.class, (friendlyByteBuf, vec3) -> {
            friendlyByteBuf.writeFloat(vec3.x());
            friendlyByteBuf.writeFloat(vec3.y());
            friendlyByteBuf.writeFloat(vec3.z());
        }, friendlyByteBuf -> new Vector3f(friendlyByteBuf.readFloat(), friendlyByteBuf.readFloat(), friendlyByteBuf.readFloat()));
        registerSerializer(FriendlyByteBuf.class, (buf, other) -> {
            buf.writeVarInt(other.readableBytes());
            buf.writeBytes(other);
            other.release();
        }, buf -> new FriendlyByteBuf(buf.readBytes(buf.readVarInt())));
        registerSerializer(SoundEvent.class, Registries.SOUND_EVENT);
        registerSerializer(Fluid.class, Registries.FLUID);
        registerSerializer(MobEffect.class, Registries.MOB_EFFECT);
        registerSerializer(Block.class, Registries.BLOCK);
        registerSerializer(Enchantment.class, Registries.ENCHANTMENT);
        registerSerializer(EntityType.class, Registries.ENTITY_TYPE);
        registerSerializer(Item.class, Registries.ITEM);
        registerSerializer(Potion.class, Registries.POTION);
        registerSerializer(ParticleType.class, Registries.PARTICLE_TYPE);
        registerSerializer(BlockEntityType.class, Registries.BLOCK_ENTITY_TYPE);
        registerSerializer(MenuType.class, Registries.MENU);
        registerSerializer(Attribute.class, Registries.ATTRIBUTE);
        registerSerializer(GameEvent.class, Registries.GAME_EVENT);
        registerSerializer(VillagerType.class, Registries.VILLAGER_TYPE);
        registerSerializer(VillagerProfession.class, Registries.VILLAGER_PROFESSION);
        registerSerializer(PoiType.class, Registries.POINT_OF_INTEREST_TYPE);
        registerContainerProvider(Map.class, MessageSerializers::createMapSerializer);
        registerContainerProvider(List.class, typeArguments -> createCollectionSerializer(typeArguments, Lists::newArrayListWithExpectedSize));
        registerContainerProvider(Optional.class, MessageSerializers::createOptionalSerializer);
        registerContainerProvider(Either.class, MessageSerializers::createEitherSerializer);
    }

    private static record MessageSerializerImpl<T>(FriendlyByteBuf.Writer<T> writer, FriendlyByteBuf.Reader<T> reader) implements MessageSerializer<T> {

        @Override
        public void write(FriendlyByteBuf buf, T instance) {
            this.writer.accept(buf, instance);
        }

        @Override
        public T read(FriendlyByteBuf buf) {
            return (T) this.reader.apply(buf);
        }
    }
}