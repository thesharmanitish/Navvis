sudo curl -Lo /usr/local/bin/ecs-cli https://amazon-ecs-cli.s3.amazonaws
dnf install gnupg
wget https://gist.githubusercontent.com/raphaelmansuy/5aab3c9e6c03e532e9dcf6c97c78b4ff/raw/f39b4df58833f09eb381700a6a854b1adfea482e/ecs-cli-signature-key.key
gpg --import ./signature.key
sudo chmod +x /usr/local/bin/ecs-cli
export AWS_ACCESS_KEY_ID=
export AWS_SECRET_ACCESS_KEY=
export AWS_DEFAULT_REGION=us-east-1
aws ec2 create-key-pair --key-name phone-service-cluster --query 'Key' --output text > ~/.ssh/phone-service-cluster.pem
KEY_PAIR=phone-service-cluster
    ecs-cli up \
      --keypair $KEY_PAIR  \
      --capability-iam \
      --size 2 \
      --instance-type t3.medium \
      --tags project=phone-service,owner=nitish \
      --cluster-config phone-service \
      --ecs-profile phone-service

ecs-cli compose --project-name phone-service  --file docker-compose.yml \
--debug service up  \
--deployment-max-percent 100 --deployment-min-healthy-percent 0 \
--region us-east-1--ecs-profile tutorial --cluster-config phone-service




