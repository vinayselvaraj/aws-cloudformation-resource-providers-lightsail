package software.amazon.lightsail.cfn.instance;

import com.amazonaws.services.lightsail.AmazonLightsail;
import com.amazonaws.services.lightsail.AmazonLightsailClientBuilder;
import com.amazonaws.services.lightsail.model.CreateInstancesRequest;
import com.amazonaws.services.lightsail.model.CreateInstancesResult;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.OperationStatus;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

import java.util.Collections;
import java.util.UUID;

public class CreateHandler extends BaseHandler<CallbackContext> {

    private Logger logger;

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
        final AmazonWebServicesClientProxy proxy,
        final ResourceHandlerRequest<ResourceModel> request,
        final CallbackContext callbackContext,
        final Logger logger) {

        this.logger = logger;
        final ResourceModel model = request.getDesiredResourceState();

        logger.log(String.format("Invoked CreateHandler.handleRequest request=%s", request));

        String instanceName = model.getInstanceName();

        if(instanceName == null) {
            instanceName = UUID.randomUUID().toString();
        }

        AmazonLightsail lightsail = AmazonLightsailClientBuilder.defaultClient();
        CreateInstancesRequest createInstancesRequest = new CreateInstancesRequest()
                .withAvailabilityZone(model.getAvailabilityZone())
                .withBlueprintId(model.getBlueprintId())
                .withInstanceNames(Collections.singletonList(instanceName))
                .withAvailabilityZone(model.getAvailabilityZone())
                .withBundleId(model.getBundleId());
        CreateInstancesResult result = proxy.injectCredentialsAndInvoke(createInstancesRequest, lightsail::createInstances);

        return ProgressEvent.<ResourceModel, CallbackContext>builder()
            .resourceModel(model)
            .status(OperationStatus.SUCCESS)
            .build();
    }

}
