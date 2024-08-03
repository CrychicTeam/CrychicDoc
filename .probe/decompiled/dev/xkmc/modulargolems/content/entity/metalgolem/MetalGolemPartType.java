package dev.xkmc.modulargolems.content.entity.metalgolem;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;

public enum MetalGolemPartType implements IGolemPart<MetalGolemPartType> {

    RIGHT, BODY, LEFT, LEG;

    public void setupItemRender(PoseStack stack, ItemDisplayContext transform, @Nullable MetalGolemPartType part) {
        MetalGolemRenderer.transform(stack, transform, part);
    }

    @Override
    public MutableComponent getDesc(MutableComponent desc) {
        return Component.translatable("golem_part.metal_golem." + this.name().toLowerCase(Locale.ROOT), desc).withStyle(ChatFormatting.GREEN);
    }

    @Override
    public GolemPart<?, MetalGolemPartType> toItem() {
        return switch(this) {
            case BODY ->
                (GolemPart) GolemItems.GOLEM_BODY.get();
            case LEG ->
                (GolemPart) GolemItems.GOLEM_LEGS.get();
            default ->
                (GolemPart) GolemItems.GOLEM_ARM.get();
        };
    }
}