package dev.xkmc.l2backpack.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.l2backpack.content.click.DrawerQuickInsert;
import dev.xkmc.l2backpack.content.click.VanillaQuickInsert;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ChestMenu.class })
public abstract class ChestMenuMixin extends AbstractContainerMenu implements VanillaQuickInsert {

    @Shadow
    @Final
    private int containerRows;

    @Unique
    private Inventory l2Backpack$inv;

    protected ChestMenuMixin(@Nullable MenuType<?> menuType0, int int1) {
        super(menuType0, int1);
    }

    @Inject(at = { @At("TAIL") }, method = { "<init>(Lnet/minecraft/world/inventory/MenuType;ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/Container;I)V" })
    public void l2backpack$chestInit(MenuType<?> menuType0, int int1, Inventory inv, Container container2, int int3, CallbackInfo ci) {
        this.l2Backpack$inv = inv;
    }

    @WrapOperation(method = { "quickMoveStack" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ChestMenu;moveItemStackTo(Lnet/minecraft/world/item/ItemStack;IIZ)Z") })
    public boolean l2backpack$moveItem$supportDrawerAndBags(ChestMenu self, ItemStack stack, int start, int end, boolean dir, Operation<Boolean> original) {
        return this.l2Backpack$inv == null ? (Boolean) original.call(new Object[] { self, stack, start, end, dir }) : DrawerQuickInsert.moveItemStackTo(this.l2Backpack$inv.player, self, stack, start, end, dir);
    }

    @Override
    public void l2backpack$quickMove(ServerPlayer player, AbstractContainerMenu menu, ItemStack stack, int id) {
        if (id < this.containerRows * 9) {
            DrawerQuickInsert.moveItemStackTo(player, menu, stack, this.containerRows * 9, this.f_38839_.size(), true);
        } else {
            DrawerQuickInsert.moveItemStackTo(player, menu, stack, 0, this.containerRows * 9, false);
        }
    }
}