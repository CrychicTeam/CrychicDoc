package fr.frinn.custommachinery.common.init;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.common.util.PartialBlockState;
import fr.frinn.custommachinery.impl.codec.DefaultCodecs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class StructureCreatorItem extends Item {

    private static final NamedCodec<List<List<String>>> PATTERN_CODEC = NamedCodec.STRING.listOf().listOf();

    private static final NamedCodec<Map<Character, PartialBlockState>> KEYS_CODEC = NamedCodec.unboundedMap(DefaultCodecs.CHARACTER, PartialBlockState.CODEC, "Map<Character, Block>");

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public StructureCreatorItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() == null) {
            return InteractionResult.FAIL;
        } else {
            BlockPos pos = context.getClickedPos();
            BlockState state = context.getLevel().getBlockState(pos);
            ItemStack stack = context.getItemInHand();
            if (state.m_60734_() instanceof CustomMachineBlock) {
                if (!context.getLevel().isClientSide()) {
                    this.finishStructure(stack, pos, (Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING), (ServerPlayer) context.getPlayer());
                }
                return InteractionResult.SUCCESS;
            } else if (!getSelectedBlocks(stack).contains(pos)) {
                addSelectedBlock(stack, pos);
                return InteractionResult.SUCCESS;
            } else if (getSelectedBlocks(stack).contains(pos)) {
                removeSelectedBlock(stack, pos);
                return InteractionResult.SUCCESS;
            } else {
                return super.useOn(context);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        int amount = getSelectedBlocks(stack).size();
        if (amount <= 0) {
            tooltip.add(Component.translatable("custommachinery.structure_creator.no_blocks").withStyle(ChatFormatting.RED));
        } else {
            tooltip.add(Component.translatable("custommachinery.structure_creator.amount", getSelectedBlocks(stack).size()).withStyle(ChatFormatting.BLUE));
        }
        tooltip.add(Component.translatable("custommachinery.structure_creator.select").withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.translatable("custommachinery.structure_creator.reset").withStyle(ChatFormatting.GOLD));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player.m_6047_() && player.m_21120_(hand).getItem() == this) {
            ItemStack stack = player.m_21120_(hand);
            stack.removeTagKey("custommachinery");
            return InteractionResultHolder.success(stack);
        } else {
            return super.use(level, player, hand);
        }
    }

    public static List<BlockPos> getSelectedBlocks(ItemStack stack) {
        return (List<BlockPos>) Arrays.stream(stack.getOrCreateTagElement("custommachinery").getLongArray("blocks")).mapToObj(BlockPos::m_122022_).collect(Collectors.toList());
    }

    public static void addSelectedBlock(ItemStack stack, BlockPos pos) {
        long packed = pos.asLong();
        long[] posList = stack.getOrCreateTagElement("custommachinery").getLongArray("blocks");
        long[] newList = new long[posList.length + 1];
        System.arraycopy(posList, 0, newList, 0, posList.length);
        newList[posList.length] = packed;
        stack.getOrCreateTagElement("custommachinery").putLongArray("blocks", newList);
    }

    public static void removeSelectedBlock(ItemStack stack, BlockPos pos) {
        long packed = pos.asLong();
        long[] posList = stack.getOrCreateTagElement("custommachinery").getLongArray("blocks");
        long[] newList = Arrays.stream(posList).filter(l -> l != packed).toArray();
        stack.getOrCreateTagElement("custommachinery").putLongArray("blocks", newList);
    }

    private void finishStructure(ItemStack stack, BlockPos machinePos, Direction machineFacing, ServerPlayer player) {
        List<BlockPos> blocks = getSelectedBlocks(stack);
        blocks.add(machinePos);
        if (blocks.size() <= 1) {
            player.sendSystemMessage(Component.translatable("custommachinery.structure_creator.no_blocks"));
        } else {
            Level world = player.m_9236_();
            PartialBlockState[][][] states = this.getStructureArray(blocks, machineFacing, world);
            HashBiMap<Character, PartialBlockState> keys = HashBiMap.create();
            AtomicInteger charIndex = new AtomicInteger(97);
            Arrays.stream(states).flatMap(Arrays::stream).flatMap(Arrays::stream).filter(state -> !(state.getBlockState().m_60734_() instanceof CustomMachineBlock) && state != PartialBlockState.ANY).distinct().forEach(state -> {
                if (charIndex.get() == 109) {
                    charIndex.incrementAndGet();
                }
                keys.put((char) charIndex.getAndIncrement(), state);
                if (charIndex.get() == 122) {
                    charIndex.set(65);
                }
            });
            List<List<String>> pattern = new ArrayList();
            for (int i = 0; i < states.length; i++) {
                List<String> floor = new ArrayList();
                for (int j = 0; j < states[i].length; j++) {
                    StringBuilder row = new StringBuilder();
                    for (int k = 0; k < states[i][j].length; k++) {
                        PartialBlockState partial = states[i][j][k];
                        char key;
                        if (partial.getBlockState().m_60734_() instanceof CustomMachineBlock) {
                            key = 'm';
                        } else if (partial == PartialBlockState.ANY) {
                            key = ' ';
                        } else if (keys.containsValue(partial)) {
                            key = (Character) keys.inverse().get(partial);
                        } else {
                            key = '?';
                        }
                        row.append(key);
                    }
                    floor.add(row.toString());
                }
                pattern.add(floor);
            }
            JsonElement keysJson = (JsonElement) KEYS_CODEC.encodeStart(JsonOps.INSTANCE, keys).result().orElseThrow(IllegalStateException::new);
            JsonElement patternJson = (JsonElement) PATTERN_CODEC.encodeStart(JsonOps.INSTANCE, pattern).result().orElseThrow(IllegalStateException::new);
            JsonObject both = new JsonObject();
            both.add("keys", keysJson);
            both.add("pattern", patternJson);
            String ctKubeString = ".requireStructure(" + patternJson.toString() + ", " + keysJson.toString() + ")";
            Component jsonText = Component.literal("[JSON]").withStyle(style -> style.applyFormats(ChatFormatting.YELLOW).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(both.toString()))).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, both.toString())));
            Component prettyJsonText = Component.literal("[PRETTY JSON]").withStyle(style -> style.applyFormats(ChatFormatting.GOLD).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(GSON.toJson(both)))).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, GSON.toJson(both))));
            Component crafttweakerText = Component.literal("[CRAFTTWEAKER]").withStyle(style -> style.applyFormats(ChatFormatting.AQUA).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(ctKubeString))).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, ctKubeString)));
            Component kubeJSText = Component.literal("[KUBEJS]").withStyle(style -> style.applyFormats(ChatFormatting.DARK_PURPLE).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(ctKubeString))).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, ctKubeString)));
            Component message = Component.translatable("custommachinery.structure_creator.message", jsonText, prettyJsonText, crafttweakerText, kubeJSText);
            player.sendSystemMessage(message);
        }
    }

    private PartialBlockState[][][] getStructureArray(List<BlockPos> blocks, Direction machineFacing, Level world) {
        int minX = blocks.stream().mapToInt(Vec3i::m_123341_).min().orElseThrow(IllegalStateException::new);
        int maxX = blocks.stream().mapToInt(Vec3i::m_123341_).max().orElseThrow(IllegalStateException::new);
        int minY = blocks.stream().mapToInt(Vec3i::m_123342_).min().orElseThrow(IllegalStateException::new);
        int maxY = blocks.stream().mapToInt(Vec3i::m_123342_).max().orElseThrow(IllegalStateException::new);
        int minZ = blocks.stream().mapToInt(Vec3i::m_123343_).min().orElseThrow(IllegalStateException::new);
        int maxZ = blocks.stream().mapToInt(Vec3i::m_123343_).max().orElseThrow(IllegalStateException::new);
        PartialBlockState[][][] states;
        if (machineFacing.getAxis() == Direction.Axis.X) {
            states = new PartialBlockState[maxY - minY + 1][maxX - minX + 1][maxZ - minZ + 1];
        } else {
            states = new PartialBlockState[maxY - minY + 1][maxZ - minZ + 1][maxX - minX + 1];
        }
        AABB box = new AABB((double) minX, (double) minY, (double) minZ, (double) maxX, (double) maxY, (double) maxZ);
        Map<BlockState, PartialBlockState> cache = new HashMap();
        BlockPos.betweenClosedStream(box).forEach(p -> {
            BlockState state = world.getBlockState(p);
            PartialBlockState partial;
            if (!blocks.contains(p)) {
                partial = PartialBlockState.ANY;
            } else if (cache.containsKey(state)) {
                partial = (PartialBlockState) cache.get(state);
            } else {
                partial = new PartialBlockState(state, Lists.newArrayList(state.m_61147_()), (CompoundTag) Optional.ofNullable(world.getBlockEntity(p)).map(BlockEntity::m_187480_).orElse(null));
                cache.put(state, partial);
            }
            switch(machineFacing) {
                case EAST:
                    states[p.m_123342_() - minY][p.m_123341_() - minX][maxZ - p.m_123343_()] = partial;
                    break;
                case WEST:
                    states[p.m_123342_() - minY][maxX - p.m_123341_()][p.m_123343_() - minZ] = partial;
                    break;
                case SOUTH:
                    states[p.m_123342_() - minY][p.m_123343_() - minZ][p.m_123341_() - minX] = partial;
                    break;
                case NORTH:
                    states[p.m_123342_() - minY][maxZ - p.m_123343_()][maxX - p.m_123341_()] = partial;
            }
        });
        return states;
    }
}