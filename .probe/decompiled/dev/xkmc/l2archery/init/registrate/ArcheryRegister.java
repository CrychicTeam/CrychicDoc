package dev.xkmc.l2archery.init.registrate;

import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2archery.content.crafting.BowRecipe;
import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import dev.xkmc.l2archery.content.entity.GenericArrowRenderer;
import dev.xkmc.l2archery.content.stats.BowArrowStatType;
import dev.xkmc.l2archery.content.stats.StatType;
import dev.xkmc.l2archery.content.upgrade.Upgrade;
import dev.xkmc.l2archery.init.L2Archery;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.serial.recipe.AbstractShapedRecipe;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.ForgeRegistries;

public class ArcheryRegister {

    public static final L2Registrate.RegistryInstance<BowArrowStatType> STAT_TYPE = L2Archery.REGISTRATE.newRegistry("stat_type", BowArrowStatType.class);

    public static final L2Registrate.RegistryInstance<Upgrade> UPGRADE = L2Archery.REGISTRATE.newRegistry("upgrade", Upgrade.class);

    public static final RegistryEntry<BowArrowStatType> DAMAGE = regStat("damage", StatType.COMMON, 0.0);

    public static final RegistryEntry<BowArrowStatType> PUNCH = regStat("punch", StatType.COMMON, 0.0);

    public static final RegistryEntry<BowArrowStatType> SPEED = regStat("speed", StatType.BOW, 3.0);

    public static final RegistryEntry<BowArrowStatType> PULL_TIME = regStat("pull_time", StatType.BOW, 20.0);

    public static final RegistryEntry<BowArrowStatType> FOV_TIME = regStat("fov_time", StatType.BOW, 20.0);

    public static final RegistryEntry<BowArrowStatType> FOV = regStat("max_fov", StatType.BOW, 0.15);

    public static final EntityEntry<GenericArrowEntity> ET_ARROW = L2Archery.REGISTRATE.entity("generic_arrow", GenericArrowEntity::new, MobCategory.MISC).properties(e -> e.sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).setShouldReceiveVelocityUpdates(true)).renderer(() -> GenericArrowRenderer::new).defaultLang().register();

    public static final RegistryEntry<AbstractShapedRecipe.Serializer<BowRecipe>> BOW_RECIPE = L2Archery.REGISTRATE.simple("bow_craft", ForgeRegistries.Keys.RECIPE_SERIALIZERS, () -> new AbstractShapedRecipe.Serializer<>(BowRecipe::new));

    public static RegistryEntry<BowArrowStatType> regStat(String id, StatType type, double def) {
        return L2Archery.REGISTRATE.generic(STAT_TYPE, id, () -> new BowArrowStatType(type, def)).defaultLang().register();
    }

    public static void register() {
    }
}