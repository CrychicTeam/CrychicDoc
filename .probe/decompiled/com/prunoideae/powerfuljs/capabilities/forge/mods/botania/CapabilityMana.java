package com.prunoideae.powerfuljs.capabilities.forge.mods.botania;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaItem;
import vazkii.botania.api.mana.ManaReceiver;

public class CapabilityMana {

    public CapabilityMana.ItemStackBuilder itemStack() {
        return new CapabilityMana.ItemStackBuilder();
    }

    public CapabilityMana.BlockEntityBuilder blockEntity() {
        return new CapabilityMana.BlockEntityBuilder();
    }

    public static class BlockEntityBuilder extends CapabilityBuilderForge<BlockEntity, ManaReceiver> {

        private ToIntFunction<BlockEntity> getCurrentMana;

        private Predicate<BlockEntity> isFull;

        private BiConsumer<BlockEntity, Integer> receiveMana;

        private Predicate<BlockEntity> canReceiveManaFromBurst;

        public CapabilityMana.BlockEntityBuilder getCurrentMana(ToIntFunction<BlockEntity> getCurrentMana) {
            this.getCurrentMana = getCurrentMana;
            return this;
        }

        public CapabilityMana.BlockEntityBuilder isFull(Predicate<BlockEntity> isFull) {
            this.isFull = isFull;
            return this;
        }

        public CapabilityMana.BlockEntityBuilder receiveMana(BiConsumer<BlockEntity, Integer> receiveMana) {
            this.receiveMana = receiveMana;
            return this;
        }

        public CapabilityMana.BlockEntityBuilder canReceiveManaFromBurst(Predicate<BlockEntity> canReceiveManaFromBurst) {
            this.canReceiveManaFromBurst = canReceiveManaFromBurst;
            return this;
        }

        public ManaReceiver getCapability(BlockEntity instance) {
            return new ManaReceiver() {

                public Level getManaReceiverLevel() {
                    return instance.getLevel();
                }

                public BlockPos getManaReceiverPos() {
                    return instance.getBlockPos();
                }

                public int getCurrentMana() {
                    return BlockEntityBuilder.this.getCurrentMana == null ? 0 : BlockEntityBuilder.this.getCurrentMana.applyAsInt(instance);
                }

                public boolean isFull() {
                    return BlockEntityBuilder.this.isFull != null && BlockEntityBuilder.this.isFull.test(instance);
                }

                public void receiveMana(int mana) {
                    if (BlockEntityBuilder.this.receiveMana != null) {
                        BlockEntityBuilder.this.receiveMana.accept(instance, mana);
                    }
                }

                public boolean canReceiveManaFromBursts() {
                    return BlockEntityBuilder.this.canReceiveManaFromBurst == null ? !this.isFull() : BlockEntityBuilder.this.canReceiveManaFromBurst.test(instance);
                }
            };
        }

        public Capability<ManaReceiver> getCapabilityKey() {
            return BotaniaForgeCapabilities.MANA_RECEIVER;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:mana_be");
        }
    }

    public static class ItemStackBuilder extends CapabilityBuilderForge<ItemStack, ManaItem> {

        private ToIntFunction<ItemStack> getMana;

        private ToIntFunction<ItemStack> getMaxMana;

        private BiConsumer<ItemStack, Integer> addMana;

        private BiPredicate<ItemStack, BlockEntity> canReceiveManaFromPool;

        private BiPredicate<ItemStack, ItemStack> canReceiveManaFromItem;

        private BiPredicate<ItemStack, BlockEntity> canExportManaToPool;

        private BiPredicate<ItemStack, ItemStack> canExportManaToItem;

        private boolean noExport = false;

        public CapabilityMana.ItemStackBuilder getMana(ToIntFunction<ItemStack> getMana) {
            this.getMana = getMana;
            return this;
        }

        public CapabilityMana.ItemStackBuilder getMaxMana(ToIntFunction<ItemStack> getMaxMana) {
            this.getMaxMana = getMaxMana;
            return this;
        }

        public CapabilityMana.ItemStackBuilder addMana(BiConsumer<ItemStack, Integer> addMana) {
            this.addMana = addMana;
            return this;
        }

        public CapabilityMana.ItemStackBuilder canReceiveManaFromPool(BiPredicate<ItemStack, BlockEntity> canReceiveManaFromPool) {
            this.canReceiveManaFromPool = canReceiveManaFromPool;
            return this;
        }

        public CapabilityMana.ItemStackBuilder canReceiveManaFromItem(BiPredicate<ItemStack, ItemStack> canReceiveManaFromItem) {
            this.canReceiveManaFromItem = canReceiveManaFromItem;
            return this;
        }

        public CapabilityMana.ItemStackBuilder canExportManaToPool(BiPredicate<ItemStack, BlockEntity> canExportManaToPool) {
            this.canExportManaToPool = canExportManaToPool;
            return this;
        }

        public CapabilityMana.ItemStackBuilder canExportManaToItem(BiPredicate<ItemStack, ItemStack> canExportManaToItem) {
            this.canExportManaToItem = canExportManaToItem;
            return this;
        }

        public CapabilityMana.ItemStackBuilder setNoExport(boolean noExport) {
            this.noExport = noExport;
            return this;
        }

        public ManaItem getCapability(ItemStack instance) {
            return new ManaItem() {

                public int getMana() {
                    return ItemStackBuilder.this.getMana == null ? 0 : ItemStackBuilder.this.getMana.applyAsInt(instance);
                }

                public int getMaxMana() {
                    return ItemStackBuilder.this.getMaxMana == null ? 0 : ItemStackBuilder.this.getMaxMana.applyAsInt(instance);
                }

                public void addMana(int mana) {
                    if (ItemStackBuilder.this.addMana != null) {
                        ItemStackBuilder.this.addMana.accept(instance, mana);
                    }
                }

                public boolean canReceiveManaFromPool(BlockEntity pool) {
                    return ItemStackBuilder.this.canReceiveManaFromPool != null && ItemStackBuilder.this.canReceiveManaFromPool.test(instance, pool);
                }

                public boolean canReceiveManaFromItem(ItemStack otherStack) {
                    return ItemStackBuilder.this.canReceiveManaFromItem != null && ItemStackBuilder.this.canReceiveManaFromItem.test(instance, otherStack);
                }

                public boolean canExportManaToPool(BlockEntity pool) {
                    return ItemStackBuilder.this.canExportManaToPool != null && ItemStackBuilder.this.canExportManaToPool.test(instance, pool);
                }

                public boolean canExportManaToItem(ItemStack otherStack) {
                    return ItemStackBuilder.this.canExportManaToItem != null && ItemStackBuilder.this.canExportManaToItem.test(instance, otherStack);
                }

                public boolean isNoExport() {
                    return ItemStackBuilder.this.noExport;
                }
            };
        }

        public Capability<ManaItem> getCapabilityKey() {
            return BotaniaForgeCapabilities.MANA_ITEM;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:mana_item");
        }
    }
}