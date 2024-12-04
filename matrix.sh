#!/bin/bash

# 获取传入的路径参数
paths=("$@")

# 初始化一个空数组，用于存储所有文件夹对象
all_folder_objects=()

# 遍历每个路径
for path in "${paths[@]}"; do
    # 查找目录，排除 "origin" 文件夹
    folders=$(find "$path" -mindepth 1 -maxdepth 1 -type d ! -name "origin")

    # 遍历每个文件夹，生成相应的 JSON 对象
    for folder in $folders; do
        mc_version=$(basename "$folder" | sed "s/^$path-//")
        mc_loader="$path"

        folder_object=$(jq -n \
            --arg mc_version "$mc_version" \
            --arg mc_loader "$mc_loader" \
            '{ "mc-version": $mc_version, "mc-loader": $mc_loader }')

        all_folder_objects+=("$folder_object")
    done
done

# 使用 jq 创建 JSON 结构
json=$(jq -n --argjson config "$(echo "[${all_folder_objects[*]}]" | jq -s .)" '{ config: $config }')

# 输出结果
echo "::set-output name=matrix::$json"
