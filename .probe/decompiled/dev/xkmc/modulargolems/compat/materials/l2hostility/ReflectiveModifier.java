package dev.xkmc.modulargolems.compat.materials.l2hostility;

import dev.xkmc.l2hostility.content.traits.common.ReflectTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ReflectiveModifier extends GolemModifier {

    public ReflectiveModifier(StatFilterType type, int maxLevel) {
        super(type, maxLevel);
    }

    @Override
    public void onHurt(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
        ((ReflectTrait) LHTraits.REFLECT.get()).onHurtByOthers(level, entity, event);
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        List<MutableComponent> ans = new ArrayList();
        ans.add(Component.translatable(((ReflectTrait) LHTraits.REFLECT.get()).getDescriptionId() + ".desc", Component.literal((int) Math.round(100.0 * (1.0 + (double) v * LHConfig.COMMON.reflectFactor.get())) + "").withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GREEN));
        return ans;
    }
}