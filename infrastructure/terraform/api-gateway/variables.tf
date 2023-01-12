variable "vpc_id" {
    type = string
}
variable "eks_elb_listener_uri" {
    type = string
}
variable "subnet_ids" {
    type = list(string)
}