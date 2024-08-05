package snownee.kiwi.customization.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DataResult.PartialResult;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ExportCreativeTabsCommand {

    public static void register(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.then(Commands.literal("creative_tabs").then(Commands.argument("signPos", BlockPosArgument.blockPos()).executes(ctx -> exportCreativeTabs((CommandSourceStack) ctx.getSource(), BlockPosArgument.getLoadedBlockPos(ctx, "signPos")))));
    }

    private static int exportCreativeTabs(CommandSourceStack source, BlockPos pos) {
        DataResult<Pair<ResourceLocation, Direction>> result = checkIsValidSign(source, pos);
        if (result.error().isPresent()) {
            source.sendFailure(Component.literal(((PartialResult) result.error().get()).message()));
            return 0;
        } else {
            Direction direction = (Direction) ((Pair) result.result().orElseThrow()).getSecond();
            ServerLevel level = source.getLevel();
            List<Pair<BlockPos, Container>> containers = findContainerSequence(level, pos, direction);
            Map<String, Collection<String>> data = Maps.newLinkedHashMap();
            try {
                LinkedHashSet<String> items = collectItems(source, containers);
                if (!items.isEmpty()) {
                    data.put(((ResourceLocation) ((Pair) result.result().orElseThrow()).getFirst()).toString(), items);
                }
                for (Direction leftOrRight : List.of(Rotation.CLOCKWISE_90.rotate(direction), Rotation.COUNTERCLOCKWISE_90.rotate(direction))) {
                    BlockPos.MutableBlockPos mutablePos = pos.mutable().move(leftOrRight);
                    int failed = 0;
                    while (failed < 5) {
                        mutablePos.move(leftOrRight);
                        DataResult<Pair<ResourceLocation, Direction>> result2 = checkIsValidSign(source, mutablePos);
                        if (result2.error().isPresent()) {
                            failed++;
                        } else {
                            Pair<ResourceLocation, Direction> pair = (Pair<ResourceLocation, Direction>) result2.result().orElseThrow();
                            if (direction != pair.getSecond()) {
                                failed++;
                            } else {
                                String tabId = ((ResourceLocation) pair.getFirst()).toString();
                                if (data.containsKey(tabId)) {
                                    failed = 0;
                                } else {
                                    containers = findContainerSequence(level, mutablePos, direction);
                                    items = collectItems(source, containers);
                                    if (!items.isEmpty()) {
                                        data.put(tabId, items);
                                    }
                                    failed = 0;
                                }
                            }
                        }
                    }
                }
            } catch (RuntimeException var18) {
                source.sendFailure(Component.literal(var18.getMessage()));
                return 0;
            }
            try {
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("exported_creative_tabs.json"));
                try {
                    new Gson().toJson(data, writer);
                } catch (Throwable var16) {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (Throwable var15) {
                            var16.addSuppressed(var15);
                        }
                    }
                    throw var16;
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception var17) {
                source.sendFailure(Component.literal(var17.getMessage()));
                return 0;
            }
            source.sendSuccess(() -> Component.literal("Creative tabs exported"), false);
            return 1;
        }
    }

    private static List<Pair<BlockPos, Container>> findContainerSequence(ServerLevel level, BlockPos pos, Direction direction) {
        BlockPos.MutableBlockPos mutablePos = pos.mutable();
        List<Pair<BlockPos, Container>> containers = Lists.newArrayList();
        HashSet<Container> set = Sets.newHashSet();
        while (true) {
            mutablePos.move(direction);
            if (!(level.m_7702_(mutablePos) instanceof BaseContainerBlockEntity blockEntity)) {
                return containers;
            }
            Container container = blockEntity;
            if (blockEntity.m_58900_().m_60734_() instanceof ChestBlock chestBlock) {
                if (ChestBlock.getConnectedDirection(blockEntity.m_58900_()) == direction.getOpposite()) {
                    continue;
                }
                container = ChestBlock.getContainer(chestBlock, blockEntity.m_58900_(), (Level) Objects.requireNonNull(blockEntity.m_58904_()), mutablePos, true);
            }
            if (set.add(container)) {
                containers.add(Pair.of(blockEntity.m_58899_(), container));
            }
        }
    }

    private static LinkedHashSet<String> collectItems(CommandSourceStack source, List<Pair<BlockPos, Container>> pairs) {
        ServerLevel level = source.getLevel();
        LinkedHashSet<String> items = Sets.newLinkedHashSet();
        for (Pair<BlockPos, Container> pair : pairs) {
            level.m_46597_(((BlockPos) pair.getFirst()).below(), Blocks.YELLOW_WOOL.defaultBlockState());
        }
        for (Pair<BlockPos, Container> pair : pairs) {
            Container container = (Container) pair.getSecond();
            for (int i = 0; i < container.getContainerSize(); i++) {
                ItemStack stack = container.getItem(i);
                if (!stack.isEmpty()) {
                    String item = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
                    if (!items.add(item)) {
                        for (Pair<BlockPos, Container> pair1 : pairs) {
                            if (((Container) pair1.getSecond()).hasAnyMatching($ -> ItemStack.isSameItemSameTags($, stack))) {
                                level.m_46597_(((BlockPos) pair1.getFirst()).below(), Blocks.RED_WOOL.defaultBlockState());
                            }
                        }
                        throw new IllegalStateException("Duplicate item: %s (%s)".formatted(stack.getHoverName().getString(), item));
                    }
                }
            }
            level.m_46597_(((BlockPos) pair.getFirst()).below(), Blocks.LIME_WOOL.defaultBlockState());
        }
        return items;
    }

    private static DataResult<Pair<ResourceLocation, Direction>> checkIsValidSign(CommandSourceStack source, BlockPos pos) {
        ServerLevel level = source.getLevel();
        BlockState blockState = level.m_8055_(pos);
        if (blockState.m_60734_() instanceof SignBlock block) {
            if (level.m_7702_(pos) instanceof SignBlockEntity blockEntity) {
                String signText = String.join("", Arrays.stream(blockEntity.getFrontText().getMessages(false)).map(Component::getString).toList());
                if (signText.isBlank()) {
                    return DataResult.error(() -> "The sign is empty");
                } else {
                    ResourceLocation tabId = ResourceLocation.tryParse(signText);
                    if (tabId == null) {
                        return DataResult.error(() -> "The sign text is not a valid resource location");
                    } else {
                        Direction direction = Direction.fromYRot((double) block.getYRotationDegrees(blockState)).getOpposite();
                        return DataResult.success(Pair.of(tabId, direction));
                    }
                }
            } else {
                return DataResult.error(() -> "Target block is not a sign");
            }
        } else {
            return DataResult.error(() -> "Target block is not a sign");
        }
    }
}