package dev.xkmc.l2archery.content.feature.types;

import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import dev.xkmc.l2archery.content.item.GenericArrowItem;
import dev.xkmc.l2archery.content.item.IGeneralConfig;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class DefaultShootFeature implements OnShootFeature {

    public static final DefaultShootFeature INSTANCE = new DefaultShootFeature();

    @Override
    public boolean onShoot(LivingEntity player, Consumer<Consumer<GenericArrowEntity>> consumer) {
        consumer.accept((Consumer) entity -> {
            entity.m_37251_(player, player.m_146909_(), player.m_146908_(), 0.0F, entity.data.power() * entity.data.bow().getConfig().speed(), 1.0F);
            if (entity.data.power() == 1.0F) {
                entity.m_36762_(true);
            }
            HashMap<Enchantment, Integer> map = entity.data.bow().ench();
            int power = (Integer) map.getOrDefault(Enchantments.POWER_ARROWS, 0);
            if (power > 0) {
                entity.m_36781_(entity.m_36789_() + (double) power * 0.5 + 0.5);
            }
            int punch = (Integer) map.getOrDefault(Enchantments.PUNCH_ARROWS, 0);
            if (punch > 0) {
                entity.m_36735_(punch);
            }
            if ((Integer) map.getOrDefault(Enchantments.FLAMING_ARROWS, 0) > 0) {
                entity.m_20254_(100);
            }
            if (entity.data.no_consume()) {
                entity.f_36705_ = AbstractArrow.Pickup.CREATIVE_ONLY;
            }
            IGeneralConfig config = entity.data.arrow().item() instanceof GenericArrowItem gen ? gen.getConfig() : null;
            int knock = entity.m_150123_() + entity.data.bow().getConfig().punch() + (config == null ? 0 : config.punch());
            double damage = entity.m_36789_() + (double) entity.data.bow().getConfig().damage() + (double) (config == null ? 0.0F : config.damage());
            entity.m_36735_(Math.max(0, knock));
            entity.m_36781_(Math.max(damage, 0.5));
        });
        return true;
    }

    @Override
    public void addTooltip(List<MutableComponent> list) {
    }
}