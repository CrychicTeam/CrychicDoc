package dev.xkmc.l2hostility.content.traits.legendary;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.compat.curios.EntitySlotAccess;
import dev.xkmc.l2hostility.content.item.traits.SealedItem;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class RagnarokTrait extends LegendaryTrait {

    private static boolean allowSeal(EntitySlotAccess access) {
        ItemStack stack = access.get();
        if (stack.isEmpty()) {
            return false;
        } else if (stack.is((Item) LHItems.SEAL.get())) {
            return false;
        } else if (stack.is(LHTagGen.NO_SEAL)) {
            return false;
        } else {
            if (!LHConfig.COMMON.ragnarokSealBackpack.get()) {
                ResourceLocation rl = ForgeRegistries.ITEMS.getKey(stack.getItem());
                if (rl == null) {
                    return false;
                }
                if (rl.toString().contains("backpack")) {
                    return false;
                }
            }
            return !LHConfig.COMMON.ragnarokSealSlotAdder.get() ? !CurioCompat.isSlotAdder(access) : true;
        }
    }

    public RagnarokTrait(ChatFormatting format) {
        super(format);
    }

    @Override
    public void postHurtImpl(int level, LivingEntity attacker, LivingEntity target) {
        List<EntitySlotAccess> list = new ArrayList(CurioCompat.getItemAccess(target).stream().filter(RagnarokTrait::allowSeal).toList());
        int count = Math.min(level, list.size());
        int time = LHConfig.COMMON.ragnarokTime.get() * level;
        for (int i = 0; i < count; i++) {
            int index = attacker.getRandom().nextInt(list.size());
            EntitySlotAccess slot = (EntitySlotAccess) list.remove(index);
            slot.modify(e -> SealedItem.sealItem(e, time));
        }
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(this.getDescriptionId() + ".desc", this.mapLevel(i -> Component.literal(i + "").withStyle(ChatFormatting.AQUA)), this.mapLevel(i -> Component.literal(Math.round((float) (LHConfig.COMMON.ragnarokTime.get() * i) / 20.0F) + "").withStyle(ChatFormatting.AQUA))).withStyle(ChatFormatting.GRAY));
    }
}