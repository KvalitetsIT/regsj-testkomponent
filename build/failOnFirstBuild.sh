#!/bin/sh

echo "${GITHUB_REPOSITORY}"
echo "${DOCKER_SERVICE}"
if [ "${GITHUB_REPOSITORY}" != "KvalitetsIT/regsj-testkomponent" ] && [ "${DOCKER_SERVICE}" = "kvalitetsit/regsj-testkomponent" ]; then
  echo "Please run setup.sh REPOSITORY_NAME"
  exit 1
fi
