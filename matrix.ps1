param (
    [string]$path
)

$folders = Get-ChildItem -Path $path -Directory

$filteredFolders = $folders | Where-Object { $_.Name -ne "origin" }

$folderObjects = $filteredFolders | ForEach-Object {
    [PSCustomObject]@{
        "mc-version" = $_.Name
        "mc-loader"  = $path
    }
}

$json = $folderObjects | ConvertTo-Json -Compress

Write-Output "matrix=$json" >> $GITHUB_OUTPUT