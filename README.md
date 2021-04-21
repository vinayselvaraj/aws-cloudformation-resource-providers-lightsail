# AWS Lightsail CloudFormation Resource Provider

This repository contains a CloudFormation Resource Provider to create Lightsail instances.

## Requirements
The following are needed each time the AWS Lightsail CloudFormation Resource Provider is installed in an AWS account for a given region:
- Java 8
- Apache Maven
- CloudFormation CLI

## Usage

1. Install the resource provider in the AWS account & region where you will be creating Lightsail instances via CloudFormation.
```
cd lightsail-instance
mvn package
cfn submit --set-default
```

2. Create new CloudFormation template using the VS::Lightsail::Instance resource or use an existing one.  Use the [sample template](lightsail-instance/sample.yaml) as a reference.

```yaml
AWSTemplateFormatVersion: "2010-09-09"
Description: Sample template showing the usage of VS::Lightsail::Instance

Resources:
  LightsailInstance:
    Type: VS::Lightsail::Instance
    Properties:
        AvailabilityZone: us-east-1b
        BundleId: nano_2_0
        BlueprintId: amazon_linux
```

3. Create the CloudFormation stack then check to see that the Lightsail instance was created.

More details can be found in the [usage document](lightsail-instance/docs/README.md).
