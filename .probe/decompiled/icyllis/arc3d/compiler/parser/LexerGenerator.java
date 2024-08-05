package icyllis.arc3d.compiler.parser;

import icyllis.arc3d.core.MathUtil;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

public class LexerGenerator {

    public static final String LEXICON = "INTLITERAL     = ([1-9]\\d*|0[0-7]*|0[xX][\\da-fA-F]+)[uU]?\nFLOATLITERAL   = (\\d*\\.\\d+([eE][+-]?\\d+)?|\\d+\\.\\d*([eE][+-]?\\d+)?|\\d+([eE][+-]?\\d+))[fF]?\nTRUE           = \"true\"\nFALSE          = \"false\"\nBREAK          = \"break\"\nCONTINUE       = \"continue\"\nDO             = \"do\"\nFOR            = \"for\"\nWHILE          = \"while\"\nIF             = \"if\"\nELSE           = \"else\"\nSWITCH         = \"switch\"\nCASE           = \"case\"\nDEFAULT        = \"default\"\nDISCARD        = \"discard\"\nRETURN         = \"return\"\nIN             = \"in\"\nOUT            = \"out\"\nINOUT          = \"inout\"\nCONST          = \"const\"\nUNIFORM        = \"uniform\"\nBUFFER         = \"buffer\"\nWORKGROUP      = \"workgroup\"\nSMOOTH         = \"smooth\"\nFLAT           = \"flat\"\nNOPERSPECTIVE  = \"noperspective\"\nCOHERENT       = \"coherent\"\nVOLATILE       = \"volatile\"\nRESTRICT       = \"restrict\"\nREADONLY       = \"readonly\"\nWRITEONLY      = \"writeonly\"\nSUBROUTINE     = \"subroutine\"\nLAYOUT         = \"layout\"\nSTRUCT         = \"struct\"\nINLINE         = \"inline\"\nNOINLINE       = \"noinline\"\nPURE           = \"__pure\"\nRESERVED       = shared|attribute|varying|atomic_uint|lowp|mediump|highp|precision|common|partition|active|asm|class|union|enum|typedef|template|this|resource|goto|public|static|extern|external|interface|long|double|fixed|unsigned|superp|input|output|hvec[234]|dvec[234]|fvec[234]|filter|sizeof|cast|namespace|using|[iu]?(sampler|image|texture)2DRect|sampler2DRectShadow|sampler3DRect|gl_\\w*\nIDENTIFIER     = [a-zA-Z_]\\w*\nLPAREN         = \"(\"\nRPAREN         = \")\"\nLBRACE         = \"{\"\nRBRACE         = \"}\"\nLBRACKET       = \"[\"\nRBRACKET       = \"]\"\nDOT            = \".\"\nCOMMA          = \",\"\nEQ             = \"=\"\nLT             = \"<\"\nGT             = \">\"\nBANG           = \"!\"\nTILDE          = \"~\"\nQUES           = \"?\"\nCOLON          = \":\"\nEQEQ           = \"==\"\nLTEQ           = \"<=\"\nGTEQ           = \">=\"\nBANGEQ         = \"!=\"\nPLUSPLUS       = \"++\"\nMINUSMINUS     = \"--\"\nPLUS           = \"+\"\nMINUS          = \"-\"\nSTAR           = \"*\"\nSLASH          = \"/\"\nPERCENT        = \"%\"\nLTLT           = \"<<\"\nGTGT           = \">>\"\nAMPAMP         = \"&&\"\nPIPEPIPE       = \"||\"\nCARETCARET     = \"^^\"\nAMP            = \"&\"\nPIPE           = \"|\"\nCARET          = \"^\"\nPLUSEQ         = \"+=\"\nMINUSEQ        = \"-=\"\nSTAREQ         = \"*=\"\nSLASHEQ        = \"/=\"\nPERCENTEQ      = \"%=\"\nLTLTEQ         = \"<<=\"\nGTGTEQ         = \">>=\"\nAMPEQ          = \"&=\"\nPIPEEQ         = \"|=\"\nCARETEQ        = \"^=\"\nSEMICOLON      = \";\"\nWHITESPACE     = \\s+\nLINE_COMMENT   = //.*\nBLOCK_COMMENT  = /\\*([^*]|\\*[^/])*\\*/\nINVALID        = .";

    public static final int NUM_BITS = 2;

    public static final int NUM_VALUES = 3;

    public static final int DATA_PER_BYTE = 4;

    public static final int DATA_PER_BYTE_SHIFT = Integer.numberOfTrailingZeros(4);

    @Nonnull
    public static DFA process(PrintWriter pw) {
        NFA nfa = new NFA();
        List<String> tokens = new ArrayList();
        tokens.add("END_OF_FILE");
        RegexParser parser = new RegexParser();
        for (String line : "INTLITERAL     = ([1-9]\\d*|0[0-7]*|0[xX][\\da-fA-F]+)[uU]?\nFLOATLITERAL   = (\\d*\\.\\d+([eE][+-]?\\d+)?|\\d+\\.\\d*([eE][+-]?\\d+)?|\\d+([eE][+-]?\\d+))[fF]?\nTRUE           = \"true\"\nFALSE          = \"false\"\nBREAK          = \"break\"\nCONTINUE       = \"continue\"\nDO             = \"do\"\nFOR            = \"for\"\nWHILE          = \"while\"\nIF             = \"if\"\nELSE           = \"else\"\nSWITCH         = \"switch\"\nCASE           = \"case\"\nDEFAULT        = \"default\"\nDISCARD        = \"discard\"\nRETURN         = \"return\"\nIN             = \"in\"\nOUT            = \"out\"\nINOUT          = \"inout\"\nCONST          = \"const\"\nUNIFORM        = \"uniform\"\nBUFFER         = \"buffer\"\nWORKGROUP      = \"workgroup\"\nSMOOTH         = \"smooth\"\nFLAT           = \"flat\"\nNOPERSPECTIVE  = \"noperspective\"\nCOHERENT       = \"coherent\"\nVOLATILE       = \"volatile\"\nRESTRICT       = \"restrict\"\nREADONLY       = \"readonly\"\nWRITEONLY      = \"writeonly\"\nSUBROUTINE     = \"subroutine\"\nLAYOUT         = \"layout\"\nSTRUCT         = \"struct\"\nINLINE         = \"inline\"\nNOINLINE       = \"noinline\"\nPURE           = \"__pure\"\nRESERVED       = shared|attribute|varying|atomic_uint|lowp|mediump|highp|precision|common|partition|active|asm|class|union|enum|typedef|template|this|resource|goto|public|static|extern|external|interface|long|double|fixed|unsigned|superp|input|output|hvec[234]|dvec[234]|fvec[234]|filter|sizeof|cast|namespace|using|[iu]?(sampler|image|texture)2DRect|sampler2DRectShadow|sampler3DRect|gl_\\w*\nIDENTIFIER     = [a-zA-Z_]\\w*\nLPAREN         = \"(\"\nRPAREN         = \")\"\nLBRACE         = \"{\"\nRBRACE         = \"}\"\nLBRACKET       = \"[\"\nRBRACKET       = \"]\"\nDOT            = \".\"\nCOMMA          = \",\"\nEQ             = \"=\"\nLT             = \"<\"\nGT             = \">\"\nBANG           = \"!\"\nTILDE          = \"~\"\nQUES           = \"?\"\nCOLON          = \":\"\nEQEQ           = \"==\"\nLTEQ           = \"<=\"\nGTEQ           = \">=\"\nBANGEQ         = \"!=\"\nPLUSPLUS       = \"++\"\nMINUSMINUS     = \"--\"\nPLUS           = \"+\"\nMINUS          = \"-\"\nSTAR           = \"*\"\nSLASH          = \"/\"\nPERCENT        = \"%\"\nLTLT           = \"<<\"\nGTGT           = \">>\"\nAMPAMP         = \"&&\"\nPIPEPIPE       = \"||\"\nCARETCARET     = \"^^\"\nAMP            = \"&\"\nPIPE           = \"|\"\nCARET          = \"^\"\nPLUSEQ         = \"+=\"\nMINUSEQ        = \"-=\"\nSTAREQ         = \"*=\"\nSLASHEQ        = \"/=\"\nPERCENTEQ      = \"%=\"\nLTLTEQ         = \"<<=\"\nGTGTEQ         = \">>=\"\nAMPEQ          = \"&=\"\nPIPEEQ         = \"|=\"\nCARETEQ        = \"^=\"\nSEMICOLON      = \";\"\nWHITESPACE     = \\s+\nLINE_COMMENT   = //.*\nBLOCK_COMMENT  = /\\*([^*]|\\*[^/])*\\*/\nINVALID        = .".split("\n")) {
            String[] split = line.split("\\s+");
            assert split.length == 3;
            String name = split[0];
            String delimiter = split[1];
            String pattern = split[2];
            assert !name.isEmpty();
            assert delimiter.equals("=");
            assert !pattern.isEmpty();
            tokens.add(name);
            if (pattern.startsWith("\"")) {
                assert pattern.length() > 2 && pattern.endsWith("\"");
                RegexNode node = RegexNode.Char(pattern.charAt(1));
                for (int i = 2; i < pattern.length() - 1; i++) {
                    node = RegexNode.Concat(node, RegexNode.Char(pattern.charAt(i)));
                }
                nfa.add(node);
            } else {
                nfa.add(parser.parse(pattern));
            }
        }
        NFAtoDFA converter = new NFAtoDFA(nfa);
        DFA dfa = converter.convert();
        pw.println("public static final int");
        for (int i = 0; i < tokens.size(); i++) {
            pw.println("TK_" + (String) tokens.get(i) + " = " + i + ",");
        }
        pw.println("TK_NONE = " + tokens.size() + ";");
        return dfa;
    }

    public static void writeTransitionTable(PrintWriter pw, DFA dfa, int states) {
        int numTransitions = dfa.mTransitions.length;
        record MutablePackedEntry(IntList v, IntList data) {
        }
        List<MutablePackedEntry> packedEntries = new ArrayList();
        List<IntList> fullEntries = new ArrayList();
        IntList indices = new IntArrayList();
        for (int s = 0; s < states; s++) {
            IntArraySet transitionSet = new IntArraySet();
            IntArrayList data = new IntArrayList(numTransitions);
            data.size(numTransitions);
            for (int t = 0; t < numTransitions; t++) {
                if (s < dfa.mTransitions[t].length) {
                    int value = dfa.mTransitions[t][s];
                    assert value >= 0 && value < states;
                    data.set(t, value);
                    transitionSet.add(value);
                }
            }
            transitionSet.remove(0);
            if (transitionSet.size() <= 3) {
                MutablePackedEntry result = new MutablePackedEntry(new IntArrayList(3), new IntArrayList());
                result.v.addAll(transitionSet);
                result.v.size(3);
                result.v.sort(IntComparators.OPPOSITE_COMPARATOR);
                Int2IntArrayMap translationTable = new Int2IntArrayMap();
                for (int index = 0; index < result.v.size(); index++) {
                    translationTable.put(result.v.getInt(index), index);
                }
                translationTable.put(0, result.v.size());
                for (int index = 0; index < data.size(); index++) {
                    int value = data.getInt(index);
                    assert translationTable.containsKey(value);
                    result.data.add(translationTable.get(value));
                }
                int index = packedEntries.indexOf(result);
                if (index == -1) {
                    index = packedEntries.size();
                    packedEntries.add(result);
                }
                indices.add(index);
            } else {
                int index = fullEntries.indexOf(data);
                if (index == -1) {
                    index = fullEntries.size();
                    fullEntries.add(data);
                }
                indices.add(~index);
            }
        }
        int maxValue = 0;
        for (MutablePackedEntry entry : packedEntries) {
            for (int index = 0; index < 3; index++) {
                maxValue = Math.max(maxValue, entry.v.getInt(index));
            }
        }
        int bitsPerValue = 32 - Integer.numberOfLeadingZeros(maxValue - 1);
        maxValue = (1 << bitsPerValue) - 1;
        assert bitsPerValue <= 10;
        pw.println("public static final short[][] FULL = {");
        int i = 0;
        for (int end = fullEntries.size(); i < end; i++) {
            IntList data = (IntList) fullEntries.get(i);
            assert data.size() == numTransitions;
            pw.println("{");
            for (int j = 0; j < numTransitions; j++) {
                pw.print((short) data.getInt(j));
                if (j == numTransitions - 1) {
                    pw.println();
                } else if (j % 9 == 8) {
                    pw.println(",");
                } else {
                    pw.print(", ");
                }
            }
            if (i == end - 1) {
                pw.println("}");
            } else {
                pw.println("},");
            }
        }
        pw.println("};");
        i = MathUtil.alignTo(numTransitions, 4);
        pw.println("public static final PackedEntry[] PACKED = {");
        int ix = 0;
        for (int end = packedEntries.size(); ix < end; ix++) {
            MutablePackedEntry entry = (MutablePackedEntry) packedEntries.get(ix);
            pw.print("new PackedEntry(");
            assert entry.v.size() == 3;
            pw.print(entry.v.getInt(0));
            if (entry.v.getInt(1) != 0) {
                pw.print(" | (" + entry.v.getInt(1) + " << " + bitsPerValue + ")");
            }
            if (entry.v.getInt(2) != 0) {
                pw.print(" | (" + entry.v.getInt(2) + " << " + 2 * bitsPerValue + ")");
            }
            pw.println(",");
            pw.println("new byte[]{");
            int jx = 0;
            int shiftBits = 0;
            int combinedBits = 0;
            for (int index = 0; index < numTransitions; index++) {
                combinedBits |= entry.data.getInt(index) << shiftBits;
                shiftBits += 2;
                if (shiftBits == 8) {
                    pw.print((byte) combinedBits);
                    if (jx == i - 1) {
                        pw.println();
                    } else if (jx % 4 == 3) {
                        pw.println(",");
                    } else {
                        pw.print(", ");
                    }
                    shiftBits = 0;
                    combinedBits = 0;
                    jx++;
                }
            }
            if (shiftBits > 0) {
                pw.println((byte) combinedBits);
            }
            if (ix == end - 1) {
                pw.println("})");
            } else {
                pw.println("}),");
            }
        }
        pw.println("};");
        pw.println("public static final short[] INDICES = {");
        ix = 0;
        for (int end = indices.size(); ix < end; ix++) {
            pw.print(indices.getInt(ix));
            if (ix == end - 1) {
                pw.println();
            } else if (ix % 9 == 8) {
                pw.println(",");
            } else {
                pw.print(", ");
            }
        }
        pw.println("};");
        pw.println("public static int getTransition(int transition, int state) {");
        pw.println("short index = INDICES[state];");
        pw.println("if (index < 0) return FULL[~index][transition] & 0xFFFF;");
        pw.println("final PackedEntry entry = PACKED[index];");
        pw.println("int v = entry.data[transition >> " + DATA_PER_BYTE_SHIFT + "] & 0xFF;");
        pw.println("v >>= 2 * (transition & 3);");
        pw.println("v &= 3;");
        pw.println("v *= " + bitsPerValue + ";");
        pw.println("return (entry.values >>> v) & " + maxValue + ";");
        pw.println("}");
    }

    public static void main(String[] args) {
        PrintWriter pw = new PrintWriter(System.out, false, StandardCharsets.UTF_8);
        DFA dfa = process(pw);
        int c = 0;
        int length = dfa.mCharMappings.length;
        while (c < length && dfa.mCharMappings[c] == 0) {
            c++;
        }
        assert c == 9;
        assert length == 127;
        pw.println("public static final byte[] MAPPINGS = {");
        for (int i = 0; i <= 117; i++) {
            pw.print(dfa.mCharMappings[i + 9]);
            if (i == 117) {
                pw.println();
            } else if (i % 9 == 8) {
                pw.println(",");
            } else {
                pw.print(", ");
            }
        }
        pw.println("};");
        c = 0;
        for (int[] row : dfa.mTransitions) {
            c = Math.max(c, row.length);
        }
        writeTransitionTable(pw, dfa, c);
        pw.println("public static final byte[] ACCEPTS = {");
        for (int ix = 0; ix < c; ix++) {
            if (ix < dfa.mAccepts.length) {
                pw.print(dfa.mAccepts[ix]);
            } else {
                pw.print(-1);
            }
            if (ix == c - 1) {
                pw.println();
            } else if (ix % 9 == 8) {
                pw.println(",");
            } else {
                pw.print(", ");
            }
        }
        pw.println("};");
        pw.flush();
    }
}