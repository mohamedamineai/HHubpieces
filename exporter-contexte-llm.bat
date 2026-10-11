@echo off
setlocal

set "PROJECT_DIR=%~dp0"

powershell.exe -NoProfile -ExecutionPolicy Bypass -Command ^
  "$ErrorActionPreference = 'Stop';" ^
  "$root = (Resolve-Path -LiteralPath '%PROJECT_DIR%').Path.TrimEnd('\', '/');" ^
  "$output = Join-Path $root 'llm_context.txt';" ^
  "$excludedDirs = @('.git', '.idea', 'target', 'node_modules', 'build', 'dist', 'out', '.gradle', 'coverage', '.next', 'vendor');" ^
  "$extensions = @('.java', '.kt', '.xml', '.properties', '.yml', '.yaml', '.json', '.sql', '.html', '.htm', '.css', '.js', '.ts', '.tsx', '.jsx', '.md', '.txt', '.gradle', '.kts', '.bat', '.cmd', '.sh', '.py', '.go', '.cs', '.php', '.vue', '.svelte', '.toml', '.ini', '.conf', '.properties');" ^
  "$files = Get-ChildItem -LiteralPath $root -File -Recurse | Where-Object { $relative = $_.FullName.Substring($root.Length).TrimStart('\', '/'); $parts = $relative -split '[\\/]'; ($relative -ne 'llm_context.txt') -and ($parts | Where-Object { $excludedDirs -contains $_ }).Count -eq 0 -and ($extensions -contains $_.Extension.ToLowerInvariant() -or $_.Name -in @('Dockerfile', 'Makefile', 'mvnw', 'mvnw.cmd', 'Procfile')) -and $_.Name -notmatch '(?i)^(\.env|.*secret.*|.*credential.*|.*password.*|.*private.*key.*)' -and $_.Extension.ToLowerInvariant() -notin @('.pem', '.p12', '.pfx', '.key'); } | Sort-Object FullName;" ^
  "$directories = Get-ChildItem -LiteralPath $root -Directory -Recurse | Where-Object { $relative = $_.FullName.Substring($root.Length).TrimStart('\', '/'); $parts = $relative -split '[\\/]'; ($parts | Where-Object { $excludedDirs -contains $_ }).Count -eq 0 } | Sort-Object FullName;" ^
  "$lines = [System.Collections.Generic.List[string]]::new();" ^
  "$lines.Add('PROJECT CONTEXT FOR AN LLM'); $lines.Add(('Root: ' + $root)); $lines.Add(('Generated: ' + (Get-Date -Format 'yyyy-MM-dd HH:mm:ss')));" ^
  "$lines.Add(''); $lines.Add('=== PROJECT STRUCTURE ===');" ^
  "foreach ($item in $directories) { $lines.Add(('DIR  ' + $item.FullName.Substring($root.Length).TrimStart('\', '/'))); }" ^
  "foreach ($item in $files) { $lines.Add(('FILE ' + $item.FullName.Substring($root.Length).TrimStart('\', '/'))); }" ^
  "$lines.Add(''); $lines.Add('=== FILE CONTENTS ===');" ^
  "foreach ($item in $files) { $relative = $item.FullName.Substring($root.Length).TrimStart('\', '/'); $lines.Add(''); $lines.Add(('----- BEGIN FILE: ' + $relative + ' -----')); $lines.Add([IO.File]::ReadAllText($item.FullName)); $lines.Add(('----- END FILE: ' + $relative + ' -----')); }" ^
  "[IO.File]::WriteAllLines($output, $lines, [Text.UTF8Encoding]::new($true));" ^
  "Write-Host ('Context exported to: ' + $output);"

if errorlevel 1 (
    echo Export failed.
    exit /b 1
)

echo Review llm_context.txt and remove any sensitive information before sharing it.
endlocal
