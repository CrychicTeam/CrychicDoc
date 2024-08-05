package se.mickelus.tetra.items.modular.impl.toolbelt.suspend;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.phys.Vec3;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.impl.toolbelt.ToolbeltHelper;

@ParametersAreNonnullByDefault
public class SuspendEffect {

    private static final Set<MobEffect> enablingEffects = (Set<MobEffect>) Stream.concat(Arrays.stream(BeaconBlockEntity.BEACON_EFFECTS).flatMap(Arrays::stream), Stream.of(MobEffects.CONDUIT_POWER)).collect(Collectors.toSet());

    public static void toggleSuspend(Player entity, boolean toggleOn) {
        if (toggleOn) {
            if (canSuspend(entity)) {
                Vec3 motion = entity.m_20184_();
                entity.m_20334_(motion.x, 0.0, motion.z);
                entity.f_19864_ = true;
                entity.m_7292_(new MobEffectInstance(SuspendPotionEffect.instance, 100, 0, false, false));
            }
        } else {
            entity.m_21195_(SuspendPotionEffect.instance);
        }
    }

    public static boolean canSuspend(Player entity) {
        ItemStack itemStack = ToolbeltHelper.findToolbelt(entity);
        boolean hasEffect = !itemStack.isEmpty() && ((IModularItem) itemStack.getItem()).getEffectLevel(itemStack, ItemEffect.suspendSelf) > 0;
        return hasEffect && entity.m_21220_().stream().filter(MobEffectInstance::m_19571_).map(MobEffectInstance::m_19544_).anyMatch(enablingEffects::contains);
    }
}