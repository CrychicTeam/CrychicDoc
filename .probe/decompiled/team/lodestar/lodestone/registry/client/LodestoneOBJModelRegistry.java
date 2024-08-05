package team.lodestar.lodestone.registry.client;

import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import team.lodestar.lodestone.systems.model.obj.ObjModel;
import team.lodestar.lodestone.systems.model.obj.lod.MultiLODModel;

@EventBusSubscriber(modid = "lodestone", bus = Bus.MOD, value = { Dist.CLIENT })
public class LodestoneOBJModelRegistry {

    public static List<ObjModel> OBJ_MODELS = new ArrayList();

    public static List<MultiLODModel> LOD_MODELS = new ArrayList();

    public static ObjModel registerObjModel(ObjModel objModel) {
        OBJ_MODELS.add(objModel);
        return objModel;
    }

    public static MultiLODModel registerObjModel(MultiLODModel lodModel) {
        LOD_MODELS.add(lodModel);
        return lodModel;
    }

    public static void loadModels() {
        OBJ_MODELS.forEach(ObjModel::loadModel);
        LOD_MODELS.forEach(MultiLODModel::loadModel);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onClientSetup(FMLClientSetupEvent event) {
        loadModels();
    }
}