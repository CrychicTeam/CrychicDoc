package dev.xkmc.l2artifacts.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2artifacts.init.data.slot.CurioSlotBuilder;
import java.util.Locale;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;

public enum ArtifactSlotCuriosType {

    HEAD("artifact_head", -1400), NECKLACE("artifact_necklace", -1390), BRACELET("artifact_bracelet", -1380), BODY("artifact_body", -1370), BELT("artifact_belt", -1360);

    final String id;

    final int priority;

    private ArtifactSlotCuriosType(String id, int priority) {
        this.id = id;
        this.priority = priority;
    }

    public String getIdentifier() {
        return this.id;
    }

    public String getDefTranslation() {
        return "Artifact - " + RegistrateLangProvider.toEnglishName(this.name().toLowerCase(Locale.ROOT));
    }

    public String getDesc() {
        return "curios.identifier." + this.id;
    }

    public void buildConfig(BiConsumer<String, CurioSlotBuilder> cons) {
        cons.accept("curios/curios/slots/" + this.getIdentifier(), new CurioSlotBuilder(this.priority, new ResourceLocation("l2artifacts", "slot/empty_" + this.getIdentifier() + "_slot").toString()));
    }
}