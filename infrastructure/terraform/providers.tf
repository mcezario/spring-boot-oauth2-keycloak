provider "keycloak" {
  client_id     = "terraform"
  client_secret = var.keycloak_client_secret
  base_path     = "/auth"
  url           = "http://localhost:8081"
}
