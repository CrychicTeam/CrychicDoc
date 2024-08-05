declare module "packages/com/vladsch/flexmark/parser/block/$BlockPreProcessor" {
import {$Block, $Block$Type} from "packages/com/vladsch/flexmark/util/ast/$Block"
import {$ParserState, $ParserState$Type} from "packages/com/vladsch/flexmark/parser/block/$ParserState"

export interface $BlockPreProcessor {

 "preProcess"(arg0: $ParserState$Type, arg1: $Block$Type): void

(arg0: $ParserState$Type, arg1: $Block$Type): void
}

export namespace $BlockPreProcessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockPreProcessor$Type = ($BlockPreProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockPreProcessor_ = $BlockPreProcessor$Type;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/builder/tree/$SegmentOffsetTree" {
import {$BasedSegmentBuilder, $BasedSegmentBuilder$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$BasedSegmentBuilder"
import {$IBasedSegmentBuilder, $IBasedSegmentBuilder$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$IBasedSegmentBuilder"
import {$SegmentTree, $SegmentTree$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/tree/$SegmentTree"
import {$Segment, $Segment$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/tree/$Segment"
import {$SegmentTreePos, $SegmentTreePos$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/tree/$SegmentTreePos"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$SegmentTreeRange, $SegmentTreeRange$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/tree/$SegmentTreeRange"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"
import {$Seg, $Seg$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$Seg"

export class $SegmentOffsetTree extends $SegmentTree {
static readonly "MAX_VALUE": integer
static readonly "F_ANCHOR_FLAGS": integer


public "toString"(arg0: $BasedSequence$Type): string
public static "build"(arg0: $Iterable$Type<($Seg$Type)>, arg1: charseq): $SegmentOffsetTree
public static "build"(arg0: $BasedSegmentBuilder$Type): $SegmentOffsetTree
public static "build"(arg0: $BasedSequence$Type): $SegmentOffsetTree
public "endOffset"(arg0: integer): integer
/**
 * 
 * @deprecated
 */
public "addSegments"(arg0: $IBasedSegmentBuilder$Type<(any)>, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer): void
/**
 * 
 * @deprecated
 */
public "addSegments"(arg0: $IBasedSegmentBuilder$Type<(any)>, arg1: $SegmentTreeRange$Type): void
public "getStartIndex"(arg0: integer): integer
/**
 * 
 * @deprecated
 */
public "previousAnchorOffset"(arg0: integer): integer
public "findSegmentPosByOffset"(arg0: integer): $SegmentTreePos
public "findSegmentByOffset"(arg0: integer, arg1: $BasedSequence$Type, arg2: $Segment$Type): $Segment
public "getSegment"(arg0: integer, arg1: $BasedSequence$Type): $Segment
/**
 * 
 * @deprecated
 */
public "aggrLength"(arg0: integer): integer
/**
 * 
 * @deprecated
 */
public "hasPreviousAnchor"(arg0: integer): boolean
/**
 * 
 * @deprecated
 */
public "findSegmentPos"(arg0: integer, arg1: integer, arg2: integer): $SegmentTreePos
/**
 * 
 * @deprecated
 */
public "findSegmentPos"(arg0: integer): $SegmentTreePos
/**
 * 
 * @deprecated
 */
public "getSegmentRange"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $BasedSequence$Type, arg5: $Segment$Type): $SegmentTreeRange
/**
 * 
 * @deprecated
 */
public "findSegment"(arg0: integer, arg1: integer, arg2: integer, arg3: $BasedSequence$Type, arg4: $Segment$Type): $Segment
/**
 * 
 * @deprecated
 */
public "findSegment"(arg0: integer, arg1: $BasedSequence$Type, arg2: $Segment$Type): $Segment
/**
 * 
 * @deprecated
 */
public "getPrevAnchor"(arg0: integer, arg1: $BasedSequence$Type): $Segment
public "getNextText"(arg0: $Segment$Type, arg1: $BasedSequence$Type): $Segment
public "getPreviousText"(arg0: $Segment$Type, arg1: $BasedSequence$Type): $Segment
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SegmentOffsetTree$Type = ($SegmentOffsetTree);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SegmentOffsetTree_ = $SegmentOffsetTree$Type;
}}
declare module "packages/com/vladsch/flexmark/parser/block/$BlockParser" {
import {$Block, $Block$Type} from "packages/com/vladsch/flexmark/util/ast/$Block"
import {$InlineParser, $InlineParser$Type} from "packages/com/vladsch/flexmark/parser/$InlineParser"
import {$MutableDataHolder, $MutableDataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$MutableDataHolder"
import {$ParserState, $ParserState$Type} from "packages/com/vladsch/flexmark/parser/block/$ParserState"
import {$BlockContinue, $BlockContinue$Type} from "packages/com/vladsch/flexmark/parser/block/$BlockContinue"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"
import {$BlockContent, $BlockContent$Type} from "packages/com/vladsch/flexmark/util/ast/$BlockContent"
import {$BlockParserFactory, $BlockParserFactory$Type} from "packages/com/vladsch/flexmark/parser/block/$BlockParserFactory"

export interface $BlockParser {

 "getBlock"(): $Block
 "isClosed"(): boolean
 "getDataHolder"(): $MutableDataHolder
 "addLine"(arg0: $ParserState$Type, arg1: $BasedSequence$Type): void
 "breakOutOnDoubleBlankLine"(): boolean
 "finalizeClosedBlock"(): void
 "isPropagatingLastBlankLine"(arg0: $BlockParser$Type): boolean
 "closeBlock"(arg0: $ParserState$Type): void
 "parseInlines"(arg0: $InlineParser$Type): void
 "isInterruptible"(): boolean
 "isRawText"(): boolean
 "canContain"(arg0: $ParserState$Type, arg1: $BlockParser$Type, arg2: $Block$Type): boolean
 "isParagraphParser"(): boolean
 "getBlockContent"(): $BlockContent
 "canInterruptBy"(arg0: $BlockParserFactory$Type): boolean
 "tryContinue"(arg0: $ParserState$Type): $BlockContinue
 "isContainer"(): boolean
}

export namespace $BlockParser {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockParser$Type = ($BlockParser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockParser_ = $BlockParser$Type;
}}
declare module "packages/com/vladsch/flexmark/parser/$PostProcessorFactory" {
import {$Document, $Document$Type} from "packages/com/vladsch/flexmark/util/ast/$Document"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Dependent, $Dependent$Type} from "packages/com/vladsch/flexmark/util/dependency/$Dependent"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$PostProcessor, $PostProcessor$Type} from "packages/com/vladsch/flexmark/parser/$PostProcessor"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $PostProcessorFactory extends $Function<($Document), ($PostProcessor)>, $Dependent {

 "getNodeTypes"(): $Map<($Class<(any)>), ($Set<($Class<(any)>)>)>
 "apply"(arg0: $Document$Type): $PostProcessor
 "compose"<V>(arg0: $Function$Type<(any), (any)>): $Function<(V), ($PostProcessor)>
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $Function<($Document), (V)>
 "getAfterDependents"(): $Set<($Class<(any)>)>
 "affectsGlobalScope"(): boolean
 "getBeforeDependents"(): $Set<($Class<(any)>)>
}

export namespace $PostProcessorFactory {
function identity<T>(): $Function<($Document), ($Document)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PostProcessorFactory$Type = ($PostProcessorFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PostProcessorFactory_ = $PostProcessorFactory$Type;
}}
declare module "packages/com/vladsch/flexmark/ast/util/$Parsing" {
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$DataHolder, $DataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$DataHolder"

export class $Parsing {
static readonly "INTELLIJ_DUMMY_IDENTIFIER_CHAR": character
static readonly "INTELLIJ_DUMMY_IDENTIFIER": string
static readonly "XML_NAMESPACE_START": string
static readonly "XML_NAMESPACE_CHAR": string
static readonly "XML_NAMESPACE": string
readonly "options": $DataHolder
readonly "EOL": string
readonly "ESCAPED_CHAR": string
readonly "LINK_LABEL": $Pattern
readonly "LINK_DESTINATION_ANGLES": $Pattern
readonly "LINK_TITLE_STRING": string
readonly "LINK_TITLE": $Pattern
readonly "LINK_DESTINATION": $Pattern
readonly "LINK_DESTINATION_MATCHED_PARENS": $Pattern
readonly "LINK_DESTINATION_MATCHED_PARENS_NOSP": $Pattern
static readonly "ST_HTMLCOMMENT": string
static readonly "ST_PROCESSINGINSTRUCTION": string
static readonly "ST_CDATA": string
static readonly "ST_SINGLEQUOTEDVALUE": string
static readonly "ST_DOUBLEQUOTEDVALUE": string
readonly "HTMLCOMMENT": string
readonly "PROCESSINGINSTRUCTION": string
readonly "CDATA": string
readonly "SINGLEQUOTEDVALUE": string
readonly "DOUBLEQUOTEDVALUE": string
readonly "ASCII_PUNCTUATION": string
readonly "ASCII_OPEN_PUNCTUATION": string
readonly "ASCII_CLOSE_PUNCTUATION": string
readonly "PUNCTUATION": $Pattern
readonly "PUNCTUATION_OPEN": $Pattern
readonly "PUNCTUATION_CLOSE": $Pattern
readonly "PUNCTUATION_ONLY": $Pattern
readonly "PUNCTUATION_OPEN_ONLY": $Pattern
readonly "PUNCTUATION_CLOSE_ONLY": $Pattern
readonly "ESCAPABLE": $Pattern
readonly "TICKS": $Pattern
readonly "TICKS_HERE": $Pattern
readonly "EMAIL_AUTOLINK": $Pattern
readonly "AUTOLINK": $Pattern
readonly "WWW_AUTOLINK": $Pattern
readonly "SPNL": $Pattern
readonly "SPNL_URL": $Pattern
readonly "SPNI": $Pattern
readonly "SP": $Pattern
readonly "REST_OF_LINE": $Pattern
readonly "UNICODE_WHITESPACE_CHAR": $Pattern
readonly "WHITESPACE": $Pattern
readonly "FINAL_SPACE": $Pattern
readonly "LINE_END": $Pattern
readonly "ADDITIONAL_CHARS": string
readonly "EXCLUDED_0_TO_SPACE": string
readonly "REG_CHAR": string
readonly "REG_CHAR_PARENS": string
readonly "REG_CHAR_SP": string
readonly "REG_CHAR_SP_PARENS": string
readonly "IN_PARENS_NOSP": string
readonly "IN_PARENS_W_SP": string
readonly "IN_MATCHED_PARENS_NOSP": string
readonly "IN_MATCHED_PARENS_W_SP": string
readonly "IN_BRACES_W_SP": string
readonly "DECLARATION": string
readonly "ENTITY": string
readonly "TAGNAME": string
readonly "ATTRIBUTENAME": string
readonly "UNQUOTEDVALUE": string
readonly "ATTRIBUTEVALUE": string
readonly "ATTRIBUTEVALUESPEC": string
readonly "ATTRIBUTE": string
readonly "OPENTAG": string
readonly "CLOSETAG": string
readonly "HTMLTAG": string
readonly "ENTITY_HERE": $Pattern
readonly "HTML_TAG": $Pattern
readonly "LIST_ITEM_MARKER": $Pattern
readonly "CODE_BLOCK_INDENT": integer
readonly "intellijDummyIdentifier": boolean
readonly "htmlForTranslator": boolean
readonly "translationHtmlInlineTagPattern": string
readonly "translationAutolinkTagPattern": string
readonly "spaceInLinkUrl": boolean
readonly "parseJekyllMacroInLinkUrl": boolean
readonly "itemPrefixChars": string
readonly "listsItemMarkerSpace": boolean
readonly "listsOrderedItemDotOnly": boolean
readonly "allowNameSpace": boolean

constructor(arg0: $DataHolder$Type)

/**
 * 
 * @deprecated
 */
public "ADDITIONAL_CHARS"(): string
public static "findLineBreak"(arg0: charseq, arg1: integer): integer
public static "isBlank"(arg0: charseq): boolean
public static "isLetter"(arg0: charseq, arg1: integer): boolean
/**
 * 
 * @deprecated
 */
public "EXCLUDED_0_TO_SPACE"(): string
/**
 * 
 * @deprecated
 */
public "ADDITIONAL_CHARS_SET"(arg0: string): string
public static "columnsToNextTabStop"(arg0: integer): integer
public static "isSpaceOrTab"(arg0: charseq, arg1: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Parsing$Type = ($Parsing);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Parsing_ = $Parsing$Type;
}}
declare module "packages/com/vladsch/flexmark/parser/core/delimiter/$Delimiter" {
import {$DelimiterRun, $DelimiterRun$Type} from "packages/com/vladsch/flexmark/parser/delimiter/$DelimiterRun"
import {$DelimitedNode, $DelimitedNode$Type} from "packages/com/vladsch/flexmark/util/ast/$DelimitedNode"
import {$Text, $Text$Type} from "packages/com/vladsch/flexmark/ast/$Text"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"

export class $Delimiter implements $DelimiterRun {

constructor(arg0: $BasedSequence$Type, arg1: $Text$Type, arg2: character, arg3: boolean, arg4: boolean, arg5: $Delimiter$Type, arg6: integer)

public "length"(): integer
public "getIndex"(): integer
public "getNode"(): $Text
public "getEndIndex"(): integer
public "setIndex"(arg0: integer): void
public "getInput"(): $BasedSequence
public "canOpen"(): boolean
public "isMatched"(): boolean
public "getStartIndex"(): integer
public "getPreviousNonDelimiterTextNode"(): $Text
public "moveNodesBetweenDelimitersTo"(arg0: $DelimitedNode$Type, arg1: $Delimiter$Type): void
public "convertDelimitersToText"(arg0: integer, arg1: $Delimiter$Type): void
public "getNextNonDelimiterTextNode"(): $Text
public "setNext"(arg0: $Delimiter$Type): void
public "setPrevious"(arg0: $Delimiter$Type): void
public "getTailChars"(arg0: integer): $BasedSequence
public "getNumDelims"(): integer
public "setNumDelims"(arg0: integer): void
public "setMatched"(arg0: boolean): void
public "getLeadChars"(arg0: integer): $BasedSequence
public "canClose"(): boolean
public "getDelimiterChar"(): character
get "index"(): integer
get "node"(): $Text
get "endIndex"(): integer
set "index"(value: integer)
get "input"(): $BasedSequence
get "matched"(): boolean
get "startIndex"(): integer
get "previousNonDelimiterTextNode"(): $Text
get "nextNonDelimiterTextNode"(): $Text
set "next"(value: $Delimiter$Type)
set "previous"(value: $Delimiter$Type)
get "numDelims"(): integer
set "numDelims"(value: integer)
set "matched"(value: boolean)
get "delimiterChar"(): character
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Delimiter$Type = ($Delimiter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Delimiter_ = $Delimiter$Type;
}}
declare module "packages/com/vladsch/flexmark/parser/$InlineParserOptions" {
import {$DataHolder, $DataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$DataHolder"

export class $InlineParserOptions {
readonly "matchLookaheadFirst": boolean
readonly "parseMultiLineImageUrls": boolean
readonly "hardLineBreakLimit": boolean
readonly "spaceInLinkUrls": boolean
readonly "spaceInLinkElements": boolean
readonly "codeSoftLineBreaks": boolean
readonly "inlineDelimiterDirectionalPunctuations": boolean
readonly "linksAllowMatchedParentheses": boolean
readonly "wwwAutoLinkElement": boolean
readonly "intellijDummyIdentifier": boolean
readonly "parseJekyllMacrosInUrls": boolean
readonly "useHardcodedLinkAddressParser": boolean
readonly "linkTextPriorityOverLinkRef": boolean

constructor(arg0: $DataHolder$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InlineParserOptions$Type = ($InlineParserOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InlineParserOptions_ = $InlineParserOptions$Type;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/$Range" {
import {$RichSequence, $RichSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$RichSequence"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"

export class $Range {
static readonly "NULL": $Range
static readonly "EMPTY": $Range


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compare"(arg0: $Range$Type): integer
public "isEmpty"(): boolean
public static "of"(arg0: integer, arg1: integer): $Range
/**
 * 
 * @deprecated
 */
public "subSequence"(arg0: charseq): $BasedSequence
public "contains"(arg0: $Range$Type): boolean
public "contains"(arg0: integer): boolean
public "contains"(arg0: integer, arg1: integer): boolean
public "shiftLeft"(arg0: integer): $Range
public "isNull"(): boolean
public "shiftRight"(arg0: integer): $Range
public "isEqual"(arg0: $Range$Type): boolean
public "include"(arg0: integer): $Range
public "include"(arg0: $Range$Type): $Range
public "include"(arg0: integer, arg1: integer): $Range
public "withRange"(arg0: integer, arg1: integer): $Range
public "isContainedBy"(arg0: integer, arg1: integer): boolean
public "isContainedBy"(arg0: $Range$Type): boolean
public "component2"(): integer
public "exclude"(arg0: $Range$Type): $Range
public "intersect"(arg0: $Range$Type): $Range
public "getEnd"(): integer
public "overlaps"(arg0: $Range$Type): boolean
public "isNotNull"(): boolean
public "getEndOffset"(): integer
public "getStartOffset"(): integer
public "properlyContains"(arg0: $Range$Type): boolean
public "component1"(): integer
public "isNotEmpty"(): boolean
public "isLast"(arg0: integer): boolean
public "doesOverlapOrAdjacent"(arg0: $Range$Type): boolean
public "doesNotOverlapOrAdjacent"(arg0: $Range$Type): boolean
public "doesProperlyContain"(arg0: $Range$Type): boolean
public "basedSafeSubSequence"(arg0: charseq): $BasedSequence
public "isProperlyContainedBy"(arg0: $Range$Type): boolean
public "isProperlyContainedBy"(arg0: integer, arg1: integer): boolean
public "doesNotOverlapNorAdjacent"(arg0: $Range$Type): boolean
public "richSafeSubSequence"(arg0: charseq): $RichSequence
public "isStart"(arg0: integer): boolean
public "getSpan"(): integer
public static "emptyOf"(arg0: integer): $Range
public "withStart"(arg0: integer): $Range
public "getStart"(): integer
public "isEnd"(arg0: integer): boolean
public "safeSubSequence"(arg0: charseq): charseq
public "isAdjacent"(arg0: $Range$Type): boolean
public "isAdjacent"(arg0: integer): boolean
public "withEnd"(arg0: integer): $Range
public "endPlus"(arg0: integer): $Range
public "startPlus"(arg0: integer): $Range
public "endMinus"(arg0: integer): $Range
public static "ofLength"(arg0: integer, arg1: integer): $Range
public "startMinus"(arg0: integer): $Range
public "doesContain"(arg0: $Range$Type): boolean
public "doesContain"(arg0: integer): boolean
public "doesContain"(arg0: integer, arg1: integer): boolean
public "trails"(arg0: integer): boolean
public "doesNotOverlap"(arg0: $Range$Type): boolean
public "richSubSequence"(arg0: charseq): $RichSequence
public "expandToInclude"(arg0: $Range$Type): $Range
public "expandToInclude"(arg0: integer, arg1: integer): $Range
public "overlapsOrAdjacent"(arg0: $Range$Type): boolean
public "isValidIndex"(arg0: integer): boolean
public "basedSubSequence"(arg0: charseq): $BasedSequence
public "leads"(arg0: integer): boolean
public "doesOverlap"(arg0: $Range$Type): boolean
public "charSubSequence"(arg0: charseq): charseq
public "leadBy"(arg0: integer): boolean
public "isAdjacentBefore"(arg0: integer): boolean
public "isAdjacentBefore"(arg0: $Range$Type): boolean
public "trailedBy"(arg0: integer): boolean
public "isAdjacentAfter"(arg0: $Range$Type): boolean
public "isAdjacentAfter"(arg0: integer): boolean
get "empty"(): boolean
get "null"(): boolean
get "end"(): integer
get "notNull"(): boolean
get "endOffset"(): integer
get "startOffset"(): integer
get "notEmpty"(): boolean
get "span"(): integer
get "start"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Range$Type = ($Range);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Range_ = $Range$Type;
}}
declare module "packages/com/vladsch/flexmark/util/ast/$Node" {
import {$ReversiblePeekingIterable, $ReversiblePeekingIterable$Type} from "packages/com/vladsch/flexmark/util/collection/iteration/$ReversiblePeekingIterable"
import {$Range, $Range$Type} from "packages/com/vladsch/flexmark/util/sequence/$Range"
import {$Document, $Document$Type} from "packages/com/vladsch/flexmark/util/ast/$Document"
import {$AstNode, $AstNode$Type} from "packages/com/vladsch/flexmark/util/visitor/$AstNode"
import {$ReversiblePeekingIterator, $ReversiblePeekingIterator$Type} from "packages/com/vladsch/flexmark/util/collection/iteration/$ReversiblePeekingIterator"
import {$StringBuilder, $StringBuilder$Type} from "packages/java/lang/$StringBuilder"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Pair, $Pair$Type} from "packages/com/vladsch/flexmark/util/misc/$Pair"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"

export class $Node {
static readonly "EMPTY_SEGMENTS": ($BasedSequence)[]
static readonly "SPLICE": string
static readonly "AST_ADAPTER": $AstNode<($Node)>

constructor()
constructor(arg0: $BasedSequence$Type)

public static "segmentSpanCharsToVisible"(arg0: $StringBuilder$Type, arg1: $BasedSequence$Type, arg2: string): void
public "getOldestAncestorOfTypeAfter"(arg0: $Class$Type<(any)>, arg1: $Class$Type<(any)>): $Node
public "getFirstChildAnyNot"(...arg0: ($Class$Type<(any)>)[]): $Node
public "getReversedDescendants"(): $ReversiblePeekingIterable<($Node)>
public "countDirectAncestorsOfType"(arg0: $Class$Type<(any)>, ...arg1: ($Class$Type<(any)>)[]): integer
public static "delimitedSegmentSpanChars"(arg0: $StringBuilder$Type, arg1: $BasedSequence$Type, arg2: $BasedSequence$Type, arg3: $BasedSequence$Type, arg4: string): void
public "isOrDescendantOfType"(...arg0: ($Class$Type<(any)>)[]): boolean
public "getReversedChildIterator"(): $ReversiblePeekingIterator<($Node)>
public "extractToFirstInChain"(arg0: $Node$Type): void
public "setCharsFromContentOnly"(): void
public "getBlankLineSibling"(): $Node
public "getLastBlankLineChild"(): $Node
public "moveTrailingBlankLines"(): void
public "getSegmentsForChars"(): ($BasedSequence)[]
public "getCharsFromSegments"(): $BasedSequence
public "setCharsFromSegments"(): void
public "countAncestorsOfType"(...arg0: ($Class$Type<(any)>)[]): integer
public "getReversedChildren"(): $ReversiblePeekingIterable<($Node)>
public static "delimitedSegmentSpan"(arg0: $StringBuilder$Type, arg1: $BasedSequence$Type, arg2: $BasedSequence$Type, arg3: $BasedSequence$Type, arg4: string): void
public "setCharsFromContent"(): void
public "toString"(): string
public "getChars"(): $BasedSequence
public "getParent"(): $Node
public "getLineNumber"(): integer
public "unlink"(): void
public "getTextLength"(): integer
public "getChildren"(): $ReversiblePeekingIterable<($Node)>
public "setChars"(arg0: $BasedSequence$Type): void
public "getNext"(): $Node
public "lineColumnAtStart"(): $Pair<(integer), (integer)>
public "getSourceRange"(): $Range
public "lineColumnAtIndex"(arg0: integer): $Pair<(integer), (integer)>
public "getLineColumnAtEnd"(): $Pair<(integer), (integer)>
public "getEmptySuffix"(): $BasedSequence
public "getEmptyPrefix"(): $BasedSequence
public "getBaseSequence"(): $BasedSequence
public "getAncestorOfType"(...arg0: ($Class$Type<(any)>)[]): $Node
public "baseSubSequence"(arg0: integer, arg1: integer): $BasedSequence
public "baseSubSequence"(arg0: integer): $BasedSequence
public "getChildOfType"(...arg0: ($Class$Type<(any)>)[]): $Node
public "getEndOfLine"(): integer
public "getStartOfLine"(): integer
public "getFirstInChain"(): $Node
public "astExtraChars"(arg0: $StringBuilder$Type): void
public static "astChars"(arg0: $StringBuilder$Type, arg1: charseq, arg2: string): void
public "getNextAnyNot"(...arg0: ($Class$Type<(any)>)[]): $Node
public "getNextAny"(...arg0: ($Class$Type<(any)>)[]): $Node
public "getLastChildAnyNot"(...arg0: ($Class$Type<(any)>)[]): $Node
public "getLastChildAny"(...arg0: ($Class$Type<(any)>)[]): $Node
public "getGrandParent"(): $Node
public "getSegments"(): ($BasedSequence)[]
public static "getLeadSegment"(arg0: ($BasedSequence$Type)[]): $BasedSequence
public static "getTrailSegment"(arg0: ($BasedSequence$Type)[]): $BasedSequence
public static "spanningChars"(...arg0: ($BasedSequence$Type)[]): $BasedSequence
public "getDescendants"(): $ReversiblePeekingIterable<($Node)>
public "getPreviousAnyNot"(...arg0: ($Class$Type<(any)>)[]): $Node
public "getChildIterator"(): $ReversiblePeekingIterator<($Node)>
public "getPreviousAny"(...arg0: ($Class$Type<(any)>)[]): $Node
public "prependChild"(arg0: $Node$Type): void
public "getFirstChildAny"(...arg0: ($Class$Type<(any)>)[]): $Node
public static "getNodeOfTypeIndex"(arg0: $Node$Type, ...arg1: ($Class$Type<(any)>)[]): integer
public "getNodeOfTypeIndex"(...arg0: ($Class$Type<(any)>)[]): integer
public "extractChainTo"(arg0: $Node$Type): void
public "getLastInChain"(): $Node
public "hasOrMoreChildren"(arg0: integer): boolean
public "getAstExtra"(arg0: $StringBuilder$Type): void
public "getExactChildChars"(): $BasedSequence
public "getEndLineNumber"(): integer
public static "segmentSpan"(arg0: $StringBuilder$Type, arg1: integer, arg2: integer, arg3: string): void
public static "segmentSpan"(arg0: $StringBuilder$Type, arg1: $BasedSequence$Type, arg2: string): void
public "takeChildren"(arg0: $Node$Type): void
public "toAstString"(arg0: boolean): string
public static "toSegmentSpan"(arg0: $BasedSequence$Type, arg1: string): string
public static "segmentSpanChars"(arg0: $StringBuilder$Type, arg1: integer, arg2: integer, arg3: string, arg4: string): void
public static "segmentSpanChars"(arg0: $StringBuilder$Type, arg1: $BasedSequence$Type, arg2: string): void
public static "segmentSpanChars"(arg0: $StringBuilder$Type, arg1: integer, arg2: integer, arg3: string, arg4: string, arg5: string, arg6: string): void
public "getChildChars"(): $BasedSequence
public "getStartLineNumber"(): integer
public "astString"(arg0: $StringBuilder$Type, arg1: boolean): void
public "appendChain"(arg0: $Node$Type): void
public "insertChainAfter"(arg0: $Node$Type): void
public "insertChainBefore"(arg0: $Node$Type): void
public "hasChildren"(): boolean
public "getPrevious"(): $Node
public "getEndOffset"(): integer
public "getStartOffset"(): integer
public "removeChildren"(): void
public "endOfLine"(arg0: integer): integer
public "getDocument"(): $Document
public "appendChild"(arg0: $Node$Type): void
public "getLastChild"(): $Node
public "getNodeName"(): string
public "getFirstChild"(): $Node
public "insertBefore"(arg0: $Node$Type): void
public "insertAfter"(arg0: $Node$Type): void
public "startOfLine"(arg0: integer): integer
get "reversedDescendants"(): $ReversiblePeekingIterable<($Node)>
get "reversedChildIterator"(): $ReversiblePeekingIterator<($Node)>
get "blankLineSibling"(): $Node
get "lastBlankLineChild"(): $Node
get "segmentsForChars"(): ($BasedSequence)[]
get "charsFromSegments"(): $BasedSequence
get "reversedChildren"(): $ReversiblePeekingIterable<($Node)>
get "chars"(): $BasedSequence
get "parent"(): $Node
get "lineNumber"(): integer
get "textLength"(): integer
get "children"(): $ReversiblePeekingIterable<($Node)>
set "chars"(value: $BasedSequence$Type)
get "next"(): $Node
get "sourceRange"(): $Range
get "lineColumnAtEnd"(): $Pair<(integer), (integer)>
get "emptySuffix"(): $BasedSequence
get "emptyPrefix"(): $BasedSequence
get "baseSequence"(): $BasedSequence
get "firstInChain"(): $Node
get "grandParent"(): $Node
get "segments"(): ($BasedSequence)[]
get "descendants"(): $ReversiblePeekingIterable<($Node)>
get "childIterator"(): $ReversiblePeekingIterator<($Node)>
get "lastInChain"(): $Node
get "exactChildChars"(): $BasedSequence
get "endLineNumber"(): integer
get "childChars"(): $BasedSequence
get "startLineNumber"(): integer
get "previous"(): $Node
get "endOffset"(): integer
get "startOffset"(): integer
get "document"(): $Document
get "lastChild"(): $Node
get "nodeName"(): string
get "firstChild"(): $Node
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Node$Type = ($Node);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Node_ = $Node$Type;
}}
declare module "packages/com/vladsch/flexmark/util/collection/iteration/$ReversibleIterable" {
import {$ReversibleIterator, $ReversibleIterator$Type} from "packages/com/vladsch/flexmark/util/collection/iteration/$ReversibleIterator"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export interface $ReversibleIterable<E> extends $Iterable<(E)> {

 "reversed"(): $ReversibleIterable<(E)>
 "isReversed"(): boolean
 "reversedIterator"(): $ReversibleIterator<(E)>
 "spliterator"(): $Spliterator<(E)>
 "forEach"(arg0: $Consumer$Type<(any)>): void
}

export namespace $ReversibleIterable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReversibleIterable$Type<E> = ($ReversibleIterable<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReversibleIterable_<E> = $ReversibleIterable$Type<(E)>;
}}
declare module "packages/com/vladsch/flexmark/util/data/$DataValueNullableFactory" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$DataHolder, $DataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$DataHolder"
import {$DataValueFactory, $DataValueFactory$Type} from "packages/com/vladsch/flexmark/util/data/$DataValueFactory"

export interface $DataValueNullableFactory<T> extends $DataValueFactory<(T)> {

 "apply"(arg0: $DataHolder$Type): T
 "compose"<V>(arg0: $Function$Type<(any), (any)>): $Function<(V), ($DataHolder)>
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $Function<($DataHolder), (V)>

(arg0: $DataHolder$Type): T
}

export namespace $DataValueNullableFactory {
function identity<T>(): $Function<($DataHolder), ($DataHolder)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataValueNullableFactory$Type<T> = ($DataValueNullableFactory<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataValueNullableFactory_<T> = $DataValueNullableFactory$Type<(T)>;
}}
declare module "packages/com/vladsch/flexmark/util/collection/iteration/$ReversiblePeekingIterator" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ReversibleIterator, $ReversibleIterator$Type} from "packages/com/vladsch/flexmark/util/collection/iteration/$ReversibleIterator"

export interface $ReversiblePeekingIterator<E> extends $ReversibleIterator<(E)> {

 "peek"(): E
 "isReversed"(): boolean
 "remove"(): void
 "forEachRemaining"(arg0: $Consumer$Type<(any)>): void
 "hasNext"(): boolean
 "next"(): E
}

export namespace $ReversiblePeekingIterator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReversiblePeekingIterator$Type<E> = ($ReversiblePeekingIterator<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReversiblePeekingIterator_<E> = $ReversiblePeekingIterator$Type<(E)>;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/builder/$SegmentStats" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SegmentStats {
static readonly "NULL_REPEATED_CHAR": integer
static readonly "NOT_REPEATED_CHAR": integer

constructor(arg0: boolean)

public "add"(arg0: $SegmentStats$Type): void
public "remove"(arg0: $SegmentStats$Type): void
public "toString"(): string
public "clear"(): void
public "isEmpty"(): boolean
public "isValid"(): boolean
public "getTextLength"(): integer
public "isTrackTextFirst256"(): boolean
public "getTextFirst256Length"(): integer
public "getTextFirst256Segments"(): integer
public "getTextSpaceSegments"(): integer
public "isTextRepeatedSpace"(): boolean
public "addText"(arg0: charseq): void
public "addText"(arg0: character): void
public "addText"(arg0: character, arg1: integer): void
public "getTextSegments"(): integer
public "getTextSpaceLength"(): integer
public "commitText"(): void
public "isTextFirst256"(): boolean
public "isRepeatedText"(): boolean
public "committedCopy"(): $SegmentStats
public "removeText"(arg0: charseq): void
get "empty"(): boolean
get "valid"(): boolean
get "textLength"(): integer
get "trackTextFirst256"(): boolean
get "textFirst256Length"(): integer
get "textFirst256Segments"(): integer
get "textSpaceSegments"(): integer
get "textRepeatedSpace"(): boolean
get "textSegments"(): integer
get "textSpaceLength"(): integer
get "textFirst256"(): boolean
get "repeatedText"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SegmentStats$Type = ($SegmentStats);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SegmentStats_ = $SegmentStats$Type;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/$ReplacedTextRegion" {
import {$Range, $Range$Type} from "packages/com/vladsch/flexmark/util/sequence/$Range"

export class $ReplacedTextRegion {

constructor(arg0: $Range$Type, arg1: $Range$Type, arg2: $Range$Type)

public "containsReplacedIndex"(arg0: integer): boolean
public "containsOriginalIndex"(arg0: integer): boolean
public "getBaseRange"(): $Range
public "getOriginalRange"(): $Range
public "getReplacedRange"(): $Range
public "containsBaseIndex"(arg0: integer): boolean
get "baseRange"(): $Range
get "originalRange"(): $Range
get "replacedRange"(): $Range
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReplacedTextRegion$Type = ($ReplacedTextRegion);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReplacedTextRegion_ = $ReplacedTextRegion$Type;
}}
declare module "packages/com/vladsch/flexmark/parser/$Parser$Builder" {
import {$DelimiterProcessor, $DelimiterProcessor$Type} from "packages/com/vladsch/flexmark/parser/delimiter/$DelimiterProcessor"
import {$BlockPreProcessorFactory, $BlockPreProcessorFactory$Type} from "packages/com/vladsch/flexmark/parser/block/$BlockPreProcessorFactory"
import {$ParagraphPreProcessorFactory, $ParagraphPreProcessorFactory$Type} from "packages/com/vladsch/flexmark/parser/block/$ParagraphPreProcessorFactory"
import {$DataHolder, $DataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$DataHolder"
import {$PostProcessorFactory, $PostProcessorFactory$Type} from "packages/com/vladsch/flexmark/parser/$PostProcessorFactory"
import {$BuilderBase, $BuilderBase$Type} from "packages/com/vladsch/flexmark/util/builder/$BuilderBase"
import {$InlineParserFactory, $InlineParserFactory$Type} from "packages/com/vladsch/flexmark/parser/$InlineParserFactory"
import {$LinkRefProcessorFactory, $LinkRefProcessorFactory$Type} from "packages/com/vladsch/flexmark/parser/$LinkRefProcessorFactory"
import {$CustomBlockParserFactory, $CustomBlockParserFactory$Type} from "packages/com/vladsch/flexmark/parser/block/$CustomBlockParserFactory"
import {$SpecialLeadInHandler, $SpecialLeadInHandler$Type} from "packages/com/vladsch/flexmark/util/sequence/mappers/$SpecialLeadInHandler"
import {$InlineParserExtensionFactory, $InlineParserExtensionFactory$Type} from "packages/com/vladsch/flexmark/parser/$InlineParserExtensionFactory"

export class $Parser$Builder extends $BuilderBase<($Parser$Builder)> {

constructor(arg0: $DataHolder$Type)
constructor()

public "customInlineParserExtensionFactory"(arg0: $InlineParserExtensionFactory$Type): $Parser$Builder
public "specialLeadInHandler"(arg0: $SpecialLeadInHandler$Type): $Parser$Builder
public "postProcessorFactory"(arg0: $PostProcessorFactory$Type): $Parser$Builder
public "linkRefProcessorFactory"(arg0: $LinkRefProcessorFactory$Type): $Parser$Builder
public "blockPreProcessorFactory"(arg0: $BlockPreProcessorFactory$Type): $Parser$Builder
public "customInlineParserFactory"(arg0: $InlineParserFactory$Type): $Parser$Builder
public "paragraphPreProcessorFactory"(arg0: $ParagraphPreProcessorFactory$Type): $Parser$Builder
public "customBlockParserFactory"(arg0: $CustomBlockParserFactory$Type): $Parser$Builder
public "customDelimiterProcessor"(arg0: $DelimiterProcessor$Type): $Parser$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Parser$Builder$Type = ($Parser$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Parser$Builder_ = $Parser$Builder$Type;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/builder/$SegmentOptimizer" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Object;, $Object;$Type} from "packages/[Ljava/lang/$Object;"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"

export interface $SegmentOptimizer extends $BiFunction<(charseq), ((any)[]), ((any)[])> {

 "apply"(arg0: charseq, arg1: (any)[]): (any)[]
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $BiFunction<(charseq), ($Object;), (V)>

(arg0: (any)[], arg1: integer): (any)[]
}

export namespace $SegmentOptimizer {
function insert(arg0: (any)[], arg1: integer): (any)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SegmentOptimizer$Type = ($SegmentOptimizer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SegmentOptimizer_ = $SegmentOptimizer$Type;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/builder/$SegmentedSequenceStats$StatsEntry" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"

export class $SegmentedSequenceStats$StatsEntry implements $Comparable<($SegmentedSequenceStats$StatsEntry)> {

constructor(arg0: integer)

public "add"(arg0: $SegmentedSequenceStats$StatsEntry$Type): void
public "add"(arg0: integer, arg1: integer, arg2: integer): void
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "compareTo"(arg0: $SegmentedSequenceStats$StatsEntry$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SegmentedSequenceStats$StatsEntry$Type = ($SegmentedSequenceStats$StatsEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SegmentedSequenceStats$StatsEntry_ = $SegmentedSequenceStats$StatsEntry$Type;
}}
declare module "packages/com/vladsch/flexmark/util/builder/$BuilderBase" {
import {$DataHolder, $DataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$DataHolder"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$MutableDataSet, $MutableDataSet$Type} from "packages/com/vladsch/flexmark/util/data/$MutableDataSet"
import {$DataKey, $DataKey$Type} from "packages/com/vladsch/flexmark/util/data/$DataKey"

export class $BuilderBase<T extends $BuilderBase<(T)>> extends $MutableDataSet {


/**
 * 
 * @deprecated
 */
public "get"<V>(arg0: $DataKey$Type<(V)>): V
public "set"<V>(arg0: $DataKey$Type<(V)>, arg1: V): $MutableDataSet
public "build"(): any
public "extensions"(arg0: $Collection$Type<(any)>): T
public static "removeExtensions"(arg0: $DataHolder$Type, arg1: $Collection$Type<($Class$Type<(any)>)>): $DataHolder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuilderBase$Type<T> = ($BuilderBase<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuilderBase_<T> = $BuilderBase$Type<(T)>;
}}
declare module "packages/com/vladsch/flexmark/util/data/$MutableDataValueSetter" {
import {$MutableDataHolder, $MutableDataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$MutableDataHolder"

export interface $MutableDataValueSetter<T> {

 "set"(arg0: $MutableDataHolder$Type, arg1: T): $MutableDataHolder

(arg0: $MutableDataHolder$Type, arg1: T): $MutableDataHolder
}

export namespace $MutableDataValueSetter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MutableDataValueSetter$Type<T> = ($MutableDataValueSetter<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MutableDataValueSetter_<T> = $MutableDataValueSetter$Type<(T)>;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/mappers/$SpecialLeadInHandler" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$DataHolder, $DataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$DataHolder"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"

export interface $SpecialLeadInHandler {

 "escape"(arg0: $BasedSequence$Type, arg1: $DataHolder$Type, arg2: $Consumer$Type<(charseq)>): boolean
 "unEscape"(arg0: $BasedSequence$Type, arg1: $DataHolder$Type, arg2: $Consumer$Type<(charseq)>): boolean
}

export namespace $SpecialLeadInHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpecialLeadInHandler$Type = ($SpecialLeadInHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpecialLeadInHandler_ = $SpecialLeadInHandler$Type;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/builder/tree/$Segment" {
import {$Segment$SegType, $Segment$SegType$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/tree/$Segment$SegType"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"
import {$Seg, $Seg$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$Seg"

export class $Segment {

constructor(arg0: integer, arg1: (byte)[], arg2: integer, arg3: integer)

public "length"(): integer
public "toString"(): string
public static "getChar"(arg0: (byte)[], arg1: integer): character
public static "getInt"(arg0: (byte)[], arg1: integer, arg2: integer): integer
public "charAt"(arg0: integer): character
public "getBytes"(): (byte)[]
public "getType"(): $Segment$SegType
public "getEndIndex"(): integer
public "isText"(): boolean
public "hasAll"(arg0: integer, arg1: integer): boolean
public "getByteLength"(): integer
public static "addChar"(arg0: (byte)[], arg1: integer, arg2: character): integer
public "getEndOffset"(): integer
public "getStartOffset"(): integer
public "getStartIndex"(): integer
public static "addChars"(arg0: (byte)[], arg1: integer, arg2: charseq, arg3: integer, arg4: integer): integer
public "isAnchor"(): boolean
public "getPos"(): integer
public "getCharSequence"(): charseq
public static "addCharsAscii"(arg0: (byte)[], arg1: integer, arg2: charseq, arg3: integer, arg4: integer): integer
public "isFirst256Start"(): boolean
public "offsetNotInSegment"(arg0: integer): boolean
public static "getIntBytes"(arg0: integer): integer
public static "addIntBytes"(arg0: (byte)[], arg1: integer, arg2: integer, arg3: integer): integer
public static "addCharAscii"(arg0: (byte)[], arg1: integer, arg2: character): integer
public "isRepeatedTextEnd"(): boolean
public static "getLengthBytes"(arg0: integer): integer
public static "getCharAscii"(arg0: (byte)[], arg1: integer): character
public static "getOffsetBytes"(arg0: integer): integer
public static "getSegment"(arg0: (byte)[], arg1: integer, arg2: integer, arg3: integer, arg4: $BasedSequence$Type): $Segment
public "getByteOffset"(): integer
public "notInSegment"(arg0: integer): boolean
public "isBase"(): boolean
public static "getSegType"(arg0: $Seg$Type, arg1: charseq): $Segment$SegType
public static "getSegByteLength"(arg0: $Seg$Type, arg1: charseq): integer
public static "getSegByteLength"(arg0: $Segment$SegType$Type, arg1: integer, arg2: integer): integer
public static "addSegBytes"(arg0: (byte)[], arg1: integer, arg2: $Seg$Type, arg3: charseq): integer
get "bytes"(): (byte)[]
get "type"(): $Segment$SegType
get "endIndex"(): integer
get "text"(): boolean
get "byteLength"(): integer
get "endOffset"(): integer
get "startOffset"(): integer
get "startIndex"(): integer
get "anchor"(): boolean
get "pos"(): integer
get "charSequence"(): charseq
get "first256Start"(): boolean
get "repeatedTextEnd"(): boolean
get "byteOffset"(): integer
get "base"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Segment$Type = ($Segment);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Segment_ = $Segment$Type;
}}
declare module "packages/com/vladsch/flexmark/util/data/$DataHolder" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$DataValueFactory, $DataValueFactory$Type} from "packages/com/vladsch/flexmark/util/data/$DataValueFactory"
import {$DataSet, $DataSet$Type} from "packages/com/vladsch/flexmark/util/data/$DataSet"
import {$MutableDataHolder, $MutableDataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$MutableDataHolder"
import {$MutableDataSetter, $MutableDataSetter$Type} from "packages/com/vladsch/flexmark/util/data/$MutableDataSetter"
import {$DataKey, $DataKey$Type} from "packages/com/vladsch/flexmark/util/data/$DataKey"
import {$DataKeyBase, $DataKeyBase$Type} from "packages/com/vladsch/flexmark/util/data/$DataKeyBase"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $DataHolder extends $MutableDataSetter {

/**
 * 
 * @deprecated
 */
 "get"<T>(arg0: $DataKey$Type<(T)>): T
 "contains"(arg0: $DataKeyBase$Type<(any)>): boolean
 "setIn"(arg0: $MutableDataHolder$Type): $MutableDataHolder
 "getAll"(): $Map<(any), (any)>
 "getKeys"(): $Collection<(any)>
 "toImmutable"(): $DataHolder
 "toMutable"(): $MutableDataHolder
 "toDataSet"(): $DataSet
 "getOrCompute"(arg0: $DataKeyBase$Type<(any)>, arg1: $DataValueFactory$Type<(any)>): any
}

export namespace $DataHolder {
const NULL: $DataHolder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataHolder$Type = ($DataHolder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataHolder_ = $DataHolder$Type;
}}
declare module "packages/com/vladsch/flexmark/util/ast/$TextContainer" {
import {$NodeVisitor, $NodeVisitor$Type} from "packages/com/vladsch/flexmark/util/ast/$NodeVisitor"
import {$ISequenceBuilder, $ISequenceBuilder$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$ISequenceBuilder"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"

export interface $TextContainer {

 "collectText"(arg0: $ISequenceBuilder$Type<(any), ($BasedSequence$Type)>, arg1: integer, arg2: $NodeVisitor$Type): boolean
 "collectEndText"(arg0: $ISequenceBuilder$Type<(any), ($BasedSequence$Type)>, arg1: integer, arg2: $NodeVisitor$Type): void

(arg0: $ISequenceBuilder$Type<(any), ($BasedSequence$Type)>, arg1: integer, arg2: $NodeVisitor$Type): boolean
}

export namespace $TextContainer {
const F_LINK_TEXT_TYPE: integer
const F_LINK_TEXT: integer
const F_LINK_PAGE_REF: integer
const F_LINK_ANCHOR: integer
const F_LINK_URL: integer
const F_LINK_NODE_TEXT: integer
const F_NODE_TEXT: integer
const F_FOR_HEADING_ID: integer
const F_NO_TRIM_REF_TEXT_START: integer
const F_NO_TRIM_REF_TEXT_END: integer
const F_ADD_SPACES_BETWEEN_NODES: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextContainer$Type = ($TextContainer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextContainer_ = $TextContainer$Type;
}}
declare module "packages/com/vladsch/flexmark/parser/block/$MatchedBlockParser" {
import {$BlockParser, $BlockParser$Type} from "packages/com/vladsch/flexmark/parser/block/$BlockParser"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MutableDataHolder, $MutableDataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$MutableDataHolder"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"

export interface $MatchedBlockParser {

 "getBlockParser"(): $BlockParser
 "getParagraphLines"(): $List<($BasedSequence)>
 "getParagraphDataHolder"(): $MutableDataHolder
 "getParagraphEolLengths"(): $List<(integer)>
 "getParagraphContent"(): $BasedSequence
}

export namespace $MatchedBlockParser {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MatchedBlockParser$Type = ($MatchedBlockParser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MatchedBlockParser_ = $MatchedBlockParser$Type;
}}
declare module "packages/com/vladsch/flexmark/parser/block/$ParserState" {
import {$BlockParser, $BlockParser$Type} from "packages/com/vladsch/flexmark/parser/block/$BlockParser"
import {$Block, $Block$Type} from "packages/com/vladsch/flexmark/util/ast/$Block"
import {$ParserPhase, $ParserPhase$Type} from "packages/com/vladsch/flexmark/parser/block/$ParserPhase"
import {$BlockTracker, $BlockTracker$Type} from "packages/com/vladsch/flexmark/util/ast/$BlockTracker"
import {$List, $List$Type} from "packages/java/util/$List"
import {$InlineParser, $InlineParser$Type} from "packages/com/vladsch/flexmark/parser/$InlineParser"
import {$Parsing, $Parsing$Type} from "packages/com/vladsch/flexmark/ast/util/$Parsing"
import {$MutableDataHolder, $MutableDataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$MutableDataHolder"
import {$Node, $Node$Type} from "packages/com/vladsch/flexmark/util/ast/$Node"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"
import {$BlockParserTracker, $BlockParserTracker$Type} from "packages/com/vladsch/flexmark/parser/block/$BlockParserTracker"

export interface $ParserState extends $BlockTracker, $BlockParserTracker {

 "isBlank"(): boolean
 "getProperties"(): $MutableDataHolder
 "getLineNumber"(): integer
 "getIndex"(): integer
 "getLine"(): $BasedSequence
 "getLineEndIndex"(): integer
 "getInlineParser"(): $InlineParser
 "endsWithBlankLine"(arg0: $Node$Type): boolean
 "getParserPhase"(): $ParserPhase
 "isBlankLine"(): boolean
 "getParsing"(): $Parsing
 "getLineWithEOL"(): $BasedSequence
 "isLastLineBlank"(arg0: $Node$Type): boolean
 "getLineSegments"(): $List<($BasedSequence)>
 "getLineEolLength"(): integer
 "getIndent"(): integer
 "getLineStart"(): integer
 "getNextNonSpaceIndex"(): integer
 "getActiveBlockParser"(arg0: $Block$Type): $BlockParser
 "getActiveBlockParser"(): $BlockParser
 "getActiveBlockParsers"(): $List<($BlockParser)>
 "getColumn"(): integer
 "blockAdded"(arg0: $Block$Type): void
 "blockRemoved"(arg0: $Block$Type): void
 "blockAddedWithChildren"(arg0: $Block$Type): void
 "blockAddedWithDescendants"(arg0: $Block$Type): void
 "blockRemovedWithDescendants"(arg0: $Block$Type): void
 "blockRemovedWithChildren"(arg0: $Block$Type): void
 "blockParserAdded"(arg0: $BlockParser$Type): void
 "blockParserRemoved"(arg0: $BlockParser$Type): void
}

export namespace $ParserState {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParserState$Type = ($ParserState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParserState_ = $ParserState$Type;
}}
declare module "packages/com/vladsch/flexmark/util/data/$NullableDataKey" {
import {$DataValueNullableFactory, $DataValueNullableFactory$Type} from "packages/com/vladsch/flexmark/util/data/$DataValueNullableFactory"
import {$DataHolder, $DataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$DataHolder"
import {$DataValueFactory, $DataValueFactory$Type} from "packages/com/vladsch/flexmark/util/data/$DataValueFactory"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$MutableDataHolder, $MutableDataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$MutableDataHolder"
import {$DataKeyBase, $DataKeyBase$Type} from "packages/com/vladsch/flexmark/util/data/$DataKeyBase"

export class $NullableDataKey<T> extends $DataKeyBase<(T)> {

constructor(arg0: string)
constructor(arg0: string, arg1: T)
constructor(arg0: string, arg1: $DataKeyBase$Type<(T)>)
constructor(arg0: string, arg1: T, arg2: $DataValueFactory$Type<(T)>)
constructor(arg0: string, arg1: $DataValueNullableFactory$Type<(T)>)
constructor(arg0: string, arg1: $Supplier$Type<(T)>)

public "get"(arg0: $DataHolder$Type): T
public "toString"(): string
public "set"(arg0: $MutableDataHolder$Type, arg1: T): $MutableDataHolder
public "getDefaultValue"(arg0: $DataHolder$Type): T
public "getDefaultValue"(): T
get "defaultValue"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NullableDataKey$Type<T> = ($NullableDataKey<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NullableDataKey_<T> = $NullableDataKey$Type<(T)>;
}}
declare module "packages/com/vladsch/flexmark/util/visitor/$AstActionHandler" {
import {$AstNode, $AstNode$Type} from "packages/com/vladsch/flexmark/util/visitor/$AstNode"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$AstHandler, $AstHandler$Type} from "packages/com/vladsch/flexmark/util/visitor/$AstHandler"
import {$AstAction, $AstAction$Type} from "packages/com/vladsch/flexmark/util/visitor/$AstAction"

export class $AstActionHandler<C extends $AstActionHandler<(C), (N), (A), (H)>, N, A extends $AstAction<(N)>, H extends $AstHandler<(N), (A)>> {

constructor(arg0: $AstNode$Type<(N)>)

public "getAction"(arg0: $Class$Type<(any)>): A
public "getAction"(arg0: N): A
public "getNodeClasses"(): $Set<($Class<(any)>)>
get "nodeClasses"(): $Set<($Class<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AstActionHandler$Type<C, N, A, H> = ($AstActionHandler<(C), (N), (A), (H)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AstActionHandler_<C, N, A, H> = $AstActionHandler$Type<(C), (N), (A), (H)>;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/$BasedSequence" {
import {$Range, $Range$Type} from "packages/com/vladsch/flexmark/util/sequence/$Range"
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$DataHolder, $DataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$DataHolder"
import {$IntStream, $IntStream$Type} from "packages/java/util/stream/$IntStream"
import {$IBasedSegmentBuilder, $IBasedSegmentBuilder$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$IBasedSegmentBuilder"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$ReplacedTextMapper, $ReplacedTextMapper$Type} from "packages/com/vladsch/flexmark/util/sequence/$ReplacedTextMapper"
import {$IRichSequence, $IRichSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$IRichSequence"
import {$Pair, $Pair$Type} from "packages/com/vladsch/flexmark/util/misc/$Pair"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$CharPredicate, $CharPredicate$Type} from "packages/com/vladsch/flexmark/util/misc/$CharPredicate"
import {$DataKeyBase, $DataKeyBase$Type} from "packages/com/vladsch/flexmark/util/data/$DataKeyBase"
import {$SegmentTree, $SegmentTree$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/tree/$SegmentTree"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StringBuilder, $StringBuilder$Type} from "packages/java/lang/$StringBuilder"
import {$CharMapper, $CharMapper$Type} from "packages/com/vladsch/flexmark/util/sequence/mappers/$CharMapper"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$BasedOptionsHolder, $BasedOptionsHolder$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedOptionsHolder"

export interface $BasedSequence extends $IRichSequence<($BasedSequence)>, $BasedOptionsHolder {

 "baseLineColumnAtStart"(): $Pair<(integer), (integer)>
 "baseLineColumnAtEnd"(): $Pair<(integer), (integer)>
 "baseLineColumnAtIndex"(arg0: integer): $Pair<(integer), (integer)>
 "subSequence"(arg0: integer, arg1: integer): $BasedSequence
 "unescape"(arg0: $ReplacedTextMapper$Type): $BasedSequence
 "unescape"(): string
 "getBase"(): any
 "getSourceRange"(): $Range
 "getEmptySuffix"(): $BasedSequence
 "getEmptyPrefix"(): $BasedSequence
 "getBaseSequence"(): $BasedSequence
 "baseSubSequence"(arg0: integer): $BasedSequence
 "baseSubSequence"(arg0: integer, arg1: integer): $BasedSequence
 "baseStartOfLine"(): integer
 "baseStartOfLine"(arg0: integer): integer
 "baseEndOfLine"(): integer
 "baseEndOfLine"(arg0: integer): integer
 "addSegments"(arg0: $IBasedSegmentBuilder$Type<(any)>): void
 "intersect"(arg0: $BasedSequence$Type): $BasedSequence
 "getEndOffset"(): integer
 "getStartOffset"(): integer
 "baseLineRangeAtIndex"(arg0: integer): $Range
 "baseLineRangeAtStart"(): $Range
 "extendToStartOfLine"(): $BasedSequence
 "extendToStartOfLine"(arg0: $CharPredicate$Type, arg1: boolean): $BasedSequence
 "extendToStartOfLine"(arg0: $CharPredicate$Type): $BasedSequence
 "extendToStartOfLine"(arg0: boolean): $BasedSequence
 "baseStartOfLineAnyEOL"(arg0: integer): integer
 "baseStartOfLineAnyEOL"(): integer
 "baseEndOfLineAnyEOL"(arg0: integer): integer
 "baseEndOfLineAnyEOL"(): integer
 "extendByOneOfAnyNot"(arg0: $CharPredicate$Type): $BasedSequence
 "normalizeEndWithEOL"(arg0: $ReplacedTextMapper$Type): $BasedSequence
 "getIndexOffset"(arg0: integer): integer
 "getSegmentTree"(): $SegmentTree
 "extendByOneOfAny"(arg0: $CharPredicate$Type): $BasedSequence
 "isContinuedBy"(arg0: $BasedSequence$Type): boolean
 "containsSomeOf"(arg0: $BasedSequence$Type): boolean
 "safeBaseCharAt"(arg0: integer): character
 "isContinuationOf"(arg0: $BasedSequence$Type): boolean
 "containsAllOf"(arg0: $BasedSequence$Type): boolean
 "spliceAtEnd"(arg0: $BasedSequence$Type): $BasedSequence
 "suffixOf"(arg0: $BasedSequence$Type): $BasedSequence
 "normalizeEOL"(arg0: $ReplacedTextMapper$Type): $BasedSequence
 "isBaseCharAt"(arg0: integer, arg1: $CharPredicate$Type): boolean
 "unescapeNoEntities"(): string
 "extendByAny"(arg0: $CharPredicate$Type): $BasedSequence
 "extendByAny"(arg0: $CharPredicate$Type, arg1: integer): $BasedSequence
 "containsOnlyNotIn"(arg0: $CharPredicate$Type): boolean
 "extendToEndOfLine"(): $BasedSequence
 "extendToEndOfLine"(arg0: $CharPredicate$Type): $BasedSequence
 "extendToEndOfLine"(arg0: boolean): $BasedSequence
 "extendToEndOfLine"(arg0: $CharPredicate$Type, arg1: boolean): $BasedSequence
/**
 * 
 * @deprecated
 */
 "extendToAny"(arg0: $CharPredicate$Type, arg1: integer): $BasedSequence
/**
 * 
 * @deprecated
 */
 "extendToAny"(arg0: $CharPredicate$Type): $BasedSequence
 "containsOnlyIn"(arg0: $CharPredicate$Type): boolean
 "baseColumnAtStart"(): integer
 "baseColumnAtIndex"(arg0: integer): integer
 "extendByAnyNot"(arg0: $CharPredicate$Type): $BasedSequence
 "extendByAnyNot"(arg0: $CharPredicate$Type, arg1: integer): $BasedSequence
 "containsSomeNotIn"(arg0: $CharPredicate$Type): boolean
 "baseLineRangeAtEnd"(): $Range
 "containsSomeIn"(arg0: $CharPredicate$Type): boolean
 "prefixWithIndent"(): $BasedSequence
 "prefixWithIndent"(arg0: integer): $BasedSequence
 "baseColumnAtEnd"(): integer
 "prefixOf"(arg0: $BasedSequence$Type): $BasedSequence
 "toVisibleWhitespaceString"(): string
 "equals"(arg0: any): boolean
 "equals"(arg0: any, arg1: boolean): boolean
 "append"(...arg0: (charseq)[]): $BasedSequence
 "append"(arg0: $Iterable$Type<(any)>): $BasedSequence
 "hashCode"(): integer
 "indexOf"(arg0: character, arg1: integer): integer
 "indexOf"(arg0: charseq): integer
 "indexOf"(arg0: charseq, arg1: integer): integer
 "indexOf"(arg0: charseq, arg1: integer, arg2: integer): integer
 "indexOf"(arg0: character, arg1: integer, arg2: integer): integer
 "indexOf"(arg0: character): integer
/**
 * 
 * @deprecated
 */
 "insert"(arg0: charseq, arg1: integer): $BasedSequence
 "insert"(arg0: integer, arg1: charseq): $BasedSequence
 "startsWith"(arg0: charseq): boolean
 "startsWith"(arg0: charseq, arg1: boolean): boolean
 "startsWith"(arg0: $CharPredicate$Type): boolean
 "lastIndexOf"(arg0: charseq): integer
 "lastIndexOf"(arg0: character): integer
 "lastIndexOf"(arg0: character, arg1: integer): integer
 "lastIndexOf"(arg0: character, arg1: integer, arg2: integer): integer
 "lastIndexOf"(arg0: charseq, arg1: integer, arg2: integer): integer
 "lastIndexOf"(arg0: charseq, arg1: integer): integer
 "isEmpty"(): boolean
 "replace"(arg0: charseq, arg1: charseq): $BasedSequence
 "replace"(arg0: integer, arg1: integer, arg2: charseq): $BasedSequence
 "matches"(arg0: charseq, arg1: boolean): boolean
 "matches"(arg0: charseq): boolean
 "split"(arg0: charseq, arg1: boolean, arg2: $CharPredicate$Type): ($BasedSequence)[]
 "split"(arg0: charseq, arg1: integer, arg2: integer, arg3: $CharPredicate$Type): ($BasedSequence)[]
 "split"(arg0: charseq, arg1: integer, arg2: integer): ($BasedSequence)[]
 "split"(arg0: charseq): ($BasedSequence)[]
/**
 * 
 * @deprecated
 */
 "split"(arg0: character, arg1: integer, arg2: integer): ($BasedSequence)[]
 "split"(arg0: charseq, arg1: integer, arg2: boolean, arg3: $CharPredicate$Type): ($BasedSequence)[]
/**
 * 
 * @deprecated
 */
 "split"(arg0: character): ($BasedSequence)[]
/**
 * 
 * @deprecated
 */
 "split"(arg0: character, arg1: integer): ($BasedSequence)[]
 "toLowerCase"(): $BasedSequence
 "toUpperCase"(): $BasedSequence
 "trim"(arg0: integer): $BasedSequence
 "trim"(arg0: $CharPredicate$Type): $BasedSequence
 "trim"(arg0: integer, arg1: $CharPredicate$Type): $BasedSequence
 "trim"(): $BasedSequence
 "isBlank"(): boolean
 "equalsIgnoreCase"(arg0: any): boolean
 "endsWith"(arg0: charseq): boolean
 "endsWith"(arg0: charseq, arg1: boolean): boolean
 "endsWith"(arg0: $CharPredicate$Type): boolean
 "subSequence"(arg0: integer): $BasedSequence
 "subSequence"(arg0: $Range$Type): $BasedSequence
 "lastChar"(): character
 "delete"(arg0: integer, arg1: integer): $BasedSequence
 "appendTo"(arg0: $StringBuilder$Type, arg1: $CharMapper$Type): $BasedSequence
 "appendTo"(arg0: $StringBuilder$Type, arg1: $CharMapper$Type, arg2: integer): $BasedSequence
 "appendTo"(arg0: $StringBuilder$Type): $BasedSequence
 "appendTo"(arg0: $StringBuilder$Type, arg1: integer): $BasedSequence
 "appendTo"(arg0: $StringBuilder$Type, arg1: integer, arg2: integer): $BasedSequence
 "appendTo"(arg0: $StringBuilder$Type, arg1: $CharMapper$Type, arg2: integer, arg3: integer): $BasedSequence
 "firstChar"(): character
 "isNull"(): boolean
 "padding"(arg0: integer, arg1: character): $BasedSequence
 "padding"(arg0: integer): $BasedSequence
 "emptyArray"(): ($BasedSequence)[]
 "padStart"(arg0: integer): $BasedSequence
 "padStart"(arg0: integer, arg1: character): $BasedSequence
 "startsWithIgnoreCase"(arg0: charseq): boolean
 "trimmed"(): $Pair<($BasedSequence), ($BasedSequence)>
 "trimmed"(arg0: $CharPredicate$Type): $Pair<($BasedSequence), ($BasedSequence)>
 "trimmed"(arg0: integer): $Pair<($BasedSequence), ($BasedSequence)>
 "trimmed"(arg0: integer, arg1: $CharPredicate$Type): $Pair<($BasedSequence), ($BasedSequence)>
 "lineColumnAtIndex"(arg0: integer): $Pair<(integer), (integer)>
 "endsWithIgnoreCase"(arg0: charseq): boolean
 "indexOfAny"(arg0: $CharPredicate$Type): integer
 "indexOfAny"(arg0: $CharPredicate$Type, arg1: integer): integer
 "indexOfAny"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "lastIndexOfAny"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "lastIndexOfAny"(arg0: $CharPredicate$Type, arg1: integer): integer
 "lastIndexOfAny"(arg0: $CharPredicate$Type): integer
 "removeSuffix"(arg0: charseq, arg1: boolean): $BasedSequence
 "removeSuffix"(arg0: charseq): $BasedSequence
 "matchChars"(arg0: charseq): boolean
 "matchChars"(arg0: charseq, arg1: integer): boolean
 "matchChars"(arg0: charseq, arg1: integer, arg2: boolean): boolean
 "matchChars"(arg0: charseq, arg1: boolean): boolean
 "isNotNull"(): boolean
 "endSequence"(arg0: integer, arg1: integer): $BasedSequence
 "endSequence"(arg0: integer): $BasedSequence
 "endOfLine"(arg0: integer): integer
 "appendSpace"(): $BasedSequence
 "trimStart"(): $BasedSequence
 "trimStart"(arg0: integer, arg1: $CharPredicate$Type): $BasedSequence
 "trimStart"(arg0: integer): $BasedSequence
 "trimStart"(arg0: $CharPredicate$Type): $BasedSequence
 "countTrailingSpaceTab"(arg0: integer): integer
 "countTrailingSpaceTab"(arg0: integer, arg1: integer): integer
 "countTrailingSpaceTab"(): integer
 "countTrailingNotSpaceTab"(arg0: integer, arg1: integer): integer
 "countTrailingNotSpaceTab"(): integer
 "countTrailingNotSpaceTab"(arg0: integer): integer
 "countLeadingWhitespace"(arg0: integer, arg1: integer): integer
 "countLeadingWhitespace"(): integer
 "countLeadingWhitespace"(arg0: integer): integer
 "countTrailingWhitespace"(): integer
 "countTrailingWhitespace"(arg0: integer, arg1: integer): integer
 "countTrailingWhitespace"(arg0: integer): integer
 "countOfNotWhitespace"(): integer
 "countLeadingSpaceTab"(): integer
 "countLeadingSpaceTab"(arg0: integer): integer
 "countLeadingSpaceTab"(arg0: integer, arg1: integer): integer
 "countLeadingColumns"(arg0: integer, arg1: $CharPredicate$Type): integer
 "countLeadingNotSpaceTab"(arg0: integer): integer
 "countLeadingNotSpaceTab"(): integer
 "countLeadingNotSpaceTab"(arg0: integer, arg1: integer): integer
 "countTrailingNotWhitespace"(arg0: integer): integer
 "countTrailingNotWhitespace"(arg0: integer, arg1: integer): integer
 "countTrailingNotWhitespace"(): integer
 "nullIfNotStartsWith"(...arg0: (charseq)[]): $BasedSequence
 "nullIfNotStartsWith"(arg0: boolean, ...arg1: (charseq)[]): $BasedSequence
 "countLeadingNotSpace"(arg0: integer, arg1: integer): integer
 "countLeadingNotSpace"(arg0: integer): integer
 "countLeadingNotSpace"(): integer
 "countTrailingNotSpace"(arg0: integer): integer
 "countTrailingNotSpace"(arg0: integer, arg1: integer): integer
 "countTrailingNotSpace"(): integer
 "countLeadingNotWhitespace"(arg0: integer): integer
 "countLeadingNotWhitespace"(): integer
 "countLeadingNotWhitespace"(arg0: integer, arg1: integer): integer
 "normalizeEndWithEOL"(): string
 "leadingBlankLinesRange"(arg0: integer, arg1: integer): $Range
 "leadingBlankLinesRange"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): $Range
 "leadingBlankLinesRange"(arg0: integer): $Range
 "leadingBlankLinesRange"(): $Range
 "endOfDelimitedByAny"(arg0: $CharPredicate$Type, arg1: integer): integer
 "matchedCharCountIgnoreCase"(arg0: charseq, arg1: integer): integer
 "matchedCharCountIgnoreCase"(arg0: charseq, arg1: integer, arg2: integer): integer
 "trailingBlankLinesRange"(arg0: integer): $Range
 "trailingBlankLinesRange"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): $Range
 "trailingBlankLinesRange"(): $Range
 "trailingBlankLinesRange"(arg0: integer, arg1: integer): $Range
 "suffixOnceWithSpace"(): $BasedSequence
 "nullIfNotStartsWithIgnoreCase"(...arg0: (charseq)[]): $BasedSequence
 "startsWithWhitespace"(): boolean
 "nullIfEndsWithIgnoreCase"(...arg0: (charseq)[]): $BasedSequence
 "startOfDelimitedByAny"(arg0: $CharPredicate$Type, arg1: integer): integer
 "nullIfNotEndsWithIgnoreCase"(...arg0: (charseq)[]): $BasedSequence
 "removeProperSuffixIgnoreCase"(arg0: charseq): $BasedSequence
/**
 * 
 * @deprecated
 */
 "getLineColumnAtIndex"(arg0: integer): $Pair<(integer), (integer)>
 "removePrefixIgnoreCase"(arg0: charseq): $BasedSequence
 "matchCharsReversedIgnoreCase"(arg0: charseq, arg1: integer): boolean
 "prefixOnceWithSpace"(): $BasedSequence
 "nullIfStartsWithIgnoreCase"(...arg0: (charseq)[]): $BasedSequence
 "removeProperPrefixIgnoreCase"(arg0: charseq): $BasedSequence
 "endOfDelimitedByAnyNot"(arg0: $CharPredicate$Type, arg1: integer): integer
 "blankLinesRemovedRanges"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): $List<($Range)>
 "blankLinesRemovedRanges"(): $List<($Range)>
 "blankLinesRemovedRanges"(arg0: integer): $List<($Range)>
 "blankLinesRemovedRanges"(arg0: integer, arg1: integer): $List<($Range)>
 "matchedCharCountReversed"(arg0: charseq, arg1: integer, arg2: integer): integer
 "matchedCharCountReversed"(arg0: charseq, arg1: integer, arg2: boolean): integer
 "matchedCharCountReversed"(arg0: charseq, arg1: integer): integer
 "matchedCharCountReversed"(arg0: charseq, arg1: integer, arg2: integer, arg3: boolean): integer
/**
 * 
 * @deprecated
 */
 "nullIfStartsWithNot"(...arg0: (charseq)[]): $BasedSequence
 "matchCharsIgnoreCase"(arg0: charseq): boolean
 "matchCharsIgnoreCase"(arg0: charseq, arg1: integer): boolean
 "matchedCharCountReversedIgnoreCase"(arg0: charseq, arg1: integer): integer
 "matchedCharCountReversedIgnoreCase"(arg0: charseq, arg1: integer, arg2: integer): integer
 "startOfDelimitedByAnyNot"(arg0: $CharPredicate$Type, arg1: integer): integer
 "removeSuffixIgnoreCase"(arg0: charseq): $BasedSequence
 "splitList"(arg0: charseq, arg1: integer, arg2: integer): $List<($BasedSequence)>
 "splitList"(arg0: charseq): $List<($BasedSequence)>
 "splitList"(arg0: charseq, arg1: integer, arg2: integer, arg3: $CharPredicate$Type): $List<($BasedSequence)>
 "splitList"(arg0: charseq, arg1: boolean, arg2: $CharPredicate$Type): $List<($BasedSequence)>
 "splitList"(arg0: charseq, arg1: integer, arg2: boolean, arg3: $CharPredicate$Type): $List<($BasedSequence)>
 "isNotBlank"(): boolean
 "isNotEmpty"(): boolean
 "padEnd"(arg0: integer): $BasedSequence
 "padEnd"(arg0: integer, arg1: character): $BasedSequence
 "removePrefix"(arg0: charseq, arg1: boolean): $BasedSequence
 "removePrefix"(arg0: charseq): $BasedSequence
 "trimEnd"(): $BasedSequence
 "trimEnd"(arg0: $CharPredicate$Type): $BasedSequence
 "trimEnd"(arg0: integer, arg1: $CharPredicate$Type): $BasedSequence
 "trimEnd"(arg0: integer): $BasedSequence
 "startOfLine"(arg0: integer): integer
 "sequenceOf"(arg0: charseq, arg1: integer, arg2: integer): $BasedSequence
 "sequenceOf"(arg0: charseq): $BasedSequence
 "sequenceOf"(arg0: charseq, arg1: integer): $BasedSequence
 "isIn"(arg0: $Collection$Type<(any)>): boolean
 "isIn"(arg0: (string)[]): boolean
 "normalizeEOL"(): string
 "midSequence"(arg0: integer): $BasedSequence
 "midSequence"(arg0: integer, arg1: integer): $BasedSequence
 "countLeading"(arg0: $CharPredicate$Type, arg1: integer): integer
 "countLeading"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "countLeading"(arg0: $CharPredicate$Type): integer
/**
 * 
 * @deprecated
 */
 "countLeading"(arg0: character): integer
/**
 * 
 * @deprecated
 */
 "countLeading"(): integer
 "midCharAt"(arg0: integer): character
 "subSequenceBefore"(arg0: $Range$Type): $BasedSequence
 "safeSubSequence"(arg0: integer): $BasedSequence
 "safeSubSequence"(arg0: integer, arg1: integer): $BasedSequence
 "indexOfNot"(arg0: character, arg1: integer): integer
 "indexOfNot"(arg0: character): integer
 "indexOfNot"(arg0: character, arg1: integer, arg2: integer): integer
 "countLeadingNot"(arg0: $CharPredicate$Type, arg1: integer): integer
 "countLeadingNot"(arg0: $CharPredicate$Type): integer
 "countLeadingNot"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "nullSequence"(): $BasedSequence
 "endCharAt"(arg0: integer): character
 "subSequenceAfter"(arg0: $Range$Type): $BasedSequence
 "lastIndexOfNot"(arg0: character, arg1: integer, arg2: integer): integer
 "lastIndexOfNot"(arg0: character, arg1: integer): integer
 "lastIndexOfNot"(arg0: character): integer
 "lastIndexOfAnyNot"(arg0: $CharPredicate$Type, arg1: integer): integer
 "lastIndexOfAnyNot"(arg0: $CharPredicate$Type): integer
 "lastIndexOfAnyNot"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "indexOfAnyNot"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "indexOfAnyNot"(arg0: $CharPredicate$Type, arg1: integer): integer
 "indexOfAnyNot"(arg0: $CharPredicate$Type): integer
 "safeCharAt"(arg0: integer): character
 "countOfWhitespace"(): integer
 "countOfAnyNot"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "countOfAnyNot"(arg0: $CharPredicate$Type): integer
 "countOfAnyNot"(arg0: $CharPredicate$Type, arg1: integer): integer
 "countLeadingSpace"(arg0: integer): integer
 "countLeadingSpace"(): integer
 "countLeadingSpace"(arg0: integer, arg1: integer): integer
 "countTrailingNot"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "countTrailingNot"(arg0: $CharPredicate$Type, arg1: integer): integer
 "countTrailingNot"(arg0: $CharPredicate$Type): integer
 "countTrailing"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "countTrailing"(arg0: $CharPredicate$Type, arg1: integer): integer
 "countTrailing"(arg0: $CharPredicate$Type): integer
/**
 * 
 * @deprecated
 */
 "countTrailing"(): integer
 "countOfSpaceTab"(): integer
 "countOfAny"(arg0: $CharPredicate$Type, arg1: integer): integer
 "countOfAny"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "countOfAny"(arg0: $CharPredicate$Type): integer
 "countTrailingSpace"(): integer
 "countTrailingSpace"(arg0: integer): integer
 "countTrailingSpace"(arg0: integer, arg1: integer): integer
 "trimStartRange"(arg0: $CharPredicate$Type): $Range
 "trimStartRange"(): $Range
 "trimStartRange"(arg0: integer): $Range
 "trimStartRange"(arg0: integer, arg1: $CharPredicate$Type): $Range
 "trimEndRange"(arg0: integer, arg1: $CharPredicate$Type): $Range
 "trimEndRange"(arg0: integer): $Range
 "trimEndRange"(arg0: $CharPredicate$Type): $Range
 "trimEndRange"(): $Range
/**
 * 
 * @deprecated
 */
 "countOf"(arg0: character): integer
 "trimRange"(arg0: integer): $Range
 "trimRange"(arg0: $CharPredicate$Type): $Range
 "trimRange"(): $Range
 "trimRange"(arg0: integer, arg1: $CharPredicate$Type): $Range
 "countOfNotSpaceTab"(): integer
 "nullIfNot"(arg0: $BiPredicate$Type<(any), (any)>, ...arg1: (charseq)[]): $BasedSequence
 "nullIfNot"(...arg0: (charseq)[]): $BasedSequence
 "nullIfNot"(arg0: $Predicate$Type<(any)>, ...arg1: (charseq)[]): $BasedSequence
 "nullIfEndsWith"(...arg0: (charseq)[]): $BasedSequence
 "nullIfEndsWith"(arg0: boolean, ...arg1: (charseq)[]): $BasedSequence
 "ifNull"(arg0: $BasedSequence$Type): $BasedSequence
 "ifNullEmptyAfter"(arg0: $BasedSequence$Type): $BasedSequence
 "nullIfNotEndsWith"(arg0: boolean, ...arg1: (charseq)[]): $BasedSequence
 "nullIfNotEndsWith"(...arg0: (charseq)[]): $BasedSequence
 "nullIfStartsWith"(arg0: boolean, ...arg1: (charseq)[]): $BasedSequence
 "nullIfStartsWith"(...arg0: (charseq)[]): $BasedSequence
 "trimmedStart"(arg0: integer): $BasedSequence
 "trimmedStart"(arg0: $CharPredicate$Type): $BasedSequence
 "trimmedStart"(): $BasedSequence
 "trimmedStart"(arg0: integer, arg1: $CharPredicate$Type): $BasedSequence
 "ifNullEmptyBefore"(arg0: $BasedSequence$Type): $BasedSequence
 "nullIf"(arg0: boolean): $BasedSequence
 "nullIf"(...arg0: (charseq)[]): $BasedSequence
 "nullIf"(arg0: $BiPredicate$Type<(any), (any)>, ...arg1: (charseq)[]): $BasedSequence
 "nullIf"(arg0: $Predicate$Type<(any)>, ...arg1: (charseq)[]): $BasedSequence
 "nullIfEmpty"(): $BasedSequence
 "nullIfBlank"(): $BasedSequence
 "trimmedEnd"(arg0: integer, arg1: $CharPredicate$Type): $BasedSequence
 "trimmedEnd"(): $BasedSequence
 "trimmedEnd"(arg0: integer): $BasedSequence
 "trimmedEnd"(arg0: $CharPredicate$Type): $BasedSequence
 "startOfDelimitedBy"(arg0: charseq, arg1: integer): integer
 "eolStartRange"(arg0: integer): $Range
/**
 * 
 * @deprecated
 */
 "nullIfEndsWithNot"(...arg0: (charseq)[]): $BasedSequence
 "trimmedEOL"(): $BasedSequence
 "endOfDelimitedBy"(arg0: charseq, arg1: integer): integer
 "startOfLineAnyEOL"(arg0: integer): integer
 "lineRangeAt"(arg0: integer): $Range
 "eolEndLength"(): integer
 "eolEndLength"(arg0: integer): integer
 "eolEndRange"(arg0: integer): $Range
 "lineRangeAtAnyEOL"(arg0: integer): $Range
 "lineAt"(arg0: integer): $BasedSequence
 "lineAtAnyEOL"(arg0: integer): $BasedSequence
 "trimTailBlankLines"(): $BasedSequence
 "trimEOL"(): $BasedSequence
 "trimToStartOfLine"(arg0: $CharPredicate$Type, arg1: boolean, arg2: integer): $BasedSequence
 "trimToStartOfLine"(arg0: boolean, arg1: integer): $BasedSequence
 "trimToStartOfLine"(): $BasedSequence
 "trimToStartOfLine"(arg0: boolean): $BasedSequence
 "trimToStartOfLine"(arg0: integer): $BasedSequence
 "trimToEndOfLine"(arg0: boolean, arg1: integer): $BasedSequence
 "trimToEndOfLine"(arg0: $CharPredicate$Type, arg1: boolean, arg2: integer): $BasedSequence
 "trimToEndOfLine"(): $BasedSequence
 "trimToEndOfLine"(arg0: integer): $BasedSequence
 "trimToEndOfLine"(arg0: boolean): $BasedSequence
 "matchedCharCount"(arg0: charseq, arg1: integer, arg2: integer, arg3: boolean): integer
 "matchedCharCount"(arg0: charseq, arg1: integer, arg2: integer): integer
 "matchedCharCount"(arg0: charseq, arg1: integer): integer
 "matchedCharCount"(arg0: charseq, arg1: integer, arg2: integer, arg3: boolean, arg4: boolean): integer
 "matchedCharCount"(arg0: charseq, arg1: integer, arg2: boolean): integer
 "matchCharsReversed"(arg0: charseq, arg1: integer, arg2: boolean): boolean
 "matchCharsReversed"(arg0: charseq, arg1: integer): boolean
 "matchesIgnoreCase"(arg0: charseq): boolean
 "trimLeadBlankLines"(): $BasedSequence
 "startsWithSpaceTab"(): boolean
 "endsWithWhitespace"(): boolean
 "removeProperSuffix"(arg0: charseq, arg1: boolean): $BasedSequence
 "removeProperSuffix"(arg0: charseq): $BasedSequence
 "startsWithSpace"(): boolean
 "endsWithSpace"(): boolean
 "endsWithSpaceTab"(): boolean
 "startsWithEOL"(): boolean
 "endsWithAnyEOL"(): boolean
 "startsWithAnyEOL"(): boolean
 "endsWithEOL"(): boolean
 "toSpc"(): $BasedSequence
 "toNbSp"(): $BasedSequence
 "removeProperPrefix"(arg0: charseq): $BasedSequence
 "removeProperPrefix"(arg0: charseq, arg1: boolean): $BasedSequence
 "toMapped"(arg0: $CharMapper$Type): $BasedSequence
 "prefixWith"(arg0: charseq): $BasedSequence
 "suffixWith"(arg0: charseq): $BasedSequence
 "splitEOL"(): ($BasedSequence)[]
 "splitEOL"(arg0: boolean): ($BasedSequence)[]
 "appendEOL"(): $BasedSequence
 "suffixWithEOL"(): $BasedSequence
 "splitListEOL"(arg0: boolean, arg1: $CharPredicate$Type): $List<($BasedSequence)>
 "splitListEOL"(arg0: boolean): $List<($BasedSequence)>
 "splitListEOL"(): $List<($BasedSequence)>
 "indexOfAll"(arg0: charseq): (integer)[]
 "prefixWithEOL"(): $BasedSequence
 "prefixOnceWithEOL"(): $BasedSequence
 "suffixOnceWithEOL"(): $BasedSequence
 "suffixWithSpace"(): $BasedSequence
 "prefixWithSpace"(): $BasedSequence
 "prefixOnceWith"(arg0: charseq): $BasedSequence
 "suffixOnceWith"(arg0: charseq): $BasedSequence
 "extractRanges"(...arg0: ($Range$Type)[]): $BasedSequence
 "extractRanges"(arg0: $Iterable$Type<($Range$Type)>): $BasedSequence
 "columnAtIndex"(arg0: integer): integer
 "appendSpaces"(arg0: integer): $BasedSequence
 "suffixWithSpaces"(arg0: integer): $BasedSequence
/**
 * 
 * @deprecated
 */
 "getColumnAtIndex"(arg0: integer): integer
 "prefixWithSpaces"(arg0: integer): $BasedSequence
 "appendRangesTo"(arg0: $StringBuilder$Type, ...arg1: ($Range$Type)[]): $BasedSequence
 "appendRangesTo"(arg0: $StringBuilder$Type, arg1: $Iterable$Type<(any)>): $BasedSequence
 "appendRangesTo"(arg0: $StringBuilder$Type, arg1: $CharMapper$Type, ...arg2: ($Range$Type)[]): $BasedSequence
 "appendRangesTo"(arg0: $StringBuilder$Type, arg1: $CharMapper$Type, arg2: $Iterable$Type<(any)>): $BasedSequence
 "toStringOrNull"(): string
 "isCharAt"(arg0: integer, arg1: $CharPredicate$Type): boolean
/**
 * 
 * @deprecated
 */
 "eolLength"(arg0: integer): integer
 "endOfLineAnyEOL"(arg0: integer): integer
 "eolStartLength"(arg0: integer): integer
/**
 * 
 * @deprecated
 */
 "eolStartLength"(): integer
 "getOptions"(): $DataHolder
 "getOption"<T>(arg0: $DataKeyBase$Type<(T)>): T
 "allOptions"(arg0: integer): boolean
 "getOptionFlags"(): integer
 "anyOptions"(arg0: integer): boolean
 "length"(): integer
 "toString"(): string
 "charAt"(arg0: integer): character
 "codePoints"(): $IntStream
 "chars"(): $IntStream
 "compareTo"(arg0: charseq): integer
}

export namespace $BasedSequence {
const NULL: $BasedSequence
const EMPTY: $BasedSequence
const EOL: $BasedSequence
const SPACE: $BasedSequence
const EMPTY_LIST: $List<($BasedSequence)>
const EMPTY_ARRAY: ($BasedSequence)[]
const EMPTY_SEGMENTS: ($BasedSequence)[]
const LINE_SEP: $BasedSequence
function of(arg0: charseq): $BasedSequence
function ofSpaces(arg0: integer): $BasedSequence
function repeatOf(arg0: character, arg1: integer): $BasedSequence
function repeatOf(arg0: charseq, arg1: integer, arg2: integer): $BasedSequence
function repeatOf(arg0: charseq, arg1: integer): $BasedSequence
function optionsToString(arg0: integer): string
function compare(arg0: charseq, arg1: charseq): integer
function toVisibleWhitespaceString(arg0: charseq): string
function equals(arg0: charseq, arg1: any): boolean
function hashCode(arg0: charseq): integer
function indexOf(arg0: charseq, arg1: charseq, arg2: integer): integer
function indexOf(arg0: charseq, arg1: character, arg2: integer): integer
function indexOf(arg0: charseq, arg1: charseq): integer
function indexOf(arg0: charseq, arg1: character): integer
function indexOf(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function indexOf(arg0: charseq, arg1: character, arg2: integer, arg3: integer): integer
function compare(arg0: charseq, arg1: charseq, arg2: boolean): integer
function compare(arg0: charseq, arg1: charseq, arg2: boolean, arg3: $CharPredicate$Type): integer
function startsWith(arg0: charseq, arg1: charseq, arg2: boolean): boolean
function startsWith(arg0: charseq, arg1: $CharPredicate$Type): boolean
function startsWith(arg0: charseq, arg1: charseq): boolean
function lastIndexOf(arg0: charseq, arg1: charseq, arg2: integer): integer
function lastIndexOf(arg0: charseq, arg1: charseq): integer
function lastIndexOf(arg0: charseq, arg1: character, arg2: integer): integer
function lastIndexOf(arg0: charseq, arg1: character, arg2: integer, arg3: integer): integer
function lastIndexOf(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function lastIndexOf(arg0: charseq, arg1: character): integer
function isEmpty(arg0: charseq): boolean
function matches(arg0: charseq, arg1: charseq): boolean
function matches(arg0: charseq, arg1: charseq, arg2: boolean): boolean
function split<T>(arg0: T, arg1: (T)[], arg2: charseq, arg3: integer, arg4: integer, arg5: $CharPredicate$Type): (T)[]
function split<T>(arg0: T, arg1: (T)[], arg2: charseq, arg3: integer, arg4: integer): (T)[]
function split<T>(arg0: T, arg1: (T)[], arg2: charseq, arg3: integer, arg4: boolean, arg5: $CharPredicate$Type): (T)[]
function split<T>(arg0: T, arg1: (T)[], arg2: charseq, arg3: boolean, arg4: $CharPredicate$Type): (T)[]
function split<T>(arg0: T, arg1: (T)[], arg2: charseq): (T)[]
function trim<T>(arg0: T, arg1: integer): T
function trim<T>(arg0: T): T
function trim<T>(arg0: T, arg1: $CharPredicate$Type): T
function trim<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): T
function isBlank(arg0: charseq): boolean
function endsWith(arg0: charseq, arg1: charseq, arg2: boolean): boolean
function endsWith(arg0: charseq, arg1: $CharPredicate$Type): boolean
function endsWith(arg0: charseq, arg1: charseq): boolean
function subSequence<T>(arg0: T, arg1: integer): T
function subSequence<T>(arg0: T, arg1: $Range$Type): T
function lastChar(arg0: charseq): character
function firstChar(arg0: charseq): character
function expandTo(arg0: (integer)[], arg1: integer, arg2: integer): (integer)[]
function padStart(arg0: charseq, arg1: integer): string
function padStart(arg0: charseq, arg1: integer, arg2: character): string
function trimmed<T>(arg0: T): $Pair<(T), (T)>
function trimmed<T>(arg0: T, arg1: integer): $Pair<(T), (T)>
function trimmed<T>(arg0: T, arg1: $CharPredicate$Type): $Pair<(T), (T)>
function trimmed<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): $Pair<(T), (T)>
function parseIntOrDefault(arg0: string, arg1: integer): integer
function parseIntOrDefault(arg0: string, arg1: integer, arg2: integer): integer
function lineColumnAtIndex(arg0: charseq, arg1: integer): $Pair<(integer), (integer)>
function containsAny(arg0: charseq, arg1: $CharPredicate$Type): boolean
function containsAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): boolean
function indexOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function indexOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function indexOfAny(arg0: charseq, arg1: $CharPredicate$Type): integer
function lastIndexOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function lastIndexOfAny(arg0: charseq, arg1: $CharPredicate$Type): integer
function lastIndexOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function matchChars(arg0: charseq, arg1: charseq, arg2: integer): boolean
function matchChars(arg0: charseq, arg1: charseq, arg2: integer, arg3: boolean): boolean
function matchChars(arg0: charseq, arg1: charseq): boolean
function matchChars(arg0: charseq, arg1: charseq, arg2: boolean): boolean
function endOfLine(arg0: charseq, arg1: integer): integer
function validateIndex(arg0: integer, arg1: integer): void
function trimStart<T>(arg0: T, arg1: $CharPredicate$Type): T
function trimStart<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): T
function trimStart<T>(arg0: T): T
function trimStart<T>(arg0: T, arg1: integer): T
function toStringArray(...arg0: (charseq)[]): (string)[]
function countTrailingSpaceTab(arg0: charseq, arg1: integer, arg2: integer): integer
function countTrailingSpaceTab(arg0: charseq, arg1: integer): integer
function countTrailingSpaceTab(arg0: charseq): integer
function countTrailingNotSpaceTab(arg0: charseq, arg1: integer, arg2: integer): integer
function countTrailingNotSpaceTab(arg0: charseq, arg1: integer): integer
function countTrailingNotSpaceTab(arg0: charseq): integer
function countLeadingWhitespace(arg0: charseq, arg1: integer): integer
function countLeadingWhitespace(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingWhitespace(arg0: charseq): integer
function countTrailingWhitespace(arg0: charseq, arg1: integer): integer
function countTrailingWhitespace(arg0: charseq): integer
function countTrailingWhitespace(arg0: charseq, arg1: integer, arg2: integer): integer
function countOfNotWhitespace(arg0: charseq): integer
function countLeadingSpaceTab(arg0: charseq, arg1: integer): integer
function countLeadingSpaceTab(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingSpaceTab(arg0: charseq): integer
function countLeadingColumns(arg0: charseq, arg1: integer, arg2: $CharPredicate$Type): integer
function countLeadingNotSpaceTab(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingNotSpaceTab(arg0: charseq, arg1: integer): integer
function countLeadingNotSpaceTab(arg0: charseq): integer
function countTrailingNotWhitespace(arg0: charseq): integer
function countTrailingNotWhitespace(arg0: charseq, arg1: integer): integer
function countTrailingNotWhitespace(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingNotSpace(arg0: charseq, arg1: integer): integer
function countLeadingNotSpace(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingNotSpace(arg0: charseq): integer
function countTrailingNotSpace(arg0: charseq, arg1: integer, arg2: integer): integer
function countTrailingNotSpace(arg0: charseq, arg1: integer): integer
function countTrailingNotSpace(arg0: charseq): integer
function countLeadingNotWhitespace(arg0: charseq): integer
function countLeadingNotWhitespace(arg0: charseq, arg1: integer): integer
function countLeadingNotWhitespace(arg0: charseq, arg1: integer, arg2: integer): integer
function leadingBlankLinesRange(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): $Range
function leadingBlankLinesRange(arg0: charseq): $Range
function leadingBlankLinesRange(arg0: charseq, arg1: integer, arg2: integer): $Range
function leadingBlankLinesRange(arg0: charseq, arg1: integer): $Range
function endOfDelimitedByAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function getVisibleSpacesMap(): $Map<(character), (string)>
function matchedCharCountIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer): integer
function matchedCharCountIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function trailingBlankLinesRange(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): $Range
function trailingBlankLinesRange(arg0: charseq, arg1: integer, arg2: integer): $Range
function trailingBlankLinesRange(arg0: charseq, arg1: integer): $Range
function trailingBlankLinesRange(arg0: charseq): $Range
function isVisibleWhitespace(arg0: character): boolean
function columnsToNextTabStop(arg0: integer): integer
function startsWithWhitespace(arg0: charseq): boolean
function validateIndexInclusiveEnd(arg0: integer, arg1: integer): void
function parseUnsignedIntOrNull(arg0: string): integer
function parseUnsignedIntOrNull(arg0: string, arg1: integer): integer
function startOfDelimitedByAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function parseNumberPrefixOrNull(arg0: string, arg1: $Predicate$Type<(string)>): $Pair<(number), (string)>
function subSequenceBeforeAfter<T>(arg0: T, arg1: $Range$Type): $Pair<(T), (T)>
function parseUnsignedIntOrDefault(arg0: string, arg1: integer): integer
function parseUnsignedIntOrDefault(arg0: string, arg1: integer, arg2: integer): integer
function matchCharsReversedIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer): boolean
function endOfDelimitedByAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function blankLinesRemovedRanges(arg0: charseq, arg1: integer): $List<($Range)>
function blankLinesRemovedRanges(arg0: charseq): $List<($Range)>
function blankLinesRemovedRanges(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): $List<($Range)>
function blankLinesRemovedRanges(arg0: charseq, arg1: integer, arg2: integer): $List<($Range)>
function matchedCharCountReversed(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function matchedCharCountReversed(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer, arg4: boolean): integer
function matchedCharCountReversed(arg0: charseq, arg1: charseq, arg2: integer, arg3: boolean): integer
function matchedCharCountReversed(arg0: charseq, arg1: charseq, arg2: integer): integer
function matchCharsIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer): boolean
function matchCharsIgnoreCase(arg0: charseq, arg1: charseq): boolean
function matchedCharCountReversedIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function matchedCharCountReversedIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer): integer
function startOfDelimitedByAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function splitList<T>(arg0: T, arg1: charseq, arg2: integer, arg3: integer): $List<(T)>
function splitList<T>(arg0: T, arg1: charseq, arg2: boolean, arg3: $CharPredicate$Type): $List<(T)>
function splitList<T>(arg0: T, arg1: charseq, arg2: integer, arg3: boolean, arg4: $CharPredicate$Type): $List<(T)>
function splitList<T>(arg0: T, arg1: charseq, arg2: integer, arg3: integer, arg4: $CharPredicate$Type): $List<(T)>
function splitList<T>(arg0: T, arg1: charseq): $List<(T)>
function isNotBlank(arg0: charseq): boolean
function isNotEmpty(arg0: charseq): boolean
function padEnd(arg0: charseq, arg1: integer): string
function padEnd(arg0: charseq, arg1: integer, arg2: character): string
function trimEnd<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): T
function trimEnd<T>(arg0: T): T
function trimEnd<T>(arg0: T, arg1: integer): T
function trimEnd<T>(arg0: T, arg1: $CharPredicate$Type): T
function startOfLine(arg0: charseq, arg1: integer): integer
function countLeading(arg0: charseq, arg1: $CharPredicate$Type): integer
function countLeading(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countLeading(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function subSequenceBefore<T>(arg0: T, arg1: $Range$Type): T
function indexOfNot(arg0: charseq, arg1: character, arg2: integer, arg3: integer): integer
function indexOfNot(arg0: charseq, arg1: character, arg2: integer): integer
function indexOfNot(arg0: charseq, arg1: character): integer
function countLeadingNot(arg0: charseq, arg1: $CharPredicate$Type): integer
function countLeadingNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countLeadingNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function subSequenceAfter<T>(arg0: T, arg1: $Range$Type): T
function lastIndexOfNot(arg0: charseq, arg1: character, arg2: integer): integer
function lastIndexOfNot(arg0: charseq, arg1: character): integer
function lastIndexOfNot(arg0: charseq, arg1: character, arg2: integer, arg3: integer): integer
function lastIndexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function lastIndexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function lastIndexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type): integer
function indexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type): integer
function indexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function indexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function safeCharAt(arg0: charseq, arg1: integer): character
function countOfWhitespace(arg0: charseq): integer
function countOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type): integer
function countOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function countLeadingSpace(arg0: charseq, arg1: integer): integer
function countLeadingSpace(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingSpace(arg0: charseq): integer
function countTrailingNot(arg0: charseq, arg1: $CharPredicate$Type): integer
function countTrailingNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function countTrailingNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countTrailing(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countTrailing(arg0: charseq, arg1: $CharPredicate$Type): integer
function countTrailing(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function countOfSpaceTab(arg0: charseq): integer
function countOfAny(arg0: charseq, arg1: $CharPredicate$Type): integer
function countOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function countTrailingSpace(arg0: charseq, arg1: integer): integer
function countTrailingSpace(arg0: charseq, arg1: integer, arg2: integer): integer
function countTrailingSpace(arg0: charseq): integer
function trimStartRange(arg0: charseq, arg1: integer): $Range
function trimStartRange(arg0: charseq, arg1: $CharPredicate$Type): $Range
function trimStartRange(arg0: charseq, arg1: integer, arg2: $CharPredicate$Type): $Range
function trimStartRange(arg0: charseq): $Range
function trimEndRange(arg0: charseq, arg1: $CharPredicate$Type): $Range
function trimEndRange(arg0: charseq): $Range
function trimEndRange(arg0: charseq, arg1: integer): $Range
function trimEndRange(arg0: charseq, arg1: integer, arg2: $CharPredicate$Type): $Range
function trimRange(arg0: charseq): $Range
function trimRange(arg0: charseq, arg1: integer): $Range
function trimRange(arg0: charseq, arg1: $CharPredicate$Type): $Range
function trimRange(arg0: charseq, arg1: integer, arg2: $CharPredicate$Type): $Range
function countOfNotSpaceTab(arg0: charseq): integer
function trimmedStart<T>(arg0: T, arg1: $CharPredicate$Type): T
function trimmedStart<T>(arg0: T): T
function trimmedStart<T>(arg0: T, arg1: integer): T
function trimmedStart<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): T
function trimmedEnd<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): T
function trimmedEnd<T>(arg0: T): T
function trimmedEnd<T>(arg0: T, arg1: $CharPredicate$Type): T
function trimmedEnd<T>(arg0: T, arg1: integer): T
function startOfDelimitedBy(arg0: charseq, arg1: charseq, arg2: integer): integer
function eolStartRange(arg0: charseq, arg1: integer): $Range
function trimmedEOL<T>(arg0: T): T
function endOfDelimitedBy(arg0: charseq, arg1: charseq, arg2: integer): integer
function startOfLineAnyEOL(arg0: charseq, arg1: integer): integer
function lineRangeAt(arg0: charseq, arg1: integer): $Range
function eolEndLength(arg0: charseq): integer
function eolEndLength(arg0: charseq, arg1: integer): integer
function eolEndRange(arg0: charseq, arg1: integer): $Range
function lineRangeAtAnyEOL(arg0: charseq, arg1: integer): $Range
function trimTailBlankLines<T>(arg0: T): T
function trimEOL<T>(arg0: T): T
function matchedCharCount(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer, arg4: boolean): integer
function matchedCharCount(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer, arg4: boolean, arg5: boolean): integer
function matchedCharCount(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function matchedCharCount(arg0: charseq, arg1: charseq, arg2: integer): integer
function matchedCharCount(arg0: charseq, arg1: charseq, arg2: integer, arg3: boolean): integer
function matchCharsReversed(arg0: charseq, arg1: charseq, arg2: integer): boolean
function matchCharsReversed(arg0: charseq, arg1: charseq, arg2: integer, arg3: boolean): boolean
function matchesIgnoreCase(arg0: charseq, arg1: charseq): boolean
function trimLeadBlankLines<T>(arg0: T): T
function startsWithSpaceTab(arg0: charseq): boolean
function endsWithWhitespace(arg0: charseq): boolean
function startsWithSpace(arg0: charseq): boolean
function endsWithSpace(arg0: charseq): boolean
function endsWithSpaceTab(arg0: charseq): boolean
function startsWithEOL(arg0: charseq): boolean
function endsWithAnyEOL(arg0: charseq): boolean
function startsWithAnyEOL(arg0: charseq): boolean
function endsWithEOL(arg0: charseq): boolean
function splitEOL<T>(arg0: T, arg1: (T)[], arg2: boolean): (T)[]
function splitEOL<T>(arg0: T, arg1: (T)[]): (T)[]
function splitListEOL<T>(arg0: T, arg1: boolean, arg2: $CharPredicate$Type): $List<(T)>
function splitListEOL<T>(arg0: T, arg1: boolean): $List<(T)>
function splitListEOL<T>(arg0: T): $List<(T)>
function indexOfAll(arg0: charseq, arg1: charseq): (integer)[]
function columnAtIndex(arg0: charseq, arg1: integer): integer
function compareReversed(arg0: charseq, arg1: charseq): integer
function containsAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): boolean
function containsAnyNot(arg0: charseq, arg1: $CharPredicate$Type): boolean
function containsAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): boolean
function truncateTo(arg0: (integer)[], arg1: integer): (integer)[]
function validateStartEnd(arg0: integer, arg1: integer, arg2: integer): void
function parseNumberOrNull(arg0: string): number
function parseLongOrNull(arg0: string, arg1: integer): long
function parseLongOrNull(arg0: string): long
function parseIntOrNull(arg0: string): integer
function parseIntOrNull(arg0: string, arg1: integer): integer
function containedBy(arg0: $Collection$Type<(any)>, arg1: charseq): boolean
function containedBy<T>(arg0: (T)[], arg1: charseq): boolean
function endOfLineAnyEOL(arg0: charseq, arg1: integer): integer
function eolStartLength(arg0: charseq, arg1: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BasedSequence$Type = ($BasedSequence);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BasedSequence_ = $BasedSequence$Type;
}}
declare module "packages/com/vladsch/flexmark/parser/$InlineParserExtensionFactory" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Dependent, $Dependent$Type} from "packages/com/vladsch/flexmark/util/dependency/$Dependent"
import {$LightInlineParser, $LightInlineParser$Type} from "packages/com/vladsch/flexmark/parser/$LightInlineParser"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$InlineParserExtension, $InlineParserExtension$Type} from "packages/com/vladsch/flexmark/parser/$InlineParserExtension"

export interface $InlineParserExtensionFactory extends $Function<($LightInlineParser), ($InlineParserExtension)>, $Dependent {

 "apply"(arg0: $LightInlineParser$Type): $InlineParserExtension
 "getCharacters"(): charseq
 "compose"<V>(arg0: $Function$Type<(any), (any)>): $Function<(V), ($InlineParserExtension)>
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $Function<($LightInlineParser), (V)>
 "getAfterDependents"(): $Set<($Class<(any)>)>
 "affectsGlobalScope"(): boolean
 "getBeforeDependents"(): $Set<($Class<(any)>)>
}

export namespace $InlineParserExtensionFactory {
function identity<T>(): $Function<($LightInlineParser), ($LightInlineParser)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InlineParserExtensionFactory$Type = ($InlineParserExtensionFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InlineParserExtensionFactory_ = $InlineParserExtensionFactory$Type;
}}
declare module "packages/com/vladsch/flexmark/util/ast/$VisitHandler" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Visitor, $Visitor$Type} from "packages/com/vladsch/flexmark/util/ast/$Visitor"
import {$AstHandler, $AstHandler$Type} from "packages/com/vladsch/flexmark/util/visitor/$AstHandler"
import {$Node, $Node$Type} from "packages/com/vladsch/flexmark/util/ast/$Node"

export class $VisitHandler<N extends $Node> extends $AstHandler<(N), ($Visitor<(N)>)> implements $Visitor<($Node)> {

constructor(arg0: $Class$Type<(N)>, arg1: $Visitor$Type<(N)>)

public "visit"(arg0: $Node$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VisitHandler$Type<N> = ($VisitHandler<(N)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VisitHandler_<N> = $VisitHandler$Type<(N)>;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/builder/$IBasedSegmentBuilder" {
import {$Range, $Range$Type} from "packages/com/vladsch/flexmark/util/sequence/$Range"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ISegmentBuilder, $ISegmentBuilder$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$ISegmentBuilder"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"
import {$Seg, $Seg$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$Seg"

export interface $IBasedSegmentBuilder<S extends $IBasedSegmentBuilder<(S)>> extends $ISegmentBuilder<(S)> {

 "getBaseSequence"(): $BasedSequence
 "toStringWithRanges"(): string
 "toStringChars"(): string
 "toStringWithRangesVisibleWhitespace"(): string
 "length"(): integer
 "toString"(arg0: charseq): string
 "append"(arg0: integer, arg1: integer): S
 "append"(arg0: charseq): S
 "append"(arg0: $Range$Type): S
 "isEmpty"(): boolean
 "size"(): integer
 "iterator"(): $Iterator<(any)>
 "getText"(): charseq
 "getTextLength"(): integer
 "getSegments"(): $Iterable<($Seg)>
 "getOptions"(): integer
 "getEndOffset"(): integer
 "getStartOffset"(): integer
 "isTrackTextFirst256"(): boolean
 "getTextFirst256Length"(): integer
 "getTextFirst256Segments"(): integer
 "getTextSpaceSegments"(): integer
 "isBaseSubSequenceRange"(): boolean
 "getBaseSubSequenceRange"(): $Range
 "haveOffsets"(): boolean
 "noAnchorsSize"(): integer
 "isIncludeAnchors"(): boolean
 "getTextSegments"(): integer
 "getTextSpaceLength"(): integer
 "getSpan"(): integer
 "toStringWithRanges"(arg0: charseq): string
 "appendAnchor"(arg0: integer): S
 "toStringWithRangesVisibleWhitespace"(arg0: charseq): string
 "spliterator"(): $Spliterator<(any)>
 "forEach"(arg0: $Consumer$Type<(any)>): void
}

export namespace $IBasedSegmentBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IBasedSegmentBuilder$Type<S> = ($IBasedSegmentBuilder<(S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IBasedSegmentBuilder_<S> = $IBasedSegmentBuilder$Type<(S)>;
}}
declare module "packages/com/vladsch/flexmark/parser/core/delimiter/$Bracket" {
import {$Text, $Text$Type} from "packages/com/vladsch/flexmark/ast/$Text"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"
import {$Delimiter, $Delimiter$Type} from "packages/com/vladsch/flexmark/parser/core/delimiter/$Delimiter"

export class $Bracket {


public "getNode"(): $Text
public static "link"(arg0: $BasedSequence$Type, arg1: $Text$Type, arg2: integer, arg3: $Bracket$Type, arg4: $Delimiter$Type): $Bracket
public "getEndIndex"(): integer
public static "image"(arg0: $BasedSequence$Type, arg1: $Text$Type, arg2: integer, arg3: $Bracket$Type, arg4: $Delimiter$Type): $Bracket
public "isAllowed"(): boolean
public "getPrevious"(): $Bracket
public "getStartIndex"(): integer
public "getPreviousDelimiter"(): $Delimiter
public "setAllowed"(arg0: boolean): void
public "isBracketAfter"(): boolean
public "isImage"(): boolean
public "setBracketAfter"(arg0: boolean): void
public "isStraddling"(arg0: $BasedSequence$Type): boolean
get "node"(): $Text
get "endIndex"(): integer
get "allowed"(): boolean
get "previous"(): $Bracket
get "startIndex"(): integer
get "previousDelimiter"(): $Delimiter
set "allowed"(value: boolean)
get "bracketAfter"(): boolean
set "bracketAfter"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Bracket$Type = ($Bracket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Bracket_ = $Bracket$Type;
}}
declare module "packages/com/vladsch/flexmark/util/collection/iteration/$ReversiblePeekingIterable" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$ReversibleIterable, $ReversibleIterable$Type} from "packages/com/vladsch/flexmark/util/collection/iteration/$ReversibleIterable"

export interface $ReversiblePeekingIterable<E> extends $ReversibleIterable<(E)> {

 "isReversed"(): boolean
 "spliterator"(): $Spliterator<(E)>
 "forEach"(arg0: $Consumer$Type<(any)>): void

(): boolean
}

export namespace $ReversiblePeekingIterable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReversiblePeekingIterable$Type<E> = ($ReversiblePeekingIterable<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReversiblePeekingIterable_<E> = $ReversiblePeekingIterable$Type<(E)>;
}}
declare module "packages/com/vladsch/flexmark/util/collection/iteration/$ReversibleIterator" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export interface $ReversibleIterator<E> extends $Iterator<(E)> {

 "isReversed"(): boolean
 "remove"(): void
 "forEachRemaining"(arg0: $Consumer$Type<(any)>): void
 "hasNext"(): boolean
 "next"(): E
}

export namespace $ReversibleIterator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReversibleIterator$Type<E> = ($ReversibleIterator<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReversibleIterator_<E> = $ReversibleIterator$Type<(E)>;
}}
declare module "packages/com/vladsch/flexmark/util/data/$MutableDataSetter" {
import {$MutableDataHolder, $MutableDataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$MutableDataHolder"

export interface $MutableDataSetter {

 "setIn"(arg0: $MutableDataHolder$Type): $MutableDataHolder

(arg0: $MutableDataHolder$Type): $MutableDataHolder
}

export namespace $MutableDataSetter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MutableDataSetter$Type = ($MutableDataSetter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MutableDataSetter_ = $MutableDataSetter$Type;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/mappers/$CharMapper" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $CharMapper {

 "map"(arg0: character): character
 "compose"(arg0: $CharMapper$Type): $CharMapper
 "andThen"(arg0: $CharMapper$Type): $CharMapper

(arg0: character): character
}

export namespace $CharMapper {
const IDENTITY: $CharMapper
function identity(): $CharMapper
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CharMapper$Type = ($CharMapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CharMapper_ = $CharMapper$Type;
}}
declare module "packages/com/vladsch/flexmark/parser/internal/$LinkRefProcessorData" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$LinkRefProcessorFactory, $LinkRefProcessorFactory$Type} from "packages/com/vladsch/flexmark/parser/$LinkRefProcessorFactory"

export class $LinkRefProcessorData {
readonly "processors": $List<($LinkRefProcessorFactory)>
readonly "maxNesting": integer
readonly "nestingIndex": (integer)[]

constructor(arg0: $List$Type<($LinkRefProcessorFactory$Type)>, arg1: integer, arg2: (integer)[])

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LinkRefProcessorData$Type = ($LinkRefProcessorData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LinkRefProcessorData_ = $LinkRefProcessorData$Type;
}}
declare module "packages/com/vladsch/flexmark/parser/block/$BlockParserFactory" {
import {$BlockStart, $BlockStart$Type} from "packages/com/vladsch/flexmark/parser/block/$BlockStart"
import {$ParserState, $ParserState$Type} from "packages/com/vladsch/flexmark/parser/block/$ParserState"
import {$MatchedBlockParser, $MatchedBlockParser$Type} from "packages/com/vladsch/flexmark/parser/block/$MatchedBlockParser"

export interface $BlockParserFactory {

 "tryStart"(arg0: $ParserState$Type, arg1: $MatchedBlockParser$Type): $BlockStart

(arg0: $ParserState$Type, arg1: $MatchedBlockParser$Type): $BlockStart
}

export namespace $BlockParserFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockParserFactory$Type = ($BlockParserFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockParserFactory_ = $BlockParserFactory$Type;
}}
declare module "packages/com/vladsch/flexmark/util/ast/$NodeVisitHandler" {
import {$Visitor, $Visitor$Type} from "packages/com/vladsch/flexmark/util/ast/$Visitor"
import {$Node, $Node$Type} from "packages/com/vladsch/flexmark/util/ast/$Node"

export interface $NodeVisitHandler extends $Visitor<($Node)> {

 "visitChildren"(arg0: $Node$Type): void
 "visitNodeOnly"(arg0: $Node$Type): void
 "visit"(arg0: $Node$Type): void
}

export namespace $NodeVisitHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NodeVisitHandler$Type = ($NodeVisitHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NodeVisitHandler_ = $NodeVisitHandler$Type;
}}
declare module "packages/com/vladsch/flexmark/util/misc/$Pair" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"
import {$Paired, $Paired$Type} from "packages/com/vladsch/flexmark/util/misc/$Paired"

export class $Pair<K, V> implements $Paired<(K), (V)> {

constructor(arg0: K, arg1: V)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getValue"(): V
public static "of"<K1, V1>(arg0: K1, arg1: V1): $Pair<(K1), (V1)>
public "getKey"(): K
public "setValue"(arg0: V): V
public "getFirst"(): K
public "getSecond"(): V
public "component2"(): V
public "component1"(): K
public static "copyOf"<K, V>(arg0: $Map$Entry$Type<(any), (any)>): $Map$Entry<(K), (V)>
public static "comparingByKey"<K, V>(arg0: $Comparator$Type<(any)>): $Comparator<($Map$Entry<(K), (V)>)>
public static "comparingByKey"<K extends $Comparable<(any)>, V>(): $Comparator<($Map$Entry<(K), (V)>)>
public static "comparingByValue"<K, V extends $Comparable<(any)>>(): $Comparator<($Map$Entry<(K), (V)>)>
public static "comparingByValue"<K, V>(arg0: $Comparator$Type<(any)>): $Comparator<($Map$Entry<(K), (V)>)>
get "value"(): V
get "key"(): K
set "value"(value: V)
get "first"(): K
get "second"(): V
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Pair$Type<K, V> = ($Pair<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Pair_<K, V> = $Pair$Type<(K), (V)>;
}}
declare module "packages/com/vladsch/flexmark/parser/$LinkRefProcessorFactory" {
import {$LinkRefProcessor, $LinkRefProcessor$Type} from "packages/com/vladsch/flexmark/parser/$LinkRefProcessor"
import {$Document, $Document$Type} from "packages/com/vladsch/flexmark/util/ast/$Document"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$DataHolder, $DataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$DataHolder"

export interface $LinkRefProcessorFactory extends $Function<($Document), ($LinkRefProcessor)> {

 "apply"(arg0: $Document$Type): $LinkRefProcessor
 "getWantExclamationPrefix"(arg0: $DataHolder$Type): boolean
 "getBracketNestingLevel"(arg0: $DataHolder$Type): integer
 "compose"<V>(arg0: $Function$Type<(any), (any)>): $Function<(V), ($LinkRefProcessor)>
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $Function<($Document), (V)>
}

export namespace $LinkRefProcessorFactory {
function identity<T>(): $Function<($Document), ($Document)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LinkRefProcessorFactory$Type = ($LinkRefProcessorFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LinkRefProcessorFactory_ = $LinkRefProcessorFactory$Type;
}}
declare module "packages/com/vladsch/flexmark/util/ast/$Document" {
import {$AstNode, $AstNode$Type} from "packages/com/vladsch/flexmark/util/visitor/$AstNode"
import {$Block, $Block$Type} from "packages/com/vladsch/flexmark/util/ast/$Block"
import {$DataHolder, $DataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$DataHolder"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$MutableDataSet, $MutableDataSet$Type} from "packages/com/vladsch/flexmark/util/data/$MutableDataSet"
import {$DataSet, $DataSet$Type} from "packages/com/vladsch/flexmark/util/data/$DataSet"
import {$MutableDataSetter, $MutableDataSetter$Type} from "packages/com/vladsch/flexmark/util/data/$MutableDataSetter"
import {$NullableDataKey, $NullableDataKey$Type} from "packages/com/vladsch/flexmark/util/data/$NullableDataKey"
import {$DataKey, $DataKey$Type} from "packages/com/vladsch/flexmark/util/data/$DataKey"
import {$DataKeyBase, $DataKeyBase$Type} from "packages/com/vladsch/flexmark/util/data/$DataKeyBase"
import {$DataValueFactory, $DataValueFactory$Type} from "packages/com/vladsch/flexmark/util/data/$DataValueFactory"
import {$Node, $Node$Type} from "packages/com/vladsch/flexmark/util/ast/$Node"
import {$MutableDataHolder, $MutableDataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$MutableDataHolder"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $Document extends $Block implements $MutableDataHolder {
static readonly "NULL": $Document
static readonly "EMPTY_SEGMENTS": ($BasedSequence)[]
static readonly "SPLICE": string
static readonly "AST_ADAPTER": $AstNode<($Node)>

constructor(arg0: $DataHolder$Type, arg1: $BasedSequence$Type)

public "clear"(): $MutableDataHolder
public "contains"(arg0: $DataKeyBase$Type<(any)>): boolean
public static "merge"(...arg0: ($DataHolder$Type)[]): $MutableDataSet
public "set"<T>(arg0: $NullableDataKey$Type<(T)>, arg1: T): $MutableDataHolder
public "set"<T>(arg0: $DataKey$Type<(T)>, arg1: T): $MutableDataHolder
public "setIn"(arg0: $MutableDataHolder$Type): $MutableDataHolder
public "getLineNumber"(arg0: integer): integer
public "setAll"(arg0: $DataHolder$Type): $MutableDataSet
public "getAll"(): $Map<(any), (any)>
public "getKeys"(): $Collection<(any)>
public "getSegments"(): ($BasedSequence)[]
public "aggregate"(): $DataHolder
public static "aggregate"(arg0: $DataHolder$Type, arg1: $DataHolder$Type): $DataHolder
public "toImmutable"(): $DataSet
public "getLineCount"(): integer
public "toMutable"(): $MutableDataSet
public "setFrom"(arg0: $MutableDataSetter$Type): $MutableDataSet
public static "aggregateActions"(arg0: $DataHolder$Type, arg1: $DataHolder$Type): $DataHolder
public "toDataSet"(): $MutableDataSet
public "getOrCompute"(arg0: $DataKeyBase$Type<(any)>, arg1: $DataValueFactory$Type<(any)>): any
/**
 * 
 * @deprecated
 */
public "get"<T>(arg0: $DataKey$Type<(T)>): T
set "in"(value: $MutableDataHolder$Type)
set "all"(value: $DataHolder$Type)
get "all"(): $Map<(any), (any)>
get "keys"(): $Collection<(any)>
get "segments"(): ($BasedSequence)[]
get "lineCount"(): integer
set "from"(value: $MutableDataSetter$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Document$Type = ($Document);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Document_ = $Document$Type;
}}
declare module "packages/com/vladsch/flexmark/parser/$PostProcessor" {
import {$Document, $Document$Type} from "packages/com/vladsch/flexmark/util/ast/$Document"
import {$NodeTracker, $NodeTracker$Type} from "packages/com/vladsch/flexmark/util/ast/$NodeTracker"
import {$Node, $Node$Type} from "packages/com/vladsch/flexmark/util/ast/$Node"

export interface $PostProcessor {

 "process"(arg0: $NodeTracker$Type, arg1: $Node$Type): void
 "processDocument"(arg0: $Document$Type): $Document
}

export namespace $PostProcessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PostProcessor$Type = ($PostProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PostProcessor_ = $PostProcessor$Type;
}}
declare module "packages/com/vladsch/flexmark/parser/block/$ParagraphPreProcessorFactory" {
import {$ParagraphPreProcessor, $ParagraphPreProcessor$Type} from "packages/com/vladsch/flexmark/parser/block/$ParagraphPreProcessor"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Dependent, $Dependent$Type} from "packages/com/vladsch/flexmark/util/dependency/$Dependent"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ParserState, $ParserState$Type} from "packages/com/vladsch/flexmark/parser/block/$ParserState"

export interface $ParagraphPreProcessorFactory extends $Function<($ParserState), ($ParagraphPreProcessor)>, $Dependent {

 "apply"(arg0: $ParserState$Type): $ParagraphPreProcessor
 "compose"<V>(arg0: $Function$Type<(any), (any)>): $Function<(V), ($ParagraphPreProcessor)>
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $Function<($ParserState), (V)>
 "getAfterDependents"(): $Set<($Class<(any)>)>
 "affectsGlobalScope"(): boolean
 "getBeforeDependents"(): $Set<($Class<(any)>)>
}

export namespace $ParagraphPreProcessorFactory {
function identity<T>(): $Function<($ParserState), ($ParserState)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParagraphPreProcessorFactory$Type = ($ParagraphPreProcessorFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParagraphPreProcessorFactory_ = $ParagraphPreProcessorFactory$Type;
}}
declare module "packages/com/vladsch/flexmark/parser/block/$BlockPreProcessorFactory" {
import {$BlockPreProcessor, $BlockPreProcessor$Type} from "packages/com/vladsch/flexmark/parser/block/$BlockPreProcessor"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Dependent, $Dependent$Type} from "packages/com/vladsch/flexmark/util/dependency/$Dependent"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ParserState, $ParserState$Type} from "packages/com/vladsch/flexmark/parser/block/$ParserState"

export interface $BlockPreProcessorFactory extends $Function<($ParserState), ($BlockPreProcessor)>, $Dependent {

 "apply"(arg0: $ParserState$Type): $BlockPreProcessor
 "getBlockTypes"(): $Set<($Class<(any)>)>
 "compose"<V>(arg0: $Function$Type<(any), (any)>): $Function<(V), ($BlockPreProcessor)>
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $Function<($ParserState), (V)>
 "getAfterDependents"(): $Set<($Class<(any)>)>
 "affectsGlobalScope"(): boolean
 "getBeforeDependents"(): $Set<($Class<(any)>)>
}

export namespace $BlockPreProcessorFactory {
function identity<T>(): $Function<($ParserState), ($ParserState)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockPreProcessorFactory$Type = ($BlockPreProcessorFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockPreProcessorFactory_ = $BlockPreProcessorFactory$Type;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/builder/$ISegmentBuilder" {
import {$Range, $Range$Type} from "packages/com/vladsch/flexmark/util/sequence/$Range"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ISegmentBuilder$Options, $ISegmentBuilder$Options$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$ISegmentBuilder$Options"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Seg, $Seg$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$Seg"

export interface $ISegmentBuilder<S extends $ISegmentBuilder<(S)>> extends $Iterable<(any)> {

 "length"(): integer
 "toString"(arg0: charseq): string
 "append"(arg0: integer, arg1: integer): S
 "append"(arg0: charseq): S
 "append"(arg0: $Range$Type): S
 "isEmpty"(): boolean
 "size"(): integer
 "iterator"(): $Iterator<(any)>
 "getText"(): charseq
 "getTextLength"(): integer
 "getSegments"(): $Iterable<($Seg)>
 "getOptions"(): integer
 "getEndOffset"(): integer
 "getStartOffset"(): integer
 "isTrackTextFirst256"(): boolean
 "getTextFirst256Length"(): integer
 "getTextFirst256Segments"(): integer
 "getTextSpaceSegments"(): integer
 "isBaseSubSequenceRange"(): boolean
 "getBaseSubSequenceRange"(): $Range
 "haveOffsets"(): boolean
 "noAnchorsSize"(): integer
 "isIncludeAnchors"(): boolean
 "getTextSegments"(): integer
 "getTextSpaceLength"(): integer
 "getSpan"(): integer
 "toStringWithRanges"(arg0: charseq): string
 "appendAnchor"(arg0: integer): S
 "toStringWithRangesVisibleWhitespace"(arg0: charseq): string
 "spliterator"(): $Spliterator<(any)>
 "forEach"(arg0: $Consumer$Type<(any)>): void
}

export namespace $ISegmentBuilder {
const O_INCLUDE_ANCHORS: $ISegmentBuilder$Options
const O_TRACK_FIRST256: $ISegmentBuilder$Options
const F_INCLUDE_ANCHORS: integer
const F_TRACK_FIRST256: integer
const F_DEFAULT: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ISegmentBuilder$Type<S> = ($ISegmentBuilder<(S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ISegmentBuilder_<S> = $ISegmentBuilder$Type<(S)>;
}}
declare module "packages/com/vladsch/flexmark/parser/$InlineParserExtension" {
import {$LightInlineParser, $LightInlineParser$Type} from "packages/com/vladsch/flexmark/parser/$LightInlineParser"
import {$InlineParser, $InlineParser$Type} from "packages/com/vladsch/flexmark/parser/$InlineParser"

export interface $InlineParserExtension {

 "parse"(arg0: $LightInlineParser$Type): boolean
 "finalizeDocument"(arg0: $InlineParser$Type): void
 "finalizeBlock"(arg0: $InlineParser$Type): void
}

export namespace $InlineParserExtension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InlineParserExtension$Type = ($InlineParserExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InlineParserExtension_ = $InlineParserExtension$Type;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/builder/tree/$Segment$SegType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Segment$SegType extends $Enum<($Segment$SegType)> {
static readonly "ANCHOR": $Segment$SegType
static readonly "BASE": $Segment$SegType
static readonly "TEXT": $Segment$SegType
static readonly "REPEATED_TEXT": $Segment$SegType
static readonly "TEXT_ASCII": $Segment$SegType
static readonly "REPEATED_ASCII": $Segment$SegType
static readonly "REPEATED_SPACE": $Segment$SegType
static readonly "REPEATED_EOL": $Segment$SegType
readonly "flags": integer


public static "values"(): ($Segment$SegType)[]
public static "valueOf"(arg0: string): $Segment$SegType
public "hasAll"(arg0: integer): boolean
public "hasChar"(): boolean
public "hasChars"(): boolean
public "hasByte"(): boolean
public "hasBytes"(): boolean
public "hasOffset"(): boolean
public "hasBoth"(): boolean
public "hasLength"(): boolean
public static "fromTypeMask"(arg0: integer): $Segment$SegType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Segment$SegType$Type = (("repeated_ascii") | ("anchor") | ("repeated_space") | ("repeated_text") | ("text") | ("repeated_eol") | ("base") | ("text_ascii")) | ($Segment$SegType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Segment$SegType_ = $Segment$SegType$Type;
}}
declare module "packages/com/vladsch/flexmark/parser/block/$ParserPhase" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ParserPhase extends $Enum<($ParserPhase)> {
static readonly "NONE": $ParserPhase
static readonly "STARTING": $ParserPhase
static readonly "PARSE_BLOCKS": $ParserPhase
static readonly "PRE_PROCESS_PARAGRAPHS": $ParserPhase
static readonly "PRE_PROCESS_BLOCKS": $ParserPhase
static readonly "PARSE_INLINES": $ParserPhase
static readonly "DONE": $ParserPhase


public static "values"(): ($ParserPhase)[]
public static "valueOf"(arg0: string): $ParserPhase
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParserPhase$Type = (("parse_inlines") | ("pre_process_paragraphs") | ("none") | ("parse_blocks") | ("pre_process_blocks") | ("starting") | ("done")) | ($ParserPhase);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParserPhase_ = $ParserPhase$Type;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/$BasedOptionsHolder$Options" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $BasedOptionsHolder$Options extends $Enum<($BasedOptionsHolder$Options)> {
static readonly "COLLECT_SEGMENTED_STATS": $BasedOptionsHolder$Options
static readonly "COLLECT_FIRST256_STATS": $BasedOptionsHolder$Options
static readonly "NO_ANCHORS": $BasedOptionsHolder$Options
static readonly "FULL_SEGMENTED_SEQUENCES": $BasedOptionsHolder$Options
static readonly "TREE_SEGMENTED_SEQUENCES": $BasedOptionsHolder$Options


public static "values"(): ($BasedOptionsHolder$Options)[]
public static "valueOf"(arg0: string): $BasedOptionsHolder$Options
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BasedOptionsHolder$Options$Type = (("collect_segmented_stats") | ("full_segmented_sequences") | ("collect_first256_stats") | ("tree_segmented_sequences") | ("no_anchors")) | ($BasedOptionsHolder$Options);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BasedOptionsHolder$Options_ = $BasedOptionsHolder$Options$Type;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/builder/tree/$SegmentTree" {
import {$BasedSegmentBuilder, $BasedSegmentBuilder$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$BasedSegmentBuilder"
import {$SegmentOffsetTree, $SegmentOffsetTree$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/tree/$SegmentOffsetTree"
import {$IBasedSegmentBuilder, $IBasedSegmentBuilder$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$IBasedSegmentBuilder"
import {$Segment, $Segment$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/tree/$Segment"
import {$SegmentTreePos, $SegmentTreePos$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/tree/$SegmentTreePos"
import {$SegmentTree$SegmentTreeData, $SegmentTree$SegmentTreeData$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/tree/$SegmentTree$SegmentTreeData"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$SegmentTreeRange, $SegmentTreeRange$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/tree/$SegmentTreeRange"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"
import {$Seg, $Seg$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$Seg"

export class $SegmentTree {
static readonly "MAX_VALUE": integer
static readonly "F_ANCHOR_FLAGS": integer


public "toString"(): string
public "toString"(arg0: $BasedSequence$Type): string
public "size"(): integer
public static "build"(arg0: $BasedSegmentBuilder$Type): $SegmentTree
public static "build"(arg0: $Iterable$Type<($Seg$Type)>, arg1: charseq): $SegmentTree
public static "byteOffset"(arg0: integer, arg1: (integer)[]): integer
public "byteOffset"(arg0: integer): integer
public "addSegments"(arg0: $IBasedSegmentBuilder$Type<(any)>, arg1: $SegmentTreeRange$Type): void
public "addSegments"(arg0: $IBasedSegmentBuilder$Type<(any)>, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer): void
public "getSegmentOffsetTree"(arg0: $BasedSequence$Type): $SegmentOffsetTree
public static "previousAnchorOffset"(arg0: integer, arg1: (integer)[]): integer
public "previousAnchorOffset"(arg0: integer): integer
public static "getCharSequence"(arg0: $Segment$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer): charseq
public "getSegment"(arg0: integer, arg1: integer, arg2: integer, arg3: $BasedSequence$Type): $Segment
public static "getSegment"(arg0: integer, arg1: (integer)[], arg2: (byte)[], arg3: $BasedSequence$Type): $Segment
public "getSegment"(arg0: integer, arg1: $BasedSequence$Type): $Segment
public "getSegmentBytes"(): (byte)[]
public "aggrLength"(arg0: integer): integer
public static "aggrLength"(arg0: integer, arg1: (integer)[]): integer
public static "byteOffsetData"(arg0: integer, arg1: (integer)[]): integer
public "byteOffsetData"(arg0: integer): integer
public "hasPreviousAnchor"(arg0: integer): boolean
public static "hasPreviousAnchor"(arg0: integer, arg1: (integer)[]): boolean
public "getTreeData"(): (integer)[]
public static "getAnchorOffset"(arg0: integer): integer
public static "getByteOffset"(arg0: integer): integer
public "findSegmentPos"(arg0: integer, arg1: integer, arg2: integer): $SegmentTreePos
public static "findSegmentPos"(arg0: integer, arg1: (integer)[], arg2: integer, arg3: integer): $SegmentTreePos
public "findSegmentPos"(arg0: integer): $SegmentTreePos
public "getTextEndOffset"(arg0: $Segment$Type, arg1: $BasedSequence$Type): integer
public "getSegmentRange"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $BasedSequence$Type, arg5: $Segment$Type): $SegmentTreeRange
public "getTextStartOffset"(arg0: $Segment$Type, arg1: $BasedSequence$Type): integer
public "findSegment"(arg0: integer, arg1: integer, arg2: integer, arg3: $BasedSequence$Type, arg4: $Segment$Type): $Segment
public "findSegment"(arg0: integer, arg1: $BasedSequence$Type, arg2: $Segment$Type): $Segment
public static "findSegment"(arg0: integer, arg1: (integer)[], arg2: integer, arg3: integer, arg4: (byte)[], arg5: $BasedSequence$Type): $Segment
public static "getPrevAnchor"(arg0: integer, arg1: (integer)[], arg2: (byte)[], arg3: $BasedSequence$Type): $Segment
public "getPrevAnchor"(arg0: integer, arg1: $BasedSequence$Type): $Segment
public static "buildTreeData"(arg0: $Iterable$Type<($Seg$Type)>, arg1: charseq, arg2: boolean): $SegmentTree$SegmentTreeData
public static "setTreeData"(arg0: integer, arg1: (integer)[], arg2: integer, arg3: integer, arg4: integer): void
get "segmentBytes"(): (byte)[]
get "treeData"(): (integer)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SegmentTree$Type = ($SegmentTree);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SegmentTree_ = $SegmentTree$Type;
}}
declare module "packages/com/vladsch/flexmark/util/ast/$DelimitedNode" {
import {$NodeVisitor, $NodeVisitor$Type} from "packages/com/vladsch/flexmark/util/ast/$NodeVisitor"
import {$ISequenceBuilder, $ISequenceBuilder$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$ISequenceBuilder"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"
import {$TextContainer, $TextContainer$Type} from "packages/com/vladsch/flexmark/util/ast/$TextContainer"

export interface $DelimitedNode extends $TextContainer {

 "getChars"(): $BasedSequence
 "getText"(): $BasedSequence
 "setText"(arg0: $BasedSequence$Type): void
 "collectText"(arg0: $ISequenceBuilder$Type<(any), ($BasedSequence$Type)>, arg1: integer, arg2: $NodeVisitor$Type): boolean
 "setOpeningMarker"(arg0: $BasedSequence$Type): void
 "getOpeningMarker"(): $BasedSequence
 "getClosingMarker"(): $BasedSequence
 "setClosingMarker"(arg0: $BasedSequence$Type): void
 "collectEndText"(arg0: $ISequenceBuilder$Type<(any), ($BasedSequence$Type)>, arg1: integer, arg2: $NodeVisitor$Type): void
}

export namespace $DelimitedNode {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DelimitedNode$Type = ($DelimitedNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DelimitedNode_ = $DelimitedNode$Type;
}}
declare module "packages/com/vladsch/flexmark/parser/block/$ParagraphPreProcessor" {
import {$Paragraph, $Paragraph$Type} from "packages/com/vladsch/flexmark/ast/$Paragraph"
import {$ParserState, $ParserState$Type} from "packages/com/vladsch/flexmark/parser/block/$ParserState"

export interface $ParagraphPreProcessor {

 "preProcessBlock"(arg0: $Paragraph$Type, arg1: $ParserState$Type): integer

(arg0: $Paragraph$Type, arg1: $ParserState$Type): integer
}

export namespace $ParagraphPreProcessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParagraphPreProcessor$Type = ($ParagraphPreProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParagraphPreProcessor_ = $ParagraphPreProcessor$Type;
}}
declare module "packages/com/vladsch/flexmark/util/ast/$NodeTracker" {
import {$Node, $Node$Type} from "packages/com/vladsch/flexmark/util/ast/$Node"

export interface $NodeTracker {

 "nodeAdded"(arg0: $Node$Type): void
 "nodeRemoved"(arg0: $Node$Type): void
 "nodeRemovedWithChildren"(arg0: $Node$Type): void
 "nodeRemovedWithDescendants"(arg0: $Node$Type): void
 "nodeAddedWithChildren"(arg0: $Node$Type): void
 "nodeAddedWithDescendants"(arg0: $Node$Type): void
}

export namespace $NodeTracker {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NodeTracker$Type = ($NodeTracker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NodeTracker_ = $NodeTracker$Type;
}}
declare module "packages/com/vladsch/flexmark/util/ast/$NodeVisitor" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$VisitHandler, $VisitHandler$Type} from "packages/com/vladsch/flexmark/util/ast/$VisitHandler"
import {$NodeVisitHandler, $NodeVisitHandler$Type} from "packages/com/vladsch/flexmark/util/ast/$NodeVisitHandler"
import {$Visitor, $Visitor$Type} from "packages/com/vladsch/flexmark/util/ast/$Visitor"
import {$Node, $Node$Type} from "packages/com/vladsch/flexmark/util/ast/$Node"
import {$AstActionHandler, $AstActionHandler$Type} from "packages/com/vladsch/flexmark/util/visitor/$AstActionHandler"

export class $NodeVisitor extends $AstActionHandler<($NodeVisitor), ($Node), ($Visitor<($Node)>), ($VisitHandler<($Node)>)> implements $NodeVisitHandler {

constructor(arg0: $Collection$Type<($VisitHandler$Type)>)
constructor(...arg0: (($VisitHandler$Type<(any)>)[])[])
constructor(...arg0: ($VisitHandler$Type<(any)>)[])
constructor()

public "visit"(arg0: $Node$Type): void
public "addHandler"(arg0: $VisitHandler$Type<(any)>): $NodeVisitor
public "visitChildren"(arg0: $Node$Type): void
public "visitNodeOnly"(arg0: $Node$Type): void
public "addHandlers"(...arg0: (($VisitHandler$Type<(any)>)[])[]): $NodeVisitor
public "addHandlers"(arg0: $Collection$Type<($VisitHandler$Type)>): $NodeVisitor
public "addHandlers"(arg0: ($VisitHandler$Type<(any)>)[]): $NodeVisitor
public "addTypedHandlers"(arg0: $Collection$Type<($VisitHandler$Type<(any)>)>): $NodeVisitor
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NodeVisitor$Type = ($NodeVisitor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NodeVisitor_ = $NodeVisitor$Type;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/$ReplacedTextMapper" {
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"
import {$ReplacedTextRegion, $ReplacedTextRegion$Type} from "packages/com/vladsch/flexmark/util/sequence/$ReplacedTextRegion"

export class $ReplacedTextMapper {

constructor(arg0: $BasedSequence$Type)

public "getParent"(): $ReplacedTextMapper
public "isModified"(): boolean
public "originalOffset"(arg0: integer): integer
public "getRegions"(): $ArrayList<($ReplacedTextRegion)>
public "startNestedReplacement"(arg0: $BasedSequence$Type): void
public "getReplacedSequence"(): $BasedSequence
public "getReplacedSegments"(): $ArrayList<($BasedSequence)>
public "isFinalized"(): boolean
public "getReplacedLength"(): integer
public "addOriginalText"(arg0: integer, arg1: integer): void
public "addReplacedText"(arg0: integer, arg1: integer, arg2: $BasedSequence$Type): void
get "parent"(): $ReplacedTextMapper
get "modified"(): boolean
get "regions"(): $ArrayList<($ReplacedTextRegion)>
get "replacedSequence"(): $BasedSequence
get "replacedSegments"(): $ArrayList<($BasedSequence)>
get "finalized"(): boolean
get "replacedLength"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReplacedTextMapper$Type = ($ReplacedTextMapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReplacedTextMapper_ = $ReplacedTextMapper$Type;
}}
declare module "packages/com/vladsch/flexmark/util/ast/$BlockContent" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"

export class $BlockContent {

constructor()
constructor(arg0: $BlockContent$Type, arg1: integer, arg2: integer)

public "add"(arg0: $BasedSequence$Type, arg1: integer): void
public "addAll"(arg0: $List$Type<($BasedSequence$Type)>, arg1: $List$Type<(integer)>): void
public "getString"(): string
public "getContents"(): $BasedSequence
public "getContents"(arg0: integer, arg1: integer): $BasedSequence
public "getLines"(): $List<($BasedSequence)>
public "getSpanningChars"(): $BasedSequence
public "getLineIndents"(): $List<(integer)>
public "getLineIndent"(): integer
public "getLine"(arg0: integer): $BasedSequence
public "getEndOffset"(): integer
public "getStartOffset"(): integer
public "getLineCount"(): integer
public "getSourceLength"(): integer
public "hasSingleLine"(): boolean
public "subContents"(arg0: integer, arg1: integer): $BlockContent
get "string"(): string
get "contents"(): $BasedSequence
get "lines"(): $List<($BasedSequence)>
get "spanningChars"(): $BasedSequence
get "lineIndents"(): $List<(integer)>
get "lineIndent"(): integer
get "endOffset"(): integer
get "startOffset"(): integer
get "lineCount"(): integer
get "sourceLength"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockContent$Type = ($BlockContent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockContent_ = $BlockContent$Type;
}}
declare module "packages/com/vladsch/flexmark/util/data/$DataKey" {
import {$DataHolder, $DataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$DataHolder"
import {$DataNotNullValueFactory, $DataNotNullValueFactory$Type} from "packages/com/vladsch/flexmark/util/data/$DataNotNullValueFactory"
import {$MutableDataHolder, $MutableDataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$MutableDataHolder"
import {$NotNullValueSupplier, $NotNullValueSupplier$Type} from "packages/com/vladsch/flexmark/util/data/$NotNullValueSupplier"
import {$DataKeyBase, $DataKeyBase$Type} from "packages/com/vladsch/flexmark/util/data/$DataKeyBase"

export class $DataKey<T> extends $DataKeyBase<(T)> {

constructor(arg0: string, arg1: T)
constructor(arg0: string, arg1: $DataKey$Type<(T)>)
constructor(arg0: string, arg1: $NotNullValueSupplier$Type<(T)>)
constructor(arg0: string, arg1: T, arg2: $DataNotNullValueFactory$Type<(T)>)

public "get"(arg0: $DataHolder$Type): T
public "toString"(): string
public "set"(arg0: $MutableDataHolder$Type, arg1: T): $MutableDataHolder
public "getDefaultValue"(arg0: $DataHolder$Type): T
public "getDefaultValue"(): T
get "defaultValue"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataKey$Type<T> = ($DataKey<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataKey_<T> = $DataKey$Type<(T)>;
}}
declare module "packages/com/vladsch/flexmark/util/ast/$Content" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"

export interface $Content {

 "getContentChars"(arg0: integer, arg1: integer): $BasedSequence
 "getContentChars"(): $BasedSequence
 "getContentLines"(): $List<($BasedSequence)>
 "getContentLines"(arg0: integer, arg1: integer): $List<($BasedSequence)>
 "getLineChars"(arg0: integer): $BasedSequence
 "getSpanningChars"(): $BasedSequence
 "getLineCount"(): integer
}

export namespace $Content {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Content$Type = ($Content);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Content_ = $Content$Type;
}}
declare module "packages/com/vladsch/flexmark/util/data/$DataKeyAggregator" {
import {$DataHolder, $DataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$DataHolder"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $DataKeyAggregator {

 "clean"(arg0: $DataHolder$Type): $DataHolder
 "aggregate"(arg0: $DataHolder$Type): $DataHolder
 "aggregateActions"(arg0: $DataHolder$Type, arg1: $DataHolder$Type, arg2: $DataHolder$Type): $DataHolder
 "invokeAfterSet"(): $Set<($Class<(any)>)>
}

export namespace $DataKeyAggregator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataKeyAggregator$Type = ($DataKeyAggregator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataKeyAggregator_ = $DataKeyAggregator$Type;
}}
declare module "packages/com/vladsch/flexmark/util/data/$NotNullValueSupplier" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export interface $NotNullValueSupplier<T> extends $Supplier<(T)> {

 "get"(): T

(): T
}

export namespace $NotNullValueSupplier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NotNullValueSupplier$Type<T> = ($NotNullValueSupplier<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NotNullValueSupplier_<T> = $NotNullValueSupplier$Type<(T)>;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/builder/$BasedSegmentBuilder" {
import {$IBasedSegmentBuilder, $IBasedSegmentBuilder$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$IBasedSegmentBuilder"
import {$SegmentOptimizer, $SegmentOptimizer$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$SegmentOptimizer"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"
import {$SegmentBuilderBase, $SegmentBuilderBase$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$SegmentBuilderBase"

export class $BasedSegmentBuilder extends $SegmentBuilderBase<($BasedSegmentBuilder)> implements $IBasedSegmentBuilder<($BasedSegmentBuilder)> {
static readonly "MIN_PART_CAPACITY": integer
static readonly "EMPTY_PARTS": (integer)[]


public "getBaseSequence"(): $BasedSequence
public static "emptyBuilder"(arg0: $BasedSequence$Type, arg1: $SegmentOptimizer$Type): $BasedSegmentBuilder
public static "emptyBuilder"(arg0: $BasedSequence$Type, arg1: integer): $BasedSegmentBuilder
public static "emptyBuilder"(arg0: $BasedSequence$Type, arg1: $SegmentOptimizer$Type, arg2: integer): $BasedSegmentBuilder
public static "emptyBuilder"(arg0: $BasedSequence$Type): $BasedSegmentBuilder
public "toStringWithRanges"(): string
public "toStringChars"(): string
public "toStringWithRangesVisibleWhitespace"(): string
get "baseSequence"(): $BasedSequence
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BasedSegmentBuilder$Type = ($BasedSegmentBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BasedSegmentBuilder_ = $BasedSegmentBuilder$Type;
}}
declare module "packages/com/vladsch/flexmark/util/dependency/$Dependent" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $Dependent {

 "getAfterDependents"(): $Set<($Class<(any)>)>
 "affectsGlobalScope"(): boolean
 "getBeforeDependents"(): $Set<($Class<(any)>)>
}

export namespace $Dependent {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Dependent$Type = ($Dependent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Dependent_ = $Dependent$Type;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/$IRichSequence" {
import {$Range, $Range$Type} from "packages/com/vladsch/flexmark/util/sequence/$Range"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$IntStream, $IntStream$Type} from "packages/java/util/stream/$IntStream"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$SequenceUtils, $SequenceUtils$Type} from "packages/com/vladsch/flexmark/util/sequence/$SequenceUtils"
import {$Pair, $Pair$Type} from "packages/com/vladsch/flexmark/util/misc/$Pair"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$CharPredicate, $CharPredicate$Type} from "packages/com/vladsch/flexmark/util/misc/$CharPredicate"
import {$StringBuilder, $StringBuilder$Type} from "packages/java/lang/$StringBuilder"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ISequenceBuilder, $ISequenceBuilder$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$ISequenceBuilder"
import {$CharMapper, $CharMapper$Type} from "packages/com/vladsch/flexmark/util/sequence/mappers/$CharMapper"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $IRichSequence<T extends $IRichSequence<(T)>> extends charseq, $Comparable<(charseq)>, $SequenceUtils {

 "toVisibleWhitespaceString"(): string
 "equals"(arg0: any): boolean
 "equals"(arg0: any, arg1: boolean): boolean
 "append"(...arg0: (charseq)[]): T
 "append"(arg0: $Iterable$Type<(any)>): T
 "hashCode"(): integer
 "indexOf"(arg0: character, arg1: integer): integer
 "indexOf"(arg0: charseq): integer
 "indexOf"(arg0: charseq, arg1: integer): integer
 "indexOf"(arg0: charseq, arg1: integer, arg2: integer): integer
 "indexOf"(arg0: character, arg1: integer, arg2: integer): integer
 "indexOf"(arg0: character): integer
/**
 * 
 * @deprecated
 */
 "insert"(arg0: charseq, arg1: integer): T
 "insert"(arg0: integer, arg1: charseq): T
 "startsWith"(arg0: charseq): boolean
 "startsWith"(arg0: charseq, arg1: boolean): boolean
 "startsWith"(arg0: $CharPredicate$Type): boolean
 "lastIndexOf"(arg0: charseq): integer
 "lastIndexOf"(arg0: character): integer
 "lastIndexOf"(arg0: character, arg1: integer): integer
 "lastIndexOf"(arg0: character, arg1: integer, arg2: integer): integer
 "lastIndexOf"(arg0: charseq, arg1: integer, arg2: integer): integer
 "lastIndexOf"(arg0: charseq, arg1: integer): integer
 "isEmpty"(): boolean
 "replace"(arg0: charseq, arg1: charseq): T
 "replace"(arg0: integer, arg1: integer, arg2: charseq): T
 "matches"(arg0: charseq, arg1: boolean): boolean
 "matches"(arg0: charseq): boolean
 "split"(arg0: charseq, arg1: boolean, arg2: $CharPredicate$Type): (T)[]
 "split"(arg0: charseq, arg1: integer, arg2: integer, arg3: $CharPredicate$Type): (T)[]
 "split"(arg0: charseq, arg1: integer, arg2: integer): (T)[]
 "split"(arg0: charseq): (T)[]
/**
 * 
 * @deprecated
 */
 "split"(arg0: character, arg1: integer, arg2: integer): (T)[]
 "split"(arg0: charseq, arg1: integer, arg2: boolean, arg3: $CharPredicate$Type): (T)[]
/**
 * 
 * @deprecated
 */
 "split"(arg0: character): (T)[]
/**
 * 
 * @deprecated
 */
 "split"(arg0: character, arg1: integer): (T)[]
 "toLowerCase"(): T
 "toUpperCase"(): T
 "trim"(arg0: integer): T
 "trim"(arg0: $CharPredicate$Type): T
 "trim"(arg0: integer, arg1: $CharPredicate$Type): T
 "trim"(): T
 "isBlank"(): boolean
 "equalsIgnoreCase"(arg0: any): boolean
 "endsWith"(arg0: charseq): boolean
 "endsWith"(arg0: charseq, arg1: boolean): boolean
 "endsWith"(arg0: $CharPredicate$Type): boolean
 "subSequence"(arg0: integer): T
 "subSequence"(arg0: $Range$Type): T
 "lastChar"(): character
 "delete"(arg0: integer, arg1: integer): T
 "appendTo"(arg0: $StringBuilder$Type, arg1: $CharMapper$Type): T
 "appendTo"(arg0: $StringBuilder$Type, arg1: $CharMapper$Type, arg2: integer): T
 "appendTo"(arg0: $StringBuilder$Type): T
 "appendTo"(arg0: $StringBuilder$Type, arg1: integer): T
 "appendTo"(arg0: $StringBuilder$Type, arg1: integer, arg2: integer): T
 "appendTo"(arg0: $StringBuilder$Type, arg1: $CharMapper$Type, arg2: integer, arg3: integer): T
 "firstChar"(): character
 "isNull"(): boolean
 "padding"(arg0: integer, arg1: character): T
 "padding"(arg0: integer): T
 "emptyArray"(): (T)[]
 "padStart"(arg0: integer): T
 "padStart"(arg0: integer, arg1: character): T
 "startsWithIgnoreCase"(arg0: charseq): boolean
 "trimmed"(): $Pair<(T), (T)>
 "trimmed"(arg0: $CharPredicate$Type): $Pair<(T), (T)>
 "trimmed"(arg0: integer): $Pair<(T), (T)>
 "trimmed"(arg0: integer, arg1: $CharPredicate$Type): $Pair<(T), (T)>
 "getBuilder"<B extends $ISequenceBuilder<(B), (T)>>(): B
 "lineColumnAtIndex"(arg0: integer): $Pair<(integer), (integer)>
 "endsWithIgnoreCase"(arg0: charseq): boolean
 "indexOfAny"(arg0: $CharPredicate$Type): integer
 "indexOfAny"(arg0: $CharPredicate$Type, arg1: integer): integer
 "indexOfAny"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "lastIndexOfAny"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "lastIndexOfAny"(arg0: $CharPredicate$Type, arg1: integer): integer
 "lastIndexOfAny"(arg0: $CharPredicate$Type): integer
 "removeSuffix"(arg0: charseq, arg1: boolean): T
 "removeSuffix"(arg0: charseq): T
 "matchChars"(arg0: charseq): boolean
 "matchChars"(arg0: charseq, arg1: integer): boolean
 "matchChars"(arg0: charseq, arg1: integer, arg2: boolean): boolean
 "matchChars"(arg0: charseq, arg1: boolean): boolean
 "isNotNull"(): boolean
 "endSequence"(arg0: integer, arg1: integer): T
 "endSequence"(arg0: integer): T
 "endOfLine"(arg0: integer): integer
 "appendSpace"(): T
 "trimStart"(): T
 "trimStart"(arg0: integer, arg1: $CharPredicate$Type): T
 "trimStart"(arg0: integer): T
 "trimStart"(arg0: $CharPredicate$Type): T
 "countTrailingSpaceTab"(arg0: integer): integer
 "countTrailingSpaceTab"(arg0: integer, arg1: integer): integer
 "countTrailingSpaceTab"(): integer
 "countTrailingNotSpaceTab"(arg0: integer, arg1: integer): integer
 "countTrailingNotSpaceTab"(): integer
 "countTrailingNotSpaceTab"(arg0: integer): integer
 "countLeadingWhitespace"(arg0: integer, arg1: integer): integer
 "countLeadingWhitespace"(): integer
 "countLeadingWhitespace"(arg0: integer): integer
 "countTrailingWhitespace"(): integer
 "countTrailingWhitespace"(arg0: integer, arg1: integer): integer
 "countTrailingWhitespace"(arg0: integer): integer
 "countOfNotWhitespace"(): integer
 "countLeadingSpaceTab"(): integer
 "countLeadingSpaceTab"(arg0: integer): integer
 "countLeadingSpaceTab"(arg0: integer, arg1: integer): integer
 "countLeadingColumns"(arg0: integer, arg1: $CharPredicate$Type): integer
 "countLeadingNotSpaceTab"(arg0: integer): integer
 "countLeadingNotSpaceTab"(): integer
 "countLeadingNotSpaceTab"(arg0: integer, arg1: integer): integer
 "countTrailingNotWhitespace"(arg0: integer): integer
 "countTrailingNotWhitespace"(arg0: integer, arg1: integer): integer
 "countTrailingNotWhitespace"(): integer
 "nullIfNotStartsWith"(...arg0: (charseq)[]): T
 "nullIfNotStartsWith"(arg0: boolean, ...arg1: (charseq)[]): T
 "countLeadingNotSpace"(arg0: integer, arg1: integer): integer
 "countLeadingNotSpace"(arg0: integer): integer
 "countLeadingNotSpace"(): integer
 "countTrailingNotSpace"(arg0: integer): integer
 "countTrailingNotSpace"(arg0: integer, arg1: integer): integer
 "countTrailingNotSpace"(): integer
 "countLeadingNotWhitespace"(arg0: integer): integer
 "countLeadingNotWhitespace"(): integer
 "countLeadingNotWhitespace"(arg0: integer, arg1: integer): integer
 "normalizeEndWithEOL"(): string
 "leadingBlankLinesRange"(arg0: integer, arg1: integer): $Range
 "leadingBlankLinesRange"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): $Range
 "leadingBlankLinesRange"(arg0: integer): $Range
 "leadingBlankLinesRange"(): $Range
 "endOfDelimitedByAny"(arg0: $CharPredicate$Type, arg1: integer): integer
 "matchedCharCountIgnoreCase"(arg0: charseq, arg1: integer): integer
 "matchedCharCountIgnoreCase"(arg0: charseq, arg1: integer, arg2: integer): integer
 "trailingBlankLinesRange"(arg0: integer): $Range
 "trailingBlankLinesRange"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): $Range
 "trailingBlankLinesRange"(): $Range
 "trailingBlankLinesRange"(arg0: integer, arg1: integer): $Range
 "suffixOnceWithSpace"(): T
 "nullIfNotStartsWithIgnoreCase"(...arg0: (charseq)[]): T
 "startsWithWhitespace"(): boolean
 "nullIfEndsWithIgnoreCase"(...arg0: (charseq)[]): T
 "startOfDelimitedByAny"(arg0: $CharPredicate$Type, arg1: integer): integer
 "nullIfNotEndsWithIgnoreCase"(...arg0: (charseq)[]): T
 "removeProperSuffixIgnoreCase"(arg0: charseq): T
/**
 * 
 * @deprecated
 */
 "getLineColumnAtIndex"(arg0: integer): $Pair<(integer), (integer)>
 "removePrefixIgnoreCase"(arg0: charseq): T
 "matchCharsReversedIgnoreCase"(arg0: charseq, arg1: integer): boolean
 "prefixOnceWithSpace"(): T
 "nullIfStartsWithIgnoreCase"(...arg0: (charseq)[]): T
 "removeProperPrefixIgnoreCase"(arg0: charseq): T
 "endOfDelimitedByAnyNot"(arg0: $CharPredicate$Type, arg1: integer): integer
 "blankLinesRemovedRanges"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): $List<($Range)>
 "blankLinesRemovedRanges"(): $List<($Range)>
 "blankLinesRemovedRanges"(arg0: integer): $List<($Range)>
 "blankLinesRemovedRanges"(arg0: integer, arg1: integer): $List<($Range)>
 "matchedCharCountReversed"(arg0: charseq, arg1: integer, arg2: integer): integer
 "matchedCharCountReversed"(arg0: charseq, arg1: integer, arg2: boolean): integer
 "matchedCharCountReversed"(arg0: charseq, arg1: integer): integer
 "matchedCharCountReversed"(arg0: charseq, arg1: integer, arg2: integer, arg3: boolean): integer
/**
 * 
 * @deprecated
 */
 "nullIfStartsWithNot"(...arg0: (charseq)[]): T
 "matchCharsIgnoreCase"(arg0: charseq): boolean
 "matchCharsIgnoreCase"(arg0: charseq, arg1: integer): boolean
 "matchedCharCountReversedIgnoreCase"(arg0: charseq, arg1: integer): integer
 "matchedCharCountReversedIgnoreCase"(arg0: charseq, arg1: integer, arg2: integer): integer
 "startOfDelimitedByAnyNot"(arg0: $CharPredicate$Type, arg1: integer): integer
 "removeSuffixIgnoreCase"(arg0: charseq): T
 "splitList"(arg0: charseq, arg1: integer, arg2: integer): $List<(T)>
 "splitList"(arg0: charseq): $List<(T)>
 "splitList"(arg0: charseq, arg1: integer, arg2: integer, arg3: $CharPredicate$Type): $List<(T)>
 "splitList"(arg0: charseq, arg1: boolean, arg2: $CharPredicate$Type): $List<(T)>
 "splitList"(arg0: charseq, arg1: integer, arg2: boolean, arg3: $CharPredicate$Type): $List<(T)>
 "isNotBlank"(): boolean
 "isNotEmpty"(): boolean
 "padEnd"(arg0: integer): T
 "padEnd"(arg0: integer, arg1: character): T
 "removePrefix"(arg0: charseq, arg1: boolean): T
 "removePrefix"(arg0: charseq): T
 "trimEnd"(): T
 "trimEnd"(arg0: $CharPredicate$Type): T
 "trimEnd"(arg0: integer, arg1: $CharPredicate$Type): T
 "trimEnd"(arg0: integer): T
 "startOfLine"(arg0: integer): integer
 "sequenceOf"(arg0: charseq, arg1: integer, arg2: integer): T
 "sequenceOf"(arg0: charseq): T
 "sequenceOf"(arg0: charseq, arg1: integer): T
 "isIn"(arg0: $Collection$Type<(any)>): boolean
 "isIn"(arg0: (string)[]): boolean
 "normalizeEOL"(): string
 "midSequence"(arg0: integer): T
 "midSequence"(arg0: integer, arg1: integer): T
 "countLeading"(arg0: $CharPredicate$Type, arg1: integer): integer
 "countLeading"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "countLeading"(arg0: $CharPredicate$Type): integer
/**
 * 
 * @deprecated
 */
 "countLeading"(arg0: character): integer
/**
 * 
 * @deprecated
 */
 "countLeading"(): integer
 "midCharAt"(arg0: integer): character
 "subSequenceBefore"(arg0: $Range$Type): T
 "safeSubSequence"(arg0: integer): T
 "safeSubSequence"(arg0: integer, arg1: integer): T
 "indexOfNot"(arg0: character, arg1: integer): integer
 "indexOfNot"(arg0: character): integer
 "indexOfNot"(arg0: character, arg1: integer, arg2: integer): integer
 "countLeadingNot"(arg0: $CharPredicate$Type, arg1: integer): integer
 "countLeadingNot"(arg0: $CharPredicate$Type): integer
 "countLeadingNot"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "nullSequence"(): T
 "endCharAt"(arg0: integer): character
 "subSequenceAfter"(arg0: $Range$Type): T
 "lastIndexOfNot"(arg0: character, arg1: integer, arg2: integer): integer
 "lastIndexOfNot"(arg0: character, arg1: integer): integer
 "lastIndexOfNot"(arg0: character): integer
 "lastIndexOfAnyNot"(arg0: $CharPredicate$Type, arg1: integer): integer
 "lastIndexOfAnyNot"(arg0: $CharPredicate$Type): integer
 "lastIndexOfAnyNot"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "indexOfAnyNot"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "indexOfAnyNot"(arg0: $CharPredicate$Type, arg1: integer): integer
 "indexOfAnyNot"(arg0: $CharPredicate$Type): integer
 "safeCharAt"(arg0: integer): character
 "countOfWhitespace"(): integer
 "countOfAnyNot"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "countOfAnyNot"(arg0: $CharPredicate$Type): integer
 "countOfAnyNot"(arg0: $CharPredicate$Type, arg1: integer): integer
 "countLeadingSpace"(arg0: integer): integer
 "countLeadingSpace"(): integer
 "countLeadingSpace"(arg0: integer, arg1: integer): integer
 "countTrailingNot"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "countTrailingNot"(arg0: $CharPredicate$Type, arg1: integer): integer
 "countTrailingNot"(arg0: $CharPredicate$Type): integer
 "countTrailing"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "countTrailing"(arg0: $CharPredicate$Type, arg1: integer): integer
 "countTrailing"(arg0: $CharPredicate$Type): integer
/**
 * 
 * @deprecated
 */
 "countTrailing"(): integer
 "countOfSpaceTab"(): integer
 "countOfAny"(arg0: $CharPredicate$Type, arg1: integer): integer
 "countOfAny"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "countOfAny"(arg0: $CharPredicate$Type): integer
 "countTrailingSpace"(): integer
 "countTrailingSpace"(arg0: integer): integer
 "countTrailingSpace"(arg0: integer, arg1: integer): integer
 "trimStartRange"(arg0: $CharPredicate$Type): $Range
 "trimStartRange"(): $Range
 "trimStartRange"(arg0: integer): $Range
 "trimStartRange"(arg0: integer, arg1: $CharPredicate$Type): $Range
 "trimEndRange"(arg0: integer, arg1: $CharPredicate$Type): $Range
 "trimEndRange"(arg0: integer): $Range
 "trimEndRange"(arg0: $CharPredicate$Type): $Range
 "trimEndRange"(): $Range
/**
 * 
 * @deprecated
 */
 "countOf"(arg0: character): integer
 "trimRange"(arg0: integer): $Range
 "trimRange"(arg0: $CharPredicate$Type): $Range
 "trimRange"(): $Range
 "trimRange"(arg0: integer, arg1: $CharPredicate$Type): $Range
 "countOfNotSpaceTab"(): integer
 "nullIfNot"(arg0: $BiPredicate$Type<(any), (any)>, ...arg1: (charseq)[]): T
 "nullIfNot"(...arg0: (charseq)[]): T
 "nullIfNot"(arg0: $Predicate$Type<(any)>, ...arg1: (charseq)[]): T
 "nullIfEndsWith"(...arg0: (charseq)[]): T
 "nullIfEndsWith"(arg0: boolean, ...arg1: (charseq)[]): T
 "ifNull"(arg0: T): T
 "ifNullEmptyAfter"(arg0: T): T
 "nullIfNotEndsWith"(arg0: boolean, ...arg1: (charseq)[]): T
 "nullIfNotEndsWith"(...arg0: (charseq)[]): T
 "nullIfStartsWith"(arg0: boolean, ...arg1: (charseq)[]): T
 "nullIfStartsWith"(...arg0: (charseq)[]): T
 "trimmedStart"(arg0: integer): T
 "trimmedStart"(arg0: $CharPredicate$Type): T
 "trimmedStart"(): T
 "trimmedStart"(arg0: integer, arg1: $CharPredicate$Type): T
 "ifNullEmptyBefore"(arg0: T): T
 "nullIf"(arg0: boolean): T
 "nullIf"(...arg0: (charseq)[]): T
 "nullIf"(arg0: $BiPredicate$Type<(any), (any)>, ...arg1: (charseq)[]): T
 "nullIf"(arg0: $Predicate$Type<(any)>, ...arg1: (charseq)[]): T
 "nullIfEmpty"(): T
 "nullIfBlank"(): T
 "trimmedEnd"(arg0: integer, arg1: $CharPredicate$Type): T
 "trimmedEnd"(): T
 "trimmedEnd"(arg0: integer): T
 "trimmedEnd"(arg0: $CharPredicate$Type): T
 "startOfDelimitedBy"(arg0: charseq, arg1: integer): integer
 "eolStartRange"(arg0: integer): $Range
/**
 * 
 * @deprecated
 */
 "nullIfEndsWithNot"(...arg0: (charseq)[]): T
 "trimmedEOL"(): T
 "endOfDelimitedBy"(arg0: charseq, arg1: integer): integer
 "startOfLineAnyEOL"(arg0: integer): integer
 "lineRangeAt"(arg0: integer): $Range
 "eolEndLength"(): integer
 "eolEndLength"(arg0: integer): integer
 "eolEndRange"(arg0: integer): $Range
 "lineRangeAtAnyEOL"(arg0: integer): $Range
 "lineAt"(arg0: integer): T
 "lineAtAnyEOL"(arg0: integer): T
 "trimTailBlankLines"(): T
 "trimEOL"(): T
 "trimToStartOfLine"(arg0: $CharPredicate$Type, arg1: boolean, arg2: integer): T
 "trimToStartOfLine"(arg0: boolean, arg1: integer): T
 "trimToStartOfLine"(): T
 "trimToStartOfLine"(arg0: boolean): T
 "trimToStartOfLine"(arg0: integer): T
 "trimToEndOfLine"(arg0: boolean, arg1: integer): T
 "trimToEndOfLine"(arg0: $CharPredicate$Type, arg1: boolean, arg2: integer): T
 "trimToEndOfLine"(): T
 "trimToEndOfLine"(arg0: integer): T
 "trimToEndOfLine"(arg0: boolean): T
 "matchedCharCount"(arg0: charseq, arg1: integer, arg2: integer, arg3: boolean): integer
 "matchedCharCount"(arg0: charseq, arg1: integer, arg2: integer): integer
 "matchedCharCount"(arg0: charseq, arg1: integer): integer
 "matchedCharCount"(arg0: charseq, arg1: integer, arg2: integer, arg3: boolean, arg4: boolean): integer
 "matchedCharCount"(arg0: charseq, arg1: integer, arg2: boolean): integer
 "matchCharsReversed"(arg0: charseq, arg1: integer, arg2: boolean): boolean
 "matchCharsReversed"(arg0: charseq, arg1: integer): boolean
 "matchesIgnoreCase"(arg0: charseq): boolean
 "trimLeadBlankLines"(): T
 "startsWithSpaceTab"(): boolean
 "endsWithWhitespace"(): boolean
 "removeProperSuffix"(arg0: charseq, arg1: boolean): T
 "removeProperSuffix"(arg0: charseq): T
 "startsWithSpace"(): boolean
 "endsWithSpace"(): boolean
 "endsWithSpaceTab"(): boolean
 "startsWithEOL"(): boolean
 "endsWithAnyEOL"(): boolean
 "startsWithAnyEOL"(): boolean
 "endsWithEOL"(): boolean
 "toSpc"(): T
 "toNbSp"(): T
 "removeProperPrefix"(arg0: charseq): T
 "removeProperPrefix"(arg0: charseq, arg1: boolean): T
 "toMapped"(arg0: $CharMapper$Type): T
 "prefixWith"(arg0: charseq): T
 "suffixWith"(arg0: charseq): T
 "splitEOL"(): (T)[]
 "splitEOL"(arg0: boolean): (T)[]
 "appendEOL"(): T
 "suffixWithEOL"(): T
 "splitListEOL"(arg0: boolean, arg1: $CharPredicate$Type): $List<(T)>
 "splitListEOL"(arg0: boolean): $List<(T)>
 "splitListEOL"(): $List<(T)>
 "indexOfAll"(arg0: charseq): (integer)[]
 "prefixWithEOL"(): T
 "prefixOnceWithEOL"(): T
 "suffixOnceWithEOL"(): T
 "suffixWithSpace"(): T
 "prefixWithSpace"(): T
 "prefixOnceWith"(arg0: charseq): T
 "suffixOnceWith"(arg0: charseq): T
 "extractRanges"(...arg0: ($Range$Type)[]): T
 "extractRanges"(arg0: $Iterable$Type<($Range$Type)>): T
 "columnAtIndex"(arg0: integer): integer
 "appendSpaces"(arg0: integer): T
 "suffixWithSpaces"(arg0: integer): T
/**
 * 
 * @deprecated
 */
 "getColumnAtIndex"(arg0: integer): integer
 "prefixWithSpaces"(arg0: integer): T
 "appendRangesTo"(arg0: $StringBuilder$Type, ...arg1: ($Range$Type)[]): T
 "appendRangesTo"(arg0: $StringBuilder$Type, arg1: $Iterable$Type<(any)>): T
 "appendRangesTo"(arg0: $StringBuilder$Type, arg1: $CharMapper$Type, ...arg2: ($Range$Type)[]): T
 "appendRangesTo"(arg0: $StringBuilder$Type, arg1: $CharMapper$Type, arg2: $Iterable$Type<(any)>): T
 "toStringOrNull"(): string
 "isCharAt"(arg0: integer, arg1: $CharPredicate$Type): boolean
/**
 * 
 * @deprecated
 */
 "eolLength"(arg0: integer): integer
 "endOfLineAnyEOL"(arg0: integer): integer
 "eolStartLength"(arg0: integer): integer
/**
 * 
 * @deprecated
 */
 "eolStartLength"(): integer
 "length"(): integer
 "toString"(): string
 "charAt"(arg0: integer): character
 "codePoints"(): $IntStream
 "chars"(): $IntStream
 "compareTo"(arg0: charseq): integer
}

export namespace $IRichSequence {
function compare(arg0: charseq, arg1: charseq): integer
function toVisibleWhitespaceString(arg0: charseq): string
function equals(arg0: charseq, arg1: any): boolean
function hashCode(arg0: charseq): integer
function indexOf(arg0: charseq, arg1: charseq, arg2: integer): integer
function indexOf(arg0: charseq, arg1: character, arg2: integer): integer
function indexOf(arg0: charseq, arg1: charseq): integer
function indexOf(arg0: charseq, arg1: character): integer
function indexOf(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function indexOf(arg0: charseq, arg1: character, arg2: integer, arg3: integer): integer
function compare(arg0: charseq, arg1: charseq, arg2: boolean): integer
function compare(arg0: charseq, arg1: charseq, arg2: boolean, arg3: $CharPredicate$Type): integer
function startsWith(arg0: charseq, arg1: charseq, arg2: boolean): boolean
function startsWith(arg0: charseq, arg1: $CharPredicate$Type): boolean
function startsWith(arg0: charseq, arg1: charseq): boolean
function lastIndexOf(arg0: charseq, arg1: charseq, arg2: integer): integer
function lastIndexOf(arg0: charseq, arg1: charseq): integer
function lastIndexOf(arg0: charseq, arg1: character, arg2: integer): integer
function lastIndexOf(arg0: charseq, arg1: character, arg2: integer, arg3: integer): integer
function lastIndexOf(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function lastIndexOf(arg0: charseq, arg1: character): integer
function isEmpty(arg0: charseq): boolean
function matches(arg0: charseq, arg1: charseq): boolean
function matches(arg0: charseq, arg1: charseq, arg2: boolean): boolean
function split<T>(arg0: T, arg1: (T)[], arg2: charseq, arg3: integer, arg4: integer, arg5: $CharPredicate$Type): (T)[]
function split<T>(arg0: T, arg1: (T)[], arg2: charseq, arg3: integer, arg4: integer): (T)[]
function split<T>(arg0: T, arg1: (T)[], arg2: charseq, arg3: integer, arg4: boolean, arg5: $CharPredicate$Type): (T)[]
function split<T>(arg0: T, arg1: (T)[], arg2: charseq, arg3: boolean, arg4: $CharPredicate$Type): (T)[]
function split<T>(arg0: T, arg1: (T)[], arg2: charseq): (T)[]
function trim<T>(arg0: T, arg1: integer): T
function trim<T>(arg0: T): T
function trim<T>(arg0: T, arg1: $CharPredicate$Type): T
function trim<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): T
function isBlank(arg0: charseq): boolean
function endsWith(arg0: charseq, arg1: charseq, arg2: boolean): boolean
function endsWith(arg0: charseq, arg1: $CharPredicate$Type): boolean
function endsWith(arg0: charseq, arg1: charseq): boolean
function subSequence<T>(arg0: T, arg1: integer): T
function subSequence<T>(arg0: T, arg1: $Range$Type): T
function lastChar(arg0: charseq): character
function firstChar(arg0: charseq): character
function expandTo(arg0: (integer)[], arg1: integer, arg2: integer): (integer)[]
function padStart(arg0: charseq, arg1: integer): string
function padStart(arg0: charseq, arg1: integer, arg2: character): string
function trimmed<T>(arg0: T): $Pair<(T), (T)>
function trimmed<T>(arg0: T, arg1: integer): $Pair<(T), (T)>
function trimmed<T>(arg0: T, arg1: $CharPredicate$Type): $Pair<(T), (T)>
function trimmed<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): $Pair<(T), (T)>
function parseIntOrDefault(arg0: string, arg1: integer): integer
function parseIntOrDefault(arg0: string, arg1: integer, arg2: integer): integer
function lineColumnAtIndex(arg0: charseq, arg1: integer): $Pair<(integer), (integer)>
function containsAny(arg0: charseq, arg1: $CharPredicate$Type): boolean
function containsAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): boolean
function indexOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function indexOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function indexOfAny(arg0: charseq, arg1: $CharPredicate$Type): integer
function lastIndexOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function lastIndexOfAny(arg0: charseq, arg1: $CharPredicate$Type): integer
function lastIndexOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function matchChars(arg0: charseq, arg1: charseq, arg2: integer): boolean
function matchChars(arg0: charseq, arg1: charseq, arg2: integer, arg3: boolean): boolean
function matchChars(arg0: charseq, arg1: charseq): boolean
function matchChars(arg0: charseq, arg1: charseq, arg2: boolean): boolean
function endOfLine(arg0: charseq, arg1: integer): integer
function validateIndex(arg0: integer, arg1: integer): void
function trimStart<T>(arg0: T, arg1: $CharPredicate$Type): T
function trimStart<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): T
function trimStart<T>(arg0: T): T
function trimStart<T>(arg0: T, arg1: integer): T
function toStringArray(...arg0: (charseq)[]): (string)[]
function countTrailingSpaceTab(arg0: charseq, arg1: integer, arg2: integer): integer
function countTrailingSpaceTab(arg0: charseq, arg1: integer): integer
function countTrailingSpaceTab(arg0: charseq): integer
function countTrailingNotSpaceTab(arg0: charseq, arg1: integer, arg2: integer): integer
function countTrailingNotSpaceTab(arg0: charseq, arg1: integer): integer
function countTrailingNotSpaceTab(arg0: charseq): integer
function countLeadingWhitespace(arg0: charseq, arg1: integer): integer
function countLeadingWhitespace(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingWhitespace(arg0: charseq): integer
function countTrailingWhitespace(arg0: charseq, arg1: integer): integer
function countTrailingWhitespace(arg0: charseq): integer
function countTrailingWhitespace(arg0: charseq, arg1: integer, arg2: integer): integer
function countOfNotWhitespace(arg0: charseq): integer
function countLeadingSpaceTab(arg0: charseq, arg1: integer): integer
function countLeadingSpaceTab(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingSpaceTab(arg0: charseq): integer
function countLeadingColumns(arg0: charseq, arg1: integer, arg2: $CharPredicate$Type): integer
function countLeadingNotSpaceTab(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingNotSpaceTab(arg0: charseq, arg1: integer): integer
function countLeadingNotSpaceTab(arg0: charseq): integer
function countTrailingNotWhitespace(arg0: charseq): integer
function countTrailingNotWhitespace(arg0: charseq, arg1: integer): integer
function countTrailingNotWhitespace(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingNotSpace(arg0: charseq, arg1: integer): integer
function countLeadingNotSpace(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingNotSpace(arg0: charseq): integer
function countTrailingNotSpace(arg0: charseq, arg1: integer, arg2: integer): integer
function countTrailingNotSpace(arg0: charseq, arg1: integer): integer
function countTrailingNotSpace(arg0: charseq): integer
function countLeadingNotWhitespace(arg0: charseq): integer
function countLeadingNotWhitespace(arg0: charseq, arg1: integer): integer
function countLeadingNotWhitespace(arg0: charseq, arg1: integer, arg2: integer): integer
function leadingBlankLinesRange(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): $Range
function leadingBlankLinesRange(arg0: charseq): $Range
function leadingBlankLinesRange(arg0: charseq, arg1: integer, arg2: integer): $Range
function leadingBlankLinesRange(arg0: charseq, arg1: integer): $Range
function endOfDelimitedByAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function getVisibleSpacesMap(): $Map<(character), (string)>
function matchedCharCountIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer): integer
function matchedCharCountIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function trailingBlankLinesRange(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): $Range
function trailingBlankLinesRange(arg0: charseq, arg1: integer, arg2: integer): $Range
function trailingBlankLinesRange(arg0: charseq, arg1: integer): $Range
function trailingBlankLinesRange(arg0: charseq): $Range
function isVisibleWhitespace(arg0: character): boolean
function columnsToNextTabStop(arg0: integer): integer
function startsWithWhitespace(arg0: charseq): boolean
function validateIndexInclusiveEnd(arg0: integer, arg1: integer): void
function parseUnsignedIntOrNull(arg0: string): integer
function parseUnsignedIntOrNull(arg0: string, arg1: integer): integer
function startOfDelimitedByAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function parseNumberPrefixOrNull(arg0: string, arg1: $Predicate$Type<(string)>): $Pair<(number), (string)>
function subSequenceBeforeAfter<T>(arg0: T, arg1: $Range$Type): $Pair<(T), (T)>
function parseUnsignedIntOrDefault(arg0: string, arg1: integer): integer
function parseUnsignedIntOrDefault(arg0: string, arg1: integer, arg2: integer): integer
function matchCharsReversedIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer): boolean
function endOfDelimitedByAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function blankLinesRemovedRanges(arg0: charseq, arg1: integer): $List<($Range)>
function blankLinesRemovedRanges(arg0: charseq): $List<($Range)>
function blankLinesRemovedRanges(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): $List<($Range)>
function blankLinesRemovedRanges(arg0: charseq, arg1: integer, arg2: integer): $List<($Range)>
function matchedCharCountReversed(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function matchedCharCountReversed(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer, arg4: boolean): integer
function matchedCharCountReversed(arg0: charseq, arg1: charseq, arg2: integer, arg3: boolean): integer
function matchedCharCountReversed(arg0: charseq, arg1: charseq, arg2: integer): integer
function matchCharsIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer): boolean
function matchCharsIgnoreCase(arg0: charseq, arg1: charseq): boolean
function matchedCharCountReversedIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function matchedCharCountReversedIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer): integer
function startOfDelimitedByAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function splitList<T>(arg0: T, arg1: charseq, arg2: integer, arg3: integer): $List<(T)>
function splitList<T>(arg0: T, arg1: charseq, arg2: boolean, arg3: $CharPredicate$Type): $List<(T)>
function splitList<T>(arg0: T, arg1: charseq, arg2: integer, arg3: boolean, arg4: $CharPredicate$Type): $List<(T)>
function splitList<T>(arg0: T, arg1: charseq, arg2: integer, arg3: integer, arg4: $CharPredicate$Type): $List<(T)>
function splitList<T>(arg0: T, arg1: charseq): $List<(T)>
function isNotBlank(arg0: charseq): boolean
function isNotEmpty(arg0: charseq): boolean
function padEnd(arg0: charseq, arg1: integer): string
function padEnd(arg0: charseq, arg1: integer, arg2: character): string
function trimEnd<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): T
function trimEnd<T>(arg0: T): T
function trimEnd<T>(arg0: T, arg1: integer): T
function trimEnd<T>(arg0: T, arg1: $CharPredicate$Type): T
function startOfLine(arg0: charseq, arg1: integer): integer
function countLeading(arg0: charseq, arg1: $CharPredicate$Type): integer
function countLeading(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countLeading(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function subSequenceBefore<T>(arg0: T, arg1: $Range$Type): T
function indexOfNot(arg0: charseq, arg1: character, arg2: integer, arg3: integer): integer
function indexOfNot(arg0: charseq, arg1: character, arg2: integer): integer
function indexOfNot(arg0: charseq, arg1: character): integer
function countLeadingNot(arg0: charseq, arg1: $CharPredicate$Type): integer
function countLeadingNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countLeadingNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function subSequenceAfter<T>(arg0: T, arg1: $Range$Type): T
function lastIndexOfNot(arg0: charseq, arg1: character, arg2: integer): integer
function lastIndexOfNot(arg0: charseq, arg1: character): integer
function lastIndexOfNot(arg0: charseq, arg1: character, arg2: integer, arg3: integer): integer
function lastIndexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function lastIndexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function lastIndexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type): integer
function indexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type): integer
function indexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function indexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function safeCharAt(arg0: charseq, arg1: integer): character
function countOfWhitespace(arg0: charseq): integer
function countOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type): integer
function countOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function countLeadingSpace(arg0: charseq, arg1: integer): integer
function countLeadingSpace(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingSpace(arg0: charseq): integer
function countTrailingNot(arg0: charseq, arg1: $CharPredicate$Type): integer
function countTrailingNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function countTrailingNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countTrailing(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countTrailing(arg0: charseq, arg1: $CharPredicate$Type): integer
function countTrailing(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function countOfSpaceTab(arg0: charseq): integer
function countOfAny(arg0: charseq, arg1: $CharPredicate$Type): integer
function countOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function countTrailingSpace(arg0: charseq, arg1: integer): integer
function countTrailingSpace(arg0: charseq, arg1: integer, arg2: integer): integer
function countTrailingSpace(arg0: charseq): integer
function trimStartRange(arg0: charseq, arg1: integer): $Range
function trimStartRange(arg0: charseq, arg1: $CharPredicate$Type): $Range
function trimStartRange(arg0: charseq, arg1: integer, arg2: $CharPredicate$Type): $Range
function trimStartRange(arg0: charseq): $Range
function trimEndRange(arg0: charseq, arg1: $CharPredicate$Type): $Range
function trimEndRange(arg0: charseq): $Range
function trimEndRange(arg0: charseq, arg1: integer): $Range
function trimEndRange(arg0: charseq, arg1: integer, arg2: $CharPredicate$Type): $Range
function trimRange(arg0: charseq): $Range
function trimRange(arg0: charseq, arg1: integer): $Range
function trimRange(arg0: charseq, arg1: $CharPredicate$Type): $Range
function trimRange(arg0: charseq, arg1: integer, arg2: $CharPredicate$Type): $Range
function countOfNotSpaceTab(arg0: charseq): integer
function trimmedStart<T>(arg0: T, arg1: $CharPredicate$Type): T
function trimmedStart<T>(arg0: T): T
function trimmedStart<T>(arg0: T, arg1: integer): T
function trimmedStart<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): T
function trimmedEnd<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): T
function trimmedEnd<T>(arg0: T): T
function trimmedEnd<T>(arg0: T, arg1: $CharPredicate$Type): T
function trimmedEnd<T>(arg0: T, arg1: integer): T
function startOfDelimitedBy(arg0: charseq, arg1: charseq, arg2: integer): integer
function eolStartRange(arg0: charseq, arg1: integer): $Range
function trimmedEOL<T>(arg0: T): T
function endOfDelimitedBy(arg0: charseq, arg1: charseq, arg2: integer): integer
function startOfLineAnyEOL(arg0: charseq, arg1: integer): integer
function lineRangeAt(arg0: charseq, arg1: integer): $Range
function eolEndLength(arg0: charseq): integer
function eolEndLength(arg0: charseq, arg1: integer): integer
function eolEndRange(arg0: charseq, arg1: integer): $Range
function lineRangeAtAnyEOL(arg0: charseq, arg1: integer): $Range
function trimTailBlankLines<T>(arg0: T): T
function trimEOL<T>(arg0: T): T
function matchedCharCount(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer, arg4: boolean): integer
function matchedCharCount(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer, arg4: boolean, arg5: boolean): integer
function matchedCharCount(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function matchedCharCount(arg0: charseq, arg1: charseq, arg2: integer): integer
function matchedCharCount(arg0: charseq, arg1: charseq, arg2: integer, arg3: boolean): integer
function matchCharsReversed(arg0: charseq, arg1: charseq, arg2: integer): boolean
function matchCharsReversed(arg0: charseq, arg1: charseq, arg2: integer, arg3: boolean): boolean
function matchesIgnoreCase(arg0: charseq, arg1: charseq): boolean
function trimLeadBlankLines<T>(arg0: T): T
function startsWithSpaceTab(arg0: charseq): boolean
function endsWithWhitespace(arg0: charseq): boolean
function startsWithSpace(arg0: charseq): boolean
function endsWithSpace(arg0: charseq): boolean
function endsWithSpaceTab(arg0: charseq): boolean
function startsWithEOL(arg0: charseq): boolean
function endsWithAnyEOL(arg0: charseq): boolean
function startsWithAnyEOL(arg0: charseq): boolean
function endsWithEOL(arg0: charseq): boolean
function splitEOL<T>(arg0: T, arg1: (T)[], arg2: boolean): (T)[]
function splitEOL<T>(arg0: T, arg1: (T)[]): (T)[]
function splitListEOL<T>(arg0: T, arg1: boolean, arg2: $CharPredicate$Type): $List<(T)>
function splitListEOL<T>(arg0: T, arg1: boolean): $List<(T)>
function splitListEOL<T>(arg0: T): $List<(T)>
function indexOfAll(arg0: charseq, arg1: charseq): (integer)[]
function columnAtIndex(arg0: charseq, arg1: integer): integer
function compareReversed(arg0: charseq, arg1: charseq): integer
function containsAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): boolean
function containsAnyNot(arg0: charseq, arg1: $CharPredicate$Type): boolean
function containsAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): boolean
function truncateTo(arg0: (integer)[], arg1: integer): (integer)[]
function validateStartEnd(arg0: integer, arg1: integer, arg2: integer): void
function parseNumberOrNull(arg0: string): number
function parseLongOrNull(arg0: string, arg1: integer): long
function parseLongOrNull(arg0: string): long
function parseIntOrNull(arg0: string): integer
function parseIntOrNull(arg0: string, arg1: integer): integer
function containedBy(arg0: $Collection$Type<(any)>, arg1: charseq): boolean
function containedBy<T>(arg0: (T)[], arg1: charseq): boolean
function endOfLineAnyEOL(arg0: charseq, arg1: integer): integer
function eolStartLength(arg0: charseq, arg1: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRichSequence$Type<T> = ($IRichSequence<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRichSequence_<T> = $IRichSequence$Type<(T)>;
}}
declare module "packages/com/vladsch/flexmark/parser/$LightInlineParser" {
import {$Matcher, $Matcher$Type} from "packages/java/util/regex/$Matcher"
import {$Document, $Document$Type} from "packages/com/vladsch/flexmark/util/ast/$Document"
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$Text, $Text$Type} from "packages/com/vladsch/flexmark/ast/$Text"
import {$Parsing, $Parsing$Type} from "packages/com/vladsch/flexmark/ast/util/$Parsing"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$Node, $Node$Type} from "packages/com/vladsch/flexmark/util/ast/$Node"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"
import {$InlineParserOptions, $InlineParserOptions$Type} from "packages/com/vladsch/flexmark/parser/$InlineParserOptions"

export interface $LightInlineParser {

 "matcher"(arg0: $Pattern$Type): $Matcher
 "sp"(): boolean
 "match"(arg0: $Pattern$Type): $BasedSequence
 "peek"(): character
 "peek"(arg0: integer): character
 "getIndex"(): integer
 "setIndex"(arg0: integer): void
 "appendText"(arg0: $BasedSequence$Type): void
 "appendText"(arg0: $BasedSequence$Type, arg1: integer, arg2: integer): void
 "setInput"(arg0: $BasedSequence$Type): void
 "getInput"(): $BasedSequence
 "getBlock"(): $Node
 "getOptions"(): $InlineParserOptions
 "appendNode"(arg0: $Node$Type): void
 "setBlock"(arg0: $Node$Type): void
 "getDocument"(): $Document
 "getParsing"(): $Parsing
 "setDocument"(arg0: $Document$Type): void
 "toEOL"(): $BasedSequence
 "getCurrentText"(): $ArrayList<($BasedSequence)>
 "appendSeparateText"(arg0: $BasedSequence$Type): $Text
 "flushTextNode"(): boolean
 "matchWithGroups"(arg0: $Pattern$Type): ($BasedSequence)[]
 "moveNodes"(arg0: $Node$Type, arg1: $Node$Type): void
 "spnl"(): boolean
 "nonIndentSp"(): boolean
 "spnlUrl"(): boolean
}

export namespace $LightInlineParser {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LightInlineParser$Type = ($LightInlineParser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LightInlineParser_ = $LightInlineParser$Type;
}}
declare module "packages/com/vladsch/flexmark/util/ast/$ContentNode" {
import {$AstNode, $AstNode$Type} from "packages/com/vladsch/flexmark/util/visitor/$AstNode"
import {$Content, $Content$Type} from "packages/com/vladsch/flexmark/util/ast/$Content"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Node, $Node$Type} from "packages/com/vladsch/flexmark/util/ast/$Node"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"
import {$BlockContent, $BlockContent$Type} from "packages/com/vladsch/flexmark/util/ast/$BlockContent"

export class $ContentNode extends $Node implements $Content {
static readonly "EMPTY_SEGMENTS": ($BasedSequence)[]
static readonly "SPLICE": string
static readonly "AST_ADAPTER": $AstNode<($Node)>

constructor(arg0: $BlockContent$Type)
constructor(arg0: $List$Type<($BasedSequence$Type)>)
constructor(arg0: $BasedSequence$Type, arg1: $List$Type<($BasedSequence$Type)>)
constructor(arg0: $BasedSequence$Type)
constructor()

public "getContentChars"(arg0: integer, arg1: integer): $BasedSequence
public "getContentChars"(): $BasedSequence
public "getContentLines"(): $List<($BasedSequence)>
public "getContentLines"(arg0: integer, arg1: integer): $List<($BasedSequence)>
public "setContentLines"(arg0: $List$Type<($BasedSequence$Type)>): void
public "getLineChars"(arg0: integer): $BasedSequence
public "getSpanningChars"(): $BasedSequence
public "setContentLine"(arg0: integer, arg1: $BasedSequence$Type): void
public "setContent"(arg0: $BasedSequence$Type, arg1: $List$Type<($BasedSequence$Type)>): void
public "setContent"(arg0: $List$Type<($BasedSequence$Type)>): void
public "setContent"(arg0: $BlockContent$Type): void
public "getLineCount"(): integer
get "contentChars"(): $BasedSequence
get "contentLines"(): $List<($BasedSequence)>
set "contentLines"(value: $List$Type<($BasedSequence$Type)>)
get "spanningChars"(): $BasedSequence
set "content"(value: $List$Type<($BasedSequence$Type)>)
set "content"(value: $BlockContent$Type)
get "lineCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContentNode$Type = ($ContentNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContentNode_ = $ContentNode$Type;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/builder/$SequenceBuilder" {
import {$Range, $Range$Type} from "packages/com/vladsch/flexmark/util/sequence/$Range"
import {$BasedSegmentBuilder, $BasedSegmentBuilder$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$BasedSegmentBuilder"
import {$ISequenceBuilder, $ISequenceBuilder$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$ISequenceBuilder"
import {$SegmentOptimizer, $SegmentOptimizer$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$SegmentOptimizer"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"
import {$CharPredicate, $CharPredicate$Type} from "packages/com/vladsch/flexmark/util/misc/$CharPredicate"

export class $SequenceBuilder implements $ISequenceBuilder<($SequenceBuilder), ($BasedSequence)> {


public "length"(): integer
public "toString"(): string
public "append"(arg0: integer, arg1: integer): $SequenceBuilder
public "append"(arg0: $Range$Type): $SequenceBuilder
public "append"(arg0: character, arg1: integer): $SequenceBuilder
public "charAt"(arg0: integer): character
public "addRange"(arg0: $Range$Type): $SequenceBuilder
public "getBuilder"(): $SequenceBuilder
public "getBaseSequence"(): $BasedSequence
public "toSequence"(arg0: $BasedSequence$Type, arg1: $CharPredicate$Type, arg2: $CharPredicate$Type): $BasedSequence
public "toSequence"(arg0: $BasedSequence$Type): $BasedSequence
public static "emptyBuilder"(arg0: $BasedSequence$Type, arg1: $SegmentOptimizer$Type): $SequenceBuilder
public static "emptyBuilder"(arg0: $BasedSequence$Type): $SequenceBuilder
public static "emptyBuilder"(arg0: $BasedSequence$Type, arg1: integer): $SequenceBuilder
public static "emptyBuilder"(arg0: $BasedSequence$Type, arg1: integer, arg2: $SegmentOptimizer$Type): $SequenceBuilder
public "getSegmentBuilder"(): $BasedSegmentBuilder
public "toStringNoAddedSpaces"(): string
public "getSingleBasedSequence"(): $BasedSequence
public "toStringWithRanges"(): string
public "toStringWithRanges"(arg0: boolean): string
public "addByLength"(arg0: integer, arg1: integer): $SequenceBuilder
public "getLastRangeOrNull"(): $Range
public "addByOffsets"(arg0: integer, arg1: integer): $SequenceBuilder
public "toSequenceByIndex"(arg0: $BasedSequence$Type, arg1: $CharPredicate$Type, arg2: $CharPredicate$Type): $BasedSequence
public "add"(arg0: charseq): $SequenceBuilder
public "append"(arg0: charseq, arg1: integer): $SequenceBuilder
public "append"(arg0: $Iterable$Type<(any)>): $SequenceBuilder
public "isEmpty"(): boolean
public "addAll"(arg0: $Iterable$Type<(any)>): $SequenceBuilder
public "isNotEmpty"(): boolean
get "builder"(): $SequenceBuilder
get "baseSequence"(): $BasedSequence
get "segmentBuilder"(): $BasedSegmentBuilder
get "singleBasedSequence"(): $BasedSequence
get "lastRangeOrNull"(): $Range
get "empty"(): boolean
get "notEmpty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SequenceBuilder$Type = ($SequenceBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SequenceBuilder_ = $SequenceBuilder$Type;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/builder/tree/$SegmentTreeRange" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SegmentTreeRange {
readonly "startIndex": integer
readonly "endIndex": integer
readonly "startOffset": integer
readonly "endOffset": integer
readonly "startPos": integer
readonly "endPos": integer
readonly "length": integer

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer)

public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SegmentTreeRange$Type = ($SegmentTreeRange);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SegmentTreeRange_ = $SegmentTreeRange$Type;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/builder/$SegmentedSequenceStats" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$SegmentedSequenceStats$StatsEntry, $SegmentedSequenceStats$StatsEntry$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$SegmentedSequenceStats$StatsEntry"

export class $SegmentedSequenceStats {


public "clear"(): void
public static "getInstance"(): $SegmentedSequenceStats
public "getCount"(arg0: integer): integer
public "getAggregatedStatsText"(): string
public "getStats"(): $List<($SegmentedSequenceStats$StatsEntry)>
public "addStats"(arg0: integer, arg1: integer, arg2: integer): void
public "getStatsText"(arg0: $List$Type<($SegmentedSequenceStats$StatsEntry$Type)>): string
public "getStatsText"(): string
public "getAggregatedStats"(): $List<($SegmentedSequenceStats$StatsEntry)>
get "instance"(): $SegmentedSequenceStats
get "aggregatedStatsText"(): string
get "stats"(): $List<($SegmentedSequenceStats$StatsEntry)>
get "statsText"(): string
get "aggregatedStats"(): $List<($SegmentedSequenceStats$StatsEntry)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SegmentedSequenceStats$Type = ($SegmentedSequenceStats);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SegmentedSequenceStats_ = $SegmentedSequenceStats$Type;
}}
declare module "packages/com/vladsch/flexmark/parser/block/$CustomBlockParserFactory" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Dependent, $Dependent$Type} from "packages/com/vladsch/flexmark/util/dependency/$Dependent"
import {$DataHolder, $DataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$DataHolder"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$BlockParserFactory, $BlockParserFactory$Type} from "packages/com/vladsch/flexmark/parser/block/$BlockParserFactory"
import {$SpecialLeadInHandler, $SpecialLeadInHandler$Type} from "packages/com/vladsch/flexmark/util/sequence/mappers/$SpecialLeadInHandler"

export interface $CustomBlockParserFactory extends $Function<($DataHolder), ($BlockParserFactory)>, $Dependent {

 "apply"(arg0: $DataHolder$Type): $BlockParserFactory
 "getLeadInHandler"(arg0: $DataHolder$Type): $SpecialLeadInHandler
 "compose"<V>(arg0: $Function$Type<(any), (any)>): $Function<(V), ($BlockParserFactory)>
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $Function<($DataHolder), (V)>
 "getAfterDependents"(): $Set<($Class<(any)>)>
 "affectsGlobalScope"(): boolean
 "getBeforeDependents"(): $Set<($Class<(any)>)>
}

export namespace $CustomBlockParserFactory {
function identity<T>(): $Function<($DataHolder), ($DataHolder)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomBlockParserFactory$Type = ($CustomBlockParserFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomBlockParserFactory_ = $CustomBlockParserFactory$Type;
}}
declare module "packages/com/vladsch/flexmark/util/ast/$Visitor" {
import {$Node, $Node$Type} from "packages/com/vladsch/flexmark/util/ast/$Node"
import {$AstAction, $AstAction$Type} from "packages/com/vladsch/flexmark/util/visitor/$AstAction"

export interface $Visitor<N extends $Node> extends $AstAction<(N)> {

 "visit"(arg0: N): void

(arg0: N): void
}

export namespace $Visitor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Visitor$Type<N> = ($Visitor<(N)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Visitor_<N> = $Visitor$Type<(N)>;
}}
declare module "packages/com/vladsch/flexmark/parser/$LinkRefProcessor" {
import {$Document, $Document$Type} from "packages/com/vladsch/flexmark/util/ast/$Document"
import {$Node, $Node$Type} from "packages/com/vladsch/flexmark/util/ast/$Node"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"

export interface $LinkRefProcessor {

 "createNode"(arg0: $BasedSequence$Type): $Node
 "isMatch"(arg0: $BasedSequence$Type): boolean
 "getWantExclamationPrefix"(): boolean
 "getBracketNestingLevel"(): integer
 "adjustInlineText"(arg0: $Document$Type, arg1: $Node$Type): $BasedSequence
 "updateNodeElements"(arg0: $Document$Type, arg1: $Node$Type): void
 "allowDelimiters"(arg0: $BasedSequence$Type, arg1: $Document$Type, arg2: $Node$Type): boolean
}

export namespace $LinkRefProcessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LinkRefProcessor$Type = ($LinkRefProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LinkRefProcessor_ = $LinkRefProcessor$Type;
}}
declare module "packages/com/vladsch/flexmark/parser/$InlineParserFactory" {
import {$DelimiterProcessor, $DelimiterProcessor$Type} from "packages/com/vladsch/flexmark/parser/delimiter/$DelimiterProcessor"
import {$DataHolder, $DataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$DataHolder"
import {$List, $List$Type} from "packages/java/util/$List"
import {$InlineParser, $InlineParser$Type} from "packages/com/vladsch/flexmark/parser/$InlineParser"
import {$LinkRefProcessorData, $LinkRefProcessorData$Type} from "packages/com/vladsch/flexmark/parser/internal/$LinkRefProcessorData"
import {$BitSet, $BitSet$Type} from "packages/java/util/$BitSet"
import {$InlineParserExtensionFactory, $InlineParserExtensionFactory$Type} from "packages/com/vladsch/flexmark/parser/$InlineParserExtensionFactory"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $InlineParserFactory {

 "inlineParser"(arg0: $DataHolder$Type, arg1: $BitSet$Type, arg2: $BitSet$Type, arg3: $Map$Type<(character), ($DelimiterProcessor$Type)>, arg4: $LinkRefProcessorData$Type, arg5: $List$Type<($InlineParserExtensionFactory$Type)>): $InlineParser

(arg0: $DataHolder$Type, arg1: $BitSet$Type, arg2: $BitSet$Type, arg3: $Map$Type<(character), ($DelimiterProcessor$Type)>, arg4: $LinkRefProcessorData$Type, arg5: $List$Type<($InlineParserExtensionFactory$Type)>): $InlineParser
}

export namespace $InlineParserFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InlineParserFactory$Type = ($InlineParserFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InlineParserFactory_ = $InlineParserFactory$Type;
}}
declare module "packages/com/vladsch/flexmark/parser/block/$CharacterNodeFactory" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Node, $Node$Type} from "packages/com/vladsch/flexmark/util/ast/$Node"

export interface $CharacterNodeFactory extends $Supplier<($Node)> {

 "skipNext"(arg0: character): boolean
 "skipPrev"(arg0: character): boolean
 "wantSkippedWhitespace"(): boolean
 "get"(): $Node
}

export namespace $CharacterNodeFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CharacterNodeFactory$Type = ($CharacterNodeFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CharacterNodeFactory_ = $CharacterNodeFactory$Type;
}}
declare module "packages/com/vladsch/flexmark/util/data/$DataSet" {
import {$DataHolder, $DataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$DataHolder"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$DataKeyAggregator, $DataKeyAggregator$Type} from "packages/com/vladsch/flexmark/util/data/$DataKeyAggregator"
import {$DataValueFactory, $DataValueFactory$Type} from "packages/com/vladsch/flexmark/util/data/$DataValueFactory"
import {$MutableDataSet, $MutableDataSet$Type} from "packages/com/vladsch/flexmark/util/data/$MutableDataSet"
import {$MutableDataHolder, $MutableDataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$MutableDataHolder"
import {$DataKey, $DataKey$Type} from "packages/com/vladsch/flexmark/util/data/$DataKey"
import {$DataKeyBase, $DataKeyBase$Type} from "packages/com/vladsch/flexmark/util/data/$DataKeyBase"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $DataSet implements $DataHolder {

constructor()
constructor(arg0: $DataHolder$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "contains"(arg0: $DataKeyBase$Type<(any)>): boolean
public static "merge"(...arg0: ($DataHolder$Type)[]): $DataSet
public "getAll"(): $Map<(any), (any)>
public "getKeys"(): $Collection<(any)>
public "aggregate"(): $DataHolder
public static "aggregate"(arg0: $DataHolder$Type, arg1: $DataHolder$Type): $DataHolder
public "toMutable"(): $MutableDataSet
public static "registerDataKeyAggregator"(arg0: $DataKeyAggregator$Type): void
public static "aggregateActions"(arg0: $DataHolder$Type, arg1: $DataHolder$Type): $DataHolder
public "toDataSet"(): $DataSet
public "getOrCompute"(arg0: $DataKeyBase$Type<(any)>, arg1: $DataValueFactory$Type<(any)>): any
/**
 * 
 * @deprecated
 */
public "get"<T>(arg0: $DataKey$Type<(T)>): T
public "setIn"(arg0: $MutableDataHolder$Type): $MutableDataHolder
get "all"(): $Map<(any), (any)>
get "keys"(): $Collection<(any)>
set "in"(value: $MutableDataHolder$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataSet$Type = ($DataSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataSet_ = $DataSet$Type;
}}
declare module "packages/com/vladsch/flexmark/util/ast/$Block" {
import {$AstNode, $AstNode$Type} from "packages/com/vladsch/flexmark/util/visitor/$AstNode"
import {$ContentNode, $ContentNode$Type} from "packages/com/vladsch/flexmark/util/ast/$ContentNode"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Node, $Node$Type} from "packages/com/vladsch/flexmark/util/ast/$Node"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"
import {$BlockContent, $BlockContent$Type} from "packages/com/vladsch/flexmark/util/ast/$BlockContent"

export class $Block extends $ContentNode {
static readonly "EMPTY_SEGMENTS": ($BasedSequence)[]
static readonly "SPLICE": string
static readonly "AST_ADAPTER": $AstNode<($Node)>

constructor(arg0: $BlockContent$Type)
constructor(arg0: $List$Type<($BasedSequence$Type)>)
constructor(arg0: $BasedSequence$Type, arg1: $List$Type<($BasedSequence$Type)>)
constructor(arg0: $BasedSequence$Type)
constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Block$Type = ($Block);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Block_ = $Block$Type;
}}
declare module "packages/com/vladsch/flexmark/util/visitor/$AstNode" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $AstNode<N> {

 "getNext"(arg0: N): N
 "getFirstChild"(arg0: N): N
}

export namespace $AstNode {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AstNode$Type<N> = ($AstNode<(N)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AstNode_<N> = $AstNode$Type<(N)>;
}}
declare module "packages/com/vladsch/flexmark/util/misc/$Paired" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export interface $Paired<K, V> extends $Map$Entry<(K), (V)> {

 "getFirst"(): K
 "getSecond"(): V
 "component2"(): V
 "component1"(): K
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "getValue"(): V
 "getKey"(): K
 "setValue"(arg0: V): V
}

export namespace $Paired {
function copyOf<K, V>(arg0: $Map$Entry$Type<(any), (any)>): $Map$Entry<(K), (V)>
function comparingByKey<K, V>(arg0: $Comparator$Type<(any)>): $Comparator<($Map$Entry<(K), (V)>)>
function comparingByKey<K, V>(): $Comparator<($Map$Entry<(K), (V)>)>
function comparingByValue<K, V>(): $Comparator<($Map$Entry<(K), (V)>)>
function comparingByValue<K, V>(arg0: $Comparator$Type<(any)>): $Comparator<($Map$Entry<(K), (V)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Paired$Type<K, V> = ($Paired<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Paired_<K, V> = $Paired$Type<(K), (V)>;
}}
declare module "packages/com/vladsch/flexmark/parser/$InlineParser" {
import {$Document, $Document$Type} from "packages/com/vladsch/flexmark/util/ast/$Document"
import {$LightInlineParser, $LightInlineParser$Type} from "packages/com/vladsch/flexmark/parser/$LightInlineParser"
import {$Parsing, $Parsing$Type} from "packages/com/vladsch/flexmark/ast/util/$Parsing"
import {$BitSet, $BitSet$Type} from "packages/java/util/$BitSet"
import {$Delimiter, $Delimiter$Type} from "packages/com/vladsch/flexmark/parser/core/delimiter/$Delimiter"
import {$Bracket, $Bracket$Type} from "packages/com/vladsch/flexmark/parser/core/delimiter/$Bracket"
import {$Matcher, $Matcher$Type} from "packages/java/util/regex/$Matcher"
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$Text, $Text$Type} from "packages/com/vladsch/flexmark/ast/$Text"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$Node, $Node$Type} from "packages/com/vladsch/flexmark/util/ast/$Node"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"
import {$InlineParserOptions, $InlineParserOptions$Type} from "packages/com/vladsch/flexmark/parser/$InlineParserOptions"
import {$CharacterNodeFactory, $CharacterNodeFactory$Type} from "packages/com/vladsch/flexmark/parser/block/$CharacterNodeFactory"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $InlineParser extends $LightInlineParser {

 "parse"(arg0: $BasedSequence$Type, arg1: $Node$Type): void
 "removeDelimiterAndNode"(arg0: $Delimiter$Type): void
 "removeDelimitersBetween"(arg0: $Delimiter$Type, arg1: $Delimiter$Type): void
 "parseLinkDestination"(): $BasedSequence
 "removeDelimiterKeepNode"(arg0: $Delimiter$Type): void
 "getLastBracket"(): $Bracket
 "getLastDelimiter"(): $Delimiter
 "finalizeDocument"(arg0: $Document$Type): void
 "mergeTextNodes"(arg0: $Node$Type, arg1: $Node$Type): void
 "initializeDocument"(arg0: $Document$Type): void
 "parseCustom"(arg0: $BasedSequence$Type, arg1: $Node$Type, arg2: $BitSet$Type, arg3: $Map$Type<(character), ($CharacterNodeFactory$Type)>): $List<($Node)>
 "toEOL"(): $BasedSequence
 "processDelimiters"(arg0: $Delimiter$Type): void
 "removeDelimiter"(arg0: $Delimiter$Type): void
 "parseLinkLabel"(): integer
 "parseNewline"(): boolean
 "parseAutolink"(): boolean
 "parseHtmlInline"(): boolean
 "parseEntity"(): boolean
 "mergeIfNeeded"(arg0: $Text$Type, arg1: $Text$Type): void
 "parseLinkTitle"(): $BasedSequence
 "matcher"(arg0: $Pattern$Type): $Matcher
 "sp"(): boolean
 "match"(arg0: $Pattern$Type): $BasedSequence
 "peek"(): character
 "peek"(arg0: integer): character
 "getIndex"(): integer
 "setIndex"(arg0: integer): void
 "appendText"(arg0: $BasedSequence$Type): void
 "appendText"(arg0: $BasedSequence$Type, arg1: integer, arg2: integer): void
 "setInput"(arg0: $BasedSequence$Type): void
 "getInput"(): $BasedSequence
 "getBlock"(): $Node
 "getOptions"(): $InlineParserOptions
 "appendNode"(arg0: $Node$Type): void
 "setBlock"(arg0: $Node$Type): void
 "getDocument"(): $Document
 "getParsing"(): $Parsing
 "setDocument"(arg0: $Document$Type): void
 "getCurrentText"(): $ArrayList<($BasedSequence)>
 "appendSeparateText"(arg0: $BasedSequence$Type): $Text
 "flushTextNode"(): boolean
 "matchWithGroups"(arg0: $Pattern$Type): ($BasedSequence)[]
 "moveNodes"(arg0: $Node$Type, arg1: $Node$Type): void
 "spnl"(): boolean
 "nonIndentSp"(): boolean
 "spnlUrl"(): boolean
}

export namespace $InlineParser {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InlineParser$Type = ($InlineParser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InlineParser_ = $InlineParser$Type;
}}
declare module "packages/com/vladsch/flexmark/util/data/$DataValueFactory" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$DataHolder, $DataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$DataHolder"

export interface $DataValueFactory<T> extends $Function<($DataHolder), (T)> {

 "apply"(arg0: $DataHolder$Type): T
 "compose"<V>(arg0: $Function$Type<(any), (any)>): $Function<(V), ($DataHolder)>
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $Function<($DataHolder), (V)>

(arg0: $DataHolder$Type): T
}

export namespace $DataValueFactory {
function identity<T>(): $Function<($DataHolder), ($DataHolder)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataValueFactory$Type<T> = ($DataValueFactory<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataValueFactory_<T> = $DataValueFactory$Type<(T)>;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/$SequenceUtils" {
import {$Range, $Range$Type} from "packages/com/vladsch/flexmark/util/sequence/$Range"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Pair, $Pair$Type} from "packages/com/vladsch/flexmark/util/misc/$Pair"
import {$CharPredicate, $CharPredicate$Type} from "packages/com/vladsch/flexmark/util/misc/$CharPredicate"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $SequenceUtils {

}

export namespace $SequenceUtils {
const EOL: string
const SPACE: string
const ANY_EOL: string
const EOL_CHAR: character
const EOL_CHAR1: character
const EOL_CHAR2: character
const SPC: character
const NUL: character
const ENC_NUL: character
const NBSP: character
const LS: character
const US: character
const LINE_SEP: string
const SPACE_TAB: string
const SPACE_EOL: string
const US_CHARS: string
const WHITESPACE: string
const NBSP_CHARS: string
const WHITESPACE_NBSP: string
const SPACE_SET: $CharPredicate
const TAB_SET: $CharPredicate
const EOL_SET: $CharPredicate
const SPACE_TAB_SET: $CharPredicate
const SPACE_TAB_NBSP_SET: $CharPredicate
const SPACE_TAB_EOL_SET: $CharPredicate
const SPACE_EOL_SET: $CharPredicate
const ANY_EOL_SET: $CharPredicate
const WHITESPACE_SET: $CharPredicate
const WHITESPACE_NBSP_SET: $CharPredicate
const BACKSLASH_SET: $CharPredicate
const US_SET: $CharPredicate
const HASH_SET: $CharPredicate
const DECIMAL_DIGITS: $CharPredicate
const HEXADECIMAL_DIGITS: $CharPredicate
const OCTAL_DIGITS: $CharPredicate
const LSEP: character
const EOL_CHARS: string
const WHITESPACE_NO_EOL_CHARS: string
const WHITESPACE_CHARS: string
const WHITESPACE_NBSP_CHARS: string
const SPLIT_INCLUDE_DELIMS: integer
const SPLIT_TRIM_PARTS: integer
const SPLIT_SKIP_EMPTY: integer
const SPLIT_INCLUDE_DELIM_PARTS: integer
const SPLIT_TRIM_SKIP_EMPTY: integer
const visibleSpacesMap: $Map<(character), (string)>
const EMPTY_INDICES: (integer)[]
function toVisibleWhitespaceString(arg0: charseq): string
function equals(arg0: charseq, arg1: any): boolean
function hashCode(arg0: charseq): integer
function indexOf(arg0: charseq, arg1: charseq, arg2: integer): integer
function indexOf(arg0: charseq, arg1: character, arg2: integer): integer
function indexOf(arg0: charseq, arg1: charseq): integer
function indexOf(arg0: charseq, arg1: character): integer
function indexOf(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function indexOf(arg0: charseq, arg1: character, arg2: integer, arg3: integer): integer
function compare(arg0: charseq, arg1: charseq, arg2: boolean): integer
function compare(arg0: charseq, arg1: charseq): integer
function compare(arg0: charseq, arg1: charseq, arg2: boolean, arg3: $CharPredicate$Type): integer
function startsWith(arg0: charseq, arg1: charseq, arg2: boolean): boolean
function startsWith(arg0: charseq, arg1: $CharPredicate$Type): boolean
function startsWith(arg0: charseq, arg1: charseq): boolean
function lastIndexOf(arg0: charseq, arg1: charseq, arg2: integer): integer
function lastIndexOf(arg0: charseq, arg1: charseq): integer
function lastIndexOf(arg0: charseq, arg1: character, arg2: integer): integer
function lastIndexOf(arg0: charseq, arg1: character, arg2: integer, arg3: integer): integer
function lastIndexOf(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function lastIndexOf(arg0: charseq, arg1: character): integer
function isEmpty(arg0: charseq): boolean
function matches(arg0: charseq, arg1: charseq): boolean
function matches(arg0: charseq, arg1: charseq, arg2: boolean): boolean
function split<T>(arg0: T, arg1: (T)[], arg2: charseq, arg3: integer, arg4: integer, arg5: $CharPredicate$Type): (T)[]
function split<T>(arg0: T, arg1: (T)[], arg2: charseq, arg3: integer, arg4: integer): (T)[]
function split<T>(arg0: T, arg1: (T)[], arg2: charseq, arg3: integer, arg4: boolean, arg5: $CharPredicate$Type): (T)[]
function split<T>(arg0: T, arg1: (T)[], arg2: charseq, arg3: boolean, arg4: $CharPredicate$Type): (T)[]
function split<T>(arg0: T, arg1: (T)[], arg2: charseq): (T)[]
function trim<T>(arg0: T, arg1: integer): T
function trim<T>(arg0: T): T
function trim<T>(arg0: T, arg1: $CharPredicate$Type): T
function trim<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): T
function isBlank(arg0: charseq): boolean
function endsWith(arg0: charseq, arg1: charseq, arg2: boolean): boolean
function endsWith(arg0: charseq, arg1: $CharPredicate$Type): boolean
function endsWith(arg0: charseq, arg1: charseq): boolean
function subSequence<T>(arg0: T, arg1: integer): T
function subSequence<T>(arg0: T, arg1: $Range$Type): T
function lastChar(arg0: charseq): character
function firstChar(arg0: charseq): character
function expandTo(arg0: (integer)[], arg1: integer, arg2: integer): (integer)[]
function padStart(arg0: charseq, arg1: integer): string
function padStart(arg0: charseq, arg1: integer, arg2: character): string
function trimmed<T>(arg0: T): $Pair<(T), (T)>
function trimmed<T>(arg0: T, arg1: integer): $Pair<(T), (T)>
function trimmed<T>(arg0: T, arg1: $CharPredicate$Type): $Pair<(T), (T)>
function trimmed<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): $Pair<(T), (T)>
function parseIntOrDefault(arg0: string, arg1: integer): integer
function parseIntOrDefault(arg0: string, arg1: integer, arg2: integer): integer
function lineColumnAtIndex(arg0: charseq, arg1: integer): $Pair<(integer), (integer)>
function containsAny(arg0: charseq, arg1: $CharPredicate$Type): boolean
function containsAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): boolean
function indexOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function indexOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function indexOfAny(arg0: charseq, arg1: $CharPredicate$Type): integer
function lastIndexOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function lastIndexOfAny(arg0: charseq, arg1: $CharPredicate$Type): integer
function lastIndexOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function matchChars(arg0: charseq, arg1: charseq, arg2: integer): boolean
function matchChars(arg0: charseq, arg1: charseq, arg2: integer, arg3: boolean): boolean
function matchChars(arg0: charseq, arg1: charseq): boolean
function matchChars(arg0: charseq, arg1: charseq, arg2: boolean): boolean
function endOfLine(arg0: charseq, arg1: integer): integer
function validateIndex(arg0: integer, arg1: integer): void
function trimStart<T>(arg0: T, arg1: $CharPredicate$Type): T
function trimStart<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): T
function trimStart<T>(arg0: T): T
function trimStart<T>(arg0: T, arg1: integer): T
function toStringArray(...arg0: (charseq)[]): (string)[]
function countTrailingSpaceTab(arg0: charseq, arg1: integer, arg2: integer): integer
function countTrailingSpaceTab(arg0: charseq, arg1: integer): integer
function countTrailingSpaceTab(arg0: charseq): integer
function countTrailingNotSpaceTab(arg0: charseq, arg1: integer, arg2: integer): integer
function countTrailingNotSpaceTab(arg0: charseq, arg1: integer): integer
function countTrailingNotSpaceTab(arg0: charseq): integer
function countLeadingWhitespace(arg0: charseq, arg1: integer): integer
function countLeadingWhitespace(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingWhitespace(arg0: charseq): integer
function countTrailingWhitespace(arg0: charseq, arg1: integer): integer
function countTrailingWhitespace(arg0: charseq): integer
function countTrailingWhitespace(arg0: charseq, arg1: integer, arg2: integer): integer
function countOfNotWhitespace(arg0: charseq): integer
function countLeadingSpaceTab(arg0: charseq, arg1: integer): integer
function countLeadingSpaceTab(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingSpaceTab(arg0: charseq): integer
function countLeadingColumns(arg0: charseq, arg1: integer, arg2: $CharPredicate$Type): integer
function countLeadingNotSpaceTab(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingNotSpaceTab(arg0: charseq, arg1: integer): integer
function countLeadingNotSpaceTab(arg0: charseq): integer
function countTrailingNotWhitespace(arg0: charseq): integer
function countTrailingNotWhitespace(arg0: charseq, arg1: integer): integer
function countTrailingNotWhitespace(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingNotSpace(arg0: charseq, arg1: integer): integer
function countLeadingNotSpace(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingNotSpace(arg0: charseq): integer
function countTrailingNotSpace(arg0: charseq, arg1: integer, arg2: integer): integer
function countTrailingNotSpace(arg0: charseq, arg1: integer): integer
function countTrailingNotSpace(arg0: charseq): integer
function countLeadingNotWhitespace(arg0: charseq): integer
function countLeadingNotWhitespace(arg0: charseq, arg1: integer): integer
function countLeadingNotWhitespace(arg0: charseq, arg1: integer, arg2: integer): integer
function leadingBlankLinesRange(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): $Range
function leadingBlankLinesRange(arg0: charseq): $Range
function leadingBlankLinesRange(arg0: charseq, arg1: integer, arg2: integer): $Range
function leadingBlankLinesRange(arg0: charseq, arg1: integer): $Range
function endOfDelimitedByAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function getVisibleSpacesMap(): $Map<(character), (string)>
function matchedCharCountIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer): integer
function matchedCharCountIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function trailingBlankLinesRange(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): $Range
function trailingBlankLinesRange(arg0: charseq, arg1: integer, arg2: integer): $Range
function trailingBlankLinesRange(arg0: charseq, arg1: integer): $Range
function trailingBlankLinesRange(arg0: charseq): $Range
function isVisibleWhitespace(arg0: character): boolean
function columnsToNextTabStop(arg0: integer): integer
function startsWithWhitespace(arg0: charseq): boolean
function validateIndexInclusiveEnd(arg0: integer, arg1: integer): void
function parseUnsignedIntOrNull(arg0: string): integer
function parseUnsignedIntOrNull(arg0: string, arg1: integer): integer
function startOfDelimitedByAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function parseNumberPrefixOrNull(arg0: string, arg1: $Predicate$Type<(string)>): $Pair<(number), (string)>
function subSequenceBeforeAfter<T>(arg0: T, arg1: $Range$Type): $Pair<(T), (T)>
function parseUnsignedIntOrDefault(arg0: string, arg1: integer): integer
function parseUnsignedIntOrDefault(arg0: string, arg1: integer, arg2: integer): integer
function matchCharsReversedIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer): boolean
function endOfDelimitedByAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function blankLinesRemovedRanges(arg0: charseq, arg1: integer): $List<($Range)>
function blankLinesRemovedRanges(arg0: charseq): $List<($Range)>
function blankLinesRemovedRanges(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): $List<($Range)>
function blankLinesRemovedRanges(arg0: charseq, arg1: integer, arg2: integer): $List<($Range)>
function matchedCharCountReversed(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function matchedCharCountReversed(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer, arg4: boolean): integer
function matchedCharCountReversed(arg0: charseq, arg1: charseq, arg2: integer, arg3: boolean): integer
function matchedCharCountReversed(arg0: charseq, arg1: charseq, arg2: integer): integer
function matchCharsIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer): boolean
function matchCharsIgnoreCase(arg0: charseq, arg1: charseq): boolean
function matchedCharCountReversedIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function matchedCharCountReversedIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer): integer
function startOfDelimitedByAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function splitList<T>(arg0: T, arg1: charseq, arg2: integer, arg3: integer): $List<(T)>
function splitList<T>(arg0: T, arg1: charseq, arg2: boolean, arg3: $CharPredicate$Type): $List<(T)>
function splitList<T>(arg0: T, arg1: charseq, arg2: integer, arg3: boolean, arg4: $CharPredicate$Type): $List<(T)>
function splitList<T>(arg0: T, arg1: charseq, arg2: integer, arg3: integer, arg4: $CharPredicate$Type): $List<(T)>
function splitList<T>(arg0: T, arg1: charseq): $List<(T)>
function isNotBlank(arg0: charseq): boolean
function isNotEmpty(arg0: charseq): boolean
function padEnd(arg0: charseq, arg1: integer): string
function padEnd(arg0: charseq, arg1: integer, arg2: character): string
function trimEnd<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): T
function trimEnd<T>(arg0: T): T
function trimEnd<T>(arg0: T, arg1: integer): T
function trimEnd<T>(arg0: T, arg1: $CharPredicate$Type): T
function startOfLine(arg0: charseq, arg1: integer): integer
function countLeading(arg0: charseq, arg1: $CharPredicate$Type): integer
function countLeading(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countLeading(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function subSequenceBefore<T>(arg0: T, arg1: $Range$Type): T
function indexOfNot(arg0: charseq, arg1: character, arg2: integer, arg3: integer): integer
function indexOfNot(arg0: charseq, arg1: character, arg2: integer): integer
function indexOfNot(arg0: charseq, arg1: character): integer
function countLeadingNot(arg0: charseq, arg1: $CharPredicate$Type): integer
function countLeadingNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countLeadingNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function subSequenceAfter<T>(arg0: T, arg1: $Range$Type): T
function lastIndexOfNot(arg0: charseq, arg1: character, arg2: integer): integer
function lastIndexOfNot(arg0: charseq, arg1: character): integer
function lastIndexOfNot(arg0: charseq, arg1: character, arg2: integer, arg3: integer): integer
function lastIndexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function lastIndexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function lastIndexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type): integer
function indexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type): integer
function indexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function indexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function safeCharAt(arg0: charseq, arg1: integer): character
function countOfWhitespace(arg0: charseq): integer
function countOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type): integer
function countOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function countLeadingSpace(arg0: charseq, arg1: integer): integer
function countLeadingSpace(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingSpace(arg0: charseq): integer
function countTrailingNot(arg0: charseq, arg1: $CharPredicate$Type): integer
function countTrailingNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function countTrailingNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countTrailing(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countTrailing(arg0: charseq, arg1: $CharPredicate$Type): integer
function countTrailing(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function countOfSpaceTab(arg0: charseq): integer
function countOfAny(arg0: charseq, arg1: $CharPredicate$Type): integer
function countOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function countTrailingSpace(arg0: charseq, arg1: integer): integer
function countTrailingSpace(arg0: charseq, arg1: integer, arg2: integer): integer
function countTrailingSpace(arg0: charseq): integer
function trimStartRange(arg0: charseq, arg1: integer): $Range
function trimStartRange(arg0: charseq, arg1: $CharPredicate$Type): $Range
function trimStartRange(arg0: charseq, arg1: integer, arg2: $CharPredicate$Type): $Range
function trimStartRange(arg0: charseq): $Range
function trimEndRange(arg0: charseq, arg1: $CharPredicate$Type): $Range
function trimEndRange(arg0: charseq): $Range
function trimEndRange(arg0: charseq, arg1: integer): $Range
function trimEndRange(arg0: charseq, arg1: integer, arg2: $CharPredicate$Type): $Range
function trimRange(arg0: charseq): $Range
function trimRange(arg0: charseq, arg1: integer): $Range
function trimRange(arg0: charseq, arg1: $CharPredicate$Type): $Range
function trimRange(arg0: charseq, arg1: integer, arg2: $CharPredicate$Type): $Range
function countOfNotSpaceTab(arg0: charseq): integer
function trimmedStart<T>(arg0: T, arg1: $CharPredicate$Type): T
function trimmedStart<T>(arg0: T): T
function trimmedStart<T>(arg0: T, arg1: integer): T
function trimmedStart<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): T
function trimmedEnd<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): T
function trimmedEnd<T>(arg0: T): T
function trimmedEnd<T>(arg0: T, arg1: $CharPredicate$Type): T
function trimmedEnd<T>(arg0: T, arg1: integer): T
function startOfDelimitedBy(arg0: charseq, arg1: charseq, arg2: integer): integer
function eolStartRange(arg0: charseq, arg1: integer): $Range
function trimmedEOL<T>(arg0: T): T
function endOfDelimitedBy(arg0: charseq, arg1: charseq, arg2: integer): integer
function startOfLineAnyEOL(arg0: charseq, arg1: integer): integer
function lineRangeAt(arg0: charseq, arg1: integer): $Range
function eolEndLength(arg0: charseq): integer
function eolEndLength(arg0: charseq, arg1: integer): integer
function eolEndRange(arg0: charseq, arg1: integer): $Range
function lineRangeAtAnyEOL(arg0: charseq, arg1: integer): $Range
function trimTailBlankLines<T>(arg0: T): T
function trimEOL<T>(arg0: T): T
function matchedCharCount(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer, arg4: boolean): integer
function matchedCharCount(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer, arg4: boolean, arg5: boolean): integer
function matchedCharCount(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function matchedCharCount(arg0: charseq, arg1: charseq, arg2: integer): integer
function matchedCharCount(arg0: charseq, arg1: charseq, arg2: integer, arg3: boolean): integer
function matchCharsReversed(arg0: charseq, arg1: charseq, arg2: integer): boolean
function matchCharsReversed(arg0: charseq, arg1: charseq, arg2: integer, arg3: boolean): boolean
function matchesIgnoreCase(arg0: charseq, arg1: charseq): boolean
function trimLeadBlankLines<T>(arg0: T): T
function startsWithSpaceTab(arg0: charseq): boolean
function endsWithWhitespace(arg0: charseq): boolean
function startsWithSpace(arg0: charseq): boolean
function endsWithSpace(arg0: charseq): boolean
function endsWithSpaceTab(arg0: charseq): boolean
function startsWithEOL(arg0: charseq): boolean
function endsWithAnyEOL(arg0: charseq): boolean
function startsWithAnyEOL(arg0: charseq): boolean
function endsWithEOL(arg0: charseq): boolean
function splitEOL<T>(arg0: T, arg1: (T)[], arg2: boolean): (T)[]
function splitEOL<T>(arg0: T, arg1: (T)[]): (T)[]
function splitListEOL<T>(arg0: T, arg1: boolean, arg2: $CharPredicate$Type): $List<(T)>
function splitListEOL<T>(arg0: T, arg1: boolean): $List<(T)>
function splitListEOL<T>(arg0: T): $List<(T)>
function indexOfAll(arg0: charseq, arg1: charseq): (integer)[]
function columnAtIndex(arg0: charseq, arg1: integer): integer
function compareReversed(arg0: charseq, arg1: charseq): integer
function containsAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): boolean
function containsAnyNot(arg0: charseq, arg1: $CharPredicate$Type): boolean
function containsAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): boolean
function truncateTo(arg0: (integer)[], arg1: integer): (integer)[]
function validateStartEnd(arg0: integer, arg1: integer, arg2: integer): void
function parseNumberOrNull(arg0: string): number
function parseLongOrNull(arg0: string, arg1: integer): long
function parseLongOrNull(arg0: string): long
function parseIntOrNull(arg0: string): integer
function parseIntOrNull(arg0: string, arg1: integer): integer
function containedBy(arg0: $Collection$Type<(any)>, arg1: charseq): boolean
function containedBy<T>(arg0: (T)[], arg1: charseq): boolean
function endOfLineAnyEOL(arg0: charseq, arg1: integer): integer
function eolStartLength(arg0: charseq, arg1: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SequenceUtils$Type = ($SequenceUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SequenceUtils_ = $SequenceUtils$Type;
}}
declare module "packages/com/vladsch/flexmark/util/visitor/$AstHandler" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$AstAction, $AstAction$Type} from "packages/com/vladsch/flexmark/util/visitor/$AstAction"

export class $AstHandler<N, A extends $AstAction<(any)>> {

constructor(arg0: $Class$Type<(any)>, arg1: A)

public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "getAdapter"(): A
public "getNodeType"(): $Class<(any)>
get "adapter"(): A
get "nodeType"(): $Class<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AstHandler$Type<N, A> = ($AstHandler<(N), (A)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AstHandler_<N, A> = $AstHandler$Type<(N), (A)>;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/builder/$SegmentBuilderBase" {
import {$Range, $Range$Type} from "packages/com/vladsch/flexmark/util/sequence/$Range"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$SegmentStats, $SegmentStats$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$SegmentStats"
import {$ISegmentBuilder, $ISegmentBuilder$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$ISegmentBuilder"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Seg, $Seg$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$Seg"

export class $SegmentBuilderBase<S extends $SegmentBuilderBase<(S)>> implements $ISegmentBuilder<(S)> {
static readonly "MIN_PART_CAPACITY": integer
static readonly "EMPTY_PARTS": (integer)[]


public "length"(): integer
public "toString"(arg0: charseq): string
public "toString"(): string
public "toString"(arg0: charseq, arg1: charseq, arg2: charseq, arg3: $Function$Type<(charseq), (charseq)>): string
public "append"(arg0: $Range$Type): S
public "append"(arg0: integer, arg1: integer): S
public "append"(arg0: charseq): S
public "append"(arg0: character): S
public "append"(arg0: character, arg1: integer): S
public "isEmpty"(): boolean
public "size"(): integer
public "iterator"(): $Iterator<(any)>
public "trimToSize"(): void
public "getText"(): charseq
public "getTextLength"(): integer
public "getSegments"(): $Iterable<($Seg)>
public "getOptions"(): integer
public "getEndOffset"(): integer
public "getStartOffset"(): integer
public "isTrackTextFirst256"(): boolean
public "getTextFirst256Length"(): integer
public "getEndOffsetIfNeeded"(): integer
public "getTextFirst256Segments"(): integer
public "getTextSpaceSegments"(): integer
public "isBaseSubSequenceRange"(): boolean
public "getStartOffsetIfNeeded"(): integer
public "getBaseSubSequenceRange"(): $Range
public "getStats"(): $SegmentStats
public "needStartOffset"(): boolean
public "needEndOffset"(): boolean
public "haveOffsets"(): boolean
public "noAnchorsSize"(): integer
public "isIncludeAnchors"(): boolean
public "getTextSegments"(): integer
public "getTextSpaceLength"(): integer
public "getSpan"(): integer
public "toStringPrep"(): string
public "toStringWithRanges"(arg0: charseq): string
public "getPart"(arg0: integer): any
public "appendAnchor"(arg0: integer): S
public "toStringWithRangesVisibleWhitespace"(arg0: charseq): string
public "spliterator"(): $Spliterator<(any)>
public "forEach"(arg0: $Consumer$Type<(any)>): void
[Symbol.iterator](): IterableIterator<any>;
get "empty"(): boolean
get "text"(): charseq
get "textLength"(): integer
get "segments"(): $Iterable<($Seg)>
get "options"(): integer
get "endOffset"(): integer
get "startOffset"(): integer
get "trackTextFirst256"(): boolean
get "textFirst256Length"(): integer
get "endOffsetIfNeeded"(): integer
get "textFirst256Segments"(): integer
get "textSpaceSegments"(): integer
get "baseSubSequenceRange"(): boolean
get "startOffsetIfNeeded"(): integer
get "baseSubSequenceRange"(): $Range
get "stats"(): $SegmentStats
get "includeAnchors"(): boolean
get "textSegments"(): integer
get "textSpaceLength"(): integer
get "span"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SegmentBuilderBase$Type<S> = ($SegmentBuilderBase<(S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SegmentBuilderBase_<S> = $SegmentBuilderBase$Type<(S)>;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/builder/$ISequenceBuilder" {
import {$Appendable, $Appendable$Type} from "packages/java/lang/$Appendable"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export interface $ISequenceBuilder<T extends $ISequenceBuilder<(T), (S)>, S extends charseq> extends $Appendable {

 "add"(arg0: charseq): T
 "length"(): integer
 "append"(arg0: charseq, arg1: integer): T
 "append"(arg0: charseq, arg1: integer, arg2: integer): T
 "append"(arg0: character, arg1: integer): T
 "append"(arg0: $Iterable$Type<(any)>): T
 "charAt"(arg0: integer): character
 "isEmpty"(): boolean
 "addAll"(arg0: $Iterable$Type<(any)>): T
 "getBuilder"(): T
 "toSequence"(): S
 "isNotEmpty"(): boolean
 "getSingleBasedSequence"(): S
}

export namespace $ISequenceBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ISequenceBuilder$Type<T, S> = ($ISequenceBuilder<(T), (S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ISequenceBuilder_<T, S> = $ISequenceBuilder$Type<(T), (S)>;
}}
declare module "packages/com/vladsch/flexmark/parser/block/$BlockParserTracker" {
import {$BlockParser, $BlockParser$Type} from "packages/com/vladsch/flexmark/parser/block/$BlockParser"

export interface $BlockParserTracker {

 "blockParserAdded"(arg0: $BlockParser$Type): void
 "blockParserRemoved"(arg0: $BlockParser$Type): void
}

export namespace $BlockParserTracker {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockParserTracker$Type = ($BlockParserTracker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockParserTracker_ = $BlockParserTracker$Type;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/builder/$Seg" {
import {$Range, $Range$Type} from "packages/com/vladsch/flexmark/util/sequence/$Range"

export class $Seg {
static readonly "NULL": $Seg
static readonly "ANCHOR_0": $Seg
static readonly "MAX_TEXT_OFFSET": integer
static readonly "F_TEXT_OPTION": integer


public "length"(): integer
public "toString"(): string
public "toString"(arg0: charseq): string
public "isNull"(): boolean
public "isText"(): boolean
public static "getTextStart"(arg0: integer, arg1: boolean): integer
public "getTextStart"(): integer
public "getRange"(): $Range
public "getEnd"(): integer
public "isAnchor"(): boolean
public "getTextEnd"(): integer
public static "getTextEnd"(arg0: integer, arg1: boolean): integer
public static "textOf"(arg0: integer, arg1: integer, arg2: boolean, arg3: boolean): $Seg
public static "segOf"(arg0: integer, arg1: integer): $Seg
public "isFirst256Start"(): boolean
public static "isFirst256Start"(arg0: integer): boolean
public "isRepeatedTextEnd"(): boolean
public static "isRepeatedTextEnd"(arg0: integer): boolean
public static "getTextOffset"(arg0: integer): integer
public "getSegEnd"(): integer
public "getStart"(): integer
public "isBase"(): boolean
public "getSegStart"(): integer
get "null"(): boolean
get "text"(): boolean
get "textStart"(): integer
get "range"(): $Range
get "end"(): integer
get "anchor"(): boolean
get "textEnd"(): integer
get "first256Start"(): boolean
get "repeatedTextEnd"(): boolean
get "segEnd"(): integer
get "start"(): integer
get "base"(): boolean
get "segStart"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Seg$Type = ($Seg);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Seg_ = $Seg$Type;
}}
declare module "packages/com/vladsch/flexmark/util/data/$DataKeyBase" {
import {$DataHolder, $DataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$DataHolder"
import {$DataValueFactory, $DataValueFactory$Type} from "packages/com/vladsch/flexmark/util/data/$DataValueFactory"
import {$MutableDataValueSetter, $MutableDataValueSetter$Type} from "packages/com/vladsch/flexmark/util/data/$MutableDataValueSetter"
import {$MutableDataHolder, $MutableDataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$MutableDataHolder"

export class $DataKeyBase<T> implements $MutableDataValueSetter<(T)> {

constructor(arg0: string, arg1: T, arg2: $DataValueFactory$Type<(T)>)
constructor(arg0: string, arg1: T)
constructor(arg0: string, arg1: $DataKeyBase$Type<(T)>)

public "getName"(): string
public "get"(arg0: $DataHolder$Type): T
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getFactory"(): $DataValueFactory<(T)>
public "getDefaultValue"(): T
public "getDefaultValue"(arg0: $DataHolder$Type): T
/**
 * 
 * @deprecated
 */
public "getFrom"(arg0: $DataHolder$Type): T
public "set"(arg0: $MutableDataHolder$Type, arg1: T): $MutableDataHolder
get "name"(): string
get "factory"(): $DataValueFactory<(T)>
get "defaultValue"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataKeyBase$Type<T> = ($DataKeyBase<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataKeyBase_<T> = $DataKeyBase$Type<(T)>;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/builder/tree/$SegmentTree$SegmentTreeData" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SegmentTree$SegmentTreeData {
readonly "treeData": (integer)[]
readonly "segmentBytes": (byte)[]
readonly "startIndices": (integer)[]

constructor(arg0: (integer)[], arg1: (byte)[], arg2: (integer)[])

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SegmentTree$SegmentTreeData$Type = ($SegmentTree$SegmentTreeData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SegmentTree$SegmentTreeData_ = $SegmentTree$SegmentTreeData$Type;
}}
declare module "packages/com/vladsch/flexmark/util/misc/$CharPredicate" {
import {$IntPredicate, $IntPredicate$Type} from "packages/java/util/function/$IntPredicate"

export interface $CharPredicate extends $IntPredicate {

 "test"(arg0: integer): boolean
 "test"(arg0: character): boolean
 "or"(arg0: $CharPredicate$Type): $CharPredicate
 "and"(arg0: $CharPredicate$Type): $CharPredicate
 "or"(arg0: $IntPredicate$Type): $IntPredicate
 "and"(arg0: $IntPredicate$Type): $IntPredicate

(arg0: charseq, arg1: character): integer
}

export namespace $CharPredicate {
const NONE: $CharPredicate
const ALL: $CharPredicate
const SPACE: $CharPredicate
const TAB: $CharPredicate
const EOL: $CharPredicate
const ANY_EOL: $CharPredicate
const ANY_EOL_NUL: $CharPredicate
const BACKSLASH: $CharPredicate
const SLASH: $CharPredicate
const LINE_SEP: $CharPredicate
const HASH: $CharPredicate
const SPACE_TAB: $CharPredicate
const SPACE_TAB_NUL: $CharPredicate
const SPACE_TAB_LINE_SEP: $CharPredicate
const SPACE_TAB_NBSP_LINE_SEP: $CharPredicate
const SPACE_EOL: $CharPredicate
const SPACE_ANY_EOL: $CharPredicate
const SPACE_TAB_NBSP: $CharPredicate
const SPACE_TAB_EOL: $CharPredicate
const SPACE_TAB_NBSP_EOL: $CharPredicate
const WHITESPACE: $CharPredicate
const WHITESPACE_OR_NUL: $CharPredicate
const WHITESPACE_NBSP: $CharPredicate
const WHITESPACE_NBSP_OR_NUL: $CharPredicate
const BLANKSPACE: $CharPredicate
const HEXADECIMAL_DIGITS: $CharPredicate
const DECIMAL_DIGITS: $CharPredicate
const OCTAL_DIGITS: $CharPredicate
const BINARY_DIGITS: $CharPredicate
const FALSE: $CharPredicate
const TRUE: $CharPredicate
const SPACE_TAB_OR_NUL: $CharPredicate
function indexOf(arg0: charseq, arg1: character): integer
function indexOf(arg0: charseq, arg1: character, arg2: integer, arg3: integer): integer
function anyOf(...arg0: (character)[]): $CharPredicate
function anyOf(arg0: charseq): $CharPredicate
function standardOrAnyOf(arg0: character, arg1: character, arg2: character, arg3: character): $CharPredicate
function standardOrAnyOf(arg0: character, arg1: character): $CharPredicate
function standardOrAnyOf(arg0: character): $CharPredicate
function standardOrAnyOf(arg0: character, arg1: character, arg2: character): $CharPredicate
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CharPredicate$Type = ($CharPredicate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CharPredicate_ = $CharPredicate$Type;
}}
declare module "packages/com/vladsch/flexmark/util/data/$DataNotNullValueFactory" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$DataHolder, $DataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$DataHolder"
import {$DataValueFactory, $DataValueFactory$Type} from "packages/com/vladsch/flexmark/util/data/$DataValueFactory"

export interface $DataNotNullValueFactory<T> extends $DataValueFactory<(T)> {

 "apply"(arg0: $DataHolder$Type): T
 "compose"<V>(arg0: $Function$Type<(any), (any)>): $Function<(V), ($DataHolder)>
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $Function<($DataHolder), (V)>

(arg0: $DataHolder$Type): T
}

export namespace $DataNotNullValueFactory {
function identity<T>(): $Function<($DataHolder), ($DataHolder)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataNotNullValueFactory$Type<T> = ($DataNotNullValueFactory<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataNotNullValueFactory_<T> = $DataNotNullValueFactory$Type<(T)>;
}}
declare module "packages/com/vladsch/flexmark/util/visitor/$AstAction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $AstAction<N> {

}

export namespace $AstAction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AstAction$Type<N> = ($AstAction<(N)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AstAction_<N> = $AstAction$Type<(N)>;
}}
declare module "packages/com/vladsch/flexmark/parser/block/$BlockContinue" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $BlockContinue {


public static "finished"(): $BlockContinue
public static "atIndex"(arg0: integer): $BlockContinue
public static "none"(): $BlockContinue
public static "atColumn"(arg0: integer): $BlockContinue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockContinue$Type = ($BlockContinue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockContinue_ = $BlockContinue$Type;
}}
declare module "packages/com/vladsch/flexmark/ast/$Text" {
import {$NodeVisitor, $NodeVisitor$Type} from "packages/com/vladsch/flexmark/util/ast/$NodeVisitor"
import {$AstNode, $AstNode$Type} from "packages/com/vladsch/flexmark/util/visitor/$AstNode"
import {$StringBuilder, $StringBuilder$Type} from "packages/java/lang/$StringBuilder"
import {$ISequenceBuilder, $ISequenceBuilder$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$ISequenceBuilder"
import {$Node, $Node$Type} from "packages/com/vladsch/flexmark/util/ast/$Node"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"
import {$TextContainer, $TextContainer$Type} from "packages/com/vladsch/flexmark/util/ast/$TextContainer"

export class $Text extends $Node implements $TextContainer {
static readonly "EMPTY_SEGMENTS": ($BasedSequence)[]
static readonly "SPLICE": string
static readonly "AST_ADAPTER": $AstNode<($Node)>

constructor(arg0: string, arg1: $BasedSequence$Type)
constructor(arg0: string)
constructor(arg0: $BasedSequence$Type)
constructor()

public "getSegments"(): ($BasedSequence)[]
public "getAstExtra"(arg0: $StringBuilder$Type): void
public "collectText"(arg0: $ISequenceBuilder$Type<(any), ($BasedSequence$Type)>, arg1: integer, arg2: $NodeVisitor$Type): boolean
public "collectEndText"(arg0: $ISequenceBuilder$Type<(any), ($BasedSequence$Type)>, arg1: integer, arg2: $NodeVisitor$Type): void
get "segments"(): ($BasedSequence)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Text$Type = ($Text);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Text_ = $Text$Type;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/builder/tree/$SegmentTreePos" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SegmentTreePos {
readonly "pos": integer
readonly "startIndex": integer
readonly "iterations": integer

constructor(arg0: integer, arg1: integer, arg2: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SegmentTreePos$Type = ($SegmentTreePos);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SegmentTreePos_ = $SegmentTreePos$Type;
}}
declare module "packages/com/vladsch/flexmark/parser/delimiter/$DelimiterProcessor" {
import {$DelimiterRun, $DelimiterRun$Type} from "packages/com/vladsch/flexmark/parser/delimiter/$DelimiterRun"
import {$InlineParser, $InlineParser$Type} from "packages/com/vladsch/flexmark/parser/$InlineParser"
import {$Node, $Node$Type} from "packages/com/vladsch/flexmark/util/ast/$Node"
import {$Delimiter, $Delimiter$Type} from "packages/com/vladsch/flexmark/parser/core/delimiter/$Delimiter"

export interface $DelimiterProcessor {

 "process"(arg0: $Delimiter$Type, arg1: $Delimiter$Type, arg2: integer): void
 "getMinLength"(): integer
 "unmatchedDelimiterNode"(arg0: $InlineParser$Type, arg1: $DelimiterRun$Type): $Node
 "getClosingCharacter"(): character
 "getOpeningCharacter"(): character
 "skipNonOpenerCloser"(): boolean
 "canBeOpener"(arg0: string, arg1: string, arg2: boolean, arg3: boolean, arg4: boolean, arg5: boolean, arg6: boolean, arg7: boolean): boolean
 "getDelimiterUse"(arg0: $DelimiterRun$Type, arg1: $DelimiterRun$Type): integer
 "canBeCloser"(arg0: string, arg1: string, arg2: boolean, arg3: boolean, arg4: boolean, arg5: boolean, arg6: boolean, arg7: boolean): boolean
}

export namespace $DelimiterProcessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DelimiterProcessor$Type = ($DelimiterProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DelimiterProcessor_ = $DelimiterProcessor$Type;
}}
declare module "packages/com/vladsch/flexmark/parser/block/$BlockStart" {
import {$BlockParser, $BlockParser$Type} from "packages/com/vladsch/flexmark/parser/block/$BlockParser"

export class $BlockStart {


public static "of"(...arg0: ($BlockParser$Type)[]): $BlockStart
public "atIndex"(arg0: integer): $BlockStart
public static "none"(): $BlockStart
public "atColumn"(arg0: integer): $BlockStart
public "replaceActiveBlockParser"(): $BlockStart
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockStart$Type = ($BlockStart);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockStart_ = $BlockStart$Type;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/$BasedOptionsHolder" {
import {$SegmentedSequenceStats, $SegmentedSequenceStats$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$SegmentedSequenceStats"
import {$DataHolder, $DataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$DataHolder"
import {$BasedOptionsHolder$Options, $BasedOptionsHolder$Options$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedOptionsHolder$Options"
import {$NullableDataKey, $NullableDataKey$Type} from "packages/com/vladsch/flexmark/util/data/$NullableDataKey"
import {$DataKeyBase, $DataKeyBase$Type} from "packages/com/vladsch/flexmark/util/data/$DataKeyBase"

export interface $BasedOptionsHolder {

 "getOptions"(): $DataHolder
 "getOption"<T>(arg0: $DataKeyBase$Type<(T)>): T
 "allOptions"(arg0: integer): boolean
 "getOptionFlags"(): integer
 "anyOptions"(arg0: integer): boolean
}

export namespace $BasedOptionsHolder {
const O_COLLECT_SEGMENTED_STATS: $BasedOptionsHolder$Options
const O_COLLECT_FIRST256_STATS: $BasedOptionsHolder$Options
const O_NO_ANCHORS: $BasedOptionsHolder$Options
const O_FULL_SEGMENTED_SEQUENCES: $BasedOptionsHolder$Options
const O_TREE_SEGMENTED_SEQUENCES: $BasedOptionsHolder$Options
const F_COLLECT_SEGMENTED_STATS: integer
const F_COLLECT_FIRST256_STATS: integer
const F_NO_ANCHORS: integer
const F_FULL_SEGMENTED_SEQUENCES: integer
const F_TREE_SEGMENTED_SEQUENCES: integer
const F_LIBRARY_OPTIONS: integer
const F_APPLICATION_OPTIONS: integer
const SEGMENTED_STATS: $NullableDataKey<($SegmentedSequenceStats)>
function optionsToString(arg0: integer): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BasedOptionsHolder$Type = ($BasedOptionsHolder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BasedOptionsHolder_ = $BasedOptionsHolder$Type;
}}
declare module "packages/com/vladsch/flexmark/util/ast/$BlockTracker" {
import {$Block, $Block$Type} from "packages/com/vladsch/flexmark/util/ast/$Block"

export interface $BlockTracker {

 "blockAdded"(arg0: $Block$Type): void
 "blockRemoved"(arg0: $Block$Type): void
 "blockAddedWithChildren"(arg0: $Block$Type): void
 "blockAddedWithDescendants"(arg0: $Block$Type): void
 "blockRemovedWithDescendants"(arg0: $Block$Type): void
 "blockRemovedWithChildren"(arg0: $Block$Type): void
}

export namespace $BlockTracker {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockTracker$Type = ($BlockTracker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockTracker_ = $BlockTracker$Type;
}}
declare module "packages/com/vladsch/flexmark/ast/$Paragraph" {
import {$NodeVisitor, $NodeVisitor$Type} from "packages/com/vladsch/flexmark/util/ast/$NodeVisitor"
import {$AstNode, $AstNode$Type} from "packages/com/vladsch/flexmark/util/visitor/$AstNode"
import {$Block, $Block$Type} from "packages/com/vladsch/flexmark/util/ast/$Block"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StringBuilder, $StringBuilder$Type} from "packages/java/lang/$StringBuilder"
import {$ISequenceBuilder, $ISequenceBuilder$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$ISequenceBuilder"
import {$Node, $Node$Type} from "packages/com/vladsch/flexmark/util/ast/$Node"
import {$BasedSequence, $BasedSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$BasedSequence"
import {$BlockContent, $BlockContent$Type} from "packages/com/vladsch/flexmark/util/ast/$BlockContent"
import {$TextContainer, $TextContainer$Type} from "packages/com/vladsch/flexmark/util/ast/$TextContainer"

export class $Paragraph extends $Block implements $TextContainer {
static readonly "EMPTY_SEGMENTS": ($BasedSequence)[]
static readonly "SPLICE": string
static readonly "AST_ADAPTER": $AstNode<($Node)>

constructor(arg0: $BlockContent$Type)
constructor(arg0: $BasedSequence$Type, arg1: $List$Type<($BasedSequence$Type)>, arg2: (integer)[])
constructor(arg0: $BasedSequence$Type, arg1: $List$Type<($BasedSequence$Type)>, arg2: $List$Type<(integer)>)
constructor(arg0: $BasedSequence$Type)
constructor()

public "setTrailingBlankLine"(arg0: boolean): void
public "setHasTableSeparator"(arg0: boolean): void
public "isTrailingBlankLine"(): boolean
public "getSegments"(): ($BasedSequence)[]
public "getAstExtra"(arg0: $StringBuilder$Type): void
public "collectText"(arg0: $ISequenceBuilder$Type<(any), ($BasedSequence$Type)>, arg1: integer, arg2: $NodeVisitor$Type): boolean
public "collectEndText"(arg0: $ISequenceBuilder$Type<(any), ($BasedSequence$Type)>, arg1: integer, arg2: $NodeVisitor$Type): void
public "getLineIndents"(): (integer)[]
public "getLineIndent"(arg0: integer): integer
public "setLineIndents"(arg0: (integer)[]): void
public "hasTableSeparator"(): boolean
public "setContent"(arg0: $BlockContent$Type, arg1: integer, arg2: integer): void
public "setContent"(arg0: $List$Type<($BasedSequence$Type)>): void
public "setContent"(arg0: $BlockContent$Type): void
public "setContent"(arg0: $BasedSequence$Type, arg1: $List$Type<($BasedSequence$Type)>, arg2: $List$Type<(integer)>): void
public "setContent"(arg0: $Paragraph$Type, arg1: integer, arg2: integer): void
public "setContent"(arg0: $BasedSequence$Type, arg1: $List$Type<($BasedSequence$Type)>): void
set "trailingBlankLine"(value: boolean)
get "trailingBlankLine"(): boolean
get "segments"(): ($BasedSequence)[]
get "lineIndents"(): (integer)[]
set "lineIndents"(value: (integer)[])
set "content"(value: $List$Type<($BasedSequence$Type)>)
set "content"(value: $BlockContent$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Paragraph$Type = ($Paragraph);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Paragraph_ = $Paragraph$Type;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/$RichSequence" {
import {$Range, $Range$Type} from "packages/com/vladsch/flexmark/util/sequence/$Range"
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$IntStream, $IntStream$Type} from "packages/java/util/stream/$IntStream"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$IRichSequence, $IRichSequence$Type} from "packages/com/vladsch/flexmark/util/sequence/$IRichSequence"
import {$Pair, $Pair$Type} from "packages/com/vladsch/flexmark/util/misc/$Pair"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$CharPredicate, $CharPredicate$Type} from "packages/com/vladsch/flexmark/util/misc/$CharPredicate"
import {$StringBuilder, $StringBuilder$Type} from "packages/java/lang/$StringBuilder"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ISequenceBuilder, $ISequenceBuilder$Type} from "packages/com/vladsch/flexmark/util/sequence/builder/$ISequenceBuilder"
import {$CharMapper, $CharMapper$Type} from "packages/com/vladsch/flexmark/util/sequence/mappers/$CharMapper"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $RichSequence extends $IRichSequence<($RichSequence)> {

 "toVisibleWhitespaceString"(): string
 "equals"(arg0: any): boolean
 "equals"(arg0: any, arg1: boolean): boolean
 "append"(...arg0: (charseq)[]): $RichSequence
 "append"(arg0: $Iterable$Type<(any)>): $RichSequence
 "hashCode"(): integer
 "indexOf"(arg0: character, arg1: integer): integer
 "indexOf"(arg0: charseq): integer
 "indexOf"(arg0: charseq, arg1: integer): integer
 "indexOf"(arg0: charseq, arg1: integer, arg2: integer): integer
 "indexOf"(arg0: character, arg1: integer, arg2: integer): integer
 "indexOf"(arg0: character): integer
/**
 * 
 * @deprecated
 */
 "insert"(arg0: charseq, arg1: integer): $RichSequence
 "insert"(arg0: integer, arg1: charseq): $RichSequence
 "startsWith"(arg0: charseq): boolean
 "startsWith"(arg0: charseq, arg1: boolean): boolean
 "startsWith"(arg0: $CharPredicate$Type): boolean
 "lastIndexOf"(arg0: charseq): integer
 "lastIndexOf"(arg0: character): integer
 "lastIndexOf"(arg0: character, arg1: integer): integer
 "lastIndexOf"(arg0: character, arg1: integer, arg2: integer): integer
 "lastIndexOf"(arg0: charseq, arg1: integer, arg2: integer): integer
 "lastIndexOf"(arg0: charseq, arg1: integer): integer
 "isEmpty"(): boolean
 "replace"(arg0: charseq, arg1: charseq): $RichSequence
 "replace"(arg0: integer, arg1: integer, arg2: charseq): $RichSequence
 "matches"(arg0: charseq, arg1: boolean): boolean
 "matches"(arg0: charseq): boolean
 "split"(arg0: charseq, arg1: boolean, arg2: $CharPredicate$Type): ($RichSequence)[]
 "split"(arg0: charseq, arg1: integer, arg2: integer, arg3: $CharPredicate$Type): ($RichSequence)[]
 "split"(arg0: charseq, arg1: integer, arg2: integer): ($RichSequence)[]
 "split"(arg0: charseq): ($RichSequence)[]
/**
 * 
 * @deprecated
 */
 "split"(arg0: character, arg1: integer, arg2: integer): ($RichSequence)[]
 "split"(arg0: charseq, arg1: integer, arg2: boolean, arg3: $CharPredicate$Type): ($RichSequence)[]
/**
 * 
 * @deprecated
 */
 "split"(arg0: character): ($RichSequence)[]
/**
 * 
 * @deprecated
 */
 "split"(arg0: character, arg1: integer): ($RichSequence)[]
 "toLowerCase"(): $RichSequence
 "toUpperCase"(): $RichSequence
 "trim"(arg0: integer): $RichSequence
 "trim"(arg0: $CharPredicate$Type): $RichSequence
 "trim"(arg0: integer, arg1: $CharPredicate$Type): $RichSequence
 "trim"(): $RichSequence
 "isBlank"(): boolean
 "equalsIgnoreCase"(arg0: any): boolean
 "endsWith"(arg0: charseq): boolean
 "endsWith"(arg0: charseq, arg1: boolean): boolean
 "endsWith"(arg0: $CharPredicate$Type): boolean
 "subSequence"(arg0: integer): $RichSequence
 "subSequence"(arg0: $Range$Type): $RichSequence
 "lastChar"(): character
 "delete"(arg0: integer, arg1: integer): $RichSequence
 "appendTo"(arg0: $StringBuilder$Type, arg1: $CharMapper$Type): $RichSequence
 "appendTo"(arg0: $StringBuilder$Type, arg1: $CharMapper$Type, arg2: integer): $RichSequence
 "appendTo"(arg0: $StringBuilder$Type): $RichSequence
 "appendTo"(arg0: $StringBuilder$Type, arg1: integer): $RichSequence
 "appendTo"(arg0: $StringBuilder$Type, arg1: integer, arg2: integer): $RichSequence
 "appendTo"(arg0: $StringBuilder$Type, arg1: $CharMapper$Type, arg2: integer, arg3: integer): $RichSequence
 "firstChar"(): character
 "isNull"(): boolean
 "padding"(arg0: integer, arg1: character): $RichSequence
 "padding"(arg0: integer): $RichSequence
 "emptyArray"(): ($RichSequence)[]
 "padStart"(arg0: integer): $RichSequence
 "padStart"(arg0: integer, arg1: character): $RichSequence
 "startsWithIgnoreCase"(arg0: charseq): boolean
 "trimmed"(): $Pair<($RichSequence), ($RichSequence)>
 "trimmed"(arg0: $CharPredicate$Type): $Pair<($RichSequence), ($RichSequence)>
 "trimmed"(arg0: integer): $Pair<($RichSequence), ($RichSequence)>
 "trimmed"(arg0: integer, arg1: $CharPredicate$Type): $Pair<($RichSequence), ($RichSequence)>
 "getBuilder"<B extends $ISequenceBuilder<(B), (T)>>(): B
 "lineColumnAtIndex"(arg0: integer): $Pair<(integer), (integer)>
 "endsWithIgnoreCase"(arg0: charseq): boolean
 "indexOfAny"(arg0: $CharPredicate$Type): integer
 "indexOfAny"(arg0: $CharPredicate$Type, arg1: integer): integer
 "indexOfAny"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "lastIndexOfAny"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "lastIndexOfAny"(arg0: $CharPredicate$Type, arg1: integer): integer
 "lastIndexOfAny"(arg0: $CharPredicate$Type): integer
 "removeSuffix"(arg0: charseq, arg1: boolean): $RichSequence
 "removeSuffix"(arg0: charseq): $RichSequence
 "matchChars"(arg0: charseq): boolean
 "matchChars"(arg0: charseq, arg1: integer): boolean
 "matchChars"(arg0: charseq, arg1: integer, arg2: boolean): boolean
 "matchChars"(arg0: charseq, arg1: boolean): boolean
 "isNotNull"(): boolean
 "endSequence"(arg0: integer, arg1: integer): $RichSequence
 "endSequence"(arg0: integer): $RichSequence
 "endOfLine"(arg0: integer): integer
 "appendSpace"(): $RichSequence
 "trimStart"(): $RichSequence
 "trimStart"(arg0: integer, arg1: $CharPredicate$Type): $RichSequence
 "trimStart"(arg0: integer): $RichSequence
 "trimStart"(arg0: $CharPredicate$Type): $RichSequence
 "countTrailingSpaceTab"(arg0: integer): integer
 "countTrailingSpaceTab"(arg0: integer, arg1: integer): integer
 "countTrailingSpaceTab"(): integer
 "countTrailingNotSpaceTab"(arg0: integer, arg1: integer): integer
 "countTrailingNotSpaceTab"(): integer
 "countTrailingNotSpaceTab"(arg0: integer): integer
 "countLeadingWhitespace"(arg0: integer, arg1: integer): integer
 "countLeadingWhitespace"(): integer
 "countLeadingWhitespace"(arg0: integer): integer
 "countTrailingWhitespace"(): integer
 "countTrailingWhitespace"(arg0: integer, arg1: integer): integer
 "countTrailingWhitespace"(arg0: integer): integer
 "countOfNotWhitespace"(): integer
 "countLeadingSpaceTab"(): integer
 "countLeadingSpaceTab"(arg0: integer): integer
 "countLeadingSpaceTab"(arg0: integer, arg1: integer): integer
 "countLeadingColumns"(arg0: integer, arg1: $CharPredicate$Type): integer
 "countLeadingNotSpaceTab"(arg0: integer): integer
 "countLeadingNotSpaceTab"(): integer
 "countLeadingNotSpaceTab"(arg0: integer, arg1: integer): integer
 "countTrailingNotWhitespace"(arg0: integer): integer
 "countTrailingNotWhitespace"(arg0: integer, arg1: integer): integer
 "countTrailingNotWhitespace"(): integer
 "nullIfNotStartsWith"(...arg0: (charseq)[]): $RichSequence
 "nullIfNotStartsWith"(arg0: boolean, ...arg1: (charseq)[]): $RichSequence
 "countLeadingNotSpace"(arg0: integer, arg1: integer): integer
 "countLeadingNotSpace"(arg0: integer): integer
 "countLeadingNotSpace"(): integer
 "countTrailingNotSpace"(arg0: integer): integer
 "countTrailingNotSpace"(arg0: integer, arg1: integer): integer
 "countTrailingNotSpace"(): integer
 "countLeadingNotWhitespace"(arg0: integer): integer
 "countLeadingNotWhitespace"(): integer
 "countLeadingNotWhitespace"(arg0: integer, arg1: integer): integer
 "normalizeEndWithEOL"(): string
 "leadingBlankLinesRange"(arg0: integer, arg1: integer): $Range
 "leadingBlankLinesRange"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): $Range
 "leadingBlankLinesRange"(arg0: integer): $Range
 "leadingBlankLinesRange"(): $Range
 "endOfDelimitedByAny"(arg0: $CharPredicate$Type, arg1: integer): integer
 "matchedCharCountIgnoreCase"(arg0: charseq, arg1: integer): integer
 "matchedCharCountIgnoreCase"(arg0: charseq, arg1: integer, arg2: integer): integer
 "trailingBlankLinesRange"(arg0: integer): $Range
 "trailingBlankLinesRange"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): $Range
 "trailingBlankLinesRange"(): $Range
 "trailingBlankLinesRange"(arg0: integer, arg1: integer): $Range
 "suffixOnceWithSpace"(): $RichSequence
 "nullIfNotStartsWithIgnoreCase"(...arg0: (charseq)[]): $RichSequence
 "startsWithWhitespace"(): boolean
 "nullIfEndsWithIgnoreCase"(...arg0: (charseq)[]): $RichSequence
 "startOfDelimitedByAny"(arg0: $CharPredicate$Type, arg1: integer): integer
 "nullIfNotEndsWithIgnoreCase"(...arg0: (charseq)[]): $RichSequence
 "removeProperSuffixIgnoreCase"(arg0: charseq): $RichSequence
/**
 * 
 * @deprecated
 */
 "getLineColumnAtIndex"(arg0: integer): $Pair<(integer), (integer)>
 "removePrefixIgnoreCase"(arg0: charseq): $RichSequence
 "matchCharsReversedIgnoreCase"(arg0: charseq, arg1: integer): boolean
 "prefixOnceWithSpace"(): $RichSequence
 "nullIfStartsWithIgnoreCase"(...arg0: (charseq)[]): $RichSequence
 "removeProperPrefixIgnoreCase"(arg0: charseq): $RichSequence
 "endOfDelimitedByAnyNot"(arg0: $CharPredicate$Type, arg1: integer): integer
 "blankLinesRemovedRanges"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): $List<($Range)>
 "blankLinesRemovedRanges"(): $List<($Range)>
 "blankLinesRemovedRanges"(arg0: integer): $List<($Range)>
 "blankLinesRemovedRanges"(arg0: integer, arg1: integer): $List<($Range)>
 "matchedCharCountReversed"(arg0: charseq, arg1: integer, arg2: integer): integer
 "matchedCharCountReversed"(arg0: charseq, arg1: integer, arg2: boolean): integer
 "matchedCharCountReversed"(arg0: charseq, arg1: integer): integer
 "matchedCharCountReversed"(arg0: charseq, arg1: integer, arg2: integer, arg3: boolean): integer
/**
 * 
 * @deprecated
 */
 "nullIfStartsWithNot"(...arg0: (charseq)[]): $RichSequence
 "matchCharsIgnoreCase"(arg0: charseq): boolean
 "matchCharsIgnoreCase"(arg0: charseq, arg1: integer): boolean
 "matchedCharCountReversedIgnoreCase"(arg0: charseq, arg1: integer): integer
 "matchedCharCountReversedIgnoreCase"(arg0: charseq, arg1: integer, arg2: integer): integer
 "startOfDelimitedByAnyNot"(arg0: $CharPredicate$Type, arg1: integer): integer
 "removeSuffixIgnoreCase"(arg0: charseq): $RichSequence
 "splitList"(arg0: charseq, arg1: integer, arg2: integer): $List<($RichSequence)>
 "splitList"(arg0: charseq): $List<($RichSequence)>
 "splitList"(arg0: charseq, arg1: integer, arg2: integer, arg3: $CharPredicate$Type): $List<($RichSequence)>
 "splitList"(arg0: charseq, arg1: boolean, arg2: $CharPredicate$Type): $List<($RichSequence)>
 "splitList"(arg0: charseq, arg1: integer, arg2: boolean, arg3: $CharPredicate$Type): $List<($RichSequence)>
 "isNotBlank"(): boolean
 "isNotEmpty"(): boolean
 "padEnd"(arg0: integer): $RichSequence
 "padEnd"(arg0: integer, arg1: character): $RichSequence
 "removePrefix"(arg0: charseq, arg1: boolean): $RichSequence
 "removePrefix"(arg0: charseq): $RichSequence
 "trimEnd"(): $RichSequence
 "trimEnd"(arg0: $CharPredicate$Type): $RichSequence
 "trimEnd"(arg0: integer, arg1: $CharPredicate$Type): $RichSequence
 "trimEnd"(arg0: integer): $RichSequence
 "startOfLine"(arg0: integer): integer
 "sequenceOf"(arg0: charseq, arg1: integer, arg2: integer): $RichSequence
 "sequenceOf"(arg0: charseq): $RichSequence
 "sequenceOf"(arg0: charseq, arg1: integer): $RichSequence
 "isIn"(arg0: $Collection$Type<(any)>): boolean
 "isIn"(arg0: (string)[]): boolean
 "normalizeEOL"(): string
 "midSequence"(arg0: integer): $RichSequence
 "midSequence"(arg0: integer, arg1: integer): $RichSequence
 "countLeading"(arg0: $CharPredicate$Type, arg1: integer): integer
 "countLeading"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "countLeading"(arg0: $CharPredicate$Type): integer
/**
 * 
 * @deprecated
 */
 "countLeading"(arg0: character): integer
/**
 * 
 * @deprecated
 */
 "countLeading"(): integer
 "midCharAt"(arg0: integer): character
 "subSequenceBefore"(arg0: $Range$Type): $RichSequence
 "safeSubSequence"(arg0: integer): $RichSequence
 "safeSubSequence"(arg0: integer, arg1: integer): $RichSequence
 "indexOfNot"(arg0: character, arg1: integer): integer
 "indexOfNot"(arg0: character): integer
 "indexOfNot"(arg0: character, arg1: integer, arg2: integer): integer
 "countLeadingNot"(arg0: $CharPredicate$Type, arg1: integer): integer
 "countLeadingNot"(arg0: $CharPredicate$Type): integer
 "countLeadingNot"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "nullSequence"(): $RichSequence
 "endCharAt"(arg0: integer): character
 "subSequenceAfter"(arg0: $Range$Type): $RichSequence
 "lastIndexOfNot"(arg0: character, arg1: integer, arg2: integer): integer
 "lastIndexOfNot"(arg0: character, arg1: integer): integer
 "lastIndexOfNot"(arg0: character): integer
 "lastIndexOfAnyNot"(arg0: $CharPredicate$Type, arg1: integer): integer
 "lastIndexOfAnyNot"(arg0: $CharPredicate$Type): integer
 "lastIndexOfAnyNot"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "indexOfAnyNot"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "indexOfAnyNot"(arg0: $CharPredicate$Type, arg1: integer): integer
 "indexOfAnyNot"(arg0: $CharPredicate$Type): integer
 "safeCharAt"(arg0: integer): character
 "countOfWhitespace"(): integer
 "countOfAnyNot"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "countOfAnyNot"(arg0: $CharPredicate$Type): integer
 "countOfAnyNot"(arg0: $CharPredicate$Type, arg1: integer): integer
 "countLeadingSpace"(arg0: integer): integer
 "countLeadingSpace"(): integer
 "countLeadingSpace"(arg0: integer, arg1: integer): integer
 "countTrailingNot"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "countTrailingNot"(arg0: $CharPredicate$Type, arg1: integer): integer
 "countTrailingNot"(arg0: $CharPredicate$Type): integer
 "countTrailing"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "countTrailing"(arg0: $CharPredicate$Type, arg1: integer): integer
 "countTrailing"(arg0: $CharPredicate$Type): integer
/**
 * 
 * @deprecated
 */
 "countTrailing"(): integer
 "countOfSpaceTab"(): integer
 "countOfAny"(arg0: $CharPredicate$Type, arg1: integer): integer
 "countOfAny"(arg0: $CharPredicate$Type, arg1: integer, arg2: integer): integer
 "countOfAny"(arg0: $CharPredicate$Type): integer
 "countTrailingSpace"(): integer
 "countTrailingSpace"(arg0: integer): integer
 "countTrailingSpace"(arg0: integer, arg1: integer): integer
 "trimStartRange"(arg0: $CharPredicate$Type): $Range
 "trimStartRange"(): $Range
 "trimStartRange"(arg0: integer): $Range
 "trimStartRange"(arg0: integer, arg1: $CharPredicate$Type): $Range
 "trimEndRange"(arg0: integer, arg1: $CharPredicate$Type): $Range
 "trimEndRange"(arg0: integer): $Range
 "trimEndRange"(arg0: $CharPredicate$Type): $Range
 "trimEndRange"(): $Range
/**
 * 
 * @deprecated
 */
 "countOf"(arg0: character): integer
 "trimRange"(arg0: integer): $Range
 "trimRange"(arg0: $CharPredicate$Type): $Range
 "trimRange"(): $Range
 "trimRange"(arg0: integer, arg1: $CharPredicate$Type): $Range
 "countOfNotSpaceTab"(): integer
 "nullIfNot"(arg0: $BiPredicate$Type<(any), (any)>, ...arg1: (charseq)[]): $RichSequence
 "nullIfNot"(...arg0: (charseq)[]): $RichSequence
 "nullIfNot"(arg0: $Predicate$Type<(any)>, ...arg1: (charseq)[]): $RichSequence
 "nullIfEndsWith"(...arg0: (charseq)[]): $RichSequence
 "nullIfEndsWith"(arg0: boolean, ...arg1: (charseq)[]): $RichSequence
 "ifNull"(arg0: $RichSequence$Type): $RichSequence
 "ifNullEmptyAfter"(arg0: $RichSequence$Type): $RichSequence
 "nullIfNotEndsWith"(arg0: boolean, ...arg1: (charseq)[]): $RichSequence
 "nullIfNotEndsWith"(...arg0: (charseq)[]): $RichSequence
 "nullIfStartsWith"(arg0: boolean, ...arg1: (charseq)[]): $RichSequence
 "nullIfStartsWith"(...arg0: (charseq)[]): $RichSequence
 "trimmedStart"(arg0: integer): $RichSequence
 "trimmedStart"(arg0: $CharPredicate$Type): $RichSequence
 "trimmedStart"(): $RichSequence
 "trimmedStart"(arg0: integer, arg1: $CharPredicate$Type): $RichSequence
 "ifNullEmptyBefore"(arg0: $RichSequence$Type): $RichSequence
 "nullIf"(arg0: boolean): $RichSequence
 "nullIf"(...arg0: (charseq)[]): $RichSequence
 "nullIf"(arg0: $BiPredicate$Type<(any), (any)>, ...arg1: (charseq)[]): $RichSequence
 "nullIf"(arg0: $Predicate$Type<(any)>, ...arg1: (charseq)[]): $RichSequence
 "nullIfEmpty"(): $RichSequence
 "nullIfBlank"(): $RichSequence
 "trimmedEnd"(arg0: integer, arg1: $CharPredicate$Type): $RichSequence
 "trimmedEnd"(): $RichSequence
 "trimmedEnd"(arg0: integer): $RichSequence
 "trimmedEnd"(arg0: $CharPredicate$Type): $RichSequence
 "startOfDelimitedBy"(arg0: charseq, arg1: integer): integer
 "eolStartRange"(arg0: integer): $Range
/**
 * 
 * @deprecated
 */
 "nullIfEndsWithNot"(...arg0: (charseq)[]): $RichSequence
 "trimmedEOL"(): $RichSequence
 "endOfDelimitedBy"(arg0: charseq, arg1: integer): integer
 "startOfLineAnyEOL"(arg0: integer): integer
 "lineRangeAt"(arg0: integer): $Range
 "eolEndLength"(): integer
 "eolEndLength"(arg0: integer): integer
 "eolEndRange"(arg0: integer): $Range
 "lineRangeAtAnyEOL"(arg0: integer): $Range
 "lineAt"(arg0: integer): $RichSequence
 "lineAtAnyEOL"(arg0: integer): $RichSequence
 "trimTailBlankLines"(): $RichSequence
 "trimEOL"(): $RichSequence
 "trimToStartOfLine"(arg0: $CharPredicate$Type, arg1: boolean, arg2: integer): $RichSequence
 "trimToStartOfLine"(arg0: boolean, arg1: integer): $RichSequence
 "trimToStartOfLine"(): $RichSequence
 "trimToStartOfLine"(arg0: boolean): $RichSequence
 "trimToStartOfLine"(arg0: integer): $RichSequence
 "trimToEndOfLine"(arg0: boolean, arg1: integer): $RichSequence
 "trimToEndOfLine"(arg0: $CharPredicate$Type, arg1: boolean, arg2: integer): $RichSequence
 "trimToEndOfLine"(): $RichSequence
 "trimToEndOfLine"(arg0: integer): $RichSequence
 "trimToEndOfLine"(arg0: boolean): $RichSequence
 "matchedCharCount"(arg0: charseq, arg1: integer, arg2: integer, arg3: boolean): integer
 "matchedCharCount"(arg0: charseq, arg1: integer, arg2: integer): integer
 "matchedCharCount"(arg0: charseq, arg1: integer): integer
 "matchedCharCount"(arg0: charseq, arg1: integer, arg2: integer, arg3: boolean, arg4: boolean): integer
 "matchedCharCount"(arg0: charseq, arg1: integer, arg2: boolean): integer
 "matchCharsReversed"(arg0: charseq, arg1: integer, arg2: boolean): boolean
 "matchCharsReversed"(arg0: charseq, arg1: integer): boolean
 "matchesIgnoreCase"(arg0: charseq): boolean
 "trimLeadBlankLines"(): $RichSequence
 "startsWithSpaceTab"(): boolean
 "endsWithWhitespace"(): boolean
 "removeProperSuffix"(arg0: charseq, arg1: boolean): $RichSequence
 "removeProperSuffix"(arg0: charseq): $RichSequence
 "startsWithSpace"(): boolean
 "endsWithSpace"(): boolean
 "endsWithSpaceTab"(): boolean
 "startsWithEOL"(): boolean
 "endsWithAnyEOL"(): boolean
 "startsWithAnyEOL"(): boolean
 "endsWithEOL"(): boolean
 "toSpc"(): $RichSequence
 "toNbSp"(): $RichSequence
 "removeProperPrefix"(arg0: charseq): $RichSequence
 "removeProperPrefix"(arg0: charseq, arg1: boolean): $RichSequence
 "toMapped"(arg0: $CharMapper$Type): $RichSequence
 "prefixWith"(arg0: charseq): $RichSequence
 "suffixWith"(arg0: charseq): $RichSequence
 "splitEOL"(): ($RichSequence)[]
 "splitEOL"(arg0: boolean): ($RichSequence)[]
 "appendEOL"(): $RichSequence
 "suffixWithEOL"(): $RichSequence
 "splitListEOL"(arg0: boolean, arg1: $CharPredicate$Type): $List<($RichSequence)>
 "splitListEOL"(arg0: boolean): $List<($RichSequence)>
 "splitListEOL"(): $List<($RichSequence)>
 "indexOfAll"(arg0: charseq): (integer)[]
 "prefixWithEOL"(): $RichSequence
 "prefixOnceWithEOL"(): $RichSequence
 "suffixOnceWithEOL"(): $RichSequence
 "suffixWithSpace"(): $RichSequence
 "prefixWithSpace"(): $RichSequence
 "prefixOnceWith"(arg0: charseq): $RichSequence
 "suffixOnceWith"(arg0: charseq): $RichSequence
 "extractRanges"(...arg0: ($Range$Type)[]): $RichSequence
 "extractRanges"(arg0: $Iterable$Type<($Range$Type)>): $RichSequence
 "columnAtIndex"(arg0: integer): integer
 "appendSpaces"(arg0: integer): $RichSequence
 "suffixWithSpaces"(arg0: integer): $RichSequence
/**
 * 
 * @deprecated
 */
 "getColumnAtIndex"(arg0: integer): integer
 "prefixWithSpaces"(arg0: integer): $RichSequence
 "appendRangesTo"(arg0: $StringBuilder$Type, ...arg1: ($Range$Type)[]): $RichSequence
 "appendRangesTo"(arg0: $StringBuilder$Type, arg1: $Iterable$Type<(any)>): $RichSequence
 "appendRangesTo"(arg0: $StringBuilder$Type, arg1: $CharMapper$Type, ...arg2: ($Range$Type)[]): $RichSequence
 "appendRangesTo"(arg0: $StringBuilder$Type, arg1: $CharMapper$Type, arg2: $Iterable$Type<(any)>): $RichSequence
 "toStringOrNull"(): string
 "isCharAt"(arg0: integer, arg1: $CharPredicate$Type): boolean
/**
 * 
 * @deprecated
 */
 "eolLength"(arg0: integer): integer
 "endOfLineAnyEOL"(arg0: integer): integer
 "eolStartLength"(arg0: integer): integer
/**
 * 
 * @deprecated
 */
 "eolStartLength"(): integer
 "length"(): integer
 "toString"(): string
 "charAt"(arg0: integer): character
 "codePoints"(): $IntStream
 "chars"(): $IntStream
 "compareTo"(arg0: charseq): integer
}

export namespace $RichSequence {
const NULL: $RichSequence
const EMPTY_ARRAY: ($RichSequence)[]
function of(arg0: charseq): $RichSequence
function of(arg0: charseq, arg1: integer, arg2: integer): $RichSequence
function of(arg0: charseq, arg1: integer): $RichSequence
function ofSpaces(arg0: integer): $RichSequence
function repeatOf(arg0: charseq, arg1: integer, arg2: integer): $RichSequence
function repeatOf(arg0: character, arg1: integer): $RichSequence
function repeatOf(arg0: charseq, arg1: integer): $RichSequence
function compare(arg0: charseq, arg1: charseq): integer
function toVisibleWhitespaceString(arg0: charseq): string
function equals(arg0: charseq, arg1: any): boolean
function hashCode(arg0: charseq): integer
function indexOf(arg0: charseq, arg1: charseq, arg2: integer): integer
function indexOf(arg0: charseq, arg1: character, arg2: integer): integer
function indexOf(arg0: charseq, arg1: charseq): integer
function indexOf(arg0: charseq, arg1: character): integer
function indexOf(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function indexOf(arg0: charseq, arg1: character, arg2: integer, arg3: integer): integer
function compare(arg0: charseq, arg1: charseq, arg2: boolean): integer
function compare(arg0: charseq, arg1: charseq, arg2: boolean, arg3: $CharPredicate$Type): integer
function startsWith(arg0: charseq, arg1: charseq, arg2: boolean): boolean
function startsWith(arg0: charseq, arg1: $CharPredicate$Type): boolean
function startsWith(arg0: charseq, arg1: charseq): boolean
function lastIndexOf(arg0: charseq, arg1: charseq, arg2: integer): integer
function lastIndexOf(arg0: charseq, arg1: charseq): integer
function lastIndexOf(arg0: charseq, arg1: character, arg2: integer): integer
function lastIndexOf(arg0: charseq, arg1: character, arg2: integer, arg3: integer): integer
function lastIndexOf(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function lastIndexOf(arg0: charseq, arg1: character): integer
function isEmpty(arg0: charseq): boolean
function matches(arg0: charseq, arg1: charseq): boolean
function matches(arg0: charseq, arg1: charseq, arg2: boolean): boolean
function split<T>(arg0: T, arg1: (T)[], arg2: charseq, arg3: integer, arg4: integer, arg5: $CharPredicate$Type): (T)[]
function split<T>(arg0: T, arg1: (T)[], arg2: charseq, arg3: integer, arg4: integer): (T)[]
function split<T>(arg0: T, arg1: (T)[], arg2: charseq, arg3: integer, arg4: boolean, arg5: $CharPredicate$Type): (T)[]
function split<T>(arg0: T, arg1: (T)[], arg2: charseq, arg3: boolean, arg4: $CharPredicate$Type): (T)[]
function split<T>(arg0: T, arg1: (T)[], arg2: charseq): (T)[]
function trim<T>(arg0: T, arg1: integer): T
function trim<T>(arg0: T): T
function trim<T>(arg0: T, arg1: $CharPredicate$Type): T
function trim<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): T
function isBlank(arg0: charseq): boolean
function endsWith(arg0: charseq, arg1: charseq, arg2: boolean): boolean
function endsWith(arg0: charseq, arg1: $CharPredicate$Type): boolean
function endsWith(arg0: charseq, arg1: charseq): boolean
function subSequence<T>(arg0: T, arg1: integer): T
function subSequence<T>(arg0: T, arg1: $Range$Type): T
function lastChar(arg0: charseq): character
function firstChar(arg0: charseq): character
function expandTo(arg0: (integer)[], arg1: integer, arg2: integer): (integer)[]
function padStart(arg0: charseq, arg1: integer): string
function padStart(arg0: charseq, arg1: integer, arg2: character): string
function trimmed<T>(arg0: T): $Pair<(T), (T)>
function trimmed<T>(arg0: T, arg1: integer): $Pair<(T), (T)>
function trimmed<T>(arg0: T, arg1: $CharPredicate$Type): $Pair<(T), (T)>
function trimmed<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): $Pair<(T), (T)>
function parseIntOrDefault(arg0: string, arg1: integer): integer
function parseIntOrDefault(arg0: string, arg1: integer, arg2: integer): integer
function lineColumnAtIndex(arg0: charseq, arg1: integer): $Pair<(integer), (integer)>
function containsAny(arg0: charseq, arg1: $CharPredicate$Type): boolean
function containsAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): boolean
function indexOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function indexOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function indexOfAny(arg0: charseq, arg1: $CharPredicate$Type): integer
function lastIndexOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function lastIndexOfAny(arg0: charseq, arg1: $CharPredicate$Type): integer
function lastIndexOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function matchChars(arg0: charseq, arg1: charseq, arg2: integer): boolean
function matchChars(arg0: charseq, arg1: charseq, arg2: integer, arg3: boolean): boolean
function matchChars(arg0: charseq, arg1: charseq): boolean
function matchChars(arg0: charseq, arg1: charseq, arg2: boolean): boolean
function endOfLine(arg0: charseq, arg1: integer): integer
function validateIndex(arg0: integer, arg1: integer): void
function trimStart<T>(arg0: T, arg1: $CharPredicate$Type): T
function trimStart<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): T
function trimStart<T>(arg0: T): T
function trimStart<T>(arg0: T, arg1: integer): T
function toStringArray(...arg0: (charseq)[]): (string)[]
function countTrailingSpaceTab(arg0: charseq, arg1: integer, arg2: integer): integer
function countTrailingSpaceTab(arg0: charseq, arg1: integer): integer
function countTrailingSpaceTab(arg0: charseq): integer
function countTrailingNotSpaceTab(arg0: charseq, arg1: integer, arg2: integer): integer
function countTrailingNotSpaceTab(arg0: charseq, arg1: integer): integer
function countTrailingNotSpaceTab(arg0: charseq): integer
function countLeadingWhitespace(arg0: charseq, arg1: integer): integer
function countLeadingWhitespace(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingWhitespace(arg0: charseq): integer
function countTrailingWhitespace(arg0: charseq, arg1: integer): integer
function countTrailingWhitespace(arg0: charseq): integer
function countTrailingWhitespace(arg0: charseq, arg1: integer, arg2: integer): integer
function countOfNotWhitespace(arg0: charseq): integer
function countLeadingSpaceTab(arg0: charseq, arg1: integer): integer
function countLeadingSpaceTab(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingSpaceTab(arg0: charseq): integer
function countLeadingColumns(arg0: charseq, arg1: integer, arg2: $CharPredicate$Type): integer
function countLeadingNotSpaceTab(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingNotSpaceTab(arg0: charseq, arg1: integer): integer
function countLeadingNotSpaceTab(arg0: charseq): integer
function countTrailingNotWhitespace(arg0: charseq): integer
function countTrailingNotWhitespace(arg0: charseq, arg1: integer): integer
function countTrailingNotWhitespace(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingNotSpace(arg0: charseq, arg1: integer): integer
function countLeadingNotSpace(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingNotSpace(arg0: charseq): integer
function countTrailingNotSpace(arg0: charseq, arg1: integer, arg2: integer): integer
function countTrailingNotSpace(arg0: charseq, arg1: integer): integer
function countTrailingNotSpace(arg0: charseq): integer
function countLeadingNotWhitespace(arg0: charseq): integer
function countLeadingNotWhitespace(arg0: charseq, arg1: integer): integer
function countLeadingNotWhitespace(arg0: charseq, arg1: integer, arg2: integer): integer
function leadingBlankLinesRange(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): $Range
function leadingBlankLinesRange(arg0: charseq): $Range
function leadingBlankLinesRange(arg0: charseq, arg1: integer, arg2: integer): $Range
function leadingBlankLinesRange(arg0: charseq, arg1: integer): $Range
function endOfDelimitedByAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function getVisibleSpacesMap(): $Map<(character), (string)>
function matchedCharCountIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer): integer
function matchedCharCountIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function trailingBlankLinesRange(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): $Range
function trailingBlankLinesRange(arg0: charseq, arg1: integer, arg2: integer): $Range
function trailingBlankLinesRange(arg0: charseq, arg1: integer): $Range
function trailingBlankLinesRange(arg0: charseq): $Range
function isVisibleWhitespace(arg0: character): boolean
function columnsToNextTabStop(arg0: integer): integer
function startsWithWhitespace(arg0: charseq): boolean
function validateIndexInclusiveEnd(arg0: integer, arg1: integer): void
function parseUnsignedIntOrNull(arg0: string): integer
function parseUnsignedIntOrNull(arg0: string, arg1: integer): integer
function startOfDelimitedByAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function parseNumberPrefixOrNull(arg0: string, arg1: $Predicate$Type<(string)>): $Pair<(number), (string)>
function subSequenceBeforeAfter<T>(arg0: T, arg1: $Range$Type): $Pair<(T), (T)>
function parseUnsignedIntOrDefault(arg0: string, arg1: integer): integer
function parseUnsignedIntOrDefault(arg0: string, arg1: integer, arg2: integer): integer
function matchCharsReversedIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer): boolean
function endOfDelimitedByAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function blankLinesRemovedRanges(arg0: charseq, arg1: integer): $List<($Range)>
function blankLinesRemovedRanges(arg0: charseq): $List<($Range)>
function blankLinesRemovedRanges(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): $List<($Range)>
function blankLinesRemovedRanges(arg0: charseq, arg1: integer, arg2: integer): $List<($Range)>
function matchedCharCountReversed(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function matchedCharCountReversed(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer, arg4: boolean): integer
function matchedCharCountReversed(arg0: charseq, arg1: charseq, arg2: integer, arg3: boolean): integer
function matchedCharCountReversed(arg0: charseq, arg1: charseq, arg2: integer): integer
function matchCharsIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer): boolean
function matchCharsIgnoreCase(arg0: charseq, arg1: charseq): boolean
function matchedCharCountReversedIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function matchedCharCountReversedIgnoreCase(arg0: charseq, arg1: charseq, arg2: integer): integer
function startOfDelimitedByAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function splitList<T>(arg0: T, arg1: charseq, arg2: integer, arg3: integer): $List<(T)>
function splitList<T>(arg0: T, arg1: charseq, arg2: boolean, arg3: $CharPredicate$Type): $List<(T)>
function splitList<T>(arg0: T, arg1: charseq, arg2: integer, arg3: boolean, arg4: $CharPredicate$Type): $List<(T)>
function splitList<T>(arg0: T, arg1: charseq, arg2: integer, arg3: integer, arg4: $CharPredicate$Type): $List<(T)>
function splitList<T>(arg0: T, arg1: charseq): $List<(T)>
function isNotBlank(arg0: charseq): boolean
function isNotEmpty(arg0: charseq): boolean
function padEnd(arg0: charseq, arg1: integer): string
function padEnd(arg0: charseq, arg1: integer, arg2: character): string
function trimEnd<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): T
function trimEnd<T>(arg0: T): T
function trimEnd<T>(arg0: T, arg1: integer): T
function trimEnd<T>(arg0: T, arg1: $CharPredicate$Type): T
function startOfLine(arg0: charseq, arg1: integer): integer
function countLeading(arg0: charseq, arg1: $CharPredicate$Type): integer
function countLeading(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countLeading(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function subSequenceBefore<T>(arg0: T, arg1: $Range$Type): T
function indexOfNot(arg0: charseq, arg1: character, arg2: integer, arg3: integer): integer
function indexOfNot(arg0: charseq, arg1: character, arg2: integer): integer
function indexOfNot(arg0: charseq, arg1: character): integer
function countLeadingNot(arg0: charseq, arg1: $CharPredicate$Type): integer
function countLeadingNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countLeadingNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function subSequenceAfter<T>(arg0: T, arg1: $Range$Type): T
function lastIndexOfNot(arg0: charseq, arg1: character, arg2: integer): integer
function lastIndexOfNot(arg0: charseq, arg1: character): integer
function lastIndexOfNot(arg0: charseq, arg1: character, arg2: integer, arg3: integer): integer
function lastIndexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function lastIndexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function lastIndexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type): integer
function indexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type): integer
function indexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function indexOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function safeCharAt(arg0: charseq, arg1: integer): character
function countOfWhitespace(arg0: charseq): integer
function countOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type): integer
function countOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countOfAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function countLeadingSpace(arg0: charseq, arg1: integer): integer
function countLeadingSpace(arg0: charseq, arg1: integer, arg2: integer): integer
function countLeadingSpace(arg0: charseq): integer
function countTrailingNot(arg0: charseq, arg1: $CharPredicate$Type): integer
function countTrailingNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function countTrailingNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countTrailing(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countTrailing(arg0: charseq, arg1: $CharPredicate$Type): integer
function countTrailing(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function countOfSpaceTab(arg0: charseq): integer
function countOfAny(arg0: charseq, arg1: $CharPredicate$Type): integer
function countOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): integer
function countOfAny(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): integer
function countTrailingSpace(arg0: charseq, arg1: integer): integer
function countTrailingSpace(arg0: charseq, arg1: integer, arg2: integer): integer
function countTrailingSpace(arg0: charseq): integer
function trimStartRange(arg0: charseq, arg1: integer): $Range
function trimStartRange(arg0: charseq, arg1: $CharPredicate$Type): $Range
function trimStartRange(arg0: charseq, arg1: integer, arg2: $CharPredicate$Type): $Range
function trimStartRange(arg0: charseq): $Range
function trimEndRange(arg0: charseq, arg1: $CharPredicate$Type): $Range
function trimEndRange(arg0: charseq): $Range
function trimEndRange(arg0: charseq, arg1: integer): $Range
function trimEndRange(arg0: charseq, arg1: integer, arg2: $CharPredicate$Type): $Range
function trimRange(arg0: charseq): $Range
function trimRange(arg0: charseq, arg1: integer): $Range
function trimRange(arg0: charseq, arg1: $CharPredicate$Type): $Range
function trimRange(arg0: charseq, arg1: integer, arg2: $CharPredicate$Type): $Range
function countOfNotSpaceTab(arg0: charseq): integer
function trimmedStart<T>(arg0: T, arg1: $CharPredicate$Type): T
function trimmedStart<T>(arg0: T): T
function trimmedStart<T>(arg0: T, arg1: integer): T
function trimmedStart<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): T
function trimmedEnd<T>(arg0: T, arg1: integer, arg2: $CharPredicate$Type): T
function trimmedEnd<T>(arg0: T): T
function trimmedEnd<T>(arg0: T, arg1: $CharPredicate$Type): T
function trimmedEnd<T>(arg0: T, arg1: integer): T
function startOfDelimitedBy(arg0: charseq, arg1: charseq, arg2: integer): integer
function eolStartRange(arg0: charseq, arg1: integer): $Range
function trimmedEOL<T>(arg0: T): T
function endOfDelimitedBy(arg0: charseq, arg1: charseq, arg2: integer): integer
function startOfLineAnyEOL(arg0: charseq, arg1: integer): integer
function lineRangeAt(arg0: charseq, arg1: integer): $Range
function eolEndLength(arg0: charseq): integer
function eolEndLength(arg0: charseq, arg1: integer): integer
function eolEndRange(arg0: charseq, arg1: integer): $Range
function lineRangeAtAnyEOL(arg0: charseq, arg1: integer): $Range
function trimTailBlankLines<T>(arg0: T): T
function trimEOL<T>(arg0: T): T
function matchedCharCount(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer, arg4: boolean): integer
function matchedCharCount(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer, arg4: boolean, arg5: boolean): integer
function matchedCharCount(arg0: charseq, arg1: charseq, arg2: integer, arg3: integer): integer
function matchedCharCount(arg0: charseq, arg1: charseq, arg2: integer): integer
function matchedCharCount(arg0: charseq, arg1: charseq, arg2: integer, arg3: boolean): integer
function matchCharsReversed(arg0: charseq, arg1: charseq, arg2: integer): boolean
function matchCharsReversed(arg0: charseq, arg1: charseq, arg2: integer, arg3: boolean): boolean
function matchesIgnoreCase(arg0: charseq, arg1: charseq): boolean
function trimLeadBlankLines<T>(arg0: T): T
function startsWithSpaceTab(arg0: charseq): boolean
function endsWithWhitespace(arg0: charseq): boolean
function startsWithSpace(arg0: charseq): boolean
function endsWithSpace(arg0: charseq): boolean
function endsWithSpaceTab(arg0: charseq): boolean
function startsWithEOL(arg0: charseq): boolean
function endsWithAnyEOL(arg0: charseq): boolean
function startsWithAnyEOL(arg0: charseq): boolean
function endsWithEOL(arg0: charseq): boolean
function splitEOL<T>(arg0: T, arg1: (T)[], arg2: boolean): (T)[]
function splitEOL<T>(arg0: T, arg1: (T)[]): (T)[]
function splitListEOL<T>(arg0: T, arg1: boolean, arg2: $CharPredicate$Type): $List<(T)>
function splitListEOL<T>(arg0: T, arg1: boolean): $List<(T)>
function splitListEOL<T>(arg0: T): $List<(T)>
function indexOfAll(arg0: charseq, arg1: charseq): (integer)[]
function columnAtIndex(arg0: charseq, arg1: integer): integer
function compareReversed(arg0: charseq, arg1: charseq): integer
function containsAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer): boolean
function containsAnyNot(arg0: charseq, arg1: $CharPredicate$Type): boolean
function containsAnyNot(arg0: charseq, arg1: $CharPredicate$Type, arg2: integer, arg3: integer): boolean
function truncateTo(arg0: (integer)[], arg1: integer): (integer)[]
function validateStartEnd(arg0: integer, arg1: integer, arg2: integer): void
function parseNumberOrNull(arg0: string): number
function parseLongOrNull(arg0: string, arg1: integer): long
function parseLongOrNull(arg0: string): long
function parseIntOrNull(arg0: string): integer
function parseIntOrNull(arg0: string, arg1: integer): integer
function containedBy(arg0: $Collection$Type<(any)>, arg1: charseq): boolean
function containedBy<T>(arg0: (T)[], arg1: charseq): boolean
function endOfLineAnyEOL(arg0: charseq, arg1: integer): integer
function eolStartLength(arg0: charseq, arg1: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RichSequence$Type = ($RichSequence);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RichSequence_ = $RichSequence$Type;
}}
declare module "packages/com/vladsch/flexmark/util/data/$MutableDataHolder" {
import {$DataHolder, $DataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$DataHolder"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$DataValueFactory, $DataValueFactory$Type} from "packages/com/vladsch/flexmark/util/data/$DataValueFactory"
import {$DataSet, $DataSet$Type} from "packages/com/vladsch/flexmark/util/data/$DataSet"
import {$MutableDataSetter, $MutableDataSetter$Type} from "packages/com/vladsch/flexmark/util/data/$MutableDataSetter"
import {$DataKey, $DataKey$Type} from "packages/com/vladsch/flexmark/util/data/$DataKey"
import {$NullableDataKey, $NullableDataKey$Type} from "packages/com/vladsch/flexmark/util/data/$NullableDataKey"
import {$DataKeyBase, $DataKeyBase$Type} from "packages/com/vladsch/flexmark/util/data/$DataKeyBase"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $MutableDataHolder extends $DataHolder, $MutableDataSetter {

 "remove"(arg0: $DataKeyBase$Type<(any)>): $MutableDataHolder
/**
 * 
 * @deprecated
 */
 "get"<T>(arg0: $DataKey$Type<(T)>): T
 "clear"(): $MutableDataHolder
 "set"<T>(arg0: $DataKey$Type<(T)>, arg1: T): $MutableDataHolder
 "set"<T>(arg0: $NullableDataKey$Type<(T)>, arg1: T): $MutableDataHolder
 "setIn"(arg0: $MutableDataHolder$Type): $MutableDataHolder
 "setAll"(arg0: $DataHolder$Type): $MutableDataHolder
 "setFrom"(arg0: $MutableDataSetter$Type): $MutableDataHolder
 "getOrCompute"(arg0: $DataKeyBase$Type<(any)>, arg1: $DataValueFactory$Type<(any)>): any
 "contains"(arg0: $DataKeyBase$Type<(any)>): boolean
 "getAll"(): $Map<(any), (any)>
 "getKeys"(): $Collection<(any)>
 "toImmutable"(): $DataHolder
 "toMutable"(): $MutableDataHolder
 "toDataSet"(): $DataSet
}

export namespace $MutableDataHolder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MutableDataHolder$Type = ($MutableDataHolder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MutableDataHolder_ = $MutableDataHolder$Type;
}}
declare module "packages/com/vladsch/flexmark/util/sequence/builder/$ISegmentBuilder$Options" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ISegmentBuilder$Options extends $Enum<($ISegmentBuilder$Options)> {
static readonly "INCLUDE_ANCHORS": $ISegmentBuilder$Options
static readonly "TRACK_FIRST256": $ISegmentBuilder$Options


public static "values"(): ($ISegmentBuilder$Options)[]
public static "valueOf"(arg0: string): $ISegmentBuilder$Options
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ISegmentBuilder$Options$Type = (("include_anchors") | ("track_first256")) | ($ISegmentBuilder$Options);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ISegmentBuilder$Options_ = $ISegmentBuilder$Options$Type;
}}
declare module "packages/com/vladsch/flexmark/parser/delimiter/$DelimiterRun" {
import {$Text, $Text$Type} from "packages/com/vladsch/flexmark/ast/$Text"

export interface $DelimiterRun {

 "length"(): integer
 "getNode"(): $Text
 "canOpen"(): boolean
 "getNext"(): $DelimiterRun
 "getPrevious"(): $DelimiterRun
 "canClose"(): boolean
 "getDelimiterChar"(): character
}

export namespace $DelimiterRun {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DelimiterRun$Type = ($DelimiterRun);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DelimiterRun_ = $DelimiterRun$Type;
}}
declare module "packages/com/vladsch/flexmark/util/data/$MutableDataSet" {
import {$DataHolder, $DataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$DataHolder"
import {$DataValueFactory, $DataValueFactory$Type} from "packages/com/vladsch/flexmark/util/data/$DataValueFactory"
import {$DataSet, $DataSet$Type} from "packages/com/vladsch/flexmark/util/data/$DataSet"
import {$MutableDataHolder, $MutableDataHolder$Type} from "packages/com/vladsch/flexmark/util/data/$MutableDataHolder"
import {$DataKey, $DataKey$Type} from "packages/com/vladsch/flexmark/util/data/$DataKey"
import {$DataKeyBase, $DataKeyBase$Type} from "packages/com/vladsch/flexmark/util/data/$DataKeyBase"

export class $MutableDataSet extends $DataSet implements $MutableDataHolder {

constructor()
constructor(arg0: $DataHolder$Type)

public static "merge"(...arg0: ($DataHolder$Type)[]): $MutableDataSet
public "setIn"(arg0: $MutableDataHolder$Type): $MutableDataHolder
public "setAll"(arg0: $DataHolder$Type): $MutableDataSet
public "getOrCompute"(arg0: $DataKeyBase$Type<(any)>, arg1: $DataValueFactory$Type<(any)>): any
/**
 * 
 * @deprecated
 */
public "get"<T>(arg0: $DataKey$Type<(T)>): T
set "in"(value: $MutableDataHolder$Type)
set "all"(value: $DataHolder$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MutableDataSet$Type = ($MutableDataSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MutableDataSet_ = $MutableDataSet$Type;
}}
