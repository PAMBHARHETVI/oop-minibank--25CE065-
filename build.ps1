$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
if (Test-Path out) { Remove-Item out -Recurse -Force }
if (Test-Path MiniBank.jar) { Remove-Item MiniBank.jar -Force }
New-Item -ItemType Directory -Path out | Out-Null
$files = Get-ChildItem -Path "src/main/java" -Filter "*.java" -Recurse | ForEach-Object { $_.FullName }
& javac --release 17 -encoding UTF-8 -d out $files
if ($LASTEXITCODE -ne 0) { throw "javac compilation failed" }
"Main-Class: MiniBank" | Set-Content -Path manifest.mf -Encoding ascii
& jar cfm MiniBank.jar manifest.mf -C out .
if ($LASTEXITCODE -ne 0) { throw "JAR packaging failed" }
Write-Host "Build successful: MiniBank.jar"
