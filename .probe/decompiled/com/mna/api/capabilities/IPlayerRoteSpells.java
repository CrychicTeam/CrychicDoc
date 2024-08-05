package com.mna.api.capabilities;

import com.mna.api.spells.base.ISpellComponent;
import com.mna.api.spells.parts.Modifier;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.parts.SpellEffect;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public interface IPlayerRoteSpells {

    boolean isRote(ISpellComponent var1);

    boolean addRoteXP(@Nullable Player var1, ISpellComponent var2);

    boolean addRoteXP(@Nullable Player var1, ISpellComponent var2, float var3);

    float getMastery(ISpellComponent var1);

    boolean addMastery(@Nullable Player var1, ISpellComponent var2, float var3);

    void setMastery(ResourceLocation var1, float var2);

    List<Shape> getRoteShapes();

    List<SpellEffect> getRoteComponents();

    List<Modifier> getRoteModifiers();

    HashMap<ISpellComponent, Float> getRoteData();

    HashMap<ISpellComponent, Float> getMasteryData();

    boolean isDirty();

    void setDirty();

    void clearDirty();

    void copyFrom(IPlayerRoteSpells var1);

    void setRoteXP(ResourceLocation var1, float var2);

    float getRoteProgression(ISpellComponent var1);

    void resetRote();

    void resetMastery();
}