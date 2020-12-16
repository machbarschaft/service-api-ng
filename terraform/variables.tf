variable "org_id" {}

variable "billing_account" {}

variable "project_id_sta" {}

variable "project_id_prd" {}

variable "region" {
  default  = "europe-west3"
}

variable "region_run" {
  default  = "europe-west1"
}

variable "location" {
  default  = "EU"
}

locals {
  env = terraform.workspace

  # Currently not used because we build upon default VPC thus cidr definition unused
  cidrs = {
    default = "10.90"
    sta     = "10.20"
    prd     = "10.10"
  }
  cidr = local.cidrs[local.env]

  project_ids = {
    default = "defaulty"
    sta     = var.project_id_sta
    prd     = var.project_id_prd
  }
  project_id = local.project_ids[local.env]

  sql_instance_sizes = {
    default = "db-f1-micro"
    sta     = "db-f1-micro"
    prd     = "db-n1-standard-1"
  }
  sql_instance_size = local.sql_instance_sizes[local.env]
}
