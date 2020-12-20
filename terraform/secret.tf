resource "google_project_service" "secretmanager" {
  service            = "secretmanager.googleapis.com"
  project            = local.project_id
  disable_on_destroy = false
}


resource "google_secret_manager_secret" "database_password" {
  project   = local.project_id
  secret_id = "database_password"

  labels = {
    organization = "machbarschaft"
    source       = "terraform"
  }

  replication {
    automatic = true
  }

  depends_on = [ google_project_service.secretmanager ]
}

resource "google_secret_manager_secret_version" "database_password_basic" {
  secret      = google_secret_manager_secret.database_password.id
  secret_data = google_sql_user.user.password
}

resource "google_secret_manager_secret_iam_member" "database_password" {
  project   = local.project_id

  secret_id = google_secret_manager_secret.database_password.id
  role      = "roles/secretmanager.secretAccessor"
  # TODO: this assigmnent is pretty useless must be applied to service account of run
  member    = "user:harald@sylox.io" # or serviceAccount:my-app@...
}
