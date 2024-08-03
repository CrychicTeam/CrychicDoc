package com.mna.api.entities.construct.ai;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.IConstructDiagnostics;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fluids.FluidStack;

public abstract class ConstructAITask<T extends ConstructAITask<?>> extends Goal {

    protected static final int MOVE_COOLDOWN = 20;

    public static final int SWING_BUFFER_TIME = 5;

    public static final int MELEE_ATTACK_TIME = 8;

    public static final int MELEE_ATTACK_DIST = 2;

    protected IConstruct<?> construct;

    protected IMutexManager mutexManager;

    protected Player owner;

    protected int exitCode = -1;

    protected boolean hasStarted = false;

    protected boolean isOneOff = false;

    protected UUID oneOffFollowTarget = null;

    protected int moveCooldown;

    protected int fleeCooldown;

    protected int teleportCooldown = 0;

    private BlockPos moveBlockTarget = null;

    private Entity moveEntityTarget = null;

    protected int attackCooldown = 0;

    private List<ConstructAITaskParameter> _parameters;

    private String id = null;

    protected final ResourceLocation guiIcon;

    private boolean isStart = false;

    private final HashMap<Integer, String> connections;

    public ConstructAITask(IConstruct<?> construct, ResourceLocation guiIcon) {
        this.construct = construct;
        this._parameters = this.instantiateParameters();
        this.connections = new HashMap();
        this.guiIcon = guiIcon;
    }

    public IConstruct<?> getConstruct() {
        return this.construct;
    }

    @Nullable
    public IMutexManager getMutexManager() {
        return this.mutexManager;
    }

    public final String getId() {
        return this.id;
    }

    public final boolean isStart() {
        return this.isStart;
    }

    public abstract ResourceLocation getType();

    public final boolean isOneOff() {
        return this.isOneOff;
    }

    public final UUID getOneOffFollowTarget() {
        return this.oneOffFollowTarget;
    }

    public final AbstractGolem getConstructAsEntity() {
        return this.getConstruct().asEntity();
    }

    public int getInteractTime(ConstructCapability capability, int baseline) {
        if (this.getConstruct() == null) {
            return 30;
        } else {
            ConstructMaterial material = this.getConstruct().getConstructData().getLowestMaterialCooldownMultiplierForCapability(capability);
            return material == null ? baseline : (int) Math.max(material.getCooldownMultiplierFor(capability) * (float) baseline, 5.0F);
        }
    }

    public int getInteractTime(ConstructCapability capability) {
        return this.getInteractTime(capability, 50);
    }

    public ConstructCapability[] requiredCapabilities() {
        return new ConstructCapability[0];
    }

    public boolean allowSteeringMountedConstructsDuringTask() {
        return false;
    }

    public final String getConnectionForResultCode(int result) {
        return (String) this.connections.getOrDefault(result, null);
    }

    protected Player createFakePlayer() {
        return FakePlayerFactory.getMinecraft((ServerLevel) this.construct.asEntity().m_9236_());
    }

    public boolean hasStarted() {
        return this.hasStarted;
    }

    public boolean areCapabilitiesMet() {
        return this.construct.getConstructData().areCapabilitiesEnabled(this.requiredCapabilities());
    }

    protected Optional<ConstructAITaskParameter> getParameter(String id) {
        return this.getParameters().stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public List<String> getConnectedIDs() {
        return this.connections.values().stream().toList();
    }

    public int getRequiredIntelligence() {
        return 1;
    }

    public boolean hasMoveTarget() {
        return this.moveBlockTarget != null || this.moveEntityTarget != null;
    }

    public BlockPos getMoveBlockTarget() {
        return this.moveBlockTarget;
    }

    public Entity getMoveEntityTarget() {
        return this.moveEntityTarget;
    }

    public final void setIdAndIsStart(String id, boolean isStart) {
        if (this.id == null) {
            this.id = id;
            this.isStart = isStart;
        }
    }

    public void setConstruct(IConstruct<?> construct) {
        this.construct = construct;
    }

    public void setMutexManager(IMutexManager mutexManager) {
        this.mutexManager = mutexManager;
    }

    public void clearMoveTarget() {
        this.moveBlockTarget = null;
        this.moveEntityTarget = null;
    }

    public void setMoveTarget(BlockPos pos) {
        this.moveBlockTarget = pos;
        this.moveEntityTarget = null;
        if (pos != null) {
            this.construct.getDiagnostics().setMovePos(Vec3.atCenterOf(pos));
        } else {
            this.construct.getDiagnostics().setMovePos(null);
        }
    }

    public void setMoveTarget(Entity entity) {
        this.moveBlockTarget = null;
        this.moveEntityTarget = entity;
        if (entity != null) {
            this.construct.getDiagnostics().pushTaskUpdate(this.id, this.guiIcon, IConstructDiagnostics.Status.RUNNING, entity.getId());
        } else {
            this.construct.getDiagnostics().pushTaskUpdate(this.id, this.guiIcon, IConstructDiagnostics.Status.RUNNING, -1);
        }
    }

    public final void setOneOff(Player followWhenDone) {
        this.isOneOff = true;
        this.oneOffFollowTarget = followWhenDone != null && followWhenDone.getGameProfile() != null ? followWhenDone.getGameProfile().getId() : null;
    }

    public abstract void inflateParameters();

    public void onTaskSet() {
        this.exitCode = -1;
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    @Override
    public void tick() {
        if (this.moveCooldown > 0) {
            this.moveCooldown--;
        }
        if (this.attackCooldown > 0) {
            this.attackCooldown--;
        }
        if (this.teleportCooldown > 0) {
            this.teleportCooldown--;
        }
        if (this.fleeCooldown > 0) {
            this.fleeCooldown--;
        }
        if (this.owner != null && !this.owner.m_6084_()) {
            this.owner = null;
        }
    }

    @Override
    public boolean canUse() {
        if (this.construct == null) {
            return false;
        } else if (!this.areCapabilitiesMet()) {
            this.confuseConstructCapsMissing();
            this.forceFail();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.isFinished();
    }

    @Override
    public void start() {
        if (this.construct != null) {
            this.owner = this.construct.getOwner();
        }
        this.hasStarted = true;
        this.construct.getDiagnostics().pushTaskUpdate(this.id, this.guiIcon, IConstructDiagnostics.Status.RUNNING, -1);
    }

    public boolean isFinished() {
        return this.exitCode >= 0;
    }

    public boolean isSuccess() {
        return this.exitCode == 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.hasStarted = false;
        this.releaseMutexes();
        if (this.exitCode == 1) {
            this.construct.getDiagnostics().pushTaskUpdate(this.id, this.guiIcon, IConstructDiagnostics.Status.FAILURE, -1);
        } else {
            this.construct.getDiagnostics().pushTaskUpdate(this.id, this.guiIcon, IConstructDiagnostics.Status.SUCCESS, -1);
        }
    }

    protected boolean claimBlockMutex(BlockPos pos) {
        return this.getMutexManager() != null ? this.getMutexManager().claimMutex(pos, this.construct, this) : true;
    }

    protected void releaseBlockMutex(BlockPos pos) {
        if (this.getMutexManager() != null) {
            this.getMutexManager().releaseMutex(pos, this.construct, this);
        }
    }

    protected boolean claimEntityMutex(Entity entity) {
        return this.getMutexManager() != null ? this.getMutexManager().claimMutex(entity, this.construct, this) : true;
    }

    protected void releaseEntityMutex(Entity entity) {
        if (this.getMutexManager() != null) {
            this.getMutexManager().releaseMutex(entity, this.construct, this);
        }
    }

    public void releaseMutexes() {
        if (this.getMutexManager() != null) {
            this.getMutexManager().releaseAllMutexes(this.construct, this);
        }
    }

    protected void pushDiagnosticMessage(String message, boolean allowDuplicates) {
        this.construct.pushDiagnosticMessage(message, this.guiIcon, allowDuplicates);
    }

    protected void pushDiagnosticMessage(Component message, boolean allowDuplicates) {
        this.pushDiagnosticMessage(message.getContents().toString(), allowDuplicates);
    }

    public void pushDiagnosticMessage(Component message) {
        this.pushDiagnosticMessage(message, false);
    }

    public void pushDiagnosticMessage(String message) {
        this.pushDiagnosticMessage(message, false);
    }

    public final void confuseConstructCapsMissing() {
        String cap = (String) Arrays.asList(this.requiredCapabilities()).stream().map(c -> c.toString()).collect(Collectors.joining(", "));
        this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.capability_missing", cap));
        this.construct.setConfused(100);
    }

    public final void confuseConstructLowIntelligence() {
        this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.low_intelligence"));
        this.construct.setConfused(100);
    }

    public void insertOrDiscardItem(ItemStack stack) {
        AbstractGolem c = this.getConstructAsEntity();
        int limit = this.getConstruct().getCarrySize();
        InteractionHand[] hands = this.getConstruct().getCarryingHands();
        for (int i = 0; i < hands.length; i++) {
            ItemStack carried = c.m_21120_(hands[i]);
            if (ItemStack.matches(stack, carried)) {
                if (carried.getCount() + stack.getCount() <= limit) {
                    carried.setCount(carried.getCount() + stack.getCount());
                    return;
                }
                if (carried.getCount() < limit) {
                    int remaining = limit - carried.getCount();
                    carried.setCount(limit);
                    stack.setCount(remaining);
                }
            }
        }
        hands = this.getConstruct().getEmptyHands();
        for (int ix = 0; ix < hands.length; ix++) {
            if (stack.getCount() <= limit) {
                c.m_21008_(hands[ix], stack);
                return;
            }
            ItemStack copy = stack.copy();
            copy.setCount(limit);
            stack.setCount(stack.getCount() - limit);
            c.m_21008_(hands[ix], copy);
        }
        if (!stack.isEmpty() && this.construct.getConstructData().isCapabilityEnabled(ConstructCapability.ITEM_STORAGE)) {
            ManaAndArtificeMod.getInventoryHelper().mergeIntoInventory(this.construct, stack);
        }
    }

    public void insertOrDropItem(ItemStack stack) {
        AbstractGolem c = this.getConstructAsEntity();
        int limit = this.getConstruct().getCarrySize();
        InteractionHand[] hands = this.getConstruct().getCarryingHands();
        for (int i = 0; i < hands.length; i++) {
            ItemStack carried = c.m_21120_(hands[i]);
            if (ItemStack.matches(stack, carried)) {
                if (carried.getCount() + stack.getCount() <= limit) {
                    carried.setCount(carried.getCount() + stack.getCount());
                    return;
                }
                if (carried.getCount() < limit) {
                    int remaining = limit - carried.getCount();
                    carried.setCount(limit);
                    stack.setCount(remaining);
                }
            }
        }
        hands = this.getConstruct().getEmptyHands();
        for (int ix = 0; ix < hands.length; ix++) {
            if (stack.getCount() <= limit) {
                c.m_21008_(hands[ix], stack);
                return;
            }
            ItemStack copy = stack.copy();
            copy.setCount(limit);
            stack.setCount(stack.getCount() - limit);
            c.m_21008_(hands[ix], copy);
        }
        if (!stack.isEmpty() && (!this.construct.getConstructData().isCapabilityEnabled(ConstructCapability.ITEM_STORAGE) || !ManaAndArtificeMod.getInventoryHelper().mergeIntoInventory(this.construct, stack))) {
            ItemEntity drop = new ItemEntity(c.m_9236_(), c.m_20185_(), c.m_20186_(), c.m_20189_(), stack);
            drop.m_20334_(0.0, 0.25, 0.0);
            drop.setDefaultPickUpDelay();
            c.m_9236_().m_7967_(drop);
        }
    }

    public List<ItemStack> getHeldItems() {
        AbstractGolem c = this.getConstructAsEntity();
        ArrayList<ItemStack> stacks = new ArrayList();
        if (!c.m_21205_().isEmpty()) {
            stacks.add(c.m_21205_());
        }
        if (!c.m_21206_().isEmpty()) {
            stacks.add(c.m_21206_());
        }
        return stacks;
    }

    protected List<ItemStack> getLootRoll(ResourceLocation lootTable, ItemStack tool, LootContextParamSet paramSet) {
        AbstractGolem c = this.construct.asEntity();
        LootParams lootparams = new LootParams.Builder((ServerLevel) c.m_9236_()).withParameter(LootContextParams.THIS_ENTITY, this.construct.asEntity()).withParameter(LootContextParams.ORIGIN, this.construct.asEntity().m_20182_()).withParameter(LootContextParams.TOOL, tool).create(paramSet);
        LootTable loottable = c.m_9236_().getServer().getLootData().m_278676_(lootTable);
        return loottable.getRandomItems(lootparams);
    }

    protected void swingHandWithCapability(ConstructCapability cap) {
        this.construct.getHandWithCapability(ConstructCapability.SHEAR).ifPresent(h -> this.construct.asEntity().m_6674_(h));
    }

    public boolean doMove() {
        return this.doMove(2.5F);
    }

    public boolean doMove(float closeEnough) {
        if (this.moveEntityTarget != null && !this.moveEntityTarget.isAlive()) {
            this.moveEntityTarget = null;
        }
        if (this.moveBlockTarget == null && this.moveEntityTarget == null) {
            return false;
        } else {
            Vec3 invPos = this.moveEntityTarget != null ? this.moveEntityTarget.position().add(0.0, 0.25, 0.0) : new Vec3((double) this.moveBlockTarget.m_123341_() + 0.5, (double) this.moveBlockTarget.m_123342_(), (double) this.moveBlockTarget.m_123343_() + 0.5);
            AbstractGolem c = this.getConstructAsEntity();
            this.construct.getDiagnostics().setMovePos(invPos);
            double dist = c.m_20238_(invPos);
            boolean movecomplete = false;
            if (dist > (double) (closeEnough * closeEnough)) {
                if (this.moveCooldown == 0 || c.m_21573_().isDone()) {
                    if (this.moveEntityTarget != null) {
                        movecomplete = this.setPathToEntity(this.moveEntityTarget, closeEnough);
                    } else {
                        movecomplete = this.setPathToXYZ(invPos, closeEnough);
                    }
                    this.moveCooldown = 20;
                }
            } else {
                movecomplete = true;
            }
            if (movecomplete) {
                c.m_21573_().stop();
                this.construct.getDiagnostics().setMovePos(null);
                return true;
            } else {
                return false;
            }
        }
    }

    protected final String translate(String key, Object... args) {
        return Component.translatable(key, args).getString();
    }

    protected final String translate(BlockEntity te) {
        return te == null ? "Null Tile Entity" : this.translate(te.getBlockState());
    }

    protected final String translate(ItemStack stack) {
        return this.translate(stack.getDescriptionId());
    }

    protected final String translate(FluidStack stack) {
        return this.translate(stack.getTranslationKey());
    }

    protected final String translate(BlockState state) {
        return state == null ? "Null Block state" : this.translate(state.m_60734_().getDescriptionId());
    }

    protected final String translate(Entity e) {
        return e == null ? "Null Entity" : this.translate(e.getType().getDescriptionId());
    }

    protected boolean setPathToEntity(Entity entity, float closeEnough) {
        if (this.construct.getConstructData().isCapabilityEnabled(ConstructCapability.TELEPORT) && this.teleportCooldown <= 0) {
            return this.setPathToXYZ(entity.position(), closeEnough);
        } else {
            double dist = entity.position().distanceToSqr(this.construct.asEntity().m_20182_());
            if (dist < (double) (closeEnough * closeEnough)) {
                return true;
            } else {
                AbstractGolem c = this.getConstructAsEntity();
                c.m_7910_(Math.min((float) c.m_21133_(Attributes.MOVEMENT_SPEED), 0.55F));
                if (!c.m_21573_().moveTo(entity, (double) c.m_6113_())) {
                    if (c.m_21573_().isDone()) {
                        return true;
                    }
                    this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.move_entity_fail", this.translate(entity)), false);
                }
                return false;
            }
        }
    }

    protected boolean setPathToXYZ(Vec3 pos, float closeEnough) {
        AbstractGolem c = this.getConstructAsEntity();
        double dist = pos.distanceToSqr(c.m_20182_());
        if (dist < (double) (closeEnough * closeEnough)) {
            return true;
        } else {
            if (this.construct.getConstructData().isCapabilityEnabled(ConstructCapability.TELEPORT) && this.teleportCooldown <= 0) {
                Vec3 targetPos = pos.add(this.construct.asEntity().m_20182_().subtract(pos).normalize().scale((double) (closeEnough * 0.95F)));
                if (dist > 36.0 || this.construct.asEntity().f_19862_ || c.m_9236_().m_45547_(new ClipContext(c.m_146892_(), targetPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, c)).getType() != HitResult.Type.MISS) {
                    this.teleportCooldown = this.getInteractTime(ConstructCapability.TELEPORT);
                    BlockHitResult bhr = c.m_9236_().m_45547_(new ClipContext(targetPos.add(0.0, 2.0, 0.0), targetPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, c));
                    Vec3 teleportPos = targetPos;
                    boolean canTeleport = true;
                    if (bhr != null && bhr.getType() != HitResult.Type.MISS) {
                        BlockPos bp = BlockPos.containing(bhr.m_82450_());
                        if (!bhr.isInside() && this.isTeleportDestinationValid(c, bp)) {
                            teleportPos = bhr.m_82450_();
                        } else {
                            canTeleport = false;
                            for (int xShift = 0; xShift <= 2 && !canTeleport; xShift++) {
                                for (int yShift = -1; yShift <= 1; yShift++) {
                                    for (int zShift = 0; zShift <= 2; zShift++) {
                                        if (xShift != 0 || zShift != 0) {
                                            bhr = c.m_9236_().m_45547_(new ClipContext(targetPos.add((double) xShift, (double) (2 + yShift), (double) zShift), targetPos.add((double) xShift, (double) yShift, (double) zShift), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, c));
                                            if (bhr != null) {
                                                bp = BlockPos.containing(bhr.m_82450_());
                                                if (this.isTeleportDestinationValid(c, bp)) {
                                                    teleportPos = bhr.m_82450_();
                                                    canTeleport = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (canTeleport) {
                        c.m_6021_(teleportPos.x, teleportPos.y, teleportPos.z);
                        c.m_9236_().playSound((Player) null, c.f_19854_, c.f_19855_, c.f_19856_, SoundEvents.ENDERMAN_TELEPORT, c.m_5720_(), 0.01F, 1.0F);
                        c.m_5496_(SoundEvents.ENDERMAN_TELEPORT, 0.1F, 1.0F);
                        c.m_7618_(EntityAnchorArgument.Anchor.FEET, pos);
                        this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.move_block_success", Math.floor(pos.x * 100.0) / 100.0, Math.floor(pos.y * 100.0) / 100.0, Math.floor(pos.z * 100.0) / 100.0), false);
                        return true;
                    }
                }
            }
            c.m_7910_(Math.min((float) c.m_21133_(Attributes.MOVEMENT_SPEED), 0.55F));
            if (!c.m_21573_().moveTo(pos.x, pos.y, pos.z, (double) c.m_6113_())) {
                if (c.m_21573_().isDone()) {
                    return true;
                }
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.move_block_fail", Math.floor(pos.x * 100.0) / 100.0, Math.floor(pos.y * 100.0) / 100.0, Math.floor(pos.z * 100.0) / 100.0), false);
                BlockPos blockPos = BlockPos.containing(pos);
                double[] dists = new double[] { blockPos.m_203193_(pos), blockPos.offset(1, 0, 0).m_203193_(pos), blockPos.offset(0, 0, 1).m_203193_(pos), blockPos.offset(1, 0, 1).m_203193_(pos) };
                for (double bpDist : dists) {
                    if (bpDist <= (double) closeEnough) {
                        return true;
                    }
                }
                c.m_7618_(EntityAnchorArgument.Anchor.FEET, pos);
                c.m_6478_(MoverType.SELF, pos.subtract(c.m_20182_()).normalize().scale((double) ((float) c.m_21133_(Attributes.MOVEMENT_SPEED))));
            }
            return false;
        }
    }

    protected final boolean isTeleportDestinationValid(AbstractGolem c, BlockPos pos) {
        return c.m_9236_().m_45556_(new AABB(pos, pos.above())).allMatch(state -> state.m_60795_());
    }

    protected boolean doAttack() {
        this.moveEntityTarget = this.construct.asEntity().m_5448_();
        int ranged_result = this.doRangedAttack();
        return ranged_result != -1 ? ranged_result != 0 : this.doMeleeAttack();
    }

    private boolean doMeleeAttack() {
        if (!this.construct.getConstructData().areCapabilitiesEnabled(ConstructCapability.MELEE_ATTACK)) {
            return true;
        } else if (this.moveEntityTarget instanceof LivingEntity && this.construct.validateFriendlyTarget((LivingEntity) this.moveEntityTarget)) {
            this.construct.asEntity().m_6710_(null);
            return true;
        } else if (this.doMove(2.0F) && this.attackCooldown <= 0 && this.construct.getAttackDelay() <= 0) {
            this.construct.asEntity().m_21391_(this.construct.asEntity().m_5448_(), 10.0F, 10.0F);
            this.construct.getHandWithCapability(ConstructCapability.MELEE_ATTACK).ifPresent(h -> {
                if (this.construct.canDualSweep() && Math.random() < 0.25) {
                    this.construct.setDualSweeping();
                    DelayedEventQueue.pushEvent(this.construct.asEntity().m_9236_(), new TimedDelayedEvent<>("attack", 8, this.moveEntityTarget, this::meleeSweepCallback));
                } else {
                    this.construct.asEntity().m_6674_(h);
                    DelayedEventQueue.pushEvent(this.construct.asEntity().m_9236_(), new TimedDelayedEvent<>("attack", 8, this.moveEntityTarget, this::meleeAttackCallback));
                }
            });
            return true;
        } else {
            return false;
        }
    }

    private void meleeAttackCallback(String identifier, Entity target) {
        if (target != null) {
            this.attackCooldown = 10;
            this.construct.asEntity().m_7327_(target);
        }
    }

    private void meleeSweepCallback(String identifier, Entity target) {
        if (target != null) {
            this.attackCooldown = 10;
            AbstractGolem c = this.construct.asEntity();
            for (LivingEntity livingentity : c.m_9236_().m_45976_(LivingEntity.class, target.getBoundingBox().inflate(2.0, 0.25, 2.0))) {
                if (livingentity != c && !c.m_7307_(livingentity) && c.m_20280_(livingentity) < 9.0) {
                    livingentity.knockback(0.4F, (double) Mth.sin(c.m_146908_() * (float) (Math.PI / 180.0)), (double) (-Mth.cos(c.m_146908_() * (float) (Math.PI / 180.0))));
                    c.m_7327_(livingentity);
                }
            }
            c.m_9236_().playSound((Player) null, c.m_20185_(), c.m_20186_(), c.m_20189_(), SoundEvents.PLAYER_ATTACK_SWEEP, c.m_5720_(), 1.0F, 1.0F);
            if (c.m_9236_() instanceof ServerLevel) {
                double d0 = (double) (-Mth.sin(c.m_146908_() * (float) (Math.PI / 180.0)));
                double d1 = (double) Mth.cos(c.m_146908_() * (float) (Math.PI / 180.0));
                ((ServerLevel) c.m_9236_()).sendParticles(ParticleTypes.SWEEP_ATTACK, c.m_20185_() + d0, c.m_20227_(0.5), c.m_20189_() + d1, 0, d0, 0.0, d1, 0.0);
            }
        }
    }

    private int doRangedAttack() {
        if (!this.construct.isRangedAttacking() && !this.construct.canManaCannonAttack() && !this.construct.canFluidSpray() && !this.construct.canSpellCast()) {
            return -1;
        } else {
            AbstractGolem constructEntity = this.construct.asEntity();
            LivingEntity target = constructEntity.m_5448_();
            double dist = (double) constructEntity.m_20270_(target);
            if (target != this.construct.getOwner() && dist < 2.0 && this.construct.getConstructData().isCapabilityEnabled(ConstructCapability.MELEE_ATTACK)) {
                return -1;
            } else {
                int closeEnoughDist = 16;
                if (this.construct.canFluidSpray()) {
                    closeEnoughDist = 6;
                }
                if ((this.fleeCooldown == 0 || constructEntity.m_21573_().isDone()) && this.doMove((float) closeEnoughDist)) {
                    this.construct.asEntity().m_21563_().setLookAt(this.construct.asEntity().m_5448_());
                    if (this.construct.getAttackDelay() <= 0 && this.construct.asEntity().m_21563_().isLookingAtTarget() && this.construct.performRangedAttack(this.construct.asEntity().m_5448_())) {
                        return 1;
                    }
                }
                return 0;
            }
        }
    }

    public abstract boolean isFullyConfigured();

    protected void faceBlockPos(BlockPos pos) {
        AbstractGolem c = this.getConstructAsEntity();
        double d0 = (double) pos.m_123341_() + 0.5 - c.m_20185_();
        double d2 = (double) pos.m_123343_() + 0.5 - c.m_20189_();
        double d1 = (double) pos.m_123342_() + 0.5;
        double d3 = (double) Mth.sqrt((float) (d0 * d0 + d2 * d2));
        float f = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
        float f1 = (float) (-(Mth.atan2(d1, d3) * 180.0F / (float) Math.PI));
        c.f_19860_ = c.m_146909_();
        c.m_146926_(this.updateRotation(c.m_146909_(), f1, 30.0F));
        c.f_19859_ = c.m_146908_();
        c.m_146922_(this.updateRotation(c.m_146908_(), f, 30.0F));
        c.f_20884_ = c.m_6080_();
        c.m_5616_(this.updateRotation(c.m_146908_(), f, 30.0F));
    }

    private float updateRotation(float start, float end, float step) {
        float f = Mth.wrapDegrees(end - start);
        if (f > step) {
            f = step;
        }
        if (f < -step) {
            f = -step;
        }
        return start + f;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public abstract T copyFrom(ConstructAITask<?> var1);

    public final void copyConnections(ConstructAITask<?> other) {
        this.connections.clear();
        other.connections.forEach((k, v) -> this.connections.put(k, v));
    }

    public abstract void readNBT(CompoundTag var1);

    public final CompoundTag writeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("type", this.getType().toString());
        this.writeInternal(nbt);
        return nbt;
    }

    protected abstract CompoundTag writeInternal(CompoundTag var1);

    public abstract T duplicate();

    public final void loadParameterData(ListTag list) {
        if (!list.isEmpty() && list.getElementType() != 10) {
            ManaAndArtificeMod.LOGGER.warn("M&A >> construct parameters were not a list of NBT tags; skipping.  This task will be unconfigured.");
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (i >= this._parameters.size()) {
                    return;
                }
                ((ConstructAITaskParameter) this._parameters.get(i)).loadData(list.getCompound(i));
            }
        }
    }

    public final ListTag saveParameterData() {
        ListTag list = new ListTag();
        this._parameters.forEach(p -> list.add(p.saveData()));
        return list;
    }

    public final void loadConnections(ListTag list) {
        list.forEach(t -> {
            if (t instanceof CompoundTag connection && connection.contains("index") && connection.contains("target")) {
                String id = connection.getString("target");
                int index = connection.getInt("index");
                this.connections.put(index, id);
            }
        });
    }

    protected List<ConstructAITaskParameter> instantiateParameters() {
        return new ArrayList();
    }

    public List<ConstructAITaskParameter> getParameters() {
        return this._parameters;
    }

    public static final ConstructTask getTypeFromNBT(CompoundTag nbt) {
        if (nbt.contains("type", 8)) {
            ResourceLocation id = new ResourceLocation(nbt.getString("type"));
            ConstructTask task = ManaAndArtificeMod.getConstructTaskRegistry().getValue(id);
            if (task != null) {
                return task;
            }
        }
        return ManaAndArtificeMod.getConstructTaskRegistry().getValue(new ResourceLocation("mna", "stay"));
    }

    public String getNextTask() {
        return (String) this.connections.getOrDefault(this.exitCode, null);
    }

    public void forceFail() {
        this.exitCode = 1;
    }

    public void setSuccessCode() {
        this.exitCode = 0;
    }
}