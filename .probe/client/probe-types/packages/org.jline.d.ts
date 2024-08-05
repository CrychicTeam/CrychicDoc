declare module "packages/org/jline/reader/$Widget" {
import {$Binding, $Binding$Type} from "packages/org/jline/reader/$Binding"

export interface $Widget extends $Binding {

 "apply"(): boolean

(): boolean
}

export namespace $Widget {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Widget$Type = ($Widget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Widget_ = $Widget$Type;
}}
declare module "packages/org/jline/reader/$History" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$LineReader, $LineReader$Type} from "packages/org/jline/reader/$LineReader"
import {$ListIterator, $ListIterator$Type} from "packages/java/util/$ListIterator"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$History$Entry, $History$Entry$Type} from "packages/org/jline/reader/$History$Entry"

export interface $History extends $Iterable<($History$Entry)> {

 "moveToEnd"(): void
 "moveToLast"(): boolean
 "reverseIterator"(): $Iterator<($History$Entry)>
 "reverseIterator"(arg0: integer): $Iterator<($History$Entry)>
 "isPersistable"(arg0: $History$Entry$Type): boolean
 "moveToFirst"(): boolean
 "index"(): integer
 "add"(arg0: string): void
 "add"(arg0: $Instant$Type, arg1: string): void
 "get"(arg0: integer): string
 "append"(arg0: $Path$Type, arg1: boolean): void
 "load"(): void
 "isEmpty"(): boolean
 "size"(): integer
 "iterator"(arg0: integer): $ListIterator<($History$Entry)>
 "iterator"(): $ListIterator<($History$Entry)>
 "next"(): boolean
 "last"(): integer
 "first"(): integer
 "write"(arg0: $Path$Type, arg1: boolean): void
 "current"(): string
 "read"(arg0: $Path$Type, arg1: boolean): void
 "save"(): void
 "previous"(): boolean
 "attach"(arg0: $LineReader$Type): void
 "purge"(): void
 "moveTo"(arg0: integer): boolean
 "spliterator"(): $Spliterator<($History$Entry)>
 "forEach"(arg0: $Consumer$Type<(any)>): void
}

export namespace $History {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $History$Type = ($History);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $History_ = $History$Type;
}}
declare module "packages/org/jline/reader/$Binding" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Binding {

}

export namespace $Binding {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Binding$Type = ($Binding);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Binding_ = $Binding$Type;
}}
declare module "packages/org/jline/terminal/$MouseEvent$Modifier" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $MouseEvent$Modifier extends $Enum<($MouseEvent$Modifier)> {
static readonly "Shift": $MouseEvent$Modifier
static readonly "Alt": $MouseEvent$Modifier
static readonly "Control": $MouseEvent$Modifier


public static "values"(): ($MouseEvent$Modifier)[]
public static "valueOf"(arg0: string): $MouseEvent$Modifier
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MouseEvent$Modifier$Type = (("shift") | ("alt") | ("control")) | ($MouseEvent$Modifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MouseEvent$Modifier_ = $MouseEvent$Modifier$Type;
}}
declare module "packages/org/jline/reader/$LineReader$RegionType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $LineReader$RegionType extends $Enum<($LineReader$RegionType)> {
static readonly "NONE": $LineReader$RegionType
static readonly "CHAR": $LineReader$RegionType
static readonly "LINE": $LineReader$RegionType
static readonly "PASTE": $LineReader$RegionType


public static "values"(): ($LineReader$RegionType)[]
public static "valueOf"(arg0: string): $LineReader$RegionType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LineReader$RegionType$Type = (("line") | ("char") | ("none") | ("paste")) | ($LineReader$RegionType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LineReader$RegionType_ = $LineReader$RegionType$Type;
}}
declare module "packages/org/jline/terminal/$Terminal$Signal" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Terminal$Signal extends $Enum<($Terminal$Signal)> {
static readonly "INT": $Terminal$Signal
static readonly "QUIT": $Terminal$Signal
static readonly "TSTP": $Terminal$Signal
static readonly "CONT": $Terminal$Signal
static readonly "INFO": $Terminal$Signal
static readonly "WINCH": $Terminal$Signal


public static "values"(): ($Terminal$Signal)[]
public static "valueOf"(arg0: string): $Terminal$Signal
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Terminal$Signal$Type = (("quit") | ("cont") | ("tstp") | ("int") | ("info") | ("winch")) | ($Terminal$Signal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Terminal$Signal_ = $Terminal$Signal$Type;
}}
declare module "packages/org/jline/terminal/$Attributes$ControlFlag" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Attributes$ControlFlag extends $Enum<($Attributes$ControlFlag)> {
static readonly "CIGNORE": $Attributes$ControlFlag
static readonly "CS5": $Attributes$ControlFlag
static readonly "CS6": $Attributes$ControlFlag
static readonly "CS7": $Attributes$ControlFlag
static readonly "CS8": $Attributes$ControlFlag
static readonly "CSTOPB": $Attributes$ControlFlag
static readonly "CREAD": $Attributes$ControlFlag
static readonly "PARENB": $Attributes$ControlFlag
static readonly "PARODD": $Attributes$ControlFlag
static readonly "HUPCL": $Attributes$ControlFlag
static readonly "CLOCAL": $Attributes$ControlFlag
static readonly "CCTS_OFLOW": $Attributes$ControlFlag
static readonly "CRTS_IFLOW": $Attributes$ControlFlag
static readonly "CDTR_IFLOW": $Attributes$ControlFlag
static readonly "CDSR_OFLOW": $Attributes$ControlFlag
static readonly "CCAR_OFLOW": $Attributes$ControlFlag


public static "values"(): ($Attributes$ControlFlag)[]
public static "valueOf"(arg0: string): $Attributes$ControlFlag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Attributes$ControlFlag$Type = (("cignore") | ("cs5") | ("parodd") | ("cs7") | ("ccts_oflow") | ("cdsr_oflow") | ("cs6") | ("cstopb") | ("cs8") | ("crts_iflow") | ("cread") | ("ccar_oflow") | ("parenb") | ("cdtr_iflow") | ("hupcl") | ("clocal")) | ($Attributes$ControlFlag);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Attributes$ControlFlag_ = $Attributes$ControlFlag$Type;
}}
declare module "packages/org/jline/reader/$Candidate" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"

export class $Candidate implements $Comparable<($Candidate)> {

constructor(arg0: string)
constructor(arg0: string, arg1: string, arg2: string, arg3: string, arg4: string, arg5: string, arg6: boolean)

public "group"(): string
public "value"(): string
public "compareTo"(arg0: $Candidate$Type): integer
public "suffix"(): string
public "key"(): string
public "complete"(): boolean
public "descr"(): string
public "displ"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Candidate$Type = ($Candidate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Candidate_ = $Candidate$Type;
}}
declare module "packages/org/jline/reader/$Completer" {
import {$Candidate, $Candidate$Type} from "packages/org/jline/reader/$Candidate"
import {$LineReader, $LineReader$Type} from "packages/org/jline/reader/$LineReader"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ParsedLine, $ParsedLine$Type} from "packages/org/jline/reader/$ParsedLine"

export interface $Completer {

 "complete"(arg0: $LineReader$Type, arg1: $ParsedLine$Type, arg2: $List$Type<($Candidate$Type)>): void

(arg0: $LineReader$Type, arg1: $ParsedLine$Type, arg2: $List$Type<($Candidate$Type)>): void
}

export namespace $Completer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Completer$Type = ($Completer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Completer_ = $Completer$Type;
}}
declare module "packages/org/jline/reader/$Parser" {
import {$Parser$ParseContext, $Parser$ParseContext$Type} from "packages/org/jline/reader/$Parser$ParseContext"
import {$ParsedLine, $ParsedLine$Type} from "packages/org/jline/reader/$ParsedLine"

export interface $Parser {

 "parse"(arg0: string, arg1: integer, arg2: $Parser$ParseContext$Type): $ParsedLine
 "parse"(arg0: string, arg1: integer): $ParsedLine
 "isEscapeChar"(arg0: character): boolean

(arg0: string, arg1: integer, arg2: $Parser$ParseContext$Type): $ParsedLine
}

export namespace $Parser {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Parser$Type = ($Parser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Parser_ = $Parser$Type;
}}
declare module "packages/org/jline/reader/$LineReader" {
import {$LineReader$Option, $LineReader$Option$Type} from "packages/org/jline/reader/$LineReader$Option"
import {$KeyMap, $KeyMap$Type} from "packages/org/jline/keymap/$KeyMap"
import {$Binding, $Binding$Type} from "packages/org/jline/reader/$Binding"
import {$Parser, $Parser$Type} from "packages/org/jline/reader/$Parser"
import {$LineReader$RegionType, $LineReader$RegionType$Type} from "packages/org/jline/reader/$LineReader$RegionType"
import {$ParsedLine, $ParsedLine$Type} from "packages/org/jline/reader/$ParsedLine"
import {$Terminal, $Terminal$Type} from "packages/org/jline/terminal/$Terminal"
import {$Expander, $Expander$Type} from "packages/org/jline/reader/$Expander"
import {$Buffer, $Buffer$Type} from "packages/org/jline/reader/$Buffer"
import {$AttributedString, $AttributedString$Type} from "packages/org/jline/utils/$AttributedString"
import {$History, $History$Type} from "packages/org/jline/reader/$History"
import {$Widget, $Widget$Type} from "packages/org/jline/reader/$Widget"
import {$MouseEvent, $MouseEvent$Type} from "packages/org/jline/terminal/$MouseEvent"
import {$Highlighter, $Highlighter$Type} from "packages/org/jline/reader/$Highlighter"
import {$MaskingCallback, $MaskingCallback$Type} from "packages/org/jline/reader/$MaskingCallback"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $LineReader {

 "isSet"(arg0: $LineReader$Option$Type): boolean
 "readLine"(arg0: string, arg1: character): string
 "readLine"(arg0: string, arg1: character, arg2: string): string
 "readLine"(arg0: string, arg1: string, arg2: character, arg3: string): string
 "readLine"(arg0: string, arg1: string, arg2: $MaskingCallback$Type, arg3: string): string
 "readLine"(): string
 "readLine"(arg0: character): string
 "readLine"(arg0: string): string
 "option"(arg0: $LineReader$Option$Type, arg1: boolean): $LineReader
 "getKeys"(): $KeyMap<($Binding)>
 "getBuffer"(): $Buffer
 "getKeyMaps"(): $Map<(string), ($KeyMap<($Binding)>)>
 "callWidget"(arg0: string): void
 "getKeyMap"(): string
 "getVariables"(): $Map<(string), (any)>
 "getVariable"(arg0: string): any
 "setVariable"(arg0: string, arg1: any): void
 "isReading"(): boolean
 "defaultKeyMaps"(): $Map<(string), ($KeyMap<($Binding)>)>
 "getAppName"(): string
 "unsetOpt"(arg0: $LineReader$Option$Type): void
 "getWidgets"(): $Map<(string), ($Widget)>
 "runMacro"(arg0: string): void
 "readMouseEvent"(): $MouseEvent
 "getHistory"(): $History
 "getParser"(): $Parser
 "getBuiltinWidgets"(): $Map<(string), ($Widget)>
 "setOpt"(arg0: $LineReader$Option$Type): void
 "variable"(arg0: string, arg1: any): $LineReader
 "getExpander"(): $Expander
 "getHighlighter"(): $Highlighter
 "getParsedLine"(): $ParsedLine
 "getRegionActive"(): $LineReader$RegionType
 "getSearchTerm"(): string
 "setKeyMap"(arg0: string): boolean
 "getRegionMark"(): integer
 "getTerminal"(): $Terminal
 "printAbove"(arg0: $AttributedString$Type): void
 "printAbove"(arg0: string): void
}

export namespace $LineReader {
const PROP_SUPPORT_PARSEDLINE: string
const CALLBACK_INIT: string
const CALLBACK_FINISH: string
const CALLBACK_KEYMAP: string
const ACCEPT_AND_INFER_NEXT_HISTORY: string
const ACCEPT_AND_HOLD: string
const ACCEPT_LINE: string
const ACCEPT_LINE_AND_DOWN_HISTORY: string
const ARGUMENT_BASE: string
const BACKWARD_CHAR: string
const BACKWARD_DELETE_CHAR: string
const BACKWARD_DELETE_WORD: string
const BACKWARD_KILL_LINE: string
const BACKWARD_KILL_WORD: string
const BACKWARD_WORD: string
const BEEP: string
const BEGINNING_OF_BUFFER_OR_HISTORY: string
const BEGINNING_OF_HISTORY: string
const BEGINNING_OF_LINE: string
const BEGINNING_OF_LINE_HIST: string
const CAPITALIZE_WORD: string
const CHARACTER_SEARCH: string
const CHARACTER_SEARCH_BACKWARD: string
const CLEAR: string
const CLEAR_SCREEN: string
const COMPLETE_PREFIX: string
const COMPLETE_WORD: string
const COPY_PREV_WORD: string
const COPY_REGION_AS_KILL: string
const DELETE_CHAR: string
const DELETE_CHAR_OR_LIST: string
const DELETE_WORD: string
const DIGIT_ARGUMENT: string
const DO_LOWERCASE_VERSION: string
const DOWN_CASE_WORD: string
const DOWN_HISTORY: string
const DOWN_LINE: string
const DOWN_LINE_OR_HISTORY: string
const DOWN_LINE_OR_SEARCH: string
const EMACS_BACKWARD_WORD: string
const EMACS_EDITING_MODE: string
const EMACS_FORWARD_WORD: string
const END_OF_BUFFER_OR_HISTORY: string
const END_OF_HISTORY: string
const END_OF_LINE: string
const END_OF_LINE_HIST: string
const EXCHANGE_POINT_AND_MARK: string
const EXECUTE_NAMED_CMD: string
const EXPAND_HISTORY: string
const EXPAND_OR_COMPLETE: string
const EXPAND_OR_COMPLETE_PREFIX: string
const EXPAND_WORD: string
const FRESH_LINE: string
const FORWARD_CHAR: string
const FORWARD_WORD: string
const HISTORY_BEGINNING_SEARCH_BACKWARD: string
const HISTORY_BEGINNING_SEARCH_FORWARD: string
const HISTORY_INCREMENTAL_PATTERN_SEARCH_BACKWARD: string
const HISTORY_INCREMENTAL_PATTERN_SEARCH_FORWARD: string
const HISTORY_INCREMENTAL_SEARCH_BACKWARD: string
const HISTORY_INCREMENTAL_SEARCH_FORWARD: string
const HISTORY_SEARCH_BACKWARD: string
const HISTORY_SEARCH_FORWARD: string
const INSERT_CLOSE_CURLY: string
const INSERT_CLOSE_PAREN: string
const INSERT_CLOSE_SQUARE: string
const INFER_NEXT_HISTORY: string
const INSERT_COMMENT: string
const INSERT_LAST_WORD: string
const KILL_BUFFER: string
const KILL_LINE: string
const KILL_REGION: string
const KILL_WHOLE_LINE: string
const KILL_WORD: string
const LIST_CHOICES: string
const LIST_EXPAND: string
const MAGIC_SPACE: string
const MENU_EXPAND_OR_COMPLETE: string
const MENU_COMPLETE: string
const MENU_SELECT: string
const NEG_ARGUMENT: string
const OVERWRITE_MODE: string
const PUT_REPLACE_SELECTION: string
const QUOTED_INSERT: string
const READ_COMMAND: string
const RECURSIVE_EDIT: string
const REDISPLAY: string
const REDRAW_LINE: string
const REDO: string
const REVERSE_MENU_COMPLETE: string
const SELF_INSERT: string
const SELF_INSERT_UNMETA: string
const SEND_BREAK: string
const SET_LOCAL_HISTORY: string
const SET_MARK_COMMAND: string
const SPELL_WORD: string
const SPLIT_UNDO: string
const TRANSPOSE_CHARS: string
const TRANSPOSE_WORDS: string
const UNDEFINED_KEY: string
const UNDO: string
const UNIVERSAL_ARGUMENT: string
const UP_CASE_WORD: string
const UP_HISTORY: string
const UP_LINE: string
const UP_LINE_OR_HISTORY: string
const UP_LINE_OR_SEARCH: string
const VI_ADD_EOL: string
const VI_ADD_NEXT: string
const VI_BACKWARD_BLANK_WORD: string
const VI_BACKWARD_BLANK_WORD_END: string
const VI_BACKWARD_CHAR: string
const VI_BACKWARD_DELETE_CHAR: string
const VI_BACKWARD_KILL_WORD: string
const VI_BACKWARD_WORD: string
const VI_BACKWARD_WORD_END: string
const VI_BEGINNING_OF_LINE: string
const VI_CHANGE: string
const VI_CHANGE_EOL: string
const VI_CHANGE_WHOLE_LINE: string
const VI_CMD_MODE: string
const VI_DELETE: string
const VI_DELETE_CHAR: string
const VI_DIGIT_OR_BEGINNING_OF_LINE: string
const VI_DOWN_LINE_OR_HISTORY: string
const VI_END_OF_LINE: string
const VI_FETCH_HISTORY: string
const VI_FIND_NEXT_CHAR: string
const VI_FIND_NEXT_CHAR_SKIP: string
const VI_FIND_PREV_CHAR: string
const VI_FIND_PREV_CHAR_SKIP: string
const VI_FIRST_NON_BLANK: string
const VI_FORWARD_BLANK_WORD: string
const VI_FORWARD_BLANK_WORD_END: string
const VI_FORWARD_CHAR: string
const VI_FORWARD_WORD: string
const VI_FORWARD_WORD_END: string
const VI_GOTO_COLUMN: string
const VI_HISTORY_SEARCH_BACKWARD: string
const VI_HISTORY_SEARCH_FORWARD: string
const VI_INSERT: string
const VI_INSERT_BOL: string
const VI_INSERT_COMMENT: string
const VI_JOIN: string
const VI_KILL_EOL: string
const VI_KILL_LINE: string
const VI_MATCH_BRACKET: string
const VI_OPEN_LINE_ABOVE: string
const VI_OPEN_LINE_BELOW: string
const VI_OPER_SWAP_CASE: string
const VI_PUT_AFTER: string
const VI_PUT_BEFORE: string
const VI_QUOTED_INSERT: string
const VI_REPEAT_CHANGE: string
const VI_REPEAT_FIND: string
const VI_REPEAT_SEARCH: string
const VI_REPLACE: string
const VI_REPLACE_CHARS: string
const VI_REV_REPEAT_FIND: string
const VI_REV_REPEAT_SEARCH: string
const VI_SET_BUFFER: string
const VI_SUBSTITUTE: string
const VI_SWAP_CASE: string
const VI_UNDO_CHANGE: string
const VI_UP_LINE_OR_HISTORY: string
const VI_YANK: string
const VI_YANK_EOL: string
const VI_YANK_WHOLE_LINE: string
const VISUAL_LINE_MODE: string
const VISUAL_MODE: string
const WHAT_CURSOR_POSITION: string
const YANK: string
const YANK_POP: string
const MOUSE: string
const FOCUS_IN: string
const FOCUS_OUT: string
const BEGIN_PASTE: string
const VICMD: string
const VIINS: string
const VIOPP: string
const VISUAL: string
const MAIN: string
const EMACS: string
const SAFE: string
const MENU: string
const BIND_TTY_SPECIAL_CHARS: string
const COMMENT_BEGIN: string
const BELL_STYLE: string
const PREFER_VISIBLE_BELL: string
const LIST_MAX: string
const DISABLE_HISTORY: string
const DISABLE_COMPLETION: string
const EDITING_MODE: string
const KEYMAP: string
const BLINK_MATCHING_PAREN: string
const WORDCHARS: string
const REMOVE_SUFFIX_CHARS: string
const SEARCH_TERMINATORS: string
const ERRORS: string
const OTHERS_GROUP_NAME: string
const ORIGINAL_GROUP_NAME: string
const COMPLETION_STYLE_GROUP: string
const COMPLETION_STYLE_SELECTION: string
const COMPLETION_STYLE_DESCRIPTION: string
const COMPLETION_STYLE_STARTING: string
const SECONDARY_PROMPT_PATTERN: string
const LINE_OFFSET: string
const AMBIGUOUS_BINDING: string
const HISTORY_IGNORE: string
const HISTORY_FILE: string
const HISTORY_SIZE: string
const HISTORY_FILE_SIZE: string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LineReader$Type = ($LineReader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LineReader_ = $LineReader$Type;
}}
declare module "packages/org/jline/reader/$LineReader$Option" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $LineReader$Option extends $Enum<($LineReader$Option)> {
static readonly "COMPLETE_IN_WORD": $LineReader$Option
static readonly "DISABLE_EVENT_EXPANSION": $LineReader$Option
static readonly "HISTORY_VERIFY": $LineReader$Option
static readonly "HISTORY_IGNORE_SPACE": $LineReader$Option
static readonly "HISTORY_IGNORE_DUPS": $LineReader$Option
static readonly "HISTORY_REDUCE_BLANKS": $LineReader$Option
static readonly "HISTORY_BEEP": $LineReader$Option
static readonly "HISTORY_INCREMENTAL": $LineReader$Option
static readonly "HISTORY_TIMESTAMPED": $LineReader$Option
static readonly "AUTO_GROUP": $LineReader$Option
static readonly "AUTO_MENU": $LineReader$Option
static readonly "AUTO_LIST": $LineReader$Option
static readonly "RECOGNIZE_EXACT": $LineReader$Option
static readonly "GROUP": $LineReader$Option
static readonly "CASE_INSENSITIVE": $LineReader$Option
static readonly "LIST_AMBIGUOUS": $LineReader$Option
static readonly "LIST_PACKED": $LineReader$Option
static readonly "LIST_ROWS_FIRST": $LineReader$Option
static readonly "GLOB_COMPLETE": $LineReader$Option
static readonly "MENU_COMPLETE": $LineReader$Option
static readonly "AUTO_FRESH_LINE": $LineReader$Option
static readonly "DELAY_LINE_WRAP": $LineReader$Option
static readonly "AUTO_PARAM_SLASH": $LineReader$Option
static readonly "AUTO_REMOVE_SLASH": $LineReader$Option
static readonly "INSERT_TAB": $LineReader$Option
static readonly "MOUSE": $LineReader$Option
static readonly "DISABLE_HIGHLIGHTER": $LineReader$Option
static readonly "BRACKETED_PASTE": $LineReader$Option
static readonly "ERASE_LINE_ON_FINISH": $LineReader$Option
static readonly "CASE_INSENSITIVE_SEARCH": $LineReader$Option


public static "values"(): ($LineReader$Option)[]
public static "valueOf"(arg0: string): $LineReader$Option
public "isDef"(): boolean
get "def"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LineReader$Option$Type = (("history_ignore_space") | ("menu_complete") | ("history_incremental") | ("list_packed") | ("disable_highlighter") | ("mouse") | ("history_verify") | ("history_timestamped") | ("insert_tab") | ("history_ignore_dups") | ("list_ambiguous") | ("recognize_exact") | ("auto_remove_slash") | ("history_reduce_blanks") | ("auto_param_slash") | ("auto_group") | ("auto_fresh_line") | ("group") | ("delay_line_wrap") | ("case_insensitive_search") | ("case_insensitive") | ("erase_line_on_finish") | ("auto_menu") | ("list_rows_first") | ("auto_list") | ("disable_event_expansion") | ("complete_in_word") | ("glob_complete") | ("bracketed_paste") | ("history_beep")) | ($LineReader$Option);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LineReader$Option_ = $LineReader$Option$Type;
}}
declare module "packages/org/jline/terminal/$Attributes$ControlChar" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Attributes$ControlChar extends $Enum<($Attributes$ControlChar)> {
static readonly "VEOF": $Attributes$ControlChar
static readonly "VEOL": $Attributes$ControlChar
static readonly "VEOL2": $Attributes$ControlChar
static readonly "VERASE": $Attributes$ControlChar
static readonly "VWERASE": $Attributes$ControlChar
static readonly "VKILL": $Attributes$ControlChar
static readonly "VREPRINT": $Attributes$ControlChar
static readonly "VINTR": $Attributes$ControlChar
static readonly "VQUIT": $Attributes$ControlChar
static readonly "VSUSP": $Attributes$ControlChar
static readonly "VDSUSP": $Attributes$ControlChar
static readonly "VSTART": $Attributes$ControlChar
static readonly "VSTOP": $Attributes$ControlChar
static readonly "VLNEXT": $Attributes$ControlChar
static readonly "VDISCARD": $Attributes$ControlChar
static readonly "VMIN": $Attributes$ControlChar
static readonly "VTIME": $Attributes$ControlChar
static readonly "VSTATUS": $Attributes$ControlChar


public static "values"(): ($Attributes$ControlChar)[]
public static "valueOf"(arg0: string): $Attributes$ControlChar
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Attributes$ControlChar$Type = (("vsusp") | ("vdiscard") | ("vkill") | ("vstatus") | ("veol2") | ("vlnext") | ("vintr") | ("vstart") | ("vmin") | ("vstop") | ("vreprint") | ("verase") | ("veof") | ("vwerase") | ("vquit") | ("vtime") | ("veol") | ("vdsusp")) | ($Attributes$ControlChar);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Attributes$ControlChar_ = $Attributes$ControlChar$Type;
}}
declare module "packages/org/jline/reader/$History$Entry" {
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"

export interface $History$Entry {

 "index"(): integer
 "line"(): string
 "time"(): $Instant
}

export namespace $History$Entry {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $History$Entry$Type = ($History$Entry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $History$Entry_ = $History$Entry$Type;
}}
declare module "packages/org/jline/reader/$Parser$ParseContext" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Parser$ParseContext extends $Enum<($Parser$ParseContext)> {
static readonly "UNSPECIFIED": $Parser$ParseContext
static readonly "ACCEPT_LINE": $Parser$ParseContext
static readonly "COMPLETE": $Parser$ParseContext
static readonly "SECONDARY_PROMPT": $Parser$ParseContext


public static "values"(): ($Parser$ParseContext)[]
public static "valueOf"(arg0: string): $Parser$ParseContext
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Parser$ParseContext$Type = (("secondary_prompt") | ("unspecified") | ("complete") | ("accept_line")) | ($Parser$ParseContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Parser$ParseContext_ = $Parser$ParseContext$Type;
}}
declare module "packages/org/jline/terminal/$Size" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Size {

constructor()
constructor(arg0: integer, arg1: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "copy"(arg0: $Size$Type): void
public "setRows"(arg0: integer): void
public "getRows"(): integer
public "getColumns"(): integer
public "setColumns"(arg0: integer): void
public "cursorPos"(arg0: integer, arg1: integer): integer
set "rows"(value: integer)
get "rows"(): integer
get "columns"(): integer
set "columns"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Size$Type = ($Size);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Size_ = $Size$Type;
}}
declare module "packages/org/jline/terminal/$MouseEvent$Button" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $MouseEvent$Button extends $Enum<($MouseEvent$Button)> {
static readonly "NoButton": $MouseEvent$Button
static readonly "Button1": $MouseEvent$Button
static readonly "Button2": $MouseEvent$Button
static readonly "Button3": $MouseEvent$Button
static readonly "WheelUp": $MouseEvent$Button
static readonly "WheelDown": $MouseEvent$Button


public static "values"(): ($MouseEvent$Button)[]
public static "valueOf"(arg0: string): $MouseEvent$Button
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MouseEvent$Button$Type = (("button2") | ("button3") | ("nobutton") | ("wheeldown") | ("wheelup") | ("button1")) | ($MouseEvent$Button);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MouseEvent$Button_ = $MouseEvent$Button$Type;
}}
declare module "packages/org/jline/utils/$NonBlockingReader" {
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"

export class $NonBlockingReader extends $Reader {
static readonly "EOF": integer
static readonly "READ_EXPIRED": integer

constructor()

public "shutdown"(): void
public "read"(arg0: (character)[], arg1: integer, arg2: integer): integer
public "read"(arg0: long): integer
public "read"(): integer
public "available"(): integer
public "peek"(arg0: long): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NonBlockingReader$Type = ($NonBlockingReader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NonBlockingReader_ = $NonBlockingReader$Type;
}}
declare module "packages/org/jline/reader/$Buffer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Buffer {

 "length"(): integer
 "toString"(): string
 "clear"(): boolean
 "substring"(arg0: integer): string
 "substring"(arg0: integer, arg1: integer): string
 "write"(arg0: integer, arg1: boolean): void
 "write"(arg0: charseq, arg1: boolean): void
 "write"(arg0: charseq): void
 "write"(arg0: integer): void
 "delete"(arg0: integer): integer
 "delete"(): boolean
 "copy"(): $Buffer
 "nextChar"(): integer
 "up"(): boolean
 "cursor"(): integer
 "cursor"(arg0: integer): boolean
 "move"(arg0: integer): integer
 "down"(): boolean
 "copyFrom"(arg0: $Buffer$Type): void
 "backspace"(): boolean
 "backspace"(arg0: integer): integer
 "currChar"(): integer
 "currChar"(arg0: integer): boolean
 "prevChar"(): integer
 "upToCursor"(): string
 "atChar"(arg0: integer): integer
 "moveXY"(arg0: integer, arg1: integer): boolean
}

export namespace $Buffer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Buffer$Type = ($Buffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Buffer_ = $Buffer$Type;
}}
declare module "packages/org/jline/terminal/$Attributes$OutputFlag" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Attributes$OutputFlag extends $Enum<($Attributes$OutputFlag)> {
static readonly "OPOST": $Attributes$OutputFlag
static readonly "ONLCR": $Attributes$OutputFlag
static readonly "OXTABS": $Attributes$OutputFlag
static readonly "ONOEOT": $Attributes$OutputFlag
static readonly "OCRNL": $Attributes$OutputFlag
static readonly "ONOCR": $Attributes$OutputFlag
static readonly "ONLRET": $Attributes$OutputFlag
static readonly "OFILL": $Attributes$OutputFlag
static readonly "NLDLY": $Attributes$OutputFlag
static readonly "TABDLY": $Attributes$OutputFlag
static readonly "CRDLY": $Attributes$OutputFlag
static readonly "FFDLY": $Attributes$OutputFlag
static readonly "BSDLY": $Attributes$OutputFlag
static readonly "VTDLY": $Attributes$OutputFlag
static readonly "OFDEL": $Attributes$OutputFlag


public static "values"(): ($Attributes$OutputFlag)[]
public static "valueOf"(arg0: string): $Attributes$OutputFlag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Attributes$OutputFlag$Type = (("vtdly") | ("crdly") | ("tabdly") | ("onocr") | ("onlcr") | ("nldly") | ("onoeot") | ("ocrnl") | ("ofdel") | ("bsdly") | ("oxtabs") | ("ffdly") | ("onlret") | ("ofill") | ("opost")) | ($Attributes$OutputFlag);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Attributes$OutputFlag_ = $Attributes$OutputFlag$Type;
}}
declare module "packages/org/jline/reader/$Highlighter" {
import {$LineReader, $LineReader$Type} from "packages/org/jline/reader/$LineReader"
import {$AttributedString, $AttributedString$Type} from "packages/org/jline/utils/$AttributedString"

export interface $Highlighter {

 "highlight"(arg0: $LineReader$Type, arg1: string): $AttributedString

(arg0: $LineReader$Type, arg1: string): $AttributedString
}

export namespace $Highlighter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Highlighter$Type = ($Highlighter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Highlighter_ = $Highlighter$Type;
}}
declare module "packages/org/jline/utils/$InfoCmp$Capability" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $InfoCmp$Capability extends $Enum<($InfoCmp$Capability)> {
static readonly "auto_left_margin": $InfoCmp$Capability
static readonly "auto_right_margin": $InfoCmp$Capability
static readonly "back_color_erase": $InfoCmp$Capability
static readonly "can_change": $InfoCmp$Capability
static readonly "ceol_standout_glitch": $InfoCmp$Capability
static readonly "col_addr_glitch": $InfoCmp$Capability
static readonly "cpi_changes_res": $InfoCmp$Capability
static readonly "cr_cancels_micro_mode": $InfoCmp$Capability
static readonly "dest_tabs_magic_smso": $InfoCmp$Capability
static readonly "eat_newline_glitch": $InfoCmp$Capability
static readonly "erase_overstrike": $InfoCmp$Capability
static readonly "generic_type": $InfoCmp$Capability
static readonly "hard_copy": $InfoCmp$Capability
static readonly "hard_cursor": $InfoCmp$Capability
static readonly "has_meta_key": $InfoCmp$Capability
static readonly "has_print_wheel": $InfoCmp$Capability
static readonly "has_status_line": $InfoCmp$Capability
static readonly "hue_lightness_saturation": $InfoCmp$Capability
static readonly "insert_null_glitch": $InfoCmp$Capability
static readonly "lpi_changes_res": $InfoCmp$Capability
static readonly "memory_above": $InfoCmp$Capability
static readonly "memory_below": $InfoCmp$Capability
static readonly "move_insert_mode": $InfoCmp$Capability
static readonly "move_standout_mode": $InfoCmp$Capability
static readonly "needs_xon_xoff": $InfoCmp$Capability
static readonly "no_esc_ctlc": $InfoCmp$Capability
static readonly "no_pad_char": $InfoCmp$Capability
static readonly "non_dest_scroll_region": $InfoCmp$Capability
static readonly "non_rev_rmcup": $InfoCmp$Capability
static readonly "over_strike": $InfoCmp$Capability
static readonly "prtr_silent": $InfoCmp$Capability
static readonly "row_addr_glitch": $InfoCmp$Capability
static readonly "semi_auto_right_margin": $InfoCmp$Capability
static readonly "status_line_esc_ok": $InfoCmp$Capability
static readonly "tilde_glitch": $InfoCmp$Capability
static readonly "transparent_underline": $InfoCmp$Capability
static readonly "xon_xoff": $InfoCmp$Capability
static readonly "columns": $InfoCmp$Capability
static readonly "init_tabs": $InfoCmp$Capability
static readonly "label_height": $InfoCmp$Capability
static readonly "label_width": $InfoCmp$Capability
static readonly "lines": $InfoCmp$Capability
static readonly "lines_of_memory": $InfoCmp$Capability
static readonly "magic_cookie_glitch": $InfoCmp$Capability
static readonly "max_attributes": $InfoCmp$Capability
static readonly "max_colors": $InfoCmp$Capability
static readonly "max_pairs": $InfoCmp$Capability
static readonly "maximum_windows": $InfoCmp$Capability
static readonly "no_color_video": $InfoCmp$Capability
static readonly "num_labels": $InfoCmp$Capability
static readonly "padding_baud_rate": $InfoCmp$Capability
static readonly "virtual_terminal": $InfoCmp$Capability
static readonly "width_status_line": $InfoCmp$Capability
static readonly "bit_image_entwining": $InfoCmp$Capability
static readonly "bit_image_type": $InfoCmp$Capability
static readonly "buffer_capacity": $InfoCmp$Capability
static readonly "buttons": $InfoCmp$Capability
static readonly "dot_horz_spacing": $InfoCmp$Capability
static readonly "dot_vert_spacing": $InfoCmp$Capability
static readonly "max_micro_address": $InfoCmp$Capability
static readonly "max_micro_jump": $InfoCmp$Capability
static readonly "micro_col_size": $InfoCmp$Capability
static readonly "micro_line_size": $InfoCmp$Capability
static readonly "number_of_pins": $InfoCmp$Capability
static readonly "output_res_char": $InfoCmp$Capability
static readonly "output_res_horz_inch": $InfoCmp$Capability
static readonly "output_res_line": $InfoCmp$Capability
static readonly "output_res_vert_inch": $InfoCmp$Capability
static readonly "print_rate": $InfoCmp$Capability
static readonly "wide_char_size": $InfoCmp$Capability
static readonly "acs_chars": $InfoCmp$Capability
static readonly "back_tab": $InfoCmp$Capability
static readonly "bell": $InfoCmp$Capability
static readonly "carriage_return": $InfoCmp$Capability
static readonly "change_char_pitch": $InfoCmp$Capability
static readonly "change_line_pitch": $InfoCmp$Capability
static readonly "change_res_horz": $InfoCmp$Capability
static readonly "change_res_vert": $InfoCmp$Capability
static readonly "change_scroll_region": $InfoCmp$Capability
static readonly "char_padding": $InfoCmp$Capability
static readonly "clear_all_tabs": $InfoCmp$Capability
static readonly "clear_margins": $InfoCmp$Capability
static readonly "clear_screen": $InfoCmp$Capability
static readonly "clr_bol": $InfoCmp$Capability
static readonly "clr_eol": $InfoCmp$Capability
static readonly "clr_eos": $InfoCmp$Capability
static readonly "column_address": $InfoCmp$Capability
static readonly "command_character": $InfoCmp$Capability
static readonly "create_window": $InfoCmp$Capability
static readonly "cursor_address": $InfoCmp$Capability
static readonly "cursor_down": $InfoCmp$Capability
static readonly "cursor_home": $InfoCmp$Capability
static readonly "cursor_invisible": $InfoCmp$Capability
static readonly "cursor_left": $InfoCmp$Capability
static readonly "cursor_mem_address": $InfoCmp$Capability
static readonly "cursor_normal": $InfoCmp$Capability
static readonly "cursor_right": $InfoCmp$Capability
static readonly "cursor_to_ll": $InfoCmp$Capability
static readonly "cursor_up": $InfoCmp$Capability
static readonly "cursor_visible": $InfoCmp$Capability
static readonly "define_char": $InfoCmp$Capability
static readonly "delete_character": $InfoCmp$Capability
static readonly "delete_line": $InfoCmp$Capability
static readonly "dial_phone": $InfoCmp$Capability
static readonly "dis_status_line": $InfoCmp$Capability
static readonly "display_clock": $InfoCmp$Capability
static readonly "down_half_line": $InfoCmp$Capability
static readonly "ena_acs": $InfoCmp$Capability
static readonly "enter_alt_charset_mode": $InfoCmp$Capability
static readonly "enter_am_mode": $InfoCmp$Capability
static readonly "enter_blink_mode": $InfoCmp$Capability
static readonly "enter_bold_mode": $InfoCmp$Capability
static readonly "enter_ca_mode": $InfoCmp$Capability
static readonly "enter_delete_mode": $InfoCmp$Capability
static readonly "enter_dim_mode": $InfoCmp$Capability
static readonly "enter_doublewide_mode": $InfoCmp$Capability
static readonly "enter_draft_quality": $InfoCmp$Capability
static readonly "enter_insert_mode": $InfoCmp$Capability
static readonly "enter_italics_mode": $InfoCmp$Capability
static readonly "enter_leftward_mode": $InfoCmp$Capability
static readonly "enter_micro_mode": $InfoCmp$Capability
static readonly "enter_near_letter_quality": $InfoCmp$Capability
static readonly "enter_normal_quality": $InfoCmp$Capability
static readonly "enter_protected_mode": $InfoCmp$Capability
static readonly "enter_reverse_mode": $InfoCmp$Capability
static readonly "enter_secure_mode": $InfoCmp$Capability
static readonly "enter_shadow_mode": $InfoCmp$Capability
static readonly "enter_standout_mode": $InfoCmp$Capability
static readonly "enter_subscript_mode": $InfoCmp$Capability
static readonly "enter_superscript_mode": $InfoCmp$Capability
static readonly "enter_underline_mode": $InfoCmp$Capability
static readonly "enter_upward_mode": $InfoCmp$Capability
static readonly "enter_xon_mode": $InfoCmp$Capability
static readonly "erase_chars": $InfoCmp$Capability
static readonly "exit_alt_charset_mode": $InfoCmp$Capability
static readonly "exit_am_mode": $InfoCmp$Capability
static readonly "exit_attribute_mode": $InfoCmp$Capability
static readonly "exit_ca_mode": $InfoCmp$Capability
static readonly "exit_delete_mode": $InfoCmp$Capability
static readonly "exit_doublewide_mode": $InfoCmp$Capability
static readonly "exit_insert_mode": $InfoCmp$Capability
static readonly "exit_italics_mode": $InfoCmp$Capability
static readonly "exit_leftward_mode": $InfoCmp$Capability
static readonly "exit_micro_mode": $InfoCmp$Capability
static readonly "exit_shadow_mode": $InfoCmp$Capability
static readonly "exit_standout_mode": $InfoCmp$Capability
static readonly "exit_subscript_mode": $InfoCmp$Capability
static readonly "exit_superscript_mode": $InfoCmp$Capability
static readonly "exit_underline_mode": $InfoCmp$Capability
static readonly "exit_upward_mode": $InfoCmp$Capability
static readonly "exit_xon_mode": $InfoCmp$Capability
static readonly "fixed_pause": $InfoCmp$Capability
static readonly "flash_hook": $InfoCmp$Capability
static readonly "flash_screen": $InfoCmp$Capability
static readonly "form_feed": $InfoCmp$Capability
static readonly "from_status_line": $InfoCmp$Capability
static readonly "goto_window": $InfoCmp$Capability
static readonly "hangup": $InfoCmp$Capability
static readonly "init_1string": $InfoCmp$Capability
static readonly "init_2string": $InfoCmp$Capability
static readonly "init_3string": $InfoCmp$Capability
static readonly "init_file": $InfoCmp$Capability
static readonly "init_prog": $InfoCmp$Capability
static readonly "initialize_color": $InfoCmp$Capability
static readonly "initialize_pair": $InfoCmp$Capability
static readonly "insert_character": $InfoCmp$Capability
static readonly "insert_line": $InfoCmp$Capability
static readonly "insert_padding": $InfoCmp$Capability
static readonly "key_a1": $InfoCmp$Capability
static readonly "key_a3": $InfoCmp$Capability
static readonly "key_b2": $InfoCmp$Capability
static readonly "key_backspace": $InfoCmp$Capability
static readonly "key_beg": $InfoCmp$Capability
static readonly "key_btab": $InfoCmp$Capability
static readonly "key_c1": $InfoCmp$Capability
static readonly "key_c3": $InfoCmp$Capability
static readonly "key_cancel": $InfoCmp$Capability
static readonly "key_catab": $InfoCmp$Capability
static readonly "key_clear": $InfoCmp$Capability
static readonly "key_close": $InfoCmp$Capability
static readonly "key_command": $InfoCmp$Capability
static readonly "key_copy": $InfoCmp$Capability
static readonly "key_create": $InfoCmp$Capability
static readonly "key_ctab": $InfoCmp$Capability
static readonly "key_dc": $InfoCmp$Capability
static readonly "key_dl": $InfoCmp$Capability
static readonly "key_down": $InfoCmp$Capability
static readonly "key_eic": $InfoCmp$Capability
static readonly "key_end": $InfoCmp$Capability
static readonly "key_enter": $InfoCmp$Capability
static readonly "key_eol": $InfoCmp$Capability
static readonly "key_eos": $InfoCmp$Capability
static readonly "key_exit": $InfoCmp$Capability
static readonly "key_f0": $InfoCmp$Capability
static readonly "key_f1": $InfoCmp$Capability
static readonly "key_f10": $InfoCmp$Capability
static readonly "key_f11": $InfoCmp$Capability
static readonly "key_f12": $InfoCmp$Capability
static readonly "key_f13": $InfoCmp$Capability
static readonly "key_f14": $InfoCmp$Capability
static readonly "key_f15": $InfoCmp$Capability
static readonly "key_f16": $InfoCmp$Capability
static readonly "key_f17": $InfoCmp$Capability
static readonly "key_f18": $InfoCmp$Capability
static readonly "key_f19": $InfoCmp$Capability
static readonly "key_f2": $InfoCmp$Capability
static readonly "key_f20": $InfoCmp$Capability
static readonly "key_f21": $InfoCmp$Capability
static readonly "key_f22": $InfoCmp$Capability
static readonly "key_f23": $InfoCmp$Capability
static readonly "key_f24": $InfoCmp$Capability
static readonly "key_f25": $InfoCmp$Capability
static readonly "key_f26": $InfoCmp$Capability
static readonly "key_f27": $InfoCmp$Capability
static readonly "key_f28": $InfoCmp$Capability
static readonly "key_f29": $InfoCmp$Capability
static readonly "key_f3": $InfoCmp$Capability
static readonly "key_f30": $InfoCmp$Capability
static readonly "key_f31": $InfoCmp$Capability
static readonly "key_f32": $InfoCmp$Capability
static readonly "key_f33": $InfoCmp$Capability
static readonly "key_f34": $InfoCmp$Capability
static readonly "key_f35": $InfoCmp$Capability
static readonly "key_f36": $InfoCmp$Capability
static readonly "key_f37": $InfoCmp$Capability
static readonly "key_f38": $InfoCmp$Capability
static readonly "key_f39": $InfoCmp$Capability
static readonly "key_f4": $InfoCmp$Capability
static readonly "key_f40": $InfoCmp$Capability
static readonly "key_f41": $InfoCmp$Capability
static readonly "key_f42": $InfoCmp$Capability
static readonly "key_f43": $InfoCmp$Capability
static readonly "key_f44": $InfoCmp$Capability
static readonly "key_f45": $InfoCmp$Capability
static readonly "key_f46": $InfoCmp$Capability
static readonly "key_f47": $InfoCmp$Capability
static readonly "key_f48": $InfoCmp$Capability
static readonly "key_f49": $InfoCmp$Capability
static readonly "key_f5": $InfoCmp$Capability
static readonly "key_f50": $InfoCmp$Capability
static readonly "key_f51": $InfoCmp$Capability
static readonly "key_f52": $InfoCmp$Capability
static readonly "key_f53": $InfoCmp$Capability
static readonly "key_f54": $InfoCmp$Capability
static readonly "key_f55": $InfoCmp$Capability
static readonly "key_f56": $InfoCmp$Capability
static readonly "key_f57": $InfoCmp$Capability
static readonly "key_f58": $InfoCmp$Capability
static readonly "key_f59": $InfoCmp$Capability
static readonly "key_f6": $InfoCmp$Capability
static readonly "key_f60": $InfoCmp$Capability
static readonly "key_f61": $InfoCmp$Capability
static readonly "key_f62": $InfoCmp$Capability
static readonly "key_f63": $InfoCmp$Capability
static readonly "key_f7": $InfoCmp$Capability
static readonly "key_f8": $InfoCmp$Capability
static readonly "key_f9": $InfoCmp$Capability
static readonly "key_find": $InfoCmp$Capability
static readonly "key_help": $InfoCmp$Capability
static readonly "key_home": $InfoCmp$Capability
static readonly "key_ic": $InfoCmp$Capability
static readonly "key_il": $InfoCmp$Capability
static readonly "key_left": $InfoCmp$Capability
static readonly "key_ll": $InfoCmp$Capability
static readonly "key_mark": $InfoCmp$Capability
static readonly "key_message": $InfoCmp$Capability
static readonly "key_move": $InfoCmp$Capability
static readonly "key_next": $InfoCmp$Capability
static readonly "key_npage": $InfoCmp$Capability
static readonly "key_open": $InfoCmp$Capability
static readonly "key_options": $InfoCmp$Capability
static readonly "key_ppage": $InfoCmp$Capability
static readonly "key_previous": $InfoCmp$Capability
static readonly "key_print": $InfoCmp$Capability
static readonly "key_redo": $InfoCmp$Capability
static readonly "key_reference": $InfoCmp$Capability
static readonly "key_refresh": $InfoCmp$Capability
static readonly "key_replace": $InfoCmp$Capability
static readonly "key_restart": $InfoCmp$Capability
static readonly "key_resume": $InfoCmp$Capability
static readonly "key_right": $InfoCmp$Capability
static readonly "key_save": $InfoCmp$Capability
static readonly "key_sbeg": $InfoCmp$Capability
static readonly "key_scancel": $InfoCmp$Capability
static readonly "key_scommand": $InfoCmp$Capability
static readonly "key_scopy": $InfoCmp$Capability
static readonly "key_screate": $InfoCmp$Capability
static readonly "key_sdc": $InfoCmp$Capability
static readonly "key_sdl": $InfoCmp$Capability
static readonly "key_select": $InfoCmp$Capability
static readonly "key_send": $InfoCmp$Capability
static readonly "key_seol": $InfoCmp$Capability
static readonly "key_sexit": $InfoCmp$Capability
static readonly "key_sf": $InfoCmp$Capability
static readonly "key_sfind": $InfoCmp$Capability
static readonly "key_shelp": $InfoCmp$Capability
static readonly "key_shome": $InfoCmp$Capability
static readonly "key_sic": $InfoCmp$Capability
static readonly "key_sleft": $InfoCmp$Capability
static readonly "key_smessage": $InfoCmp$Capability
static readonly "key_smove": $InfoCmp$Capability
static readonly "key_snext": $InfoCmp$Capability
static readonly "key_soptions": $InfoCmp$Capability
static readonly "key_sprevious": $InfoCmp$Capability
static readonly "key_sprint": $InfoCmp$Capability
static readonly "key_sr": $InfoCmp$Capability
static readonly "key_sredo": $InfoCmp$Capability
static readonly "key_sreplace": $InfoCmp$Capability
static readonly "key_sright": $InfoCmp$Capability
static readonly "key_srsume": $InfoCmp$Capability
static readonly "key_ssave": $InfoCmp$Capability
static readonly "key_ssuspend": $InfoCmp$Capability
static readonly "key_stab": $InfoCmp$Capability
static readonly "key_sundo": $InfoCmp$Capability
static readonly "key_suspend": $InfoCmp$Capability
static readonly "key_undo": $InfoCmp$Capability
static readonly "key_up": $InfoCmp$Capability
static readonly "keypad_local": $InfoCmp$Capability
static readonly "keypad_xmit": $InfoCmp$Capability
static readonly "lab_f0": $InfoCmp$Capability
static readonly "lab_f1": $InfoCmp$Capability
static readonly "lab_f10": $InfoCmp$Capability
static readonly "lab_f2": $InfoCmp$Capability
static readonly "lab_f3": $InfoCmp$Capability
static readonly "lab_f4": $InfoCmp$Capability
static readonly "lab_f5": $InfoCmp$Capability
static readonly "lab_f6": $InfoCmp$Capability
static readonly "lab_f7": $InfoCmp$Capability
static readonly "lab_f8": $InfoCmp$Capability
static readonly "lab_f9": $InfoCmp$Capability
static readonly "label_format": $InfoCmp$Capability
static readonly "label_off": $InfoCmp$Capability
static readonly "label_on": $InfoCmp$Capability
static readonly "meta_off": $InfoCmp$Capability
static readonly "meta_on": $InfoCmp$Capability
static readonly "micro_column_address": $InfoCmp$Capability
static readonly "micro_down": $InfoCmp$Capability
static readonly "micro_left": $InfoCmp$Capability
static readonly "micro_right": $InfoCmp$Capability
static readonly "micro_row_address": $InfoCmp$Capability
static readonly "micro_up": $InfoCmp$Capability
static readonly "newline": $InfoCmp$Capability
static readonly "order_of_pins": $InfoCmp$Capability
static readonly "orig_colors": $InfoCmp$Capability
static readonly "orig_pair": $InfoCmp$Capability
static readonly "pad_char": $InfoCmp$Capability
static readonly "parm_dch": $InfoCmp$Capability
static readonly "parm_delete_line": $InfoCmp$Capability
static readonly "parm_down_cursor": $InfoCmp$Capability
static readonly "parm_down_micro": $InfoCmp$Capability
static readonly "parm_ich": $InfoCmp$Capability
static readonly "parm_index": $InfoCmp$Capability
static readonly "parm_insert_line": $InfoCmp$Capability
static readonly "parm_left_cursor": $InfoCmp$Capability
static readonly "parm_left_micro": $InfoCmp$Capability
static readonly "parm_right_cursor": $InfoCmp$Capability
static readonly "parm_right_micro": $InfoCmp$Capability
static readonly "parm_rindex": $InfoCmp$Capability
static readonly "parm_up_cursor": $InfoCmp$Capability
static readonly "parm_up_micro": $InfoCmp$Capability
static readonly "pkey_key": $InfoCmp$Capability
static readonly "pkey_local": $InfoCmp$Capability
static readonly "pkey_xmit": $InfoCmp$Capability
static readonly "plab_norm": $InfoCmp$Capability
static readonly "print_screen": $InfoCmp$Capability
static readonly "prtr_non": $InfoCmp$Capability
static readonly "prtr_off": $InfoCmp$Capability
static readonly "prtr_on": $InfoCmp$Capability
static readonly "pulse": $InfoCmp$Capability
static readonly "quick_dial": $InfoCmp$Capability
static readonly "remove_clock": $InfoCmp$Capability
static readonly "repeat_char": $InfoCmp$Capability
static readonly "req_for_input": $InfoCmp$Capability
static readonly "reset_1string": $InfoCmp$Capability
static readonly "reset_2string": $InfoCmp$Capability
static readonly "reset_3string": $InfoCmp$Capability
static readonly "reset_file": $InfoCmp$Capability
static readonly "restore_cursor": $InfoCmp$Capability
static readonly "row_address": $InfoCmp$Capability
static readonly "save_cursor": $InfoCmp$Capability
static readonly "scroll_forward": $InfoCmp$Capability
static readonly "scroll_reverse": $InfoCmp$Capability
static readonly "select_char_set": $InfoCmp$Capability
static readonly "set_attributes": $InfoCmp$Capability
static readonly "set_background": $InfoCmp$Capability
static readonly "set_bottom_margin": $InfoCmp$Capability
static readonly "set_bottom_margin_parm": $InfoCmp$Capability
static readonly "set_clock": $InfoCmp$Capability
static readonly "set_color_pair": $InfoCmp$Capability
static readonly "set_foreground": $InfoCmp$Capability
static readonly "set_left_margin": $InfoCmp$Capability
static readonly "set_left_margin_parm": $InfoCmp$Capability
static readonly "set_right_margin": $InfoCmp$Capability
static readonly "set_right_margin_parm": $InfoCmp$Capability
static readonly "set_tab": $InfoCmp$Capability
static readonly "set_top_margin": $InfoCmp$Capability
static readonly "set_top_margin_parm": $InfoCmp$Capability
static readonly "set_window": $InfoCmp$Capability
static readonly "start_bit_image": $InfoCmp$Capability
static readonly "start_char_set_def": $InfoCmp$Capability
static readonly "stop_bit_image": $InfoCmp$Capability
static readonly "stop_char_set_def": $InfoCmp$Capability
static readonly "subscript_characters": $InfoCmp$Capability
static readonly "superscript_characters": $InfoCmp$Capability
static readonly "tab": $InfoCmp$Capability
static readonly "these_cause_cr": $InfoCmp$Capability
static readonly "to_status_line": $InfoCmp$Capability
static readonly "tone": $InfoCmp$Capability
static readonly "underline_char": $InfoCmp$Capability
static readonly "up_half_line": $InfoCmp$Capability
static readonly "user0": $InfoCmp$Capability
static readonly "user1": $InfoCmp$Capability
static readonly "user2": $InfoCmp$Capability
static readonly "user3": $InfoCmp$Capability
static readonly "user4": $InfoCmp$Capability
static readonly "user5": $InfoCmp$Capability
static readonly "user6": $InfoCmp$Capability
static readonly "user7": $InfoCmp$Capability
static readonly "user8": $InfoCmp$Capability
static readonly "user9": $InfoCmp$Capability
static readonly "wait_tone": $InfoCmp$Capability
static readonly "xoff_character": $InfoCmp$Capability
static readonly "xon_character": $InfoCmp$Capability
static readonly "zero_motion": $InfoCmp$Capability
static readonly "alt_scancode_esc": $InfoCmp$Capability
static readonly "bit_image_carriage_return": $InfoCmp$Capability
static readonly "bit_image_newline": $InfoCmp$Capability
static readonly "bit_image_repeat": $InfoCmp$Capability
static readonly "char_set_names": $InfoCmp$Capability
static readonly "code_set_init": $InfoCmp$Capability
static readonly "color_names": $InfoCmp$Capability
static readonly "define_bit_image_region": $InfoCmp$Capability
static readonly "device_type": $InfoCmp$Capability
static readonly "display_pc_char": $InfoCmp$Capability
static readonly "end_bit_image_region": $InfoCmp$Capability
static readonly "enter_pc_charset_mode": $InfoCmp$Capability
static readonly "enter_scancode_mode": $InfoCmp$Capability
static readonly "exit_pc_charset_mode": $InfoCmp$Capability
static readonly "exit_scancode_mode": $InfoCmp$Capability
static readonly "get_mouse": $InfoCmp$Capability
static readonly "key_mouse": $InfoCmp$Capability
static readonly "mouse_info": $InfoCmp$Capability
static readonly "pc_term_options": $InfoCmp$Capability
static readonly "pkey_plab": $InfoCmp$Capability
static readonly "req_mouse_pos": $InfoCmp$Capability
static readonly "scancode_escape": $InfoCmp$Capability
static readonly "set0_des_seq": $InfoCmp$Capability
static readonly "set1_des_seq": $InfoCmp$Capability
static readonly "set2_des_seq": $InfoCmp$Capability
static readonly "set3_des_seq": $InfoCmp$Capability
static readonly "set_a_background": $InfoCmp$Capability
static readonly "set_a_foreground": $InfoCmp$Capability
static readonly "set_color_band": $InfoCmp$Capability
static readonly "set_lr_margin": $InfoCmp$Capability
static readonly "set_page_length": $InfoCmp$Capability
static readonly "set_tb_margin": $InfoCmp$Capability
static readonly "enter_horizontal_hl_mode": $InfoCmp$Capability
static readonly "enter_left_hl_mode": $InfoCmp$Capability
static readonly "enter_low_hl_mode": $InfoCmp$Capability
static readonly "enter_right_hl_mode": $InfoCmp$Capability
static readonly "enter_top_hl_mode": $InfoCmp$Capability
static readonly "enter_vertical_hl_mode": $InfoCmp$Capability
static readonly "set_a_attributes": $InfoCmp$Capability
static readonly "set_pglen_inch": $InfoCmp$Capability


public static "values"(): ($InfoCmp$Capability)[]
public static "valueOf"(arg0: string): $InfoCmp$Capability
public "getNames"(): (string)[]
public static "byName"(arg0: string): $InfoCmp$Capability
get "names"(): (string)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InfoCmp$Capability$Type = (("key_f37") | ("key_f38") | ("key_f35") | ("key_f36") | ("change_res_vert") | ("set_tb_margin") | ("key_btab") | ("mouse_info") | ("key_f39") | ("key_f30") | ("superscript_characters") | ("key_f33") | ("key_f34") | ("key_f31") | ("key_f32") | ("wait_tone") | ("enter_pc_charset_mode") | ("define_bit_image_region") | ("bit_image_type") | ("move_insert_mode") | ("key_shome") | ("initialize_color") | ("clear_all_tabs") | ("cursor_to_ll") | ("key_sundo") | ("virtual_terminal") | ("key_f26") | ("key_f27") | ("enter_protected_mode") | ("key_f24") | ("key_f25") | ("stop_char_set_def") | ("key_up") | ("key_f28") | ("key_f29") | ("cr_cancels_micro_mode") | ("key_f22") | ("key_sright") | ("exit_standout_mode") | ("key_f23") | ("key_f20") | ("clear_screen") | ("key_f21") | ("label_on") | ("set_a_foreground") | ("eat_newline_glitch") | ("restore_cursor") | ("remove_clock") | ("key_sreplace") | ("key_left") | ("bit_image_newline") | ("key_f15") | ("set1_des_seq") | ("key_f16") | ("key_f13") | ("key_eos") | ("key_f14") | ("key_f19") | ("key_message") | ("micro_right") | ("key_f17") | ("key_f18") | ("exit_insert_mode") | ("key_eol") | ("repeat_char") | ("non_rev_rmcup") | ("key_f11") | ("key_f12") | ("key_f10") | ("key_seol") | ("key_select") | ("enter_standout_mode") | ("key_sr") | ("change_res_horz") | ("key_move") | ("micro_left") | ("insert_line") | ("key_sf") | ("set_tab") | ("max_attributes") | ("buffer_capacity") | ("bit_image_carriage_return") | ("enter_superscript_mode") | ("parm_rindex") | ("exit_doublewide_mode") | ("reset_3string") | ("max_micro_address") | ("key_copy") | ("set_attributes") | ("label_height") | ("hue_lightness_saturation") | ("set_left_margin") | ("cursor_right") | ("erase_chars") | ("exit_upward_mode") | ("key_ppage") | ("ceol_standout_glitch") | ("ena_acs") | ("subscript_characters") | ("key_f62") | ("key_f63") | ("key_f60") | ("key_f61") | ("keypad_xmit") | ("carriage_return") | ("enter_horizontal_hl_mode") | ("enter_scancode_mode") | ("key_send") | ("enter_subscript_mode") | ("parm_delete_line") | ("micro_row_address") | ("set_color_pair") | ("key_f59") | ("key_f57") | ("key_f58") | ("prtr_silent") | ("key_f51") | ("key_f52") | ("key_f50") | ("parm_index") | ("tilde_glitch") | ("key_f55") | ("key_f56") | ("key_f53") | ("exit_underline_mode") | ("key_f54") | ("key_sbeg") | ("bit_image_repeat") | ("enter_normal_quality") | ("flash_hook") | ("key_sprint") | ("non_dest_scroll_region") | ("padding_baud_rate") | ("lines") | ("key_f48") | ("key_f49") | ("key_f46") | ("key_f47") | ("key_f40") | ("key_f41") | ("key_f44") | ("semi_auto_right_margin") | ("key_f45") | ("key_f42") | ("label_width") | ("key_f43") | ("scroll_forward") | ("generic_type") | ("key_help") | ("hard_copy") | ("cursor_address") | ("key_sleft") | ("select_char_set") | ("enter_underline_mode") | ("wide_char_size") | ("key_smove") | ("key_screate") | ("no_color_video") | ("key_next") | ("init_2string") | ("reset_1string") | ("no_pad_char") | ("enter_left_hl_mode") | ("orig_colors") | ("hard_cursor") | ("key_close") | ("meta_off") | ("set_page_length") | ("exit_scancode_mode") | ("key_create") | ("bell") | ("has_status_line") | ("key_sdl") | ("key_eic") | ("key_sdc") | ("cursor_left") | ("enter_am_mode") | ("key_scommand") | ("enter_draft_quality") | ("flash_screen") | ("parm_down_micro") | ("char_padding") | ("to_status_line") | ("tone") | ("auto_right_margin") | ("exit_attribute_mode") | ("key_suspend") | ("micro_up") | ("lab_f4") | ("reset_2string") | ("cursor_up") | ("lab_f5") | ("lab_f6") | ("init_3string") | ("lab_f7") | ("lab_f8") | ("lab_f9") | ("lines_of_memory") | ("enter_alt_charset_mode") | ("insert_null_glitch") | ("key_clear") | ("key_scopy") | ("key_print") | ("dial_phone") | ("key_mark") | ("enter_vertical_hl_mode") | ("exit_xon_mode") | ("key_previous") | ("set_bottom_margin_parm") | ("cursor_down") | ("key_sic") | ("orig_pair") | ("init_prog") | ("key_a3") | ("key_a1") | ("hangup") | ("key_end") | ("key_scancel") | ("key_catab") | ("from_status_line") | ("dis_status_line") | ("enter_reverse_mode") | ("lab_f0") | ("lab_f1") | ("key_b2") | ("lab_f2") | ("lab_f3") | ("exit_shadow_mode") | ("goto_window") | ("key_shelp") | ("cursor_visible") | ("create_window") | ("dest_tabs_magic_smso") | ("exit_leftward_mode") | ("color_names") | ("xoff_character") | ("enter_micro_mode") | ("exit_superscript_mode") | ("key_beg") | ("parm_ich") | ("auto_left_margin") | ("enter_shadow_mode") | ("enter_ca_mode") | ("key_enter") | ("parm_up_cursor") | ("prtr_off") | ("key_snext") | ("col_addr_glitch") | ("buttons") | ("keypad_local") | ("enter_right_hl_mode") | ("key_soptions") | ("init_tabs") | ("init_1string") | ("key_right") | ("key_open") | ("exit_alt_charset_mode") | ("micro_down") | ("pulse") | ("zero_motion") | ("lpi_changes_res") | ("key_stab") | ("set0_des_seq") | ("display_clock") | ("key_dc") | ("key_redo") | ("enter_bold_mode") | ("parm_right_micro") | ("parm_up_micro") | ("erase_overstrike") | ("cursor_invisible") | ("start_char_set_def") | ("needs_xon_xoff") | ("code_set_init") | ("set_lr_margin") | ("set_a_attributes") | ("enter_upward_mode") | ("enter_blink_mode") | ("label_format") | ("width_status_line") | ("clear_margins") | ("enter_near_letter_quality") | ("key_f8") | ("num_labels") | ("key_f9") | ("key_f6") | ("key_f7") | ("key_f4") | ("key_f5") | ("key_f2") | ("key_f3") | ("enter_top_hl_mode") | ("key_f0") | ("key_f1") | ("micro_line_size") | ("enter_xon_mode") | ("key_dl") | ("enter_insert_mode") | ("row_addr_glitch") | ("output_res_line") | ("key_options") | ("key_find") | ("cursor_home") | ("transparent_underline") | ("bit_image_entwining") | ("print_screen") | ("back_tab") | ("key_c3") | ("set_left_margin_parm") | ("pad_char") | ("key_c1") | ("req_for_input") | ("memory_above") | ("xon_xoff") | ("order_of_pins") | ("key_replace") | ("reset_file") | ("exit_italics_mode") | ("down_half_line") | ("clr_bol") | ("label_off") | ("set_a_background") | ("set_right_margin_parm") | ("get_mouse") | ("key_save") | ("parm_dch") | ("max_micro_jump") | ("set2_des_seq") | ("cursor_mem_address") | ("key_sprevious") | ("delete_character") | ("exit_micro_mode") | ("quick_dial") | ("output_res_char") | ("pc_term_options") | ("save_cursor") | ("pkey_plab") | ("parm_insert_line") | ("key_ic") | ("meta_on") | ("magic_cookie_glitch") | ("command_character") | ("maximum_windows") | ("set_foreground") | ("status_line_esc_ok") | ("move_standout_mode") | ("key_smessage") | ("pkey_local") | ("alt_scancode_esc") | ("define_char") | ("key_restart") | ("key_refresh") | ("enter_delete_mode") | ("key_resume") | ("enter_leftward_mode") | ("dot_vert_spacing") | ("exit_delete_mode") | ("pkey_xmit") | ("stop_bit_image") | ("fixed_pause") | ("cursor_normal") | ("set_window") | ("set_top_margin_parm") | ("key_sredo") | ("scroll_reverse") | ("cpi_changes_res") | ("scancode_escape") | ("insert_padding") | ("set_color_band") | ("underline_char") | ("change_line_pitch") | ("set_bottom_margin") | ("exit_am_mode") | ("key_ssuspend") | ("delete_line") | ("key_undo") | ("end_bit_image_region") | ("change_scroll_region") | ("char_set_names") | ("these_cause_cr") | ("key_sfind") | ("key_exit") | ("row_address") | ("clr_eol") | ("display_pc_char") | ("exit_pc_charset_mode") | ("clr_eos") | ("exit_ca_mode") | ("key_ll") | ("number_of_pins") | ("columns") | ("dot_horz_spacing") | ("has_meta_key") | ("plab_norm") | ("key_cancel") | ("key_down") | ("key_mouse") | ("set_clock") | ("key_il") | ("user1") | ("prtr_non") | ("user2") | ("set_background") | ("user0") | ("key_reference") | ("key_home") | ("user9") | ("user7") | ("lab_f10") | ("user8") | ("user5") | ("user6") | ("user3") | ("user4") | ("xon_character") | ("key_srsume") | ("key_command") | ("output_res_vert_inch") | ("print_rate") | ("set_pglen_inch") | ("key_ssave") | ("key_backspace") | ("acs_chars") | ("micro_column_address") | ("output_res_horz_inch") | ("parm_left_micro") | ("device_type") | ("insert_character") | ("parm_left_cursor") | ("newline") | ("has_print_wheel") | ("set_right_margin") | ("tab") | ("change_char_pitch") | ("can_change") | ("prtr_on") | ("start_bit_image") | ("key_npage") | ("req_mouse_pos") | ("max_colors") | ("enter_secure_mode") | ("initialize_pair") | ("set3_des_seq") | ("key_sexit") | ("back_color_erase") | ("init_file") | ("pkey_key") | ("micro_col_size") | ("key_ctab") | ("parm_right_cursor") | ("up_half_line") | ("memory_below") | ("max_pairs") | ("column_address") | ("no_esc_ctlc") | ("form_feed") | ("enter_doublewide_mode") | ("over_strike") | ("enter_dim_mode") | ("parm_down_cursor") | ("set_top_margin") | ("exit_subscript_mode") | ("enter_low_hl_mode") | ("enter_italics_mode")) | ($InfoCmp$Capability);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InfoCmp$Capability_ = $InfoCmp$Capability$Type;
}}
declare module "packages/org/jline/terminal/$Terminal$MouseTracking" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Terminal$MouseTracking extends $Enum<($Terminal$MouseTracking)> {
static readonly "Off": $Terminal$MouseTracking
static readonly "Normal": $Terminal$MouseTracking
static readonly "Button": $Terminal$MouseTracking
static readonly "Any": $Terminal$MouseTracking


public static "values"(): ($Terminal$MouseTracking)[]
public static "valueOf"(arg0: string): $Terminal$MouseTracking
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Terminal$MouseTracking$Type = (("button") | ("normal") | ("any") | ("off")) | ($Terminal$MouseTracking);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Terminal$MouseTracking_ = $Terminal$MouseTracking$Type;
}}
declare module "packages/org/jline/terminal/$Cursor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Cursor {

constructor(arg0: integer, arg1: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getY"(): integer
public "getX"(): integer
get "y"(): integer
get "x"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Cursor$Type = ($Cursor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Cursor_ = $Cursor$Type;
}}
declare module "packages/org/jline/utils/$AttributedCharSequence" {
import {$IntStream, $IntStream$Type} from "packages/java/util/stream/$IntStream"
import {$AttributedString, $AttributedString$Type} from "packages/org/jline/utils/$AttributedString"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Terminal, $Terminal$Type} from "packages/org/jline/terminal/$Terminal"
import {$AttributedStyle, $AttributedStyle$Type} from "packages/org/jline/utils/$AttributedStyle"

export class $AttributedCharSequence implements charseq {

constructor()

public "println"(arg0: $Terminal$Type): void
public "toString"(): string
public "isHidden"(arg0: integer): boolean
public "charAt"(arg0: integer): character
public "codePointAt"(arg0: integer): integer
public "codePointBefore"(arg0: integer): integer
public "codePointCount"(arg0: integer, arg1: integer): integer
public "substring"(arg0: integer, arg1: integer): $AttributedString
public "subSequence"(arg0: integer, arg1: integer): $AttributedString
public "contains"(arg0: character): boolean
public "print"(arg0: $Terminal$Type): void
public "runStart"(arg0: integer): integer
public "runLimit"(arg0: integer): integer
/**
 * 
 * @deprecated
 */
public static "rgbColor"(arg0: integer): integer
public "toAnsi"(arg0: integer, arg1: boolean): string
public "toAnsi"(arg0: $Terminal$Type): string
public "toAnsi"(): string
public "toAnsi"(arg0: integer, arg1: boolean, arg2: string, arg3: string): string
/**
 * 
 * @deprecated
 */
public static "roundRgbColor"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): integer
public "columnSubSequence"(arg0: integer, arg1: integer): $AttributedString
public "columnSplitLength"(arg0: integer): $List<($AttributedString)>
public "columnSplitLength"(arg0: integer, arg1: boolean, arg2: boolean): $List<($AttributedString)>
/**
 * 
 * @deprecated
 */
public static "roundColor"(arg0: integer, arg1: integer): integer
public "columnLength"(): integer
public "toAttributedString"(): $AttributedString
public "styleAt"(arg0: integer): $AttributedStyle
public "length"(): integer
public static "compare"(arg0: charseq, arg1: charseq): integer
public "isEmpty"(): boolean
public "codePoints"(): $IntStream
public "chars"(): $IntStream
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AttributedCharSequence$Type = ($AttributedCharSequence);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AttributedCharSequence_ = $AttributedCharSequence$Type;
}}
declare module "packages/org/jline/utils/$AttributedStyle" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $AttributedStyle {
static readonly "BLACK": integer
static readonly "RED": integer
static readonly "GREEN": integer
static readonly "YELLOW": integer
static readonly "BLUE": integer
static readonly "MAGENTA": integer
static readonly "CYAN": integer
static readonly "WHITE": integer
static readonly "BRIGHT": integer
static readonly "DEFAULT": $AttributedStyle
static readonly "BOLD": $AttributedStyle
static readonly "BOLD_OFF": $AttributedStyle
static readonly "INVERSE": $AttributedStyle
static readonly "INVERSE_OFF": $AttributedStyle
static readonly "HIDDEN": $AttributedStyle
static readonly "HIDDEN_OFF": $AttributedStyle

constructor(arg0: integer, arg1: integer)
constructor(arg0: $AttributedStyle$Type)
constructor()

public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "getMask"(): integer
public "boldDefault"(): $AttributedStyle
public "conceal"(): $AttributedStyle
public "crossedOutDefault"(): $AttributedStyle
public "faintDefault"(): $AttributedStyle
public "foregroundDefault"(): $AttributedStyle
public "concealDefault"(): $AttributedStyle
public "boldOff"(): $AttributedStyle
public "concealOff"(): $AttributedStyle
public "blinkDefault"(): $AttributedStyle
public "crossedOut"(): $AttributedStyle
public "underlineOff"(): $AttributedStyle
public "inverseDefault"(): $AttributedStyle
public "italicOff"(): $AttributedStyle
public "backgroundOff"(): $AttributedStyle
public "inverseNeg"(): $AttributedStyle
public "blinkOff"(): $AttributedStyle
public "faintOff"(): $AttributedStyle
public "hiddenOff"(): $AttributedStyle
public "underlineDefault"(): $AttributedStyle
public "inverseOff"(): $AttributedStyle
public "foregroundOff"(): $AttributedStyle
public "italicDefault"(): $AttributedStyle
public "backgroundDefault"(): $AttributedStyle
public "hiddenDefault"(): $AttributedStyle
public "crossedOutOff"(): $AttributedStyle
public "faint"(): $AttributedStyle
public "underline"(): $AttributedStyle
public "background"(arg0: integer): $AttributedStyle
public "foreground"(arg0: integer): $AttributedStyle
public "hidden"(): $AttributedStyle
public "inverse"(): $AttributedStyle
public "blink"(): $AttributedStyle
public "getStyle"(): integer
public "italic"(): $AttributedStyle
public "bold"(): $AttributedStyle
get "mask"(): integer
get "style"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AttributedStyle$Type = ($AttributedStyle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AttributedStyle_ = $AttributedStyle$Type;
}}
declare module "packages/org/jline/reader/$MaskingCallback" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $MaskingCallback {

 "display"(arg0: string): string
 "history"(arg0: string): string
}

export namespace $MaskingCallback {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MaskingCallback$Type = ($MaskingCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MaskingCallback_ = $MaskingCallback$Type;
}}
declare module "packages/org/jline/terminal/$MouseEvent$Type" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $MouseEvent$Type extends $Enum<($MouseEvent$Type)> {
static readonly "Released": $MouseEvent$Type
static readonly "Pressed": $MouseEvent$Type
static readonly "Wheel": $MouseEvent$Type
static readonly "Moved": $MouseEvent$Type
static readonly "Dragged": $MouseEvent$Type


public static "values"(): ($MouseEvent$Type)[]
public static "valueOf"(arg0: string): $MouseEvent$Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MouseEvent$Type$Type = (("pressed") | ("wheel") | ("moved") | ("dragged") | ("released")) | ($MouseEvent$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MouseEvent$Type_ = $MouseEvent$Type$Type;
}}
declare module "packages/org/jline/terminal/$Terminal" {
import {$Size, $Size$Type} from "packages/org/jline/terminal/$Size"
import {$Terminal$MouseTracking, $Terminal$MouseTracking$Type} from "packages/org/jline/terminal/$Terminal$MouseTracking"
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"
import {$NonBlockingReader, $NonBlockingReader$Type} from "packages/org/jline/utils/$NonBlockingReader"
import {$IntSupplier, $IntSupplier$Type} from "packages/java/util/function/$IntSupplier"
import {$Cursor, $Cursor$Type} from "packages/org/jline/terminal/$Cursor"
import {$Charset, $Charset$Type} from "packages/java/nio/charset/$Charset"
import {$Flushable, $Flushable$Type} from "packages/java/io/$Flushable"
import {$Terminal$Signal, $Terminal$Signal$Type} from "packages/org/jline/terminal/$Terminal$Signal"
import {$Attributes, $Attributes$Type} from "packages/org/jline/terminal/$Attributes"
import {$Closeable, $Closeable$Type} from "packages/java/io/$Closeable"
import {$Terminal$SignalHandler, $Terminal$SignalHandler$Type} from "packages/org/jline/terminal/$Terminal$SignalHandler"
import {$InfoCmp$Capability, $InfoCmp$Capability$Type} from "packages/org/jline/utils/$InfoCmp$Capability"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$IntConsumer, $IntConsumer$Type} from "packages/java/util/function/$IntConsumer"
import {$MouseEvent, $MouseEvent$Type} from "packages/org/jline/terminal/$MouseEvent"
import {$PrintWriter, $PrintWriter$Type} from "packages/java/io/$PrintWriter"

export interface $Terminal extends $Closeable, $Flushable {

 "getName"(): string
 "flush"(): void
 "resume"(): void
 "reader"(): $NonBlockingReader
 "writer"(): $PrintWriter
 "encoding"(): $Charset
 "getType"(): string
 "getSize"(): $Size
 "handle"(arg0: $Terminal$Signal$Type, arg1: $Terminal$SignalHandler$Type): $Terminal$SignalHandler
 "getAttributes"(): $Attributes
 "input"(): $InputStream
 "raise"(arg0: $Terminal$Signal$Type): void
 "output"(): $OutputStream
 "setSize"(arg0: $Size$Type): void
 "setAttributes"(arg0: $Attributes$Type): void
 "readMouseEvent"(): $MouseEvent
 "readMouseEvent"(arg0: $IntSupplier$Type): $MouseEvent
 "hasFocusSupport"(): boolean
 "trackFocus"(arg0: boolean): boolean
 "canPauseResume"(): boolean
 "getWidth"(): integer
 "paused"(): boolean
 "pause"(arg0: boolean): void
 "pause"(): void
 "enterRawMode"(): $Attributes
 "getCursorPosition"(arg0: $IntConsumer$Type): $Cursor
 "echo"(arg0: boolean): boolean
 "echo"(): boolean
 "puts"(arg0: $InfoCmp$Capability$Type, ...arg1: (any)[]): boolean
 "hasMouseSupport"(): boolean
 "getHeight"(): integer
 "trackMouse"(arg0: $Terminal$MouseTracking$Type): boolean
 "getBooleanCapability"(arg0: $InfoCmp$Capability$Type): boolean
 "getNumericCapability"(arg0: $InfoCmp$Capability$Type): integer
 "getStringCapability"(arg0: $InfoCmp$Capability$Type): string
 "getBufferSize"(): $Size
 "close"(): void
}

export namespace $Terminal {
const TYPE_DUMB: string
const TYPE_DUMB_COLOR: string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Terminal$Type = ($Terminal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Terminal_ = $Terminal$Type;
}}
declare module "packages/org/jline/reader/$Expander" {
import {$History, $History$Type} from "packages/org/jline/reader/$History"

export interface $Expander {

 "expandVar"(arg0: string): string
 "expandHistory"(arg0: $History$Type, arg1: string): string
}

export namespace $Expander {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Expander$Type = ($Expander);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Expander_ = $Expander$Type;
}}
declare module "packages/org/jline/keymap/$KeyMap" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$InfoCmp$Capability, $InfoCmp$Capability$Type} from "packages/org/jline/utils/$InfoCmp$Capability"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Terminal, $Terminal$Type} from "packages/org/jline/terminal/$Terminal"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $KeyMap<T> {
static readonly "KEYMAP_LENGTH": integer
static readonly "DEFAULT_AMBIGUOUS_TIMEOUT": long
static readonly "KEYSEQ_COMPARATOR": $Comparator<(string)>

constructor()

public static "key"(arg0: $Terminal$Type, arg1: $InfoCmp$Capability$Type): string
public "bind"(arg0: T, arg1: $Iterable$Type<(any)>): void
public "bind"(arg0: T, arg1: charseq): void
public "bind"(arg0: T, ...arg1: (charseq)[]): void
public static "display"(arg0: string): string
public static "range"(arg0: string): $Collection<(string)>
public static "del"(): string
public static "alt"(arg0: character): string
public static "alt"(arg0: string): string
public static "esc"(): string
public static "translate"(arg0: string): string
public "setUnicode"(arg0: T): void
public "unbind"(...arg0: (charseq)[]): void
public "unbind"(arg0: charseq): void
public "getAmbiguousTimeout"(): long
public "setAmbiguousTimeout"(arg0: long): void
public static "ctrl"(arg0: character): string
public "bindIfNotBound"(arg0: T, arg1: charseq): void
public "getNomatch"(): T
public "getAnotherKey"(): T
public "setNomatch"(arg0: T): void
public "getBound"(arg0: charseq, arg1: (integer)[]): T
public "getBound"(arg0: charseq): T
public "getBoundKeys"(): $Map<(string), (T)>
public "getUnicode"(): T
set "unicode"(value: T)
get "ambiguousTimeout"(): long
set "ambiguousTimeout"(value: long)
get "nomatch"(): T
get "anotherKey"(): T
set "nomatch"(value: T)
get "boundKeys"(): $Map<(string), (T)>
get "unicode"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyMap$Type<T> = ($KeyMap<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyMap_<T> = $KeyMap$Type<(T)>;
}}
declare module "packages/org/jline/terminal/$Attributes$InputFlag" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Attributes$InputFlag extends $Enum<($Attributes$InputFlag)> {
static readonly "IGNBRK": $Attributes$InputFlag
static readonly "BRKINT": $Attributes$InputFlag
static readonly "IGNPAR": $Attributes$InputFlag
static readonly "PARMRK": $Attributes$InputFlag
static readonly "INPCK": $Attributes$InputFlag
static readonly "ISTRIP": $Attributes$InputFlag
static readonly "INLCR": $Attributes$InputFlag
static readonly "IGNCR": $Attributes$InputFlag
static readonly "ICRNL": $Attributes$InputFlag
static readonly "IXON": $Attributes$InputFlag
static readonly "IXOFF": $Attributes$InputFlag
static readonly "IXANY": $Attributes$InputFlag
static readonly "IMAXBEL": $Attributes$InputFlag
static readonly "IUTF8": $Attributes$InputFlag


public static "values"(): ($Attributes$InputFlag)[]
public static "valueOf"(arg0: string): $Attributes$InputFlag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Attributes$InputFlag$Type = (("ignbrk") | ("icrnl") | ("ixany") | ("iutf8") | ("brkint") | ("igncr") | ("inpck") | ("inlcr") | ("parmrk") | ("imaxbel") | ("istrip") | ("ignpar") | ("ixon") | ("ixoff")) | ($Attributes$InputFlag);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Attributes$InputFlag_ = $Attributes$InputFlag$Type;
}}
declare module "packages/org/jline/terminal/$Attributes" {
import {$Attributes$OutputFlag, $Attributes$OutputFlag$Type} from "packages/org/jline/terminal/$Attributes$OutputFlag"
import {$Attributes$InputFlag, $Attributes$InputFlag$Type} from "packages/org/jline/terminal/$Attributes$InputFlag"
import {$EnumSet, $EnumSet$Type} from "packages/java/util/$EnumSet"
import {$Attributes$LocalFlag, $Attributes$LocalFlag$Type} from "packages/org/jline/terminal/$Attributes$LocalFlag"
import {$Attributes$ControlFlag, $Attributes$ControlFlag$Type} from "packages/org/jline/terminal/$Attributes$ControlFlag"
import {$Attributes$ControlChar, $Attributes$ControlChar$Type} from "packages/org/jline/terminal/$Attributes$ControlChar"
import {$EnumMap, $EnumMap$Type} from "packages/java/util/$EnumMap"

export class $Attributes {

constructor()
constructor(arg0: $Attributes$Type)

public "toString"(): string
public "copy"(arg0: $Attributes$Type): void
public "getInputFlag"(arg0: $Attributes$InputFlag$Type): boolean
public "setInputFlag"(arg0: $Attributes$InputFlag$Type, arg1: boolean): void
public "getInputFlags"(): $EnumSet<($Attributes$InputFlag)>
public "setInputFlags"(arg0: $EnumSet$Type<($Attributes$InputFlag$Type)>, arg1: boolean): void
public "setInputFlags"(arg0: $EnumSet$Type<($Attributes$InputFlag$Type)>): void
public "setOutputFlag"(arg0: $Attributes$OutputFlag$Type, arg1: boolean): void
public "getOutputFlag"(arg0: $Attributes$OutputFlag$Type): boolean
public "setOutputFlags"(arg0: $EnumSet$Type<($Attributes$OutputFlag$Type)>): void
public "setOutputFlags"(arg0: $EnumSet$Type<($Attributes$OutputFlag$Type)>, arg1: boolean): void
public "setLocalFlags"(arg0: $EnumSet$Type<($Attributes$LocalFlag$Type)>): void
public "setLocalFlags"(arg0: $EnumSet$Type<($Attributes$LocalFlag$Type)>, arg1: boolean): void
public "setControlChars"(arg0: $EnumMap$Type<($Attributes$ControlChar$Type), (integer)>): void
public "setControlFlag"(arg0: $Attributes$ControlFlag$Type, arg1: boolean): void
public "getLocalFlag"(arg0: $Attributes$LocalFlag$Type): boolean
public "setControlChar"(arg0: $Attributes$ControlChar$Type, arg1: integer): void
public "getControlFlag"(arg0: $Attributes$ControlFlag$Type): boolean
public "getOutputFlags"(): $EnumSet<($Attributes$OutputFlag)>
public "getLocalFlags"(): $EnumSet<($Attributes$LocalFlag)>
public "setLocalFlag"(arg0: $Attributes$LocalFlag$Type, arg1: boolean): void
public "getControlChars"(): $EnumMap<($Attributes$ControlChar), (integer)>
public "getControlChar"(arg0: $Attributes$ControlChar$Type): integer
public "getControlFlags"(): $EnumSet<($Attributes$ControlFlag)>
public "setControlFlags"(arg0: $EnumSet$Type<($Attributes$ControlFlag$Type)>): void
public "setControlFlags"(arg0: $EnumSet$Type<($Attributes$ControlFlag$Type)>, arg1: boolean): void
get "inputFlags"(): $EnumSet<($Attributes$InputFlag)>
set "inputFlags"(value: $EnumSet$Type<($Attributes$InputFlag$Type)>)
set "outputFlags"(value: $EnumSet$Type<($Attributes$OutputFlag$Type)>)
set "localFlags"(value: $EnumSet$Type<($Attributes$LocalFlag$Type)>)
set "controlChars"(value: $EnumMap$Type<($Attributes$ControlChar$Type), (integer)>)
get "outputFlags"(): $EnumSet<($Attributes$OutputFlag)>
get "localFlags"(): $EnumSet<($Attributes$LocalFlag)>
get "controlChars"(): $EnumMap<($Attributes$ControlChar), (integer)>
get "controlFlags"(): $EnumSet<($Attributes$ControlFlag)>
set "controlFlags"(value: $EnumSet$Type<($Attributes$ControlFlag$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Attributes$Type = ($Attributes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Attributes_ = $Attributes$Type;
}}
declare module "packages/org/jline/reader/$ParsedLine" {
import {$List, $List$Type} from "packages/java/util/$List"

export interface $ParsedLine {

 "line"(): string
 "cursor"(): integer
 "words"(): $List<(string)>
 "word"(): string
 "wordIndex"(): integer
 "wordCursor"(): integer
}

export namespace $ParsedLine {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParsedLine$Type = ($ParsedLine);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParsedLine_ = $ParsedLine$Type;
}}
declare module "packages/org/jline/utils/$AttributedString" {
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AttributedCharSequence, $AttributedCharSequence$Type} from "packages/org/jline/utils/$AttributedCharSequence"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$AttributedStyle, $AttributedStyle$Type} from "packages/org/jline/utils/$AttributedStyle"

export class $AttributedString extends $AttributedCharSequence {
static readonly "EMPTY": $AttributedString
static readonly "NEWLINE": $AttributedString

constructor(arg0: charseq, arg1: integer, arg2: integer, arg3: $AttributedStyle$Type)
constructor(arg0: charseq, arg1: $AttributedStyle$Type)
constructor(arg0: charseq, arg1: integer, arg2: integer)
constructor(arg0: charseq)

public "equals"(arg0: any): boolean
public "length"(): integer
public "hashCode"(): integer
public static "join"(arg0: $AttributedString$Type, arg1: $Iterable$Type<($AttributedString$Type)>): $AttributedString
public static "join"(arg0: $AttributedString$Type, ...arg1: ($AttributedString$Type)[]): $AttributedString
public "subSequence"(arg0: integer, arg1: integer): $AttributedString
public "styleMatches"(arg0: $Pattern$Type, arg1: $AttributedStyle$Type): $AttributedString
public static "fromAnsi"(arg0: string, arg1: integer): $AttributedString
public static "fromAnsi"(arg0: string): $AttributedString
public static "fromAnsi"(arg0: string, arg1: $List$Type<(integer)>): $AttributedString
public static "stripAnsi"(arg0: string): string
public "styleAt"(arg0: integer): $AttributedStyle
public static "compare"(arg0: charseq, arg1: charseq): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AttributedString$Type = ($AttributedString);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AttributedString_ = $AttributedString$Type;
}}
declare module "packages/org/jline/terminal/$Terminal$SignalHandler" {
import {$Terminal$Signal, $Terminal$Signal$Type} from "packages/org/jline/terminal/$Terminal$Signal"

export interface $Terminal$SignalHandler {

 "handle"(arg0: $Terminal$Signal$Type): void

(arg0: $Terminal$Signal$Type): void
}

export namespace $Terminal$SignalHandler {
const SIG_DFL: $Terminal$SignalHandler
const SIG_IGN: $Terminal$SignalHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Terminal$SignalHandler$Type = ($Terminal$SignalHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Terminal$SignalHandler_ = $Terminal$SignalHandler$Type;
}}
declare module "packages/org/jline/terminal/$Attributes$LocalFlag" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Attributes$LocalFlag extends $Enum<($Attributes$LocalFlag)> {
static readonly "ECHOKE": $Attributes$LocalFlag
static readonly "ECHOE": $Attributes$LocalFlag
static readonly "ECHOK": $Attributes$LocalFlag
static readonly "ECHO": $Attributes$LocalFlag
static readonly "ECHONL": $Attributes$LocalFlag
static readonly "ECHOPRT": $Attributes$LocalFlag
static readonly "ECHOCTL": $Attributes$LocalFlag
static readonly "ISIG": $Attributes$LocalFlag
static readonly "ICANON": $Attributes$LocalFlag
static readonly "ALTWERASE": $Attributes$LocalFlag
static readonly "IEXTEN": $Attributes$LocalFlag
static readonly "EXTPROC": $Attributes$LocalFlag
static readonly "TOSTOP": $Attributes$LocalFlag
static readonly "FLUSHO": $Attributes$LocalFlag
static readonly "NOKERNINFO": $Attributes$LocalFlag
static readonly "PENDIN": $Attributes$LocalFlag
static readonly "NOFLSH": $Attributes$LocalFlag


public static "values"(): ($Attributes$LocalFlag)[]
public static "valueOf"(arg0: string): $Attributes$LocalFlag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Attributes$LocalFlag$Type = (("echoke") | ("echoctl") | ("extproc") | ("echo") | ("icanon") | ("altwerase") | ("nokerninfo") | ("pendin") | ("iexten") | ("isig") | ("tostop") | ("echok") | ("flusho") | ("echoe") | ("noflsh") | ("echonl") | ("echoprt")) | ($Attributes$LocalFlag);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Attributes$LocalFlag_ = $Attributes$LocalFlag$Type;
}}
declare module "packages/org/jline/terminal/$MouseEvent" {
import {$EnumSet, $EnumSet$Type} from "packages/java/util/$EnumSet"
import {$MouseEvent$Button, $MouseEvent$Button$Type} from "packages/org/jline/terminal/$MouseEvent$Button"
import {$MouseEvent$Modifier, $MouseEvent$Modifier$Type} from "packages/org/jline/terminal/$MouseEvent$Modifier"
import {$MouseEvent$Type, $MouseEvent$Type$Type} from "packages/org/jline/terminal/$MouseEvent$Type"

export class $MouseEvent {

constructor(arg0: $MouseEvent$Type$Type, arg1: $MouseEvent$Button$Type, arg2: $EnumSet$Type<($MouseEvent$Modifier$Type)>, arg3: integer, arg4: integer)

public "toString"(): string
public "getModifiers"(): $EnumSet<($MouseEvent$Modifier)>
public "getType"(): $MouseEvent$Type
public "getY"(): integer
public "getX"(): integer
public "getButton"(): $MouseEvent$Button
get "modifiers"(): $EnumSet<($MouseEvent$Modifier)>
get "type"(): $MouseEvent$Type
get "y"(): integer
get "x"(): integer
get "button"(): $MouseEvent$Button
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MouseEvent$Type = ($MouseEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MouseEvent_ = $MouseEvent$Type;
}}
