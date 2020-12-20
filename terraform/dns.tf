data "google_dns_managed_zone" "api" {
  project = "mbs-domain-management"
  name    = "machbarschaft-jetzt"
}

resource "google_dns_record_set" "api" {
  project      = "mbs-domain-management"
  name         = "${local.env == "prd" ? "api" : "api-sta"}.${data.google_dns_managed_zone.api.dns_name}"
  managed_zone = data.google_dns_managed_zone.api.name
  type         = "CNAME"
  ttl          = 300
  rrdatas      = [ "${replace(google_cloud_run_service.machbarschaft.status[0].url, "/^https:\\/\\//", "")}." ]
}


