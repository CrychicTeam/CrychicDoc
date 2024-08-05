package snownee.jade.addon.harvest;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableList.Builder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.Identifiers;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.impl.ui.SubTextElement;
import snownee.jade.util.ClientProxy;
import snownee.jade.util.CommonProxy;

public enum HarvestToolProvider implements IBlockComponentProvider, ResourceManagerReloadListener {

    INSTANCE;

    public static final Cache<BlockState, ImmutableList<ItemStack>> resultCache = CacheBuilder.newBuilder().expireAfterAccess(5L, TimeUnit.MINUTES).build();

    public static final Map<String, ToolHandler> TOOL_HANDLERS = Maps.newLinkedHashMap();

    private static final Component CHECK = Component.literal("✔");

    private static final Component X = Component.literal("✕");

    private static final Vec2 ITEM_SIZE = new Vec2(10.0F, 0.0F);

    public static ImmutableList<ItemStack> getTool(BlockState state, Level world, BlockPos pos) {
        Builder<ItemStack> tools = ImmutableList.builder();
        for (ToolHandler handler : TOOL_HANDLERS.values()) {
            ItemStack tool = handler.test(state, world, pos);
            if (!tool.isEmpty()) {
                tools.add(tool);
            }
        }
        return tools.build();
    }

    public static synchronized void registerHandler(ToolHandler handler) {
        TOOL_HANDLERS.put(handler.getName(), handler);
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        Player player = accessor.getPlayer();
        if (config.get(Identifiers.MC_HARVEST_TOOL_CREATIVE) || !player.isCreative() && !player.isSpectator()) {
            BlockState state = accessor.getBlockState();
            Level level = accessor.getLevel();
            BlockPos pos = accessor.getPosition();
            float destroySpeed = state.m_60800_(level, pos);
            float destroyProgress = state.m_60625_(player, level, pos);
            if (!(destroySpeed < 0.0F) && !(destroyProgress <= 0.0F)) {
                boolean newLine = config.get(Identifiers.MC_HARVEST_TOOL_NEW_LINE);
                List<IElement> elements = this.getText(accessor, config);
                if (!elements.isEmpty()) {
                    elements.forEach(e -> e.message(null));
                    if (newLine) {
                        tooltip.add(elements);
                    } else {
                        elements.forEach(e -> e.align(IElement.Align.RIGHT));
                        tooltip.append(0, elements);
                    }
                }
            } else {
                if (config.get(Identifiers.MC_SHOW_UNBREAKABLE)) {
                    Component text = IThemeHelper.get().failure(Component.translatable("jade.harvest_tool.unbreakable"));
                    tooltip.add(IElementHelper.get().text(text).message(null));
                }
            }
        }
    }

    public List<IElement> getText(BlockAccessor accessor, IPluginConfig config) {
        BlockState state = accessor.getBlockState();
        if (!state.m_60834_() && !config.get(Identifiers.MC_EFFECTIVE_TOOL)) {
            return List.of();
        } else {
            List<ItemStack> tools = List.of();
            try {
                tools = (List<ItemStack>) resultCache.get(state, () -> getTool(state, accessor.getLevel(), accessor.getPosition()));
            } catch (ExecutionException var12) {
                var12.printStackTrace();
            }
            if (tools.isEmpty()) {
                return List.of();
            } else {
                int offsetY = -3;
                boolean newLine = config.get(Identifiers.MC_HARVEST_TOOL_NEW_LINE);
                List<IElement> elements = Lists.newArrayList();
                for (ItemStack tool : tools) {
                    elements.add(IElementHelper.get().item(tool, 0.75F).translate(new Vec2(-1.0F, (float) offsetY)).size(ITEM_SIZE).message(null));
                }
                if (!elements.isEmpty()) {
                    elements.add(0, IElementHelper.get().spacer(newLine ? -2 : 5, newLine ? 10 : 0));
                    ItemStack held = accessor.getPlayer().m_21205_();
                    boolean canHarvest = held.isCorrectToolForDrops(state);
                    if (CommonProxy.isShearable(state) && CommonProxy.isShears(held)) {
                        canHarvest = true;
                    }
                    if (state.m_60834_() || canHarvest) {
                        IThemeHelper t = IThemeHelper.get();
                        Component text = canHarvest ? t.success(CHECK) : t.danger(X);
                        elements.add(new SubTextElement(text).translate(new Vec2(-3.0F, (float) (7 + offsetY))));
                    }
                }
                return elements;
            }
        }
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        resultCache.invalidateAll();
    }

    @Override
    public ResourceLocation getUid() {
        return Identifiers.MC_HARVEST_TOOL;
    }

    @Override
    public int getDefaultPriority() {
        return -8000;
    }

    static {
        if (CommonProxy.isPhysicallyClient()) {
            registerHandler(new SimpleToolHandler("pickaxe", BlockTags.MINEABLE_WITH_PICKAXE, Items.WOODEN_PICKAXE, Items.STONE_PICKAXE, Items.IRON_PICKAXE, Items.DIAMOND_PICKAXE, Items.NETHERITE_PICKAXE));
            registerHandler(new SimpleToolHandler("axe", BlockTags.MINEABLE_WITH_AXE, Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE));
            registerHandler(new SimpleToolHandler("shovel", BlockTags.MINEABLE_WITH_SHOVEL, Items.WOODEN_SHOVEL, Items.STONE_SHOVEL, Items.IRON_SHOVEL, Items.DIAMOND_SHOVEL, Items.NETHERITE_SHOVEL));
            registerHandler(new SimpleToolHandler("hoe", BlockTags.MINEABLE_WITH_HOE, Items.WOODEN_HOE, Items.STONE_HOE, Items.IRON_HOE, Items.DIAMOND_HOE, Items.NETHERITE_HOE));
            registerHandler(ClientProxy.createSwordToolHandler());
            registerHandler(new ShearsToolHandler());
        }
    }
}