package com.github.alexmodguy.alexscaves.server.item;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.DarkArrowEntity;
import com.github.alexmodguy.alexscaves.server.message.UpdateItemTagMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.potion.DarknessIncarnateEffect;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;

public class DreadbowItem extends ProjectileWeaponItem implements UpdatesStackTags {

    public DreadbowItem() {
        super(new Item.Properties().rarity(ACItemRegistry.RARITY_DEMONIC).durability(500));
    }

    @Nullable
    public static EntityType getTypeOfArrow(ItemStack itemStackIn) {
        if (itemStackIn.getTag() != null && itemStackIn.getTag().contains("LastUsedArrowType")) {
            String str = itemStackIn.getTag().getString("LastUsedArrowType");
            return ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(str));
        } else {
            return null;
        }
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) AlexsCaves.PROXY.getISTERProperties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.m_21120_(interactionHand);
        ItemStack ammo = player.getProjectile(itemstack);
        boolean flag = player.isCreative();
        if (!flag && ammo.isEmpty()) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            AbstractArrow lastArrow = this.createArrow(player, itemstack, ItemStack.EMPTY);
            EntityType lastArrowType = lastArrow == null ? EntityType.ARROW : lastArrow.m_6095_();
            itemstack.getOrCreateTag().putString("LastUsedArrowType", ForgeRegistries.ENTITY_TYPES.getKey(lastArrowType).toString());
            player.m_6672_(interactionHand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean held) {
        boolean var10000;
        label70: {
            super.m_6883_(stack, level, entity, i, held);
            if (entity instanceof LivingEntity living && living.getUseItem().equals(stack)) {
                var10000 = true;
                break label70;
            }
            var10000 = false;
        }
        boolean using = var10000;
        int useTime = getUseTime(stack);
        if (level.isClientSide) {
            CompoundTag tag = stack.getOrCreateTag();
            if (tag.getInt("PrevUseTime") != tag.getInt("UseTime")) {
                tag.putInt("PrevUseTime", getUseTime(stack));
            }
            if (using && getPerfectShotTicks(stack) > 0) {
                setPerfectShotTicks(stack, getPerfectShotTicks(stack) - 1);
                AlexsCaves.sendMSGToServer(new UpdateItemTagMessage(entity.getId(), stack));
            }
            boolean relentless = stack.getEnchantmentLevel(ACEnchantmentRegistry.RELENTLESS_DARKNESS.get()) > 0;
            int twilightPerfection = stack.getEnchantmentLevel(ACEnchantmentRegistry.TWILIGHT_PERFECTION.get());
            int maxLoadTime = getMaxLoadTime(stack);
            if (using && useTime < maxLoadTime) {
                int set = useTime + (relentless ? 3 : 1);
                setUseTime(stack, set);
                if (twilightPerfection > 0) {
                    if (set >= maxLoadTime && useTime <= maxLoadTime) {
                        setPerfectShotTicks(stack, 4 + (twilightPerfection - 1) * 3);
                        AlexsCaves.sendMSGToServer(new UpdateItemTagMessage(entity.getId(), stack));
                    } else {
                        setPerfectShotTicks(stack, 0);
                        AlexsCaves.sendMSGToServer(new UpdateItemTagMessage(entity.getId(), stack));
                    }
                }
            }
            if (relentless && using && useTime >= maxLoadTime) {
                setUseTime(stack, 0);
            }
            if (!using && (float) useTime > 0.0F) {
                setUseTime(stack, Math.max(0, useTime - 5));
                setPerfectShotTicks(stack, 0);
            }
            if (using) {
                Vec3 particlePos = entity.position().add((double) ((level.random.nextFloat() - 0.5F) * 2.5F), 0.0, (double) ((level.random.nextFloat() - 0.5F) * 2.5F));
                level.addParticle(ACParticleRegistry.UNDERZEALOT_MAGIC.get(), particlePos.x, particlePos.y, particlePos.z, entity.getX(), entity.getY(0.5), entity.getZ());
            }
        }
    }

    private static int getMaxLoadTime(ItemStack stack) {
        return stack.getEnchantmentLevel(ACEnchantmentRegistry.RELENTLESS_DARKNESS.get()) > 0 ? 5 : 40 - 8 * stack.getEnchantmentLevel(ACEnchantmentRegistry.DARK_NOCK.get());
    }

    public static int getUseTime(ItemStack stack) {
        CompoundTag compoundtag = stack.getTag();
        return compoundtag != null ? compoundtag.getInt("UseTime") : 0;
    }

    public static void setUseTime(ItemStack stack, int useTime) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("PrevUseTime", getUseTime(stack));
        tag.putInt("UseTime", useTime);
    }

    public static int getPerfectShotTicks(ItemStack stack) {
        CompoundTag compoundtag = stack.getTag();
        return compoundtag != null ? compoundtag.getInt("PerfectShotTicks") : 0;
    }

    public static void setPerfectShotTicks(ItemStack stack, int ticks) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("PerfectShotTicks", ticks);
    }

    public static float getLerpedUseTime(ItemStack stack, float f) {
        CompoundTag compoundtag = stack.getTag();
        float prev = compoundtag != null ? (float) compoundtag.getInt("PrevUseTime") : 0.0F;
        float current = compoundtag != null ? (float) compoundtag.getInt("UseTime") : 0.0F;
        return prev + f * (current - prev);
    }

    public static float getPullingAmount(ItemStack itemStack, float partialTicks) {
        return Math.min(getLerpedUseTime(itemStack, partialTicks) / (float) getMaxLoadTime(itemStack), 1.0F);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public static float getPowerForTime(int i, ItemStack itemStack) {
        float f = (float) i / (float) getMaxLoadTime(itemStack);
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return stack.getCount() == 1;
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i1) {
        if (livingEntity instanceof Player player && itemStack.getEnchantmentLevel(ACEnchantmentRegistry.RELENTLESS_DARKNESS.get()) <= 0) {
            int i = this.getUseDuration(itemStack) - i1;
            float f = getPowerForTime(i, itemStack);
            boolean precise = itemStack.getEnchantmentLevel(ACEnchantmentRegistry.PRECISE_VOLLEY.get()) > 0;
            boolean respite = itemStack.getEnchantmentLevel(ACEnchantmentRegistry.SHADED_RESPITE.get()) > 0 && !DarknessIncarnateEffect.isInLight(player, 11);
            boolean perfectShot = itemStack.getEnchantmentLevel(ACEnchantmentRegistry.TWILIGHT_PERFECTION.get()) > 0 && getPerfectShotTicks(itemStack) > 0;
            if ((double) f > 0.1) {
                player.m_216990_(ACSoundRegistry.DREADBOW_RELEASE.get());
                ItemStack ammoStack = player.getProjectile(itemStack);
                if (respite && ammoStack.isEmpty()) {
                    ammoStack = new ItemStack(Items.ARROW);
                }
                AbstractArrow abstractArrow = this.createArrow(player, itemStack, ammoStack);
                if (abstractArrow != null) {
                    float maxDist = 128.0F * f;
                    HitResult realHitResult = ProjectileUtil.getHitResultOnViewVector(player, Entity::m_271807_, (double) maxDist);
                    if (realHitResult.getType() == HitResult.Type.MISS) {
                        realHitResult = ProjectileUtil.getHitResultOnViewVector(player, Entity::m_271807_, (double) (f * 42.0F));
                    }
                    BlockPos mutableSkyPos = new BlockPos.MutableBlockPos(realHitResult.getLocation().x, realHitResult.getLocation().y + 1.5, realHitResult.getLocation().z);
                    int maxFallHeight = 15;
                    for (int k = 0; mutableSkyPos.m_123342_() < level.m_151558_() && level.m_46859_(mutableSkyPos) && k < maxFallHeight; k++) {
                        mutableSkyPos = mutableSkyPos.above();
                    }
                    boolean darkArrows = isConvertibleArrow(abstractArrow);
                    int maxArrows = darkArrows ? 30 : 8;
                    abstractArrow.pickup = AbstractArrow.Pickup.ALLOWED;
                    for (int j = 0; (double) j < Math.ceil((double) ((float) maxArrows * f)); j++) {
                        if (darkArrows) {
                            DarkArrowEntity darkArrowEntity = new DarkArrowEntity(level, livingEntity);
                            darkArrowEntity.setShadowArrowDamage(precise ? 2.0F : 3.0F);
                            darkArrowEntity.setPerfectShot(perfectShot);
                            abstractArrow = darkArrowEntity;
                        } else if (perfectShot) {
                            abstractArrow.setBaseDamage(abstractArrow.getBaseDamage() * 2.0);
                        }
                        Vec3 vec3 = mutableSkyPos.getCenter().add((double) (level.random.nextFloat() * 16.0F - 8.0F), (double) (level.random.nextFloat() * 4.0F - 2.0F), (double) (level.random.nextFloat() * 16.0F - 8.0F));
                        for (int clearTries = 0; clearTries < 6 && !level.m_46859_(BlockPos.containing(vec3)) && level.getFluidState(BlockPos.containing(vec3)).isEmpty(); vec3 = mutableSkyPos.getCenter().add((double) (level.random.nextFloat() * 16.0F - 8.0F), (double) (level.random.nextFloat() * 4.0F - 2.0F), (double) (level.random.nextFloat() * 16.0F - 8.0F))) {
                            clearTries++;
                        }
                        if (!level.m_46859_(BlockPos.containing(vec3)) && level.getFluidState(BlockPos.containing(vec3)).isEmpty()) {
                            vec3 = mutableSkyPos.getCenter();
                        }
                        abstractArrow.m_146884_(vec3);
                        Vec3 vec31 = realHitResult.getLocation().subtract(vec3);
                        float randomness = precise ? 0.0F : (darkArrows ? 20.0F : 5.0F) + level.random.nextFloat() * 10.0F;
                        if (!precise && level.random.nextFloat() < 0.25F) {
                            randomness = level.random.nextFloat();
                        }
                        abstractArrow.shoot(vec31.x, vec31.y, vec31.z, 0.5F + 1.5F * level.random.nextFloat(), randomness);
                        level.m_7967_(abstractArrow);
                        abstractArrow = this.createArrow(player, itemStack, ammoStack);
                        abstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    }
                    if (darkArrows) {
                        Vec3 vec3 = realHitResult.getLocation();
                        level.playSound((Player) null, vec3.x, vec3.y, vec3.z, ACSoundRegistry.DREADBOW_RAIN.get(), SoundSource.PLAYERS, 12.0F, 1.0F);
                    }
                    if (!player.isCreative()) {
                        if (!respite) {
                            itemStack.hurtAndBreak(1, player, player1 -> player1.m_21190_(player1.m_7655_()));
                        }
                        if (!respite || !ammoStack.is(Items.ARROW)) {
                            ammoStack.shrink(1);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack itemStack, int timeUsing) {
        super.m_5929_(level, living, itemStack, timeUsing);
        if (living instanceof Player player && itemStack.getEnchantmentLevel(ACEnchantmentRegistry.RELENTLESS_DARKNESS.get()) > 0 && timeUsing % 3 == 0) {
            boolean respite = itemStack.getEnchantmentLevel(ACEnchantmentRegistry.SHADED_RESPITE.get()) > 0 && !DarknessIncarnateEffect.isInLight(living, 11);
            player.m_216990_(ACSoundRegistry.DREADBOW_RELEASE.get());
            ItemStack ammoStack = player.getProjectile(itemStack);
            if (respite && ammoStack.isEmpty()) {
                ammoStack = new ItemStack(Items.ARROW);
            }
            AbstractArrow abstractArrow = this.createArrow(player, itemStack, ammoStack);
            boolean darkArrows = isConvertibleArrow(abstractArrow);
            int maxArrows = darkArrows ? 1 + living.getRandom().nextInt(2) : 1;
            float randomness = 0.5F;
            for (int i = 0; i < maxArrows; i++) {
                abstractArrow.pickup = AbstractArrow.Pickup.ALLOWED;
                if (darkArrows) {
                    DarkArrowEntity darkArrowEntity = new DarkArrowEntity(level, living);
                    darkArrowEntity.setShadowArrowDamage(2.0F);
                    abstractArrow = darkArrowEntity;
                }
                abstractArrow.m_146884_(abstractArrow.m_20182_().add((double) (level.random.nextFloat() - 0.5F), (double) (level.random.nextFloat() - 0.5F), (double) (level.random.nextFloat() - 0.5F)));
                Vec3 vec3 = player.m_20252_(1.0F);
                abstractArrow.shoot(vec3.x, vec3.y, vec3.z, 4.0F + 3.0F * level.random.nextFloat(), randomness);
                randomness += 2.0F;
                level.m_7967_(abstractArrow);
                abstractArrow = this.createArrow(player, itemStack, ammoStack);
                abstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }
            if (!player.isCreative()) {
                if (!respite) {
                    itemStack.hurtAndBreak(1, player, player1 -> player1.m_21190_(player1.m_7655_()));
                }
                if (!respite || !ammoStack.is(Items.ARROW)) {
                    ammoStack.shrink(1);
                }
            }
        }
    }

    private AbstractArrow createArrow(Player player, ItemStack bowStack, ItemStack ammoIn) {
        ItemStack ammo = ammoIn.isEmpty() ? player.getProjectile(bowStack) : ammoIn;
        ArrowItem arrowitem = (ArrowItem) (ammo.getItem() instanceof ArrowItem ? ammo.getItem() : Items.ARROW);
        return arrowitem.createArrow(player.m_9236_(), ammo, player);
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.is(ACItemRegistry.DREADBOW.get()) || !newStack.is(ACItemRegistry.DREADBOW.get());
    }

    public static boolean isConvertibleArrow(Entity arrowEntity) {
        if (arrowEntity instanceof Arrow arrow && arrow.getColor() == -1) {
            return true;
        }
        return false;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return f_43005_;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 64;
    }
}