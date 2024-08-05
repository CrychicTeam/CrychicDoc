package dev.xkmc.l2hostility.content.item.curio.core;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.curios.AttrTooltip;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.code.GenericItemStack;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

public class CurseCurioItem extends MultiSlotItem {

    public static List<GenericItemStack<CurseCurioItem>> getFromPlayer(LivingEntity player) {
        List<ItemStack> list = CurioCompat.getItems(player, ex -> ex.getItem() instanceof CurseCurioItem);
        List<GenericItemStack<CurseCurioItem>> ans = new ArrayList();
        for (ItemStack e : list) {
            if (e.getItem() instanceof CurseCurioItem item) {
                ans.add(new GenericItemStack<>(item, e));
            }
        }
        return ans;
    }

    public CurseCurioItem(Item.Properties props) {
        super(props);
    }

    @Override
    public final Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        return this.getAttributeModifiers(slotContext.entity(), uuid);
    }

    protected Multimap<Attribute, AttributeModifier> getAttributeModifiers(@Nullable LivingEntity wearer, UUID uuid) {
        Multimap<Attribute, AttributeModifier> map = LinkedHashMultimap.create();
        int lv = this.getExtraLevel();
        if (lv > 0) {
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(this);
            assert id != null;
            map.put((Attribute) LHMiscs.ADD_LEVEL.get(), new AttributeModifier(uuid, id.getPath(), (double) lv, AttributeModifier.Operation.ADDITION));
        }
        return map;
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
        return AttrTooltip.modifyTooltip(super.getAttributesTooltip(tooltips, stack), this.getAttributeModifiers(Proxy.getPlayer(), Util.NIL_UUID), false);
    }

    public int getExtraLevel() {
        return 0;
    }

    public double getLootFactor(ItemStack stack, PlayerDifficulty player, MobTraitCap mob) {
        return 1.0;
    }

    public double getGrowFactor(ItemStack stack, PlayerDifficulty player, MobTraitCap mob) {
        return 1.0;
    }

    public boolean reflectTrait(MobTrait trait) {
        return false;
    }

    public void onHurtTarget(ItemStack stack, LivingEntity user, AttackCache cache) {
    }

    public void onDamage(ItemStack stack, LivingEntity user, LivingDamageEvent event) {
    }
}