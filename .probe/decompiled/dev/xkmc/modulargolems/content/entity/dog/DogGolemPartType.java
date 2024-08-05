package dev.xkmc.modulargolems.content.entity.dog;

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

public enum DogGolemPartType implements IGolemPart<DogGolemPartType> {

    BODY, LEGS;

    @Override
    public MutableComponent getDesc(MutableComponent desc) {
        return Component.translatable("golem_part.dog_golem." + this.name().toLowerCase(Locale.ROOT), desc).withStyle(ChatFormatting.GREEN);
    }

    @Override
    public GolemPart<?, DogGolemPartType> toItem() {
        return switch(this) {
            case BODY ->
                (GolemPart) GolemItems.DOG_BODY.get();
            case LEGS ->
                (GolemPart) GolemItems.DOG_LEGS.get();
        };
    }

    public void setupItemRender(PoseStack stack, ItemDisplayContext transform, @Nullable DogGolemPartType part) {
        DogGolemRenderer.transform(stack, transform, part);
    }
}