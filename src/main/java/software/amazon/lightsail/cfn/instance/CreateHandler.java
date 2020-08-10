package software.amazon.lightsail.cfn.instance;

import com.amazonaws.services.lightsail.AmazonLightsail;
import com.amazonaws.services.lightsail.AmazonLightsailClientBuilder;
import com.amazonaws.services.lightsail.model.CreateInstancesRequest;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.OperationStatus;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

import java.util.Collections;

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

        AmazonLightsail lightsail = AmazonLightsailClientBuilder.defaultClient();
        lightsail.createInstances(
                new CreateInstancesRequest()
                    .withAvailabilityZone(model.getAvailabilityZone())
                    .withBlueprintId(model.getBlueprintId())
                    .withInstanceNames(Collections.singletonList("foobar"))
                    .withAvailabilityZone(model.getAvailabilityZone())
                    .withBundleId(model.getBundleId())
        );


        return ProgressEvent.<ResourceModel, CallbackContext>builder()
            .resourceModel(model)
            .status(OperationStatus.SUCCESS)
            .build();
    }
}
