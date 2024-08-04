$file = Get-ChildItem -Path "tool" -Filter "ModMultiVersionTool*.jar" | Select-Object -First 1

# 如果找到文件，则使用java -jar执行
if ($file) {
    java -jar $file.FullName
} else {
    Write-Output "can't find"
}
