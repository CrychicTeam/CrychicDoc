package de.keksuccino.fancymenu.customization.gameintro;

import de.keksuccino.fancymenu.FancyMenu;
import de.keksuccino.fancymenu.customization.animation.AdvancedAnimation;
import de.keksuccino.fancymenu.customization.animation.AnimationHandler;
import de.keksuccino.fancymenu.customization.placeholder.PlaceholderParser;
import de.keksuccino.fancymenu.util.file.type.FileType;
import de.keksuccino.fancymenu.util.file.type.types.FileTypes;
import de.keksuccino.fancymenu.util.file.type.types.ImageFileType;
import de.keksuccino.fancymenu.util.file.type.types.VideoFileType;
import de.keksuccino.fancymenu.util.resource.PlayableResource;
import de.keksuccino.fancymenu.util.resource.ResourceHandlers;
import de.keksuccino.fancymenu.util.resource.ResourceSource;
import de.keksuccino.fancymenu.util.resource.resources.texture.ITexture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GameIntroHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    public static boolean introPlayed = false;

    public static boolean shouldPlayIntro() {
        return FancyMenu.getOptions().gameIntroAnimation.getValue().trim().isEmpty() ? false : getIntro() != null;
    }

    @Nullable
    public static PlayableResource getIntro() {
        String source = FancyMenu.getOptions().gameIntroAnimation.getValue();
        if (source.trim().isEmpty()) {
            return null;
        } else {
            source = PlaceholderParser.replacePlaceholders(source);
            AdvancedAnimation animation = getGameIntroAnimation(source);
            if (animation != null) {
                return animation;
            } else {
                ResourceSource resourceSource = ResourceSource.of(source);
                FileType<?> fileType = FileTypes.getType(resourceSource, false);
                if (fileType != null) {
                    if (fileType instanceof ImageFileType) {
                        ITexture t = ResourceHandlers.getImageHandler().get(resourceSource);
                        if (t instanceof PlayableResource) {
                            return (PlayableResource) t;
                        }
                    } else if (fileType instanceof VideoFileType) {
                        return ResourceHandlers.getVideoHandler().get(resourceSource);
                    }
                }
                return null;
            }
        }
    }

    public static boolean introIsAnimation() {
        String source = FancyMenu.getOptions().gameIntroAnimation.getValue();
        return source.trim().isEmpty() ? false : getGameIntroAnimation(source) != null;
    }

    @Deprecated
    @Nullable
    private static AdvancedAnimation getGameIntroAnimation(@NotNull String name) {
        if (AnimationHandler.animationExists(name) && AnimationHandler.getAnimation(name) instanceof AdvancedAnimation a) {
            a.setLooped(false);
            a.resetAnimation();
            a.stop();
            a.setMuteAudio(false);
            return a;
        } else {
            return null;
        }
    }
}