param (
    [string[]]$paths
)

$allFolderObjects = @()

foreach ($path in $paths) {
    $folders = Get-ChildItem -Path $path -Directory
    $filteredFolders = $folders | Where-Object { $_.Name -ne "origin" }
    $supportVersionFile = "$mcLoader\$mcLoader-$mcVersion\support_version.txt"

    # 读取 support_version.txt 文件内容
    if (Test-Path $supportVersionFile) {
        $supportVersion = Get-Content -Path $supportVersionFile | Out-String
        $supportVersion = $supportVersion.Trim()
    } else {
        $supportVersion = $mcVersion
    }

    # 根据 mcLoader 设置 publishLoaders
    if ($mcLoader -eq "fabric") {
        $publishLoaders = "fabric quilt"
    } else {
        $publishLoaders = $mcLoader
    }

    $folderObjects = $filteredFolders | ForEach-Object {
        [PSCustomObject]@{
            "mc-version" = $_.Name.Replace("$path-", "")
            "mc-loader"  = $path
            "publish-loaders" = $publishLoaders
            "publish-version" = $supportVersion
        }
    }

    $allFolderObjects += $folderObjects
}

$json = [PSCustomObject]@{
    "config" = $allFolderObjects
} | ConvertTo-Json -Compress

Write-Output "::set-output name=matrix::$json"
