#!/bin/bash

DOCKER_REGISTRY='218005993626.dkr.ecr.us-east-1.amazonaws.com'
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin $DOCKER_REGISTRY
DOCKER_PASSW=$(aws ecr get-login-password --region us-east-1)
./gradlew bootBuildImage --imageName="${DOCKER_REGISTRY}/movie-reader"
docker push "${DOCKER_REGISTRY}/movie-reader:latest"

kubectl create secret docker-registry docker-creds --docker-server=$DOCKER_REGISTRY --docker-username=AWS --docker-password="$DOCKER_PASSW" --docker-email=sachin.tcs2k7@gmail.com --namespace=movies-app --dry-run=client -o yaml | kubectl apply -f -

kubectl create namespace movies-app --dry-run=client -o yaml | kubectl apply -f -
ADMIN_CONNECTION_STRING=$(kubectl get secret mongodb-admin-moviesappuser --namespace=mongodb -o jsonpath='{.data.connectionString\.standard}' | base64 --decode)
MOIVES_CONNECTION_STRING="${ADMIN_CONNECTION_STRING/admin?/movies?authSource=admin&}"
echo $MOIVES_CONNECTION_STRING
ENCODED_MOVIES_CONNECTION_STRING=$(echo "${MOIVES_CONNECTION_STRING}" | base64)
yq e ".data.connectionString=\"${ENCODED_MOVIES_CONNECTION_STRING}\"" k8s/mongodb-creds.yaml  | kubectl apply -f -
kubectl apply -f k8s/movie-reader.yaml --namespace=movies-app