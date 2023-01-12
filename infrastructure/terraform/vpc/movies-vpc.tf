provider "aws" {
  region = "us-east-1"
}

provider "kubernetes" {
  host                   = module.eks.cluster_endpoint
  cluster_ca_certificate = base64decode(module.eks.cluster_certificate_authority_data)

  exec {
    api_version = "client.authentication.k8s.io/v1beta1"
    command     = "aws"
    # This requires the awscli to be installed locally where Terraform is executed
    args = ["eks", "get-token", "--cluster-name", module.eks.cluster_name]
  }
}

 module "vpc" {
    source = "terraform-aws-modules/vpc/aws"

    name  = "movies-vpc"
    cidr = "10.0.0.0/16"

    azs = ["us-east-1a", "us-east-1b"]
    private_subnets = ["10.0.0.0/19", "10.0.32.0/19"]
    public_subnets = ["10.0.64.0/19", "10.0.96.0/19"]

    enable_nat_gateway = true
    single_nat_gateway = true
    create_igw = true

    public_subnet_tags = {
        "kubernetes.io/cluster/movies-cluster" = "shared"
        "kubernetes.io/role/elb"               = "1"
    }
    
    private_subnet_tags = {
        "kubernetes.io/cluster/movies-cluster" = "shared"
        "kubernetes.io/role/internal-elb"      = "1"
    }
    tags = {
        "kubernetes.io/cluster/movies-cluster" = "shared"
    }
 }

 module "eks" {
    source  = "terraform-aws-modules/eks/aws"

    cluster_name = "movies-cluster"
    cluster_endpoint_public_access = true
    cluster_version = "1.24"
    
    vpc_id = module.vpc.vpc_id
    subnet_ids = flatten([module.vpc.public_subnets, module.vpc.private_subnets])

    eks_managed_node_group_defaults = {
        iam_role_name            = "movies-cluster-node-group-role"

        iam_role_additional_policies = {
            AmazonEC2ContainerRegistryReadOnly = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
            AmazonEKSWorkerNodePolicy = "arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy"
            AmazonEKS_CNI_Policy = "arn:aws:iam::aws:policy/AmazonEKS_CNI_Policy"
            AmazonEBSCSIDriverPolicy = "arn:aws:iam::aws:policy/service-role/AmazonEBSCSIDriverPolicy"
        }
    }

    eks_managed_node_groups = {
        green = {
            min_size     = 1
            max_size     = 5
            desired_size = 3

            instance_types = ["t3.large"]
            capacity_type  = "ON_DEMAND"
            
            ebs_optimized = true
            iam_role_name            = "movies-cluster-node-group-role"

            iam_role_additional_policies = {
                AmazonEC2ContainerRegistryReadOnly = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
                AmazonEKSWorkerNodePolicy = "arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy"
                AmazonEKS_CNI_Policy = "arn:aws:iam::aws:policy/AmazonEKS_CNI_Policy"
                AmazonEBSCSIDriverPolicy = "arn:aws:iam::aws:policy/service-role/AmazonEBSCSIDriverPolicy"

            }
        }

    }

    cluster_addons = {
        coredns = {
            most_recent = true
        }
        kube-proxy = {
            most_recent = true
        }
        vpc-cni = {
             most_recent = true
        }
        aws-ebs-csi-driver = {
            most_recent = true
        }
    }
    
    create_iam_role = true
    iam_role_name = "movies-cluster-role"
    create_aws_auth_configmap = true
    manage_aws_auth_configmap = true
 }

 resource "aws_efs_file_system" "efs" {
  creation_token = "${module.eks.cluster_name}-efs"

  tags = {
    Name = "${module.eks.cluster_name}-efs"
  }
}

resource "aws_efs_mount_target" "movies-mount" {
  count = 2
  file_system_id = aws_efs_file_system.efs.id
  subnet_id = module.vpc.private_subnets[count.index]
  security_groups = [module.eks.cluster_security_group_id,module.eks.node_security_group_id]
}

resource "kubernetes_persistent_volume" "mongo-pv" {
  metadata {
    name = "mongo-pv"
  }
  spec {
    storage_class_name = "efs-sc"
    persistent_volume_reclaim_policy = "Retain"
    capacity = {
      storage = "2Gi"
    }
    access_modes = ["ReadWriteMany"]
    persistent_volume_source {
      nfs {
        path = "/"
        server = aws_efs_file_system.efs.id
      }
    }
  }
}