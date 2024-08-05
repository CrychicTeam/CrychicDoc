package net.mehvahdjukaar.supplementaries.common.block.tiles;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.Optional;
import java.util.UUID;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.util.FakePlayerManager;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.mehvahdjukaar.moonlight.core.misc.DummyWorld;
import net.mehvahdjukaar.supplementaries.client.cannon.CannonController;
import net.mehvahdjukaar.supplementaries.common.entities.SlingshotProjectileEntity;
import net.mehvahdjukaar.supplementaries.common.inventories.CannonContainerMenu;
import net.mehvahdjukaar.supplementaries.reg.ModParticles;
import net.mehvahdjukaar.supplementaries.reg.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

public class CannonBlockTile extends OpeneableContainerBlockEntity {

    private static final int TIME_TO_FIRE = 40;

    private static final int FIRE_COOLDOWN = 60;

    private float pitch = 0.0F;

    private float prevPitch = 0.0F;

    private float yaw = 0.0F;

    private float prevYaw = 0.0F;

    private float cooldown = 0.0F;

    private float chargeTimer = 0.0F;

    private byte firePower = 1;

    private float projectileDrag = 0.0F;

    private float projectileGravity = 0.0F;

    private static final GameProfile FAKE_PLAYER = new GameProfile(UUID.fromString("11242C44-14d5-1f22-3d27-13D2C45CA355"), "[CANNON_TESTER]");

    public CannonBlockTile(BlockPos pos, BlockState blockState) {
        super(null, pos, blockState, 2);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putFloat("yaw", this.yaw);
        tag.putFloat("pitch", this.pitch);
        tag.putFloat("cooldown", this.cooldown);
        tag.putFloat("fire_timer", this.chargeTimer);
        tag.putByte("fire_power", this.firePower);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.yaw = tag.getFloat("yaw");
        this.pitch = tag.getFloat("pitch");
        this.cooldown = tag.getFloat("cooldown");
        this.chargeTimer = tag.getFloat("fire_timer");
        this.firePower = tag.getByte("fire_power");
        if (this.f_58857_ != null) {
            this.recalculateProjectileStats();
        }
    }

    @Override
    public void setChanged() {
        if (this.f_58857_ != null) {
            this.recalculateProjectileStats();
        }
    }

    public boolean readyToFire() {
        return this.cooldown == 0.0F && this.chargeTimer == 0.0F && this.hasFuelAndProjectiles();
    }

    public boolean hasFuelAndProjectiles() {
        return !this.getProjectile().isEmpty() && !this.getFuel().isEmpty() && this.getFuel().getCount() >= this.firePower;
    }

    public float getCooldown() {
        return this.cooldown;
    }

    public float getFireTimer() {
        return this.chargeTimer;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public ItemStack getProjectile() {
        return this.m_8020_(1).copyWithCount(1);
    }

    public ItemStack getFuel() {
        return this.m_8020_(0);
    }

    public float getProjectileDrag() {
        return this.projectileDrag;
    }

    public float getProjectileGravity() {
        return this.projectileGravity;
    }

    public byte getFirePower() {
        return this.firePower;
    }

    public float getYaw(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevYaw, this.yaw);
    }

    public float getPitch(float partialTicks) {
        return Mth.rotLerp(partialTicks, this.prevPitch, this.pitch);
    }

    public void syncAttributes(float yaw, float pitch, byte firePower, boolean fire) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.firePower = firePower;
        if (fire) {
            this.ignite();
        }
    }

    public void setPitch(float pitch) {
        this.pitch = Mth.wrapDegrees(pitch);
    }

    public void setYaw(float yaw) {
        this.yaw = Mth.wrapDegrees(yaw);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("gui.supplementaries.cannon");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new CannonContainerMenu(id, player, this);
    }

    @Override
    protected void updateBlockState(BlockState state, boolean b) {
    }

    @Override
    protected void playOpenSound(BlockState state) {
    }

    @Override
    protected void playCloseSound(BlockState state) {
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return index == 0 ? stack.is(Items.GUNPOWDER) : true;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        return this.canPlaceItem(direction != null && !direction.getAxis().isHorizontal() ? 0 : 1, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[] { side.getAxis().isHorizontal() ? 1 : 0 };
    }

    public void use(Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.isSecondaryUseActive()) {
            if (!(player instanceof ServerPlayer serverPlayer)) {
                CannonController.activateCannonCamera(this);
            }
        } else if (player instanceof ServerPlayer sp) {
            PlatHelper.openCustomMenu(sp, this, this.f_58858_);
        }
    }

    public void ignite() {
        this.f_58857_.playSound(null, this.f_58858_, (SoundEvent) ModSounds.GUNPOWDER_IGNITE.get(), SoundSource.BLOCKS, 1.0F, 1.8F + this.f_58857_.getRandom().nextFloat() * 0.2F);
        this.chargeTimer = 1.0F;
        this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 3);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CannonBlockTile t) {
        t.prevYaw = t.yaw;
        t.prevPitch = t.pitch;
        if (t.cooldown > 0.0F) {
            t.cooldown -= 0.016666668F;
            if (t.cooldown < 0.0F) {
                t.cooldown = 0.0F;
            }
        }
        if (t.chargeTimer > 0.0F) {
            t.chargeTimer -= 0.025F;
            if (t.chargeTimer <= 0.0F) {
                t.chargeTimer = 0.0F;
                t.fire();
            }
        }
    }

    public void fire() {
        if (this.f_58857_.isClientSide) {
            BlockPos pos = this.f_58858_;
            this.f_58857_.addParticle((ParticleOptions) ModParticles.CANNON_FIRE_PARTICLE.get(), (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, (double) (this.pitch * (float) (Math.PI / 180.0)), (double) (-this.yaw * (float) (Math.PI / 180.0)), 0.0);
            PoseStack poseStack = new PoseStack();
            RandomSource ran = this.f_58857_.random;
            poseStack.translate((float) pos.m_123341_() + 0.5F, (float) pos.m_123342_() + 0.5F + 0.0625F, (float) pos.m_123343_() + 0.5F);
            poseStack.mulPose(Axis.YP.rotationDegrees(-this.yaw));
            poseStack.mulPose(Axis.XP.rotationDegrees(this.pitch));
            poseStack.translate(0.0, 0.0, -1.4);
            this.spawnDustRing(poseStack);
            this.spawnSmokeTrail(poseStack, ran);
        } else {
            this.shootProjectile();
        }
        this.cooldown = 1.0F;
    }

    private boolean shootProjectile() {
        BlockPos pos = this.f_58858_;
        Vec3 facing = Vec3.directionFromRotation(this.pitch, this.yaw).scale(0.01);
        ItemStack projectile = this.getProjectile();
        if (getProjectileFromItemHack(this.f_58857_, projectile) instanceof Projectile arrow) {
            arrow.cachedOwner = null;
            arrow.ownerUUID = null;
            CompoundTag c = new CompoundTag();
            arrow.m_20223_(c);
            Optional<Entity> opt = EntityType.create(c, this.f_58857_);
            if (opt.isPresent()) {
                Projectile var10 = (Projectile) opt.get();
                var10.m_6034_((double) pos.m_123341_() + 0.5 - facing.x, (double) pos.m_123342_() + 0.5 - facing.y, (double) pos.m_123343_() + 0.5 - facing.z);
                float inaccuracy = 0.0F;
                float power = -this.projectileDrag * (float) this.getFirePower();
                var10.shoot(facing.x, facing.y, facing.z, power, inaccuracy);
                this.f_58857_.m_7967_(var10);
                return true;
            }
        }
        return false;
    }

    private void spawnSmokeTrail(PoseStack poseStack, RandomSource ran) {
        int smokeCount = 20;
        for (int i = 0; i < smokeCount; i++) {
            poseStack.pushPose();
            Vector4f speed = poseStack.last().pose().transform(new Vector4f(0.0F, 0.0F, -MthUtils.nextWeighted(ran, 0.5F, 1.0F, 0.06F), 0.0F));
            float aperture = 0.5F;
            poseStack.translate(-aperture / 2.0F + ran.nextFloat() * aperture, -aperture / 2.0F + ran.nextFloat() * aperture, 0.0F);
            Vector4f p = poseStack.last().pose().transform(new Vector4f(0.0F, 0.0F, 1.0F, 1.0F));
            this.f_58857_.addParticle(ParticleTypes.SMOKE, (double) p.x, (double) p.y, (double) p.z, (double) speed.x, (double) speed.y, (double) speed.z);
            poseStack.popPose();
        }
    }

    private void spawnDustRing(PoseStack poseStack) {
        poseStack.pushPose();
        Vector4f p = poseStack.last().pose().transform(new Vector4f(0.0F, 0.0F, 1.0F, 1.0F));
        int dustCount = 16;
        for (int i = 0; i < dustCount; i++) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(380.0F * (float) i / (float) dustCount));
            Vector4f speed = poseStack.last().pose().transform(new Vector4f(0.0F, 0.0F, 0.05F, 0.0F));
            this.f_58857_.addParticle((ParticleOptions) ModParticles.BOMB_SMOKE_PARTICLE.get(), (double) p.x, (double) p.y, (double) p.z, (double) speed.x, (double) speed.y, (double) speed.z);
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    private static Entity getProjectileFromItemHack(Level level, ItemStack projectile) {
        Player fakePlayer = FakePlayerManager.get(FAKE_PLAYER, level);
        if (projectile.getItem() instanceof ArrowItem ai) {
            return ai.createArrow(level, projectile, fakePlayer);
        } else {
            CannonBlockTile.ProjectileTestLevel testLevel = CannonBlockTile.ProjectileTestLevel.getCachedInstance("cannon_test_level", CannonBlockTile.ProjectileTestLevel::new);
            testLevel.setup();
            fakePlayer.m_21008_(InteractionHand.MAIN_HAND, projectile.copy());
            projectile.use(testLevel, fakePlayer, InteractionHand.MAIN_HAND);
            Entity p = testLevel.projectile;
            if (p != null) {
                return p;
            } else {
                return (Entity) (projectile.is(Items.FIRE_CHARGE) ? EntityType.SMALL_FIREBALL.create(level) : new SlingshotProjectileEntity(level, projectile, ItemStack.EMPTY));
            }
        }
    }

    private void recalculateProjectileStats() {
        ItemStack projectile = this.getProjectile();
        if (!projectile.isEmpty()) {
            Entity proj = getProjectileFromItemHack(this.f_58857_, projectile);
            proj.setDeltaMovement(1.0, 0.0, 0.0);
            proj.tick();
            Vec3 newMovement = proj.getDeltaMovement();
            this.projectileDrag = (float) newMovement.x;
            this.projectileGravity = (float) (-newMovement.y);
        }
    }

    private static class ProjectileTestLevel extends DummyWorld {

        private Entity projectile = null;

        public ProjectileTestLevel() {
            super(false, false);
        }

        public void setup() {
            this.projectile = null;
        }

        @Override
        public boolean addFreshEntity(Entity entity) {
            this.projectile = entity;
            return true;
        }
    }
}