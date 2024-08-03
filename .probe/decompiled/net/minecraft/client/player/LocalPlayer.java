package net.minecraft.client.player;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.client.gui.screens.inventory.CommandBlockEditScreen;
import net.minecraft.client.gui.screens.inventory.HangingSignEditScreen;
import net.minecraft.client.gui.screens.inventory.JigsawBlockEditScreen;
import net.minecraft.client.gui.screens.inventory.MinecartCommandBlockEditScreen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.client.gui.screens.inventory.StructureBlockEditScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.resources.sounds.AmbientSoundHandler;
import net.minecraft.client.resources.sounds.BiomeAmbientSoundsHandler;
import net.minecraft.client.resources.sounds.BubbleColumnAmbientSoundHandler;
import net.minecraft.client.resources.sounds.ElytraOnPlayerSoundInstance;
import net.minecraft.client.resources.sounds.RidingMinecartSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.UnderwaterAmbientSoundHandler;
import net.minecraft.client.resources.sounds.UnderwaterAmbientSoundInstances;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.network.protocol.game.ServerboundRecipeBookSeenRecipePacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.StatsCounter;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.slf4j.Logger;

public class LocalPlayer extends AbstractClientPlayer {

    public static final Logger LOGGER = LogUtils.getLogger();

    private static final int POSITION_REMINDER_INTERVAL = 20;

    private static final int WATER_VISION_MAX_TIME = 600;

    private static final int WATER_VISION_QUICK_TIME = 100;

    private static final float WATER_VISION_QUICK_PERCENT = 0.6F;

    private static final double SUFFOCATING_COLLISION_CHECK_SCALE = 0.35;

    private static final double MINOR_COLLISION_ANGLE_THRESHOLD_RADIAN = 0.13962634F;

    private static final float DEFAULT_SNEAKING_MOVEMENT_FACTOR = 0.3F;

    public final ClientPacketListener connection;

    private final StatsCounter stats;

    private final ClientRecipeBook recipeBook;

    private final List<AmbientSoundHandler> ambientSoundHandlers = Lists.newArrayList();

    private int permissionLevel = 0;

    private double xLast;

    private double yLast1;

    private double zLast;

    private float yRotLast;

    private float xRotLast;

    private boolean lastOnGround;

    private boolean crouching;

    private boolean wasShiftKeyDown;

    private boolean wasSprinting;

    private int positionReminder;

    private boolean flashOnSetHealth;

    @Nullable
    private String serverBrand;

    public Input input;

    protected final Minecraft minecraft;

    protected int sprintTriggerTime;

    public float yBob;

    public float xBob;

    public float yBobO;

    public float xBobO;

    private int jumpRidingTicks;

    private float jumpRidingScale;

    public float spinningEffectIntensity;

    public float oSpinningEffectIntensity;

    private boolean startedUsingItem;

    @Nullable
    private InteractionHand usingItemHand;

    private boolean handsBusy;

    private boolean autoJumpEnabled = true;

    private int autoJumpTime;

    private boolean wasFallFlying;

    private int waterVisionTime;

    private boolean showDeathScreen = true;

    public LocalPlayer(Minecraft minecraft0, ClientLevel clientLevel1, ClientPacketListener clientPacketListener2, StatsCounter statsCounter3, ClientRecipeBook clientRecipeBook4, boolean boolean5, boolean boolean6) {
        super(clientLevel1, clientPacketListener2.getLocalGameProfile());
        this.minecraft = minecraft0;
        this.connection = clientPacketListener2;
        this.stats = statsCounter3;
        this.recipeBook = clientRecipeBook4;
        this.wasShiftKeyDown = boolean5;
        this.wasSprinting = boolean6;
        this.ambientSoundHandlers.add(new UnderwaterAmbientSoundHandler(this, minecraft0.getSoundManager()));
        this.ambientSoundHandlers.add(new BubbleColumnAmbientSoundHandler(this));
        this.ambientSoundHandlers.add(new BiomeAmbientSoundsHandler(this, minecraft0.getSoundManager(), clientLevel1.m_7062_()));
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        return false;
    }

    @Override
    public void heal(float float0) {
    }

    @Override
    public boolean startRiding(Entity entity0, boolean boolean1) {
        if (!super.m_7998_(entity0, boolean1)) {
            return false;
        } else {
            if (entity0 instanceof AbstractMinecart) {
                this.minecraft.getSoundManager().play(new RidingMinecartSoundInstance(this, (AbstractMinecart) entity0, true));
                this.minecraft.getSoundManager().play(new RidingMinecartSoundInstance(this, (AbstractMinecart) entity0, false));
            }
            return true;
        }
    }

    @Override
    public void removeVehicle() {
        super.m_6038_();
        this.handsBusy = false;
    }

    @Override
    public float getViewXRot(float float0) {
        return this.m_146909_();
    }

    @Override
    public float getViewYRot(float float0) {
        return this.m_20159_() ? super.m_5675_(float0) : this.m_146908_();
    }

    @Override
    public void tick() {
        if (this.m_9236_().m_151577_(this.m_146903_(), this.m_146907_())) {
            super.tick();
            if (this.m_20159_()) {
                this.connection.send(new ServerboundMovePlayerPacket.Rot(this.m_146908_(), this.m_146909_(), this.m_20096_()));
                this.connection.send(new ServerboundPlayerInputPacket(this.f_20900_, this.f_20902_, this.input.jumping, this.input.shiftKeyDown));
                Entity $$0 = this.m_20201_();
                if ($$0 != this && $$0.isControlledByLocalInstance()) {
                    this.connection.send(new ServerboundMoveVehiclePacket($$0));
                    this.sendIsSprintingIfNeeded();
                }
            } else {
                this.sendPosition();
            }
            for (AmbientSoundHandler $$1 : this.ambientSoundHandlers) {
                $$1.tick();
            }
        }
    }

    public float getCurrentMood() {
        for (AmbientSoundHandler $$0 : this.ambientSoundHandlers) {
            if ($$0 instanceof BiomeAmbientSoundsHandler) {
                return ((BiomeAmbientSoundsHandler) $$0).getMoodiness();
            }
        }
        return 0.0F;
    }

    private void sendPosition() {
        this.sendIsSprintingIfNeeded();
        boolean $$0 = this.isShiftKeyDown();
        if ($$0 != this.wasShiftKeyDown) {
            ServerboundPlayerCommandPacket.Action $$1 = $$0 ? ServerboundPlayerCommandPacket.Action.PRESS_SHIFT_KEY : ServerboundPlayerCommandPacket.Action.RELEASE_SHIFT_KEY;
            this.connection.send(new ServerboundPlayerCommandPacket(this, $$1));
            this.wasShiftKeyDown = $$0;
        }
        if (this.isControlledCamera()) {
            double $$2 = this.m_20185_() - this.xLast;
            double $$3 = this.m_20186_() - this.yLast1;
            double $$4 = this.m_20189_() - this.zLast;
            double $$5 = (double) (this.m_146908_() - this.yRotLast);
            double $$6 = (double) (this.m_146909_() - this.xRotLast);
            this.positionReminder++;
            boolean $$7 = Mth.lengthSquared($$2, $$3, $$4) > Mth.square(2.0E-4) || this.positionReminder >= 20;
            boolean $$8 = $$5 != 0.0 || $$6 != 0.0;
            if (this.m_20159_()) {
                Vec3 $$9 = this.m_20184_();
                this.connection.send(new ServerboundMovePlayerPacket.PosRot($$9.x, -999.0, $$9.z, this.m_146908_(), this.m_146909_(), this.m_20096_()));
                $$7 = false;
            } else if ($$7 && $$8) {
                this.connection.send(new ServerboundMovePlayerPacket.PosRot(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_146908_(), this.m_146909_(), this.m_20096_()));
            } else if ($$7) {
                this.connection.send(new ServerboundMovePlayerPacket.Pos(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_20096_()));
            } else if ($$8) {
                this.connection.send(new ServerboundMovePlayerPacket.Rot(this.m_146908_(), this.m_146909_(), this.m_20096_()));
            } else if (this.lastOnGround != this.m_20096_()) {
                this.connection.send(new ServerboundMovePlayerPacket.StatusOnly(this.m_20096_()));
            }
            if ($$7) {
                this.xLast = this.m_20185_();
                this.yLast1 = this.m_20186_();
                this.zLast = this.m_20189_();
                this.positionReminder = 0;
            }
            if ($$8) {
                this.yRotLast = this.m_146908_();
                this.xRotLast = this.m_146909_();
            }
            this.lastOnGround = this.m_20096_();
            this.autoJumpEnabled = this.minecraft.options.autoJump().get();
        }
    }

    private void sendIsSprintingIfNeeded() {
        boolean $$0 = this.m_20142_();
        if ($$0 != this.wasSprinting) {
            ServerboundPlayerCommandPacket.Action $$1 = $$0 ? ServerboundPlayerCommandPacket.Action.START_SPRINTING : ServerboundPlayerCommandPacket.Action.STOP_SPRINTING;
            this.connection.send(new ServerboundPlayerCommandPacket(this, $$1));
            this.wasSprinting = $$0;
        }
    }

    public boolean drop(boolean boolean0) {
        ServerboundPlayerActionPacket.Action $$1 = boolean0 ? ServerboundPlayerActionPacket.Action.DROP_ALL_ITEMS : ServerboundPlayerActionPacket.Action.DROP_ITEM;
        ItemStack $$2 = this.m_150109_().removeFromSelected(boolean0);
        this.connection.send(new ServerboundPlayerActionPacket($$1, BlockPos.ZERO, Direction.DOWN));
        return !$$2.isEmpty();
    }

    @Override
    public void swing(InteractionHand interactionHand0) {
        super.m_6674_(interactionHand0);
        this.connection.send(new ServerboundSwingPacket(interactionHand0));
    }

    @Override
    public void respawn() {
        this.connection.send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN));
        KeyMapping.resetToggleKeys();
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource0, float float1) {
        if (!this.m_6673_(damageSource0)) {
            this.m_21153_(this.m_21223_() - float1);
        }
    }

    @Override
    public void closeContainer() {
        this.connection.send(new ServerboundContainerClosePacket(this.f_36096_.containerId));
        this.clientSideCloseContainer();
    }

    public void clientSideCloseContainer() {
        super.m_6915_();
        this.minecraft.setScreen(null);
    }

    public void hurtTo(float float0) {
        if (this.flashOnSetHealth) {
            float $$1 = this.m_21223_() - float0;
            if ($$1 <= 0.0F) {
                this.m_21153_(float0);
                if ($$1 < 0.0F) {
                    this.f_19802_ = 10;
                }
            } else {
                this.f_20898_ = $$1;
                this.f_19802_ = 20;
                this.m_21153_(float0);
                this.f_20917_ = 10;
                this.f_20916_ = this.f_20917_;
            }
        } else {
            this.m_21153_(float0);
            this.flashOnSetHealth = true;
        }
    }

    @Override
    public void onUpdateAbilities() {
        this.connection.send(new ServerboundPlayerAbilitiesPacket(this.m_150110_()));
    }

    @Override
    public boolean isLocalPlayer() {
        return true;
    }

    @Override
    public boolean isSuppressingSlidingDownLadder() {
        return !this.m_150110_().flying && super.m_5791_();
    }

    @Override
    public boolean canSpawnSprintParticle() {
        return !this.m_150110_().flying && super.m_5843_();
    }

    @Override
    public boolean canSpawnSoulSpeedParticle() {
        return !this.m_150110_().flying && super.m_6039_();
    }

    protected void sendRidingJump() {
        this.connection.send(new ServerboundPlayerCommandPacket(this, ServerboundPlayerCommandPacket.Action.START_RIDING_JUMP, Mth.floor(this.getJumpRidingScale() * 100.0F)));
    }

    public void sendOpenInventory() {
        this.connection.send(new ServerboundPlayerCommandPacket(this, ServerboundPlayerCommandPacket.Action.OPEN_INVENTORY));
    }

    public void setServerBrand(@Nullable String string0) {
        this.serverBrand = string0;
    }

    @Nullable
    public String getServerBrand() {
        return this.serverBrand;
    }

    public StatsCounter getStats() {
        return this.stats;
    }

    public ClientRecipeBook getRecipeBook() {
        return this.recipeBook;
    }

    public void removeRecipeHighlight(Recipe<?> recipe0) {
        if (this.recipeBook.m_12717_(recipe0)) {
            this.recipeBook.m_12721_(recipe0);
            this.connection.send(new ServerboundRecipeBookSeenRecipePacket(recipe0));
        }
    }

    @Override
    protected int getPermissionLevel() {
        return this.permissionLevel;
    }

    public void setPermissionLevel(int int0) {
        this.permissionLevel = int0;
    }

    @Override
    public void displayClientMessage(Component component0, boolean boolean1) {
        this.minecraft.getChatListener().handleSystemMessage(component0, boolean1);
    }

    private void moveTowardsClosestSpace(double double0, double double1) {
        BlockPos $$2 = BlockPos.containing(double0, this.m_20186_(), double1);
        if (this.suffocatesAt($$2)) {
            double $$3 = double0 - (double) $$2.m_123341_();
            double $$4 = double1 - (double) $$2.m_123343_();
            Direction $$5 = null;
            double $$6 = Double.MAX_VALUE;
            Direction[] $$7 = new Direction[] { Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH };
            for (Direction $$8 : $$7) {
                double $$9 = $$8.getAxis().choose($$3, 0.0, $$4);
                double $$10 = $$8.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1.0 - $$9 : $$9;
                if ($$10 < $$6 && !this.suffocatesAt($$2.relative($$8))) {
                    $$6 = $$10;
                    $$5 = $$8;
                }
            }
            if ($$5 != null) {
                Vec3 $$11 = this.m_20184_();
                if ($$5.getAxis() == Direction.Axis.X) {
                    this.m_20334_(0.1 * (double) $$5.getStepX(), $$11.y, $$11.z);
                } else {
                    this.m_20334_($$11.x, $$11.y, 0.1 * (double) $$5.getStepZ());
                }
            }
        }
    }

    private boolean suffocatesAt(BlockPos blockPos0) {
        AABB $$1 = this.m_20191_();
        AABB $$2 = new AABB((double) blockPos0.m_123341_(), $$1.minY, (double) blockPos0.m_123343_(), (double) blockPos0.m_123341_() + 1.0, $$1.maxY, (double) blockPos0.m_123343_() + 1.0).deflate(1.0E-7);
        return this.m_9236_().m_186437_(this, $$2);
    }

    public void setExperienceValues(float float0, int int1, int int2) {
        this.f_36080_ = float0;
        this.f_36079_ = int1;
        this.f_36078_ = int2;
    }

    @Override
    public void sendSystemMessage(Component component0) {
        this.minecraft.gui.getChat().addMessage(component0);
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 >= 24 && byte0 <= 28) {
            this.setPermissionLevel(byte0 - 24);
        } else {
            super.m_7822_(byte0);
        }
    }

    public void setShowDeathScreen(boolean boolean0) {
        this.showDeathScreen = boolean0;
    }

    public boolean shouldShowDeathScreen() {
        return this.showDeathScreen;
    }

    @Override
    public void playSound(SoundEvent soundEvent0, float float1, float float2) {
        this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), soundEvent0, this.m_5720_(), float1, float2, false);
    }

    @Override
    public void playNotifySound(SoundEvent soundEvent0, SoundSource soundSource1, float float2, float float3) {
        this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), soundEvent0, soundSource1, float2, float3, false);
    }

    @Override
    public boolean isEffectiveAi() {
        return true;
    }

    @Override
    public void startUsingItem(InteractionHand interactionHand0) {
        ItemStack $$1 = this.m_21120_(interactionHand0);
        if (!$$1.isEmpty() && !this.isUsingItem()) {
            super.m_6672_(interactionHand0);
            this.startedUsingItem = true;
            this.usingItemHand = interactionHand0;
        }
    }

    @Override
    public boolean isUsingItem() {
        return this.startedUsingItem;
    }

    @Override
    public void stopUsingItem() {
        super.m_5810_();
        this.startedUsingItem = false;
    }

    @Override
    public InteractionHand getUsedItemHand() {
        return (InteractionHand) Objects.requireNonNullElse(this.usingItemHand, InteractionHand.MAIN_HAND);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor0) {
        super.m_7350_(entityDataAccessor0);
        if (f_20909_.equals(entityDataAccessor0)) {
            boolean $$1 = (this.f_19804_.<Byte>get(f_20909_) & 1) > 0;
            InteractionHand $$2 = (this.f_19804_.<Byte>get(f_20909_) & 2) > 0 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
            if ($$1 && !this.startedUsingItem) {
                this.startUsingItem($$2);
            } else if (!$$1 && this.startedUsingItem) {
                this.stopUsingItem();
            }
        }
        if (f_19805_.equals(entityDataAccessor0) && this.m_21255_() && !this.wasFallFlying) {
            this.minecraft.getSoundManager().play(new ElytraOnPlayerSoundInstance(this));
        }
    }

    @Nullable
    public PlayerRideableJumping jumpableVehicle() {
        if (this.m_275832_() instanceof PlayerRideableJumping $$0 && $$0.canJump()) {
            return $$0;
        }
        return null;
    }

    public float getJumpRidingScale() {
        return this.jumpRidingScale;
    }

    @Override
    public boolean isTextFilteringEnabled() {
        return this.minecraft.isTextFilteringEnabled();
    }

    @Override
    public void openTextEdit(SignBlockEntity signBlockEntity0, boolean boolean1) {
        if (signBlockEntity0 instanceof HangingSignBlockEntity $$2) {
            this.minecraft.setScreen(new HangingSignEditScreen($$2, boolean1, this.minecraft.isTextFilteringEnabled()));
        } else {
            this.minecraft.setScreen(new SignEditScreen(signBlockEntity0, boolean1, this.minecraft.isTextFilteringEnabled()));
        }
    }

    @Override
    public void openMinecartCommandBlock(BaseCommandBlock baseCommandBlock0) {
        this.minecraft.setScreen(new MinecartCommandBlockEditScreen(baseCommandBlock0));
    }

    @Override
    public void openCommandBlock(CommandBlockEntity commandBlockEntity0) {
        this.minecraft.setScreen(new CommandBlockEditScreen(commandBlockEntity0));
    }

    @Override
    public void openStructureBlock(StructureBlockEntity structureBlockEntity0) {
        this.minecraft.setScreen(new StructureBlockEditScreen(structureBlockEntity0));
    }

    @Override
    public void openJigsawBlock(JigsawBlockEntity jigsawBlockEntity0) {
        this.minecraft.setScreen(new JigsawBlockEditScreen(jigsawBlockEntity0));
    }

    @Override
    public void openItemGui(ItemStack itemStack0, InteractionHand interactionHand1) {
        if (itemStack0.is(Items.WRITABLE_BOOK)) {
            this.minecraft.setScreen(new BookEditScreen(this, itemStack0, interactionHand1));
        }
    }

    @Override
    public void crit(Entity entity0) {
        this.minecraft.particleEngine.createTrackingEmitter(entity0, ParticleTypes.CRIT);
    }

    @Override
    public void magicCrit(Entity entity0) {
        this.minecraft.particleEngine.createTrackingEmitter(entity0, ParticleTypes.ENCHANTED_HIT);
    }

    @Override
    public boolean isShiftKeyDown() {
        return this.input != null && this.input.shiftKeyDown;
    }

    @Override
    public boolean isCrouching() {
        return this.crouching;
    }

    public boolean isMovingSlowly() {
        return this.isCrouching() || this.m_20143_();
    }

    @Override
    public void serverAiStep() {
        super.m_6140_();
        if (this.isControlledCamera()) {
            this.f_20900_ = this.input.leftImpulse;
            this.f_20902_ = this.input.forwardImpulse;
            this.f_20899_ = this.input.jumping;
            this.yBobO = this.yBob;
            this.xBobO = this.xBob;
            this.xBob = this.xBob + (this.m_146909_() - this.xBob) * 0.5F;
            this.yBob = this.yBob + (this.m_146908_() - this.yBob) * 0.5F;
        }
    }

    protected boolean isControlledCamera() {
        return this.minecraft.getCameraEntity() == this;
    }

    public void resetPos() {
        this.m_20124_(Pose.STANDING);
        if (this.m_9236_() != null) {
            for (double $$0 = this.m_20186_(); $$0 > (double) this.m_9236_().m_141937_() && $$0 < (double) this.m_9236_().m_151558_(); $$0++) {
                this.m_6034_(this.m_20185_(), $$0, this.m_20189_());
                if (this.m_9236_().m_45786_(this)) {
                    break;
                }
            }
            this.m_20256_(Vec3.ZERO);
            this.m_146926_(0.0F);
        }
        this.m_21153_(this.m_21233_());
        this.f_20919_ = 0;
    }

    @Override
    public void aiStep() {
        if (this.sprintTriggerTime > 0) {
            this.sprintTriggerTime--;
        }
        if (!(this.minecraft.screen instanceof ReceivingLevelScreen)) {
            this.handleNetherPortalClient();
        }
        boolean $$0 = this.input.jumping;
        boolean $$1 = this.input.shiftKeyDown;
        boolean $$2 = this.hasEnoughImpulseToStartSprinting();
        this.crouching = !this.m_150110_().flying && !this.m_6069_() && this.m_20175_(Pose.CROUCHING) && (this.isShiftKeyDown() || !this.m_5803_() && !this.m_20175_(Pose.STANDING));
        float $$3 = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(this), 0.0F, 1.0F);
        this.input.tick(this.isMovingSlowly(), $$3);
        this.minecraft.getTutorial().onInput(this.input);
        if (this.isUsingItem() && !this.m_20159_()) {
            this.input.leftImpulse *= 0.2F;
            this.input.forwardImpulse *= 0.2F;
            this.sprintTriggerTime = 0;
        }
        boolean $$4 = false;
        if (this.autoJumpTime > 0) {
            this.autoJumpTime--;
            $$4 = true;
            this.input.jumping = true;
        }
        if (!this.f_19794_) {
            this.moveTowardsClosestSpace(this.m_20185_() - (double) this.m_20205_() * 0.35, this.m_20189_() + (double) this.m_20205_() * 0.35);
            this.moveTowardsClosestSpace(this.m_20185_() - (double) this.m_20205_() * 0.35, this.m_20189_() - (double) this.m_20205_() * 0.35);
            this.moveTowardsClosestSpace(this.m_20185_() + (double) this.m_20205_() * 0.35, this.m_20189_() - (double) this.m_20205_() * 0.35);
            this.moveTowardsClosestSpace(this.m_20185_() + (double) this.m_20205_() * 0.35, this.m_20189_() + (double) this.m_20205_() * 0.35);
        }
        if ($$1) {
            this.sprintTriggerTime = 0;
        }
        boolean $$5 = this.canStartSprinting();
        boolean $$6 = this.m_20159_() ? this.m_20202_().onGround() : this.m_20096_();
        boolean $$7 = !$$1 && !$$2;
        if (($$6 || this.isUnderWater()) && $$7 && $$5) {
            if (this.sprintTriggerTime <= 0 && !this.minecraft.options.keySprint.isDown()) {
                this.sprintTriggerTime = 7;
            } else {
                this.m_6858_(true);
            }
        }
        if ((!this.m_20069_() || this.isUnderWater()) && $$5 && this.minecraft.options.keySprint.isDown()) {
            this.m_6858_(true);
        }
        if (this.m_20142_()) {
            boolean $$8 = !this.input.hasForwardImpulse() || !this.hasEnoughFoodToStartSprinting();
            boolean $$9 = $$8 || this.f_19862_ && !this.f_185931_ || this.m_20069_() && !this.isUnderWater();
            if (this.m_6069_()) {
                if (!this.m_20096_() && !this.input.shiftKeyDown && $$8 || !this.m_20069_()) {
                    this.m_6858_(false);
                }
            } else if ($$9) {
                this.m_6858_(false);
            }
        }
        boolean $$10 = false;
        if (this.m_150110_().mayfly) {
            if (this.minecraft.gameMode.isAlwaysFlying()) {
                if (!this.m_150110_().flying) {
                    this.m_150110_().flying = true;
                    $$10 = true;
                    this.onUpdateAbilities();
                }
            } else if (!$$0 && this.input.jumping && !$$4) {
                if (this.f_36098_ == 0) {
                    this.f_36098_ = 7;
                } else if (!this.m_6069_()) {
                    this.m_150110_().flying = !this.m_150110_().flying;
                    $$10 = true;
                    this.onUpdateAbilities();
                    this.f_36098_ = 0;
                }
            }
        }
        if (this.input.jumping && !$$10 && !$$0 && !this.m_150110_().flying && !this.m_20159_() && !this.m_6147_()) {
            ItemStack $$11 = this.m_6844_(EquipmentSlot.CHEST);
            if ($$11.is(Items.ELYTRA) && ElytraItem.isFlyEnabled($$11) && this.m_36319_()) {
                this.connection.send(new ServerboundPlayerCommandPacket(this, ServerboundPlayerCommandPacket.Action.START_FALL_FLYING));
            }
        }
        this.wasFallFlying = this.m_21255_();
        if (this.m_20069_() && this.input.shiftKeyDown && this.m_6129_()) {
            this.m_21208_();
        }
        if (this.m_204029_(FluidTags.WATER)) {
            int $$12 = this.m_5833_() ? 10 : 1;
            this.waterVisionTime = Mth.clamp(this.waterVisionTime + $$12, 0, 600);
        } else if (this.waterVisionTime > 0) {
            this.m_204029_(FluidTags.WATER);
            this.waterVisionTime = Mth.clamp(this.waterVisionTime - 10, 0, 600);
        }
        if (this.m_150110_().flying && this.isControlledCamera()) {
            int $$13 = 0;
            if (this.input.shiftKeyDown) {
                $$13--;
            }
            if (this.input.jumping) {
                $$13++;
            }
            if ($$13 != 0) {
                this.m_20256_(this.m_20184_().add(0.0, (double) ((float) $$13 * this.m_150110_().getFlyingSpeed() * 3.0F), 0.0));
            }
        }
        PlayerRideableJumping $$14 = this.jumpableVehicle();
        if ($$14 != null && $$14.getJumpCooldown() == 0) {
            if (this.jumpRidingTicks < 0) {
                this.jumpRidingTicks++;
                if (this.jumpRidingTicks == 0) {
                    this.jumpRidingScale = 0.0F;
                }
            }
            if ($$0 && !this.input.jumping) {
                this.jumpRidingTicks = -10;
                $$14.onPlayerJump(Mth.floor(this.getJumpRidingScale() * 100.0F));
                this.sendRidingJump();
            } else if (!$$0 && this.input.jumping) {
                this.jumpRidingTicks = 0;
                this.jumpRidingScale = 0.0F;
            } else if ($$0) {
                this.jumpRidingTicks++;
                if (this.jumpRidingTicks < 10) {
                    this.jumpRidingScale = (float) this.jumpRidingTicks * 0.1F;
                } else {
                    this.jumpRidingScale = 0.8F + 2.0F / (float) (this.jumpRidingTicks - 9) * 0.1F;
                }
            }
        } else {
            this.jumpRidingScale = 0.0F;
        }
        super.m_8107_();
        if (this.m_20096_() && this.m_150110_().flying && !this.minecraft.gameMode.isAlwaysFlying()) {
            this.m_150110_().flying = false;
            this.onUpdateAbilities();
        }
    }

    @Override
    protected void tickDeath() {
        this.f_20919_++;
        if (this.f_20919_ == 20) {
            this.m_142687_(Entity.RemovalReason.KILLED);
        }
    }

    private void handleNetherPortalClient() {
        this.oSpinningEffectIntensity = this.spinningEffectIntensity;
        float $$0 = 0.0F;
        if (this.f_19817_) {
            if (this.minecraft.screen != null && !this.minecraft.screen.isPauseScreen() && !(this.minecraft.screen instanceof DeathScreen)) {
                if (this.minecraft.screen instanceof AbstractContainerScreen) {
                    this.closeContainer();
                }
                this.minecraft.setScreen(null);
            }
            if (this.spinningEffectIntensity == 0.0F) {
                this.minecraft.getSoundManager().play(SimpleSoundInstance.forLocalAmbience(SoundEvents.PORTAL_TRIGGER, this.f_19796_.nextFloat() * 0.4F + 0.8F, 0.25F));
            }
            $$0 = 0.0125F;
            this.f_19817_ = false;
        } else if (this.m_21023_(MobEffects.CONFUSION) && !this.m_21124_(MobEffects.CONFUSION).endsWithin(60)) {
            $$0 = 0.006666667F;
        } else if (this.spinningEffectIntensity > 0.0F) {
            $$0 = -0.05F;
        }
        this.spinningEffectIntensity = Mth.clamp(this.spinningEffectIntensity + $$0, 0.0F, 1.0F);
        this.m_8021_();
    }

    @Override
    public void rideTick() {
        super.m_6083_();
        this.handsBusy = false;
        if (this.m_275832_() instanceof Boat $$0) {
            $$0.setInput(this.input.left, this.input.right, this.input.up, this.input.down);
            this.handsBusy = this.handsBusy | (this.input.left || this.input.right || this.input.up || this.input.down);
        }
    }

    public boolean isHandsBusy() {
        return this.handsBusy;
    }

    @Nullable
    @Override
    public MobEffectInstance removeEffectNoUpdate(@Nullable MobEffect mobEffect0) {
        if (mobEffect0 == MobEffects.CONFUSION) {
            this.oSpinningEffectIntensity = 0.0F;
            this.spinningEffectIntensity = 0.0F;
        }
        return super.m_6234_(mobEffect0);
    }

    @Override
    public void move(MoverType moverType0, Vec3 vec1) {
        double $$2 = this.m_20185_();
        double $$3 = this.m_20189_();
        super.m_6478_(moverType0, vec1);
        this.updateAutoJump((float) (this.m_20185_() - $$2), (float) (this.m_20189_() - $$3));
    }

    public boolean isAutoJumpEnabled() {
        return this.autoJumpEnabled;
    }

    protected void updateAutoJump(float float0, float float1) {
        if (this.canAutoJump()) {
            Vec3 $$2 = this.m_20182_();
            Vec3 $$3 = $$2.add((double) float0, 0.0, (double) float1);
            Vec3 $$4 = new Vec3((double) float0, 0.0, (double) float1);
            float $$5 = this.m_6113_();
            float $$6 = (float) $$4.lengthSqr();
            if ($$6 <= 0.001F) {
                Vec2 $$7 = this.input.getMoveVector();
                float $$8 = $$5 * $$7.x;
                float $$9 = $$5 * $$7.y;
                float $$10 = Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0));
                float $$11 = Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0));
                $$4 = new Vec3((double) ($$8 * $$11 - $$9 * $$10), $$4.y, (double) ($$9 * $$11 + $$8 * $$10));
                $$6 = (float) $$4.lengthSqr();
                if ($$6 <= 0.001F) {
                    return;
                }
            }
            float $$12 = Mth.invSqrt($$6);
            Vec3 $$13 = $$4.scale((double) $$12);
            Vec3 $$14 = this.m_20156_();
            float $$15 = (float) ($$14.x * $$13.x + $$14.z * $$13.z);
            if (!($$15 < -0.15F)) {
                CollisionContext $$16 = CollisionContext.of(this);
                BlockPos $$17 = BlockPos.containing(this.m_20185_(), this.m_20191_().maxY, this.m_20189_());
                BlockState $$18 = this.m_9236_().getBlockState($$17);
                if ($$18.m_60742_(this.m_9236_(), $$17, $$16).isEmpty()) {
                    $$17 = $$17.above();
                    BlockState $$19 = this.m_9236_().getBlockState($$17);
                    if ($$19.m_60742_(this.m_9236_(), $$17, $$16).isEmpty()) {
                        float $$20 = 7.0F;
                        float $$21 = 1.2F;
                        if (this.m_21023_(MobEffects.JUMP)) {
                            $$21 += (float) (this.m_21124_(MobEffects.JUMP).getAmplifier() + 1) * 0.75F;
                        }
                        float $$22 = Math.max($$5 * 7.0F, 1.0F / $$12);
                        Vec3 $$24 = $$3.add($$13.scale((double) $$22));
                        float $$25 = this.m_20205_();
                        float $$26 = this.m_20206_();
                        AABB $$27 = new AABB($$2, $$24.add(0.0, (double) $$26, 0.0)).inflate((double) $$25, 0.0, (double) $$25);
                        Vec3 $$23 = $$2.add(0.0, 0.51F, 0.0);
                        $$24 = $$24.add(0.0, 0.51F, 0.0);
                        Vec3 $$28 = $$13.cross(new Vec3(0.0, 1.0, 0.0));
                        Vec3 $$29 = $$28.scale((double) ($$25 * 0.5F));
                        Vec3 $$30 = $$23.subtract($$29);
                        Vec3 $$31 = $$24.subtract($$29);
                        Vec3 $$32 = $$23.add($$29);
                        Vec3 $$33 = $$24.add($$29);
                        Iterable<VoxelShape> $$34 = this.m_9236_().m_186431_(this, $$27);
                        Iterator<AABB> $$35 = StreamSupport.stream($$34.spliterator(), false).flatMap(p_234124_ -> p_234124_.toAabbs().stream()).iterator();
                        float $$36 = Float.MIN_VALUE;
                        while ($$35.hasNext()) {
                            AABB $$37 = (AABB) $$35.next();
                            if ($$37.intersects($$30, $$31) || $$37.intersects($$32, $$33)) {
                                $$36 = (float) $$37.maxY;
                                Vec3 $$38 = $$37.getCenter();
                                BlockPos $$39 = BlockPos.containing($$38);
                                for (int $$40 = 1; (float) $$40 < $$21; $$40++) {
                                    BlockPos $$41 = $$39.above($$40);
                                    BlockState $$42 = this.m_9236_().getBlockState($$41);
                                    VoxelShape $$43;
                                    if (!($$43 = $$42.m_60742_(this.m_9236_(), $$41, $$16)).isEmpty()) {
                                        $$36 = (float) $$43.max(Direction.Axis.Y) + (float) $$41.m_123342_();
                                        if ((double) $$36 - this.m_20186_() > (double) $$21) {
                                            return;
                                        }
                                    }
                                    if ($$40 > 1) {
                                        $$17 = $$17.above();
                                        BlockState $$44 = this.m_9236_().getBlockState($$17);
                                        if (!$$44.m_60742_(this.m_9236_(), $$17, $$16).isEmpty()) {
                                            return;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                        if ($$36 != Float.MIN_VALUE) {
                            float $$45 = (float) ((double) $$36 - this.m_20186_());
                            if (!($$45 <= 0.5F) && !($$45 > $$21)) {
                                this.autoJumpTime = 1;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected boolean isHorizontalCollisionMinor(Vec3 vec0) {
        float $$1 = this.m_146908_() * (float) (Math.PI / 180.0);
        double $$2 = (double) Mth.sin($$1);
        double $$3 = (double) Mth.cos($$1);
        double $$4 = (double) this.f_20900_ * $$3 - (double) this.f_20902_ * $$2;
        double $$5 = (double) this.f_20902_ * $$3 + (double) this.f_20900_ * $$2;
        double $$6 = Mth.square($$4) + Mth.square($$5);
        double $$7 = Mth.square(vec0.x) + Mth.square(vec0.z);
        if (!($$6 < 1.0E-5F) && !($$7 < 1.0E-5F)) {
            double $$8 = $$4 * vec0.x + $$5 * vec0.z;
            double $$9 = Math.acos($$8 / Math.sqrt($$6 * $$7));
            return $$9 < 0.13962634F;
        } else {
            return false;
        }
    }

    private boolean canAutoJump() {
        return this.isAutoJumpEnabled() && this.autoJumpTime <= 0 && this.m_20096_() && !this.m_36343_() && !this.m_20159_() && this.isMoving() && (double) this.m_20098_() >= 1.0;
    }

    private boolean isMoving() {
        Vec2 $$0 = this.input.getMoveVector();
        return $$0.x != 0.0F || $$0.y != 0.0F;
    }

    private boolean canStartSprinting() {
        return !this.m_20142_() && this.hasEnoughImpulseToStartSprinting() && this.hasEnoughFoodToStartSprinting() && !this.isUsingItem() && !this.m_21023_(MobEffects.BLINDNESS) && (!this.m_20159_() || this.vehicleCanSprint(this.m_20202_())) && !this.m_21255_();
    }

    private boolean vehicleCanSprint(Entity entity0) {
        return entity0.canSprint() && entity0.isControlledByLocalInstance();
    }

    private boolean hasEnoughImpulseToStartSprinting() {
        double $$0 = 0.8;
        return this.isUnderWater() ? this.input.hasForwardImpulse() : (double) this.input.forwardImpulse >= 0.8;
    }

    private boolean hasEnoughFoodToStartSprinting() {
        return this.m_20159_() || (float) this.m_36324_().getFoodLevel() > 6.0F || this.m_150110_().mayfly;
    }

    public float getWaterVision() {
        if (!this.m_204029_(FluidTags.WATER)) {
            return 0.0F;
        } else {
            float $$0 = 600.0F;
            float $$1 = 100.0F;
            if ((float) this.waterVisionTime >= 600.0F) {
                return 1.0F;
            } else {
                float $$2 = Mth.clamp((float) this.waterVisionTime / 100.0F, 0.0F, 1.0F);
                float $$3 = (float) this.waterVisionTime < 100.0F ? 0.0F : Mth.clamp(((float) this.waterVisionTime - 100.0F) / 500.0F, 0.0F, 1.0F);
                return $$2 * 0.6F + $$3 * 0.39999998F;
            }
        }
    }

    public void onGameModeChanged(GameType gameType0) {
        if (gameType0 == GameType.SPECTATOR) {
            this.m_20256_(this.m_20184_().with(Direction.Axis.Y, 0.0));
        }
    }

    @Override
    public boolean isUnderWater() {
        return this.f_36076_;
    }

    @Override
    protected boolean updateIsUnderwater() {
        boolean $$0 = this.f_36076_;
        boolean $$1 = super.m_7602_();
        if (this.m_5833_()) {
            return this.f_36076_;
        } else {
            if (!$$0 && $$1) {
                this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundSource.AMBIENT, 1.0F, 1.0F, false);
                this.minecraft.getSoundManager().play(new UnderwaterAmbientSoundInstances.UnderwaterAmbientSoundInstance(this));
            }
            if ($$0 && !$$1) {
                this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.AMBIENT_UNDERWATER_EXIT, SoundSource.AMBIENT, 1.0F, 1.0F, false);
            }
            return this.f_36076_;
        }
    }

    @Override
    public Vec3 getRopeHoldPosition(float float0) {
        if (this.minecraft.options.getCameraType().isFirstPerson()) {
            float $$1 = Mth.lerp(float0 * 0.5F, this.m_146908_(), this.f_19859_) * (float) (Math.PI / 180.0);
            float $$2 = Mth.lerp(float0 * 0.5F, this.m_146909_(), this.f_19860_) * (float) (Math.PI / 180.0);
            double $$3 = this.m_5737_() == HumanoidArm.RIGHT ? -1.0 : 1.0;
            Vec3 $$4 = new Vec3(0.39 * $$3, -0.6, 0.3);
            return $$4.xRot(-$$2).yRot(-$$1).add(this.m_20299_(float0));
        } else {
            return super.m_7398_(float0);
        }
    }

    @Override
    public void updateTutorialInventoryAction(ItemStack itemStack0, ItemStack itemStack1, ClickAction clickAction2) {
        this.minecraft.getTutorial().onInventoryAction(itemStack0, itemStack1, clickAction2);
    }

    @Override
    public float getVisualRotationYInDegrees() {
        return this.m_146908_();
    }
}