resource "keycloak_user" "user_bob" {
  realm_id = keycloak_realm.application_realm.id
  username = "bob"
  enabled  = true

  email      = "bob@domain.com"
  first_name = "Bob"
  last_name  = "Bobson"

  initial_password {
    value     = "pwdbob"
    temporary = false
  }
}

resource "keycloak_user_roles" "bob_roles" {
  realm_id = keycloak_realm.application_realm.id
  user_id  = keycloak_user.user_bob.id

  role_ids = [
    keycloak_role.application_realm_role_user.id
  ]
}

resource "keycloak_user" "user_alice" {
  realm_id = keycloak_realm.application_realm.id
  username = "alice"
  enabled  = true

  email      = "alice@domain.com"
  first_name = "Alice"
  last_name  = "Aliceberg"

  initial_password {
    value     = "pwdalice"
    temporary = false
  }
}

resource "keycloak_user_roles" "alice_roles" {
  realm_id = keycloak_realm.application_realm.id
  user_id  = keycloak_user.user_alice.id

  role_ids = [
    keycloak_role.application_realm_role_admin.id
  ]
}
