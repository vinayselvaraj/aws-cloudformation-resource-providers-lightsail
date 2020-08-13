package software.amazon.lightsail.cfn.instance;

import com.amazonaws.services.lightsail.model.CreateInstancesRequest;
import com.amazonaws.services.lightsail.model.CreateInstancesResult;
import org.mockito.Mockito;
import software.amazon.cloudformation.proxy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CreateHandlerTest {

    private CreateHandler handler;

    @Mock
    private AmazonWebServicesClientProxy proxy;

    @Mock
    private Logger logger;

    @BeforeEach
    public void setup() {
        logger = Mockito.mock(Logger.class);
        proxy = Mockito.mock(AmazonWebServicesClientProxy.class);
        handler = new CreateHandler();
    }

    @Test
    public void handleRequest_SimpleSuccess() {

        Mockito.doReturn(new CreateInstancesResult()).when(proxy).injectCredentialsAndInvoke(Mockito.any(CreateInstancesRequest.class), Mockito.any());

        final ResourceModel model = ResourceModel.builder().build();
        model.setBundleId("nano_2_0");
        model.setBlueprintId("amazon_linux");
        model.setAvailabilityZone("us-east-1a");

        final ResourceHandlerRequest<ResourceModel> request = ResourceHandlerRequest.<ResourceModel>builder()
            .desiredResourceState(model)
            .build();

        final ProgressEvent<ResourceModel, CallbackContext> response
            = handler.handleRequest(proxy, request, null, logger);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(OperationStatus.SUCCESS);
        assertThat(response.getCallbackContext()).isNull();
        assertThat(response.getCallbackDelaySeconds()).isEqualTo(0);
        assertThat(response.getResourceModel()).isEqualTo(request.getDesiredResourceState());
        assertThat(response.getResourceModels()).isNull();
        assertThat(response.getMessage()).isNull();
        assertThat(response.getErrorCode()).isNull();
    }
}
