package com.github.alexmodguy.alexscaves.server.inventory;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.CaveInfoItem;
import com.github.alexmodguy.alexscaves.server.message.WorldEventMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class SpelunkeryTableMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;

    private static final String NEEDS_TUTORIAL_IDENTIFIER = "alexscaves_spelunkery_tutorial_complete";

    long lastSoundTime;

    public final Container container = new SimpleContainer(2) {

        @Override
        public void setChanged() {
            SpelunkeryTableMenu.this.slotsChanged(this);
            super.setChanged();
        }
    };

    private final ResultContainer resultContainer = new ResultContainer() {

        @Override
        public void setChanged() {
            SpelunkeryTableMenu.this.slotsChanged(this);
            super.setChanged();
        }
    };

    public SpelunkeryTableMenu(int id, Inventory inventory) {
        this(id, inventory, ContainerLevelAccess.NULL);
    }

    public SpelunkeryTableMenu(int id, Inventory inventory, final ContainerLevelAccess access) {
        super(ACMenuRegistry.SPELUNKERY_TABLE_MENU.get(), id);
        this.access = access;
        this.m_38897_(new Slot(this.container, 0, 50, 143) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ACItemRegistry.CAVE_TABLET.get());
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public boolean mayPickup(Player player) {
                return SpelunkeryTableMenu.this.container.getItem(1).isEmpty();
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                access.execute((level, blockPos) -> level.playSound((Player) null, blockPos, ACSoundRegistry.SPELUNKERY_TABLE_TABLET_REMOVE.get(), SoundSource.BLOCKS, 1.0F, 1.0F));
                super.onTake(player, stack);
            }

            @Override
            public void setByPlayer(ItemStack stack) {
                super.setByPlayer(stack);
                if (!stack.isEmpty()) {
                    access.execute((level, blockPos) -> level.playSound((Player) null, blockPos, ACSoundRegistry.SPELUNKERY_TABLE_TABLET_INSERT.get(), SoundSource.BLOCKS, 1.0F, 1.0F));
                }
            }
        });
        this.m_38897_(new Slot(this.container, 1, 70, 143) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(Items.PAPER);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public boolean mayPickup(Player player) {
                return SpelunkeryTableMenu.this.container.getItem(0).isEmpty();
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                access.execute((level, blockPos) -> level.playSound((Player) null, blockPos, ACSoundRegistry.SPELUNKERY_TABLE_PAPER_REMOVE.get(), SoundSource.BLOCKS, 1.0F, 1.0F));
                super.onTake(player, stack);
            }

            @Override
            public void setByPlayer(ItemStack stack) {
                super.setByPlayer(stack);
                if (!stack.isEmpty()) {
                    access.execute((level, blockPos) -> level.playSound((Player) null, blockPos, ACSoundRegistry.SPELUNKERY_TABLE_PAPER_INSERT.get(), SoundSource.BLOCKS, 1.0F, 1.0F));
                }
            }
        });
        this.m_38897_(new Slot(this.resultContainer, 2, 142, 143) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                access.execute((level, blockPos) -> level.playSound((Player) null, blockPos, ACSoundRegistry.SPELUNKERY_TABLE_CODEX_REMOVE.get(), SoundSource.BLOCKS, 1.0F, 1.0F));
                stack.getItem().onCraftedBy(stack, player.m_9236_(), player);
                super.onTake(player, stack);
            }
        });
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.m_38897_(new Slot(inventory, j + i * 9 + 9, 24 + j * 18, 174 + i * 18));
            }
        }
        for (int k = 0; k < 9; k++) {
            this.m_38897_(new Slot(inventory, k, 24 + k * 18, 232));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return m_38889_(this.access, player, ACBlockRegistry.SPELUNKERY_TABLE.get());
    }

    @Override
    public void slotsChanged(Container container) {
    }

    private void setupResultSlot(ResourceKey<Biome> biomeResourceKey, Player player) {
        this.access.execute((p_39170_, p_39171_) -> {
            ItemStack itemInFinalSlot = this.resultContainer.getItem(2);
            ItemStack itemstack = biomeResourceKey == null ? new ItemStack(Items.PAPER) : CaveInfoItem.create(ACItemRegistry.CAVE_CODEX.get(), biomeResourceKey);
            if (itemInFinalSlot.isEmpty()) {
                this.resultContainer.setItem(2, itemstack);
            } else if (ItemStack.isSameItemSameTags(itemInFinalSlot, itemstack) && itemInFinalSlot.getCount() + itemstack.getCount() < itemInFinalSlot.getMaxStackSize()) {
                itemInFinalSlot.setCount(itemInFinalSlot.getCount() + itemstack.getCount());
                this.resultContainer.setItem(2, itemInFinalSlot);
            } else {
                player.drop(itemstack, true);
            }
            this.m_38946_();
        });
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.resultContainer && super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (slotIndex == 2) {
                itemstack1.getItem().onCraftedBy(itemstack1, player.m_9236_(), player);
                if (!this.m_38903_(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (slotIndex != 1 && slotIndex != 0) {
                if (itemstack1.is(Items.PAPER)) {
                    if (!this.m_38903_(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack1.is(ACItemRegistry.CAVE_TABLET.get()) && !this.m_38903_(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(itemstack1, 3, 39, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }
            slot.setChanged();
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, itemstack1);
            this.m_38946_();
        }
        return itemstack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((p_39152_, p_39153_) -> {
            this.m_150411_(player, this.container);
            this.m_150411_(player, this.resultContainer);
        });
    }

    public void onMessageFromScreen(Player player, boolean pass) {
        ItemStack copyOf = this.m_38853_(0).getItem().copy();
        this.m_38853_(0).getItem().shrink(1);
        if (pass && !copyOf.isEmpty()) {
            if (this.m_38853_(1).getItem().is(Items.PAPER)) {
                this.m_38853_(1).getItem().shrink(1);
            }
            ResourceKey<Biome> biomeResourceKey = CaveInfoItem.getCaveBiome(copyOf);
            if (biomeResourceKey != null) {
                this.setupResultSlot(biomeResourceKey, player);
            }
            setTutorialComplete(player, true);
        } else {
            this.access.execute(this::makeStoneParticles);
        }
    }

    public static void setTutorialComplete(Player player, boolean done) {
        CompoundTag playerData = player.getPersistentData();
        CompoundTag data = playerData.getCompound("PlayerPersisted");
        if (data != null) {
            data.putBoolean("alexscaves_spelunkery_tutorial_complete", done);
            playerData.put("PlayerPersisted", data);
        }
    }

    public static boolean hasCompletedTutorial(Player player) {
        CompoundTag playerData = player.getPersistentData();
        CompoundTag data = playerData.getCompound("PlayerPersisted");
        return data != null && data.getBoolean("alexscaves_spelunkery_tutorial_complete");
    }

    public void makeStoneParticles(Level level, BlockPos blockPos) {
        if (!level.isClientSide) {
            AlexsCaves.sendMSGToAll(new WorldEventMessage(5, blockPos.m_123341_(), blockPos.m_123342_(), blockPos.m_123343_()));
        }
    }

    public int getHighlightColor(Level level) {
        ItemStack stack = this.m_38853_(0).getItem();
        return stack.getItem() == ACItemRegistry.CAVE_TABLET.get() ? CaveInfoItem.getBiomeColorOf(level, stack, true) : -1;
    }
}