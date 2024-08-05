package org.violetmoon.quark.content.automation.module;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "automation")
public class DispensersPlaceBlocksModule extends ZetaModule {

    @Config
    public static List<String> blacklist = Lists.newArrayList(new String[] { "minecraft:water", "minecraft:lava", "minecraft:fire" });

    @Config(description = "Set to false to refrain from registering any behaviors for blocks that have optional dispense behaviors already set.\nAn optional behavior is one that will defer to the generic dispense item behavior if its condition fails.\ne.g. the Shulker Box behavior is optional, because it'll throw out the item if it fails, whereas TNT is not optional.\nIf true, it'll attempt to use the previous behavior before trying to place the block in the world.\nRequires a game restart to re-apply.")
    public static boolean wrapExistingBehaviors = true;

    @LoadEvent
    public void setup(ZCommonSetup e) {
        if (this.enabled) {
            DispensersPlaceBlocksModule.BlockBehavior baseBehavior = new DispensersPlaceBlocksModule.BlockBehavior();
            e.enqueueWork(() -> {
                Map<Item, DispenseItemBehavior> registry = DispenserBlock.DISPENSER_REGISTRY;
                for (Block b : BuiltInRegistries.BLOCK) {
                    ResourceLocation res = BuiltInRegistries.BLOCK.getKey(b);
                    if (!blacklist.contains(Objects.toString(res))) {
                        Item item = b.asItem();
                        if (item instanceof BlockItem) {
                            DispenseItemBehavior original = (DispenseItemBehavior) registry.get(item);
                            boolean exists = original != null && original.getClass() != DefaultDispenseItemBehavior.class;
                            if (!(original instanceof DispensersPlaceBlocksModule.BlockBehavior)) {
                                if (exists) {
                                    if (wrapExistingBehaviors && original instanceof OptionalDispenseItemBehavior opt) {
                                        registry.put(item, new DispensersPlaceBlocksModule.BlockBehavior(opt));
                                    }
                                } else {
                                    registry.put(item, baseBehavior);
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    public static class BlockBehavior extends OptionalDispenseItemBehavior {

        private final OptionalDispenseItemBehavior wrapped;

        public BlockBehavior() {
            this(null);
        }

        public BlockBehavior(OptionalDispenseItemBehavior wrapped) {
            this.wrapped = wrapped;
        }

        @NotNull
        @Override
        public ItemStack execute(BlockSource source, ItemStack stack) {
            if (this.wrapped != null) {
                ItemStack wrappedResult = this.wrapped.m_6115_(source, stack);
                if (this.wrapped.isSuccess()) {
                    this.m_123573_(true);
                    return wrappedResult;
                }
            }
            this.m_123573_(false);
            Direction direction = (Direction) source.getBlockState().m_61143_(DispenserBlock.FACING);
            Direction against = direction;
            BlockPos pos = source.getPos().relative(direction);
            if (stack.getItem() instanceof BlockItem item) {
                Block block = item.getBlock();
                if (block instanceof StairBlock && direction.getAxis() != Direction.Axis.Y) {
                    direction = direction.getOpposite();
                } else if (block instanceof SlabBlock) {
                    against = Direction.UP;
                }
                this.m_123573_(item.place(new DispensersPlaceBlocksModule.NotStupidDirectionalPlaceContext(source.getLevel(), pos, direction, stack, against)).consumesAction());
            }
            return stack;
        }
    }

    private static class NotStupidDirectionalPlaceContext extends DirectionalPlaceContext {

        protected boolean replaceClicked;

        protected Direction direction;

        public NotStupidDirectionalPlaceContext(Level worldIn, BlockPos pos, Direction facing, ItemStack stack, Direction against) {
            super(worldIn, pos, facing, stack, against);
            this.replaceClicked = worldIn.getBlockState(this.m_43718_().getBlockPos()).m_60629_(this);
            this.direction = facing;
        }

        @Override
        public boolean canPlace() {
            return this.replaceClicked;
        }

        @NotNull
        @Override
        public Direction getNearestLookingDirection() {
            return this.direction.getOpposite();
        }
    }
}