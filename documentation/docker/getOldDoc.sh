#!/bin/bash

if docker pull dockerregistry.regsj.hosting.kitkube.dk/regsj/regsj-testkomponent-documentation:latest; then
    echo "Copy from old documentation image."
    docker cp $(docker create dockerregistry.regsj.hosting.kitkube.dk/regsj/regsj-testkomponent-documentation:latest):/usr/share/nginx/html target/old
fi
