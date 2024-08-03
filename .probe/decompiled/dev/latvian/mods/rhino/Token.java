package dev.latvian.mods.rhino;

public interface Token {

    int ERROR = -1;

    int EOF = 0;

    int EOL = 1;

    int FIRST_BYTECODE_TOKEN = 2;

    int ENTERWITH = 2;

    int LEAVEWITH = 3;

    int RETURN = 4;

    int GOTO = 5;

    int IFEQ = 6;

    int IFNE = 7;

    int SETNAME = 8;

    int BITOR = 9;

    int BITXOR = 10;

    int BITAND = 11;

    int EQ = 12;

    int NE = 13;

    int LT = 14;

    int LE = 15;

    int GT = 16;

    int GE = 17;

    int LSH = 18;

    int RSH = 19;

    int URSH = 20;

    int ADD = 21;

    int SUB = 22;

    int MUL = 23;

    int DIV = 24;

    int MOD = 25;

    int NOT = 26;

    int BITNOT = 27;

    int POS = 28;

    int NEG = 29;

    int NEW = 30;

    int DELPROP = 31;

    int TYPEOF = 32;

    int GETPROP = 33;

    int GETPROPNOWARN = 34;

    int SETPROP = 35;

    int GETELEM = 36;

    int SETELEM = 37;

    int CALL = 38;

    int NAME = 39;

    int NUMBER = 40;

    int STRING = 41;

    int NULL = 42;

    int THIS = 43;

    int FALSE = 44;

    int TRUE = 45;

    int SHEQ = 46;

    int SHNE = 47;

    int REGEXP = 48;

    int BINDNAME = 49;

    int THROW = 50;

    int RETHROW = 51;

    int IN = 52;

    int INSTANCEOF = 53;

    int LOCAL_LOAD = 54;

    int GETVAR = 55;

    int SETVAR = 56;

    int CATCH_SCOPE = 57;

    int ENUM_INIT_KEYS = 58;

    int ENUM_INIT_VALUES = 59;

    int ENUM_INIT_ARRAY = 60;

    int ENUM_INIT_VALUES_IN_ORDER = 61;

    int ENUM_NEXT = 62;

    int ENUM_ID = 63;

    int THISFN = 64;

    int RETURN_RESULT = 65;

    int ARRAYLIT = 66;

    int OBJECTLIT = 67;

    int GET_REF = 68;

    int SET_REF = 69;

    int DEL_REF = 70;

    int REF_CALL = 71;

    int REF_SPECIAL = 72;

    int YIELD = 73;

    int STRICT_SETNAME = 74;

    int NULLISH_COALESCING = 75;

    int POW = 76;

    int OPTIONAL_CHAINING = 77;

    int GETOPTIONAL = 78;

    int LAST_BYTECODE_TOKEN = 81;

    int TRY = 82;

    int SEMI = 83;

    int LB = 84;

    int RB = 85;

    int LC = 86;

    int RC = 87;

    int LP = 88;

    int RP = 89;

    int COMMA = 90;

    int ASSIGN = 91;

    int ASSIGN_BITOR = 92;

    int ASSIGN_BITXOR = 93;

    int ASSIGN_BITAND = 94;

    int ASSIGN_LSH = 95;

    int ASSIGN_RSH = 96;

    int ASSIGN_URSH = 97;

    int ASSIGN_ADD = 98;

    int ASSIGN_SUB = 99;

    int ASSIGN_MUL = 100;

    int ASSIGN_DIV = 101;

    int ASSIGN_MOD = 102;

    int FIRST_ASSIGN = 91;

    int LAST_ASSIGN = 102;

    int HOOK = 103;

    int COLON = 104;

    int OR = 105;

    int AND = 106;

    int INC = 107;

    int DEC = 108;

    int DOT = 109;

    int FUNCTION = 110;

    int EXPORT = 111;

    int IMPORT = 112;

    int IF = 113;

    int ELSE = 114;

    int SWITCH = 115;

    int CASE = 116;

    int DEFAULT = 117;

    int WHILE = 118;

    int DO = 119;

    int FOR = 120;

    int BREAK = 121;

    int CONTINUE = 122;

    int VAR = 123;

    int WITH = 124;

    int CATCH = 125;

    int FINALLY = 126;

    int VOID = 127;

    int RESERVED = 128;

    int EMPTY = 129;

    int BLOCK = 130;

    int LABEL = 131;

    int TARGET = 132;

    int LOOP = 133;

    int EXPR_VOID = 134;

    int EXPR_RESULT = 135;

    int JSR = 136;

    int SCRIPT = 137;

    int TYPEOFNAME = 138;

    int USE_STACK = 139;

    int SETPROP_OP = 140;

    int SETELEM_OP = 141;

    int LOCAL_BLOCK = 142;

    int SET_REF_OP = 143;

    int TO_OBJECT = 150;

    int TO_DOUBLE = 151;

    int GET = 152;

    int SET = 153;

    int LET = 154;

    int CONST = 155;

    int SETCONST = 156;

    int SETCONSTVAR = 157;

    int ARRAYCOMP = 158;

    int LETEXPR = 159;

    int WITHEXPR = 160;

    int COMMENT = 162;

    int GENEXPR = 163;

    int METHOD = 164;

    int ARROW = 165;

    int YIELD_STAR = 166;

    int TEMPLATE_LITERAL = 167;

    int TEMPLATE_CHARS = 168;

    int TEMPLATE_LITERAL_SUBST = 169;

    int TAGGED_TEMPLATE_LITERAL = 170;

    int LAST_TOKEN = 170;

    static String name(int token) {
        return String.valueOf(token);
    }

    static String typeToName(int token) {
        return switch(token) {
            case -1 ->
                "ERROR";
            case 0 ->
                "EOF";
            case 1 ->
                "EOL";
            case 2 ->
                "ENTERWITH";
            case 3 ->
                "LEAVEWITH";
            case 4 ->
                "RETURN";
            case 5 ->
                "GOTO";
            case 6 ->
                "IFEQ";
            case 7 ->
                "IFNE";
            case 8 ->
                "SETNAME";
            case 9 ->
                "BITOR";
            case 10 ->
                "BITXOR";
            case 11 ->
                "BITAND";
            case 12 ->
                "EQ";
            case 13 ->
                "NE";
            case 14 ->
                "LT";
            case 15 ->
                "LE";
            case 16 ->
                "GT";
            case 17 ->
                "GE";
            case 18 ->
                "LSH";
            case 19 ->
                "RSH";
            case 20 ->
                "URSH";
            case 21 ->
                "ADD";
            case 22 ->
                "SUB";
            case 23 ->
                "MUL";
            case 24 ->
                "DIV";
            case 25 ->
                "MOD";
            case 26 ->
                "NOT";
            case 27 ->
                "BITNOT";
            case 28 ->
                "POS";
            case 29 ->
                "NEG";
            case 30 ->
                "NEW";
            case 31 ->
                "DELETE";
            case 32 ->
                "TYPEOF";
            case 33 ->
                "GETPROP";
            case 34 ->
                "GETPROPNOWARN";
            case 35 ->
                "SETPROP";
            case 36 ->
                "GETELEM";
            case 37 ->
                "SETELEM";
            case 38 ->
                "CALL";
            case 39 ->
                "NAME";
            case 40 ->
                "NUMBER";
            case 41 ->
                "STRING";
            case 42 ->
                "NULL";
            case 43 ->
                "THIS";
            case 44 ->
                "FALSE";
            case 45 ->
                "TRUE";
            case 46 ->
                "SHEQ";
            case 47 ->
                "SHNE";
            case 48 ->
                "REGEXP";
            case 49 ->
                "BINDNAME";
            case 50 ->
                "THROW";
            case 51 ->
                "RETHROW";
            case 52 ->
                "IN";
            case 53 ->
                "INSTANCEOF";
            case 54 ->
                "LOCAL_LOAD";
            case 55 ->
                "GETVAR";
            case 56 ->
                "SETVAR";
            case 57 ->
                "CATCH_SCOPE";
            case 58 ->
                "ENUM_INIT_KEYS";
            case 59 ->
                "ENUM_INIT_VALUES";
            case 60 ->
                "ENUM_INIT_ARRAY";
            case 61 ->
                "ENUM_INIT_VALUES_IN_ORDER";
            case 62 ->
                "ENUM_NEXT";
            case 63 ->
                "ENUM_ID";
            case 64 ->
                "THISFN";
            case 65 ->
                "RETURN_RESULT";
            case 66 ->
                "ARRAYLIT";
            case 67 ->
                "OBJECTLIT";
            case 68 ->
                "GET_REF";
            case 69 ->
                "SET_REF";
            case 70 ->
                "DEL_REF";
            case 71 ->
                "REF_CALL";
            case 72 ->
                "REF_SPECIAL";
            case 73 ->
                "YIELD";
            default ->
                throw new IllegalStateException(String.valueOf(token));
            case 75 ->
                "NULLISH_COALESCING";
            case 76 ->
                "POW";
            case 77 ->
                "OPTIONAL_CHAINING";
            case 78 ->
                "GETOPTIONAL";
            case 82 ->
                "TRY";
            case 83 ->
                "SEMI";
            case 84 ->
                "LB";
            case 85 ->
                "RB";
            case 86 ->
                "LC";
            case 87 ->
                "RC";
            case 88 ->
                "LP";
            case 89 ->
                "RP";
            case 90 ->
                "COMMA";
            case 91 ->
                "ASSIGN";
            case 92 ->
                "ASSIGN_BITOR";
            case 93 ->
                "ASSIGN_BITXOR";
            case 94 ->
                "ASSIGN_BITAND";
            case 95 ->
                "ASSIGN_LSH";
            case 96 ->
                "ASSIGN_RSH";
            case 97 ->
                "ASSIGN_URSH";
            case 98 ->
                "ASSIGN_ADD";
            case 99 ->
                "ASSIGN_SUB";
            case 100 ->
                "ASSIGN_MUL";
            case 101 ->
                "ASSIGN_DIV";
            case 102 ->
                "ASSIGN_MOD";
            case 103 ->
                "HOOK";
            case 104 ->
                "COLON";
            case 105 ->
                "OR";
            case 106 ->
                "AND";
            case 107 ->
                "INC";
            case 108 ->
                "DEC";
            case 109 ->
                "DOT";
            case 110 ->
                "FUNCTION";
            case 111 ->
                "EXPORT";
            case 112 ->
                "IMPORT";
            case 113 ->
                "IF";
            case 114 ->
                "ELSE";
            case 115 ->
                "SWITCH";
            case 116 ->
                "CASE";
            case 117 ->
                "DEFAULT";
            case 118 ->
                "WHILE";
            case 119 ->
                "DO";
            case 120 ->
                "FOR";
            case 121 ->
                "BREAK";
            case 122 ->
                "CONTINUE";
            case 123 ->
                "VAR";
            case 124 ->
                "WITH";
            case 125 ->
                "CATCH";
            case 126 ->
                "FINALLY";
            case 127 ->
                "VOID";
            case 128 ->
                "RESERVED";
            case 129 ->
                "EMPTY";
            case 130 ->
                "BLOCK";
            case 131 ->
                "LABEL";
            case 132 ->
                "TARGET";
            case 133 ->
                "LOOP";
            case 134 ->
                "EXPR_VOID";
            case 135 ->
                "EXPR_RESULT";
            case 136 ->
                "JSR";
            case 137 ->
                "SCRIPT";
            case 138 ->
                "TYPEOFNAME";
            case 139 ->
                "USE_STACK";
            case 140 ->
                "SETPROP_OP";
            case 141 ->
                "SETELEM_OP";
            case 142 ->
                "LOCAL_BLOCK";
            case 143 ->
                "SET_REF_OP";
            case 150 ->
                "TO_OBJECT";
            case 151 ->
                "TO_DOUBLE";
            case 152 ->
                "GET";
            case 153 ->
                "SET";
            case 154 ->
                "LET";
            case 155 ->
                "CONST";
            case 156 ->
                "SETCONST";
            case 158 ->
                "ARRAYCOMP";
            case 159 ->
                "LETEXPR";
            case 160 ->
                "WITHEXPR";
            case 162 ->
                "COMMENT";
            case 163 ->
                "GENEXPR";
            case 164 ->
                "METHOD";
            case 165 ->
                "ARROW";
            case 166 ->
                "YIELD_STAR";
            case 167 ->
                "TEMPLATE_LITERAL";
            case 168 ->
                "TEMPLATE_CHARS";
            case 169 ->
                "TEMPLATE_LITERAL_SUBST";
            case 170 ->
                "TAGGED_TEMPLATE_LITERAL";
        };
    }

    static boolean isValidToken(int code) {
        return code >= -1 && code <= 170;
    }

    public static enum CommentType {

        LINE, BLOCK_COMMENT, JSDOC, HTML
    }
}