#!/usr/bin/env bash

TARGET_LOADER="$1"  # fabric / forge / neoforge
TARGET_VERSION="$2" # 1.16.5 / 1.18.2 / ...
RULES_JDK21="$3"    # JDK 21 规则
RULES_JDK17="$4"    # JDK 17 规则
RULES_JDK8="$5"     # JDK 8 规则



matches_rule() {
  local rule="$1"
  local loader="$TARGET_LOADER"
  local version="$TARGET_VERSION"
  
  if [[ "$rule" =~ ^([>=]=?)([^-]+)-(.+)$ ]]; then
    local op="${BASH_REMATCH[1]}"
    local rule_loader="${BASH_REMATCH[2]}"
    local rule_version="${BASH_REMATCH[3]}"
    if [[ "$loader" == "$rule_loader" ]]; then
      if [[ "$op" == "=" && "$version" == "$rule_version" ]]; then
        return 0
      elif [[ "$op" == ">=" && "$(printf "%s\n%s" "$rule_version" "$version" | sort -V | head -n1)" == "$rule_version" ]]; then
        return 0
      fi
    fi
  fi
  return 1
}

# **按照 JDK 21 → JDK 17 → JDK 8 顺序匹配**
for jdk in 21 17 8; do
  eval "RULES=\$RULES_JDK$jdk"
  IFS=',' read -ra RULES_ARRAY <<< "$RULES"
  for rule in "${RULES_ARRAY[@]}"; do
    if matches_rule "$rule"; then
      JAVA_VERSION=$jdk
      break 2
    fi
  done
done

if [[ -z "$JAVA_VERSION" ]]; then
  echo "❌ No matching JDK version found for $TARGET_LOADER-$TARGET_VERSION"
  exit 1
fi

echo "✅ Using JDK $JAVA_VERSION"

if [[ -n "$GITHUB_ENV" ]]; then
  echo "JAVA_VERSION=$JAVA_VERSION" >> "$GITHUB_ENV"
fi
