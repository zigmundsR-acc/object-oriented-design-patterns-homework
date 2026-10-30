param([switch]$Test)

$ErrorActionPreference = 'Stop'

if (-not (Get-Command javac -ErrorAction SilentlyContinue)) {
    Write-Output 'A JDK (Java 8 or newer) is required. Add its bin directory to PATH.'
    exit 1
}

$outputDirectory = Join-Path $PSScriptRoot 'out'
New-Item -ItemType Directory -Path $outputDirectory -Force | Out-Null
$sources = @(Get-ChildItem -LiteralPath (Join-Path $PSScriptRoot 'src/main/java/coffeeshop') -Filter '*.java' |
        Select-Object -ExpandProperty FullName)
$entryPoint = 'coffeeshop.Main'
if ($Test) {
    $sources += Join-Path $PSScriptRoot 'src/test/java/coffeeshop/CoffeeShopTest.java'
    $entryPoint = 'coffeeshop.CoffeeShopTest'
}

javac -encoding UTF-8 -Xlint:all -d $outputDirectory $sources
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

java -cp $outputDirectory $entryPoint
exit $LASTEXITCODE
