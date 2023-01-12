## The API
The endpoint: https://1uf7awebzk.execute-api.us-east-1.amazonaws.com/reader/api/v0/movies
Produces: application/json
Example Output: 
```
{
    "results": [
        {
            "id": "63c053d3bd91411b8177c38d",
            "title": "Harry Potter",
            "year": 2002,
            "cast": [
                "Daniel Radcliffe",
                "Emma Watson"
            ],
            "genres": [
                "Fantasy"
            ]
        },
        {
            "id": "63c053d4bd91411b8177c38e",
            "title": "Top Gun",
            "year": 1989,
            "cast": [
                "Tom Cruise"
            ],
            "genres": [
                "Action"
            ]
        }
    ],
    "pageData": {
        "offSet": 0,
        "pageSize": 2
    }
}
```

Optional Parameters: 
| Parameter        | Description               | Defaults  |
| -------------    |:-------------------------------------------------------------------------------------:            | -----:    |
| title            | the title to query by     | null      |
| cast             | single member to query by | null      |
| year             | year can be any number. if it is an invalid year it will simply return an empty response           | null      |
| genre            | single genre string       | null      |
| offset           | for paging -> the first record by number that we want to fetch                                     | 0      |
| limit             | for paging -> expected number of records per page                                                 | 50      |

Example queries:
By title and genre: https://1uf7awebzk.execute-api.us-east-1.amazonaws.com/reader/api/v0/movies?title="Harry Potter"&genre="Fantasy"
For paging: https://1uf7awebzk.execute-api.us-east-1.amazonaws.com/reader/api/v0/movies?limit=30&offset=31

## Deploying To AWS
Tools required: awscli, kubectl, eksctl, terraform, bash
Credentials: aws configure -> with account id and secret key

1. Deploy the VPC: This will create the VPC, the EKS cluster, and all the associated networking components
```
cd infrastructure/terraform/vpc
terraform init
terraform apply
cd ../..
```
Ensure you copy the vpc_id and private subnet_ids from the output.
2. Deploy mongo: This will deploy MongoDB community version 4.2.6 as a StatefulSet deployment in the mongodb namespace. It uses the mongodbcommunity operator and mongodbcommunity resource to create the deployments. The complexities around handling persistent volumes are handled by the EBS CI controller and the mongodbcommunity operator. It also creates the user accounts and credentials that we could use.
```
cd infrastructure/mongodb
./deploy-mongo.sh <preferredAdminPassword> <preferredMovieAppPassword>
cd ../..
```

3. Deploy the Reader App(the API): this is a standard kubernetes deployment using a private LoadBalancer.
```
cd apps/reader
./deploy-reader.sh
cd ../..
```

4. Go to the AWS console > Load Balancers. Select the load balancer created for our deployment above. Click on listeners and copy the listener arn.
5. Update api-gateway.tfvars with the correct vpc-id and private subnet ids from step 1, and the listener arn from step 4.
6. Deploy the API gateway 
```
cd infrastructure/api-gateway
terraform init
terraform apply
cd ../..
```
This should output the URL from where we can now access the movies app.




## Referred Material
- https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/s3_bucket_notification event notifications
- https://github.com/mongodb/mongodb-kubernetes-operator/blob/master/docs for MongoDB Community deployments
- https://antonputra.com/amazon/Integrate-amazon-api-gateway-with-amazon-eks/#deploy-app-to-kubernetes-and-expose-it-with-nlb 