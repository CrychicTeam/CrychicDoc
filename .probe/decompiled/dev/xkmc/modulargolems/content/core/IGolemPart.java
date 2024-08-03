package dev.xkmc.modulargolems.content.core;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public interface IGolemPart<P extends IGolemPart<P>> {

    int ordinal();

    @OnlyIn(Dist.CLIENT)
    void setupItemRender(PoseStack var1, ItemDisplayContext var2, @Nullable P var3);

    MutableComponent getDesc(MutableComponent var1);

    GolemPart<?, P> toItem();
}