#!/bin/bash

if [ -z "$1" ] || [ -z "$2" ]; then
  echo "Usage: ./deploy-mongo.sh <adminPassword> <moviePassword>"
  exit 1
fi

DB_NAMESPACE=mongodb
ADMIN_PASSWORD=$1
MOVIE_APP_USER_PASSWORD=$2

#helm repo add mongodb https://mongodb.github.io/helm-charts
helm install community-operator mongodb/community-operator --namespace $DB_NAMESPACE

kubectl create secret generic mongodb-admin-secret --from-literal=password='$ADMIN_PASSWORD' --namespace=$DB_NAMESPACE
kubectl create secret generic mongodb-movie-user-secret --from-literal=password='$MOVIE_APP_USER_PASSWORD' --namespace=$DB_NAMESPACE

kubectl apply -f mongodb.yaml --namespace=$DB_NAMESPACE

echo "Waiting for at least one mongo pod to be ready. This may take some time. You can terminate the script to avoid waiting."
kubectl wait --timeout=600s --for=condition=Ready pod/mongodb-0 --namespace=mongodb