data "google_project" "project" {
  project_id = local.project_id
}

resource "random_id" "id" {
  byte_length = 4
}
