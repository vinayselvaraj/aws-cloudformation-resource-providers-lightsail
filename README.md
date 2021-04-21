# AWS Lightsail CloudFormation Resource Provider

This repository contains a CloudFormation Resource Provider to create Lightsail instances.  The resource provider is writeen in Java and can be used as an example to create other CloudFormation resource providers.  You can find more information on creating resource providers using the CloudFormation CLI [here](https://docs.aws.amazon.com/cloudformation-cli/latest/userguide/resource-types.html).

## Requirements
The following are needed each time the AWS Lightsail CloudFormation Resource Provider is installed in an AWS account for a given region:
- [Java 8](https://docs.aws.amazon.com/corretto/latest/corretto-8-ug/downloads-list.html)
- [Apache Maven](https://maven.apache.org)
- [CloudFormation CLI](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html)

## Usage

1. Install the resource provider in the AWS account & region where you will be creating Lightsail instances via CloudFormation.
```
cd lightsail-instance
mvn package
cfn submit --set-default
```

2. Now that the resource provider has been installed, you can use it to create resources using CloudFormation.  Create new CloudFormation template using the VS::Lightsail::Instance resource or use an existing one.  Use the [sample template](lightsail-instance/sample.yaml) as a reference.

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

3. Create the CloudFormation stack:
```
aws cloudformation deploy --template-file sample.yaml --stack-name lightsail-sample
```

4. Check the stack creation status in the CloudFormation console.  After the stack has been successfully created, you will also see the Lightsail instance that was created in the Amazon Lightsail console.

More details can be found in the [usage document](lightsail-instance/docs/README.md).
