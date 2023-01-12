output "vpc_id" {
    value = module.vpc.vpc_id
}

output "private_subnet_ids" {
    value = module.vpc.private_subnets
}

output "eks_cluster_id" {
    value = module.eks.cluster_id
}

output "efs_fsid" {
  value = aws_efs_file_system.efs.id
}