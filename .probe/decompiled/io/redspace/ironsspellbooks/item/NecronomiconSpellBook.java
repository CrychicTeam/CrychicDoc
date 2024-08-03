package io.redspace.ironsspellbooks.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NecronomiconSpellBook extends UniqueSpellBook {

    public NecronomiconSpellBook() {
        super(SpellRarity.LEGENDARY, SpellDataRegistryHolder.of(new SpellDataRegistryHolder(SpellRegistry.BLOOD_SLASH_SPELL, 5), new SpellDataRegistryHolder(SpellRegistry.BLOOD_STEP_SPELL, 5), new SpellDataRegistryHolder(SpellRegistry.RAY_OF_SIPHONING_SPELL, 5), new SpellDataRegistryHolder(SpellRegistry.BLAZE_STORM_SPELL, 5)), 6, () -> {
            Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(AttributeRegistry.MAX_MANA.get(), new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", 200.0, AttributeModifier.Operation.ADDITION));
            return builder.build();
        });
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> lines, @NotNull TooltipFlag flag) {
        super.m_7373_(itemStack, level, lines, flag);
        AffinityData affinityData = AffinityData.getAffinityData(itemStack);
        AbstractSpell spell = affinityData.getSpell();
        if (spell != SpellRegistry.none()) {
            int i = TooltipsUtils.indexOfComponent(lines, "tooltip.irons_spellbooks.spellbook_spell_count");
            lines.add(i < 0 ? lines.size() : i + 1, Component.translatable("tooltip.irons_spellbooks.enhance_spell_level", spell.getDisplayName(MinecraftInstanceHelper.instance.player()).withStyle(spell.getSchoolType().getDisplayName().getStyle())).withStyle(ChatFormatting.YELLOW));
        }
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack != null) {
            super.initializeSpellContainer(itemStack);
            AffinityData.setAffinityData(itemStack, SpellRegistry.RAISE_DEAD_SPELL.get());
        }
    }
}