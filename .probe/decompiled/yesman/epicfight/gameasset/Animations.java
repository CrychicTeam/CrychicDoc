package yesman.epicfight.gameasset;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.joml.Quaternionf;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.JointTransform;
import yesman.epicfight.api.animation.Keyframe;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.Pose;
import yesman.epicfight.api.animation.TransformSheet;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.property.MoveCoordFunctions;
import yesman.epicfight.api.animation.types.ActionAnimation;
import yesman.epicfight.api.animation.types.AimAnimation;
import yesman.epicfight.api.animation.types.AirSlashAnimation;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.BasicAttackAnimation;
import yesman.epicfight.api.animation.types.DashAttackAnimation;
import yesman.epicfight.api.animation.types.DodgeAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.animation.types.GuardAnimation;
import yesman.epicfight.api.animation.types.HitAnimation;
import yesman.epicfight.api.animation.types.InvincibleAnimation;
import yesman.epicfight.api.animation.types.KnockdownAnimation;
import yesman.epicfight.api.animation.types.LinkAnimation;
import yesman.epicfight.api.animation.types.LongHitAnimation;
import yesman.epicfight.api.animation.types.MirrorAnimation;
import yesman.epicfight.api.animation.types.MountAttackAnimation;
import yesman.epicfight.api.animation.types.MovementAnimation;
import yesman.epicfight.api.animation.types.OffAnimation;
import yesman.epicfight.api.animation.types.RangedAttackAnimation;
import yesman.epicfight.api.animation.types.ReboundAnimation;
import yesman.epicfight.api.animation.types.SelectiveAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.animation.types.grappling.GrapplingAttackAnimation;
import yesman.epicfight.api.animation.types.grappling.GrapplingTryAnimation;
import yesman.epicfight.api.animation.types.procedural.EnderDragonActionAnimation;
import yesman.epicfight.api.animation.types.procedural.EnderDragonAttackAnimation;
import yesman.epicfight.api.animation.types.procedural.EnderDragonDeathAnimation;
import yesman.epicfight.api.animation.types.procedural.EnderDragonDynamicActionAnimation;
import yesman.epicfight.api.animation.types.procedural.EnderDraonWalkAnimation;
import yesman.epicfight.api.animation.types.procedural.IKInfo;
import yesman.epicfight.api.collider.OBBCollider;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.api.utils.HitEntityList;
import yesman.epicfight.api.utils.LevelUtil;
import yesman.epicfight.api.utils.TimePairList;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.QuaternionUtils;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.model.armature.CreeperArmature;
import yesman.epicfight.model.armature.DragonArmature;
import yesman.epicfight.model.armature.EndermanArmature;
import yesman.epicfight.model.armature.HoglinArmature;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.model.armature.IronGolemArmature;
import yesman.epicfight.model.armature.PiglinArmature;
import yesman.epicfight.model.armature.RavagerArmature;
import yesman.epicfight.model.armature.SpiderArmature;
import yesman.epicfight.model.armature.VexArmature;
import yesman.epicfight.model.armature.WitherArmature;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.identity.MeteorSlamSkill;
import yesman.epicfight.skill.weaponinnate.SteelWhirlwindSkill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.boss.WitherPatch;
import yesman.epicfight.world.capabilities.entitypatch.boss.enderdragon.EnderDragonPatch;
import yesman.epicfight.world.capabilities.entitypatch.boss.enderdragon.PatchedPhases;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageSources;
import yesman.epicfight.world.damagesource.EpicFightDamageType;
import yesman.epicfight.world.damagesource.ExtraDamageInstance;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

@EventBusSubscriber(modid = "epicfight", bus = Bus.MOD)
public class Animations {

    public static StaticAnimation DUMMY_ANIMATION = new StaticAnimation();

    public static StaticAnimation BIPED_IDLE;

    public static StaticAnimation BIPED_WALK;

    public static StaticAnimation BIPED_RUN;

    public static StaticAnimation BIPED_SNEAK;

    public static StaticAnimation BIPED_SWIM;

    public static StaticAnimation BIPED_FLOAT;

    public static StaticAnimation BIPED_KNEEL;

    public static StaticAnimation BIPED_FALL;

    public static StaticAnimation BIPED_FLYING;

    public static StaticAnimation BIPED_CREATIVE_IDLE;

    public static StaticAnimation BIPED_CREATIVE_FLYING;

    public static StaticAnimation BIPED_CREATIVE_FLYING_FORWARD;

    public static StaticAnimation BIPED_CREATIVE_FLYING_BACKWARD;

    public static StaticAnimation BIPED_MOUNT;

    public static StaticAnimation BIPED_SIT;

    public static StaticAnimation BIPED_JUMP;

    public static StaticAnimation BIPED_DEATH;

    public static StaticAnimation BIPED_DIG_MAINHAND;

    public static StaticAnimation BIPED_DIG_OFFHAND;

    public static StaticAnimation BIPED_DIG;

    public static StaticAnimation BIPED_RUN_SPEAR;

    public static StaticAnimation BIPED_HOLD_GREATSWORD;

    public static StaticAnimation BIPED_HOLD_UCHIGATANA_SHEATHING;

    public static StaticAnimation BIPED_HOLD_UCHIGATANA;

    public static StaticAnimation BIPED_HOLD_TACHI;

    public static StaticAnimation BIPED_HOLD_LONGSWORD;

    public static StaticAnimation BIPED_HOLD_LIECHTENAUER;

    public static StaticAnimation BIPED_HOLD_SPEAR;

    public static StaticAnimation BIPED_HOLD_DUAL_WEAPON;

    public static StaticAnimation BIPED_HOLD_CROSSBOW;

    public static StaticAnimation BIPED_HOLD_MAP_TWOHAND;

    public static StaticAnimation BIPED_HOLD_MAP_OFFHAND;

    public static StaticAnimation BIPED_HOLD_MAP_MAINHAND;

    public static StaticAnimation BIPED_HOLD_MAP_TWOHAND_MOVE;

    public static StaticAnimation BIPED_HOLD_MAP_OFFHAND_MOVE;

    public static StaticAnimation BIPED_HOLD_MAP_MAINHAND_MOVE;

    public static StaticAnimation BIPED_WALK_GREATSWORD;

    public static StaticAnimation BIPED_WALK_SPEAR;

    public static StaticAnimation BIPED_WALK_UCHIGATANA_SHEATHING;

    public static StaticAnimation BIPED_WALK_UCHIGATANA;

    public static StaticAnimation BIPED_WALK_TWOHAND;

    public static StaticAnimation BIPED_WALK_LONGSWORD;

    public static StaticAnimation BIPED_WALK_LIECHTENAUER;

    public static StaticAnimation BIPED_RUN_GREATSWORD;

    public static StaticAnimation BIPED_RUN_UCHIGATANA;

    public static StaticAnimation BIPED_RUN_UCHIGATANA_SHEATHING;

    public static StaticAnimation BIPED_RUN_DUAL;

    public static StaticAnimation BIPED_RUN_LONGSWORD;

    public static StaticAnimation BIPED_UCHIGATANA_SCRAP;

    public static StaticAnimation BIPED_LIECHTENAUER_READY;

    public static StaticAnimation BIPED_HIT_SHIELD;

    public static StaticAnimation BIPED_CLIMBING;

    public static StaticAnimation BIPED_SLEEPING;

    public static StaticAnimation BIPED_BOW_AIM;

    public static StaticAnimation BIPED_BOW_SHOT;

    public static StaticAnimation BIPED_DRINK;

    public static StaticAnimation BIPED_EAT;

    public static StaticAnimation BIPED_SPYGLASS_USE;

    public static StaticAnimation BIPED_CROSSBOW_AIM;

    public static StaticAnimation BIPED_CROSSBOW_SHOT;

    public static StaticAnimation BIPED_CROSSBOW_RELOAD;

    public static StaticAnimation BIPED_JAVELIN_AIM;

    public static StaticAnimation BIPED_JAVELIN_THROW;

    public static StaticAnimation BIPED_HIT_SHORT;

    public static StaticAnimation BIPED_HIT_LONG;

    public static StaticAnimation BIPED_HIT_ON_MOUNT;

    public static StaticAnimation BIPED_LANDING;

    public static StaticAnimation BIPED_KNOCKDOWN;

    public static StaticAnimation BIPED_BLOCK;

    public static StaticAnimation BIPED_ROLL_FORWARD;

    public static StaticAnimation BIPED_ROLL_BACKWARD;

    public static StaticAnimation BIPED_STEP_FORWARD;

    public static StaticAnimation BIPED_STEP_BACKWARD;

    public static StaticAnimation BIPED_STEP_LEFT;

    public static StaticAnimation BIPED_STEP_RIGHT;

    public static StaticAnimation BIPED_KNOCKDOWN_WAKEUP_LEFT;

    public static StaticAnimation BIPED_KNOCKDOWN_WAKEUP_RIGHT;

    public static StaticAnimation BIPED_DEMOLITION_LEAP_CHARGING;

    public static StaticAnimation BIPED_DEMOLITION_LEAP;

    public static StaticAnimation BIPED_PHANTOM_ASCENT_FORWARD;

    public static StaticAnimation BIPED_PHANTOM_ASCENT_BACKWARD;

    public static StaticAnimation BIPED_MOB_ONEHAND1;

    public static StaticAnimation BIPED_MOB_ONEHAND2;

    public static StaticAnimation BIPED_MOB_GREATSWORD;

    public static StaticAnimation BIPED_MOB_TACHI;

    public static StaticAnimation BIPED_MOB_SPEAR_ONEHAND;

    public static StaticAnimation BIPED_MOB_SPEAR_TWOHAND1;

    public static StaticAnimation BIPED_MOB_SPEAR_TWOHAND2;

    public static StaticAnimation BIPED_MOB_SPEAR_TWOHAND3;

    public static StaticAnimation BIPED_MOB_SWORD_DUAL1;

    public static StaticAnimation BIPED_MOB_SWORD_DUAL2;

    public static StaticAnimation BIPED_MOB_SWORD_DUAL3;

    public static StaticAnimation BIPED_MOB_LONGSWORD1;

    public static StaticAnimation BIPED_MOB_LONGSWORD2;

    public static StaticAnimation BIPED_MOB_UCHIGATANA1;

    public static StaticAnimation BIPED_MOB_UCHIGATANA2;

    public static StaticAnimation BIPED_MOB_UCHIGATANA3;

    public static StaticAnimation BIPED_MOB_DAGGER_ONEHAND1;

    public static StaticAnimation BIPED_MOB_DAGGER_ONEHAND2;

    public static StaticAnimation BIPED_MOB_DAGGER_ONEHAND3;

    public static StaticAnimation BIPED_MOB_DAGGER_TWOHAND1;

    public static StaticAnimation BIPED_MOB_DAGGER_TWOHAND2;

    public static StaticAnimation BIPED_MOB_THROW;

    public static StaticAnimation CREEPER_IDLE;

    public static StaticAnimation CREEPER_WALK;

    public static StaticAnimation CREEPER_HIT_LONG;

    public static StaticAnimation CREEPER_HIT_SHORT;

    public static StaticAnimation CREEPER_DEATH;

    public static StaticAnimation DRAGON_IDLE;

    public static StaticAnimation DRAGON_WALK;

    public static StaticAnimation DRAGON_WALK_PROCEDURAL;

    public static StaticAnimation DRAGON_FLY;

    public static StaticAnimation DRAGON_DEATH;

    public static StaticAnimation DRAGON_GROUND_TO_FLY;

    public static StaticAnimation DRAGON_FLY_TO_GROUND;

    public static StaticAnimation DRAGON_ATTACK1;

    public static StaticAnimation DRAGON_ATTACK2;

    public static StaticAnimation DRAGON_ATTACK3;

    public static StaticAnimation DRAGON_ATTACK4;

    public static StaticAnimation DRAGON_ATTACK4_RECOVERY;

    public static StaticAnimation DRAGON_FIREBALL;

    public static StaticAnimation DRAGON_AIRSTRIKE;

    public static StaticAnimation DRAGON_BACKJUMP_PREPARE;

    public static StaticAnimation DRAGON_BACKJUMP_MOVE;

    public static StaticAnimation DRAGON_BACKJUMP_RECOVERY;

    public static StaticAnimation DRAGON_CRYSTAL_LINK;

    public static StaticAnimation DRAGON_NEUTRALIZED;

    public static StaticAnimation DRAGON_NEUTRALIZED_RECOVERY;

    public static StaticAnimation ENDERMAN_IDLE;

    public static StaticAnimation ENDERMAN_WALK;

    public static StaticAnimation ENDERMAN_DEATH;

    public static StaticAnimation ENDERMAN_HIT_SHORT;

    public static StaticAnimation ENDERMAN_HIT_LONG;

    public static StaticAnimation ENDERMAN_NEUTRALIZED;

    public static StaticAnimation ENDERMAN_CONVERT_RAGE;

    public static StaticAnimation ENDERMAN_RAGE_IDLE;

    public static StaticAnimation ENDERMAN_RAGE_WALK;

    public static StaticAnimation ENDERMAN_GRASP;

    public static StaticAnimation ENDERMAN_TP_KICK1;

    public static StaticAnimation ENDERMAN_TP_KICK2;

    public static StaticAnimation ENDERMAN_KNEE;

    public static StaticAnimation ENDERMAN_KICK1;

    public static StaticAnimation ENDERMAN_KICK2;

    public static StaticAnimation ENDERMAN_KICK_COMBO;

    public static StaticAnimation ENDERMAN_TP_EMERGENCE;

    public static StaticAnimation SPIDER_IDLE;

    public static StaticAnimation SPIDER_CRAWL;

    public static StaticAnimation SPIDER_DEATH;

    public static StaticAnimation SPIDER_HIT;

    public static StaticAnimation SPIDER_NEUTRALIZED;

    public static StaticAnimation SPIDER_ATTACK;

    public static StaticAnimation SPIDER_JUMP_ATTACK;

    public static StaticAnimation GOLEM_IDLE;

    public static StaticAnimation GOLEM_WALK;

    public static StaticAnimation GOLEM_DEATH;

    public static StaticAnimation GOLEM_ATTACK1;

    public static StaticAnimation GOLEM_ATTACK2;

    public static StaticAnimation GOLEM_ATTACK3;

    public static StaticAnimation GOLEM_ATTACK4;

    public static StaticAnimation HOGLIN_IDLE;

    public static StaticAnimation HOGLIN_WALK;

    public static StaticAnimation HOGLIN_DEATH;

    public static StaticAnimation HOGLIN_ATTACK;

    public static StaticAnimation ILLAGER_IDLE;

    public static StaticAnimation ILLAGER_WALK;

    public static StaticAnimation VINDICATOR_IDLE_AGGRESSIVE;

    public static StaticAnimation VINDICATOR_CHASE;

    public static StaticAnimation VINDICATOR_SWING_AXE1;

    public static StaticAnimation VINDICATOR_SWING_AXE2;

    public static StaticAnimation VINDICATOR_SWING_AXE3;

    public static StaticAnimation EVOKER_CAST_SPELL;

    public static StaticAnimation PIGLIN_IDLE;

    public static StaticAnimation PIGLIN_WALK;

    public static StaticAnimation PIGLIN_ZOMBIFIED_IDLE;

    public static StaticAnimation PIGLIN_ZOMBIFIED_WALK;

    public static StaticAnimation PIGLIN_ZOMBIFIED_CHASE;

    public static StaticAnimation PIGLIN_CELEBRATE1;

    public static StaticAnimation PIGLIN_CELEBRATE2;

    public static StaticAnimation PIGLIN_CELEBRATE3;

    public static StaticAnimation PIGLIN_ADMIRE;

    public static StaticAnimation PIGLIN_DEATH;

    public static StaticAnimation RAVAGER_IDLE;

    public static StaticAnimation RAVAGER_WALK;

    public static StaticAnimation RAVAGER_DEATH;

    public static StaticAnimation RAVAGER_STUN;

    public static StaticAnimation RAVAGER_ATTACK1;

    public static StaticAnimation RAVAGER_ATTACK2;

    public static StaticAnimation RAVAGER_ATTACK3;

    public static StaticAnimation VEX_IDLE;

    public static StaticAnimation VEX_FLIPPING;

    public static StaticAnimation VEX_DEATH;

    public static StaticAnimation VEX_HIT;

    public static StaticAnimation VEX_CHARGE;

    public static StaticAnimation VEX_NEUTRALIZED;

    public static StaticAnimation WITCH_DRINKING;

    public static StaticAnimation WITHER_SKELETON_IDLE;

    public static StaticAnimation WITHER_SKELETON_SPECIAL_SPAWN;

    public static StaticAnimation WITHER_SKELETON_WALK;

    public static StaticAnimation WITHER_SKELETON_CHASE;

    public static StaticAnimation WITHER_SKELETON_ATTACK1;

    public static StaticAnimation WITHER_SKELETON_ATTACK2;

    public static StaticAnimation WITHER_SKELETON_ATTACK3;

    public static StaticAnimation WITHER_IDLE;

    public static StaticAnimation WITHER_CHARGE;

    public static StaticAnimation WITHER_DEATH;

    public static StaticAnimation WITHER_NEUTRALIZED;

    public static StaticAnimation WITHER_SPELL_ARMOR;

    public static StaticAnimation WITHER_BLOCKED;

    public static StaticAnimation WITHER_GHOST_STANDBY;

    public static StaticAnimation WITHER_SWIRL;

    public static StaticAnimation WITHER_BEAM;

    public static StaticAnimation WITHER_BACKFLIP;

    public static StaticAnimation ZOMBIE_IDLE;

    public static StaticAnimation ZOMBIE_WALK;

    public static StaticAnimation ZOMBIE_CHASE;

    public static StaticAnimation ZOMBIE_ATTACK1;

    public static StaticAnimation ZOMBIE_ATTACK2;

    public static StaticAnimation ZOMBIE_ATTACK3;

    public static StaticAnimation AXE_AUTO1;

    public static StaticAnimation AXE_AUTO2;

    public static StaticAnimation AXE_DASH;

    public static StaticAnimation AXE_AIRSLASH;

    public static StaticAnimation FIST_AUTO1;

    public static StaticAnimation FIST_AUTO2;

    public static StaticAnimation FIST_AUTO3;

    public static StaticAnimation FIST_DASH;

    public static StaticAnimation FIST_AIR_SLASH;

    public static StaticAnimation SPEAR_ONEHAND_AUTO;

    public static StaticAnimation SPEAR_BLOCK_AUTO;

    public static StaticAnimation SPEAR_ONEHAND_AIR_SLASH;

    public static StaticAnimation SPEAR_TWOHAND_AUTO1;

    public static StaticAnimation SPEAR_TWOHAND_AUTO2;

    public static StaticAnimation SPEAR_TWOHAND_AIR_SLASH;

    public static StaticAnimation SPEAR_DASH;

    public static StaticAnimation SPEAR_MOUNT_ATTACK;

    public static StaticAnimation SPEAR_GUARD;

    public static StaticAnimation SPEAR_GUARD_HIT;

    public static StaticAnimation SWORD_AUTO1;

    public static StaticAnimation SWORD_AUTO2;

    public static StaticAnimation SWORD_AUTO3;

    public static StaticAnimation SWORD_DASH;

    public static StaticAnimation SWORD_AIR_SLASH;

    public static StaticAnimation SWORD_GUARD;

    public static StaticAnimation SWORD_GUARD_HIT;

    public static StaticAnimation SWORD_GUARD_ACTIVE_HIT1;

    public static StaticAnimation SWORD_GUARD_ACTIVE_HIT2;

    public static StaticAnimation SWORD_GUARD_ACTIVE_HIT3;

    public static StaticAnimation LONGSWORD_GUARD_ACTIVE_HIT1;

    public static StaticAnimation LONGSWORD_GUARD_ACTIVE_HIT2;

    public static StaticAnimation SWORD_DUAL_AUTO1;

    public static StaticAnimation SWORD_DUAL_AUTO2;

    public static StaticAnimation SWORD_DUAL_AUTO3;

    public static StaticAnimation SWORD_DUAL_DASH;

    public static StaticAnimation SWORD_DUAL_AIR_SLASH;

    public static StaticAnimation SWORD_DUAL_GUARD;

    public static StaticAnimation SWORD_DUAL_GUARD_HIT;

    public static StaticAnimation BIPED_COMMON_NEUTRALIZED;

    public static StaticAnimation GREATSWORD_GUARD_BREAK;

    public static StaticAnimation METEOR_SLAM;

    public static StaticAnimation REVELATION_ONEHAND;

    public static StaticAnimation REVELATION_TWOHAND;

    public static StaticAnimation LONGSWORD_AUTO1;

    public static StaticAnimation LONGSWORD_AUTO2;

    public static StaticAnimation LONGSWORD_AUTO3;

    public static StaticAnimation LONGSWORD_DASH;

    public static StaticAnimation LONGSWORD_LIECHTENAUER_AUTO1;

    public static StaticAnimation LONGSWORD_LIECHTENAUER_AUTO2;

    public static StaticAnimation LONGSWORD_LIECHTENAUER_AUTO3;

    public static StaticAnimation LONGSWORD_AIR_SLASH;

    public static StaticAnimation LONGSWORD_GUARD;

    public static StaticAnimation LONGSWORD_GUARD_HIT;

    public static StaticAnimation TACHI_AUTO1;

    public static StaticAnimation TACHI_AUTO2;

    public static StaticAnimation TACHI_AUTO3;

    public static StaticAnimation TACHI_DASH;

    public static StaticAnimation TOOL_AUTO1;

    public static StaticAnimation TOOL_AUTO2;

    public static StaticAnimation TOOL_DASH;

    public static StaticAnimation UCHIGATANA_AUTO1;

    public static StaticAnimation UCHIGATANA_AUTO2;

    public static StaticAnimation UCHIGATANA_AUTO3;

    public static StaticAnimation UCHIGATANA_DASH;

    public static StaticAnimation UCHIGATANA_AIR_SLASH;

    public static StaticAnimation UCHIGATANA_SHEATHING_AUTO;

    public static StaticAnimation UCHIGATANA_SHEATHING_DASH;

    public static StaticAnimation UCHIGATANA_SHEATH_AIR_SLASH;

    public static StaticAnimation UCHIGATANA_GUARD;

    public static StaticAnimation UCHIGATANA_GUARD_HIT;

    public static StaticAnimation SWORD_MOUNT_ATTACK;

    public static StaticAnimation GREATSWORD_AUTO1;

    public static StaticAnimation GREATSWORD_AUTO2;

    public static StaticAnimation GREATSWORD_DASH;

    public static StaticAnimation GREATSWORD_AIR_SLASH;

    public static StaticAnimation GREATSWORD_GUARD;

    public static StaticAnimation GREATSWORD_GUARD_HIT;

    public static StaticAnimation DAGGER_AUTO1;

    public static StaticAnimation DAGGER_AUTO2;

    public static StaticAnimation DAGGER_AUTO3;

    public static StaticAnimation DAGGER_DASH;

    public static StaticAnimation DAGGER_AIR_SLASH;

    public static StaticAnimation DAGGER_DUAL_AUTO1;

    public static StaticAnimation DAGGER_DUAL_AUTO2;

    public static StaticAnimation DAGGER_DUAL_AUTO3;

    public static StaticAnimation DAGGER_DUAL_AUTO4;

    public static StaticAnimation DAGGER_DUAL_DASH;

    public static StaticAnimation DAGGER_DUAL_AIR_SLASH;

    public static StaticAnimation TRIDENT_AUTO1;

    public static StaticAnimation TRIDENT_AUTO2;

    public static StaticAnimation TRIDENT_AUTO3;

    public static StaticAnimation THE_GUILLOTINE;

    public static StaticAnimation SWEEPING_EDGE;

    public static StaticAnimation DANCING_EDGE;

    public static StaticAnimation HEARTPIERCER;

    public static StaticAnimation GRASPING_SPIRAL_FIRST;

    public static StaticAnimation GRASPING_SPIRAL_SECOND;

    public static StaticAnimation STEEL_WHIRLWIND_CHARGING;

    public static StaticAnimation STEEL_WHIRLWIND;

    public static StaticAnimation BATTOJUTSU;

    public static StaticAnimation BATTOJUTSU_DASH;

    public static StaticAnimation RUSHING_TEMPO1;

    public static StaticAnimation RUSHING_TEMPO2;

    public static StaticAnimation RUSHING_TEMPO3;

    public static StaticAnimation RELENTLESS_COMBO;

    public static StaticAnimation EVISCERATE_FIRST;

    public static StaticAnimation EVISCERATE_SECOND;

    public static StaticAnimation BLADE_RUSH_COMBO1;

    public static StaticAnimation BLADE_RUSH_COMBO2;

    public static StaticAnimation BLADE_RUSH_COMBO3;

    public static StaticAnimation BLADE_RUSH_HIT;

    public static StaticAnimation BLADE_RUSH_EXECUTE_BIPED;

    public static StaticAnimation BLADE_RUSH_TRY;

    public static StaticAnimation BLADE_RUSH_FAILED;

    public static StaticAnimation WRATHFUL_LIGHTING;

    public static StaticAnimation TSUNAMI;

    public static StaticAnimation TSUNAMI_REINFORCED;

    public static StaticAnimation EVERLASTING_ALLEGIANCE_CALL;

    public static StaticAnimation EVERLASTING_ALLEGIANCE_CATCH;

    public static StaticAnimation SHARP_STAB;

    public static StaticAnimation OFF_ANIMATION_HIGHEST;

    public static StaticAnimation OFF_ANIMATION_MIDDLE;

    @SubscribeEvent
    public static void registerAnimations(AnimationRegistryEvent event) {
        event.getRegistryMap().put("epicfight", Animations::build);
    }

    private static void build() {
        HumanoidArmature biped = Armatures.BIPED;
        CreeperArmature creeper = Armatures.CREEPER;
        EndermanArmature enderman = Armatures.ENDERMAN;
        SpiderArmature spider = Armatures.SPIDER;
        IronGolemArmature ironGolem = Armatures.IRON_GOLEM;
        RavagerArmature ravager = Armatures.RAVAGER;
        VexArmature vex = Armatures.VEX;
        PiglinArmature piglin = Armatures.PIGLIN;
        HoglinArmature hoglin = Armatures.HOGLIN;
        DragonArmature dragon = Armatures.DRAGON;
        WitherArmature wither = Armatures.WITHER;
        BIPED_IDLE = new StaticAnimation(true, "biped/living/idle", biped);
        BIPED_WALK = new MovementAnimation(true, "biped/living/walk", biped);
        BIPED_FLYING = new StaticAnimation(true, "biped/living/fly", biped);
        BIPED_CREATIVE_IDLE = new StaticAnimation(true, "biped/living/creative_idle", biped);
        BIPED_CREATIVE_FLYING_FORWARD = new MovementAnimation(true, "biped/living/creative_fly", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.FLYING_CORRECTION);
        BIPED_CREATIVE_FLYING_BACKWARD = new MovementAnimation(true, "biped/living/creative_fly_backward", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.FLYING_CORRECTION2);
        BIPED_CREATIVE_FLYING = new SelectiveAnimation(entitypatch -> {
            Vec3 view = ((LivingEntity) entitypatch.getOriginal()).m_20252_(1.0F);
            Vec3 move = ((LivingEntity) entitypatch.getOriginal()).m_20184_();
            double dot = view.dot(move);
            return dot < 0.0 ? 1 : 0;
        }, BIPED_CREATIVE_FLYING_FORWARD, BIPED_CREATIVE_FLYING_BACKWARD);
        BIPED_HOLD_CROSSBOW = new StaticAnimation(true, "biped/living/hold_crossbow", biped);
        BIPED_HOLD_MAP_TWOHAND = new StaticAnimation(true, "biped/living/hold_map_twohand", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.MAP_ARMS_CORRECTION);
        BIPED_HOLD_MAP_OFFHAND = new StaticAnimation(true, "biped/living/hold_map_offhand", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.MAP_ARMS_CORRECTION);
        BIPED_HOLD_MAP_MAINHAND = new StaticAnimation(true, "biped/living/hold_map_mainhand", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.MAP_ARMS_CORRECTION);
        BIPED_HOLD_MAP_TWOHAND_MOVE = new StaticAnimation(true, "biped/living/hold_map_twohand_move", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.MAP_ARMS_CORRECTION);
        BIPED_HOLD_MAP_OFFHAND_MOVE = new StaticAnimation(true, "biped/living/hold_map_offhand_move", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.MAP_ARMS_CORRECTION);
        BIPED_HOLD_MAP_MAINHAND_MOVE = new StaticAnimation(true, "biped/living/hold_map_mainhand_move", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.MAP_ARMS_CORRECTION);
        BIPED_RUN = new MovementAnimation(true, "biped/living/run", biped);
        BIPED_SNEAK = new MovementAnimation(true, "biped/living/sneak", biped);
        BIPED_SWIM = new MovementAnimation(true, "biped/living/swim", biped);
        BIPED_FLOAT = new StaticAnimation(true, "biped/living/float", biped);
        BIPED_KNEEL = new StaticAnimation(true, "biped/living/kneel", biped);
        BIPED_FALL = new StaticAnimation(true, "biped/living/fall", biped);
        BIPED_MOUNT = new StaticAnimation(true, "biped/living/mount", biped);
        BIPED_SIT = new StaticAnimation(true, "biped/living/sit", biped);
        BIPED_DIG_MAINHAND = new StaticAnimation(0.11F, true, "biped/living/dig", biped);
        BIPED_DIG_OFFHAND = new StaticAnimation(0.11F, true, "biped/living/dig_offhand", biped);
        BIPED_DIG = new SelectiveAnimation(entitypatch -> ((LivingEntity) entitypatch.getOriginal()).swingingArm == InteractionHand.OFF_HAND ? 1 : 0, BIPED_DIG_MAINHAND, BIPED_DIG_OFFHAND);
        BIPED_BOW_AIM = new AimAnimation(false, "biped/combat/bow_aim_mid", "biped/combat/bow_aim_up", "biped/combat/bow_aim_down", "biped/combat/bow_aim_lying", biped);
        BIPED_BOW_SHOT = new ReboundAnimation(0.04F, false, "biped/combat/bow_shot_mid", "biped/combat/bow_shot_up", "biped/combat/bow_shot_down", "biped/combat/bow_shot_lying", biped);
        BIPED_DRINK = new MirrorAnimation(0.35F, true, "biped/living/drink", "biped/living/drink_offhand", biped);
        BIPED_EAT = new MirrorAnimation(0.35F, true, "biped/living/eat", "biped/living/eat_offhand", biped);
        BIPED_SPYGLASS_USE = new MirrorAnimation(0.15F, true, "biped/living/spyglass", "biped/living/spyglass_offhand", biped).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, elapsedTime) -> self instanceof LinkAnimation ? speed : 0.0F).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, (self, pose, entitypatch, elapsedTime, partialTicks) -> {
            if (entitypatch.isFirstPerson()) {
                pose.getJointTransformData().clear();
            } else if (!(self instanceof LinkAnimation)) {
                LivingMotion livingMotion = entitypatch.getCurrentLivingMotion();
                Pose rawPose;
                if (livingMotion != LivingMotions.SWIM && livingMotion != LivingMotions.FLY && livingMotion != LivingMotions.CREATIVE_FLY) {
                    float xRot = Mth.clamp((entitypatch.getOriginal().m_146909_() + 90.0F) * 0.016666668F, 0.0F, 3.0F);
                    rawPose = self.getRawPose(xRot);
                    float f = 90.0F;
                    float ratio = (f - Math.abs(entitypatch.getOriginal().m_146909_())) / f;
                    float yawOffset = entitypatch.getOriginal().m_20202_() != null ? entitypatch.getOriginal().getYHeadRot() : entitypatch.getOriginal().yBodyRot;
                    ((JointTransform) rawPose.getJointTransformData().get("Chest")).frontResult(JointTransform.getRotation(QuaternionUtils.YP.rotationDegrees(Mth.wrapDegrees(entitypatch.getOriginal().getYHeadRot() - yawOffset) * ratio)), OpenMatrix4f::mulAsOriginFront);
                } else {
                    rawPose = self.getRawPose(3.3333F);
                }
                rawPose.getJointTransformData().forEach(pose.getJointTransformData()::put);
            }
        });
        BIPED_CROSSBOW_AIM = new AimAnimation(false, "biped/combat/crossbow_aim_mid", "biped/combat/crossbow_aim_up", "biped/combat/crossbow_aim_down", "biped/combat/crossbow_aim_lying", biped);
        BIPED_CROSSBOW_SHOT = new ReboundAnimation(false, "biped/combat/crossbow_shot_mid", "biped/combat/crossbow_shot_up", "biped/combat/crossbow_shot_down", "biped/combat/crossbow_shot_lying", biped);
        BIPED_CROSSBOW_RELOAD = new StaticAnimation(false, "biped/combat/crossbow_reload", biped);
        BIPED_JUMP = new StaticAnimation(0.083F, false, "biped/living/jump", biped);
        BIPED_RUN_SPEAR = new MovementAnimation(true, "biped/living/run_spear", biped);
        BIPED_BLOCK = new MirrorAnimation(0.25F, true, "biped/living/shield", "biped/living/shield_mirror", biped);
        BIPED_HOLD_GREATSWORD = new StaticAnimation(true, "biped/living/hold_greatsword", biped);
        BIPED_HOLD_UCHIGATANA_SHEATHING = new StaticAnimation(true, "biped/living/hold_uchigatana_sheath", biped);
        BIPED_HOLD_UCHIGATANA = new StaticAnimation(true, "biped/living/hold_uchigatana", biped);
        BIPED_HOLD_TACHI = new StaticAnimation(true, "biped/living/hold_tachi", biped);
        BIPED_HOLD_LONGSWORD = new StaticAnimation(true, "biped/living/hold_longsword", biped);
        BIPED_HOLD_SPEAR = new StaticAnimation(true, "biped/living/hold_spear", biped);
        BIPED_HOLD_DUAL_WEAPON = new StaticAnimation(true, "biped/living/hold_dual", biped);
        BIPED_HOLD_LIECHTENAUER = new StaticAnimation(true, "biped/living/hold_liechtenauer", biped);
        BIPED_WALK_GREATSWORD = new MovementAnimation(true, "biped/living/walk_greatsword", biped);
        BIPED_WALK_SPEAR = new MovementAnimation(true, "biped/living/walk_spear", biped);
        BIPED_WALK_UCHIGATANA_SHEATHING = new MovementAnimation(true, "biped/living/walk_uchigatana_sheath", biped);
        BIPED_WALK_UCHIGATANA = new MovementAnimation(true, "biped/living/walk_uchigatana", biped);
        BIPED_WALK_TWOHAND = new MovementAnimation(true, "biped/living/walk_twohand", biped);
        BIPED_WALK_LONGSWORD = new MovementAnimation(true, "biped/living/walk_longsword", biped);
        BIPED_WALK_LIECHTENAUER = new MovementAnimation(true, "biped/living/walk_liechtenauer", biped);
        BIPED_RUN_GREATSWORD = new MovementAnimation(true, "biped/living/run_greatsword", biped);
        BIPED_RUN_UCHIGATANA = new MovementAnimation(true, "biped/living/run_uchigatana", biped);
        BIPED_RUN_UCHIGATANA_SHEATHING = new MovementAnimation(true, "biped/living/run_uchigatana_sheath", biped);
        BIPED_RUN_DUAL = new MovementAnimation(true, "biped/living/run_dual", biped);
        BIPED_RUN_LONGSWORD = new MovementAnimation(true, "biped/living/run_longsword", biped);
        BIPED_UCHIGATANA_SCRAP = new StaticAnimation(0.05F, false, "biped/living/uchigatana_scrap", biped).addEvents(AnimationEvent.TimeStampedEvent.create(0.15F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(EpicFightSounds.SWORD_IN.get()));
        BIPED_LIECHTENAUER_READY = new StaticAnimation(0.1F, false, "biped/living/liechtenauer_ready", biped);
        BIPED_HIT_SHIELD = new MirrorAnimation(0.05F, false, "biped/combat/hit_shield", "biped/combat/hit_shield_mirror", biped);
        BIPED_CLIMBING = new MovementAnimation(0.16F, true, "biped/living/climb", biped).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE);
        BIPED_SLEEPING = new StaticAnimation(0.16F, true, "biped/living/sleep", biped);
        BIPED_JAVELIN_AIM = new AimAnimation(false, "biped/combat/javelin_aim_mid", "biped/combat/javelin_aim_up", "biped/combat/javelin_aim_down", "biped/combat/javelin_aim_lying", biped);
        BIPED_JAVELIN_THROW = new ReboundAnimation(0.08F, false, "biped/combat/javelin_throw_mid", "biped/combat/javelin_throw_up", "biped/combat/javelin_throw_down", "biped/combat/javelin_throw_lying", biped);
        OFF_ANIMATION_HIGHEST = new OffAnimation("off_highest");
        OFF_ANIMATION_MIDDLE = new OffAnimation("off_middle");
        ZOMBIE_IDLE = new StaticAnimation(true, "zombie/idle", biped);
        ZOMBIE_WALK = new MovementAnimation(true, "zombie/walk", biped);
        ZOMBIE_CHASE = new MovementAnimation(true, "zombie/chase", biped);
        CREEPER_IDLE = new StaticAnimation(true, "creeper/idle", creeper);
        CREEPER_WALK = new MovementAnimation(true, "creeper/walk", creeper);
        ENDERMAN_IDLE = new StaticAnimation(true, "enderman/idle", enderman);
        ENDERMAN_WALK = new MovementAnimation(true, "enderman/walk", enderman);
        ENDERMAN_RAGE_IDLE = new StaticAnimation(true, "enderman/rage_idle", enderman);
        ENDERMAN_RAGE_WALK = new MovementAnimation(true, "enderman/rage_walk", enderman);
        WITHER_SKELETON_WALK = new MovementAnimation(true, "wither_skeleton/walk", biped);
        WITHER_SKELETON_CHASE = new MovementAnimation(0.36F, true, "wither_skeleton/chase", biped);
        WITHER_SKELETON_IDLE = new StaticAnimation(true, "wither_skeleton/idle", biped);
        WITHER_SKELETON_SPECIAL_SPAWN = new InvincibleAnimation(0.0F, "wither_skeleton/special_spawn", biped);
        SPIDER_IDLE = new StaticAnimation(true, "spider/idle", spider);
        SPIDER_CRAWL = new MovementAnimation(true, "spider/crawl", spider);
        GOLEM_IDLE = new StaticAnimation(true, "iron_golem/idle", ironGolem);
        GOLEM_WALK = new MovementAnimation(true, "iron_golem/walk", ironGolem);
        HOGLIN_IDLE = new StaticAnimation(true, "hoglin/idle", hoglin);
        HOGLIN_WALK = new MovementAnimation(true, "hoglin/walk", hoglin);
        ILLAGER_IDLE = new StaticAnimation(true, "illager/idle", biped);
        ILLAGER_WALK = new MovementAnimation(true, "illager/walk", biped);
        VINDICATOR_IDLE_AGGRESSIVE = new StaticAnimation(true, "illager/idle_aggressive", biped);
        VINDICATOR_CHASE = new MovementAnimation(true, "illager/chase", biped);
        EVOKER_CAST_SPELL = new StaticAnimation(true, "illager/spellcast", biped);
        RAVAGER_IDLE = new StaticAnimation(true, "ravager/idle", ravager);
        RAVAGER_WALK = new MovementAnimation(true, "ravager/walk", ravager);
        VEX_IDLE = new StaticAnimation(true, "vex/idle", vex);
        VEX_FLIPPING = new StaticAnimation(0.05F, true, "vex/flip", vex);
        PIGLIN_IDLE = new StaticAnimation(true, "piglin/idle", piglin);
        PIGLIN_WALK = new MovementAnimation(true, "piglin/walk", piglin);
        PIGLIN_ZOMBIFIED_IDLE = new StaticAnimation(true, "piglin/zombified_idle", piglin);
        PIGLIN_ZOMBIFIED_WALK = new MovementAnimation(true, "piglin/zombified_walk", piglin);
        PIGLIN_ZOMBIFIED_CHASE = new MovementAnimation(true, "piglin/zombified_chase", piglin);
        PIGLIN_CELEBRATE1 = new StaticAnimation(true, "piglin/celebrate1", piglin);
        PIGLIN_CELEBRATE2 = new StaticAnimation(true, "piglin/celebrate2", piglin);
        PIGLIN_CELEBRATE3 = new StaticAnimation(true, "piglin/celebrate3", piglin);
        PIGLIN_ADMIRE = new StaticAnimation(true, "piglin/admire", piglin);
        WITHER_IDLE = new StaticAnimation(true, "wither/idle", wither);
        SPEAR_GUARD = new StaticAnimation(true, "biped/skill/guard_spear", biped);
        SWORD_GUARD = new StaticAnimation(true, "biped/skill/guard_sword", biped);
        SWORD_DUAL_GUARD = new StaticAnimation(true, "biped/skill/guard_dualsword", biped);
        GREATSWORD_GUARD = new StaticAnimation(0.25F, true, "biped/skill/guard_greatsword", biped);
        UCHIGATANA_GUARD = new StaticAnimation(0.25F, true, "biped/skill/guard_uchigatana", biped);
        LONGSWORD_GUARD = new StaticAnimation(0.25F, true, "biped/skill/guard_longsword", biped);
        STEEL_WHIRLWIND_CHARGING = new StaticAnimation(0.15F, false, "biped/skill/steel_whirlwind_charging", biped).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CHARGING);
        BIPED_ROLL_FORWARD = new DodgeAnimation(0.1F, "biped/skill/roll_forward", 0.6F, 0.8F, biped).addEvents(new AnimationEvent.TimeStampedEvent[] { AnimationEvent.TimeStampedEvent.create(0.0F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.ROLL.get()) });
        BIPED_ROLL_BACKWARD = new DodgeAnimation(0.1F, "biped/skill/roll_backward", 0.6F, 0.8F, biped).addEvents(new AnimationEvent.TimeStampedEvent[] { AnimationEvent.TimeStampedEvent.create(0.0F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.ROLL.get()) });
        BIPED_STEP_FORWARD = new DodgeAnimation(0.1F, 0.35F, "biped/skill/step_forward", 0.6F, 1.65F, biped).addState(EntityState.LOCKON_ROTATE, Boolean.valueOf(true)).newTimePair(0.0F, 0.2F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false).addStateRemoveOld(EntityState.CAN_SKILL_EXECUTION, false);
        BIPED_STEP_BACKWARD = new DodgeAnimation(0.1F, 0.35F, "biped/skill/step_backward", 0.6F, 1.65F, biped).addState(EntityState.LOCKON_ROTATE, Boolean.valueOf(true)).newTimePair(0.0F, 0.2F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false).addStateRemoveOld(EntityState.CAN_SKILL_EXECUTION, false);
        BIPED_STEP_LEFT = new DodgeAnimation(0.1F, 0.35F, "biped/skill/step_left", 0.6F, 1.65F, biped).addState(EntityState.LOCKON_ROTATE, Boolean.valueOf(true)).newTimePair(0.0F, 0.2F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false).addStateRemoveOld(EntityState.CAN_SKILL_EXECUTION, false);
        BIPED_STEP_RIGHT = new DodgeAnimation(0.1F, 0.35F, "biped/skill/step_right", 0.6F, 1.65F, biped).addState(EntityState.LOCKON_ROTATE, Boolean.valueOf(true)).newTimePair(0.0F, 0.2F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false).addStateRemoveOld(EntityState.CAN_SKILL_EXECUTION, false);
        BIPED_KNOCKDOWN_WAKEUP_LEFT = new DodgeAnimation(0.1F, "biped/skill/knockdown_wakeup_left", 0.8F, 0.6F, biped);
        BIPED_KNOCKDOWN_WAKEUP_RIGHT = new DodgeAnimation(0.1F, "biped/skill/knockdown_wakeup_right", 0.8F, 0.6F, biped);
        BIPED_DEMOLITION_LEAP_CHARGING = new ActionAnimation(0.15F, "biped/skill/demolition_leap_charge", biped).addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, true).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CHARGING).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, (self, pose, entitypatch, time, partialTicks) -> {
            if (self.isStaticAnimation()) {
                float xRot = Mth.clamp(entitypatch.getCameraXRot(), -60.0F, 50.0F);
                float yRot = Mth.clamp(Mth.wrapDegrees(entitypatch.getCameraYRot() - entitypatch.getOriginal().m_146908_()), -60.0F, 60.0F);
                JointTransform chest = pose.getOrDefaultTransform("Chest");
                chest.frontResult(JointTransform.getRotation(QuaternionUtils.YP.rotationDegrees(yRot)), OpenMatrix4f::mulAsOriginFront);
                JointTransform head = pose.getOrDefaultTransform("Head");
                MathUtils.mulQuaternion(QuaternionUtils.XP.rotationDegrees(xRot), head.rotation(), head.rotation());
            }
        }).newTimePair(0.0F, Float.MAX_VALUE).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, true).addStateRemoveOld(EntityState.CAN_SKILL_EXECUTION, true);
        BIPED_DEMOLITION_LEAP = new ActionAnimation(0.05F, 0.45F, "biped/skill/demolition_leap", biped);
        BIPED_PHANTOM_ASCENT_FORWARD = new ActionAnimation(0.05F, 0.7F, "biped/skill/phantom_ascent_forward", biped).addStateRemoveOld(EntityState.MOVEMENT_LOCKED, Boolean.valueOf(false)).newTimePair(0.0F, 0.5F).addStateRemoveOld(EntityState.INACTION, true).addEvents(AnimationProperty.StaticAnimationProperty.ON_BEGIN_EVENTS, AnimationEvent.create((entitypatch, animation, params) -> {
            Vec3 pos = entitypatch.getOriginal().m_20182_();
            entitypatch.playSound(EpicFightSounds.ROLL.get(), 0.0F, 0.0F);
            entitypatch.getOriginal().m_9236_().addAlwaysVisibleParticle(EpicFightParticles.AIR_BURST.get(), pos.x, pos.y + (double) entitypatch.getOriginal().m_20206_() * 0.5, pos.z, 0.0, -1.0, 2.0);
        }, AnimationEvent.Side.CLIENT)).addEvents(AnimationProperty.StaticAnimationProperty.ON_END_EVENTS, AnimationEvent.create((entitypatch, animation, params) -> {
            if (entitypatch instanceof PlayerPatch<?> playerpatch) {
                playerpatch.changeModelYRot(0.0F);
            }
        }, AnimationEvent.Side.CLIENT));
        BIPED_PHANTOM_ASCENT_BACKWARD = new ActionAnimation(0.05F, 0.7F, "biped/skill/phantom_ascent_backward", biped).addStateRemoveOld(EntityState.MOVEMENT_LOCKED, Boolean.valueOf(false)).newTimePair(0.0F, 0.5F).addStateRemoveOld(EntityState.INACTION, true).addEvents(AnimationProperty.StaticAnimationProperty.ON_BEGIN_EVENTS, AnimationEvent.create((entitypatch, animation, params) -> {
            Vec3 pos = entitypatch.getOriginal().m_20182_();
            entitypatch.playSound(EpicFightSounds.ROLL.get(), 0.0F, 0.0F);
            entitypatch.getOriginal().m_9236_().addAlwaysVisibleParticle(EpicFightParticles.AIR_BURST.get(), pos.x, pos.y + (double) entitypatch.getOriginal().m_20206_() * 0.5, pos.z, 0.0, -1.0, 2.0);
        }, AnimationEvent.Side.CLIENT)).addEvents(AnimationProperty.StaticAnimationProperty.ON_END_EVENTS, AnimationEvent.create((entitypatch, animation, params) -> {
            if (entitypatch instanceof PlayerPatch<?> playerpatch) {
                playerpatch.changeModelYRot(0.0F);
            }
        }, AnimationEvent.Side.CLIENT));
        FIST_AUTO1 = new BasicAttackAnimation(0.08F, 0.0F, 0.11F, 0.16F, InteractionHand.OFF_HAND, null, biped.toolL, "biped/combat/fist_auto1", biped).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT);
        FIST_AUTO2 = new BasicAttackAnimation(0.08F, 0.0F, 0.11F, 0.16F, null, biped.toolR, "biped/combat/fist_auto2", biped).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT);
        FIST_AUTO3 = new BasicAttackAnimation(0.08F, 0.05F, 0.16F, 0.5F, InteractionHand.OFF_HAND, null, biped.toolL, "biped/combat/fist_auto3", biped).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT);
        FIST_DASH = new DashAttackAnimation(0.06F, 0.05F, 0.15F, 0.3F, 0.7F, null, biped.shoulderR, "biped/combat/fist_dash", biped).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE);
        SWORD_AUTO1 = new BasicAttackAnimation(0.1F, 0.0F, 0.1F, 0.4F, null, biped.toolR, "biped/combat/sword_auto1", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.6F));
        SWORD_AUTO2 = new BasicAttackAnimation(0.1F, 0.05F, 0.15F, 0.4F, null, biped.toolR, "biped/combat/sword_auto2", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.6F));
        SWORD_AUTO3 = new BasicAttackAnimation(0.1F, 0.05F, 0.15F, 0.6F, null, biped.toolR, "biped/combat/sword_auto3", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.6F));
        SWORD_DASH = new DashAttackAnimation(0.1F, 0.1F, 0.1F, 0.2F, 0.65F, null, biped.toolR, "biped/combat/sword_dash", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.6F));
        GREATSWORD_AUTO1 = new BasicAttackAnimation(0.25F, 0.15F, 0.25F, 0.65F, null, biped.toolR, "biped/combat/greatsword_auto1", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.0F));
        GREATSWORD_AUTO2 = new BasicAttackAnimation(0.1F, 0.5F, 0.65F, 1.5F, null, biped.toolR, "biped/combat/greatsword_auto2", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.0F));
        GREATSWORD_DASH = new DashAttackAnimation(0.2F, 0.2F, 0.35F, 0.6F, 1.2F, null, biped.toolR, "biped/combat/greatsword_dash", biped, false).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.FINISHER)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.0F).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, false).addEvents(new AnimationEvent.TimeStampedEvent[] { AnimationEvent.TimeStampedEvent.create(0.4F, Animations.ReusableSources.FRACTURE_GROUND_SIMPLE, AnimationEvent.Side.CLIENT).params(new Vec3f(0.0F, -0.24F, -2.0F), Armatures.BIPED.toolR, 1.1, 0.55F) });
        SPEAR_ONEHAND_AUTO = new BasicAttackAnimation(0.1F, 0.35F, 0.45F, 0.75F, null, biped.toolR, "biped/combat/spear_onehand_auto", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.2F));
        SPEAR_TWOHAND_AUTO1 = new BasicAttackAnimation(0.1F, 0.2F, 0.3F, 0.45F, null, biped.toolR, "biped/combat/spear_twohand_auto1", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.2F));
        SPEAR_TWOHAND_AUTO2 = new BasicAttackAnimation(0.1F, 0.2F, 0.3F, 0.7F, null, biped.toolR, "biped/combat/spear_twohand_auto2", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.2F));
        SPEAR_DASH = new DashAttackAnimation(0.1F, 0.25F, 0.3F, 0.4F, 0.8F, null, biped.toolR, "biped/combat/spear_dash", biped);
        TOOL_AUTO1 = new BasicAttackAnimation(0.13F, 0.05F, 0.15F, 0.3F, null, biped.toolR, String.valueOf(SWORD_AUTO1.getId()), biped);
        TOOL_AUTO2 = new BasicAttackAnimation(0.13F, 0.05F, 0.15F, 0.4F, null, biped.toolR, "biped/combat/sword_auto4", biped);
        TOOL_DASH = new DashAttackAnimation(0.16F, 0.08F, 0.15F, 0.25F, 0.58F, null, biped.toolR, "biped/combat/tool_dash", biped).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(1.0F));
        AXE_DASH = new DashAttackAnimation(0.25F, 0.08F, 0.4F, 0.46F, 0.9F, null, biped.toolR, "biped/combat/axe_dash", biped);
        SWORD_DUAL_AUTO1 = new BasicAttackAnimation(0.08F, 0.1F, 0.2F, 0.3F, null, biped.toolR, "biped/combat/sword_dual_auto1", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.6F)).newTimePair(0.0F, 0.2F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false);
        SWORD_DUAL_AUTO2 = new BasicAttackAnimation(0.1F, 0.1F, 0.2F, 0.3F, InteractionHand.OFF_HAND, null, biped.toolL, "biped/combat/sword_dual_auto2", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.6F)).newTimePair(0.0F, 0.2F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false);
        SWORD_DUAL_AUTO3 = new BasicAttackAnimation(0.1F, "biped/combat/sword_dual_auto3", biped, new AttackAnimation.Phase(0.0F, 0.25F, 0.25F, 0.35F, 0.6F, Float.MAX_VALUE, false, InteractionHand.MAIN_HAND, List.of(Pair.of(biped.toolR, null), Pair.of(biped.toolL, null)))).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.6F));
        SWORD_DUAL_DASH = new DashAttackAnimation(0.16F, "biped/combat/sword_dual_dash", biped, new AttackAnimation.Phase(0.0F, 0.05F, 0.05F, 0.3F, 0.75F, Float.MAX_VALUE, false, InteractionHand.MAIN_HAND, List.of(Pair.of(biped.toolR, null), Pair.of(biped.toolL, null)))).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.6F)).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.RAW_COORD).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, null);
        UCHIGATANA_AUTO1 = new BasicAttackAnimation(0.05F, 0.15F, 0.25F, 0.3F, null, biped.toolR, "biped/combat/uchigatana_auto1", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(2.0F));
        UCHIGATANA_AUTO2 = new BasicAttackAnimation(0.05F, 0.2F, 0.3F, 0.3F, null, biped.toolR, "biped/combat/uchigatana_auto2", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(2.0F));
        UCHIGATANA_AUTO3 = new BasicAttackAnimation(0.1F, 0.15F, 0.25F, 0.5F, null, biped.toolR, "biped/combat/uchigatana_auto3", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(2.0F));
        UCHIGATANA_DASH = new DashAttackAnimation(0.1F, 0.05F, 0.05F, 0.15F, 0.6F, null, biped.toolR, "biped/combat/uchigatana_dash", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(2.0F));
        UCHIGATANA_SHEATHING_AUTO = new BasicAttackAnimation(0.05F, 0.0F, 0.06F, 0.65F, ColliderPreset.BATTOJUTSU, biped.rootJoint, "biped/combat/uchigatana_sheath_auto", biped).addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(30.0F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.0F)).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(3.0F)).addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP.get());
        UCHIGATANA_SHEATHING_DASH = new DashAttackAnimation(0.05F, 0.05F, 0.2F, 0.35F, 0.65F, ColliderPreset.BATTOJUTSU_DASH, biped.rootJoint, "biped/combat/uchigatana_sheath_dash", biped).addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(30.0F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.0F)).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(3.0F)).addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP.get());
        AXE_AUTO1 = new BasicAttackAnimation(0.16F, 0.05F, 0.15F, 0.7F, null, biped.toolR, "biped/combat/axe_auto1", biped);
        AXE_AUTO2 = new BasicAttackAnimation(0.16F, 0.05F, 0.15F, 0.85F, null, biped.toolR, "biped/combat/axe_auto2", biped);
        LONGSWORD_AUTO1 = new BasicAttackAnimation(0.1F, 0.25F, 0.35F, 0.5F, null, biped.toolR, "biped/combat/longsword_auto1", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.2F));
        LONGSWORD_AUTO2 = new BasicAttackAnimation(0.15F, 0.2F, 0.3F, 0.45F, null, biped.toolR, "biped/combat/longsword_auto2", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.2F));
        LONGSWORD_AUTO3 = new BasicAttackAnimation(0.05F, 0.2F, 0.3F, 1.0F, null, biped.toolR, "biped/combat/longsword_auto3", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.2F));
        LONGSWORD_DASH = new DashAttackAnimation(0.1F, 0.1F, 0.25F, 0.4F, 0.75F, null, biped.toolR, "biped/combat/longsword_dash", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.2F));
        LONGSWORD_LIECHTENAUER_AUTO1 = new BasicAttackAnimation(0.1F, 0.15F, 0.25F, 0.5F, null, biped.toolR, "biped/combat/longsword_liechtenauer_auto1", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.2F));
        LONGSWORD_LIECHTENAUER_AUTO2 = new BasicAttackAnimation(0.1F, 0.2F, 0.3F, 0.5F, null, biped.toolR, "biped/combat/longsword_liechtenauer_auto2", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.2F));
        LONGSWORD_LIECHTENAUER_AUTO3 = new BasicAttackAnimation(0.25F, 0.1F, 0.2F, 0.7F, null, biped.toolR, "biped/combat/longsword_liechtenauer_auto3", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.2F));
        TACHI_AUTO1 = new BasicAttackAnimation(0.1F, 0.35F, 0.4F, 0.5F, null, biped.toolR, "biped/combat/tachi_auto1", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.2F)).addProperty(AnimationProperty.AttackAnimationProperty.EXTRA_COLLIDERS, 3);
        TACHI_AUTO2 = new BasicAttackAnimation(0.15F, 0.2F, 0.3F, 0.5F, null, biped.toolR, "biped/combat/tachi_auto2", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.2F));
        TACHI_AUTO3 = new BasicAttackAnimation(0.15F, 0.2F, 0.3F, 0.85F, null, biped.toolR, "biped/combat/tachi_auto3", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.2F));
        TACHI_DASH = new DashAttackAnimation(0.05F, 0.3F, 0.3F, 0.4F, 1.0F, null, biped.toolR, "biped/combat/tachi_dash", biped, false).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.2F));
        DAGGER_AUTO1 = new BasicAttackAnimation(0.05F, 0.05F, 0.15F, 0.25F, null, biped.toolR, "biped/combat/dagger_auto1", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(2.4F));
        DAGGER_AUTO2 = new BasicAttackAnimation(0.05F, 0.0F, 0.1F, 0.25F, null, biped.toolR, "biped/combat/dagger_auto2", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(2.4F));
        DAGGER_AUTO3 = new BasicAttackAnimation(0.05F, 0.2F, 0.25F, 0.4F, null, biped.toolR, "biped/combat/dagger_auto3", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(2.4F));
        DAGGER_DASH = new DashAttackAnimation(0.05F, 0.1F, 0.2F, 0.25F, 0.6F, null, biped.toolR, "biped/combat/dagger_dash", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(2.4F)).newTimePair(0.0F, 0.4F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false).newConditionalTimePair(entitypatch -> entitypatch.isLastAttackSuccess() ? 1 : 0, 0.4F, 0.6F).addConditionalState(0, EntityState.CAN_BASIC_ATTACK, false).addConditionalState(1, EntityState.CAN_BASIC_ATTACK, true);
        DAGGER_DUAL_AUTO1 = new BasicAttackAnimation(0.05F, 0.1F, 0.2F, 0.25F, null, biped.toolR, "biped/combat/dagger_dual_auto1", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(2.4F));
        DAGGER_DUAL_AUTO2 = new BasicAttackAnimation(0.05F, 0.0F, 0.11F, 0.16F, InteractionHand.OFF_HAND, null, biped.toolL, "biped/combat/dagger_dual_auto2", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(2.4F));
        DAGGER_DUAL_AUTO3 = new BasicAttackAnimation(0.05F, 0.0F, 0.11F, 0.2F, null, biped.toolR, "biped/combat/dagger_dual_auto3", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(2.4F));
        DAGGER_DUAL_AUTO4 = new BasicAttackAnimation(0.15F, "biped/combat/dagger_dual_auto4", biped, new AttackAnimation.Phase(0.0F, 0.1F, 0.1F, 0.2F, 0.2F, 0.2F, InteractionHand.OFF_HAND, biped.toolL, null), new AttackAnimation.Phase(0.2F, 0.2F, 0.3F, 0.6F, 0.6F, biped.toolR, null)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(2.4F));
        DAGGER_DUAL_DASH = new DashAttackAnimation(0.1F, 0.1F, 0.25F, 0.3F, 0.65F, ColliderPreset.DUAL_DAGGER_DASH, biped.rootJoint, "biped/combat/dagger_dual_dash", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(2.4F)).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true);
        TRIDENT_AUTO1 = new BasicAttackAnimation(0.3F, 0.05F, 0.16F, 0.45F, null, biped.toolR, "biped/combat/trident_auto1", biped);
        TRIDENT_AUTO2 = new BasicAttackAnimation(0.05F, 0.25F, 0.36F, 0.55F, null, biped.toolR, "biped/combat/trident_auto2", biped);
        TRIDENT_AUTO3 = new BasicAttackAnimation(0.2F, 0.3F, 0.46F, 0.9F, null, biped.toolR, "biped/combat/trident_auto3", biped);
        SWORD_AIR_SLASH = new AirSlashAnimation(0.1F, 0.15F, 0.26F, 0.5F, null, biped.toolR, "biped/combat/sword_airslash", biped);
        SWORD_DUAL_AIR_SLASH = new AirSlashAnimation(0.1F, 0.15F, 0.26F, 0.5F, ColliderPreset.DUAL_SWORD_AIR_SLASH, biped.torso, "biped/combat/sword_dual_airslash", biped);
        UCHIGATANA_AIR_SLASH = new AirSlashAnimation(0.1F, 0.05F, 0.16F, 0.3F, null, biped.toolR, "biped/combat/uchigatana_airslash", biped);
        UCHIGATANA_SHEATH_AIR_SLASH = new AirSlashAnimation(0.1F, 0.1F, 0.16F, 0.3F, null, biped.toolR, "biped/combat/uchigatana_sheath_airslash", biped).addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(30.0F)).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(2.0F)).addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP.get()).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.0F);
        SPEAR_ONEHAND_AIR_SLASH = new AirSlashAnimation(0.1F, 0.15F, 0.26F, 0.4F, null, biped.toolR, "biped/combat/spear_onehand_airslash", biped);
        SPEAR_TWOHAND_AIR_SLASH = new AirSlashAnimation(0.1F, 0.25F, 0.36F, 0.6F, null, biped.toolR, "biped/combat/spear_twohand_airslash", biped).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.FINISHER));
        LONGSWORD_AIR_SLASH = new AirSlashAnimation(0.1F, 0.3F, 0.41F, 0.5F, null, biped.toolR, "biped/combat/longsword_airslash", biped);
        GREATSWORD_AIR_SLASH = new AirSlashAnimation(0.1F, 0.5F, 0.55F, 0.71F, 0.75F, false, null, biped.toolR, "biped/combat/greatsword_airslash", biped).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.FINISHER));
        FIST_AIR_SLASH = new AirSlashAnimation(0.1F, 0.15F, 0.26F, 0.4F, null, biped.toolR, "biped/combat/fist_airslash", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(4.0F));
        DAGGER_AIR_SLASH = new AirSlashAnimation(0.1F, 0.15F, 0.26F, 0.45F, null, biped.toolR, "biped/combat/dagger_airslash", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(2.4F));
        DAGGER_DUAL_AIR_SLASH = new AirSlashAnimation(0.1F, 0.15F, 0.26F, 0.4F, ColliderPreset.DUAL_DAGGER_AIR_SLASH, biped.torso, String.valueOf(SWORD_DUAL_AIR_SLASH.getId()), biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(2.0F));
        AXE_AIRSLASH = new AirSlashAnimation(0.1F, 0.3F, 0.4F, 0.65F, null, biped.toolR, "biped/combat/axe_airslash", biped);
        SWORD_MOUNT_ATTACK = new MountAttackAnimation(0.16F, 0.1F, 0.2F, 0.25F, 0.7F, null, biped.toolR, "biped/combat/sword_mount_attack", biped);
        SPEAR_MOUNT_ATTACK = new MountAttackAnimation(0.16F, 0.38F, 0.38F, 0.45F, 0.8F, null, biped.toolR, "biped/combat/spear_mount_attack", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        BIPED_MOB_ONEHAND1 = new AttackAnimation(0.08F, 0.45F, 0.55F, 0.66F, 0.95F, null, biped.toolR, "biped/combat/mob_onehand1", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        BIPED_MOB_ONEHAND2 = new AttackAnimation(0.08F, 0.45F, 0.5F, 0.61F, 0.95F, null, biped.toolR, "biped/combat/mob_onehand2", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        BIPED_MOB_GREATSWORD = new AttackAnimation(0.15F, 0.45F, 0.85F, 0.95F, 2.2F, null, biped.toolR, "biped/combat/mob_greatsword1", biped).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.KNOCKDOWN).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        BIPED_MOB_TACHI = new AttackAnimation(0.15F, 0.15F, 0.25F, 0.35F, 1.0F, null, biped.toolR, "biped/combat/mob_tachi_special", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        BIPED_MOB_SPEAR_ONEHAND = new AttackAnimation(0.15F, 0.15F, 0.4F, 0.5F, 1.1F, null, biped.toolR, "biped/combat/mob_spear_onehand", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        BIPED_MOB_SPEAR_TWOHAND1 = new AttackAnimation(0.15F, 0.15F, 0.4F, 0.5F, 1.0F, null, biped.toolR, "biped/combat/mob_spear_twohand1", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        BIPED_MOB_SPEAR_TWOHAND2 = new AttackAnimation(0.15F, 0.15F, 0.4F, 0.5F, 1.0F, null, biped.toolR, "biped/combat/mob_spear_twohand2", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        BIPED_MOB_SPEAR_TWOHAND3 = new AttackAnimation(0.15F, 0.15F, 0.4F, 0.5F, 1.0F, null, biped.toolR, "biped/combat/mob_spear_twohand3", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        BIPED_MOB_SWORD_DUAL1 = new AttackAnimation(0.1F, "biped/combat/mob_sword_dual1", biped, new AttackAnimation.Phase(0.0F, 0.35F, 0.4F, 0.5F, 0.55F, 0.55F, InteractionHand.OFF_HAND, biped.toolL, null), new AttackAnimation.Phase(0.55F, 0.55F, 0.65F, 0.75F, 1.15F, Float.MAX_VALUE, biped.toolR, null)).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        BIPED_MOB_SWORD_DUAL2 = new AttackAnimation(0.1F, "biped/combat/mob_sword_dual2", biped, new AttackAnimation.Phase(0.0F, 0.3F, 0.3F, 0.45F, 0.55F, 0.55F, InteractionHand.OFF_HAND, biped.toolL, null), new AttackAnimation.Phase(0.55F, 0.55F, 0.65F, 0.75F, 1.15F, Float.MAX_VALUE, biped.toolR, null)).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        BIPED_MOB_SWORD_DUAL3 = new AttackAnimation(0.1F, 0.25F, 0.85F, 0.95F, 1.4F, null, biped.toolR, "biped/combat/mob_sword_dual3", biped).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true);
        BIPED_MOB_LONGSWORD1 = new AttackAnimation(0.15F, "biped/combat/mob_longsword1", biped, new AttackAnimation.Phase(0.0F, 0.15F, 0.25F, 0.35F, 0.45F, 0.65F, biped.toolR, null), new AttackAnimation.Phase(0.65F, 0.85F, 1.0F, 1.1F, 1.55F, Float.MAX_VALUE, biped.toolR, null)).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        BIPED_MOB_LONGSWORD2 = new AttackAnimation(0.25F, 0.3F, 0.45F, 0.55F, 1.0F, null, biped.toolR, "biped/combat/mob_longsword2", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        BIPED_MOB_UCHIGATANA1 = new AttackAnimation(0.05F, 0.3F, 0.2F, 0.3F, 0.7F, null, biped.toolR, "biped/combat/mob_uchigatana1", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        BIPED_MOB_UCHIGATANA2 = new AttackAnimation(0.15F, 0.01F, 0.01F, 0.1F, 0.55F, null, biped.toolR, "biped/combat/mob_uchigatana2", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        BIPED_MOB_UCHIGATANA3 = new AttackAnimation(0.15F, 0.01F, 0.1F, 0.2F, 0.7F, null, biped.toolR, "biped/combat/mob_uchigatana3", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        BIPED_MOB_DAGGER_ONEHAND1 = new AttackAnimation(0.1F, 0.05F, 0.15F, 0.25F, 0.4F, null, biped.toolR, "biped/combat/mob_dagger_onehand1", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        BIPED_MOB_DAGGER_ONEHAND2 = new AttackAnimation(0.1F, 0.05F, 0.01F, 0.1F, 0.45F, null, biped.toolR, "biped/combat/mob_dagger_onehand2", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        BIPED_MOB_DAGGER_ONEHAND3 = new AttackAnimation(0.1F, 0.3F, 0.5F, 0.6F, 0.9F, null, biped.toolR, "biped/combat/mob_dagger_onehand3", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        BIPED_MOB_DAGGER_TWOHAND1 = new AttackAnimation(0.15F, "biped/combat/mob_dagger_twohand1", biped, new AttackAnimation.Phase(0.0F, 0.0F, 0.05F, 0.15F, 0.3F, 0.3F, biped.toolR, null), new AttackAnimation.Phase(0.3F, 0.3F, 0.3F, 0.4F, 0.5F, 0.5F, InteractionHand.OFF_HAND, biped.toolL, null), new AttackAnimation.Phase(0.5F, 0.5F, 0.55F, 0.65F, 1.0F, Float.MAX_VALUE, biped.toolR, null)).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        BIPED_MOB_DAGGER_TWOHAND2 = new AttackAnimation(0.1F, 0.25F, 0.75F, 0.85F, 1.0F, null, biped.toolR, "biped/combat/mob_dagger_twohand2", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        BIPED_MOB_THROW = new RangedAttackAnimation(0.11F, 0.1F, 0.45F, 0.49F, 0.95F, null, biped.rootJoint, "biped/combat/mob_throw", biped);
        SWORD_GUARD_HIT = new GuardAnimation(0.05F, "biped/skill/guard_sword_hit", biped);
        SWORD_GUARD_ACTIVE_HIT1 = new GuardAnimation(0.05F, 0.2F, "biped/skill/guard_sword_hit_active1", biped);
        SWORD_GUARD_ACTIVE_HIT2 = new GuardAnimation(0.05F, 0.2F, "biped/skill/guard_sword_hit_active2", biped);
        SWORD_GUARD_ACTIVE_HIT3 = new GuardAnimation(0.05F, 0.2F, "biped/skill/guard_sword_hit_active3", biped);
        LONGSWORD_GUARD_ACTIVE_HIT1 = new GuardAnimation(0.05F, 0.2F, "biped/skill/guard_longsword_hit_active1", biped);
        LONGSWORD_GUARD_ACTIVE_HIT2 = new GuardAnimation(0.05F, 0.2F, "biped/skill/guard_longsword_hit_active2", biped);
        SWORD_DUAL_GUARD_HIT = new GuardAnimation(0.05F, "biped/skill/guard_dualsword_hit", biped);
        BIPED_COMMON_NEUTRALIZED = new LongHitAnimation(0.05F, "biped/skill/guard_break1", biped);
        GREATSWORD_GUARD_BREAK = new LongHitAnimation(0.05F, "biped/skill/guard_break2", biped);
        LONGSWORD_GUARD_HIT = new GuardAnimation(0.05F, "biped/skill/guard_longsword_hit", biped);
        SPEAR_GUARD_HIT = new GuardAnimation(0.05F, "biped/skill/guard_spear_hit", biped);
        GREATSWORD_GUARD_HIT = new GuardAnimation(0.05F, "biped/skill/guard_greatsword_hit", biped);
        UCHIGATANA_GUARD_HIT = new GuardAnimation(0.05F, "biped/skill/guard_uchigatana_hit", biped);
        METEOR_SLAM = new AttackAnimation(0.05F, 0.0F, 0.2F, 0.3F, 1.0F, ColliderPreset.GREATSWORD, biped.toolR, "biped/skill/greatsword_slam", biped).addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.NO_SOUND.get()).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_ON_LINK, false).addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, true).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, (self, entitypatch, transformSheet) -> {
            HitResult hitResult = entitypatch.getOriginal().m_19907_(50.0, 1.0F, false);
            Vec3 to = hitResult.getLocation();
            Vec3 from = entitypatch.getOriginal().m_20182_();
            Vec3 correction = to.subtract(from).normalize().scale(2.0);
            TransformSheet correctedCoord = self.getCoord().getCorrectedModelCoord(entitypatch, from, to.add(correction), 0, 2);
            transformSheet.readFrom(correctedCoord);
        }).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, null).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, elapsedTime) -> {
            if (0.2F > elapsedTime && entitypatch instanceof PlayerPatch<?> playerpatch) {
                SkillContainer skill = playerpatch.getSkill(EpicFightSkills.METEOR_STRIKE);
                if (skill != null) {
                    return (float) Math.sqrt((double) (7.0F / MeteorSlamSkill.getFallDistance(skill)));
                }
            }
            return 1.0F;
        }).addEvents(AnimationProperty.StaticAnimationProperty.ON_BEGIN_EVENTS, AnimationEvent.create((entitypatch, animation, params) -> entitypatch.playSound(EpicFightSounds.ENTITY_MOVE.get(), 1.0F, 0.0F, 0.0F), AnimationEvent.Side.CLIENT)).addEvents(AnimationEvent.TimeStampedEvent.create(0.25F, Animations.ReusableSources.FRACTURE_METEOR_STRIKE, AnimationEvent.Side.SERVER).params(new Vec3f(0.0F, -0.2F, -1.8F), Armatures.BIPED.toolR, 0.3F));
        REVELATION_ONEHAND = new AttackAnimation(0.05F, 0.0F, 0.05F, 0.1F, 0.35F, ColliderPreset.FIST, biped.legR, "biped/skill/revelation_normal", biped).addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH.get()).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT.get()).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.COUNTER)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.NEUTRALIZE).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(1.0F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(0.5F)).addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.setter(0.0F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(2.0F)).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_LOCROT_TARGET).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, MoveCoordFunctions.TRACE_LOCROT_TARGET).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE);
        REVELATION_TWOHAND = new AttackAnimation(0.1F, 0.0F, 0.05F, 0.1F, 0.35F, ColliderPreset.FIST_FIXED, biped.rootJoint, "biped/skill/revelation_twohand", biped).addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH.get()).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT.get()).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.COUNTER)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.NEUTRALIZE).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(1.0F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(0.5F)).addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.setter(0.0F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(2.0F)).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_LOCROT_TARGET).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, MoveCoordFunctions.TRACE_LOCROT_TARGET).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE);
        BIPED_HIT_SHORT = new HitAnimation(0.05F, "biped/combat/hit_short", biped);
        BIPED_HIT_LONG = new LongHitAnimation(0.08F, "biped/combat/hit_long", biped);
        BIPED_HIT_ON_MOUNT = new LongHitAnimation(0.08F, "biped/combat/hit_on_mount", biped);
        BIPED_LANDING = new LongHitAnimation(0.03F, "biped/living/landing", biped);
        BIPED_KNOCKDOWN = new KnockdownAnimation(0.08F, 2.1F, "biped/combat/knockdown", biped);
        BIPED_DEATH = new LongHitAnimation(0.16F, "biped/living/death", biped);
        CREEPER_HIT_SHORT = new HitAnimation(0.05F, "creeper/hit_short", creeper);
        CREEPER_HIT_LONG = new LongHitAnimation(0.08F, "creeper/hit_long", creeper);
        CREEPER_DEATH = new LongHitAnimation(0.16F, "creeper/death", creeper);
        ENDERMAN_HIT_SHORT = new HitAnimation(0.05F, "enderman/hit_short", enderman);
        ENDERMAN_HIT_LONG = new LongHitAnimation(0.08F, "enderman/hit_long", enderman);
        ENDERMAN_NEUTRALIZED = new LongHitAnimation(0.18F, "enderman/neutralized", enderman);
        ENDERMAN_CONVERT_RAGE = new InvincibleAnimation(0.16F, "enderman/convert_rage", enderman);
        ENDERMAN_TP_KICK1 = new AttackAnimation(0.06F, 0.15F, 0.3F, 0.4F, 1.0F, ColliderPreset.ENDERMAN_LIMB, enderman.legR, "enderman/tp_kick1", enderman);
        ENDERMAN_TP_KICK2 = new AttackAnimation(0.16F, 0.15F, 0.25F, 0.45F, 1.0F, ColliderPreset.ENDERMAN_LIMB, enderman.legR, "enderman/tp_kick2", enderman);
        ENDERMAN_KICK1 = new AttackAnimation(0.16F, 0.66F, 0.7F, 0.81F, 1.6F, ColliderPreset.ENDERMAN_LIMB, enderman.legL, "enderman/rush_kick", enderman).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(4.0F));
        ENDERMAN_KICK2 = new AttackAnimation(0.16F, 0.8F, 0.8F, 0.9F, 1.3F, ColliderPreset.ENDERMAN_LIMB, enderman.legR, "enderman/jump_kick", enderman);
        ENDERMAN_KNEE = new AttackAnimation(0.16F, 0.25F, 0.25F, 0.31F, 1.0F, ColliderPreset.FIST, enderman.legR, "enderman/knee", enderman).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.LONG);
        ENDERMAN_KICK_COMBO = new AttackAnimation(0.1F, "enderman/kick_twice", enderman, new AttackAnimation.Phase(0.0F, 0.15F, 0.15F, 0.21F, 0.46F, 0.6F, enderman.legR, ColliderPreset.ENDERMAN_LIMB), new AttackAnimation.Phase(0.6F, 0.75F, 0.75F, 0.81F, 1.6F, Float.MAX_VALUE, enderman.legL, ColliderPreset.ENDERMAN_LIMB)).addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, true);
        ENDERMAN_GRASP = new AttackAnimation(0.06F, 0.5F, 0.45F, 1.0F, 1.0F, ColliderPreset.ENDERMAN_LIMB, biped.toolR, "enderman/grasp", enderman).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        ENDERMAN_DEATH = new LongHitAnimation(0.16F, "enderman/death", enderman);
        ENDERMAN_TP_EMERGENCE = new ActionAnimation(0.05F, "enderman/teleport", enderman).addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, true);
        DRAGON_IDLE = new StaticAnimation(0.6F, true, "dragon/idle", dragon);
        DRAGON_WALK = new EnderDraonWalkAnimation(0.35F, "dragon/walk", dragon, new IKInfo[] { IKInfo.make(dragon.legFrontL1, dragon.legFrontL3, dragon.legFrontR3, Pair.of(0, 3), 0.12F, 0, new boolean[] { true, true, true }), IKInfo.make(dragon.legFrontR1, dragon.legFrontR3, dragon.legFrontL3, Pair.of(2, 5), 0.12F, 2, new boolean[] { true, true, true }), IKInfo.make(dragon.legBackL1, dragon.legBackL3, dragon.legBackR3, Pair.of(2, 5), 0.1344F, 4, new boolean[] { true, true, true }), IKInfo.make(dragon.legBackR1, dragon.legBackR3, dragon.legBackL3, Pair.of(0, 3), 0.1344F, 2, new boolean[] { true, true, true }) });
        DRAGON_FLY = new StaticAnimation(0.35F, true, "dragon/fly", dragon).addEvents(AnimationEvent.TimeStampedEvent.create(0.4F, Animations.ReusableSources.WING_FLAP, AnimationEvent.Side.CLIENT));
        DRAGON_DEATH = new EnderDragonDeathAnimation(1.0F, "dragon/death", dragon);
        DRAGON_GROUND_TO_FLY = new EnderDragonActionAnimation(0.25F, "dragon/ground_to_fly", dragon, new IKInfo[] { IKInfo.make(dragon.legFrontL1, dragon.legFrontL3, null, Pair.of(3, 7), 0.12F, 0, new boolean[] { true, false, false, false }), IKInfo.make(dragon.legFrontR1, dragon.legFrontR3, null, Pair.of(3, 7), 0.12F, 0, new boolean[] { true, false, false, false }), IKInfo.make(dragon.legBackL1, dragon.legBackL3, null, Pair.of(4, 7), 0.1344F, 0, new boolean[] { true, false, false, false }), IKInfo.make(dragon.legBackR1, dragon.legBackR3, null, Pair.of(4, 7), 0.1344F, 0, new boolean[] { true, false, false, false }) }).addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, Boolean.valueOf(true)).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true).addEvents(new AnimationEvent.TimeStampedEvent[] { AnimationEvent.TimeStampedEvent.create(0.25F, Animations.ReusableSources.WING_FLAP, AnimationEvent.Side.CLIENT), AnimationEvent.TimeStampedEvent.create(1.05F, Animations.ReusableSources.WING_FLAP, AnimationEvent.Side.CLIENT), AnimationEvent.TimeStampedEvent.create(1.45F, (entitypatch, animation, params) -> {
            if (entitypatch instanceof EnderDragonPatch enderDragonPatch) {
                enderDragonPatch.setFlyingPhase();
            }
        }, AnimationEvent.Side.BOTH) });
        DRAGON_FLY_TO_GROUND = new EnderDragonDynamicActionAnimation(0.35F, "dragon/fly_to_ground", dragon, new IKInfo[] { IKInfo.make(dragon.legFrontL1, dragon.legFrontL3, null, Pair.of(0, 4), 0.12F, 9, new boolean[] { false, false, false, true }), IKInfo.make(dragon.legFrontR1, dragon.legFrontR3, null, Pair.of(0, 4), 0.12F, 9, new boolean[] { false, false, false, true }), IKInfo.make(dragon.legBackL1, dragon.legBackL3, null, Pair.of(0, 4), 0.1344F, 7, new boolean[] { false, false, false, true }), IKInfo.make(dragon.legBackR1, dragon.legBackR3, null, Pair.of(0, 4), 0.1344F, 7, new boolean[] { false, false, false, true }) }).addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, Boolean.valueOf(true)).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_ON_LINK, false).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_TIME, TimePairList.create(0.0F, 1.35F)).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, (self, entitypatch, transformSheet) -> {
            if (entitypatch instanceof EnderDragonPatch dragonpatch) {
                TransformSheet transform = self.getCoord().copyAll();
                Vec3 dragonpos = dragonpatch.getOriginal().m_20182_();
                Vec3 targetpos = dragonpatch.getOriginal().getPhaseManager().getPhase(PatchedPhases.LANDING).getLandingPosition();
                float horizontalDistance = (float) dragonpos.subtract(0.0, dragonpos.y, 0.0).distanceTo(targetpos.subtract(0.0, targetpos.y, 0.0));
                float verticalDistance = (float) Math.abs(dragonpos.y - targetpos.y);
                JointTransform jt0 = transform.getKeyframes()[0].transform();
                JointTransform jt1 = transform.getKeyframes()[1].transform();
                JointTransform jt2 = transform.getKeyframes()[2].transform();
                OpenMatrix4f coordReverse = OpenMatrix4f.createRotatorDeg(90.0F, Vec3f.X_AXIS);
                Vec3f jointCoord = OpenMatrix4f.transform3v(coordReverse, new Vec3f(jt0.translation().x, verticalDistance, horizontalDistance), null);
                jt0.translation().set(jointCoord);
                jt1.translation().set(MathUtils.lerpVector(jt0.translation(), jt2.translation(), transform.getKeyframes()[1].time() / transform.getKeyframes()[2].time()));
                transformSheet.readFrom(transform);
            }
        }).addEvents(new AnimationEvent.TimeStampedEvent[] { AnimationEvent.TimeStampedEvent.create(0.3F, Animations.ReusableSources.WING_FLAP, AnimationEvent.Side.CLIENT), AnimationEvent.TimeStampedEvent.create(1.1F, (entitypatch, animation, params) -> {
            entitypatch.playSound(EpicFightSounds.GROUND_SLAM.get(), 0.0F, 0.0F);
            LivingEntity original = entitypatch.getOriginal();
            BlockPos blockpos = original.m_9236_().m_5452_(Heightmap.Types.WORLD_SURFACE, original.m_20183_());
            original.m_9236_().addParticle(EpicFightParticles.GROUND_SLAM.get(), (double) blockpos.m_123341_(), (double) blockpos.m_123342_(), (double) blockpos.m_123343_(), 3.0, 100.0, 1.0);
        }, AnimationEvent.Side.CLIENT), AnimationEvent.TimeStampedEvent.create(1.1F, (entitypatch, animation, params) -> {
            LivingEntity original = entitypatch.getOriginal();
            EpicFightDamageSources damageSources = EpicFightDamageSources.of(original.m_9236_());
            DamageSource extDamageSource = damageSources.mobAttack(original).setAnimation(DRAGON_FLY_TO_GROUND).setStunType(StunType.KNOCKDOWN);
            for (Entity entity : original.m_9236_().m_45933_(original, original.m_20191_().deflate(3.0, 0.0, 3.0))) {
                entity.hurt(extDamageSource, 6.0F);
            }
        }, AnimationEvent.Side.SERVER) });
        DRAGON_ATTACK1 = new EnderDragonAttackAnimation(0.35F, 0.4F, 0.65F, 0.76F, 1.9F, ColliderPreset.DRAGON_LEG, dragon.legFrontR3, "dragon/attack1", dragon, new IKInfo[] { IKInfo.make(dragon.legFrontL1, dragon.legFrontL3, null, Pair.of(2, 4), 0.12F, 0, new boolean[] { true, true }), IKInfo.make(dragon.legFrontR1, dragon.legFrontR3, null, Pair.of(0, 5), 0.12F, 0, new boolean[] { false, false, false, false, true }), IKInfo.make(dragon.legBackL1, dragon.legBackL3, null, null, 0.1344F, 0, new boolean[0]), IKInfo.make(dragon.legBackR1, dragon.legBackR3, null, Pair.of(1, 4), 0.1344F, 0, new boolean[] { true, false, true }) }).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.KNOCKDOWN).addEvents(new AnimationEvent.TimeStampedEvent[] { AnimationEvent.TimeStampedEvent.create(0.65F, (entitypatch, animation, params) -> {
            entitypatch.playSound(EpicFightSounds.GROUND_SLAM.get(), 0.0F, 0.0F);
            if (entitypatch instanceof EnderDragonPatch dragonpatch) {
                Vec3f tipPosition = dragonpatch.getTipPointAnimation(dragon.legFrontR3.getName()).getTargetPosition();
                LivingEntity original = entitypatch.getOriginal();
                original.m_9236_().addParticle(EpicFightParticles.GROUND_SLAM.get(), (double) tipPosition.x, (double) tipPosition.y, (double) tipPosition.z, 0.5, 100.0, 0.5);
            }
        }, AnimationEvent.Side.CLIENT) });
        DRAGON_ATTACK2 = new EnderDragonAttackAnimation(0.35F, 0.25F, 0.45F, 0.66F, 0.75F, ColliderPreset.DRAGON_LEG, dragon.legFrontR3, "dragon/attack2", dragon, new IKInfo[] { IKInfo.make(dragon.legFrontL1, dragon.legFrontL3, null, Pair.of(1, 4), 0.12F, 0, new boolean[] { true, true, true }), IKInfo.make(dragon.legBackL1, dragon.legBackL3, null, null, 0.1344F, 0, new boolean[0]), IKInfo.make(dragon.legBackR1, dragon.legBackR3, null, null, 0.1344F, 0, new boolean[0]) });
        DRAGON_ATTACK3 = new EnderDragonAttackAnimation(0.35F, 0.25F, 0.45F, 0.66F, 0.75F, ColliderPreset.DRAGON_LEG, dragon.legFrontL3, "dragon/attack3", dragon, new IKInfo[] { IKInfo.make(dragon.legFrontR1, dragon.legFrontR3, null, Pair.of(1, 4), 0.12F, 0, new boolean[] { true, true, true }), IKInfo.make(dragon.legBackL1, dragon.legBackL3, null, null, 0.1344F, 0, new boolean[0]), IKInfo.make(dragon.legBackR1, dragon.legBackR3, null, null, 0.1344F, 0, new boolean[0]) });
        DRAGON_ATTACK4 = new EnderDragonAttackAnimation(0.35F, 0.5F, 1.15F, 1.26F, 1.9F, ColliderPreset.DRAGON_BODY, dragon.rootJoint, "dragon/attack4", dragon, new IKInfo[] { IKInfo.make(dragon.legFrontL1, dragon.legFrontL3, null, Pair.of(0, 7), 0.12F, 0, new boolean[] { false, false, false, false, true, true, true }), IKInfo.make(dragon.legFrontR1, dragon.legFrontR3, null, Pair.of(0, 7), 0.12F, 0, new boolean[] { false, false, false, false, true, true, true }), IKInfo.make(dragon.legBackL1, dragon.legBackL3, null, Pair.of(3, 8), 0.1344F, 0, new boolean[] { false, false, false, false, true }), IKInfo.make(dragon.legBackR1, dragon.legBackR3, null, Pair.of(3, 8), 0.1344F, 0, new boolean[] { false, false, false, false, true }) }).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.KNOCKDOWN).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true).addEvents(new AnimationEvent.TimeStampedEvent[] { AnimationEvent.TimeStampedEvent.create(1.2F, (entitypatch, animation, params) -> {
            entitypatch.playSound(EpicFightSounds.GROUND_SLAM.get(), 0.0F, 0.0F);
            if (entitypatch instanceof EnderDragonPatch dragonpatch) {
                Vec3f tipPosition = dragonpatch.getTipPointAnimation(dragon.legFrontR3.getName()).getTargetPosition();
                LivingEntity original = entitypatch.getOriginal();
                original.m_9236_().addParticle(EpicFightParticles.GROUND_SLAM.get(), (double) tipPosition.x, (double) tipPosition.y, (double) tipPosition.z, 3.0, 100.0, 1.0);
            }
        }, AnimationEvent.Side.CLIENT), AnimationEvent.TimeStampedEvent.create(1.85F, (entitypatch, animation, params) -> entitypatch.<Animator>getAnimator().reserveAnimation(DRAGON_ATTACK4_RECOVERY), AnimationEvent.Side.BOTH) });
        DRAGON_ATTACK4_RECOVERY = new EnderDragonActionAnimation(0.35F, "dragon/attack4_recovery", dragon, new IKInfo[] { IKInfo.make(dragon.legFrontL1, dragon.legFrontL3, null, Pair.of(0, 4), 0.12F, 0, new boolean[] { true, false, true, true }), IKInfo.make(dragon.legFrontR1, dragon.legFrontR3, null, Pair.of(0, 3), 0.12F, 0, new boolean[] { true, true, true }), IKInfo.make(dragon.legBackL1, dragon.legBackL3, null, Pair.of(0, 5), 0.1344F, 0, new boolean[] { true, true, false, false, true }), IKInfo.make(dragon.legBackR1, dragon.legBackR3, null, Pair.of(0, 4), 0.1344F, 0, new boolean[] { true, true, false, false }) });
        DRAGON_FIREBALL = new EnderDragonActionAnimation(0.16F, "dragon/fireball", dragon, new IKInfo[] { IKInfo.make(dragon.legFrontL1, dragon.legFrontL3, null, Pair.of(0, 5), 0.12F, 0, new boolean[] { true, true, true, true, true }), IKInfo.make(dragon.legFrontR1, dragon.legFrontR3, null, Pair.of(0, 5), 0.12F, 0, new boolean[] { true, true, true, true, true }), IKInfo.make(dragon.legBackL1, dragon.legBackL3, null, Pair.of(0, 5), 0.1344F, 0, new boolean[] { true, true, true, true, true }), IKInfo.make(dragon.legBackR1, dragon.legBackR3, null, Pair.of(0, 5), 0.1344F, 0, new boolean[] { true, true, true, true, true }) }).addEvents(new AnimationEvent.TimeStampedEvent[] { AnimationEvent.TimeStampedEvent.create(0.65F, (entitypatch, animation, params) -> {
            LivingEntity original = entitypatch.getOriginal();
            Entity target = entitypatch.getTarget();
            Vec3 pos = original.m_20182_();
            Vec3 toTarget = target.position().subtract(original.m_20182_()).normalize().scale((double) original.m_20205_() * 0.5);
            double d6 = (double) ((float) (pos.x + toTarget.x));
            double d7 = (double) ((float) (pos.y + 2.0));
            double d8 = (double) ((float) (pos.z + toTarget.z));
            double d9 = target.getX() - d6;
            double d10 = target.getY(0.5) - d7;
            double d11 = target.getZ() - d8;
            if (!original.m_20067_()) {
                original.m_9236_().m_5898_(null, 1017, original.m_20183_(), 0);
            }
            DragonFireball dragonfireball = new DragonFireball(original.m_9236_(), original, d9, d10, d11);
            dragonfireball.m_7678_(d6, d7, d8, 0.0F, 0.0F);
            original.m_9236_().m_7967_(dragonfireball);
        }, AnimationEvent.Side.SERVER) });
        DRAGON_AIRSTRIKE = new StaticAnimation(0.35F, true, "dragon/airstrike", dragon).addEvents(AnimationEvent.TimeStampedEvent.create(0.3F, Animations.ReusableSources.WING_FLAP, AnimationEvent.Side.CLIENT));
        DRAGON_BACKJUMP_PREPARE = new EnderDragonActionAnimation(0.35F, "dragon/backjump_prepare", dragon, new IKInfo[] { IKInfo.make(dragon.legFrontL1, dragon.legFrontL3, null, Pair.of(0, 4), 0.12F, 0, new boolean[] { true, true, true, true }), IKInfo.make(dragon.legFrontR1, dragon.legFrontR3, null, Pair.of(0, 4), 0.12F, 0, new boolean[] { true, true, true, true }), IKInfo.make(dragon.legBackL1, dragon.legBackL3, null, Pair.of(0, 4), 0.1344F, 0, new boolean[] { true, true, true, true }), IKInfo.make(dragon.legBackR1, dragon.legBackR3, null, Pair.of(0, 4), 0.1344F, 0, new boolean[] { true, true, true, true }) }).addEvents(new AnimationEvent.TimeStampedEvent[] { AnimationEvent.TimeStampedEvent.create(0.3F, (entitypatch, animation, params) -> entitypatch.<Animator>getAnimator().reserveAnimation(DRAGON_BACKJUMP_MOVE), AnimationEvent.Side.BOTH) });
        DRAGON_BACKJUMP_MOVE = new AttackAnimation(0.0F, 10.0F, 10.0F, 10.0F, 10.0F, ColliderPreset.FIST, dragon.rootJoint, "dragon/backjump_move", dragon).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.RAW_COORD).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, null).addEvents(new AnimationEvent.TimeStampedEvent[] { AnimationEvent.TimeStampedEvent.create(1.0F, (entitypatch, animation, params) -> entitypatch.<Animator>getAnimator().reserveAnimation(DRAGON_BACKJUMP_RECOVERY), AnimationEvent.Side.BOTH) });
        DRAGON_BACKJUMP_RECOVERY = new EnderDragonActionAnimation(0.0F, "dragon/backjump_recovery", dragon, new IKInfo[] { IKInfo.make(dragon.legFrontL1, dragon.legFrontL3, null, Pair.of(0, 4), 0.12F, 0, new boolean[] { false, true, true, true }), IKInfo.make(dragon.legFrontR1, dragon.legFrontR3, null, Pair.of(0, 4), 0.12F, 0, new boolean[] { false, true, true, true }), IKInfo.make(dragon.legBackL1, dragon.legBackL3, null, Pair.of(0, 4), 0.1344F, 0, new boolean[] { true, true, true, true }), IKInfo.make(dragon.legBackR1, dragon.legBackR3, null, Pair.of(0, 4), 0.1344F, 0, new boolean[] { true, true, true, true }) }).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, Boolean.valueOf(true)).addEvents(new AnimationEvent.TimeStampedEvent[] { AnimationEvent.TimeStampedEvent.create(0.15F, (entitypatch, animation, params) -> {
            entitypatch.playSound(EpicFightSounds.GROUND_SLAM.get(), 0.0F, 0.0F);
            if (entitypatch instanceof EnderDragonPatch dragonpatch) {
                Vec3f tipPosition = dragonpatch.getTipPointAnimation(dragon.legFrontR3.getName()).getTargetPosition();
                LivingEntity original = entitypatch.getOriginal();
                original.m_9236_().addParticle(EpicFightParticles.GROUND_SLAM.get(), (double) tipPosition.x, (double) tipPosition.y, (double) tipPosition.z, 3.0, 100.0, 1.0);
            }
        }, AnimationEvent.Side.CLIENT) });
        DRAGON_CRYSTAL_LINK = new EnderDragonActionAnimation(0.5F, "dragon/crystal_link", dragon, new IKInfo[] { IKInfo.make(dragon.legFrontL1, dragon.legFrontL3, null, Pair.of(0, 2), 0.12F, 0, new boolean[] { true, true }), IKInfo.make(dragon.legFrontR1, dragon.legFrontR3, null, Pair.of(0, 2), 0.12F, 0, new boolean[] { true, true }), IKInfo.make(dragon.legBackL1, dragon.legBackL3, null, Pair.of(0, 2), 0.1344F, 0, new boolean[] { true, true }), IKInfo.make(dragon.legBackR1, dragon.legBackR3, null, Pair.of(0, 2), 0.1344F, 0, new boolean[] { true, true }) }).addEvents(new AnimationEvent.TimeStampedEvent[] { AnimationEvent.TimeStampedEvent.create(7.0F, (entitypatch, animation, params) -> {
            entitypatch.getOriginal().m_5496_(SoundEvents.ENDER_DRAGON_GROWL, 7.0F, 0.8F + entitypatch.getOriginal().getRandom().nextFloat() * 0.3F);
            entitypatch.getOriginal().setHealth(entitypatch.getOriginal().getMaxHealth());
            if (entitypatch instanceof EnderDragonPatch dragonpatch) {
                dragonpatch.getOriginal().getPhaseManager().setPhase(PatchedPhases.GROUND_BATTLE);
                dragonpatch.setStunShield(0.0F);
            }
        }, AnimationEvent.Side.SERVER), AnimationEvent.TimeStampedEvent.create(7.0F, (entitypatch, animation, params) -> {
            Entity original = entitypatch.getOriginal();
            original.level().addParticle(EpicFightParticles.FORCE_FIELD_END.get(), original.getX(), original.getY() + 2.0, original.getZ(), 0.0, 0.0, 0.0);
        }, AnimationEvent.Side.CLIENT) });
        DRAGON_NEUTRALIZED = new EnderDragonActionAnimation(0.1F, "dragon/neutralized", dragon, new IKInfo[] { IKInfo.make(dragon.legFrontL1, dragon.legFrontL3, null, Pair.of(0, 4), 0.12F, 0, new boolean[] { true, true, true, true }), IKInfo.make(dragon.legFrontR1, dragon.legFrontR3, null, Pair.of(0, 4), 0.12F, 0, new boolean[] { true, true, true, true }), IKInfo.make(dragon.legBackL1, dragon.legBackL3, null, Pair.of(0, 4), 0.1344F, 0, new boolean[] { true, true, true, true }), IKInfo.make(dragon.legBackR1, dragon.legBackR3, null, Pair.of(0, 4), 0.1344F, 0, new boolean[] { true, true, true, true }) }).addEvents(new AnimationEvent.TimeStampedEvent[] { AnimationEvent.TimeStampedEvent.create(3.95F, (entitypatch, animation, params) -> entitypatch.<Animator>getAnimator().playAnimation(DRAGON_NEUTRALIZED_RECOVERY, 0.0F), AnimationEvent.Side.BOTH) });
        DRAGON_NEUTRALIZED_RECOVERY = new EnderDragonActionAnimation(0.05F, "dragon/neutralized_recovery", dragon, new IKInfo[] { IKInfo.make(dragon.legFrontL1, dragon.legFrontL3, null, Pair.of(0, 5), 0.12F, 0, new boolean[] { true, true, true, false, true }), IKInfo.make(dragon.legFrontR1, dragon.legFrontR3, null, Pair.of(0, 5), 0.12F, 0, new boolean[] { true, false, true, true, true }), IKInfo.make(dragon.legBackL1, dragon.legBackL3, null, Pair.of(0, 5), 0.1344F, 0, new boolean[] { true, true, true, true, true }), IKInfo.make(dragon.legBackR1, dragon.legBackR3, null, Pair.of(0, 4), 0.1344F, 0, new boolean[] { true, true, true, true }) }).addEvents(new AnimationEvent.TimeStampedEvent[] { AnimationEvent.TimeStampedEvent.create(1.6F, (entitypatch, animation, params) -> {
            if (entitypatch instanceof EnderDragonPatch) {
                ((EnderDragonPatch) entitypatch).getOriginal().getPhaseManager().getPhase(PatchedPhases.GROUND_BATTLE).fly();
            }
        }, AnimationEvent.Side.SERVER) });
        SPIDER_ATTACK = new AttackAnimation(0.15F, 0.31F, 0.31F, 0.36F, 0.44F, ColliderPreset.SPIDER, spider.head, "spider/attack", spider);
        SPIDER_JUMP_ATTACK = new AttackAnimation(0.15F, 0.25F, 0.5F, 0.6F, 1.0F, ColliderPreset.SPIDER, spider.head, "spider/jump_attack", spider).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true);
        SPIDER_HIT = new HitAnimation(0.08F, "spider/hit", spider);
        SPIDER_NEUTRALIZED = new LongHitAnimation(0.08F, "spider/neutralized", spider);
        SPIDER_DEATH = new LongHitAnimation(0.16F, "spider/death", spider);
        GOLEM_ATTACK1 = new AttackAnimation(0.2F, 0.1F, 0.15F, 0.25F, 0.9F, ColliderPreset.HEAD, ironGolem.head, "iron_golem/attack1", ironGolem).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.KNOCKDOWN);
        GOLEM_ATTACK2 = new AttackAnimation(0.34F, 0.1F, 0.4F, 0.6F, 1.3F, ColliderPreset.GOLEM_SMASHDOWN, ironGolem.LA4, "iron_golem/attack2", ironGolem).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.FINISHER));
        GOLEM_ATTACK3 = new AttackAnimation(0.16F, 0.4F, 0.4F, 0.5F, 0.9F, ColliderPreset.GOLEM_SWING_ARM, ironGolem.RA4, "iron_golem/attack3", ironGolem).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        GOLEM_ATTACK4 = new AttackAnimation(0.16F, 0.4F, 0.4F, 0.5F, 0.9F, ColliderPreset.GOLEM_SWING_ARM, ironGolem.LA4, "iron_golem/attack4", ironGolem).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        GOLEM_DEATH = new LongHitAnimation(0.11F, "iron_golem/death", ironGolem);
        VINDICATOR_SWING_AXE1 = new AttackAnimation(0.2F, 0.2F, 0.3F, 0.4F, 0.9F, ColliderPreset.TOOLS, biped.toolR, "illager/swing_axe1", biped);
        VINDICATOR_SWING_AXE2 = new AttackAnimation(0.1F, 0.2F, 0.3F, 0.4F, 0.9F, ColliderPreset.TOOLS, biped.toolR, "illager/swing_axe2", biped);
        VINDICATOR_SWING_AXE3 = new AttackAnimation(0.1F, 0.15F, 0.45F, 0.55F, 1.05F, ColliderPreset.TOOLS, biped.toolR, "illager/swing_axe3", biped);
        PIGLIN_DEATH = new LongHitAnimation(0.16F, "piglin/death", piglin);
        HOGLIN_DEATH = new LongHitAnimation(0.16F, "hoglin/death", hoglin);
        HOGLIN_ATTACK = new AttackAnimation(0.16F, 0.25F, 0.25F, 0.45F, 1.0F, ColliderPreset.GOLEM_SWING_ARM, hoglin.head, "hoglin/attack", hoglin);
        RAVAGER_DEATH = new LongHitAnimation(0.11F, "ravager/death", ravager);
        RAVAGER_STUN = new ActionAnimation(0.16F, "ravager/groggy", ravager).addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, true);
        RAVAGER_ATTACK1 = new AttackAnimation(0.16F, 0.2F, 0.4F, 0.5F, 0.55F, ColliderPreset.HEADBUTT_RAVAGER, ravager.head, "ravager/attack1", ravager);
        RAVAGER_ATTACK2 = new AttackAnimation(0.16F, 0.2F, 0.4F, 0.5F, 1.3F, ColliderPreset.HEADBUTT_RAVAGER, ravager.head, "ravager/attack2", ravager);
        RAVAGER_ATTACK3 = new AttackAnimation(0.16F, 0.0F, 1.1F, 1.16F, 1.6F, ColliderPreset.HEADBUTT_RAVAGER, ravager.head, "ravager/attack3", ravager);
        VEX_HIT = new HitAnimation(0.048F, "vex/hit", vex);
        VEX_DEATH = new LongHitAnimation(0.16F, "vex/death", vex);
        VEX_CHARGE = new AttackAnimation(0.11F, 0.3F, 0.3F, 0.5F, 1.2F, ColliderPreset.VEX_CHARGE, vex.rootJoint, "vex/charge", vex).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_LOCATION_PROVIDER, entitypatch -> entitypatch.getLastAttackPosition()).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.VEX_TRACE).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, (self, entitypatch, transformSheet) -> {
        }).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true).addEvents(AnimationProperty.StaticAnimationProperty.ON_BEGIN_EVENTS, new AnimationEvent[] { AnimationEvent.create((entitypatch, animation, params) -> entitypatch.setLastAttackPosition(), AnimationEvent.Side.SERVER) });
        VEX_NEUTRALIZED = new LongHitAnimation(0.1F, "vex/neutralized", vex);
        WITCH_DRINKING = new StaticAnimation(0.16F, false, "witch/drink", biped);
        WITHER_SKELETON_ATTACK1 = new AttackAnimation(0.16F, 0.2F, 0.3F, 0.41F, 0.7F, ColliderPreset.SWORD, biped.toolR, "wither_skeleton/sword_attack1", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        WITHER_SKELETON_ATTACK2 = new AttackAnimation(0.16F, 0.25F, 0.25F, 0.36F, 0.7F, ColliderPreset.SWORD, biped.toolR, "wither_skeleton/sword_attack2", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        WITHER_SKELETON_ATTACK3 = new AttackAnimation(0.16F, 0.25F, 0.25F, 0.36F, 0.7F, ColliderPreset.SWORD, biped.toolR, "wither_skeleton/sword_attack3", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        WITHER_CHARGE = new AttackAnimation(0.35F, 0.35F, 0.35F, 0.66F, 2.05F, ColliderPreset.WITHER_CHARGE, wither.rootJoint, "wither/rush", wither).addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.BIG_ENTITY_MOVE.get()).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD.get()).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_LOCATION_PROVIDER, entitypatch -> entitypatch.getLastAttackPosition()).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.KNOCKDOWN).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(100.0F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(15.0F)).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, (self, entitypatch, transformSheet) -> {
            if (entitypatch instanceof WitherPatch witherpatch && witherpatch.getOriginal().getAlternativeTarget(0) > 0) {
                TransformSheet transform = ((TransformSheet) self.getTransfroms().get("Root")).copyAll();
                Keyframe[] keyframes = transform.getKeyframes();
                int startFrame = 1;
                int endFrame = 5;
                Vec3f keyOrigin = keyframes[startFrame].transform().translation().multiply(1.0F, 1.0F, 0.0F);
                Vec3f keyLast = keyframes[3].transform().translation();
                Vec3 pos = entitypatch.getOriginal().m_146892_();
                Vec3 targetpos = entitypatch.getOriginal().m_9236_().getEntity(witherpatch.getOriginal().getAlternativeTarget(0)).position();
                float horizontalDistance = (float) targetpos.subtract(pos).length();
                float verticalDistance = (float) (targetpos.y - pos.y);
                Vec3f prevPosition = Vec3f.sub(keyLast, keyOrigin, null);
                Vec3f newPosition = new Vec3f(keyLast.x, verticalDistance, -horizontalDistance);
                float scale = Math.min(newPosition.length() / prevPosition.length(), 5.0F);
                Quaternionf rotator = Vec3f.getRotatorBetween(newPosition, keyLast);
                for (int i = startFrame; i <= endFrame; i++) {
                    Vec3f translation = keyframes[i].transform().translation();
                    translation.z *= scale;
                    OpenMatrix4f.transform3v(OpenMatrix4f.fromQuaternion(rotator), translation, translation);
                }
                transformSheet.readFrom(transform);
                return;
            }
            transformSheet.readFrom(((TransformSheet) self.getTransfroms().get("Root")).copyAll());
        }).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, (self, entitypatch, transformSheet) -> {
        }).addEvents(new AnimationEvent.TimeStampedEvent[] { AnimationEvent.TimeStampedEvent.create(0.4F, (entitypatch, animation, params) -> {
            if (entitypatch instanceof WitherPatch witherpatch) {
                witherpatch.startCharging();
            } else {
                entitypatch.setLastAttackPosition();
            }
        }, AnimationEvent.Side.SERVER), AnimationEvent.TimeStampedEvent.create(0.4F, (entitypatch, animation, params) -> {
            Entity entity = entitypatch.getOriginal();
            entitypatch.getOriginal().m_9236_().addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble((long) entity.getId()), 0.0, 0.0);
        }, AnimationEvent.Side.CLIENT) }).addEvents(AnimationProperty.StaticAnimationProperty.ON_BEGIN_EVENTS, AnimationEvent.create((entitypatch, animation, params) -> {
            if (entitypatch instanceof WitherPatch witherpatch && !witherpatch.getOriginal().isPowered()) {
                ((WitherPatch) entitypatch).setArmorActivated(true);
            }
        }, AnimationEvent.Side.CLIENT)).addEvents(AnimationProperty.StaticAnimationProperty.ON_END_EVENTS, AnimationEvent.create((entitypatch, animation, params) -> {
            if (entitypatch instanceof WitherPatch witherpatch && !witherpatch.getOriginal().isPowered()) {
                ((WitherPatch) entitypatch).setArmorActivated(false);
            }
        }, AnimationEvent.Side.CLIENT));
        WITHER_DEATH = new LongHitAnimation(0.16F, "wither/death", wither);
        WITHER_NEUTRALIZED = new LongHitAnimation(0.05F, "wither/neutralized", wither).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, Boolean.valueOf(true)).addEvents(AnimationProperty.StaticAnimationProperty.ON_BEGIN_EVENTS, new AnimationEvent[] { AnimationEvent.create((entitypatch, animation, params) -> {
            Entity entity = entitypatch.getOriginal();
            entity.level().addParticle(EpicFightParticles.NEUTRALIZE.get(), entity.getX(), entity.getEyeY(), entity.getZ(), 3.0, Double.longBitsToDouble(15L), Double.NaN);
        }, AnimationEvent.Side.CLIENT) });
        WITHER_SPELL_ARMOR = new InvincibleAnimation(0.35F, "wither/spell_wither_armor", wither).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, Boolean.valueOf(false)).addEvents(AnimationProperty.StaticAnimationProperty.ON_BEGIN_EVENTS, new AnimationEvent[] { AnimationEvent.create((entitypatch, animation, params) -> {
            entitypatch.playSound(EpicFightSounds.WITHER_SPELL_ARMOR.get(), 5.0F, 0.0F, 0.0F);
            Entity entity = entitypatch.getOriginal();
            entity.level().addParticle(EpicFightParticles.BOSS_CASTING.get(), entity.getX(), entity.getEyeY(), entity.getZ(), 5.0, Double.longBitsToDouble(20L), Double.longBitsToDouble(4L));
        }, AnimationEvent.Side.CLIENT) }).addEvents(AnimationEvent.TimeStampedEvent.create(0.5F, (entitypatch, animation, params) -> ((WitherPatch) entitypatch).setArmorActivated(true), AnimationEvent.Side.SERVER));
        WITHER_BLOCKED = new ActionAnimation(0.05F, "wither/charging_blocked", wither).addEvents(AnimationProperty.StaticAnimationProperty.ON_BEGIN_EVENTS, new AnimationEvent[] { AnimationEvent.create((entitypatch, animation, params) -> {
            if (entitypatch instanceof WitherPatch witherpatch && !witherpatch.getOriginal().isPowered()) {
                ((WitherPatch) entitypatch).setArmorActivated(true);
            }
        }, AnimationEvent.Side.SERVER) }).addEvents(AnimationProperty.StaticAnimationProperty.ON_END_EVENTS, AnimationEvent.create((entitypatch, animation, params) -> {
            if (entitypatch instanceof WitherPatch witherpatch && !witherpatch.getOriginal().isPowered()) {
                ((WitherPatch) entitypatch).setArmorActivated(false);
            }
        }, AnimationEvent.Side.SERVER));
        WITHER_GHOST_STANDBY = new InvincibleAnimation(0.16F, "wither/ghost_stand", wither);
        WITHER_SWIRL = new AttackAnimation(0.2F, 0.05F, 0.4F, 0.51F, 1.6F, ColliderPreset.WITHER_CHARGE, wither.torso, "wither/swirl", wither).addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_BIG.get()).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(3.0F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(6.0F));
        WITHER_BEAM = new ActionAnimation(0.05F, "wither/laser", wither).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, false).addEvents(new AnimationEvent.TimeStampedEvent[] { AnimationEvent.TimeStampedEvent.create(0.0F, (entitypatch, animation, params) -> {
            entitypatch.playSound(EpicFightSounds.BUZZ.get(), 0.0F, 0.0F);
            if (entitypatch instanceof WitherPatch witherpatch) {
                for (int i = 0; i < 3; i++) {
                    Entity headTarget = witherpatch.getAlternativeTargetEntity(i);
                    if (headTarget == null) {
                        headTarget = witherpatch.getAlternativeTargetEntity(0);
                    }
                    if (headTarget != null) {
                        witherpatch.setLaserTarget(i, headTarget);
                    }
                }
            }
        }, AnimationEvent.Side.SERVER), AnimationEvent.TimeStampedEvent.create(0.7F, (entitypatch, animation, params) -> {
            if (entitypatch instanceof WitherPatch witherpatch) {
                for (int i = 0; i < 3; i++) {
                    Entity headTarget = witherpatch.getLaserTargetEntity(i);
                    if (headTarget != null) {
                        Vec3 pos = headTarget.position().add(0.0, (double) headTarget.getBbHeight() * 0.5, 0.0);
                        witherpatch.setLaserTargetPosition(i, pos);
                        witherpatch.setLaserTarget(i, null);
                    }
                }
            }
        }, AnimationEvent.Side.SERVER), AnimationEvent.TimeStampedEvent.create(0.9F, (entitypatch, animation, params) -> {
            if (entitypatch instanceof WitherPatch witherpatch) {
                WitherBoss witherboss = witherpatch.getOriginal();
                witherboss.m_9236_().playLocalSound(witherboss.m_20185_(), witherboss.m_20186_(), witherboss.m_20189_(), EpicFightSounds.LASER_BLAST.get(), SoundSource.HOSTILE, 1.0F, 1.0F, false);
                for (int i = 0; i < 3; i++) {
                    Vec3 laserDestination = witherpatch.getLaserTargetPosition(i);
                    Entity headTarget = witherpatch.getAlternativeTargetEntity(i);
                    if (headTarget != null) {
                        witherpatch.getOriginal().m_9236_().addAlwaysVisibleParticle(EpicFightParticles.LASER.get(), witherboss.getHeadX(i), witherboss.getHeadY(i), witherboss.getHeadZ(i), laserDestination.x, laserDestination.y, laserDestination.z);
                    }
                }
            }
        }, AnimationEvent.Side.CLIENT), AnimationEvent.TimeStampedEvent.create(0.9F, (entitypatch, animation, params) -> {
            if (entitypatch instanceof WitherPatch witherpatch) {
                WitherBoss witherboss = witherpatch.getOriginal();
                List<Entity> hurted = Lists.newArrayList();
                for (int i = 0; i < 3; i++) {
                    Vec3 laserDestination = witherpatch.getLaserTargetPosition(i);
                    Entity headTarget = witherpatch.getAlternativeTargetEntity(i);
                    if (headTarget != null) {
                        double x = witherboss.getHeadX(i);
                        double y = witherboss.getHeadY(i);
                        double z = witherboss.getHeadZ(i);
                        Vec3 direction = laserDestination.subtract(x, y, z);
                        Vec3 start = new Vec3(x, y, z);
                        Vec3 destination = start.add(direction.normalize().scale(200.0));
                        BlockHitResult hitResult = witherboss.m_9236_().m_45547_(new ClipContext(start, destination, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
                        Vec3 hitLocation = hitResult.m_82450_();
                        double xLength = hitLocation.x - x;
                        double yLength = hitLocation.y - y;
                        double zLength = hitLocation.z - z;
                        double horizontalDistance = Math.sqrt(xLength * xLength + zLength * zLength);
                        double length = Math.sqrt(xLength * xLength + yLength * yLength + zLength * zLength);
                        float yRot = (float) (-Math.atan2(zLength, xLength) * (180.0 / Math.PI)) - 90.0F;
                        float xRot = (float) (Math.atan2(yLength, horizontalDistance) * (180.0 / Math.PI));
                        OBBCollider collider = new OBBCollider(0.25, 0.25, length * 0.5, 0.0, 0.0, length * 0.5);
                        collider.transform(OpenMatrix4f.createTranslation((float) (-x), (float) y, (float) (-z)).rotateDeg(yRot, Vec3f.Y_AXIS).rotateDeg(-xRot, Vec3f.X_AXIS));
                        List<Entity> hitEntities = collider.getCollideEntities(witherboss);
                        EpicFightDamageSources damageSources = EpicFightDamageSources.of(witherboss.m_9236_());
                        EpicFightDamageSource damagesource = damageSources.witherBeam(witherboss).setAnimation(WITHER_BEAM);
                        hitEntities.forEach(entity -> {
                            if (!hurted.contains(entity)) {
                                hurted.add(entity);
                                entity.hurt(damagesource, 12.0F);
                            }
                        });
                        Level.ExplosionInteraction explosion$blockinteraction = ForgeEventFactory.getMobGriefingEvent(witherboss.m_9236_(), witherboss) ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.NONE;
                        witherboss.m_9236_().explode(witherboss, hitLocation.x, hitLocation.y, hitLocation.z, 0.0F, false, explosion$blockinteraction);
                    }
                }
            }
        }, AnimationEvent.Side.SERVER), AnimationEvent.TimeStampedEvent.create(2.3F, (entitypatch, animation, params) -> {
            if (entitypatch instanceof WitherPatch witherpatch) {
                for (int i = 0; i < 3; i++) {
                    witherpatch.setLaserTargetPosition(i, new Vec3(Double.NaN, Double.NaN, Double.NaN));
                }
            }
        }, AnimationEvent.Side.SERVER) });
        WITHER_BACKFLIP = new AttackAnimation(0.2F, 0.3F, 0.5F, 0.66F, 2.1F, ColliderPreset.WITHER_CHARGE, wither.torso, "wither/backflip", wither).addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.BIG_ENTITY_MOVE.get()).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD.get()).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(100.0F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(10.0F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.KNOCKDOWN).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.RAW_COORD).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, null);
        ZOMBIE_ATTACK1 = new AttackAnimation(0.1F, 0.3F, 0.4F, 0.6F, 0.85F, ColliderPreset.FIST, biped.toolR, "zombie/attack1", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        ZOMBIE_ATTACK2 = new AttackAnimation(0.1F, 0.3F, 0.4F, 0.6F, 0.85F, ColliderPreset.FIST, biped.toolL, "zombie/attack2", biped).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        ZOMBIE_ATTACK3 = new AttackAnimation(0.1F, 0.5F, 0.5F, 0.6F, 1.15F, ColliderPreset.HEAD, biped.head, "zombie/attack3", biped);
        SWEEPING_EDGE = new AttackAnimation(0.1F, 0.0F, 0.15F, 0.3F, 0.8F, null, biped.toolR, "biped/skill/sweeping_edge", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F).addProperty(AnimationProperty.AttackAnimationProperty.EXTRA_COLLIDERS, 1).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        DANCING_EDGE = new AttackAnimation(0.1F, "biped/skill/dancing_edge", biped, new AttackAnimation.Phase(0.0F, 0.25F, 0.4F, 0.4F, 0.4F, biped.toolR, null), new AttackAnimation.Phase(0.4F, 0.4F, 0.5F, 0.55F, 0.6F, InteractionHand.OFF_HAND, biped.toolL, null), new AttackAnimation.Phase(0.6F, 0.6F, 0.7F, 1.15F, Float.MAX_VALUE, biped.toolR, null)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true);
        THE_GUILLOTINE = new AttackAnimation(0.15F, 0.2F, 0.7F, 0.75F, 1.1F, null, biped.toolR, "biped/skill/the_guillotine", biped).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE);
        HEARTPIERCER = new AttackAnimation(0.11F, "biped/skill/heartpiercer", biped, new AttackAnimation.Phase(0.0F, 0.3F, 0.36F, 0.5F, 0.5F, biped.toolR, null), new AttackAnimation.Phase(0.5F, 0.5F, 0.56F, 0.75F, 0.75F, biped.toolR, null), new AttackAnimation.Phase(0.75F, 0.75F, 0.81F, 1.05F, Float.MAX_VALUE, biped.toolR, null)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.2F).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        GRASPING_SPIRAL_FIRST = new AttackAnimation(0.1F, 0.25F, 0.3F, 0.4F, 0.8F, null, biped.toolR, String.valueOf(SPEAR_DASH.getId()), biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.2F).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER).addEvents(AnimationProperty.StaticAnimationProperty.ON_END_EVENTS, AnimationEvent.create((entitypatch, animation, params) -> {
            List<LivingEntity> hitEnemies = entitypatch.getCurrenltyHurtEntities();
            Vec3 vec = entitypatch.getOriginal().m_20182_().add(Vec3.directionFromRotation(new Vec2(0.0F, entitypatch.getOriginal().m_146908_())));
            AttackAnimation attackAnimation = (AttackAnimation) animation;
            for (LivingEntity e : hitEnemies) {
                if (e.isAlive()) {
                    LivingEntityPatch<?> targetpatch = EpicFightCapabilities.getEntityPatch(e, LivingEntityPatch.class);
                    if (targetpatch != null) {
                        DamageSource dmgSource = attackAnimation.getEpicFightDamageSource(entitypatch, e, attackAnimation.phases[0]);
                        if (!targetpatch.tryHurt(dmgSource, 0.0F).resultType.dealtDamage()) {
                            continue;
                        }
                    }
                    Vec3 toAttacker = e.m_20182_().subtract(vec).multiply(0.3F, 0.3F, 0.3F);
                    e.m_146884_(vec.add(toAttacker));
                }
            }
        }, AnimationEvent.Side.SERVER)).addEvents(AnimationEvent.TimeStampedEvent.create(0.75F, (entitypatch, animation, params) -> {
            if (entitypatch.isLastAttackSuccess()) {
                entitypatch.playAnimationSynchronized(GRASPING_SPIRAL_SECOND, 0.0F);
            }
        }, AnimationEvent.Side.SERVER));
        GRASPING_SPIRAL_SECOND = new AttackAnimation(0.1F, 0.0F, 0.5F, 0.6F, 0.95F, null, biped.toolR, "biped/skill/grasping_spire_second", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.2F).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        STEEL_WHIRLWIND = new AttackAnimation(0.15F, "biped/skill/steel_whirlwind", biped, new AttackAnimation.Phase(0.0F, 0.0F, 0.0F, 0.2F, 0.45F, 0.45F, biped.toolR, null), new AttackAnimation.Phase(0.45F, 0.45F, 0.45F, 0.65F, 1.0F, 1.0F, biped.toolR, null), new AttackAnimation.Phase(1.0F, 1.0F, 1.0F, 1.2F, 2.55F, Float.MAX_VALUE, biped.toolR, null)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.0F).addProperty(AnimationProperty.AttackAnimationProperty.EXTRA_COLLIDERS, 4).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, (animation, entitypatch, transformSheet) -> {
            if (entitypatch instanceof PlayerPatch<?> playerpatch) {
                int chargingPower = SteelWhirlwindSkill.getChargingPower(playerpatch.getSkill(SkillSlots.WEAPON_INNATE));
                transformSheet.readFrom(animation.getCoord().copyAll().extendsZCoord(0.6666F + (float) chargingPower / 5.0F, 0, 2));
            } else {
                MoveCoordFunctions.RAW_COORD.set(animation, entitypatch, transformSheet);
            }
        }).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, null).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, false).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, elapsedTime) -> {
            if (elapsedTime < 1.05F && entitypatch instanceof PlayerPatch<?> playerpatch) {
                int chargingPower = SteelWhirlwindSkill.getChargingPower(playerpatch.getSkill(SkillSlots.WEAPON_INNATE));
                return 0.6666F + (float) chargingPower / 20.0F;
            } else {
                return 1.0F;
            }
        }).newTimePair(0.0F, 2.55F).addStateRemoveOld(EntityState.CAN_SKILL_EXECUTION, false);
        BATTOJUTSU = new AttackAnimation(0.15F, 0.0F, 0.75F, 0.8F, 1.2F, ColliderPreset.BATTOJUTSU, biped.rootJoint, "biped/skill/battojutsu", biped).addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP.get()).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE).addEvents(AnimationEvent.TimeStampedEvent.create(0.05F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.SWORD_IN.get()));
        BATTOJUTSU_DASH = new AttackAnimation(0.15F, 0.43F, 0.7F, 0.8F, 1.4F, ColliderPreset.BATTOJUTSU_DASH, biped.rootJoint, "biped/skill/battojutsu_dash", biped).addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP.get()).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.RAW_COORD).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, null).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE).addEvents(AnimationEvent.TimeStampedEvent.create(0.05F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.SWORD_IN.get()), AnimationEvent.TimeStampedEvent.create(0.7F, (entitypatch, animation, params) -> {
            Entity entity = entitypatch.getOriginal();
            entity.level().addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble((long) entity.getId()), 0.0, 0.0);
            RandomSource random = entitypatch.getOriginal().getRandom();
            double x = entity.getX() + (random.nextDouble() - random.nextDouble()) * 2.0;
            double y = entity.getY();
            double z = entity.getZ() + (random.nextDouble() - random.nextDouble()) * 2.0;
            entity.level().addParticle(ParticleTypes.EXPLOSION, x, y, z, random.nextDouble() * 0.005, 0.0, 0.0);
        }, AnimationEvent.Side.CLIENT));
        RUSHING_TEMPO1 = new AttackAnimation(0.05F, 0.0F, 0.15F, 0.25F, 0.6F, null, biped.toolR, "biped/skill/rushing_tempo1", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F).addProperty(AnimationProperty.AttackAnimationProperty.EXTRA_COLLIDERS, 2).addProperty(AnimationProperty.ActionAnimationProperty.RESET_PLAYER_COMBO_COUNTER, false).newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false);
        RUSHING_TEMPO2 = new AttackAnimation(0.05F, 0.0F, 0.15F, 0.25F, 0.6F, null, biped.toolR, "biped/skill/rushing_tempo2", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F).addProperty(AnimationProperty.AttackAnimationProperty.EXTRA_COLLIDERS, 2).addProperty(AnimationProperty.ActionAnimationProperty.RESET_PLAYER_COMBO_COUNTER, false).newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false);
        RUSHING_TEMPO3 = new AttackAnimation(0.05F, 0.0F, 0.2F, 0.25F, 0.6F, null, biped.toolR, "biped/skill/rushing_tempo3", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F).addProperty(AnimationProperty.AttackAnimationProperty.EXTRA_COLLIDERS, 2).addProperty(AnimationProperty.ActionAnimationProperty.RESET_PLAYER_COMBO_COUNTER, false).newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false);
        RELENTLESS_COMBO = new AttackAnimation(0.05F, "biped/skill/relentless_combo", biped, new AttackAnimation.Phase(0.0F, 0.016F, 0.066F, 0.133F, 0.133F, InteractionHand.OFF_HAND, biped.rootJoint, ColliderPreset.FIST_FIXED), new AttackAnimation.Phase(0.133F, 0.133F, 0.183F, 0.25F, 0.25F, biped.rootJoint, ColliderPreset.FIST_FIXED), new AttackAnimation.Phase(0.25F, 0.25F, 0.3F, 0.366F, 0.366F, InteractionHand.OFF_HAND, biped.rootJoint, ColliderPreset.FIST_FIXED), new AttackAnimation.Phase(0.366F, 0.366F, 0.416F, 0.483F, 0.483F, biped.rootJoint, ColliderPreset.FIST_FIXED), new AttackAnimation.Phase(0.483F, 0.483F, 0.533F, 0.6F, 0.6F, InteractionHand.OFF_HAND, biped.rootJoint, ColliderPreset.FIST_FIXED), new AttackAnimation.Phase(0.6F, 0.6F, 0.65F, 0.716F, 0.716F, biped.rootJoint, ColliderPreset.FIST_FIXED), new AttackAnimation.Phase(0.716F, 0.716F, 0.766F, 0.833F, 0.833F, InteractionHand.OFF_HAND, biped.rootJoint, ColliderPreset.FIST_FIXED), new AttackAnimation.Phase(0.833F, 0.833F, 0.883F, 1.1F, 1.1F, biped.rootJoint, ColliderPreset.FIST_FIXED)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 4.0F);
        EVISCERATE_FIRST = new AttackAnimation(0.08F, 0.0F, 0.05F, 0.15F, 0.45F, null, biped.toolR, "biped/skill/eviscerate_first", biped).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.4F).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_LOCROT_TARGET).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, MoveCoordFunctions.TRACE_LOCROT_TARGET);
        EVISCERATE_SECOND = new AttackAnimation(0.15F, 0.0F, 0.0F, 0.0F, 0.4F, null, biped.toolR, "biped/skill/eviscerate_second", biped).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.EVISCERATE.get()).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.EVISCERATE).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.4F);
        BLADE_RUSH_COMBO1 = new AttackAnimation(0.1F, 0.0F, 0.2F, 0.25F, 0.85F, ColliderPreset.BIPED_BODY_COLLIDER, biped.rootJoint, "biped/skill/blade_rush_combo1", biped).addProperty(AnimationProperty.AttackPhaseProperty.HIT_PRIORITY, HitEntityList.Priority.TARGET).addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_ON_LINK, false).addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.0F, 0.35F)).addProperty(AnimationProperty.ActionAnimationProperty.COORD_UPDATE_TIME, TimePairList.create(0.0F, 0.25F)).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_DEST_LOCATION_BEGIN).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, MoveCoordFunctions.TRACE_DEST_LOCATION).addProperty(AnimationProperty.ActionAnimationProperty.COORD_GET, MoveCoordFunctions.WORLD_COORD).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE).newTimePair(0.0F, 0.65F).addStateRemoveOld(EntityState.CAN_SKILL_EXECUTION, false);
        BLADE_RUSH_COMBO2 = new AttackAnimation(0.1F, 0.0F, 0.2F, 0.25F, 0.85F, ColliderPreset.BIPED_BODY_COLLIDER, biped.rootJoint, "biped/skill/blade_rush_combo2", biped).addProperty(AnimationProperty.AttackPhaseProperty.HIT_PRIORITY, HitEntityList.Priority.TARGET).addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_ON_LINK, false).addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.0F, 0.35F)).addProperty(AnimationProperty.ActionAnimationProperty.COORD_UPDATE_TIME, TimePairList.create(0.0F, 0.25F)).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_DEST_LOCATION_BEGIN).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, MoveCoordFunctions.TRACE_DEST_LOCATION).addProperty(AnimationProperty.ActionAnimationProperty.COORD_GET, MoveCoordFunctions.WORLD_COORD).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE).newTimePair(0.0F, 0.65F).addStateRemoveOld(EntityState.CAN_SKILL_EXECUTION, false);
        BLADE_RUSH_COMBO3 = new AttackAnimation(0.1F, 0.0F, 0.25F, 0.35F, 0.85F, ColliderPreset.BIPED_BODY_COLLIDER, biped.rootJoint, "biped/skill/blade_rush_combo3", biped).addProperty(AnimationProperty.AttackPhaseProperty.HIT_PRIORITY, HitEntityList.Priority.TARGET).addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_ON_LINK, false).addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.0F, 0.35F)).addProperty(AnimationProperty.ActionAnimationProperty.COORD_UPDATE_TIME, TimePairList.create(0.0F, 0.25F)).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_DEST_LOCATION_BEGIN).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, MoveCoordFunctions.TRACE_DEST_LOCATION).addProperty(AnimationProperty.ActionAnimationProperty.COORD_GET, MoveCoordFunctions.WORLD_COORD).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE).newTimePair(0.0F, 0.6F).addStateRemoveOld(EntityState.CAN_SKILL_EXECUTION, false);
        BLADE_RUSH_HIT = new LongHitAnimation(0.1F, "biped/interact/blade_rush_hit", biped).addProperty(AnimationProperty.ActionAnimationProperty.IS_DEATH_ANIMATION, Boolean.valueOf(true));
        BLADE_RUSH_EXECUTE_BIPED = new GrapplingAttackAnimation(0.5F, 1.5F, "biped/skill/blade_rush_execute", biped).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.EXECUTION)).addProperty(AnimationProperty.ActionAnimationProperty.COORD_UPDATE_TIME, TimePairList.create(0.0F, 0.5F)).addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.0F, 0.95F)).addEvents(new AnimationEvent.TimeStampedEvent[] { AnimationEvent.TimeStampedEvent.create(0.1F, (entitypatch, animation, params) -> {
            LivingEntity grapplingTarget = entitypatch.getGrapplingTarget();
            if (grapplingTarget != null) {
                entitypatch.playSound(EpicFightSounds.BLADE_HIT.get(), 0.0F, 0.0F);
            }
        }, AnimationEvent.Side.CLIENT), AnimationEvent.TimeStampedEvent.create(0.3F, (entitypatch, animation, params) -> {
            LivingEntity grapplingTarget = entitypatch.getGrapplingTarget();
            if (grapplingTarget != null) {
                entitypatch.playSound(EpicFightSounds.BLADE_HIT.get(), 0.0F, 0.0F);
            }
        }, AnimationEvent.Side.CLIENT) });
        BLADE_RUSH_FAILED = new ActionAnimation(0.0F, 0.85F, "biped/skill/blade_rush_failed", biped).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true).addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.0F, 0.0F));
        BLADE_RUSH_TRY = new GrapplingTryAnimation(0.1F, 0.0F, 0.4F, 0.4F, 0.45F, ColliderPreset.BIPED_BODY_COLLIDER, biped.rootJoint, "biped/skill/blade_rush_try", BLADE_RUSH_HIT, BLADE_RUSH_EXECUTE_BIPED, BLADE_RUSH_FAILED, biped).addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.15F, 0.35F)).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, null);
        WRATHFUL_LIGHTING = new AttackAnimation(0.15F, "biped/skill/wrathful_lighting", biped, new AttackAnimation.Phase(0.0F, 0.0F, 0.3F, 0.36F, 1.0F, Float.MAX_VALUE, biped.toolR, null), new AttackAnimation.Phase(InteractionHand.MAIN_HAND, biped.rootJoint, null)).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE).addEvents(AnimationEvent.TimeStampedEvent.create(0.35F, Animations.ReusableSources.SUMMON_THUNDER, AnimationEvent.Side.SERVER));
        TSUNAMI = new AttackAnimation(0.2F, 0.2F, 0.35F, 1.0F, 1.8F, ColliderPreset.BIPED_BODY_COLLIDER, biped.rootJoint, "biped/skill/tsunami", biped).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(10.0F)).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.RAW_COORD).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, null).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true).addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.2F, 1.1F)).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE).addEvents(AnimationProperty.StaticAnimationProperty.ON_END_EVENTS, AnimationEvent.create(Animations.ReusableSources.RESTORE_BOUNDING_BOX, AnimationEvent.Side.BOTH)).addEvents(AnimationProperty.StaticAnimationProperty.EVENTS, AnimationEvent.create(Animations.ReusableSources.RESIZE_BOUNDING_BOX, AnimationEvent.Side.BOTH).params(EntityDimensions.scalable(0.6F, 1.0F))).addEvents(AnimationEvent.TimePeriodEvent.create(0.35F, 1.0F, (entitypatch, animation, params) -> {
            Vec3 pos = entitypatch.getOriginal().m_20182_();
            for (int x = -1; x <= 1; x += 2) {
                for (int z = -1; z <= 1; z += 2) {
                    Vec3 rand = new Vec3(Math.random() * (double) x, Math.random(), Math.random() * (double) z).normalize().scale(2.0);
                    entitypatch.getOriginal().m_9236_().addParticle(EpicFightParticles.TSUNAMI_SPLASH.get(), pos.x + rand.x, pos.y + rand.y - 1.0, pos.z + rand.z, rand.x * 0.1, rand.y * 0.1, rand.z * 0.1);
                }
            }
        }, AnimationEvent.Side.CLIENT)).addEvents(AnimationEvent.TimeStampedEvent.create(0.35F, (entitypatch, animation, params) -> entitypatch.playSound(SoundEvents.TRIDENT_RIPTIDE_3, 0.0F, 0.0F), AnimationEvent.Side.CLIENT), AnimationEvent.TimeStampedEvent.create(0.35F, (entitypatch, animation, params) -> {
            if (entitypatch.getOriginal().m_20096_()) {
                entitypatch.setAirborneState(true);
            }
        }, AnimationEvent.Side.SERVER));
        TSUNAMI_REINFORCED = new AttackAnimation(0.2F, 0.2F, 0.35F, 0.65F, 1.3F, ColliderPreset.BIPED_BODY_COLLIDER, biped.rootJoint, "biped/skill/tsunami_reinforced", biped).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(10.0F)).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.RAW_COORD).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, null).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.RAW_COORD_WITH_X_ROT).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, MoveCoordFunctions.RAW_COORD_WITH_X_ROT).addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.15F, 0.85F)).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.ROOT_X_MODIFIER).addEvents(AnimationProperty.StaticAnimationProperty.ON_END_EVENTS, AnimationEvent.create(Animations.ReusableSources.RESTORE_BOUNDING_BOX, AnimationEvent.Side.BOTH)).addEvents(AnimationProperty.StaticAnimationProperty.EVENTS, AnimationEvent.create(Animations.ReusableSources.RESIZE_BOUNDING_BOX, AnimationEvent.Side.BOTH).params(EntityDimensions.scalable(0.6F, 1.0F))).addEvents(AnimationEvent.TimePeriodEvent.create(0.35F, 1.0F, (entitypatch, animation, params) -> {
            Vec3 pos = entitypatch.getOriginal().m_20182_();
            for (int x = -1; x <= 1; x += 2) {
                for (int z = -1; z <= 1; z += 2) {
                    Vec3 rand = new Vec3(Math.random() * (double) x, Math.random(), Math.random() * (double) z).normalize().scale(2.0);
                    entitypatch.getOriginal().m_9236_().addParticle(EpicFightParticles.TSUNAMI_SPLASH.get(), pos.x + rand.x, pos.y + rand.y - 1.0, pos.z + rand.z, rand.x * 0.1, rand.y * 0.1, rand.z * 0.1);
                }
            }
        }, AnimationEvent.Side.CLIENT)).addEvents(AnimationEvent.TimeStampedEvent.create(0.35F, (entitypatch, animation, params) -> entitypatch.playSound(SoundEvents.TRIDENT_RIPTIDE_3, 0.0F, 0.0F), AnimationEvent.Side.CLIENT), AnimationEvent.TimeStampedEvent.create(0.35F, (entitypatch, animation, params) -> {
            if (entitypatch.getOriginal().m_20096_()) {
                entitypatch.setAirborneState(true);
            }
        }, AnimationEvent.Side.SERVER));
        EVERLASTING_ALLEGIANCE_CALL = new ActionAnimation(0.1F, 0.55F, "biped/skill/everlasting_allegiance_call", biped).addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, true);
        EVERLASTING_ALLEGIANCE_CATCH = new ActionAnimation(0.05F, 0.8F, "biped/skill/everlasting_allegiance_catch", biped).addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, true);
        SHARP_STAB = new AttackAnimation(0.15F, 0.05F, 0.1F, 0.15F, 0.7F, ColliderPreset.LONGSWORD, biped.toolR, "biped/skill/sharp_stab", biped);
    }

    public static class ReusableSources {

        public static final AnimationEvent.AnimationEventConsumer RESIZE_BOUNDING_BOX = (entitypatch, animation, params) -> {
            if (params != null) {
                entitypatch.resetSize((EntityDimensions) params[0]);
            }
        };

        public static final AnimationEvent.AnimationEventConsumer RESTORE_BOUNDING_BOX = (entitypatch, animation, params) -> entitypatch.getOriginal().m_6210_();

        public static final AnimationEvent.AnimationEventConsumer WING_FLAP = (entitypatch, animation, params) -> {
            if (entitypatch instanceof EnderDragonPatch enderDragonPatch) {
                enderDragonPatch.getOriginal().onFlap();
            }
        };

        public static final AnimationEvent.AnimationEventConsumer FRACTURE_GROUND_SIMPLE = (entitypatch, animation, params) -> {
            Vec3 position = entitypatch.getOriginal().m_20182_();
            OpenMatrix4f modelTransform = entitypatch.getArmature().getBindedTransformFor(animation.getPoseByTime(entitypatch, (Float) params[3], 1.0F), (Joint) params[1]).mulFront(OpenMatrix4f.createTranslation((float) position.x, (float) position.y, (float) position.z).mulBack(OpenMatrix4f.createRotatorDeg(180.0F, Vec3f.Y_AXIS).mulBack(entitypatch.getModelMatrix(1.0F))));
            Level level = entitypatch.getOriginal().m_9236_();
            Vec3 weaponEdge = OpenMatrix4f.transform(modelTransform, ((Vec3f) params[0]).toDoubleVector());
            BlockHitResult hitResult = level.m_45547_(new ClipContext(position.add(0.0, 0.1, 0.0), weaponEdge, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entitypatch.getOriginal()));
            Vec3 slamStartPos;
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                Direction direction = hitResult.getDirection();
                BlockPos collidePos = hitResult.getBlockPos().offset(direction.getStepX(), direction.getStepY(), direction.getStepZ());
                if (!LevelUtil.canTransferShockWave(level, collidePos, level.getBlockState(collidePos))) {
                    collidePos = collidePos.below();
                }
                slamStartPos = new Vec3((double) collidePos.m_123341_(), (double) collidePos.m_123342_(), (double) collidePos.m_123343_());
            } else {
                slamStartPos = weaponEdge.subtract(0.0, 1.0, 0.0);
            }
            LevelUtil.circleSlamFracture(entitypatch.getOriginal(), level, slamStartPos, (Double) params[2], false, false);
        };

        public static final AnimationEvent.AnimationEventConsumer FRACTURE_METEOR_STRIKE = (entitypatch, animation, params) -> {
            if (entitypatch instanceof PlayerPatch<?> playerpatch) {
                SkillContainer skill = playerpatch.getSkill(EpicFightSkills.METEOR_STRIKE);
                if (skill != null) {
                    double slamPower = (double) ((float) Math.log((double) MeteorSlamSkill.getFallDistance(skill) * entitypatch.getOriginal().getAttributeValue(EpicFightAttributes.IMPACT.get())));
                    FRACTURE_GROUND_SIMPLE.fire(entitypatch, animation, params[0], params[1], slamPower, params[2]);
                }
            }
        };

        public static final AnimationEvent.AnimationEventConsumer SUMMON_THUNDER = (entitypatch, animation, params) -> {
            if (!entitypatch.isLogicalClient()) {
                if (animation instanceof AttackAnimation attackAnimation) {
                    AttackAnimation.Phase phase = attackAnimation.phases[1];
                    int i = (int) ((ValueModifier) phase.getProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER).orElse(ValueModifier.setter(3.0F))).getTotalValue(0.0F);
                    float damage = ((ValueModifier) phase.getProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER).orElse(ValueModifier.setter(8.0F))).getTotalValue(0.0F);
                    LivingEntity original = entitypatch.getOriginal();
                    ServerLevel level = (ServerLevel) original.m_9236_();
                    float total = damage + ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create().get(original, original.getItemInHand(InteractionHand.MAIN_HAND), null, damage);
                    List<Entity> list = level.m_6249_(original, original.m_20191_().inflate(10.0, 4.0, 10.0), ex -> !(ex.distanceToSqr(original) > 100.0) && !ex.isAlliedTo(original) && ((LivingEntity) entitypatch.getOriginal()).hasLineOfSight(ex));
                    list = HitEntityList.Priority.HOSTILITY.sort(entitypatch, list);
                    int count = 0;
                    while (count < i && count < list.size()) {
                        Entity e = (Entity) list.get(count++);
                        BlockPos blockpos = e.blockPosition();
                        LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(level);
                        lightningbolt.setVisualOnly(true);
                        lightningbolt.m_20219_(Vec3.atBottomCenterOf(blockpos));
                        lightningbolt.setDamage(0.0F);
                        lightningbolt.setCause(entitypatch instanceof ServerPlayerPatch serverPlayerPatch ? serverPlayerPatch.getOriginal() : null);
                        DamageSource dmgSource = new DamageSource(e.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.LIGHTNING_BOLT), entitypatch.getOriginal());
                        EpicFightDamageSource damageSource = attackAnimation.getEpicFightDamageSource(dmgSource, entitypatch, e, phase).setHurtItem(entitypatch.getOriginal().getItemInHand(InteractionHand.MAIN_HAND));
                        e.hurt(damageSource, total);
                        e.thunderHit(level, lightningbolt);
                        level.addFreshEntity(lightningbolt);
                    }
                    if (count > 0) {
                        if (level.m_46469_().getBoolean(GameRules.RULE_WEATHER_CYCLE) && level.f_46441_.nextFloat() < 0.08F && level.m_46661_(1.0F) < 1.0F) {
                            level.setWeatherParameters(0, Mth.randomBetweenInclusive(level.f_46441_, 12000, 180000), true, true);
                        }
                        original.m_5496_(SoundEvents.TRIDENT_THUNDER, 5.0F, 1.0F);
                    }
                }
            }
        };

        public static final AnimationEvent.AnimationEventConsumer PLAY_SOUND = (entitypatch, animation, params) -> entitypatch.playSound((SoundEvent) params[0], 0.0F, 0.0F);

        public static final AnimationProperty.PoseModifier COMBO_ATTACK_DIRECTION_MODIFIER = (self, pose, entitypatch, time, partialTicks) -> {
            if (self.isStaticAnimation()) {
                if (entitypatch instanceof PlayerPatch<?> playerpatch && playerpatch.isFirstPerson()) {
                    return;
                }
                float pitch = entitypatch.getAttackDirectionPitch();
                JointTransform chest = pose.getOrDefaultTransform("Chest");
                chest.frontResult(JointTransform.getRotation(QuaternionUtils.XP.rotationDegrees(-pitch)), OpenMatrix4f::mulAsOriginFront);
                if (entitypatch instanceof PlayerPatch) {
                    JointTransform head = pose.getOrDefaultTransform("Head");
                    MathUtils.mulQuaternion(QuaternionUtils.XP.rotationDegrees(pitch), head.rotation(), head.rotation());
                }
            }
        };

        public static final AnimationProperty.PoseModifier ROOT_X_MODIFIER = (self, pose, entitypatch, time, partialTicks) -> {
            float pitch = -entitypatch.getOriginal().m_146909_();
            JointTransform chest = pose.getOrDefaultTransform("Root");
            chest.frontResult(JointTransform.getRotation(QuaternionUtils.XP.rotationDegrees(-pitch)), OpenMatrix4f::mulAsOriginFront);
        };

        public static final AnimationProperty.PoseModifier FLYING_CORRECTION = (self, pose, entitypatch, elapsedTime, partialTicks) -> {
            Vec3 vec3d = entitypatch.getOriginal().m_20252_(partialTicks);
            Vec3 vec3d1 = entitypatch.getOriginal().m_20184_();
            double d0 = vec3d1.horizontalDistanceSqr();
            double d1 = vec3d.horizontalDistanceSqr();
            if (d0 > 0.0 && d1 > 0.0) {
                JointTransform root = pose.getOrDefaultTransform("Root");
                JointTransform head = pose.getOrDefaultTransform("Head");
                double d2 = (vec3d1.x * vec3d.x + vec3d1.z * vec3d.z) / (Math.sqrt(d0) * Math.sqrt(d1));
                double d3 = vec3d1.x * vec3d.z - vec3d1.z * vec3d.x;
                float zRot = Mth.clamp((float) (Math.signum(d3) * Math.acos(d2)), -1.0F, 1.0F);
                root.frontResult(JointTransform.getRotation(QuaternionUtils.ZP.rotation(zRot)), OpenMatrix4f::mulAsOriginFront);
                float xRot = (float) MathUtils.getXRotOfVector(vec3d1) * 2.0F;
                MathUtils.mulQuaternion(QuaternionUtils.XP.rotationDegrees(xRot), root.rotation(), root.rotation());
                MathUtils.mulQuaternion(QuaternionUtils.XP.rotationDegrees(-xRot), head.rotation(), head.rotation());
            }
        };

        public static final AnimationProperty.PoseModifier FLYING_CORRECTION2 = (self, pose, entitypatch, elapsedTime, partialTicks) -> {
            Vec3 vec3d = entitypatch.getOriginal().m_20252_(partialTicks);
            Vec3 vec3d1 = entitypatch.getOriginal().m_20184_();
            double d0 = vec3d1.horizontalDistanceSqr();
            double d1 = vec3d.horizontalDistanceSqr();
            if (d0 > 0.0 && d1 > 0.0) {
                JointTransform root = pose.getOrDefaultTransform("Root");
                JointTransform head = pose.getOrDefaultTransform("Head");
                float xRot = (float) MathUtils.getXRotOfVector(vec3d1) * 2.0F;
                MathUtils.mulQuaternion(QuaternionUtils.XP.rotationDegrees(-xRot), root.rotation(), root.rotation());
                MathUtils.mulQuaternion(QuaternionUtils.XP.rotationDegrees(xRot), head.rotation(), head.rotation());
            }
        };

        public static final AnimationProperty.PoseModifier MAP_ARMS_CORRECTION = (self, pose, entitypatch, elapsedTime, partialTicks) -> {
            float xRot = 50.0F - (entitypatch.getOriginal().f_19860_ + (entitypatch.getOriginal().m_146909_() - entitypatch.getOriginal().f_19860_) * partialTicks);
            xRot = Mth.clamp(xRot, 0.0F, 50.0F);
            JointTransform shoulderL = pose.getOrDefaultTransform("Shoulder_L");
            JointTransform shoulderR = pose.getOrDefaultTransform("Shoulder_R");
            float trans = xRot / 500.0F;
            shoulderL.jointLocal(JointTransform.getTranslation(new Vec3f(0.0F, trans, -trans)), OpenMatrix4f::mul);
            shoulderR.jointLocal(JointTransform.getTranslation(new Vec3f(0.0F, trans, -trans)), OpenMatrix4f::mul);
            shoulderL.frontResult(JointTransform.getRotation(QuaternionUtils.XP.rotationDegrees(xRot)), OpenMatrix4f::mulAsOriginFront);
            shoulderR.frontResult(JointTransform.getRotation(QuaternionUtils.XP.rotationDegrees(xRot)), OpenMatrix4f::mulAsOriginFront);
        };

        public static final AnimationProperty.PlaybackTimeModifier CONSTANT_ONE = (self, entitypatch, speed, elapsedTime) -> 1.0F;

        public static final AnimationProperty.PlaybackTimeModifier CHARGING = (self, entitypatch, speed, elapsedTime) -> self instanceof LinkAnimation ? 1.0F : (float) (-Math.pow((double) ((self.getTotalTime() - elapsedTime) / self.getTotalTime() - 1.0F), 2.0)) + 1.0F;
    }
}