package dev.xkmc.l2archery.content.feature.arrow;

import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import dev.xkmc.l2archery.content.feature.types.OnHitFeature;
import dev.xkmc.l2archery.init.data.LangData;
import java.util.List;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class EnderArrowFeature implements OnHitFeature {

    @Override
    public void onHitLivingEntity(GenericArrowEntity genericArrow, LivingEntity target, EntityHitResult hit) {
        Entity owner = genericArrow.m_19749_();
        if (owner != null) {
            Vec3 pos = owner.getPosition(1.0F);
            Vec3 tpos = target.m_20318_(1.0F);
            owner.teleportTo(tpos.x, tpos.y, tpos.z);
            target.m_6021_(pos.x, pos.y, pos.z);
        }
    }

    @Override
    public void onHitBlock(GenericArrowEntity genericArrow, BlockHitResult result) {
        Entity owner = genericArrow.m_19749_();
        if (owner != null) {
            Vec3 pos = result.m_82450_();
            owner.teleportTo(pos.x, pos.y, pos.z);
        }
        genericArrow.m_146870_();
    }

    @Override
    public void addTooltip(List<MutableComponent> list) {
        list.add(LangData.FEATURE_ENDER_ARROW.get());
    }
}