package dev.xkmc.l2artifacts.init.data;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.config.LinearFuncConfig;
import dev.xkmc.l2artifacts.content.config.SlotStatConfig;
import dev.xkmc.l2artifacts.content.config.StatTypeConfig;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.LinearFuncHandle;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;

public class ConfigGen extends ConfigDataProvider {

    public static final ResourceLocation HEALTH_ADD = regLang("health_add", "Health +");

    public static final ResourceLocation ARMOR_ADD = regLang("armor_add", "Armor +");

    public static final ResourceLocation TOUGH_ADD = regLang("tough_add", "Armor Toughness +");

    public static final ResourceLocation ATK_ADD = regLang("attack_add", "Melee Damage +");

    public static final ResourceLocation REACH_ADD = regLang("reach_add", "Attack Range +");

    public static final ResourceLocation CR_ADD = regLang("crit_rate_add", "Crit Rate +");

    public static final ResourceLocation CD_ADD = regLang("crit_damage_add", "Crit Damage +");

    public static final ResourceLocation ATK_MULT = regLang("attack_mult", "Melee Damage +%%");

    public static final ResourceLocation SPEED_MULT = regLang("speed_mult", "Speed +%%");

    public static final ResourceLocation ATK_SPEED_MULT = regLang("attack_speed_mult", "Attack Speed +%%");

    public static final ResourceLocation BOW_ADD = regLang("bow_strength_add", "Bow Strength +%%");

    public static final ResourceLocation EXPLOSION_ADD = regLang("explosion_add", "Explosion Damage +%%");

    public static final ResourceLocation MAGIC_ADD = regLang("magic_add", "Magic Damage +%%");

    private static ResourceLocation regLang(String id, String name) {
        L2Artifacts.REGISTRATE.addRawLang("stat_type.l2artifacts." + id, name);
        return new ResourceLocation("l2artifacts", id);
    }

    public static void register() {
    }

    public ConfigGen(DataGenerator generator) {
        super(generator, "Artifact Config");
    }

    @Override
    public void add(ConfigDataProvider.Collector map) {
        regStat(map, HEALTH_ADD, Attributes.MAX_HEALTH, AttributeModifier.Operation.ADDITION, false, 2.0);
        regStat(map, ARMOR_ADD, Attributes.ARMOR, AttributeModifier.Operation.ADDITION, false, 2.0);
        regStat(map, TOUGH_ADD, Attributes.ARMOR_TOUGHNESS, AttributeModifier.Operation.ADDITION, false, 1.0);
        regStat(map, ATK_ADD, Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.ADDITION, false, 2.0);
        regStat(map, REACH_ADD, ForgeMod.ENTITY_REACH.get(), AttributeModifier.Operation.ADDITION, false, 0.1);
        regStat(map, CR_ADD, (Attribute) L2DamageTracker.CRIT_RATE.get(), AttributeModifier.Operation.ADDITION, true, 0.05);
        regStat(map, CD_ADD, (Attribute) L2DamageTracker.CRIT_DMG.get(), AttributeModifier.Operation.ADDITION, true, 0.1);
        regStat(map, ATK_MULT, Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.MULTIPLY_BASE, true, 0.1);
        regStat(map, SPEED_MULT, Attributes.MOVEMENT_SPEED, AttributeModifier.Operation.MULTIPLY_BASE, true, 0.05);
        regStat(map, ATK_SPEED_MULT, Attributes.ATTACK_SPEED, AttributeModifier.Operation.MULTIPLY_BASE, true, 0.05);
        regStat(map, BOW_ADD, (Attribute) L2DamageTracker.BOW_STRENGTH.get(), AttributeModifier.Operation.ADDITION, true, 0.1);
        regStat(map, EXPLOSION_ADD, (Attribute) L2DamageTracker.EXPLOSION_FACTOR.get(), AttributeModifier.Operation.ADDITION, true, 0.2);
        regStat(map, MAGIC_ADD, (Attribute) L2DamageTracker.MAGIC_FACTOR.get(), AttributeModifier.Operation.ADDITION, true, 0.15);
        ArrayList<ResourceLocation> all = new ArrayList();
        all.add(HEALTH_ADD);
        all.add(ARMOR_ADD);
        all.add(TOUGH_ADD);
        all.add(ATK_ADD);
        all.add(ATK_MULT);
        all.add(CR_ADD);
        all.add(CD_ADD);
        all.add(REACH_ADD);
        all.add(ATK_SPEED_MULT);
        all.add(SPEED_MULT);
        all.add(BOW_ADD);
        all.add(EXPLOSION_ADD);
        all.add(MAGIC_ADD);
        ArrayList<ResourceLocation> list = new ArrayList();
        list.add(HEALTH_ADD);
        list.add(ARMOR_ADD);
        list.add(SPEED_MULT);
        list.add(CR_ADD);
        list.add(TOUGH_ADD);
        list.add(EXPLOSION_ADD);
        list.add(MAGIC_ADD);
        addSlotStat(map, (ArtifactSlot) ArtifactTypeRegistry.SLOT_BODY.get(), list, all);
        list = new ArrayList();
        list.add(ATK_ADD);
        list.add(ATK_MULT);
        list.add(BOW_ADD);
        list.add(CD_ADD);
        list.add(REACH_ADD);
        list.add(EXPLOSION_ADD);
        list.add(MAGIC_ADD);
        addSlotStat(map, (ArtifactSlot) ArtifactTypeRegistry.SLOT_BRACELET.get(), list, all);
        list = new ArrayList();
        list.add(HEALTH_ADD);
        list.add(ARMOR_ADD);
        list.add(ATK_ADD);
        list.add(ATK_MULT);
        list.add(BOW_ADD);
        list.add(EXPLOSION_ADD);
        list.add(MAGIC_ADD);
        list.add(CR_ADD);
        list.add(CD_ADD);
        addSlotStat(map, (ArtifactSlot) ArtifactTypeRegistry.SLOT_NECKLACE.get(), list, all);
        list = new ArrayList();
        list.add(HEALTH_ADD);
        list.add(ARMOR_ADD);
        list.add(ATK_ADD);
        list.add(ATK_MULT);
        list.add(BOW_ADD);
        list.add(EXPLOSION_ADD);
        list.add(MAGIC_ADD);
        list.add(ATK_SPEED_MULT);
        list.add(SPEED_MULT);
        addSlotStat(map, (ArtifactSlot) ArtifactTypeRegistry.SLOT_BELT.get(), list, all);
        list = new ArrayList();
        list.add(HEALTH_ADD);
        list.add(ARMOR_ADD);
        list.add(ATK_ADD);
        list.add(ATK_MULT);
        list.add(BOW_ADD);
        list.add(EXPLOSION_ADD);
        list.add(MAGIC_ADD);
        list.add(TOUGH_ADD);
        list.add(REACH_ADD);
        list.add(CR_ADD);
        list.add(CD_ADD);
        list.add(ATK_SPEED_MULT);
        list.add(SPEED_MULT);
        addSlotStat(map, (ArtifactSlot) ArtifactTypeRegistry.SLOT_HEAD.get(), list, all);
        for (SetEntry<?> set : L2Artifacts.REGISTRATE.SET_LIST) {
            addArtifactSet(map, (ArtifactSet) set.get(), set.builder);
        }
        for (ResourceLocation key : L2Artifacts.REGISTRATE.LINEAR_LIST.keySet()) {
            LinearFuncConfig config = new LinearFuncConfig();
            for (LinearFuncEntry entry : L2Artifacts.REGISTRATE.LINEAR_LIST.get(key)) {
                config.map.put((LinearFuncHandle) entry.get(), new LinearFuncConfig.Entry(entry.base, entry.slope));
            }
            map.add(NetworkManager.LINEAR, key, config);
        }
    }

    public static void addSlotStat(ConfigDataProvider.Collector map, ArtifactSlot slot, ArrayList<ResourceLocation> main, ArrayList<ResourceLocation> sub) {
        SlotStatConfig config = new SlotStatConfig();
        ResourceLocation rl = (ResourceLocation) Objects.requireNonNull(slot.getRegistryName());
        config.available_main_stats.put(slot, main);
        config.available_sub_stats.put(slot, sub);
        map.add(NetworkManager.SLOT_STATS, rl, config);
    }

    private static void regStat(ConfigDataProvider.Collector map, ResourceLocation id, Attribute attr, AttributeModifier.Operation op, boolean perc, double base) {
        map.add(NetworkManager.STAT_TYPES, id, genEntry(attr, op, perc, base, 0.2, 2.0));
    }

    private static void addArtifactSet(ConfigDataProvider.Collector map, ArtifactSet set, Consumer<ArtifactSetConfig.SetBuilder> builder) {
        ResourceLocation rl = (ResourceLocation) Objects.requireNonNull(set.getRegistryName());
        map.add(NetworkManager.ARTIFACT_SETS, rl, ArtifactSetConfig.construct(set, builder));
    }

    private static StatTypeConfig genEntry(Attribute attr, AttributeModifier.Operation op, boolean perc, double base, double sub, double factor) {
        StatTypeConfig entry = new StatTypeConfig();
        entry.base = base;
        entry.base_low = 1.0;
        entry.base_high = factor;
        entry.main_low = sub;
        entry.main_high = sub * factor;
        entry.sub_low = sub;
        entry.sub_high = sub * factor;
        entry.attr = attr;
        entry.op = op;
        entry.usePercent = perc;
        return entry;
    }
}