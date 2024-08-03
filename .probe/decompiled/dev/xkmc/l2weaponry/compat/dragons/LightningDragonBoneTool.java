package dev.xkmc.l2weaponry.compat.dragons;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class LightningDragonBoneTool extends ExtraToolConfig {

    public void onHit(ItemStack stack, LivingEntity target, LivingEntity user) {
        super.onHit(stack, target, user);
        if (IafConfig.dragonWeaponLightningAbility) {
            boolean flag = !(user instanceof Player) || !((double) user.attackAnim > 0.2);
            if (!user.m_9236_().isClientSide && flag) {
                LightningBolt entity = EntityType.LIGHTNING_BOLT.create(target.m_9236_());
                assert entity != null;
                entity.m_20049_("l2weaponry:lightning");
                entity.m_20219_(target.m_20182_());
                if (user instanceof ServerPlayer sp) {
                    entity.setCause(sp);
                }
                if (!target.m_9236_().isClientSide) {
                    target.m_9236_().m_7967_(entity);
                }
            }
            if (target instanceof EntityFireDragon || target instanceof EntityIceDragon) {
                target.hurt(user.m_9236_().damageSources().lightningBolt(), 9.5F);
            }
            target.knockback(1.0, user.m_20185_() - target.m_20185_(), user.m_20189_() - target.m_20189_());
        }
    }

    public void addTooltip(ItemStack stack, List<Component> list) {
        list.add(Component.translatable("dragon_sword_lightning.hurt1").withStyle(ChatFormatting.GREEN));
        if (IafConfig.dragonWeaponLightningAbility) {
            list.add(Component.translatable("dragon_sword_lightning.hurt2").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }
}