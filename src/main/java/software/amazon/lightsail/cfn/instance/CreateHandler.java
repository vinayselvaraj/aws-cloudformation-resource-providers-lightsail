package software.amazon.lightsail.cfn.instance;

import com.amazonaws.services.lightsail.AmazonLightsail;
import com.amazonaws.services.lightsail.AmazonLightsailClientBuilder;
import com.amazonaws.services.lightsail.model.CreateInstancesRequest;
import com.amazonaws.services.lightsail.model.CreateInstancesResult;
import com.amazonaws.services.lightsail.model.Operation;
import software.amazon.cloudformation.exceptions.CfnServiceInternalErrorException;
import software.amazon.cloudformation.proxy.*;
import software.amazon.cloudformation.resource.IdentifierUtils;


public class CreateHandler extends BaseHandler<CallbackContext> {

    private Logger logger;
    private final AmazonLightsail lightsailClient = AmazonLightsailClientBuilder.defaultClient();

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
        final AmazonWebServicesClientProxy proxy,
        final ResourceHandlerRequest<ResourceModel> request,
        final CallbackContext callbackContext,
        final Logger logger) {

        this.logger = logger;
        final ResourceModel model = request.getDesiredResourceState();

        if(model.getArn() != null) {
            // Check the status of the instance creation
        }

        if (model.getInstanceName() == null || model.getInstanceName().isEmpty()) {
            model.setInstanceName(
                    IdentifierUtils.generateResourceIdentifier(
                            "inst-", request.getClientRequestToken(), 128));
        }

        CreateInstancesResult result = createResource(Translator.translateToCreateRequest(model), proxy);
        for(Operation op : result.getOperations()) {
            if(op != null) {
                logger.log(op.toString());
            }
        }

        return ProgressEvent.defaultSuccessHandler(model);


//        return proxy.initiate("lightsail::CreateInstances", proxyClient, model, callbackContext)
//                .translateToServiceRequest((resourceModel) -> Translator.translateToCreateRequest(resourceModel))
//                .makeServiceCall(this::createResource)
//                .stabilize(CreateHandler::stabilize)
//                .progress();
    }

    private CreateInstancesResult createResource(final CreateInstancesRequest createInstancesRequest,
                                                final AmazonWebServicesClientProxy proxy) {
        try {
            return proxy.injectCredentialsAndInvoke(createInstancesRequest, lightsailClient::createInstances);
        } catch (final Exception e) {
            throw new CfnServiceInternalErrorException("CreateInstance", e);
        }
    }

    protected static boolean stabilize(
            final CreateInstancesRequest createInstancesRequest,
            final CreateInstancesResult createInstancesResult,
            final AmazonWebServicesClientProxy proxy,
            final ResourceModel resourceModel,
            final CallbackContext callbackContext
    ) {
        return true;
    }

}
