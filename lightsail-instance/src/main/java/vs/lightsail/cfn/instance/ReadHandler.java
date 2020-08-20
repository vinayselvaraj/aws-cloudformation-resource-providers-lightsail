package vs.lightsail.cfn.instance;

import com.amazonaws.services.lightsail.AmazonLightsail;
import com.amazonaws.services.lightsail.AmazonLightsailClientBuilder;
import com.amazonaws.services.lightsail.model.GetInstanceRequest;
import com.amazonaws.services.lightsail.model.GetInstanceResult;
import com.amazonaws.services.lightsail.model.Instance;
import software.amazon.cloudformation.proxy.*;

public class ReadHandler extends BaseHandler<CallbackContext> {

    private final AmazonLightsail lightsailClient = AmazonLightsailClientBuilder.defaultClient();

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
        final AmazonWebServicesClientProxy proxy,
        final ResourceHandlerRequest<ResourceModel> request,
        final CallbackContext callbackContext,
        final Logger logger) {

        logger.log("Invoked ReadHandler.handleRequest");
        logger.log("callbackContext=" + callbackContext);
        logger.log("request=" + request);

        try {
            final ResourceModel model = request.getDesiredResourceState();
            GetInstanceRequest getInstanceRequest = new GetInstanceRequest();
            getInstanceRequest.setInstanceName(model.getInstanceName());

            GetInstanceResult getInstanceResult =
                    proxy.injectCredentialsAndInvoke(getInstanceRequest, lightsailClient::getInstance);


            ResourceModel fetchedModel = Translator.createModelFromInstance(getInstanceResult.getInstance());

            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .resourceModel(fetchedModel)
                    .status(OperationStatus.SUCCESS)
                    .build();

        } catch(Exception e) {
            return ProgressEvent.defaultFailureHandler(
                    e,
                    HandlerErrorCode.NotFound
            );
        }
    }
}
