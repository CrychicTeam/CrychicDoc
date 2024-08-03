package com.mna.items.ritual;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.items.ITieredItem;
import com.mna.api.rituals.IRitualReagent;
import com.mna.api.rituals.RitualBlockPos;
import com.mna.api.rituals.RitualEffect;
import com.mna.blocks.BlockInit;
import com.mna.blocks.ritual.ChalkRuneBlock;
import com.mna.blocks.tileentities.ChalkRuneTile;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.entities.EntityInit;
import com.mna.entities.rituals.Ritual;
import com.mna.gui.containers.providers.NamedRitualKit;
import com.mna.inventory.InventoryRitualKit;
import com.mna.inventory.ItemInventoryBase;
import com.mna.items.ItemInit;
import com.mna.items.base.IRadialMenuItem;
import com.mna.items.base.ItemBagBase;
import com.mna.items.filters.ItemFilterGroup;
import com.mna.items.manaweaving.ItemManaweaveBottle;
import com.mna.items.runes.ItemRuneMarking;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mna.recipes.rituals.RitualRecipe;
import com.mna.rituals.MatchedRitual;
import com.mna.rituals.contexts.RitualCheckContext;
import com.mna.tools.InventoryUtilities;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.registries.IForgeRegistry;

public class ItemPractitionersPouch extends ItemBagBase implements IRadialMenuItem, ITieredItem<ItemPractitionersPouch>, DyeableLeatherItem {

    private static final String TAG_MASTER = "ritual_kit_data";

    private static final String KEY_INDEX = "ritual_kit_index";

    private static final String TAG_RITUAL_RLOC = "ritual_rloc";

    private static final String TAG_RITUAL_POSITION_COUNT = "ritual_position_count";

    private static final String TAG_RITUAL_POSITION_PREFIX = "position_";

    private static final String TAG_RITUAL_REAGENT_COUNT = "ritual_reagent_count";

    private static final String TAG_RITUAL_REAGENT_PREFIX = "reagent_";

    private static final String TAG_PATCHES = "patches";

    public static final int MAX_RITUALS = 8;

    public static final int MAX_PATCHES = 4;

    private int tier;

    public static String NBT_ID = "mna:ritual_bag_data";

    private boolean storeRitual(MatchedRitual match, Level world, ItemStack stack) {
        CompoundTag compound = new CompoundTag();
        compound.putString("ritual_rloc", match.getRitual().m_6423_().toString());
        ArrayList<ItemStack> reagents = new ArrayList();
        ArrayList<BlockPos> reagent_positions = new ArrayList();
        ArrayList<BlockPos> rune_positions = new ArrayList();
        for (RitualBlockPos rbp : match.getPositions()) {
            BlockPos pos = rbp.getBlockPos().subtract(match.getCenter());
            rune_positions.add(pos);
            IRitualReagent reagent = rbp.getReagent();
            if (reagent != null && !reagent.isEmpty()) {
                ChalkRuneTile tecr = (ChalkRuneTile) world.getBlockEntity(match.getCenter().offset(pos.m_123341_(), 0, pos.m_123343_()));
                if (tecr == null) {
                    return false;
                }
                ItemStack tecrStack = tecr.m_8020_(0);
                if (tecrStack.isEmpty()) {
                    if (!reagent.isOptional()) {
                        return false;
                    }
                } else {
                    ItemStack tecrCopy = tecrStack.copy();
                    tecrCopy.getOrCreateTagElement("ritual_tags").putBoolean("noConsumeReagent", !reagent.shouldConsumeReagent());
                    reagents.add(tecrCopy);
                    reagent_positions.add(new BlockPos(pos.m_123341_(), 0, pos.m_123343_()));
                }
            }
        }
        compound.putInt("ritual_position_count", rune_positions.size());
        for (int i = 0; i < rune_positions.size(); i++) {
            CompoundTag positionData = new CompoundTag();
            positionData.putInt("x", ((BlockPos) rune_positions.get(i)).m_123341_());
            positionData.putInt("y", ((BlockPos) rune_positions.get(i)).m_123342_());
            positionData.putInt("z", ((BlockPos) rune_positions.get(i)).m_123343_());
            compound.put("position_" + i, positionData);
        }
        compound.putInt("ritual_reagent_count", reagents.size());
        for (int i = 0; i < reagents.size(); i++) {
            CompoundTag reagentData = new CompoundTag();
            reagentData.putInt("x", ((BlockPos) reagent_positions.get(i)).m_123341_());
            reagentData.putInt("y", ((BlockPos) reagent_positions.get(i)).m_123342_());
            reagentData.putInt("z", ((BlockPos) reagent_positions.get(i)).m_123343_());
            ((ItemStack) reagents.get(i)).save(reagentData);
            compound.put("reagent_" + i, reagentData);
        }
        Component baseDisplayName = new ItemStack(stack.getItem()).getHoverName();
        MutableComponent stc = Component.literal("").append(baseDisplayName).append(": ");
        stc = stc.append(Component.translatable(match.getRitual().m_6423_().toString())).withStyle(ChatFormatting.GOLD);
        stack.setHoverName(stc);
        CompoundTag mainCompound = stack.getOrCreateTag();
        ListTag list = null;
        if (mainCompound.contains("ritual_kit_data", 9)) {
            list = mainCompound.getList("ritual_kit_data", 10);
        } else {
            list = new ListTag();
        }
        int index = getIndex(stack);
        while (list.size() < 8) {
            list.add(new CompoundTag());
        }
        list.set(index, (Tag) compound);
        mainCompound.put("ritual_kit_data", list);
        return true;
    }

    private boolean placeRitual(ItemStack stack, BlockPos center, Level world) {
        ArrayList<RitualBlockPos> out = new ArrayList();
        ArrayList<ItemStack> reagents = this.getReagents(stack);
        ArrayList<BlockPos> positions = this.getReagentLocations(stack);
        ArrayList<BlockPos> rune_positions = this.getRuneLocations(stack);
        if (reagents.size() != positions.size()) {
            return false;
        } else {
            for (BlockPos runePos : rune_positions) {
                BlockPos pos = center.offset(runePos.m_123341_(), 1, runePos.m_123343_());
                world.setBlockAndUpdate(pos, (BlockState) ((BlockState) ((BlockState) BlockInit.CHALK_RUNE.get().defaultBlockState().m_61124_(ChalkRuneBlock.RUNEINDEX, (int) Math.floor(Math.random() * (double) (ChalkRuneBlock.RUNEINDEX.getPossibleValues().size() - 1)))).m_61124_(ChalkRuneBlock.METAL, false)).m_61124_(ChalkRuneBlock.ACTIVATED, true));
                ChalkRuneTile rune = (ChalkRuneTile) world.getBlockEntity(pos);
                if (rune == null) {
                    world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                    out.clear();
                    return false;
                }
                int count = 0;
                for (BlockPos reagent_pos : positions) {
                    if (reagent_pos.m_123341_() == runePos.m_123341_() && reagent_pos.m_123343_() == runePos.m_123343_()) {
                        if (!((ItemStack) reagents.get(count)).isEmpty()) {
                            boolean ghost = false;
                            ItemStack runeStack = ((ItemStack) reagents.get(count)).copy();
                            if (runeStack.getOrCreateTagElement("ritual_tags").getBoolean("noConsumeReagent")) {
                                ghost = true;
                            }
                            runeStack.removeTagKey("ritual_tags");
                            rune.setItem(0, runeStack.copy());
                            rune.setGhostItem(ghost);
                        }
                        break;
                    }
                    count++;
                }
                rune.setReadOnly(true);
            }
            return true;
        }
    }

    @Nullable
    protected Entity activateRitual(Player caster, ItemStack stack, RitualRecipe ritual, BlockPos center, Level world) {
        NonNullList<RitualBlockPos> matched = ritual.matchInWorld(center, world);
        if (matched == null) {
            return null;
        } else {
            IPlayerProgression progression = (IPlayerProgression) caster.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
            float speed = 1.0F - 0.2F * (float) (progression.getTier() - ritual.getTier());
            int numSpeedPatches = this.getPatchLevel(stack, PractitionersPouchPatches.SPEED);
            float speedReduction = 0.3F * (float) Math.min(numSpeedPatches, 3);
            speed -= speed * speedReduction;
            InventoryRitualKit inv = new InventoryRitualKit(stack);
            Ritual ritualEntity = new Ritual(EntityInit.RITUAL_ENTITY.get(), world);
            ritualEntity.m_6034_((double) ((float) center.m_123341_() + 0.5F), (double) center.m_123342_(), (double) ((float) center.m_123343_() + 0.5F));
            ritualEntity.setRitualBlockLocations(matched);
            ritualEntity.setSpeed(speed);
            ritualEntity.setRitualName(ritual.m_6423_());
            ritualEntity.setForceConsumeReagents(true);
            ritualEntity.setCasterUUID(caster.m_20148_());
            if (this.getPatchLevel(stack, PractitionersPouchPatches.WEAVE) > 0) {
                ArrayList<Pair<IItemHandler, Direction>> inventories = new ArrayList();
                Pair<IItemHandler, Direction> remoteInv = this.resolveRemoteInventory(stack, caster.m_9236_());
                if (remoteInv != null) {
                    inventories.add(remoteInv);
                }
                if (this.getPatchLevel(stack, PractitionersPouchPatches.RIFT) > 0) {
                    caster.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> inventories.add(new Pair(new InvWrapper(m.getRiftInventory()), Direction.UP)));
                }
                inventories.add(new Pair(inv, Direction.UP));
                ritualEntity.preConsumePatterns(this.consumePatternItems(inventories, ritual));
            }
            ritualEntity.confirmRitualReagents();
            world.m_7967_(ritualEntity);
            return ritualEntity;
        }
    }

    private ArrayList<ItemStack> getReagents(ItemStack stack) {
        ArrayList<ItemStack> out = new ArrayList();
        CompoundTag nbt = getCurrentCompound(stack, getIndex(stack));
        if (nbt != null && nbt.contains("ritual_reagent_count")) {
            int count = nbt.getInt("ritual_reagent_count");
            for (int i = 0; i < count; i++) {
                if (!nbt.contains("reagent_" + i)) {
                    out.clear();
                    return out;
                }
                CompoundTag reagent_data = nbt.getCompound("reagent_" + i);
                ItemStack reagent = ItemStack.of(reagent_data);
                if (reagent == null) {
                    out.clear();
                    return out;
                }
                out.add(reagent);
            }
            return out;
        } else {
            return out;
        }
    }

    private ArrayList<BlockPos> getReagentLocations(ItemStack stack) {
        ArrayList<BlockPos> out = new ArrayList();
        CompoundTag nbt = getCurrentCompound(stack, getIndex(stack));
        if (nbt != null && nbt.contains("ritual_reagent_count")) {
            int count = nbt.getInt("ritual_reagent_count");
            for (int i = 0; i < count; i++) {
                if (!nbt.contains("reagent_" + i)) {
                    out.clear();
                    return out;
                }
                CompoundTag reagent_data = nbt.getCompound("reagent_" + i);
                if (!reagent_data.contains("x") || !reagent_data.contains("y") || !reagent_data.contains("z")) {
                    out.clear();
                    return out;
                }
                out.add(new BlockPos(reagent_data.getInt("x"), reagent_data.getInt("y"), reagent_data.getInt("z")));
            }
            return out;
        } else {
            return out;
        }
    }

    private ArrayList<BlockPos> getRuneLocations(ItemStack stack) {
        ArrayList<BlockPos> out = new ArrayList();
        CompoundTag nbt = getCurrentCompound(stack, getIndex(stack));
        if (nbt != null && nbt.contains("ritual_position_count")) {
            int count = nbt.getInt("ritual_position_count");
            for (int i = 0; i < count; i++) {
                if (!nbt.contains("position_" + i)) {
                    out.clear();
                    return out;
                }
                CompoundTag position_data = nbt.getCompound("position_" + i);
                if (!position_data.contains("x") || !position_data.contains("y") || !position_data.contains("z")) {
                    out.clear();
                    return out;
                }
                out.add(new BlockPos(position_data.getInt("x"), position_data.getInt("y"), position_data.getInt("z")));
            }
            return out;
        } else {
            return out;
        }
    }

    private boolean consumeItem(ItemStack stack, InventoryRitualKit inventory, Container riftInv, IItemHandler remoteInv, Direction remoteFace, boolean save) {
        boolean removed = false;
        if (!removed && riftInv != null) {
            removed = InventoryUtilities.removeItemFromInventory(stack, true, true, new InvWrapper(riftInv));
        }
        if (!removed && remoteInv != null) {
            removed = InventoryUtilities.removeItemFromInventory(stack, true, true, remoteInv, remoteFace);
        }
        if (!removed) {
            removed = InventoryUtilities.removeItemFromInventory(stack, true, true, inventory);
        }
        if (save) {
            inventory.writeItemStack();
        }
        return removed;
    }

    private boolean containsItem(ItemStack stack, InventoryRitualKit inventory, Container riftInv, IItemHandler remoteInv, Direction remoteFace) {
        if (riftInv != null && InventoryUtilities.hasStackInInventory(stack, true, true, new InvWrapper(riftInv))) {
            return true;
        } else {
            return remoteInv != null && InventoryUtilities.hasStackInInventory(stack, true, true, remoteInv, remoteFace) ? true : InventoryUtilities.hasStackInInventory(stack, true, true, inventory);
        }
    }

    private boolean ritualReagentsPresent(ItemStack stack, Container riftInv, IItemHandler remoteInv, Direction remoteFace) {
        InventoryRitualKit inventory = new InventoryRitualKit(stack);
        ArrayList<ItemStack> reagents = this.getReagents(stack);
        ArrayList<ItemStack> combinedReagents = new ArrayList();
        for (ItemStack reagent : reagents) {
            boolean found = false;
            for (ItemStack listStack : combinedReagents) {
                if (listStack.getItem() == reagent.getItem()) {
                    listStack.setCount(listStack.getCount() + 1);
                    found = true;
                    break;
                }
            }
            if (!found) {
                combinedReagents.add(reagent.copy());
            }
        }
        combinedReagents.add(new ItemStack(ItemInit.PURIFIED_VINTEUM_DUST.get()));
        for (ItemStack reagent : combinedReagents) {
            int totalCount = 0;
            ItemStack searchCopy = reagent.copy();
            if (searchCopy.hasTag()) {
                searchCopy.getTag().remove("ritual_tags");
                if (searchCopy.getTag().getAllKeys().size() == 0) {
                    searchCopy.setTag(null);
                }
            }
            if (riftInv != null) {
                totalCount += InventoryUtilities.countItem(searchCopy, new InvWrapper(riftInv), remoteFace, true, true);
            }
            if (remoteInv != null) {
                totalCount += InventoryUtilities.countItem(searchCopy, remoteInv, remoteFace, true, true);
            }
            totalCount += InventoryUtilities.countItem(searchCopy, inventory, remoteFace, true, true);
            if (totalCount < searchCopy.getCount()) {
                return false;
            }
        }
        return true;
    }

    private boolean chalkPresent(ItemStack stack, Container riftInv, IItemHandler remoteInv, Direction remoteFace, int requiredUses) {
        InventoryRitualKit inventory = new InventoryRitualKit(stack);
        int foundUses = 0;
        if (riftInv != null) {
            for (int i = 0; i < riftInv.getContainerSize(); i++) {
                ItemStack invStack = riftInv.getItem(i);
                if (invStack.getItem() instanceof ItemWizardChalk) {
                    foundUses += invStack.getMaxDamage() - invStack.getDamageValue();
                }
            }
        }
        if (remoteInv != null) {
            for (int ix = 0; ix < remoteInv.getSlots(); ix++) {
                ItemStack invStack = remoteInv.extractItem(ix, 1, true);
                if (invStack.getItem() instanceof ItemWizardChalk) {
                    foundUses += invStack.getMaxDamage() - invStack.getDamageValue();
                }
            }
        }
        for (int ixx = 0; ixx < inventory.getSlots(); ixx++) {
            ItemStack invStack = inventory.getStackInSlot(ixx);
            if (invStack.getItem() instanceof ItemWizardChalk) {
                foundUses += invStack.getMaxDamage() - invStack.getDamageValue();
            }
        }
        return foundUses >= requiredUses;
    }

    private boolean consumeChalk(ItemStack stack, IItemHandlerModifiable riftInv, IItemHandler remoteInv, Direction remoteFace, int amount) {
        InventoryRitualKit inventory = new InventoryRitualKit(stack);
        int remaining = amount;
        if (amount > 0 && riftInv != null) {
            remaining = this.consumeChalkFromInventory(riftInv, amount);
        }
        if (remaining > 0 && remoteInv != null) {
            remaining = this.consumeChalkFromInventory(remoteInv, remoteFace, remaining);
        }
        if (remaining > 0 && inventory != null) {
            remaining = this.consumeChalkFromInventory(inventory, remaining);
            inventory.writeItemStack();
        }
        return remaining == 0;
    }

    private List<ResourceLocation> consumePatternItems(List<Pair<IItemHandler, Direction>> inventories, RitualRecipe recipe) {
        String[] requiredPatterns = recipe.getManaweavePatterns();
        List<ResourceLocation> consumedPatterns = new ArrayList();
        for (String pattern : requiredPatterns) {
            ResourceLocation rLoc = new ResourceLocation(pattern);
            for (int i = 0; i < inventories.size(); i++) {
                IItemHandler inventory = (IItemHandler) ((Pair) inventories.get(i)).getFirst();
                if (inventory != null && this.consumePatternJar(inventory, rLoc)) {
                    consumedPatterns.add(rLoc);
                    break;
                }
            }
        }
        return consumedPatterns;
    }

    public boolean consumePatternJar(IItemHandler inventory, ResourceLocation rLoc) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty() && ItemManaweaveBottle.hasPattern(stack)) {
                ManaweavingPattern pattern = ItemManaweaveBottle.getPattern(stack);
                if (pattern.m_6423_().equals(rLoc) && !inventory.extractItem(i, 1, false).isEmpty()) {
                    if (inventory instanceof ItemInventoryBase) {
                        ((ItemInventoryBase) inventory).writeItemStack();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private int consumeChalkFromInventory(IItemHandlerModifiable inventory, int amount) {
        int remaining = amount;
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack inventoryStack = inventory.getStackInSlot(i);
            if (inventoryStack.getItem() instanceof ItemWizardChalk) {
                int uses = inventoryStack.getMaxDamage() - inventoryStack.getDamageValue();
                if (uses > remaining) {
                    inventoryStack.setDamageValue(inventoryStack.getDamageValue() + remaining);
                    inventory.setStackInSlot(i, inventoryStack);
                    return 0;
                }
                if (uses == remaining) {
                    inventory.setStackInSlot(i, ItemStack.EMPTY);
                    return 0;
                }
                remaining -= uses;
                inventory.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
        return remaining;
    }

    private int consumeChalkFromInventory(IItemHandler handler, Direction remoteFace, int amount) {
        int remaining = amount;
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack inventoryStack = handler.extractItem(i, 1, true);
            if (inventoryStack.getItem() instanceof ItemWizardChalk) {
                int uses = inventoryStack.getMaxDamage() - inventoryStack.getDamageValue();
                if (uses > remaining) {
                    inventoryStack = handler.extractItem(i, 1, false);
                    inventoryStack.setDamageValue(inventoryStack.getDamageValue() + remaining);
                    inventoryStack = handler.insertItem(i, inventoryStack, false);
                    if (inventoryStack.isEmpty()) {
                        return 0;
                    }
                    break;
                }
                if (uses == remaining) {
                    handler.extractItem(i, 1, false);
                    return 0;
                }
                remaining -= uses;
                handler.extractItem(i, 1, false);
            }
        }
        return remaining;
    }

    private boolean consumeRitualReagents(RitualRecipe recipe, Container riftInv, IItemHandler remoteInv, Direction remoteFace, ItemStack stack) {
        InventoryRitualKit inventory = new InventoryRitualKit(stack);
        for (ItemStack reagent : this.getReagents(stack)) {
            ItemStack searchCopy = reagent.copy();
            if (searchCopy.hasTag()) {
                if (searchCopy.getOrCreateTagElement("ritual_tags").getBoolean("noConsumeReagent")) {
                    continue;
                }
                searchCopy.getTag().remove("ritual_tags");
                if (searchCopy.getTag().isEmpty()) {
                    searchCopy.setTag(null);
                }
            }
            this.consumeItem(searchCopy, inventory, riftInv, remoteInv, remoteFace, false);
        }
        this.consumeItem(new ItemStack(ItemInit.PURIFIED_VINTEUM_DUST.get()), inventory, riftInv, remoteInv, remoteFace, false);
        inventory.writeItemStack();
        return true;
    }

    public boolean hasRoomForItem(ItemStack bag, ItemStack stack) {
        InventoryRitualKit inv = new InventoryRitualKit(bag);
        return inv.canMergeItem(stack);
    }

    public ItemStack insertItem(ItemStack bag, ItemStack stack) {
        InventoryRitualKit inv = new InventoryRitualKit(bag);
        ItemStack resultantStack = inv.addItem(stack);
        inv.writeItemStack();
        return resultantStack;
    }

    public boolean shouldVoidItem(ItemStack bag, ItemStack stack) {
        InventoryRitualKit inv = new InventoryRitualKit(bag);
        return inv.shouldVoidItem(stack);
    }

    private CompoundTag getPatchTag(ItemStack stack) {
        return stack.getOrCreateTagElement("patches");
    }

    public boolean isPatchValid(ItemStack stack, PractitionersPouchPatches patch, int level) {
        int existingLevel = this.getPatchLevel(stack, patch);
        if (existingLevel >= level) {
            return false;
        } else {
            return existingLevel < level ? true : this.getAppliedPatches(stack).length < 4;
        }
    }

    public boolean addPatch(ItemStack stack, PractitionersPouchPatches patch, int level) {
        if (this.isPatchValid(stack, patch, level)) {
            this.getPatchTag(stack).putInt(patch.name(), Math.min(level, patch.getLevels()));
            return true;
        } else {
            return false;
        }
    }

    public int getPatchLevel(ItemStack stack, PractitionersPouchPatches patch) {
        CompoundTag patchTag = this.getPatchTag(stack);
        return patchTag.contains(patch.name()) ? patchTag.getInt(patch.name()) : 0;
    }

    public PractitionersPouchPatches[] getAppliedPatches(ItemStack stack) {
        CompoundTag patchTag = this.getPatchTag(stack);
        List<PractitionersPouchPatches> patchList = (List<PractitionersPouchPatches>) patchTag.getAllKeys().stream().map(s -> {
            try {
                return PractitionersPouchPatches.valueOf(s);
            } catch (Exception var2x) {
                return null;
            }
        }).filter(p -> p != null).collect(Collectors.toList());
        return (PractitionersPouchPatches[]) patchList.toArray(new PractitionersPouchPatches[0]);
    }

    public int countAppliedPatchesForLimit(ItemStack stack, ItemStack patchStack) {
        if (patchStack.getItem() instanceof ItemPractitionersPatch && ((ItemPractitionersPatch) patchStack.getItem()).getPatch() == PractitionersPouchPatches.SPEED) {
            return 0;
        } else {
            CompoundTag patchTag = this.getPatchTag(stack);
            List<PractitionersPouchPatches> patchList = (List<PractitionersPouchPatches>) patchTag.getAllKeys().stream().map(s -> {
                try {
                    return PractitionersPouchPatches.valueOf(s);
                } catch (Exception var2x) {
                    return null;
                }
            }).filter(p -> p != null && p != PractitionersPouchPatches.SPEED).collect(Collectors.toList());
            return patchList.size();
        }
    }

    @Nullable
    public Pair<IItemHandler, Direction> resolveRemoteInventory(ItemStack stack, Level world) {
        InventoryRitualKit inv = new InventoryRitualKit(stack);
        ItemStack markStack = inv.getStackInSlot(21);
        if (!markStack.isEmpty() && markStack.getItem() instanceof ItemRuneMarking) {
            BlockPos pos = ItemInit.RUNE_MARKING.get().getLocation(markStack);
            Direction face = ItemInit.RUNE_MARKING.get().getFace(markStack);
            if (pos == null) {
                return new Pair(null, Direction.UP);
            } else if (!world.isLoaded(pos)) {
                return new Pair(null, Direction.UP);
            } else {
                BlockEntity te = world.getBlockEntity(pos);
                if (te == null) {
                    return new Pair(null, Direction.UP);
                } else {
                    LazyOptional<IItemHandler> handler = te.getCapability(ForgeCapabilities.ITEM_HANDLER, face);
                    return !handler.isPresent() ? new Pair(null, Direction.UP) : new Pair((IItemHandler) handler.resolve().get(), face);
                }
            }
        } else {
            return new Pair(null, Direction.UP);
        }
    }

    @Nullable
    private static RitualRecipe getRitual(Level world, ItemStack stack) {
        ResourceLocation rLoc = new ResourceLocation(getRitualRLoc(stack, getIndex(stack)));
        return RitualRecipe.find(world, rLoc);
    }

    public static String getRitualRLoc(ItemStack stack, int index) {
        CompoundTag compound = getCurrentCompound(stack, index);
        return compound != null && compound.contains("ritual_rloc") ? compound.getString("ritual_rloc") : "";
    }

    @Nullable
    private static CompoundTag getCurrentCompound(ItemStack stack, int index) {
        if (stack != null && stack.hasTag() && stack.getTag().contains("ritual_kit_data")) {
            CompoundTag tag = stack.getTag();
            if (tag.contains("ritual_kit_data", 10)) {
                return tag.getCompound("ritual_kit_data");
            } else {
                if (tag.contains("ritual_kit_data", 9)) {
                    ListTag list = (ListTag) tag.get("ritual_kit_data");
                    if (index < list.size()) {
                        return list.getCompound(index);
                    }
                }
                return null;
            }
        } else {
            return null;
        }
    }

    public static void setIndex(ItemStack stack, int index) {
        stack.getOrCreateTag().putInt("ritual_kit_index", index);
        String translationKey = getRitualRLoc(stack, getIndex(stack));
        if (translationKey != "") {
            Component baseDisplayName = new ItemStack(stack.getItem()).getHoverName();
            MutableComponent stc = Component.literal("").append(baseDisplayName).append(": ");
            stc = stc.append(Component.translatable(translationKey)).withStyle(ChatFormatting.GOLD);
            stack.setHoverName(stc);
        } else {
            stack.resetHoverName();
        }
    }

    public static int getIndex(ItemStack stack) {
        return stack.hasTag() ? stack.getTag().getInt("ritual_kit_index") : 0;
    }

    public static boolean consumeItem(ItemStack bagStack, Container riftInv, IItemHandler remoteInv, Direction remoteFace, ItemStack consumeStack) {
        InventoryRitualKit inventory = new InventoryRitualKit(bagStack);
        return ItemInit.PRACTITIONERS_POUCH.get().consumeItem(consumeStack, inventory, riftInv, remoteInv, remoteFace, true);
    }

    public static boolean containsItem(ItemStack bagStack, Container riftInv, IItemHandler remoteInv, Direction remoteFace, ItemStack searchStack) {
        return ItemInit.PRACTITIONERS_POUCH.get().containsItem(searchStack, new InventoryRitualKit(bagStack), riftInv, remoteInv, remoteFace);
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
        if (!world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            this.openGuiIfModifierPressed(player.m_21120_(hand), player, world);
        }
        return InteractionResultHolder.success(player.m_21120_(hand));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        if (!world.isClientSide && context.getHand() == InteractionHand.MAIN_HAND && this.openGuiIfModifierPressed(context.getItemInHand(), context.getPlayer(), context.getLevel())) {
            return InteractionResult.SUCCESS;
        } else {
            IPlayerMagic magic = (IPlayerMagic) context.getPlayer().getCapability(PlayerMagicProvider.MAGIC).orElse(null);
            IPlayerProgression progression = (IPlayerProgression) context.getPlayer().getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
            if (!magic.isMagicUnlocked()) {
                return InteractionResult.FAIL;
            } else if (world.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                BlockState state = world.getBlockState(pos);
                if (state.m_60734_() == BlockInit.CHALK_RUNE.get() && context.getPlayer().m_6047_()) {
                    MatchedRitual ritualMatch = RitualRecipe.matchAnyInWorld(pos, world);
                    if (ritualMatch != null) {
                        if (!ritualMatch.getRitual().getIsKittable()) {
                            context.getPlayer().m_213846_(Component.translatable("item.mna.practitioners_pouch.not_kittable"));
                        } else if (this.storeRitual(ritualMatch, world, stack)) {
                            context.getPlayer().m_213846_(Component.translatable("item.mna.practitioners_pouch.stored"));
                        } else {
                            context.getPlayer().m_213846_(Component.translatable("item.mna.practitioners_pouch.store_failed"));
                        }
                    } else {
                        context.getPlayer().m_213846_(Component.translatable("item.mna.practitioners_pouch.not_found"));
                    }
                    return InteractionResult.SUCCESS;
                } else {
                    RitualRecipe ritual = getRitual(context.getLevel(), stack);
                    if (ritual == null) {
                        return InteractionResult.SUCCESS;
                    } else if (ritual.getTier() > progression.getTier()) {
                        context.getPlayer().m_213846_(Component.translatable("mna:ritual-start-tier-fail"));
                        return InteractionResult.FAIL;
                    } else {
                        int bound = ritual.getLowerBound();
                        for (int i = -bound; i <= bound; i++) {
                            for (int j = -bound; j <= bound; j++) {
                                if (!Block.canSupportRigidBlock(world, pos.offset(i, 0, j))) {
                                    return InteractionResult.PASS;
                                }
                                if (!world.m_46859_(pos.offset(i, 1, j))) {
                                    return InteractionResult.PASS;
                                }
                            }
                        }
                        Container riftInv = null;
                        Pair<IItemHandler, Direction> remoteInv = new Pair(null, Direction.UP);
                        if (this.getPatchLevel(stack, PractitionersPouchPatches.RIFT) > 0) {
                            riftInv = magic.getRiftInventory();
                        }
                        if (this.getPatchLevel(stack, PractitionersPouchPatches.CONVEYANCE) > 0) {
                            remoteInv = this.resolveRemoteInventory(stack, world);
                        }
                        if (this.ritualReagentsPresent(stack, riftInv, (IItemHandler) remoteInv.getFirst(), (Direction) remoteInv.getSecond()) && this.chalkPresent(stack, riftInv, (IItemHandler) remoteInv.getFirst(), (Direction) remoteInv.getSecond(), ritual.countRunes())) {
                            RitualEffect matchedEffect = (RitualEffect) ((IForgeRegistry) Registries.RitualEffect.get()).getValues().stream().filter(r -> r.matchRitual(ritual.m_6423_())).findFirst().orElse(null);
                            if (matchedEffect == null) {
                                context.getPlayer().m_213846_(Component.translatable("mna:ritual-start-failed"));
                                return InteractionResult.FAIL;
                            } else {
                                if (!context.getPlayer().isCreative() || matchedEffect.applyStartCheckInCreative()) {
                                    Component cantStartReason = matchedEffect.canRitualStart(new RitualCheckContext(context.getPlayer(), (ServerLevel) world, ritual, pos.above(), null));
                                    if (cantStartReason != null) {
                                        context.getPlayer().m_213846_(cantStartReason);
                                        return InteractionResult.FAIL;
                                    }
                                }
                                if (this.consumeRitualReagents(ritual, riftInv, (IItemHandler) remoteInv.getFirst(), (Direction) remoteInv.getSecond(), stack) && this.consumeChalk(stack, riftInv != null ? new InvWrapper(riftInv) : null, (IItemHandler) remoteInv.getFirst(), (Direction) remoteInv.getSecond(), ritual.countRunes())) {
                                    if (this.placeRitual(stack, pos, world)) {
                                        this.activateRitual(context.getPlayer(), stack, ritual, pos.above(), world);
                                    }
                                    return InteractionResult.SUCCESS;
                                } else {
                                    ManaAndArtifice.LOGGER.error("Failed to consume ritual reagents from the ritual bag.  This is likely due to a conveyance rune marked on an inventory it can't insert AND extract from.");
                                    return InteractionResult.PASS;
                                }
                            }
                        } else {
                            context.getPlayer().m_213846_(Component.translatable("item.mna.practitioners_pouch.not_enough_items"));
                            return InteractionResult.PASS;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        RitualRecipe ritual = getRitual(worldIn, stack);
        if (Screen.hasShiftDown()) {
            String patchString = I18n.get("item.mna.patch.prompt");
            PractitionersPouchPatches[] patches = this.getAppliedPatches(stack);
            if (patches.length > 0) {
                for (int i = 0; i < patches.length; i++) {
                    ItemStack patchStack = ItemPractitionersPatch.getItemFor(patches[i], this.getPatchLevel(stack, patches[i]));
                    patchString = patchString + I18n.get(patchStack.getDescriptionId() + ".simple") + ", ";
                }
                patchString = patchString.substring(0, patchString.length() - 2);
                tooltip.add(Component.literal(patchString).withStyle(ChatFormatting.YELLOW));
            }
            if (ritual == null) {
                tooltip.add(Component.translatable("item.mna.practitioners_pouch.no_ritual_stored"));
            } else {
                ArrayList<ItemStack> reagents = this.getReagents(stack);
                InventoryRitualKit irk = new InventoryRitualKit(stack);
                ArrayList<ItemStack> combinedReagents = new ArrayList();
                tooltip.add(Component.translatable("item.mna.practitioners_pouch.reagents"));
                for (ItemStack reagent : reagents) {
                    boolean found = false;
                    for (ItemStack listStack : combinedReagents) {
                        if (listStack.getItem() == reagent.getItem()) {
                            listStack.setCount(listStack.getCount() + 1);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        combinedReagents.add(reagent.copy());
                    }
                }
                combinedReagents.add(new ItemStack(ItemInit.PURIFIED_VINTEUM_DUST.get()));
                boolean hasRiftPatch = this.getPatchLevel(stack, PractitionersPouchPatches.RIFT) > 0;
                boolean hasRemoteInvPatch = this.getPatchLevel(stack, PractitionersPouchPatches.CONVEYANCE) > 0;
                ChatFormatting notFoundColor = hasRiftPatch && hasRemoteInvPatch ? ChatFormatting.LIGHT_PURPLE : (hasRiftPatch && !hasRemoteInvPatch ? ChatFormatting.DARK_PURPLE : (!hasRiftPatch && hasRemoteInvPatch ? ChatFormatting.YELLOW : ChatFormatting.RED));
                if (this.chalkPresent(stack, null, null, Direction.UP, ritual.countRunes())) {
                    tooltip.add(Component.translatable(ItemInit.WIZARD_CHALK.get().m_5524_()).withStyle(ChatFormatting.GREEN));
                } else {
                    tooltip.add(Component.translatable(ItemInit.WIZARD_CHALK.get().m_5524_()).withStyle(notFoundColor));
                }
                for (ItemStack listStackx : combinedReagents) {
                    if (!listStackx.isEmpty()) {
                        ItemStack searchCopy = listStackx.copy();
                        if (searchCopy.hasTag()) {
                            searchCopy.getTag().remove("ritual_tags");
                            if (searchCopy.getTag().getAllKeys().size() == 0) {
                                searchCopy.setTag(null);
                            }
                        }
                        if (InventoryUtilities.countItem(searchCopy, irk, Direction.UP, true, true) >= listStackx.getCount()) {
                            tooltip.add(Component.translatable(listStackx.getItem().getDescriptionId()).withStyle(ChatFormatting.GREEN));
                        } else {
                            tooltip.add(Component.translatable(listStackx.getItem().getDescriptionId()).withStyle(notFoundColor));
                        }
                    }
                }
            }
        } else {
            tooltip.add(Component.translatable("item.mna.spell.shift_prompt").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
        IRadialMenuItem.super.appendHoverText(stack, worldIn, tooltip, flagIn);
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ItemFilterGroup filterGroup() {
        return ItemFilterGroup.ALL_ITEMS;
    }

    @Override
    public MenuProvider getProvider(ItemStack stack) {
        return new NamedRitualKit(stack);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if (nbt != null && nbt.contains(NBT_ID)) {
            stack.setTag(nbt);
        }
        return null;
    }

    @Override
    public int getCachedTier() {
        return this.tier;
    }

    @Override
    public void setCachedTier(int tier) {
        this.tier = tier;
    }
}