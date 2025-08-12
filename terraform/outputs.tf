output "alb_dns_name" {
  description = "URL pública del ALB"
  value       = aws_lb.alb.dns_name
}
