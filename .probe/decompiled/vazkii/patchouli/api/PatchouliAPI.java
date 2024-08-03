package vazkii.patchouli.api;

import com.google.common.base.Suppliers;
import java.io.InputStream;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.stub.StubPatchouliAPI;

public class PatchouliAPI {

    private static final Supplier<PatchouliAPI.IPatchouliAPI> LAZY_INSTANCE = Suppliers.memoize(() -> {
        try {
            return (PatchouliAPI.IPatchouliAPI) Class.forName("vazkii.patchouli.common.base.PatchouliAPIImpl").newInstance();
        } catch (ReflectiveOperationException var1) {
            LogManager.getLogger().warn("Unable to find PatchouliAPIImpl, using a dummy");
            return StubPatchouliAPI.INSTANCE;
        }
    });

    public static final String MOD_ID = "patchouli";

    public static final Logger LOGGER = LogManager.getLogger("patchouli");

    public static PatchouliAPI.IPatchouliAPI get() {
        return (PatchouliAPI.IPatchouliAPI) LAZY_INSTANCE.get();
    }

    public interface IPatchouliAPI {

        boolean isStub();

        PatchouliConfigAccess getConfig();

        void setConfigFlag(String var1, boolean var2);

        boolean getConfigFlag(String var1);

        void openBookGUI(ServerPlayer var1, ResourceLocation var2);

        void openBookEntry(ServerPlayer var1, ResourceLocation var2, ResourceLocation var3, int var4);

        void openBookGUI(ResourceLocation var1);

        void openBookEntry(ResourceLocation var1, ResourceLocation var2, int var3);

        @Nullable
        ResourceLocation getOpenBookGui();

        Component getSubtitle(ResourceLocation var1);

        ItemStack getBookStack(ResourceLocation var1);

        void registerTemplateAsBuiltin(ResourceLocation var1, Supplier<InputStream> var2);

        void registerCommand(String var1, Function<IStyleStack, String> var2);

        void registerFunction(String var1, BiFunction<String, IStyleStack, String> var2);

        @Nullable
        IMultiblock getMultiblock(ResourceLocation var1);

        IMultiblock registerMultiblock(ResourceLocation var1, IMultiblock var2);

        @Nullable
        IMultiblock getCurrentMultiblock();

        void showMultiblock(IMultiblock var1, Component var2, BlockPos var3, Rotation var4);

        void clearMultiblock();

        IMultiblock makeMultiblock(String[][] var1, Object... var2);

        IMultiblock makeSparseMultiblock(Map<BlockPos, IStateMatcher> var1);

        IStateMatcher predicateMatcher(BlockState var1, Predicate<BlockState> var2);

        IStateMatcher predicateMatcher(Block var1, Predicate<BlockState> var2);

        IStateMatcher stateMatcher(BlockState var1);

        IStateMatcher propertyMatcher(BlockState var1, Property<?>... var2);

        IStateMatcher looseBlockMatcher(Block var1);

        IStateMatcher strictBlockMatcher(Block var1);

        IStateMatcher displayOnlyMatcher(BlockState var1);

        IStateMatcher displayOnlyMatcher(Block var1);

        IStateMatcher tagMatcher(TagKey<Block> var1);

        IStateMatcher airMatcher();

        IStateMatcher anyMatcher();
    }
}