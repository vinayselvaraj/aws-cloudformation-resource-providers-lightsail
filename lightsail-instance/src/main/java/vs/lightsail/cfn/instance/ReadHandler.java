package vs.lightsail.cfn.instance;

import com.amazonaws.services.lightsail.AmazonLightsail;
import com.amazonaws.services.lightsail.AmazonLightsailClientBuilder;
import com.amazonaws.services.lightsail.model.GetInstanceRequest;
import com.amazonaws.services.lightsail.model.GetInstanceResult;
import com.amazonaws.services.lightsail.model.Instance;
import com.amazonaws.services.lightsail.model.NotFoundException;
import software.amazon.cloudformation.exceptions.CfnNotFoundException;
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

        final ResourceModel model = request.getDesiredResourceState();
        GetInstanceRequest getInstanceRequest = new GetInstanceRequest();
        getInstanceRequest.setInstanceName(model.getInstanceName());

        try {
            GetInstanceResult getInstanceResult =
                    proxy.injectCredentialsAndInvoke(getInstanceRequest, lightsailClient::getInstance);

            ResourceModel fetchedModel = Translator.createModelFromInstance(getInstanceResult.getInstance());
            logger.log(("Read instance: " + fetchedModel));


            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .resourceModel(fetchedModel)
                    .status(OperationStatus.SUCCESS)
                    .build();
        } catch(NotFoundException e) {
            throw new CfnNotFoundException(ResourceModel.TYPE_NAME, model.getInstanceName());
        }

    }
}
