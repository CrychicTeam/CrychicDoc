package dev.latvian.mods.rhino;

import java.io.IOException;

class TokenStream {

    private static final int EOF_CHAR = -1;

    private static final char BYTE_ORDER_MARK = '\ufeff';

    private final StringBuilder rawString = new StringBuilder();

    private final ObjToIntMap allStrings = new ObjToIntMap(50);

    private final int[] ungetBuffer = new int[3];

    private final String sourceString;

    private final int sourceEnd;

    private final Parser parser;

    String regExpFlags;

    int lineno;

    int sourceCursor;

    int cursor;

    int tokenBeg;

    int tokenEnd;

    Token.CommentType commentType;

    private boolean dirtyLine;

    private String string = "";

    private double number;

    private boolean isBinary;

    private boolean isOldOctal;

    private boolean isOctal;

    private boolean isHex;

    private int quoteChar;

    private char[] stringBuffer = new char[128];

    private int stringBufferTop;

    private int ungetCursor;

    private boolean hitEOF = false;

    private int lineStart = 0;

    private int lineEndChar = -1;

    static boolean isKeyword(String s, boolean isStrict) {
        return 0 != stringToKeyword(s, isStrict);
    }

    private static int stringToKeyword(String name, boolean isStrict) {
        return stringToKeywordForES(name, isStrict);
    }

    private static int stringToKeywordForES(String name, boolean isStrict) {
        return switch(name) {
            case "break" ->
                121;
            case "case" ->
                116;
            case "catch" ->
                125;
            case "const" ->
                155;
            case "continue" ->
                122;
            case "default" ->
                117;
            case "delete" ->
                31;
            case "do" ->
                119;
            case "else" ->
                114;
            case "finally" ->
                126;
            case "for" ->
                120;
            case "function" ->
                110;
            case "if" ->
                113;
            case "in" ->
                52;
            case "instanceof" ->
                53;
            case "new" ->
                30;
            case "return" ->
                4;
            case "switch" ->
                115;
            case "this" ->
                43;
            case "throw" ->
                50;
            case "try" ->
                82;
            case "typeof" ->
                32;
            case "var" ->
                123;
            case "void" ->
                127;
            case "while" ->
                118;
            case "with" ->
                124;
            case "yield" ->
                73;
            case "false" ->
                44;
            case "null" ->
                42;
            case "true" ->
                45;
            case "let" ->
                154;
            case "class", "export", "static", "public", "protected", "private", "package", "interface", "implements", "enum", "await", "super", "import", "extends" ->
                128;
            default ->
                0;
        };
    }

    private static boolean isAlpha(int c) {
        return c <= 90 ? 65 <= c : 97 <= c && c <= 122;
    }

    static boolean isDigit(int c) {
        return 48 <= c && c <= 57;
    }

    static boolean isJSSpace(int c) {
        return c <= 127 ? c == 32 || c == 9 || c == 12 || c == 11 : c == 160 || c == 65279 || Character.getType((char) c) == 12;
    }

    private static boolean isJSFormatChar(int c) {
        return c > 127 && Character.getType((char) c) == 16;
    }

    private static String convertLastCharToHex(String str) {
        int lastIndex = str.length() - 1;
        StringBuilder buf = new StringBuilder(str.substring(0, lastIndex));
        buf.append("\\u");
        String hexCode = Integer.toHexString(str.charAt(lastIndex));
        for (int i = 0; i < 4 - hexCode.length(); i++) {
            buf.append('0');
        }
        buf.append(hexCode);
        return buf.toString();
    }

    TokenStream(Parser parser, String sourceString, int lineno) {
        this.parser = parser;
        this.lineno = lineno;
        if (sourceString == null) {
            Kit.codeBug();
        }
        this.sourceString = sourceString;
        this.sourceEnd = sourceString.length();
        this.sourceCursor = this.cursor = 0;
    }

    final String getSourceString() {
        return this.sourceString;
    }

    final int getLineno() {
        return this.lineno;
    }

    final String getString() {
        return this.string;
    }

    final char getQuoteChar() {
        return (char) this.quoteChar;
    }

    final double getNumber() {
        return this.number;
    }

    final boolean isNumberBinary() {
        return this.isBinary;
    }

    final boolean isNumberOldOctal() {
        return this.isOldOctal;
    }

    final boolean isNumberOctal() {
        return this.isOctal;
    }

    final boolean isNumberHex() {
        return this.isHex;
    }

    final boolean eof() {
        return this.hitEOF;
    }

    final int getToken() throws IOException {
        int c;
        do {
            c = this.getChar();
            if (c == -1) {
                this.tokenBeg = this.cursor - 1;
                this.tokenEnd = this.cursor;
                return 0;
            }
            if (c == 10) {
                this.dirtyLine = false;
                this.tokenBeg = this.cursor - 1;
                this.tokenEnd = this.cursor;
                return 1;
            }
        } while (isJSSpace(c));
        if (c != 45) {
            this.dirtyLine = true;
        }
        this.tokenBeg = this.cursor - 1;
        this.tokenEnd = this.cursor;
        boolean isUnicodeEscapeStart = false;
        boolean identifierStart;
        if (c == 92) {
            c = this.getChar();
            if (c == 117) {
                identifierStart = true;
                isUnicodeEscapeStart = true;
                this.stringBufferTop = 0;
            } else {
                identifierStart = false;
                this.ungetChar(c);
                c = 92;
            }
        } else {
            identifierStart = Character.isJavaIdentifierStart((char) c);
            if (identifierStart) {
                this.stringBufferTop = 0;
                this.addToString(c);
            }
        }
        if (identifierStart) {
            boolean containsEscape = isUnicodeEscapeStart;
            while (true) {
                while (isUnicodeEscapeStart) {
                    int escapeVal = 0;
                    int i = 0;
                    while (true) {
                        if (i != 4) {
                            c = this.getChar();
                            escapeVal = Kit.xDigitToInt(c, escapeVal);
                            if (escapeVal >= 0) {
                                i++;
                                continue;
                            }
                        }
                        if (escapeVal < 0) {
                            this.parser.addError("msg.invalid.escape");
                            return -1;
                        }
                        this.addToString(escapeVal);
                        isUnicodeEscapeStart = false;
                        break;
                    }
                }
                c = this.getChar();
                if (c != 92) {
                    if (c == -1 || c == 65279 || !Character.isJavaIdentifierPart((char) c)) {
                        this.ungetChar(c);
                        String str = this.getStringFromBuffer();
                        if (!containsEscape) {
                            int result = stringToKeyword(str, this.parser.inUseStrictDirective());
                            if (result != 0) {
                                this.string = (String) this.allStrings.intern(str);
                                return result;
                            }
                        } else if (isKeyword(str, this.parser.inUseStrictDirective())) {
                            str = convertLastCharToHex(str);
                        }
                        this.string = (String) this.allStrings.intern(str);
                        return 39;
                    }
                    this.addToString(c);
                } else {
                    c = this.getChar();
                    if (c != 117) {
                        this.parser.addError("msg.illegal.character", c);
                        return -1;
                    }
                    isUnicodeEscapeStart = true;
                    containsEscape = true;
                }
            }
        } else if (isDigit(c) || c == 46 && isDigit(this.peekChar())) {
            this.stringBufferTop = 0;
            int base = 10;
            this.isHex = this.isOldOctal = this.isOctal = this.isBinary = false;
            if (c == 48) {
                c = this.getChar();
                if (c == 120 || c == 88) {
                    base = 16;
                    this.isHex = true;
                    c = this.getChar();
                } else if (c == 111 || c == 79) {
                    base = 8;
                    this.isOctal = true;
                    c = this.getChar();
                } else if (c == 98 || c == 66) {
                    base = 2;
                    this.isBinary = true;
                    c = this.getChar();
                } else if (isDigit(c)) {
                    base = 8;
                    this.isOldOctal = true;
                } else {
                    this.addToString(48);
                }
            }
            boolean isEmpty = true;
            if (base == 16) {
                while (0 <= Kit.xDigitToInt(c, 0)) {
                    this.addToString(c);
                    c = this.getChar();
                    isEmpty = false;
                }
            } else {
                while (48 <= c && c <= 57) {
                    if (base == 8 && c >= 56) {
                        if (!this.isOldOctal) {
                            this.parser.addError("msg.caught.nfe");
                            return -1;
                        }
                        this.parser.addWarning("msg.bad.octal.literal", c == 56 ? "8" : "9");
                        base = 10;
                    } else if (base == 2 && c >= 50) {
                        this.parser.addError("msg.caught.nfe");
                        return -1;
                    }
                    this.addToString(c);
                    c = this.getChar();
                    isEmpty = false;
                }
            }
            if (!isEmpty || !this.isBinary && !this.isOctal && !this.isHex) {
                boolean isInteger = true;
                if (base == 10 && (c == 46 || c == 101 || c == 69)) {
                    isInteger = false;
                    if (c == 46) {
                        do {
                            this.addToString(c);
                            c = this.getChar();
                        } while (isDigit(c));
                    }
                    if (c == 101 || c == 69) {
                        this.addToString(c);
                        c = this.getChar();
                        if (c == 43 || c == 45) {
                            this.addToString(c);
                            c = this.getChar();
                        }
                        if (!isDigit(c)) {
                            this.parser.addError("msg.missing.exponent");
                            return -1;
                        }
                        do {
                            this.addToString(c);
                            c = this.getChar();
                        } while (isDigit(c));
                    }
                }
                this.ungetChar(c);
                String numString = this.getStringFromBuffer();
                this.string = numString;
                double dval;
                if (base == 10 && !isInteger) {
                    try {
                        dval = Double.parseDouble(numString);
                    } catch (NumberFormatException var11) {
                        this.parser.addError("msg.caught.nfe");
                        return -1;
                    }
                } else {
                    dval = ScriptRuntime.stringPrefixToNumber(numString, 0, base);
                }
                this.number = dval;
                return 40;
            } else {
                this.parser.addError("msg.caught.nfe");
                return -1;
            }
        } else if (c != 34 && c != 39) {
            switch(c) {
                case 33:
                    if (this.matchChar(61)) {
                        if (this.matchChar(61)) {
                            return 47;
                        }
                        return 13;
                    }
                    return 26;
                case 34:
                case 35:
                case 36:
                case 39:
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57:
                case 64:
                case 65:
                case 66:
                case 67:
                case 68:
                case 69:
                case 70:
                case 71:
                case 72:
                case 73:
                case 74:
                case 75:
                case 76:
                case 77:
                case 78:
                case 79:
                case 80:
                case 81:
                case 82:
                case 83:
                case 84:
                case 85:
                case 86:
                case 87:
                case 88:
                case 89:
                case 90:
                case 92:
                case 95:
                case 97:
                case 98:
                case 99:
                case 100:
                case 101:
                case 102:
                case 103:
                case 104:
                case 105:
                case 106:
                case 107:
                case 108:
                case 109:
                case 110:
                case 111:
                case 112:
                case 113:
                case 114:
                case 115:
                case 116:
                case 117:
                case 118:
                case 119:
                case 120:
                case 121:
                case 122:
                default:
                    this.parser.addError("msg.illegal.character", c);
                    return -1;
                case 37:
                    if (this.matchChar(61)) {
                        return 102;
                    }
                    return 25;
                case 38:
                    if (this.matchChar(38)) {
                        return 106;
                    } else {
                        if (this.matchChar(61)) {
                            return 94;
                        }
                        return 11;
                    }
                case 40:
                    return 88;
                case 41:
                    return 89;
                case 42:
                    if (this.matchChar(61)) {
                        return 100;
                    } else {
                        if (this.matchChar(42)) {
                            return 76;
                        }
                        return 23;
                    }
                case 43:
                    if (this.matchChar(61)) {
                        return 98;
                    } else {
                        if (this.matchChar(43)) {
                            return 107;
                        }
                        return 21;
                    }
                case 44:
                    return 90;
                case 45:
                    byte var15;
                    if (this.matchChar(61)) {
                        var15 = 99;
                    } else if (this.matchChar(45)) {
                        if (!this.dirtyLine && this.matchChar(62)) {
                            this.markCommentStart("--");
                            this.skipLine();
                            this.commentType = Token.CommentType.HTML;
                            return 162;
                        }
                        var15 = 108;
                    } else {
                        var15 = 22;
                    }
                    this.dirtyLine = true;
                    return var15;
                case 46:
                    return 109;
                case 47:
                    this.markCommentStart();
                    if (this.matchChar(47)) {
                        this.tokenBeg = this.cursor - 2;
                        this.skipLine();
                        this.commentType = Token.CommentType.LINE;
                        return 162;
                    } else if (this.matchChar(42)) {
                        boolean lookForSlash = false;
                        this.tokenBeg = this.cursor - 2;
                        if (this.matchChar(42)) {
                            lookForSlash = true;
                            this.commentType = Token.CommentType.JSDOC;
                        } else {
                            this.commentType = Token.CommentType.BLOCK_COMMENT;
                        }
                        while (true) {
                            c = this.getChar();
                            if (c == -1) {
                                this.tokenEnd = this.cursor - 1;
                                this.parser.addError("msg.unterminated.comment");
                                return 162;
                            }
                            if (c == 42) {
                                lookForSlash = true;
                            } else if (c == 47) {
                                if (lookForSlash) {
                                    this.tokenEnd = this.cursor;
                                    return 162;
                                }
                            } else {
                                lookForSlash = false;
                                this.tokenEnd = this.cursor;
                            }
                        }
                    } else {
                        if (this.matchChar(61)) {
                            return 101;
                        }
                        return 24;
                    }
                case 58:
                    return 104;
                case 59:
                    return 83;
                case 60:
                    if (this.matchChar(33)) {
                        if (this.matchChar(45)) {
                            if (this.matchChar(45)) {
                                this.tokenBeg = this.cursor - 4;
                                this.skipLine();
                                this.commentType = Token.CommentType.HTML;
                                return 162;
                            }
                            this.ungetCharIgnoreLineEnd(45);
                        }
                        this.ungetCharIgnoreLineEnd(33);
                    }
                    if (this.matchChar(60)) {
                        if (this.matchChar(61)) {
                            return 95;
                        }
                        return 18;
                    } else {
                        if (this.matchChar(61)) {
                            return 15;
                        }
                        return 14;
                    }
                case 61:
                    if (this.matchChar(61)) {
                        if (this.matchChar(61)) {
                            return 46;
                        }
                        return 12;
                    } else {
                        if (this.matchChar(62)) {
                            return 165;
                        }
                        return 91;
                    }
                case 62:
                    if (this.matchChar(62)) {
                        if (this.matchChar(62)) {
                            if (this.matchChar(61)) {
                                return 97;
                            }
                            return 20;
                        } else {
                            if (this.matchChar(61)) {
                                return 96;
                            }
                            return 19;
                        }
                    } else {
                        if (this.matchChar(61)) {
                            return 17;
                        }
                        return 16;
                    }
                case 63:
                    if (this.matchChar(63)) {
                        return 75;
                    } else {
                        if (this.matchChar(46)) {
                            return 77;
                        }
                        return 103;
                    }
                case 91:
                    return 84;
                case 93:
                    return 85;
                case 94:
                    if (this.matchChar(61)) {
                        return 93;
                    }
                    return 10;
                case 96:
                    return 167;
                case 123:
                    return 86;
                case 124:
                    if (this.matchChar(124)) {
                        return 105;
                    } else {
                        if (this.matchChar(61)) {
                            return 92;
                        }
                        return 9;
                    }
                case 125:
                    return 87;
                case 126:
                    return 27;
            }
        } else {
            this.quoteChar = c;
            this.stringBufferTop = 0;
            c = this.getChar(false);
            label410: while (c != this.quoteChar) {
                if (c == 10 || c == -1) {
                    this.ungetChar(c);
                    this.tokenEnd = this.cursor;
                    this.parser.addError("msg.unterminated.string.lit");
                    return -1;
                }
                if (c == 92) {
                    c = this.getChar();
                    switch(c) {
                        case 10:
                            c = this.getChar();
                            continue;
                        case 98:
                            c = 8;
                            break;
                        case 102:
                            c = 12;
                            break;
                        case 110:
                            c = 10;
                            break;
                        case 114:
                            c = 13;
                            break;
                        case 116:
                            c = 9;
                            break;
                        case 117:
                            int escapeStart = this.stringBufferTop;
                            this.addToString(117);
                            int escapeValx = 0;
                            for (int i = 0; i != 4; i++) {
                                c = this.getChar();
                                escapeValx = Kit.xDigitToInt(c, escapeValx);
                                if (escapeValx < 0) {
                                    continue label410;
                                }
                                this.addToString(c);
                            }
                            this.stringBufferTop = escapeStart;
                            c = escapeValx;
                            break;
                        case 118:
                            c = 11;
                            break;
                        case 120:
                            c = this.getChar();
                            int escapeVal = Kit.xDigitToInt(c, 0);
                            if (escapeVal < 0) {
                                this.addToString(120);
                                continue;
                            }
                            int c1 = c;
                            c = this.getChar();
                            escapeVal = Kit.xDigitToInt(c, escapeVal);
                            if (escapeVal < 0) {
                                this.addToString(120);
                                this.addToString(c1);
                                continue;
                            }
                            c = escapeVal;
                            break;
                        default:
                            if (48 <= c && c < 56) {
                                int val = c - 48;
                                c = this.getChar();
                                if (48 <= c && c < 56) {
                                    val = 8 * val + c - 48;
                                    c = this.getChar();
                                    if (48 <= c && c < 56 && val <= 31) {
                                        val = 8 * val + c - 48;
                                        c = this.getChar();
                                    }
                                }
                                this.ungetChar(c);
                                c = val;
                            }
                    }
                }
                this.addToString(c);
                c = this.getChar(false);
            }
            String str = this.getStringFromBuffer();
            this.string = (String) this.allStrings.intern(str);
            return 41;
        }
    }

    void readRegExp(int startToken) throws IOException {
        int start = this.tokenBeg;
        this.stringBufferTop = 0;
        if (startToken == 101) {
            this.addToString(61);
        } else {
            if (startToken != 24) {
                Kit.codeBug();
            }
            if (this.peekChar() == 42) {
                this.tokenEnd = this.cursor - 1;
                this.string = new String(this.stringBuffer, 0, this.stringBufferTop);
                this.parser.reportError("msg.unterminated.re.lit");
                return;
            }
        }
        boolean inCharSet = false;
        int c;
        while ((c = this.getChar()) != 47 || inCharSet) {
            if (c == 10 || c == -1) {
                this.ungetChar(c);
                this.tokenEnd = this.cursor - 1;
                this.string = new String(this.stringBuffer, 0, this.stringBufferTop);
                this.parser.reportError("msg.unterminated.re.lit");
                return;
            }
            if (c == 92) {
                this.addToString(c);
                c = this.getChar();
                if (c == 10 || c == -1) {
                    this.ungetChar(c);
                    this.tokenEnd = this.cursor - 1;
                    this.string = new String(this.stringBuffer, 0, this.stringBufferTop);
                    this.parser.reportError("msg.unterminated.re.lit");
                    return;
                }
            } else if (c == 91) {
                inCharSet = true;
            } else if (c == 93) {
                inCharSet = false;
            }
            this.addToString(c);
        }
        int reEnd = this.stringBufferTop;
        while (true) {
            while (!this.matchChar(103)) {
                if (this.matchChar(105)) {
                    this.addToString(105);
                } else if (this.matchChar(109)) {
                    this.addToString(109);
                } else {
                    if (!this.matchChar(121)) {
                        this.tokenEnd = start + this.stringBufferTop + 2;
                        if (isAlpha(this.peekChar())) {
                            this.parser.reportError("msg.invalid.re.flag");
                        }
                        this.string = new String(this.stringBuffer, 0, reEnd);
                        this.regExpFlags = new String(this.stringBuffer, reEnd, this.stringBufferTop - reEnd);
                        return;
                    }
                    this.addToString(121);
                }
            }
            this.addToString(103);
        }
    }

    String readAndClearRegExpFlags() {
        String flags = this.regExpFlags;
        this.regExpFlags = null;
        return flags;
    }

    String getRawString() {
        return this.rawString.length() == 0 ? "" : this.rawString.toString();
    }

    private int getTemplateLiteralChar() throws IOException {
        boolean unget = this.ungetCursor != 0;
        int oldLineEnd = this.lineEndChar;
        int c = this.getCharIgnoreLineEnd(false);
        if (c == 10) {
            c = this.lineEndChar;
        }
        if (oldLineEnd >= 0 && !unget && (oldLineEnd != 13 || c != 10)) {
            this.lineEndChar = -1;
            this.lineStart = this.sourceCursor - 1;
            this.lineno++;
        }
        this.rawString.append((char) c);
        return c;
    }

    private void ungetTemplateLiteralChar(int c) {
        this.ungetCharIgnoreLineEnd(c);
        this.rawString.setLength(this.rawString.length() - 1);
    }

    private boolean matchTemplateLiteralChar(int test) throws IOException {
        int c = this.getTemplateLiteralChar();
        if (c == test) {
            return true;
        } else {
            this.ungetTemplateLiteralChar(c);
            return false;
        }
    }

    private int peekTemplateLiteralChar() throws IOException {
        int c = this.getTemplateLiteralChar();
        this.ungetTemplateLiteralChar(c);
        return c;
    }

    int readTemplateLiteral() throws IOException {
        this.rawString.setLength(0);
        this.stringBufferTop = 0;
        while (true) {
            int c = this.getTemplateLiteralChar();
            switch(c) {
                case -1:
                    this.string = this.getStringFromBuffer();
                    this.tokenEnd = this.cursor - 1;
                    this.parser.reportError("msg.unexpected.eof");
                    return -1;
                case 36:
                    if (this.matchTemplateLiteralChar(123)) {
                        this.rawString.setLength(this.rawString.length() - 2);
                        this.string = this.getStringFromBuffer();
                        this.tokenEnd = this.cursor - 1;
                        return 169;
                    }
                    this.addToString(c);
                    break;
                case 92:
                    c = this.getTemplateLiteralChar();
                    switch(c) {
                        case 10:
                        case 8232:
                        case 8233:
                            continue;
                        case 13:
                            this.matchTemplateLiteralChar(10);
                            continue;
                        case 34:
                        case 39:
                        case 92:
                        default:
                            break;
                        case 48:
                            int d = this.peekTemplateLiteralChar();
                            if (d >= 48 && d <= 57) {
                                this.parser.reportError("msg.syntax");
                                return -1;
                            }
                            c = 0;
                            break;
                        case 49:
                        case 50:
                        case 51:
                        case 52:
                        case 53:
                        case 54:
                        case 55:
                        case 56:
                        case 57:
                            this.parser.reportError("msg.syntax");
                            return -1;
                        case 98:
                            c = 8;
                            break;
                        case 102:
                            c = 12;
                            break;
                        case 110:
                            c = 10;
                            break;
                        case 114:
                            c = 13;
                            break;
                        case 116:
                            c = 9;
                            break;
                        case 117:
                            int escapeVal = 0;
                            c = this.getTemplateLiteralChar();
                            if (c == 123) {
                                c = this.getTemplateLiteralChar();
                                do {
                                    escapeVal = Kit.xDigitToInt(c, escapeVal);
                                    if (escapeVal < 0 || escapeVal > 1114111) {
                                        this.parser.reportError("msg.syntax");
                                        return -1;
                                    }
                                } while ((c = this.getTemplateLiteralChar()) != 125);
                                if (escapeVal > 65535) {
                                    this.addToString(Character.highSurrogate(escapeVal));
                                    this.addToString(Character.lowSurrogate(escapeVal));
                                    continue;
                                }
                                c = escapeVal;
                            } else {
                                escapeVal = Kit.xDigitToInt(c, escapeVal);
                                escapeVal = Kit.xDigitToInt(this.getTemplateLiteralChar(), escapeVal);
                                escapeVal = Kit.xDigitToInt(this.getTemplateLiteralChar(), escapeVal);
                                escapeVal = Kit.xDigitToInt(this.getTemplateLiteralChar(), escapeVal);
                                if (escapeVal < 0) {
                                    this.parser.reportError("msg.syntax");
                                    return -1;
                                }
                                c = escapeVal;
                            }
                            break;
                        case 118:
                            c = 11;
                            break;
                        case 120:
                            int escapeVal = 0;
                            escapeVal = Kit.xDigitToInt(this.getTemplateLiteralChar(), escapeVal);
                            escapeVal = Kit.xDigitToInt(this.getTemplateLiteralChar(), escapeVal);
                            if (escapeVal < 0) {
                                this.parser.reportError("msg.syntax");
                                return -1;
                            }
                            c = escapeVal;
                    }
                    this.addToString(c);
                    break;
                case 96:
                    this.rawString.setLength(this.rawString.length() - 1);
                    this.string = this.getStringFromBuffer();
                    return 167;
                default:
                    this.addToString(c);
            }
        }
    }

    private String getStringFromBuffer() {
        this.tokenEnd = this.cursor;
        return new String(this.stringBuffer, 0, this.stringBufferTop);
    }

    private void addToString(int c) {
        int N = this.stringBufferTop;
        if (N == this.stringBuffer.length) {
            char[] tmp = new char[this.stringBuffer.length * 2];
            System.arraycopy(this.stringBuffer, 0, tmp, 0, N);
            this.stringBuffer = tmp;
        }
        this.stringBuffer[N] = (char) c;
        this.stringBufferTop = N + 1;
    }

    private boolean canUngetChar() {
        return this.ungetCursor == 0 || this.ungetBuffer[this.ungetCursor - 1] != 10;
    }

    private void ungetChar(int c) {
        if (this.ungetCursor != 0 && this.ungetBuffer[this.ungetCursor - 1] == 10) {
            Kit.codeBug();
        }
        this.ungetBuffer[this.ungetCursor++] = c;
        this.cursor--;
    }

    private boolean matchChar(int test) throws IOException {
        int c = this.getCharIgnoreLineEnd();
        if (c == test) {
            this.tokenEnd = this.cursor;
            return true;
        } else {
            this.ungetCharIgnoreLineEnd(c);
            return false;
        }
    }

    private int peekChar() throws IOException {
        int c = this.getChar();
        this.ungetChar(c);
        return c;
    }

    private int getChar() throws IOException {
        return this.getChar(true);
    }

    private int getChar(boolean skipFormattingChars) throws IOException {
        if (this.ungetCursor != 0) {
            this.cursor++;
            return this.ungetBuffer[--this.ungetCursor];
        } else {
            int c;
            while (true) {
                while (true) {
                    if (this.sourceCursor == this.sourceEnd) {
                        this.hitEOF = true;
                        return -1;
                    }
                    this.cursor++;
                    c = this.sourceString.charAt(this.sourceCursor++);
                    if (this.lineEndChar < 0) {
                        break;
                    }
                    if (this.lineEndChar != 13 || c != 10) {
                        this.lineEndChar = -1;
                        this.lineStart = this.sourceCursor - 1;
                        this.lineno++;
                        break;
                    }
                    this.lineEndChar = 10;
                }
                if (c <= 127) {
                    if (c == 10 || c == 13) {
                        this.lineEndChar = c;
                        c = 10;
                    }
                    break;
                }
                if (c == 65279) {
                    return c;
                }
                if (!skipFormattingChars || !isJSFormatChar(c)) {
                    if (ScriptRuntime.isJSLineTerminator(c)) {
                        this.lineEndChar = c;
                        c = 10;
                    }
                    break;
                }
            }
            return c;
        }
    }

    private int getCharIgnoreLineEnd() throws IOException {
        return this.getCharIgnoreLineEnd(true);
    }

    private int getCharIgnoreLineEnd(boolean skipFormattingChars) throws IOException {
        if (this.ungetCursor != 0) {
            this.cursor++;
            return this.ungetBuffer[--this.ungetCursor];
        } else {
            int c;
            while (true) {
                if (this.sourceCursor == this.sourceEnd) {
                    this.hitEOF = true;
                    return -1;
                }
                this.cursor++;
                c = this.sourceString.charAt(this.sourceCursor++);
                if (c <= 127) {
                    if (c == 10 || c == 13) {
                        this.lineEndChar = c;
                        c = 10;
                    }
                    break;
                }
                if (c == 65279) {
                    return c;
                }
                if (!skipFormattingChars || !isJSFormatChar(c)) {
                    if (ScriptRuntime.isJSLineTerminator(c)) {
                        this.lineEndChar = c;
                        c = 10;
                    }
                    break;
                }
            }
            return c;
        }
    }

    private void ungetCharIgnoreLineEnd(int c) {
        this.ungetBuffer[this.ungetCursor++] = c;
        this.cursor--;
    }

    private void skipLine() throws IOException {
        int c;
        while ((c = this.getChar()) != -1 && c != 10) {
        }
        this.ungetChar(c);
        this.tokenEnd = this.cursor;
    }

    final int getOffset() {
        int n = this.sourceCursor - this.lineStart;
        if (this.lineEndChar >= 0) {
            n--;
        }
        return n;
    }

    private int charAt(int index) {
        return index >= 0 && index < this.sourceEnd ? this.sourceString.charAt(index) : -1;
    }

    private String substring(int beginIndex, int endIndex) {
        return this.sourceString.substring(beginIndex, endIndex);
    }

    final String getLine() {
        int lineEnd = this.sourceCursor;
        if (this.lineEndChar >= 0) {
            if (this.lineEndChar == 10 && this.charAt(--lineEnd - 1) == 13) {
                lineEnd--;
            }
        } else {
            int lineLength = lineEnd - this.lineStart;
            while (true) {
                int c = this.charAt(this.lineStart + lineLength);
                if (c == -1 || ScriptRuntime.isJSLineTerminator(c)) {
                    lineEnd = this.lineStart + lineLength;
                    break;
                }
                lineLength++;
            }
        }
        return this.substring(this.lineStart, lineEnd);
    }

    final String getLine(int position, int[] linep) {
        assert position >= 0 && position <= this.cursor;
        assert linep.length == 2;
        int delta = this.cursor + this.ungetCursor - position;
        int cur = this.sourceCursor;
        if (delta > cur) {
            return null;
        } else {
            int end = 0;
            int lines;
            for (lines = 0; delta > 0; cur--) {
                assert cur > 0;
                int c = this.charAt(cur - 1);
                if (ScriptRuntime.isJSLineTerminator(c)) {
                    if (c == 10 && this.charAt(cur - 2) == 13) {
                        delta--;
                        cur--;
                    }
                    lines++;
                    end = cur - 1;
                }
                delta--;
            }
            int start = 0;
            int offset;
            for (offset = 0; cur > 0; offset++) {
                int c = this.charAt(cur - 1);
                if (ScriptRuntime.isJSLineTerminator(c)) {
                    start = cur;
                    break;
                }
                cur--;
            }
            linep[0] = this.lineno - lines + (this.lineEndChar >= 0 ? 1 : 0);
            linep[1] = offset;
            return lines == 0 ? this.getLine() : this.substring(start, end);
        }
    }

    public int getCursor() {
        return this.cursor;
    }

    public int getTokenBeg() {
        return this.tokenBeg;
    }

    public int getTokenEnd() {
        return this.tokenEnd;
    }

    public int getTokenLength() {
        return this.tokenEnd - this.tokenBeg;
    }

    public Token.CommentType getCommentType() {
        return this.commentType;
    }

    private void markCommentStart() {
        this.markCommentStart("");
    }

    private void markCommentStart(String prefix) {
    }
}