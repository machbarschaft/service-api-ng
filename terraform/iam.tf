resource "google_service_account" "identitymgmt" {
  account_id   = "identityaccess-${local.project_id}"
  display_name = "Identity Access ${local.project_id}"
}

resource "google_service_account_key" "imkey" {
  service_account_id = google_service_account.identitymgmt.name
}

resource "google_project_iam_member" "firebaseadmin" {
  project = local.project_id
  role    = "roles/firebase.sdkAdminServiceAgent"
  member  = "serviceAccount:${google_service_account.identitymgmt.email}"
}

resource "google_project_iam_member" "tokencreator" {
  project = local.project_id
  role    = "roles/iam.serviceAccountTokenCreator"
  member  = "serviceAccount:${google_service_account.identitymgmt.email}"
}
