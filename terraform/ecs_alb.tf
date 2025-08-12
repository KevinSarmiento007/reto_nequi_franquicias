# ECS Cluster
resource "aws_ecs_cluster" "cluster" {
  name = "${var.project_name}-cluster"
}

# Usar la default VPC y sus subnets
data "aws_vpc" "default" {
  default = true
}

data "aws_subnets" "default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
}

# Log group para ECS
resource "aws_cloudwatch_log_group" "ecs" {
  name              = "/ecs/${var.project_name}"
  retention_in_days = 14
}

# Security Group ALB (internet)
resource "aws_security_group" "alb_sg" {
  name        = "${var.project_name}-alb-sg"
  description = "Allow HTTP"
  vpc_id      = data.aws_vpc.default.id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# Security Group ECS (permite desde ALB)
resource "aws_security_group" "ecs_sg" {
  name        = "${var.project_name}-ecs-sg"
  description = "Allow traffic from ALB"
  vpc_id      = data.aws_vpc.default.id

  ingress {
    from_port       = var.container_port
    to_port         = var.container_port
    protocol        = "tcp"
    security_groups = [aws_security_group.alb_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# ALB (internet-facing)
resource "aws_lb" "alb" {
  name               = "${var.project_name}-alb"
  load_balancer_type = "application"
  internal           = false
  security_groups    = [aws_security_group.alb_sg.id]
  subnets            = data.aws_subnets.default.ids
}

# Target Group
resource "aws_lb_target_group" "tg" {
  name        = "${var.project_name}-tg"
  port        = var.container_port
  protocol    = "HTTP"
  vpc_id      = data.aws_vpc.default.id
  target_type = "ip"

  health_check {
    path                = "/ping"
    protocol            = "HTTP"
    matcher             = "200-399"
    interval            = 30
    healthy_threshold   = 2
    unhealthy_threshold = 2
    timeout             = 5
  }
}

# Listener
resource "aws_lb_listener" "listener" {
  load_balancer_arn = aws_lb.alb.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.tg.arn
  }
}

# Task Definition
resource "aws_ecs_task_definition" "task" {
  family                   = var.project_name
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "512"
  memory                   = "1024"
  execution_role_arn       = aws_iam_role.ecs_task_exec.arn
  task_role_arn            = aws_iam_role.ecs_task_exec.arn

  container_definitions = jsonencode([{
    name  = var.project_name
    image = "${aws_ecr_repository.repo.repository_url}:latest"
    portMappings = [{
      containerPort = var.container_port
      hostPort      = var.container_port
      protocol      = "tcp"
    }]
    logConfiguration = {
      logDriver = "awslogs"
      options = {
        awslogs-group         = aws_cloudwatch_log_group.ecs.name
        awslogs-region        = var.aws_region
        awslogs-stream-prefix = var.project_name
      }
    }
  }])
}

# ECS Service
resource "aws_ecs_service" "service" {
  name            = "${var.project_name}-service"
  cluster         = aws_ecs_cluster.cluster.id
  task_definition = aws_ecs_task_definition.task.arn
  desired_count   = var.desired_count
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = data.aws_subnets.default.ids
    security_groups  = [aws_security_group.ecs_sg.id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.tg.arn
    container_name   = var.project_name
    container_port   = var.container_port
  }

  depends_on = [aws_lb_listener.listener]
}