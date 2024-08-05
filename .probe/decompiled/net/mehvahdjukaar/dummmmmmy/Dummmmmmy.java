package net.mehvahdjukaar.dummmmmmy;

import java.util.function.Supplier;
import net.mehvahdjukaar.dummmmmmy.common.TargetDummyEntity;
import net.mehvahdjukaar.dummmmmmy.common.TargetDummyItem;
import net.mehvahdjukaar.dummmmmmy.configs.ClientConfigs;
import net.mehvahdjukaar.dummmmmmy.configs.CommonConfigs;
import net.mehvahdjukaar.dummmmmmy.network.NetworkHandler;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Dummmmmmy {

    public static final String MOD_ID = "dummmmmmy";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final String TARGET_DUMMY_NAME = "target_dummy";

    public static final Supplier<EntityType<TargetDummyEntity>> TARGET_DUMMY = RegHelper.registerEntityType(res("target_dummy"), () -> EntityType.Builder.of(TargetDummyEntity::new, MobCategory.MISC).sized(0.6F, 2.0F).build("target_dummy"));

    public static final Supplier<Item> DUMMY_ITEM = RegHelper.registerItem(res("target_dummy"), () -> new TargetDummyItem(new Item.Properties().stacksTo(16)));

    public static final Supplier<SimpleParticleType> NUMBER_PARTICLE = RegHelper.registerParticle(res("number"));

    public static final TagKey<DamageType> IS_THORN = TagKey.create(Registries.DAMAGE_TYPE, res("is_thorn"));

    public static final TagKey<DamageType> IS_FIRE = TagKey.create(Registries.DAMAGE_TYPE, res("is_fire"));

    public static final TagKey<DamageType> IS_EXPLOSION = TagKey.create(Registries.DAMAGE_TYPE, res("is_explosion"));

    public static final TagKey<DamageType> IS_WITHER = TagKey.create(Registries.DAMAGE_TYPE, res("is_wither"));

    public static final TagKey<DamageType> IS_COLD = TagKey.create(Registries.DAMAGE_TYPE, res("is_cold"));

    public static final ResourceLocation TRUE_DAMAGE = res("true");

    public static final ResourceLocation CRITICAL_DAMAGE = res("critical");

    public static ResourceLocation res(String name) {
        return new ResourceLocation("dummmmmmy", name);
    }

    public static void init() {
        if (PlatHelper.getPhysicalSide().isClient()) {
            DummmmmmyClient.init();
            ClientConfigs.init();
        }
        PlatHelper.addCommonSetup(Dummmmmmy::setup);
        CommonConfigs.init();
        RegHelper.addAttributeRegistration(Dummmmmmy::registerEntityAttributes);
        RegHelper.addItemsToTabsRegistration(Dummmmmmy::registerItemsToTab);
    }

    public static void setup() {
        NetworkHandler.registerMessages();
        DispenserBlock.registerBehavior((ItemLike) DUMMY_ITEM.get(), new Dummmmmmy.SpawnDummyBehavior());
    }

    private static void registerItemsToTab(RegHelper.ItemToTabEvent event) {
        event.addBefore(CreativeModeTabs.COMBAT, i -> i.is(Items.TOTEM_OF_UNDYING), (ItemLike) DUMMY_ITEM.get());
    }

    private static void registerEntityAttributes(RegHelper.AttributeEvent event) {
        event.register((EntityType<? extends LivingEntity>) TARGET_DUMMY.get(), TargetDummyEntity.makeAttributes());
    }

    private static class SpawnDummyBehavior implements DispenseItemBehavior {

        @Override
        public ItemStack dispense(BlockSource dispenser, ItemStack itemStack) {
            Level world = dispenser.getLevel();
            Direction direction = (Direction) dispenser.getBlockState().m_61143_(DispenserBlock.FACING);
            BlockPos pos = dispenser.getPos().relative(direction);
            TargetDummyEntity dummy = new TargetDummyEntity(world);
            float f = direction.toYRot();
            dummy.m_7678_((double) pos.m_123341_() + 0.5, (double) pos.m_123342_(), (double) pos.m_123343_() + 0.5, f, 0.0F);
            world.m_7967_(dummy);
            itemStack.shrink(1);
            world.m_46796_(1000, dispenser.getPos(), 0);
            return itemStack;
        }
    }
}