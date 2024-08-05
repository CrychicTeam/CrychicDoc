package dev.xkmc.l2artifacts.content.effects.v3;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2library.base.effects.EffectUtil;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class VampireBurn extends SetEffect {

    private final LinearFuncEntry light;

    public VampireBurn(LinearFuncEntry lightLow) {
        super(0);
        this.light = lightLow;
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        int lo = (int) Math.round(this.light.getFromRank(rank));
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", lo));
    }

    @Override
    public void tick(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
        if (enabled) {
            if (!player.m_9236_().isClientSide()) {
                int light = PlayerLight.playerUnderSun(player);
                Level level = player.m_9236_();
                if ((double) light > this.light.getFromRank(rank) && level.m_45527_(player.m_20097_()) && level.isDay() && !fireImmune(player) && player.m_20094_() < 40) {
                    player.m_7311_(60);
                }
                if ((double) light <= this.light.getFromRank(rank)) {
                    EffectUtil.refreshEffect(player, new MobEffectInstance(MobEffects.NIGHT_VISION, 400), EffectUtil.AddReason.SELF, player);
                }
            }
        }
    }

    private static boolean fireImmune(LivingEntity entity) {
        return entity.m_20071_() || entity.f_146808_ || entity.f_146809_ || entity.m_5825_() || entity.hasEffect(MobEffects.FIRE_RESISTANCE);
    }
}