package snownee.lychee.core.contextual;

import com.google.gson.JsonObject;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.FluidPredicate;
import net.minecraft.advancements.critereon.LightPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.ContextualConditionTypes;
import snownee.lychee.LycheeLootContextParams;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.def.BlockPredicateHelper;
import snownee.lychee.core.def.BoundsHelper;
import snownee.lychee.core.def.LocationPredicateHelper;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.mixin.BlockPredicateAccess;
import snownee.lychee.mixin.LightPredicateAccess;
import snownee.lychee.mixin.LocationCheckAccess;
import snownee.lychee.mixin.LocationPredicateAccess;
import snownee.lychee.util.ClientProxy;
import snownee.lychee.util.CommonProxy;

public record Location(LocationCheck check) implements ContextualCondition {

    private static final Location.Rule X = new Location.PosRule("x", LocationPredicateAccess::getX, Vec3::m_7096_);

    private static final Location.Rule Y = new Location.PosRule("y", LocationPredicateAccess::getY, Vec3::m_7098_);

    private static final Location.Rule Z = new Location.PosRule("z", LocationPredicateAccess::getZ, Vec3::m_7094_);

    private static final Location.Rule DIMENSION = new Location.DimensionRule();

    private static final Location.Rule FEATURE = new Location.FeatureRule();

    private static final Location.Rule BIOME = new Location.BiomeRule();

    private static final Location.Rule BLOCK = new Location.BlockRule();

    private static final Location.Rule FLUID = new Location.FluidRule();

    private static final Location.Rule LIGHT = new Location.LightRule();

    private static final Location.Rule SMOKEY = new Location.SmokeyRule();

    public static final Location.Rule[] RULES = new Location.Rule[] { X, Y, Z, DIMENSION, FEATURE, BIOME, BLOCK, FLUID, LIGHT, SMOKEY };

    @Override
    public ContextualConditionType<? extends ContextualCondition> getType() {
        return ContextualConditionTypes.LOCATION;
    }

    @Override
    public int test(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        if (ctx.getLevel().isClientSide) {
            return this.testClient(ctx.getLevel(), ctx.getParamOrNull(LycheeLootContextParams.BLOCK_POS), ctx.getParamOrNull(LootContextParams.ORIGIN)) == InteractionResult.SUCCESS ? times : 0;
        } else {
            return this.check.test(ctx.toLootContext()) ? times : 0;
        }
    }

    @Override
    public InteractionResult testInTooltips(Level level, @Nullable Player player) {
        if (player == null) {
            return InteractionResult.PASS;
        } else {
            LocationCheckAccess checkAccess = (LocationCheckAccess) this.check;
            if (!BlockPos.ZERO.equals(checkAccess.getOffset())) {
                return InteractionResult.PASS;
            } else {
                Vec3 vec = player.m_20182_();
                BlockPos pos = player.m_20183_();
                return this.testClient(level, pos, vec);
            }
        }
    }

    public InteractionResult testClient(Level level, BlockPos pos, Vec3 vec) {
        LocationCheckAccess checkAccess = (LocationCheckAccess) this.check;
        BlockPos offset = checkAccess.getOffset();
        if (!BlockPos.ZERO.equals(offset)) {
            pos = pos.offset(offset.m_123341_(), offset.m_123342_(), offset.m_123343_());
        }
        LocationPredicateAccess access = (LocationPredicateAccess) checkAccess.getPredicate();
        boolean hasPass = false;
        for (Location.Rule rule : RULES) {
            if (!rule.isAny(access)) {
                InteractionResult result = rule.testClient(access, level, pos, vec);
                if (result == InteractionResult.FAIL) {
                    return result;
                }
                if (result == InteractionResult.PASS) {
                    hasPass = true;
                }
            }
        }
        return hasPass ? InteractionResult.PASS : InteractionResult.SUCCESS;
    }

    @Override
    public void appendTooltips(List<Component> tooltips, Level level, @Nullable Player player, int indent, boolean inverted) {
        LocationCheckAccess checkAccess = (LocationCheckAccess) this.check;
        LocationPredicateAccess access = (LocationPredicateAccess) checkAccess.getPredicate();
        boolean test = false;
        Vec3 vec = null;
        BlockPos pos = null;
        String key = this.makeDescriptionId(inverted);
        boolean noOffset = BlockPos.ZERO.equals(checkAccess.getOffset());
        if (!noOffset) {
            BlockPos offset = checkAccess.getOffset();
            MutableComponent content = Component.translatable(key, offset.m_123341_(), offset.m_123342_(), offset.m_123343_()).withStyle(ChatFormatting.GRAY);
            InteractionResult result = this.testInTooltips(level, player);
            ContextualCondition.desc(tooltips, result, indent, content);
            indent++;
        }
        if (noOffset && player != null) {
            test = true;
            vec = player.m_20182_();
            pos = player.m_20183_();
        }
        for (Location.Rule rule : RULES) {
            if (!rule.isAny(access)) {
                InteractionResult result = InteractionResult.PASS;
                if (test) {
                    result = rule.testClient(access, level, pos, vec);
                }
                rule.appendTooltips(tooltips, indent, key, access, result);
            }
        }
    }

    @Override
    public MutableComponent getDescription(boolean inverted) {
        return Component.translatable(this.makeDescriptionId(inverted));
    }

    @Override
    public int showingCount() {
        int c = 0;
        LocationCheckAccess checkAccess = (LocationCheckAccess) this.check;
        LocationPredicateAccess access = (LocationPredicateAccess) checkAccess.getPredicate();
        for (Location.Rule rule : RULES) {
            if (!rule.isAny(access)) {
                c++;
            }
        }
        return c;
    }

    private static class BiomeRule implements Location.Rule {

        @Override
        public String getName() {
            return "biome";
        }

        @Override
        public boolean isAny(LocationPredicateAccess access) {
            return access.getBiome() == null && ((LocationPredicateHelper) access).lychee$getBiomeTag() == null;
        }

        @Override
        public InteractionResult testClient(LocationPredicateAccess access, Level level, BlockPos pos, Vec3 vec) {
            Holder<Biome> biome = level.m_204166_(pos);
            if (access.getBiome() != null && biome.is(access.getBiome().location())) {
                return InteractionResult.SUCCESS;
            } else {
                TagKey<Biome> tag = ((LocationPredicateHelper) access).lychee$getBiomeTag();
                return tag != null && biome.is(tag) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
            }
        }

        @Override
        public void appendTooltips(List<Component> tooltips, int indent, String key, LocationPredicateAccess access, InteractionResult result) {
            String valueKey;
            if (access.getBiome() != null) {
                valueKey = Util.makeDescriptionId("biome", access.getBiome().location());
            } else {
                valueKey = Util.makeDescriptionId("biomeTag", ((LocationPredicateHelper) access).lychee$getBiomeTag().location());
            }
            MutableComponent name = Component.translatable(valueKey).withStyle(ChatFormatting.WHITE);
            ContextualCondition.desc(tooltips, result, indent, Component.translatable(key + "." + this.getName(), name));
        }
    }

    private static class BlockRule implements Location.Rule {

        @Override
        public String getName() {
            return "block";
        }

        @Override
        public boolean isAny(LocationPredicateAccess access) {
            return access.getBlock() == BlockPredicate.ANY;
        }

        @Override
        public void appendTooltips(List<Component> tooltips, int indent, String key, LocationPredicateAccess access, InteractionResult result) {
            Block block = CommonProxy.getCycledItem(List.copyOf(BlockPredicateHelper.getMatchedBlocks(access.getBlock())), Blocks.AIR, 1000);
            MutableComponent name = block.getName().withStyle(ChatFormatting.WHITE);
            BlockPredicateAccess blockAccess = (BlockPredicateAccess) access.getBlock();
            if (blockAccess.getProperties() != StatePropertiesPredicate.ANY || blockAccess.getNbt() != NbtPredicate.ANY) {
                name.append("*");
            }
            ContextualCondition.desc(tooltips, result, indent, Component.translatable(key + "." + this.getName(), name));
        }

        @Override
        public InteractionResult testClient(LocationPredicateAccess access, Level level, BlockPos pos, Vec3 vec) {
            return BlockPredicateHelper.fastMatch(access.getBlock(), level.getBlockState(pos), () -> level.getBlockEntity(pos)) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        }
    }

    private static class DimensionRule implements Location.Rule {

        @Override
        public String getName() {
            return "dimension";
        }

        @Override
        public boolean isAny(LocationPredicateAccess access) {
            return access.getDimension() == null;
        }

        @Override
        public InteractionResult testClient(LocationPredicateAccess access, Level level, BlockPos pos, Vec3 vec) {
            return CommonProxy.interactionResult(level.dimension() == access.getDimension());
        }

        @Override
        public void appendTooltips(List<Component> tooltips, int indent, String key, LocationPredicateAccess access, InteractionResult result) {
            MutableComponent name = ClientProxy.getDimensionDisplayName(access.getDimension()).withStyle(ChatFormatting.WHITE);
            ContextualCondition.desc(tooltips, result, indent, Component.translatable(key + "." + this.getName(), name));
        }
    }

    private static class FeatureRule implements Location.Rule {

        @Override
        public String getName() {
            return "feature";
        }

        @Override
        public boolean isAny(LocationPredicateAccess access) {
            return access.getStructure() == null;
        }

        @Override
        public void appendTooltips(List<Component> tooltips, int indent, String key, LocationPredicateAccess access, InteractionResult result) {
            MutableComponent name = ClientProxy.getStructureDisplayName(access.getStructure().location()).withStyle(ChatFormatting.WHITE);
            ContextualCondition.desc(tooltips, result, indent, Component.translatable(key + "." + this.getName(), name));
        }
    }

    private static class FluidRule implements Location.Rule {

        @Override
        public String getName() {
            return "fluid";
        }

        @Override
        public boolean isAny(LocationPredicateAccess access) {
            return access.getFluid() == FluidPredicate.ANY;
        }

        @Override
        public void appendTooltips(List<Component> tooltips, int indent, String key, LocationPredicateAccess access, InteractionResult result) {
        }
    }

    private static class LightRule implements Location.Rule {

        @Override
        public String getName() {
            return "light";
        }

        @Override
        public boolean isAny(LocationPredicateAccess access) {
            return access.getLight() == LightPredicate.ANY;
        }

        @Override
        public InteractionResult testClient(LocationPredicateAccess access, Level level, BlockPos pos, Vec3 vec) {
            int light = level.m_46803_(pos);
            return CommonProxy.interactionResult(((LightPredicateAccess) access.getLight()).getComposite().matches(light));
        }

        @Override
        public void appendTooltips(List<Component> tooltips, int indent, String key, LocationPredicateAccess access, InteractionResult result) {
            MutableComponent bounds = BoundsHelper.getDescription(((LightPredicateAccess) access.getLight()).getComposite()).withStyle(ChatFormatting.WHITE);
            ContextualCondition.desc(tooltips, result, indent, Component.translatable(key + "." + this.getName(), bounds));
        }
    }

    private static record PosRule(String name, Function<LocationPredicateAccess, MinMaxBounds.Doubles> boundsGetter, Function<Vec3, Double> valueGetter) implements Location.Rule {

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public boolean isAny(LocationPredicateAccess access) {
            return ((MinMaxBounds.Doubles) this.boundsGetter.apply(access)).m_55327_();
        }

        @Override
        public InteractionResult testClient(LocationPredicateAccess access, Level level, BlockPos pos, Vec3 vec) {
            return CommonProxy.interactionResult(((MinMaxBounds.Doubles) this.boundsGetter.apply(access)).matches((Double) this.valueGetter.apply(vec)));
        }

        @Override
        public void appendTooltips(List<Component> tooltips, int indent, String key, LocationPredicateAccess access, InteractionResult result) {
            ContextualCondition.desc(tooltips, result, indent, Component.translatable(key + "." + this.getName(), BoundsHelper.getDescription((MinMaxBounds<?>) this.boundsGetter.apply(access)).withStyle(ChatFormatting.WHITE)));
        }
    }

    public interface Rule {

        String getName();

        boolean isAny(LocationPredicateAccess var1);

        default InteractionResult testClient(LocationPredicateAccess access, Level level, BlockPos pos, Vec3 vec) {
            return InteractionResult.PASS;
        }

        void appendTooltips(List<Component> var1, int var2, String var3, LocationPredicateAccess var4, InteractionResult var5);
    }

    private static class SmokeyRule implements Location.Rule {

        @Override
        public String getName() {
            return "smokey";
        }

        @Override
        public boolean isAny(LocationPredicateAccess access) {
            return access.getSmokey() == null;
        }

        @Override
        public void appendTooltips(List<Component> tooltips, int indent, String key, LocationPredicateAccess access, InteractionResult result) {
            key = key + "." + this.getName();
            if (access.getSmokey() == Boolean.FALSE) {
                key = key + ".not";
            }
            ContextualCondition.desc(tooltips, result, indent, Component.translatable(key));
        }
    }

    public static class Type extends ContextualConditionType<Location> {

        public Location fromJson(JsonObject o) {
            LocationCheck check = (LocationCheck) LootItemConditions.LOCATION_CHECK.m_79331_().deserialize(o, ContextualCondition.gsonContext);
            return new Location(check);
        }

        public void toJson(Location condition, JsonObject o) {
            new LocationCheck.Serializer().serialize(o, condition.check(), ContextualCondition.gsonContext);
        }

        public Location fromNetwork(FriendlyByteBuf buf) {
            LocationPredicate.Builder builder = LocationPredicateHelper.fromNetwork(buf);
            LocationCheck check = (LocationCheck) LocationCheck.checkLocation(builder, buf.readBlockPos()).build();
            ResourceLocation biomeTag = CommonProxy.readNullableRL(buf);
            if (biomeTag != null) {
                LocationPredicateHelper access = (LocationPredicateHelper) ((LocationCheckAccess) check).getPredicate();
                access.lychee$setBiomeTag(TagKey.create(Registries.BIOME, biomeTag));
            }
            return new Location(check);
        }

        public void toNetwork(Location condition, FriendlyByteBuf buf) {
            LocationCheckAccess access = (LocationCheckAccess) condition.check();
            LocationPredicateHelper.toNetwork(access.getPredicate(), buf);
            buf.writeBlockPos(access.getOffset());
            ResourceLocation biomeTag = (ResourceLocation) Optional.ofNullable(((LocationPredicateHelper) access.getPredicate()).lychee$getBiomeTag()).map(TagKey::f_203868_).orElse(null);
            CommonProxy.writeNullableRL(biomeTag, buf);
        }
    }
}