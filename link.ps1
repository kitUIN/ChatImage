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

function Create-Copy {
    param($target, $path, $source)
    $fullPath = Join-Path -Path $target -ChildPath $path
    if (Test-Path $fullPath) {
        Remove-Item $fullPath -Recurse
    }
    Copy-Item $source -Destination $fullPath -Force -Recurse
}

function Create-SymbolicLink {
    param($target, $path, $source)
    if ($ci) {
        Create-Copy -target $target -path $path -source $source
    }
    else{
        $fullPath = Join-Path -Path $target -ChildPath $path
        if (Test-Path $fullPath) {
            Remove-Item $fullPath -Recurse
        }
        New-Item -ItemType SymbolicLink -Path $fullPath -Target $source
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
