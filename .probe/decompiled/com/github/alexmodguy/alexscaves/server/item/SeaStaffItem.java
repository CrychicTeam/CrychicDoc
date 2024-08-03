package com.github.alexmodguy.alexscaves.server.item;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.WaterBoltEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import java.util.function.Consumer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class SeaStaffItem extends Item {

    public SeaStaffItem(Item.Properties properties) {
        super(properties);
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) AlexsCaves.PROXY.getISTERProperties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        level.playSound((Player) null, player.m_20185_(), player.m_20186_(), player.m_20189_(), ACSoundRegistry.SEA_STAFF_CAST.get(), SoundSource.PLAYERS, 0.5F, level.getRandom().nextFloat() * 0.45F + 0.75F);
        player.m_6674_(hand);
        float seekAmount = (float) itemstack.getEnchantmentLevel(ACEnchantmentRegistry.SOAK_SEEKING.get());
        if (!level.isClientSide) {
            double dist = 128.0;
            Entity closestValid = null;
            Vec3 playerEyes = player.m_20299_(1.0F);
            HitResult hitresult = level.m_45547_(new ClipContext(playerEyes, playerEyes.add(player.m_20154_().scale(dist)), ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, player));
            if (hitresult instanceof EntityHitResult) {
                Entity entity = ((EntityHitResult) hitresult).getEntity();
                if (!entity.equals(player) && !player.m_7307_(entity) && !entity.isAlliedTo(player) && entity instanceof Mob && player.m_142582_(entity)) {
                    closestValid = entity;
                }
            } else {
                Vec3 at = hitresult.getLocation();
                AABB around = new AABB(at.add(-0.5, -0.5, -0.5), at.add(0.5, 0.5, 0.5)).inflate(15.0);
                for (Entity entity : level.m_45976_(LivingEntity.class, around.inflate(dist))) {
                    if (!entity.equals(player) && !player.m_7307_(entity) && !entity.isAlliedTo(player) && entity instanceof Mob && player.m_142582_(entity) && (closestValid == null || entity.distanceToSqr(at) < closestValid.distanceToSqr(at))) {
                        closestValid = entity;
                    }
                }
            }
            int bolts = itemstack.getEnchantmentLevel(ACEnchantmentRegistry.TRIPLE_SPLASH.get()) > 0 ? 3 : 1;
            for (int i = 0; i < bolts; i++) {
                float shootRot = i == 0 ? 0.0F : (i == 1 ? -50.0F : 50.0F);
                WaterBoltEntity bolt = new WaterBoltEntity(level, player);
                float rot = player.f_20885_ + (float) (hand == InteractionHand.MAIN_HAND ? 45 : -45);
                bolt.m_6034_(player.m_20185_() - (double) player.m_20205_() * 1.1F * (double) Mth.sin(rot * (float) (Math.PI / 180.0)), player.m_20188_() - 0.4F, player.m_20189_() + (double) player.m_20205_() * 1.1F * (double) Mth.cos(rot * (float) (Math.PI / 180.0)));
                bolt.m_37251_(player, player.m_146909_(), player.m_146908_() + shootRot, -20.0F, i > 0 ? 1.0F : 2.0F, 12.0F);
                if (itemstack.getEnchantmentLevel(ACEnchantmentRegistry.ENVELOPING_BUBBLE.get()) > 0) {
                    bolt.setBubbling(player.m_217043_().nextBoolean());
                }
                if (itemstack.getEnchantmentLevel(ACEnchantmentRegistry.BOUNCING_BOLT.get()) > 0) {
                    bolt.ricochet = true;
                }
                bolt.seekAmount = 0.3F + seekAmount * 0.2F;
                if (closestValid != null) {
                    bolt.setArcingTowards(closestValid.getUUID());
                }
                level.m_7967_(bolt);
            }
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemstack.hurtAndBreak(1, player, player1 -> player1.m_21190_(hand));
        }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
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
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean held) {
        boolean var10000;
        label24: {
            super.inventoryTick(stack, level, entity, i, held);
            if (entity instanceof LivingEntity living && living.getUseItem().equals(stack)) {
                var10000 = true;
                break label24;
            }
            var10000 = false;
        }
        boolean using = var10000;
        if (!level.isClientSide && stack.getEnchantmentLevel(ACEnchantmentRegistry.SEAPAIRING.get()) > 0 && !using && level.random.nextFloat() < 0.02F && entity.isInWaterRainOrBubble()) {
            stack.setDamageValue(Math.min(0, stack.getDamageValue() - 1));
        }
    }
}