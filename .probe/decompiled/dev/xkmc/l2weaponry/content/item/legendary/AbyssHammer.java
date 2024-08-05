package dev.xkmc.l2weaponry.content.item.legendary;

import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.attack.PlayerAttackCache;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2weaponry.content.item.types.HammerItem;
import dev.xkmc.l2weaponry.init.data.LangData;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event.Result;
import org.jetbrains.annotations.Nullable;

public class AbyssHammer extends HammerItem implements LegendaryWeapon {

    public AbyssHammer(Tier tier, int damage, float speed, Item.Properties prop, ExtraToolConfig config) {
        super(tier, damage, speed, prop, config);
    }

    @Override
    public void modifySource(LivingEntity attacker, CreateSourceEvent event, ItemStack item, @Nullable Entity target) {
        super.modifySource(attacker, event, item, target);
        Optional.of(event).map(CreateSourceEvent::getPlayerAttackCache).map(PlayerAttackCache::getCriticalHitEvent).filter(e -> e.isVanillaCritical() || e.getResult() == Result.ALLOW).ifPresent(e -> event.enable(DefaultDamageState.BYPASS_MAGIC));
        if (attacker instanceof Mob) {
            event.enable(DefaultDamageState.BYPASS_MAGIC);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> list, TooltipFlag pIsAdvanced) {
        list.add(LangData.ABYSS_HAMMER.get());
    }
}