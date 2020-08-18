package software.amazon.lightsail.cfn.instance;

import com.amazonaws.services.lightsail.AmazonLightsail;
import com.amazonaws.services.lightsail.AmazonLightsailClientBuilder;
import com.amazonaws.services.lightsail.model.*;
import software.amazon.cloudformation.exceptions.CfnServiceInternalErrorException;
import software.amazon.cloudformation.proxy.*;
import software.amazon.cloudformation.resource.IdentifierUtils;

import java.time.Duration;


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

        final CallbackContext currentContext = callbackContext == null
                ? CallbackContext.builder()
                .stabilizationRetriesRemaining(Constants.NUMBER_OF_STATUS_POLL_RETRIES)
                .build()
                : callbackContext;

        if (currentContext.getStabilizationRetriesRemaining() == 0) {
            throw new RuntimeException(Constants.CREATE_TIMED_OUT_MESSAGE);
        }

        if(currentContext.getStatus() != null
                && currentContext.getStatus().equals(Constants.STATUS_CREATION_PENDING)) {
            // Check the status of the instance creation
            return checkStatus(proxy, request, currentContext, logger);
        }

        if (model.getInstanceName() == null || model.getInstanceName().isEmpty()) {
            model.setInstanceName(
                    IdentifierUtils.generateResourceIdentifier(
                            "inst", request.getClientRequestToken(), 128));
        }

        CreateInstancesResult result = createResource(Translator.translateToCreateRequest(model), proxy);
        for(Operation op : result.getOperations()) {
            if(op != null && op.getOperationType().equals(Constants.CREATE_OPERATION)) {
                if(op.getErrorCode() != null) {
                    return ProgressEvent.defaultFailureHandler(
                            new InternalError(String.format("%s: %s", op.getErrorCode(), op.getErrorDetails())),
                            HandlerErrorCode.ServiceInternalError);
                }
            }
        }


        CallbackContext newCallbackContext = CallbackContext.builder()
                .stabilizationRetriesRemaining(currentContext.getStabilizationRetriesRemaining() - 1)
                .status(Constants.STATUS_CREATION_PENDING)
                .build();

        return ProgressEvent.defaultInProgressHandler(
                newCallbackContext,
                (int) Duration.ofSeconds(Constants.CALLBACK_DELAY_SECONDS).getSeconds(),
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
            final CallbackContext callbackContext,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();

        GetInstanceRequest getInstanceRequest = new GetInstanceRequest();
        getInstanceRequest.setInstanceName(model.getInstanceName());

        GetInstanceResult getInstanceResult =
                proxy.injectCredentialsAndInvoke(getInstanceRequest, lightsailClient::getInstance);
        Instance instance = getInstanceResult.getInstance();
        if(instance != null || instance.getState().getName().equals(Constants.INSTANCE_STATE_RUNNING)) {
            logger.log(String.format("Instance %s is running", model.getInstanceName()));
            return ProgressEvent.defaultSuccessHandler(model);
        }

        logger.log(String.format("Instance %s has not started running", model.getInstanceName()));
        CallbackContext newCallbackContext = CallbackContext.builder()
                .stabilizationRetriesRemaining(callbackContext.getStabilizationRetriesRemaining() - 1)
                .status(Constants.STATUS_CREATION_PENDING)
                .build();
        return ProgressEvent.defaultInProgressHandler(
                newCallbackContext,
                (int) Duration.ofSeconds(Constants.CALLBACK_DELAY_SECONDS).getSeconds(),
                model);
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
