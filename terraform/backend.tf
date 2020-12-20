terraform {
 backend "gcs" {
   bucket  = "terraform-admin-machbarschaft"
   prefix  = "terraform/state"
 }
}
