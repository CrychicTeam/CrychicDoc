package de.keksuccino.fancymenu.customization.action.blocks;

import de.keksuccino.fancymenu.customization.action.ActionInstance;
import de.keksuccino.fancymenu.customization.action.Executable;
import de.keksuccino.fancymenu.customization.action.blocks.statements.ElseExecutableBlock;
import de.keksuccino.fancymenu.customization.action.blocks.statements.ElseIfExecutableBlock;
import de.keksuccino.fancymenu.customization.action.blocks.statements.IfExecutableBlock;
import de.keksuccino.fancymenu.util.properties.PropertyContainer;
import de.keksuccino.konkrete.input.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExecutableBlockDeserializer {

    @NotNull
    public static List<AbstractExecutableBlock> deserializeAll(@NotNull PropertyContainer serialized) {
        Map<AbstractExecutableBlock, ExecutableBlockDeserializer.BlockMeta> executableBlocks = new LinkedHashMap();
        for (Entry<String, String> m : serialized.getProperties().entrySet()) {
            if (((String) m.getKey()).startsWith("[executable_block:") && ((String) m.getKey()).contains("]")) {
                String identifier = ((String) m.getKey()).split("\\[executable_block:", 2)[1].split("]", 2)[0];
                if (((String) m.getKey()).contains("[type:")) {
                    String type = ((String) m.getKey()).split("\\[type:", 2)[1];
                    if (type.contains("]")) {
                        type = type.split("]", 2)[0];
                        AbstractExecutableBlock b = deserializeEmptyWithTypeAndIdentifier(serialized, type, identifier);
                        if (b != null) {
                            String contentRaw = (String) m.getValue();
                            if (contentRaw.contains("[executables:") && contentRaw.contains("]")) {
                                ExecutableBlockDeserializer.BlockMeta meta = new ExecutableBlockDeserializer.BlockMeta();
                                contentRaw = contentRaw.split("\\[executables:", 2)[1].split("]", 2)[0];
                                if (contentRaw.contains(";")) {
                                    meta.content = Arrays.asList(StringUtils.splitLines(contentRaw, ";"));
                                } else {
                                    meta.content.add(contentRaw);
                                }
                                if (((String) m.getValue()).contains("[appended:")) {
                                    String childIdentifier = ((String) m.getValue()).split("\\[appended:", 2)[1];
                                    if (childIdentifier.contains("]")) {
                                        childIdentifier = childIdentifier.split("]", 2)[0];
                                        meta.childIdentifier = childIdentifier;
                                    }
                                }
                                executableBlocks.put(b, meta);
                            }
                        }
                    }
                }
            }
        }
        List<Executable> possibleContent = new ArrayList();
        possibleContent.addAll(executableBlocks.keySet());
        possibleContent.addAll(ActionInstance.deserializeAll(serialized));
        for (Entry<AbstractExecutableBlock, ExecutableBlockDeserializer.BlockMeta> mx : executableBlocks.entrySet()) {
            AbstractExecutableBlock b = (AbstractExecutableBlock) mx.getKey();
            for (String executableId : ((ExecutableBlockDeserializer.BlockMeta) mx.getValue()).content) {
                for (Executable executable : possibleContent) {
                    if (executable.getIdentifier().equals(executableId)) {
                        b.addExecutable(executable);
                        break;
                    }
                }
            }
            if (((ExecutableBlockDeserializer.BlockMeta) mx.getValue()).childIdentifier != null) {
                for (Executable executablex : possibleContent) {
                    if (executablex.getIdentifier().equals(((ExecutableBlockDeserializer.BlockMeta) mx.getValue()).childIdentifier) && executablex instanceof AbstractExecutableBlock aeb) {
                        b.setAppendedBlock(aeb);
                        break;
                    }
                }
            }
        }
        return new ArrayList(executableBlocks.keySet());
    }

    @Nullable
    public static AbstractExecutableBlock deserializeWithIdentifier(@NotNull PropertyContainer serialized, @NotNull String identifier) {
        for (AbstractExecutableBlock b : deserializeAll(serialized)) {
            if (b.identifier.equals(identifier)) {
                return b;
            }
        }
        return null;
    }

    @Nullable
    public static AbstractExecutableBlock deserializeEmptyWithTypeAndIdentifier(@NotNull PropertyContainer serialized, @NotNull String type, @NotNull String identifier) {
        if (type.equals("generic")) {
            return GenericExecutableBlock.deserializeEmptyWithIdentifier(serialized, identifier);
        } else if (type.equals("if")) {
            return IfExecutableBlock.deserializeEmptyWithIdentifier(serialized, identifier);
        } else if (type.equals("else-if")) {
            return ElseIfExecutableBlock.deserializeEmptyWithIdentifier(serialized, identifier);
        } else {
            return type.equals("else") ? ElseExecutableBlock.deserializeEmptyWithIdentifier(serialized, identifier) : null;
        }
    }

    protected static class BlockMeta {

        @NotNull
        protected List<String> content = new ArrayList();

        @Nullable
        protected String childIdentifier;
    }
}