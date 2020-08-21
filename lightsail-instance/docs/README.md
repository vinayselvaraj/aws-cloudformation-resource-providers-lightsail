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
        "<a href="#instancename" title="InstanceName">InstanceName</a>" : <i>String</i>,
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
    <a href="#instancename" title="InstanceName">InstanceName</a>: <i>String</i>
    <a href="#blueprintid" title="BlueprintId">BlueprintId</a>: <i>String</i>
    <a href="#bundleid" title="BundleId">BundleId</a>: <i>String</i>
    <a href="#userdata" title="UserData">UserData</a>: <i>String</i>
</pre>

## Properties

#### AvailabilityZone

The Availability Zone in which to create your instance. Use the following format: us-east-2a (case sensitive). You can get a list of Availability Zones by using the get regions operation. Be sure to add the include Availability Zones parameter to your request.

_Required_: Yes

_Type_: String

_Update requires_: [Replacement](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-replacement)

#### InstanceName

The names to use for your new Lightsail instances.

_Required_: No

_Type_: String

_Minimum_: <code>2</code>

_Maximum_: <code>255</code>

_Pattern_: <code>^[a-zA-Z0-9][a-zA-Z0-9\\-_.]{2,254}</code>

_Update requires_: [Replacement](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-replacement)

#### BlueprintId

The ID for a virtual private server image (e.g., app_wordpress_4_4 or app_lamp_7_0). Use the get blueprints operation to return a list of available images (or blueprints).

_Required_: Yes

_Type_: String

_Update requires_: [Replacement](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-replacement)

#### BundleId

The bundle of specification information for your virtual private server (or instance), including the pricing plan (e.g., micro_1_0).

_Required_: Yes

_Type_: String

_Update requires_: [Replacement](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-replacement)

#### UserData

User data script to use when the instance starts up

_Required_: No

_Type_: String

_Update requires_: [Replacement](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-replacement)

## Return Values

### Ref

When you pass the logical ID of this resource to the intrinsic `Ref` function, Ref returns the InstanceName.
