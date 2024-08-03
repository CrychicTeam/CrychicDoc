declare module "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool" {
import {$NewClassNameBuilder, $NewClassNameBuilder$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$NewClassNameBuilder"
import {$PrimitiveConstant, $PrimitiveConstant$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$PrimitiveConstant"
import {$DataInputStream, $DataInputStream$Type} from "packages/java/io/$DataInputStream"
import {$PooledConstant, $PooledConstant$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$PooledConstant"
import {$LinkConstant, $LinkConstant$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$LinkConstant"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ConstantPool implements $NewClassNameBuilder {
static readonly "FIELD": integer
static readonly "METHOD": integer

constructor(arg0: $DataInputStream$Type)

public "buildNewClassname"(arg0: string): string
public "getConstant"(arg0: integer): $PooledConstant
public "getLinkConstant"(arg0: integer): $LinkConstant
public "getClassElement"(arg0: integer, arg1: string, arg2: integer, arg3: integer): (string)[]
public "getPrimitiveConstant"(arg0: integer): $PrimitiveConstant
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConstantPool$Type = ($ConstantPool);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConstantPool_ = $ConstantPool$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/$Fernflower" {
import {$IBytecodeProvider, $IBytecodeProvider$Type} from "packages/org/jetbrains/java/decompiler/main/extern/$IBytecodeProvider"
import {$IContextSource, $IContextSource$Type} from "packages/org/jetbrains/java/decompiler/main/extern/$IContextSource"
import {$IDecompiledData, $IDecompiledData$Type} from "packages/org/jetbrains/java/decompiler/struct/$IDecompiledData"
import {$IFernflowerLogger, $IFernflowerLogger$Type} from "packages/org/jetbrains/java/decompiler/main/extern/$IFernflowerLogger"
import {$StructClass, $StructClass$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructClass"
import {$File, $File$Type} from "packages/java/io/$File"
import {$IResultSaver, $IResultSaver$Type} from "packages/org/jetbrains/java/decompiler/main/extern/$IResultSaver"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Fernflower implements $IDecompiledData {

constructor(arg0: $IResultSaver$Type, arg1: $Map$Type<(string), (any)>, arg2: $IFernflowerLogger$Type)
/**
 * 
 * @deprecated
 */
constructor(arg0: $IBytecodeProvider$Type, arg1: $IResultSaver$Type, arg2: $Map$Type<(string), (any)>, arg3: $IFernflowerLogger$Type)

public "addSource"(arg0: $IContextSource$Type): void
public "addSource"(arg0: $File$Type): void
public "processClass"(arg0: $StructClass$Type): void
public "addWhitelist"(arg0: string): void
public "decompileContext"(): void
public "clearContext"(): void
public "getClassContent"(arg0: $StructClass$Type): string
public "addLibrary"(arg0: $IContextSource$Type): void
public "addLibrary"(arg0: $File$Type): void
public "getClassEntryName"(arg0: $StructClass$Type, arg1: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Fernflower$Type = ($Fernflower);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Fernflower_ = $Fernflower$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/extern/$IFernflowerLogger$Severity" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $IFernflowerLogger$Severity extends $Enum<($IFernflowerLogger$Severity)> {
static readonly "TRACE": $IFernflowerLogger$Severity
static readonly "INFO": $IFernflowerLogger$Severity
static readonly "WARN": $IFernflowerLogger$Severity
static readonly "ERROR": $IFernflowerLogger$Severity
readonly "prefix": string


public static "values"(): ($IFernflowerLogger$Severity)[]
public static "valueOf"(arg0: string): $IFernflowerLogger$Severity
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IFernflowerLogger$Severity$Type = (("warn") | ("trace") | ("error") | ("info")) | ($IFernflowerLogger$Severity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IFernflowerLogger$Severity_ = $IFernflowerLogger$Severity$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/code/$CodeConstants" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $CodeConstants {

}

export namespace $CodeConstants {
const TYPE_BYTE: integer
const TYPE_CHAR: integer
const TYPE_DOUBLE: integer
const TYPE_FLOAT: integer
const TYPE_INT: integer
const TYPE_LONG: integer
const TYPE_SHORT: integer
const TYPE_BOOLEAN: integer
const TYPE_OBJECT: integer
const TYPE_ADDRESS: integer
const TYPE_VOID: integer
const TYPE_ANY: integer
const TYPE_GROUP2EMPTY: integer
const TYPE_NULL: integer
const TYPE_NOTINITIALIZED: integer
const TYPE_BYTECHAR: integer
const TYPE_SHORTCHAR: integer
const TYPE_UNKNOWN: integer
const TYPE_GENVAR: integer
const ACC_PUBLIC: integer
const ACC_PRIVATE: integer
const ACC_PROTECTED: integer
const ACC_STATIC: integer
const ACC_FINAL: integer
const ACC_SYNCHRONIZED: integer
const ACC_OPEN: integer
const ACC_TRANSITIVE: integer
const ACC_STATIC_PHASE: integer
const ACC_NATIVE: integer
const ACC_ABSTRACT: integer
const ACC_STRICT: integer
const ACC_VOLATILE: integer
const ACC_BRIDGE: integer
const ACC_TRANSIENT: integer
const ACC_VARARGS: integer
const ACC_SYNTHETIC: integer
const ACC_ANNOTATION: integer
const ACC_ENUM: integer
const ACC_MANDATED: integer
const ACC_MODULE: integer
const ACC_SUPER: integer
const ACC_INTERFACE: integer
const GROUP_GENERAL: integer
const GROUP_JUMP: integer
const GROUP_SWITCH: integer
const GROUP_INVOCATION: integer
const GROUP_FIELDACCESS: integer
const GROUP_RETURN: integer
const CONSTANT_Utf8: integer
const CONSTANT_Integer: integer
const CONSTANT_Float: integer
const CONSTANT_Long: integer
const CONSTANT_Double: integer
const CONSTANT_Class: integer
const CONSTANT_String: integer
const CONSTANT_Fieldref: integer
const CONSTANT_Methodref: integer
const CONSTANT_InterfaceMethodref: integer
const CONSTANT_NameAndType: integer
const CONSTANT_MethodHandle: integer
const CONSTANT_MethodType: integer
const CONSTANT_Dynamic: integer
const CONSTANT_InvokeDynamic: integer
const CONSTANT_Module: integer
const CONSTANT_Package: integer
const CONSTANT_MethodHandle_REF_getField: integer
const CONSTANT_MethodHandle_REF_getStatic: integer
const CONSTANT_MethodHandle_REF_putField: integer
const CONSTANT_MethodHandle_REF_putStatic: integer
const CONSTANT_MethodHandle_REF_invokeVirtual: integer
const CONSTANT_MethodHandle_REF_invokeStatic: integer
const CONSTANT_MethodHandle_REF_invokeSpecial: integer
const CONSTANT_MethodHandle_REF_newInvokeSpecial: integer
const CONSTANT_MethodHandle_REF_invokeInterface: integer
const opc_nop: integer
const opc_aconst_null: integer
const opc_iconst_m1: integer
const opc_iconst_0: integer
const opc_iconst_1: integer
const opc_iconst_2: integer
const opc_iconst_3: integer
const opc_iconst_4: integer
const opc_iconst_5: integer
const opc_lconst_0: integer
const opc_lconst_1: integer
const opc_fconst_0: integer
const opc_fconst_1: integer
const opc_fconst_2: integer
const opc_dconst_0: integer
const opc_dconst_1: integer
const opc_bipush: integer
const opc_sipush: integer
const opc_ldc: integer
const opc_ldc_w: integer
const opc_ldc2_w: integer
const opc_iload: integer
const opc_lload: integer
const opc_fload: integer
const opc_dload: integer
const opc_aload: integer
const opc_iload_0: integer
const opc_iload_1: integer
const opc_iload_2: integer
const opc_iload_3: integer
const opc_lload_0: integer
const opc_lload_1: integer
const opc_lload_2: integer
const opc_lload_3: integer
const opc_fload_0: integer
const opc_fload_1: integer
const opc_fload_2: integer
const opc_fload_3: integer
const opc_dload_0: integer
const opc_dload_1: integer
const opc_dload_2: integer
const opc_dload_3: integer
const opc_aload_0: integer
const opc_aload_1: integer
const opc_aload_2: integer
const opc_aload_3: integer
const opc_iaload: integer
const opc_laload: integer
const opc_faload: integer
const opc_daload: integer
const opc_aaload: integer
const opc_baload: integer
const opc_caload: integer
const opc_saload: integer
const opc_istore: integer
const opc_lstore: integer
const opc_fstore: integer
const opc_dstore: integer
const opc_astore: integer
const opc_istore_0: integer
const opc_istore_1: integer
const opc_istore_2: integer
const opc_istore_3: integer
const opc_lstore_0: integer
const opc_lstore_1: integer
const opc_lstore_2: integer
const opc_lstore_3: integer
const opc_fstore_0: integer
const opc_fstore_1: integer
const opc_fstore_2: integer
const opc_fstore_3: integer
const opc_dstore_0: integer
const opc_dstore_1: integer
const opc_dstore_2: integer
const opc_dstore_3: integer
const opc_astore_0: integer
const opc_astore_1: integer
const opc_astore_2: integer
const opc_astore_3: integer
const opc_iastore: integer
const opc_lastore: integer
const opc_fastore: integer
const opc_dastore: integer
const opc_aastore: integer
const opc_bastore: integer
const opc_castore: integer
const opc_sastore: integer
const opc_pop: integer
const opc_pop2: integer
const opc_dup: integer
const opc_dup_x1: integer
const opc_dup_x2: integer
const opc_dup2: integer
const opc_dup2_x1: integer
const opc_dup2_x2: integer
const opc_swap: integer
const opc_iadd: integer
const opc_ladd: integer
const opc_fadd: integer
const opc_dadd: integer
const opc_isub: integer
const opc_lsub: integer
const opc_fsub: integer
const opc_dsub: integer
const opc_imul: integer
const opc_lmul: integer
const opc_fmul: integer
const opc_dmul: integer
const opc_idiv: integer
const opc_ldiv: integer
const opc_fdiv: integer
const opc_ddiv: integer
const opc_irem: integer
const opc_lrem: integer
const opc_frem: integer
const opc_drem: integer
const opc_ineg: integer
const opc_lneg: integer
const opc_fneg: integer
const opc_dneg: integer
const opc_ishl: integer
const opc_lshl: integer
const opc_ishr: integer
const opc_lshr: integer
const opc_iushr: integer
const opc_lushr: integer
const opc_iand: integer
const opc_land: integer
const opc_ior: integer
const opc_lor: integer
const opc_ixor: integer
const opc_lxor: integer
const opc_iinc: integer
const opc_i2l: integer
const opc_i2f: integer
const opc_i2d: integer
const opc_l2i: integer
const opc_l2f: integer
const opc_l2d: integer
const opc_f2i: integer
const opc_f2l: integer
const opc_f2d: integer
const opc_d2i: integer
const opc_d2l: integer
const opc_d2f: integer
const opc_i2b: integer
const opc_i2c: integer
const opc_i2s: integer
const opc_lcmp: integer
const opc_fcmpl: integer
const opc_fcmpg: integer
const opc_dcmpl: integer
const opc_dcmpg: integer
const opc_ifeq: integer
const opc_ifne: integer
const opc_iflt: integer
const opc_ifge: integer
const opc_ifgt: integer
const opc_ifle: integer
const opc_if_icmpeq: integer
const opc_if_icmpne: integer
const opc_if_icmplt: integer
const opc_if_icmpge: integer
const opc_if_icmpgt: integer
const opc_if_icmple: integer
const opc_if_acmpeq: integer
const opc_if_acmpne: integer
const opc_goto: integer
const opc_jsr: integer
const opc_ret: integer
const opc_tableswitch: integer
const opc_lookupswitch: integer
const opc_ireturn: integer
const opc_lreturn: integer
const opc_freturn: integer
const opc_dreturn: integer
const opc_areturn: integer
const opc_return: integer
const opc_getstatic: integer
const opc_putstatic: integer
const opc_getfield: integer
const opc_putfield: integer
const opc_invokevirtual: integer
const opc_invokespecial: integer
const opc_invokestatic: integer
const opc_invokeinterface: integer
const opc_invokedynamic: integer
const opc_new: integer
const opc_newarray: integer
const opc_anewarray: integer
const opc_arraylength: integer
const opc_athrow: integer
const opc_checkcast: integer
const opc_instanceof: integer
const opc_monitorenter: integer
const opc_monitorexit: integer
const opc_wide: integer
const opc_multianewarray: integer
const opc_ifnull: integer
const opc_ifnonnull: integer
const opc_goto_w: integer
const opc_jsr_w: integer
const CLINIT_NAME: string
const INIT_NAME: string
function isSignaturePolymorphic(arg0: string, arg1: string, arg2: boolean): boolean
function isReturnPolymorphic(arg0: string, arg1: string): boolean
function areParametersPolymorphic(arg0: string, arg1: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CodeConstants$Type = ($CodeConstants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CodeConstants_ = $CodeConstants$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/flow/$DirectNodeType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $DirectNodeType extends $Enum<($DirectNodeType)> {
static readonly "DIRECT": $DirectNodeType
static readonly "TAIL": $DirectNodeType
static readonly "INIT": $DirectNodeType
static readonly "CONDITION": $DirectNodeType
static readonly "INCREMENT": $DirectNodeType
static readonly "TRY": $DirectNodeType
static readonly "CATCH": $DirectNodeType
static readonly "COMBINED_CATCH": $DirectNodeType
static readonly "FINALLY": $DirectNodeType
static readonly "FINALLY_END": $DirectNodeType
static readonly "FOREACH_VARDEF": $DirectNodeType
static readonly "CASE": $DirectNodeType


public static "values"(): ($DirectNodeType)[]
public static "valueOf"(arg0: string): $DirectNodeType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DirectNodeType$Type = (("init") | ("condition") | ("foreach_vardef") | ("finally_end") | ("tail") | ("finally") | ("direct") | ("increment") | ("try") | ("catch") | ("case") | ("combined_catch")) | ($DirectNodeType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DirectNodeType_ = $DirectNodeType$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/consts/$PooledConstant" {
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$CodeConstants, $CodeConstants$Type} from "packages/org/jetbrains/java/decompiler/code/$CodeConstants"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $PooledConstant implements $CodeConstants {
readonly "type": integer

constructor(arg0: integer)

public "resolveConstant"(arg0: $ConstantPool$Type): void
public static "isSignaturePolymorphic"(arg0: string, arg1: string, arg2: boolean): boolean
public static "isReturnPolymorphic"(arg0: string, arg1: string): boolean
public static "areParametersPolymorphic"(arg0: string, arg1: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PooledConstant$Type = ($PooledConstant);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PooledConstant_ = $PooledConstant$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/decompose/$IGraphNode" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IGraphNode {

 "getPredecessors"(): $Collection<(any)>

(): $Collection<(any)>
}

export namespace $IGraphNode {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IGraphNode$Type = ($IGraphNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IGraphNode_ = $IGraphNode$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/util/$Key" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Key<T> {
readonly "name": string

constructor(arg0: string)

public static "of"<T>(arg0: string): $Key<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Key$Type<T> = ($Key<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Key_<T> = $Key$Type<(T)>;
}}
declare module "packages/org/jetbrains/java/decompiler/main/extern/$IContextSource$IOutputSink" {
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IContextSource$IOutputSink extends $AutoCloseable {

 "acceptClass"(arg0: string, arg1: string, arg2: string, arg3: (integer)[]): void
 "acceptOther"(arg0: string): void
 "acceptDirectory"(arg0: string): void
 "begin"(): void
 "close"(): void
}

export namespace $IContextSource$IOutputSink {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IContextSource$IOutputSink$Type = ($IContextSource$IOutputSink);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IContextSource$IOutputSink_ = $IContextSource$IOutputSink$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$Exprent" {
import {$SFormsConstructor, $SFormsConstructor$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/sforms/$SFormsConstructor"
import {$VarType, $VarType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$VarType"
import {$Exprent$Type, $Exprent$Type$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$Exprent$Type"
import {$BitSet, $BitSet$Type} from "packages/java/util/$BitSet"
import {$VarMapHolder, $VarMapHolder$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/sforms/$VarMapHolder"
import {$Statement, $Statement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$Statement"
import {$IMatchable, $IMatchable$Type} from "packages/org/jetbrains/java/decompiler/struct/match/$IMatchable"
import {$CheckTypesResult, $CheckTypesResult$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$CheckTypesResult"
import {$MatchEngine, $MatchEngine$Type} from "packages/org/jetbrains/java/decompiler/struct/match/$MatchEngine"
import {$TextBuffer, $TextBuffer$Type} from "packages/org/jetbrains/java/decompiler/util/$TextBuffer"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MatchNode, $MatchNode$Type} from "packages/org/jetbrains/java/decompiler/struct/match/$MatchNode"
import {$VarVersionPair, $VarVersionPair$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarVersionPair"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Exprent implements $IMatchable {
static readonly "MULTIPLE_USES": integer
static readonly "SIDE_EFFECTS_FREE": integer
static readonly "BOTH_FLAGS": integer
readonly "type": $Exprent$Type
readonly "id": integer
 "bytecode": $BitSet


public "toString"(): string
public "match"(arg0: $MatchNode$Type, arg1: $MatchEngine$Type): boolean
public "copy"(): $Exprent
public "getPrecedence"(): integer
public "getExprentUse"(): integer
public "getExprType"(): $VarType
public "toJava"(): $TextBuffer
public "toJava"(arg0: integer): $TextBuffer
public "containsExprent"(arg0: $Exprent$Type): boolean
public "containsVar"(arg0: $VarVersionPair$Type): boolean
public static "sortIndexed"(arg0: $List$Type<(any)>): $List<(any)>
public "getAllExprents"(arg0: boolean): $List<($Exprent)>
public "getAllExprents"(arg0: boolean, arg1: boolean): $List<($Exprent)>
public "getAllExprents"(): $List<($Exprent)>
public "addBytecodeOffsets"(arg0: $BitSet$Type): void
public "getBytecodeRange"(arg0: $BitSet$Type): void
public "getAllVariables"(): $Set<($VarVersionPair)>
public "replaceExprent"(arg0: $Exprent$Type, arg1: $Exprent$Type): void
public "processSforms"(arg0: $SFormsConstructor$Type, arg1: $VarMapHolder$Type, arg2: $Statement$Type, arg3: boolean): void
public "getNamedGenerics"(): $Map<($VarType), ($List<($VarType)>)>
public "setIsQualifier"(): void
public "findObject"(arg0: $MatchNode$Type, arg1: integer): $IMatchable
public "getInferredExprType"(arg0: $VarType$Type): $VarType
public "allowNewlineAfterQualifier"(): boolean
public "checkExprTypeBounds"(): $CheckTypesResult
public "setInvocationInstance"(): void
get "precedence"(): integer
get "exprentUse"(): integer
get "exprType"(): $VarType
get "allExprents"(): $List<($Exprent)>
get "allVariables"(): $Set<($VarVersionPair)>
get "namedGenerics"(): $Map<($VarType), ($List<($VarType)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Exprent$Type = ($Exprent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Exprent_ = $Exprent$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/collectors/$VarNamesCollector" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $VarNamesCollector {

constructor()
constructor(arg0: $Collection$Type<(string)>)

public "addName"(arg0: string): void
public "getFreeName"(arg0: string): string
public "getFreeName"(arg0: integer): string
public "addNames"(arg0: $Collection$Type<(string)>): void
public "getUsedNames"(): $Set<(string)>
get "usedNames"(): $Set<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VarNamesCollector$Type = ($VarNamesCollector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VarNamesCollector_ = $VarNamesCollector$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/$ClassesProcessor$ClassNode" {
import {$ClassWrapper, $ClassWrapper$Type} from "packages/org/jetbrains/java/decompiler/main/rels/$ClassWrapper"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$StructClass, $StructClass$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructClass"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$InvocationExprent, $InvocationExprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$InvocationExprent"
import {$VarType, $VarType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$VarType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ClassesProcessor$ClassNode$Type, $ClassesProcessor$ClassNode$Type$Type} from "packages/org/jetbrains/java/decompiler/main/$ClassesProcessor$ClassNode$Type"
import {$ClassesProcessor$ClassNode$LambdaInformation, $ClassesProcessor$ClassNode$LambdaInformation$Type} from "packages/org/jetbrains/java/decompiler/main/$ClassesProcessor$ClassNode$LambdaInformation"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$VarVersionPair, $VarVersionPair$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarVersionPair"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ClassesProcessor$ClassNode implements $Comparable<($ClassesProcessor$ClassNode)> {
 "type": $ClassesProcessor$ClassNode$Type
 "access": integer
 "simpleName": string
readonly "classStruct": $StructClass
 "enclosingMethod": string
 "superInvocation": $InvocationExprent
readonly "mapFieldsToVars": $Map<(string), ($VarVersionPair)>
 "anonymousClassType": $VarType
readonly "nested": $List<($ClassesProcessor$ClassNode)>
readonly "enclosingClasses": $Set<(string)>
 "parent": $ClassesProcessor$ClassNode
 "lambdaInformation": $ClassesProcessor$ClassNode$LambdaInformation

constructor(arg0: string, arg1: string, arg2: string, arg3: integer, arg4: string, arg5: string, arg6: string, arg7: $StructClass$Type)
constructor(arg0: $ClassesProcessor$ClassNode$Type$Type, arg1: $StructClass$Type)

public "toString"(): string
public "compareTo"(arg0: $ClassesProcessor$ClassNode$Type): integer
public "getClassNode"(arg0: string): $ClassesProcessor$ClassNode
public "getWrapper"(): $ClassWrapper
public "getAllNested"(): $Set<($ClassesProcessor$ClassNode)>
get "wrapper"(): $ClassWrapper
get "allNested"(): $Set<($ClassesProcessor$ClassNode)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassesProcessor$ClassNode$Type = ($ClassesProcessor$ClassNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassesProcessor$ClassNode_ = $ClassesProcessor$ClassNode$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/flow/$DirectNode" {
import {$BasicBlockStatement, $BasicBlockStatement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$BasicBlockStatement"
import {$Exprent, $Exprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$Exprent"
import {$List, $List$Type} from "packages/java/util/$List"
import {$DirectEdgeType, $DirectEdgeType$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/flow/$DirectEdgeType"
import {$DirectNodeType, $DirectNodeType$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/flow/$DirectNodeType"
import {$Statement, $Statement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$Statement"
import {$DirectEdge, $DirectEdge$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/flow/$DirectEdge"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $DirectNode {
readonly "type": $DirectNodeType
readonly "id": string
 "block": $BasicBlockStatement
readonly "statement": $Statement
 "exprents": $List<($Exprent)>
readonly "tryFinally": $DirectNode


public "getSuccessors"(arg0: $DirectEdgeType$Type): $List<($DirectEdge)>
public "hasPredecessors"(arg0: $DirectEdgeType$Type): boolean
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getPredecessors"(arg0: $DirectEdgeType$Type): $List<($DirectEdge)>
public "addSuccessor"(arg0: $DirectEdge$Type): void
public "hasSuccessors"(arg0: $DirectEdgeType$Type): boolean
public static "forStat"(arg0: $DirectNodeType$Type, arg1: $Statement$Type, arg2: $DirectNode$Type): $DirectNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DirectNode$Type = ($DirectNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DirectNode_ = $DirectNode$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/collectors/$CounterContainer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $CounterContainer {
static readonly "STATEMENT_COUNTER": integer
static readonly "EXPRESSION_COUNTER": integer
static readonly "VAR_COUNTER": integer

constructor()

public "setCounter"(arg0: integer, arg1: integer): void
public "getCounter"(arg0: integer): integer
public "getCounterAndIncrement"(arg0: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CounterContainer$Type = ($CounterContainer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CounterContainer_ = $CounterContainer$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$InvocationExprent$Type" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $InvocationExprent$Type extends $Enum<($InvocationExprent$Type)> {
static readonly "GENERAL": $InvocationExprent$Type
static readonly "INIT": $InvocationExprent$Type
static readonly "CLINIT": $InvocationExprent$Type


public static "values"(): ($InvocationExprent$Type)[]
public static "valueOf"(arg0: string): $InvocationExprent$Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InvocationExprent$Type$Type = (("general") | ("init") | ("clinit")) | ($InvocationExprent$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InvocationExprent$Type_ = $InvocationExprent$Type$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/rels/$ClassWrapper" {
import {$StructClass, $StructClass$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructClass"
import {$Exprent, $Exprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$Exprent"
import {$LanguageSpec, $LanguageSpec$Type} from "packages/org/jetbrains/java/decompiler/api/plugin/$LanguageSpec"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$VBStyleCollection, $VBStyleCollection$Type} from "packages/org/jetbrains/java/decompiler/util/collections/$VBStyleCollection"
import {$MethodWrapper, $MethodWrapper$Type} from "packages/org/jetbrains/java/decompiler/main/rels/$MethodWrapper"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ClassWrapper {

constructor(arg0: $StructClass$Type)

public "getStaticFieldInitializers"(): $VBStyleCollection<($Exprent), (string)>
public "getDynamicFieldInitializers"(): $VBStyleCollection<($Exprent), (string)>
public "toString"(): string
public "getMethods"(): $VBStyleCollection<($MethodWrapper), (string)>
public "init"(arg0: $LanguageSpec$Type): void
public "getMethodWrapper"(arg0: integer): $MethodWrapper
public "getMethodWrapper"(arg0: string, arg1: string): $MethodWrapper
public "getClassStruct"(): $StructClass
public "getHiddenMembers"(): $Set<(string)>
get "staticFieldInitializers"(): $VBStyleCollection<($Exprent), (string)>
get "dynamicFieldInitializers"(): $VBStyleCollection<($Exprent), (string)>
get "methods"(): $VBStyleCollection<($MethodWrapper), (string)>
get "classStruct"(): $StructClass
get "hiddenMembers"(): $Set<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassWrapper$Type = ($ClassWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassWrapper_ = $ClassWrapper$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarVersionNode" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$StructLocalVariableTableAttribute$LocalVariable, $StructLocalVariableTableAttribute$LocalVariable$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute$LocalVariable"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$VarVersionNode$State, $VarVersionNode$State$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarVersionNode$State"
import {$SFormsFastMapDirect, $SFormsFastMapDirect$Type} from "packages/org/jetbrains/java/decompiler/util/collections/$SFormsFastMapDirect"
import {$IGraphNode, $IGraphNode$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/decompose/$IGraphNode"
import {$VarVersionPair, $VarVersionPair$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarVersionPair"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $VarVersionNode implements $IGraphNode {
readonly "var": integer
readonly "version": integer
readonly "successors": $Set<($VarVersionNode)>
readonly "predecessors": $Set<($VarVersionNode)>
 "phantomNode": $VarVersionNode
 "phantomParentNode": $VarVersionNode
 "live": $SFormsFastMapDirect
 "lvt": $StructLocalVariableTableAttribute$LocalVariable
 "state": $VarVersionNode$State


public "toString"(): string
public "getPredecessors"(): $Collection<($VarVersionNode)>
public "hasSinglePredecessor"(): boolean
public "asPair"(): $VarVersionPair
public "removePredecessor"(arg0: $VarVersionNode$Type): void
public "removeSuccessor"(arg0: $VarVersionNode$Type): void
public "getSinglePredecessor"(): $VarVersionNode
public "hasAnySuccessors"(): boolean
get "predecessors"(): $Collection<($VarVersionNode)>
get "singlePredecessor"(): $VarVersionNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VarVersionNode$Type = ($VarVersionNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VarVersionNode_ = $VarVersionNode$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/code/cfg/$ControlFlowGraph" {
import {$ExceptionRangeCFG, $ExceptionRangeCFG$Type} from "packages/org/jetbrains/java/decompiler/code/cfg/$ExceptionRangeCFG"
import {$StructClass, $StructClass$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructClass"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$InstructionSequence, $InstructionSequence$Type} from "packages/org/jetbrains/java/decompiler/code/$InstructionSequence"
import {$VBStyleCollection, $VBStyleCollection$Type} from "packages/org/jetbrains/java/decompiler/util/collections/$VBStyleCollection"
import {$CodeConstants, $CodeConstants$Type} from "packages/org/jetbrains/java/decompiler/code/$CodeConstants"
import {$BasicBlock, $BasicBlock$Type} from "packages/org/jetbrains/java/decompiler/code/cfg/$BasicBlock"
import {$StructMethod, $StructMethod$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructMethod"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ControlFlowGraph implements $CodeConstants {
 "last_id": integer
 "commentLines": $Set<(string)>
 "addErrorComment": boolean

constructor(arg0: $InstructionSequence$Type)

public "getReversePostOrder"(): $List<($BasicBlock)>
public "toString"(): string
public "getFirst"(): $BasicBlock
public "getLast"(): $BasicBlock
public "getSequence"(): $InstructionSequence
public "removeMarkers"(): void
public "getExceptionRange"(arg0: $BasicBlock$Type, arg1: $BasicBlock$Type): $ExceptionRangeCFG
public "getFinallyExits"(): $Set<($BasicBlock)>
public "getExceptions"(): $List<($ExceptionRangeCFG)>
public "addComment"(arg0: string): void
public "removeBlock"(arg0: $BasicBlock$Type): void
public "getBlocks"(): $VBStyleCollection<($BasicBlock), (integer)>
public "setFirst"(arg0: $BasicBlock$Type): void
public "inlineJsr"(arg0: $StructClass$Type, arg1: $StructMethod$Type): void
public static "isSignaturePolymorphic"(arg0: string, arg1: string, arg2: boolean): boolean
public static "isReturnPolymorphic"(arg0: string, arg1: string): boolean
public static "areParametersPolymorphic"(arg0: string, arg1: string): boolean
get "reversePostOrder"(): $List<($BasicBlock)>
get "first"(): $BasicBlock
get "last"(): $BasicBlock
get "sequence"(): $InstructionSequence
get "finallyExits"(): $Set<($BasicBlock)>
get "exceptions"(): $List<($ExceptionRangeCFG)>
get "blocks"(): $VBStyleCollection<($BasicBlock), (integer)>
set "first"(value: $BasicBlock$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ControlFlowGraph$Type = ($ControlFlowGraph);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ControlFlowGraph_ = $ControlFlowGraph$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/gen/generics/$GenericMethodDescriptor" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$VarType, $VarType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$VarType"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $GenericMethodDescriptor {
readonly "typeParameters": $List<(string)>
readonly "typeParameterBounds": $List<($List<($VarType)>)>
readonly "parameterTypes": $List<($VarType)>
readonly "returnType": $VarType
readonly "exceptionTypes": $List<($VarType)>

constructor(arg0: $List$Type<(string)>, arg1: $List$Type<($List$Type<($VarType$Type)>)>, arg2: $List$Type<($VarType$Type)>, arg3: $VarType$Type, arg4: $List$Type<($VarType$Type)>)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenericMethodDescriptor$Type = ($GenericMethodDescriptor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenericMethodDescriptor_ = $GenericMethodDescriptor$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/extern/$TextTokenVisitor$Factory" {
import {$TextTokenVisitor, $TextTokenVisitor$Type} from "packages/org/jetbrains/java/decompiler/main/extern/$TextTokenVisitor"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $TextTokenVisitor$Factory {

 "create"(arg0: $TextTokenVisitor$Type): $TextTokenVisitor
 "andThen"(arg0: $TextTokenVisitor$Factory$Type): $TextTokenVisitor$Factory

(arg0: $TextTokenVisitor$Type): $TextTokenVisitor
}

export namespace $TextTokenVisitor$Factory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextTokenVisitor$Factory$Type = ($TextTokenVisitor$Factory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextTokenVisitor$Factory_ = $TextTokenVisitor$Factory$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$Statement$StatementType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Statement$StatementType extends $Enum<($Statement$StatementType)> {
static readonly "ROOT": $Statement$StatementType
static readonly "BASIC_BLOCK": $Statement$StatementType
static readonly "SEQUENCE": $Statement$StatementType
static readonly "DUMMY_EXIT": $Statement$StatementType
static readonly "GENERAL": $Statement$StatementType
static readonly "IF": $Statement$StatementType
static readonly "DO": $Statement$StatementType
static readonly "SWITCH": $Statement$StatementType
static readonly "SYNCHRONIZED": $Statement$StatementType
static readonly "TRY_CATCH": $Statement$StatementType
static readonly "CATCH_ALL": $Statement$StatementType
static readonly "OTHER": $Statement$StatementType


public static "values"(): ($Statement$StatementType)[]
public static "valueOf"(arg0: string): $Statement$StatementType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Statement$StatementType$Type = (("try_catch") | ("sequence") | ("general") | ("synchronized") | ("other") | ("dummy_exit") | ("root") | ("basic_block") | ("do") | ("catch_all") | ("if") | ("switch")) | ($Statement$StatementType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Statement$StatementType_ = $Statement$StatementType$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/code/cfg/$ExceptionRangeCFG" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$BasicBlock, $BasicBlock$Type} from "packages/org/jetbrains/java/decompiler/code/cfg/$BasicBlock"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ExceptionRangeCFG {

constructor(arg0: $List$Type<($BasicBlock$Type)>, arg1: $BasicBlock$Type, arg2: $List$Type<(string)>)
constructor(arg0: $List$Type<($BasicBlock$Type)>, arg1: $BasicBlock$Type, arg2: string)

public "getUniqueExceptionsString"(): string
public "toString"(): string
public "getExceptionTypes"(): $List<(string)>
public "getHandler"(): $BasicBlock
public "setHandler"(arg0: $BasicBlock$Type): void
public "isCircular"(): boolean
public "getProtectedRange"(): $List<($BasicBlock)>
public "addExceptionType"(arg0: string): void
get "uniqueExceptionsString"(): string
get "exceptionTypes"(): $List<(string)>
get "handler"(): $BasicBlock
set "handler"(value: $BasicBlock$Type)
get "circular"(): boolean
get "protectedRange"(): $List<($BasicBlock)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExceptionRangeCFG$Type = ($ExceptionRangeCFG);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExceptionRangeCFG_ = $ExceptionRangeCFG$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $BytecodeVersion implements $Comparable<($BytecodeVersion)> {
readonly "major": integer
readonly "minor": integer
static readonly "PREVIEW": integer
static readonly "MAJOR_1_0_2": integer
static readonly "MAJOR_1_2": integer
static readonly "MAJOR_1_3": integer
static readonly "MAJOR_1_4": integer
static readonly "MAJOR_5": integer
static readonly "MAJOR_6": integer
static readonly "MAJOR_7": integer
static readonly "MAJOR_8": integer
static readonly "MAJOR_9": integer
static readonly "MAJOR_10": integer
static readonly "MAJOR_11": integer
static readonly "MAJOR_12": integer
static readonly "MAJOR_13": integer
static readonly "MAJOR_14": integer
static readonly "MAJOR_15": integer
static readonly "MAJOR_16": integer
static readonly "MAJOR_17": integer
static readonly "MAJOR_18": integer
static readonly "MAJOR_19": integer
static readonly "MAJOR_20": integer
static readonly "MAJOR_21": integer

constructor(arg0: integer, arg1: integer)

public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "compareTo"(arg0: $BytecodeVersion$Type): integer
public "predatesJava"(): boolean
public "hasEnums"(): boolean
public "hasSealedClasses"(): boolean
public "hasJsr"(): boolean
public "hasLambdas"(): boolean
public "hasInvokeDynamic"(): boolean
public "hasOverride"(): boolean
public "hasSwitchPatternMatch"(): boolean
public "hasIfPatternMatching"(): boolean
public "has14ClassReferences"(): boolean
public "hasNewTryWithResources"(): boolean
public "hasSwitchExpressions"(): boolean
public "hasIndyStringConcat"(): boolean
public "hasLocalEnumsAndInterfaces"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BytecodeVersion$Type = ($BytecodeVersion);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BytecodeVersion_ = $BytecodeVersion$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$AnnotationExprent" {
import {$TextBuffer, $TextBuffer$Type} from "packages/org/jetbrains/java/decompiler/util/$TextBuffer"
import {$Exprent, $Exprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$Exprent"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AnnotationExprent$Type, $AnnotationExprent$Type$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$AnnotationExprent$Type"
import {$Exprent$Type, $Exprent$Type$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$Exprent$Type"
import {$BitSet, $BitSet$Type} from "packages/java/util/$BitSet"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $AnnotationExprent extends $Exprent {
static readonly "MULTIPLE_USES": integer
static readonly "SIDE_EFFECTS_FREE": integer
static readonly "BOTH_FLAGS": integer
readonly "type": $Exprent$Type
readonly "id": integer
 "bytecode": $BitSet

constructor(arg0: string, arg1: $List$Type<(string)>, arg2: $List$Type<(any)>)

public "equals"(arg0: any): boolean
public "getAnnotationType"(): $AnnotationExprent$Type
public "copy"(): $Exprent
public "getClassName"(): string
public "getParValues"(): $List<(any)>
public "getParNames"(): $List<(string)>
public "toJava"(arg0: integer): $TextBuffer
public "getBytecodeRange"(arg0: $BitSet$Type): void
get "annotationType"(): $AnnotationExprent$Type
get "className"(): string
get "parValues"(): $List<(any)>
get "parNames"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnnotationExprent$Type = ($AnnotationExprent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnnotationExprent_ = $AnnotationExprent$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute$LocalVariable" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$VarType, $VarType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$VarType"
import {$VarVersionPair, $VarVersionPair$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarVersionPair"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructLocalVariableTableAttribute$LocalVariable implements $Comparable<($StructLocalVariableTableAttribute$LocalVariable)> {


public "getName"(): string
public "toString"(): string
public "compareTo"(arg0: $StructLocalVariableTableAttribute$LocalVariable$Type): integer
public "getDescriptor"(): string
public "getSignature"(): string
public "rename"(arg0: string): $StructLocalVariableTableAttribute$LocalVariable
public "getVersion"(): $VarVersionPair
public "getEnd"(): integer
public "getVarType"(): $VarType
public "getStart"(): integer
get "name"(): string
get "descriptor"(): string
get "signature"(): string
get "version"(): $VarVersionPair
get "end"(): integer
get "varType"(): $VarType
get "start"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructLocalVariableTableAttribute$LocalVariable$Type = ($StructLocalVariableTableAttribute$LocalVariable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructLocalVariableTableAttribute$LocalVariable_ = $StructLocalVariableTableAttribute$LocalVariable$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructCodeAttribute" {
import {$StructMethodParametersAttribute, $StructMethodParametersAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute"
import {$StructNestHostAttribute, $StructNestHostAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructNestHostAttribute"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$Key, $Key$Type} from "packages/org/jetbrains/java/decompiler/util/$Key"
import {$StructAnnotationParameterAttribute, $StructAnnotationParameterAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationParameterAttribute"
import {$StructPermittedSubclassesAttribute, $StructPermittedSubclassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructPermittedSubclassesAttribute"
import {$StructLineNumberTableAttribute, $StructLineNumberTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLineNumberTableAttribute"
import {$StructEnclosingMethodAttribute, $StructEnclosingMethodAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructEnclosingMethodAttribute"
import {$StructSourceFileAttribute, $StructSourceFileAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructSourceFileAttribute"
import {$StructBootstrapMethodsAttribute, $StructBootstrapMethodsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructBootstrapMethodsAttribute"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$StructGenericSignatureAttribute, $StructGenericSignatureAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGenericSignatureAttribute"
import {$StructLocalVariableTableAttribute, $StructLocalVariableTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute"
import {$StructTypeAnnotationAttribute, $StructTypeAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructTypeAnnotationAttribute"
import {$StructConstantValueAttribute, $StructConstantValueAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructConstantValueAttribute"
import {$StructAnnotationAttribute, $StructAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationAttribute"
import {$StructRecordAttribute, $StructRecordAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructRecordAttribute"
import {$StructModuleAttribute, $StructModuleAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute"
import {$StructGeneralAttribute, $StructGeneralAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGeneralAttribute"
import {$StructInnerClassesAttribute, $StructInnerClassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute"
import {$StructLocalVariableTypeTableAttribute, $StructLocalVariableTypeTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTypeTableAttribute"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructExceptionsAttribute, $StructExceptionsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructExceptionsAttribute"
import {$StructAnnDefaultAttribute, $StructAnnDefaultAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnDefaultAttribute"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructCodeAttribute extends $StructGeneralAttribute {
 "localVariables": integer
 "codeAndExceptionData": (byte)[]
 "codeAttributes": $Map<(string), ($StructGeneralAttribute)>
static readonly "ATTRIBUTE_CODE": $Key<($StructCodeAttribute)>
static readonly "ATTRIBUTE_INNER_CLASSES": $Key<($StructInnerClassesAttribute)>
static readonly "ATTRIBUTE_SIGNATURE": $Key<($StructGenericSignatureAttribute)>
static readonly "ATTRIBUTE_ANNOTATION_DEFAULT": $Key<($StructAnnDefaultAttribute)>
static readonly "ATTRIBUTE_EXCEPTIONS": $Key<($StructExceptionsAttribute)>
static readonly "ATTRIBUTE_ENCLOSING_METHOD": $Key<($StructEnclosingMethodAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TABLE": $Key<($StructLocalVariableTableAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TYPE_TABLE": $Key<($StructLocalVariableTypeTableAttribute)>
static readonly "ATTRIBUTE_CONSTANT_VALUE": $Key<($StructConstantValueAttribute)>
static readonly "ATTRIBUTE_BOOTSTRAP_METHODS": $Key<($StructBootstrapMethodsAttribute)>
static readonly "ATTRIBUTE_SYNTHETIC": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_DEPRECATED": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_LINE_NUMBER_TABLE": $Key<($StructLineNumberTableAttribute)>
static readonly "ATTRIBUTE_METHOD_PARAMETERS": $Key<($StructMethodParametersAttribute)>
static readonly "ATTRIBUTE_MODULE": $Key<($StructModuleAttribute)>
static readonly "ATTRIBUTE_RECORD": $Key<($StructRecordAttribute)>
static readonly "ATTRIBUTE_PERMITTED_SUBCLASSES": $Key<($StructPermittedSubclassesAttribute)>
static readonly "ATTRIBUTE_SOURCE_FILE": $Key<($StructSourceFileAttribute)>
static readonly "ATTRIBUTE_NEST_HOST": $Key<($StructNestHostAttribute)>

constructor()

public "initContent"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructCodeAttribute$Type = ($StructCodeAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructCodeAttribute_ = $StructCodeAttribute$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/gen/generics/$GenericClassDescriptor" {
import {$GenericType, $GenericType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/generics/$GenericType"
import {$VarType, $VarType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$VarType"
import {$List, $List$Type} from "packages/java/util/$List"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $GenericClassDescriptor {
 "superclass": $VarType
 "genericType": $GenericType
readonly "superinterfaces": $List<($VarType)>
readonly "fparameters": $List<(string)>
readonly "fbounds": $List<($List<($VarType)>)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenericClassDescriptor$Type = ($GenericClassDescriptor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenericClassDescriptor_ = $GenericClassDescriptor$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/util/collections/$VBStyleCollection" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $VBStyleCollection<E, K> extends $ArrayList<(E)> {

constructor()
constructor(arg0: integer)

public "add"(arg0: integer, arg1: E): void
public "add"(arg0: E): boolean
public "remove"(arg0: integer): E
public "remove"(arg0: any): boolean
public "clone"(): $VBStyleCollection<(E), (K)>
public "clear"(): void
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "getKey"(arg0: integer): K
public "containsKey"(arg0: K): boolean
public "getLast"(): E
public "getWithKey"(arg0: K): E
public "addWithKey"(arg0: E, arg1: K): void
public "toStringVb"(): string
public "removeWithKey"(arg0: K): void
public "getLstKeys"(): $ArrayList<(K)>
public "putWithKey"(arg0: E, arg1: K): E
public "addAllWithKey"(arg0: $Collection$Type<(E)>, arg1: $Collection$Type<(K)>): void
public "addWithKeyAndIndex"(arg0: integer, arg1: E, arg2: K): void
public "getIndexByKey"(arg0: K): integer
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E): $List<(E)>
public static "of"<E>(arg0: E): $List<(E)>
public static "of"<E>(): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $List<(E)>
public static "of"<E>(...arg0: (E)[]): $List<(E)>
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
get "last"(): E
get "lstKeys"(): $ArrayList<(K)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VBStyleCollection$Type<E, K> = ($VBStyleCollection<(E), (K)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VBStyleCollection_<E, K> = $VBStyleCollection$Type<(E), (K)>;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute" {
import {$StructMethodParametersAttribute, $StructMethodParametersAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute"
import {$StructNestHostAttribute, $StructNestHostAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructNestHostAttribute"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$Key, $Key$Type} from "packages/org/jetbrains/java/decompiler/util/$Key"
import {$StructAnnotationParameterAttribute, $StructAnnotationParameterAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationParameterAttribute"
import {$StructPermittedSubclassesAttribute, $StructPermittedSubclassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructPermittedSubclassesAttribute"
import {$StructLineNumberTableAttribute, $StructLineNumberTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLineNumberTableAttribute"
import {$StructCodeAttribute, $StructCodeAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructCodeAttribute"
import {$StructEnclosingMethodAttribute, $StructEnclosingMethodAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructEnclosingMethodAttribute"
import {$StructSourceFileAttribute, $StructSourceFileAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructSourceFileAttribute"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructInnerClassesAttribute$Entry, $StructInnerClassesAttribute$Entry$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute$Entry"
import {$StructBootstrapMethodsAttribute, $StructBootstrapMethodsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructBootstrapMethodsAttribute"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$StructGenericSignatureAttribute, $StructGenericSignatureAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGenericSignatureAttribute"
import {$StructLocalVariableTableAttribute, $StructLocalVariableTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute"
import {$StructTypeAnnotationAttribute, $StructTypeAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructTypeAnnotationAttribute"
import {$StructConstantValueAttribute, $StructConstantValueAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructConstantValueAttribute"
import {$StructAnnotationAttribute, $StructAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationAttribute"
import {$StructRecordAttribute, $StructRecordAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructRecordAttribute"
import {$StructModuleAttribute, $StructModuleAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute"
import {$StructGeneralAttribute, $StructGeneralAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGeneralAttribute"
import {$StructLocalVariableTypeTableAttribute, $StructLocalVariableTypeTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTypeTableAttribute"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructExceptionsAttribute, $StructExceptionsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructExceptionsAttribute"
import {$StructAnnDefaultAttribute, $StructAnnDefaultAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnDefaultAttribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructInnerClassesAttribute extends $StructGeneralAttribute {
static readonly "ATTRIBUTE_CODE": $Key<($StructCodeAttribute)>
static readonly "ATTRIBUTE_INNER_CLASSES": $Key<($StructInnerClassesAttribute)>
static readonly "ATTRIBUTE_SIGNATURE": $Key<($StructGenericSignatureAttribute)>
static readonly "ATTRIBUTE_ANNOTATION_DEFAULT": $Key<($StructAnnDefaultAttribute)>
static readonly "ATTRIBUTE_EXCEPTIONS": $Key<($StructExceptionsAttribute)>
static readonly "ATTRIBUTE_ENCLOSING_METHOD": $Key<($StructEnclosingMethodAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TABLE": $Key<($StructLocalVariableTableAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TYPE_TABLE": $Key<($StructLocalVariableTypeTableAttribute)>
static readonly "ATTRIBUTE_CONSTANT_VALUE": $Key<($StructConstantValueAttribute)>
static readonly "ATTRIBUTE_BOOTSTRAP_METHODS": $Key<($StructBootstrapMethodsAttribute)>
static readonly "ATTRIBUTE_SYNTHETIC": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_DEPRECATED": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_LINE_NUMBER_TABLE": $Key<($StructLineNumberTableAttribute)>
static readonly "ATTRIBUTE_METHOD_PARAMETERS": $Key<($StructMethodParametersAttribute)>
static readonly "ATTRIBUTE_MODULE": $Key<($StructModuleAttribute)>
static readonly "ATTRIBUTE_RECORD": $Key<($StructRecordAttribute)>
static readonly "ATTRIBUTE_PERMITTED_SUBCLASSES": $Key<($StructPermittedSubclassesAttribute)>
static readonly "ATTRIBUTE_SOURCE_FILE": $Key<($StructSourceFileAttribute)>
static readonly "ATTRIBUTE_NEST_HOST": $Key<($StructNestHostAttribute)>

constructor()

public "getEntries"(): $List<($StructInnerClassesAttribute$Entry)>
public "initContent"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): void
get "entries"(): $List<($StructInnerClassesAttribute$Entry)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructInnerClassesAttribute$Type = ($StructInnerClassesAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructInnerClassesAttribute_ = $StructInnerClassesAttribute$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/$StructRecordComponent" {
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructField, $StructField$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructField"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructRecordComponent extends $StructField {


public static "create"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): $StructRecordComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructRecordComponent$Type = ($StructRecordComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructRecordComponent_ = $StructRecordComponent$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/match/$IMatchable" {
import {$MatchEngine, $MatchEngine$Type} from "packages/org/jetbrains/java/decompiler/struct/match/$MatchEngine"
import {$MatchNode, $MatchNode$Type} from "packages/org/jetbrains/java/decompiler/struct/match/$MatchNode"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IMatchable {

 "match"(arg0: $MatchNode$Type, arg1: $MatchEngine$Type): boolean
 "findObject"(arg0: $MatchNode$Type, arg1: integer): $IMatchable
}

export namespace $IMatchable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMatchable$Type = ($IMatchable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMatchable_ = $IMatchable$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream" {
import {$DataInputStream, $DataInputStream$Type} from "packages/java/io/$DataInputStream"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $DataInputFullStream extends $DataInputStream {

constructor(arg0: (byte)[])

public "read"(arg0: integer): (byte)[]
public "discard"(arg0: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataInputFullStream$Type = ($DataInputFullStream);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataInputFullStream_ = $DataInputFullStream$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/flow/$DirectGraph" {
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$DirectNode, $DirectNode$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/flow/$DirectNode"
import {$List, $List$Type} from "packages/java/util/$List"
import {$DirectGraph$ExprentIterator, $DirectGraph$ExprentIterator$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/flow/$DirectGraph$ExprentIterator"
import {$VBStyleCollection, $VBStyleCollection$Type} from "packages/org/jetbrains/java/decompiler/util/collections/$VBStyleCollection"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $DirectGraph {
readonly "nodes": $VBStyleCollection<($DirectNode), (string)>
readonly "extraNodes": $List<($DirectNode)>
 "first": $DirectNode
readonly "mapNegIfBranch": $HashMap<(string), (string)>
readonly "mapFinallyMonitorExceptionPathExits": $HashMap<(string), (string)>

constructor()

public "iterateExprents"(arg0: $DirectGraph$ExprentIterator$Type): boolean
public "sortReversePostOrder"(): void
public "iterateExprentsDeep"(arg0: $DirectGraph$ExprentIterator$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DirectGraph$Type = ($DirectGraph);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DirectGraph_ = $DirectGraph$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/api/plugin/$GraphParser" {
import {$RootStatement, $RootStatement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$RootStatement"
import {$ControlFlowGraph, $ControlFlowGraph$Type} from "packages/org/jetbrains/java/decompiler/code/cfg/$ControlFlowGraph"
import {$StructMethod, $StructMethod$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructMethod"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $GraphParser {

 "createStatement"(arg0: $ControlFlowGraph$Type, arg1: $StructMethod$Type): $RootStatement

(arg0: $ControlFlowGraph$Type, arg1: $StructMethod$Type): $RootStatement
}

export namespace $GraphParser {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GraphParser$Type = ($GraphParser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GraphParser_ = $GraphParser$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructPermittedSubclassesAttribute" {
import {$StructMethodParametersAttribute, $StructMethodParametersAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute"
import {$StructNestHostAttribute, $StructNestHostAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructNestHostAttribute"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$Key, $Key$Type} from "packages/org/jetbrains/java/decompiler/util/$Key"
import {$StructAnnotationParameterAttribute, $StructAnnotationParameterAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationParameterAttribute"
import {$StructLineNumberTableAttribute, $StructLineNumberTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLineNumberTableAttribute"
import {$StructCodeAttribute, $StructCodeAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructCodeAttribute"
import {$StructEnclosingMethodAttribute, $StructEnclosingMethodAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructEnclosingMethodAttribute"
import {$StructSourceFileAttribute, $StructSourceFileAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructSourceFileAttribute"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructBootstrapMethodsAttribute, $StructBootstrapMethodsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructBootstrapMethodsAttribute"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$StructGenericSignatureAttribute, $StructGenericSignatureAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGenericSignatureAttribute"
import {$StructLocalVariableTableAttribute, $StructLocalVariableTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute"
import {$StructTypeAnnotationAttribute, $StructTypeAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructTypeAnnotationAttribute"
import {$StructConstantValueAttribute, $StructConstantValueAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructConstantValueAttribute"
import {$StructAnnotationAttribute, $StructAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationAttribute"
import {$StructRecordAttribute, $StructRecordAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructRecordAttribute"
import {$StructModuleAttribute, $StructModuleAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute"
import {$StructGeneralAttribute, $StructGeneralAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGeneralAttribute"
import {$StructInnerClassesAttribute, $StructInnerClassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute"
import {$StructLocalVariableTypeTableAttribute, $StructLocalVariableTypeTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTypeTableAttribute"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructExceptionsAttribute, $StructExceptionsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructExceptionsAttribute"
import {$StructAnnDefaultAttribute, $StructAnnDefaultAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnDefaultAttribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructPermittedSubclassesAttribute extends $StructGeneralAttribute {
static readonly "ATTRIBUTE_CODE": $Key<($StructCodeAttribute)>
static readonly "ATTRIBUTE_INNER_CLASSES": $Key<($StructInnerClassesAttribute)>
static readonly "ATTRIBUTE_SIGNATURE": $Key<($StructGenericSignatureAttribute)>
static readonly "ATTRIBUTE_ANNOTATION_DEFAULT": $Key<($StructAnnDefaultAttribute)>
static readonly "ATTRIBUTE_EXCEPTIONS": $Key<($StructExceptionsAttribute)>
static readonly "ATTRIBUTE_ENCLOSING_METHOD": $Key<($StructEnclosingMethodAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TABLE": $Key<($StructLocalVariableTableAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TYPE_TABLE": $Key<($StructLocalVariableTypeTableAttribute)>
static readonly "ATTRIBUTE_CONSTANT_VALUE": $Key<($StructConstantValueAttribute)>
static readonly "ATTRIBUTE_BOOTSTRAP_METHODS": $Key<($StructBootstrapMethodsAttribute)>
static readonly "ATTRIBUTE_SYNTHETIC": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_DEPRECATED": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_LINE_NUMBER_TABLE": $Key<($StructLineNumberTableAttribute)>
static readonly "ATTRIBUTE_METHOD_PARAMETERS": $Key<($StructMethodParametersAttribute)>
static readonly "ATTRIBUTE_MODULE": $Key<($StructModuleAttribute)>
static readonly "ATTRIBUTE_RECORD": $Key<($StructRecordAttribute)>
static readonly "ATTRIBUTE_PERMITTED_SUBCLASSES": $Key<($StructPermittedSubclassesAttribute)>
static readonly "ATTRIBUTE_SOURCE_FILE": $Key<($StructSourceFileAttribute)>
static readonly "ATTRIBUTE_NEST_HOST": $Key<($StructNestHostAttribute)>

constructor()

public "getClasses"(): $List<(string)>
public "initContent"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): void
get "classes"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructPermittedSubclassesAttribute$Type = ($StructPermittedSubclassesAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructPermittedSubclassesAttribute_ = $StructPermittedSubclassesAttribute$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$VarExprent" {
import {$Instruction, $Instruction$Type} from "packages/org/jetbrains/java/decompiler/code/$Instruction"
import {$SFormsConstructor, $SFormsConstructor$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/sforms/$SFormsConstructor"
import {$VarType, $VarType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$VarType"
import {$Exprent$Type, $Exprent$Type$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$Exprent$Type"
import {$BitSet, $BitSet$Type} from "packages/java/util/$BitSet"
import {$VarMapHolder, $VarMapHolder$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/sforms/$VarMapHolder"
import {$VarProcessor, $VarProcessor$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarProcessor"
import {$Statement, $Statement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$Statement"
import {$CheckTypesResult, $CheckTypesResult$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$CheckTypesResult"
import {$MatchEngine, $MatchEngine$Type} from "packages/org/jetbrains/java/decompiler/struct/match/$MatchEngine"
import {$TextBuffer, $TextBuffer$Type} from "packages/org/jetbrains/java/decompiler/util/$TextBuffer"
import {$StructLocalVariableTableAttribute$LocalVariable, $StructLocalVariableTableAttribute$LocalVariable$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute$LocalVariable"
import {$Exprent, $Exprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$Exprent"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MatchNode, $MatchNode$Type} from "packages/org/jetbrains/java/decompiler/struct/match/$MatchNode"
import {$VarVersionPair, $VarVersionPair$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarVersionPair"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $VarExprent extends $Exprent {
static readonly "STACK_BASE": integer
static readonly "VAR_NAMELESS_ENCLOSURE": string
static readonly "MULTIPLE_USES": integer
static readonly "SIDE_EFFECTS_FREE": integer
static readonly "BOTH_FLAGS": integer
readonly "type": $Exprent$Type
readonly "id": integer
 "bytecode": $BitSet

constructor(arg0: integer, arg1: $VarType$Type, arg2: $VarProcessor$Type)
constructor(arg0: integer, arg1: $VarType$Type, arg2: $VarProcessor$Type, arg3: $BitSet$Type)

public "setClassDef"(arg0: boolean): void
public "getLVT"(): $StructLocalVariableTableAttribute$LocalVariable
public "isEffectivelyFinal"(): boolean
public "getName"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "match"(arg0: $MatchNode$Type, arg1: $MatchEngine$Type): boolean
public "copy"(): $Exprent
public "getIndex"(): integer
public "setIndex"(arg0: integer): void
public "getVersion"(): integer
public "setVersion"(arg0: integer): void
public "setDefinition"(arg0: boolean): void
public "getVarType"(): $VarType
public "setVarType"(arg0: $VarType$Type): void
public "setStack"(arg0: boolean): void
public "getExprentUse"(): integer
public "getExprType"(): $VarType
public "toJava"(arg0: integer): $TextBuffer
public "getAllExprents"(arg0: $List$Type<($Exprent$Type)>): $List<($Exprent)>
public "getBytecodeRange"(arg0: $BitSet$Type): void
public "processSforms"(arg0: $SFormsConstructor$Type, arg1: $VarMapHolder$Type, arg2: $Statement$Type, arg3: boolean): void
public "getVarVersionPair"(): $VarVersionPair
public "isDefinition"(): boolean
public "getBackingInstr"(): $Instruction
public "isStack"(): boolean
public "setBackingInstr"(arg0: $Instruction$Type): void
public "isVarReferenced"(arg0: $Statement$Type, ...arg1: ($VarExprent$Type)[]): boolean
public "isVarReferenced"(arg0: $Exprent$Type, ...arg1: ($VarExprent$Type)[]): boolean
public "equalsVersions"(arg0: any): boolean
public "setBoundType"(arg0: $VarType$Type): void
public "isClassDef"(): boolean
public "getDefinitionType"(): string
public "getProcessor"(): $VarProcessor
public "getDefinitionVarType"(): $VarType
public "setEffectivelyFinal"(arg0: boolean): void
public "setLVT"(arg0: $StructLocalVariableTableAttribute$LocalVariable$Type): void
public "getInferredExprType"(arg0: $VarType$Type): $VarType
public "allowNewlineAfterQualifier"(): boolean
public "checkExprTypeBounds"(): $CheckTypesResult
set "classDef"(value: boolean)
get "lVT"(): $StructLocalVariableTableAttribute$LocalVariable
get "effectivelyFinal"(): boolean
get "name"(): string
get "index"(): integer
set "index"(value: integer)
get "version"(): integer
set "version"(value: integer)
set "definition"(value: boolean)
get "varType"(): $VarType
set "varType"(value: $VarType$Type)
set "stack"(value: boolean)
get "exprentUse"(): integer
get "exprType"(): $VarType
get "varVersionPair"(): $VarVersionPair
get "definition"(): boolean
get "backingInstr"(): $Instruction
get "stack"(): boolean
set "backingInstr"(value: $Instruction$Type)
set "boundType"(value: $VarType$Type)
get "classDef"(): boolean
get "definitionType"(): string
get "processor"(): $VarProcessor
get "definitionVarType"(): $VarType
set "effectivelyFinal"(value: boolean)
set "lVT"(value: $StructLocalVariableTableAttribute$LocalVariable$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VarExprent$Type = ($VarExprent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VarExprent_ = $VarExprent$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$AnnotationExprent$Type" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $AnnotationExprent$Type extends $Enum<($AnnotationExprent$Type)> {
static readonly "NORMAL": $AnnotationExprent$Type
static readonly "MARKER": $AnnotationExprent$Type
static readonly "SINGLE_ELEMENT": $AnnotationExprent$Type


public static "values"(): ($AnnotationExprent$Type)[]
public static "valueOf"(arg0: string): $AnnotationExprent$Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnnotationExprent$Type$Type = (("normal") | ("single_element") | ("marker")) | ($AnnotationExprent$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnnotationExprent$Type_ = $AnnotationExprent$Type$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/$StructClass" {
import {$StructField, $StructField$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructField"
import {$VarType, $VarType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$VarType"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$StructMethod, $StructMethod$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructMethod"
import {$GenericClassDescriptor, $GenericClassDescriptor$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/generics/$GenericClassDescriptor"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$PrimitiveConstant, $PrimitiveConstant$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$PrimitiveConstant"
import {$StructRecordComponent, $StructRecordComponent$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructRecordComponent"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructMember, $StructMember$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructMember"
import {$VBStyleCollection, $VBStyleCollection$Type} from "packages/org/jetbrains/java/decompiler/util/collections/$VBStyleCollection"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructClass extends $StructMember {
readonly "qualifiedName": string
readonly "superClass": $PrimitiveConstant


public "getAllSuperClasses"(): $List<($StructClass)>
public "getInterfaceNames"(): (string)[]
public "toString"(): string
public "getInterfaces"(): (integer)[]
public "getMethod"(arg0: string, arg1: string): $StructMethod
public "getMethod"(arg0: string): $StructMethod
public "getFields"(): $VBStyleCollection<($StructField), (string)>
public "getMethods"(): $VBStyleCollection<($StructMethod), (string)>
public "getField"(arg0: string, arg1: string): $StructField
public "getRecordComponents"(): $List<($StructRecordComponent)>
public "getSignature"(): $GenericClassDescriptor
public static "create"(arg0: $DataInputFullStream$Type, arg1: boolean): $StructClass
public "getPool"(): $ConstantPool
public "getVersion"(): $BytecodeVersion
public "releaseResources"(): void
public "getInterface"(arg0: integer): string
public "isOwn"(): boolean
public "hasField"(arg0: string, arg1: string): boolean
public "getAllGenerics"(): $Map<(string), ($Map<($VarType), ($VarType)>)>
public "getMethodRecursive"(arg0: string, arg1: string): $StructMethod
get "allSuperClasses"(): $List<($StructClass)>
get "interfaceNames"(): (string)[]
get "interfaces"(): (integer)[]
get "fields"(): $VBStyleCollection<($StructField), (string)>
get "methods"(): $VBStyleCollection<($StructMethod), (string)>
get "recordComponents"(): $List<($StructRecordComponent)>
get "signature"(): $GenericClassDescriptor
get "pool"(): $ConstantPool
get "version"(): $BytecodeVersion
get "own"(): boolean
get "allGenerics"(): $Map<(string), ($Map<($VarType), ($VarType)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructClass$Type = ($StructClass);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructClass_ = $StructClass$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/util/collections/$ListStack" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ListStack<T> extends $ArrayList<(T)> {

constructor()
constructor(arg0: $Collection$Type<(any)>)

public "peek"(): T
public "push"(arg0: T): void
public "pop"(): T
public "pop"(arg0: integer): T
public "insertByOffset"(arg0: integer, arg1: T): void
public "removeMultiple"(arg0: integer): void
public "getByOffset"(arg0: integer): T
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E): $List<(E)>
public static "of"<E>(arg0: E): $List<(E)>
public static "of"<E>(): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $List<(E)>
public static "of"<E>(...arg0: (E)[]): $List<(E)>
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListStack$Type<T> = ($ListStack<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListStack_<T> = $ListStack$Type<(T)>;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructNestHostAttribute" {
import {$StructMethodParametersAttribute, $StructMethodParametersAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$Key, $Key$Type} from "packages/org/jetbrains/java/decompiler/util/$Key"
import {$StructAnnotationParameterAttribute, $StructAnnotationParameterAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationParameterAttribute"
import {$StructPermittedSubclassesAttribute, $StructPermittedSubclassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructPermittedSubclassesAttribute"
import {$StructLineNumberTableAttribute, $StructLineNumberTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLineNumberTableAttribute"
import {$StructCodeAttribute, $StructCodeAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructCodeAttribute"
import {$StructEnclosingMethodAttribute, $StructEnclosingMethodAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructEnclosingMethodAttribute"
import {$StructSourceFileAttribute, $StructSourceFileAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructSourceFileAttribute"
import {$StructBootstrapMethodsAttribute, $StructBootstrapMethodsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructBootstrapMethodsAttribute"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$StructGenericSignatureAttribute, $StructGenericSignatureAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGenericSignatureAttribute"
import {$StructLocalVariableTableAttribute, $StructLocalVariableTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute"
import {$StructTypeAnnotationAttribute, $StructTypeAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructTypeAnnotationAttribute"
import {$StructConstantValueAttribute, $StructConstantValueAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructConstantValueAttribute"
import {$StructAnnotationAttribute, $StructAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationAttribute"
import {$StructRecordAttribute, $StructRecordAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructRecordAttribute"
import {$StructModuleAttribute, $StructModuleAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute"
import {$StructGeneralAttribute, $StructGeneralAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGeneralAttribute"
import {$StructInnerClassesAttribute, $StructInnerClassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute"
import {$StructLocalVariableTypeTableAttribute, $StructLocalVariableTypeTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTypeTableAttribute"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructExceptionsAttribute, $StructExceptionsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructExceptionsAttribute"
import {$StructAnnDefaultAttribute, $StructAnnDefaultAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnDefaultAttribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructNestHostAttribute extends $StructGeneralAttribute {
static readonly "ATTRIBUTE_CODE": $Key<($StructCodeAttribute)>
static readonly "ATTRIBUTE_INNER_CLASSES": $Key<($StructInnerClassesAttribute)>
static readonly "ATTRIBUTE_SIGNATURE": $Key<($StructGenericSignatureAttribute)>
static readonly "ATTRIBUTE_ANNOTATION_DEFAULT": $Key<($StructAnnDefaultAttribute)>
static readonly "ATTRIBUTE_EXCEPTIONS": $Key<($StructExceptionsAttribute)>
static readonly "ATTRIBUTE_ENCLOSING_METHOD": $Key<($StructEnclosingMethodAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TABLE": $Key<($StructLocalVariableTableAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TYPE_TABLE": $Key<($StructLocalVariableTypeTableAttribute)>
static readonly "ATTRIBUTE_CONSTANT_VALUE": $Key<($StructConstantValueAttribute)>
static readonly "ATTRIBUTE_BOOTSTRAP_METHODS": $Key<($StructBootstrapMethodsAttribute)>
static readonly "ATTRIBUTE_SYNTHETIC": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_DEPRECATED": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_LINE_NUMBER_TABLE": $Key<($StructLineNumberTableAttribute)>
static readonly "ATTRIBUTE_METHOD_PARAMETERS": $Key<($StructMethodParametersAttribute)>
static readonly "ATTRIBUTE_MODULE": $Key<($StructModuleAttribute)>
static readonly "ATTRIBUTE_RECORD": $Key<($StructRecordAttribute)>
static readonly "ATTRIBUTE_PERMITTED_SUBCLASSES": $Key<($StructPermittedSubclassesAttribute)>
static readonly "ATTRIBUTE_SOURCE_FILE": $Key<($StructSourceFileAttribute)>
static readonly "ATTRIBUTE_NEST_HOST": $Key<($StructNestHostAttribute)>

constructor()

public "getHostClass"(arg0: $ConstantPool$Type): string
public "initContent"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructNestHostAttribute$Type = ($StructNestHostAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructNestHostAttribute_ = $StructNestHostAttribute$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/consts/$LinkConstant" {
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$PooledConstant, $PooledConstant$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$PooledConstant"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $LinkConstant extends $PooledConstant {
 "index1": integer
 "index2": integer
 "classname": string
 "elementname": string
 "descriptor": string
readonly "type": integer

constructor(arg0: integer, arg1: string, arg2: string, arg3: string)
constructor(arg0: integer, arg1: integer, arg2: integer)

public "equals"(arg0: any): boolean
public "resolveConstant"(arg0: $ConstantPool$Type): void
public static "isSignaturePolymorphic"(arg0: string, arg1: string, arg2: boolean): boolean
public static "isReturnPolymorphic"(arg0: string, arg1: string): boolean
public static "areParametersPolymorphic"(arg0: string, arg1: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LinkConstant$Type = ($LinkConstant);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LinkConstant_ = $LinkConstant$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/sforms/$SFormsConstructor" {
import {$FieldExprent, $FieldExprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$FieldExprent"
import {$RootStatement, $RootStatement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$RootStatement"
import {$VarExprent, $VarExprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$VarExprent"
import {$VarVersionNode, $VarVersionNode$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarVersionNode"
import {$SFormsFastMapDirect, $SFormsFastMapDirect$Type} from "packages/org/jetbrains/java/decompiler/util/collections/$SFormsFastMapDirect"
import {$StructMethod, $StructMethod$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructMethod"
import {$DirectGraph, $DirectGraph$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/flow/$DirectGraph"
import {$VarMapHolder, $VarMapHolder$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/sforms/$VarMapHolder"
import {$Statement, $Statement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$Statement"
import {$VarVersionPair, $VarVersionPair$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarVersionPair"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $SFormsConstructor {
/**
 * 
 * @deprecated
 */
readonly "trackFieldVars": boolean
/**
 * 
 * @deprecated
 */
readonly "trackDirectAssignments": boolean

constructor(arg0: boolean, arg1: boolean)

public "updateVarExprent"(arg0: $VarExprent$Type, arg1: $Statement$Type, arg2: $SFormsFastMapDirect$Type, arg3: boolean): void
public "getOrCreatePhantom"(arg0: $VarVersionPair$Type): $VarVersionPair
public static "getFirstProtectedRange"(arg0: $Statement$Type): $Statement
public "fieldRead"(arg0: $FieldExprent$Type, arg1: $SFormsFastMapDirect$Type): void
public "varRead"(arg0: $VarMapHolder$Type, arg1: $Statement$Type, arg2: boolean, arg3: $VarExprent$Type): void
public "getFieldIndex"(arg0: $FieldExprent$Type): integer
public "markDirectAssignment"(arg0: $VarVersionPair$Type, arg1: $VarVersionPair$Type): void
public "splitVariables"(arg0: $RootStatement$Type, arg1: $StructMethod$Type): void
public "initParameter"(arg0: integer, arg1: $SFormsFastMapDirect$Type, arg2: boolean): void
public static "makeReadEdge"(arg0: $VarVersionNode$Type, arg1: $VarVersionNode$Type): void
public "getDirectGraph"(): $DirectGraph
get "directGraph"(): $DirectGraph
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SFormsConstructor$Type = ($SFormsConstructor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SFormsConstructor_ = $SFormsConstructor$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/collectors/$BytecodeMappingTracer" {
import {$StructLineNumberTableAttribute, $StructLineNumberTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLineNumberTableAttribute"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$BitSet, $BitSet$Type} from "packages/java/util/$BitSet"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $BytecodeMappingTracer {
static readonly "DUMMY": $BytecodeMappingTracer

constructor(arg0: integer)
constructor()

public "addTracer"(arg0: $BytecodeMappingTracer$Type): void
public "setLineNumberTable"(arg0: $StructLineNumberTableAttribute$Type): void
public "addMapping"(arg0: $BitSet$Type): void
public "addMapping"(arg0: integer): void
public "getUnmappedLines"(): $Set<(integer)>
public "getMapping"(): $Map<(integer), (integer)>
public "incrementCurrentSourceLine"(arg0: integer): void
public "incrementCurrentSourceLine"(): void
public "getCurrentSourceLine"(): integer
public "setCurrentSourceLine"(arg0: integer): void
public "getOriginalLinesMapping"(): $Map<(integer), (integer)>
set "lineNumberTable"(value: $StructLineNumberTableAttribute$Type)
get "unmappedLines"(): $Set<(integer)>
get "mapping"(): $Map<(integer), (integer)>
get "currentSourceLine"(): integer
set "currentSourceLine"(value: integer)
get "originalLinesMapping"(): $Map<(integer), (integer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BytecodeMappingTracer$Type = ($BytecodeMappingTracer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BytecodeMappingTracer_ = $BytecodeMappingTracer$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/util/$TextBuffer" {
import {$FieldDescriptor, $FieldDescriptor$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$FieldDescriptor"
import {$BytecodeMappingTracer, $BytecodeMappingTracer$Type} from "packages/org/jetbrains/java/decompiler/main/collectors/$BytecodeMappingTracer"
import {$TextTokenVisitor, $TextTokenVisitor$Type} from "packages/org/jetbrains/java/decompiler/main/extern/$TextTokenVisitor"
import {$List, $List$Type} from "packages/java/util/$List"
import {$VarType, $VarType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$VarType"
import {$MethodDescriptor, $MethodDescriptor$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$MethodDescriptor"
import {$TextToken, $TextToken$Type} from "packages/org/jetbrains/java/decompiler/util/token/$TextToken"
import {$BitSet, $BitSet$Type} from "packages/java/util/$BitSet"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Pair, $Pair$Type} from "packages/org/jetbrains/java/decompiler/util/$Pair"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $TextBuffer {

constructor()
constructor(arg0: string)
constructor(arg0: integer)

public "reformat"(): void
public "getTracers"(): $Map<($Pair<(string), (string)>), ($BytecodeMappingTracer)>
public "countLines"(arg0: integer): integer
public "countLines"(): integer
public "length"(): integer
public "toString"(): string
public "append"(arg0: character): $TextBuffer
public "append"(arg0: integer): $TextBuffer
public "append"(arg0: string): $TextBuffer
public "append"(arg0: $TextBuffer$Type): $TextBuffer
public "append"(arg0: $TextBuffer$Type, arg1: string, arg2: string): $TextBuffer
public "contentEquals"(arg0: string): boolean
public "count"(arg0: string, arg1: integer): integer
public "setLength"(arg0: integer): void
public "prepend"(arg0: string): $TextBuffer
public "appendText"(arg0: $TextBuffer$Type): $TextBuffer
/**
 * 
 * @deprecated
 */
public "countChars"(arg0: character): integer
public "setStart"(arg0: integer): void
public "getTokens"(): $List<($TextToken)>
public "enclose"(arg0: string, arg1: string): $TextBuffer
public "appendTypeName"(arg0: string, arg1: $VarType$Type): $TextBuffer
public "appendTypeName"(arg0: $VarType$Type): $TextBuffer
public "addClassToken"(arg0: integer, arg1: integer, arg2: string): $TextBuffer
public "appendIndent"(arg0: integer): $TextBuffer
public "appendMethod"(arg0: string, arg1: boolean, arg2: string, arg3: string, arg4: $MethodDescriptor$Type): $TextBuffer
public "appendMethod"(arg0: string, arg1: boolean, arg2: string, arg3: string, arg4: string): $TextBuffer
public "visitTokens"(arg0: $TextTokenVisitor$Type): void
public "appendCastTypeName"(arg0: $VarType$Type): $TextBuffer
public "appendCastTypeName"(arg0: string, arg1: $VarType$Type): $TextBuffer
public "popNewlineGroup"(): $TextBuffer
public "appendAllClasses"(arg0: string, arg1: string): $TextBuffer
public "encloseWithParens"(): $TextBuffer
public "appendField"(arg0: string, arg1: boolean, arg2: string, arg3: string, arg4: string): $TextBuffer
public "appendField"(arg0: string, arg1: boolean, arg2: string, arg3: string, arg4: $FieldDescriptor$Type): $TextBuffer
public "pushNewlineGroup"(arg0: integer, arg1: integer): $TextBuffer
public "addBytecodeMapping"(arg0: integer): void
public "addBytecodeMapping"(arg0: $BitSet$Type): void
public "getPos"(arg0: integer): string
public "addGenericTypeTokens"(arg0: integer, arg1: string, arg2: $VarType$Type): $TextBuffer
public "clearUnassignedBytecodeMappingData"(): void
public "containsOnlyWhitespaces"(): boolean
public "addStartBytecodeMapping"(arg0: $BitSet$Type): void
public "addStartBytecodeMapping"(arg0: integer): void
public "addTypeNameToken"(arg0: $VarType$Type, arg1: integer): $TextBuffer
public "appendClass"(arg0: string, arg1: boolean, arg2: string): $TextBuffer
public "addAllClassTokens"(arg0: integer, arg1: string, arg2: string): $TextBuffer
public "appendVariable"(arg0: string, arg1: boolean, arg2: boolean, arg3: string, arg4: string, arg5: string, arg6: integer, arg7: string): $TextBuffer
public "appendVariable"(arg0: string, arg1: boolean, arg2: boolean, arg3: string, arg4: string, arg5: $MethodDescriptor$Type, arg6: integer, arg7: string): $TextBuffer
public static "checkLeaks"(): void
public "convertToStringAndAllowDataDiscard"(): string
public "appendLineSeparator"(): $TextBuffer
public "appendPossibleNewline"(arg0: string, arg1: boolean): $TextBuffer
public "appendPossibleNewline"(arg0: string): $TextBuffer
public "appendPossibleNewline"(): $TextBuffer
public "dumpOriginalLineNumbers"(arg0: (integer)[]): void
get "tracers"(): $Map<($Pair<(string), (string)>), ($BytecodeMappingTracer)>
set "start"(value: integer)
get "tokens"(): $List<($TextToken)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextBuffer$Type = ($TextBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextBuffer_ = $TextBuffer$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute$RequiresEntry" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructModuleAttribute$RequiresEntry {
readonly "moduleName": string
readonly "flags": integer
readonly "moduleVersion": string

constructor(arg0: string, arg1: integer, arg2: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructModuleAttribute$RequiresEntry$Type = ($StructModuleAttribute$RequiresEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructModuleAttribute$RequiresEntry_ = $StructModuleAttribute$RequiresEntry$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$Statement$LastBasicType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Statement$LastBasicType extends $Enum<($Statement$LastBasicType)> {
static readonly "IF": $Statement$LastBasicType
static readonly "SWITCH": $Statement$LastBasicType
static readonly "GENERAL": $Statement$LastBasicType


public static "values"(): ($Statement$LastBasicType)[]
public static "valueOf"(arg0: string): $Statement$LastBasicType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Statement$LastBasicType$Type = (("general") | ("if") | ("switch")) | ($Statement$LastBasicType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Statement$LastBasicType_ = $Statement$LastBasicType$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/extern/$IResultSaver" {
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"
import {$Manifest, $Manifest$Type} from "packages/java/util/jar/$Manifest"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IResultSaver extends $AutoCloseable {

 "close"(): void
 "copyFile"(arg0: string, arg1: string, arg2: string): void
 "copyEntry"(arg0: string, arg1: string, arg2: string, arg3: string): void
 "saveDirEntry"(arg0: string, arg1: string, arg2: string): void
 "saveClassEntry"(arg0: string, arg1: string, arg2: string, arg3: string, arg4: string): void
 "saveClassEntry"(arg0: string, arg1: string, arg2: string, arg3: string, arg4: string, arg5: (integer)[]): void
 "createArchive"(arg0: string, arg1: string, arg2: $Manifest$Type): void
 "closeArchive"(arg0: string, arg1: string): void
 "saveClassFile"(arg0: string, arg1: string, arg2: string, arg3: string, arg4: (integer)[]): void
 "saveFolder"(arg0: string): void
 "getCodeLineData"(arg0: (integer)[]): (byte)[]
}

export namespace $IResultSaver {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IResultSaver$Type = ($IResultSaver);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IResultSaver_ = $IResultSaver$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$CheckTypesResult" {
import {$CheckTypesResult$ExprentTypePair, $CheckTypesResult$ExprentTypePair$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$CheckTypesResult$ExprentTypePair"
import {$Exprent, $Exprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$Exprent"
import {$List, $List$Type} from "packages/java/util/$List"
import {$VarType, $VarType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$VarType"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $CheckTypesResult {

constructor()

public "toString"(): string
public "getLstMinTypeExprents"(): $List<($CheckTypesResult$ExprentTypePair)>
public "getLstMaxTypeExprents"(): $List<($CheckTypesResult$ExprentTypePair)>
public "addMinTypeExprent"(arg0: $Exprent$Type, arg1: $VarType$Type): void
public "addMaxTypeExprent"(arg0: $Exprent$Type, arg1: $VarType$Type): void
get "lstMinTypeExprents"(): $List<($CheckTypesResult$ExprentTypePair)>
get "lstMaxTypeExprents"(): $List<($CheckTypesResult$ExprentTypePair)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CheckTypesResult$Type = ($CheckTypesResult);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CheckTypesResult_ = $CheckTypesResult$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/consts/$PrimitiveConstant" {
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$PooledConstant, $PooledConstant$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$PooledConstant"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $PrimitiveConstant extends $PooledConstant {
 "index": integer
 "value": any
 "isArray": boolean
readonly "type": integer

constructor(arg0: integer, arg1: any)
constructor(arg0: integer, arg1: integer)

public "equals"(arg0: any): boolean
public "getString"(): string
public "resolveConstant"(arg0: $ConstantPool$Type): void
public static "isSignaturePolymorphic"(arg0: string, arg1: string, arg2: boolean): boolean
public static "isReturnPolymorphic"(arg0: string, arg1: string): boolean
public static "areParametersPolymorphic"(arg0: string, arg1: string): boolean
get "string"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrimitiveConstant$Type = ($PrimitiveConstant);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrimitiveConstant_ = $PrimitiveConstant$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/flow/$DirectEdge" {
import {$DirectNode, $DirectNode$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/flow/$DirectNode"
import {$DirectEdgeType, $DirectEdgeType$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/flow/$DirectEdgeType"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $DirectEdge {

constructor(arg0: $DirectNode$Type, arg1: $DirectNode$Type, arg2: $DirectEdgeType$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "of"(arg0: $DirectNode$Type, arg1: $DirectNode$Type): $DirectEdge
public static "exception"(arg0: $DirectNode$Type, arg1: $DirectNode$Type): $DirectEdge
public "getType"(): $DirectEdgeType
public "getSource"(): $DirectNode
public "getDestination"(): $DirectNode
get "type"(): $DirectEdgeType
get "source"(): $DirectNode
get "destination"(): $DirectNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DirectEdge$Type = ($DirectEdge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DirectEdge_ = $DirectEdge$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/match/$MatchNode" {
import {$IMatchable$MatchProperties, $IMatchable$MatchProperties$Type} from "packages/org/jetbrains/java/decompiler/struct/match/$IMatchable$MatchProperties"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MatchNode$RuleValue, $MatchNode$RuleValue$Type} from "packages/org/jetbrains/java/decompiler/struct/match/$MatchNode$RuleValue"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MatchNode {
static readonly "MATCHNODE_STATEMENT": integer
static readonly "MATCHNODE_EXPRENT": integer

constructor(arg0: integer)

public "getType"(): integer
public "getChildren"(): $List<($MatchNode)>
public "addRule"(arg0: $IMatchable$MatchProperties$Type, arg1: $MatchNode$RuleValue$Type): void
public "addChild"(arg0: $MatchNode$Type): void
public "getRuleValue"(arg0: $IMatchable$MatchProperties$Type): any
public "iterateRules"(arg0: $BiFunction$Type<($IMatchable$MatchProperties$Type), ($MatchNode$RuleValue$Type), (boolean)>): boolean
public "getRawRule"(arg0: $IMatchable$MatchProperties$Type): $MatchNode$RuleValue
get "type"(): integer
get "children"(): $List<($MatchNode)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MatchNode$Type = ($MatchNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MatchNode_ = $MatchNode$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/extern/$IIdentifierRenamer" {
import {$IIdentifierRenamer$Type, $IIdentifierRenamer$Type$Type} from "packages/org/jetbrains/java/decompiler/main/extern/$IIdentifierRenamer$Type"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IIdentifierRenamer {

 "getNextClassName"(arg0: string, arg1: string): string
 "toBeRenamed"(arg0: $IIdentifierRenamer$Type$Type, arg1: string, arg2: string, arg3: string): boolean
 "getNextMethodName"(arg0: string, arg1: string, arg2: string): string
 "getNextFieldName"(arg0: string, arg1: string, arg2: string): string
}

export namespace $IIdentifierRenamer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIdentifierRenamer$Type = ($IIdentifierRenamer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIdentifierRenamer_ = $IIdentifierRenamer$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/code/$ExceptionTable" {
import {$ExceptionHandler, $ExceptionHandler$Type} from "packages/org/jetbrains/java/decompiler/code/$ExceptionHandler"
import {$List, $List$Type} from "packages/java/util/$List"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ExceptionTable {
static readonly "EMPTY": $ExceptionTable

constructor(arg0: $List$Type<($ExceptionHandler$Type)>)

public "getHandlers"(): $List<($ExceptionHandler)>
get "handlers"(): $List<($ExceptionHandler)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExceptionTable$Type = ($ExceptionTable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExceptionTable_ = $ExceptionTable$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$BasicBlockStatement" {
import {$TextBuffer, $TextBuffer$Type} from "packages/org/jetbrains/java/decompiler/util/$TextBuffer"
import {$Exprent, $Exprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$Exprent"
import {$Statement$StatementType, $Statement$StatementType$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$Statement$StatementType"
import {$VarExprent, $VarExprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$VarExprent"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BasicBlock, $BasicBlock$Type} from "packages/org/jetbrains/java/decompiler/code/cfg/$BasicBlock"
import {$StartEndPair, $StartEndPair$Type} from "packages/org/jetbrains/java/decompiler/util/$StartEndPair"
import {$Statement, $Statement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$Statement"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $BasicBlockStatement extends $Statement {
static readonly "STATEDGE_ALL": integer
static readonly "STATEDGE_DIRECT_ALL": integer
readonly "type": $Statement$StatementType
readonly "id": integer

constructor(arg0: $BasicBlock$Type)

public "getSimpleCopy"(): $Statement
public "getStartEndRange"(): $StartEndPair
public "getBasichead"(): $BasicBlockStatement
public "hasBasicSuccEdge"(): boolean
public "markMonitorexitDead"(): void
public "getImplicitlyDefinedVars"(): $List<($VarExprent)>
public "setRemovableMonitorexit"(arg0: boolean): void
public "isRemovableMonitorexit"(): boolean
public static "create"(): $BasicBlockStatement
public "getBlock"(): $BasicBlock
public "toJava"(arg0: integer): $TextBuffer
public "replaceExprent"(arg0: $Exprent$Type, arg1: $Exprent$Type): void
get "simpleCopy"(): $Statement
get "startEndRange"(): $StartEndPair
get "basichead"(): $BasicBlockStatement
get "implicitlyDefinedVars"(): $List<($VarExprent)>
set "removableMonitorexit"(value: boolean)
get "removableMonitorexit"(): boolean
get "block"(): $BasicBlock
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BasicBlockStatement$Type = ($BasicBlockStatement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BasicBlockStatement_ = $BasicBlockStatement$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnDefaultAttribute" {
import {$StructMethodParametersAttribute, $StructMethodParametersAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute"
import {$StructNestHostAttribute, $StructNestHostAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructNestHostAttribute"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$Key, $Key$Type} from "packages/org/jetbrains/java/decompiler/util/$Key"
import {$StructAnnotationParameterAttribute, $StructAnnotationParameterAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationParameterAttribute"
import {$StructPermittedSubclassesAttribute, $StructPermittedSubclassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructPermittedSubclassesAttribute"
import {$StructLineNumberTableAttribute, $StructLineNumberTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLineNumberTableAttribute"
import {$StructCodeAttribute, $StructCodeAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructCodeAttribute"
import {$StructEnclosingMethodAttribute, $StructEnclosingMethodAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructEnclosingMethodAttribute"
import {$StructSourceFileAttribute, $StructSourceFileAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructSourceFileAttribute"
import {$Exprent, $Exprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$Exprent"
import {$StructBootstrapMethodsAttribute, $StructBootstrapMethodsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructBootstrapMethodsAttribute"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$StructGenericSignatureAttribute, $StructGenericSignatureAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGenericSignatureAttribute"
import {$StructLocalVariableTableAttribute, $StructLocalVariableTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute"
import {$StructTypeAnnotationAttribute, $StructTypeAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructTypeAnnotationAttribute"
import {$StructConstantValueAttribute, $StructConstantValueAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructConstantValueAttribute"
import {$StructAnnotationAttribute, $StructAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationAttribute"
import {$StructRecordAttribute, $StructRecordAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructRecordAttribute"
import {$StructModuleAttribute, $StructModuleAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute"
import {$StructGeneralAttribute, $StructGeneralAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGeneralAttribute"
import {$StructInnerClassesAttribute, $StructInnerClassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute"
import {$StructLocalVariableTypeTableAttribute, $StructLocalVariableTypeTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTypeTableAttribute"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructExceptionsAttribute, $StructExceptionsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructExceptionsAttribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructAnnDefaultAttribute extends $StructGeneralAttribute {
static readonly "ATTRIBUTE_CODE": $Key<($StructCodeAttribute)>
static readonly "ATTRIBUTE_INNER_CLASSES": $Key<($StructInnerClassesAttribute)>
static readonly "ATTRIBUTE_SIGNATURE": $Key<($StructGenericSignatureAttribute)>
static readonly "ATTRIBUTE_ANNOTATION_DEFAULT": $Key<($StructAnnDefaultAttribute)>
static readonly "ATTRIBUTE_EXCEPTIONS": $Key<($StructExceptionsAttribute)>
static readonly "ATTRIBUTE_ENCLOSING_METHOD": $Key<($StructEnclosingMethodAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TABLE": $Key<($StructLocalVariableTableAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TYPE_TABLE": $Key<($StructLocalVariableTypeTableAttribute)>
static readonly "ATTRIBUTE_CONSTANT_VALUE": $Key<($StructConstantValueAttribute)>
static readonly "ATTRIBUTE_BOOTSTRAP_METHODS": $Key<($StructBootstrapMethodsAttribute)>
static readonly "ATTRIBUTE_SYNTHETIC": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_DEPRECATED": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_LINE_NUMBER_TABLE": $Key<($StructLineNumberTableAttribute)>
static readonly "ATTRIBUTE_METHOD_PARAMETERS": $Key<($StructMethodParametersAttribute)>
static readonly "ATTRIBUTE_MODULE": $Key<($StructModuleAttribute)>
static readonly "ATTRIBUTE_RECORD": $Key<($StructRecordAttribute)>
static readonly "ATTRIBUTE_PERMITTED_SUBCLASSES": $Key<($StructPermittedSubclassesAttribute)>
static readonly "ATTRIBUTE_SOURCE_FILE": $Key<($StructSourceFileAttribute)>
static readonly "ATTRIBUTE_NEST_HOST": $Key<($StructNestHostAttribute)>

constructor()

public "getDefaultValue"(): $Exprent
public "initContent"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): void
get "defaultValue"(): $Exprent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructAnnDefaultAttribute$Type = ($StructAnnDefaultAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructAnnDefaultAttribute_ = $StructAnnDefaultAttribute$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarProcessor" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$VarType, $VarType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$VarType"
import {$MethodDescriptor, $MethodDescriptor$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$MethodDescriptor"
import {$StructMethod, $StructMethod$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructMethod"
import {$RootStatement, $RootStatement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$RootStatement"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$StructLocalVariableTableAttribute$LocalVariable, $StructLocalVariableTableAttribute$LocalVariable$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute$LocalVariable"
import {$VarExprent, $VarExprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$VarExprent"
import {$List, $List$Type} from "packages/java/util/$List"
import {$VarTypeProcessor$FinalType, $VarTypeProcessor$FinalType$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarTypeProcessor$FinalType"
import {$VarNamesCollector, $VarNamesCollector$Type} from "packages/org/jetbrains/java/decompiler/main/collectors/$VarNamesCollector"
import {$VarVersionPair, $VarVersionPair$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarVersionPair"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Pair, $Pair$Type} from "packages/org/jetbrains/java/decompiler/util/$Pair"
import {$VarVersionsProcessor, $VarVersionsProcessor$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarVersionsProcessor"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $VarProcessor {
 "nestedProcessed": boolean

constructor(arg0: $StructMethod$Type, arg1: $MethodDescriptor$Type)

public "getSyntheticSemaphores"(): $Set<(integer)>
public "getVarOriginalIndex"(arg0: integer): integer
public "getVarFinal"(arg0: $VarVersionPair$Type): $VarTypeProcessor$FinalType
public "getParams"(): $List<($VarVersionPair)>
public "setVarName"(arg0: $VarVersionPair$Type, arg1: string): void
public "getVarName"(arg0: $VarVersionPair$Type): string
public "getVarType"(arg0: $VarVersionPair$Type): $VarType
public "setVarType"(arg0: $VarVersionPair$Type, arg1: $VarType$Type): void
public "getVarNamesCollector"(): $VarNamesCollector
public "getThisVars"(): $Map<($VarVersionPair), (string)>
public "rerunClashing"(arg0: $RootStatement$Type): void
public "getClashingName"(arg0: $VarVersionPair$Type): string
public "markParam"(arg0: $VarVersionPair$Type): void
public "refreshVarNames"(arg0: $VarNamesCollector$Type): void
public "setDebugVarNames"(arg0: $RootStatement$Type, arg1: $Map$Type<($VarVersionPair$Type), (string)>): void
public "setVarVersions"(arg0: $RootStatement$Type): void
public "setVarDefinitions"(arg0: $RootStatement$Type): void
public "setVarSource"(arg0: $VarVersionPair$Type, arg1: string, arg2: $VarVersionPair$Type): void
public "setClashingName"(arg0: $VarVersionPair$Type, arg1: string): void
public "setInheritedName"(arg0: $VarVersionPair$Type, arg1: string): void
public "setVarLVT"(arg0: $VarVersionPair$Type, arg1: $StructLocalVariableTableAttribute$LocalVariable$Type): void
public "getUsedVarVersions"(): $Set<($VarVersionPair)>
public "getCandidates"(arg0: integer): $List<($StructLocalVariableTableAttribute$LocalVariable)>
public "getInheritedNames"(): $Map<($VarVersionPair), (string)>
public "findLVT"(arg0: $VarExprent$Type, arg1: integer): void
public "getVarNames"(): $Collection<(string)>
public "getVarSource"(arg0: $VarVersionPair$Type): $Pair<(string), ($VarVersionPair)>
public "getVarLVT"(arg0: $VarVersionPair$Type): $StructLocalVariableTableAttribute$LocalVariable
public "setVarFinal"(arg0: $VarVersionPair$Type, arg1: $VarTypeProcessor$FinalType$Type): void
public "getVarVersions"(): $VarVersionsProcessor
public "getExternalVars"(): $Set<($VarVersionPair)>
public "hasLVT"(): boolean
get "syntheticSemaphores"(): $Set<(integer)>
get "params"(): $List<($VarVersionPair)>
get "varNamesCollector"(): $VarNamesCollector
get "thisVars"(): $Map<($VarVersionPair), (string)>
set "varVersions"(value: $RootStatement$Type)
set "varDefinitions"(value: $RootStatement$Type)
get "usedVarVersions"(): $Set<($VarVersionPair)>
get "inheritedNames"(): $Map<($VarVersionPair), (string)>
get "varNames"(): $Collection<(string)>
get "varVersions"(): $VarVersionsProcessor
get "externalVars"(): $Set<($VarVersionPair)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VarProcessor$Type = ($VarProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VarProcessor_ = $VarProcessor$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructRecordAttribute" {
import {$StructMethodParametersAttribute, $StructMethodParametersAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute"
import {$StructNestHostAttribute, $StructNestHostAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructNestHostAttribute"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$Key, $Key$Type} from "packages/org/jetbrains/java/decompiler/util/$Key"
import {$StructAnnotationParameterAttribute, $StructAnnotationParameterAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationParameterAttribute"
import {$StructPermittedSubclassesAttribute, $StructPermittedSubclassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructPermittedSubclassesAttribute"
import {$StructLineNumberTableAttribute, $StructLineNumberTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLineNumberTableAttribute"
import {$StructCodeAttribute, $StructCodeAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructCodeAttribute"
import {$StructEnclosingMethodAttribute, $StructEnclosingMethodAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructEnclosingMethodAttribute"
import {$StructSourceFileAttribute, $StructSourceFileAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructSourceFileAttribute"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructBootstrapMethodsAttribute, $StructBootstrapMethodsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructBootstrapMethodsAttribute"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$StructGenericSignatureAttribute, $StructGenericSignatureAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGenericSignatureAttribute"
import {$StructLocalVariableTableAttribute, $StructLocalVariableTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute"
import {$StructTypeAnnotationAttribute, $StructTypeAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructTypeAnnotationAttribute"
import {$StructConstantValueAttribute, $StructConstantValueAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructConstantValueAttribute"
import {$StructAnnotationAttribute, $StructAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationAttribute"
import {$StructModuleAttribute, $StructModuleAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute"
import {$StructGeneralAttribute, $StructGeneralAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGeneralAttribute"
import {$StructInnerClassesAttribute, $StructInnerClassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute"
import {$StructLocalVariableTypeTableAttribute, $StructLocalVariableTypeTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTypeTableAttribute"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructRecordComponent, $StructRecordComponent$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructRecordComponent"
import {$StructExceptionsAttribute, $StructExceptionsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructExceptionsAttribute"
import {$StructAnnDefaultAttribute, $StructAnnDefaultAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnDefaultAttribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructRecordAttribute extends $StructGeneralAttribute {
static readonly "ATTRIBUTE_CODE": $Key<($StructCodeAttribute)>
static readonly "ATTRIBUTE_INNER_CLASSES": $Key<($StructInnerClassesAttribute)>
static readonly "ATTRIBUTE_SIGNATURE": $Key<($StructGenericSignatureAttribute)>
static readonly "ATTRIBUTE_ANNOTATION_DEFAULT": $Key<($StructAnnDefaultAttribute)>
static readonly "ATTRIBUTE_EXCEPTIONS": $Key<($StructExceptionsAttribute)>
static readonly "ATTRIBUTE_ENCLOSING_METHOD": $Key<($StructEnclosingMethodAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TABLE": $Key<($StructLocalVariableTableAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TYPE_TABLE": $Key<($StructLocalVariableTypeTableAttribute)>
static readonly "ATTRIBUTE_CONSTANT_VALUE": $Key<($StructConstantValueAttribute)>
static readonly "ATTRIBUTE_BOOTSTRAP_METHODS": $Key<($StructBootstrapMethodsAttribute)>
static readonly "ATTRIBUTE_SYNTHETIC": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_DEPRECATED": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_LINE_NUMBER_TABLE": $Key<($StructLineNumberTableAttribute)>
static readonly "ATTRIBUTE_METHOD_PARAMETERS": $Key<($StructMethodParametersAttribute)>
static readonly "ATTRIBUTE_MODULE": $Key<($StructModuleAttribute)>
static readonly "ATTRIBUTE_RECORD": $Key<($StructRecordAttribute)>
static readonly "ATTRIBUTE_PERMITTED_SUBCLASSES": $Key<($StructPermittedSubclassesAttribute)>
static readonly "ATTRIBUTE_SOURCE_FILE": $Key<($StructSourceFileAttribute)>
static readonly "ATTRIBUTE_NEST_HOST": $Key<($StructNestHostAttribute)>

constructor()

public "initContent"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): void
public "getComponents"(): $List<($StructRecordComponent)>
get "components"(): $List<($StructRecordComponent)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructRecordAttribute$Type = ($StructRecordAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructRecordAttribute_ = $StructRecordAttribute$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/extern/$IFernflowerLogger" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$IFernflowerLogger$Severity, $IFernflowerLogger$Severity$Type} from "packages/org/jetbrains/java/decompiler/main/extern/$IFernflowerLogger$Severity"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $IFernflowerLogger {
static readonly "NO_OP": $IFernflowerLogger

constructor()

public "writeMessage"(arg0: string, arg1: $IFernflowerLogger$Severity$Type, arg2: $Throwable$Type): void
public "writeMessage"(arg0: string, arg1: $IFernflowerLogger$Severity$Type): void
public "writeMessage"(arg0: string, arg1: $Throwable$Type): void
public "accepts"(arg0: $IFernflowerLogger$Severity$Type): boolean
public "startMethod"(arg0: string): void
public "setSeverity"(arg0: $IFernflowerLogger$Severity$Type): void
public "startReadingClass"(arg0: string): void
public "endReadingClass"(): void
public "startClass"(arg0: string): void
public "endClass"(): void
public "endProcessingClass"(): void
public "startWriteClass"(arg0: string): void
public "endWriteClass"(): void
public "endMethod"(): void
public "startProcessingClass"(arg0: string): void
set "severity"(value: $IFernflowerLogger$Severity$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IFernflowerLogger$Type = ($IFernflowerLogger);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IFernflowerLogger_ = $IFernflowerLogger$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$TypeAnnotation" {
import {$AnnotationExprent, $AnnotationExprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$AnnotationExprent"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $TypeAnnotation {
static readonly "CLASS_TYPE_PARAMETER": integer
static readonly "METHOD_TYPE_PARAMETER": integer
static readonly "SUPER_TYPE_REFERENCE": integer
static readonly "CLASS_TYPE_PARAMETER_BOUND": integer
static readonly "METHOD_TYPE_PARAMETER_BOUND": integer
static readonly "FIELD": integer
static readonly "METHOD_RETURN_TYPE": integer
static readonly "METHOD_RECEIVER": integer
static readonly "METHOD_PARAMETER": integer
static readonly "THROWS_REFERENCE": integer
static readonly "LOCAL_VARIABLE": integer
static readonly "RESOURCE_VARIABLE": integer
static readonly "CATCH_CLAUSE": integer
static readonly "EXPR_INSTANCEOF": integer
static readonly "EXPR_NEW": integer
static readonly "EXPR_CONSTRUCTOR_REF": integer
static readonly "EXPR_METHOD_REF": integer
static readonly "TYPE_ARG_CAST": integer
static readonly "TYPE_ARG_CONSTRUCTOR_CALL": integer
static readonly "TYPE_ARG_METHOD_CALL": integer
static readonly "TYPE_ARG_CONSTRUCTOR_REF": integer
static readonly "TYPE_ARG_METHOD_REF": integer

constructor(arg0: integer, arg1: (byte)[], arg2: $AnnotationExprent$Type)

public "getAnnotation"(): $AnnotationExprent
public "getIndex"(): integer
public "getTargetType"(): integer
public "isTopLevel"(): boolean
get "annotation"(): $AnnotationExprent
get "index"(): integer
get "targetType"(): integer
get "topLevel"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeAnnotation$Type = ($TypeAnnotation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeAnnotation_ = $TypeAnnotation$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/$StructMember" {
import {$StructGeneralAttribute, $StructGeneralAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGeneralAttribute"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$Key, $Key$Type} from "packages/org/jetbrains/java/decompiler/util/$Key"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructMember {


public "getAccessFlags"(): integer
public "isSynthetic"(): boolean
public static "readAttributes"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: boolean, arg3: $BytecodeVersion$Type): $Map<(string), ($StructGeneralAttribute)>
public static "readAttributes"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): $Map<(string), ($StructGeneralAttribute)>
public "getAttribute"<T extends $StructGeneralAttribute>(arg0: $Key$Type<(T)>): T
public "hasAttribute"(arg0: $Key$Type<(any)>): boolean
public "hasModifier"(arg0: integer): boolean
get "accessFlags"(): integer
get "synthetic"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructMember$Type = ($StructMember);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructMember_ = $StructMember$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarVersionsProcessor" {
import {$RootStatement, $RootStatement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$RootStatement"
import {$VarType, $VarType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$VarType"
import {$VarTypeProcessor, $VarTypeProcessor$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarTypeProcessor"
import {$MethodDescriptor, $MethodDescriptor$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$MethodDescriptor"
import {$VarTypeProcessor$FinalType, $VarTypeProcessor$FinalType$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarTypeProcessor$FinalType"
import {$StructMethod, $StructMethod$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructMethod"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$VarVersionPair, $VarVersionPair$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarVersionPair"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $VarVersionsProcessor {

constructor(arg0: $StructMethod$Type, arg1: $MethodDescriptor$Type)

public "getMapOriginalVarIndices"(): $Map<(integer), ($VarVersionPair)>
public "getVarFinal"(arg0: $VarVersionPair$Type): $VarTypeProcessor$FinalType
public "getVarType"(arg0: $VarVersionPair$Type): $VarType
public "setVarType"(arg0: $VarVersionPair$Type, arg1: $VarType$Type): void
public "getTypeProcessor"(): $VarTypeProcessor
public "setVarVersions"(arg0: $RootStatement$Type, arg1: $VarVersionsProcessor$Type): void
public "setVarFinal"(arg0: $VarVersionPair$Type, arg1: $VarTypeProcessor$FinalType$Type): void
get "mapOriginalVarIndices"(): $Map<(integer), ($VarVersionPair)>
get "typeProcessor"(): $VarTypeProcessor
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VarVersionsProcessor$Type = ($VarVersionsProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VarVersionsProcessor_ = $VarVersionsProcessor$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/rels/$DecompileRecord" {
import {$RootStatement, $RootStatement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$RootStatement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructMethod, $StructMethod$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructMethod"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $DecompileRecord {

constructor(arg0: $StructMethod$Type)

public "add"(arg0: string): void
public "add"(arg0: string, arg1: $RootStatement$Type): void
public "print"(): void
public "resetMergeLoop"(): void
public "resetMainLoop"(): void
public "getNames"(): $List<(string)>
public "incrementMainLoop"(): void
public "incrementMergeLoop"(): void
get "names"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DecompileRecord$Type = ($DecompileRecord);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DecompileRecord_ = $DecompileRecord$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/collectors/$ImportCollector$Lock" {
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ImportCollector$Lock implements $AutoCloseable {


public "close"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImportCollector$Lock$Type = ($ImportCollector$Lock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImportCollector$Lock_ = $ImportCollector$Lock$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$Exprent$Type" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Exprent$Type extends $Enum<($Exprent$Type)> {
static readonly "ANNOTATION": $Exprent$Type
static readonly "ARRAY": $Exprent$Type
static readonly "ASSERT": $Exprent$Type
static readonly "ASSIGNMENT": $Exprent$Type
static readonly "CONST": $Exprent$Type
static readonly "EXIT": $Exprent$Type
static readonly "FIELD": $Exprent$Type
static readonly "FUNCTION": $Exprent$Type
static readonly "IF": $Exprent$Type
static readonly "INVOCATION": $Exprent$Type
static readonly "MONITOR": $Exprent$Type
static readonly "NEW": $Exprent$Type
static readonly "SWITCH": $Exprent$Type
static readonly "SWITCH_HEAD": $Exprent$Type
static readonly "VAR": $Exprent$Type
static readonly "YIELD": $Exprent$Type
static readonly "OTHER": $Exprent$Type


public static "values"(): ($Exprent$Type)[]
public static "valueOf"(arg0: string): $Exprent$Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Exprent$Type$Type = (("annotation") | ("new") | ("other") | ("const") | ("assignment") | ("var") | ("monitor") | ("switch_head") | ("switch") | ("exit") | ("invocation") | ("field") | ("array") | ("assert") | ("function") | ("yield") | ("if")) | ($Exprent$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Exprent$Type_ = $Exprent$Type$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/util/$StartEndPair" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StartEndPair {
readonly "start": integer
readonly "end": integer

constructor(arg0: integer, arg1: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "join"(...arg0: ($StartEndPair$Type)[]): $StartEndPair
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StartEndPair$Type = ($StartEndPair);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StartEndPair_ = $StartEndPair$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/code/$Instruction" {
import {$InstructionSequence, $InstructionSequence$Type} from "packages/org/jetbrains/java/decompiler/code/$InstructionSequence"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$CodeConstants, $CodeConstants$Type} from "packages/org/jetbrains/java/decompiler/code/$CodeConstants"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Instruction implements $CodeConstants {
readonly "opcode": integer
readonly "group": integer
readonly "wide": boolean
readonly "bytecodeVersion": $BytecodeVersion
readonly "length": integer

constructor(arg0: integer, arg1: integer, arg2: boolean, arg3: $BytecodeVersion$Type, arg4: (integer)[], arg5: integer)

public static "equals"(arg0: $Instruction$Type, arg1: $Instruction$Type): boolean
public "toString"(): string
public "clone"(): $Instruction
public static "create"(arg0: integer, arg1: boolean, arg2: integer, arg3: $BytecodeVersion$Type, arg4: (integer)[], arg5: integer): $Instruction
public "operand"(arg0: integer): integer
public "operandsCount"(): integer
public "canFallThrough"(): boolean
public "initInstruction"(arg0: $InstructionSequence$Type): void
public static "isSignaturePolymorphic"(arg0: string, arg1: string, arg2: boolean): boolean
public static "isReturnPolymorphic"(arg0: string, arg1: string): boolean
public static "areParametersPolymorphic"(arg0: string, arg1: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Instruction$Type = ($Instruction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Instruction_ = $Instruction$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationAttribute" {
import {$StructMethodParametersAttribute, $StructMethodParametersAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute"
import {$StructNestHostAttribute, $StructNestHostAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructNestHostAttribute"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$Key, $Key$Type} from "packages/org/jetbrains/java/decompiler/util/$Key"
import {$StructAnnotationParameterAttribute, $StructAnnotationParameterAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationParameterAttribute"
import {$StructPermittedSubclassesAttribute, $StructPermittedSubclassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructPermittedSubclassesAttribute"
import {$StructLineNumberTableAttribute, $StructLineNumberTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLineNumberTableAttribute"
import {$StructCodeAttribute, $StructCodeAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructCodeAttribute"
import {$StructEnclosingMethodAttribute, $StructEnclosingMethodAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructEnclosingMethodAttribute"
import {$StructSourceFileAttribute, $StructSourceFileAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructSourceFileAttribute"
import {$Exprent, $Exprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$Exprent"
import {$DataInputStream, $DataInputStream$Type} from "packages/java/io/$DataInputStream"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructBootstrapMethodsAttribute, $StructBootstrapMethodsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructBootstrapMethodsAttribute"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$AnnotationExprent, $AnnotationExprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$AnnotationExprent"
import {$StructGenericSignatureAttribute, $StructGenericSignatureAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGenericSignatureAttribute"
import {$StructLocalVariableTableAttribute, $StructLocalVariableTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute"
import {$StructTypeAnnotationAttribute, $StructTypeAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructTypeAnnotationAttribute"
import {$StructConstantValueAttribute, $StructConstantValueAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructConstantValueAttribute"
import {$StructRecordAttribute, $StructRecordAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructRecordAttribute"
import {$StructModuleAttribute, $StructModuleAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute"
import {$StructGeneralAttribute, $StructGeneralAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGeneralAttribute"
import {$StructInnerClassesAttribute, $StructInnerClassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute"
import {$StructLocalVariableTypeTableAttribute, $StructLocalVariableTypeTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTypeTableAttribute"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructExceptionsAttribute, $StructExceptionsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructExceptionsAttribute"
import {$StructAnnDefaultAttribute, $StructAnnDefaultAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnDefaultAttribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructAnnotationAttribute extends $StructGeneralAttribute {
static readonly "ATTRIBUTE_CODE": $Key<($StructCodeAttribute)>
static readonly "ATTRIBUTE_INNER_CLASSES": $Key<($StructInnerClassesAttribute)>
static readonly "ATTRIBUTE_SIGNATURE": $Key<($StructGenericSignatureAttribute)>
static readonly "ATTRIBUTE_ANNOTATION_DEFAULT": $Key<($StructAnnDefaultAttribute)>
static readonly "ATTRIBUTE_EXCEPTIONS": $Key<($StructExceptionsAttribute)>
static readonly "ATTRIBUTE_ENCLOSING_METHOD": $Key<($StructEnclosingMethodAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TABLE": $Key<($StructLocalVariableTableAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TYPE_TABLE": $Key<($StructLocalVariableTypeTableAttribute)>
static readonly "ATTRIBUTE_CONSTANT_VALUE": $Key<($StructConstantValueAttribute)>
static readonly "ATTRIBUTE_BOOTSTRAP_METHODS": $Key<($StructBootstrapMethodsAttribute)>
static readonly "ATTRIBUTE_SYNTHETIC": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_DEPRECATED": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_LINE_NUMBER_TABLE": $Key<($StructLineNumberTableAttribute)>
static readonly "ATTRIBUTE_METHOD_PARAMETERS": $Key<($StructMethodParametersAttribute)>
static readonly "ATTRIBUTE_MODULE": $Key<($StructModuleAttribute)>
static readonly "ATTRIBUTE_RECORD": $Key<($StructRecordAttribute)>
static readonly "ATTRIBUTE_PERMITTED_SUBCLASSES": $Key<($StructPermittedSubclassesAttribute)>
static readonly "ATTRIBUTE_SOURCE_FILE": $Key<($StructSourceFileAttribute)>
static readonly "ATTRIBUTE_NEST_HOST": $Key<($StructNestHostAttribute)>

constructor()

public static "parseAnnotations"(arg0: $ConstantPool$Type, arg1: $DataInputStream$Type): $List<($AnnotationExprent)>
public "getAnnotations"(): $List<($AnnotationExprent)>
public static "parseAnnotation"(arg0: $DataInputStream$Type, arg1: $ConstantPool$Type): $AnnotationExprent
public "initContent"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): void
public static "parseAnnotationElement"(arg0: $DataInputStream$Type, arg1: $ConstantPool$Type): $Exprent
get "annotations"(): $List<($AnnotationExprent)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructAnnotationAttribute$Type = ($StructAnnotationAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructAnnotationAttribute_ = $StructAnnotationAttribute$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/code/$InstructionSequence" {
import {$Instruction, $Instruction$Type} from "packages/org/jetbrains/java/decompiler/code/$Instruction"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ExceptionTable, $ExceptionTable$Type} from "packages/org/jetbrains/java/decompiler/code/$ExceptionTable"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $InstructionSequence implements $Iterable<($Instruction)> {


public "getInstr"(arg0: integer): $Instruction
public "length"(): integer
public "toString"(arg0: integer): string
public "toString"(): string
public "clear"(): void
public "isEmpty"(): boolean
public "iterator"(): $Iterator<($Instruction)>
public "getOffset"(arg0: integer): integer
public "removeLast"(): void
public "addInstruction"(arg0: integer, arg1: $Instruction$Type, arg2: integer): void
public "addInstruction"(arg0: $Instruction$Type, arg1: integer): void
public "setPointer"(arg0: integer): void
public "getPointer"(): integer
public "getExceptionTable"(): $ExceptionTable
public "addSequence"(arg0: $InstructionSequence$Type): void
public "removeInstruction"(arg0: integer): void
public "removeInstruction"(arg0: $Instruction$Type): void
public "addToPointer"(arg0: integer): void
public "getLastInstr"(): $Instruction
public "getPointerByRelOffset"(arg0: integer): integer
public "getPointerByAbsOffset"(arg0: integer): integer
public "spliterator"(): $Spliterator<($Instruction)>
public "forEach"(arg0: $Consumer$Type<(any)>): void
[Symbol.iterator](): IterableIterator<$Instruction>;
get "empty"(): boolean
set "pointer"(value: integer)
get "pointer"(): integer
get "exceptionTable"(): $ExceptionTable
get "lastInstr"(): $Instruction
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InstructionSequence$Type = ($InstructionSequence);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InstructionSequence_ = $InstructionSequence$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/$StructField" {
import {$GenericFieldDescriptor, $GenericFieldDescriptor$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/generics/$GenericFieldDescriptor"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$StructMember, $StructMember$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructMember"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructField extends $StructMember {


public "getName"(): string
public "toString"(): string
public "getDescriptor"(): string
public "getSignature"(): $GenericFieldDescriptor
public static "create"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: string, arg3: $BytecodeVersion$Type): $StructField
get "name"(): string
get "descriptor"(): string
get "signature"(): $GenericFieldDescriptor
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructField$Type = ($StructField);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructField_ = $StructField$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute$Entry" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructMethodParametersAttribute$Entry {
readonly "myName": string
readonly "myAccessFlags": integer

constructor(arg0: string, arg1: integer)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructMethodParametersAttribute$Entry$Type = ($StructMethodParametersAttribute$Entry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructMethodParametersAttribute$Entry_ = $StructMethodParametersAttribute$Entry$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/extern/$IContextSource$Entries" {
import {$IContextSource, $IContextSource$Type} from "packages/org/jetbrains/java/decompiler/main/extern/$IContextSource"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IContextSource$Entry, $IContextSource$Entry$Type} from "packages/org/jetbrains/java/decompiler/main/extern/$IContextSource$Entry"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $IContextSource$Entries {
static readonly "EMPTY": $IContextSource$Entries

constructor(arg0: $List$Type<($IContextSource$Entry$Type)>, arg1: $List$Type<(string)>, arg2: $List$Type<($IContextSource$Entry$Type)>)
constructor(arg0: $List$Type<($IContextSource$Entry$Type)>, arg1: $List$Type<(string)>, arg2: $List$Type<($IContextSource$Entry$Type)>, arg3: $List$Type<($IContextSource$Type)>)

public "directories"(): $List<(string)>
public "childContexts"(): $List<($IContextSource)>
public "classes"(): $List<($IContextSource$Entry)>
public "others"(): $List<($IContextSource$Entry)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IContextSource$Entries$Type = ($IContextSource$Entries);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IContextSource$Entries_ = $IContextSource$Entries$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/gen/generics/$GenericFieldDescriptor" {
import {$VarType, $VarType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$VarType"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $GenericFieldDescriptor {
readonly "type": $VarType

constructor(arg0: $VarType$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenericFieldDescriptor$Type = ($GenericFieldDescriptor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenericFieldDescriptor_ = $GenericFieldDescriptor$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$Statement" {
import {$Statement$EdgeDirection, $Statement$EdgeDirection$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$Statement$EdgeDirection"
import {$BasicBlockStatement, $BasicBlockStatement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$BasicBlockStatement"
import {$HashSet, $HashSet$Type} from "packages/java/util/$HashSet"
import {$BitSet, $BitSet$Type} from "packages/java/util/$BitSet"
import {$RootStatement, $RootStatement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$RootStatement"
import {$IMatchable, $IMatchable$Type} from "packages/org/jetbrains/java/decompiler/struct/match/$IMatchable"
import {$MatchEngine, $MatchEngine$Type} from "packages/org/jetbrains/java/decompiler/struct/match/$MatchEngine"
import {$TextBuffer, $TextBuffer$Type} from "packages/org/jetbrains/java/decompiler/util/$TextBuffer"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Exprent, $Exprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$Exprent"
import {$Statement$StatementType, $Statement$StatementType$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$Statement$StatementType"
import {$VarExprent, $VarExprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$VarExprent"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Statement$LastBasicType, $Statement$LastBasicType$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$Statement$LastBasicType"
import {$StatEdge, $StatEdge$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/$StatEdge"
import {$VBStyleCollection, $VBStyleCollection$Type} from "packages/org/jetbrains/java/decompiler/util/collections/$VBStyleCollection"
import {$MatchNode, $MatchNode$Type} from "packages/org/jetbrains/java/decompiler/struct/match/$MatchNode"
import {$StartEndPair, $StartEndPair$Type} from "packages/org/jetbrains/java/decompiler/util/$StartEndPair"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Statement implements $IMatchable {
static readonly "STATEDGE_ALL": integer
static readonly "STATEDGE_DIRECT_ALL": integer
readonly "type": $Statement$StatementType
readonly "id": integer


public "buildMonitorFlags"(): void
public "buildContinueSet"(): $HashSet<($Statement)>
public "addLabeledEdge"(arg0: $StatEdge$Type): void
public "setAllParent"(): void
/**
 * 
 * @deprecated
 */
public "addEdgeInternal"(arg0: $Statement$EdgeDirection$Type, arg1: $StatEdge$Type): void
/**
 * 
 * @deprecated
 */
public "removeEdgeInternal"(arg0: $Statement$EdgeDirection$Type, arg1: $StatEdge$Type): void
public "getSimpleCopy"(): $Statement
public "initExprents"(): void
public "initSimpleCopy"(): void
public "containsStatement"(arg0: $Statement$Type): boolean
public "changeEdgeNode"(arg0: $Statement$EdgeDirection$Type, arg1: $StatEdge$Type, arg2: $Statement$Type): void
public "getNeighbours"(arg0: integer, arg1: $Statement$EdgeDirection$Type): $List<($Statement)>
public "getSuccessorEdges"(arg0: integer): $List<($StatEdge)>
public "getNeighboursSet"(arg0: integer, arg1: $Statement$EdgeDirection$Type): $Set<($Statement)>
public "changeEdgeType"(arg0: $Statement$EdgeDirection$Type, arg1: $StatEdge$Type, arg2: integer): void
public "replaceStatement"(arg0: $Statement$Type, arg1: $Statement$Type): void
public "getTopParent"(): $RootStatement
public "setExprents"(arg0: $List$Type<($Exprent$Type)>): void
public "isPhantom"(): boolean
public "getPost"(): $Statement
public "getLastBasicType"(): $Statement$LastBasicType
public "setPhantom"(arg0: boolean): void
public "getStartEndRange"(): $StartEndPair
public "getVarDefinitions"(): $List<($Exprent)>
public "getFirstSuccessor"(): $StatEdge
public "getBasichead"(): $BasicBlockStatement
public "isCopied"(): boolean
public "hasBasicSuccEdge"(): boolean
public "getLabelEdges"(): $HashSet<($StatEdge)>
public "setCopied"(arg0: boolean): void
public "getContinueSet"(): $HashSet<($Statement)>
public "hasAnySuccessor"(): boolean
public "isLabeled"(): boolean
public "hasSuccessor"(arg0: integer): boolean
public "markMonitorexitDead"(): void
public "getPostReversePostOrderList"(): $List<($Statement)>
public "getPostReversePostOrderList"(arg0: $List$Type<($Statement$Type)>): $List<($Statement)>
public "containsStatementStrict"(arg0: $Statement$Type): boolean
public "getImplicitlyDefinedVars"(): $List<($VarExprent)>
public "collapseNodesToStatement"(arg0: $Statement$Type): void
public "getSequentialObjects"(): $List<(any)>
public "getSuccessorEdgeView"(arg0: integer): $List<($StatEdge)>
public "clearTempInformation"(): void
public "removeAllSuccessors"(arg0: $Statement$Type): void
public "containsMonitorExit"(): boolean
public "getReversePostOrderList"(arg0: $Statement$Type): $List<($Statement)>
public "getReversePostOrderList"(): $List<($Statement)>
public "getPredecessorEdges"(arg0: integer): $List<($StatEdge)>
public "getAllDirectSuccessorEdges"(): $List<($StatEdge)>
public "getFirstDirectSuccessor"(): $StatEdge
public "hasAnyDirectSuccessor"(): boolean
public "containsMonitorExitOrAthrow"(): boolean
public "getAllSuccessorEdges"(): $List<($StatEdge)>
public "toString"(): string
public "match"(arg0: $MatchNode$Type, arg1: $MatchEngine$Type): boolean
public "getParent"(): $Statement
public "setParent"(arg0: $Statement$Type): void
public "getOffset"(arg0: $BitSet$Type): void
public "replaceWith"(arg0: $Statement$Type): void
public "getFirst"(): $Statement
public "isMonitorEnter"(): boolean
public "getStats"(): $VBStyleCollection<($Statement), (integer)>
public "getExprents"(): $List<($Exprent)>
public "toJava"(arg0: integer): $TextBuffer
public "toJava"(): $TextBuffer
public "replaceExprent"(arg0: $Exprent$Type, arg1: $Exprent$Type): void
public "findObject"(arg0: $MatchNode$Type, arg1: integer): $IMatchable
public "replaceWithEmpty"(): $BasicBlockStatement
public "addSuccessor"(arg0: $StatEdge$Type): void
public "removePredecessor"(arg0: $StatEdge$Type): void
public "removeSuccessor"(arg0: $StatEdge$Type): void
public "addPredecessor"(arg0: $StatEdge$Type): void
public "setFirst"(arg0: $Statement$Type): void
public "getAllPredecessorEdges"(): $List<($StatEdge)>
get "simpleCopy"(): $Statement
get "topParent"(): $RootStatement
set "exprents"(value: $List$Type<($Exprent$Type)>)
get "phantom"(): boolean
get "post"(): $Statement
get "lastBasicType"(): $Statement$LastBasicType
set "phantom"(value: boolean)
get "startEndRange"(): $StartEndPair
get "varDefinitions"(): $List<($Exprent)>
get "firstSuccessor"(): $StatEdge
get "basichead"(): $BasicBlockStatement
get "copied"(): boolean
get "labelEdges"(): $HashSet<($StatEdge)>
set "copied"(value: boolean)
get "continueSet"(): $HashSet<($Statement)>
get "labeled"(): boolean
get "postReversePostOrderList"(): $List<($Statement)>
get "implicitlyDefinedVars"(): $List<($VarExprent)>
get "sequentialObjects"(): $List<(any)>
get "reversePostOrderList"(): $List<($Statement)>
get "allDirectSuccessorEdges"(): $List<($StatEdge)>
get "firstDirectSuccessor"(): $StatEdge
get "allSuccessorEdges"(): $List<($StatEdge)>
get "parent"(): $Statement
set "parent"(value: $Statement$Type)
get "first"(): $Statement
get "monitorEnter"(): boolean
get "stats"(): $VBStyleCollection<($Statement), (integer)>
get "exprents"(): $List<($Exprent)>
set "first"(value: $Statement$Type)
get "allPredecessorEdges"(): $List<($StatEdge)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Statement$Type = ($Statement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Statement_ = $Statement$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/util/token/$TextRange" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $TextRange {
readonly "start": integer
readonly "length": integer

constructor(arg0: integer, arg1: integer)

public "getEnd"(): integer
get "end"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextRange$Type = ($TextRange);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextRange_ = $TextRange$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/api/plugin/pass/$PassContext" {
import {$DecompileRecord, $DecompileRecord$Type} from "packages/org/jetbrains/java/decompiler/main/rels/$DecompileRecord"
import {$RootStatement, $RootStatement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$RootStatement"
import {$ControlFlowGraph, $ControlFlowGraph$Type} from "packages/org/jetbrains/java/decompiler/code/cfg/$ControlFlowGraph"
import {$StructClass, $StructClass$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructClass"
import {$MethodDescriptor, $MethodDescriptor$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$MethodDescriptor"
import {$StructMethod, $StructMethod$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructMethod"
import {$VarProcessor, $VarProcessor$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarProcessor"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $PassContext {

constructor(arg0: $RootStatement$Type, arg1: $ControlFlowGraph$Type, arg2: $StructMethod$Type, arg3: $StructClass$Type, arg4: $VarProcessor$Type, arg5: $DecompileRecord$Type)

public "getEnclosingClass"(): $StructClass
public "getMethod"(): $StructMethod
public "getRoot"(): $RootStatement
public "getMethodDescriptor"(): $MethodDescriptor
public "getGraph"(): $ControlFlowGraph
public "setRoot"(arg0: $RootStatement$Type): void
public "getVarProc"(): $VarProcessor
public "getRec"(): $DecompileRecord
get "enclosingClass"(): $StructClass
get "method"(): $StructMethod
get "root"(): $RootStatement
get "methodDescriptor"(): $MethodDescriptor
get "graph"(): $ControlFlowGraph
set "root"(value: $RootStatement$Type)
get "varProc"(): $VarProcessor
get "rec"(): $DecompileRecord
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PassContext$Type = ($PassContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PassContext_ = $PassContext$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$InvocationExprent" {
import {$SFormsConstructor, $SFormsConstructor$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/sforms/$SFormsConstructor"
import {$VarType, $VarType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$VarType"
import {$MethodDescriptor, $MethodDescriptor$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$MethodDescriptor"
import {$Exprent$Type, $Exprent$Type$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$Exprent$Type"
import {$BitSet, $BitSet$Type} from "packages/java/util/$BitSet"
import {$StructMethod, $StructMethod$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructMethod"
import {$VarMapHolder, $VarMapHolder$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/sforms/$VarMapHolder"
import {$PooledConstant, $PooledConstant$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$PooledConstant"
import {$InvocationExprent$InvocationType, $InvocationExprent$InvocationType$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$InvocationExprent$InvocationType"
import {$Statement, $Statement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$Statement"
import {$ListStack, $ListStack$Type} from "packages/org/jetbrains/java/decompiler/util/collections/$ListStack"
import {$CheckTypesResult, $CheckTypesResult$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$CheckTypesResult"
import {$MatchEngine, $MatchEngine$Type} from "packages/org/jetbrains/java/decompiler/struct/match/$MatchEngine"
import {$InvocationExprent$Type, $InvocationExprent$Type$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$InvocationExprent$Type"
import {$TextBuffer, $TextBuffer$Type} from "packages/org/jetbrains/java/decompiler/util/$TextBuffer"
import {$Exprent, $Exprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$Exprent"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MatchNode, $MatchNode$Type} from "packages/org/jetbrains/java/decompiler/struct/match/$MatchNode"
import {$LinkConstant, $LinkConstant$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$LinkConstant"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $InvocationExprent extends $Exprent {
 "forceGenericQualfication": boolean
static readonly "MULTIPLE_USES": integer
static readonly "SIDE_EFFECTS_FREE": integer
static readonly "BOTH_FLAGS": integer
readonly "type": $Exprent$Type
readonly "id": integer
 "bytecode": $BitSet

constructor(arg0: integer, arg1: $LinkConstant$Type, arg2: $LinkConstant$Type, arg3: $List$Type<($PooledConstant$Type)>, arg4: $ListStack$Type<(any)>, arg5: $BitSet$Type)
constructor()

public "getName"(): string
public "equals"(arg0: any): boolean
public "getDescriptor"(): $MethodDescriptor
public "isStatic"(): boolean
public "getInstance"(): $Exprent
public "match"(arg0: $MatchNode$Type, arg1: $MatchEngine$Type): boolean
public "setName"(arg0: string): void
public "copy"(): $Exprent
public "getInvocationType"(): $InvocationExprent$InvocationType
public "getBootstrapMethod"(): $LinkConstant
public "getDesc"(): $StructMethod
public "getLstParameters"(): $List<($Exprent)>
public "appendParamList"(arg0: integer): $TextBuffer
public "getExprType"(): $VarType
public "toJava"(arg0: integer): $TextBuffer
public "getAllExprents"(arg0: $List$Type<($Exprent$Type)>): $List<($Exprent)>
public "getGenericArgs"(): $List<($VarType)>
public "getBytecodeRange"(arg0: $BitSet$Type): void
public "replaceExprent"(arg0: $Exprent$Type, arg1: $Exprent$Type): void
public "processSforms"(arg0: $SFormsConstructor$Type, arg1: $VarMapHolder$Type, arg2: $Statement$Type, arg3: boolean): void
public "setIsQualifier"(): void
public "getClassname"(): string
public "isSyntheticNullCheck"(): boolean
public "getStringDescriptor"(): string
public "setStringDescriptor"(arg0: string): void
public "markUsingBoxingResult"(): void
public "shouldForceUnboxing"(): boolean
public "getInvokeDynamicClassSuffix"(): string
public "setSyntheticNullCheck"(): void
public "setInstance"(arg0: $Exprent$Type): void
public "isBoxingCall"(): boolean
public "shouldForceBoxing"(): boolean
public "forceUnboxing"(arg0: boolean): void
public "isUnboxingCall"(): boolean
public "forceBoxing"(arg0: boolean): void
public "setStatic"(arg0: boolean): void
public "getGenericsMap"(): $Map<($VarType), ($VarType)>
public "setLstParameters"(arg0: $List$Type<($Exprent$Type)>): void
public "setFunctype"(arg0: $InvocationExprent$Type$Type): void
public "markWasLazyCondy"(): $InvocationExprent
public "setClassname"(arg0: string): void
public "getFunctype"(): $InvocationExprent$Type
public "setDescriptor"(arg0: $MethodDescriptor$Type): void
public "getInferredExprType"(arg0: $VarType$Type): $VarType
public "checkExprTypeBounds"(): $CheckTypesResult
public "setInvocationInstance"(): void
public "getBootstrapArguments"(): $List<($PooledConstant)>
get "name"(): string
get "descriptor"(): $MethodDescriptor
get "static"(): boolean
get "instance"(): $Exprent
set "name"(value: string)
get "invocationType"(): $InvocationExprent$InvocationType
get "bootstrapMethod"(): $LinkConstant
get "desc"(): $StructMethod
get "lstParameters"(): $List<($Exprent)>
get "exprType"(): $VarType
get "genericArgs"(): $List<($VarType)>
get "classname"(): string
get "syntheticNullCheck"(): boolean
get "stringDescriptor"(): string
set "stringDescriptor"(value: string)
get "invokeDynamicClassSuffix"(): string
set "instance"(value: $Exprent$Type)
get "boxingCall"(): boolean
get "unboxingCall"(): boolean
set "static"(value: boolean)
get "genericsMap"(): $Map<($VarType), ($VarType)>
set "lstParameters"(value: $List$Type<($Exprent$Type)>)
set "functype"(value: $InvocationExprent$Type$Type)
set "classname"(value: string)
get "functype"(): $InvocationExprent$Type
set "descriptor"(value: $MethodDescriptor$Type)
get "bootstrapArguments"(): $List<($PooledConstant)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InvocationExprent$Type = ($InvocationExprent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InvocationExprent_ = $InvocationExprent$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructConstantValueAttribute" {
import {$StructMethodParametersAttribute, $StructMethodParametersAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute"
import {$StructNestHostAttribute, $StructNestHostAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructNestHostAttribute"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$Key, $Key$Type} from "packages/org/jetbrains/java/decompiler/util/$Key"
import {$StructAnnotationParameterAttribute, $StructAnnotationParameterAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationParameterAttribute"
import {$StructPermittedSubclassesAttribute, $StructPermittedSubclassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructPermittedSubclassesAttribute"
import {$StructLineNumberTableAttribute, $StructLineNumberTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLineNumberTableAttribute"
import {$StructCodeAttribute, $StructCodeAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructCodeAttribute"
import {$StructEnclosingMethodAttribute, $StructEnclosingMethodAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructEnclosingMethodAttribute"
import {$StructSourceFileAttribute, $StructSourceFileAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructSourceFileAttribute"
import {$StructBootstrapMethodsAttribute, $StructBootstrapMethodsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructBootstrapMethodsAttribute"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$StructGenericSignatureAttribute, $StructGenericSignatureAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGenericSignatureAttribute"
import {$StructLocalVariableTableAttribute, $StructLocalVariableTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute"
import {$StructTypeAnnotationAttribute, $StructTypeAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructTypeAnnotationAttribute"
import {$StructAnnotationAttribute, $StructAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationAttribute"
import {$StructRecordAttribute, $StructRecordAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructRecordAttribute"
import {$StructModuleAttribute, $StructModuleAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute"
import {$StructGeneralAttribute, $StructGeneralAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGeneralAttribute"
import {$StructInnerClassesAttribute, $StructInnerClassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute"
import {$StructLocalVariableTypeTableAttribute, $StructLocalVariableTypeTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTypeTableAttribute"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructExceptionsAttribute, $StructExceptionsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructExceptionsAttribute"
import {$StructAnnDefaultAttribute, $StructAnnDefaultAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnDefaultAttribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructConstantValueAttribute extends $StructGeneralAttribute {
static readonly "ATTRIBUTE_CODE": $Key<($StructCodeAttribute)>
static readonly "ATTRIBUTE_INNER_CLASSES": $Key<($StructInnerClassesAttribute)>
static readonly "ATTRIBUTE_SIGNATURE": $Key<($StructGenericSignatureAttribute)>
static readonly "ATTRIBUTE_ANNOTATION_DEFAULT": $Key<($StructAnnDefaultAttribute)>
static readonly "ATTRIBUTE_EXCEPTIONS": $Key<($StructExceptionsAttribute)>
static readonly "ATTRIBUTE_ENCLOSING_METHOD": $Key<($StructEnclosingMethodAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TABLE": $Key<($StructLocalVariableTableAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TYPE_TABLE": $Key<($StructLocalVariableTypeTableAttribute)>
static readonly "ATTRIBUTE_CONSTANT_VALUE": $Key<($StructConstantValueAttribute)>
static readonly "ATTRIBUTE_BOOTSTRAP_METHODS": $Key<($StructBootstrapMethodsAttribute)>
static readonly "ATTRIBUTE_SYNTHETIC": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_DEPRECATED": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_LINE_NUMBER_TABLE": $Key<($StructLineNumberTableAttribute)>
static readonly "ATTRIBUTE_METHOD_PARAMETERS": $Key<($StructMethodParametersAttribute)>
static readonly "ATTRIBUTE_MODULE": $Key<($StructModuleAttribute)>
static readonly "ATTRIBUTE_RECORD": $Key<($StructRecordAttribute)>
static readonly "ATTRIBUTE_PERMITTED_SUBCLASSES": $Key<($StructPermittedSubclassesAttribute)>
static readonly "ATTRIBUTE_SOURCE_FILE": $Key<($StructSourceFileAttribute)>
static readonly "ATTRIBUTE_NEST_HOST": $Key<($StructNestHostAttribute)>

constructor()

public "getIndex"(): integer
public "initContent"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): void
get "index"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructConstantValueAttribute$Type = ($StructConstantValueAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructConstantValueAttribute_ = $StructConstantValueAttribute$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/gen/$FieldDescriptor" {
import {$NewClassNameBuilder, $NewClassNameBuilder$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$NewClassNameBuilder"
import {$VarType, $VarType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$VarType"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $FieldDescriptor {
static readonly "INTEGER_DESCRIPTOR": $FieldDescriptor
static readonly "LONG_DESCRIPTOR": $FieldDescriptor
static readonly "FLOAT_DESCRIPTOR": $FieldDescriptor
static readonly "DOUBLE_DESCRIPTOR": $FieldDescriptor
readonly "type": $VarType
readonly "descriptorString": string


public "buildNewDescriptor"(arg0: $NewClassNameBuilder$Type): string
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "parseDescriptor"(arg0: string): $FieldDescriptor
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FieldDescriptor$Type = ($FieldDescriptor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FieldDescriptor_ = $FieldDescriptor$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarVersionNode$State" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $VarVersionNode$State extends $Enum<($VarVersionNode$State)> {
static readonly "WRITE": $VarVersionNode$State
static readonly "READ": $VarVersionNode$State
static readonly "PHI": $VarVersionNode$State
static readonly "DEAD_READ": $VarVersionNode$State
static readonly "PHANTOM": $VarVersionNode$State
static readonly "PARAM": $VarVersionNode$State
static readonly "CATCH": $VarVersionNode$State


public static "values"(): ($VarVersionNode$State)[]
public static "valueOf"(arg0: string): $VarVersionNode$State
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VarVersionNode$State$Type = (("phi") | ("phantom") | ("dead_read") | ("read") | ("param") | ("catch") | ("write")) | ($VarVersionNode$State);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VarVersionNode$State_ = $VarVersionNode$State$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructGenericSignatureAttribute" {
import {$StructMethodParametersAttribute, $StructMethodParametersAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute"
import {$StructNestHostAttribute, $StructNestHostAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructNestHostAttribute"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$Key, $Key$Type} from "packages/org/jetbrains/java/decompiler/util/$Key"
import {$StructAnnotationParameterAttribute, $StructAnnotationParameterAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationParameterAttribute"
import {$StructPermittedSubclassesAttribute, $StructPermittedSubclassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructPermittedSubclassesAttribute"
import {$StructLineNumberTableAttribute, $StructLineNumberTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLineNumberTableAttribute"
import {$StructCodeAttribute, $StructCodeAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructCodeAttribute"
import {$StructEnclosingMethodAttribute, $StructEnclosingMethodAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructEnclosingMethodAttribute"
import {$StructSourceFileAttribute, $StructSourceFileAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructSourceFileAttribute"
import {$StructBootstrapMethodsAttribute, $StructBootstrapMethodsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructBootstrapMethodsAttribute"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$StructLocalVariableTableAttribute, $StructLocalVariableTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute"
import {$StructTypeAnnotationAttribute, $StructTypeAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructTypeAnnotationAttribute"
import {$StructConstantValueAttribute, $StructConstantValueAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructConstantValueAttribute"
import {$StructAnnotationAttribute, $StructAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationAttribute"
import {$StructRecordAttribute, $StructRecordAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructRecordAttribute"
import {$StructModuleAttribute, $StructModuleAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute"
import {$StructGeneralAttribute, $StructGeneralAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGeneralAttribute"
import {$StructInnerClassesAttribute, $StructInnerClassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute"
import {$StructLocalVariableTypeTableAttribute, $StructLocalVariableTypeTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTypeTableAttribute"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructExceptionsAttribute, $StructExceptionsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructExceptionsAttribute"
import {$StructAnnDefaultAttribute, $StructAnnDefaultAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnDefaultAttribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructGenericSignatureAttribute extends $StructGeneralAttribute {
static readonly "ATTRIBUTE_CODE": $Key<($StructCodeAttribute)>
static readonly "ATTRIBUTE_INNER_CLASSES": $Key<($StructInnerClassesAttribute)>
static readonly "ATTRIBUTE_SIGNATURE": $Key<($StructGenericSignatureAttribute)>
static readonly "ATTRIBUTE_ANNOTATION_DEFAULT": $Key<($StructAnnDefaultAttribute)>
static readonly "ATTRIBUTE_EXCEPTIONS": $Key<($StructExceptionsAttribute)>
static readonly "ATTRIBUTE_ENCLOSING_METHOD": $Key<($StructEnclosingMethodAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TABLE": $Key<($StructLocalVariableTableAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TYPE_TABLE": $Key<($StructLocalVariableTypeTableAttribute)>
static readonly "ATTRIBUTE_CONSTANT_VALUE": $Key<($StructConstantValueAttribute)>
static readonly "ATTRIBUTE_BOOTSTRAP_METHODS": $Key<($StructBootstrapMethodsAttribute)>
static readonly "ATTRIBUTE_SYNTHETIC": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_DEPRECATED": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_LINE_NUMBER_TABLE": $Key<($StructLineNumberTableAttribute)>
static readonly "ATTRIBUTE_METHOD_PARAMETERS": $Key<($StructMethodParametersAttribute)>
static readonly "ATTRIBUTE_MODULE": $Key<($StructModuleAttribute)>
static readonly "ATTRIBUTE_RECORD": $Key<($StructRecordAttribute)>
static readonly "ATTRIBUTE_PERMITTED_SUBCLASSES": $Key<($StructPermittedSubclassesAttribute)>
static readonly "ATTRIBUTE_SOURCE_FILE": $Key<($StructSourceFileAttribute)>
static readonly "ATTRIBUTE_NEST_HOST": $Key<($StructNestHostAttribute)>

constructor()

public "getSignature"(): string
public "initContent"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): void
get "signature"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructGenericSignatureAttribute$Type = ($StructGenericSignatureAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructGenericSignatureAttribute_ = $StructGenericSignatureAttribute$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/extern/$IContextSource" {
import {$IContextSource$IOutputSink, $IContextSource$IOutputSink$Type} from "packages/org/jetbrains/java/decompiler/main/extern/$IContextSource$IOutputSink"
import {$IResultSaver, $IResultSaver$Type} from "packages/org/jetbrains/java/decompiler/main/extern/$IResultSaver"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$IContextSource$Entries, $IContextSource$Entries$Type} from "packages/org/jetbrains/java/decompiler/main/extern/$IContextSource$Entries"
import {$IContextSource$Entry, $IContextSource$Entry$Type} from "packages/org/jetbrains/java/decompiler/main/extern/$IContextSource$Entry"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IContextSource {

 "createOutputSink"(arg0: $IResultSaver$Type): $IContextSource$IOutputSink
 "getName"(): string
 "getInputStream"(arg0: $IContextSource$Entry$Type): $InputStream
 "getInputStream"(arg0: string): $InputStream
 "getEntries"(): $IContextSource$Entries
 "getClassBytes"(arg0: string): (byte)[]
 "hasClass"(arg0: string): boolean
 "isLazy"(): boolean
}

export namespace $IContextSource {
const CLASS_SUFFIX: string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IContextSource$Type = ($IContextSource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IContextSource_ = $IContextSource$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$DummyExitStatement" {
import {$Statement$StatementType, $Statement$StatementType$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$Statement$StatementType"
import {$BitSet, $BitSet$Type} from "packages/java/util/$BitSet"
import {$Statement, $Statement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$Statement"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $DummyExitStatement extends $Statement {
 "bytecode": $BitSet
static readonly "STATEDGE_ALL": integer
static readonly "STATEDGE_DIRECT_ALL": integer
readonly "type": $Statement$StatementType
readonly "id": integer

constructor()

public "addBytecodeOffsets"(arg0: $BitSet$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DummyExitStatement$Type = ($DummyExitStatement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DummyExitStatement_ = $DummyExitStatement$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/api/plugin/pass/$Pass" {
import {$PassContext, $PassContext$Type} from "packages/org/jetbrains/java/decompiler/api/plugin/pass/$PassContext"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $Pass {

 "run"(arg0: $PassContext$Type): boolean

(arg0: $PassContext$Type): boolean
}

export namespace $Pass {
const NO_OP: $Pass
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Pass$Type = ($Pass);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Pass_ = $Pass$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/match/$IMatchable$MatchProperties" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $IMatchable$MatchProperties extends $Enum<($IMatchable$MatchProperties)> {
static readonly "STATEMENT_TYPE": $IMatchable$MatchProperties
static readonly "STATEMENT_RET": $IMatchable$MatchProperties
static readonly "STATEMENT_STATSIZE": $IMatchable$MatchProperties
static readonly "STATEMENT_EXPRSIZE": $IMatchable$MatchProperties
static readonly "STATEMENT_POSITION": $IMatchable$MatchProperties
static readonly "STATEMENT_IFTYPE": $IMatchable$MatchProperties
static readonly "EXPRENT_TYPE": $IMatchable$MatchProperties
static readonly "EXPRENT_RET": $IMatchable$MatchProperties
static readonly "EXPRENT_POSITION": $IMatchable$MatchProperties
static readonly "EXPRENT_FUNCTYPE": $IMatchable$MatchProperties
static readonly "EXPRENT_EXITTYPE": $IMatchable$MatchProperties
static readonly "EXPRENT_CONSTTYPE": $IMatchable$MatchProperties
static readonly "EXPRENT_CONSTVALUE": $IMatchable$MatchProperties
static readonly "EXPRENT_INVOCATION_CLASS": $IMatchable$MatchProperties
static readonly "EXPRENT_INVOCATION_SIGNATURE": $IMatchable$MatchProperties
static readonly "EXPRENT_PARAMETER": $IMatchable$MatchProperties
static readonly "EXPRENT_VAR_INDEX": $IMatchable$MatchProperties
static readonly "EXPRENT_NAME": $IMatchable$MatchProperties


public static "values"(): ($IMatchable$MatchProperties)[]
public static "valueOf"(arg0: string): $IMatchable$MatchProperties
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMatchable$MatchProperties$Type = (("exprent_ret") | ("statement_exprsize") | ("exprent_constvalue") | ("exprent_invocation_class") | ("statement_iftype") | ("exprent_parameter") | ("statement_position") | ("exprent_type") | ("statement_ret") | ("exprent_position") | ("exprent_invocation_signature") | ("exprent_exittype") | ("exprent_consttype") | ("statement_statsize") | ("exprent_functype") | ("exprent_var_index") | ("exprent_name") | ("statement_type")) | ($IMatchable$MatchProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMatchable$MatchProperties_ = $IMatchable$MatchProperties$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute" {
import {$StructNestHostAttribute, $StructNestHostAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructNestHostAttribute"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$Key, $Key$Type} from "packages/org/jetbrains/java/decompiler/util/$Key"
import {$StructAnnotationParameterAttribute, $StructAnnotationParameterAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationParameterAttribute"
import {$StructPermittedSubclassesAttribute, $StructPermittedSubclassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructPermittedSubclassesAttribute"
import {$StructLineNumberTableAttribute, $StructLineNumberTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLineNumberTableAttribute"
import {$StructCodeAttribute, $StructCodeAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructCodeAttribute"
import {$StructEnclosingMethodAttribute, $StructEnclosingMethodAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructEnclosingMethodAttribute"
import {$StructSourceFileAttribute, $StructSourceFileAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructSourceFileAttribute"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructBootstrapMethodsAttribute, $StructBootstrapMethodsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructBootstrapMethodsAttribute"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$StructGenericSignatureAttribute, $StructGenericSignatureAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGenericSignatureAttribute"
import {$StructLocalVariableTableAttribute, $StructLocalVariableTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute"
import {$StructTypeAnnotationAttribute, $StructTypeAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructTypeAnnotationAttribute"
import {$StructConstantValueAttribute, $StructConstantValueAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructConstantValueAttribute"
import {$StructAnnotationAttribute, $StructAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationAttribute"
import {$StructMethodParametersAttribute$Entry, $StructMethodParametersAttribute$Entry$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute$Entry"
import {$StructRecordAttribute, $StructRecordAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructRecordAttribute"
import {$StructModuleAttribute, $StructModuleAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute"
import {$StructGeneralAttribute, $StructGeneralAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGeneralAttribute"
import {$StructInnerClassesAttribute, $StructInnerClassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute"
import {$StructLocalVariableTypeTableAttribute, $StructLocalVariableTypeTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTypeTableAttribute"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructExceptionsAttribute, $StructExceptionsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructExceptionsAttribute"
import {$StructAnnDefaultAttribute, $StructAnnDefaultAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnDefaultAttribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructMethodParametersAttribute extends $StructGeneralAttribute {
static readonly "ATTRIBUTE_CODE": $Key<($StructCodeAttribute)>
static readonly "ATTRIBUTE_INNER_CLASSES": $Key<($StructInnerClassesAttribute)>
static readonly "ATTRIBUTE_SIGNATURE": $Key<($StructGenericSignatureAttribute)>
static readonly "ATTRIBUTE_ANNOTATION_DEFAULT": $Key<($StructAnnDefaultAttribute)>
static readonly "ATTRIBUTE_EXCEPTIONS": $Key<($StructExceptionsAttribute)>
static readonly "ATTRIBUTE_ENCLOSING_METHOD": $Key<($StructEnclosingMethodAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TABLE": $Key<($StructLocalVariableTableAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TYPE_TABLE": $Key<($StructLocalVariableTypeTableAttribute)>
static readonly "ATTRIBUTE_CONSTANT_VALUE": $Key<($StructConstantValueAttribute)>
static readonly "ATTRIBUTE_BOOTSTRAP_METHODS": $Key<($StructBootstrapMethodsAttribute)>
static readonly "ATTRIBUTE_SYNTHETIC": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_DEPRECATED": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_LINE_NUMBER_TABLE": $Key<($StructLineNumberTableAttribute)>
static readonly "ATTRIBUTE_METHOD_PARAMETERS": $Key<($StructMethodParametersAttribute)>
static readonly "ATTRIBUTE_MODULE": $Key<($StructModuleAttribute)>
static readonly "ATTRIBUTE_RECORD": $Key<($StructRecordAttribute)>
static readonly "ATTRIBUTE_PERMITTED_SUBCLASSES": $Key<($StructPermittedSubclassesAttribute)>
static readonly "ATTRIBUTE_SOURCE_FILE": $Key<($StructSourceFileAttribute)>
static readonly "ATTRIBUTE_NEST_HOST": $Key<($StructNestHostAttribute)>

constructor()

public "getEntries"(): $List<($StructMethodParametersAttribute$Entry)>
public "initContent"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): void
get "entries"(): $List<($StructMethodParametersAttribute$Entry)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructMethodParametersAttribute$Type = ($StructMethodParametersAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructMethodParametersAttribute_ = $StructMethodParametersAttribute$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/extern/$TextTokenVisitor" {
import {$FieldDescriptor, $FieldDescriptor$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$FieldDescriptor"
import {$TextRange, $TextRange$Type} from "packages/org/jetbrains/java/decompiler/util/token/$TextRange"
import {$MethodDescriptor, $MethodDescriptor$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$MethodDescriptor"
import {$TextTokenVisitor$Factory, $TextTokenVisitor$Factory$Type} from "packages/org/jetbrains/java/decompiler/main/extern/$TextTokenVisitor$Factory"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $TextTokenVisitor {
static readonly "EMPTY": $TextTokenVisitor

constructor(arg0: $TextTokenVisitor$Type)

public "start"(arg0: string): void
public "end"(): void
public "visitField"(arg0: $TextRange$Type, arg1: boolean, arg2: string, arg3: string, arg4: $FieldDescriptor$Type): void
public "visitMethod"(arg0: $TextRange$Type, arg1: boolean, arg2: string, arg3: string, arg4: $MethodDescriptor$Type): void
public "visitParameter"(arg0: $TextRange$Type, arg1: boolean, arg2: string, arg3: string, arg4: $MethodDescriptor$Type, arg5: integer, arg6: string): void
public static "createVisitor"(arg0: $TextTokenVisitor$Factory$Type): $TextTokenVisitor
public static "createVisitor"(): $TextTokenVisitor
public "visitClass"(arg0: $TextRange$Type, arg1: boolean, arg2: string): void
public static "addVisitor"(arg0: $TextTokenVisitor$Factory$Type): void
public "visitLocal"(arg0: $TextRange$Type, arg1: boolean, arg2: string, arg3: string, arg4: $MethodDescriptor$Type, arg5: integer, arg6: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextTokenVisitor$Type = ($TextTokenVisitor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextTokenVisitor_ = $TextTokenVisitor$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/api/plugin/$StatementWriter" {
import {$ClassWrapper, $ClassWrapper$Type} from "packages/org/jetbrains/java/decompiler/main/rels/$ClassWrapper"
import {$ClassesProcessor$ClassNode, $ClassesProcessor$ClassNode$Type} from "packages/org/jetbrains/java/decompiler/main/$ClassesProcessor$ClassNode"
import {$StructClass, $StructClass$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructClass"
import {$TextBuffer, $TextBuffer$Type} from "packages/org/jetbrains/java/decompiler/util/$TextBuffer"
import {$StructField, $StructField$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructField"
import {$ImportCollector, $ImportCollector$Type} from "packages/org/jetbrains/java/decompiler/main/collectors/$ImportCollector"
import {$StructMethod, $StructMethod$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructMethod"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $StatementWriter {

 "writeClassHeader"(arg0: $StructClass$Type, arg1: $TextBuffer$Type, arg2: $ImportCollector$Type): void
 "writeMethod"(arg0: $ClassesProcessor$ClassNode$Type, arg1: $StructMethod$Type, arg2: integer, arg3: $TextBuffer$Type, arg4: integer): boolean
 "writeField"(arg0: $ClassWrapper$Type, arg1: $StructClass$Type, arg2: $StructField$Type, arg3: $TextBuffer$Type, arg4: integer): void
 "writeClass"(arg0: $ClassesProcessor$ClassNode$Type, arg1: $TextBuffer$Type, arg2: integer): void
}

export namespace $StatementWriter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StatementWriter$Type = ($StatementWriter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StatementWriter_ = $StatementWriter$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$InvocationExprent$InvocationType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $InvocationExprent$InvocationType extends $Enum<($InvocationExprent$InvocationType)> {
static readonly "SPECIAL": $InvocationExprent$InvocationType
static readonly "VIRTUAL": $InvocationExprent$InvocationType
static readonly "STATIC": $InvocationExprent$InvocationType
static readonly "INTERFACE": $InvocationExprent$InvocationType
static readonly "DYNAMIC": $InvocationExprent$InvocationType
static readonly "CONSTANT_DYNAMIC": $InvocationExprent$InvocationType


public static "values"(): ($InvocationExprent$InvocationType)[]
public static "valueOf"(arg0: string): $InvocationExprent$InvocationType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InvocationExprent$InvocationType$Type = (("special") | ("virtual") | ("static") | ("constant_dynamic") | ("dynamic") | ("interface")) | ($InvocationExprent$InvocationType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InvocationExprent$InvocationType_ = $InvocationExprent$InvocationType$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructTypeAnnotationAttribute" {
import {$StructMethodParametersAttribute, $StructMethodParametersAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute"
import {$StructNestHostAttribute, $StructNestHostAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructNestHostAttribute"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$Key, $Key$Type} from "packages/org/jetbrains/java/decompiler/util/$Key"
import {$StructAnnotationParameterAttribute, $StructAnnotationParameterAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationParameterAttribute"
import {$StructPermittedSubclassesAttribute, $StructPermittedSubclassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructPermittedSubclassesAttribute"
import {$StructLineNumberTableAttribute, $StructLineNumberTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLineNumberTableAttribute"
import {$StructCodeAttribute, $StructCodeAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructCodeAttribute"
import {$StructEnclosingMethodAttribute, $StructEnclosingMethodAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructEnclosingMethodAttribute"
import {$StructSourceFileAttribute, $StructSourceFileAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructSourceFileAttribute"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TypeAnnotation, $TypeAnnotation$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$TypeAnnotation"
import {$StructBootstrapMethodsAttribute, $StructBootstrapMethodsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructBootstrapMethodsAttribute"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$StructGenericSignatureAttribute, $StructGenericSignatureAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGenericSignatureAttribute"
import {$StructLocalVariableTableAttribute, $StructLocalVariableTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute"
import {$StructConstantValueAttribute, $StructConstantValueAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructConstantValueAttribute"
import {$StructAnnotationAttribute, $StructAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationAttribute"
import {$StructRecordAttribute, $StructRecordAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructRecordAttribute"
import {$StructModuleAttribute, $StructModuleAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute"
import {$StructGeneralAttribute, $StructGeneralAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGeneralAttribute"
import {$StructInnerClassesAttribute, $StructInnerClassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute"
import {$StructLocalVariableTypeTableAttribute, $StructLocalVariableTypeTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTypeTableAttribute"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructExceptionsAttribute, $StructExceptionsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructExceptionsAttribute"
import {$StructAnnDefaultAttribute, $StructAnnDefaultAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnDefaultAttribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructTypeAnnotationAttribute extends $StructGeneralAttribute {
static readonly "ATTRIBUTE_CODE": $Key<($StructCodeAttribute)>
static readonly "ATTRIBUTE_INNER_CLASSES": $Key<($StructInnerClassesAttribute)>
static readonly "ATTRIBUTE_SIGNATURE": $Key<($StructGenericSignatureAttribute)>
static readonly "ATTRIBUTE_ANNOTATION_DEFAULT": $Key<($StructAnnDefaultAttribute)>
static readonly "ATTRIBUTE_EXCEPTIONS": $Key<($StructExceptionsAttribute)>
static readonly "ATTRIBUTE_ENCLOSING_METHOD": $Key<($StructEnclosingMethodAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TABLE": $Key<($StructLocalVariableTableAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TYPE_TABLE": $Key<($StructLocalVariableTypeTableAttribute)>
static readonly "ATTRIBUTE_CONSTANT_VALUE": $Key<($StructConstantValueAttribute)>
static readonly "ATTRIBUTE_BOOTSTRAP_METHODS": $Key<($StructBootstrapMethodsAttribute)>
static readonly "ATTRIBUTE_SYNTHETIC": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_DEPRECATED": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_LINE_NUMBER_TABLE": $Key<($StructLineNumberTableAttribute)>
static readonly "ATTRIBUTE_METHOD_PARAMETERS": $Key<($StructMethodParametersAttribute)>
static readonly "ATTRIBUTE_MODULE": $Key<($StructModuleAttribute)>
static readonly "ATTRIBUTE_RECORD": $Key<($StructRecordAttribute)>
static readonly "ATTRIBUTE_PERMITTED_SUBCLASSES": $Key<($StructPermittedSubclassesAttribute)>
static readonly "ATTRIBUTE_SOURCE_FILE": $Key<($StructSourceFileAttribute)>
static readonly "ATTRIBUTE_NEST_HOST": $Key<($StructNestHostAttribute)>

constructor()

public "getAnnotations"(): $List<($TypeAnnotation)>
public "initContent"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): void
get "annotations"(): $List<($TypeAnnotation)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructTypeAnnotationAttribute$Type = ($StructTypeAnnotationAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructTypeAnnotationAttribute_ = $StructTypeAnnotationAttribute$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$Statement$EdgeDirection" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Statement$EdgeDirection extends $Enum<($Statement$EdgeDirection)> {
static readonly "BACKWARD": $Statement$EdgeDirection
static readonly "FORWARD": $Statement$EdgeDirection


public static "values"(): ($Statement$EdgeDirection)[]
public static "valueOf"(arg0: string): $Statement$EdgeDirection
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Statement$EdgeDirection$Type = (("forward") | ("backward")) | ($Statement$EdgeDirection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Statement$EdgeDirection_ = $Statement$EdgeDirection$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructEnclosingMethodAttribute" {
import {$StructMethodParametersAttribute, $StructMethodParametersAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute"
import {$StructNestHostAttribute, $StructNestHostAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructNestHostAttribute"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$Key, $Key$Type} from "packages/org/jetbrains/java/decompiler/util/$Key"
import {$StructAnnotationParameterAttribute, $StructAnnotationParameterAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationParameterAttribute"
import {$StructPermittedSubclassesAttribute, $StructPermittedSubclassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructPermittedSubclassesAttribute"
import {$StructLineNumberTableAttribute, $StructLineNumberTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLineNumberTableAttribute"
import {$StructCodeAttribute, $StructCodeAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructCodeAttribute"
import {$StructSourceFileAttribute, $StructSourceFileAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructSourceFileAttribute"
import {$StructBootstrapMethodsAttribute, $StructBootstrapMethodsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructBootstrapMethodsAttribute"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$StructGenericSignatureAttribute, $StructGenericSignatureAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGenericSignatureAttribute"
import {$StructLocalVariableTableAttribute, $StructLocalVariableTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute"
import {$StructTypeAnnotationAttribute, $StructTypeAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructTypeAnnotationAttribute"
import {$StructConstantValueAttribute, $StructConstantValueAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructConstantValueAttribute"
import {$StructAnnotationAttribute, $StructAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationAttribute"
import {$StructRecordAttribute, $StructRecordAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructRecordAttribute"
import {$StructModuleAttribute, $StructModuleAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute"
import {$StructGeneralAttribute, $StructGeneralAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGeneralAttribute"
import {$StructInnerClassesAttribute, $StructInnerClassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute"
import {$StructLocalVariableTypeTableAttribute, $StructLocalVariableTypeTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTypeTableAttribute"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructExceptionsAttribute, $StructExceptionsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructExceptionsAttribute"
import {$StructAnnDefaultAttribute, $StructAnnDefaultAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnDefaultAttribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructEnclosingMethodAttribute extends $StructGeneralAttribute {
static readonly "ATTRIBUTE_CODE": $Key<($StructCodeAttribute)>
static readonly "ATTRIBUTE_INNER_CLASSES": $Key<($StructInnerClassesAttribute)>
static readonly "ATTRIBUTE_SIGNATURE": $Key<($StructGenericSignatureAttribute)>
static readonly "ATTRIBUTE_ANNOTATION_DEFAULT": $Key<($StructAnnDefaultAttribute)>
static readonly "ATTRIBUTE_EXCEPTIONS": $Key<($StructExceptionsAttribute)>
static readonly "ATTRIBUTE_ENCLOSING_METHOD": $Key<($StructEnclosingMethodAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TABLE": $Key<($StructLocalVariableTableAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TYPE_TABLE": $Key<($StructLocalVariableTypeTableAttribute)>
static readonly "ATTRIBUTE_CONSTANT_VALUE": $Key<($StructConstantValueAttribute)>
static readonly "ATTRIBUTE_BOOTSTRAP_METHODS": $Key<($StructBootstrapMethodsAttribute)>
static readonly "ATTRIBUTE_SYNTHETIC": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_DEPRECATED": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_LINE_NUMBER_TABLE": $Key<($StructLineNumberTableAttribute)>
static readonly "ATTRIBUTE_METHOD_PARAMETERS": $Key<($StructMethodParametersAttribute)>
static readonly "ATTRIBUTE_MODULE": $Key<($StructModuleAttribute)>
static readonly "ATTRIBUTE_RECORD": $Key<($StructRecordAttribute)>
static readonly "ATTRIBUTE_PERMITTED_SUBCLASSES": $Key<($StructPermittedSubclassesAttribute)>
static readonly "ATTRIBUTE_SOURCE_FILE": $Key<($StructSourceFileAttribute)>
static readonly "ATTRIBUTE_NEST_HOST": $Key<($StructNestHostAttribute)>

constructor()

public "getMethodDescriptor"(): string
public "getClassName"(): string
public "getMethodName"(): string
public "initContent"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): void
get "methodDescriptor"(): string
get "className"(): string
get "methodName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructEnclosingMethodAttribute$Type = ($StructEnclosingMethodAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructEnclosingMethodAttribute_ = $StructEnclosingMethodAttribute$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructGeneralAttribute" {
import {$StructMethodParametersAttribute, $StructMethodParametersAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute"
import {$StructNestHostAttribute, $StructNestHostAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructNestHostAttribute"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$Key, $Key$Type} from "packages/org/jetbrains/java/decompiler/util/$Key"
import {$StructAnnotationParameterAttribute, $StructAnnotationParameterAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationParameterAttribute"
import {$StructPermittedSubclassesAttribute, $StructPermittedSubclassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructPermittedSubclassesAttribute"
import {$StructLineNumberTableAttribute, $StructLineNumberTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLineNumberTableAttribute"
import {$StructCodeAttribute, $StructCodeAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructCodeAttribute"
import {$StructEnclosingMethodAttribute, $StructEnclosingMethodAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructEnclosingMethodAttribute"
import {$StructSourceFileAttribute, $StructSourceFileAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructSourceFileAttribute"
import {$StructBootstrapMethodsAttribute, $StructBootstrapMethodsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructBootstrapMethodsAttribute"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$StructGenericSignatureAttribute, $StructGenericSignatureAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGenericSignatureAttribute"
import {$StructLocalVariableTableAttribute, $StructLocalVariableTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute"
import {$StructTypeAnnotationAttribute, $StructTypeAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructTypeAnnotationAttribute"
import {$StructConstantValueAttribute, $StructConstantValueAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructConstantValueAttribute"
import {$StructAnnotationAttribute, $StructAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationAttribute"
import {$StructRecordAttribute, $StructRecordAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructRecordAttribute"
import {$StructModuleAttribute, $StructModuleAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute"
import {$StructInnerClassesAttribute, $StructInnerClassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute"
import {$StructLocalVariableTypeTableAttribute, $StructLocalVariableTypeTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTypeTableAttribute"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructExceptionsAttribute, $StructExceptionsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructExceptionsAttribute"
import {$StructAnnDefaultAttribute, $StructAnnDefaultAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnDefaultAttribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructGeneralAttribute {
static readonly "ATTRIBUTE_CODE": $Key<($StructCodeAttribute)>
static readonly "ATTRIBUTE_INNER_CLASSES": $Key<($StructInnerClassesAttribute)>
static readonly "ATTRIBUTE_SIGNATURE": $Key<($StructGenericSignatureAttribute)>
static readonly "ATTRIBUTE_ANNOTATION_DEFAULT": $Key<($StructAnnDefaultAttribute)>
static readonly "ATTRIBUTE_EXCEPTIONS": $Key<($StructExceptionsAttribute)>
static readonly "ATTRIBUTE_ENCLOSING_METHOD": $Key<($StructEnclosingMethodAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TABLE": $Key<($StructLocalVariableTableAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TYPE_TABLE": $Key<($StructLocalVariableTypeTableAttribute)>
static readonly "ATTRIBUTE_CONSTANT_VALUE": $Key<($StructConstantValueAttribute)>
static readonly "ATTRIBUTE_BOOTSTRAP_METHODS": $Key<($StructBootstrapMethodsAttribute)>
static readonly "ATTRIBUTE_SYNTHETIC": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_DEPRECATED": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_LINE_NUMBER_TABLE": $Key<($StructLineNumberTableAttribute)>
static readonly "ATTRIBUTE_METHOD_PARAMETERS": $Key<($StructMethodParametersAttribute)>
static readonly "ATTRIBUTE_MODULE": $Key<($StructModuleAttribute)>
static readonly "ATTRIBUTE_RECORD": $Key<($StructRecordAttribute)>
static readonly "ATTRIBUTE_PERMITTED_SUBCLASSES": $Key<($StructPermittedSubclassesAttribute)>
static readonly "ATTRIBUTE_SOURCE_FILE": $Key<($StructSourceFileAttribute)>
static readonly "ATTRIBUTE_NEST_HOST": $Key<($StructNestHostAttribute)>

constructor()

public static "init"(): void
public static "createAttribute"(arg0: string): $StructGeneralAttribute
public "initContent"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructGeneralAttribute$Type = ($StructGeneralAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructGeneralAttribute_ = $StructGeneralAttribute$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/flow/$DirectGraph$ExprentIterator" {
import {$Exprent, $Exprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$Exprent"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $DirectGraph$ExprentIterator {

 "processExprent"(arg0: $Exprent$Type): integer

(arg0: $Exprent$Type): integer
}

export namespace $DirectGraph$ExprentIterator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DirectGraph$ExprentIterator$Type = ($DirectGraph$ExprentIterator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DirectGraph$ExprentIterator_ = $DirectGraph$ExprentIterator$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/extern/$IIdentifierRenamer$Type" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $IIdentifierRenamer$Type extends $Enum<($IIdentifierRenamer$Type)> {
static readonly "ELEMENT_CLASS": $IIdentifierRenamer$Type
static readonly "ELEMENT_FIELD": $IIdentifierRenamer$Type
static readonly "ELEMENT_METHOD": $IIdentifierRenamer$Type


public static "values"(): ($IIdentifierRenamer$Type)[]
public static "valueOf"(arg0: string): $IIdentifierRenamer$Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIdentifierRenamer$Type$Type = (("element_field") | ("element_method") | ("element_class")) | ($IIdentifierRenamer$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIdentifierRenamer$Type_ = $IIdentifierRenamer$Type$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructExceptionsAttribute" {
import {$StructMethodParametersAttribute, $StructMethodParametersAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute"
import {$StructNestHostAttribute, $StructNestHostAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructNestHostAttribute"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$Key, $Key$Type} from "packages/org/jetbrains/java/decompiler/util/$Key"
import {$StructAnnotationParameterAttribute, $StructAnnotationParameterAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationParameterAttribute"
import {$StructPermittedSubclassesAttribute, $StructPermittedSubclassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructPermittedSubclassesAttribute"
import {$StructLineNumberTableAttribute, $StructLineNumberTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLineNumberTableAttribute"
import {$StructCodeAttribute, $StructCodeAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructCodeAttribute"
import {$StructEnclosingMethodAttribute, $StructEnclosingMethodAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructEnclosingMethodAttribute"
import {$StructSourceFileAttribute, $StructSourceFileAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructSourceFileAttribute"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructBootstrapMethodsAttribute, $StructBootstrapMethodsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructBootstrapMethodsAttribute"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$StructGenericSignatureAttribute, $StructGenericSignatureAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGenericSignatureAttribute"
import {$StructLocalVariableTableAttribute, $StructLocalVariableTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute"
import {$StructTypeAnnotationAttribute, $StructTypeAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructTypeAnnotationAttribute"
import {$StructConstantValueAttribute, $StructConstantValueAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructConstantValueAttribute"
import {$StructAnnotationAttribute, $StructAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationAttribute"
import {$StructRecordAttribute, $StructRecordAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructRecordAttribute"
import {$StructModuleAttribute, $StructModuleAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute"
import {$StructGeneralAttribute, $StructGeneralAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGeneralAttribute"
import {$StructInnerClassesAttribute, $StructInnerClassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute"
import {$StructLocalVariableTypeTableAttribute, $StructLocalVariableTypeTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTypeTableAttribute"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructAnnDefaultAttribute, $StructAnnDefaultAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnDefaultAttribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructExceptionsAttribute extends $StructGeneralAttribute {
static readonly "ATTRIBUTE_CODE": $Key<($StructCodeAttribute)>
static readonly "ATTRIBUTE_INNER_CLASSES": $Key<($StructInnerClassesAttribute)>
static readonly "ATTRIBUTE_SIGNATURE": $Key<($StructGenericSignatureAttribute)>
static readonly "ATTRIBUTE_ANNOTATION_DEFAULT": $Key<($StructAnnDefaultAttribute)>
static readonly "ATTRIBUTE_EXCEPTIONS": $Key<($StructExceptionsAttribute)>
static readonly "ATTRIBUTE_ENCLOSING_METHOD": $Key<($StructEnclosingMethodAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TABLE": $Key<($StructLocalVariableTableAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TYPE_TABLE": $Key<($StructLocalVariableTypeTableAttribute)>
static readonly "ATTRIBUTE_CONSTANT_VALUE": $Key<($StructConstantValueAttribute)>
static readonly "ATTRIBUTE_BOOTSTRAP_METHODS": $Key<($StructBootstrapMethodsAttribute)>
static readonly "ATTRIBUTE_SYNTHETIC": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_DEPRECATED": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_LINE_NUMBER_TABLE": $Key<($StructLineNumberTableAttribute)>
static readonly "ATTRIBUTE_METHOD_PARAMETERS": $Key<($StructMethodParametersAttribute)>
static readonly "ATTRIBUTE_MODULE": $Key<($StructModuleAttribute)>
static readonly "ATTRIBUTE_RECORD": $Key<($StructRecordAttribute)>
static readonly "ATTRIBUTE_PERMITTED_SUBCLASSES": $Key<($StructPermittedSubclassesAttribute)>
static readonly "ATTRIBUTE_SOURCE_FILE": $Key<($StructSourceFileAttribute)>
static readonly "ATTRIBUTE_NEST_HOST": $Key<($StructNestHostAttribute)>

constructor()

public "initContent"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): void
public "getExcClassname"(arg0: integer, arg1: $ConstantPool$Type): string
public "getThrowsExceptions"(): $List<(integer)>
get "throwsExceptions"(): $List<(integer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructExceptionsAttribute$Type = ($StructExceptionsAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructExceptionsAttribute_ = $StructExceptionsAttribute$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/code/cfg/$BasicBlock" {
import {$Instruction, $Instruction$Type} from "packages/org/jetbrains/java/decompiler/code/$Instruction"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$InstructionSequence, $InstructionSequence$Type} from "packages/org/jetbrains/java/decompiler/code/$InstructionSequence"
import {$IGraphNode, $IGraphNode$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/decompose/$IGraphNode"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $BasicBlock implements $IGraphNode {
readonly "id": integer
 "mark": integer

constructor(arg0: integer)

public "addPredecessorException"(arg0: $BasicBlock$Type): void
public "getStartInstruction"(): integer
public "removePredecessorException"(arg0: $BasicBlock$Type): void
public "getSeq"(): $InstructionSequence
public "removeSuccessorException"(arg0: $BasicBlock$Type): void
public "addSuccessorException"(arg0: $BasicBlock$Type): void
public "toString"(arg0: integer): string
public "toString"(): string
public "size"(): integer
public "getId"(): integer
public "getPredecessors"(): $Collection<(any)>
public "getOldOffset"(arg0: integer): integer
public "setSeq"(arg0: $InstructionSequence$Type): void
public "getEndInstruction"(): integer
public "getInstruction"(arg0: integer): $Instruction
public "isSuccessor"(arg0: $BasicBlock$Type): boolean
public "addSuccessor"(arg0: $BasicBlock$Type): void
public "removePredecessor"(arg0: $BasicBlock$Type): void
public "removeSuccessor"(arg0: $BasicBlock$Type): void
public "addPredecessor"(arg0: $BasicBlock$Type): void
public "getPredExceptions"(): $List<($BasicBlock)>
public "getSuccs"(): $List<($BasicBlock)>
public "getSuccExceptions"(): $List<($BasicBlock)>
public "getPreds"(): $List<($BasicBlock)>
public "getInstrOldOffsets"(): $List<(integer)>
public "getLastInstruction"(): $Instruction
public "replaceSuccessor"(arg0: $BasicBlock$Type, arg1: $BasicBlock$Type): void
get "startInstruction"(): integer
get "seq"(): $InstructionSequence
get "id"(): integer
get "predecessors"(): $Collection<(any)>
set "seq"(value: $InstructionSequence$Type)
get "endInstruction"(): integer
get "predExceptions"(): $List<($BasicBlock)>
get "succs"(): $List<($BasicBlock)>
get "succExceptions"(): $List<($BasicBlock)>
get "preds"(): $List<($BasicBlock)>
get "instrOldOffsets"(): $List<(integer)>
get "lastInstruction"(): $Instruction
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BasicBlock$Type = ($BasicBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BasicBlock_ = $BasicBlock$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarTypeProcessor" {
import {$RootStatement, $RootStatement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$RootStatement"
import {$VarType, $VarType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$VarType"
import {$MethodDescriptor, $MethodDescriptor$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$MethodDescriptor"
import {$VarTypeProcessor$FinalType, $VarTypeProcessor$FinalType$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarTypeProcessor$FinalType"
import {$StructMethod, $StructMethod$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructMethod"
import {$DirectGraph, $DirectGraph$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/flow/$DirectGraph"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$VarVersionPair, $VarVersionPair$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarVersionPair"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $VarTypeProcessor {

constructor(arg0: $StructMethod$Type, arg1: $MethodDescriptor$Type)

public "getMapExprentMaxTypes"(): $Map<($VarVersionPair), ($VarType)>
public "getVarType"(arg0: $VarVersionPair$Type): $VarType
public "setVarType"(arg0: $VarVersionPair$Type, arg1: $VarType$Type): void
public "getMapFinalVars"(): $Map<($VarVersionPair), ($VarTypeProcessor$FinalType)>
public "calculateVarTypes"(arg0: $RootStatement$Type, arg1: $DirectGraph$Type): void
public "getMapExprentMinTypes"(): $Map<($VarVersionPair), ($VarType)>
get "mapExprentMaxTypes"(): $Map<($VarVersionPair), ($VarType)>
get "mapFinalVars"(): $Map<($VarVersionPair), ($VarTypeProcessor$FinalType)>
get "mapExprentMinTypes"(): $Map<($VarVersionPair), ($VarType)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VarTypeProcessor$Type = ($VarTypeProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VarTypeProcessor_ = $VarTypeProcessor$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/$IDecompiledData" {
import {$StructClass, $StructClass$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructClass"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IDecompiledData {

 "processClass"(arg0: $StructClass$Type): void
 "getClassContent"(arg0: $StructClass$Type): string
 "getClassEntryName"(arg0: $StructClass$Type, arg1: string): string
}

export namespace $IDecompiledData {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IDecompiledData$Type = ($IDecompiledData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IDecompiledData_ = $IDecompiledData$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/$StatEdge" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Statement, $Statement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$Statement"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StatEdge {
static readonly "TYPE_REGULAR": integer
static readonly "TYPE_EXCEPTION": integer
static readonly "TYPE_BREAK": integer
static readonly "TYPE_CONTINUE": integer
static readonly "TYPE_FINALLYEXIT": integer
static readonly "TYPES": (integer)[]
 "closure": $Statement
 "labeled": boolean
 "explicit": boolean
 "canInline": boolean
 "phantomContinue": boolean

constructor(arg0: integer, arg1: $Statement$Type, arg2: $Statement$Type, arg3: $Statement$Type)
constructor(arg0: integer, arg1: $Statement$Type, arg2: $Statement$Type)
constructor(arg0: $Statement$Type, arg1: $Statement$Type, arg2: $List$Type<(string)>)

public "changeClosure"(arg0: $Statement$Type): void
public "changeDestination"(arg0: $Statement$Type): void
public "changeSource"(arg0: $Statement$Type): void
public "removeClosure"(): void
public "remove"(): void
public "toString"(): string
public "getType"(): integer
public "getSource"(): $Statement
public "setType"(arg0: integer): void
public "setDestination"(arg0: $Statement$Type): void
public "setSource"(arg0: $Statement$Type): void
public "getDestination"(): $Statement
public "getExceptions"(): $List<(string)>
public "changeType"(arg0: integer): void
get "type"(): integer
get "source"(): $Statement
set "type"(value: integer)
set "destination"(value: $Statement$Type)
set "source"(value: $Statement$Type)
get "destination"(): $Statement
get "exceptions"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StatEdge$Type = ($StatEdge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StatEdge_ = $StatEdge$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/util/$Pair" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Pair<A, B> {
readonly "a": A
readonly "b": B


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "of"<A, B>(arg0: A, arg1: B): $Pair<(A), (B)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Pair$Type<A, B> = ($Pair<(A), (B)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Pair_<A, B> = $Pair$Type<(A), (B)>;
}}
declare module "packages/org/jetbrains/java/decompiler/main/$ClassesProcessor$ClassNode$LambdaInformation" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ClassesProcessor$ClassNode$LambdaInformation {
 "method_name": string
 "method_descriptor": string
 "content_class_name": string
 "content_method_name": string
 "content_method_descriptor": string
 "content_method_invocation_type": integer
 "content_method_key": string
 "is_method_reference": boolean
 "is_content_method_static": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassesProcessor$ClassNode$LambdaInformation$Type = ($ClassesProcessor$ClassNode$LambdaInformation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassesProcessor$ClassNode$LambdaInformation_ = $ClassesProcessor$ClassNode$LambdaInformation$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$FieldExprent" {
import {$SFormsConstructor, $SFormsConstructor$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/sforms/$SFormsConstructor"
import {$VarType, $VarType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$VarType"
import {$Exprent$Type, $Exprent$Type$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$Exprent$Type"
import {$BitSet, $BitSet$Type} from "packages/java/util/$BitSet"
import {$VarMapHolder, $VarMapHolder$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/sforms/$VarMapHolder"
import {$Statement, $Statement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$Statement"
import {$FieldDescriptor, $FieldDescriptor$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$FieldDescriptor"
import {$MatchEngine, $MatchEngine$Type} from "packages/org/jetbrains/java/decompiler/struct/match/$MatchEngine"
import {$TextBuffer, $TextBuffer$Type} from "packages/org/jetbrains/java/decompiler/util/$TextBuffer"
import {$Exprent, $Exprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$Exprent"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MatchNode, $MatchNode$Type} from "packages/org/jetbrains/java/decompiler/struct/match/$MatchNode"
import {$LinkConstant, $LinkConstant$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$LinkConstant"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $FieldExprent extends $Exprent {
static readonly "MULTIPLE_USES": integer
static readonly "SIDE_EFFECTS_FREE": integer
static readonly "BOTH_FLAGS": integer
readonly "type": $Exprent$Type
readonly "id": integer
 "bytecode": $BitSet

constructor(arg0: string, arg1: string, arg2: boolean, arg3: $Exprent$Type, arg4: $FieldDescriptor$Type, arg5: $BitSet$Type, arg6: boolean, arg7: boolean)
constructor(arg0: string, arg1: string, arg2: boolean, arg3: $Exprent$Type, arg4: $FieldDescriptor$Type, arg5: $BitSet$Type)
constructor(arg0: $LinkConstant$Type, arg1: $Exprent$Type, arg2: $BitSet$Type)

public "getName"(): string
public "equals"(arg0: any): boolean
public "getDescriptor"(): $FieldDescriptor
public "isStatic"(): boolean
public "getInstance"(): $Exprent
public "match"(arg0: $MatchNode$Type, arg1: $MatchEngine$Type): boolean
public "copy"(): $Exprent
public "getExprentUse"(): integer
public "getExprType"(): $VarType
public "toJava"(arg0: integer): $TextBuffer
public "getAllExprents"(arg0: $List$Type<($Exprent$Type)>): $List<($Exprent)>
public "getBytecodeRange"(arg0: $BitSet$Type): void
public "replaceExprent"(arg0: $Exprent$Type, arg1: $Exprent$Type): void
public "processSforms"(arg0: $SFormsConstructor$Type, arg1: $VarMapHolder$Type, arg2: $Statement$Type, arg3: boolean): void
public "setIsQualifier"(): void
public "forceQualified"(arg0: boolean): void
public "getClassname"(): string
public "getInferredExprType"(arg0: $VarType$Type): $VarType
public "allowNewlineAfterQualifier"(): boolean
get "name"(): string
get "descriptor"(): $FieldDescriptor
get "static"(): boolean
get "instance"(): $Exprent
get "exprentUse"(): integer
get "exprType"(): $VarType
get "classname"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FieldExprent$Type = ($FieldExprent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FieldExprent_ = $FieldExprent$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/extern/$IVariableNameProvider" {
import {$VarType, $VarType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$VarType"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$VarVersionPair, $VarVersionPair$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarVersionPair"
import {$Pair, $Pair$Type} from "packages/org/jetbrains/java/decompiler/util/$Pair"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IVariableNameProvider {

 "addParentContext"(arg0: $IVariableNameProvider$Type): void
 "rename"(arg0: $Map$Type<($VarVersionPair$Type), ($Pair$Type<($VarType$Type), (string)>)>): $Map<($VarVersionPair), (string)>
 "renameParameter"(arg0: integer, arg1: $VarType$Type, arg2: string, arg3: integer): string
 "renameAbstractParameter"(arg0: string, arg1: integer): string
}

export namespace $IVariableNameProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IVariableNameProvider$Type = ($IVariableNameProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IVariableNameProvider_ = $IVariableNameProvider$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/match/$MatchEngine" {
import {$IMatchable, $IMatchable$Type} from "packages/org/jetbrains/java/decompiler/struct/match/$IMatchable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MatchEngine {

constructor(...arg0: (string)[])
constructor(arg0: string)

public "match"(arg0: $IMatchable$Type): boolean
public "getVariableValue"(arg0: string): any
public "checkAndSetVariableValue"(arg0: string, arg1: any): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MatchEngine$Type = ($MatchEngine);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MatchEngine_ = $MatchEngine$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$CheckTypesResult$ExprentTypePair" {
import {$Exprent, $Exprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$Exprent"
import {$VarType, $VarType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$VarType"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $CheckTypesResult$ExprentTypePair {
readonly "exprent": $Exprent
readonly "type": $VarType

constructor(arg0: $Exprent$Type, arg1: $VarType$Type)

public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CheckTypesResult$ExprentTypePair$Type = ($CheckTypesResult$ExprentTypePair);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CheckTypesResult$ExprentTypePair_ = $CheckTypesResult$ExprentTypePair$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/flow/$DirectEdgeType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $DirectEdgeType extends $Enum<($DirectEdgeType)> {
static readonly "REGULAR": $DirectEdgeType
static readonly "EXCEPTION": $DirectEdgeType
static readonly "TYPES": ($DirectEdgeType)[]


public static "values"(): ($DirectEdgeType)[]
public static "valueOf"(arg0: string): $DirectEdgeType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DirectEdgeType$Type = (("exception") | ("regular")) | ($DirectEdgeType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DirectEdgeType_ = $DirectEdgeType$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/match/$MatchNode$RuleValue" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MatchNode$RuleValue {
readonly "parameter": integer
readonly "value": any

constructor(arg0: integer, arg1: any)

public "toString"(): string
public "isVariable"(): boolean
get "variable"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MatchNode$RuleValue$Type = ($MatchNode$RuleValue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MatchNode$RuleValue_ = $MatchNode$RuleValue$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/gen/$TypeFamily" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $TypeFamily extends $Enum<($TypeFamily)> {
static readonly "UNKNOWN": $TypeFamily
static readonly "BOOLEAN": $TypeFamily
static readonly "INTEGER": $TypeFamily
static readonly "FLOAT": $TypeFamily
static readonly "LONG": $TypeFamily
static readonly "DOUBLE": $TypeFamily
static readonly "OBJECT": $TypeFamily


public static "values"(): ($TypeFamily)[]
public static "valueOf"(arg0: string): $TypeFamily
public "isLesser"(arg0: $TypeFamily$Type): boolean
public "isLesserOrEqual"(arg0: $TypeFamily$Type): boolean
public "isGreater"(arg0: $TypeFamily$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeFamily$Type = (("boolean") | ("double") | ("integer") | ("float") | ("long") | ("unknown") | ("object")) | ($TypeFamily);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeFamily_ = $TypeFamily$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/$ClassesProcessor$ClassNode$Type" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ClassesProcessor$ClassNode$Type extends $Enum<($ClassesProcessor$ClassNode$Type)> {
static readonly "ROOT": $ClassesProcessor$ClassNode$Type
static readonly "MEMBER": $ClassesProcessor$ClassNode$Type
static readonly "ANONYMOUS": $ClassesProcessor$ClassNode$Type
static readonly "LOCAL": $ClassesProcessor$ClassNode$Type
static readonly "LAMBDA": $ClassesProcessor$ClassNode$Type


public static "values"(): ($ClassesProcessor$ClassNode$Type)[]
public static "valueOf"(arg0: string): $ClassesProcessor$ClassNode$Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassesProcessor$ClassNode$Type$Type = (("lambda") | ("root") | ("member") | ("anonymous") | ("local")) | ($ClassesProcessor$ClassNode$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassesProcessor$ClassNode$Type_ = $ClassesProcessor$ClassNode$Type$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructSourceFileAttribute" {
import {$StructMethodParametersAttribute, $StructMethodParametersAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute"
import {$StructNestHostAttribute, $StructNestHostAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructNestHostAttribute"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$Key, $Key$Type} from "packages/org/jetbrains/java/decompiler/util/$Key"
import {$StructAnnotationParameterAttribute, $StructAnnotationParameterAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationParameterAttribute"
import {$StructPermittedSubclassesAttribute, $StructPermittedSubclassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructPermittedSubclassesAttribute"
import {$StructLineNumberTableAttribute, $StructLineNumberTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLineNumberTableAttribute"
import {$StructCodeAttribute, $StructCodeAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructCodeAttribute"
import {$StructEnclosingMethodAttribute, $StructEnclosingMethodAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructEnclosingMethodAttribute"
import {$StructBootstrapMethodsAttribute, $StructBootstrapMethodsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructBootstrapMethodsAttribute"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$StructGenericSignatureAttribute, $StructGenericSignatureAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGenericSignatureAttribute"
import {$StructLocalVariableTableAttribute, $StructLocalVariableTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute"
import {$StructTypeAnnotationAttribute, $StructTypeAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructTypeAnnotationAttribute"
import {$StructConstantValueAttribute, $StructConstantValueAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructConstantValueAttribute"
import {$StructAnnotationAttribute, $StructAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationAttribute"
import {$StructRecordAttribute, $StructRecordAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructRecordAttribute"
import {$StructModuleAttribute, $StructModuleAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute"
import {$StructGeneralAttribute, $StructGeneralAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGeneralAttribute"
import {$StructInnerClassesAttribute, $StructInnerClassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute"
import {$StructLocalVariableTypeTableAttribute, $StructLocalVariableTypeTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTypeTableAttribute"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructExceptionsAttribute, $StructExceptionsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructExceptionsAttribute"
import {$StructAnnDefaultAttribute, $StructAnnDefaultAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnDefaultAttribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructSourceFileAttribute extends $StructGeneralAttribute {
static readonly "ATTRIBUTE_CODE": $Key<($StructCodeAttribute)>
static readonly "ATTRIBUTE_INNER_CLASSES": $Key<($StructInnerClassesAttribute)>
static readonly "ATTRIBUTE_SIGNATURE": $Key<($StructGenericSignatureAttribute)>
static readonly "ATTRIBUTE_ANNOTATION_DEFAULT": $Key<($StructAnnDefaultAttribute)>
static readonly "ATTRIBUTE_EXCEPTIONS": $Key<($StructExceptionsAttribute)>
static readonly "ATTRIBUTE_ENCLOSING_METHOD": $Key<($StructEnclosingMethodAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TABLE": $Key<($StructLocalVariableTableAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TYPE_TABLE": $Key<($StructLocalVariableTypeTableAttribute)>
static readonly "ATTRIBUTE_CONSTANT_VALUE": $Key<($StructConstantValueAttribute)>
static readonly "ATTRIBUTE_BOOTSTRAP_METHODS": $Key<($StructBootstrapMethodsAttribute)>
static readonly "ATTRIBUTE_SYNTHETIC": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_DEPRECATED": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_LINE_NUMBER_TABLE": $Key<($StructLineNumberTableAttribute)>
static readonly "ATTRIBUTE_METHOD_PARAMETERS": $Key<($StructMethodParametersAttribute)>
static readonly "ATTRIBUTE_MODULE": $Key<($StructModuleAttribute)>
static readonly "ATTRIBUTE_RECORD": $Key<($StructRecordAttribute)>
static readonly "ATTRIBUTE_PERMITTED_SUBCLASSES": $Key<($StructPermittedSubclassesAttribute)>
static readonly "ATTRIBUTE_SOURCE_FILE": $Key<($StructSourceFileAttribute)>
static readonly "ATTRIBUTE_NEST_HOST": $Key<($StructNestHostAttribute)>

constructor()

public "initContent"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): void
public "getSourceFile"(arg0: $ConstantPool$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructSourceFileAttribute$Type = ($StructSourceFileAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructSourceFileAttribute_ = $StructSourceFileAttribute$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/code/$ExceptionHandler" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ExceptionHandler {
 "from": integer
 "to": integer
 "handler": integer
 "from_instr": integer
 "to_instr": integer
 "handler_instr": integer
 "exceptionClass": string

constructor()

public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExceptionHandler$Type = ($ExceptionHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExceptionHandler_ = $ExceptionHandler$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructLineNumberTableAttribute" {
import {$StructMethodParametersAttribute, $StructMethodParametersAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute"
import {$StructNestHostAttribute, $StructNestHostAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructNestHostAttribute"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$Key, $Key$Type} from "packages/org/jetbrains/java/decompiler/util/$Key"
import {$StructAnnotationParameterAttribute, $StructAnnotationParameterAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationParameterAttribute"
import {$StructPermittedSubclassesAttribute, $StructPermittedSubclassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructPermittedSubclassesAttribute"
import {$StructCodeAttribute, $StructCodeAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructCodeAttribute"
import {$StructEnclosingMethodAttribute, $StructEnclosingMethodAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructEnclosingMethodAttribute"
import {$StructSourceFileAttribute, $StructSourceFileAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructSourceFileAttribute"
import {$StructBootstrapMethodsAttribute, $StructBootstrapMethodsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructBootstrapMethodsAttribute"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$StructGenericSignatureAttribute, $StructGenericSignatureAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGenericSignatureAttribute"
import {$StructLocalVariableTableAttribute, $StructLocalVariableTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute"
import {$StructTypeAnnotationAttribute, $StructTypeAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructTypeAnnotationAttribute"
import {$StructConstantValueAttribute, $StructConstantValueAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructConstantValueAttribute"
import {$StructAnnotationAttribute, $StructAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationAttribute"
import {$StructRecordAttribute, $StructRecordAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructRecordAttribute"
import {$StructModuleAttribute, $StructModuleAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute"
import {$StructGeneralAttribute, $StructGeneralAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGeneralAttribute"
import {$StructInnerClassesAttribute, $StructInnerClassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute"
import {$StructLocalVariableTypeTableAttribute, $StructLocalVariableTypeTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTypeTableAttribute"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructExceptionsAttribute, $StructExceptionsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructExceptionsAttribute"
import {$StructAnnDefaultAttribute, $StructAnnDefaultAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnDefaultAttribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructLineNumberTableAttribute extends $StructGeneralAttribute {
static readonly "ATTRIBUTE_CODE": $Key<($StructCodeAttribute)>
static readonly "ATTRIBUTE_INNER_CLASSES": $Key<($StructInnerClassesAttribute)>
static readonly "ATTRIBUTE_SIGNATURE": $Key<($StructGenericSignatureAttribute)>
static readonly "ATTRIBUTE_ANNOTATION_DEFAULT": $Key<($StructAnnDefaultAttribute)>
static readonly "ATTRIBUTE_EXCEPTIONS": $Key<($StructExceptionsAttribute)>
static readonly "ATTRIBUTE_ENCLOSING_METHOD": $Key<($StructEnclosingMethodAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TABLE": $Key<($StructLocalVariableTableAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TYPE_TABLE": $Key<($StructLocalVariableTypeTableAttribute)>
static readonly "ATTRIBUTE_CONSTANT_VALUE": $Key<($StructConstantValueAttribute)>
static readonly "ATTRIBUTE_BOOTSTRAP_METHODS": $Key<($StructBootstrapMethodsAttribute)>
static readonly "ATTRIBUTE_SYNTHETIC": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_DEPRECATED": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_LINE_NUMBER_TABLE": $Key<($StructLineNumberTableAttribute)>
static readonly "ATTRIBUTE_METHOD_PARAMETERS": $Key<($StructMethodParametersAttribute)>
static readonly "ATTRIBUTE_MODULE": $Key<($StructModuleAttribute)>
static readonly "ATTRIBUTE_RECORD": $Key<($StructRecordAttribute)>
static readonly "ATTRIBUTE_PERMITTED_SUBCLASSES": $Key<($StructPermittedSubclassesAttribute)>
static readonly "ATTRIBUTE_SOURCE_FILE": $Key<($StructSourceFileAttribute)>
static readonly "ATTRIBUTE_NEST_HOST": $Key<($StructNestHostAttribute)>

constructor()

public "getRawData"(): (integer)[]
public "initContent"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): void
public "findLineNumber"(arg0: integer): integer
get "rawData"(): (integer)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructLineNumberTableAttribute$Type = ($StructLineNumberTableAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructLineNumberTableAttribute_ = $StructLineNumberTableAttribute$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/api/plugin/$LanguageSpec" {
import {$Pass, $Pass$Type} from "packages/org/jetbrains/java/decompiler/api/plugin/pass/$Pass"
import {$GraphParser, $GraphParser$Type} from "packages/org/jetbrains/java/decompiler/api/plugin/$GraphParser"
import {$LanguageChooser, $LanguageChooser$Type} from "packages/org/jetbrains/java/decompiler/api/plugin/$LanguageChooser"
import {$StatementWriter, $StatementWriter$Type} from "packages/org/jetbrains/java/decompiler/api/plugin/$StatementWriter"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $LanguageSpec {
readonly "name": string
readonly "chooser": $LanguageChooser
readonly "graphParser": $GraphParser
readonly "writer": $StatementWriter
readonly "pass": $Pass

constructor(arg0: string, arg1: $LanguageChooser$Type, arg2: $GraphParser$Type, arg3: $StatementWriter$Type, arg4: $Pass$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LanguageSpec$Type = ($LanguageSpec);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LanguageSpec_ = $LanguageSpec$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute$OpensEntry" {
import {$List, $List$Type} from "packages/java/util/$List"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructModuleAttribute$OpensEntry {
readonly "packageName": string
readonly "flags": integer
readonly "opensToModules": $List<(string)>

constructor(arg0: string, arg1: integer, arg2: $List$Type<(string)>)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructModuleAttribute$OpensEntry$Type = ($StructModuleAttribute$OpensEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructModuleAttribute$OpensEntry_ = $StructModuleAttribute$OpensEntry$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/gen/$NewClassNameBuilder" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $NewClassNameBuilder {

 "buildNewClassname"(arg0: string): string

(arg0: string): string
}

export namespace $NewClassNameBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NewClassNameBuilder$Type = ($NewClassNameBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NewClassNameBuilder_ = $NewClassNameBuilder$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute$Entry" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructInnerClassesAttribute$Entry {
readonly "outerNameIdx": integer
readonly "simpleNameIdx": integer
readonly "accessFlags": integer
readonly "innerName": string
readonly "enclosingName": string
readonly "simpleName": string


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructInnerClassesAttribute$Entry$Type = ($StructInnerClassesAttribute$Entry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructInnerClassesAttribute$Entry_ = $StructInnerClassesAttribute$Entry$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/$StructMethod" {
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructClass, $StructClass$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructClass"
import {$IVariableNameProvider, $IVariableNameProvider$Type} from "packages/org/jetbrains/java/decompiler/main/extern/$IVariableNameProvider"
import {$StructLocalVariableTableAttribute, $StructLocalVariableTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute"
import {$MethodDescriptor, $MethodDescriptor$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$MethodDescriptor"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$StructMember, $StructMember$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructMember"
import {$InstructionSequence, $InstructionSequence$Type} from "packages/org/jetbrains/java/decompiler/code/$InstructionSequence"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$GenericMethodDescriptor, $GenericMethodDescriptor$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/generics/$GenericMethodDescriptor"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructMethod extends $StructMember {


public "expandData"(arg0: $StructClass$Type): void
public "clearVariableNamer"(): void
public "getName"(): string
public "toString"(): string
public "getDescriptor"(): string
public "getSignature"(): $GenericMethodDescriptor
public "methodDescriptor"(): $MethodDescriptor
public static "create"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: string, arg3: $BytecodeVersion$Type, arg4: boolean): $StructMethod
public "releaseResources"(): void
public "getLocalVariables"(): integer
public "getVariableNamer"(): $IVariableNameProvider
public "containsCode"(): boolean
public "getBytecodeVersion"(): $BytecodeVersion
public "getLocalVariableAttr"(): $StructLocalVariableTableAttribute
public "getClassQualifiedName"(): string
public "getInstructionSequence"(): $InstructionSequence
get "name"(): string
get "descriptor"(): string
get "signature"(): $GenericMethodDescriptor
get "localVariables"(): integer
get "variableNamer"(): $IVariableNameProvider
get "bytecodeVersion"(): $BytecodeVersion
get "localVariableAttr"(): $StructLocalVariableTableAttribute
get "classQualifiedName"(): string
get "instructionSequence"(): $InstructionSequence
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructMethod$Type = ($StructMethod);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructMethod_ = $StructMethod$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarTypeProcessor$FinalType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $VarTypeProcessor$FinalType extends $Enum<($VarTypeProcessor$FinalType)> {
static readonly "NON_FINAL": $VarTypeProcessor$FinalType
static readonly "EXPLICIT_FINAL": $VarTypeProcessor$FinalType
static readonly "FINAL": $VarTypeProcessor$FinalType


public static "values"(): ($VarTypeProcessor$FinalType)[]
public static "valueOf"(arg0: string): $VarTypeProcessor$FinalType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VarTypeProcessor$FinalType$Type = (("explicit_final") | ("non_final") | ("final")) | ($VarTypeProcessor$FinalType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VarTypeProcessor$FinalType_ = $VarTypeProcessor$FinalType$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/gen/generics/$GenericType" {
import {$CodeType, $CodeType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$CodeType"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$TypeFamily, $TypeFamily$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$TypeFamily"
import {$VarType, $VarType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$VarType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $GenericType extends $VarType {
static readonly "WILDCARD_EXTENDS": integer
static readonly "WILDCARD_SUPER": integer
static readonly "WILDCARD_UNBOUND": integer
static readonly "WILDCARD_NO": integer
static readonly "DUMMY_VAR": $GenericType
static readonly "EMPTY_ARRAY": ($VarType)[]
static readonly "VARTYPE_UNKNOWN": $VarType
static readonly "VARTYPE_INT": $VarType
static readonly "VARTYPE_FLOAT": $VarType
static readonly "VARTYPE_LONG": $VarType
static readonly "VARTYPE_DOUBLE": $VarType
static readonly "VARTYPE_BYTE": $VarType
static readonly "VARTYPE_CHAR": $VarType
static readonly "VARTYPE_SHORT": $VarType
static readonly "VARTYPE_BOOLEAN": $VarType
static readonly "VARTYPE_BYTECHAR": $VarType
static readonly "VARTYPE_SHORTCHAR": $VarType
static readonly "VARTYPE_NULL": $VarType
static readonly "VARTYPE_STRING": $VarType
static readonly "VARTYPE_CLASS": $VarType
static readonly "VARTYPE_OBJECT": $VarType
static readonly "VARTYPE_INTEGER": $VarType
static readonly "VARTYPE_CHARACTER": $VarType
static readonly "VARTYPE_BYTE_OBJ": $VarType
static readonly "VARTYPE_SHORT_OBJ": $VarType
static readonly "VARTYPE_BOOLEAN_OBJ": $VarType
static readonly "VARTYPE_FLOAT_OBJ": $VarType
static readonly "VARTYPE_DOUBLE_OBJ": $VarType
static readonly "VARTYPE_LONG_OBJ": $VarType
static readonly "VARTYPE_VOID": $VarType
static readonly "UNBOXING_TYPES": $Map<($VarType), ($VarType)>
readonly "type": $CodeType
readonly "arrayDim": integer
readonly "value": string
readonly "typeFamily": $TypeFamily
readonly "stackSize": integer

constructor(arg0: $CodeType$Type, arg1: integer, arg2: string, arg3: $VarType$Type, arg4: $List$Type<($VarType$Type)>, arg5: integer)

public "toString"(): string
public "getParent"(): $VarType
public "isGeneric"(): boolean
public static "parse"(arg0: string, arg1: integer): $VarType
public static "parse"(arg0: string): $VarType
public static "isAssignable"(arg0: $VarType$Type, arg1: $VarType$Type, arg2: $Map$Type<($VarType$Type), ($List$Type<($VarType$Type)>)>): boolean
public "getArguments"(): $List<($VarType)>
public "equalsExact"(arg0: any): boolean
public "isTypeUnfinished"(): boolean
public "resizeArrayDim"(arg0: integer): $VarType
public "decreaseArrayDim"(): $GenericType
public "getAllGenericVars"(): $List<($GenericType)>
public "getWildcard"(): integer
public "mapGenVarsTo"(arg0: $GenericType$Type, arg1: $Map$Type<($VarType$Type), ($VarType$Type)>): void
public "remap"(arg0: $Map$Type<($VarType$Type), ($VarType$Type)>): $VarType
public static "cleanLoweredGenericTypes"(arg0: $Map$Type<($VarType$Type), ($VarType$Type)>, arg1: $GenericType$Type, arg2: $GenericType$Type, arg3: $Set$Type<($VarType$Type)>): void
public static "areArgumentsAssignable"(arg0: $VarType$Type, arg1: $VarType$Type, arg2: $Map$Type<($VarType$Type), ($List$Type<($VarType$Type)>)>): boolean
public static "getNextType"(arg0: string): string
public "getCastName"(): string
public "findBaseType"(): $GenericType
public static "withWildcard"(arg0: $VarType$Type, arg1: integer): $VarType
public "argumentsEqual"(arg0: $GenericType$Type): boolean
public "hasUnknownGenericType"(arg0: $Set$Type<($VarType$Type)>): boolean
public static "getGenericSuperType"(arg0: $VarType$Type, arg1: $VarType$Type): $VarType
get "parent"(): $VarType
get "generic"(): boolean
get "arguments"(): $List<($VarType)>
get "typeUnfinished"(): boolean
get "allGenericVars"(): $List<($GenericType)>
get "wildcard"(): integer
get "castName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenericType$Type = ($GenericType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenericType_ = $GenericType$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationParameterAttribute" {
import {$StructMethodParametersAttribute, $StructMethodParametersAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute"
import {$StructNestHostAttribute, $StructNestHostAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructNestHostAttribute"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$Key, $Key$Type} from "packages/org/jetbrains/java/decompiler/util/$Key"
import {$StructPermittedSubclassesAttribute, $StructPermittedSubclassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructPermittedSubclassesAttribute"
import {$StructLineNumberTableAttribute, $StructLineNumberTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLineNumberTableAttribute"
import {$StructCodeAttribute, $StructCodeAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructCodeAttribute"
import {$StructEnclosingMethodAttribute, $StructEnclosingMethodAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructEnclosingMethodAttribute"
import {$StructSourceFileAttribute, $StructSourceFileAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructSourceFileAttribute"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructBootstrapMethodsAttribute, $StructBootstrapMethodsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructBootstrapMethodsAttribute"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$AnnotationExprent, $AnnotationExprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$AnnotationExprent"
import {$StructGenericSignatureAttribute, $StructGenericSignatureAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGenericSignatureAttribute"
import {$StructLocalVariableTableAttribute, $StructLocalVariableTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute"
import {$StructTypeAnnotationAttribute, $StructTypeAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructTypeAnnotationAttribute"
import {$StructConstantValueAttribute, $StructConstantValueAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructConstantValueAttribute"
import {$StructAnnotationAttribute, $StructAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationAttribute"
import {$StructRecordAttribute, $StructRecordAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructRecordAttribute"
import {$StructModuleAttribute, $StructModuleAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute"
import {$StructGeneralAttribute, $StructGeneralAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGeneralAttribute"
import {$StructInnerClassesAttribute, $StructInnerClassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute"
import {$StructLocalVariableTypeTableAttribute, $StructLocalVariableTypeTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTypeTableAttribute"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructExceptionsAttribute, $StructExceptionsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructExceptionsAttribute"
import {$StructAnnDefaultAttribute, $StructAnnDefaultAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnDefaultAttribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructAnnotationParameterAttribute extends $StructGeneralAttribute {
static readonly "ATTRIBUTE_CODE": $Key<($StructCodeAttribute)>
static readonly "ATTRIBUTE_INNER_CLASSES": $Key<($StructInnerClassesAttribute)>
static readonly "ATTRIBUTE_SIGNATURE": $Key<($StructGenericSignatureAttribute)>
static readonly "ATTRIBUTE_ANNOTATION_DEFAULT": $Key<($StructAnnDefaultAttribute)>
static readonly "ATTRIBUTE_EXCEPTIONS": $Key<($StructExceptionsAttribute)>
static readonly "ATTRIBUTE_ENCLOSING_METHOD": $Key<($StructEnclosingMethodAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TABLE": $Key<($StructLocalVariableTableAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TYPE_TABLE": $Key<($StructLocalVariableTypeTableAttribute)>
static readonly "ATTRIBUTE_CONSTANT_VALUE": $Key<($StructConstantValueAttribute)>
static readonly "ATTRIBUTE_BOOTSTRAP_METHODS": $Key<($StructBootstrapMethodsAttribute)>
static readonly "ATTRIBUTE_SYNTHETIC": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_DEPRECATED": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_LINE_NUMBER_TABLE": $Key<($StructLineNumberTableAttribute)>
static readonly "ATTRIBUTE_METHOD_PARAMETERS": $Key<($StructMethodParametersAttribute)>
static readonly "ATTRIBUTE_MODULE": $Key<($StructModuleAttribute)>
static readonly "ATTRIBUTE_RECORD": $Key<($StructRecordAttribute)>
static readonly "ATTRIBUTE_PERMITTED_SUBCLASSES": $Key<($StructPermittedSubclassesAttribute)>
static readonly "ATTRIBUTE_SOURCE_FILE": $Key<($StructSourceFileAttribute)>
static readonly "ATTRIBUTE_NEST_HOST": $Key<($StructNestHostAttribute)>

constructor()

public "getParamAnnotations"(): $List<($List<($AnnotationExprent)>)>
public "initContent"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): void
get "paramAnnotations"(): $List<($List<($AnnotationExprent)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructAnnotationParameterAttribute$Type = ($StructAnnotationParameterAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructAnnotationParameterAttribute_ = $StructAnnotationParameterAttribute$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarVersionPair" {
import {$VarExprent, $VarExprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$VarExprent"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $VarVersionPair {
readonly "var": integer
readonly "version": integer

constructor(arg0: integer, arg1: integer)
constructor(arg0: $VarExprent$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VarVersionPair$Type = ($VarVersionPair);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VarVersionPair_ = $VarVersionPair$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$RootStatement" {
import {$ControlFlowGraph, $ControlFlowGraph$Type} from "packages/org/jetbrains/java/decompiler/code/cfg/$ControlFlowGraph"
import {$TextBuffer, $TextBuffer$Type} from "packages/org/jetbrains/java/decompiler/util/$TextBuffer"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Statement$StatementType, $Statement$StatementType$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$Statement$StatementType"
import {$StructMethod, $StructMethod$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructMethod"
import {$DummyExitStatement, $DummyExitStatement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$DummyExitStatement"
import {$StartEndPair, $StartEndPair$Type} from "packages/org/jetbrains/java/decompiler/util/$StartEndPair"
import {$Statement, $Statement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$Statement"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $RootStatement extends $Statement {
readonly "mt": $StructMethod
 "commentLines": $Set<(string)>
 "addErrorComment": boolean
static readonly "STATEDGE_ALL": integer
static readonly "STATEDGE_DIRECT_ALL": integer
readonly "type": $Statement$StatementType
readonly "id": integer

constructor(arg0: $Statement$Type, arg1: $DummyExitStatement$Type, arg2: $StructMethod$Type)

public "getStartEndRange"(): $StartEndPair
public "hasSwitch"(): boolean
public "hasTryCatch"(): boolean
public "hasLoops"(): boolean
public "buildContentFlags"(): void
public "getDummyExit"(): $DummyExitStatement
public "toJava"(arg0: integer): $TextBuffer
public "addComment"(arg0: string, arg1: boolean): void
public "addComment"(arg0: string): void
public "addComments"(arg0: $ControlFlowGraph$Type): void
public "addComments"(arg0: $RootStatement$Type): void
get "startEndRange"(): $StartEndPair
get "dummyExit"(): $DummyExitStatement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RootStatement$Type = ($RootStatement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RootStatement_ = $RootStatement$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/gen/$CodeType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $CodeType extends $Enum<($CodeType)> {
static readonly "BYTE": $CodeType
static readonly "CHAR": $CodeType
static readonly "DOUBLE": $CodeType
static readonly "FLOAT": $CodeType
static readonly "INT": $CodeType
static readonly "LONG": $CodeType
static readonly "SHORT": $CodeType
static readonly "BOOLEAN": $CodeType
static readonly "OBJECT": $CodeType
static readonly "ADDRESS": $CodeType
static readonly "VOID": $CodeType
static readonly "ANY": $CodeType
static readonly "GROUP2EMPTY": $CodeType
static readonly "NULL": $CodeType
static readonly "NOTINITIALIZED": $CodeType
static readonly "BYTECHAR": $CodeType
static readonly "SHORTCHAR": $CodeType
static readonly "UNKNOWN": $CodeType
static readonly "GENVAR": $CodeType


public static "values"(): ($CodeType)[]
public static "valueOf"(arg0: string): $CodeType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CodeType$Type = (("address") | ("void") | ("byte") | ("double") | ("group2empty") | ("notinitialized") | ("bytechar") | ("float") | ("any") | ("int") | ("long") | ("unknown") | ("genvar") | ("boolean") | ("null") | ("shortchar") | ("char") | ("short") | ("object")) | ($CodeType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CodeType_ = $CodeType$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/collectors/$ImportCollector" {
import {$ClassesProcessor$ClassNode, $ClassesProcessor$ClassNode$Type} from "packages/org/jetbrains/java/decompiler/main/$ClassesProcessor$ClassNode"
import {$TextBuffer, $TextBuffer$Type} from "packages/org/jetbrains/java/decompiler/util/$TextBuffer"
import {$ImportCollector$Lock, $ImportCollector$Lock$Type} from "packages/org/jetbrains/java/decompiler/main/collectors/$ImportCollector$Lock"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ImportCollector {

constructor(arg0: $ClassesProcessor$ClassNode$Type)
constructor(arg0: $ImportCollector$Type)

public "writeImports"(arg0: $TextBuffer$Type, arg1: boolean): void
public "lock"(): $ImportCollector$Lock
public "getShortName"(arg0: string, arg1: boolean): string
public "getShortName"(arg0: string): string
public "getShortNameInClassContext"(arg0: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImportCollector$Type = ($ImportCollector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImportCollector_ = $ImportCollector$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructBootstrapMethodsAttribute" {
import {$StructMethodParametersAttribute, $StructMethodParametersAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute"
import {$StructNestHostAttribute, $StructNestHostAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructNestHostAttribute"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$Key, $Key$Type} from "packages/org/jetbrains/java/decompiler/util/$Key"
import {$StructAnnotationParameterAttribute, $StructAnnotationParameterAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationParameterAttribute"
import {$PooledConstant, $PooledConstant$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$PooledConstant"
import {$StructPermittedSubclassesAttribute, $StructPermittedSubclassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructPermittedSubclassesAttribute"
import {$StructLineNumberTableAttribute, $StructLineNumberTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLineNumberTableAttribute"
import {$StructCodeAttribute, $StructCodeAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructCodeAttribute"
import {$StructEnclosingMethodAttribute, $StructEnclosingMethodAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructEnclosingMethodAttribute"
import {$StructSourceFileAttribute, $StructSourceFileAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructSourceFileAttribute"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$LinkConstant, $LinkConstant$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$LinkConstant"
import {$StructGenericSignatureAttribute, $StructGenericSignatureAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGenericSignatureAttribute"
import {$StructLocalVariableTableAttribute, $StructLocalVariableTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute"
import {$StructTypeAnnotationAttribute, $StructTypeAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructTypeAnnotationAttribute"
import {$StructConstantValueAttribute, $StructConstantValueAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructConstantValueAttribute"
import {$StructAnnotationAttribute, $StructAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationAttribute"
import {$StructRecordAttribute, $StructRecordAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructRecordAttribute"
import {$StructModuleAttribute, $StructModuleAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute"
import {$StructGeneralAttribute, $StructGeneralAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGeneralAttribute"
import {$StructInnerClassesAttribute, $StructInnerClassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute"
import {$StructLocalVariableTypeTableAttribute, $StructLocalVariableTypeTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTypeTableAttribute"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructExceptionsAttribute, $StructExceptionsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructExceptionsAttribute"
import {$StructAnnDefaultAttribute, $StructAnnDefaultAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnDefaultAttribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructBootstrapMethodsAttribute extends $StructGeneralAttribute {
static readonly "ATTRIBUTE_CODE": $Key<($StructCodeAttribute)>
static readonly "ATTRIBUTE_INNER_CLASSES": $Key<($StructInnerClassesAttribute)>
static readonly "ATTRIBUTE_SIGNATURE": $Key<($StructGenericSignatureAttribute)>
static readonly "ATTRIBUTE_ANNOTATION_DEFAULT": $Key<($StructAnnDefaultAttribute)>
static readonly "ATTRIBUTE_EXCEPTIONS": $Key<($StructExceptionsAttribute)>
static readonly "ATTRIBUTE_ENCLOSING_METHOD": $Key<($StructEnclosingMethodAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TABLE": $Key<($StructLocalVariableTableAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TYPE_TABLE": $Key<($StructLocalVariableTypeTableAttribute)>
static readonly "ATTRIBUTE_CONSTANT_VALUE": $Key<($StructConstantValueAttribute)>
static readonly "ATTRIBUTE_BOOTSTRAP_METHODS": $Key<($StructBootstrapMethodsAttribute)>
static readonly "ATTRIBUTE_SYNTHETIC": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_DEPRECATED": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_LINE_NUMBER_TABLE": $Key<($StructLineNumberTableAttribute)>
static readonly "ATTRIBUTE_METHOD_PARAMETERS": $Key<($StructMethodParametersAttribute)>
static readonly "ATTRIBUTE_MODULE": $Key<($StructModuleAttribute)>
static readonly "ATTRIBUTE_RECORD": $Key<($StructRecordAttribute)>
static readonly "ATTRIBUTE_PERMITTED_SUBCLASSES": $Key<($StructPermittedSubclassesAttribute)>
static readonly "ATTRIBUTE_SOURCE_FILE": $Key<($StructSourceFileAttribute)>
static readonly "ATTRIBUTE_NEST_HOST": $Key<($StructNestHostAttribute)>

constructor()

public "getMethodArguments"(arg0: integer): $List<($PooledConstant)>
public "getMethodReference"(arg0: integer): $LinkConstant
public "getMethodsNumber"(): integer
public "initContent"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): void
get "methodsNumber"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructBootstrapMethodsAttribute$Type = ($StructBootstrapMethodsAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructBootstrapMethodsAttribute_ = $StructBootstrapMethodsAttribute$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/util/collections/$FastSparseSetFactory" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$FastSparseSetFactory$FastSparseSet, $FastSparseSetFactory$FastSparseSet$Type} from "packages/org/jetbrains/java/decompiler/util/collections/$FastSparseSetFactory$FastSparseSet"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $FastSparseSetFactory<E> {

constructor(arg0: $Collection$Type<(any)>)

public "createEmptySet"(): $FastSparseSetFactory$FastSparseSet<(E)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastSparseSetFactory$Type<E> = ($FastSparseSetFactory<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastSparseSetFactory_<E> = $FastSparseSetFactory$Type<(E)>;
}}
declare module "packages/org/jetbrains/java/decompiler/util/collections/$FastSparseSetFactory$FastSparseSet" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $FastSparseSetFactory$FastSparseSet<E> implements $Iterable<(E)> {
static readonly "EMPTY_ARRAY": ($FastSparseSetFactory$FastSparseSet<(any)>)[]


public "add"(arg0: E): void
public "remove"(arg0: E): void
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "isEmpty"(): boolean
public "iterator"(): $Iterator<(E)>
public "contains"(arg0: E): boolean
public "complement"(arg0: $FastSparseSetFactory$FastSparseSet$Type<(E)>): void
public "union"(arg0: $FastSparseSetFactory$FastSparseSet$Type<(E)>): void
public "toPlainSet"(): $Set<(E)>
public "getCopy"(): $FastSparseSetFactory$FastSparseSet<(E)>
public "intersection"(arg0: $FastSparseSetFactory$FastSparseSet$Type<(E)>): void
public "getCardinality"(): integer
public "spliterator"(): $Spliterator<(E)>
public "forEach"(arg0: $Consumer$Type<(any)>): void
[Symbol.iterator](): IterableIterator<E>;
get "empty"(): boolean
get "copy"(): $FastSparseSetFactory$FastSparseSet<(E)>
get "cardinality"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastSparseSetFactory$FastSparseSet$Type<E> = ($FastSparseSetFactory$FastSparseSet<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastSparseSetFactory$FastSparseSet_<E> = $FastSparseSetFactory$FastSparseSet$Type<(E)>;
}}
declare module "packages/org/jetbrains/java/decompiler/util/collections/$SFormsFastMapDirect" {
import {$VarExprent, $VarExprent$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/exps/$VarExprent"
import {$VarVersionNode, $VarVersionNode$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarVersionNode"
import {$List, $List$Type} from "packages/java/util/$List"
import {$FastSparseSetFactory$FastSparseSet, $FastSparseSetFactory$FastSparseSet$Type} from "packages/org/jetbrains/java/decompiler/util/collections/$FastSparseSetFactory$FastSparseSet"
import {$FastSparseSetFactory, $FastSparseSetFactory$Type} from "packages/org/jetbrains/java/decompiler/util/collections/$FastSparseSetFactory"
import {$VarVersionPair, $VarVersionPair$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarVersionPair"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $SFormsFastMapDirect {

constructor(arg0: $FastSparseSetFactory$Type<(integer)>)

public "setCurrentVar"(arg0: $VarVersionPair$Type): void
public "setCurrentVar"(arg0: $VarExprent$Type): void
public "setCurrentVar"(arg0: integer, arg1: integer): void
public "setCurrentVar"(arg0: $VarVersionNode$Type): void
public "remove"(arg0: integer): void
public "get"(arg0: integer): $FastSparseSetFactory$FastSparseSet<(integer)>
public "get"(arg0: $VarExprent$Type): $FastSparseSetFactory$FastSparseSet<(integer)>
public "put"(arg0: integer, arg1: $FastSparseSetFactory$FastSparseSet$Type<(integer)>): void
public "toString"(): string
public "isEmpty"(): boolean
public "size"(): integer
public "containsKey"(arg0: integer): boolean
public "complement"(arg0: $SFormsFastMapDirect$Type): void
public "union"(arg0: $SFormsFastMapDirect$Type): void
public "getCopy"(): $SFormsFastMapDirect
public "removeAllFields"(): void
public "entryList"(): $List<($Map$Entry<(integer), ($FastSparseSetFactory$FastSparseSet<(integer)>)>)>
public "intersection"(arg0: $SFormsFastMapDirect$Type): void
public "removeAllStacks"(): void
set "currentVar"(value: $VarVersionPair$Type)
set "currentVar"(value: $VarExprent$Type)
set "currentVar"(value: $VarVersionNode$Type)
get "empty"(): boolean
get "copy"(): $SFormsFastMapDirect
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SFormsFastMapDirect$Type = ($SFormsFastMapDirect);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SFormsFastMapDirect_ = $SFormsFastMapDirect$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/gen/$MethodDescriptor" {
import {$NewClassNameBuilder, $NewClassNameBuilder$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$NewClassNameBuilder"
import {$ClassesProcessor$ClassNode, $ClassesProcessor$ClassNode$Type} from "packages/org/jetbrains/java/decompiler/main/$ClassesProcessor$ClassNode"
import {$VarType, $VarType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$VarType"
import {$StructMethod, $StructMethod$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructMethod"
import {$GenericMethodDescriptor, $GenericMethodDescriptor$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/generics/$GenericMethodDescriptor"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MethodDescriptor {
readonly "params": ($VarType)[]
readonly "ret": $VarType
 "genericInfo": $GenericMethodDescriptor


public "buildNewDescriptor"(arg0: $NewClassNameBuilder$Type): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "parseDescriptor"(arg0: $StructMethod$Type, arg1: $ClassesProcessor$ClassNode$Type): $MethodDescriptor
public static "parseDescriptor"(arg0: string): $MethodDescriptor
public "addGenericDescriptor"(arg0: $GenericMethodDescriptor$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MethodDescriptor$Type = ($MethodDescriptor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MethodDescriptor_ = $MethodDescriptor$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/gen/$VarType" {
import {$CodeType, $CodeType$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$CodeType"
import {$TypeFamily, $TypeFamily$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$TypeFamily"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $VarType {
static readonly "EMPTY_ARRAY": ($VarType)[]
static readonly "VARTYPE_UNKNOWN": $VarType
static readonly "VARTYPE_INT": $VarType
static readonly "VARTYPE_FLOAT": $VarType
static readonly "VARTYPE_LONG": $VarType
static readonly "VARTYPE_DOUBLE": $VarType
static readonly "VARTYPE_BYTE": $VarType
static readonly "VARTYPE_CHAR": $VarType
static readonly "VARTYPE_SHORT": $VarType
static readonly "VARTYPE_BOOLEAN": $VarType
static readonly "VARTYPE_BYTECHAR": $VarType
static readonly "VARTYPE_SHORTCHAR": $VarType
static readonly "VARTYPE_NULL": $VarType
static readonly "VARTYPE_STRING": $VarType
static readonly "VARTYPE_CLASS": $VarType
static readonly "VARTYPE_OBJECT": $VarType
static readonly "VARTYPE_INTEGER": $VarType
static readonly "VARTYPE_CHARACTER": $VarType
static readonly "VARTYPE_BYTE_OBJ": $VarType
static readonly "VARTYPE_SHORT_OBJ": $VarType
static readonly "VARTYPE_BOOLEAN_OBJ": $VarType
static readonly "VARTYPE_FLOAT_OBJ": $VarType
static readonly "VARTYPE_DOUBLE_OBJ": $VarType
static readonly "VARTYPE_LONG_OBJ": $VarType
static readonly "VARTYPE_VOID": $VarType
static readonly "UNBOXING_TYPES": $Map<($VarType), ($VarType)>
readonly "type": $CodeType
readonly "arrayDim": integer
readonly "value": string
readonly "typeFamily": $TypeFamily
readonly "stackSize": integer

constructor(arg0: string, arg1: boolean)
constructor(arg0: string)
constructor(arg0: $CodeType$Type)
constructor(arg0: $CodeType$Type, arg1: integer)
constructor(arg0: $CodeType$Type, arg1: integer, arg2: string)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "isPrimitive"(arg0: $VarType$Type): boolean
public static "getChar"(arg0: $CodeType$Type): string
public static "getType"(arg0: character): $CodeType
public "isGeneric"(): boolean
public "resizeArrayDim"(arg0: integer): $VarType
public static "getMinTypeInFamily"(arg0: $TypeFamily$Type): $VarType
public "decreaseArrayDim"(): $VarType
public "remap"(arg0: $Map$Type<($VarType$Type), ($VarType$Type)>): $VarType
public static "getCommonSupertype"(arg0: $VarType$Type, arg1: $VarType$Type): $VarType
public "isSuperset"(arg0: $VarType$Type): boolean
public "isStrictSuperset"(arg0: $VarType$Type): boolean
public static "getCommonMinType"(arg0: $VarType$Type, arg1: $VarType$Type): $VarType
get "generic"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VarType$Type = ($VarType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VarType_ = $VarType$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/modules/decompiler/sforms/$VarMapHolder" {
import {$SFormsFastMapDirect, $SFormsFastMapDirect$Type} from "packages/org/jetbrains/java/decompiler/util/collections/$SFormsFastMapDirect"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $VarMapHolder {


public "mergeIfFalse"(arg0: $SFormsFastMapDirect$Type): void
public "makeFullyMutable"(): void
public static "ofNormal"(arg0: $SFormsFastMapDirect$Type): $VarMapHolder
public "getIfFalse"(): $SFormsFastMapDirect
public "getIfTrue"(): $SFormsFastMapDirect
public "setNormal"(arg0: $SFormsFastMapDirect$Type): void
public "mergeIfTrue"(arg0: $SFormsFastMapDirect$Type): void
public "set"(arg0: $VarMapHolder$Type): void
public "swap"(): void
public "isNormal"(): boolean
public "mergeNormal"(arg0: $SFormsFastMapDirect$Type): void
public "getNormal"(): $SFormsFastMapDirect
public "toNormal"(): $SFormsFastMapDirect
public "assertIsNormal"(): void
get "ifFalse"(): $SFormsFastMapDirect
get "ifTrue"(): $SFormsFastMapDirect
set "normal"(value: $SFormsFastMapDirect$Type)
get "normal"(): boolean
get "normal"(): $SFormsFastMapDirect
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VarMapHolder$Type = ($VarMapHolder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VarMapHolder_ = $VarMapHolder$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/api/plugin/$LanguageChooser" {
import {$StructClass, $StructClass$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructClass"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $LanguageChooser {

 "isLanguage"(arg0: $StructClass$Type): boolean

(arg0: $StructClass$Type): boolean
}

export namespace $LanguageChooser {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LanguageChooser$Type = ($LanguageChooser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LanguageChooser_ = $LanguageChooser$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute$ProvidesEntry" {
import {$List, $List$Type} from "packages/java/util/$List"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructModuleAttribute$ProvidesEntry {
readonly "interfaceName": string
readonly "implementationNames": $List<(string)>

constructor(arg0: string, arg1: $List$Type<(string)>)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructModuleAttribute$ProvidesEntry$Type = ($StructModuleAttribute$ProvidesEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructModuleAttribute$ProvidesEntry_ = $StructModuleAttribute$ProvidesEntry$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/extern/$IBytecodeProvider" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * 
 * @deprecated
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IBytecodeProvider {

 "getBytecode"(arg0: string, arg1: string): (byte)[]

(arg0: string, arg1: string): (byte)[]
}

export namespace $IBytecodeProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IBytecodeProvider$Type = ($IBytecodeProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IBytecodeProvider_ = $IBytecodeProvider$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/extern/$IContextSource$Entry" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $IContextSource$Entry {
static readonly "BASE_VERSION": integer

constructor(arg0: string, arg1: integer)

public "path"(): string
public static "parse"(arg0: string): $IContextSource$Entry
public "basePath"(): string
public "multirelease"(): integer
public static "atBase"(arg0: string): $IContextSource$Entry
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IContextSource$Entry$Type = ($IContextSource$Entry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IContextSource$Entry_ = $IContextSource$Entry$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute$ExportsEntry" {
import {$List, $List$Type} from "packages/java/util/$List"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructModuleAttribute$ExportsEntry {
readonly "packageName": string
readonly "flags": integer
readonly "exportToModules": $List<(string)>

constructor(arg0: string, arg1: integer, arg2: $List$Type<(string)>)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructModuleAttribute$ExportsEntry$Type = ($StructModuleAttribute$ExportsEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructModuleAttribute$ExportsEntry_ = $StructModuleAttribute$ExportsEntry$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute" {
import {$StructModuleAttribute$ProvidesEntry, $StructModuleAttribute$ProvidesEntry$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute$ProvidesEntry"
import {$ModuleDescriptor, $ModuleDescriptor$Type} from "packages/java/lang/module/$ModuleDescriptor"
import {$StructMethodParametersAttribute, $StructMethodParametersAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute"
import {$StructNestHostAttribute, $StructNestHostAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructNestHostAttribute"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$Key, $Key$Type} from "packages/org/jetbrains/java/decompiler/util/$Key"
import {$StructAnnotationParameterAttribute, $StructAnnotationParameterAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationParameterAttribute"
import {$StructPermittedSubclassesAttribute, $StructPermittedSubclassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructPermittedSubclassesAttribute"
import {$StructModuleAttribute$OpensEntry, $StructModuleAttribute$OpensEntry$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute$OpensEntry"
import {$StructLineNumberTableAttribute, $StructLineNumberTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLineNumberTableAttribute"
import {$StructCodeAttribute, $StructCodeAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructCodeAttribute"
import {$StructEnclosingMethodAttribute, $StructEnclosingMethodAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructEnclosingMethodAttribute"
import {$StructSourceFileAttribute, $StructSourceFileAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructSourceFileAttribute"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructBootstrapMethodsAttribute, $StructBootstrapMethodsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructBootstrapMethodsAttribute"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$StructModuleAttribute$RequiresEntry, $StructModuleAttribute$RequiresEntry$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute$RequiresEntry"
import {$StructGenericSignatureAttribute, $StructGenericSignatureAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGenericSignatureAttribute"
import {$StructLocalVariableTableAttribute, $StructLocalVariableTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute"
import {$StructTypeAnnotationAttribute, $StructTypeAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructTypeAnnotationAttribute"
import {$StructConstantValueAttribute, $StructConstantValueAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructConstantValueAttribute"
import {$StructAnnotationAttribute, $StructAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationAttribute"
import {$StructRecordAttribute, $StructRecordAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructRecordAttribute"
import {$StructGeneralAttribute, $StructGeneralAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGeneralAttribute"
import {$StructInnerClassesAttribute, $StructInnerClassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute"
import {$StructLocalVariableTypeTableAttribute, $StructLocalVariableTypeTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTypeTableAttribute"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructExceptionsAttribute, $StructExceptionsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructExceptionsAttribute"
import {$StructModuleAttribute$ExportsEntry, $StructModuleAttribute$ExportsEntry$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute$ExportsEntry"
import {$StructAnnDefaultAttribute, $StructAnnDefaultAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnDefaultAttribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructModuleAttribute extends $StructGeneralAttribute {
 "moduleName": string
 "moduleFlags": integer
 "moduleVersion": string
 "requires": $List<($StructModuleAttribute$RequiresEntry)>
 "exports": $List<($StructModuleAttribute$ExportsEntry)>
 "opens": $List<($StructModuleAttribute$OpensEntry)>
 "uses": $List<(string)>
 "provides": $List<($StructModuleAttribute$ProvidesEntry)>
static readonly "ATTRIBUTE_CODE": $Key<($StructCodeAttribute)>
static readonly "ATTRIBUTE_INNER_CLASSES": $Key<($StructInnerClassesAttribute)>
static readonly "ATTRIBUTE_SIGNATURE": $Key<($StructGenericSignatureAttribute)>
static readonly "ATTRIBUTE_ANNOTATION_DEFAULT": $Key<($StructAnnDefaultAttribute)>
static readonly "ATTRIBUTE_EXCEPTIONS": $Key<($StructExceptionsAttribute)>
static readonly "ATTRIBUTE_ENCLOSING_METHOD": $Key<($StructEnclosingMethodAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TABLE": $Key<($StructLocalVariableTableAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TYPE_TABLE": $Key<($StructLocalVariableTypeTableAttribute)>
static readonly "ATTRIBUTE_CONSTANT_VALUE": $Key<($StructConstantValueAttribute)>
static readonly "ATTRIBUTE_BOOTSTRAP_METHODS": $Key<($StructBootstrapMethodsAttribute)>
static readonly "ATTRIBUTE_SYNTHETIC": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_DEPRECATED": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_LINE_NUMBER_TABLE": $Key<($StructLineNumberTableAttribute)>
static readonly "ATTRIBUTE_METHOD_PARAMETERS": $Key<($StructMethodParametersAttribute)>
static readonly "ATTRIBUTE_MODULE": $Key<($StructModuleAttribute)>
static readonly "ATTRIBUTE_RECORD": $Key<($StructRecordAttribute)>
static readonly "ATTRIBUTE_PERMITTED_SUBCLASSES": $Key<($StructPermittedSubclassesAttribute)>
static readonly "ATTRIBUTE_SOURCE_FILE": $Key<($StructSourceFileAttribute)>
static readonly "ATTRIBUTE_NEST_HOST": $Key<($StructNestHostAttribute)>

constructor()

public "asDescriptor"(): $ModuleDescriptor
public "readRequires"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type): $List<($StructModuleAttribute$RequiresEntry)>
public "initContent"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructModuleAttribute$Type = ($StructModuleAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructModuleAttribute_ = $StructModuleAttribute$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/util/token/$TextToken" {
import {$TextTokenVisitor, $TextTokenVisitor$Type} from "packages/org/jetbrains/java/decompiler/main/extern/$TextTokenVisitor"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $TextToken {

constructor(arg0: integer, arg1: integer, arg2: boolean)

public "getLength"(): integer
public "copy"(): $TextToken
public "shift"(arg0: integer): void
public "visit"(arg0: $TextTokenVisitor$Type): void
public "isDeclaration"(): boolean
public "getStart"(): integer
get "length"(): integer
get "declaration"(): boolean
get "start"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextToken$Type = ($TextToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextToken_ = $TextToken$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/main/rels/$MethodWrapper" {
import {$RootStatement, $RootStatement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$RootStatement"
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$StructClass, $StructClass$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructClass"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MethodDescriptor, $MethodDescriptor$Type} from "packages/org/jetbrains/java/decompiler/struct/gen/$MethodDescriptor"
import {$StructMethod, $StructMethod$Type} from "packages/org/jetbrains/java/decompiler/struct/$StructMethod"
import {$CounterContainer, $CounterContainer$Type} from "packages/org/jetbrains/java/decompiler/main/collectors/$CounterContainer"
import {$DirectGraph, $DirectGraph$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/flow/$DirectGraph"
import {$VarProcessor, $VarProcessor$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarProcessor"
import {$VarVersionPair, $VarVersionPair$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarVersionPair"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MethodWrapper {
readonly "root": $RootStatement
readonly "varproc": $VarProcessor
readonly "methodStruct": $StructMethod
readonly "classStruct": $StructClass
readonly "counter": $CounterContainer
readonly "setOuterVarNames": $Set<(string)>
 "graph": $DirectGraph
 "synthParameters": $List<($VarVersionPair)>
 "decompileError": $Throwable
 "commentLines": $Set<(string)>
 "addErrorComment": boolean

constructor(arg0: $RootStatement$Type, arg1: $VarProcessor$Type, arg2: $StructMethod$Type, arg3: $StructClass$Type, arg4: $CounterContainer$Type)

public "getOrBuildGraph"(): $DirectGraph
public "toString"(): string
public "desc"(): $MethodDescriptor
public "addComment"(arg0: string): void
get "orBuildGraph"(): $DirectGraph
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MethodWrapper$Type = ($MethodWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MethodWrapper_ = $MethodWrapper$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTypeTableAttribute" {
import {$StructMethodParametersAttribute, $StructMethodParametersAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute"
import {$StructNestHostAttribute, $StructNestHostAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructNestHostAttribute"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$Key, $Key$Type} from "packages/org/jetbrains/java/decompiler/util/$Key"
import {$StructAnnotationParameterAttribute, $StructAnnotationParameterAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationParameterAttribute"
import {$StructPermittedSubclassesAttribute, $StructPermittedSubclassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructPermittedSubclassesAttribute"
import {$StructLineNumberTableAttribute, $StructLineNumberTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLineNumberTableAttribute"
import {$StructCodeAttribute, $StructCodeAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructCodeAttribute"
import {$StructEnclosingMethodAttribute, $StructEnclosingMethodAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructEnclosingMethodAttribute"
import {$StructSourceFileAttribute, $StructSourceFileAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructSourceFileAttribute"
import {$StructBootstrapMethodsAttribute, $StructBootstrapMethodsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructBootstrapMethodsAttribute"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$StructGenericSignatureAttribute, $StructGenericSignatureAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGenericSignatureAttribute"
import {$StructLocalVariableTableAttribute, $StructLocalVariableTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute"
import {$StructTypeAnnotationAttribute, $StructTypeAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructTypeAnnotationAttribute"
import {$StructConstantValueAttribute, $StructConstantValueAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructConstantValueAttribute"
import {$StructAnnotationAttribute, $StructAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationAttribute"
import {$StructRecordAttribute, $StructRecordAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructRecordAttribute"
import {$StructModuleAttribute, $StructModuleAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute"
import {$StructGeneralAttribute, $StructGeneralAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGeneralAttribute"
import {$StructInnerClassesAttribute, $StructInnerClassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructExceptionsAttribute, $StructExceptionsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructExceptionsAttribute"
import {$StructAnnDefaultAttribute, $StructAnnDefaultAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnDefaultAttribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructLocalVariableTypeTableAttribute extends $StructGeneralAttribute {
static readonly "ATTRIBUTE_CODE": $Key<($StructCodeAttribute)>
static readonly "ATTRIBUTE_INNER_CLASSES": $Key<($StructInnerClassesAttribute)>
static readonly "ATTRIBUTE_SIGNATURE": $Key<($StructGenericSignatureAttribute)>
static readonly "ATTRIBUTE_ANNOTATION_DEFAULT": $Key<($StructAnnDefaultAttribute)>
static readonly "ATTRIBUTE_EXCEPTIONS": $Key<($StructExceptionsAttribute)>
static readonly "ATTRIBUTE_ENCLOSING_METHOD": $Key<($StructEnclosingMethodAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TABLE": $Key<($StructLocalVariableTableAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TYPE_TABLE": $Key<($StructLocalVariableTypeTableAttribute)>
static readonly "ATTRIBUTE_CONSTANT_VALUE": $Key<($StructConstantValueAttribute)>
static readonly "ATTRIBUTE_BOOTSTRAP_METHODS": $Key<($StructBootstrapMethodsAttribute)>
static readonly "ATTRIBUTE_SYNTHETIC": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_DEPRECATED": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_LINE_NUMBER_TABLE": $Key<($StructLineNumberTableAttribute)>
static readonly "ATTRIBUTE_METHOD_PARAMETERS": $Key<($StructMethodParametersAttribute)>
static readonly "ATTRIBUTE_MODULE": $Key<($StructModuleAttribute)>
static readonly "ATTRIBUTE_RECORD": $Key<($StructRecordAttribute)>
static readonly "ATTRIBUTE_PERMITTED_SUBCLASSES": $Key<($StructPermittedSubclassesAttribute)>
static readonly "ATTRIBUTE_SOURCE_FILE": $Key<($StructSourceFileAttribute)>
static readonly "ATTRIBUTE_NEST_HOST": $Key<($StructNestHostAttribute)>

constructor()

public "add"(arg0: $StructLocalVariableTypeTableAttribute$Type): void
public "getSignature"(arg0: integer, arg1: integer): string
public "initContent"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructLocalVariableTypeTableAttribute$Type = ($StructLocalVariableTypeTableAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructLocalVariableTypeTableAttribute_ = $StructLocalVariableTypeTableAttribute$Type;
}}
declare module "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute" {
import {$StructMethodParametersAttribute, $StructMethodParametersAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructMethodParametersAttribute"
import {$StructNestHostAttribute, $StructNestHostAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructNestHostAttribute"
import {$DataInputFullStream, $DataInputFullStream$Type} from "packages/org/jetbrains/java/decompiler/util/$DataInputFullStream"
import {$Key, $Key$Type} from "packages/org/jetbrains/java/decompiler/util/$Key"
import {$StructAnnotationParameterAttribute, $StructAnnotationParameterAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationParameterAttribute"
import {$StructPermittedSubclassesAttribute, $StructPermittedSubclassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructPermittedSubclassesAttribute"
import {$StructLineNumberTableAttribute, $StructLineNumberTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLineNumberTableAttribute"
import {$StructCodeAttribute, $StructCodeAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructCodeAttribute"
import {$StructEnclosingMethodAttribute, $StructEnclosingMethodAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructEnclosingMethodAttribute"
import {$StructSourceFileAttribute, $StructSourceFileAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructSourceFileAttribute"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$StructBootstrapMethodsAttribute, $StructBootstrapMethodsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructBootstrapMethodsAttribute"
import {$BytecodeVersion, $BytecodeVersion$Type} from "packages/org/jetbrains/java/decompiler/code/$BytecodeVersion"
import {$VarVersionPair, $VarVersionPair$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/vars/$VarVersionPair"
import {$StructGenericSignatureAttribute, $StructGenericSignatureAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGenericSignatureAttribute"
import {$StructTypeAnnotationAttribute, $StructTypeAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructTypeAnnotationAttribute"
import {$StructConstantValueAttribute, $StructConstantValueAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructConstantValueAttribute"
import {$StructAnnotationAttribute, $StructAnnotationAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnotationAttribute"
import {$StructRecordAttribute, $StructRecordAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructRecordAttribute"
import {$StructModuleAttribute, $StructModuleAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructModuleAttribute"
import {$Statement, $Statement$Type} from "packages/org/jetbrains/java/decompiler/modules/decompiler/stats/$Statement"
import {$StructGeneralAttribute, $StructGeneralAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructGeneralAttribute"
import {$StructInnerClassesAttribute, $StructInnerClassesAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructInnerClassesAttribute"
import {$StructLocalVariableTypeTableAttribute, $StructLocalVariableTypeTableAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTypeTableAttribute"
import {$ConstantPool, $ConstantPool$Type} from "packages/org/jetbrains/java/decompiler/struct/consts/$ConstantPool"
import {$StructLocalVariableTableAttribute$LocalVariable, $StructLocalVariableTableAttribute$LocalVariable$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructLocalVariableTableAttribute$LocalVariable"
import {$StructExceptionsAttribute, $StructExceptionsAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructExceptionsAttribute"
import {$StructAnnDefaultAttribute, $StructAnnDefaultAttribute$Type} from "packages/org/jetbrains/java/decompiler/struct/attr/$StructAnnDefaultAttribute"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StructLocalVariableTableAttribute extends $StructGeneralAttribute {
static readonly "ATTRIBUTE_CODE": $Key<($StructCodeAttribute)>
static readonly "ATTRIBUTE_INNER_CLASSES": $Key<($StructInnerClassesAttribute)>
static readonly "ATTRIBUTE_SIGNATURE": $Key<($StructGenericSignatureAttribute)>
static readonly "ATTRIBUTE_ANNOTATION_DEFAULT": $Key<($StructAnnDefaultAttribute)>
static readonly "ATTRIBUTE_EXCEPTIONS": $Key<($StructExceptionsAttribute)>
static readonly "ATTRIBUTE_ENCLOSING_METHOD": $Key<($StructEnclosingMethodAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS": $Key<($StructAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS": $Key<($StructAnnotationParameterAttribute)>
static readonly "ATTRIBUTE_RUNTIME_VISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS": $Key<($StructTypeAnnotationAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TABLE": $Key<($StructLocalVariableTableAttribute)>
static readonly "ATTRIBUTE_LOCAL_VARIABLE_TYPE_TABLE": $Key<($StructLocalVariableTypeTableAttribute)>
static readonly "ATTRIBUTE_CONSTANT_VALUE": $Key<($StructConstantValueAttribute)>
static readonly "ATTRIBUTE_BOOTSTRAP_METHODS": $Key<($StructBootstrapMethodsAttribute)>
static readonly "ATTRIBUTE_SYNTHETIC": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_DEPRECATED": $Key<($StructGeneralAttribute)>
static readonly "ATTRIBUTE_LINE_NUMBER_TABLE": $Key<($StructLineNumberTableAttribute)>
static readonly "ATTRIBUTE_METHOD_PARAMETERS": $Key<($StructMethodParametersAttribute)>
static readonly "ATTRIBUTE_MODULE": $Key<($StructModuleAttribute)>
static readonly "ATTRIBUTE_RECORD": $Key<($StructRecordAttribute)>
static readonly "ATTRIBUTE_PERMITTED_SUBCLASSES": $Key<($StructPermittedSubclassesAttribute)>
static readonly "ATTRIBUTE_SOURCE_FILE": $Key<($StructSourceFileAttribute)>
static readonly "ATTRIBUTE_NEST_HOST": $Key<($StructNestHostAttribute)>

constructor()

public "getName"(arg0: integer, arg1: integer): string
public "add"(arg0: $StructLocalVariableTableAttribute$Type): void
public "getDescriptor"(arg0: integer, arg1: integer): string
public "getRange"(arg0: integer, arg1: integer): $Stream<($StructLocalVariableTableAttribute$LocalVariable)>
public "getVariables"(): $Stream<($StructLocalVariableTableAttribute$LocalVariable)>
public "matchingVars"(arg0: integer, arg1: integer): $Stream<($StructLocalVariableTableAttribute$LocalVariable)>
public "matchingVars"(arg0: integer): $Stream<($StructLocalVariableTableAttribute$LocalVariable)>
public "matchingVars"(arg0: $Statement$Type): $Stream<($StructLocalVariableTableAttribute$LocalVariable)>
public "getMapNames"(): $Map<($VarVersionPair), (string)>
public "initContent"(arg0: $DataInputFullStream$Type, arg1: $ConstantPool$Type, arg2: $BytecodeVersion$Type): void
public "containsName"(arg0: string): boolean
public "mergeSignatures"(arg0: $StructLocalVariableTypeTableAttribute$Type): void
get "variables"(): $Stream<($StructLocalVariableTableAttribute$LocalVariable)>
get "mapNames"(): $Map<($VarVersionPair), (string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructLocalVariableTableAttribute$Type = ($StructLocalVariableTableAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructLocalVariableTableAttribute_ = $StructLocalVariableTableAttribute$Type;
}}
