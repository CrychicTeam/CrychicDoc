package net.minecraft.world.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;

public class SuspiciousStewItem extends Item {

    public static final String EFFECTS_TAG = "Effects";

    public static final String EFFECT_ID_TAG = "EffectId";

    public static final String EFFECT_DURATION_TAG = "EffectDuration";

    public static final int DEFAULT_DURATION = 160;

    public SuspiciousStewItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    public static void saveMobEffect(ItemStack itemStack0, MobEffect mobEffect1, int int2) {
        CompoundTag $$3 = itemStack0.getOrCreateTag();
        ListTag $$4 = $$3.getList("Effects", 9);
        CompoundTag $$5 = new CompoundTag();
        $$5.putInt("EffectId", MobEffect.getId(mobEffect1));
        $$5.putInt("EffectDuration", int2);
        $$4.add($$5);
        $$3.put("Effects", $$4);
    }

    private static void listPotionEffects(ItemStack itemStack0, Consumer<MobEffectInstance> consumerMobEffectInstance1) {
        CompoundTag $$2 = itemStack0.getTag();
        if ($$2 != null && $$2.contains("Effects", 9)) {
            ListTag $$3 = $$2.getList("Effects", 10);
            for (int $$4 = 0; $$4 < $$3.size(); $$4++) {
                CompoundTag $$5 = $$3.getCompound($$4);
                int $$6;
                if ($$5.contains("EffectDuration", 99)) {
                    $$6 = $$5.getInt("EffectDuration");
                } else {
                    $$6 = 160;
                }
                MobEffect $$8 = MobEffect.byId($$5.getInt("EffectId"));
                if ($$8 != null) {
                    consumerMobEffectInstance1.accept(new MobEffectInstance($$8, $$6));
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack0, @Nullable Level level1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
        super.appendHoverText(itemStack0, level1, listComponent2, tooltipFlag3);
        if (tooltipFlag3.isCreative()) {
            List<MobEffectInstance> $$4 = new ArrayList();
            listPotionEffects(itemStack0, $$4::add);
            PotionUtils.addPotionTooltip($$4, listComponent2, 1.0F);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack0, Level level1, LivingEntity livingEntity2) {
        ItemStack $$3 = super.finishUsingItem(itemStack0, level1, livingEntity2);
        listPotionEffects($$3, livingEntity2::m_7292_);
        return livingEntity2 instanceof Player && ((Player) livingEntity2).getAbilities().instabuild ? $$3 : new ItemStack(Items.BOWL);
    }
}