package io.redspace.ironsspellbooks.block.alchemist_cauldron;

import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.item.consumables.SimpleElixir;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.Util;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public class AlchemistCauldronTile extends BlockEntity implements WorldlyContainer {

    public Object2ObjectOpenHashMap<Item, AlchemistCauldronInteraction> interactions = newInteractionMap();

    public final NonNullList<ItemStack> inputItems = NonNullList.withSize(4, ItemStack.EMPTY);

    public final NonNullList<ItemStack> resultItems = NonNullList.withSize(4, ItemStack.EMPTY);

    private final int[] cooktimes = new int[4];

    public AlchemistCauldronTile(BlockPos pWorldPosition, BlockState pBlockState) {
        super(BlockRegistry.ALCHEMIST_CAULDRON_TILE.get(), pWorldPosition, pBlockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState blockState, AlchemistCauldronTile cauldronTile) {
        for (int i = 0; i < cauldronTile.inputItems.size(); i++) {
            ItemStack itemStack = cauldronTile.inputItems.get(i);
            if (!itemStack.isEmpty() && AlchemistCauldronBlock.isBoiling(blockState)) {
                cauldronTile.cooktimes[i]++;
            } else {
                cauldronTile.cooktimes[i] = 0;
            }
            if (cauldronTile.cooktimes[i] > 100) {
                cauldronTile.meltComponent(itemStack);
                cauldronTile.cooktimes[i] = 0;
            }
        }
        RandomSource random = Utils.random;
        if (AlchemistCauldronBlock.isBoiling(blockState)) {
            float waterLevel = Mth.lerp((float) AlchemistCauldronBlock.getLevel(blockState) / 4.0F, 0.25F, 0.9F);
            MagicManager.spawnParticles(level, ParticleTypes.BUBBLE_POP, (double) ((float) pos.m_123341_() + Mth.randomBetween(random, 0.2F, 0.8F)), (double) ((float) pos.m_123342_() + waterLevel), (double) ((float) pos.m_123343_() + Mth.randomBetween(random, 0.2F, 0.8F)), 1, 0.0, 0.0, 0.0, 0.0, false);
        }
    }

    public InteractionResult handleUse(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand) {
        ItemStack itemStack = player.m_21120_(hand);
        int currentLevel = (Integer) blockState.m_61143_(AlchemistCauldronBlock.LEVEL);
        ItemStack cauldronInteractionResult = ((AlchemistCauldronInteraction) this.interactions.get(itemStack.getItem())).interact(blockState, level, pos, currentLevel, itemStack);
        if (cauldronInteractionResult != null) {
            player.m_21008_(hand, ItemUtils.createFilledResult(itemStack, player, cauldronInteractionResult));
            this.setChanged();
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else if (this.isValidInput(itemStack)) {
            if (!level.isClientSide && appendItem(this.inputItems, itemStack)) {
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                this.setChanged();
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            if (itemStack.isEmpty() && hand.equals(InteractionHand.MAIN_HAND)) {
                ItemStack item = grabItem(this.inputItems);
                if (!item.isEmpty()) {
                    if (!level.isClientSide) {
                        player.m_21008_(hand, item);
                        this.setChanged();
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
            return InteractionResult.PASS;
        }
    }

    public void meltComponent(ItemStack itemStack) {
        if (this.f_58857_ != null) {
            boolean shouldMelt = false;
            boolean success = true;
            if (itemStack.is(ItemRegistry.SCROLL.get()) && !isFull(this.resultItems)) {
                if ((double) Utils.random.nextFloat() < ServerConfigs.SCROLL_RECYCLE_CHANCE.get()) {
                    ItemStack result = new ItemStack(getInkFromScroll(itemStack));
                    appendItem(this.resultItems, result);
                } else {
                    success = false;
                }
                shouldMelt = true;
            }
            if (!shouldMelt && isBrewable(itemStack)) {
                for (int i = 0; i < this.resultItems.size(); i++) {
                    ItemStack potentialPotion = this.resultItems.get(i);
                    ItemStack output = BrewingRecipeRegistry.getOutput(potentialPotion.isEmpty() ? PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER) : potentialPotion, itemStack);
                    if (!output.isEmpty()) {
                        this.resultItems.set(i, output);
                        shouldMelt = true;
                    }
                }
            }
            if (!shouldMelt && AlchemistCauldronRecipeRegistry.isValidIngredient(itemStack)) {
                for (int ix = 0; ix < this.resultItems.size(); ix++) {
                    ItemStack potentialInput = this.resultItems.get(ix).copy();
                    List<Integer> matchingItems = new ArrayList(List.of(ix));
                    if (!potentialInput.isEmpty()) {
                        for (int j = 0; j < this.resultItems.size(); j++) {
                            if (j != ix && ItemStack.isSameItemSameTags(this.resultItems.get(j), potentialInput)) {
                                int c = this.resultItems.get(j).getCount();
                                potentialInput.grow(c);
                                matchingItems.add(j);
                            }
                        }
                    }
                    int inputsCollected = potentialInput.getCount();
                    ItemStack output = AlchemistCauldronRecipeRegistry.getOutput(potentialInput, itemStack.copy(), true);
                    if (!output.isEmpty()) {
                        int inputsToConsume = inputsCollected - potentialInput.getCount();
                        for (Integer integer : matchingItems) {
                            if (inputsToConsume > 0) {
                                int c = this.resultItems.get(integer).getCount();
                                this.resultItems.get(integer).shrink(c);
                                inputsToConsume -= c;
                            }
                        }
                        for (int jx = 0; jx < this.resultItems.size(); jx++) {
                            if (matchingItems.contains(jx) && output.getCount() >= 1) {
                                this.resultItems.set(jx, output.split(1));
                            }
                        }
                        shouldMelt = true;
                        break;
                    }
                }
            }
            if (shouldMelt) {
                itemStack.shrink(1);
                this.setChanged();
                if (success) {
                    this.f_58857_.playSound(null, this.m_58899_(), SoundEvents.BREWING_STAND_BREW, SoundSource.MASTER, 1.0F, 1.0F);
                    this.f_58857_.markAndNotifyBlock(this.m_58899_(), this.f_58857_.getChunkAt(this.m_58899_()), this.m_58900_(), this.m_58900_(), 1, 1);
                } else {
                    this.f_58857_.playSound(null, this.m_58899_(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.MASTER, 1.0F, 1.0F);
                }
            }
        }
    }

    public boolean isValidInput(ItemStack itemStack) {
        return itemStack.is(ItemRegistry.SCROLL.get()) || isBrewable(itemStack) || AlchemistCauldronRecipeRegistry.isValidIngredient(itemStack);
    }

    public static boolean isBrewable(ItemStack itemStack) {
        return ServerConfigs.ALLOW_CAULDRON_BREWING.get() && BrewingRecipeRegistry.isValidIngredient(itemStack);
    }

    public int getItemWaterColor(ItemStack itemStack) {
        if (this.m_58904_() == null) {
            return 0;
        } else if (itemStack.getItem() instanceof SimpleElixir simpleElixir) {
            return simpleElixir.getMobEffect().getEffect().getColor();
        } else if (itemStack.is(ItemRegistry.INK_COMMON.get())) {
            return 2236962;
        } else if (itemStack.is(ItemRegistry.INK_UNCOMMON.get())) {
            return 1196800;
        } else if (itemStack.is(ItemRegistry.INK_RARE.get())) {
            return 997444;
        } else if (itemStack.is(ItemRegistry.INK_EPIC.get())) {
            return 10825376;
        } else if (itemStack.is(ItemRegistry.INK_LEGENDARY.get())) {
            return 16559900;
        } else if (itemStack.is(ItemRegistry.BLOOD_VIAL.get())) {
            return 5965590;
        } else {
            return PotionUtils.getPotion(itemStack) != Potions.EMPTY ? PotionUtils.getColor(itemStack) : BiomeColors.getAverageWaterColor(this.m_58904_(), this.m_58899_());
        }
    }

    public int getAverageWaterColor() {
        float f = 0.0F;
        float f1 = 0.0F;
        float f2 = 0.0F;
        for (ItemStack itemStack : this.resultItems) {
            int k = this.getItemWaterColor(itemStack);
            f += (float) (k >> 16 & 0xFF) / 255.0F;
            f1 += (float) (k >> 8 & 0xFF) / 255.0F;
            f2 += (float) (k >> 0 & 0xFF) / 255.0F;
        }
        f = f / 4.0F * 255.0F;
        f1 = f1 / 4.0F * 255.0F;
        f2 = f2 / 4.0F * 255.0F;
        return (int) f << 16 | (int) f1 << 8 | (int) f2;
    }

    public static Item getInkFromScroll(ItemStack scrollStack) {
        if (scrollStack.getItem() instanceof Scroll scroll) {
            ISpellContainer spellContainer = ISpellContainer.get(scrollStack);
            SpellData spellData = spellContainer.getSpellAtIndex(0);
            SpellRarity rarity = spellData.getSpell().getRarity(spellData.getLevel());
            return switch(rarity) {
                case COMMON ->
                    (Item) ItemRegistry.INK_COMMON.get();
                case UNCOMMON ->
                    (Item) ItemRegistry.INK_UNCOMMON.get();
                case RARE ->
                    (Item) ItemRegistry.INK_RARE.get();
                case EPIC ->
                    (Item) ItemRegistry.INK_EPIC.get();
                case LEGENDARY ->
                    (Item) ItemRegistry.INK_LEGENDARY.get();
                default ->
                    (Item) ItemRegistry.INK_COMMON.get();
            };
        } else {
            return null;
        }
    }

    public static boolean appendItem(NonNullList<ItemStack> container, ItemStack newItem) {
        for (int i = 0; i < container.size(); i++) {
            if (container.get(i).isEmpty()) {
                ItemStack newItemCopy = newItem.copy();
                newItemCopy.setCount(1);
                container.set(i, newItemCopy);
                return true;
            }
        }
        return false;
    }

    public static ItemStack grabItem(NonNullList<ItemStack> container) {
        for (int i = container.size() - 1; i >= 0; i--) {
            ItemStack item = container.get(i);
            if (!item.isEmpty()) {
                return item.split(1);
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean isEmpty(NonNullList<ItemStack> container) {
        for (ItemStack itemStack : container) {
            if (!itemStack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isFull(NonNullList<ItemStack> container) {
        for (ItemStack itemStack : container) {
            if (itemStack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.f_58857_ != null) {
            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 2);
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }

    @Override
    public void load(CompoundTag tag) {
        Utils.loadAllItems(tag, this.inputItems, "Items");
        Utils.loadAllItems(tag, this.resultItems, "Results");
        super.load(tag);
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag) {
        Utils.saveAllItems(tag, this.inputItems, "Items");
        Utils.saveAllItems(tag, this.resultItems, "Results");
        super.saveAdditional(tag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.handleUpdateTag(pkt.getTag());
        if (this.f_58857_ != null) {
            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 3);
        }
    }

    public void handleUpdateTag(CompoundTag tag) {
        this.inputItems.clear();
        this.resultItems.clear();
        if (tag != null) {
            this.load(tag);
        }
    }

    public void drops() {
        SimpleContainer simpleContainer = new SimpleContainer(this.inputItems.size());
        for (int i = 0; i < this.inputItems.size(); i++) {
            simpleContainer.setItem(i, this.inputItems.get(i));
        }
        if (this.f_58857_ != null) {
            Containers.dropContents(this.f_58857_, this.f_58858_, simpleContainer);
        }
    }

    static Object2ObjectOpenHashMap<Item, AlchemistCauldronInteraction> newInteractionMap() {
        Object2ObjectOpenHashMap<Item, AlchemistCauldronInteraction> map = Util.make(new Object2ObjectOpenHashMap(), o2o -> o2o.defaultReturnValue((AlchemistCauldronInteraction) (blockState, level, blockPos, i, itemStack) -> null));
        map.put(Items.WATER_BUCKET, (AlchemistCauldronInteraction) (blockState, level, pos, currentLevel, itemstack) -> currentLevel < 4 ? createFilledResult(level, blockState, pos, 4, new ItemStack(Items.BUCKET), SoundEvents.BUCKET_EMPTY) : null);
        map.put(Items.BUCKET, (AlchemistCauldronInteraction) (blockState, level, pos, currentLevel, itemstack) -> {
            if (level.getBlockEntity(pos) instanceof AlchemistCauldronTile tile && isEmpty(tile.resultItems) && currentLevel == 4) {
                return createFilledResult(level, blockState, pos, 0, new ItemStack(Items.WATER_BUCKET), SoundEvents.BUCKET_FILL);
            }
            return null;
        });
        map.put(Items.GLASS_BOTTLE, (AlchemistCauldronInteraction) (blockState, level, pos, currentLevel, itemstack) -> {
            if (currentLevel > 0 && level.getBlockEntity(pos) instanceof AlchemistCauldronTile tile) {
                NonNullList<ItemStack> storedItems = tile.resultItems;
                return isEmpty(storedItems) ? createFilledResult(level, blockState, pos, currentLevel - 1, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER), SoundEvents.BOTTLE_FILL) : createFilledResult(level, blockState, pos, currentLevel, grabItem(storedItems), SoundEvents.BOTTLE_FILL_DRAGONBREATH);
            } else {
                return null;
            }
        });
        map.put(Items.POTION, (AlchemistCauldronInteraction) (blockState, level, pos, currentLevel, itemstack) -> {
            if (PotionUtils.getPotion(itemstack) == Potions.WATER) {
                if (currentLevel < 4) {
                    return createFilledResult(level, blockState, pos, currentLevel + 1, new ItemStack(Items.GLASS_BOTTLE), SoundEvents.BOTTLE_EMPTY);
                }
            } else if (level.getBlockEntity(pos) instanceof AlchemistCauldronTile tile && !isFull(tile.resultItems)) {
                appendItem(tile.resultItems, itemstack);
                return createFilledResult(level, blockState, pos, Math.min(currentLevel + 1, 4), new ItemStack(Items.GLASS_BOTTLE), SoundEvents.BOTTLE_EMPTY);
            }
            return null;
        });
        createInkInteraction(map, ItemRegistry.INK_COMMON);
        createInkInteraction(map, ItemRegistry.INK_UNCOMMON);
        createInkInteraction(map, ItemRegistry.INK_RARE);
        createInkInteraction(map, ItemRegistry.INK_EPIC);
        createInkInteraction(map, ItemRegistry.INK_LEGENDARY);
        return map;
    }

    private static void createInkInteraction(Object2ObjectOpenHashMap<Item, AlchemistCauldronInteraction> map, RegistryObject<Item> ink) {
        map.put(ink.get(), (AlchemistCauldronInteraction) (blockState, level, pos, currentLevel, itemstack) -> {
            if (currentLevel > 0 && level.getBlockEntity(pos) instanceof AlchemistCauldronTile tile && !isFull(tile.resultItems)) {
                appendItem(tile.resultItems, itemstack);
                return createFilledResult(level, blockState, pos, currentLevel, new ItemStack(Items.GLASS_BOTTLE), SoundEvents.BOTTLE_EMPTY);
            } else {
                return null;
            }
        });
    }

    private static ItemStack createFilledResult(Level level, BlockState blockState, BlockPos blockPos, int newLevel, ItemStack resultItem, SoundEvent soundEvent) {
        level.setBlock(blockPos, (BlockState) blockState.m_61124_(AlchemistCauldronBlock.LEVEL, newLevel), 3);
        level.playSound(null, blockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
        return resultItem;
    }

    @Override
    public int[] getSlotsForFace(Direction pSide) {
        return new int[] { 0, 1, 2, 3 };
    }

    @Override
    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
        return !isFull(this.inputItems) && this.isValidInput(pItemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        return false;
    }

    @Override
    public void clearContent() {
    }

    @Override
    public int getContainerSize() {
        return 4;
    }

    @Override
    public boolean isEmpty() {
        return isEmpty(this.inputItems);
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        return pSlot >= 0 && pSlot <= this.inputItems.size() ? this.inputItems.remove(pSlot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return pSlot >= 0 && pSlot <= this.inputItems.size() ? this.inputItems.remove(pSlot) : ItemStack.EMPTY;
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        if (pSlot >= 0 && pSlot <= this.inputItems.size()) {
            if (this.inputItems.get(pSlot).isEmpty()) {
                this.inputItems.set(pSlot, pStack);
            } else {
                appendItem(this.inputItems, pStack);
            }
        }
    }
}