package net.minecraftforge.client.model.obj;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import joptsimple.internal.Strings;
import org.joml.Vector4f;

public class ObjMaterialLibrary {

    public static final ObjMaterialLibrary EMPTY = new ObjMaterialLibrary();

    final Map<String, ObjMaterialLibrary.Material> materials = Maps.newHashMap();

    private ObjMaterialLibrary() {
    }

    public ObjMaterialLibrary(ObjTokenizer reader) throws IOException {
        ObjMaterialLibrary.Material currentMaterial = null;
        String[] line;
        while ((line = reader.readAndSplitLine(true)) != null) {
            String var4 = line[0];
            switch(var4) {
                case "newmtl":
                    String name = Strings.join((String[]) Arrays.copyOfRange(line, 1, line.length), " ");
                    currentMaterial = new ObjMaterialLibrary.Material(name);
                    this.materials.put(name, currentMaterial);
                    break;
                case "Ka":
                    currentMaterial.ambientColor = ObjModel.parseVector4(line);
                    break;
                case "map_Ka":
                    currentMaterial.ambientColorMap = line[line.length - 1];
                    break;
                case "Kd":
                    currentMaterial.diffuseColor = ObjModel.parseVector4(line);
                    break;
                case "forge_TintIndex":
                    currentMaterial.diffuseTintIndex = Integer.parseInt(line[1]);
                    break;
                case "map_Kd":
                    currentMaterial.diffuseColorMap = line[line.length - 1];
                    break;
                case "Ks":
                    currentMaterial.specularColor = ObjModel.parseVector4(line);
                    break;
                case "Ns":
                    currentMaterial.specularHighlight = Float.parseFloat(line[1]);
                    break;
                case "map_Ks":
                    currentMaterial.specularColorMap = line[line.length - 1];
                    break;
                case "d":
                    currentMaterial.dissolve = Float.parseFloat(line[1]);
                    break;
                case "Tr":
                    currentMaterial.transparency = Float.parseFloat(line[1]);
            }
        }
    }

    public ObjMaterialLibrary.Material getMaterial(String mat) {
        if (!this.materials.containsKey(mat)) {
            throw new NoSuchElementException("The material was not found in the library: " + mat);
        } else {
            return (ObjMaterialLibrary.Material) this.materials.get(mat);
        }
    }

    public static class Material {

        public final String name;

        public Vector4f ambientColor = new Vector4f();

        public String ambientColorMap;

        public Vector4f diffuseColor = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);

        public String diffuseColorMap;

        public Vector4f specularColor = new Vector4f();

        public float specularHighlight = 0.0F;

        public String specularColorMap;

        public float dissolve = 1.0F;

        public float transparency = 0.0F;

        public int diffuseTintIndex = 0;

        public Material(String name) {
            this.name = name;
        }
    }
}