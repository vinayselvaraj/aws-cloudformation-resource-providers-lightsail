package vs.lightsail.cfn.instance;

import com.amazonaws.services.lightsail.AmazonLightsail;
import com.amazonaws.services.lightsail.AmazonLightsailClientBuilder;
import com.amazonaws.services.lightsail.model.GetInstancesRequest;
import com.amazonaws.services.lightsail.model.GetInstancesResult;
import software.amazon.cloudformation.proxy.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListHandler extends BaseHandler<CallbackContext> {

    private final AmazonLightsail lightsailClient = AmazonLightsailClientBuilder.defaultClient();

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
        final AmazonWebServicesClientProxy proxy,
        final ResourceHandlerRequest<ResourceModel> request,
        final CallbackContext callbackContext,
        final Logger logger) {

        logger.log("Invoked ListHandler.handleRequest");

        final List<ResourceModel> models = new ArrayList<>();

        try {
            GetInstancesResult getInstancesResult = proxy.injectCredentialsAndInvoke(
                    new GetInstancesRequest().withPageToken(request.getNextToken()),
                    lightsailClient::getInstances);
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .status(OperationStatus.SUCCESS)
                    .resourceModels(createListResourceModels(getInstancesResult))
                    .nextToken(getInstancesResult.getNextPageToken())
                    .build();
        } catch(Exception e) {
            return ProgressEvent.defaultFailureHandler(e, HandlerErrorCode.InternalFailure);
        }

    }

    private List<ResourceModel> createListResourceModels(final GetInstancesResult response) {
        return response.getInstances()
                .stream()
                .map(instance -> Translator.createModelFromInstance(instance))
                .collect(Collectors.toList());
    }
}
