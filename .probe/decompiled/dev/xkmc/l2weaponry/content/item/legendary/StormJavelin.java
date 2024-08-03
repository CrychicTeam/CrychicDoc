package dev.xkmc.l2weaponry.content.item.legendary;

import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2weaponry.content.entity.BaseThrownWeaponEntity;
import dev.xkmc.l2weaponry.content.item.types.JavelinItem;
import dev.xkmc.l2weaponry.init.data.LangData;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class StormJavelin extends JavelinItem implements LegendaryWeapon {

    public StormJavelin(Tier tier, int damage, float speed, Item.Properties prop, ExtraToolConfig config) {
        super(tier, damage, speed, prop, config);
    }

    @Override
    public boolean causeThunder(BaseThrownWeaponEntity<?> entity) {
        return true;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity user) {
        super.m_7579_(stack, target, user);
        if (user.m_9236_().isClientSide) {
            return true;
        } else {
            BlockPos blockpos = target.m_20183_();
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(user.m_9236_());
            assert bolt != null;
            bolt.m_20219_(Vec3.atBottomCenterOf(blockpos));
            bolt.setCause(user instanceof ServerPlayer serverPlayer ? serverPlayer : null);
            user.m_9236_().m_7967_(bolt);
            user.m_5496_(SoundEvents.TRIDENT_THUNDER, 5.0F, 1.0F);
            return true;
        }
    }

    @Override
    public boolean isImmuneTo(DamageSource source) {
        return source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypeTags.IS_LIGHTNING);
    }

    @Override
    protected boolean canSweep() {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> list, TooltipFlag pIsAdvanced) {
        list.add(LangData.STORM_JAVELIN.get());
    }
}