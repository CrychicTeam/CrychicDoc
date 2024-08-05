package com.rekindled.embers.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.Embers;
import com.rekindled.embers.api.event.InfoGogglesEvent;
import com.rekindled.embers.datagen.EmbersBlockTags;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class Misc {

    public static final double LOG_E = Math.log10(Math.exp(1.0));

    public static Random random = new Random();

    public static Direction[] horizontals = new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST };

    public static final List<BiPredicate<Player, InteractionHand>> IS_HOLDING_HAMMER = new ArrayList();

    public static final List<Function<Player, Pair<BlockPos, Direction>>> GET_HAMMER_TARGET = new ArrayList();

    public static final List<Predicate<Player>> IS_WEARING_LENS = new ArrayList();

    public static final List<Function<ItemStack, Double>> GET_EMBER_RESONANCE = new ArrayList();

    public static HashMap<ResourceLocation, Item> tagItems = new HashMap();

    public static String[] lightstyle = new String[] { "m", "mmnmmommommnonmmonqnmmo", "abcdefghijklmnopqrstuvwxyzyxwvutsrqponmlkjihgfedcba", "mmmmmaaaaammmmmaaaaaabcdefgabcdefg", "mamamamamama", "jklmnopqrstuvwxyzyxwvutsrqponmlkj", "nmonqnmomnmomomno", "mmmaaaabcdefgmmmmaaaammmaamm", "mmmaaammmaaammmabcdefaaaammmmabcdefmmmaaaa", "aaaaaaaazzzzzzzz", "mmamammmmammamamaaamammma", "abcdefghijklmnopqrrqponmlkjihgfedcba", "a" };

    public static void spawnInventoryInWorld(Level world, double x, double y, double z, IItemHandler inventory) {
        if (inventory != null && !world.isClientSide) {
            for (int i = 0; i < inventory.getSlots(); i++) {
                if (!inventory.getStackInSlot(i).isEmpty()) {
                    world.m_7967_(new ItemEntity(world, x, y, z, inventory.getStackInSlot(i)));
                }
            }
        }
    }

    public static boolean isHoldingHammer(Player player, InteractionHand hand) {
        for (BiPredicate<Player, InteractionHand> predicate : IS_HOLDING_HAMMER) {
            if (predicate.test(player, hand)) {
                return true;
            }
        }
        return false;
    }

    public static Pair<BlockPos, Direction> getHammerTarget(Player player) {
        for (Function<Player, Pair<BlockPos, Direction>> func : GET_HAMMER_TARGET) {
            Pair<BlockPos, Direction> target = (Pair<BlockPos, Direction>) func.apply(player);
            if (target != null) {
                return target;
            }
        }
        return null;
    }

    public static boolean isWearingLens(Player player) {
        boolean wearingLens = false;
        for (Predicate<Player> predicate : IS_WEARING_LENS) {
            if (predicate.test(player)) {
                wearingLens = true;
            }
        }
        InfoGogglesEvent event = new InfoGogglesEvent(player, wearingLens);
        MinecraftForge.EVENT_BUS.post(event);
        return event.shouldDisplay();
    }

    public static double getEmberResonance(ItemStack stack) {
        for (Function<ItemStack, Double> func : GET_EMBER_RESONANCE) {
            double resonance = (Double) func.apply(stack);
            if (resonance >= 1.0) {
                return resonance;
            }
        }
        return 1.0;
    }

    public static Direction readNullableFacing(int index) {
        return index > 0 ? Direction.from3DDataValue(index) : null;
    }

    public static int writeNullableFacing(Direction facing) {
        return facing != null ? facing.get3DDataValue() : -1;
    }

    public static FluidStack deserializeFluidStack(JsonObject json) {
        String fluidName = GsonHelper.getAsString(json, "fluid");
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidName));
        if (fluid != null && fluid != Fluids.EMPTY) {
            int amount = GsonHelper.getAsInt(json, "amount");
            return new FluidStack(fluid, amount);
        } else {
            throw new JsonSyntaxException("Unknown fluid " + fluidName);
        }
    }

    public static JsonObject serializeFluidStack(FluidStack stack) {
        JsonObject json = new JsonObject();
        json.addProperty("fluid", ((ResourceLocation) Objects.requireNonNull(((ResourceKey) ForgeRegistries.FLUIDS.getResourceKey(stack.getFluid()).get()).location())).toString());
        json.addProperty("amount", stack.getAmount());
        return json;
    }

    public static boolean isGaseousFluid(FluidStack resource) {
        return resource != null && resource.getFluid().getFluidType().getDensity() <= 0;
    }

    public static double getDiminishedPower(double power, double softcap, double slope) {
        return power > softcap ? softcap * slope + Math.log10(power - softcap + LOG_E / slope) - Math.log10(LOG_E / slope) : power * slope;
    }

    public static void drawComponents(Font fontRenderer, GuiGraphics guiGraphics, int x, int y, Component... components) {
        for (Component component : components) {
            guiGraphics.drawString(fontRenderer, component, x, y, 16777215);
            y += 9 + 2;
        }
    }

    public static int intColor(int r, int g, int b) {
        return r * 65536 + g * 256 + b;
    }

    public static int intColor(int a, int r, int g, int b) {
        return (a << 24) + (r << 16) + (g << 8) + b;
    }

    public static Vector3f colorFromInt(int color) {
        return new Vector3f((float) ((0xFF0000 & color) >> 16) / 255.0F, (float) ((0xFF00 & color) >> 8) / 255.0F, (float) (0xFF & color) / 255.0F);
    }

    public static Color lerpColor(Color color1, Color color2, double scale) {
        return new Color(lerp(color1.getRed(), color2.getRed(), scale), lerp(color1.getGreen(), color2.getGreen(), scale), lerp(color1.getBlue(), color2.getBlue(), scale), lerp(color1.getAlpha(), color2.getAlpha(), scale));
    }

    public static int lerp(int a, int b, double scale) {
        return (int) ((double) a * (1.0 - scale) + (double) b * scale);
    }

    public static <C extends Container, T extends Recipe<C>> T getRecipe(T cache, RecipeType<?> type, C container, Level level) {
        if (cache != null && cache.matches(container, level)) {
            return cache;
        } else {
            List<T> recipes = level.getRecipeManager().getRecipesFor((RecipeType<T>) type, container, level);
            return (T) (recipes.isEmpty() ? null : recipes.get(0));
        }
    }

    public static Item getTaggedItem(TagKey<Item> tag) {
        if (tagItems.containsKey(tag.location())) {
            return (Item) tagItems.get(tag.location());
        } else {
            Item output = Items.AIR;
            int index = Integer.MAX_VALUE;
            List<? extends String> preferences = ConfigManager.TAG_PREFERENCES.get();
            for (Holder<Item> holder : BuiltInRegistries.ITEM.m_206058_(tag)) {
                for (int i = 0; i < preferences.size(); i++) {
                    if (i < index && ((String) preferences.get(i)).equals(BuiltInRegistries.ITEM.getKey((Item) holder.get()).getNamespace())) {
                        output = (Item) holder.get();
                        index = i;
                    }
                }
                if (output == Items.AIR) {
                    output = (Item) holder.get();
                }
            }
            if (output != Items.AIR) {
                tagItems.put(tag.location(), output);
            }
            return output;
        }
    }

    public static ItemStack getPreferredItem(ItemStack[] items) {
        ItemStack output = ItemStack.EMPTY;
        int index = Integer.MAX_VALUE;
        List<? extends String> itemPreferences = ConfigManager.ITEM_PREFERENCES.get();
        List<? extends String> preferences = ConfigManager.TAG_PREFERENCES.get();
        for (ItemStack item : items) {
            ResourceLocation key = BuiltInRegistries.ITEM.getKey(item.getItem());
            for (int i = 0; i < itemPreferences.size(); i++) {
                if (key.toString().equals(itemPreferences.get(i))) {
                    return item;
                }
            }
            for (int ix = 0; ix < preferences.size(); ix++) {
                if (ix < index && ((String) preferences.get(ix)).equals(key.getNamespace())) {
                    output = item;
                    index = ix;
                }
            }
            if (output.isEmpty()) {
                output = item;
            }
        }
        return output;
    }

    public static List<EntityHitResult> getEntityHitResults(Level level, Entity projectile, Entity shooter, Vec3 startVec, Vec3 endVec, AABB boundingBox, Predicate<Entity> pFilter, float pInflationAmount) {
        List<EntityHitResult> entities = new ArrayList();
        double motionX = endVec.x - startVec.x;
        double motionY = endVec.y - startVec.y;
        double motionZ = endVec.z - startVec.z;
        for (Entity entity : level.getEntities(projectile, boundingBox.expandTowards(motionX, motionY, motionZ).inflate(1.0), pFilter)) {
            if (entity != shooter) {
                AABB aabb = entity.getBoundingBox().inflate((double) pInflationAmount);
                Optional<Vec3> optional = aabb.clip(startVec, endVec);
                if (optional.isPresent()) {
                    entities.add(new EntityHitResult(entity, (Vec3) optional.get()));
                }
            }
        }
        entities.sort((o1, o2) -> Double.compare(startVec.distanceToSqr(o1.m_82450_()), startVec.distanceToSqr(o2.m_82450_())));
        return entities;
    }

    public static EquipmentSlot handToSlot(InteractionHand hand) {
        switch(hand) {
            case MAIN_HAND:
                return EquipmentSlot.MAINHAND;
            case OFF_HAND:
                return EquipmentSlot.OFFHAND;
            default:
                return null;
        }
    }

    public static IItemHandler makeRestrictedItemHandler(final IItemHandler handler, final boolean input, final boolean output) {
        return new IItemHandler() {

            @Override
            public int getSlots() {
                return handler.getSlots();
            }

            @Override
            public ItemStack getStackInSlot(int slot) {
                return handler.getStackInSlot(slot);
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                return !input ? stack : handler.insertItem(slot, stack, simulate);
            }

            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return !output ? ItemStack.EMPTY : handler.extractItem(slot, amount, simulate);
            }

            @Override
            public int getSlotLimit(int slot) {
                return handler.getSlotLimit(slot);
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return input && handler.isItemValid(slot, stack);
            }
        };
    }

    public static IFluidHandler makeRestrictedFluidHandler(final IFluidHandler handler, final boolean input, final boolean output) {
        return new IFluidHandler() {

            @Override
            public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
                return !input ? 0 : handler.fill(resource, action);
            }

            @Override
            public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
                return !output ? null : handler.drain(resource, action);
            }

            @Override
            public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
                return !output ? null : handler.drain(maxDrain, action);
            }

            @Override
            public int getTanks() {
                return handler.getTanks();
            }

            @NotNull
            @Override
            public FluidStack getFluidInTank(int tank) {
                return handler.getFluidInTank(tank);
            }

            @Override
            public int getTankCapacity(int tank) {
                return handler.getTankCapacity(tank);
            }

            @Override
            public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
                return input && handler.isFluidValid(tank, stack);
            }
        };
    }

    public static Direction.Axis getOtherAxis(Direction.Axis axis1, Direction.Axis axis2) {
        switch(axis1) {
            case Y:
                switch(axis2) {
                    case Z:
                        return Direction.Axis.X;
                    case X:
                        return Direction.Axis.Z;
                    default:
                        return axis2;
                }
            case Z:
                switch(axis2) {
                    case Y:
                        return Direction.Axis.X;
                    case X:
                        return Direction.Axis.Y;
                    default:
                        return axis2;
                }
            case X:
                switch(axis2) {
                    case Y:
                        return Direction.Axis.Z;
                    case Z:
                        return Direction.Axis.Y;
                    default:
                        return axis2;
                }
            default:
                return axis2;
        }
    }

    public static float getLightBrightness(int style, int ticks) {
        String table = lightstyle[style];
        return (float) (table.charAt(ticks / 2 % table.length()) - 'a') / 25.0F;
    }

    public static Ingredient getRepairIngredient(Item item) {
        if (item instanceof TieredItem tool) {
            try {
                Ingredient ingredient = tool.getTier().getRepairIngredient();
                if (ingredient != null) {
                    return ingredient;
                }
                Embers.LOGGER.error("Item: \"" + ForgeRegistries.ITEMS.getKey(item) + "\" has no proper repair ingredient, this is a bug and should be reported to " + ForgeRegistries.ITEMS.getKey(item).getNamespace());
            } catch (Exception var4) {
                Embers.LOGGER.error("Item: \"" + ForgeRegistries.ITEMS.getKey(item) + "\" has no proper tool tier, this is a bug and should be reported to " + ForgeRegistries.ITEMS.getKey(item).getNamespace(), var4);
            }
        }
        if (item instanceof ArmorItem armor) {
            try {
                Ingredient ingredient = armor.getMaterial().getRepairIngredient();
                if (ingredient != null) {
                    return ingredient;
                }
                Embers.LOGGER.error("Item: \"" + ForgeRegistries.ITEMS.getKey(item) + "\" of material: \"" + armor.getMaterial().getName() + "\" has no proper repair ingredient, this is a bug and should be reported to " + ForgeRegistries.ITEMS.getKey(item).getNamespace());
            } catch (Exception var3) {
                Embers.LOGGER.error("Item: \"" + ForgeRegistries.ITEMS.getKey(item) + "\" has no proper armor material, this is a bug and should be reported to " + ForgeRegistries.ITEMS.getKey(item).getNamespace(), var3);
            }
        }
        return Ingredient.EMPTY;
    }

    public static InteractionResult useItemOnInventory(IItemHandlerModifiable inventory, Level level, Player player, InteractionHand hand) {
        ItemStack heldItem = player.m_21120_(hand);
        if (!heldItem.isEmpty()) {
            ItemStack leftover = inventory.insertItem(0, heldItem, false);
            if (!ItemStack.matches(heldItem, leftover)) {
                player.m_21008_(hand, leftover);
                return InteractionResult.SUCCESS;
            }
        }
        if (!inventory.getStackInSlot(0).isEmpty()) {
            if (!level.isClientSide) {
                level.m_7967_(new ItemEntity(level, player.m_20182_().x, player.m_20182_().y, player.m_20182_().z, inventory.getStackInSlot(0)));
                inventory.setStackInSlot(0, ItemStack.EMPTY);
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    public static void sendToTrackingPlayers(Level level, BlockPos pos, Packet<?> packet) {
        if (level instanceof ServerLevel && packet != null) {
            for (ServerPlayer serverplayer : ((ServerChunkCache) level.m_7726_()).chunkMap.getPlayers(new ChunkPos(pos), false)) {
                serverplayer.connection.send(packet);
            }
        }
    }

    public static boolean isSideProxyable(BlockState state, Direction face) {
        return state.m_204336_(EmbersBlockTags.MECH_CORE_PROXYABLE) || state.m_204336_(EmbersBlockTags.MECH_CORE_PROXYABLE_BOTTOM) && face == Direction.DOWN || state.m_204336_(EmbersBlockTags.MECH_CORE_PROXYABLE_TOP) && face == Direction.UP;
    }
}