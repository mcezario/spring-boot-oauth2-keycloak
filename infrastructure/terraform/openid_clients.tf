resource "keycloak_openid_client" "openid_client" {
  realm_id      = keycloak_realm.application_realm.id
  client_id     = "spring-resource-server"
  enabled       = true

  access_type                  = "PUBLIC"
  direct_access_grants_enabled = true
}
