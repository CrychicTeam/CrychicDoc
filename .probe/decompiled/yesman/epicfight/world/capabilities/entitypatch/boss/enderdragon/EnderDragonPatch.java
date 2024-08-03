package yesman.epicfight.world.capabilities.entitypatch.boss.enderdragon;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.TransformSheet;
import yesman.epicfight.api.animation.types.ActionAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.animation.types.procedural.IKInfo;
import yesman.epicfight.api.animation.types.procedural.TipPointAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.item.EpicFightItems;
import yesman.epicfight.world.item.SkillBookItem;

public class EnderDragonPatch extends MobPatch<EnderDragon> {

    public static final TargetingConditions DRAGON_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight();

    public static EnderDragonPatch INSTANCE_CLIENT;

    public static EnderDragonPatch INSTANCE_SERVER;

    private final Map<String, TipPointAnimation> tipPointAnimations = Maps.newHashMap();

    private final Map<LivingMotions, StaticAnimation> livingMotions = Maps.newHashMap();

    private final Map<Player, Integer> contributors = Maps.newHashMap();

    private boolean groundPhase;

    public float xRoot;

    public float xRootO;

    public float zRoot;

    public float zRootO;

    public LivingMotion prevMotion = LivingMotions.FLY;

    public void onConstructed(EnderDragon entityIn) {
        this.livingMotions.put(LivingMotions.IDLE, Animations.DRAGON_IDLE);
        this.livingMotions.put(LivingMotions.WALK, Animations.DRAGON_WALK);
        this.livingMotions.put(LivingMotions.FLY, Animations.DRAGON_FLY);
        this.livingMotions.put(LivingMotions.CHASE, Animations.DRAGON_AIRSTRIKE);
        this.livingMotions.put(LivingMotions.DEATH, Animations.DRAGON_DEATH);
        super.onConstructed(entityIn);
        this.currentLivingMotion = LivingMotions.FLY;
    }

    public void onJoinWorld(EnderDragon enderdragon, EntityJoinLevelEvent event) {
        super.onJoinWorld(enderdragon, event);
        DragonPhaseInstance currentPhase = this.original.phaseManager.getCurrentPhase();
        EnderDragonPhase<?> startPhase = currentPhase != null && currentPhase instanceof PatchedDragonPhase ? this.original.phaseManager.getCurrentPhase().getPhase() : PatchedPhases.FLYING;
        this.original.phaseManager = new PhaseManagerPatch(this.original, this);
        this.original.phaseManager.setPhase(startPhase);
        enderdragon.m_274367_(1.0F);
        if (enderdragon.m_9236_().isClientSide()) {
            INSTANCE_CLIENT = this;
        } else {
            INSTANCE_SERVER = this;
        }
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.original.m_21051_(EpicFightAttributes.IMPACT.get()).setBaseValue(8.0);
        this.original.m_21051_(EpicFightAttributes.MAX_STRIKES.get()).setBaseValue(Double.MAX_VALUE);
        this.original.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(10.0);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void initAnimator(ClientAnimator clientAnimator) {
        for (Entry<LivingMotions, StaticAnimation> livingmotionEntry : this.livingMotions.entrySet()) {
            clientAnimator.addLivingAnimation((LivingMotion) livingmotionEntry.getKey(), (StaticAnimation) livingmotionEntry.getValue());
        }
        clientAnimator.setCurrentMotionsAsDefault();
    }

    @Override
    public void updateMotion(boolean considerInaction) {
        if (this.original.m_21223_() <= 0.0F) {
            this.currentLivingMotion = LivingMotions.DEATH;
        } else if (this.state.inaction() && considerInaction) {
            this.currentLivingMotion = LivingMotions.INACTION;
        } else {
            DragonPhaseInstance phase = this.original.getPhaseManager().getCurrentPhase();
            if (!this.groundPhase) {
                if (phase.getPhase() == PatchedPhases.AIRSTRIKE && ((DragonAirstrikePhase) phase).isActuallyAttacking()) {
                    this.currentLivingMotion = LivingMotions.CHASE;
                } else {
                    this.currentLivingMotion = LivingMotions.FLY;
                }
            } else if (phase.getPhase() == PatchedPhases.GROUND_BATTLE) {
                if (this.original.m_5448_() != null) {
                    this.currentLivingMotion = LivingMotions.WALK;
                } else {
                    this.currentLivingMotion = LivingMotions.IDLE;
                }
            } else {
                this.currentLivingMotion = LivingMotions.IDLE;
            }
        }
    }

    @Override
    public void tick(LivingEvent.LivingTickEvent event) {
        super.tick(event);
        if (this.original.getPhaseManager().getCurrentPhase().isSitting()) {
            this.original.nearestCrystal = null;
        }
    }

    @Override
    public void serverTick(LivingEvent.LivingTickEvent event) {
        super.serverTick(event);
        this.original.f_20916_ = 2;
        this.original.m_21574_().tick();
        this.updateMotion(true);
        if (this.prevMotion != this.currentLivingMotion && !this.animator.getEntityState().inaction()) {
            if (this.livingMotions.containsKey(this.currentLivingMotion)) {
                this.animator.playAnimation((StaticAnimation) this.livingMotions.get(this.currentLivingMotion), 0.0F);
            }
            this.prevMotion = this.currentLivingMotion;
        }
        this.updateTipPoints();
        Entity bodyPart = this.original.getParts()[2];
        AABB bodyBoundingBox = bodyPart.getBoundingBox();
        List<Entity> list = this.original.m_9236_().getEntities(this.original, bodyBoundingBox, EntitySelector.pushableBy(this.original));
        if (!list.isEmpty()) {
            for (int l = 0; l < list.size(); l++) {
                Entity entity = (Entity) list.get(l);
                double d0 = entity.getX() - this.original.m_20185_();
                double d1 = entity.getZ() - this.original.m_20189_();
                double d2 = Mth.absMax(d0, d1);
                if (d2 >= 0.01) {
                    d2 = Math.sqrt(d2);
                    d0 /= d2;
                    d1 /= d2;
                    double d3 = 1.0 / d2;
                    if (d3 > 1.0) {
                        d3 = 1.0;
                    }
                    d0 = d0 * d3 * 0.2;
                    d1 = d1 * d3 * 0.2;
                    if (!entity.isVehicle()) {
                        entity.push(d0, 0.0, d1);
                        entity.hurtMarked = true;
                    }
                }
            }
        }
        this.contributors.entrySet().removeIf(entry -> this.original.f_19797_ - (Integer) entry.getValue() > 600 || !((Player) entry.getKey()).m_6084_());
    }

    @Override
    public void clientTick(LivingEvent.LivingTickEvent event) {
        this.xRootO = this.xRoot;
        this.zRootO = this.zRoot;
        super.clientTick(event);
        this.updateTipPoints();
    }

    @Override
    public void setStunShield(float value) {
        super.setStunShield(value);
        if (value <= 0.0F) {
            DragonPhaseInstance currentPhase = this.original.getPhaseManager().getCurrentPhase();
            if (currentPhase.getPhase() == PatchedPhases.CRYSTAL_LINK && ((DragonCrystalLinkPhase) currentPhase).getChargingCount() > 0) {
                this.original.m_5496_(EpicFightSounds.NEUTRALIZE_BOSSES.get(), 5.0F, 1.0F);
                this.original.getPhaseManager().setPhase(PatchedPhases.NEUTRALIZED);
            }
        }
    }

    @Override
    public AttackResult tryHurt(DamageSource damageSource, float amount) {
        boolean isConsumingCrystal = this.original.getPhaseManager().getCurrentPhase().getPhase() == PatchedPhases.CRYSTAL_LINK;
        if (!isConsumingCrystal && amount > 0.0F && damageSource.getEntity() instanceof Player player) {
            this.contributors.put(player, this.original.f_19797_);
        }
        return super.tryHurt(damageSource, isConsumingCrystal ? 0.0F : amount);
    }

    @Override
    public void rotateTo(Entity target, float limit, boolean partialSync) {
        double d0 = target.getX() - this.original.m_20185_();
        double d1 = target.getZ() - this.original.m_20189_();
        float degree = 180.0F - (float) Math.toDegrees(Mth.atan2(d0, d1));
        this.rotateTo(degree, limit, partialSync);
    }

    @Override
    public void onDeath(LivingDeathEvent event) {
        super.onDeath(event);
        for (Player player : this.contributors.keySet()) {
            ItemStack skillbook = new ItemStack(EpicFightItems.SKILLBOOK.get());
            SkillBookItem.setContainingSkill(EpicFightSkills.DEMOLITION_LEAP, skillbook);
            player.addItem(skillbook);
        }
    }

    public void updateTipPoints() {
        for (Entry<String, TipPointAnimation> entry : this.tipPointAnimations.entrySet()) {
            if (((TipPointAnimation) entry.getValue()).isOnWorking()) {
                ((TipPointAnimation) entry.getValue()).tick();
            }
        }
        if (this.tipPointAnimations.size() > 0) {
            TipPointAnimation frontL = this.getTipPointAnimation("Leg_Front_L3");
            TipPointAnimation frontR = this.getTipPointAnimation("Leg_Front_R3");
            TipPointAnimation backL = this.getTipPointAnimation("Leg_Back_L3");
            TipPointAnimation backR = this.getTipPointAnimation("Leg_Back_R3");
            float entityPosY = (float) this.original.m_20182_().y;
            float yFrontL = frontL != null && frontL.isTouchingGround() ? frontL.getTargetPosition().y : entityPosY;
            float yFrontR = frontR != null && frontR.isTouchingGround() ? frontR.getTargetPosition().y : entityPosY;
            float yBackL = backL != null && backL.isTouchingGround() ? backL.getTargetPosition().y : entityPosY;
            float yBackR = backR != null && backR.isTouchingGround() ? backR.getTargetPosition().y : entityPosY;
            float xdiff = (yFrontL + yBackL) * 0.5F - (yFrontR + yBackR) * 0.5F;
            float zdiff = (yFrontL + yFrontR) * 0.5F - (yBackL + yBackR) * 0.5F;
            float xdistance = 4.0F;
            float zdistance = 5.7F;
            this.xRoot = this.xRoot + Mth.clamp((float) Math.toDegrees(Math.atan2((double) zdiff, (double) zdistance)) - this.xRoot, -1.0F, 1.0F);
            this.zRoot = this.zRoot + Mth.clamp((float) Math.toDegrees(Math.atan2((double) xdiff, (double) xdistance)) - this.zRoot, -1.0F, 1.0F);
            float averageY = (yFrontL + yFrontR + yBackL + yBackR) * 0.25F;
            if (!this.isLogicalClient()) {
                float dy = averageY - entityPosY;
                this.original.m_6478_(MoverType.SELF, new Vec3(0.0, (double) dy, 0.0));
            }
        }
    }

    public int getNearbyCrystals() {
        return this.original.getDragonFight() != null ? this.original.getDragonFight().getCrystalsAlive() : 0;
    }

    public void resetTipAnimations() {
        this.tipPointAnimations.clear();
    }

    public void setFlyingPhase() {
        this.groundPhase = false;
        this.original.f_19862_ = false;
        this.original.f_19863_ = false;
    }

    public void setGroundPhase() {
        this.groundPhase = true;
    }

    public boolean isGroundPhase() {
        return this.groundPhase;
    }

    @Override
    public boolean shouldMoveOnCurrentSide(ActionAnimation actionAnimation) {
        return true;
    }

    @Override
    public SoundEvent getSwingSound(InteractionHand hand) {
        return EpicFightSounds.WHOOSH_BIG.get();
    }

    @Override
    public StaticAnimation getHitAnimation(StunType stunType) {
        return null;
    }

    @Override
    public OpenMatrix4f getModelMatrix(float partialTicks) {
        return MathUtils.getModelMatrixIntegral(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, this.original.f_19859_, this.original.m_146908_(), partialTicks, -1.0F, 1.0F, -1.0F);
    }

    @Override
    public double getAngleTo(Entity entityIn) {
        Vec3 a = this.original.m_20154_().scale(-1.0);
        Vec3 b = new Vec3(entityIn.getX() - this.original.m_20185_(), entityIn.getY() - this.original.m_20186_(), entityIn.getZ() - this.original.m_20189_()).normalize();
        double cosTheta = a.x * b.x + a.y * b.y + a.z * b.z;
        return Math.toDegrees(Math.acos(cosTheta));
    }

    @Override
    public double getAngleToHorizontal(Entity entityIn) {
        Vec3 a = this.original.m_20154_().scale(-1.0);
        Vec3 b = new Vec3(entityIn.getX() - this.original.m_20185_(), 0.0, entityIn.getZ() - this.original.m_20189_()).normalize();
        double cos = a.x * b.x + a.y * b.y + a.z * b.z;
        return Math.toDegrees(Math.acos(cos));
    }

    public TipPointAnimation getTipPointAnimation(String jointName) {
        return (TipPointAnimation) this.tipPointAnimations.get(jointName);
    }

    public void addTipPointAnimation(String jointName, Vec3f initpos, TransformSheet transformSheet, IKInfo ikSetter) {
        this.tipPointAnimations.put(jointName, new TipPointAnimation(transformSheet, initpos, ikSetter));
    }

    public Collection<TipPointAnimation> getTipPointAnimations() {
        return this.tipPointAnimations.values();
    }
}