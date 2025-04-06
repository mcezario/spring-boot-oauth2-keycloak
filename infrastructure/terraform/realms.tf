resource "keycloak_realm" "application_realm" {
  realm   = "spring-boot-app-realm"
  enabled = true
}

resource "keycloak_role" "application_realm_role_user" {
  realm_id = keycloak_realm.application_realm.id
  name     = "ROLE_USER"
}

resource "keycloak_role" "application_realm_role_admin" {
  realm_id = keycloak_realm.application_realm.id
  name     = "ROLE_ADMIN"
}
