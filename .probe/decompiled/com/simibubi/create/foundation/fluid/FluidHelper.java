package com.simibubi.create.foundation.fluid;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.simibubi.create.Create;
import com.simibubi.create.content.fluids.tank.CreativeFluidTankBlockEntity;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.content.fluids.transfer.GenericItemFilling;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import javax.annotation.Nullable;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidHelper {

    public static boolean isWater(Fluid fluid) {
        return convertToStill(fluid) == Fluids.WATER;
    }

    public static boolean isLava(Fluid fluid) {
        return convertToStill(fluid) == Fluids.LAVA;
    }

    public static boolean isTag(Fluid fluid, TagKey<Fluid> tag) {
        return fluid.is(tag);
    }

    public static boolean isTag(FluidState fluid, TagKey<Fluid> tag) {
        return fluid.is(tag);
    }

    public static boolean isTag(FluidStack fluid, TagKey<Fluid> tag) {
        return isTag(fluid.getFluid(), tag);
    }

    public static SoundEvent getFillSound(FluidStack fluid) {
        SoundEvent soundevent = fluid.getFluid().getFluidType().getSound(fluid, SoundActions.BUCKET_FILL);
        if (soundevent == null) {
            soundevent = isTag(fluid, FluidTags.LAVA) ? SoundEvents.BUCKET_FILL_LAVA : SoundEvents.BUCKET_FILL;
        }
        return soundevent;
    }

    public static SoundEvent getEmptySound(FluidStack fluid) {
        SoundEvent soundevent = fluid.getFluid().getFluidType().getSound(fluid, SoundActions.BUCKET_EMPTY);
        if (soundevent == null) {
            soundevent = isTag(fluid, FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
        }
        return soundevent;
    }

    public static boolean hasBlockState(Fluid fluid) {
        BlockState blockState = fluid.defaultFluidState().createLegacyBlock();
        return blockState != null && blockState != Blocks.AIR.defaultBlockState();
    }

    public static FluidStack copyStackWithAmount(FluidStack fs, int amount) {
        if (amount <= 0) {
            return FluidStack.EMPTY;
        } else if (fs.isEmpty()) {
            return FluidStack.EMPTY;
        } else {
            FluidStack copy = fs.copy();
            copy.setAmount(amount);
            return copy;
        }
    }

    public static Fluid convertToFlowing(Fluid fluid) {
        if (fluid == Fluids.WATER) {
            return Fluids.FLOWING_WATER;
        } else if (fluid == Fluids.LAVA) {
            return Fluids.FLOWING_LAVA;
        } else {
            return fluid instanceof ForgeFlowingFluid ? ((ForgeFlowingFluid) fluid).getFlowing() : fluid;
        }
    }

    public static Fluid convertToStill(Fluid fluid) {
        if (fluid == Fluids.FLOWING_WATER) {
            return Fluids.WATER;
        } else if (fluid == Fluids.FLOWING_LAVA) {
            return Fluids.LAVA;
        } else {
            return fluid instanceof ForgeFlowingFluid ? ((ForgeFlowingFluid) fluid).getSource() : fluid;
        }
    }

    public static JsonElement serializeFluidStack(FluidStack stack) {
        JsonObject json = new JsonObject();
        json.addProperty("fluid", RegisteredObjects.getKeyOrThrow(stack.getFluid()).toString());
        json.addProperty("amount", stack.getAmount());
        if (stack.hasTag()) {
            json.addProperty("nbt", stack.getTag().toString());
        }
        return json;
    }

    public static FluidStack deserializeFluidStack(JsonObject json) {
        ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(json, "fluid"));
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(id);
        if (fluid == null) {
            throw new JsonSyntaxException("Unknown fluid '" + id + "'");
        } else {
            int amount = GsonHelper.getAsInt(json, "amount");
            FluidStack stack = new FluidStack(fluid, amount);
            if (!json.has("nbt")) {
                return stack;
            } else {
                try {
                    JsonElement element = json.get("nbt");
                    stack.setTag(TagParser.parseTag(element.isJsonObject() ? Create.GSON.toJson(element) : GsonHelper.convertToString(element, "nbt")));
                } catch (CommandSyntaxException var6) {
                    var6.printStackTrace();
                }
                return stack;
            }
        }
    }

    public static boolean tryEmptyItemIntoBE(Level worldIn, Player player, InteractionHand handIn, ItemStack heldItem, SmartBlockEntity be) {
        if (!GenericItemEmptying.canItemBeEmptied(worldIn, heldItem)) {
            return false;
        } else {
            Pair<FluidStack, ItemStack> emptyingResult = GenericItemEmptying.emptyItem(worldIn, heldItem, true);
            LazyOptional<IFluidHandler> capability = be.getCapability(ForgeCapabilities.FLUID_HANDLER);
            IFluidHandler tank = capability.orElse(null);
            FluidStack fluidStack = emptyingResult.getFirst();
            if (tank == null || fluidStack.getAmount() != tank.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE)) {
                return false;
            } else if (worldIn.isClientSide) {
                return true;
            } else {
                ItemStack copyOfHeld = heldItem.copy();
                emptyingResult = GenericItemEmptying.emptyItem(worldIn, copyOfHeld, false);
                tank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                if (!player.isCreative() && !(be instanceof CreativeFluidTankBlockEntity)) {
                    if (copyOfHeld.isEmpty()) {
                        player.m_21008_(handIn, emptyingResult.getSecond());
                    } else {
                        player.m_21008_(handIn, copyOfHeld);
                        player.getInventory().placeItemBackInInventory(emptyingResult.getSecond());
                    }
                }
                return true;
            }
        }
    }

    public static boolean tryFillItemFromBE(Level world, Player player, InteractionHand handIn, ItemStack heldItem, SmartBlockEntity be) {
        if (!GenericItemFilling.canItemBeFilled(world, heldItem)) {
            return false;
        } else {
            LazyOptional<IFluidHandler> capability = be.getCapability(ForgeCapabilities.FLUID_HANDLER);
            IFluidHandler tank = capability.orElse(null);
            if (tank == null) {
                return false;
            } else {
                for (int i = 0; i < tank.getTanks(); i++) {
                    FluidStack fluid = tank.getFluidInTank(i);
                    if (!fluid.isEmpty()) {
                        int requiredAmountForItem = GenericItemFilling.getRequiredAmountForItem(world, heldItem, fluid.copy());
                        if (requiredAmountForItem != -1 && requiredAmountForItem <= fluid.getAmount()) {
                            if (world.isClientSide) {
                                return true;
                            }
                            if (player.isCreative() || be instanceof CreativeFluidTankBlockEntity) {
                                heldItem = heldItem.copy();
                            }
                            ItemStack out = GenericItemFilling.fillItem(world, requiredAmountForItem, heldItem, fluid.copy());
                            FluidStack copy = fluid.copy();
                            copy.setAmount(requiredAmountForItem);
                            tank.drain(copy, IFluidHandler.FluidAction.EXECUTE);
                            if (!player.isCreative()) {
                                player.getInventory().placeItemBackInInventory(out);
                            }
                            be.notifyUpdate();
                            return true;
                        }
                    }
                }
                return false;
            }
        }
    }

    @Nullable
    public static FluidHelper.FluidExchange exchange(IFluidHandler fluidTank, IFluidHandlerItem fluidItem, FluidHelper.FluidExchange preferred, int maxAmount) {
        return exchange(fluidTank, fluidItem, preferred, true, maxAmount);
    }

    @Nullable
    public static FluidHelper.FluidExchange exchangeAll(IFluidHandler fluidTank, IFluidHandlerItem fluidItem, FluidHelper.FluidExchange preferred) {
        return exchange(fluidTank, fluidItem, preferred, false, Integer.MAX_VALUE);
    }

    @Nullable
    private static FluidHelper.FluidExchange exchange(IFluidHandler fluidTank, IFluidHandlerItem fluidItem, FluidHelper.FluidExchange preferred, boolean singleOp, int maxTransferAmountPerTank) {
        FluidHelper.FluidExchange lockedExchange = null;
        for (int tankSlot = 0; tankSlot < fluidTank.getTanks(); tankSlot++) {
            for (int slot = 0; slot < fluidItem.getTanks(); slot++) {
                FluidStack fluidInTank = fluidTank.getFluidInTank(tankSlot);
                int tankCapacity = fluidTank.getTankCapacity(tankSlot) - fluidInTank.getAmount();
                boolean tankEmpty = fluidInTank.isEmpty();
                FluidStack fluidInItem = fluidItem.getFluidInTank(tankSlot);
                int itemCapacity = fluidItem.getTankCapacity(tankSlot) - fluidInItem.getAmount();
                boolean itemEmpty = fluidInItem.isEmpty();
                boolean undecided = lockedExchange == null;
                boolean canMoveToTank = (undecided || lockedExchange == FluidHelper.FluidExchange.ITEM_TO_TANK) && tankCapacity > 0;
                boolean canMoveToItem = (undecided || lockedExchange == FluidHelper.FluidExchange.TANK_TO_ITEM) && itemCapacity > 0;
                if (tankEmpty || itemEmpty || fluidInItem.isFluidEqual(fluidInTank)) {
                    if ((tankEmpty || itemCapacity <= 0) && canMoveToTank || undecided && preferred == FluidHelper.FluidExchange.ITEM_TO_TANK) {
                        int amount = fluidTank.fill(fluidItem.drain(Math.min(maxTransferAmountPerTank, tankCapacity), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                        if (amount > 0) {
                            lockedExchange = FluidHelper.FluidExchange.ITEM_TO_TANK;
                            if (singleOp) {
                                return lockedExchange;
                            }
                            continue;
                        }
                    }
                    if ((itemEmpty || tankCapacity <= 0) && canMoveToItem || undecided && preferred == FluidHelper.FluidExchange.TANK_TO_ITEM) {
                        int amount = fluidItem.fill(fluidTank.drain(Math.min(maxTransferAmountPerTank, itemCapacity), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                        if (amount > 0) {
                            lockedExchange = FluidHelper.FluidExchange.TANK_TO_ITEM;
                            if (singleOp) {
                                return lockedExchange;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static enum FluidExchange {

        ITEM_TO_TANK, TANK_TO_ITEM
    }
}