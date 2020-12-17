# Terraform @mbs


## Start and Prerequisites

0. You must have terraform installed.
1. Create a project in GCP console. It must be assigned to an organization and billing account. In our case the organization is 'machbarschaft.jetzt'
2. Assign the role 'Owner' to `terraform@terraform-admin-machbarschaft.iam.gserviceaccount.com`

## Prepare Initial Cloud Build

The preparation of the initial cloud build avoids the failure of cloud run service in the consecutiove step.

0. You must have GCP gcloud tools installed.
1. Login to GCP by running `gcloud auth login`
2. Run `export PROJECT_ID=YOUR_PROJCT_ID` and replace PROJECT_ID with ID from the previously created project.
3. Set the gcloud project by running `gcloud config set project ${PROJECT_ID}`
4. Enable the GCP cloud build service `gcloud services enable cloudbuild.googleapis.com`
5. Build the project with `./gradlew clean assemble`
6. Run the commands as follows to submit a build job to Cloud Build:
```bash
touch empty.ignore
gcloud builds submit --ignore-file empty.ignore --tag gcr.io/${PROJECT_ID}/machbarschaft-api:develop
```

## IAC - Build Your Environment

0. Change into the terraform directory
1. Set some environment variables tp proceed:
```bash
export TF_CREDS=~/.config/gcloud/terraform-admin-machbarschaft.json
export TF_ADMIN=terraform-admin-machbarschaft
export GOOGLE_APPLICATION_CREDENTIALS=${TF_CREDS}
export GOOGLE_PROJECT=${TF_ADMIN}
```
1. Check the project_id in variables for the particular environment and amend it if required.
2. Set the workspace - `sta` or `prd`, which should be used, e.g. `terraform workspace select sta`
3. Run terraform with `terraform plan` followed by `terraform apply`

## General Remarks

The current firebase implementation by GCP and/or Terraform is far from stable. E.g. if you run `terraform destroy` it keeps the firebase project and related web apps alive but marks them in terraform state as deleted. Running `terraform apply` in the same projects fails due to existing objects. This could be healed by performing the mentioned import commands:
- `terraform import google_firebase_project.dashboard projects/PROJECT_ID`
- `terraform import google_firebase_web_app.dashboard projects/PROJECT_ID/webApps/APP_ID`

## Manual Steps

- Cloud DNS must be set up - we did this into another project, which allows us to manipulate DNS entries but keep the project longer than the ones holding the respective objects.
- [https://www.google.com/webmasters/verification/home](https://www.google.com/webmasters/verification/home) add email address of terraform user
- Add CNAME records for every subdomain in form `CNAME SUBDOMAIN ghs.googlehosted.com`
- Set secret PROJECT_ID_STA in GitHub
- Set secret GCLOUD_AUTH_STA in GitHub
