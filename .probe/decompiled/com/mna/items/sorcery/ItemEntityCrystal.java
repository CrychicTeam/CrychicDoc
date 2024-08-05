package com.mna.items.sorcery;

import com.mna.ManaAndArtifice;
import com.mna.items.ItemInit;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemEntityCrystal extends Item {

    public ItemEntityCrystal() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (!world.isClientSide) {
            Entity e = restoreEntity(world, context.getItemInHand());
            if (e != null) {
                context.getItemInHand().shrink(1);
                context.getPlayer().addItem(new ItemStack(Items.DIAMOND));
                Vec3 pos = new Vec3(context.getClickLocation().x, context.getClickLocation().y, context.getClickLocation().z);
                if (context.getClickedFace().getAxis() != Direction.Axis.Y) {
                    Vec3i normal = context.getClickedFace().getNormal();
                    pos = pos.add((double) normal.getX(), (double) normal.getY(), (double) normal.getZ());
                }
                e.setPos(pos);
                world.m_7967_(e);
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        CompoundTag nbt = stack.getOrCreateTag();
        if (nbt.contains("entityType")) {
            String entityID = nbt.getString("entityType");
            EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(entityID));
            if (type != null) {
                Entity e = type.create(ManaAndArtifice.instance.proxy.getClientWorld());
                tooltip.add(Component.translatable("item.mna.entrapment_crystal.trapped", e.getDisplayName().getString()));
            } else {
                tooltip.add(Component.translatable("item.mna.entrapment_crystal.missing"));
            }
        } else {
            tooltip.add(Component.translatable("item.mna.entrapment_crystal.empty"));
        }
    }

    @Nullable
    public static EntityType<?> getEntityType(ItemStack stack) {
        CompoundTag entityNBT = stack.getOrCreateTag();
        return entityNBT.contains("entityType") ? (EntityType) EntityType.byString(stack.getTag().getString("entityType")).orElse(null) : null;
    }

    public static ItemStack storeEntity(Entity e) {
        ItemStack stack = new ItemStack(ItemInit.ENTITY_ENTRAPMENT_CRYSTAL.get());
        CompoundTag nbt = new CompoundTag();
        e.saveWithoutId(nbt);
        stack.getOrCreateTag().put("entityData", nbt);
        stack.getOrCreateTag().putString("entityType", EntityType.getKey(e.getType()).toString());
        return stack;
    }

    public static Entity restoreEntity(Level world, ItemStack stack) {
        EntityType<?> type = getEntityType(stack);
        if (type == null) {
            return null;
        } else {
            CompoundTag entityNBT = stack.getOrCreateTag();
            Entity e = type.create(world);
            e.load(entityNBT.getCompound("entityData"));
            return e;
        }
    }

    public static void setNoDiamondBack(ItemStack stack) {
        stack.getOrCreateTag().putBoolean("noDiamondBack", true);
    }

    public static boolean hasNoDiamondBack(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        return !nbt.contains("noDiamondBack") ? false : nbt.getBoolean("noDiamondBack");
    }
}