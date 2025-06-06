import { preloadAllSidebars } from '../config/loadSidebar.ts';

async function generate() {
  console.log('Starting sidebar generation for all languages...');
  
  try {
    // First pass: Generate sidebars and run cleanup
    console.log('Pass 1: Initial sidebar generation with cleanup...');
    await preloadAllSidebars();
    
    // Small delay to ensure file system changes are recognized
    console.log('Pass 2: Regenerating to pick up cleanup changes...');
    await new Promise(resolve => setTimeout(resolve, 100));
    
    // Second pass: Generate again to pick up any cleanup changes
    await preloadAllSidebars();
    
    console.log('Sidebars generated successfully for all languages.');
    
  } catch (error) {
    console.error('Error during sidebar generation:', error);
    process.exit(1); // Crucial: exit with non-zero code to stop subsequent build steps
  }
}

generate(); 