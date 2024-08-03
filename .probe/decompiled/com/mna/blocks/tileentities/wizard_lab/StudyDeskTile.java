package com.mna.blocks.tileentities.wizard_lab;

import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.blocks.tile.IEldrinConsumerTile;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.capabilities.IPlayerRoteSpells;
import com.mna.api.faction.IFaction;
import com.mna.api.sound.SFX;
import com.mna.api.spells.ICanContainSpell;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.capabilities.playerdata.rote.PlayerRoteSpellsProvider;
import com.mna.items.sorcery.ItemTornJournalPage;
import com.mna.spells.SpellCaster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;

public class StudyDeskTile extends WizardLabTile implements IEldrinConsumerTile {

    public static final int SLOT_THESIS = 0;

    public static final int INVENTORY_SIZE = 1;

    public StudyDeskTile(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
        super(type, pos, state, inventorySize);
    }

    public StudyDeskTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.STUDY_DESK.get(), pos, state, 1);
    }

    @Override
    public boolean canActivate(Player player) {
        boolean base = this.canContinue();
        return !base ? false : this.getCantStartReason(player) == StudyDeskTile.CantStartReason.NONE;
    }

    @Override
    protected boolean canContinue() {
        return this.hasStack(0);
    }

    @Override
    public float getPctComplete() {
        return this.hasStack(0) ? 1.0F : 0.0F;
    }

    @Override
    protected List<Integer> getSyncedInventorySlots() {
        return Arrays.asList();
    }

    @Override
    protected boolean canActiveTick() {
        return true;
    }

    @Override
    protected void onComplete() {
        ItemStack thesis = this.m_8020_(0);
        Player crafter = this.getCrafter();
        if (!thesis.isEmpty() && crafter != null) {
            HashMap<ISpellComponent, Float> partsToRote = new HashMap();
            if (thesis.getItem() instanceof ItemTornJournalPage && !this.m_58904_().isClientSide()) {
                ISpellComponent component = ((ItemTornJournalPage) thesis.getItem()).getComponent(thesis);
                if (component == null) {
                    return;
                }
                partsToRote.put(component, ((ItemTornJournalPage) thesis.getItem()).isThesis(thesis) ? (float) component.requiredXPForRote() : (float) component.requiredXPForRote() * 0.25F);
            } else {
                if (!(thesis.getItem() instanceof ICanContainSpell)) {
                    return;
                }
                ISpellDefinition spell = ((ICanContainSpell) thesis.getItem()).getSpell(thesis);
                if (!spell.isChargedSpell(thesis) || spell.isMysterious()) {
                    return;
                }
                partsToRote.put(spell.getShape().getPart(), (float) spell.getShape().getPart().requiredXPForRote() * 0.05F);
                spell.iterateComponents(c -> partsToRote.put(c.getPart(), (float) ((SpellEffect) c.getPart()).requiredXPForRote() * 0.1F));
                spell.getModifiers().forEach(m -> partsToRote.put(m, (float) m.requiredXPForRote() * 0.1F));
            }
            thesis.shrink(1);
            if (!this.m_58904_().isClientSide()) {
                this.m_58904_().playSound(null, (double) this.m_58899_().m_123341_(), (double) this.m_58899_().m_123342_(), (double) this.m_58899_().m_123343_(), SFX.Gui.CHARCOAL_SCRIBBLE, SoundSource.BLOCKS, 1.0F, (float) (0.95 + Math.random() * 0.1F));
            }
            crafter.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(p -> {
                List<ISpellComponent> alreadyRote = new ArrayList();
                for (Entry<ISpellComponent, Float> e : partsToRote.entrySet()) {
                    if (p.isRote((ISpellComponent) e.getKey())) {
                        alreadyRote.add((ISpellComponent) e.getKey());
                    } else {
                        p.addRoteXP(crafter, (ISpellComponent) e.getKey(), (Float) e.getValue());
                        float progression = p.getRoteProgression((ISpellComponent) e.getKey());
                        partsToRote.put((ISpellComponent) e.getKey(), progression);
                        if (crafter instanceof ServerPlayer) {
                            CustomAdvancementTriggers.STUDY_DESK_ROTE.trigger((ServerPlayer) crafter, ((ISpellComponent) e.getKey()).getRegistryName(), progression);
                        }
                    }
                }
                for (ISpellComponent ex : alreadyRote) {
                    partsToRote.remove(ex);
                }
            });
            partsToRote.forEach((c, p) -> {
                if (p < 1.0F) {
                    ChatFormatting color = ChatFormatting.WHITE;
                    IFaction faction = c.getFactionRequirement();
                    if (faction != null) {
                        color = faction.getTornJournalPageFactionColor();
                    }
                    crafter.m_213846_(Component.translatable("item.mna.torn_journal_page.learned.prefix").withStyle(ChatFormatting.ITALIC).append(Component.translatable(c.getRegistryName().toString()).withStyle(color)).append(Component.translatable("item.mna.torn_journal_page.learned.suffix").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.WHITE)));
                } else {
                    SpellCaster.sendRoteMessage(crafter, c);
                }
            });
        }
    }

    @Override
    protected boolean needsPower() {
        return false;
    }

    @Override
    public int getXPCost(Player crafter) {
        ItemStack thesis = this.m_8020_(0);
        if (!thesis.isEmpty()) {
            if (thesis.getItem() instanceof ItemTornJournalPage) {
                return ((ItemTornJournalPage) thesis.getItem()).isThesis(thesis) ? 100 : 20;
            }
            if (thesis.getItem() instanceof ICanContainSpell) {
                ISpellDefinition spell = ((ICanContainSpell) thesis.getItem()).getSpell(thesis);
                if (spell.isChargedSpell(thesis) && !spell.isMysterious()) {
                    return 20;
                }
                return super.getXPCost(crafter);
            }
        }
        return super.getXPCost(crafter);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[] { 0 };
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return !this.isActive();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction direction) {
        if (this.isActive()) {
            return false;
        } else {
            ItemStack existing = this.m_8020_(index);
            switch(index) {
                case 0:
                    if (existing.getItem() instanceof ItemTornJournalPage) {
                        return existing.isEmpty() || existing.getCount() < existing.getMaxStackSize();
                    } else if (existing.getItem() instanceof ICanContainSpell) {
                        ISpellDefinition spell = ((ICanContainSpell) existing.getItem()).getSpell(existing);
                        return spell.isChargedSpell(existing) && !spell.isMysterious();
                    }
                default:
                    return false;
            }
        }
    }

    public StudyDeskTile.CantStartReason getCantStartReason(Player player) {
        if (!player.isCreative() && player.totalExperience < this.getXPCost(player)) {
            return StudyDeskTile.CantStartReason.NOT_ENOUGH_XP;
        } else {
            LazyOptional<IPlayerRoteSpells> rote = player.getCapability(PlayerRoteSpellsProvider.ROTE);
            LazyOptional<IPlayerProgression> progression = player.getCapability(PlayerProgressionProvider.PROGRESSION);
            if (rote.isPresent() && progression.isPresent()) {
                ItemStack thesis = this.m_8020_(0);
                ArrayList<ISpellComponent> components = new ArrayList();
                if (thesis.getItem() instanceof ItemTornJournalPage) {
                    ISpellComponent component = ((ItemTornJournalPage) thesis.getItem()).getComponent(thesis);
                    if (component == null) {
                        return StudyDeskTile.CantStartReason.EMPTY_SPELL;
                    }
                    components.add(component);
                } else if (thesis.getItem() instanceof ICanContainSpell) {
                    ISpellDefinition spell = ((ICanContainSpell) thesis.getItem()).getSpell(thesis);
                    if (!spell.isChargedSpell(thesis)) {
                        return StudyDeskTile.CantStartReason.NON_CHARGED_SPELL;
                    }
                    if (spell.isMysterious()) {
                        return StudyDeskTile.CantStartReason.MYSTERIOUS_SPELL;
                    }
                    components.add(spell.getShape().getPart());
                    spell.iterateComponents(c -> components.add(c.getPart()));
                    spell.getModifiers().forEach(m -> components.add(m));
                }
                if (components.size() == 0) {
                    return StudyDeskTile.CantStartReason.EMPTY_SPELL;
                } else if (components.stream().anyMatch(c -> ((IPlayerProgression) progression.resolve().get()).getTier() < c.getTier(player.m_9236_()))) {
                    return StudyDeskTile.CantStartReason.TIER_TOO_LOW;
                } else {
                    return components.stream().allMatch(c -> ((IPlayerRoteSpells) rote.resolve().get()).isRote(c)) ? StudyDeskTile.CantStartReason.ALREADY_FULLY_ROTE : StudyDeskTile.CantStartReason.NONE;
                }
            } else {
                return StudyDeskTile.CantStartReason.MISSING_CAPABILITY;
            }
        }
    }

    public static enum CantStartReason {

        NONE(""),
        MISSING_CAPABILITY("gui.mna.study_desk.capability_missing"),
        MYSTERIOUS_SPELL("gui.mna.study_desk.mysterious_spell"),
        NON_CHARGED_SPELL("gui.mna.study_desk.non_charged"),
        ALREADY_FULLY_ROTE("gui.mna.study_desk.already_rote"),
        TIER_TOO_LOW("gui.mna.study_desk.low_tier"),
        NOT_ENOUGH_XP("gui.mna.study_desk.not_enough_xp"),
        EMPTY_SPELL("gui.mna.study_desk.empty_spell");

        private Component tooltip;

        private CantStartReason(String componentTranslationString) {
            this.tooltip = Component.translatable(componentTranslationString);
        }

        public Component tooltip() {
            return this.tooltip;
        }
    }
}