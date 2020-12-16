terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "3.50.0"
    }
  }
}

provider "google" {
  project = local.project_id
  region  = var.region
}


provider "google-beta" {
  project = local.project_id
  region  = var.region
}