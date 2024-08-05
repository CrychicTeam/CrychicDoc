package noppes.npcs.api.wrapper;

import java.lang.reflect.Field;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.entity.EntityProjectile;
import noppes.npcs.shared.common.util.LogWriter;

public class WrapperEntityData implements ICapabilityProvider {

    private static final Field capField;

    public static Capability<WrapperEntityData> ENTITYDATA_CAPABILITY;

    private LazyOptional<WrapperEntityData> instance = LazyOptional.of(() -> this);

    public IEntity base;

    private static WrapperEntityData backup;

    private static final ResourceLocation key;

    public WrapperEntityData(IEntity base) {
        this.base = base;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
        return capability == ENTITYDATA_CAPABILITY ? this.instance.cast() : LazyOptional.empty();
    }

    public static IEntity get(Entity entity) {
        if (entity != null && entity.position() != Vec3.ZERO) {
            try {
                CapabilityDispatcher dispatcher = (CapabilityDispatcher) capField.get(entity);
                if (dispatcher == null) {
                    LogWriter.warn("Unable to get EntityData for " + entity);
                    return getData(entity).base;
                } else {
                    WrapperEntityData data = dispatcher.getCapability(ENTITYDATA_CAPABILITY, null).orElse(backup);
                    if (data != null && data != backup) {
                        return data.base;
                    } else {
                        LogWriter.warn("Unable to get EntityData for " + entity);
                        return getData(entity).base;
                    }
                }
            } catch (IllegalAccessException var3) {
                var3.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static void register(AttachCapabilitiesEvent<Entity> event) {
        event.addCapability(key, getData(event.getObject()));
    }

    private static WrapperEntityData getData(Entity entity) {
        if (entity == null || entity.level() == null || entity.level().isClientSide) {
            return null;
        } else if (entity instanceof ServerPlayer) {
            return new WrapperEntityData(new PlayerWrapper((ServerPlayer) entity));
        } else if (PixelmonHelper.isPixelmon(entity)) {
            return new WrapperEntityData(new PixelmonWrapper((AbstractHorse) entity));
        } else if (entity instanceof Villager) {
            return new WrapperEntityData(new VillagerWrapper((Villager) entity));
        } else if (entity instanceof Animal) {
            return new WrapperEntityData(new AnimalWrapper((Animal) entity));
        } else if (entity instanceof Monster) {
            return new WrapperEntityData(new MonsterWrapper((Monster) entity));
        } else if (entity instanceof Mob) {
            return new WrapperEntityData(new EntityLivingWrapper((Mob) entity));
        } else if (entity instanceof LivingEntity) {
            return new WrapperEntityData(new EntityLivingBaseWrapper((LivingEntity) entity));
        } else if (entity instanceof ItemEntity) {
            return new WrapperEntityData(new EntityItemWrapper((ItemEntity) entity));
        } else if (entity instanceof EntityProjectile) {
            return new WrapperEntityData(new ProjectileWrapper((EntityProjectile) entity));
        } else if (entity instanceof ThrowableProjectile) {
            return new WrapperEntityData(new ThrowableWrapper((ThrowableProjectile) entity));
        } else {
            return entity instanceof AbstractArrow ? new WrapperEntityData(new ArrowWrapper((AbstractArrow) entity)) : new WrapperEntityData(new EntityWrapper<>(entity));
        }
    }

    static {
        Field f = null;
        try {
            f = CapabilityProvider.class.getDeclaredField("capabilities");
            f.setAccessible(true);
        } catch (NoSuchFieldException var2) {
            var2.printStackTrace();
        }
        capField = f;
        ENTITYDATA_CAPABILITY = CapabilityManager.get(new CapabilityToken<WrapperEntityData>() {
        });
        backup = new WrapperEntityData(null);
        key = new ResourceLocation("customnpcs", "entitydata");
    }
}