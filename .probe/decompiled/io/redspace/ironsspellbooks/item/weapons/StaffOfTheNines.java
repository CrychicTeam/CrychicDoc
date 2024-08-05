package io.redspace.ironsspellbooks.item.weapons;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.render.StaffArmPose;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.function.Consumer;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class StaffOfTheNines extends Item {

    public StaffOfTheNines(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand) {
        if (!level.isClientSide) {
            Vec3 pos = this.getPosition(player.m_146892_(), 0.425F, 0.275F, 0.1775F, player.m_20155_());
            MagicManager.spawnParticles(level, ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.x, pos.y, pos.z, 5, 0.1, 0.1, 0.1, 0.01, false);
            level.playSound(null, player.m_20183_(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 4.0F, 1.5F);
            level.playSound(null, player.m_20183_(), SoundEvents.FIREWORK_ROCKET_BLAST_FAR, SoundSource.PLAYERS, 6.0F, 1.5F);
            HitResult hit = Utils.raycastForEntity(level, player, 64.0F, true, 0.1F);
            if (hit instanceof BlockHitResult blockHitResult) {
                Vec3 loc = blockHitResult.m_82450_();
                MagicManager.spawnParticles(level, new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(blockHitResult.getBlockPos())), loc.x, loc.y, loc.z, 25, 0.1, 0.1, 0.1, 0.25, true);
            } else if (hit instanceof EntityHitResult entityHitResult) {
                entityHitResult.getEntity().hurt(level.damageSources().magic(), (float) (10.0 * player.m_21133_(AttributeRegistry.SPELL_POWER.get())));
                Vec3 loc = entityHitResult.m_82450_();
                MagicManager.spawnParticles(level, ParticleHelper.BLOOD, loc.x, loc.y, loc.z, 25, 0.1, 0.1, 0.1, 0.25, true);
            }
            CameraShakeManager.addCameraShake(new CameraShakeData(5, player.m_20182_(), 5.0F));
            ((ServerPlayer) player).teleportTo((ServerLevel) level, player.m_20185_(), player.m_20186_(), player.m_20189_(), player.m_146908_(), player.m_146909_() - (float) Utils.random.nextIntBetweenInclusive(6, 9));
        }
        return super.use(level, player, pUsedHand);
    }

    public Vec3 getPosition(Vec3 vec3, float forwards, float up, float left, Vec2 vec2) {
        float f = Mth.cos((vec2.y + 90.0F) * (float) (Math.PI / 180.0));
        float f1 = Mth.sin((vec2.y + 90.0F) * (float) (Math.PI / 180.0));
        float f2 = Mth.cos(-vec2.x * (float) (Math.PI / 180.0));
        float f3 = Mth.sin(-vec2.x * (float) (Math.PI / 180.0));
        float f4 = Mth.cos((-vec2.x + 90.0F) * (float) (Math.PI / 180.0));
        float f5 = Mth.sin((-vec2.x + 90.0F) * (float) (Math.PI / 180.0));
        Vec3 vec31 = new Vec3((double) (f * f2), (double) f3, (double) (f1 * f2));
        Vec3 vec32 = new Vec3((double) (f * f4), (double) f5, (double) (f1 * f4));
        Vec3 vec33 = vec31.cross(vec32).scale(-1.0);
        double d0 = vec31.x * (double) forwards + vec32.x * (double) up + vec33.x * (double) left;
        double d1 = vec31.y * (double) forwards + vec32.y * (double) up + vec33.y * (double) left;
        double d2 = vec31.z * (double) forwards + vec32.z * (double) up + vec33.z * (double) left;
        return new Vec3(vec3.x + d0, vec3.y + d1, vec3.z + d2);
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        StaffArmPose.initializeClientHelper(consumer);
    }
}