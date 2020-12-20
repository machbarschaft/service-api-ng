resource "google_storage_bucket" "keystorage" {
  name          = "keystorage-${local.project_id}"
  location      = var.location
  force_destroy = true
}

resource "google_storage_bucket_object" "sdkkey" {
  name   = "credentials.json"
  content = base64decode(google_service_account_key.imkey.private_key)
  bucket = google_storage_bucket.keystorage.name
}
