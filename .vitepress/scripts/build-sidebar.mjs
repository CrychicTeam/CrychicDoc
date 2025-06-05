import { preloadAllSidebars } from '../config/loadSidebar.ts';

async function generate() {
  console.log('Starting sidebar generation for all languages...');
  try {
    await preloadAllSidebars();
    console.log('Sidebars generated successfully for all languages.');
    // If preloadAllSidebars throws, the catch block will handle it.
    // If it completes without error, this script will exit with code 0 by default.
  } catch (error) {
    console.error('Error during sidebar generation:', error);
    process.exit(1); // Crucial: exit with non-zero code to stop subsequent build steps
  }
}

generate(); 