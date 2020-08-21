package vs.lightsail.cfn.instance;

import com.amazonaws.services.lightsail.AmazonLightsail;
import com.amazonaws.services.lightsail.AmazonLightsailClientBuilder;
import com.amazonaws.services.lightsail.model.*;
import software.amazon.cloudformation.exceptions.CfnNotFoundException;
import software.amazon.cloudformation.exceptions.CfnNotStabilizedException;
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

        logger.log("Invoked DeleteHandler.handleRequest");
        logger.log("callbackContext=" + callbackContext);
        logger.log("request=" + request);

        final ResourceModel model = request.getDesiredResourceState();

        final CallbackContext currentContext = callbackContext == null
                ? CallbackContext.builder()
                .stabilizationRetriesRemaining(Constants.NUMBER_OF_STATUS_POLL_RETRIES)
                .build()
                : callbackContext;

        if (currentContext.getStabilizationRetriesRemaining() == 0) {
            throw new CfnNotStabilizedException(ResourceModel.TYPE_NAME, model.getInstanceName());
        }

        if(currentContext.getStatus() != null && currentContext.getStatus().equals(Constants.STATUS_DELETE_PENDING)) {
            return checkDeleteStatus(proxy, request, currentContext, logger);
        }

        DeleteInstanceRequest deleteInstanceRequest = new DeleteInstanceRequest();
        deleteInstanceRequest.setInstanceName(model.getInstanceName());

        if(!SharedHelper.doesInstanceExist(model, proxy, logger, lightsailClient)) {
            throw new CfnNotFoundException(ResourceModel.TYPE_NAME, model.getInstanceName());
        }

        proxy.injectCredentialsAndInvoke(deleteInstanceRequest, lightsailClient::deleteInstance);

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

        if(SharedHelper.doesInstanceExist(model, proxy, logger, lightsailClient)) {
            CallbackContext newCallbackContext = CallbackContext.builder()
                    .stabilizationRetriesRemaining(callbackContext.getStabilizationRetriesRemaining() - 1)
                    .status(Constants.STATUS_DELETE_PENDING)
                    .build();
            return ProgressEvent.defaultInProgressHandler(
                    newCallbackContext,
                    (int) Duration.ofSeconds(Constants.CALLBACK_DELAY_SECONDS).getSeconds(),
                    model);
        }

        return ProgressEvent.defaultSuccessHandler(null);
    }
}
