# secrets.auto.tfvars - name is intentional ;-)
variable "org_id" {
  type        = number
  description = "GCP ID of the organization. A required information for some resources."
}
variable "billing_account" {
  type        = string
  description = "String ID for the GCP billing account"
}
variable "project_id_sta" {
  type        = string
  description = "Staging project ID"
}
variable "project_id_prd" {
  type        = string
  description = "Production project ID"
}

# terraform.tfvars - not so secret
variable "region" {
  type        = string
  description = "This is our general region, which most resources utilize."
}
variable "region_run" {
  type        = string
  description = "This region provides additional services, e.g. cloud run domain mappings."
}
variable "location" {
  type        = string
  description = "GCP provides different names for location and terraform uses both, e.g. EU"
}
variable "location_long" {
  type        = string
  description = "GCP provides different names for location and terraform uses both, e.g. europe-west"
}


locals {
  env         = terraform.workspace

  tld_naked   = trimsuffix(data.google_dns_managed_zone.api.dns_name, ".")

  # terraform workspace aware vars - just use workspaces without var headaches
  project_ids = {
    default = "defaulty"
    sta     = var.project_id_sta
    prd     = var.project_id_prd
  }
  project_id  = local.project_ids[local.env]

  sql_instance_sizes = {
    default = "db-f1-micro"
    sta     = "db-f1-micro"
    prd     = "db-g1-small"
  }
  sql_instance_size  = local.sql_instance_sizes[local.env]
}
