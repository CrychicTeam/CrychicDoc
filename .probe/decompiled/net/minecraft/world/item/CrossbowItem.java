package net.minecraft.world.item;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CrossbowItem extends ProjectileWeaponItem implements Vanishable {

    private static final String TAG_CHARGED = "Charged";

    private static final String TAG_CHARGED_PROJECTILES = "ChargedProjectiles";

    private static final int MAX_CHARGE_DURATION = 25;

    public static final int DEFAULT_RANGE = 8;

    private boolean startSoundPlayed = false;

    private boolean midLoadSoundPlayed = false;

    private static final float START_SOUND_PERCENT = 0.2F;

    private static final float MID_SOUND_PERCENT = 0.5F;

    private static final float ARROW_POWER = 3.15F;

    private static final float FIREWORK_POWER = 1.6F;

    public CrossbowItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return f_43006_;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return f_43005_;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        ItemStack $$3 = player1.m_21120_(interactionHand2);
        if (isCharged($$3)) {
            performShooting(level0, player1, interactionHand2, $$3, getShootingPower($$3), 1.0F);
            setCharged($$3, false);
            return InteractionResultHolder.consume($$3);
        } else if (!player1.getProjectile($$3).isEmpty()) {
            if (!isCharged($$3)) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
                player1.m_6672_(interactionHand2);
            }
            return InteractionResultHolder.consume($$3);
        } else {
            return InteractionResultHolder.fail($$3);
        }
    }

    private static float getShootingPower(ItemStack itemStack0) {
        return containsChargedProjectile(itemStack0, Items.FIREWORK_ROCKET) ? 1.6F : 3.15F;
    }

    @Override
    public void releaseUsing(ItemStack itemStack0, Level level1, LivingEntity livingEntity2, int int3) {
        int $$4 = this.getUseDuration(itemStack0) - int3;
        float $$5 = getPowerForTime($$4, itemStack0);
        if ($$5 >= 1.0F && !isCharged(itemStack0) && tryLoadProjectiles(livingEntity2, itemStack0)) {
            setCharged(itemStack0, true);
            SoundSource $$6 = livingEntity2 instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
            level1.playSound(null, livingEntity2.m_20185_(), livingEntity2.m_20186_(), livingEntity2.m_20189_(), SoundEvents.CROSSBOW_LOADING_END, $$6, 1.0F, 1.0F / (level1.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
        }
    }

    private static boolean tryLoadProjectiles(LivingEntity livingEntity0, ItemStack itemStack1) {
        int $$2 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, itemStack1);
        int $$3 = $$2 == 0 ? 1 : 3;
        boolean $$4 = livingEntity0 instanceof Player && ((Player) livingEntity0).getAbilities().instabuild;
        ItemStack $$5 = livingEntity0.getProjectile(itemStack1);
        ItemStack $$6 = $$5.copy();
        for (int $$7 = 0; $$7 < $$3; $$7++) {
            if ($$7 > 0) {
                $$5 = $$6.copy();
            }
            if ($$5.isEmpty() && $$4) {
                $$5 = new ItemStack(Items.ARROW);
                $$6 = $$5.copy();
            }
            if (!loadProjectile(livingEntity0, itemStack1, $$5, $$7 > 0, $$4)) {
                return false;
            }
        }
        return true;
    }

    private static boolean loadProjectile(LivingEntity livingEntity0, ItemStack itemStack1, ItemStack itemStack2, boolean boolean3, boolean boolean4) {
        if (itemStack2.isEmpty()) {
            return false;
        } else {
            boolean $$5 = boolean4 && itemStack2.getItem() instanceof ArrowItem;
            ItemStack $$6;
            if (!$$5 && !boolean4 && !boolean3) {
                $$6 = itemStack2.split(1);
                if (itemStack2.isEmpty() && livingEntity0 instanceof Player) {
                    ((Player) livingEntity0).getInventory().removeItem(itemStack2);
                }
            } else {
                $$6 = itemStack2.copy();
            }
            addChargedProjectile(itemStack1, $$6);
            return true;
        }
    }

    public static boolean isCharged(ItemStack itemStack0) {
        CompoundTag $$1 = itemStack0.getTag();
        return $$1 != null && $$1.getBoolean("Charged");
    }

    public static void setCharged(ItemStack itemStack0, boolean boolean1) {
        CompoundTag $$2 = itemStack0.getOrCreateTag();
        $$2.putBoolean("Charged", boolean1);
    }

    private static void addChargedProjectile(ItemStack itemStack0, ItemStack itemStack1) {
        CompoundTag $$2 = itemStack0.getOrCreateTag();
        ListTag $$3;
        if ($$2.contains("ChargedProjectiles", 9)) {
            $$3 = $$2.getList("ChargedProjectiles", 10);
        } else {
            $$3 = new ListTag();
        }
        CompoundTag $$5 = new CompoundTag();
        itemStack1.save($$5);
        $$3.add($$5);
        $$2.put("ChargedProjectiles", $$3);
    }

    private static List<ItemStack> getChargedProjectiles(ItemStack itemStack0) {
        List<ItemStack> $$1 = Lists.newArrayList();
        CompoundTag $$2 = itemStack0.getTag();
        if ($$2 != null && $$2.contains("ChargedProjectiles", 9)) {
            ListTag $$3 = $$2.getList("ChargedProjectiles", 10);
            if ($$3 != null) {
                for (int $$4 = 0; $$4 < $$3.size(); $$4++) {
                    CompoundTag $$5 = $$3.getCompound($$4);
                    $$1.add(ItemStack.of($$5));
                }
            }
        }
        return $$1;
    }

    private static void clearChargedProjectiles(ItemStack itemStack0) {
        CompoundTag $$1 = itemStack0.getTag();
        if ($$1 != null) {
            ListTag $$2 = $$1.getList("ChargedProjectiles", 9);
            $$2.clear();
            $$1.put("ChargedProjectiles", $$2);
        }
    }

    public static boolean containsChargedProjectile(ItemStack itemStack0, Item item1) {
        return getChargedProjectiles(itemStack0).stream().anyMatch(p_40870_ -> p_40870_.is(item1));
    }

    private static void shootProjectile(Level level0, LivingEntity livingEntity1, InteractionHand interactionHand2, ItemStack itemStack3, ItemStack itemStack4, float float5, boolean boolean6, float float7, float float8, float float9) {
        if (!level0.isClientSide) {
            boolean $$10 = itemStack4.is(Items.FIREWORK_ROCKET);
            Projectile $$11;
            if ($$10) {
                $$11 = new FireworkRocketEntity(level0, itemStack4, livingEntity1, livingEntity1.m_20185_(), livingEntity1.m_20188_() - 0.15F, livingEntity1.m_20189_(), true);
            } else {
                $$11 = getArrow(level0, livingEntity1, itemStack3, itemStack4);
                if (boolean6 || float9 != 0.0F) {
                    ((AbstractArrow) $$11).pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }
            }
            if (livingEntity1 instanceof CrossbowAttackMob $$13) {
                $$13.shootCrossbowProjectile($$13.getTarget(), itemStack3, $$11, float9);
            } else {
                Vec3 $$14 = livingEntity1.m_20289_(1.0F);
                Quaternionf $$15 = new Quaternionf().setAngleAxis((double) (float9 * (float) (Math.PI / 180.0)), $$14.x, $$14.y, $$14.z);
                Vec3 $$16 = livingEntity1.m_20252_(1.0F);
                Vector3f $$17 = $$16.toVector3f().rotate($$15);
                $$11.shoot((double) $$17.x(), (double) $$17.y(), (double) $$17.z(), float7, float8);
            }
            itemStack3.hurtAndBreak($$10 ? 3 : 1, livingEntity1, p_40858_ -> p_40858_.broadcastBreakEvent(interactionHand2));
            level0.m_7967_($$11);
            level0.playSound(null, livingEntity1.m_20185_(), livingEntity1.m_20186_(), livingEntity1.m_20189_(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, float5);
        }
    }

    private static AbstractArrow getArrow(Level level0, LivingEntity livingEntity1, ItemStack itemStack2, ItemStack itemStack3) {
        ArrowItem $$4 = (ArrowItem) (itemStack3.getItem() instanceof ArrowItem ? itemStack3.getItem() : Items.ARROW);
        AbstractArrow $$5 = $$4.createArrow(level0, itemStack3, livingEntity1);
        if (livingEntity1 instanceof Player) {
            $$5.setCritArrow(true);
        }
        $$5.setSoundEvent(SoundEvents.CROSSBOW_HIT);
        $$5.setShotFromCrossbow(true);
        int $$6 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, itemStack2);
        if ($$6 > 0) {
            $$5.setPierceLevel((byte) $$6);
        }
        return $$5;
    }

    public static void performShooting(Level level0, LivingEntity livingEntity1, InteractionHand interactionHand2, ItemStack itemStack3, float float4, float float5) {
        List<ItemStack> $$6 = getChargedProjectiles(itemStack3);
        float[] $$7 = getShotPitches(livingEntity1.getRandom());
        for (int $$8 = 0; $$8 < $$6.size(); $$8++) {
            ItemStack $$9 = (ItemStack) $$6.get($$8);
            boolean $$10 = livingEntity1 instanceof Player && ((Player) livingEntity1).getAbilities().instabuild;
            if (!$$9.isEmpty()) {
                if ($$8 == 0) {
                    shootProjectile(level0, livingEntity1, interactionHand2, itemStack3, $$9, $$7[$$8], $$10, float4, float5, 0.0F);
                } else if ($$8 == 1) {
                    shootProjectile(level0, livingEntity1, interactionHand2, itemStack3, $$9, $$7[$$8], $$10, float4, float5, -10.0F);
                } else if ($$8 == 2) {
                    shootProjectile(level0, livingEntity1, interactionHand2, itemStack3, $$9, $$7[$$8], $$10, float4, float5, 10.0F);
                }
            }
        }
        onCrossbowShot(level0, livingEntity1, itemStack3);
    }

    private static float[] getShotPitches(RandomSource randomSource0) {
        boolean $$1 = randomSource0.nextBoolean();
        return new float[] { 1.0F, getRandomShotPitch($$1, randomSource0), getRandomShotPitch(!$$1, randomSource0) };
    }

    private static float getRandomShotPitch(boolean boolean0, RandomSource randomSource1) {
        float $$2 = boolean0 ? 0.63F : 0.43F;
        return 1.0F / (randomSource1.nextFloat() * 0.5F + 1.8F) + $$2;
    }

    private static void onCrossbowShot(Level level0, LivingEntity livingEntity1, ItemStack itemStack2) {
        if (livingEntity1 instanceof ServerPlayer $$3) {
            if (!level0.isClientSide) {
                CriteriaTriggers.SHOT_CROSSBOW.trigger($$3, itemStack2);
            }
            $$3.m_36246_(Stats.ITEM_USED.get(itemStack2.getItem()));
        }
        clearChargedProjectiles(itemStack2);
    }

    @Override
    public void onUseTick(Level level0, LivingEntity livingEntity1, ItemStack itemStack2, int int3) {
        if (!level0.isClientSide) {
            int $$4 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, itemStack2);
            SoundEvent $$5 = this.getStartSound($$4);
            SoundEvent $$6 = $$4 == 0 ? SoundEvents.CROSSBOW_LOADING_MIDDLE : null;
            float $$7 = (float) (itemStack2.getUseDuration() - int3) / (float) getChargeDuration(itemStack2);
            if ($$7 < 0.2F) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
            }
            if ($$7 >= 0.2F && !this.startSoundPlayed) {
                this.startSoundPlayed = true;
                level0.playSound(null, livingEntity1.m_20185_(), livingEntity1.m_20186_(), livingEntity1.m_20189_(), $$5, SoundSource.PLAYERS, 0.5F, 1.0F);
            }
            if ($$7 >= 0.5F && $$6 != null && !this.midLoadSoundPlayed) {
                this.midLoadSoundPlayed = true;
                level0.playSound(null, livingEntity1.m_20185_(), livingEntity1.m_20186_(), livingEntity1.m_20189_(), $$6, SoundSource.PLAYERS, 0.5F, 1.0F);
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack itemStack0) {
        return getChargeDuration(itemStack0) + 3;
    }

    public static int getChargeDuration(ItemStack itemStack0) {
        int $$1 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, itemStack0);
        return $$1 == 0 ? 25 : 25 - 5 * $$1;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack0) {
        return UseAnim.CROSSBOW;
    }

    private SoundEvent getStartSound(int int0) {
        switch(int0) {
            case 1:
                return SoundEvents.CROSSBOW_QUICK_CHARGE_1;
            case 2:
                return SoundEvents.CROSSBOW_QUICK_CHARGE_2;
            case 3:
                return SoundEvents.CROSSBOW_QUICK_CHARGE_3;
            default:
                return SoundEvents.CROSSBOW_LOADING_START;
        }
    }

    private static float getPowerForTime(int int0, ItemStack itemStack1) {
        float $$2 = (float) int0 / (float) getChargeDuration(itemStack1);
        if ($$2 > 1.0F) {
            $$2 = 1.0F;
        }
        return $$2;
    }

    @Override
    public void appendHoverText(ItemStack itemStack0, @Nullable Level level1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
        List<ItemStack> $$4 = getChargedProjectiles(itemStack0);
        if (isCharged(itemStack0) && !$$4.isEmpty()) {
            ItemStack $$5 = (ItemStack) $$4.get(0);
            listComponent2.add(Component.translatable("item.minecraft.crossbow.projectile").append(CommonComponents.SPACE).append($$5.getDisplayName()));
            if (tooltipFlag3.isAdvanced() && $$5.is(Items.FIREWORK_ROCKET)) {
                List<Component> $$6 = Lists.newArrayList();
                Items.FIREWORK_ROCKET.appendHoverText($$5, level1, $$6, tooltipFlag3);
                if (!$$6.isEmpty()) {
                    for (int $$7 = 0; $$7 < $$6.size(); $$7++) {
                        $$6.set($$7, Component.literal("  ").append((Component) $$6.get($$7)).withStyle(ChatFormatting.GRAY));
                    }
                    listComponent2.addAll($$6);
                }
            }
        }
    }

    @Override
    public boolean useOnRelease(ItemStack itemStack0) {
        return itemStack0.is(this);
    }

    @Override
    public int getDefaultProjectileRange() {
        return 8;
    }
}