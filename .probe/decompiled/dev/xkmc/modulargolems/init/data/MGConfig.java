package dev.xkmc.modulargolems.init.data;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;
import org.apache.commons.lang3.tuple.Pair;

public class MGConfig {

    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final MGConfig.Client CLIENT;

    public static final ForgeConfigSpec COMMON_SPEC;

    public static final MGConfig.Common COMMON;

    public static void init() {
        register(Type.CLIENT, CLIENT_SPEC);
        register(Type.COMMON, COMMON_SPEC);
    }

    private static void register(Type type, IConfigSpec<?> spec) {
        ModContainer mod = ModLoadingContext.get().getActiveContainer();
        String path = "l2_configs/" + mod.getModId() + "-" + type.extension() + ".toml";
        ModLoadingContext.get().registerConfig(type, spec, path);
    }

    static {
        Pair<MGConfig.Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(MGConfig.Client::new);
        CLIENT_SPEC = (ForgeConfigSpec) client.getRight();
        CLIENT = (MGConfig.Client) client.getLeft();
        Pair<MGConfig.Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(MGConfig.Common::new);
        COMMON_SPEC = (ForgeConfigSpec) specPair.getRight();
        COMMON = (MGConfig.Common) specPair.getLeft();
    }

    public static class Client {

        Client(ForgeConfigSpec.Builder builder) {
        }
    }

    public static class Common {

        public final ForgeConfigSpec.DoubleValue thorn;

        public final ForgeConfigSpec.DoubleValue fiery;

        public final ForgeConfigSpec.DoubleValue magicResistance;

        public final ForgeConfigSpec.DoubleValue explosionResistance;

        public final ForgeConfigSpec.DoubleValue compatTFHealing;

        public final ForgeConfigSpec.DoubleValue compatTFDamage;

        public final ForgeConfigSpec.IntValue manaMendingCost;

        public final ForgeConfigSpec.DoubleValue manaMendingVal;

        public final ForgeConfigSpec.DoubleValue manaBoostingDamage;

        public final ForgeConfigSpec.IntValue manaBoostingCost;

        public final ForgeConfigSpec.IntValue manaProductionVal;

        public final ForgeConfigSpec.DoubleValue manaBurstDamage;

        public final ForgeConfigSpec.IntValue manaBurstCost;

        public final ForgeConfigSpec.DoubleValue pixieAttackProb;

        public final ForgeConfigSpec.DoubleValue pixieCounterattackProb;

        public final ForgeConfigSpec.IntValue carminiteTime;

        public final ForgeConfigSpec.DoubleValue coating;

        public final ForgeConfigSpec.DoubleValue mechSpeed;

        public final ForgeConfigSpec.DoubleValue mechAttack;

        public final ForgeConfigSpec.IntValue mechMaxFuel;

        public final ForgeConfigSpec.BooleanValue barehandRetrieve;

        public final ForgeConfigSpec.BooleanValue strictInteract;

        public final ForgeConfigSpec.DoubleValue damageCap;

        public final ForgeConfigSpec.IntValue conduitCooldown;

        public final ForgeConfigSpec.IntValue conduitDamage;

        public final ForgeConfigSpec.DoubleValue conduitBoostAttack;

        public final ForgeConfigSpec.IntValue conduitBoostArmor;

        public final ForgeConfigSpec.IntValue conduitBoostToughness;

        public final ForgeConfigSpec.DoubleValue conduitBoostReduction;

        public final ForgeConfigSpec.DoubleValue conduitBoostSpeed;

        public final ForgeConfigSpec.IntValue thunderHeal;

        public final ForgeConfigSpec.IntValue teleportRadius;

        public final ForgeConfigSpec.IntValue teleportCooldown;

        public final ForgeConfigSpec.DoubleValue targetDamageBonus;

        public final ForgeConfigSpec.IntValue basePickupRange;

        public final ForgeConfigSpec.IntValue mendingXpCost;

        public final ForgeConfigSpec.IntValue summonDistance;

        public final ForgeConfigSpec.IntValue retrieveDistance;

        public final ForgeConfigSpec.IntValue retrieveRange;

        public final ForgeConfigSpec.BooleanValue ownerPickupOnly;

        public final ForgeConfigSpec.DoubleValue startFollowRadius;

        public final ForgeConfigSpec.DoubleValue stopWanderRadius;

        public final ForgeConfigSpec.DoubleValue maxWanderRadius;

        public final ForgeConfigSpec.DoubleValue riddenSpeedFactor;

        public final ForgeConfigSpec.IntValue targetResetTime;

        public final ForgeConfigSpec.DoubleValue targetResetNoMovementRange;

        public final ForgeConfigSpec.IntValue dogGolemSlot;

        public final ForgeConfigSpec.IntValue humanoidGolemSlot;

        public final ForgeConfigSpec.IntValue largeGolemSlot;

        public final ForgeConfigSpec.BooleanValue doEnemyAggro;

        Common(ForgeConfigSpec.Builder builder) {
            this.barehandRetrieve = builder.comment("Allow players to retrieve the golems by bare hand").define("barehandRetrieve", true);
            this.doEnemyAggro = builder.comment("Make mobs aggro to iron golem automatically aggro to modular golems").define("doEnemyAggro", true);
            this.summonDistance = builder.comment("Max distance to summon single golem").defineInRange("summonDistance", 64, 1, 1000);
            this.retrieveDistance = builder.comment("Max distance to retrieve single golem").defineInRange("retrieveDistance", 64, 1, 1000);
            this.retrieveRange = builder.comment("Max distance to retrieve all golems").defineInRange("retrieveRange", 20, 1, 1000);
            this.ownerPickupOnly = builder.comment("Only owner can pickup").define("ownerPickupOnly", true);
            this.strictInteract = builder.comment("When enabled, the following features will be disabled when player holds item in hand:").comment("- give item to golem or take items from golem by right click").comment("- order dog golem to seat").define("strictInteract", false);
            this.startFollowRadius = builder.comment("Max golem activity radius before following player in follow mode").defineInRange("startFollowRadius", 10.0, 1.0, 100.0);
            this.stopWanderRadius = builder.comment("Max golem activity radius before willing to go back to origin in wander mode").defineInRange("stopWanderRadius", 20.0, 1.0, 100.0);
            this.maxWanderRadius = builder.comment("Max golem activity radius before being teleported back").defineInRange("maxWanderRadius", 30.0, 1.0, 100.0);
            this.riddenSpeedFactor = builder.comment("Speed factor for dog golem when ridden by entities").defineInRange("riddenSpeedFactor", 0.8, 0.0, 2.0);
            this.targetResetTime = builder.comment("Target reset time after no movement, in ticks").defineInRange("targetResetTime", 600, 1, 10000);
            this.targetResetNoMovementRange = builder.comment("Distance considered as no movement").defineInRange("targetResetNoMovementRange", 0.5, 0.0, 10.0);
            this.largeGolemSlot = builder.comment("Default slots for large golem").defineInRange("largeGolemSlot", 4, 0, 100);
            this.humanoidGolemSlot = builder.comment("Default slots for humanoid golem").defineInRange("humanoidGolemSlot", 3, 0, 100);
            this.dogGolemSlot = builder.comment("Default slots for dog golem").defineInRange("dogGolemSlot", 2, 0, 100);
            builder.push("modifiers");
            this.thorn = builder.comment("Percentage damage reflection per level of thorn").defineInRange("thorn", 0.2, 0.0, 100.0);
            this.magicResistance = builder.comment("Percentage damage reduction per level of magic resistance").defineInRange("magicResistance", 0.2, 0.0, 1.0);
            this.explosionResistance = builder.comment("Percentage damage reduction per level of explosion resistance").defineInRange("explosionResistance", 0.2, 0.0, 1.0);
            this.damageCap = builder.comment("Damage Cap at max level").defineInRange("damageCap", 0.1, 0.0, 1.0);
            this.thunderHeal = builder.comment("Healing when thunder immune golems are striked").defineInRange("thunderHeal", 10, 1, 10000);
            builder.pop();
            builder.push("twilight forest compat");
            this.fiery = builder.comment("Percentage damage addition per level of fiery").defineInRange("fiery", 0.5, 0.0, 100.0);
            this.compatTFHealing = builder.comment("Percentage healing bonus per level of twilight healing").defineInRange("compatTFHealing", 0.5, 0.0, 100.0);
            this.compatTFDamage = builder.comment("Percentage damage bonus per level of twilight damage").defineInRange("compatTFDamage", 0.2, 0.0, 100.0);
            this.carminiteTime = builder.comment("Time for the golem to be invincible (in ticks) per level of carminite").defineInRange("carminiteTime", 20, 1, 100000);
            builder.pop();
            builder.push("botania compat");
            this.manaMendingCost = builder.comment("Mana mending cost per HP").defineInRange("mendingManaCost", 200, 1, 50000);
            this.manaMendingVal = builder.comment("Mana mending value per level").defineInRange("manaMendingVal", 4.0, 0.1, 100.0);
            this.manaBoostingDamage = builder.comment("Percentage damage bonus per level of mana boost").defineInRange("manaBoostingDamage", 0.2, 0.0, 100.0);
            this.manaBoostingCost = builder.comment("Mana boosting cost per level (of 1 hit)").defineInRange("manaBoostingCost", 500, 0, 100000);
            this.manaProductionVal = builder.comment("Mana production value per level").defineInRange("manaProductionVal", 20, 0, 100000);
            this.manaBurstDamage = builder.comment("Damage of magic burst as percentage of attack damage").defineInRange("manaBurstDamage", 0.25, 0.0, 1.0);
            this.manaBurstCost = builder.comment("Mana burst cost per level").defineInRange("manaBurstCost", 2000, 0, 100000);
            this.pixieAttackProb = builder.comment("Probability of summoning a pixie when attacking").defineInRange("pixieAttackProb", 0.05, 0.0, 1.0);
            this.pixieCounterattackProb = builder.comment("Probability of summoning a pixie when attacked").defineInRange("pixiecounterattackProb", 0.25, 0.0, 1.0);
            builder.pop();
            builder.push("create compat");
            this.coating = builder.comment("Damage absorbed per level of coating").defineInRange("coating", 1.0, 0.0, 10000.0);
            this.mechSpeed = builder.comment("Speed boost per level of Mechanical Mobility").defineInRange("mechSpeed", 0.2, 0.0, 1.0);
            this.mechAttack = builder.comment("Attack boost per level of Mechanical Force").defineInRange("mechAttack", 0.2, 0.0, 1.0);
            this.mechMaxFuel = builder.comment("Maximum number of fuel item that can be added at the same time").defineInRange("mechMaxFuel", 3, 1, 364);
            builder.pop();
            builder.push("l2complements compat");
            this.conduitCooldown = builder.comment("Conduit cooldown").defineInRange("conduitCooldown", 120, 0, 10000);
            this.conduitDamage = builder.comment("Conduit damage").defineInRange("conduitDamage", 2, 0, 10000);
            this.conduitBoostAttack = builder.comment("Conduit modifier boosts attack (percentage) when golem is in water").defineInRange("conduitBoostAttack", 0.2, 0.0, 100.0);
            this.conduitBoostArmor = builder.comment("Conduit modifier boosts armor when golem is in water").defineInRange("conduitBoostArmor", 5, 0, 100);
            this.conduitBoostToughness = builder.comment("Conduit modifier boosts toughness when golem is in water").defineInRange("conduitBoostToughness", 2, 0, 100);
            this.conduitBoostSpeed = builder.comment("Conduit modifier boosts speed (percentage) when golem is in water").defineInRange("conduitBoostSpeed", 0.2, 0.0, 100.0);
            this.conduitBoostReduction = builder.comment("Conduit modifier reduce damage taken when golem is in water (multiplicative)").defineInRange("conduitBoostReduction", 0.2, 0.0, 100.0);
            this.teleportRadius = builder.comment("Teleport max radius").defineInRange("teleportRadius", 6, 1, 32);
            this.teleportCooldown = builder.comment("Teleport cooldown in ticks for avoiding physical damage").defineInRange("teleportCooldown", 40, 1, 10000);
            this.targetDamageBonus = builder.comment("Damage bonus for attacking specific type of enemy").defineInRange("targetDamageBonus", 0.5, 0.0, 100.0);
            this.basePickupRange = builder.comment("Pickup range per level for pickup upgrade").defineInRange("basePickupRange", 6, 1, 16);
            this.mendingXpCost = builder.comment("Mending Xp Cost per health point").defineInRange("mendingXpCost", 2, 1, 10000);
            builder.pop();
        }
    }
}