#!/bin/bash

if docker pull kvalitetsit/regsj-testkomponent-documentation:latest; then
    echo "Copy from old documentation image."
    docker cp $(docker create kvalitetsit/regsj-testkomponent-documentation:latest):/usr/share/nginx/html target/old
fi
