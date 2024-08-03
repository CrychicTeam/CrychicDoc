package snownee.kiwi;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

public class KiwiTabBuilder extends CreativeModeTab.Builder {

    public static final List<KiwiTabBuilder> BUILDERS = Lists.newArrayList();

    public final ResourceLocation id;

    private CreativeModeTab tab;

    public KiwiTabBuilder(ResourceLocation id) {
        super(CreativeModeTab.Row.TOP, 0);
        this.id = id;
        this.m_257941_(Component.translatable("itemGroup.%s.%s".formatted(id.getNamespace(), id.getPath())));
        BUILDERS.add(this);
    }

    @Override
    public CreativeModeTab build() {
        if (this.tab == null) {
            this.tab = super.build();
        }
        return this.tab;
    }
}