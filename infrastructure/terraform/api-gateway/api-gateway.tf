provider "aws" {
  region = "us-east-1"
}

resource "aws_apigatewayv2_api" "api" {
  name          = "movies-api"
  protocol_type = "HTTP"
}

resource "aws_apigatewayv2_stage" "stage" {
  api_id = aws_apigatewayv2_api.api.id

  name        = "reader"
  auto_deploy = true
}

resource "aws_security_group" "vpc_link_sg" {
  name   = "vpc-link-sg"
  vpc_id = var.vpc_id

  egress {
    from_port        = 0
    to_port          = 0
    protocol         = "-1"
    cidr_blocks      = ["0.0.0.0/0"]
  }
}

resource "aws_apigatewayv2_vpc_link" "eks-link" {
  name               = "eks-link"
  security_group_ids = [aws_security_group.vpc_link_sg.id]
  subnet_ids = var.subnet_ids
}

resource "aws_apigatewayv2_integration" "api-gateway" {
  api_id = aws_apigatewayv2_api.api.id

  integration_uri    = var.eks_elb_listener_uri
  integration_type   = "HTTP_PROXY"
  integration_method = "ANY"
  connection_type    = "VPC_LINK"
  connection_id      = aws_apigatewayv2_vpc_link.eks-link.id
}

resource "aws_apigatewayv2_route" "get_reader" {
  api_id = aws_apigatewayv2_api.api.id

  route_key = "GET /api/v0/movies"
  target    = "integrations/${aws_apigatewayv2_integration.api-gateway.id}"
}

resource "aws_apigatewayv2_route" "dummy_build_movie" {
  api_id = aws_apigatewayv2_api.api.id

  route_key = "GET /api/v0/buildMovie"
  target    = "integrations/${aws_apigatewayv2_integration.api-gateway.id}"
}
