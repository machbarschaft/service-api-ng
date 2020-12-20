resource "google_project_service" "sqladmin" {
  service            = "sqladmin.googleapis.com"
  project            = local.project_id
  disable_on_destroy = false
}

resource "google_sql_database_instance" "master" {
  name             = "${local.project_id}-${random_id.id.hex}"
  database_version = "POSTGRES_11"
  project          = local.project_id
  region           = var.region

  settings {
    tier = local.sql_instance_size

    backup_configuration {
      binary_log_enabled = local.env == "prd" ? true : false
      enabled            = local.env == "prd" ? true : false
    }
  }

  deletion_protection = local.env == "prd" ? true : false

  depends_on          = [ google_project_service.sqladmin ]
}

resource "google_sql_database" "database" {
  project  = local.project_id
  name     = "machbarschaft_${local.env}_database"
  instance = google_sql_database_instance.master.name
}

resource "random_password" "password" {
  length = 49
  special = true
  override_special = "_%@"
}

resource "google_sql_user" "user" {
  project         = local.project_id
  name            = "machbarschaft_${local.env}_user"
  instance        = google_sql_database_instance.master.name
  password        = random_password.password.result

  deletion_policy = "ABANDON"
}

output "sql_instance_connection_name" {
  value = google_sql_database_instance.master.connection_name
}

output "sql_database_name" {
  value = google_sql_database.database.name
}

output "sql_user_name" {
  value = google_sql_user.user.name
}

output "sql_user_password" {
  value = google_sql_user.user.password
}
