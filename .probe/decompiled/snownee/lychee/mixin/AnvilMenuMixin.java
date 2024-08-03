package snownee.lychee.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snownee.lychee.LycheeLootContextParams;
import snownee.lychee.RecipeTypes;
import snownee.lychee.anvil_crafting.AnvilContext;
import snownee.lychee.anvil_crafting.AnvilCraftingRecipe;
import snownee.lychee.core.input.ItemHolderCollection;

@Mixin({ AnvilMenu.class })
public abstract class AnvilMenuMixin extends ItemCombinerMenu {

    @Shadow
    private int repairItemCountCost;

    @Shadow
    private String itemName;

    @Final
    @Shadow
    private DataSlot cost;

    @Unique
    private AnvilCraftingRecipe lychee$recipe;

    @Unique
    private AnvilContext lychee$ctx;

    @Unique
    private AnvilContext lychee$onTakeCtx;

    public AnvilMenuMixin(MenuType<?> menuType0, int int1, Inventory inventory2, ContainerLevelAccess containerLevelAccess3) {
        super(menuType0, int1, inventory2, containerLevelAccess3);
    }

    @Inject(at = { @At("HEAD") }, method = { "createResult" }, cancellable = true)
    private void lychee_createResult(CallbackInfo ci) {
        this.lychee$recipe = null;
        this.lychee$ctx = null;
        if (!RecipeTypes.ANVIL_CRAFTING.isEmpty()) {
            ItemStack left = this.f_39769_.getItem(0);
            if (!left.isEmpty()) {
                ItemStack right = this.f_39769_.getItem(1);
                AnvilContext.Builder builder = new AnvilContext.Builder(this.f_39771_.m_9236_(), left, right, this.itemName);
                BlockPos pos = (BlockPos) this.f_39770_.evaluate((level, pos0) -> pos0).orElseGet(this.f_39771_::m_20183_);
                builder.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos));
                if (this.f_39770_ != ContainerLevelAccess.NULL) {
                    builder.withOptionalParameter(LycheeLootContextParams.BLOCK_POS, pos);
                    builder.withOptionalParameter(LootContextParams.BLOCK_STATE, this.f_39771_.m_9236_().getBlockState(pos));
                }
                builder.withParameter(LootContextParams.THIS_ENTITY, this.f_39771_);
                AnvilContext ctx = builder.create(RecipeTypes.ANVIL_CRAFTING.contextParamSet);
                ctx.itemHolders = ItemHolderCollection.Inventory.of(ctx, left.copy(), right.copy(), ItemStack.EMPTY);
                RecipeTypes.ANVIL_CRAFTING.findFirst(ctx, this.f_39771_.m_9236_()).ifPresent($ -> {
                    ItemStack output = $.assemble(ctx, this.f_39771_.m_9236_().registryAccess());
                    if (output.isEmpty()) {
                        this.f_39768_.setItem(0, ItemStack.EMPTY);
                        this.cost.set(0);
                    } else {
                        this.lychee$recipe = $;
                        this.lychee$ctx = ctx;
                        this.f_39768_.setItem(0, output);
                        if (!this.f_39771_.isCreative() && left.getCount() != 1) {
                            this.cost.set(Integer.MAX_VALUE);
                        } else {
                            this.cost.set(ctx.levelCost);
                        }
                        this.repairItemCountCost = ctx.materialCost;
                    }
                    this.m_38946_();
                    ci.cancel();
                });
            }
        }
    }

    @Inject(at = { @At("HEAD") }, method = { "onTake" })
    private void lychee_onTake(Player player, ItemStack stack, CallbackInfo ci) {
        if (this.lychee$recipe != null && this.lychee$ctx != null && !this.lychee$ctx.getLevel().isClientSide) {
            this.lychee$onTakeCtx = this.lychee$ctx;
            this.lychee$recipe.applyPostActions(this.lychee$ctx, 1);
        }
    }

    @Inject(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ContainerLevelAccess;execute(Ljava/util/function/BiConsumer;)V") }, method = { "onTake" }, cancellable = true)
    private void lychee_preventDefault(Player player, ItemStack stack, CallbackInfo ci) {
        if (this.lychee$onTakeCtx != null) {
            for (int i = 0; i < 2; i++) {
                if (this.lychee$onTakeCtx.itemHolders.ignoreConsumptionFlags.get(i)) {
                    this.f_39769_.setItem(i, this.lychee$onTakeCtx.itemHolders.get(i).get());
                }
            }
            boolean prevent = !this.lychee$onTakeCtx.runtime.doDefault;
            this.lychee$onTakeCtx = null;
            if (prevent) {
                this.f_39770_.execute((level, pos) -> level.m_46796_(1030, pos, 0));
                ci.cancel();
            }
        }
    }
}