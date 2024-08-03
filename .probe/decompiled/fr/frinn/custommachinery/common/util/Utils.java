package fr.frinn.custommachinery.common.util;

import fr.frinn.custommachinery.CustomMachinery;
import fr.frinn.custommachinery.common.init.CustomMachineBlock;
import fr.frinn.custommachinery.common.machine.CustomMachine;
import fr.frinn.custommachinery.common.machine.MachineAppearance;
import fr.frinn.custommachinery.common.util.ingredient.IIngredient;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Utils {

    public static final Random RAND = new Random();

    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#,###");

    public static boolean canPlayerManageMachines(Player player) {
        return player.m_20310_(((MinecraftServer) Objects.requireNonNull(player.m_20194_())).getOperatorUserPermissionLevel());
    }

    public static Vec3 vec3dFromBlockPos(BlockPos pos) {
        return new Vec3((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_());
    }

    public static boolean testNBT(CompoundTag nbt, @Nullable CompoundTag tested) {
        if (tested == null) {
            return false;
        } else {
            for (String key : tested.getAllKeys()) {
                if (!nbt.contains(key) || nbt.getTagType(key) != tested.getTagType(key) || !testINBT(nbt.get(key), tested.get(key))) {
                    return false;
                }
            }
            return true;
        }
    }

    public static <T extends Tag> boolean testINBT(@Nullable T inbt, @Nullable T tested) {
        if (inbt != null && tested != null) {
            return switch(inbt.getId()) {
                case 1 ->
                    ((ByteTag) inbt).getAsByte() == ((ByteTag) tested).getAsByte();
                case 2 ->
                    ((ShortTag) inbt).getAsShort() == ((ShortTag) tested).getAsShort();
                case 3 ->
                    ((IntTag) inbt).getAsInt() == ((IntTag) tested).getAsInt();
                case 4 ->
                    ((LongTag) inbt).getAsLong() == ((LongTag) tested).getAsLong();
                case 5 ->
                    ((FloatTag) inbt).getAsFloat() == ((FloatTag) tested).getAsFloat();
                case 6 ->
                    ((DoubleTag) inbt).getAsDouble() == ((DoubleTag) tested).getAsDouble();
                case 7 ->
                    ((ByteArrayTag) inbt).containsAll((ByteArrayTag) tested);
                case 8 ->
                    inbt.getAsString().equals(tested.getAsString());
                case 9 ->
                    ((ListTag) inbt).containsAll((ListTag) tested);
                case 10 ->
                    testNBT((CompoundTag) inbt, (CompoundTag) tested);
                case 11 ->
                    ((IntArrayTag) inbt).containsAll((IntArrayTag) tested);
                case 12 ->
                    ((LongArrayTag) inbt).containsAll((LongArrayTag) tested);
                case 99 ->
                    ((NumericTag) inbt).getAsNumber().equals(((NumericTag) tested).getAsNumber());
                default ->
                    false;
            };
        } else {
            return false;
        }
    }

    public static AABB rotateBox(AABB box, Direction to) {
        return switch(to) {
            case EAST ->
                new AABB(box.minZ, box.minY, -box.minX, box.maxZ, box.maxY, -box.maxX);
            case NORTH ->
                new AABB(-box.minX, box.minY, -box.minZ, -box.maxX, box.maxY, -box.maxZ);
            case WEST ->
                new AABB(-box.minZ, box.minY, box.minX, -box.maxZ, box.maxY, box.maxX);
            default ->
                new AABB(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
        };
    }

    public static boolean isResourceNameValid(String resourceLocation) {
        try {
            new ResourceLocation(resourceLocation);
            return true;
        } catch (ResourceLocationException var2) {
            return false;
        }
    }

    public static float getMachineBreakSpeed(MachineAppearance appearance, BlockGetter world, BlockPos pos, Player player) {
        float hardness = appearance.getHardness();
        if (hardness <= 0.0F) {
            return 0.0F;
        } else {
            float digSpeed = player.getDestroySpeed((BlockState) MachineBlockState.CACHE.getUnchecked(appearance));
            float canHarvest = player.hasCorrectToolForDrops((BlockState) MachineBlockState.CACHE.getUnchecked(appearance)) ? 30.0F : 100.0F;
            return digSpeed / hardness / canHarvest;
        }
    }

    public static ItemStack makeItemStack(Item item, int amount, @Nullable CompoundTag nbt) {
        ItemStack stack = new ItemStack(item, amount);
        stack.setTag(nbt == null ? null : nbt.copy());
        return stack;
    }

    public static int toInt(long l) {
        try {
            return Math.toIntExact(l);
        } catch (ArithmeticException var3) {
            return Integer.MAX_VALUE;
        }
    }

    public static int toInt(float l) {
        try {
            return Math.round(l);
        } catch (ArithmeticException var2) {
            return Integer.MAX_VALUE;
        }
    }

    @Nullable
    public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> blockEntityTypeA0, BlockEntityType<E> blockEntityTypeE1, BlockEntityTicker<? super E> blockEntityTickerSuperE2) {
        return blockEntityTypeE1 == blockEntityTypeA0 ? blockEntityTickerSuperE2 : null;
    }

    public static String format(int number) {
        return NUMBER_FORMAT.format((long) number);
    }

    public static String format(long number) {
        return NUMBER_FORMAT.format(number);
    }

    public static String format(double number) {
        return NUMBER_FORMAT.format(number);
    }

    public static <T> T[] addToArray(T[] array, T toAdd) {
        T[] newArray = (T[]) Arrays.copyOf(array, array.length + 1);
        newArray[array.length] = toAdd;
        return newArray;
    }

    public static MutableComponent getBlockName(IIngredient<PartialBlockState> ingredient) {
        if (ingredient.getAll().size() == 1) {
            PartialBlockState partialBlockState = (PartialBlockState) ingredient.getAll().get(0);
            if (partialBlockState.getBlockState().m_60734_() instanceof CustomMachineBlock && partialBlockState.getNbt() != null && partialBlockState.getNbt().contains("machineID", 8)) {
                ResourceLocation machineID = ResourceLocation.tryParse(partialBlockState.getNbt().getString("machineID"));
                if (machineID != null) {
                    CustomMachine machine = (CustomMachine) CustomMachinery.MACHINES.get(machineID);
                    if (machine != null) {
                        return (MutableComponent) machine.getName();
                    }
                }
            }
            return partialBlockState.getName();
        } else {
            return Component.literal(ingredient.toString());
        }
    }

    public static long clamp(long value, long min, long max) {
        return value < min ? min : Math.min(value, max);
    }
}