param(
    [bool]$ci = $false
)
$settings_gradle = "fabric\common\settings.gradle"
$build_gradle = "fabric\common\build.gradle"
$license = "LICENSE"
$gradlew_file = "gradlew"
$gradlew_bat_file = "gradlew.bat"
$gradlew_jar_file = "gradle-wrapper.jar"
$assets_source = "resources\assets"
$logo_source = "resources\logo.png"
$targetDir = "fabric"
$prefix = "fabric"


function Create-SymbolicLink {
    param($target, $path, $source)
    $fullPath = Join-Path -Path $target -ChildPath $path
    if (-not (Test-Path $fullPath)) {
        New-Item -ItemType Directory -Force -Path $fullPath
    }
    if (Test-Path $fullPath) {
        Remove-Item $fullPath -Recurse
    }
}


Get-ChildItem -Path "$targetDir\$prefix*" -Directory | ForEach-Object {
    $target = $_.FullName
    Copy-Item $gradlew_file -Destination "$target\$gradlew_file" -Force
    Copy-Item $gradlew_bat_file -Destination "$target\$gradlew_bat_file" -Force
    Copy-Item $license -Destination "$target\$license" -Force
    Copy-Item $gradlew_jar_file -Destination "$target\gradle\wrapper\$gradlew_jar_file" -Force

    Create-SymbolicLink -target $target -path "settings.gradle" -source $settings_gradle
    Create-SymbolicLink -target $target -path "build.gradle" -source $build_gradle
    Create-SymbolicLink -target $target -path "src\main\$assets_source" -source $assets_source
    Create-SymbolicLink -target $target -path "src\main\$logo_source" -source $logo_source
}

$json = Get-Content -Path 'fabric/common/main.json' | ConvertFrom-Json
$namespace = 'src\main\java\io\github\kituin\chatimage'
foreach ($obj in $json) {
    $aPath = Join-Path -Path "fabric" -ChildPath $obj.main
    $bPath = Join-Path -Path $namespace -ChildPath $obj.file
    $mainPath = Join-Path -Path $aPath -ChildPath $bPath

    foreach ($other in $obj.others) {
        Create-SymbolicLink -target "fabric\$other\$namespace" -path $obj.file -source $mainPath
    }
}