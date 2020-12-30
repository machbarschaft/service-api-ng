resource "google_project_service" "run" {
  service            = "run.googleapis.com"
  project            = local.project_id
  disable_on_destroy = false
}

resource "google_cloud_run_service" "machbarschaft" {
  provider = google-beta
  name     = "machbarschaft-api-${local.env}"
  location = var.region_run
  project  = local.project_id

  template {
    spec {
      containers {
        # We must build this image in advance otherwise the service fails
        image = "gcr.io/${local.project_id}/machbarschaft-api:${local.env == "prd" ? "stable": "develop"}"
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
        "run.googleapis.com/launch-stage"       = "BETA"
        "run.googleapis.com/cloudsql-instances" = google_sql_database_instance.master.connection_name
        "autoscaling.knative.dev/minScale"      = "1"
        "autoscaling.knative.dev/maxScale"      = "1000"
      }
      # namespace = local.project_id
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

resource "google_cloud_run_service_iam_policy" "noauth-api" {
  location    = google_cloud_run_service.machbarschaft.location
  project     = google_cloud_run_service.machbarschaft.project
  service     = google_cloud_run_service.machbarschaft.name

  policy_data = data.google_iam_policy.noauth.policy_data
}

resource "google_cloud_run_domain_mapping" "default" {
  provider = google-beta
  location = var.region_run
  name     = "${local.subdomain_api}.${local.tld_naked}"

  # TODO: Google managed certificate
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


resource "google_cloud_run_service" "dashboard" {
  provider = google-beta
  name     = "machbarschaft-dashboard-${local.env}"
  location = var.region_run
  project  = local.project_id

  template {
    spec {
      containers {
        # We must build this image in advance otherwise the service fails
        image = "gcr.io/${local.project_id}/machbarschaft-dashboard:latest"
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
        "run.googleapis.com/launch-stage"       = "BETA"
        # "autoscaling.knative.dev/minScale"      = "1"
        "autoscaling.knative.dev/maxScale"      = "1000"
      }
      # namespace = local.project_id
    }
  }

  traffic {
    percent         = 100
    latest_revision = true
  }
  autogenerate_revision_name = true 

  depends_on = [ google_project_service.run ]
}

resource "google_cloud_run_service_iam_policy" "noauth-dashboard" {
  location    = google_cloud_run_service.dashboard.location
  project     = google_cloud_run_service.dashboard.project
  service     = google_cloud_run_service.dashboard.name

  policy_data = data.google_iam_policy.noauth.policy_data
}

resource "google_cloud_run_domain_mapping" "dashboard" {
  provider = google-beta
  location = var.region_run
  name     = "${local.subdomain_dashboard}.${local.tld_naked}"

  # TODO: Google managed certificate
  metadata {
    namespace = local.project_id
    annotations = {
      "run.googleapis.com/launch-stage" = "BETA"
    }
  }

  spec {
    route_name     = google_cloud_run_service.dashboard.name
    force_override = true
  }
}

output "dashboard_url" {
  value = google_cloud_run_service.dashboard.status[0].url
}


resource "google_cloud_run_service" "webapp" {
  provider = google-beta
  name     = "machbarschaft-webapp-${local.env}"
  location = var.region_run
  project  = local.project_id

  template {
    spec {
      containers {
        # We must build this image in advance otherwise the service fails
        image = "gcr.io/${local.project_id}/machbarschaft-webapp:latest"
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
        "run.googleapis.com/launch-stage"       = "BETA"
        # "autoscaling.knative.dev/minScale"      = "1"
        "autoscaling.knative.dev/maxScale"      = "1000"
      }
      # namespace = local.project_id
    }
  }

  traffic {
    percent         = 100
    latest_revision = true
  }
  autogenerate_revision_name = true 

  depends_on = [ google_project_service.run ]
}

resource "google_cloud_run_service_iam_policy" "noauth-webapp" {
  location    = google_cloud_run_service.webapp.location
  project     = google_cloud_run_service.webapp.project
  service     = google_cloud_run_service.webapp.name

  policy_data = data.google_iam_policy.noauth.policy_data
}

resource "google_cloud_run_domain_mapping" "webapp" {
  provider = google-beta
  location = var.region_run
  name     = "${local.subdomain_webapp}.${local.tld_naked}"

  # TODO: Google managed certificate
  metadata {
    namespace = local.project_id
    annotations = {
      "run.googleapis.com/launch-stage" = "BETA"
    }
  }

  spec {
    route_name     = google_cloud_run_service.webapp.name
    force_override = true
  }
}

output "webapp_url" {
  value = google_cloud_run_service.webapp.status[0].url
}


data "google_client_config" "current" {
}

output "project" {
  value = data.google_client_config.current.project
}