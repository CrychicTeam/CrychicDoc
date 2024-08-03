declare module "packages/org/objectweb/asm/$FieldVisitor" {
import {$AnnotationVisitor, $AnnotationVisitor$Type} from "packages/org/objectweb/asm/$AnnotationVisitor"
import {$TypePath, $TypePath$Type} from "packages/org/objectweb/asm/$TypePath"
import {$Attribute, $Attribute$Type} from "packages/org/objectweb/asm/$Attribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $FieldVisitor {


public "visitAnnotation"(arg0: string, arg1: boolean): $AnnotationVisitor
public "visitEnd"(): void
public "visitTypeAnnotation"(arg0: integer, arg1: $TypePath$Type, arg2: string, arg3: boolean): $AnnotationVisitor
public "visitAttribute"(arg0: $Attribute$Type): void
public "getDelegate"(): $FieldVisitor
get "delegate"(): $FieldVisitor
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FieldVisitor$Type = ($FieldVisitor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FieldVisitor_ = $FieldVisitor$Type;
}}
declare module "packages/org/objectweb/asm/$ClassVisitor" {
import {$FieldVisitor, $FieldVisitor$Type} from "packages/org/objectweb/asm/$FieldVisitor"
import {$AnnotationVisitor, $AnnotationVisitor$Type} from "packages/org/objectweb/asm/$AnnotationVisitor"
import {$TypePath, $TypePath$Type} from "packages/org/objectweb/asm/$TypePath"
import {$MethodVisitor, $MethodVisitor$Type} from "packages/org/objectweb/asm/$MethodVisitor"
import {$ModuleVisitor, $ModuleVisitor$Type} from "packages/org/objectweb/asm/$ModuleVisitor"
import {$RecordComponentVisitor, $RecordComponentVisitor$Type} from "packages/org/objectweb/asm/$RecordComponentVisitor"
import {$Attribute, $Attribute$Type} from "packages/org/objectweb/asm/$Attribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ClassVisitor {


public "visit"(arg0: integer, arg1: integer, arg2: string, arg3: string, arg4: string, arg5: (string)[]): void
public "visitSource"(arg0: string, arg1: string): void
public "visitField"(arg0: integer, arg1: string, arg2: string, arg3: string, arg4: any): $FieldVisitor
public "visitAnnotation"(arg0: string, arg1: boolean): $AnnotationVisitor
public "visitEnd"(): void
public "visitMethod"(arg0: integer, arg1: string, arg2: string, arg3: string, arg4: (string)[]): $MethodVisitor
public "visitTypeAnnotation"(arg0: integer, arg1: $TypePath$Type, arg2: string, arg3: boolean): $AnnotationVisitor
public "visitAttribute"(arg0: $Attribute$Type): void
public "visitNestHost"(arg0: string): void
public "visitOuterClass"(arg0: string, arg1: string, arg2: string): void
public "visitNestMember"(arg0: string): void
public "visitInnerClass"(arg0: string, arg1: string, arg2: string, arg3: integer): void
public "visitModule"(arg0: string, arg1: integer, arg2: string): $ModuleVisitor
public "visitRecordComponent"(arg0: string, arg1: string, arg2: string): $RecordComponentVisitor
public "getDelegate"(): $ClassVisitor
public "visitPermittedSubclass"(arg0: string): void
get "delegate"(): $ClassVisitor
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassVisitor$Type = ($ClassVisitor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassVisitor_ = $ClassVisitor$Type;
}}
declare module "packages/org/objectweb/asm/$TypePath" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $TypePath {
static readonly "ARRAY_ELEMENT": integer
static readonly "INNER_TYPE": integer
static readonly "WILDCARD_BOUND": integer
static readonly "TYPE_ARGUMENT": integer


public "toString"(): string
public "getLength"(): integer
public static "fromString"(arg0: string): $TypePath
public "getStep"(arg0: integer): integer
public "getStepArgument"(arg0: integer): integer
get "length"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypePath$Type = ($TypePath);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypePath_ = $TypePath$Type;
}}
declare module "packages/org/objectweb/asm/tree/$AnnotationNode" {
import {$AnnotationVisitor, $AnnotationVisitor$Type} from "packages/org/objectweb/asm/$AnnotationVisitor"
import {$List, $List$Type} from "packages/java/util/$List"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $AnnotationNode extends $AnnotationVisitor {
 "desc": string
 "values": $List<(any)>

constructor(arg0: integer, arg1: string)
constructor(arg0: string)

public "accept"(arg0: $AnnotationVisitor$Type): void
public "check"(arg0: integer): void
public "visit"(arg0: string, arg1: any): void
public "visitAnnotation"(arg0: string, arg1: string): $AnnotationVisitor
public "visitEnd"(): void
public "visitEnum"(arg0: string, arg1: string, arg2: string): void
public "visitArray"(arg0: string): $AnnotationVisitor
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnnotationNode$Type = ($AnnotationNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnnotationNode_ = $AnnotationNode$Type;
}}
declare module "packages/org/objectweb/asm/$Type" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Method, $Method$Type} from "packages/java/lang/reflect/$Method"
import {$Constructor, $Constructor$Type} from "packages/java/lang/reflect/$Constructor"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Type {
static readonly "VOID": integer
static readonly "BOOLEAN": integer
static readonly "CHAR": integer
static readonly "BYTE": integer
static readonly "SHORT": integer
static readonly "INT": integer
static readonly "FLOAT": integer
static readonly "LONG": integer
static readonly "DOUBLE": integer
static readonly "ARRAY": integer
static readonly "OBJECT": integer
static readonly "METHOD": integer
static readonly "VOID_TYPE": $Type
static readonly "BOOLEAN_TYPE": $Type
static readonly "CHAR_TYPE": $Type
static readonly "BYTE_TYPE": $Type
static readonly "SHORT_TYPE": $Type
static readonly "INT_TYPE": $Type
static readonly "FLOAT_TYPE": $Type
static readonly "LONG_TYPE": $Type
static readonly "DOUBLE_TYPE": $Type


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getDescriptor"(): string
public static "getDescriptor"(arg0: $Class$Type<(any)>): string
public "getReturnType"(): $Type
public static "getReturnType"(arg0: string): $Type
public static "getReturnType"(arg0: $Method$Type): $Type
public static "getType"(arg0: $Class$Type<(any)>): $Type
public static "getType"(arg0: string): $Type
public static "getType"(arg0: $Method$Type): $Type
public static "getType"(arg0: $Constructor$Type<(any)>): $Type
public "getSize"(): integer
public static "getMethodType"(arg0: $Type$Type, ...arg1: ($Type$Type)[]): $Type
public static "getMethodType"(arg0: string): $Type
public static "getMethodDescriptor"(arg0: $Type$Type, ...arg1: ($Type$Type)[]): string
public static "getMethodDescriptor"(arg0: $Method$Type): string
public "getClassName"(): string
public "getInternalName"(): string
public static "getInternalName"(arg0: $Class$Type<(any)>): string
public "getSort"(): integer
public static "getObjectType"(arg0: string): $Type
public static "getArgumentTypes"(arg0: string): ($Type)[]
public static "getArgumentTypes"(arg0: $Method$Type): ($Type)[]
public "getArgumentTypes"(): ($Type)[]
public static "getArgumentsAndReturnSizes"(arg0: string): integer
public "getArgumentsAndReturnSizes"(): integer
public static "getConstructorDescriptor"(arg0: $Constructor$Type<(any)>): string
public "getDimensions"(): integer
public "getElementType"(): $Type
public "getOpcode"(arg0: integer): integer
get "descriptor"(): string
get "returnType"(): $Type
get "size"(): integer
get "className"(): string
get "internalName"(): string
get "sort"(): integer
get "argumentTypes"(): ($Type)[]
get "argumentsAndReturnSizes"(): integer
get "dimensions"(): integer
get "elementType"(): $Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Type$Type = ($Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Type_ = $Type$Type;
}}
declare module "packages/org/objectweb/asm/tree/$TypeAnnotationNode" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$TypePath, $TypePath$Type} from "packages/org/objectweb/asm/$TypePath"
import {$List, $List$Type} from "packages/java/util/$List"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $TypeAnnotationNode extends $AnnotationNode {
 "typeRef": integer
 "typePath": $TypePath
 "desc": string
 "values": $List<(any)>

constructor(arg0: integer, arg1: $TypePath$Type, arg2: string)
constructor(arg0: integer, arg1: integer, arg2: $TypePath$Type, arg3: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeAnnotationNode$Type = ($TypeAnnotationNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeAnnotationNode_ = $TypeAnnotationNode$Type;
}}
declare module "packages/org/objectweb/asm/$Label" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Label {
 "info": any

constructor()

public "toString"(): string
public "getOffset"(): integer
get "offset"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Label$Type = ($Label);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Label_ = $Label$Type;
}}
declare module "packages/org/objectweb/asm/tree/$ModuleExportNode" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ModuleVisitor, $ModuleVisitor$Type} from "packages/org/objectweb/asm/$ModuleVisitor"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ModuleExportNode {
 "packaze": string
 "access": integer
 "modules": $List<(string)>

constructor(arg0: string, arg1: integer, arg2: $List$Type<(string)>)

public "accept"(arg0: $ModuleVisitor$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleExportNode$Type = ($ModuleExportNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleExportNode_ = $ModuleExportNode$Type;
}}
declare module "packages/org/objectweb/asm/tree/$ModuleRequireNode" {
import {$ModuleVisitor, $ModuleVisitor$Type} from "packages/org/objectweb/asm/$ModuleVisitor"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ModuleRequireNode {
 "module": string
 "access": integer
 "version": string

constructor(arg0: string, arg1: integer, arg2: string)

public "accept"(arg0: $ModuleVisitor$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleRequireNode$Type = ($ModuleRequireNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleRequireNode_ = $ModuleRequireNode$Type;
}}
declare module "packages/org/objectweb/asm/tree/$AbstractInsnNode" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$LabelNode, $LabelNode$Type} from "packages/org/objectweb/asm/tree/$LabelNode"
import {$MethodVisitor, $MethodVisitor$Type} from "packages/org/objectweb/asm/$MethodVisitor"
import {$TypeAnnotationNode, $TypeAnnotationNode$Type} from "packages/org/objectweb/asm/tree/$TypeAnnotationNode"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $AbstractInsnNode {
static readonly "INSN": integer
static readonly "INT_INSN": integer
static readonly "VAR_INSN": integer
static readonly "TYPE_INSN": integer
static readonly "FIELD_INSN": integer
static readonly "METHOD_INSN": integer
static readonly "INVOKE_DYNAMIC_INSN": integer
static readonly "JUMP_INSN": integer
static readonly "LABEL": integer
static readonly "LDC_INSN": integer
static readonly "IINC_INSN": integer
static readonly "TABLESWITCH_INSN": integer
static readonly "LOOKUPSWITCH_INSN": integer
static readonly "MULTIANEWARRAY_INSN": integer
static readonly "FRAME": integer
static readonly "LINE": integer
 "visibleTypeAnnotations": $List<($TypeAnnotationNode)>
 "invisibleTypeAnnotations": $List<($TypeAnnotationNode)>


public "clone"(arg0: $Map$Type<($LabelNode$Type), ($LabelNode$Type)>): $AbstractInsnNode
public "accept"(arg0: $MethodVisitor$Type): void
public "getType"(): integer
public "getOpcode"(): integer
public "getNext"(): $AbstractInsnNode
public "getPrevious"(): $AbstractInsnNode
get "type"(): integer
get "opcode"(): integer
get "next"(): $AbstractInsnNode
get "previous"(): $AbstractInsnNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractInsnNode$Type = ($AbstractInsnNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractInsnNode_ = $AbstractInsnNode$Type;
}}
declare module "packages/org/objectweb/asm/tree/$ModuleOpenNode" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ModuleVisitor, $ModuleVisitor$Type} from "packages/org/objectweb/asm/$ModuleVisitor"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ModuleOpenNode {
 "packaze": string
 "access": integer
 "modules": $List<(string)>

constructor(arg0: string, arg1: integer, arg2: $List$Type<(string)>)

public "accept"(arg0: $ModuleVisitor$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleOpenNode$Type = ($ModuleOpenNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleOpenNode_ = $ModuleOpenNode$Type;
}}
declare module "packages/org/objectweb/asm/signature/$SignatureVisitor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $SignatureVisitor {
static readonly "EXTENDS": character
static readonly "SUPER": character
static readonly "INSTANCEOF": character


public "visitEnd"(): void
public "visitInterface"(): $SignatureVisitor
public "visitParameterType"(): $SignatureVisitor
public "visitExceptionType"(): $SignatureVisitor
public "visitClassBound"(): $SignatureVisitor
public "visitBaseType"(arg0: character): void
public "visitSuperclass"(): $SignatureVisitor
public "visitTypeVariable"(arg0: string): void
public "visitArrayType"(): $SignatureVisitor
public "visitTypeArgument"(arg0: character): $SignatureVisitor
public "visitTypeArgument"(): void
public "visitReturnType"(): $SignatureVisitor
public "visitClassType"(arg0: string): void
public "visitInnerClassType"(arg0: string): void
public "visitInterfaceBound"(): $SignatureVisitor
public "visitFormalTypeParameter"(arg0: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SignatureVisitor$Type = ($SignatureVisitor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SignatureVisitor_ = $SignatureVisitor$Type;
}}
declare module "packages/org/objectweb/asm/tree/$ModuleNode" {
import {$ClassVisitor, $ClassVisitor$Type} from "packages/org/objectweb/asm/$ClassVisitor"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ModuleVisitor, $ModuleVisitor$Type} from "packages/org/objectweb/asm/$ModuleVisitor"
import {$ModuleProvideNode, $ModuleProvideNode$Type} from "packages/org/objectweb/asm/tree/$ModuleProvideNode"
import {$ModuleRequireNode, $ModuleRequireNode$Type} from "packages/org/objectweb/asm/tree/$ModuleRequireNode"
import {$ModuleOpenNode, $ModuleOpenNode$Type} from "packages/org/objectweb/asm/tree/$ModuleOpenNode"
import {$ModuleExportNode, $ModuleExportNode$Type} from "packages/org/objectweb/asm/tree/$ModuleExportNode"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ModuleNode extends $ModuleVisitor {
 "name": string
 "access": integer
 "version": string
 "mainClass": string
 "packages": $List<(string)>
 "requires": $List<($ModuleRequireNode)>
 "exports": $List<($ModuleExportNode)>
 "opens": $List<($ModuleOpenNode)>
 "uses": $List<(string)>
 "provides": $List<($ModuleProvideNode)>

constructor(arg0: string, arg1: integer, arg2: string)
constructor(arg0: integer, arg1: string, arg2: integer, arg3: string, arg4: $List$Type<($ModuleRequireNode$Type)>, arg5: $List$Type<($ModuleExportNode$Type)>, arg6: $List$Type<($ModuleOpenNode$Type)>, arg7: $List$Type<(string)>, arg8: $List$Type<($ModuleProvideNode$Type)>)

public "accept"(arg0: $ClassVisitor$Type): void
public "visitEnd"(): void
public "visitMainClass"(arg0: string): void
public "visitPackage"(arg0: string): void
public "visitRequire"(arg0: string, arg1: integer, arg2: string): void
public "visitExport"(arg0: string, arg1: integer, ...arg2: (string)[]): void
public "visitOpen"(arg0: string, arg1: integer, ...arg2: (string)[]): void
public "visitUse"(arg0: string): void
public "visitProvide"(arg0: string, ...arg1: (string)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleNode$Type = ($ModuleNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleNode_ = $ModuleNode$Type;
}}
declare module "packages/org/objectweb/asm/tree/$LocalVariableNode" {
import {$LabelNode, $LabelNode$Type} from "packages/org/objectweb/asm/tree/$LabelNode"
import {$MethodVisitor, $MethodVisitor$Type} from "packages/org/objectweb/asm/$MethodVisitor"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $LocalVariableNode {
 "name": string
 "desc": string
 "signature": string
 "start": $LabelNode
 "end": $LabelNode
 "index": integer

constructor(arg0: string, arg1: string, arg2: string, arg3: $LabelNode$Type, arg4: $LabelNode$Type, arg5: integer)

public "accept"(arg0: $MethodVisitor$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalVariableNode$Type = ($LocalVariableNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalVariableNode_ = $LocalVariableNode$Type;
}}
declare module "packages/org/objectweb/asm/tree/$ParameterNode" {
import {$MethodVisitor, $MethodVisitor$Type} from "packages/org/objectweb/asm/$MethodVisitor"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ParameterNode {
 "name": string
 "access": integer

constructor(arg0: string, arg1: integer)

public "accept"(arg0: $MethodVisitor$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParameterNode$Type = ($ParameterNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParameterNode_ = $ParameterNode$Type;
}}
declare module "packages/org/objectweb/asm/tree/$InsnList" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$MethodVisitor, $MethodVisitor$Type} from "packages/org/objectweb/asm/$MethodVisitor"
import {$ListIterator, $ListIterator$Type} from "packages/java/util/$ListIterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$AbstractInsnNode, $AbstractInsnNode$Type} from "packages/org/objectweb/asm/tree/$AbstractInsnNode"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $InsnList implements $Iterable<($AbstractInsnNode)> {

constructor()

public "add"(arg0: $InsnList$Type): void
public "add"(arg0: $AbstractInsnNode$Type): void
public "remove"(arg0: $AbstractInsnNode$Type): void
public "get"(arg0: integer): $AbstractInsnNode
public "indexOf"(arg0: $AbstractInsnNode$Type): integer
public "insert"(arg0: $AbstractInsnNode$Type, arg1: $InsnList$Type): void
public "insert"(arg0: $InsnList$Type): void
public "insert"(arg0: $AbstractInsnNode$Type, arg1: $AbstractInsnNode$Type): void
public "insert"(arg0: $AbstractInsnNode$Type): void
public "clear"(): void
public "size"(): integer
public "toArray"(): ($AbstractInsnNode)[]
public "iterator"(arg0: integer): $ListIterator<($AbstractInsnNode)>
public "contains"(arg0: $AbstractInsnNode$Type): boolean
public "set"(arg0: $AbstractInsnNode$Type, arg1: $AbstractInsnNode$Type): void
public "accept"(arg0: $MethodVisitor$Type): void
public "getFirst"(): $AbstractInsnNode
public "getLast"(): $AbstractInsnNode
public "resetLabels"(): void
public "insertBefore"(arg0: $AbstractInsnNode$Type, arg1: $AbstractInsnNode$Type): void
public "insertBefore"(arg0: $AbstractInsnNode$Type, arg1: $InsnList$Type): void
public "spliterator"(): $Spliterator<($AbstractInsnNode)>
public "forEach"(arg0: $Consumer$Type<(any)>): void
[Symbol.iterator](): IterableIterator<$AbstractInsnNode>;
get "first"(): $AbstractInsnNode
get "last"(): $AbstractInsnNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InsnList$Type = ($InsnList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InsnList_ = $InsnList$Type;
}}
declare module "packages/org/objectweb/asm/tree/$ClassNode" {
import {$ClassVisitor, $ClassVisitor$Type} from "packages/org/objectweb/asm/$ClassVisitor"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$FieldVisitor, $FieldVisitor$Type} from "packages/org/objectweb/asm/$FieldVisitor"
import {$AnnotationVisitor, $AnnotationVisitor$Type} from "packages/org/objectweb/asm/$AnnotationVisitor"
import {$InnerClassNode, $InnerClassNode$Type} from "packages/org/objectweb/asm/tree/$InnerClassNode"
import {$TypeAnnotationNode, $TypeAnnotationNode$Type} from "packages/org/objectweb/asm/tree/$TypeAnnotationNode"
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$TypePath, $TypePath$Type} from "packages/org/objectweb/asm/$TypePath"
import {$ModuleNode, $ModuleNode$Type} from "packages/org/objectweb/asm/tree/$ModuleNode"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MethodVisitor, $MethodVisitor$Type} from "packages/org/objectweb/asm/$MethodVisitor"
import {$ModuleVisitor, $ModuleVisitor$Type} from "packages/org/objectweb/asm/$ModuleVisitor"
import {$RecordComponentNode, $RecordComponentNode$Type} from "packages/org/objectweb/asm/tree/$RecordComponentNode"
import {$RecordComponentVisitor, $RecordComponentVisitor$Type} from "packages/org/objectweb/asm/$RecordComponentVisitor"
import {$Attribute, $Attribute$Type} from "packages/org/objectweb/asm/$Attribute"
import {$FieldNode, $FieldNode$Type} from "packages/org/objectweb/asm/tree/$FieldNode"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ClassNode extends $ClassVisitor {
 "version": integer
 "access": integer
 "name": string
 "signature": string
 "superName": string
 "interfaces": $List<(string)>
 "sourceFile": string
 "sourceDebug": string
 "module": $ModuleNode
 "outerClass": string
 "outerMethod": string
 "outerMethodDesc": string
 "visibleAnnotations": $List<($AnnotationNode)>
 "invisibleAnnotations": $List<($AnnotationNode)>
 "visibleTypeAnnotations": $List<($TypeAnnotationNode)>
 "invisibleTypeAnnotations": $List<($TypeAnnotationNode)>
 "attrs": $List<($Attribute)>
 "innerClasses": $List<($InnerClassNode)>
 "nestHostClass": string
 "nestMembers": $List<(string)>
 "permittedSubclasses": $List<(string)>
 "recordComponents": $List<($RecordComponentNode)>
 "fields": $List<($FieldNode)>
 "methods": $List<($MethodNode)>

constructor()
constructor(arg0: integer)

public "accept"(arg0: $ClassVisitor$Type): void
public "check"(arg0: integer): void
public "visit"(arg0: integer, arg1: integer, arg2: string, arg3: string, arg4: string, arg5: (string)[]): void
public "visitSource"(arg0: string, arg1: string): void
public "visitField"(arg0: integer, arg1: string, arg2: string, arg3: string, arg4: any): $FieldVisitor
public "visitAnnotation"(arg0: string, arg1: boolean): $AnnotationVisitor
public "visitEnd"(): void
public "visitMethod"(arg0: integer, arg1: string, arg2: string, arg3: string, arg4: (string)[]): $MethodVisitor
public "visitTypeAnnotation"(arg0: integer, arg1: $TypePath$Type, arg2: string, arg3: boolean): $AnnotationVisitor
public "visitAttribute"(arg0: $Attribute$Type): void
public "visitNestHost"(arg0: string): void
public "visitOuterClass"(arg0: string, arg1: string, arg2: string): void
public "visitNestMember"(arg0: string): void
public "visitInnerClass"(arg0: string, arg1: string, arg2: string, arg3: integer): void
public "visitModule"(arg0: string, arg1: integer, arg2: string): $ModuleVisitor
public "visitRecordComponent"(arg0: string, arg1: string, arg2: string): $RecordComponentVisitor
public "visitPermittedSubclass"(arg0: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassNode$Type = ($ClassNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassNode_ = $ClassNode$Type;
}}
declare module "packages/org/objectweb/asm/$RecordComponentVisitor" {
import {$AnnotationVisitor, $AnnotationVisitor$Type} from "packages/org/objectweb/asm/$AnnotationVisitor"
import {$TypePath, $TypePath$Type} from "packages/org/objectweb/asm/$TypePath"
import {$Attribute, $Attribute$Type} from "packages/org/objectweb/asm/$Attribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $RecordComponentVisitor {


public "visitAnnotation"(arg0: string, arg1: boolean): $AnnotationVisitor
public "visitEnd"(): void
public "visitTypeAnnotation"(arg0: integer, arg1: $TypePath$Type, arg2: string, arg3: boolean): $AnnotationVisitor
public "visitAttribute"(arg0: $Attribute$Type): void
public "getDelegate"(): $RecordComponentVisitor
get "delegate"(): $RecordComponentVisitor
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordComponentVisitor$Type = ($RecordComponentVisitor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordComponentVisitor_ = $RecordComponentVisitor$Type;
}}
declare module "packages/org/objectweb/asm/$AnnotationVisitor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $AnnotationVisitor {


public "visit"(arg0: string, arg1: any): void
public "visitAnnotation"(arg0: string, arg1: string): $AnnotationVisitor
public "visitEnd"(): void
public "visitEnum"(arg0: string, arg1: string, arg2: string): void
public "visitArray"(arg0: string): $AnnotationVisitor
public "getDelegate"(): $AnnotationVisitor
get "delegate"(): $AnnotationVisitor
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnnotationVisitor$Type = ($AnnotationVisitor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnnotationVisitor_ = $AnnotationVisitor$Type;
}}
declare module "packages/org/objectweb/asm/tree/$FieldInsnNode" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$LabelNode, $LabelNode$Type} from "packages/org/objectweb/asm/tree/$LabelNode"
import {$MethodVisitor, $MethodVisitor$Type} from "packages/org/objectweb/asm/$MethodVisitor"
import {$TypeAnnotationNode, $TypeAnnotationNode$Type} from "packages/org/objectweb/asm/tree/$TypeAnnotationNode"
import {$AbstractInsnNode, $AbstractInsnNode$Type} from "packages/org/objectweb/asm/tree/$AbstractInsnNode"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $FieldInsnNode extends $AbstractInsnNode {
 "owner": string
 "name": string
 "desc": string
static readonly "INSN": integer
static readonly "INT_INSN": integer
static readonly "VAR_INSN": integer
static readonly "TYPE_INSN": integer
static readonly "FIELD_INSN": integer
static readonly "METHOD_INSN": integer
static readonly "INVOKE_DYNAMIC_INSN": integer
static readonly "JUMP_INSN": integer
static readonly "LABEL": integer
static readonly "LDC_INSN": integer
static readonly "IINC_INSN": integer
static readonly "TABLESWITCH_INSN": integer
static readonly "LOOKUPSWITCH_INSN": integer
static readonly "MULTIANEWARRAY_INSN": integer
static readonly "FRAME": integer
static readonly "LINE": integer
 "visibleTypeAnnotations": $List<($TypeAnnotationNode)>
 "invisibleTypeAnnotations": $List<($TypeAnnotationNode)>

constructor(arg0: integer, arg1: string, arg2: string, arg3: string)

public "clone"(arg0: $Map$Type<($LabelNode$Type), ($LabelNode$Type)>): $AbstractInsnNode
public "accept"(arg0: $MethodVisitor$Type): void
public "getType"(): integer
public "setOpcode"(arg0: integer): void
get "type"(): integer
set "opcode"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FieldInsnNode$Type = ($FieldInsnNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FieldInsnNode_ = $FieldInsnNode$Type;
}}
declare module "packages/org/objectweb/asm/tree/$MethodNode" {
import {$ClassVisitor, $ClassVisitor$Type} from "packages/org/objectweb/asm/$ClassVisitor"
import {$AnnotationVisitor, $AnnotationVisitor$Type} from "packages/org/objectweb/asm/$AnnotationVisitor"
import {$LocalVariableAnnotationNode, $LocalVariableAnnotationNode$Type} from "packages/org/objectweb/asm/tree/$LocalVariableAnnotationNode"
import {$LocalVariableNode, $LocalVariableNode$Type} from "packages/org/objectweb/asm/tree/$LocalVariableNode"
import {$InsnList, $InsnList$Type} from "packages/org/objectweb/asm/tree/$InsnList"
import {$Handle, $Handle$Type} from "packages/org/objectweb/asm/$Handle"
import {$TypeAnnotationNode, $TypeAnnotationNode$Type} from "packages/org/objectweb/asm/tree/$TypeAnnotationNode"
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$ParameterNode, $ParameterNode$Type} from "packages/org/objectweb/asm/tree/$ParameterNode"
import {$TryCatchBlockNode, $TryCatchBlockNode$Type} from "packages/org/objectweb/asm/tree/$TryCatchBlockNode"
import {$TypePath, $TypePath$Type} from "packages/org/objectweb/asm/$TypePath"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Label, $Label$Type} from "packages/org/objectweb/asm/$Label"
import {$MethodVisitor, $MethodVisitor$Type} from "packages/org/objectweb/asm/$MethodVisitor"
import {$Attribute, $Attribute$Type} from "packages/org/objectweb/asm/$Attribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MethodNode extends $MethodVisitor {
 "access": integer
 "name": string
 "desc": string
 "signature": string
 "exceptions": $List<(string)>
 "parameters": $List<($ParameterNode)>
 "visibleAnnotations": $List<($AnnotationNode)>
 "invisibleAnnotations": $List<($AnnotationNode)>
 "visibleTypeAnnotations": $List<($TypeAnnotationNode)>
 "invisibleTypeAnnotations": $List<($TypeAnnotationNode)>
 "attrs": $List<($Attribute)>
 "annotationDefault": any
 "visibleAnnotableParameterCount": integer
 "visibleParameterAnnotations": ($List<($AnnotationNode)>)[]
 "invisibleAnnotableParameterCount": integer
 "invisibleParameterAnnotations": ($List<($AnnotationNode)>)[]
 "instructions": $InsnList
 "tryCatchBlocks": $List<($TryCatchBlockNode)>
 "maxStack": integer
 "maxLocals": integer
 "localVariables": $List<($LocalVariableNode)>
 "visibleLocalVariableAnnotations": $List<($LocalVariableAnnotationNode)>
 "invisibleLocalVariableAnnotations": $List<($LocalVariableAnnotationNode)>

constructor()
constructor(arg0: integer, arg1: integer, arg2: string, arg3: string, arg4: string, arg5: (string)[])
constructor(arg0: integer)
constructor(arg0: integer, arg1: string, arg2: string, arg3: string, arg4: (string)[])

public "visitFrame"(arg0: integer, arg1: integer, arg2: (any)[], arg3: integer, arg4: (any)[]): void
public "accept"(arg0: $ClassVisitor$Type): void
public "accept"(arg0: $MethodVisitor$Type): void
public "check"(arg0: integer): void
public "visitAnnotation"(arg0: string, arg1: boolean): $AnnotationVisitor
public "visitEnd"(): void
public "visitCode"(): void
public "visitFieldInsn"(arg0: integer, arg1: string, arg2: string, arg3: string): void
public "visitInsn"(arg0: integer): void
public "visitMaxs"(arg0: integer, arg1: integer): void
public "visitVarInsn"(arg0: integer, arg1: integer): void
public "visitMethodInsn"(arg0: integer, arg1: string, arg2: string, arg3: string, arg4: boolean): void
public "visitTypeInsn"(arg0: integer, arg1: string): void
public "visitIntInsn"(arg0: integer, arg1: integer): void
public "visitLdcInsn"(arg0: any): void
public "visitParameter"(arg0: string, arg1: integer): void
public "visitAnnotationDefault"(): $AnnotationVisitor
public "visitTypeAnnotation"(arg0: integer, arg1: $TypePath$Type, arg2: string, arg3: boolean): $AnnotationVisitor
public "visitAnnotableParameterCount"(arg0: integer, arg1: boolean): void
public "visitParameterAnnotation"(arg0: integer, arg1: string, arg2: boolean): $AnnotationVisitor
public "visitAttribute"(arg0: $Attribute$Type): void
public "visitInvokeDynamicInsn"(arg0: string, arg1: string, arg2: $Handle$Type, ...arg3: (any)[]): void
public "visitJumpInsn"(arg0: integer, arg1: $Label$Type): void
public "visitLabel"(arg0: $Label$Type): void
public "visitIincInsn"(arg0: integer, arg1: integer): void
public "visitTableSwitchInsn"(arg0: integer, arg1: integer, arg2: $Label$Type, ...arg3: ($Label$Type)[]): void
public "visitLookupSwitchInsn"(arg0: $Label$Type, arg1: (integer)[], arg2: ($Label$Type)[]): void
public "visitMultiANewArrayInsn"(arg0: string, arg1: integer): void
public "visitInsnAnnotation"(arg0: integer, arg1: $TypePath$Type, arg2: string, arg3: boolean): $AnnotationVisitor
public "visitTryCatchBlock"(arg0: $Label$Type, arg1: $Label$Type, arg2: $Label$Type, arg3: string): void
public "visitTryCatchAnnotation"(arg0: integer, arg1: $TypePath$Type, arg2: string, arg3: boolean): $AnnotationVisitor
public "visitLocalVariable"(arg0: string, arg1: string, arg2: string, arg3: $Label$Type, arg4: $Label$Type, arg5: integer): void
public "visitLocalVariableAnnotation"(arg0: integer, arg1: $TypePath$Type, arg2: ($Label$Type)[], arg3: ($Label$Type)[], arg4: (integer)[], arg5: string, arg6: boolean): $AnnotationVisitor
public "visitLineNumber"(arg0: integer, arg1: $Label$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MethodNode$Type = ($MethodNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MethodNode_ = $MethodNode$Type;
}}
declare module "packages/org/objectweb/asm/tree/$RecordComponentNode" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$ClassVisitor, $ClassVisitor$Type} from "packages/org/objectweb/asm/$ClassVisitor"
import {$AnnotationVisitor, $AnnotationVisitor$Type} from "packages/org/objectweb/asm/$AnnotationVisitor"
import {$TypePath, $TypePath$Type} from "packages/org/objectweb/asm/$TypePath"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecordComponentVisitor, $RecordComponentVisitor$Type} from "packages/org/objectweb/asm/$RecordComponentVisitor"
import {$Attribute, $Attribute$Type} from "packages/org/objectweb/asm/$Attribute"
import {$TypeAnnotationNode, $TypeAnnotationNode$Type} from "packages/org/objectweb/asm/tree/$TypeAnnotationNode"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $RecordComponentNode extends $RecordComponentVisitor {
 "name": string
 "descriptor": string
 "signature": string
 "visibleAnnotations": $List<($AnnotationNode)>
 "invisibleAnnotations": $List<($AnnotationNode)>
 "visibleTypeAnnotations": $List<($TypeAnnotationNode)>
 "invisibleTypeAnnotations": $List<($TypeAnnotationNode)>
 "attrs": $List<($Attribute)>

constructor(arg0: string, arg1: string, arg2: string)
constructor(arg0: integer, arg1: string, arg2: string, arg3: string)

public "accept"(arg0: $ClassVisitor$Type): void
public "check"(arg0: integer): void
public "visitAnnotation"(arg0: string, arg1: boolean): $AnnotationVisitor
public "visitEnd"(): void
public "visitTypeAnnotation"(arg0: integer, arg1: $TypePath$Type, arg2: string, arg3: boolean): $AnnotationVisitor
public "visitAttribute"(arg0: $Attribute$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordComponentNode$Type = ($RecordComponentNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordComponentNode_ = $RecordComponentNode$Type;
}}
declare module "packages/org/objectweb/asm/tree/$FieldNode" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$ClassVisitor, $ClassVisitor$Type} from "packages/org/objectweb/asm/$ClassVisitor"
import {$AnnotationVisitor, $AnnotationVisitor$Type} from "packages/org/objectweb/asm/$AnnotationVisitor"
import {$FieldVisitor, $FieldVisitor$Type} from "packages/org/objectweb/asm/$FieldVisitor"
import {$TypePath, $TypePath$Type} from "packages/org/objectweb/asm/$TypePath"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Attribute, $Attribute$Type} from "packages/org/objectweb/asm/$Attribute"
import {$TypeAnnotationNode, $TypeAnnotationNode$Type} from "packages/org/objectweb/asm/tree/$TypeAnnotationNode"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $FieldNode extends $FieldVisitor {
 "access": integer
 "name": string
 "desc": string
 "signature": string
 "value": any
 "visibleAnnotations": $List<($AnnotationNode)>
 "invisibleAnnotations": $List<($AnnotationNode)>
 "visibleTypeAnnotations": $List<($TypeAnnotationNode)>
 "invisibleTypeAnnotations": $List<($TypeAnnotationNode)>
 "attrs": $List<($Attribute)>

constructor(arg0: integer, arg1: string, arg2: string, arg3: string, arg4: any)
constructor(arg0: integer, arg1: integer, arg2: string, arg3: string, arg4: string, arg5: any)

public "accept"(arg0: $ClassVisitor$Type): void
public "check"(arg0: integer): void
public "visitAnnotation"(arg0: string, arg1: boolean): $AnnotationVisitor
public "visitEnd"(): void
public "visitTypeAnnotation"(arg0: integer, arg1: $TypePath$Type, arg2: string, arg3: boolean): $AnnotationVisitor
public "visitAttribute"(arg0: $Attribute$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FieldNode$Type = ($FieldNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FieldNode_ = $FieldNode$Type;
}}
declare module "packages/org/objectweb/asm/tree/$TryCatchBlockNode" {
import {$LabelNode, $LabelNode$Type} from "packages/org/objectweb/asm/tree/$LabelNode"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MethodVisitor, $MethodVisitor$Type} from "packages/org/objectweb/asm/$MethodVisitor"
import {$TypeAnnotationNode, $TypeAnnotationNode$Type} from "packages/org/objectweb/asm/tree/$TypeAnnotationNode"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $TryCatchBlockNode {
 "start": $LabelNode
 "end": $LabelNode
 "handler": $LabelNode
 "type": string
 "visibleTypeAnnotations": $List<($TypeAnnotationNode)>
 "invisibleTypeAnnotations": $List<($TypeAnnotationNode)>

constructor(arg0: $LabelNode$Type, arg1: $LabelNode$Type, arg2: $LabelNode$Type, arg3: string)

public "accept"(arg0: $MethodVisitor$Type): void
public "updateIndex"(arg0: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TryCatchBlockNode$Type = ($TryCatchBlockNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TryCatchBlockNode_ = $TryCatchBlockNode$Type;
}}
declare module "packages/org/objectweb/asm/tree/$LabelNode" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Label, $Label$Type} from "packages/org/objectweb/asm/$Label"
import {$MethodVisitor, $MethodVisitor$Type} from "packages/org/objectweb/asm/$MethodVisitor"
import {$TypeAnnotationNode, $TypeAnnotationNode$Type} from "packages/org/objectweb/asm/tree/$TypeAnnotationNode"
import {$AbstractInsnNode, $AbstractInsnNode$Type} from "packages/org/objectweb/asm/tree/$AbstractInsnNode"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $LabelNode extends $AbstractInsnNode {
static readonly "INSN": integer
static readonly "INT_INSN": integer
static readonly "VAR_INSN": integer
static readonly "TYPE_INSN": integer
static readonly "FIELD_INSN": integer
static readonly "METHOD_INSN": integer
static readonly "INVOKE_DYNAMIC_INSN": integer
static readonly "JUMP_INSN": integer
static readonly "LABEL": integer
static readonly "LDC_INSN": integer
static readonly "IINC_INSN": integer
static readonly "TABLESWITCH_INSN": integer
static readonly "LOOKUPSWITCH_INSN": integer
static readonly "MULTIANEWARRAY_INSN": integer
static readonly "FRAME": integer
static readonly "LINE": integer
 "visibleTypeAnnotations": $List<($TypeAnnotationNode)>
 "invisibleTypeAnnotations": $List<($TypeAnnotationNode)>

constructor()
constructor(arg0: $Label$Type)

public "clone"(arg0: $Map$Type<($LabelNode$Type), ($LabelNode$Type)>): $AbstractInsnNode
public "accept"(arg0: $MethodVisitor$Type): void
public "getType"(): integer
public "resetLabel"(): void
public "getLabel"(): $Label
get "type"(): integer
get "label"(): $Label
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LabelNode$Type = ($LabelNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LabelNode_ = $LabelNode$Type;
}}
declare module "packages/org/objectweb/asm/tree/$LocalVariableAnnotationNode" {
import {$TypePath, $TypePath$Type} from "packages/org/objectweb/asm/$TypePath"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LabelNode, $LabelNode$Type} from "packages/org/objectweb/asm/tree/$LabelNode"
import {$MethodVisitor, $MethodVisitor$Type} from "packages/org/objectweb/asm/$MethodVisitor"
import {$TypeAnnotationNode, $TypeAnnotationNode$Type} from "packages/org/objectweb/asm/tree/$TypeAnnotationNode"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $LocalVariableAnnotationNode extends $TypeAnnotationNode {
 "start": $List<($LabelNode)>
 "end": $List<($LabelNode)>
 "index": $List<(integer)>
 "typeRef": integer
 "typePath": $TypePath
 "desc": string
 "values": $List<(any)>

constructor(arg0: integer, arg1: $TypePath$Type, arg2: ($LabelNode$Type)[], arg3: ($LabelNode$Type)[], arg4: (integer)[], arg5: string)
constructor(arg0: integer, arg1: integer, arg2: $TypePath$Type, arg3: ($LabelNode$Type)[], arg4: ($LabelNode$Type)[], arg5: (integer)[], arg6: string)

public "accept"(arg0: $MethodVisitor$Type, arg1: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalVariableAnnotationNode$Type = ($LocalVariableAnnotationNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalVariableAnnotationNode_ = $LocalVariableAnnotationNode$Type;
}}
declare module "packages/org/objectweb/asm/tree/$ModuleProvideNode" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ModuleVisitor, $ModuleVisitor$Type} from "packages/org/objectweb/asm/$ModuleVisitor"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ModuleProvideNode {
 "service": string
 "providers": $List<(string)>

constructor(arg0: string, arg1: $List$Type<(string)>)

public "accept"(arg0: $ModuleVisitor$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleProvideNode$Type = ($ModuleProvideNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleProvideNode_ = $ModuleProvideNode$Type;
}}
declare module "packages/org/objectweb/asm/$Handle" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Handle {

/**
 * 
 * @deprecated
 */
constructor(arg0: integer, arg1: string, arg2: string, arg3: string)
constructor(arg0: integer, arg1: string, arg2: string, arg3: string, arg4: boolean)

public "getName"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "isInterface"(): boolean
public "getOwner"(): string
public "getTag"(): integer
public "getDesc"(): string
get "name"(): string
get "interface"(): boolean
get "owner"(): string
get "tag"(): integer
get "desc"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Handle$Type = ($Handle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Handle_ = $Handle$Type;
}}
declare module "packages/org/objectweb/asm/tree/$InnerClassNode" {
import {$ClassVisitor, $ClassVisitor$Type} from "packages/org/objectweb/asm/$ClassVisitor"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $InnerClassNode {
 "name": string
 "outerName": string
 "innerName": string
 "access": integer

constructor(arg0: string, arg1: string, arg2: string, arg3: integer)

public "accept"(arg0: $ClassVisitor$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InnerClassNode$Type = ($InnerClassNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InnerClassNode_ = $InnerClassNode$Type;
}}
declare module "packages/org/objectweb/asm/tree/$TypeInsnNode" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$LabelNode, $LabelNode$Type} from "packages/org/objectweb/asm/tree/$LabelNode"
import {$MethodVisitor, $MethodVisitor$Type} from "packages/org/objectweb/asm/$MethodVisitor"
import {$TypeAnnotationNode, $TypeAnnotationNode$Type} from "packages/org/objectweb/asm/tree/$TypeAnnotationNode"
import {$AbstractInsnNode, $AbstractInsnNode$Type} from "packages/org/objectweb/asm/tree/$AbstractInsnNode"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $TypeInsnNode extends $AbstractInsnNode {
 "desc": string
static readonly "INSN": integer
static readonly "INT_INSN": integer
static readonly "VAR_INSN": integer
static readonly "TYPE_INSN": integer
static readonly "FIELD_INSN": integer
static readonly "METHOD_INSN": integer
static readonly "INVOKE_DYNAMIC_INSN": integer
static readonly "JUMP_INSN": integer
static readonly "LABEL": integer
static readonly "LDC_INSN": integer
static readonly "IINC_INSN": integer
static readonly "TABLESWITCH_INSN": integer
static readonly "LOOKUPSWITCH_INSN": integer
static readonly "MULTIANEWARRAY_INSN": integer
static readonly "FRAME": integer
static readonly "LINE": integer
 "visibleTypeAnnotations": $List<($TypeAnnotationNode)>
 "invisibleTypeAnnotations": $List<($TypeAnnotationNode)>

constructor(arg0: integer, arg1: string)

public "clone"(arg0: $Map$Type<($LabelNode$Type), ($LabelNode$Type)>): $AbstractInsnNode
public "accept"(arg0: $MethodVisitor$Type): void
public "getType"(): integer
public "setOpcode"(arg0: integer): void
get "type"(): integer
set "opcode"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeInsnNode$Type = ($TypeInsnNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeInsnNode_ = $TypeInsnNode$Type;
}}
declare module "packages/org/objectweb/asm/$Attribute" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Attribute {
readonly "type": string


public "isUnknown"(): boolean
public "isCodeAttribute"(): boolean
get "unknown"(): boolean
get "codeAttribute"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Attribute$Type = ($Attribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Attribute_ = $Attribute$Type;
}}
declare module "packages/org/objectweb/asm/$ModuleVisitor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ModuleVisitor {


public "visitEnd"(): void
public "visitMainClass"(arg0: string): void
public "visitPackage"(arg0: string): void
public "visitRequire"(arg0: string, arg1: integer, arg2: string): void
public "visitExport"(arg0: string, arg1: integer, ...arg2: (string)[]): void
public "visitOpen"(arg0: string, arg1: integer, ...arg2: (string)[]): void
public "visitUse"(arg0: string): void
public "visitProvide"(arg0: string, ...arg1: (string)[]): void
public "getDelegate"(): $ModuleVisitor
get "delegate"(): $ModuleVisitor
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleVisitor$Type = ($ModuleVisitor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleVisitor_ = $ModuleVisitor$Type;
}}
declare module "packages/org/objectweb/asm/tree/$MethodInsnNode" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$LabelNode, $LabelNode$Type} from "packages/org/objectweb/asm/tree/$LabelNode"
import {$MethodVisitor, $MethodVisitor$Type} from "packages/org/objectweb/asm/$MethodVisitor"
import {$TypeAnnotationNode, $TypeAnnotationNode$Type} from "packages/org/objectweb/asm/tree/$TypeAnnotationNode"
import {$AbstractInsnNode, $AbstractInsnNode$Type} from "packages/org/objectweb/asm/tree/$AbstractInsnNode"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MethodInsnNode extends $AbstractInsnNode {
 "owner": string
 "name": string
 "desc": string
 "itf": boolean
static readonly "INSN": integer
static readonly "INT_INSN": integer
static readonly "VAR_INSN": integer
static readonly "TYPE_INSN": integer
static readonly "FIELD_INSN": integer
static readonly "METHOD_INSN": integer
static readonly "INVOKE_DYNAMIC_INSN": integer
static readonly "JUMP_INSN": integer
static readonly "LABEL": integer
static readonly "LDC_INSN": integer
static readonly "IINC_INSN": integer
static readonly "TABLESWITCH_INSN": integer
static readonly "LOOKUPSWITCH_INSN": integer
static readonly "MULTIANEWARRAY_INSN": integer
static readonly "FRAME": integer
static readonly "LINE": integer
 "visibleTypeAnnotations": $List<($TypeAnnotationNode)>
 "invisibleTypeAnnotations": $List<($TypeAnnotationNode)>

constructor(arg0: integer, arg1: string, arg2: string, arg3: string)
constructor(arg0: integer, arg1: string, arg2: string, arg3: string, arg4: boolean)

public "clone"(arg0: $Map$Type<($LabelNode$Type), ($LabelNode$Type)>): $AbstractInsnNode
public "accept"(arg0: $MethodVisitor$Type): void
public "getType"(): integer
public "setOpcode"(arg0: integer): void
get "type"(): integer
set "opcode"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MethodInsnNode$Type = ($MethodInsnNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MethodInsnNode_ = $MethodInsnNode$Type;
}}
declare module "packages/org/objectweb/asm/$MethodVisitor" {
import {$AnnotationVisitor, $AnnotationVisitor$Type} from "packages/org/objectweb/asm/$AnnotationVisitor"
import {$TypePath, $TypePath$Type} from "packages/org/objectweb/asm/$TypePath"
import {$Label, $Label$Type} from "packages/org/objectweb/asm/$Label"
import {$Attribute, $Attribute$Type} from "packages/org/objectweb/asm/$Attribute"
import {$Handle, $Handle$Type} from "packages/org/objectweb/asm/$Handle"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MethodVisitor {


public "visitFrame"(arg0: integer, arg1: integer, arg2: (any)[], arg3: integer, arg4: (any)[]): void
public "visitAnnotation"(arg0: string, arg1: boolean): $AnnotationVisitor
public "visitEnd"(): void
public "visitCode"(): void
public "visitFieldInsn"(arg0: integer, arg1: string, arg2: string, arg3: string): void
public "visitInsn"(arg0: integer): void
public "visitMaxs"(arg0: integer, arg1: integer): void
public "visitVarInsn"(arg0: integer, arg1: integer): void
/**
 * 
 * @deprecated
 */
public "visitMethodInsn"(arg0: integer, arg1: string, arg2: string, arg3: string): void
public "visitMethodInsn"(arg0: integer, arg1: string, arg2: string, arg3: string, arg4: boolean): void
public "visitTypeInsn"(arg0: integer, arg1: string): void
public "visitIntInsn"(arg0: integer, arg1: integer): void
public "visitLdcInsn"(arg0: any): void
public "visitParameter"(arg0: string, arg1: integer): void
public "visitAnnotationDefault"(): $AnnotationVisitor
public "visitTypeAnnotation"(arg0: integer, arg1: $TypePath$Type, arg2: string, arg3: boolean): $AnnotationVisitor
public "visitAnnotableParameterCount"(arg0: integer, arg1: boolean): void
public "visitParameterAnnotation"(arg0: integer, arg1: string, arg2: boolean): $AnnotationVisitor
public "visitAttribute"(arg0: $Attribute$Type): void
public "visitInvokeDynamicInsn"(arg0: string, arg1: string, arg2: $Handle$Type, ...arg3: (any)[]): void
public "visitJumpInsn"(arg0: integer, arg1: $Label$Type): void
public "visitLabel"(arg0: $Label$Type): void
public "visitIincInsn"(arg0: integer, arg1: integer): void
public "visitTableSwitchInsn"(arg0: integer, arg1: integer, arg2: $Label$Type, ...arg3: ($Label$Type)[]): void
public "visitLookupSwitchInsn"(arg0: $Label$Type, arg1: (integer)[], arg2: ($Label$Type)[]): void
public "visitMultiANewArrayInsn"(arg0: string, arg1: integer): void
public "visitInsnAnnotation"(arg0: integer, arg1: $TypePath$Type, arg2: string, arg3: boolean): $AnnotationVisitor
public "visitTryCatchBlock"(arg0: $Label$Type, arg1: $Label$Type, arg2: $Label$Type, arg3: string): void
public "visitTryCatchAnnotation"(arg0: integer, arg1: $TypePath$Type, arg2: string, arg3: boolean): $AnnotationVisitor
public "visitLocalVariable"(arg0: string, arg1: string, arg2: string, arg3: $Label$Type, arg4: $Label$Type, arg5: integer): void
public "visitLocalVariableAnnotation"(arg0: integer, arg1: $TypePath$Type, arg2: ($Label$Type)[], arg3: ($Label$Type)[], arg4: (integer)[], arg5: string, arg6: boolean): $AnnotationVisitor
public "visitLineNumber"(arg0: integer, arg1: $Label$Type): void
public "getDelegate"(): $MethodVisitor
get "delegate"(): $MethodVisitor
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MethodVisitor$Type = ($MethodVisitor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MethodVisitor_ = $MethodVisitor$Type;
}}
