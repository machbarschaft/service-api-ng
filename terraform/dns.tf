data "google_dns_managed_zone" "api" {
  project = "mbs-domain-management"
  name    = "machbarschaft-jetzt"
}

resource "google_dns_record_set" "api" {
  project      = "mbs-domain-management"
  name         = "${local.subdomain_api}.${data.google_dns_managed_zone.api.dns_name}"
  managed_zone = data.google_dns_managed_zone.api.name
  type         = "CNAME"
  ttl          = 300
  rrdatas      = [ "ghs.googlehosted.com." ]
}

resource "google_dns_record_set" "dashboard" {
  project      = "mbs-domain-management"
  name         = "${local.subdomain_dashboard}.${data.google_dns_managed_zone.api.dns_name}"
  managed_zone = data.google_dns_managed_zone.api.name
  type         = "CNAME"
  ttl          = 300
  rrdatas      = [ "ghs.googlehosted.com." ]
}

resource "google_dns_record_set" "webapp" {
  project      = "mbs-domain-management"
  name         = "${local.subdomain_webapp}.${data.google_dns_managed_zone.api.dns_name}"
  managed_zone = data.google_dns_managed_zone.api.name
  type         = "CNAME"
  ttl          = 300
  rrdatas      = [ "ghs.googlehosted.com." ]
}
