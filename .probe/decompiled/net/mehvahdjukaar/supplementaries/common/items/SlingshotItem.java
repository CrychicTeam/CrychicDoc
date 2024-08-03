package net.mehvahdjukaar.supplementaries.common.items;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.mehvahdjukaar.moonlight.api.item.IFirstPersonAnimationProvider;
import net.mehvahdjukaar.moonlight.api.item.IThirdPersonAnimationProvider;
import net.mehvahdjukaar.moonlight.api.item.additional_placements.AdditionalItemPlacementsAPI;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.mehvahdjukaar.supplementaries.common.entities.SlingshotProjectileEntity;
import net.mehvahdjukaar.supplementaries.common.events.overrides.InteractEventsHandler;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModSounds;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.item.ThrowablePotionItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class SlingshotItem extends ProjectileWeaponItem implements Vanishable, IFirstPersonAnimationProvider, IThirdPersonAnimationProvider {

    public SlingshotItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            ItemStack projectileStack = player.getProjectile(stack);
            if (!projectileStack.isEmpty() && this.getAllSupportedProjectiles().test(projectileStack)) {
                float power = this.getPowerForTime(stack, (float) timeLeft);
                if ((double) power >= 0.085) {
                    int maxProjectiles = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, stack) > 0 ? 3 : 1;
                    List<ItemStack> projectiles = new ArrayList();
                    for (int p = 0; p < maxProjectiles && this.getAllSupportedProjectiles().test(projectileStack); p++) {
                        projectiles.add(projectileStack.copy());
                        if (!player.getAbilities().instabuild) {
                            projectileStack.shrink(1);
                            if (projectileStack.isEmpty()) {
                                player.getInventory().removeItem(projectileStack);
                            }
                        }
                        projectileStack = player.getProjectile(stack);
                    }
                    if (!world.isClientSide) {
                        float[] pitches = getShotPitches(world.getRandom());
                        int count = projectiles.size();
                        float angle = 10.0F;
                        for (int j = 0; j < count; j++) {
                            boolean stasis = EnchantmentHelper.getItemEnchantmentLevel((Enchantment) ModRegistry.STASIS_ENCHANTMENT.get(), stack) != 0;
                            InteractionHand hand = player.m_7655_();
                            power = (float) ((double) power * ((Double) CommonConfigs.Tools.SLINGSHOT_RANGE.get() + (stasis ? 0.5 : 0.0)) * 1.1);
                            shootProjectile(world, entity, hand, stack, (ItemStack) projectiles.get(j), count == 1 ? 1.0F : pitches[j], power, 1.0F, angle * ((float) j - (float) (count - 1) / 2.0F));
                        }
                    }
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    private static void shootProjectile(Level level, LivingEntity entity, InteractionHand hand, ItemStack stack, ItemStack projectileStack, float soundPitch, float power, float accuracy, float yaw) {
        projectileStack.setCount(1);
        SlingshotProjectileEntity projectile = new SlingshotProjectileEntity(entity, level, projectileStack, stack);
        Vec3 vector3d1 = entity.m_20289_(1.0F);
        Quaternionf quaternionf = new Quaternionf().setAngleAxis((double) (yaw * (float) (Math.PI / 180.0)), vector3d1.x(), vector3d1.y(), vector3d1.z());
        Vector3f vector3f = entity.m_20252_(1.0F).toVector3f();
        vector3f.rotate(quaternionf);
        projectile.m_6686_((double) vector3f.x(), (double) vector3f.y(), (double) vector3f.z(), power, accuracy);
        stack.hurtAndBreak(1, entity, p -> p.broadcastBreakEvent(hand));
        level.m_7967_(projectile);
        level.playSound(null, entity, (SoundEvent) ModSounds.SLINGSHOT_SHOOT.get(), SoundSource.PLAYERS, 1.0F, soundPitch * (1.0F / (level.random.nextFloat() * 0.3F + 0.9F) + power * 0.6F));
    }

    private static float[] getShotPitches(RandomSource random) {
        boolean flag = random.nextBoolean();
        return new float[] { getRandomShotPitch(random, flag), 1.0F, getRandomShotPitch(random, !flag) };
    }

    private static float getRandomShotPitch(RandomSource random, boolean left) {
        float f = left ? 0.63F : 0.43F;
        return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + f;
    }

    public float getPowerForTime(ItemStack stack, float timeLeft) {
        float useTime = (float) this.getUseDuration(stack) - timeLeft;
        float f = useTime / (float) getChargeDuration(stack);
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public static int getChargeDuration(ItemStack stack) {
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
        int maxCharge = (Integer) CommonConfigs.Tools.SLINGSHOT_CHARGE.get();
        return i == 0 ? maxCharge : maxCharge - maxCharge / 4 * i;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        boolean flag = this.getAllSupportedProjectiles().test(player.getProjectile(itemstack));
        if (!flag) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.m_6672_(hand);
            player.m_9236_().playSound(player, player, this.getChargeSound(itemstack), SoundSource.PLAYERS, 1.0F, 1.0F * (1.0F / (world.random.nextFloat() * 0.3F + 0.9F)));
            return InteractionResultHolder.consume(itemstack);
        }
    }

    public SoundEvent getChargeSound(ItemStack stack) {
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
        return switch(i) {
            case 0 ->
                (SoundEvent) ModSounds.SLINGSHOT_CHARGE_0.get();
            case 1 ->
                (SoundEvent) ModSounds.SLINGSHOT_CHARGE_1.get();
            case 2 ->
                (SoundEvent) ModSounds.SLINGSHOT_CHARGE_2.get();
            default ->
                (SoundEvent) ModSounds.SLINGSHOT_CHARGE_3.get();
        };
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return s -> {
            Item i = s.getItem();
            if (i instanceof ThrowablePotionItem) {
                return (Boolean) CommonConfigs.Tools.SLINGSHOT_POTIONS.get();
            } else if (i instanceof BombItem) {
                return (Boolean) CommonConfigs.Tools.SLINGSHOT_BOMBS.get();
            } else if (i instanceof SnowballItem) {
                return (Boolean) CommonConfigs.Tools.SLINGSHOT_SNOWBALL.get();
            } else if (i instanceof EnderpearlItem) {
                return (Boolean) CommonConfigs.Tools.SLINGSHOT_ENDERPEARLS.get();
            } else {
                return i instanceof FireChargeItem ? (Boolean) CommonConfigs.Tools.SLINGSHOT_FIRECHARGE.get() : !(i instanceof DispensibleContainerItem) && !s.is(ModTags.SLINGSHOT_BLACKLIST) && i instanceof BlockItem || AdditionalItemPlacementsAPI.hasBehavior(i) || InteractEventsHandler.hasBlockPlacementAssociated(i);
            }
        };
    }

    @Override
    public int getDefaultProjectileRange() {
        return 10;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public <T extends LivingEntity> boolean poseLeftArm(ItemStack stack, HumanoidModel<T> model, T entity, HumanoidArm mainHand) {
        if (entity.getUseItemRemainingTicks() > 0 && entity.getUseItem().getItem() == this) {
            model.leftArm.yRot = MthUtils.wrapRad(0.1F + model.head.yRot);
            model.leftArm.xRot = MthUtils.wrapRad((float) (-Math.PI / 2) + model.head.xRot);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public <T extends LivingEntity> boolean poseRightArm(ItemStack stack, HumanoidModel<T> model, T entity, HumanoidArm mainHand) {
        if (entity.getUseItemRemainingTicks() > 0 && entity.getUseItem().getItem() == this) {
            model.rightArm.yRot = MthUtils.wrapRad(-0.1F + model.head.yRot);
            model.rightArm.xRot = MthUtils.wrapRad((float) (-Math.PI / 2) + model.head.xRot);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void animateItemFirstPerson(LivingEntity entity, ItemStack stack, InteractionHand hand, PoseStack matrixStack, float partialTicks, float pitch, float attackAnim, float handHeight) {
        if (entity.isUsingItem() && entity.getUseItemRemainingTicks() > 0 && entity.getUsedItemHand() == hand) {
            float timeLeft = (float) stack.getUseDuration() - ((float) entity.getUseItemRemainingTicks() - partialTicks + 1.0F);
            float f12 = this.getPowerForTime(stack, timeLeft);
            if (f12 > 0.1F) {
                float f15 = Mth.sin((timeLeft - 0.1F) * 1.3F);
                float f18 = f12 - 0.1F;
                float f20 = f15 * f18;
                matrixStack.translate(0.0F, f20 * 0.004F, 0.0F);
            }
            matrixStack.translate(0.0F, 0.0F, f12 * 0.04F);
            matrixStack.scale(1.0F, 1.0F, 1.0F + f12 * 0.2F);
        }
    }

    public static void animateCrossbowCharge(ModelPart offHand, ModelPart mainHand, LivingEntity entity, boolean right) {
        offHand.xRot = mainHand.xRot;
        float f = (float) CrossbowItem.getChargeDuration(entity.getUseItem());
        float f1 = Mth.clamp((float) entity.getTicksUsingItem(), 0.0F, f);
        float f2 = f1 / f;
        offHand.yRot = Mth.lerp(f2, 0.4F, 0.85F) * (float) (right ? 1 : -1);
        offHand.xRot = Mth.lerp(f2, offHand.xRot, (float) (-Math.PI / 2));
    }
}