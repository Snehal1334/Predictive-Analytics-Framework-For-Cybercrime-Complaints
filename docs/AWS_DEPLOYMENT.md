# AWS Deployment Notes

## Suggested Production Topology

- Backend: ECS Fargate service behind an Application Load Balancer.
- Frontend: S3 static hosting with CloudFront, or an Nginx container on ECS.
- ML service: separate ECS Fargate service with autoscaling.
- Database: Amazon RDS MySQL 8 with automated backups.
- Secrets: AWS Secrets Manager for `JWT_SECRET`, database password, and service URLs.
- Logs: CloudWatch log groups per service.

## Deployment Steps

1. Build and push images to Amazon ECR for `backend`, `frontend`, and `ml-service`.
2. Create RDS MySQL and allow inbound traffic only from the backend security group.
3. Configure ECS task definitions with environment variables from Secrets Manager.
4. Configure ALB rules:
   - `/api/*` to backend
   - `/ml/*` optional internal route to ML service, or keep ML private
   - frontend as default target
5. Set `CORS_ALLOWED_ORIGINS` to the CloudFront or ALB frontend URL.
6. Enable HTTPS with ACM certificates.
