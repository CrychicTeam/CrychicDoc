package com.github.alexthe666.alexsmobs.item;

import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.RegistryObject;

public class AMBlockItem extends BlockItem implements CustomTabBehavior {

    private final RegistryObject<Block> blockSupplier;

    public AMBlockItem(RegistryObject<Block> blockSupplier, Item.Properties props) {
        super((Block) null, props);
        this.blockSupplier = blockSupplier;
    }

    @Override
    public Block getBlock() {
        return this.blockSupplier.get();
    }

    public boolean canFitInsideCraftingRemainingItems() {
        return !(this.blockSupplier.get() instanceof ShulkerBoxBlock);
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity0) {
        if (this.blockSupplier.get() instanceof ShulkerBoxBlock) {
            ItemStack itemstack = itemEntity0.getItem();
            CompoundTag compoundtag = m_186336_(itemstack);
            if (compoundtag != null && compoundtag.contains("Items", 9)) {
                ListTag listtag = compoundtag.getList("Items", 10);
                ItemUtils.onContainerDestroyed(itemEntity0, listtag.stream().map(CompoundTag.class::cast).map(ItemStack::m_41712_));
            }
        }
    }

    @Override
    public boolean canBeHurtBy(DamageSource damage) {
        return super.m_41386_(damage) && (this != AMBlockRegistry.TRANSMUTATION_TABLE.get().asItem() || !damage.is(DamageTypeTags.IS_EXPLOSION));
    }

    @Override
    public void fillItemCategory(CreativeModeTab.Output contents) {
        if (!this.blockSupplier.equals(AMBlockRegistry.SAND_CIRCLE) && !this.blockSupplier.equals(AMBlockRegistry.RED_SAND_CIRCLE)) {
            contents.accept(this);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return this.blockSupplier.equals(AMBlockRegistry.TRIOPS_EGGS) ? InteractionResult.PASS : super.useOn(context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (this.blockSupplier.equals(AMBlockRegistry.TRIOPS_EGGS)) {
            BlockHitResult blockhitresult = m_41435_(level, player, ClipContext.Fluid.SOURCE_ONLY);
            BlockHitResult blockhitresult1 = blockhitresult.withPosition(blockhitresult.getBlockPos().above());
            InteractionResult interactionresult = super.useOn(new UseOnContext(player, hand, blockhitresult1));
            return new InteractionResultHolder<>(interactionresult, player.m_21120_(hand));
        } else {
            return super.m_7203_(level, player, hand);
        }
    }
}