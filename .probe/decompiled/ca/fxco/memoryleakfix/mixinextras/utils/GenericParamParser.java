package ca.fxco.memoryleakfix.mixinextras.utils;

import ca.fxco.memoryleakfix.mixinextras.lib.apache.commons.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;
import org.spongepowered.asm.util.asm.ASM;

public class GenericParamParser extends SignatureVisitor {

    private final List<Type> results = new ArrayList();

    private GenericParamParser() {
        super(ASM.API_VERSION);
    }

    public static List<Type> getParameterGenerics(String desc, String signature) {
        if (signature != null && !signature.isEmpty()) {
            GenericParamParser parser = new GenericParamParser();
            new SignatureReader(signature).accept(parser);
            return parser.results;
        } else {
            return Collections.nCopies(Type.getArgumentTypes(desc).length, null);
        }
    }

    public SignatureVisitor visitParameterType() {
        final int index = this.results.size();
        this.results.add(null);
        return new SignatureVisitor(this.api) {

            public SignatureVisitor visitTypeArgument(char wildcard) {
                return (SignatureVisitor) (wildcard != '=' ? this : new SignatureVisitor(this.api) {

                    private int depth;

                    private int arrayDimensions;

                    private String internalName;

                    public SignatureVisitor visitArrayType() {
                        if (this.depth == 0) {
                            this.arrayDimensions++;
                        }
                        return this;
                    }

                    public void visitBaseType(char descriptor) {
                        if (this.depth == 0) {
                            GenericParamParser.this.results.set(index, Type.getType(StringUtils.repeat('[', this.arrayDimensions) + descriptor));
                        }
                    }

                    public void visitClassType(String name) {
                        if (++this.depth == 1) {
                            this.internalName = name;
                        }
                    }

                    public void visitInnerClassType(String name) {
                        if (this.depth == 1) {
                            this.internalName = this.internalName + '$' + name;
                        }
                    }

                    public void visitEnd() {
                        this.depth--;
                        String prefix = StringUtils.repeat('[', this.arrayDimensions);
                        GenericParamParser.this.results.set(index, Type.getType(prefix + Type.getObjectType(this.internalName).getDescriptor()));
                    }
                });
            }
        };
    }
}