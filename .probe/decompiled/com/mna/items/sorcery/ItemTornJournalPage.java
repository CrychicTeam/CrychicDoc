package com.mna.items.sorcery;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.capabilities.IPlayerRoteSpells;
import com.mna.api.faction.IFaction;
import com.mna.api.items.TieredItem;
import com.mna.api.spells.SpellCraftingContext;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.capabilities.playerdata.rote.PlayerRoteSpellsProvider;
import com.mna.items.ItemInit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.IForgeRegistry;

public class ItemTornJournalPage extends TieredItem {

    public static final String NBT_COMPONENT = "part";

    public static final String NBT_THESIS = "thesis";

    private final boolean isThesis;

    public ItemTornJournalPage(boolean isThesis) {
        super(new Item.Properties().stacksTo(16).rarity(isThesis ? Rarity.UNCOMMON : Rarity.RARE));
        this.isThesis = isThesis;
    }

    @Override
    public void appendHoverText(ItemStack itemStack0, Level level1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
        ISpellComponent comp = this.getComponent(itemStack0);
        if (comp != null) {
            ChatFormatting color = ChatFormatting.WHITE;
            if (comp.getFactionRequirement() != null) {
                color = comp.getFactionRequirement().getTornJournalPageFactionColor();
            }
            if (this.isThesis) {
                listComponent2.add(Component.translatable("item.mna.spell_part_thesis.contains").withStyle(ChatFormatting.ITALIC).append(Component.translatable(comp.getRegistryName().toString()).withStyle(color)));
            } else {
                listComponent2.add(Component.translatable("item.mna.torn_journal_page.contains").withStyle(ChatFormatting.ITALIC).append(Component.translatable(comp.getRegistryName().toString()).withStyle(color)));
            }
            Player p = ManaAndArtifice.instance.proxy.getClientPlayer();
            if (p != null) {
                IPlayerRoteSpells rote = (IPlayerRoteSpells) p.getCapability(PlayerRoteSpellsProvider.ROTE).orElse(null);
                if (rote != null) {
                    if (rote.isRote(comp)) {
                        listComponent2.add(Component.translatable("item.mna.torn_journal_page.rote").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GOLD));
                    } else {
                        listComponent2.add(Component.translatable("item.mna.torn_journal_page.not_rote").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GOLD));
                    }
                }
            }
        } else {
            listComponent2.add(Component.translatable("item.mna.torn_journal_page.empty").withStyle(ChatFormatting.ITALIC));
        }
        super.m_7373_(itemStack0, level1, listComponent2, tooltipFlag3);
    }

    @Nullable
    public ISpellComponent getComponent(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("part")) {
            ResourceLocation rLoc = new ResourceLocation(stack.getTag().getString("part"));
            SpellEffect effect = (SpellEffect) ((IForgeRegistry) Registries.SpellEffect.get()).getValue(rLoc);
            if (effect != null) {
                return effect;
            } else {
                Shape shape = (Shape) ((IForgeRegistry) Registries.Shape.get()).getValue(rLoc);
                return (ISpellComponent) (shape != null ? shape : (ISpellComponent) ((IForgeRegistry) Registries.Modifier.get()).getValue(rLoc));
            }
        } else {
            return null;
        }
    }

    public boolean isThesis(ItemStack stack) {
        return !stack.hasTag() ? false : stack.getTag().getBoolean("thesis");
    }

    public void setComponent(ItemStack stack, ISpellComponent component) {
        stack.getOrCreateTag().putString("part", component.getRegistryName().toString());
        stack.getOrCreateTag().putBoolean("thesis", this.isThesis);
    }

    public void setRandomComponent(ItemStack stack, @Nullable IFaction faction) {
        ArrayList<ISpellComponent> all = new ArrayList();
        SpellCraftingContext ctx = new SpellCraftingContext(null);
        all.addAll((Collection) ((IForgeRegistry) Registries.Shape.get()).getValues().stream().filter(s -> !s.isSilverSpell() && s.isCraftable(ctx) ? faction == null || s.getFactionRequirement() == faction : false).collect(Collectors.toList()));
        all.addAll((Collection) ((IForgeRegistry) Registries.SpellEffect.get()).getValues().stream().filter(s -> s.getUseTag() != SpellPartTags.DONOTUSE && !s.isSilverSpell() && s.isCraftable(ctx) ? faction == null || s.getFactionRequirement() == faction : false).collect(Collectors.toList()));
        all.addAll((Collection) ((IForgeRegistry) Registries.Modifier.get()).getValues().stream().filter(s -> !s.isSilverSpell() && s.isCraftable(ctx) ? faction == null || s.getFactionRequirement() == faction : false).collect(Collectors.toList()));
        this.setComponent(stack, (ISpellComponent) all.get((int) (Math.random() * (double) all.size())));
    }

    public static ItemStack getRandomPage(RandomSource random) {
        ItemStack rnd = new ItemStack(ItemInit.TORN_JOURNAL_PAGE.get());
        ItemInit.TORN_JOURNAL_PAGE.get().setRandomComponent(rnd, null);
        return rnd;
    }

    public static ItemStack getRandomPage(Random random, @Nullable IFaction faction) {
        ItemStack rnd = new ItemStack(ItemInit.TORN_JOURNAL_PAGE.get());
        ItemInit.TORN_JOURNAL_PAGE.get().setRandomComponent(rnd, faction);
        return rnd;
    }
}