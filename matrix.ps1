param (
    [string]$path
)

$folders = Get-ChildItem -Path $path -Directory

$filteredFolders = $folders | Where-Object { $_.Name -ne "origin" -and $_.Name -ne "tool" }


$folderObjects = $filteredFolders | ForEach-Object {
    [PSCustomObject]@{
        "mc-version" = $_.Name
        "mc-loader"  = $path
    }
}

$json = [PSCustomObject]@{
    "config" = $folderObjects
} | ConvertTo-Json -Compress

#Write-Output "matrix=$json" >> $GITHUB_OUTPUT
Write-Output "::set-output name=matrix::$json"