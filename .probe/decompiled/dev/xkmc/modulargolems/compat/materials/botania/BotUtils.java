package dev.xkmc.modulargolems.compat.materials.botania;

import dev.xkmc.modulargolems.init.data.MGLangData;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaItem;

public class BotUtils {

    private final List<ManaItem> list;

    private static List<ManaItem> getManaItems(LivingEntity e) {
        Optional<ICuriosItemHandler> opt = CuriosApi.getCuriosInventory(e).resolve();
        if (opt.isEmpty()) {
            return List.of();
        } else {
            List<SlotResult> list = ((ICuriosItemHandler) opt.get()).findCurios(stack -> stack.getCapability(BotaniaForgeCapabilities.MANA_ITEM).resolve().isPresent());
            List<ManaItem> ans = new ArrayList();
            for (SlotResult item : list) {
                Optional<ManaItem> optMana = item.stack().getCapability(BotaniaForgeCapabilities.MANA_ITEM).resolve();
                if (!optMana.isEmpty()) {
                    ans.add((ManaItem) optMana.get());
                }
            }
            return ans;
        }
    }

    public BotUtils(LivingEntity e) {
        this.list = getManaItems(e);
    }

    public int count() {
        return this.list.size();
    }

    public int getMana() {
        int total = 0;
        for (ManaItem manaItem : this.list) {
            total += manaItem.getMana();
        }
        return total;
    }

    public int getMaxMana() {
        int total = 0;
        for (ManaItem manaItem : this.list) {
            total += manaItem.getMaxMana();
        }
        return total;
    }

    public int consumeMana(int target) {
        int consumed = 0;
        for (ManaItem manaItem : this.list) {
            int remainMana = manaItem.getMana();
            if (remainMana > 0) {
                int cost = Math.min(remainMana, target);
                consumed += cost;
                target -= cost;
                manaItem.addMana(-cost);
                if (target == 0) {
                    break;
                }
            }
        }
        return consumed;
    }

    public int generateMana(int available) {
        for (ManaItem manaItem : this.list) {
            int space = manaItem.getMaxMana() - manaItem.getMana();
            if (space > 0) {
                int toGen = Math.min(available, space);
                available -= toGen;
                manaItem.addMana(toGen);
                if (available == 0) {
                    break;
                }
            }
        }
        return available;
    }

    private static String parse(int mana) {
        if (mana < 10000) {
            return mana + "";
        } else {
            mana /= 1000;
            return mana < 10000 ? mana + "k" : mana / 1000 + "M";
        }
    }

    public static Component getDesc(LivingEntity golem) {
        BotUtils bot = new BotUtils(golem);
        return bot.count() == 0 ? MGLangData.BOT_NO_RING.get().withStyle(ChatFormatting.RED) : MGLangData.BOT_MANA.get(parse(bot.getMana()), parse(bot.getMaxMana())).withStyle(ChatFormatting.LIGHT_PURPLE);
    }
}