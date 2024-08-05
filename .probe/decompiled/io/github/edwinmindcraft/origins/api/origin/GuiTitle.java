package io.github.edwinmindcraft.origins.api.origin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import io.github.edwinmindcraft.calio.api.network.OptionalFuncs;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public record GuiTitle(@Nullable Component view, @Nullable Component choose) {

    public static final GuiTitle DEFAULT = new GuiTitle(null, null);

    public static final Codec<GuiTitle> CODEC = RecordCodecBuilder.create(instance -> instance.group(CalioCodecHelper.COMPONENT_CODEC.optionalFieldOf("view_origin").forGetter(OptionalFuncs.opt(GuiTitle::view)), CalioCodecHelper.COMPONENT_CODEC.optionalFieldOf("choose_origin").forGetter(OptionalFuncs.opt(GuiTitle::choose))).apply(instance, OptionalFuncs.of(GuiTitle::new)));
}