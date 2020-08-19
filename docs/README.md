# VS::LightSail::Instance

LightSail Instance Resource Provider

## Syntax

To declare this entity in your AWS CloudFormation template, use the following syntax:

### JSON

<pre>
{
    "Type" : "VS::LightSail::Instance",
    "Properties" : {
        "<a href="#availabilityzone" title="AvailabilityZone">AvailabilityZone</a>" : <i>String</i>,
        "<a href="#blueprintid" title="BlueprintId">BlueprintId</a>" : <i>String</i>,
        "<a href="#bundleid" title="BundleId">BundleId</a>" : <i>String</i>,
        "<a href="#userdata" title="UserData">UserData</a>" : <i>String</i>
    }
}
</pre>

### YAML

<pre>
Type: VS::LightSail::Instance
Properties:
    <a href="#availabilityzone" title="AvailabilityZone">AvailabilityZone</a>: <i>String</i>
    <a href="#blueprintid" title="BlueprintId">BlueprintId</a>: <i>String</i>
    <a href="#bundleid" title="BundleId">BundleId</a>: <i>String</i>
    <a href="#userdata" title="UserData">UserData</a>: <i>String</i>
</pre>

## Properties

#### AvailabilityZone

The Availability Zone in which to create your instance. Use the following format: us-east-2a (case sensitive). You can get a list of Availability Zones by using the get regions operation. Be sure to add the include Availability Zones parameter to your request.

_Required_: Yes

_Type_: String

_Pattern_: <code>.*\S.*</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### BlueprintId

The ID for a virtual private server image (e.g., app_wordpress_4_4 or app_lamp_7_0). Use the get blueprints operation to return a list of available images (or blueprints).

_Required_: Yes

_Type_: String

_Pattern_: <code>.*\S.*</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### BundleId

The bundle of specification information for your virtual private server (or instance), including the pricing plan (e.g., micro_1_0).

_Required_: Yes

_Type_: String

_Pattern_: <code>.*\S.*</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### UserData

User data script to use when the instance starts up

_Required_: No

_Type_: String

_Pattern_: <code>.*\S.*</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

## Return Values

### Ref

When you pass the logical ID of this resource to the intrinsic `Ref` function, Ref returns the InstanceName.

### Fn::GetAtt

The `Fn::GetAtt` intrinsic function returns a value for a specified attribute of this type. The following are the available attributes and sample return values.

For more information about using the `Fn::GetAtt` intrinsic function, see [Fn::GetAtt](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/intrinsic-function-reference-getatt.html).

#### InstanceName

The names to use for your new Lightsail instances.

#### Arn

Returns the <code>Arn</code> value.

