package com.mna.items.artifice;

import com.mna.api.items.TieredBlockItem;
import com.mna.api.spells.targeting.SpellTargetHelper;
import com.mna.blocks.artifice.FluidJugBlock;
import com.mna.items.renderers.fluid_jugs.FluidJugItemRenderer;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

public class ItemFluidJug extends TieredBlockItem {

    public static final int CAPACITY = 16000;

    public ItemFluidJug(FluidJugBlock forBlock) {
        super(forBlock, new Item.Properties().stacksTo(1));
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private final NonNullLazy<BlockEntityWithoutLevelRenderer> ister = NonNullLazy.of(() -> new FluidJugItemRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels(), ItemFluidJug.this.isInfinite() ? FluidJugItemRenderer.jug_artifact : FluidJugItemRenderer.jug_base));

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.ister.get();
            }
        });
    }

    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new FluidHandlerItemStack(stack, 16000);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        super.m_7373_(stack, world, tooltip, flag);
        FluidStack fluidStack = this.getFluidTagData(stack);
        if (!fluidStack.isEmpty()) {
            int mb = fluidStack.getAmount();
            Component name = fluidStack.getDisplayName();
            tooltip.add(Component.translatable("block.mna.fluid_jug.contents", mb, name));
        }
    }

    public FluidStack getFluidTagData(ItemStack stack) {
        Optional<FluidStack> contained = FluidUtil.getFluidContained(stack);
        return contained.isPresent() && !((FluidStack) contained.get()).isEmpty() ? (FluidStack) contained.get() : FluidStack.EMPTY;
    }

    public boolean isInfinite() {
        return ((FluidJugBlock) this.m_40614_()).is_infinite();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return context.getPlayer().m_6047_() ? super.m_6225_(context) : InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        FluidStack flStack = this.getFluidTagData(itemstack);
        BlockHitResult rayTraceResult = m_41435_(world, player, ClipContext.Fluid.SOURCE_ONLY);
        EntityHitResult cowHit = SpellTargetHelper.rayTraceEntities(world, player, player.m_146892_(), player.m_146892_().add(player.m_20156_().scale(player.m_21133_(ForgeMod.BLOCK_REACH.get()))), player.m_20191_().inflate(player.m_21133_(ForgeMod.BLOCK_REACH.get())), e -> e instanceof Cow && e.isAlive());
        if (cowHit != null) {
            FluidStack insertStack = new FluidStack(ForgeMod.MILK.get(), 1000);
            if (!flStack.getFluid().isSame(ForgeMod.MILK.get()) && !flStack.isEmpty()) {
                return InteractionResultHolder.fail(itemstack);
            } else {
                LazyOptional<IFluidHandlerItem> handler = FluidUtil.getFluidHandler(itemstack);
                if (handler.isPresent()) {
                    IFluidHandlerItem handlerResolved = (IFluidHandlerItem) handler.resolve().get();
                    handlerResolved.fill(insertStack, IFluidHandler.FluidAction.EXECUTE);
                }
                return InteractionResultHolder.sidedSuccess(itemstack, world.isClientSide);
            }
        } else if (rayTraceResult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            BlockPos blockpos = rayTraceResult.getBlockPos();
            Direction direction = rayTraceResult.getDirection();
            BlockPos offsetPos = blockpos.relative(direction);
            if (world.mayInteract(player, blockpos) && player.mayUseItemAt(offsetPos, direction, itemstack)) {
                FluidState worldFluid = world.getFluidState(blockpos);
                if (!worldFluid.isEmpty() && (flStack.isEmpty() || worldFluid.is(flStack.getFluid())) && (this.isInfinite() || flStack.getAmount() < 16000)) {
                    return this.pickupFluid(world, blockpos, itemstack, player, direction);
                }
                if (worldFluid.isEmpty() && !flStack.isEmpty()) {
                    return this.placeFluid(world, blockpos, offsetPos, player, rayTraceResult, itemstack, hand);
                }
            }
            return InteractionResultHolder.fail(itemstack);
        }
    }

    private InteractionResultHolder<ItemStack> pickupFluid(Level world, BlockPos blockpos, ItemStack itemstack, Player player, Direction dir) {
        FluidActionResult res = FluidUtil.tryPickUpFluid(itemstack, player, world, blockpos, dir);
        return res.success ? InteractionResultHolder.sidedSuccess(res.result, world.isClientSide) : InteractionResultHolder.fail(itemstack);
    }

    private InteractionResultHolder<ItemStack> placeFluid(Level world, BlockPos blockpos, BlockPos offsetPos, Player player, BlockHitResult rayTraceResult, ItemStack itemstack, InteractionHand hand) {
        FluidStack myFluid = this.getFluidTagData(itemstack);
        BlockState worldState = world.getBlockState(blockpos);
        BlockPos resolvedOffset = this.canBlockContainFluid(world, blockpos, worldState, myFluid) ? blockpos : offsetPos;
        FluidActionResult res = FluidUtil.tryPlaceFluid(player, world, hand, resolvedOffset, itemstack, myFluid);
        return res.success ? InteractionResultHolder.sidedSuccess(res.result, world.isClientSide) : InteractionResultHolder.fail(itemstack);
    }

    private boolean canBlockContainFluid(Level worldIn, BlockPos posIn, BlockState blockstate, FluidStack myFluid) {
        return blockstate.m_60734_() instanceof LiquidBlockContainer && ((LiquidBlockContainer) blockstate.m_60734_()).canPlaceLiquid(worldIn, posIn, blockstate, myFluid.getFluid());
    }
}