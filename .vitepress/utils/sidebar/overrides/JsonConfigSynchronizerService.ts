import path from 'node:path';
import {
    SidebarItem,
    // EffectiveDirConfig, // May not be needed directly by this service anymore
    JsonFileMetadata,
    // GlobalSidebarConfig, 
    // DirectoryConfig 
} from '../types';
import { FileSystem } from '../shared/FileSystem';
// import { ConfigReaderService } from '../config'; // ConfigReader might not be directly used here
import { normalizePathSeparators, sanitizeTitleForPath } from '../shared/objectUtils';
import { JsonFileHandler, JsonOverrideFileType } from './JsonFileHandler';
import { MetadataManager } from './MetadataManager';
import { SyncEngine } from './SyncEngine';
import { JsonItemSorter } from './JsonItemSorter';
import { PathKeyProcessor } from './PathKeyProcessor';
import { DirectorySignatureManager } from './DirectorySignatureManager';
import { DirectoryMigrationService } from './DirectoryMigrationService';
import { DirectoryCleanupService } from './DirectoryCleanupService';
import { RecursiveSynchronizer } from './RecursiveSynchronizer';
import { KeyMigrationService } from './KeyMigrationService';

/**
 * @file JsonConfigSynchronizerService.ts
 * @description Lightweight orchestrator for JSON config synchronization using focused services.
 */
export class JsonConfigSynchronizerService {
    private readonly docsPath: string;
    private readonly absDocsPath: string;

    // Core services
    private jsonFileHandler: JsonFileHandler;
    private metadataManager: MetadataManager;
    private syncEngine: SyncEngine;
    private jsonItemSorter: JsonItemSorter;

    // Specialized services
    private pathProcessor: PathKeyProcessor;
    private directorySignatureManager: DirectorySignatureManager;
    private migrationService: DirectoryMigrationService;
    private cleanupService: DirectoryCleanupService;
    private recursiveSynchronizer: RecursiveSynchronizer;
    private keyMigrationService: KeyMigrationService;

    constructor(docsPath: string, fs: FileSystem /*, configReader: ConfigReaderService */) {
        this.absDocsPath = normalizePathSeparators(docsPath);
        this.docsPath = normalizePathSeparators(docsPath);
        
        const baseOverridesPath = normalizePathSeparators(path.join(this.docsPath, '..', '.vitepress', 'config', 'sidebar'));
        const metadataStorageBasePath = normalizePathSeparators(path.join(baseOverridesPath, '.metadata'));

        // Initialize core services
        this.jsonFileHandler = new JsonFileHandler(fs, baseOverridesPath);
        this.metadataManager = new MetadataManager(fs, metadataStorageBasePath);
        this.syncEngine = new SyncEngine();
        this.jsonItemSorter = new JsonItemSorter();

        // Initialize specialized services
        this.pathProcessor = new PathKeyProcessor();
        this.directorySignatureManager = new DirectorySignatureManager(fs, this.docsPath);
        this.migrationService = new DirectoryMigrationService(this.jsonFileHandler, this.metadataManager, fs, this.docsPath);
        this.cleanupService = new DirectoryCleanupService(this.jsonFileHandler, this.metadataManager, fs, this.docsPath);
        this.recursiveSynchronizer = new RecursiveSynchronizer(
            this.jsonFileHandler,
            this.metadataManager,
            this.syncEngine,
            this.jsonItemSorter,
            this.absDocsPath
        );
        this.keyMigrationService = new KeyMigrationService(this.jsonFileHandler, this.metadataManager);
    }

    /**
     * Public facing method to synchronize an entire tree of sidebar items for a given language.
     */
    public async synchronize(
        rootPathKey: string,
        sidebarTreeInput: SidebarItem[],
        lang: string,
        isDevMode: boolean,
        langGitbookPaths: string[]
    ): Promise<SidebarItem[]> {
        if (!Array.isArray(sidebarTreeInput)) {

            return [];
        }

        // Get root directory signature
        const rootConfigDirectorySignature = this.pathProcessor.getSignatureForRootView(rootPathKey, lang);
        const normalizedGitbookPaths = langGitbookPaths.map(p => normalizePathSeparators(p));

        // Early check: If this root view itself is a GitBook root, skip all JSON processing


        const isGitBook = this.pathProcessor.isGitBookRoot(rootConfigDirectorySignature, lang, normalizedGitbookPaths, this.absDocsPath);

        
        if (isGitBook) {

            return sidebarTreeInput;
        }

        const processedTree = sidebarTreeInput.map(item => JSON.parse(JSON.stringify(item))) as SidebarItem[];

        // MIGRATION PHASE: Transform old data format to new format

        const keyMigrationOccurred = await this.keyMigrationService.migrateKeysRecursively(
            processedTree,
            rootConfigDirectorySignature,
            lang,
            normalizedGitbookPaths,
            this.absDocsPath
        );

        if (keyMigrationOccurred) {

        }

        // Clean up duplicate directories from old system
        await this.keyMigrationService.cleanupDuplicateDirectories(rootConfigDirectorySignature, lang);

        // Clean up old metadata entries that are no longer needed
        await this.keyMigrationService.cleanupOldMetadata(processedTree, rootConfigDirectorySignature, lang);

        // Collect all active directory signatures for cleanup
        const activeDirectorySignatures = new Set<string>();
        this.directorySignatureManager.collectActiveDirectorySignatures(
            processedTree, 
            rootConfigDirectorySignature, 
            normalizedGitbookPaths, 
            lang, 
            this.absDocsPath, 
            activeDirectorySignatures
        );

        // Identify outdated directories
        const outdatedDirs = await this.directorySignatureManager.identifyOutdatedDirectories(
            rootConfigDirectorySignature, 
            lang, 
            activeDirectorySignatures
        );

        // Handle directory migrations
        const migrationOccurred = await this.migrationService.handleDirectoryMigrations(
            rootConfigDirectorySignature, 
            lang, 
            activeDirectorySignatures, 
            outdatedDirs
        );

        // Clean up metadata from renamed directories AFTER migration (e.g., tool �?tools)
        await this.keyMigrationService.cleanupRenamedDirectoryMetadata(
            rootConfigDirectorySignature,
            lang,
            activeDirectorySignatures
        );

        // Clean up remaining outdated directories
        await this.cleanupService.cleanupOutdatedDirectories(outdatedDirs, lang);

        // Synchronize items recursively
        await this.recursiveSynchronizer.synchronizeItemsRecursively(
            processedTree, 
            rootConfigDirectorySignature, 
            lang, 
            isDevMode, 
            normalizedGitbookPaths, 
            true
        );

        // Apply JSON overrides as final step to ensure all values are properly applied
        await this.recursiveSynchronizer.reapplyMigratedValues(
            processedTree, 
            rootConfigDirectorySignature, 
            lang, 
            normalizedGitbookPaths
        );

        return processedTree;
    }
} 

