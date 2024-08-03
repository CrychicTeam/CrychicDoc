package net.mehvahdjukaar.supplementaries.reg;

import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedFallingBlockEntity;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.entities.BombEntity;
import net.mehvahdjukaar.supplementaries.common.entities.FallingAshEntity;
import net.mehvahdjukaar.supplementaries.common.entities.FallingUrnEntity;
import net.mehvahdjukaar.supplementaries.common.entities.HatStandEntity;
import net.mehvahdjukaar.supplementaries.common.entities.PearlMarker;
import net.mehvahdjukaar.supplementaries.common.entities.RedMerchantEntity;
import net.mehvahdjukaar.supplementaries.common.entities.RopeArrowEntity;
import net.mehvahdjukaar.supplementaries.common.entities.SlingshotProjectileEntity;
import net.mehvahdjukaar.supplementaries.common.entities.ThrowableBrickEntity;
import net.mehvahdjukaar.supplementaries.common.entities.dispenser_minecart.DispenserMinecartEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {

    public static final Supplier<EntityType<PearlMarker>> PEARL_MARKER = regEntity("pearl_marker", PearlMarker::new, MobCategory.MISC, 0.999F, 0.999F, 4, false, -1);

    public static final Supplier<EntityType<DispenserMinecartEntity>> DISPENSER_MINECART = regEntity("dispenser_minecart", () -> EntityType.Builder.of(DispenserMinecartEntity::new, MobCategory.MISC).sized(0.98F, 0.7F).clientTrackingRange(8));

    public static final Supplier<EntityType<HatStandEntity>> HAT_STAND = regEntity("hat_stand", HatStandEntity::new, MobCategory.MISC, 0.625F, 0.75F, 10, false, 3);

    public static final Supplier<EntityType<RedMerchantEntity>> RED_MERCHANT = regEntity("red_merchant", RedMerchantEntity::new, MobCategory.CREATURE, 0.6F, 1.95F, 10, true, 3);

    public static final Supplier<EntityType<FallingUrnEntity>> FALLING_URN = regEntity("falling_urn", () -> EntityType.Builder.of(FallingUrnEntity::new, MobCategory.MISC).sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(20));

    public static final Supplier<EntityType<FallingAshEntity>> FALLING_ASH = regEntity("falling_ash", () -> EntityType.Builder.of(FallingAshEntity::new, MobCategory.MISC).sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(20));

    public static final Supplier<EntityType<ImprovedFallingBlockEntity>> FALLING_SACK = regEntity("falling_sack", () -> EntityType.Builder.of(ImprovedFallingBlockEntity::new, MobCategory.MISC).sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(20));

    public static final Supplier<EntityType<ThrowableBrickEntity>> THROWABLE_BRICK = regEntity("brick_projectile", () -> EntityType.Builder.of(ThrowableBrickEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(1));

    public static final Supplier<EntityType<BombEntity>> BOMB = regEntity("bomb", () -> EntityType.Builder.of(BombEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(8).updateInterval(1));

    public static final Supplier<EntityType<RopeArrowEntity>> ROPE_ARROW = regEntity("rope_arrow", () -> EntityType.Builder.of(RopeArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(1));

    public static final Supplier<EntityType<SlingshotProjectileEntity>> SLINGSHOT_PROJECTILE = regEntity("slingshot_projectile", () -> EntityType.Builder.of(SlingshotProjectileEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(1));

    public static void init() {
        RegHelper.addAttributeRegistration(ModEntities::registerEntityAttributes);
    }

    public static void registerEntityAttributes(RegHelper.AttributeEvent event) {
        event.register((EntityType<? extends LivingEntity>) RED_MERCHANT.get(), Mob.createMobAttributes());
        event.register((EntityType<? extends LivingEntity>) HAT_STAND.get(), LivingEntity.createLivingAttributes());
    }

    public static <T extends Entity> Supplier<EntityType<T>> regEntity(String name, Supplier<EntityType.Builder<T>> builder) {
        return RegHelper.registerEntityType(Supplementaries.res(name), () -> ((EntityType.Builder) builder.get()).build(name));
    }

    public static <T extends Entity> Supplier<EntityType<T>> regEntity(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, int clientTrackingRange, boolean velocityUpdates, int updateInterval) {
        return RegHelper.registerEntityType(Supplementaries.res(name), () -> PlatHelper.newEntityType(name, factory, category, width, height, clientTrackingRange, velocityUpdates, updateInterval));
    }
}