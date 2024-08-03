package dev.xkmc.modulargolems.content.core;

import com.tterrag.registrate.util.entry.EntityEntry;
import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class GolemType<T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> extends NamedEntry<GolemType<?, ?>> {

    private static final HashMap<ResourceLocation, GolemType<?, ?>> ENTITY_TYPE_TO_GOLEM_TYPE = new HashMap();

    public static final HashMap<ResourceLocation, GolemHolder<?, ?>> GOLEM_TYPE_TO_ITEM = new HashMap();

    public static final HashMap<ResourceLocation, Supplier<ModelProvider<?, ?>>> GOLEM_TYPE_TO_MODEL = new HashMap();

    private final EntityEntry<T> type;

    private final Supplier<P[]> list;

    private final P body;

    public static <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> GolemType<T, P> getGolemType(EntityType<T> type) {
        return (GolemType<T, P>) Wrappers.cast((GolemType) ENTITY_TYPE_TO_GOLEM_TYPE.get(ForgeRegistries.ENTITY_TYPES.getKey(type)));
    }

    public static <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> GolemHolder<T, P> getGolemHolder(GolemType<T, ?> type) {
        return (GolemHolder<T, P>) Wrappers.cast((GolemHolder) GOLEM_TYPE_TO_ITEM.get(type.getRegistryName()));
    }

    public static <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> GolemHolder<T, P> getGolemHolder(EntityType<T> type) {
        return getGolemHolder(getGolemType(type));
    }

    public GolemType(EntityEntry<T> type, Supplier<P[]> list, P body, Supplier<ModelProvider<T, P>> model) {
        super(GolemTypes.TYPES);
        this.type = type;
        this.list = list;
        this.body = body;
        ENTITY_TYPE_TO_GOLEM_TYPE.put(type.getId(), this);
        GOLEM_TYPE_TO_MODEL.put(type.getId(), (Supplier) Wrappers.cast(model));
    }

    public T create(Level level) {
        return (T) Objects.requireNonNull((AbstractGolemEntity) ((EntityType) this.type.get()).create(level));
    }

    public T create(ServerLevel level, CompoundTag tag) {
        return (T) Wrappers.cast((Entity) EntityType.create(tag, level).get());
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public T createForDisplay(CompoundTag tag) {
        Entity ans = (Entity) EntityType.create(tag, Proxy.getClientWorld()).orElse(null);
        if (ans == null) {
            return null;
        } else {
            T golem = (T) Wrappers.cast(ans);
            if (tag.contains("Attributes", 9)) {
                golem.m_21204_().load(tag.getList("Attributes", 10));
            }
            if (tag.contains("Health", 5)) {
                golem.m_21153_(tag.getFloat("Health"));
            }
            golem.f_20885_ = 0.0F;
            golem.f_20886_ = 0.0F;
            golem.f_20883_ = 0.0F;
            golem.f_20884_ = 0.0F;
            golem.f_19860_ = 0.0F;
            golem.m_146926_(0.0F);
            return golem;
        }
    }

    public EntityType<?> type() {
        return (EntityType<?>) this.type.get();
    }

    public P[] values() {
        return (P[]) ((IGolemPart[]) this.list.get());
    }

    public P getBodyPart() {
        return this.body;
    }
}