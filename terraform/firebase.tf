resource "google_project_service" "firebase" {
  service            = "firebase.googleapis.com"
  project            = local.project_id
  disable_on_destroy = false
}

resource "google_firebase_project" "dashboard" {
    provider   = google-beta
    project    = local.project_id

    depends_on = [ google_project_service.firebase ]
}

resource "google_firebase_project_location" "basic" {
    provider = google-beta
    project = local.project_id

    location_id = "europe-west"
}

resource "google_firebase_web_app" "dashboard" {
    provider = google-beta
    project = local.project_id
    display_name = "mbs dashboard - ${local.env}"

    depends_on = [google_firebase_project.dashboard]
}

data "google_firebase_web_app_config" "dashboard" {
  provider   = google-beta
  web_app_id = google_firebase_web_app.dashboard.app_id
}

resource "google_storage_bucket" "dashboard" {
    provider = google-beta
    name = "mbs-dashboard-${local.env}"
}

resource "google_storage_bucket_object" "dashboard" {
    provider = google-beta
    bucket = google_storage_bucket.dashboard.name
    name = "firebase-config.json"

    content = jsonencode({
        appId              = google_firebase_web_app.dashboard.app_id
        apiKey             = data.google_firebase_web_app_config.dashboard.api_key
        authDomain         = data.google_firebase_web_app_config.dashboard.auth_domain
        databaseURL        = lookup(data.google_firebase_web_app_config.dashboard, "database_url", "")
        storageBucket      = lookup(data.google_firebase_web_app_config.dashboard, "storage_bucket", "")
        messagingSenderId  = lookup(data.google_firebase_web_app_config.dashboard, "messaging_sender_id", "")
        measurementId      = lookup(data.google_firebase_web_app_config.dashboard, "measurement_id", "")
    })
}

data "google_service_account_key" "sdk" {
  name            = "projects/${local.project_id}/serviceAccounts/${var.adminsdk_username}/keys/${var.key_name}"
}

output "firebase-adminsdk" {
  value = "${data.google_service_account_key.sdk}"
}
