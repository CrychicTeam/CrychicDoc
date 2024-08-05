package com.mna.events;

import com.mna.api.cantrips.ICantrip;
import com.mna.api.events.CantripCastEvent;
import com.mna.api.events.ManaweaveCraftingEvent;
import com.mna.api.events.ManaweavePatternDrawnEvent;
import com.mna.api.events.PlayerMagicLevelUpEvent;
import com.mna.api.events.RitualCompleteEvent;
import com.mna.api.events.RuneforgeCraftingEvent;
import com.mna.api.events.RuneforgeEnchantEvent;
import com.mna.api.events.RunescribeCraftingEvent;
import com.mna.api.events.SpellCastEvent;
import com.mna.api.events.SpellCraftedEvent;
import com.mna.api.events.construct.ConstructCraftedEvent;
import com.mna.api.recipes.IManaweavePattern;
import com.mna.api.recipes.IManaweavingRecipe;
import com.mna.api.recipes.IRitualRecipe;
import com.mna.api.recipes.IRuneforgeRecipe;
import com.mna.api.recipes.IRunescribeRecipe;
import com.mna.api.rituals.RitualEffect;
import com.mna.api.spells.base.ISpellDefinition;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class EventDispatcher {

    public static void DispatchRitualComplete(IRitualRecipe ritual, NonNullList<RitualEffect> handlers, BlockPos center, List<ItemStack> reagents, Player caster) {
        RitualCompleteEvent evt = new RitualCompleteEvent(ritual, handlers, center, caster, reagents);
        MinecraftForge.EVENT_BUS.post(evt);
    }

    public static boolean DispatchManaweaveCrafting(IManaweavingRecipe recipe, ItemStack output, Player crafter) {
        ManaweaveCraftingEvent evt = new ManaweaveCraftingEvent(recipe, output, crafter);
        MinecraftForge.EVENT_BUS.post(evt);
        return !evt.isCanceled();
    }

    public static boolean DispatchRuneforgeCraft(IRuneforgeRecipe recipe, ItemStack output, Player crafter) {
        RuneforgeCraftingEvent evt = new RuneforgeCraftingEvent(recipe, output, crafter);
        MinecraftForge.EVENT_BUS.post(evt);
        return !evt.isCanceled();
    }

    public static boolean DispatchRuneforgeEnchant(ItemStack output, Player crafter) {
        RuneforgeEnchantEvent evt = new RuneforgeEnchantEvent(output, crafter);
        MinecraftForge.EVENT_BUS.post(evt);
        return !evt.isCanceled();
    }

    public static boolean DispatchRunescribeCraft(IRunescribeRecipe recipe, ItemStack output, Player crafter) {
        RunescribeCraftingEvent evt = new RunescribeCraftingEvent(recipe, output, crafter);
        MinecraftForge.EVENT_BUS.post(evt);
        return !evt.isCanceled();
    }

    public static boolean DispatchManaweavePatternDrawn(IManaweavePattern pattern, LivingEntity caster) {
        ManaweavePatternDrawnEvent evt = new ManaweavePatternDrawnEvent(pattern, caster);
        MinecraftForge.EVENT_BUS.post(evt);
        return !evt.isCanceled();
    }

    public static boolean DispatchPlayerLevelUp(Player player, int newLevel) {
        PlayerMagicLevelUpEvent evt = new PlayerMagicLevelUpEvent(player, newLevel);
        MinecraftForge.EVENT_BUS.post(evt);
        return !evt.isCanceled();
    }

    public static boolean DispatchSpellCast(ISpellDefinition spell, Player caster) {
        SpellCastEvent evt = new SpellCastEvent(spell, caster);
        MinecraftForge.EVENT_BUS.post(evt);
        return !evt.isCanceled();
    }

    public static void DispatchSpellCrafted(ISpellDefinition spell, Player caster) {
        SpellCraftedEvent evt = new SpellCraftedEvent(spell, caster);
        MinecraftForge.EVENT_BUS.post(evt);
    }

    public static void DispatchConstructCrafted(Entity construct, Player caster) {
        ConstructCraftedEvent evt = new ConstructCraftedEvent(construct, caster);
        MinecraftForge.EVENT_BUS.post(evt);
    }

    public static void DispatchCantripCast(ICantrip cantrip, Player caster) {
        CantripCastEvent evt = new CantripCastEvent(cantrip, caster);
        MinecraftForge.EVENT_BUS.post(evt);
    }
}