package noppes.npcs.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fluids.FluidType;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.EventHooks;
import noppes.npcs.IChatMessages;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.VersionCompatibility;
import noppes.npcs.ai.CombatHandler;
import noppes.npcs.ai.EntityAIAnimation;
import noppes.npcs.ai.EntityAIAttackTarget;
import noppes.npcs.ai.EntityAIAvoidTarget;
import noppes.npcs.ai.EntityAIBustDoor;
import noppes.npcs.ai.EntityAIFindShade;
import noppes.npcs.ai.EntityAIFollow;
import noppes.npcs.ai.EntityAIJob;
import noppes.npcs.ai.EntityAILook;
import noppes.npcs.ai.EntityAIMoveIndoors;
import noppes.npcs.ai.EntityAIMovingPath;
import noppes.npcs.ai.EntityAIPanic;
import noppes.npcs.ai.EntityAIPounceTarget;
import noppes.npcs.ai.EntityAIRangedAttack;
import noppes.npcs.ai.EntityAIReturn;
import noppes.npcs.ai.EntityAIRole;
import noppes.npcs.ai.EntityAISprintToTarget;
import noppes.npcs.ai.EntityAITransform;
import noppes.npcs.ai.EntityAIWander;
import noppes.npcs.ai.EntityAIWatchClosest;
import noppes.npcs.ai.EntityAIWaterNav;
import noppes.npcs.ai.EntityAIWorldLines;
import noppes.npcs.ai.FlyingMoveHelper;
import noppes.npcs.ai.selector.NPCAttackSelector;
import noppes.npcs.ai.target.EntityAIClearTarget;
import noppes.npcs.ai.target.EntityAIOwnerHurtByTarget;
import noppes.npcs.ai.target.EntityAIOwnerHurtTarget;
import noppes.npcs.ai.target.NpcNearestAttackableTargetGoal;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.constants.PotionEffectType;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IProjectile;
import noppes.npcs.api.event.NpcEvent;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.client.ISynchedEntityData;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.LinkedNpcController;
import noppes.npcs.controllers.VisibilityController;
import noppes.npcs.controllers.data.DataTransform;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogOption;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.QuestData;
import noppes.npcs.entity.data.DataAI;
import noppes.npcs.entity.data.DataAbilities;
import noppes.npcs.entity.data.DataAdvanced;
import noppes.npcs.entity.data.DataDisplay;
import noppes.npcs.entity.data.DataInventory;
import noppes.npcs.entity.data.DataScript;
import noppes.npcs.entity.data.DataStats;
import noppes.npcs.entity.data.DataTimers;
import noppes.npcs.items.ItemSoulstoneFilled;
import noppes.npcs.mixin.EntityIMixin;
import noppes.npcs.mixin.GoalSelectorMixin;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketChatBubble;
import noppes.npcs.packets.client.PacketNpcUpdate;
import noppes.npcs.packets.client.PacketNpcVisibleFalse;
import noppes.npcs.packets.client.PacketNpcVisibleTrue;
import noppes.npcs.packets.client.PacketPlaySound;
import noppes.npcs.packets.client.PacketQuestCompletion;
import noppes.npcs.roles.JobBard;
import noppes.npcs.roles.JobFollower;
import noppes.npcs.roles.JobInterface;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.roles.RoleFollower;
import noppes.npcs.roles.RoleInterface;
import noppes.npcs.util.GameProfileAlt;

public abstract class EntityNPCInterface extends PathfinderMob implements IEntityAdditionalSpawnData, RangedAttackMob {

    public static final EntityDataAccessor<Boolean> Attacking = SynchedEntityData.defineId(EntityNPCInterface.class, EntityDataSerializers.BOOLEAN);

    protected static final EntityDataAccessor<Integer> Animation = SynchedEntityData.defineId(EntityNPCInterface.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<String> RoleData = SynchedEntityData.defineId(EntityNPCInterface.class, EntityDataSerializers.STRING);

    private static final EntityDataAccessor<String> JobData = SynchedEntityData.defineId(EntityNPCInterface.class, EntityDataSerializers.STRING);

    private static final EntityDataAccessor<Integer> FactionData = SynchedEntityData.defineId(EntityNPCInterface.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> Walking = SynchedEntityData.defineId(EntityNPCInterface.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> Interacting = SynchedEntityData.defineId(EntityNPCInterface.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> IsDead = SynchedEntityData.defineId(EntityNPCInterface.class, EntityDataSerializers.BOOLEAN);

    public static final GameProfileAlt CommandProfile = new GameProfileAlt();

    public static final GameProfileAlt ChatEventProfile = new GameProfileAlt();

    public static final GameProfileAlt GenericProfile = new GameProfileAlt();

    public static FakePlayer ChatEventPlayer;

    public static FakePlayer CommandPlayer;

    public static FakePlayer GenericPlayer;

    public ICustomNpc wrappedNPC;

    public final DataAbilities abilities = new DataAbilities(this);

    public DataDisplay display = new DataDisplay(this);

    public DataStats stats = new DataStats(this);

    public DataInventory inventory = new DataInventory(this);

    public final DataAI ais = new DataAI(this);

    public final DataAdvanced advanced = new DataAdvanced(this);

    public final DataScript script = new DataScript(this);

    public final DataTransform transform = new DataTransform(this);

    public final DataTimers timers = new DataTimers(this);

    public CombatHandler combatHandler = new CombatHandler(this);

    public String linkedName = "";

    public long linkedLast = 0L;

    public LinkedNpcController.LinkedData linkedData;

    public EntityDimensions baseSize = new EntityDimensions(0.6F, 1.8F, false);

    private static final EntityDimensions sizeSleep = new EntityDimensions(0.8F, 0.4F, false);

    public float scaleX;

    public float scaleY;

    public float scaleZ;

    private boolean wasKilled = false;

    public RoleInterface role = RoleInterface.NONE;

    public JobInterface job = JobInterface.NONE;

    public HashMap<Integer, DialogOption> dialogs;

    public boolean hasDied = false;

    public long killedtime = 0L;

    public long totalTicksAlive = 0L;

    private int taskCount = 1;

    public int lastInteract = 0;

    public Faction faction;

    private EntityAIRangedAttack aiRange;

    private Goal aiAttackTarget;

    public EntityAILook lookAi;

    public EntityAIAnimation animateAi;

    public List<LivingEntity> interactingEntities = new ArrayList();

    public ResourceLocation textureLocation = null;

    public ResourceLocation textureGlowLocation = null;

    public ResourceLocation textureCloakLocation = null;

    public int currentAnimation = 0;

    public int animationStart = 0;

    public int npcVersion = VersionCompatibility.ModRev;

    public IChatMessages messages;

    public boolean updateClient = false;

    public boolean updateAI = false;

    public final ServerBossEvent bossInfo;

    public final HashSet<Integer> tracking = new HashSet();

    public double prevChasingPosX;

    public double prevChasingPosY;

    public double prevChasingPosZ;

    public double chasingPosX;

    public double chasingPosY;

    public double chasingPosZ;

    private double startYPos = -6666.0;

    public EntityNPCInterface(EntityType<? extends PathfinderMob> type, Level world) {
        super(type, world);
        if (!this.isClientSide()) {
            this.wrappedNPC = new NPCWrapper<>(this);
        }
        this.registerBaseAttributes();
        this.dialogs = new HashMap();
        if (!CustomNpcs.DefaultInteractLine.isEmpty()) {
            this.advanced.interactLines.lines.put(0, new Line(CustomNpcs.DefaultInteractLine));
        }
        this.f_21364_ = 0;
        this.scaleX = this.scaleY = this.scaleZ = 0.9375F;
        this.faction = this.getFaction();
        this.setFaction(this.faction.id);
        this.updateAI = true;
        this.bossInfo = new ServerBossEvent(this.m_5446_(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS);
        this.bossInfo.setVisible(false);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return this.ais.movementType == 2;
    }

    @Override
    public boolean isPushedByFluid() {
        return this.ais.movementType != 2;
    }

    private void registerBaseAttributes() {
        this.m_21051_(Attributes.MAX_HEALTH).setBaseValue((double) this.stats.maxHealth);
        this.m_21051_(Attributes.FOLLOW_RANGE).setBaseValue((double) CustomNpcs.NpcNavRange);
        this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue((double) this.getSpeed());
        this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue((double) this.stats.melee.getStrength());
        this.m_21051_(Attributes.FLYING_SPEED).setBaseValue((double) (this.getSpeed() * 2.0F));
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_DAMAGE).add(Attributes.FLYING_SPEED).add(Attributes.FOLLOW_RANGE);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(RoleData, String.valueOf(""));
        this.f_19804_.define(JobData, String.valueOf(""));
        this.f_19804_.define(FactionData, 0);
        this.f_19804_.define(Animation, 0);
        this.f_19804_.define(Walking, false);
        this.f_19804_.define(Interacting, false);
        this.f_19804_.define(IsDead, false);
        this.f_19804_.define(Attacking, false);
    }

    @Override
    public boolean isAlive() {
        return super.m_6084_() && !this.isKilled();
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.f_19797_ % 10 == 0) {
            this.startYPos = this.calculateStartYPos(this.ais.startPos()) + 1.0;
            if (this.startYPos < (double) this.m_9236_().m_141937_() && !this.isClientSide()) {
                this.m_146870_();
            }
            EventHooks.onNPCTick(this);
        }
        this.timers.update();
        if (this.m_9236_().isClientSide && this.wasKilled != this.isKilled() && this.wasKilled) {
            this.f_20919_ = 0;
            this.m_6210_();
        }
        this.wasKilled = this.isKilled();
        if (this.currentAnimation == 14) {
            this.f_20919_ = 19;
        }
    }

    @Override
    public boolean doHurtTarget(Entity par1Entity) {
        float f = (float) this.stats.melee.getStrength();
        if (this.stats.melee.getDelay() < 10) {
            par1Entity.invulnerableTime = 0;
        }
        if (par1Entity instanceof LivingEntity) {
            NpcEvent.MeleeAttackEvent event = new NpcEvent.MeleeAttackEvent(this.wrappedNPC, (LivingEntity) par1Entity, f);
            if (EventHooks.onNPCAttacksMelee(this, event)) {
                return false;
            }
            f = event.damage;
        }
        boolean var4 = par1Entity.hurt(this.m_269291_().mobAttack(this), f);
        if (var4) {
            if (this.getOwner() instanceof Player) {
                EntityUtil.setRecentlyHit((LivingEntity) par1Entity);
            }
            if (this.stats.melee.getKnockback() > 0) {
                par1Entity.push((double) (-Mth.sin(this.m_146908_() * (float) Math.PI / 180.0F) * (float) this.stats.melee.getKnockback() * 0.5F), 0.1, (double) (Mth.cos(this.m_146908_() * (float) Math.PI / 180.0F) * (float) this.stats.melee.getKnockback() * 0.5F));
                this.m_20256_(this.m_20184_().multiply(0.6, 1.0, 0.6));
            }
            if (this.role.getType() == 6) {
                ((RoleCompanion) this.role).attackedEntity(par1Entity);
            }
        }
        if (this.stats.melee.getEffectType() != 0) {
            if (this.stats.melee.getEffectType() != 666) {
                ((LivingEntity) par1Entity).addEffect(new MobEffectInstance(PotionEffectType.getMCType(this.stats.melee.getEffectType()), this.stats.melee.getEffectTime() * 20, this.stats.melee.getEffectStrength()));
            } else {
                par1Entity.setRemainingFireTicks(this.stats.melee.getEffectTime() * 20);
            }
        }
        return var4;
    }

    @Override
    public void aiStep() {
        if (!CustomNpcs.FreezeNPCs) {
            if (this.m_21525_()) {
                super.m_8107_();
            } else {
                this.totalTicksAlive++;
                this.m_21203_();
                if (this.f_19797_ % 20 == 0) {
                    this.faction = this.getFaction();
                }
                if (!this.m_9236_().isClientSide) {
                    if (!this.isKilled() && this.f_19797_ % 20 == 0) {
                        this.advanced.scenes.update();
                        if (this.m_21223_() < this.m_21233_()) {
                            if (this.stats.healthRegen > 0 && !this.isAttacking()) {
                                this.m_5634_((float) this.stats.healthRegen);
                            }
                            if (this.stats.combatRegen > 0 && this.isAttacking()) {
                                this.m_5634_((float) this.stats.combatRegen);
                            }
                        }
                        if (this.faction.getsAttacked && !this.isAttacking()) {
                            for (Monster mob : this.m_9236_().m_45976_(Monster.class, this.m_20191_().inflate(16.0, 16.0, 16.0))) {
                                if (mob.m_5448_() == null && this.canNpcSee(mob)) {
                                    mob.m_6710_(this);
                                }
                            }
                        }
                        if (this.linkedData != null && this.linkedData.time > this.linkedLast) {
                            LinkedNpcController.Instance.loadNpcData(this);
                        }
                        if (this.updateClient) {
                            this.updateClient();
                        }
                        if (this.updateAI) {
                            this.updateTasks();
                            this.updateAI = false;
                        }
                    }
                    if (this.m_21223_() <= 0.0F && !this.isKilled()) {
                        this.m_21219_();
                        this.f_19804_.set(IsDead, true);
                        this.updateTasks();
                        this.m_6210_();
                    }
                    if (this.display.getBossbar() == 2) {
                        this.bossInfo.setVisible(this.m_5448_() != null);
                    }
                    this.f_19804_.set(Walking, !this.m_21573_().isDone());
                    this.f_19804_.set(Interacting, this.isInteracting());
                    this.combatHandler.update();
                    this.onCollide();
                }
                if (this.wasKilled != this.isKilled() && this.wasKilled) {
                    this.reset();
                }
                if (this.m_9236_().isDay() && !this.m_9236_().isClientSide && this.stats.burnInSun) {
                    float f = this.m_213856_();
                    if (f > 0.5F && this.f_19796_.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.m_9236_().m_45527_(this.m_20183_())) {
                        this.m_7311_(160);
                    }
                }
                super.m_8107_();
                if (this.m_9236_().isClientSide) {
                    this.role.clientUpdate();
                    if (this.textureCloakLocation != null) {
                        this.cloakUpdate();
                    }
                    if (this.currentAnimation != this.f_19804_.get(Animation)) {
                        this.currentAnimation = this.f_19804_.get(Animation);
                        this.animationStart = this.f_19797_;
                        this.m_6210_();
                    }
                    if (this.job.getType() == 1) {
                        ((JobBard) this.job).aiStep();
                    }
                }
                if (this.display.getBossbar() > 0) {
                    this.bossInfo.setProgress(this.m_21223_() / this.m_21233_());
                }
            }
        }
    }

    public void updateClient() {
        Packets.sendNearby(this, new PacketNpcUpdate(this.m_19879_(), this.writeSpawnData()));
        this.updateClient = false;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.m_9236_().isClientSide) {
            return this.isAttacking() ? InteractionResult.FAIL : InteractionResult.PASS;
        } else if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        } else {
            ItemStack stack = player.m_21120_(hand);
            if (stack != null) {
                Item item = stack.getItem();
                if (item == CustomItems.cloner || item == CustomItems.wand || item == CustomItems.mount || item == CustomItems.scripter) {
                    this.setTarget(null);
                    this.m_6703_(null);
                    return InteractionResult.SUCCESS;
                }
                if (item == CustomItems.moving) {
                    this.setTarget(null);
                    stack.addTagElement("NPCID", IntTag.valueOf(this.m_19879_()));
                    player.m_213846_(Component.translatable("message.pather.register", this.getName()));
                    return InteractionResult.SUCCESS;
                }
            }
            if (EventHooks.onNPCInteract(this, player)) {
                return InteractionResult.FAIL;
            } else if (!this.getFaction().isAggressiveToPlayer(player) && !this.isAttacking()) {
                this.addInteract(player);
                Dialog dialog = this.getDialog(player);
                QuestData data = PlayerData.get(player).questData.getQuestCompletion(player, this);
                if (data != null) {
                    Packets.send((ServerPlayer) player, new PacketQuestCompletion(data.quest.id));
                } else if (dialog != null) {
                    NoppesUtilServer.openDialog(player, this, dialog);
                } else if (this.role.getType() != 0) {
                    this.role.interact(player);
                } else {
                    this.say(player, this.advanced.getInteractLine());
                }
                return InteractionResult.PASS;
            } else {
                return InteractionResult.FAIL;
            }
        }
    }

    public void addInteract(LivingEntity entity) {
        if (this.ais.stopAndInteract && !this.isAttacking() && entity.isAlive() && !this.m_21525_()) {
            if (this.f_19797_ - this.lastInteract < 180) {
                this.interactingEntities.clear();
            }
            this.m_21573_().stop();
            this.lastInteract = this.f_19797_;
            if (!this.interactingEntities.contains(entity)) {
                this.interactingEntities.add(entity);
            }
        }
    }

    public boolean isInteracting() {
        return this.f_19797_ - this.lastInteract >= 40 && (!this.isClientSide() || !this.f_19804_.get(Interacting)) ? this.ais.stopAndInteract && !this.interactingEntities.isEmpty() && this.f_19797_ - this.lastInteract < 180 : true;
    }

    private Dialog getDialog(Player player) {
        for (DialogOption option : this.dialogs.values()) {
            if (option != null && option.hasDialog()) {
                Dialog dialog = option.getDialog();
                if (dialog.availability.isAvailable(player)) {
                    return dialog;
                }
            }
        }
        return null;
    }

    @Override
    public boolean hurt(DamageSource damagesource, float i) {
        if (!this.m_9236_().isClientSide && !CustomNpcs.FreezeNPCs && !damagesource.getMsgId().equals("inWall")) {
            if (damagesource.getMsgId().equals("outOfLevel") && this.isKilled()) {
                this.reset();
            }
            i = this.stats.resistances.applyResistance(damagesource, i);
            if ((float) this.f_19802_ > 20.0F / 2.0F && i <= this.f_20898_) {
                return false;
            } else {
                Entity entity = NoppesUtilServer.GetDamageSourcee(damagesource);
                LivingEntity attackingEntity = null;
                if (entity instanceof LivingEntity) {
                    attackingEntity = (LivingEntity) entity;
                }
                if (attackingEntity != null && attackingEntity == this.getOwner()) {
                    return false;
                } else {
                    if (attackingEntity instanceof EntityNPCInterface npc) {
                        if (npc.faction.id == this.faction.id) {
                            return false;
                        }
                        if (npc.getOwner() instanceof Player) {
                            this.f_20916_ = 100;
                        }
                    } else if (attackingEntity instanceof Player && this.faction.isFriendlyToPlayer((Player) attackingEntity)) {
                        ForgeHooks.onLivingAttack(this, damagesource, i);
                        return false;
                    }
                    NpcEvent.DamagedEvent event = new NpcEvent.DamagedEvent(this.wrappedNPC, entity, i, damagesource);
                    if (EventHooks.onNPCDamaged(this, event)) {
                        ForgeHooks.onLivingAttack(this, damagesource, i);
                        return false;
                    } else {
                        i = event.damage;
                        if (this.isKilled()) {
                            return false;
                        } else if (attackingEntity == null) {
                            return super.m_6469_(damagesource, i);
                        } else {
                            boolean inRange;
                            try {
                                if (!this.isAttacking()) {
                                    if (i > 0.0F) {
                                        for (EntityNPCInterface npc : this.m_9236_().m_45976_(EntityNPCInterface.class, this.m_20191_().inflate(32.0, 16.0, 32.0))) {
                                            if (!npc.isKilled() && npc.advanced.defendFaction && npc.faction.id == this.faction.id && (npc.canNpcSee(this) || npc.ais.directLOS || npc.canNpcSee(attackingEntity))) {
                                                npc.onAttack(attackingEntity);
                                            }
                                        }
                                        this.setTarget(attackingEntity);
                                    }
                                    return super.m_6469_(damagesource, i);
                                }
                                if (this.m_5448_() != null && this.m_20280_(this.m_5448_()) > this.m_20280_(attackingEntity)) {
                                    this.setTarget(attackingEntity);
                                }
                                inRange = super.m_6469_(damagesource, i);
                            } finally {
                                if (event.clearTarget) {
                                    this.setTarget(null);
                                    this.m_6703_(null);
                                }
                            }
                            return inRange;
                        }
                    }
                }
            }
        } else {
            return false;
        }
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.m_6475_(damageSrc, damageAmount);
        this.combatHandler.damage(damageSrc, damageAmount);
    }

    public void onAttack(LivingEntity entity) {
        if (entity != null && entity != this && !this.isAttacking() && this.ais.onAttack != 3 && entity != this.getOwner()) {
            super.m_6710_(entity);
        }
    }

    @Override
    public void setTarget(LivingEntity entity) {
        if ((!(entity instanceof Player) || !((Player) entity).getAbilities().invulnerable) && (entity == null || entity != this.getOwner()) && this.m_5448_() != entity) {
            if (entity != null) {
                NpcEvent.TargetEvent event = new NpcEvent.TargetEvent(this.wrappedNPC, entity);
                if (EventHooks.onNPCTarget(this, event)) {
                    return;
                }
                if (event.entity == null) {
                    entity = null;
                } else {
                    entity = event.entity.getMCEntity();
                }
            } else {
                for (WrappedGoal en : this.f_21346_.getAvailableGoals()) {
                    en.stop();
                }
                if (EventHooks.onNPCTargetLost(this, this.m_5448_())) {
                    return;
                }
            }
            if (entity != null && entity != this && this.ais.onAttack != 3 && !this.isAttacking() && !this.isClientSide()) {
                Line line = this.advanced.getAttackLine();
                if (line != null) {
                    this.saySurrounding(Line.formatTarget(line, entity));
                }
            }
            super.m_6710_(entity);
        }
    }

    @Override
    public void performRangedAttack(LivingEntity entity, float f) {
        ItemStack proj = ItemStackWrapper.MCItem(this.inventory.getProjectile());
        if (proj == null) {
            this.updateAI = true;
        } else {
            NpcEvent.RangedLaunchedEvent event = new NpcEvent.RangedLaunchedEvent(this.wrappedNPC, entity, (float) this.stats.ranged.getStrength());
            for (int i = 0; i < this.stats.ranged.getShotCount(); i++) {
                EntityProjectile projectile = this.shoot(entity, this.stats.ranged.getAccuracy(), proj, f == 1.0F);
                projectile.damage = event.damage;
                projectile.callback = (projectile1, pos, entity1) -> {
                    if (proj.getItem() == CustomItems.soulstoneFull) {
                        Entity e = ItemSoulstoneFilled.Spawn(null, proj, this.m_9236_(), pos);
                        if (e instanceof LivingEntity && entity1 instanceof LivingEntity) {
                            if (e instanceof Mob) {
                                ((Mob) e).setTarget((LivingEntity) entity1);
                            } else {
                                ((LivingEntity) e).setLastHurtByMob((LivingEntity) entity1);
                            }
                        }
                    }
                    SoundEvent soundx = this.stats.ranged.getSoundEvent(entity1 != null ? 1 : 2);
                    if (soundx != null) {
                        projectile1.m_5496_(soundx, 1.0F, 1.2F / (this.m_217043_().nextFloat() * 0.2F + 0.9F));
                    }
                    return false;
                };
                SoundEvent sound = this.stats.ranged.getSoundEvent(0);
                if (sound != null) {
                    this.m_5496_(sound, 2.0F, 1.0F);
                }
                event.projectiles.add((IProjectile) NpcAPI.Instance().getIEntity(projectile));
            }
            EventHooks.onNPCRangedLaunched(this, event);
        }
    }

    public EntityProjectile shoot(LivingEntity entity, int accuracy, ItemStack proj, boolean indirect) {
        return this.shoot(entity.m_20185_(), entity.m_20191_().minY + (double) (entity.m_20206_() / 2.0F), entity.m_20189_(), accuracy, proj, indirect);
    }

    public EntityProjectile shoot(double x, double y, double z, int accuracy, ItemStack proj, boolean indirect) {
        EntityProjectile projectile = new EntityProjectile(this.m_9236_(), this, proj.copy(), true);
        double varX = x - this.m_20185_();
        double varY = y - (this.m_20186_() + (double) this.m_20192_());
        double varZ = z - this.m_20189_();
        float varF = projectile.hasGravity() ? (float) Math.sqrt(varX * varX + varZ * varZ) : 0.0F;
        float angle = projectile.getAngleForXYZ(varX, varY, varZ, varF, indirect);
        float acc = 20.0F - (float) Mth.floor((float) accuracy / 5.0F);
        projectile.shoot(varX, varY, varZ, angle, acc);
        this.m_9236_().m_7967_(projectile);
        return projectile;
    }

    private void clearTasks(GoalSelector tasks) {
        for (WrappedGoal entityaitaskentry : new ArrayList(tasks.getAvailableGoals())) {
            tasks.removeGoal(entityaitaskentry);
        }
        tasks.getAvailableGoals().clear();
        ((GoalSelectorMixin) tasks).lockedFlags().clear();
        ((GoalSelectorMixin) tasks).disabledFlags().clear();
    }

    private void updateTasks() {
        if (this.m_9236_() != null && !this.m_9236_().isClientSide && this.m_9236_() instanceof ServerLevel) {
            ServerLevel sLevel = (ServerLevel) this.m_9236_();
            this.clearTasks(this.f_21345_);
            this.clearTasks(this.f_21346_);
            if (!this.isKilled()) {
                this.f_21346_.addGoal(0, new EntityAIClearTarget(this));
                this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
                this.f_21346_.addGoal(2, new NpcNearestAttackableTargetGoal(this, LivingEntity.class, 4, this.ais.directLOS, false, new NPCAttackSelector(this)));
                this.f_21346_.addGoal(3, new EntityAIOwnerHurtByTarget(this));
                this.f_21346_.addGoal(4, new EntityAIOwnerHurtTarget(this));
                if (this.ais.movementType == 1) {
                    this.f_21342_ = new FlyingMoveHelper(this);
                    this.f_21344_ = new FlyingPathNavigation(this, this.m_9236_());
                } else if (this.ais.movementType == 2) {
                    this.f_21342_ = new FlyingMoveHelper(this);
                    this.f_21344_ = new WaterBoundPathNavigation(this, this.m_9236_());
                } else {
                    this.f_21342_ = new MoveControl(this);
                    this.f_21344_ = new GroundPathNavigation(this, this.m_9236_());
                    this.f_21345_.addGoal(0, new EntityAIWaterNav(this));
                }
                this.taskCount = 1;
                this.addRegularEntries();
                this.doorInteractType();
                this.seekShelter();
                this.setResponse();
                this.setMoveType();
            }
        }
    }

    private void setResponse() {
        this.aiAttackTarget = this.aiRange = null;
        if (this.ais.canSprint) {
            this.f_21345_.addGoal(this.taskCount++, new EntityAISprintToTarget(this));
        }
        if (this.ais.onAttack == 1) {
            this.f_21345_.addGoal(this.taskCount++, new EntityAIPanic(this, 1.2F));
        } else if (this.ais.onAttack == 2) {
            this.f_21345_.addGoal(this.taskCount++, new EntityAIAvoidTarget(this));
        } else if (this.ais.onAttack == 0) {
            if (this.ais.canLeap) {
                this.f_21345_.addGoal(this.taskCount++, new EntityAIPounceTarget(this));
            }
            this.f_21345_.addGoal(this.taskCount, this.aiAttackTarget = new EntityAIAttackTarget(this));
            if (this.inventory.getProjectile() != null) {
                this.f_21345_.addGoal(this.taskCount++, this.aiRange = new EntityAIRangedAttack(this));
            }
        } else if (this.ais.onAttack == 3) {
        }
    }

    public boolean canFly() {
        return this.f_21344_ instanceof FlyingPathNavigation;
    }

    public void setMoveType() {
        if (this.ais.getMovingType() == 1) {
            this.f_21345_.addGoal(this.taskCount++, new EntityAIWander(this));
        }
        if (this.ais.getMovingType() == 2) {
            this.f_21345_.addGoal(this.taskCount++, new EntityAIMovingPath(this));
        }
    }

    public void doorInteractType() {
        if (this.f_21344_ instanceof GroundPathNavigation) {
            Goal aiDoor = null;
            if (this.ais.doorInteract == 1) {
                this.f_21345_.addGoal(this.taskCount++, aiDoor = new OpenDoorGoal(this, true));
            } else if (this.ais.doorInteract == 0) {
                this.f_21345_.addGoal(this.taskCount++, aiDoor = new EntityAIBustDoor(this));
            }
            ((GroundPathNavigation) this.f_21344_).setCanOpenDoors(aiDoor != null);
        }
    }

    public void seekShelter() {
        if (this.ais.findShelter == 0) {
            this.f_21345_.addGoal(this.taskCount++, new EntityAIMoveIndoors(this));
        } else if (this.ais.findShelter == 1) {
            if (!this.canFly()) {
                this.f_21345_.addGoal(this.taskCount++, new RestrictSunGoal(this));
            }
            this.f_21345_.addGoal(this.taskCount++, new EntityAIFindShade(this));
        }
    }

    public void addRegularEntries() {
        this.f_21345_.addGoal(this.taskCount++, new EntityAIReturn(this));
        this.f_21345_.addGoal(this.taskCount++, new EntityAIFollow(this));
        if (this.ais.getStandingType() != 1 && this.ais.getStandingType() != 3) {
            this.f_21345_.addGoal(this.taskCount++, new EntityAIWatchClosest(this, LivingEntity.class, 5.0F));
        }
        this.f_21345_.addGoal(this.taskCount++, this.lookAi = new EntityAILook(this));
        this.f_21345_.addGoal(this.taskCount++, new EntityAIWorldLines(this));
        this.f_21345_.addGoal(this.taskCount++, new EntityAIJob(this));
        this.f_21345_.addGoal(this.taskCount++, new EntityAIRole(this));
        this.f_21345_.addGoal(this.taskCount++, this.animateAi = new EntityAIAnimation(this));
        if (this.transform.isValid()) {
            this.f_21345_.addGoal(this.taskCount++, new EntityAITransform(this));
        }
    }

    @Override
    public float getSpeed() {
        return (float) this.ais.getWalkingSpeed() / 20.0F;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos) {
        if (this.ais.movementType == 2) {
            return this.m_20069_() ? 10.0F : 0.0F;
        } else {
            float weight = (float) this.m_9236_().m_7146_(pos) - 0.5F;
            if (this.m_9236_().getBlockState(pos).m_60804_(this.m_9236_(), pos)) {
                weight += 10.0F;
            }
            return weight;
        }
    }

    @Override
    protected int decreaseAirSupply(int par1) {
        return !this.stats.canDrown ? par1 : super.m_7302_(par1);
    }

    @Override
    public MobType getMobType() {
        return this.stats == null ? null : this.stats.creatureType;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 160;
    }

    @Override
    public void playAmbientSound() {
        if (this.isAlive()) {
            this.advanced.playSound(this.m_5448_() != null ? 1 : 0, this.m_6121_(), this.getVoicePitch());
        }
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        this.advanced.playSound(2, this.m_6121_(), this.getVoicePitch());
    }

    @Override
    public SoundEvent getDeathSound() {
        return null;
    }

    @Override
    public float getVoicePitch() {
        return this.advanced.disablePitch ? 1.0F : super.m_6100_();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (this.advanced.getSound(4) != null) {
            this.advanced.playSound(4, 0.15F, 1.0F);
        } else {
            super.m_7355_(pos, state);
        }
    }

    public ServerPlayer getFakeChatPlayer() {
        if (this.m_9236_().isClientSide) {
            return null;
        } else {
            EntityUtil.Copy(this, ChatEventPlayer);
            ChatEventProfile.npc = this;
            ((EntityIMixin) ChatEventPlayer).setLevel((ServerLevel) this.m_9236_());
            ChatEventPlayer.m_6034_(this.m_20185_(), this.m_20186_(), this.m_20189_());
            return ChatEventPlayer;
        }
    }

    public void saySurrounding(Line line) {
        if (line != null) {
            if (line.getShowText() && !line.getText().isEmpty()) {
                ServerChatEvent event = new ServerChatEvent(this.getFakeChatPlayer(), line.getText(), Component.translatable(line.getText().replace("%", "%%")));
                if (CustomNpcs.NpcSpeachTriggersChatEvent && (MinecraftForge.EVENT_BUS.post(event) || event.getMessage() == null)) {
                    return;
                }
                line.setText(event.getMessage().getString().replace("%%", "%"));
            }
            for (Player player : this.m_9236_().m_45976_(Player.class, this.m_20191_().inflate(20.0, 20.0, 20.0))) {
                this.say(player, line);
            }
        }
    }

    public void say(Player player, Line line) {
        if (line != null && this.canNpcSee(player)) {
            if (!line.getSound().isEmpty()) {
                BlockPos pos = this.m_20183_();
                Packets.send((ServerPlayer) player, new PacketPlaySound(line.getSound(), pos, this.m_6121_(), this.getVoicePitch()));
            }
            if (!line.getText().isEmpty()) {
                Packets.send((ServerPlayer) player, new PacketChatBubble(this.m_19879_(), Component.translatable(line.getText()), line.getShowText()));
            }
        }
    }

    @Override
    public boolean shouldShowName() {
        return true;
    }

    @Override
    public void push(double d, double d1, double d2) {
        if (this.isWalking() && !this.isKilled()) {
            super.m_5997_(d, d1, d2);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.npcVersion = compound.getInt("ModRev");
        VersionCompatibility.CheckNpcCompatibility(this, compound);
        this.display.readToNBT(compound);
        this.stats.readToNBT(compound);
        this.ais.readToNBT(compound);
        this.script.load(compound);
        this.timers.load(compound);
        this.advanced.readToNBT(compound);
        this.role.load(compound);
        this.job.load(compound);
        this.inventory.load(compound);
        this.transform.readToNBT(compound);
        this.killedtime = compound.getLong("KilledTime");
        this.totalTicksAlive = compound.getLong("TotalTicksAlive");
        this.linkedName = compound.getString("LinkedNpcName");
        if (!this.isClientSide()) {
            LinkedNpcController.Instance.loadNpcData(this);
        }
        this.m_21051_(Attributes.FOLLOW_RANGE).setBaseValue((double) CustomNpcs.NpcNavRange);
        this.updateAI = true;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        this.display.save(compound);
        this.stats.save(compound);
        this.ais.save(compound);
        this.script.save(compound);
        this.timers.save(compound);
        this.advanced.save(compound);
        this.role.save(compound);
        this.job.save(compound);
        this.inventory.save(compound);
        this.transform.save(compound);
        compound.putLong("KilledTime", this.killedtime);
        compound.putLong("TotalTicksAlive", this.totalTicksAlive);
        compound.putInt("ModRev", this.npcVersion);
        compound.putString("LinkedNpcName", this.linkedName);
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn) {
        EntityDimensions size = this.baseSize;
        if (this.currentAnimation == 2 || this.currentAnimation == 7 || this.f_20919_ > 0) {
            size = sizeSleep;
        } else if (this.m_20159_() || this.currentAnimation == 1) {
            size = this.baseSize.scale(1.0F, 0.77F);
        }
        size = size.scale((float) this.display.getSize() * 0.2F);
        if (this.display.getHitboxState() == 1 || this.isKilled() && this.stats.hideKilledBody) {
            size = EntityDimensions.scalable(1.0E-5F, size.height);
        }
        if ((double) (size.width / 2.0F) > this.m_9236_().getMaxEntityRadius()) {
            this.m_9236_().increaseMaxEntityRadius((double) (size.width / 2.0F));
        }
        return size;
    }

    @Override
    public void tickDeath() {
        if (this.stats.spawnCycle != 3 && this.stats.spawnCycle != 4) {
            this.f_20919_++;
            if (!this.m_9236_().isClientSide) {
                if (!this.hasDied) {
                    this.remove(Entity.RemovalReason.KILLED);
                }
                if (this.killedtime < System.currentTimeMillis() && (this.stats.spawnCycle == 0 || this.m_9236_().isDay() && this.stats.spawnCycle == 1 || !this.m_9236_().isDay() && this.stats.spawnCycle == 2)) {
                    this.reset();
                }
            }
        } else {
            super.m_6153_();
        }
    }

    public void reset() {
        this.hasDied = false;
        this.m_146912_();
        this.f_20890_ = false;
        this.revive();
        this.wasKilled = false;
        this.m_6858_(false);
        this.m_21153_(this.m_21233_());
        this.f_19804_.set(Animation, 0);
        this.f_19804_.set(Walking, false);
        this.f_19804_.set(IsDead, false);
        this.f_19804_.set(Interacting, false);
        this.interactingEntities.clear();
        this.combatHandler.reset();
        this.setTarget(null);
        this.m_6703_(null);
        this.f_20919_ = 0;
        if (this.ais.returnToStart && !this.hasOwner() && !this.isClientSide() && !this.m_20159_()) {
            this.m_7678_((double) this.getStartXPos(), this.getStartYPos(), (double) this.getStartZPos(), this.m_146908_(), this.m_146909_());
        }
        this.killedtime = 0L;
        this.m_20095_();
        this.m_21219_();
        this.travel(Vec3.ZERO);
        this.f_19867_ = this.f_19787_ = 0.0F;
        this.m_21573_().stop();
        this.currentAnimation = 0;
        this.m_6210_();
        this.updateAI = true;
        this.ais.movingPos = 0;
        if (this.getOwner() != null) {
            this.getOwner().setLastHurtMob(null);
        }
        this.bossInfo.setVisible(this.display.getBossbar() == 1);
        this.job.reset();
        EventHooks.onNPCInit(this);
    }

    public void onCollide() {
        if (this.isAlive() && this.f_19797_ % 4 == 0 && !this.m_9236_().isClientSide) {
            AABB axisalignedbb = null;
            if (this.m_20202_() != null && this.m_20202_().isAlive()) {
                axisalignedbb = this.m_20191_().minmax(this.m_20202_().getBoundingBox()).inflate(1.0, 0.0, 1.0);
            } else {
                axisalignedbb = this.m_20191_().inflate(1.0, 0.5, 1.0);
            }
            List list = this.m_9236_().m_45976_(LivingEntity.class, axisalignedbb);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    Entity entity = (Entity) list.get(i);
                    if (entity != this && entity.isAlive()) {
                        EventHooks.onNPCCollide(this, entity);
                    }
                }
            }
        }
    }

    @Override
    public void handleInsidePortal(BlockPos pos) {
    }

    public void cloakUpdate() {
        this.prevChasingPosX = this.chasingPosX;
        this.prevChasingPosY = this.chasingPosY;
        this.prevChasingPosZ = this.chasingPosZ;
        double d0 = this.m_20185_() - this.chasingPosX;
        double d1 = this.m_20186_() - this.chasingPosY;
        double d2 = this.m_20189_() - this.chasingPosZ;
        double d3 = 10.0;
        if (d0 > 10.0) {
            this.chasingPosX = this.m_20185_();
            this.prevChasingPosX = this.chasingPosX;
        }
        if (d2 > 10.0) {
            this.chasingPosZ = this.m_20189_();
            this.prevChasingPosZ = this.chasingPosZ;
        }
        if (d1 > 10.0) {
            this.chasingPosY = this.m_20186_();
            this.prevChasingPosY = this.chasingPosY;
        }
        if (d0 < -10.0) {
            this.chasingPosX = this.m_20185_();
            this.prevChasingPosX = this.chasingPosX;
        }
        if (d2 < -10.0) {
            this.chasingPosZ = this.m_20189_();
            this.prevChasingPosZ = this.chasingPosZ;
        }
        if (d1 < -10.0) {
            this.chasingPosY = this.m_20186_();
            this.prevChasingPosY = this.chasingPosY;
        }
        this.chasingPosX += d0 * 0.25;
        this.chasingPosZ += d2 * 0.25;
        this.chasingPosY += d1 * 0.25;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToPlayer) {
        return this.stats != null && this.stats.spawnCycle == 4;
    }

    @Override
    public ItemStack getMainHandItem() {
        IItemStack item = null;
        if (this.isAttacking()) {
            item = this.inventory.getRightHand();
        } else if (this.role.getType() == 6) {
            item = ((RoleCompanion) this.role).getItemInHand();
        } else if (this.job.overrideMainHand) {
            item = this.job.getMainhand();
        } else {
            item = this.inventory.getRightHand();
        }
        return ItemStackWrapper.MCItem(item);
    }

    @Override
    public ItemStack getOffhandItem() {
        IItemStack item = null;
        if (this.isAttacking()) {
            item = this.inventory.getLeftHand();
        } else if (this.job.overrideOffHand) {
            item = this.job.getOffhand();
        } else {
            item = this.inventory.getLeftHand();
        }
        return ItemStackWrapper.MCItem(item);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return this.getMainHandItem();
        } else {
            return slot == EquipmentSlot.OFFHAND ? this.getOffhandItem() : ItemStackWrapper.MCItem(this.inventory.getArmor(3 - slot.getIndex()));
        }
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack item) {
        if (slot == EquipmentSlot.MAINHAND) {
            this.inventory.weapons.put(0, NpcAPI.Instance().getIItemStack(item));
        } else if (slot == EquipmentSlot.OFFHAND) {
            this.inventory.weapons.put(2, NpcAPI.Instance().getIItemStack(item));
        } else {
            this.inventory.armor.put(3 - slot.getIndex(), NpcAPI.Instance().getIItemStack(item));
        }
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        ArrayList<ItemStack> list = new ArrayList();
        for (int i = 0; i < 4; i++) {
            list.add(ItemStackWrapper.MCItem((IItemStack) this.inventory.armor.get(3 - i)));
        }
        return list;
    }

    @Override
    public Iterable<ItemStack> getAllSlots() {
        ArrayList list = new ArrayList();
        list.add(ItemStackWrapper.MCItem((IItemStack) this.inventory.weapons.get(0)));
        list.add(ItemStackWrapper.MCItem((IItemStack) this.inventory.weapons.get(2)));
        return list;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
    }

    @Override
    protected void dropFromLootTable(DamageSource damageSourceIn, boolean attackedRecently) {
    }

    @Override
    public void die(DamageSource damagesource) {
        this.m_6858_(false);
        this.m_21573_().stop();
        this.m_20095_();
        this.m_21219_();
        if (!this.isClientSide()) {
            this.advanced.playSound(3, this.m_6121_(), this.getVoicePitch());
            Entity attackingEntity = NoppesUtilServer.GetDamageSourcee(damagesource);
            NpcEvent.DiedEvent event = new NpcEvent.DiedEvent(this.wrappedNPC, damagesource, attackingEntity);
            event.droppedItems = this.inventory.getItemsRNG();
            event.expDropped = this.inventory.getExpRNG();
            event.line = this.advanced.getKilledLine();
            EventHooks.onNPCDied(this, event);
            this.bossInfo.setVisible(false);
            this.inventory.dropStuff(event, attackingEntity, damagesource);
            if (event.line != null) {
                this.saySurrounding(Line.formatTarget((Line) event.line, attackingEntity instanceof LivingEntity ? (LivingEntity) attackingEntity : null));
            }
        }
        super.m_6667_(damagesource);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.m_6457_(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.m_6452_(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        if (reason != Entity.RemovalReason.KILLED) {
            super.m_142687_(reason);
        } else {
            this.hasDied = true;
            this.m_20153_();
            this.m_8127_();
            if (!this.m_9236_().isClientSide && this.stats.spawnCycle != 3 && this.stats.spawnCycle != 4) {
                this.m_21153_(-1.0F);
                this.m_6858_(false);
                this.m_21573_().stop();
                this.setCurrentAnimation(2);
                this.m_6210_();
                if (this.killedtime <= 0L) {
                    this.killedtime = (long) (this.stats.respawnTime * 1000) + System.currentTimeMillis();
                }
                this.role.killed();
                this.job.killed();
            } else {
                this.delete();
            }
        }
    }

    public void delete() {
        VisibilityController.instance.remove(this);
        this.role.delete();
        this.job.delete();
        super.m_142687_(Entity.RemovalReason.DISCARDED);
    }

    public float getStartXPos() {
        return (float) this.ais.startPos().m_123341_() + this.ais.bodyOffsetX / 10.0F;
    }

    public float getStartZPos() {
        return (float) this.ais.startPos().m_123343_() + this.ais.bodyOffsetZ / 10.0F;
    }

    public boolean isVeryNearAssignedPlace() {
        double xx = this.m_20185_() - (double) this.getStartXPos();
        double zz = this.m_20189_() - (double) this.getStartZPos();
        return !(xx < -0.2) && !(xx > 0.2) ? !(zz < -0.2) && !(zz > 0.2) : false;
    }

    public double getStartYPos() {
        return this.startYPos < (double) this.m_9236_().m_141937_() ? this.calculateStartYPos(this.ais.startPos()) : this.startYPos;
    }

    private double calculateStartYPos(BlockPos pos) {
        BlockPos startPos = this.ais.startPos();
        while (pos.m_123342_() > this.m_9236_().m_141937_()) {
            BlockState state = this.m_9236_().getBlockState(pos);
            VoxelShape shape = state.m_60808_(this.m_9236_(), pos);
            if (!shape.isEmpty()) {
                AABB bb = shape.bounds().move(pos);
                if (this.ais.movementType != 2 || startPos.m_123342_() > pos.m_123342_() || !state.m_60713_(Blocks.WATER)) {
                    return bb.maxY;
                }
                pos = pos.below();
            } else {
                pos = pos.below();
            }
        }
        return (double) this.m_9236_().m_141937_();
    }

    private BlockPos calculateTopPos(BlockPos pos) {
        for (BlockPos check = pos; check.m_123342_() > this.m_9236_().m_141937_(); check = check.below()) {
            BlockState state = this.m_9236_().getBlockState(pos);
            VoxelShape shape = state.m_60808_(this.m_9236_(), pos);
            if (!shape.isEmpty()) {
                AABB bb = shape.bounds().move(pos);
                if (bb != null) {
                    return check;
                }
            }
        }
        return pos;
    }

    public boolean isInRange(Entity entity, double range) {
        return this.isInRange(entity.getX(), entity.getY(), entity.getZ(), range);
    }

    public boolean isInRange(double posX, double posY, double posZ, double range) {
        double y = Math.abs(this.m_20186_() - posY);
        if (posY >= (double) this.m_9236_().m_141937_() && y > range) {
            return false;
        } else {
            double x = Math.abs(this.m_20185_() - posX);
            double z = Math.abs(this.m_20189_() - posZ);
            return x <= range && z <= range;
        }
    }

    public void givePlayerItem(Player player, ItemStack item) {
        if (!this.m_9236_().isClientSide) {
            item = item.copy();
            float f = 0.7F;
            double d = (double) (this.m_9236_().random.nextFloat() * f) + (double) (1.0F - f);
            double d1 = (double) (this.m_9236_().random.nextFloat() * f) + (double) (1.0F - f);
            double d2 = (double) (this.m_9236_().random.nextFloat() * f) + (double) (1.0F - f);
            ItemEntity entityitem = new ItemEntity(this.m_9236_(), this.m_20185_() + d, this.m_20186_() + d1, this.m_20189_() + d2, item);
            entityitem.setPickUpDelay(2);
            this.m_9236_().m_7967_(entityitem);
            int i = item.getCount();
            if (player.getInventory().add(item)) {
                this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                player.m_7938_(entityitem, i);
                if (item.getCount() <= 0) {
                    entityitem.m_146870_();
                }
            }
        }
    }

    @Override
    public boolean isSleeping() {
        return this.currentAnimation == 2 && !this.isAttacking();
    }

    public boolean isWalking() {
        return this.ais.getMovingType() != 0 || this.isAttacking() || this.isFollower() || this.f_19804_.get(Walking);
    }

    @Override
    public boolean isCrouching() {
        return this.currentAnimation == 4;
    }

    @Override
    public void knockback(double strength, double ratioX, double ratioZ) {
        super.m_147240_(strength * (double) (2.0F - this.stats.resistances.knockback), ratioX, ratioZ);
    }

    public Faction getFaction() {
        Faction fac = FactionController.instance.getFaction(this.f_19804_.get(FactionData));
        return fac == null ? FactionController.instance.getFaction(FactionController.instance.getFirstFactionId()) : fac;
    }

    public boolean isClientSide() {
        return this.m_9236_() == null || this.m_9236_().isClientSide;
    }

    public void setFaction(int id) {
        if (id >= 0 && !this.isClientSide()) {
            this.f_19804_.set(FactionData, id);
        }
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        if (this.stats.potionImmune) {
            return false;
        } else {
            return this.getMobType() == MobType.ARTHROPOD && effect.getEffect() == MobEffects.POISON ? false : super.m_7301_(effect);
        }
    }

    public boolean isAttacking() {
        return this.f_19804_.get(Attacking);
    }

    public boolean isKilled() {
        return this.m_213877_() || this.f_19804_.get(IsDead);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeNbt(this.writeSpawnData());
    }

    public CompoundTag writeSpawnData() {
        CompoundTag compound = new CompoundTag();
        this.display.save(compound);
        compound.putInt("MaxHealth", this.stats.maxHealth);
        compound.put("Armor", NBTTags.nbtIItemStackMap(this.inventory.armor));
        compound.put("Weapons", NBTTags.nbtIItemStackMap(this.inventory.weapons));
        compound.putInt("Speed", this.ais.getWalkingSpeed());
        compound.putBoolean("DeadBody", this.stats.hideKilledBody);
        compound.putInt("StandingState", this.ais.getStandingType());
        compound.putInt("MovingState", this.ais.getMovingType());
        compound.putInt("Orientation", this.ais.orientation);
        compound.putFloat("PositionXOffset", this.ais.bodyOffsetX);
        compound.putFloat("PositionYOffset", this.ais.bodyOffsetY);
        compound.putFloat("PositionZOffset", this.ais.bodyOffsetZ);
        compound.putInt("Role", this.role.getType());
        compound.putInt("Job", this.job.getType());
        if (this.job.getType() == 1) {
            CompoundTag bard = new CompoundTag();
            this.job.save(bard);
            compound.put("Bard", bard);
        }
        if (this.job.getType() == 9) {
            CompoundTag bard = new CompoundTag();
            this.job.save(bard);
            compound.put("Puppet", bard);
        }
        if (this.role.getType() == 6) {
            CompoundTag bard = new CompoundTag();
            this.role.save(bard);
            compound.put("Companion", bard);
        }
        if (this instanceof EntityCustomNpc) {
            compound.put("ModelData", ((EntityCustomNpc) this).modelData.save());
        }
        return compound;
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buf) {
        this.readSpawnData(buf.readNbt());
    }

    public void readSpawnData(CompoundTag compound) {
        this.stats.setMaxHealth(compound.getInt("MaxHealth"));
        this.ais.setWalkingSpeed(compound.getInt("Speed"));
        this.stats.hideKilledBody = compound.getBoolean("DeadBody");
        this.ais.setStandingType(compound.getInt("StandingState"));
        this.ais.setMovingType(compound.getInt("MovingState"));
        this.ais.orientation = compound.getInt("Orientation");
        this.ais.bodyOffsetX = compound.getFloat("PositionXOffset");
        this.ais.bodyOffsetY = compound.getFloat("PositionYOffset");
        this.ais.bodyOffsetZ = compound.getFloat("PositionZOffset");
        this.inventory.armor = NBTTags.getIItemStackMap(compound.getList("Armor", 10));
        this.inventory.weapons = NBTTags.getIItemStackMap(compound.getList("Weapons", 10));
        this.advanced.setRole(compound.getInt("Role"));
        this.advanced.setJob(compound.getInt("Job"));
        if (this.job.getType() == 1) {
            CompoundTag bard = compound.getCompound("Bard");
            this.job.load(bard);
        }
        if (this.job.getType() == 9) {
            CompoundTag puppet = compound.getCompound("Puppet");
            this.job.load(puppet);
        }
        if (this.role.getType() == 6) {
            CompoundTag puppet = compound.getCompound("Companion");
            this.role.load(puppet);
        }
        if (this instanceof EntityCustomNpc) {
            ((EntityCustomNpc) this).modelData.load(compound.getCompound("ModelData"));
        }
        this.display.readToNBT(compound);
        this.m_6210_();
    }

    @Override
    public CommandSourceStack createCommandSourceStack() {
        if (this.m_9236_().isClientSide) {
            return super.m_20203_();
        } else {
            EntityUtil.Copy(this, CommandPlayer);
            ((EntityIMixin) CommandPlayer).setLevel((ServerLevel) this.m_9236_());
            CommandPlayer.m_6034_(this.m_20185_(), this.m_20186_(), this.m_20189_());
            return new CommandSourceStack(this, this.m_20182_(), this.m_20155_(), this.m_9236_() instanceof ServerLevel ? (ServerLevel) this.m_9236_() : null, this.m_8088_(), this.getName().getString(), this.m_5446_(), this.m_9236_().getServer(), this);
        }
    }

    @Override
    public Component getName() {
        return Component.translatable(this.display.getName());
    }

    public void setImmuneToFire(boolean immuneToFire) {
        this.stats.immuneToFire = immuneToFire;
    }

    @Override
    public boolean fireImmune() {
        return this.stats.immuneToFire;
    }

    @Override
    public boolean causeFallDamage(float distance, float modifier, DamageSource source) {
        return !this.stats.noFallDamage ? super.m_142535_(distance, modifier, source) : false;
    }

    @Override
    public void makeStuckInBlock(BlockState state, Vec3 motionMultiplierIn) {
        if (state != null && !state.m_60713_(Blocks.COBWEB) || !this.stats.ignoreCobweb) {
            super.m_7601_(state, motionMultiplierIn);
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isKilled() && this.display.getHitboxState() == 2;
    }

    @Override
    protected void pushEntities() {
        if (this.display.getHitboxState() == 0) {
            super.m_6138_();
        }
    }

    @Override
    public boolean isPushable() {
        return this.isWalking() && !this.isKilled();
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return this.display.getHitboxState() == 0 ? super.m_7752_() : PushReaction.IGNORE;
    }

    public EntityAIRangedAttack getRangedTask() {
        return this.aiRange;
    }

    public String getRoleData() {
        return this.f_19804_.get(RoleData);
    }

    public void setRoleData(String s) {
        this.f_19804_.set(RoleData, s);
    }

    public String getJobData() {
        return this.f_19804_.get(RoleData);
    }

    public void setJobData(String s) {
        this.f_19804_.set(RoleData, s);
    }

    @Override
    public Level getCommandSenderWorld() {
        return this.m_9236_();
    }

    @Override
    public boolean isInvisibleTo(Player player) {
        return this.display.getVisible() == 1 && player.m_21205_().getItem() != CustomItems.wand && !this.display.availability.hasOptions();
    }

    @Override
    public boolean isInvisible() {
        return this.display.getVisible() != 0 && !this.display.availability.hasOptions();
    }

    public void setInvisible(ServerPlayer playerMP) {
        if (this.tracking.contains(playerMP.m_19879_())) {
            this.tracking.remove(playerMP.m_19879_());
            Packets.send(playerMP, new PacketNpcVisibleFalse(this.m_19879_()));
        }
    }

    public void setVisible(ServerPlayer playerMP) {
        if (!this.tracking.contains(playerMP.m_19879_())) {
            this.tracking.add(playerMP.m_19879_());
            Packets.send(playerMP, new PacketNpcVisibleTrue(this));
            playerMP.connection.send(new ClientboundSetEntityDataPacket(this.m_19879_(), this.m_20088_().packDirty()));
        }
        Packets.send(playerMP, new PacketNpcUpdate(this.m_19879_(), this.writeSpawnData()));
    }

    public void setCurrentAnimation(int animation) {
        this.currentAnimation = animation;
        this.f_19804_.set(Animation, animation);
    }

    public boolean canNpcSee(Entity entity) {
        return this.m_21574_().hasLineOfSight(entity);
    }

    public boolean isFollower() {
        return this.advanced.scenes.getOwner() != null ? true : this.role.isFollowing() || this.job.isFollowing();
    }

    public LivingEntity getOwner() {
        if (this.advanced.scenes.getOwner() != null) {
            return this.advanced.scenes.getOwner();
        } else if (this.role.getType() == 2 && this.role instanceof RoleFollower) {
            return ((RoleFollower) this.role).owner;
        } else if (this.role.getType() == 6 && this.role instanceof RoleCompanion) {
            return ((RoleCompanion) this.role).owner;
        } else {
            return this.job.getType() == 5 && this.job instanceof JobFollower ? ((JobFollower) this.job).following : null;
        }
    }

    public boolean hasOwner() {
        return this.advanced.scenes.getOwner() != null ? true : this.role.getType() == 2 && ((RoleFollower) this.role).hasOwner() || this.role.getType() == 6 && ((RoleCompanion) this.role).hasOwner() || this.job.getType() == 5 && ((JobFollower) this.job).hasOwner();
    }

    public int followRange() {
        if (this.advanced.scenes.getOwner() != null) {
            return 4;
        } else if (this.role.getType() == 2 && this.role.isFollowing()) {
            return 6;
        } else if (this.role.getType() == 6 && this.role.isFollowing()) {
            return 4;
        } else {
            return this.job.getType() == 5 && this.job.isFollowing() ? 4 : 15;
        }
    }

    @Override
    protected float getDamageAfterArmorAbsorb(DamageSource source, float damage) {
        if (this.role.getType() == 6) {
            damage = ((RoleCompanion) this.role).getDamageAfterArmorAbsorb(source, damage);
        }
        return damage;
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        if (!this.isClientSide()) {
            if (entity instanceof Player && this.getFaction().isFriendlyToPlayer((Player) entity)) {
                return true;
            }
            if (entity == this.getOwner()) {
                return true;
            }
            if (entity instanceof EntityNPCInterface && ((EntityNPCInterface) entity).faction.id == this.faction.id) {
                return true;
            }
        }
        return super.m_7307_(entity);
    }

    public void setDataWatcher(SynchedEntityData entityData) {
        List<SynchedEntityData.DataValue<?>> list = new ArrayList();
        for (SynchedEntityData.DataItem<?> entry : ((ISynchedEntityData) entityData).getAll()) {
            if (entry.getValue() instanceof SynchedEntityData.DataValue) {
                list.add((SynchedEntityData.DataValue) entry.getValue());
            }
        }
        this.f_19804_.assignValues(list);
    }

    @Override
    public void travel(Vec3 travelVector) {
        BlockPos pos = this.m_20183_();
        super.m_7023_(travelVector);
        if (this.role.getType() == 6 && !this.isClientSide()) {
            BlockPos delta = this.m_20183_().subtract(pos);
            ((RoleCompanion) this.role).addMovementStat((double) delta.m_123341_(), (double) delta.m_123342_(), (double) delta.m_123343_());
        }
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    public boolean isLeashed() {
        return false;
    }

    public boolean nearPosition(BlockPos pos) {
        BlockPos npcpos = this.m_20183_();
        float x = (float) (npcpos.m_123341_() - pos.m_123341_());
        float z = (float) (npcpos.m_123343_() - pos.m_123343_());
        float y = (float) (npcpos.m_123342_() - pos.m_123342_());
        float height = (float) (Mth.ceil(this.m_20206_() + 1.0F) * Mth.ceil(this.m_20206_() + 1.0F));
        return (double) (x * x + z * z) < 2.5 && (double) (y * y) < (double) height + 2.5;
    }

    public void tpTo(LivingEntity owner) {
        if (owner != null) {
            Direction facing = owner.m_6350_().getOpposite();
            BlockPos pos = new BlockPos((int) owner.m_20185_(), (int) owner.m_20191_().minY, (int) owner.m_20189_());
            pos = pos.offset(facing.getStepX(), 0, facing.getStepZ());
            pos = this.calculateTopPos(pos);
            for (int i = -1; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    BlockPos check;
                    if (facing.getStepX() == 0) {
                        check = pos.offset(i, 0, j * facing.getStepZ());
                    } else {
                        check = pos.offset(j * facing.getStepX(), 0, i);
                    }
                    check = this.calculateTopPos(check);
                    if (!this.m_9236_().getBlockState(check).m_60804_(this.m_9236_(), check) && !this.m_9236_().getBlockState(check.above()).m_60804_(this.m_9236_(), check.above())) {
                        this.m_7678_((double) ((float) check.m_123341_() + 0.5F), (double) check.m_123342_(), (double) ((float) check.m_123343_() + 0.5F), this.m_146908_(), this.m_146909_());
                        this.m_21573_().stop();
                        break;
                    }
                }
            }
        }
    }

    public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
        return false;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> para) {
        super.m_7350_(para);
        if (Animation.equals(para)) {
            this.m_6210_();
        }
    }
}