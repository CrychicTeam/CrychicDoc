package com.prunoideae.powerfuljs.capabilities.forge.mods.botania;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.Capability;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.item.BlockProvider;

public class CapabilityBlockProvider {

    public CapabilityBlockProvider.BuilderItemStack blockProvider() {
        return new CapabilityBlockProvider.BuilderItemStack();
    }

    public static class BuilderItemStack extends CapabilityBuilderForge<ItemStack, BlockProvider> {

        private CapabilityBlockProvider.ProvideBlock provideBlock;

        private CapabilityBlockProvider.GetBlockCount getBlockCount;

        public CapabilityBlockProvider.BuilderItemStack provideBlock(CapabilityBlockProvider.ProvideBlock provideBlock) {
            this.provideBlock = provideBlock;
            return this;
        }

        public CapabilityBlockProvider.BuilderItemStack getBlockCount(CapabilityBlockProvider.GetBlockCount getBlockCount) {
            this.getBlockCount = getBlockCount;
            return this;
        }

        @HideFromJS
        public BlockProvider getCapability(ItemStack instance) {
            return new BlockProvider() {

                public boolean provideBlock(Player player, ItemStack requestor, Block block, boolean doit) {
                    return BuilderItemStack.this.provideBlock != null && BuilderItemStack.this.provideBlock.provideBlock(instance, player, requestor, block, doit);
                }

                public int getBlockCount(Player player, ItemStack requestor, Block block) {
                    return BuilderItemStack.this.getBlockCount == null ? 0 : BuilderItemStack.this.getBlockCount.getBlockCount(instance, player, requestor, block);
                }
            };
        }

        @HideFromJS
        public Capability<BlockProvider> getCapabilityKey() {
            return BotaniaForgeCapabilities.BLOCK_PROVIDER;
        }

        @HideFromJS
        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:block_provider_botania");
        }
    }

    @FunctionalInterface
    public interface GetBlockCount {

        int getBlockCount(ItemStack var1, Player var2, ItemStack var3, Block var4);
    }

    @FunctionalInterface
    public interface ProvideBlock {

        boolean provideBlock(ItemStack var1, Player var2, ItemStack var3, Block var4, boolean var5);
    }
}