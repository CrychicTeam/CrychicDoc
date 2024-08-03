declare module "packages/java/sql/$Ref" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $Ref {

 "getObject"(): any
 "getObject"(arg0: $Map$Type<(string), ($Class$Type<(any)>)>): any
 "setObject"(arg0: any): void
 "getBaseTypeName"(): string
}

export namespace $Ref {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Ref$Type = ($Ref);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Ref_ = $Ref$Type;
}}
declare module "packages/java/sql/$DatabaseMetaData" {
import {$Connection, $Connection$Type} from "packages/java/sql/$Connection"
import {$Wrapper, $Wrapper$Type} from "packages/java/sql/$Wrapper"
import {$RowIdLifetime, $RowIdLifetime$Type} from "packages/java/sql/$RowIdLifetime"
import {$ResultSet, $ResultSet$Type} from "packages/java/sql/$ResultSet"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $DatabaseMetaData extends $Wrapper {

 "getAttributes"(arg0: string, arg1: string, arg2: string, arg3: string): $ResultSet
 "isReadOnly"(): boolean
 "getURL"(): string
 "getConnection"(): $Connection
 "getDriverVersion"(): string
 "getMaxConnections"(): integer
 "getDriverName"(): string
 "getTypeInfo"(): $ResultSet
 "getColumns"(arg0: string, arg1: string, arg2: string, arg3: string): $ResultSet
 "getUserName"(): string
 "getTables"(arg0: string, arg1: string, arg2: string, arg3: (string)[]): $ResultSet
 "getFunctions"(arg0: string, arg1: string, arg2: string): $ResultSet
 "nullsAreSortedLow"(): boolean
 "usesLocalFiles"(): boolean
 "nullsAreSortedHigh"(): boolean
 "getSQLKeywords"(): string
 "getSystemFunctions"(): string
 "getStringFunctions"(): string
 "getSchemaTerm"(): string
 "getProcedureTerm"(): string
 "getCatalogTerm"(): string
 "isCatalogAtStart"(): boolean
 "supportsOuterJoins"(): boolean
 "supportsConvert"(): boolean
 "supportsConvert"(arg0: integer, arg1: integer): boolean
 "supportsGroupBy"(): boolean
 "supportsUnion"(): boolean
 "supportsUnionAll"(): boolean
 "getVersionColumns"(arg0: string, arg1: string, arg2: string): $ResultSet
 "getTableTypes"(): $ResultSet
 "getMaxStatements"(): integer
 "getPrimaryKeys"(arg0: string, arg1: string, arg2: string): $ResultSet
 "getCrossReference"(arg0: string, arg1: string, arg2: string, arg3: string, arg4: string, arg5: string): $ResultSet
 "getExportedKeys"(arg0: string, arg1: string, arg2: string): $ResultSet
 "getCatalogs"(): $ResultSet
 "getSchemas"(arg0: string, arg1: string): $ResultSet
 "getSchemas"(): $ResultSet
 "getMaxRowSize"(): integer
 "getProcedures"(arg0: string, arg1: string, arg2: string): $ResultSet
 "getTablePrivileges"(arg0: string, arg1: string, arg2: string): $ResultSet
 "getIndexInfo"(arg0: string, arg1: string, arg2: string, arg3: boolean, arg4: boolean): $ResultSet
 "getImportedKeys"(arg0: string, arg1: string, arg2: string): $ResultSet
 "getMaxIndexLength"(): integer
 "insertsAreDetected"(arg0: integer): boolean
 "locatorsUpdateCopy"(): boolean
 "getFunctionColumns"(arg0: string, arg1: string, arg2: string, arg3: string): $ResultSet
 "supportsSharding"(): boolean
 "getRowIdLifetime"(): $RowIdLifetime
 "supportsRefCursors"(): boolean
 "getSuperTables"(arg0: string, arg1: string, arg2: string): $ResultSet
 "supportsSavepoints"(): boolean
 "getSuperTypes"(arg0: string, arg1: string, arg2: string): $ResultSet
 "getSQLStateType"(): integer
 "getPseudoColumns"(arg0: string, arg1: string, arg2: string, arg3: string): $ResultSet
 "updatesAreDetected"(arg0: integer): boolean
 "getUDTs"(arg0: string, arg1: string, arg2: string, arg3: (integer)[]): $ResultSet
 "deletesAreDetected"(arg0: integer): boolean
 "allTablesAreSelectable"(): boolean
 "nullsAreSortedAtEnd"(): boolean
 "getDatabaseProductName"(): string
 "getDatabaseProductVersion"(): string
 "getDriverMajorVersion"(): integer
 "usesLocalFilePerTable"(): boolean
 "nullsAreSortedAtStart"(): boolean
 "supportsMixedCaseIdentifiers"(): boolean
 "getDriverMinorVersion"(): integer
 "allProceduresAreCallable"(): boolean
 "supportsSchemasInProcedureCalls"(): boolean
 "supportsColumnAliasing"(): boolean
 "supportsPositionedDelete"(): boolean
 "supportsSubqueriesInComparisons"(): boolean
 "supportsSubqueriesInIns"(): boolean
 "supportsCatalogsInDataManipulation"(): boolean
 "supportsAlterTableWithAddColumn"(): boolean
 "supportsAlterTableWithDropColumn"(): boolean
 "supportsCatalogsInTableDefinitions"(): boolean
 "supportsSubqueriesInQuantifieds"(): boolean
 "getIdentifierQuoteString"(): string
 "supportsOpenCursorsAcrossRollback"(): boolean
 "supportsMinimumSQLGrammar"(): boolean
 "supportsCorrelatedSubqueries"(): boolean
 "supportsOpenStatementsAcrossCommit"(): boolean
 "getCatalogSeparator"(): string
 "supportsOpenCursorsAcrossCommit"(): boolean
 "getMaxBinaryLiteralLength"(): integer
 "getNumericFunctions"(): string
 "nullPlusNonNullIsNull"(): boolean
 "supportsGroupByBeyondSelect"(): boolean
 "storesUpperCaseIdentifiers"(): boolean
 "supportsTableCorrelationNames"(): boolean
 "supportsSchemasInTableDefinitions"(): boolean
 "supportsOrderByUnrelated"(): boolean
 "supportsMultipleTransactions"(): boolean
 "supportsANSI92FullSQL"(): boolean
 "supportsLimitedOuterJoins"(): boolean
 "supportsCatalogsInProcedureCalls"(): boolean
 "getSearchStringEscape"(): string
 "supportsCoreSQLGrammar"(): boolean
 "getExtraNameCharacters"(): string
 "supportsFullOuterJoins"(): boolean
 "supportsNonNullableColumns"(): boolean
 "supportsCatalogsInIndexDefinitions"(): boolean
 "supportsPositionedUpdate"(): boolean
 "supportsStoredProcedures"(): boolean
 "storesMixedCaseIdentifiers"(): boolean
 "supportsMixedCaseQuotedIdentifiers"(): boolean
 "getTimeDateFunctions"(): string
 "supportsSubqueriesInExists"(): boolean
 "supportsSchemasInIndexDefinitions"(): boolean
 "supportsSelectForUpdate"(): boolean
 "getMaxCharLiteralLength"(): integer
 "storesLowerCaseQuotedIdentifiers"(): boolean
 "supportsMultipleResultSets"(): boolean
 "supportsANSI92EntryLevelSQL"(): boolean
 "storesLowerCaseIdentifiers"(): boolean
 "supportsLikeEscapeClause"(): boolean
 "supportsANSI92IntermediateSQL"(): boolean
 "supportsExtendedSQLGrammar"(): boolean
 "supportsSchemasInDataManipulation"(): boolean
 "storesMixedCaseQuotedIdentifiers"(): boolean
 "supportsExpressionsInOrderBy"(): boolean
 "supportsGroupByUnrelated"(): boolean
 "storesUpperCaseQuotedIdentifiers"(): boolean
 "getColumnPrivileges"(arg0: string, arg1: string, arg2: string, arg3: string): $ResultSet
 "supportsTransactions"(): boolean
 "ownUpdatesAreVisible"(arg0: integer): boolean
 "othersInsertsAreVisible"(arg0: integer): boolean
 "othersUpdatesAreVisible"(arg0: integer): boolean
 "getMaxUserNameLength"(): integer
 "getProcedureColumns"(arg0: string, arg1: string, arg2: string, arg3: string): $ResultSet
 "supportsBatchUpdates"(): boolean
 "getMaxColumnsInSelect"(): integer
 "supportsResultSetConcurrency"(arg0: integer, arg1: integer): boolean
 "othersDeletesAreVisible"(arg0: integer): boolean
 "supportsGetGeneratedKeys"(): boolean
 "getMaxTableNameLength"(): integer
 "supportsResultSetHoldability"(arg0: integer): boolean
 "getJDBCMajorVersion"(): integer
 "ownDeletesAreVisible"(arg0: integer): boolean
 "getMaxCursorNameLength"(): integer
 "getMaxStatementLength"(): integer
 "getDefaultTransactionIsolation"(): integer
 "getBestRowIdentifier"(arg0: string, arg1: string, arg2: string, arg3: integer, arg4: boolean): $ResultSet
 "supportsResultSetType"(arg0: integer): boolean
 "getMaxCatalogNameLength"(): integer
 "doesMaxRowSizeIncludeBlobs"(): boolean
 "ownInsertsAreVisible"(arg0: integer): boolean
 "supportsNamedParameters"(): boolean
 "getResultSetHoldability"(): integer
 "getMaxColumnNameLength"(): integer
 "getMaxColumnsInTable"(): integer
 "getMaxSchemaNameLength"(): integer
 "getMaxProcedureNameLength"(): integer
 "supportsMultipleOpenResults"(): boolean
 "getMaxColumnsInIndex"(): integer
 "getMaxTablesInSelect"(): integer
 "getDatabaseMajorVersion"(): integer
 "getDatabaseMinorVersion"(): integer
 "getJDBCMinorVersion"(): integer
 "getMaxColumnsInOrderBy"(): integer
 "supportsTransactionIsolationLevel"(arg0: integer): boolean
 "getMaxColumnsInGroupBy"(): integer
 "getClientInfoProperties"(): $ResultSet
 "generatedKeyAlwaysReturned"(): boolean
 "getMaxLogicalLobSize"(): long
 "supportsStatementPooling"(): boolean
 "supportsDataDefinitionAndDataManipulationTransactions"(): boolean
 "supportsStoredFunctionsUsingCallSyntax"(): boolean
 "autoCommitFailureClosesAllResultSets"(): boolean
 "supportsSchemasInPrivilegeDefinitions"(): boolean
 "supportsDataManipulationTransactionsOnly"(): boolean
 "dataDefinitionCausesTransactionCommit"(): boolean
 "supportsOpenStatementsAcrossRollback"(): boolean
 "supportsIntegrityEnhancementFacility"(): boolean
 "dataDefinitionIgnoredInTransactions"(): boolean
 "supportsCatalogsInPrivilegeDefinitions"(): boolean
 "supportsDifferentTableCorrelationNames"(): boolean
 "unwrap"<T>(arg0: $Class$Type<(T)>): T
 "isWrapperFor"(arg0: $Class$Type<(any)>): boolean
}

export namespace $DatabaseMetaData {
const procedureResultUnknown: integer
const procedureNoResult: integer
const procedureReturnsResult: integer
const procedureColumnUnknown: integer
const procedureColumnIn: integer
const procedureColumnInOut: integer
const procedureColumnOut: integer
const procedureColumnReturn: integer
const procedureColumnResult: integer
const procedureNoNulls: integer
const procedureNullable: integer
const procedureNullableUnknown: integer
const columnNoNulls: integer
const columnNullable: integer
const columnNullableUnknown: integer
const bestRowTemporary: integer
const bestRowTransaction: integer
const bestRowSession: integer
const bestRowUnknown: integer
const bestRowNotPseudo: integer
const bestRowPseudo: integer
const versionColumnUnknown: integer
const versionColumnNotPseudo: integer
const versionColumnPseudo: integer
const importedKeyCascade: integer
const importedKeyRestrict: integer
const importedKeySetNull: integer
const importedKeyNoAction: integer
const importedKeySetDefault: integer
const importedKeyInitiallyDeferred: integer
const importedKeyInitiallyImmediate: integer
const importedKeyNotDeferrable: integer
const typeNoNulls: integer
const typeNullable: integer
const typeNullableUnknown: integer
const typePredNone: integer
const typePredChar: integer
const typePredBasic: integer
const typeSearchable: integer
const tableIndexStatistic: short
const tableIndexClustered: short
const tableIndexHashed: short
const tableIndexOther: short
const attributeNoNulls: short
const attributeNullable: short
const attributeNullableUnknown: short
const sqlStateXOpen: integer
const sqlStateSQL: integer
const sqlStateSQL99: integer
const functionColumnUnknown: integer
const functionColumnIn: integer
const functionColumnInOut: integer
const functionColumnOut: integer
const functionReturn: integer
const functionColumnResult: integer
const functionNoNulls: integer
const functionNullable: integer
const functionNullableUnknown: integer
const functionResultUnknown: integer
const functionNoTable: integer
const functionReturnsTable: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DatabaseMetaData$Type = ($DatabaseMetaData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DatabaseMetaData_ = $DatabaseMetaData$Type;
}}
declare module "packages/java/sql/$SQLType" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $SQLType {

 "getName"(): string
 "getVendorTypeNumber"(): integer
 "getVendor"(): string
}

export namespace $SQLType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SQLType$Type = ($SQLType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SQLType_ = $SQLType$Type;
}}
declare module "packages/java/sql/$NClob" {
import {$Clob, $Clob$Type} from "packages/java/sql/$Clob"
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"

export interface $NClob extends $Clob {

 "length"(): long
 "position"(arg0: $Clob$Type, arg1: long): long
 "position"(arg0: string, arg1: long): long
 "free"(): void
 "truncate"(arg0: long): void
 "setCharacterStream"(arg0: long): $Writer
 "getCharacterStream"(arg0: long, arg1: long): $Reader
 "getCharacterStream"(): $Reader
 "setString"(arg0: long, arg1: string): integer
 "setString"(arg0: long, arg1: string, arg2: integer, arg3: integer): integer
 "getAsciiStream"(): $InputStream
 "setAsciiStream"(arg0: long): $OutputStream
 "getSubString"(arg0: long, arg1: integer): string
}

export namespace $NClob {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NClob$Type = ($NClob);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NClob_ = $NClob$Type;
}}
declare module "packages/java/sql/$RowIdLifetime" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $RowIdLifetime extends $Enum<($RowIdLifetime)> {
static readonly "ROWID_UNSUPPORTED": $RowIdLifetime
static readonly "ROWID_VALID_OTHER": $RowIdLifetime
static readonly "ROWID_VALID_SESSION": $RowIdLifetime
static readonly "ROWID_VALID_TRANSACTION": $RowIdLifetime
static readonly "ROWID_VALID_FOREVER": $RowIdLifetime


public static "values"(): ($RowIdLifetime)[]
public static "valueOf"(arg0: string): $RowIdLifetime
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RowIdLifetime$Type = (("rowid_unsupported") | ("rowid_valid_session") | ("rowid_valid_transaction") | ("rowid_valid_other") | ("rowid_valid_forever")) | ($RowIdLifetime);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RowIdLifetime_ = $RowIdLifetime$Type;
}}
declare module "packages/java/sql/$Clob" {
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"

export interface $Clob {

 "length"(): long
 "position"(arg0: $Clob$Type, arg1: long): long
 "position"(arg0: string, arg1: long): long
 "free"(): void
 "truncate"(arg0: long): void
 "setCharacterStream"(arg0: long): $Writer
 "getCharacterStream"(arg0: long, arg1: long): $Reader
 "getCharacterStream"(): $Reader
 "setString"(arg0: long, arg1: string): integer
 "setString"(arg0: long, arg1: string, arg2: integer, arg3: integer): integer
 "getAsciiStream"(): $InputStream
 "setAsciiStream"(arg0: long): $OutputStream
 "getSubString"(arg0: long, arg1: integer): string
}

export namespace $Clob {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Clob$Type = ($Clob);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Clob_ = $Clob$Type;
}}
declare module "packages/java/sql/$SQLXML" {
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"
import {$Source, $Source$Type} from "packages/javax/xml/transform/$Source"
import {$Result, $Result$Type} from "packages/javax/xml/transform/$Result"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"

export interface $SQLXML {

 "setResult"<T extends $Result>(arg0: $Class$Type<(T)>): T
 "getString"(): string
 "free"(): void
 "getSource"<T extends $Source>(arg0: $Class$Type<(T)>): T
 "setCharacterStream"(): $Writer
 "getCharacterStream"(): $Reader
 "setString"(arg0: string): void
 "getBinaryStream"(): $InputStream
 "setBinaryStream"(): $OutputStream
}

export namespace $SQLXML {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SQLXML$Type = ($SQLXML);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SQLXML_ = $SQLXML$Type;
}}
declare module "packages/java/sql/$Struct" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $Struct {

 "getAttributes"(): (any)[]
 "getAttributes"(arg0: $Map$Type<(string), ($Class$Type<(any)>)>): (any)[]
 "getSQLTypeName"(): string
}

export namespace $Struct {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Struct$Type = ($Struct);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Struct_ = $Struct$Type;
}}
declare module "packages/java/sql/$Statement" {
import {$Connection, $Connection$Type} from "packages/java/sql/$Connection"
import {$Wrapper, $Wrapper$Type} from "packages/java/sql/$Wrapper"
import {$ResultSet, $ResultSet$Type} from "packages/java/sql/$ResultSet"
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$SQLWarning, $SQLWarning$Type} from "packages/java/sql/$SQLWarning"

export interface $Statement extends $Wrapper, $AutoCloseable {

 "getLargeUpdateCount"(): long
 "setEscapeProcessing"(arg0: boolean): void
 "getResultSetConcurrency"(): integer
 "isCloseOnCompletion"(): boolean
 "enquoteNCharLiteral"(arg0: string): string
 "execute"(arg0: string): boolean
 "execute"(arg0: string, arg1: integer): boolean
 "execute"(arg0: string, arg1: (string)[]): boolean
 "execute"(arg0: string, arg1: (integer)[]): boolean
 "close"(): void
 "cancel"(): void
 "getConnection"(): $Connection
 "isClosed"(): boolean
 "getWarnings"(): $SQLWarning
 "setMaxRows"(arg0: integer): void
 "setCursorName"(arg0: string): void
 "getResultSet"(): $ResultSet
 "setMaxFieldSize"(arg0: integer): void
 "executeQuery"(arg0: string): $ResultSet
 "getMaxFieldSize"(): integer
 "setQueryTimeout"(arg0: integer): void
 "executeUpdate"(arg0: string, arg1: (string)[]): integer
 "executeUpdate"(arg0: string, arg1: integer): integer
 "executeUpdate"(arg0: string, arg1: (integer)[]): integer
 "executeUpdate"(arg0: string): integer
 "getQueryTimeout"(): integer
 "getUpdateCount"(): integer
 "getMoreResults"(): boolean
 "getMoreResults"(arg0: integer): boolean
 "getResultSetType"(): integer
 "addBatch"(arg0: string): void
 "setLargeMaxRows"(arg0: long): void
 "executeLargeBatch"(): (long)[]
 "clearBatch"(): void
 "getLargeMaxRows"(): long
 "getGeneratedKeys"(): $ResultSet
 "executeBatch"(): (integer)[]
 "closeOnCompletion"(): void
 "setPoolable"(arg0: boolean): void
 "isPoolable"(): boolean
 "executeLargeUpdate"(arg0: string): long
 "executeLargeUpdate"(arg0: string, arg1: (string)[]): long
 "executeLargeUpdate"(arg0: string, arg1: (integer)[]): long
 "executeLargeUpdate"(arg0: string, arg1: integer): long
 "enquoteLiteral"(arg0: string): string
 "isSimpleIdentifier"(arg0: string): boolean
 "enquoteIdentifier"(arg0: string, arg1: boolean): string
 "getFetchSize"(): integer
 "setFetchSize"(arg0: integer): void
 "clearWarnings"(): void
 "setFetchDirection"(arg0: integer): void
 "getFetchDirection"(): integer
 "getResultSetHoldability"(): integer
 "getMaxRows"(): integer
 "unwrap"<T>(arg0: $Class$Type<(T)>): T
 "isWrapperFor"(arg0: $Class$Type<(any)>): boolean
}

export namespace $Statement {
const CLOSE_CURRENT_RESULT: integer
const KEEP_CURRENT_RESULT: integer
const CLOSE_ALL_RESULTS: integer
const SUCCESS_NO_INFO: integer
const EXECUTE_FAILED: integer
const RETURN_GENERATED_KEYS: integer
const NO_GENERATED_KEYS: integer
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
declare module "packages/java/sql/$Connection" {
import {$Wrapper, $Wrapper$Type} from "packages/java/sql/$Wrapper"
import {$ShardingKey, $ShardingKey$Type} from "packages/java/sql/$ShardingKey"
import {$SQLXML, $SQLXML$Type} from "packages/java/sql/$SQLXML"
import {$PreparedStatement, $PreparedStatement$Type} from "packages/java/sql/$PreparedStatement"
import {$NClob, $NClob$Type} from "packages/java/sql/$NClob"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Properties, $Properties$Type} from "packages/java/util/$Properties"
import {$Executor, $Executor$Type} from "packages/java/util/concurrent/$Executor"
import {$Struct, $Struct$Type} from "packages/java/sql/$Struct"
import {$CallableStatement, $CallableStatement$Type} from "packages/java/sql/$CallableStatement"
import {$DatabaseMetaData, $DatabaseMetaData$Type} from "packages/java/sql/$DatabaseMetaData"
import {$Savepoint, $Savepoint$Type} from "packages/java/sql/$Savepoint"
import {$Clob, $Clob$Type} from "packages/java/sql/$Clob"
import {$Blob, $Blob$Type} from "packages/java/sql/$Blob"
import {$Array, $Array$Type} from "packages/java/sql/$Array"
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"
import {$SQLWarning, $SQLWarning$Type} from "packages/java/sql/$SQLWarning"
import {$Statement, $Statement$Type} from "packages/java/sql/$Statement"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $Connection extends $Wrapper, $AutoCloseable {

 "setReadOnly"(arg0: boolean): void
 "close"(): void
 "isReadOnly"(): boolean
 "commit"(): void
 "isValid"(arg0: integer): boolean
 "abort"(arg0: $Executor$Type): void
 "isClosed"(): boolean
 "getSchema"(): string
 "setSchema"(arg0: string): void
 "getMetaData"(): $DatabaseMetaData
 "getWarnings"(): $SQLWarning
 "clearWarnings"(): void
 "getHoldability"(): integer
 "setAutoCommit"(arg0: boolean): void
 "getAutoCommit"(): boolean
 "prepareStatement"(arg0: string, arg1: integer): $PreparedStatement
 "prepareStatement"(arg0: string, arg1: (integer)[]): $PreparedStatement
 "prepareStatement"(arg0: string, arg1: integer, arg2: integer): $PreparedStatement
 "prepareStatement"(arg0: string, arg1: integer, arg2: integer, arg3: integer): $PreparedStatement
 "prepareStatement"(arg0: string, arg1: (string)[]): $PreparedStatement
 "prepareStatement"(arg0: string): $PreparedStatement
 "rollback"(arg0: $Savepoint$Type): void
 "rollback"(): void
 "getCatalog"(): string
 "nativeSQL"(arg0: string): string
 "setCatalog"(arg0: string): void
 "prepareCall"(arg0: string, arg1: integer, arg2: integer, arg3: integer): $CallableStatement
 "prepareCall"(arg0: string): $CallableStatement
 "prepareCall"(arg0: string, arg1: integer, arg2: integer): $CallableStatement
 "createBlob"(): $Blob
 "createStruct"(arg0: string, arg1: (any)[]): $Struct
 "setClientInfo"(arg0: string, arg1: string): void
 "setClientInfo"(arg0: $Properties$Type): void
 "setShardingKey"(arg0: $ShardingKey$Type, arg1: $ShardingKey$Type): void
 "setShardingKey"(arg0: $ShardingKey$Type): void
 "createArrayOf"(arg0: string, arg1: (any)[]): $Array
 "releaseSavepoint"(arg0: $Savepoint$Type): void
 "createNClob"(): $NClob
 "setTypeMap"(arg0: $Map$Type<(string), ($Class$Type<(any)>)>): void
 "setNetworkTimeout"(arg0: $Executor$Type, arg1: integer): void
 "createSQLXML"(): $SQLXML
 "getNetworkTimeout"(): integer
 "beginRequest"(): void
 "setHoldability"(arg0: integer): void
 "endRequest"(): void
 "setSavepoint"(): $Savepoint
 "setSavepoint"(arg0: string): $Savepoint
 "createClob"(): $Clob
 "getClientInfo"(): $Properties
 "getClientInfo"(arg0: string): string
 "setTransactionIsolation"(arg0: integer): void
 "getTransactionIsolation"(): integer
 "setShardingKeyIfValid"(arg0: $ShardingKey$Type, arg1: integer): boolean
 "setShardingKeyIfValid"(arg0: $ShardingKey$Type, arg1: $ShardingKey$Type, arg2: integer): boolean
 "getTypeMap"(): $Map<(string), ($Class<(any)>)>
 "createStatement"(): $Statement
 "createStatement"(arg0: integer, arg1: integer, arg2: integer): $Statement
 "createStatement"(arg0: integer, arg1: integer): $Statement
 "unwrap"<T>(arg0: $Class$Type<(T)>): T
 "isWrapperFor"(arg0: $Class$Type<(any)>): boolean
}

export namespace $Connection {
const TRANSACTION_NONE: integer
const TRANSACTION_READ_UNCOMMITTED: integer
const TRANSACTION_READ_COMMITTED: integer
const TRANSACTION_REPEATABLE_READ: integer
const TRANSACTION_SERIALIZABLE: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Connection$Type = ($Connection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Connection_ = $Connection$Type;
}}
declare module "packages/java/sql/$ParameterMetaData" {
import {$Wrapper, $Wrapper$Type} from "packages/java/sql/$Wrapper"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $ParameterMetaData extends $Wrapper {

 "getParameterCount"(): integer
 "isSigned"(arg0: integer): boolean
 "getPrecision"(arg0: integer): integer
 "getScale"(arg0: integer): integer
 "getParameterMode"(arg0: integer): integer
 "isNullable"(arg0: integer): integer
 "getParameterType"(arg0: integer): integer
 "getParameterTypeName"(arg0: integer): string
 "getParameterClassName"(arg0: integer): string
 "unwrap"<T>(arg0: $Class$Type<(T)>): T
 "isWrapperFor"(arg0: $Class$Type<(any)>): boolean
}

export namespace $ParameterMetaData {
const parameterNoNulls: integer
const parameterNullable: integer
const parameterNullableUnknown: integer
const parameterModeUnknown: integer
const parameterModeIn: integer
const parameterModeInOut: integer
const parameterModeOut: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParameterMetaData$Type = ($ParameterMetaData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParameterMetaData_ = $ParameterMetaData$Type;
}}
declare module "packages/java/sql/$PreparedStatement" {
import {$SQLXML, $SQLXML$Type} from "packages/java/sql/$SQLXML"
import {$ResultSet, $ResultSet$Type} from "packages/java/sql/$ResultSet"
import {$Date, $Date$Type} from "packages/java/sql/$Date"
import {$NClob, $NClob$Type} from "packages/java/sql/$NClob"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$BigDecimal, $BigDecimal$Type} from "packages/java/math/$BigDecimal"
import {$RowId, $RowId$Type} from "packages/java/sql/$RowId"
import {$Time, $Time$Type} from "packages/java/sql/$Time"
import {$ResultSetMetaData, $ResultSetMetaData$Type} from "packages/java/sql/$ResultSetMetaData"
import {$Timestamp, $Timestamp$Type} from "packages/java/sql/$Timestamp"
import {$Connection, $Connection$Type} from "packages/java/sql/$Connection"
import {$Calendar, $Calendar$Type} from "packages/java/util/$Calendar"
import {$SQLType, $SQLType$Type} from "packages/java/sql/$SQLType"
import {$Clob, $Clob$Type} from "packages/java/sql/$Clob"
import {$Ref, $Ref$Type} from "packages/java/sql/$Ref"
import {$Blob, $Blob$Type} from "packages/java/sql/$Blob"
import {$Array, $Array$Type} from "packages/java/sql/$Array"
import {$ParameterMetaData, $ParameterMetaData$Type} from "packages/java/sql/$ParameterMetaData"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$SQLWarning, $SQLWarning$Type} from "packages/java/sql/$SQLWarning"
import {$URL, $URL$Type} from "packages/java/net/$URL"
import {$Statement, $Statement$Type} from "packages/java/sql/$Statement"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"

export interface $PreparedStatement extends $Statement {

 "execute"(): boolean
 "setBoolean"(arg0: integer, arg1: boolean): void
 "setByte"(arg0: integer, arg1: byte): void
 "setShort"(arg0: integer, arg1: short): void
 "setInt"(arg0: integer, arg1: integer): void
 "setLong"(arg0: integer, arg1: long): void
 "setFloat"(arg0: integer, arg1: float): void
 "setDouble"(arg0: integer, arg1: double): void
 "setURL"(arg0: integer, arg1: $URL$Type): void
 "setArray"(arg0: integer, arg1: $Array$Type): void
 "setTime"(arg0: integer, arg1: $Time$Type): void
 "setTime"(arg0: integer, arg1: $Time$Type, arg2: $Calendar$Type): void
 "setDate"(arg0: integer, arg1: $Date$Type): void
 "setDate"(arg0: integer, arg1: $Date$Type, arg2: $Calendar$Type): void
 "clearParameters"(): void
 "setCharacterStream"(arg0: integer, arg1: $Reader$Type, arg2: integer): void
 "setCharacterStream"(arg0: integer, arg1: $Reader$Type, arg2: long): void
 "setCharacterStream"(arg0: integer, arg1: $Reader$Type): void
 "getParameterMetaData"(): $ParameterMetaData
 "setObject"(arg0: integer, arg1: any, arg2: $SQLType$Type, arg3: integer): void
 "setObject"(arg0: integer, arg1: any): void
 "setObject"(arg0: integer, arg1: any, arg2: $SQLType$Type): void
 "setObject"(arg0: integer, arg1: any, arg2: integer): void
 "setObject"(arg0: integer, arg1: any, arg2: integer, arg3: integer): void
 "getMetaData"(): $ResultSetMetaData
 "setString"(arg0: integer, arg1: string): void
 "setClob"(arg0: integer, arg1: $Clob$Type): void
 "setClob"(arg0: integer, arg1: $Reader$Type): void
 "setClob"(arg0: integer, arg1: $Reader$Type, arg2: long): void
 "executeQuery"(): $ResultSet
 "executeUpdate"(): integer
 "addBatch"(): void
 "executeLargeUpdate"(): long
 "setNCharacterStream"(arg0: integer, arg1: $Reader$Type): void
 "setNCharacterStream"(arg0: integer, arg1: $Reader$Type, arg2: long): void
 "setTimestamp"(arg0: integer, arg1: $Timestamp$Type): void
 "setTimestamp"(arg0: integer, arg1: $Timestamp$Type, arg2: $Calendar$Type): void
 "setNull"(arg0: integer, arg1: integer): void
 "setNull"(arg0: integer, arg1: integer, arg2: string): void
 "setBytes"(arg0: integer, arg1: (byte)[]): void
 "setBinaryStream"(arg0: integer, arg1: $InputStream$Type, arg2: long): void
 "setBinaryStream"(arg0: integer, arg1: $InputStream$Type, arg2: integer): void
 "setBinaryStream"(arg0: integer, arg1: $InputStream$Type): void
 "setAsciiStream"(arg0: integer, arg1: $InputStream$Type, arg2: long): void
 "setAsciiStream"(arg0: integer, arg1: $InputStream$Type): void
 "setAsciiStream"(arg0: integer, arg1: $InputStream$Type, arg2: integer): void
 "setBigDecimal"(arg0: integer, arg1: $BigDecimal$Type): void
 "setRef"(arg0: integer, arg1: $Ref$Type): void
 "setSQLXML"(arg0: integer, arg1: $SQLXML$Type): void
 "setNString"(arg0: integer, arg1: string): void
 "setNClob"(arg0: integer, arg1: $Reader$Type, arg2: long): void
 "setNClob"(arg0: integer, arg1: $Reader$Type): void
 "setNClob"(arg0: integer, arg1: $NClob$Type): void
/**
 * 
 * @deprecated
 */
 "setUnicodeStream"(arg0: integer, arg1: $InputStream$Type, arg2: integer): void
 "setBlob"(arg0: integer, arg1: $Blob$Type): void
 "setBlob"(arg0: integer, arg1: $InputStream$Type): void
 "setBlob"(arg0: integer, arg1: $InputStream$Type, arg2: long): void
 "setRowId"(arg0: integer, arg1: $RowId$Type): void
 "getLargeUpdateCount"(): long
 "setEscapeProcessing"(arg0: boolean): void
 "getResultSetConcurrency"(): integer
 "isCloseOnCompletion"(): boolean
 "enquoteNCharLiteral"(arg0: string): string
 "execute"(arg0: string): boolean
 "execute"(arg0: string, arg1: integer): boolean
 "execute"(arg0: string, arg1: (string)[]): boolean
 "execute"(arg0: string, arg1: (integer)[]): boolean
 "close"(): void
 "cancel"(): void
 "getConnection"(): $Connection
 "isClosed"(): boolean
 "getWarnings"(): $SQLWarning
 "setMaxRows"(arg0: integer): void
 "setCursorName"(arg0: string): void
 "getResultSet"(): $ResultSet
 "setMaxFieldSize"(arg0: integer): void
 "executeQuery"(arg0: string): $ResultSet
 "getMaxFieldSize"(): integer
 "setQueryTimeout"(arg0: integer): void
 "executeUpdate"(arg0: string, arg1: (string)[]): integer
 "executeUpdate"(arg0: string, arg1: integer): integer
 "executeUpdate"(arg0: string, arg1: (integer)[]): integer
 "executeUpdate"(arg0: string): integer
 "getQueryTimeout"(): integer
 "getUpdateCount"(): integer
 "getMoreResults"(): boolean
 "getMoreResults"(arg0: integer): boolean
 "getResultSetType"(): integer
 "addBatch"(arg0: string): void
 "setLargeMaxRows"(arg0: long): void
 "executeLargeBatch"(): (long)[]
 "clearBatch"(): void
 "getLargeMaxRows"(): long
 "getGeneratedKeys"(): $ResultSet
 "executeBatch"(): (integer)[]
 "closeOnCompletion"(): void
 "setPoolable"(arg0: boolean): void
 "isPoolable"(): boolean
 "executeLargeUpdate"(arg0: string): long
 "executeLargeUpdate"(arg0: string, arg1: (string)[]): long
 "executeLargeUpdate"(arg0: string, arg1: (integer)[]): long
 "executeLargeUpdate"(arg0: string, arg1: integer): long
 "enquoteLiteral"(arg0: string): string
 "isSimpleIdentifier"(arg0: string): boolean
 "enquoteIdentifier"(arg0: string, arg1: boolean): string
 "getFetchSize"(): integer
 "setFetchSize"(arg0: integer): void
 "clearWarnings"(): void
 "setFetchDirection"(arg0: integer): void
 "getFetchDirection"(): integer
 "getResultSetHoldability"(): integer
 "getMaxRows"(): integer
 "unwrap"<T>(arg0: $Class$Type<(T)>): T
 "isWrapperFor"(arg0: $Class$Type<(any)>): boolean
}

export namespace $PreparedStatement {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PreparedStatement$Type = ($PreparedStatement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PreparedStatement_ = $PreparedStatement$Type;
}}
declare module "packages/java/sql/$Date" {
import {$Date as $Date$0, $Date$Type as $Date$0$Type} from "packages/java/util/$Date"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$LocalDate, $LocalDate$Type} from "packages/java/time/$LocalDate"

export class $Date extends $Date$0 {

/**
 * 
 * @deprecated
 */
constructor(arg0: integer, arg1: integer, arg2: integer)
constructor(arg0: long)

public "toString"(): string
public static "valueOf"(arg0: string): $Date
public static "valueOf"(arg0: $LocalDate$Type): $Date
public "toInstant"(): $Instant
public "setTime"(arg0: long): void
/**
 * 
 * @deprecated
 */
public "getSeconds"(): integer
public "toLocalDate"(): $LocalDate
/**
 * 
 * @deprecated
 */
public "getHours"(): integer
/**
 * 
 * @deprecated
 */
public "setHours"(arg0: integer): void
/**
 * 
 * @deprecated
 */
public "getMinutes"(): integer
/**
 * 
 * @deprecated
 */
public "setMinutes"(arg0: integer): void
/**
 * 
 * @deprecated
 */
public "setSeconds"(arg0: integer): void
set "time"(value: long)
get "seconds"(): integer
get "hours"(): integer
set "hours"(value: integer)
get "minutes"(): integer
set "minutes"(value: integer)
set "seconds"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Date$Type = ($Date);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Date_ = $Date$Type;
}}
declare module "packages/java/sql/$ShardingKey" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ShardingKey {

}

export namespace $ShardingKey {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShardingKey$Type = ($ShardingKey);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShardingKey_ = $ShardingKey$Type;
}}
declare module "packages/java/sql/$SQLException" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Exception, $Exception$Type} from "packages/java/lang/$Exception"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export class $SQLException extends $Exception implements $Iterable<($Throwable)> {

constructor(arg0: $Throwable$Type)
constructor(arg0: string, arg1: $Throwable$Type)
constructor(arg0: string, arg1: string, arg2: $Throwable$Type)
constructor(arg0: string, arg1: string, arg2: integer, arg3: $Throwable$Type)
constructor(arg0: string, arg1: string, arg2: integer)
constructor(arg0: string, arg1: string)
constructor(arg0: string)
constructor()

public "iterator"(): $Iterator<($Throwable)>
public "getErrorCode"(): integer
public "setNextException"(arg0: $SQLException$Type): void
public "getNextException"(): $SQLException
public "getSQLState"(): string
public "spliterator"(): $Spliterator<($Throwable)>
public "forEach"(arg0: $Consumer$Type<(any)>): void
[Symbol.iterator](): IterableIterator<$Throwable>;
get "errorCode"(): integer
set "nextException"(value: $SQLException$Type)
get "nextException"(): $SQLException
get "sQLState"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SQLException$Type = ($SQLException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SQLException_ = $SQLException$Type;
}}
declare module "packages/java/sql/$Savepoint" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Savepoint {

 "getSavepointId"(): integer
 "getSavepointName"(): string
}

export namespace $Savepoint {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Savepoint$Type = ($Savepoint);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Savepoint_ = $Savepoint$Type;
}}
declare module "packages/java/sql/$Time" {
import {$Date, $Date$Type} from "packages/java/util/$Date"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$LocalTime, $LocalTime$Type} from "packages/java/time/$LocalTime"

export class $Time extends $Date {

/**
 * 
 * @deprecated
 */
constructor(arg0: integer, arg1: integer, arg2: integer)
constructor(arg0: long)

public "toString"(): string
public static "valueOf"(arg0: string): $Time
public static "valueOf"(arg0: $LocalTime$Type): $Time
public "toInstant"(): $Instant
/**
 * 
 * @deprecated
 */
public "getYear"(): integer
public "setTime"(arg0: long): void
/**
 * 
 * @deprecated
 */
public "getMonth"(): integer
public "toLocalTime"(): $LocalTime
/**
 * 
 * @deprecated
 */
public "setDate"(arg0: integer): void
/**
 * 
 * @deprecated
 */
public "setMonth"(arg0: integer): void
/**
 * 
 * @deprecated
 */
public "setYear"(arg0: integer): void
/**
 * 
 * @deprecated
 */
public "getDate"(): integer
/**
 * 
 * @deprecated
 */
public "getDay"(): integer
get "year"(): integer
set "time"(value: long)
get "month"(): integer
set "date"(value: integer)
set "month"(value: integer)
set "year"(value: integer)
get "date"(): integer
get "day"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Time$Type = ($Time);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Time_ = $Time$Type;
}}
declare module "packages/java/sql/$Wrapper" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $Wrapper {

 "unwrap"<T>(arg0: $Class$Type<(T)>): T
 "isWrapperFor"(arg0: $Class$Type<(any)>): boolean
}

export namespace $Wrapper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Wrapper$Type = ($Wrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Wrapper_ = $Wrapper$Type;
}}
declare module "packages/java/sql/$RowId" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RowId {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "getBytes"(): (byte)[]
}

export namespace $RowId {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RowId$Type = ($RowId);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RowId_ = $RowId$Type;
}}
declare module "packages/java/sql/$CallableStatement" {
import {$ResultSet, $ResultSet$Type} from "packages/java/sql/$ResultSet"
import {$PreparedStatement, $PreparedStatement$Type} from "packages/java/sql/$PreparedStatement"
import {$NClob, $NClob$Type} from "packages/java/sql/$NClob"
import {$RowId, $RowId$Type} from "packages/java/sql/$RowId"
import {$Time, $Time$Type} from "packages/java/sql/$Time"
import {$ResultSetMetaData, $ResultSetMetaData$Type} from "packages/java/sql/$ResultSetMetaData"
import {$SQLType, $SQLType$Type} from "packages/java/sql/$SQLType"
import {$Blob, $Blob$Type} from "packages/java/sql/$Blob"
import {$Array, $Array$Type} from "packages/java/sql/$Array"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$URL, $URL$Type} from "packages/java/net/$URL"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"
import {$SQLXML, $SQLXML$Type} from "packages/java/sql/$SQLXML"
import {$Date, $Date$Type} from "packages/java/sql/$Date"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$BigDecimal, $BigDecimal$Type} from "packages/java/math/$BigDecimal"
import {$Timestamp, $Timestamp$Type} from "packages/java/sql/$Timestamp"
import {$Connection, $Connection$Type} from "packages/java/sql/$Connection"
import {$Calendar, $Calendar$Type} from "packages/java/util/$Calendar"
import {$Clob, $Clob$Type} from "packages/java/sql/$Clob"
import {$Ref, $Ref$Type} from "packages/java/sql/$Ref"
import {$ParameterMetaData, $ParameterMetaData$Type} from "packages/java/sql/$ParameterMetaData"
import {$SQLWarning, $SQLWarning$Type} from "packages/java/sql/$SQLWarning"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $CallableStatement extends $PreparedStatement {

 "getBoolean"(arg0: string): boolean
 "getBoolean"(arg0: integer): boolean
 "getByte"(arg0: integer): byte
 "getByte"(arg0: string): byte
 "getShort"(arg0: integer): short
 "getShort"(arg0: string): short
 "getInt"(arg0: integer): integer
 "getInt"(arg0: string): integer
 "getLong"(arg0: string): long
 "getLong"(arg0: integer): long
 "getFloat"(arg0: integer): float
 "getFloat"(arg0: string): float
 "getDouble"(arg0: string): double
 "getDouble"(arg0: integer): double
 "getBytes"(arg0: string): (byte)[]
 "getBytes"(arg0: integer): (byte)[]
 "setBoolean"(arg0: string, arg1: boolean): void
 "setByte"(arg0: string, arg1: byte): void
 "setShort"(arg0: string, arg1: short): void
 "setInt"(arg0: string, arg1: integer): void
 "setLong"(arg0: string, arg1: long): void
 "setFloat"(arg0: string, arg1: float): void
 "setDouble"(arg0: string, arg1: double): void
 "getObject"(arg0: integer, arg1: $Map$Type<(string), ($Class$Type<(any)>)>): any
 "getObject"(arg0: integer): any
 "getObject"<T>(arg0: string, arg1: $Class$Type<(T)>): T
 "getObject"(arg0: string): any
 "getObject"(arg0: string, arg1: $Map$Type<(string), ($Class$Type<(any)>)>): any
 "getObject"<T>(arg0: integer, arg1: $Class$Type<(T)>): T
 "getRef"(arg0: string): $Ref
 "getRef"(arg0: integer): $Ref
 "getArray"(arg0: integer): $Array
 "getArray"(arg0: string): $Array
 "setURL"(arg0: string, arg1: $URL$Type): void
 "getTimestamp"(arg0: integer, arg1: $Calendar$Type): $Timestamp
 "getTimestamp"(arg0: string, arg1: $Calendar$Type): $Timestamp
 "getTimestamp"(arg0: string): $Timestamp
 "getTimestamp"(arg0: integer): $Timestamp
 "getString"(arg0: string): string
 "getString"(arg0: integer): string
 "getBigDecimal"(arg0: string): $BigDecimal
/**
 * 
 * @deprecated
 */
 "getBigDecimal"(arg0: integer, arg1: integer): $BigDecimal
 "getBigDecimal"(arg0: integer): $BigDecimal
 "getTime"(arg0: string): $Time
 "getTime"(arg0: string, arg1: $Calendar$Type): $Time
 "getTime"(arg0: integer): $Time
 "getTime"(arg0: integer, arg1: $Calendar$Type): $Time
 "setTime"(arg0: string, arg1: $Time$Type, arg2: $Calendar$Type): void
 "setTime"(arg0: string, arg1: $Time$Type): void
 "setDate"(arg0: string, arg1: $Date$Type, arg2: $Calendar$Type): void
 "setDate"(arg0: string, arg1: $Date$Type): void
 "getDate"(arg0: integer, arg1: $Calendar$Type): $Date
 "getDate"(arg0: integer): $Date
 "getDate"(arg0: string): $Date
 "getDate"(arg0: string, arg1: $Calendar$Type): $Date
 "getURL"(arg0: string): $URL
 "getURL"(arg0: integer): $URL
 "setCharacterStream"(arg0: string, arg1: $Reader$Type, arg2: long): void
 "setCharacterStream"(arg0: string, arg1: $Reader$Type, arg2: integer): void
 "setCharacterStream"(arg0: string, arg1: $Reader$Type): void
 "getCharacterStream"(arg0: integer): $Reader
 "getCharacterStream"(arg0: string): $Reader
 "setObject"(arg0: string, arg1: any): void
 "setObject"(arg0: string, arg1: any, arg2: integer): void
 "setObject"(arg0: string, arg1: any, arg2: integer, arg3: integer): void
 "setObject"(arg0: string, arg1: any, arg2: $SQLType$Type, arg3: integer): void
 "setObject"(arg0: string, arg1: any, arg2: $SQLType$Type): void
 "setString"(arg0: string, arg1: string): void
 "setClob"(arg0: string, arg1: $Reader$Type, arg2: long): void
 "setClob"(arg0: string, arg1: $Reader$Type): void
 "setClob"(arg0: string, arg1: $Clob$Type): void
 "setNCharacterStream"(arg0: string, arg1: $Reader$Type, arg2: long): void
 "setNCharacterStream"(arg0: string, arg1: $Reader$Type): void
 "wasNull"(): boolean
 "getSQLXML"(arg0: string): $SQLXML
 "getSQLXML"(arg0: integer): $SQLXML
 "getNClob"(arg0: string): $NClob
 "getNClob"(arg0: integer): $NClob
 "getRowId"(arg0: integer): $RowId
 "getRowId"(arg0: string): $RowId
 "getClob"(arg0: string): $Clob
 "getClob"(arg0: integer): $Clob
 "getNString"(arg0: integer): string
 "getNString"(arg0: string): string
 "setTimestamp"(arg0: string, arg1: $Timestamp$Type): void
 "setTimestamp"(arg0: string, arg1: $Timestamp$Type, arg2: $Calendar$Type): void
 "getNCharacterStream"(arg0: string): $Reader
 "getNCharacterStream"(arg0: integer): $Reader
 "setNull"(arg0: string, arg1: integer, arg2: string): void
 "setNull"(arg0: string, arg1: integer): void
 "registerOutParameter"(arg0: string, arg1: $SQLType$Type, arg2: integer): void
 "registerOutParameter"(arg0: integer, arg1: $SQLType$Type, arg2: string): void
 "registerOutParameter"(arg0: string, arg1: $SQLType$Type): void
 "registerOutParameter"(arg0: string, arg1: $SQLType$Type, arg2: string): void
 "registerOutParameter"(arg0: integer, arg1: $SQLType$Type, arg2: integer): void
 "registerOutParameter"(arg0: integer, arg1: $SQLType$Type): void
 "registerOutParameter"(arg0: integer, arg1: integer, arg2: integer): void
 "registerOutParameter"(arg0: string, arg1: integer, arg2: string): void
 "registerOutParameter"(arg0: string, arg1: integer, arg2: integer): void
 "registerOutParameter"(arg0: string, arg1: integer): void
 "registerOutParameter"(arg0: integer, arg1: integer, arg2: string): void
 "registerOutParameter"(arg0: integer, arg1: integer): void
 "setBytes"(arg0: string, arg1: (byte)[]): void
 "setBinaryStream"(arg0: string, arg1: $InputStream$Type): void
 "setBinaryStream"(arg0: string, arg1: $InputStream$Type, arg2: long): void
 "setBinaryStream"(arg0: string, arg1: $InputStream$Type, arg2: integer): void
 "getBlob"(arg0: string): $Blob
 "getBlob"(arg0: integer): $Blob
 "setAsciiStream"(arg0: string, arg1: $InputStream$Type, arg2: integer): void
 "setAsciiStream"(arg0: string, arg1: $InputStream$Type, arg2: long): void
 "setAsciiStream"(arg0: string, arg1: $InputStream$Type): void
 "setBigDecimal"(arg0: string, arg1: $BigDecimal$Type): void
 "setSQLXML"(arg0: string, arg1: $SQLXML$Type): void
 "setNString"(arg0: string, arg1: string): void
 "setNClob"(arg0: string, arg1: $Reader$Type): void
 "setNClob"(arg0: string, arg1: $Reader$Type, arg2: long): void
 "setNClob"(arg0: string, arg1: $NClob$Type): void
 "setBlob"(arg0: string, arg1: $Blob$Type): void
 "setBlob"(arg0: string, arg1: $InputStream$Type): void
 "setBlob"(arg0: string, arg1: $InputStream$Type, arg2: long): void
 "setRowId"(arg0: string, arg1: $RowId$Type): void
 "execute"(): boolean
 "setBoolean"(arg0: integer, arg1: boolean): void
 "setByte"(arg0: integer, arg1: byte): void
 "setShort"(arg0: integer, arg1: short): void
 "setInt"(arg0: integer, arg1: integer): void
 "setLong"(arg0: integer, arg1: long): void
 "setFloat"(arg0: integer, arg1: float): void
 "setDouble"(arg0: integer, arg1: double): void
 "setURL"(arg0: integer, arg1: $URL$Type): void
 "setArray"(arg0: integer, arg1: $Array$Type): void
 "setTime"(arg0: integer, arg1: $Time$Type): void
 "setTime"(arg0: integer, arg1: $Time$Type, arg2: $Calendar$Type): void
 "setDate"(arg0: integer, arg1: $Date$Type): void
 "setDate"(arg0: integer, arg1: $Date$Type, arg2: $Calendar$Type): void
 "clearParameters"(): void
 "setCharacterStream"(arg0: integer, arg1: $Reader$Type, arg2: integer): void
 "setCharacterStream"(arg0: integer, arg1: $Reader$Type, arg2: long): void
 "setCharacterStream"(arg0: integer, arg1: $Reader$Type): void
 "getParameterMetaData"(): $ParameterMetaData
 "setObject"(arg0: integer, arg1: any, arg2: $SQLType$Type, arg3: integer): void
 "setObject"(arg0: integer, arg1: any): void
 "setObject"(arg0: integer, arg1: any, arg2: $SQLType$Type): void
 "setObject"(arg0: integer, arg1: any, arg2: integer): void
 "setObject"(arg0: integer, arg1: any, arg2: integer, arg3: integer): void
 "getMetaData"(): $ResultSetMetaData
 "setString"(arg0: integer, arg1: string): void
 "setClob"(arg0: integer, arg1: $Clob$Type): void
 "setClob"(arg0: integer, arg1: $Reader$Type): void
 "setClob"(arg0: integer, arg1: $Reader$Type, arg2: long): void
 "executeQuery"(): $ResultSet
 "executeUpdate"(): integer
 "addBatch"(): void
 "executeLargeUpdate"(): long
 "setNCharacterStream"(arg0: integer, arg1: $Reader$Type): void
 "setNCharacterStream"(arg0: integer, arg1: $Reader$Type, arg2: long): void
 "setTimestamp"(arg0: integer, arg1: $Timestamp$Type): void
 "setTimestamp"(arg0: integer, arg1: $Timestamp$Type, arg2: $Calendar$Type): void
 "setNull"(arg0: integer, arg1: integer): void
 "setNull"(arg0: integer, arg1: integer, arg2: string): void
 "setBytes"(arg0: integer, arg1: (byte)[]): void
 "setBinaryStream"(arg0: integer, arg1: $InputStream$Type, arg2: long): void
 "setBinaryStream"(arg0: integer, arg1: $InputStream$Type, arg2: integer): void
 "setBinaryStream"(arg0: integer, arg1: $InputStream$Type): void
 "setAsciiStream"(arg0: integer, arg1: $InputStream$Type, arg2: long): void
 "setAsciiStream"(arg0: integer, arg1: $InputStream$Type): void
 "setAsciiStream"(arg0: integer, arg1: $InputStream$Type, arg2: integer): void
 "setBigDecimal"(arg0: integer, arg1: $BigDecimal$Type): void
 "setRef"(arg0: integer, arg1: $Ref$Type): void
 "setSQLXML"(arg0: integer, arg1: $SQLXML$Type): void
 "setNString"(arg0: integer, arg1: string): void
 "setNClob"(arg0: integer, arg1: $Reader$Type, arg2: long): void
 "setNClob"(arg0: integer, arg1: $Reader$Type): void
 "setNClob"(arg0: integer, arg1: $NClob$Type): void
/**
 * 
 * @deprecated
 */
 "setUnicodeStream"(arg0: integer, arg1: $InputStream$Type, arg2: integer): void
 "setBlob"(arg0: integer, arg1: $Blob$Type): void
 "setBlob"(arg0: integer, arg1: $InputStream$Type): void
 "setBlob"(arg0: integer, arg1: $InputStream$Type, arg2: long): void
 "setRowId"(arg0: integer, arg1: $RowId$Type): void
 "getLargeUpdateCount"(): long
 "setEscapeProcessing"(arg0: boolean): void
 "getResultSetConcurrency"(): integer
 "isCloseOnCompletion"(): boolean
 "enquoteNCharLiteral"(arg0: string): string
 "execute"(arg0: string): boolean
 "execute"(arg0: string, arg1: integer): boolean
 "execute"(arg0: string, arg1: (string)[]): boolean
 "execute"(arg0: string, arg1: (integer)[]): boolean
 "close"(): void
 "cancel"(): void
 "getConnection"(): $Connection
 "isClosed"(): boolean
 "getWarnings"(): $SQLWarning
 "setMaxRows"(arg0: integer): void
 "setCursorName"(arg0: string): void
 "getResultSet"(): $ResultSet
 "setMaxFieldSize"(arg0: integer): void
 "executeQuery"(arg0: string): $ResultSet
 "getMaxFieldSize"(): integer
 "setQueryTimeout"(arg0: integer): void
 "executeUpdate"(arg0: string, arg1: (string)[]): integer
 "executeUpdate"(arg0: string, arg1: integer): integer
 "executeUpdate"(arg0: string, arg1: (integer)[]): integer
 "executeUpdate"(arg0: string): integer
 "getQueryTimeout"(): integer
 "getUpdateCount"(): integer
 "getMoreResults"(): boolean
 "getMoreResults"(arg0: integer): boolean
 "getResultSetType"(): integer
 "addBatch"(arg0: string): void
 "setLargeMaxRows"(arg0: long): void
 "executeLargeBatch"(): (long)[]
 "clearBatch"(): void
 "getLargeMaxRows"(): long
 "getGeneratedKeys"(): $ResultSet
 "executeBatch"(): (integer)[]
 "closeOnCompletion"(): void
 "setPoolable"(arg0: boolean): void
 "isPoolable"(): boolean
 "executeLargeUpdate"(arg0: string): long
 "executeLargeUpdate"(arg0: string, arg1: (string)[]): long
 "executeLargeUpdate"(arg0: string, arg1: (integer)[]): long
 "executeLargeUpdate"(arg0: string, arg1: integer): long
 "enquoteLiteral"(arg0: string): string
 "isSimpleIdentifier"(arg0: string): boolean
 "enquoteIdentifier"(arg0: string, arg1: boolean): string
 "getFetchSize"(): integer
 "setFetchSize"(arg0: integer): void
 "clearWarnings"(): void
 "setFetchDirection"(arg0: integer): void
 "getFetchDirection"(): integer
 "getResultSetHoldability"(): integer
 "getMaxRows"(): integer
 "unwrap"<T>(arg0: $Class$Type<(T)>): T
 "isWrapperFor"(arg0: $Class$Type<(any)>): boolean
}

export namespace $CallableStatement {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CallableStatement$Type = ($CallableStatement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CallableStatement_ = $CallableStatement$Type;
}}
declare module "packages/java/sql/$Blob" {
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"

export interface $Blob {

 "length"(): long
 "position"(arg0: $Blob$Type, arg1: long): long
 "position"(arg0: (byte)[], arg1: long): long
 "getBytes"(arg0: long, arg1: integer): (byte)[]
 "free"(): void
 "truncate"(arg0: long): void
 "getBinaryStream"(arg0: long, arg1: long): $InputStream
 "getBinaryStream"(): $InputStream
 "setBytes"(arg0: long, arg1: (byte)[]): integer
 "setBytes"(arg0: long, arg1: (byte)[], arg2: integer, arg3: integer): integer
 "setBinaryStream"(arg0: long): $OutputStream
}

export namespace $Blob {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Blob$Type = ($Blob);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Blob_ = $Blob$Type;
}}
declare module "packages/java/sql/$Timestamp" {
import {$Date, $Date$Type} from "packages/java/util/$Date"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$LocalDateTime, $LocalDateTime$Type} from "packages/java/time/$LocalDateTime"

export class $Timestamp extends $Date {

/**
 * 
 * @deprecated
 */
constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer)
constructor(arg0: long)

public "equals"(arg0: $Timestamp$Type): boolean
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $Timestamp$Type): integer
public "compareTo"(arg0: $Date$Type): integer
public static "valueOf"(arg0: $LocalDateTime$Type): $Timestamp
public static "valueOf"(arg0: string): $Timestamp
public static "from"(arg0: $Instant$Type): $Timestamp
public "before"(arg0: $Timestamp$Type): boolean
public "after"(arg0: $Timestamp$Type): boolean
public "toInstant"(): $Instant
public "getTime"(): long
public "setTime"(arg0: long): void
public "toLocalDateTime"(): $LocalDateTime
public "getNanos"(): integer
public "setNanos"(arg0: integer): void
get "time"(): long
set "time"(value: long)
get "nanos"(): integer
set "nanos"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Timestamp$Type = ($Timestamp);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Timestamp_ = $Timestamp$Type;
}}
declare module "packages/java/sql/$ResultSet" {
import {$Wrapper, $Wrapper$Type} from "packages/java/sql/$Wrapper"
import {$SQLXML, $SQLXML$Type} from "packages/java/sql/$SQLXML"
import {$Date, $Date$Type} from "packages/java/sql/$Date"
import {$NClob, $NClob$Type} from "packages/java/sql/$NClob"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$BigDecimal, $BigDecimal$Type} from "packages/java/math/$BigDecimal"
import {$RowId, $RowId$Type} from "packages/java/sql/$RowId"
import {$Time, $Time$Type} from "packages/java/sql/$Time"
import {$ResultSetMetaData, $ResultSetMetaData$Type} from "packages/java/sql/$ResultSetMetaData"
import {$Timestamp, $Timestamp$Type} from "packages/java/sql/$Timestamp"
import {$Calendar, $Calendar$Type} from "packages/java/util/$Calendar"
import {$SQLType, $SQLType$Type} from "packages/java/sql/$SQLType"
import {$Clob, $Clob$Type} from "packages/java/sql/$Clob"
import {$Ref, $Ref$Type} from "packages/java/sql/$Ref"
import {$Blob, $Blob$Type} from "packages/java/sql/$Blob"
import {$Array, $Array$Type} from "packages/java/sql/$Array"
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"
import {$SQLWarning, $SQLWarning$Type} from "packages/java/sql/$SQLWarning"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$URL, $URL$Type} from "packages/java/net/$URL"
import {$Statement, $Statement$Type} from "packages/java/sql/$Statement"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $ResultSet extends $Wrapper, $AutoCloseable {

 "updateBytes"(arg0: integer, arg1: (byte)[]): void
 "updateBytes"(arg0: string, arg1: (byte)[]): void
 "getBoolean"(arg0: string): boolean
 "getBoolean"(arg0: integer): boolean
 "getByte"(arg0: string): byte
 "getByte"(arg0: integer): byte
 "getShort"(arg0: string): short
 "getShort"(arg0: integer): short
 "getInt"(arg0: integer): integer
 "getInt"(arg0: string): integer
 "getLong"(arg0: string): long
 "getLong"(arg0: integer): long
 "getFloat"(arg0: string): float
 "getFloat"(arg0: integer): float
 "getDouble"(arg0: integer): double
 "getDouble"(arg0: string): double
 "getBytes"(arg0: string): (byte)[]
 "getBytes"(arg0: integer): (byte)[]
 "next"(): boolean
 "last"(): boolean
 "first"(): boolean
 "close"(): void
 "getType"(): integer
 "getObject"(arg0: integer): any
 "getObject"<T>(arg0: integer, arg1: $Class$Type<(T)>): T
 "getObject"<T>(arg0: string, arg1: $Class$Type<(T)>): T
 "getObject"(arg0: string): any
 "getObject"(arg0: integer, arg1: $Map$Type<(string), ($Class$Type<(any)>)>): any
 "getObject"(arg0: string, arg1: $Map$Type<(string), ($Class$Type<(any)>)>): any
 "getRef"(arg0: string): $Ref
 "getRef"(arg0: integer): $Ref
 "previous"(): boolean
 "getArray"(arg0: integer): $Array
 "getArray"(arg0: string): $Array
 "absolute"(arg0: integer): boolean
 "getTimestamp"(arg0: integer, arg1: $Calendar$Type): $Timestamp
 "getTimestamp"(arg0: string, arg1: $Calendar$Type): $Timestamp
 "getTimestamp"(arg0: string): $Timestamp
 "getTimestamp"(arg0: integer): $Timestamp
 "getString"(arg0: string): string
 "getString"(arg0: integer): string
 "getBigDecimal"(arg0: string): $BigDecimal
 "getBigDecimal"(arg0: integer): $BigDecimal
/**
 * 
 * @deprecated
 */
 "getBigDecimal"(arg0: string, arg1: integer): $BigDecimal
/**
 * 
 * @deprecated
 */
 "getBigDecimal"(arg0: integer, arg1: integer): $BigDecimal
 "getTime"(arg0: integer): $Time
 "getTime"(arg0: integer, arg1: $Calendar$Type): $Time
 "getTime"(arg0: string): $Time
 "getTime"(arg0: string, arg1: $Calendar$Type): $Time
 "updateTime"(arg0: string, arg1: $Time$Type): void
 "updateTime"(arg0: integer, arg1: $Time$Type): void
 "getDate"(arg0: string, arg1: $Calendar$Type): $Date
 "getDate"(arg0: string): $Date
 "getDate"(arg0: integer, arg1: $Calendar$Type): $Date
 "getDate"(arg0: integer): $Date
 "getURL"(arg0: string): $URL
 "getURL"(arg0: integer): $URL
 "relative"(arg0: integer): boolean
 "isClosed"(): boolean
 "getCharacterStream"(arg0: integer): $Reader
 "getCharacterStream"(arg0: string): $Reader
 "getMetaData"(): $ResultSetMetaData
 "getRow"(): integer
 "getWarnings"(): $SQLWarning
 "getCursorName"(): string
 "afterLast"(): void
 "isBeforeFirst"(): boolean
/**
 * 
 * @deprecated
 */
 "getUnicodeStream"(arg0: integer): $InputStream
/**
 * 
 * @deprecated
 */
 "getUnicodeStream"(arg0: string): $InputStream
 "wasNull"(): boolean
 "rowInserted"(): boolean
 "beforeFirst"(): void
 "rowDeleted"(): boolean
 "getBinaryStream"(arg0: string): $InputStream
 "getBinaryStream"(arg0: integer): $InputStream
 "getFetchSize"(): integer
 "setFetchSize"(arg0: integer): void
 "getConcurrency"(): integer
 "clearWarnings"(): void
 "isAfterLast"(): boolean
 "setFetchDirection"(arg0: integer): void
 "findColumn"(arg0: string): integer
 "getFetchDirection"(): integer
 "rowUpdated"(): boolean
 "getAsciiStream"(arg0: integer): $InputStream
 "getAsciiStream"(arg0: string): $InputStream
 "updateShort"(arg0: string, arg1: short): void
 "updateShort"(arg0: integer, arg1: short): void
 "moveToInsertRow"(): void
 "updateBoolean"(arg0: integer, arg1: boolean): void
 "updateBoolean"(arg0: string, arg1: boolean): void
 "updateDate"(arg0: integer, arg1: $Date$Type): void
 "updateDate"(arg0: string, arg1: $Date$Type): void
 "cancelRowUpdates"(): void
 "updateAsciiStream"(arg0: string, arg1: $InputStream$Type, arg2: integer): void
 "updateAsciiStream"(arg0: string, arg1: $InputStream$Type): void
 "updateAsciiStream"(arg0: integer, arg1: $InputStream$Type, arg2: integer): void
 "updateAsciiStream"(arg0: string, arg1: $InputStream$Type, arg2: long): void
 "updateAsciiStream"(arg0: integer, arg1: $InputStream$Type, arg2: long): void
 "updateAsciiStream"(arg0: integer, arg1: $InputStream$Type): void
 "updateTimestamp"(arg0: integer, arg1: $Timestamp$Type): void
 "updateTimestamp"(arg0: string, arg1: $Timestamp$Type): void
 "deleteRow"(): void
 "updateFloat"(arg0: string, arg1: float): void
 "updateFloat"(arg0: integer, arg1: float): void
 "updateDouble"(arg0: integer, arg1: double): void
 "updateDouble"(arg0: string, arg1: double): void
 "moveToCurrentRow"(): void
 "updateByte"(arg0: integer, arg1: byte): void
 "updateByte"(arg0: string, arg1: byte): void
 "updateObject"(arg0: integer, arg1: any, arg2: $SQLType$Type): void
 "updateObject"(arg0: integer, arg1: any, arg2: $SQLType$Type, arg3: integer): void
 "updateObject"(arg0: string, arg1: any, arg2: $SQLType$Type, arg3: integer): void
 "updateObject"(arg0: string, arg1: any): void
 "updateObject"(arg0: integer, arg1: any, arg2: integer): void
 "updateObject"(arg0: string, arg1: any, arg2: integer): void
 "updateObject"(arg0: integer, arg1: any): void
 "updateObject"(arg0: string, arg1: any, arg2: $SQLType$Type): void
 "updateRow"(): void
 "refreshRow"(): void
 "insertRow"(): void
 "updateBigDecimal"(arg0: string, arg1: $BigDecimal$Type): void
 "updateBigDecimal"(arg0: integer, arg1: $BigDecimal$Type): void
 "updateLong"(arg0: integer, arg1: long): void
 "updateLong"(arg0: string, arg1: long): void
 "updateNull"(arg0: integer): void
 "updateNull"(arg0: string): void
 "updateString"(arg0: string, arg1: string): void
 "updateString"(arg0: integer, arg1: string): void
 "updateInt"(arg0: integer, arg1: integer): void
 "updateInt"(arg0: string, arg1: integer): void
 "updateBinaryStream"(arg0: string, arg1: $InputStream$Type): void
 "updateBinaryStream"(arg0: integer, arg1: $InputStream$Type, arg2: long): void
 "updateBinaryStream"(arg0: integer, arg1: $InputStream$Type, arg2: integer): void
 "updateBinaryStream"(arg0: integer, arg1: $InputStream$Type): void
 "updateBinaryStream"(arg0: string, arg1: $InputStream$Type, arg2: long): void
 "updateBinaryStream"(arg0: string, arg1: $InputStream$Type, arg2: integer): void
 "getSQLXML"(arg0: string): $SQLXML
 "getSQLXML"(arg0: integer): $SQLXML
 "updateArray"(arg0: integer, arg1: $Array$Type): void
 "updateArray"(arg0: string, arg1: $Array$Type): void
 "updateRef"(arg0: string, arg1: $Ref$Type): void
 "updateRef"(arg0: integer, arg1: $Ref$Type): void
 "updateClob"(arg0: string, arg1: $Clob$Type): void
 "updateClob"(arg0: integer, arg1: $Reader$Type, arg2: long): void
 "updateClob"(arg0: string, arg1: $Reader$Type, arg2: long): void
 "updateClob"(arg0: integer, arg1: $Reader$Type): void
 "updateClob"(arg0: integer, arg1: $Clob$Type): void
 "updateClob"(arg0: string, arg1: $Reader$Type): void
 "updateNClob"(arg0: integer, arg1: $NClob$Type): void
 "updateNClob"(arg0: integer, arg1: $Reader$Type, arg2: long): void
 "updateNClob"(arg0: string, arg1: $Reader$Type, arg2: long): void
 "updateNClob"(arg0: string, arg1: $NClob$Type): void
 "updateNClob"(arg0: string, arg1: $Reader$Type): void
 "updateNClob"(arg0: integer, arg1: $Reader$Type): void
 "updateRowId"(arg0: integer, arg1: $RowId$Type): void
 "updateRowId"(arg0: string, arg1: $RowId$Type): void
 "updateNString"(arg0: string, arg1: string): void
 "updateNString"(arg0: integer, arg1: string): void
 "getNClob"(arg0: integer): $NClob
 "getNClob"(arg0: string): $NClob
 "getHoldability"(): integer
 "updateSQLXML"(arg0: string, arg1: $SQLXML$Type): void
 "updateSQLXML"(arg0: integer, arg1: $SQLXML$Type): void
 "getRowId"(arg0: string): $RowId
 "getRowId"(arg0: integer): $RowId
 "updateBlob"(arg0: string, arg1: $InputStream$Type, arg2: long): void
 "updateBlob"(arg0: string, arg1: $Blob$Type): void
 "updateBlob"(arg0: integer, arg1: $Blob$Type): void
 "updateBlob"(arg0: string, arg1: $InputStream$Type): void
 "updateBlob"(arg0: integer, arg1: $InputStream$Type, arg2: long): void
 "updateBlob"(arg0: integer, arg1: $InputStream$Type): void
 "getClob"(arg0: string): $Clob
 "getClob"(arg0: integer): $Clob
 "getNString"(arg0: integer): string
 "getNString"(arg0: string): string
 "isFirst"(): boolean
 "isLast"(): boolean
 "getStatement"(): $Statement
 "updateCharacterStream"(arg0: integer, arg1: $Reader$Type, arg2: integer): void
 "updateCharacterStream"(arg0: string, arg1: $Reader$Type, arg2: integer): void
 "updateCharacterStream"(arg0: string, arg1: $Reader$Type, arg2: long): void
 "updateCharacterStream"(arg0: integer, arg1: $Reader$Type, arg2: long): void
 "updateCharacterStream"(arg0: integer, arg1: $Reader$Type): void
 "updateCharacterStream"(arg0: string, arg1: $Reader$Type): void
 "updateNCharacterStream"(arg0: integer, arg1: $Reader$Type, arg2: long): void
 "updateNCharacterStream"(arg0: string, arg1: $Reader$Type): void
 "updateNCharacterStream"(arg0: string, arg1: $Reader$Type, arg2: long): void
 "updateNCharacterStream"(arg0: integer, arg1: $Reader$Type): void
 "getNCharacterStream"(arg0: integer): $Reader
 "getNCharacterStream"(arg0: string): $Reader
 "getBlob"(arg0: integer): $Blob
 "getBlob"(arg0: string): $Blob
 "unwrap"<T>(arg0: $Class$Type<(T)>): T
 "isWrapperFor"(arg0: $Class$Type<(any)>): boolean
}

export namespace $ResultSet {
const FETCH_FORWARD: integer
const FETCH_REVERSE: integer
const FETCH_UNKNOWN: integer
const TYPE_FORWARD_ONLY: integer
const TYPE_SCROLL_INSENSITIVE: integer
const TYPE_SCROLL_SENSITIVE: integer
const CONCUR_READ_ONLY: integer
const CONCUR_UPDATABLE: integer
const HOLD_CURSORS_OVER_COMMIT: integer
const CLOSE_CURSORS_AT_COMMIT: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResultSet$Type = ($ResultSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResultSet_ = $ResultSet$Type;
}}
declare module "packages/java/sql/$SQLWarning" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$SQLException, $SQLException$Type} from "packages/java/sql/$SQLException"

export class $SQLWarning extends $SQLException {

constructor(arg0: $Throwable$Type)
constructor(arg0: string, arg1: $Throwable$Type)
constructor(arg0: string, arg1: string, arg2: $Throwable$Type)
constructor(arg0: string, arg1: string, arg2: integer, arg3: $Throwable$Type)
constructor(arg0: string, arg1: string, arg2: integer)
constructor(arg0: string, arg1: string)
constructor(arg0: string)
constructor()

public "getNextWarning"(): $SQLWarning
public "setNextWarning"(arg0: $SQLWarning$Type): void
get "nextWarning"(): $SQLWarning
set "nextWarning"(value: $SQLWarning$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SQLWarning$Type = ($SQLWarning);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SQLWarning_ = $SQLWarning$Type;
}}
declare module "packages/java/sql/$ResultSetMetaData" {
import {$Wrapper, $Wrapper$Type} from "packages/java/sql/$Wrapper"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $ResultSetMetaData extends $Wrapper {

 "isReadOnly"(arg0: integer): boolean
 "isSigned"(arg0: integer): boolean
 "getPrecision"(arg0: integer): integer
 "isWritable"(arg0: integer): boolean
 "isCaseSensitive"(arg0: integer): boolean
 "getScale"(arg0: integer): integer
 "getColumnName"(arg0: integer): string
 "isNullable"(arg0: integer): integer
 "isSearchable"(arg0: integer): boolean
 "isCurrency"(arg0: integer): boolean
 "isAutoIncrement"(arg0: integer): boolean
 "getColumnLabel"(arg0: integer): string
 "getColumnType"(arg0: integer): integer
 "getCatalogName"(arg0: integer): string
 "getTableName"(arg0: integer): string
 "getColumnClassName"(arg0: integer): string
 "getColumnTypeName"(arg0: integer): string
 "getSchemaName"(arg0: integer): string
 "getColumnDisplaySize"(arg0: integer): integer
 "isDefinitelyWritable"(arg0: integer): boolean
 "getColumnCount"(): integer
 "unwrap"<T>(arg0: $Class$Type<(T)>): T
 "isWrapperFor"(arg0: $Class$Type<(any)>): boolean
}

export namespace $ResultSetMetaData {
const columnNoNulls: integer
const columnNullable: integer
const columnNullableUnknown: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResultSetMetaData$Type = ($ResultSetMetaData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResultSetMetaData_ = $ResultSetMetaData$Type;
}}
declare module "packages/java/sql/$Array" {
import {$ResultSet, $ResultSet$Type} from "packages/java/sql/$ResultSet"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $Array {

 "getArray"(arg0: long, arg1: integer): any
 "getArray"(arg0: $Map$Type<(string), ($Class$Type<(any)>)>): any
 "getArray"(): any
 "getArray"(arg0: long, arg1: integer, arg2: $Map$Type<(string), ($Class$Type<(any)>)>): any
 "free"(): void
 "getBaseType"(): integer
 "getResultSet"(arg0: long, arg1: integer, arg2: $Map$Type<(string), ($Class$Type<(any)>)>): $ResultSet
 "getResultSet"(arg0: long, arg1: integer): $ResultSet
 "getResultSet"(arg0: $Map$Type<(string), ($Class$Type<(any)>)>): $ResultSet
 "getResultSet"(): $ResultSet
 "getBaseTypeName"(): string
}

export namespace $Array {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Array$Type = ($Array);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Array_ = $Array$Type;
}}
