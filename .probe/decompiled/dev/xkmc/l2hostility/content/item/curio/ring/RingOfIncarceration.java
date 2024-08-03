package dev.xkmc.l2hostility.content.item.curio.ring;

import dev.xkmc.l2complements.content.item.curios.CurioItem;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2library.base.effects.EffectUtil;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class RingOfIncarceration extends CurioItem implements ICurioItem {

    public RingOfIncarceration(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(LangData.ITEM_RING_INCARCERATION.get().withStyle(ChatFormatting.GOLD));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();
        if (wearer != null) {
            if (wearer.m_6144_()) {
                Attribute reach = ForgeMod.ENTITY_REACH.get();
                AttributeInstance attr = wearer.getAttribute(reach);
                double r = attr == null ? reach.getDefaultValue() : attr.getValue();
                for (LivingEntity e : wearer.m_9236_().getEntities(EntityTypeTest.forClass(LivingEntity.class), wearer.m_20191_().inflate(r), ex -> (double) wearer.m_20270_(ex) < r)) {
                    EffectUtil.refreshEffect(e, new MobEffectInstance((MobEffect) LCEffects.STONE_CAGE.get(), 40, 0, true, true), EffectUtil.AddReason.NONE, wearer);
                }
            }
        }
    }
}