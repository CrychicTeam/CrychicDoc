package io.redspace.ironsspellbooks.gui.arcane_anvil;

import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.UpgradeData;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.SpellSlotUpgradeItem;
import io.redspace.ironsspellbooks.item.UniqueItem;
import io.redspace.ironsspellbooks.item.UpgradeOrbItem;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.MenuRegistry;
import io.redspace.ironsspellbooks.util.UpgradeUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class ArcaneAnvilMenu extends ItemCombinerMenu {

    public ArcaneAnvilMenu(int pContainerId, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(MenuRegistry.ARCANE_ANVIL_MENU.get(), pContainerId, inventory, containerLevelAccess);
    }

    public ArcaneAnvilMenu(int pContainerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(pContainerId, inventory, ContainerLevelAccess.NULL);
    }

    @Override
    protected boolean mayPickup(Player pPlayer, boolean pHasStack) {
        return true;
    }

    @Override
    protected void onTake(Player player0, ItemStack itemStack1) {
        this.f_39769_.getItem(0).shrink(1);
        this.f_39769_.getItem(1).shrink(1);
        this.f_39770_.execute((level, pos) -> {
            level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 0.8F, 1.1F);
            level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        });
        this.createResult();
    }

    @Override
    protected boolean isValidBlock(BlockState pState) {
        return pState.m_60713_(BlockRegistry.ARCANE_ANVIL_BLOCK.get());
    }

    @Override
    public void createResult() {
        ItemStack result = ItemStack.EMPTY;
        ItemStack baseItemStack = this.f_39769_.getItem(0);
        ItemStack modifierItemStack = this.f_39769_.getItem(1);
        if (!baseItemStack.isEmpty() && !modifierItemStack.isEmpty()) {
            if (baseItemStack.getItem() instanceof Scroll && modifierItemStack.getItem() instanceof InkItem inkItem) {
                SpellData spell1 = ISpellContainer.get(baseItemStack).getSpellAtIndex(0);
                if (spell1.getLevel() < spell1.getSpell().getMaxLevel()) {
                    SpellRarity baseRarity = spell1.getRarity();
                    SpellRarity nextRarity = spell1.getSpell().getRarity(spell1.getLevel() + 1);
                    if (nextRarity.equals(inkItem.getRarity())) {
                        result = baseItemStack.copy();
                        result.setCount(1);
                        ISpellContainer.createScrollContainer(spell1.getSpell(), spell1.getLevel() + 1, result);
                    }
                }
            } else if (baseItemStack.getItem() instanceof UniqueItem && modifierItemStack.getItem() instanceof Scroll scroll) {
                SpellData scrollSlot = ISpellContainer.get(modifierItemStack).getSpellAtIndex(0);
                if (ISpellContainer.isSpellContainer(baseItemStack)) {
                    ISpellContainer spellContainer = ISpellContainer.get(baseItemStack);
                    int matchIndex = spellContainer.getIndexForSpell(scrollSlot.getSpell());
                    if (matchIndex >= 0) {
                        SpellData spellData = spellContainer.getSpellAtIndex(matchIndex);
                        if (spellData.getLevel() < scrollSlot.getLevel() && spellData.isLocked()) {
                            result = baseItemStack.copy();
                            spellContainer.removeSpellAtIndex(matchIndex, null);
                            spellContainer.addSpellAtIndex(scrollSlot.getSpell(), scrollSlot.getLevel(), matchIndex, true, null);
                            spellContainer.save(result);
                            result.getOrCreateTag().putBoolean("Improved", true);
                        }
                    }
                }
            } else if (Utils.canImbue(baseItemStack) && modifierItemStack.getItem() instanceof Scroll scrollx) {
                result = baseItemStack.copy();
                ISpellContainer spellContainer = ISpellContainer.getOrCreate(baseItemStack);
                SpellData scrollSlot = ISpellContainer.get(modifierItemStack).getSpellAtIndex(0);
                int nextSlotIndex = spellContainer.getNextAvailableIndex();
                if (nextSlotIndex == -1) {
                    nextSlotIndex = 0;
                }
                spellContainer.removeSpellAtIndex(0, null);
                spellContainer.addSpellAtIndex(scrollSlot.getSpell(), scrollSlot.getLevel(), nextSlotIndex, false, null);
                spellContainer.save(result);
            } else if (Utils.canBeUpgraded(baseItemStack) && UpgradeData.getUpgradeData(baseItemStack).getCount() < ServerConfigs.MAX_UPGRADES.get() && modifierItemStack.getItem() instanceof UpgradeOrbItem upgradeOrb) {
                result = baseItemStack.copy();
                String slot = UpgradeUtils.getRelevantEquipmentSlot(result);
                UpgradeData.getUpgradeData(result).addUpgrade(result, upgradeOrb.getUpgradeType(), slot);
            } else if (modifierItemStack.is(ItemRegistry.SHRIVING_STONE.get())) {
                result = Utils.handleShriving(baseItemStack);
            } else if (modifierItemStack.getItem() instanceof SpellSlotUpgradeItem spellSlotUpgradeItem && baseItemStack.getItem() instanceof SpellBook) {
                ISpellContainer spellBookContainer = ISpellContainer.get(baseItemStack);
                int max = spellSlotUpgradeItem.maxSlots();
                if (spellBookContainer.getMaxSpellCount() < max) {
                    result = baseItemStack.copy();
                    ISpellContainer upgradedContainer = ISpellContainer.get(result);
                    upgradedContainer.setMaxSpellCount(upgradedContainer.getMaxSpellCount() + 1);
                    upgradedContainer.save(result);
                }
            }
        }
        this.f_39768_.setItem(0, result);
    }

    @Override
    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create().withSlot(0, 27, 47, p_266635_ -> true).withSlot(1, 76, 47, p_266634_ -> true).withResultSlot(2, 134, 47).build();
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        return pSlot.container != this.f_39768_ && super.m_5882_(pStack, pSlot);
    }
}