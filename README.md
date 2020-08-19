# AWS LightSail CloudFormation Resource Provider

This repository contains a CloudFormation Resource Provider to create LightSail instances.

## Usage

1. Install the resource provider in the AWS account & region where you will be creating LightSail instances via CloudFormation.
```
cd lightsail-instance
mvn package
cfn submit --set-default
```

2. Create new CloudFormation template using the VS::LightSail::Instance resource or use an existing one.  Use the [sample template](https://github.com/vinayselvaraj/aws-cloudformation-resource-providers-lightsail/blob/master/lightsail-instance/sample.yaml) as a reference.

3. Create the CloudFormation stack then check to see that the LightSail instance was created.

More details can be found in the [usage document](https://github.com/vinayselvaraj/aws-cloudformation-resource-providers-lightsail/blob/master/lightsail-instance/docs/README.md).