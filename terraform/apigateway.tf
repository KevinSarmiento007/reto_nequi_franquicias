resource "aws_apigatewayv2_api" "http_api" {
  name          = "ecs-http-api"
  protocol_type = "HTTP"
}

# Integración en modo proxy, pasando todo al ALB
resource "aws_apigatewayv2_integration" "alb_proxy" {
  api_id                 = aws_apigatewayv2_api.http_api.id
  integration_type       = "HTTP_PROXY"
  integration_uri        = "http://${aws_lb.alb.dns_name}/{proxy}" # Captura todo lo que llega
  integration_method     = "ANY"
  payload_format_version = "1.0"
}

# Ruta catch-all
resource "aws_apigatewayv2_route" "catch_all" {
  api_id    = aws_apigatewayv2_api.http_api.id
  route_key = "ANY /{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.alb_proxy.id}"
}

# Stage prod (sin prefijo en la URL que rompa las rutas)
resource "aws_apigatewayv2_stage" "prod" {
  api_id      = aws_apigatewayv2_api.http_api.id
  name        = "$default" # Esto elimina el "/prod" en la URL
  auto_deploy = true
}

output "api_gateway_url" {
  value = aws_apigatewayv2_stage.prod.invoke_url
}