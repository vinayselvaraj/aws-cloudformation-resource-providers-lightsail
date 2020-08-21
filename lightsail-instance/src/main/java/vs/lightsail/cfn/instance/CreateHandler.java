package vs.lightsail.cfn.instance;

import com.amazonaws.services.lightsail.AmazonLightsail;
import com.amazonaws.services.lightsail.AmazonLightsailClientBuilder;
import com.amazonaws.services.lightsail.model.*;
import com.amazonaws.util.StringUtils;
import software.amazon.cloudformation.exceptions.CfnAlreadyExistsException;
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

        logger.log("Invoked CreateHandler.handleRequest");
        logger.log("callbackContext=" + callbackContext);
        logger.log("request=" + request);

        this.logger = logger;
        ResourceModel model = request.getDesiredResourceState();

        final CallbackContext currentContext = callbackContext == null
                ? CallbackContext.builder()
                .stabilizationRetriesRemaining(Constants.NUMBER_OF_STATUS_POLL_RETRIES)
                .build()
                : callbackContext;

        if (currentContext.getStabilizationRetriesRemaining() == 0) {
            throw new RuntimeException(Constants.CREATE_TIMED_OUT_MESSAGE);
        }

        // If status is set and set to pending
        if(currentContext.getStatus() != null
                && currentContext.getStatus().equals(Constants.STATUS_CREATION_PENDING)) {
            // Check the status of the instance creation
            return checkStatus(proxy, request, currentContext);
        }

        // Generate instance name if it is not set
        if (StringUtils.isNullOrEmpty(model.getInstanceName())) {
            model.setInstanceName(
                    IdentifierUtils.generateResourceIdentifier(
                            "inst", request.getClientRequestToken(), 128));
        }

        // Check if instance with the same name already exists
        if(SharedHelper.doesInstanceExist(model, proxy, lightsailClient)) {
            throw new CfnAlreadyExistsException(ResourceModel.TYPE_NAME, model.getInstanceName());
        }

        // Create instance and get result
        CreateInstancesResult result = proxy.injectCredentialsAndInvoke(
                SharedHelper.translateToCreateRequest(model),
                lightsailClient::createInstances);

        // Check if create operation exists in result
        for(Operation op : result.getOperations()) {
            if(op != null && op.getOperationType().equals(Constants.CREATE_OPERATION)) {
                if(op.getErrorCode() != null) {
                    return ProgressEvent.defaultFailureHandler(
                            new InternalError(String.format("%s: %s", op.getErrorCode(), op.getErrorDetails())),
                            HandlerErrorCode.ServiceInternalError);
                }
            }
        }

        logger.log("Returning model: " + model);

        CallbackContext newCallbackContext = CallbackContext.builder()
                .stabilizationRetriesRemaining(currentContext.getStabilizationRetriesRemaining() - 1)
                .status(Constants.STATUS_CREATION_PENDING)
                .build();

        return ProgressEvent.defaultInProgressHandler(
                newCallbackContext,
                Constants.CALLBACK_DELAY_SECONDS,
                model
        );
    }

    private CreateInstancesResult createResource(final CreateInstancesRequest createInstancesRequest,
                                                final AmazonWebServicesClientProxy proxy) {
        try {
            return proxy.injectCredentialsAndInvoke(createInstancesRequest, lightsailClient::createInstances);
        } catch (final Exception e) {
            throw new CfnServiceInternalErrorException("CreateInstance", e);
        }
    }

    private ProgressEvent<ResourceModel, CallbackContext> checkStatus(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext) {

        ResourceModel model = request.getDesiredResourceState();

        GetInstanceRequest getInstanceRequest = new GetInstanceRequest();
        getInstanceRequest.setInstanceName(model.getInstanceName());

        GetInstanceResult getInstanceResult =
                proxy.injectCredentialsAndInvoke(getInstanceRequest, lightsailClient::getInstance);
        Instance instance = getInstanceResult.getInstance();
        if(instance != null || instance.getState().getName().equals(Constants.INSTANCE_STATE_RUNNING)) {
            logger.log(String.format("Instance %s is running", model.getInstanceName()));
            ResourceModel fetchedModel = SharedHelper.createModelFromInstance(instance);
            logger.log("Returning model after instance creation: " + fetchedModel);
            return ProgressEvent.defaultSuccessHandler(fetchedModel);
        }

        logger.log(String.format("Instance %s has not started running", model.getInstanceName()));
        CallbackContext newCallbackContext = CallbackContext.builder()
                .stabilizationRetriesRemaining(callbackContext.getStabilizationRetriesRemaining() - 1)
                .status(Constants.STATUS_CREATION_PENDING)
                .build();
        return ProgressEvent.defaultInProgressHandler(
                newCallbackContext,
                Constants.CALLBACK_DELAY_SECONDS,
                request.getDesiredResourceState());
    }

}
