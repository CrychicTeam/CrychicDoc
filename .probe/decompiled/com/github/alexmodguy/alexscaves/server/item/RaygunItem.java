package com.github.alexmodguy.alexscaves.server.item;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexmodguy.alexscaves.server.message.UpdateEffectVisualityEntityMessage;
import com.github.alexmodguy.alexscaves.server.message.UpdateItemTagMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACDamageTypes;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class RaygunItem extends Item implements UpdatesStackTags, AlwaysCombinableOnAnvil {

    private static final int MAX_CHARGE = 1000;

    public static final Predicate<ItemStack> AMMO = stack -> stack.getItem() == ACBlockRegistry.URANIUM_ROD.get().asItem();

    public RaygunItem() {
        super(new Item.Properties().stacksTo(1));
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) AlexsCaves.PROXY.getISTERProperties());
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return stack.getCount() == 1;
    }

    public static boolean hasCharge(ItemStack stack) {
        return getCharge(stack) < 1000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.m_21120_(interactionHand);
        if (hasCharge(itemstack)) {
            player.m_6672_(interactionHand);
            player.m_216990_(ACSoundRegistry.RAYGUN_START.get());
            return InteractionResultHolder.consume(itemstack);
        } else {
            ItemStack ammo = this.findAmmo(player);
            boolean flag = player.isCreative();
            if (!ammo.isEmpty()) {
                ammo.shrink(1);
                flag = true;
            }
            if (flag) {
                setCharge(itemstack, 0);
                player.m_9236_().playSound((Player) null, player.m_20185_(), player.m_20186_(), player.m_20189_(), ACSoundRegistry.RAYGUN_RELOAD.get(), player.getSoundSource(), 1.0F, 1.0F);
            } else {
                player.m_9236_().playSound((Player) null, player.m_20185_(), player.m_20186_(), player.m_20189_(), ACSoundRegistry.RAYGUN_EMPTY.get(), player.getSoundSource(), 1.0F, 1.0F);
            }
            return InteractionResultHolder.fail(itemstack);
        }
    }

    private ItemStack findAmmo(Player entity) {
        if (entity.isCreative()) {
            return ItemStack.EMPTY;
        } else {
            for (int i = 0; i < entity.getInventory().getContainerSize(); i++) {
                ItemStack itemstack1 = entity.getInventory().getItem(i);
                if (AMMO.test(itemstack1)) {
                    return itemstack1;
                }
            }
            return ItemStack.EMPTY;
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean held) {
        boolean var10000;
        label52: {
            super.inventoryTick(stack, level, entity, i, held);
            if (entity instanceof LivingEntity living && living.getUseItem().equals(stack)) {
                var10000 = true;
                break label52;
            }
            var10000 = false;
        }
        boolean using = var10000;
        int useTime = getUseTime(stack);
        if (!level.isClientSide) {
            if (stack.getEnchantmentLevel(ACEnchantmentRegistry.SOLAR.get()) > 0 && !using) {
                int charge = getCharge(stack);
                if (charge > 0 && level.random.nextFloat() < 0.02F) {
                    BlockPos playerPos = entity.blockPosition().above();
                    float timeOfDay = level.m_46942_(1.0F);
                    if (level.m_45527_(playerPos) && level.isDay() && !level.dimensionType().hasFixedTime() && ((double) timeOfDay < 0.259 || (double) timeOfDay > 0.74)) {
                        setCharge(stack, charge - 1);
                        setUseTime(stack, 0);
                    }
                }
            }
        } else {
            CompoundTag tag = stack.getOrCreateTag();
            if (tag.getInt("PrevUseTime") != tag.getInt("UseTime")) {
                tag.putInt("PrevUseTime", getUseTime(stack));
            }
            if (using && (float) useTime < 5.0F) {
                setUseTime(stack, useTime + 1);
            }
            if (!using && (float) useTime > 0.0F) {
                setUseTime(stack, useTime - 1);
            }
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int timeUsing) {
        int i = this.getUseDuration(stack) - timeUsing;
        int realStart = 15;
        float time = i < realStart ? (float) i / (float) realStart : 1.0F;
        float maxDist = 25.0F * time;
        boolean xRay = stack.getEnchantmentLevel(ACEnchantmentRegistry.X_RAY.get()) > 0;
        HitResult realHitResult = ProjectileUtil.getHitResultOnViewVector(living, Entity::m_271807_, (double) maxDist);
        HitResult blockOnlyHitResult = living.m_19907_((double) maxDist, 0.0F, false);
        Vec3 xRayVec = living.m_20252_(0.0F).scale((double) maxDist).add(living.m_146892_());
        Vec3 vec3 = xRay ? xRayVec : blockOnlyHitResult.getLocation();
        Vec3 vec31 = xRay ? xRayVec : blockOnlyHitResult.getLocation();
        if (!hasCharge(stack)) {
            if (level.isClientSide) {
                AlexsCaves.sendMSGToServer(new UpdateItemTagMessage(living.m_19879_(), stack));
            }
            living.stopUsingItem();
            level.playSound((Player) null, living.m_20185_(), living.m_20186_(), living.m_20189_(), ACSoundRegistry.RAYGUN_EMPTY.get(), living.m_5720_(), 1.0F, 1.0F);
        } else {
            if (level.isClientSide) {
                setRayPosition(stack, vec3.x, vec3.y, vec3.z);
                AlexsCaves.PROXY.playWorldSound(living, (byte) 8);
                int efficency = stack.getEnchantmentLevel(ACEnchantmentRegistry.ENERGY_EFFICIENCY.get());
                int divis = 2 + (int) Math.floor((double) ((float) efficency * 1.5F));
                if (time >= 1.0F && i % divis == 0 && (!(living instanceof Player) || !((Player) living).isCreative())) {
                    int charge = getCharge(stack);
                    setCharge(stack, Math.min(charge + 1, 1000));
                }
            }
            float deltaX = 0.0F;
            float deltaY = 0.0F;
            float deltaZ = 0.0F;
            boolean gamma = stack.getEnchantmentLevel(ACEnchantmentRegistry.GAMMA_RAY.get()) > 0;
            ParticleOptions particleOptions;
            if (level.random.nextBoolean() && time >= 1.0F) {
                particleOptions = gamma ? ACParticleRegistry.BLUE_RAYGUN_EXPLOSION.get() : ACParticleRegistry.RAYGUN_EXPLOSION.get();
            } else {
                particleOptions = gamma ? ACParticleRegistry.BLUE_HAZMAT_BREATHE.get() : ACParticleRegistry.HAZMAT_BREATHE.get();
                deltaX = (level.random.nextFloat() - 0.5F) * 0.2F;
                deltaY = (level.random.nextFloat() - 0.5F) * 0.2F;
                deltaZ = (level.random.nextFloat() - 0.5F) * 0.2F;
            }
            level.addParticle(particleOptions, vec3.x + (double) ((level.random.nextFloat() - 0.5F) * 0.45F), vec3.y + 0.2F, vec3.z + (double) ((level.random.nextFloat() - 0.5F) * 0.45F), (double) deltaX, (double) deltaY, (double) deltaZ);
            Direction blastHitDirection = null;
            Vec3 blastHitPos = null;
            if (xRay) {
                AABB maxAABB = living.m_20191_().inflate((double) maxDist);
                float fakeRayTraceProgress = 1.0F;
                for (Vec3 startClip = living.m_146892_(); fakeRayTraceProgress < maxDist; fakeRayTraceProgress++) {
                    startClip = startClip.add(living.m_20252_(1.0F));
                    Vec3 endClip = startClip.add(living.m_20252_(1.0F));
                    HitResult attemptedHitResult = ProjectileUtil.getEntityHitResult(level, living, startClip, endClip, maxAABB, Entity::m_271807_);
                    if (attemptedHitResult != null) {
                        realHitResult = attemptedHitResult;
                        break;
                    }
                }
            } else if (realHitResult instanceof BlockHitResult blockHitResult) {
                BlockPos pos = blockHitResult.getBlockPos();
                BlockState state = level.getBlockState(pos);
                blastHitDirection = blockHitResult.getDirection();
                if (!state.m_60795_() && state.m_60783_(level, pos, blastHitDirection)) {
                    blastHitPos = realHitResult.getLocation();
                }
            }
            if (realHitResult instanceof EntityHitResult entityHitResult) {
                blastHitPos = entityHitResult.getEntity().position();
                blastHitDirection = Direction.UP;
                vec31 = blastHitPos;
            }
            if (blastHitPos != null && i % 2 == 0) {
                float offset = 0.05F + level.random.nextFloat() * 0.09F;
                Vec3 particleVec = blastHitPos.add((double) (offset * (float) blastHitDirection.getStepX()), (double) (offset * (float) blastHitDirection.getStepY()), (double) (offset * (float) blastHitDirection.getStepZ()));
                level.addParticle(ACParticleRegistry.RAYGUN_BLAST.get(), particleVec.x, particleVec.y, particleVec.z, (double) blastHitDirection.get3DDataValue(), 0.0, 0.0);
            }
            if (!level.isClientSide && (i - realStart) % 3 == 0) {
                AABB hitBox = new AABB(vec31.add(-1.0, -1.0, -1.0), vec31.add(1.0, 1.0, 1.0));
                int radiationLevel = gamma ? 4 : 0;
                for (Entity entity : level.getEntities(living, hitBox, Entity::m_271807_)) {
                    if (!entity.is(living) && !entity.isAlliedTo(living) && !living.m_7307_(entity) && !living.m_20365_(entity)) {
                        boolean flag = entity instanceof TremorzillaEntity || entity.hurt(ACDamageTypes.causeRaygunDamage(level.registryAccess(), living), gamma ? 2.0F : 1.5F);
                        if (flag && entity instanceof LivingEntity) {
                            LivingEntity livingEntity = (LivingEntity) entity;
                            if (!livingEntity.m_6095_().is(ACTagRegistry.RESISTS_RADIATION) && livingEntity.addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED.get(), 800, radiationLevel))) {
                                AlexsCaves.sendMSGToAll(new UpdateEffectVisualityEntityMessage(entity.getId(), living.m_19879_(), gamma ? 4 : 0, 800));
                            }
                        }
                    }
                }
            }
        }
    }

    public static void setUseTime(ItemStack stack, int useTime) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("PrevUseTime", getUseTime(stack));
        tag.putInt("UseTime", useTime);
    }

    public static void setRayPosition(ItemStack stack, double x, double y, double z) {
        CompoundTag tag = stack.getOrCreateTag();
        Vec3 prev = getRayPosition(stack);
        tag.putDouble("PrevRayX", prev.x);
        tag.putDouble("PrevRayY", prev.y);
        tag.putDouble("PrevRayZ", prev.z);
        tag.putDouble("RayX", x);
        tag.putDouble("RayY", y);
        tag.putDouble("RayZ", z);
    }

    public static int getUseTime(ItemStack stack) {
        CompoundTag compoundtag = stack.getTag();
        return compoundtag != null ? compoundtag.getInt("UseTime") : 0;
    }

    public static int getCharge(ItemStack stack) {
        CompoundTag compoundtag = stack.getTag();
        return compoundtag != null ? compoundtag.getInt("ChargeUsed") : 0;
    }

    public static void setCharge(ItemStack stack, int charge) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        compoundtag.putInt("ChargeUsed", charge);
    }

    public static Vec3 getRayPosition(ItemStack stack) {
        CompoundTag compoundtag = stack.getTag();
        return compoundtag != null && compoundtag.contains("RayX") ? new Vec3(compoundtag.getDouble("RayX"), compoundtag.getDouble("RayY"), compoundtag.getDouble("RayZ")) : Vec3.ZERO;
    }

    public static float getLerpedUseTime(ItemStack stack, float f) {
        CompoundTag compoundtag = stack.getTag();
        float prev = compoundtag != null ? (float) compoundtag.getInt("PrevUseTime") : 0.0F;
        float current = compoundtag != null ? (float) compoundtag.getInt("UseTime") : 0.0F;
        return prev + f * (current - prev);
    }

    @Nullable
    public static Vec3 getLerpedRayPosition(ItemStack stack, float f) {
        CompoundTag compoundtag = stack.getTag();
        if (compoundtag != null) {
            double prevX = (double) ((float) compoundtag.getDouble("PrevRayX"));
            double x = (double) ((float) compoundtag.getDouble("RayX"));
            double prevY = (double) ((float) compoundtag.getDouble("PrevRayY"));
            double y = (double) ((float) compoundtag.getDouble("RayY"));
            double prevZ = (double) ((float) compoundtag.getDouble("PrevRayZ"));
            double z = (double) ((float) compoundtag.getDouble("RayZ"));
            return new Vec3(prevX + (double) f * (x - prevX), prevY + (double) f * (y - prevY), prevZ + (double) f * (z - prevZ));
        } else {
            return null;
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity player, int useTimeLeft) {
        super.releaseUsing(stack, level, player, useTimeLeft);
        if (level.isClientSide) {
            AlexsCaves.sendMSGToServer(new UpdateItemTagMessage(player.m_19879_(), stack));
        }
        AlexsCaves.PROXY.clearSoundCacheFor(player);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getCharge(stack) != 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F - (float) getCharge(stack) * 13.0F / 1000.0F);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float pulseRate = (float) getCharge(stack) / 1000.0F * 2.0F;
        float f = (float) AlexsCaves.PROXY.getPlayerTime() + AlexsCaves.PROXY.getPartialTicks();
        float f1 = 0.5F * (float) (1.0 + Math.sin((double) (f * pulseRate)));
        return Mth.hsvToRgb(0.3F, f1 * 0.6F + 0.2F, 1.0F);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (getCharge(stack) != 0) {
            String chargeLeft = 1000 - getCharge(stack) + "";
            tooltip.add(Component.translatable("item.alexscaves.raygun.charge", chargeLeft, 1000).withStyle(ChatFormatting.GREEN));
        }
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.is(ACItemRegistry.RAYGUN.get()) || !newStack.is(ACItemRegistry.RAYGUN.get());
    }
}