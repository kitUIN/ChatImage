$settings_gradle = "fabric\common\settings.gradle"
$build_gradle = "fabric\common\build.gradle"
$license = "LICENSE"
$gradlew_file = "gradlew"
$gradlew_bat_file = "gradlew.bat"
$assets_source = "resources\assets"
$logo_source = "resources\logo.png"
$targetDir = "fabric"
$prefix = "fabric"

function Create-SymbolicLink {
    param($target, $path, $source)
    $fullPath = Join-Path -Path $target -ChildPath $path
    if (Test-Path $fullPath) {
        Remove-Item $fullPath -Recurse
    }
    New-Item -ItemType SymbolicLink -Path $fullPath -Target $source
}

Get-ChildItem -Path "$targetDir\$prefix*" -Directory | ForEach-Object {
    $target = $_.FullName
    Create-SymbolicLink -target $target -path $license -source $license
    Create-SymbolicLink -target $target -path $gradlew_file -source $gradlew_file
    Create-SymbolicLink -target $target -path $gradlew_bat_file -source $gradlew_bat_file
    Create-SymbolicLink -target $target -path "settings.gradle" -source $settings_gradle
    Create-SymbolicLink -target $target -path "build.gradle" -source $build_gradle
    Create-SymbolicLink -target $target -path "src\main\$assets_source" -source $assets_source
    Create-SymbolicLink -target $target -path "src\main\$logo_source" -source $logo_source
}
