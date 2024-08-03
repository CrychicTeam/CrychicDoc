package se.mickelus.tetra.effect;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.items.modular.IModularItem;

@ParametersAreNonnullByDefault
public class HauntedEffect {

    public static void perform(LivingEntity entity, ItemStack itemStack, double multiplier) {
        if (!entity.m_9236_().isClientSide) {
            double effectProbability = (double) EffectHelper.getEffectEfficiency(itemStack, ItemEffect.haunted);
            if (effectProbability > 0.0 && entity.getRandom().nextDouble() < effectProbability * multiplier) {
                int effectLevel = EffectHelper.getEffectLevel(itemStack, ItemEffect.haunted);
                Vex vex = EntityType.VEX.create(entity.m_9236_());
                vex.setLimitedLife(effectLevel * 20);
                vex.m_7678_(entity.m_20185_(), entity.m_20186_() + 1.0, entity.m_20189_(), entity.m_146908_(), 0.0F);
                vex.m_21008_(InteractionHand.MAIN_HAND, itemStack.copy());
                vex.m_21409_(EquipmentSlot.MAINHAND, 0.0F);
                vex.m_7292_(new MobEffectInstance(MobEffects.INVISIBILITY, 2000 + effectLevel * 20));
                entity.m_9236_().m_7967_(vex);
                ((Stream) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> Arrays.stream(item.getMajorModules(itemStack))).orElse(Stream.empty())).filter(Objects::nonNull).filter(module -> module.getImprovement(itemStack, "destabilized/haunted") != null).findAny().ifPresent(module -> {
                    int level = module.getImprovementLevel(itemStack, "destabilized/haunted");
                    if (level > 0) {
                        module.addImprovement(itemStack, "destabilized/haunted", level - 1);
                    } else {
                        module.removeImprovement(itemStack, "destabilized/haunted");
                    }
                });
                entity.m_9236_().playSound(null, entity.m_20183_(), SoundEvents.WITCH_AMBIENT, SoundSource.PLAYERS, 2.0F, 2.0F);
            }
        }
    }
}