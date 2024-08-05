package com.simibubi.create.infrastructure.config;

import com.simibubi.create.content.contraptions.ContraptionMovementSetting;
import com.simibubi.create.foundation.config.ConfigBase;

public class CKinetics extends ConfigBase {

    public final ConfigBase.ConfigBool disableStress = this.b(false, "disableStress", new String[] { CKinetics.Comments.disableStress });

    public final ConfigBase.ConfigInt maxBeltLength = this.i(20, 5, "maxBeltLength", new String[] { CKinetics.Comments.maxBeltLength });

    public final ConfigBase.ConfigInt crushingDamage = this.i(4, 0, "crushingDamage", new String[] { CKinetics.Comments.crushingDamage });

    public final ConfigBase.ConfigInt maxRotationSpeed = this.i(256, 64, "maxRotationSpeed", new String[] { CKinetics.Comments.rpm, CKinetics.Comments.maxRotationSpeed });

    public final ConfigBase.ConfigEnum<CKinetics.DeployerAggroSetting> ignoreDeployerAttacks = this.e(CKinetics.DeployerAggroSetting.CREEPERS, "ignoreDeployerAttacks", new String[] { CKinetics.Comments.ignoreDeployerAttacks });

    public final ConfigBase.ConfigInt kineticValidationFrequency = this.i(60, 5, "kineticValidationFrequency", new String[] { CKinetics.Comments.kineticValidationFrequency });

    public final ConfigBase.ConfigFloat crankHungerMultiplier = this.f(0.01F, 0.0F, 1.0F, "crankHungerMultiplier", new String[] { CKinetics.Comments.crankHungerMultiplier });

    public final ConfigBase.ConfigInt minimumWindmillSails = this.i(8, 0, "minimumWindmillSails", new String[] { CKinetics.Comments.minimumWindmillSails });

    public final ConfigBase.ConfigInt windmillSailsPerRPM = this.i(8, 1, "windmillSailsPerRPM", new String[] { CKinetics.Comments.windmillSailsPerRPM });

    public final ConfigBase.ConfigInt maxEjectorDistance = this.i(32, 0, "maxEjectorDistance", new String[] { CKinetics.Comments.maxEjectorDistance });

    public final ConfigBase.ConfigInt ejectorScanInterval = this.i(120, 10, "ejectorScanInterval", new String[] { CKinetics.Comments.ejectorScanInterval });

    public final ConfigBase.ConfigGroup fan = this.group(1, "encasedFan", new String[] { "Encased Fan" });

    public final ConfigBase.ConfigInt fanPushDistance = this.i(20, 5, "fanPushDistance", new String[] { CKinetics.Comments.fanPushDistance });

    public final ConfigBase.ConfigInt fanPullDistance = this.i(20, 5, "fanPullDistance", new String[] { CKinetics.Comments.fanPullDistance });

    public final ConfigBase.ConfigInt fanBlockCheckRate = this.i(30, 10, "fanBlockCheckRate", new String[] { CKinetics.Comments.fanBlockCheckRate });

    public final ConfigBase.ConfigInt fanRotationArgmax = this.i(256, 64, "fanRotationArgmax", new String[] { CKinetics.Comments.rpm, CKinetics.Comments.fanRotationArgmax });

    public final ConfigBase.ConfigInt fanProcessingTime = this.i(150, 0, "fanProcessingTime", new String[] { CKinetics.Comments.fanProcessingTime });

    public final ConfigBase.ConfigGroup contraptions = this.group(1, "contraptions", new String[] { "Moving Contraptions" });

    public final ConfigBase.ConfigInt maxBlocksMoved = this.i(2048, 1, "maxBlocksMoved", new String[] { CKinetics.Comments.maxBlocksMoved });

    public final ConfigBase.ConfigInt maxDataSize = this.i(2000000, 0, "maxDataSize", new String[] { CKinetics.Comments.bytes, CKinetics.Comments.maxDataDisable, CKinetics.Comments.maxDataSize, CKinetics.Comments.maxDataSize2 });

    public final ConfigBase.ConfigInt maxChassisRange = this.i(16, 1, "maxChassisRange", new String[] { CKinetics.Comments.maxChassisRange });

    public final ConfigBase.ConfigInt maxPistonPoles = this.i(64, 1, "maxPistonPoles", new String[] { CKinetics.Comments.maxPistonPoles });

    public final ConfigBase.ConfigInt maxRopeLength = this.i(256, 1, "maxRopeLength", new String[] { CKinetics.Comments.maxRopeLength });

    public final ConfigBase.ConfigInt maxCartCouplingLength = this.i(32, 1, "maxCartCouplingLength", new String[] { CKinetics.Comments.maxCartCouplingLength });

    public final ConfigBase.ConfigInt rollerFillDepth = this.i(12, 1, "rollerFillDepth", new String[] { CKinetics.Comments.rollerFillDepth });

    public final ConfigBase.ConfigBool survivalContraptionPickup = this.b(true, "survivalContraptionPickup", new String[] { CKinetics.Comments.survivalContraptionPickup });

    public final ConfigBase.ConfigEnum<ContraptionMovementSetting> spawnerMovement = this.e(ContraptionMovementSetting.NO_PICKUP, "movableSpawners", new String[] { CKinetics.Comments.spawnerMovement });

    public final ConfigBase.ConfigEnum<ContraptionMovementSetting> amethystMovement = this.e(ContraptionMovementSetting.NO_PICKUP, "amethystMovement", new String[] { CKinetics.Comments.amethystMovement });

    public final ConfigBase.ConfigEnum<ContraptionMovementSetting> obsidianMovement = this.e(ContraptionMovementSetting.UNMOVABLE, "movableObsidian", new String[] { CKinetics.Comments.obsidianMovement });

    public final ConfigBase.ConfigEnum<ContraptionMovementSetting> reinforcedDeepslateMovement = this.e(ContraptionMovementSetting.UNMOVABLE, "movableReinforcedDeepslate", new String[] { CKinetics.Comments.reinforcedDeepslateMovement });

    public final ConfigBase.ConfigBool moveItemsToStorage = this.b(true, "moveItemsToStorage", new String[] { CKinetics.Comments.moveItemsToStorage });

    public final ConfigBase.ConfigBool harvestPartiallyGrown = this.b(false, "harvestPartiallyGrown", new String[] { CKinetics.Comments.harvestPartiallyGrown });

    public final ConfigBase.ConfigBool harvesterReplants = this.b(true, "harvesterReplants", new String[] { CKinetics.Comments.harvesterReplants });

    public final ConfigBase.ConfigBool minecartContraptionInContainers = this.b(false, "minecartContraptionInContainers", new String[] { CKinetics.Comments.minecartContraptionInContainers });

    public final ConfigBase.ConfigGroup stats = this.group(1, "stats", new String[] { CKinetics.Comments.stats });

    public final ConfigBase.ConfigFloat mediumSpeed = this.f(30.0F, 0.0F, 4096.0F, "mediumSpeed", new String[] { CKinetics.Comments.rpm, CKinetics.Comments.mediumSpeed });

    public final ConfigBase.ConfigFloat fastSpeed = this.f(100.0F, 0.0F, 65535.0F, "fastSpeed", new String[] { CKinetics.Comments.rpm, CKinetics.Comments.fastSpeed });

    public final ConfigBase.ConfigFloat mediumStressImpact = this.f(4.0F, 0.0F, 4096.0F, "mediumStressImpact", new String[] { CKinetics.Comments.su, CKinetics.Comments.mediumStressImpact });

    public final ConfigBase.ConfigFloat highStressImpact = this.f(8.0F, 0.0F, 65535.0F, "highStressImpact", new String[] { CKinetics.Comments.su, CKinetics.Comments.highStressImpact });

    public final ConfigBase.ConfigFloat mediumCapacity = this.f(256.0F, 0.0F, 4096.0F, "mediumCapacity", new String[] { CKinetics.Comments.su, CKinetics.Comments.mediumCapacity });

    public final ConfigBase.ConfigFloat highCapacity = this.f(1024.0F, 0.0F, 65535.0F, "highCapacity", new String[] { CKinetics.Comments.su, CKinetics.Comments.highCapacity });

    public final CStress stressValues = this.nested(1, CStress::new, new String[] { CKinetics.Comments.stress });

    @Override
    public String getName() {
        return "kinetics";
    }

    private static class Comments {

        static String maxBeltLength = "Maximum length in blocks of mechanical belts.";

        static String crushingDamage = "Damage dealt by active Crushing Wheels.";

        static String maxRotationSpeed = "Maximum allowed rotation speed for any Kinetic Block.";

        static String fanPushDistance = "Maximum distance in blocks Fans can push entities.";

        static String fanPullDistance = "Maximum distance in blocks from where Fans can pull entities.";

        static String fanBlockCheckRate = "Game ticks between Fans checking for anything blocking their air flow.";

        static String fanRotationArgmax = "Rotation speed at which the maximum stats of fans are reached.";

        static String fanProcessingTime = "Game ticks required for a Fan-based processing recipe to take effect.";

        static String crankHungerMultiplier = "multiplier used for calculating exhaustion from speed when a crank is turned.";

        static String maxBlocksMoved = "Maximum amount of blocks in a structure movable by Pistons, Bearings or other means.";

        static String maxDataSize = "Maximum amount of data a contraption can have before it can't be synced with players.";

        static String maxDataSize2 = "Un-synced contraptions will not be visible and will not have collision.";

        static String maxDataDisable = "[0 to disable this limit]";

        static String maxChassisRange = "Maximum value of a chassis attachment range.";

        static String maxPistonPoles = "Maximum amount of extension poles behind a Mechanical Piston.";

        static String maxRopeLength = "Max length of rope available off a Rope Pulley.";

        static String maxCartCouplingLength = "Maximum allowed distance of two coupled minecarts.";

        static String rollerFillDepth = "Maximum depth of blocks filled in using a Mechanical Roller.";

        static String moveItemsToStorage = "Whether items mined or harvested by contraptions should be placed in their mounted storage.";

        static String harvestPartiallyGrown = "Whether harvesters should break crops that aren't fully grown.";

        static String harvesterReplants = "Whether harvesters should replant crops after harvesting.";

        static String stats = "Configure speed/capacity levels for requirements and indicators.";

        static String rpm = "[in Revolutions per Minute]";

        static String su = "[in Stress Units]";

        static String bytes = "[in Bytes]";

        static String mediumSpeed = "Minimum speed of rotation to be considered 'medium'";

        static String fastSpeed = "Minimum speed of rotation to be considered 'fast'";

        static String mediumStressImpact = "Minimum stress impact to be considered 'medium'";

        static String highStressImpact = "Minimum stress impact to be considered 'high'";

        static String mediumCapacity = "Minimum added Capacity by sources to be considered 'medium'";

        static String highCapacity = "Minimum added Capacity by sources to be considered 'high'";

        static String stress = "Fine tune the kinetic stats of individual components";

        static String ignoreDeployerAttacks = "Select what mobs should ignore Deployers when attacked by them.";

        static String disableStress = "Disable the Stress mechanic altogether.";

        static String kineticValidationFrequency = "Game ticks between Kinetic Blocks checking whether their source is still valid.";

        static String minimumWindmillSails = "Amount of sail-type blocks required for a windmill to assemble successfully.";

        static String windmillSailsPerRPM = "Number of sail-type blocks required to increase windmill speed by 1RPM.";

        static String maxEjectorDistance = "Max Distance in blocks a Weighted Ejector can throw";

        static String ejectorScanInterval = "Time in ticks until the next item launched by an ejector scans blocks for potential collisions";

        static String survivalContraptionPickup = "Whether minecart contraptions can be picked up in survival mode.";

        static String spawnerMovement = "Configure how Spawner blocks can be moved by contraptions.";

        static String amethystMovement = "Configure how Budding Amethyst can be moved by contraptions.";

        static String obsidianMovement = "Configure how Obsidian blocks can be moved by contraptions.";

        static String reinforcedDeepslateMovement = "Configure how Reinforced Deepslate blocks can be moved by contraptions.";

        static String minecartContraptionInContainers = "Whether minecart contraptions can be placed into container items.";
    }

    public static enum DeployerAggroSetting {

        ALL, CREEPERS, NONE
    }
}