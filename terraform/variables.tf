variable "aws_region" {
  description = "AWS region"
  default     = "us-east-1"
}

variable "project_name" {
  description = "Nombre del proyecto / prefijo para recursos"
  default     = "franchises-api"
}

variable "container_port" {
  description = "Puerto que expone la app dentro del contenedor"
  default     = 8080
}

variable "desired_count" {
  description = "Cantidad de instancias Fargate"
  default     = 1
}