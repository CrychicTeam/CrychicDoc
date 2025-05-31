import os
import sys
import subprocess
import re
from pathlib import Path

def run_git_command(cmd):
    """Run git command and return output"""
    try:
        result = subprocess.run(cmd, shell=True, capture_output=True, text=True)
        return result.returncode == 0, result.stdout.strip(), result.stderr.strip()
    except Exception as e:
        return False, "", str(e)

def pascal_case(text):
    """Convert text to PascalCase with smart rules"""
    # Handle special cases for your project
    special_cases = {
        'ponderjs': 'PonderJs',
        'kubejs': 'KubeJS',
        'javascript': 'JavaScript',
        'typescript': 'TypeScript',
        'example': 'Example',
        'examples': 'Examples'
    }
    
    text_lower = text.lower()
    if text_lower in special_cases:
        return special_cases[text_lower]
    
    # General PascalCase conversion
    return ''.join(word.capitalize() for word in re.split(r'[-_\s]+', text))

def apply_pascal_case_to_path(path):
    """Apply PascalCase to directory names in path"""
    parts = Path(path).parts
    new_parts = []
    
    for part in parts:
        if '.' in part:  # It's a file
            name, ext = os.path.splitext(part)
            new_part = pascal_case(name) + ext
        else:  # It's a directory
            new_part = pascal_case(part)
        new_parts.append(new_part)
    
    return str(Path(*new_parts))

def main():
    # Get all tracked files
    success, files_output, _ = run_git_command("git ls-files")
    if not success:
        print("❌ Failed to get git files")
        sys.exit(1)
    
    files = files_output.split('\n') if files_output else []
    
    # Group files by lowercase version to find conflicts
    file_groups = {}
    for file in files:
        if file:
            key = file.lower()
            if key not in file_groups:
                file_groups[key] = []
            file_groups[key].append(file)
    
    # Find conflicts
    conflicts = {group[0]: group for group in file_groups.values() if len(group) > 1}
    
    if not conflicts:
        print("✅ No case conflicts found!")
        return
    
    print("🔧 Fixing conflicts using PascalCase convention...")
    
    # Process each conflict group
    for target_key, conflict_files in conflicts.items():
        print(f"\n📁 Processing conflict group: {conflict_files}")
        
        # Determine the target name using PascalCase
        target_name = apply_pascal_case_to_path(conflict_files[0])
        
        # Find which file (if any) already matches the target
        files_to_remove = []
        target_file = None
        
        for file in conflict_files:
            if file == target_name:
                target_file = file
                print(f"  ✅ Keeping: {file}")
            else:
                files_to_remove.append(file)
                print(f"  🗑️  Will remove: {file}")
        
        # If no file matches target name, rename the first one
        if not target_file and files_to_remove:
            file_to_rename = files_to_remove.pop(0)
            print(f"  🔄 Renaming: {file_to_rename} -> {target_name}")
            
            # Git mv with intermediate step for case-only renames
            temp_name = file_to_rename + ".tmp"
            success1, _, _ = run_git_command(f'git mv "{file_to_rename}" "{temp_name}"')
            success2, _, _ = run_git_command(f'git mv "{temp_name}" "{target_name}"')
            
            if not (success1 and success2):
                print(f"  ⚠️  Failed to rename {file_to_rename}")
        
        # Remove duplicate files
        for file in files_to_remove:
            print(f"  🗑️  Removing duplicate: {file}")
            success, _, _ = run_git_command(f'git rm "{file}"')
            if not success:
                print(f"  ⚠️  Failed to remove {file}")
    
    print("\n✅ Case conflicts resolved!")

if __name__ == "__main__":
    main()
