#!/bin/bash

DOCKER_REGISTRY='218005993626.dkr.ecr.us-east-1.amazonaws.com'
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin $DOCKER_REGISTRY
./gradlew bootBuildImage --imageName="${DOCKER_REGISTRY}/movie-writer"
docker push "${DOCKER_REGISTRY}/movie-writer:latest"

if [ -n "$1" ] && [ -n "$2" ]; then
 echo "User passed in aws access keys lets create secrets"
 kubectl create secret generic aws-creds --from-literal=accessKey="$1" --from-literal=secretKey="$2" --namespace=movies-app
fi


kubectl apply -f k8s/movie-writer.yaml --namespace=movies-app