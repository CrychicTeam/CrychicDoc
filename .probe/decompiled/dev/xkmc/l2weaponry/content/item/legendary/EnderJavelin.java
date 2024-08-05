package dev.xkmc.l2weaponry.content.item.legendary;

import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.l2weaponry.content.entity.BaseThrownWeaponEntity;
import dev.xkmc.l2weaponry.content.entity.JavelinEntity;
import dev.xkmc.l2weaponry.content.item.types.JavelinItem;
import dev.xkmc.l2weaponry.init.data.LangData;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class EnderJavelin extends JavelinItem implements LegendaryWeapon {

    public EnderJavelin(Tier tier, int damage, float speed, Item.Properties prop, ExtraToolConfig config) {
        super(tier, damage, speed, prop, config);
    }

    @Override
    public JavelinEntity getProjectile(Level level, LivingEntity player, ItemStack stack, int slot) {
        JavelinEntity entity = super.getProjectile(level, player, stack, slot);
        entity.m_20242_(true);
        return entity;
    }

    @Override
    public void onHitBlock(BaseThrownWeaponEntity<?> entity, ItemStack item) {
        if (!entity.m_9236_().isClientSide) {
            if (entity.m_19749_() instanceof Player player) {
                if (player.m_9236_() == entity.m_9236_()) {
                    if (player.m_6084_()) {
                        player.m_6021_(entity.m_20185_(), entity.m_20186_(), entity.m_20189_());
                        player.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (selected && !level.isClientSide && entity instanceof LivingEntity le && le.m_20182_().y() < (double) level.m_141937_()) {
            EffectUtil.refreshEffect(le, new MobEffectInstance(MobEffects.SLOW_FALLING, 219), EffectUtil.AddReason.SELF, le);
            EffectUtil.refreshEffect(le, new MobEffectInstance(MobEffects.LEVITATION, 119), EffectUtil.AddReason.SELF, le);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> list, TooltipFlag pIsAdvanced) {
        list.add(LangData.ENDER_JAVELIN.get());
    }
}