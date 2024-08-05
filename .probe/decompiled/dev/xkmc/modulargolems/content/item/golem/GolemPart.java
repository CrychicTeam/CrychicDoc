package dev.xkmc.modulargolems.content.item.golem;

import com.tterrag.registrate.util.CreativeModeTabModifier;
import dev.xkmc.modulargolems.content.config.GolemMaterial;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.config.GolemPartConfig;
import dev.xkmc.modulargolems.content.core.GolemStatType;
import dev.xkmc.modulargolems.content.core.GolemType;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

public class GolemPart<T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> extends Item implements IGolemPartItem {

    public static final List<GolemPart<?, ?>> LIST = new ArrayList();

    private static final String KEY = "golem_material";

    private final Supplier<GolemType<T, P>> type;

    private final P part;

    public final int count;

    public static Optional<ResourceLocation> getMaterial(ItemStack stack) {
        return Optional.ofNullable(stack.getTag()).map(e -> e.contains("golem_material") ? new ResourceLocation(e.getString("golem_material")) : null);
    }

    public static ItemStack setMaterial(ItemStack stack, ResourceLocation material) {
        stack.getOrCreateTag().putString("golem_material", material.toString());
        return stack;
    }

    public GolemPart(Item.Properties props, Supplier<GolemType<T, P>> type, P part, int count) {
        super(props.stacksTo(1));
        this.type = type;
        this.part = part;
        this.count = count;
        LIST.add(this);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        getMaterial(stack).ifPresent(e -> {
            GolemMaterial mat = this.parseMaterial(e);
            list.add(mat.getDesc());
            mat.modifiers().forEach((m, v) -> {
                list.add(m.getTooltip(v));
                list.addAll(m.getDetail(v));
            });
            mat.stats().forEach((k, v) -> list.add(k.getAdderTooltip(v)));
        });
    }

    public GolemMaterial parseMaterial(ResourceLocation mat) {
        HashMap<GolemStatType, Double> magnifier = GolemPartConfig.get().getMagnifier(this.getEntityType());
        HashMap<StatFilterType, Double> filter = GolemPartConfig.get().getFilter(this);
        HashMap<GolemStatType, Double> stats = new HashMap();
        ((HashMap) GolemMaterialConfig.get().stats.get(mat)).forEach((k, v) -> {
            double val = v * (Double) filter.getOrDefault(k.type, 1.0) * (Double) magnifier.getOrDefault(k, 1.0);
            if (val != 0.0) {
                stats.compute(k, (e, o) -> (o == null ? 0.0 : o) + val);
            }
        });
        HashMap<GolemModifier, Integer> modifiers = new HashMap();
        ((HashMap) GolemMaterialConfig.get().modifiers.get(mat)).forEach((k, v) -> {
            if (k.canExistOn(this)) {
                modifiers.compute(k, (e, o) -> (o == null ? 0 : o) + v);
            }
        });
        return new GolemMaterial(stats, modifiers, mat, this);
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(GolemBEWLR.EXTENSIONS);
    }

    @Override
    public GolemPart<?, ?> asPart() {
        return this;
    }

    public GolemType<T, P> getEntityType() {
        return (GolemType<T, P>) this.type.get();
    }

    public P getPart() {
        return this.part;
    }

    public void fillItemCategory(CreativeModeTabModifier tab) {
        tab.m_246342_(new ItemStack(this));
        for (ResourceLocation rl : GolemMaterialConfig.get().getAllMaterials()) {
            ItemStack stack = new ItemStack(this);
            tab.m_246342_(setMaterial(stack, rl));
        }
    }
}