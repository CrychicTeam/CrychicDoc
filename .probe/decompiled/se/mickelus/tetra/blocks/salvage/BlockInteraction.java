package se.mickelus.tetra.blocks.salvage;

import com.google.common.base.Predicates;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.mutil.util.RotationHelper;
import se.mickelus.tetra.advancements.BlockInteractionCriterion;
import se.mickelus.tetra.blocks.PropertyMatcher;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.properties.PropertyHelper;

@ParametersAreNonnullByDefault
public class BlockInteraction {

    public ToolAction requiredTool;

    public int requiredLevel;

    public boolean alwaysReveal = true;

    public Direction face;

    public float minX;

    public float minY;

    public float maxX;

    public float maxY;

    public Predicate<BlockState> predicate;

    public InteractionOutcome outcome;

    protected boolean applyUsageEffects = true;

    public <V extends Comparable<V>> BlockInteraction(ToolAction requiredTool, int requiredLevel, Direction face, float minX, float maxX, float minY, float maxY, InteractionOutcome outcome) {
        this.requiredTool = requiredTool;
        this.requiredLevel = requiredLevel;
        this.face = face;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.outcome = outcome;
    }

    public BlockInteraction(ToolAction requiredTool, int requiredLevel, Direction face, float minX, float maxX, float minY, float maxY, Predicate<BlockState> predicate, InteractionOutcome outcome) {
        this(requiredTool, requiredLevel, face, minX, maxX, minY, maxY, outcome);
        this.predicate = predicate;
    }

    public <V extends Comparable<V>> BlockInteraction(ToolAction requiredTool, int requiredLevel, Direction face, float minX, float maxX, float minY, float maxY, Property<V> property, V propertyValue, InteractionOutcome outcome) {
        this(requiredTool, requiredLevel, face, minX, maxX, minY, maxY, new PropertyMatcher().where(property, Predicates.equalTo(propertyValue)), outcome);
    }

    public static InteractionResult attemptInteraction(Level world, BlockState blockState, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTrace) {
        ItemStack heldStack = player.m_21120_(hand);
        Collection<ToolAction> availableTools = PropertyHelper.getItemTools(heldStack);
        AABB boundingBox = blockState.m_60651_(world, pos, CollisionContext.of(player)).bounds();
        double hitU = 16.0 * getHitU(rayTrace.getDirection(), boundingBox, rayTrace.m_82450_().x - (double) pos.m_123341_(), rayTrace.m_82450_().y - (double) pos.m_123342_(), rayTrace.m_82450_().z - (double) pos.m_123343_());
        double hitV = 16.0 * getHitV(rayTrace.getDirection(), boundingBox, rayTrace.m_82450_().x - (double) pos.m_123341_(), rayTrace.m_82450_().y - (double) pos.m_123342_(), rayTrace.m_82450_().z - (double) pos.m_123343_());
        BlockInteraction possibleInteraction = (BlockInteraction) ((Stream) CastOptional.cast(blockState.m_60734_(), IInteractiveBlock.class).map(block -> block.getPotentialInteractions(world, pos, blockState, rayTrace.getDirection(), availableTools)).map(Arrays::stream).orElseGet(Stream::empty)).filter(interaction -> interaction.isWithinBounds(hitU, hitV)).filter(interaction -> PropertyHelper.getItemToolLevel(heldStack, interaction.requiredTool) >= interaction.requiredLevel).findFirst().orElse(null);
        if (possibleInteraction != null) {
            if (InteractionHand.MAIN_HAND == hand) {
                if ((double) player.getAttackStrengthScale(0.0F) < 0.8) {
                    if (player.m_21206_().isEmpty()) {
                        player.resetAttackStrengthTicker();
                        return InteractionResult.FAIL;
                    }
                    return InteractionResult.PASS;
                }
            } else if (player.getCooldowns().isOnCooldown(heldStack.getItem())) {
                return InteractionResult.FAIL;
            }
            possibleInteraction.applyOutcome(world, pos, blockState, player, hand, rayTrace.getDirection());
            if (availableTools.contains(possibleInteraction.requiredTool) && heldStack.isDamageableItem()) {
                if (heldStack.getItem() instanceof IModularItem) {
                    IModularItem item = (IModularItem) heldStack.getItem();
                    item.applyDamage(2, heldStack, player);
                    if (possibleInteraction.applyUsageEffects) {
                        item.applyUsageEffects(player, heldStack, (double) (possibleInteraction.requiredLevel * 2));
                    }
                } else {
                    heldStack.hurtAndBreak(2, player, breaker -> breaker.m_21190_(breaker.m_7655_()));
                }
            }
            if (player instanceof ServerPlayer) {
                BlockState newState = world.getBlockState(pos);
                BlockInteractionCriterion.trigger((ServerPlayer) player, blockState, newState, possibleInteraction.requiredTool, possibleInteraction.requiredLevel);
            }
            if (InteractionHand.MAIN_HAND == hand) {
                player.resetAttackStrengthTicker();
            } else {
                int cooldown = (Integer) CastOptional.cast(heldStack.getItem(), ItemModularHandheld.class).map(item -> (int) (20.0 * item.getCooldownBase(heldStack))).orElse(10);
                player.getCooldowns().addCooldown(heldStack.getItem(), cooldown);
            }
            if (player.m_9236_().isClientSide) {
                InteractiveBlockOverlay.markDirty();
            }
            return InteractionResult.sidedSuccess(player.m_9236_().isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    public static BlockInteraction getInteractionAtPoint(Player player, BlockState blockState, BlockPos pos, Direction hitFace, double hitX, double hitY, double hitZ) {
        AABB boundingBox = blockState.m_60808_(player.m_9236_(), pos).bounds();
        double hitU = getHitU(hitFace, boundingBox, hitX, hitY, hitZ);
        double hitV = getHitV(hitFace, boundingBox, hitX, hitY, hitZ);
        return (BlockInteraction) ((Stream) CastOptional.cast(blockState.m_60734_(), IInteractiveBlock.class).map(block -> block.getPotentialInteractions(player.m_9236_(), pos, blockState, hitFace, PropertyHelper.getPlayerTools(player))).map(Arrays::stream).orElseGet(Stream::empty)).filter(interaction -> interaction.isWithinBounds(hitU * 16.0, hitV * 16.0)).findFirst().orElse(null);
    }

    private static double getHitU(Direction facing, AABB boundingBox, double hitX, double hitY, double hitZ) {
        switch(facing) {
            case DOWN:
                return boundingBox.maxX - hitX;
            case UP:
                return boundingBox.maxX - hitX;
            case NORTH:
                return boundingBox.maxX - hitX;
            case SOUTH:
                return hitX - boundingBox.minX;
            case WEST:
                return hitZ - boundingBox.minZ;
            case EAST:
                return boundingBox.maxZ - hitZ;
            default:
                return 0.0;
        }
    }

    private static double getHitV(Direction facing, AABB boundingBox, double hitX, double hitY, double hitZ) {
        switch(facing) {
            case DOWN:
                return boundingBox.maxZ - hitZ;
            case UP:
                return boundingBox.maxZ - hitZ;
            case NORTH:
                return boundingBox.maxY - hitY;
            case SOUTH:
                return boundingBox.maxY - hitY;
            case WEST:
                return boundingBox.maxY - hitY;
            case EAST:
                return boundingBox.maxY - hitY;
            default:
                return 0.0;
        }
    }

    public static List<ItemStack> getLoot(ResourceLocation lootTable, Player player, InteractionHand hand, ServerLevel world, BlockState blockState) {
        LootTable table = world.getServer().getLootData().m_278676_(lootTable);
        LootParams context = new LootParams.Builder(world).withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player).withParameter(LootContextParams.BLOCK_STATE, blockState).withParameter(LootContextParams.TOOL, player.m_21120_(hand)).withParameter(LootContextParams.ORIGIN, player.m_20182_()).create(LootContextParamSets.BLOCK);
        return table.getRandomItems(context);
    }

    public static List<ItemStack> getLoot(ResourceLocation lootTable, ServerLevel world, BlockPos pos, BlockState blockState) {
        LootTable table = world.getServer().getLootData().m_278676_(lootTable);
        LootParams context = new LootParams.Builder(world).withParameter(LootContextParams.BLOCK_STATE, blockState).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).create(LootContextParamSets.BLOCK);
        return table.getRandomItems(context);
    }

    public static void dropLoot(ResourceLocation lootTable, @Nullable Player player, @Nullable InteractionHand hand, ServerLevel world, BlockState blockState) {
        getLoot(lootTable, player, hand, world, blockState).forEach(itemStack -> {
            if (!player.getInventory().add(itemStack)) {
                player.drop(itemStack, false);
            }
        });
    }

    public static void dropLoot(ResourceLocation lootTable, ServerLevel world, BlockPos pos, BlockState blockState) {
        getLoot(lootTable, world, pos, blockState).forEach(itemStack -> Block.popResource(world, pos, itemStack));
    }

    public boolean applicableForBlock(Level world, BlockPos pos, BlockState blockState) {
        return this.predicate.test(blockState);
    }

    public boolean isWithinBounds(double x, double y) {
        return (double) this.minX <= x && x <= (double) this.maxX && (double) this.minY <= y && y <= (double) this.maxY;
    }

    public boolean isPotentialInteraction(Level world, BlockPos pos, BlockState blockState, Direction hitFace, Collection<ToolAction> availableTools) {
        return this.isPotentialInteraction(world, pos, blockState, Direction.NORTH, hitFace, availableTools);
    }

    public boolean isPotentialInteraction(Level world, BlockPos pos, BlockState blockState, Direction blockFacing, Direction hitFace, Collection<ToolAction> availableTools) {
        return this.applicableForBlock(world, pos, blockState) && RotationHelper.rotationFromFacing(blockFacing).rotate(this.face).equals(hitFace) && (this.alwaysReveal || availableTools.contains(this.requiredTool));
    }

    public void applyOutcome(Level world, BlockPos pos, BlockState blockState, @Nullable Player player, @Nullable InteractionHand hand, Direction hitFace) {
        this.outcome.apply(world, pos, blockState, player, hand, hitFace);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            BlockInteraction that = (BlockInteraction) o;
            return this.requiredLevel == that.requiredLevel && Float.compare(that.minX, this.minX) == 0 && Float.compare(that.minY, this.minY) == 0 && Float.compare(that.maxX, this.maxX) == 0 && Float.compare(that.maxY, this.maxY) == 0 && Objects.equals(this.requiredTool, that.requiredTool) && this.face == that.face;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.requiredTool, this.requiredLevel, this.face, this.minX, this.minY, this.maxX, this.maxY });
    }
}