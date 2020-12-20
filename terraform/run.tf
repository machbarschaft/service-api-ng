resource "google_project_service" "run" {
  service            = "run.googleapis.com"
  project            = local.project_id
  disable_on_destroy = false
}

resource "google_cloud_run_service" "machbarschaft" {
  name     = "machbarschaft-api-${local.env}"
  location = var.region_run
  project  = local.project_id

  template {
    spec {
      containers {
        # We must build this image in advance otherwise the service fails
        image = "gcr.io/${local.project_id}/machbarschaft-api:develop"
        env {
          name  = "profiles_active"
          value = local.env == "prd" ? "cloudsql,prd,sdk" : "cloudsql,sta,sdk"
        }
        env {
          name  = "database_instance_connection_name"
          value = google_sql_database_instance.master.connection_name
        }
        env {
          name  = "database_name"
          value = google_sql_database.database.name
        }
        env {
          name  = "database_user"
          value = google_sql_user.user.name
        }
        # TODO: replace this insecure env with secrets manager password - we already store it there
        env {
          name  = "database_password"
          value = google_sql_user.user.password
        }
        env {
          name  = "FIREBASE_PROJECT_ID"
          value = local.project_id
        }
        resources {
          limits = {
            cpu    = "1000m"
            memory = "512Mi"
          }
        }
      }
    }
    metadata {
      annotations = {
        "run.googleapis.com/cloudsql-instances" = google_sql_database_instance.master.connection_name
        "run.googleapis.com/client-name"        = "terraform"
        "autoscaling.knative.dev/maxScale"      = "1000"
      }
      namespace = local.project_id
    }
  }

  

  traffic {
    percent         = 100
    latest_revision = true
  }
  autogenerate_revision_name = true 

  depends_on = [ google_project_service.run ]
}

data "google_iam_policy" "noauth" {
  binding {
    role = "roles/run.invoker"
    members = [
      "allUsers",
    ]
  }
}

resource "google_cloud_run_service_iam_policy" "noauth" {
  location    = google_cloud_run_service.machbarschaft.location
  project     = google_cloud_run_service.machbarschaft.project
  service     = google_cloud_run_service.machbarschaft.name

  policy_data = data.google_iam_policy.noauth.policy_data
}

resource "google_cloud_run_domain_mapping" "default" {
  location = var.region_run
  name     = "${local.env == "prd" ? "api" : "api-sta"}.${local.tld_naked}"

  metadata {
    namespace = local.project_id
    annotations = {
      "run.googleapis.com/launch-stage" = "BETA"
    }
  }

  spec {
    route_name     = google_cloud_run_service.machbarschaft.name
    force_override = true
  }
}

output "api_url" {
  value = google_cloud_run_service.machbarschaft.status[0].url
}
