package software.amazon.lightsail.cfn.instance;

import com.amazonaws.services.lightsail.AmazonLightsail;
import com.amazonaws.services.lightsail.AmazonLightsailClientBuilder;
import com.amazonaws.services.lightsail.model.*;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

import java.time.Duration;

public class DeleteHandler extends BaseHandler<CallbackContext> {

    private final AmazonLightsail lightsailClient = AmazonLightsailClientBuilder.defaultClient();

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
        final AmazonWebServicesClientProxy proxy,
        final ResourceHandlerRequest<ResourceModel> request,
        final CallbackContext callbackContext,
        final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();

        final CallbackContext currentContext = callbackContext == null
                ? CallbackContext.builder()
                .stabilizationRetriesRemaining(Constants.NUMBER_OF_STATUS_POLL_RETRIES)
                .build()
                : callbackContext;

        if (currentContext.getStabilizationRetriesRemaining() == 0) {
            throw new RuntimeException(Constants.DELETE_TIMED_OUT_MESSAGE);
        }

        if(currentContext.getStatus() != null && currentContext.getStatus().equals(Constants.STATUS_DELETE_PENDING)) {
            return checkDeleteStatus(proxy, request, currentContext, logger);
        }

        DeleteInstanceRequest deleteInstanceRequest = new DeleteInstanceRequest();
        deleteInstanceRequest.setInstanceName(model.getInstanceName());

        try {
            proxy.injectCredentialsAndInvoke(deleteInstanceRequest, lightsailClient::deleteInstance);
        } catch(NotFoundException e) {
            logger.log(String.format("Instance %s may already been deleted", model.getInstanceName()));
            return ProgressEvent.defaultSuccessHandler(model);
        }

        CallbackContext newCallbackContext = CallbackContext.builder()
                .stabilizationRetriesRemaining(currentContext.getStabilizationRetriesRemaining() - 1)
                .status(Constants.STATUS_DELETE_PENDING)
                .build();

        return ProgressEvent.defaultInProgressHandler(
                newCallbackContext,
                (int) Duration.ofSeconds(Constants.CALLBACK_DELAY_SECONDS).getSeconds(),
                model
        );
    }

    private ProgressEvent<ResourceModel, CallbackContext> checkDeleteStatus(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();

        GetInstanceRequest getInstanceRequest = new GetInstanceRequest();
        getInstanceRequest.setInstanceName(model.getInstanceName());

        Instance instance = null;

        try {
            GetInstanceResult getInstanceResult =
                    proxy.injectCredentialsAndInvoke(getInstanceRequest, lightsailClient::getInstance);
            instance = getInstanceResult.getInstance();
        } catch(NotFoundException e) {
            logger.log(String.format("Instance %s may already been deleted", model.getInstanceName()));
        }

        if(instance == null) {
            logger.log(String.format("Instance %s has been deleted", model.getInstanceName()));
            return ProgressEvent.defaultSuccessHandler(model);
        }

        logger.log(String.format("Still deleting instance %s", model.getInstanceName()));
        logger.log(instance.toString());
        CallbackContext newCallbackContext = CallbackContext.builder()
                .stabilizationRetriesRemaining(callbackContext.getStabilizationRetriesRemaining() - 1)
                .status(Constants.STATUS_DELETE_PENDING)
                .build();
        return ProgressEvent.defaultInProgressHandler(
                newCallbackContext,
                (int) Duration.ofSeconds(Constants.CALLBACK_DELAY_SECONDS).getSeconds(),
                model);
    }
}
