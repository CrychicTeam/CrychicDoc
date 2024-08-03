package de.keksuccino.fancymenu.util.resource.resources.text;

import de.keksuccino.fancymenu.util.resource.Resource;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public interface IText extends Resource {

    @Nullable
    List<String> getTextLines();
}