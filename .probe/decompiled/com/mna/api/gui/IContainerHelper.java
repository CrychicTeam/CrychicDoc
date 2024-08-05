package com.mna.api.gui;

import com.mna.api.items.DynamicItemFilter;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IContainerHelper {

    MenuType<EldrinCapacitorPermissionsContainer> GetEldrinPermissionsContainerType();

    boolean HasRoomFor(IItemHandlerModifiable var1, ItemStack var2);

    boolean HasStackInInventory(DynamicItemFilter var1, IItemHandler var2);

    boolean HasStackInInventory(ItemStack var1, boolean var2, boolean var3, IItemHandler var4);

    boolean HasStackInInventory(ItemStack var1, boolean var2, boolean var3, IItemHandler var4, Direction var5);

    int CountItem(ItemStack var1, IItemHandler var2, Direction var3, boolean var4, boolean var5);

    int CountItem(DynamicItemFilter var1, IItemHandler var2, Direction var3);

    boolean MergeIntoInventory(IItemHandler var1, ItemStack var2);

    boolean MergeIntoInventory(IItemHandler var1, ItemStack var2, int var3);

    ItemStack MergeToPlayerInvPrioritizeOffhand(Player var1, ItemStack var2);

    ItemStack GetFirstItemFromContainer(List<Item> var1, int var2, IItemHandler var3, Direction var4);

    ItemStack GetFirstItemFromContainer(List<Item> var1, int var2, IItemHandler var3, Direction var4, boolean var5);

    ItemStack GetFirstItemFromContainer(DynamicItemFilter var1, int var2, IItemHandler var3, Direction var4);

    ItemStack GetFirstItemFromContainer(DynamicItemFilter var1, int var2, IItemHandler var3, Direction var4, boolean var5);

    ItemStack GetFirstItemFromContainer(DynamicItemFilter var1, int var2, IItemHandler var3, Direction var4, boolean var5, boolean var6);

    boolean RemoveItemFromInventory(ItemStack var1, boolean var2, boolean var3, IItemHandler var4);

    boolean RemoveItemFromInventory(ItemStack var1, boolean var2, boolean var3, IItemHandler var4, Direction var5);

    boolean ConsumeAcrossInventories(ItemStack var1, boolean var2, boolean var3, boolean var4, List<Pair<IItemHandler, Direction>> var5);

    boolean ConsumeAcrossInventories(List<Item> var1, int var2, boolean var3, boolean var4, boolean var5, List<Pair<IItemHandler, Direction>> var6);

    ItemStack RemoveSingleItemFromInventory(ResourceLocation var1, Container var2);

    void RedirectCaptureOrDrop(Player var1, Level var2, List<ItemStack> var3, boolean var4);

    Pair<Boolean, Boolean> GetCaptureAndRedirect(Player var1);

    void DropItemAt(ItemStack var1, Vec3 var2, Level var3, boolean var4);

    void DropItemAt(ItemStack var1, Vec3 var2, Level var3, Vec3 var4);
}