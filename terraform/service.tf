resource "google_project_service" "service" {
  for_each  = toset([
    "appengine.googleapis.com",
    "cloudapis.googleapis.com",
    "cloudbuild.googleapis.com",
    "cloudresourcemanager.googleapis.com",
    "cloudtrace.googleapis.com",
    "containerregistry.googleapis.com",
    "dataflow.googleapis.com",
    "iam.googleapis.com",
    "logging.googleapis.com",
    "maps-backend.googleapis.com",
    "monitoring.googleapis.com",
    "pubsub.googleapis.com",
  ])

  service = each.key

  project            = local.project_id
  disable_on_destroy = false
}
