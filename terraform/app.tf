resource "google_project_service" "appengine" {
  service            = "appengine.googleapis.com"
  project            = local.project_id
  disable_on_destroy = false
}

resource "google_app_engine_application" "dashboard" {
  project     = local.project_id
  location_id = var.location_long

  depends_on  = [ google_project_service.appengine ]
}


resource "google_app_engine_domain_mapping" "domain_mapping" {
  domain_name       = "${local.env == "prd" ? "ds" : "ds-sta"}.${local.tld_naked}"

  # DomainOverrideStrategy.OVERRIDE
  override_strategy = "OVERRIDE"

  ssl_settings {
    ssl_management_type = "AUTOMATIC"
  }

  depends_on = [ google_project_service.appengine ]
}