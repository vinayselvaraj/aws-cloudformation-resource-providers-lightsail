package software.amazon.lightsail.cfn.instance;

import com.amazonaws.services.lightsail.model.CreateInstancesRequest;
import com.amazonaws.services.lightsail.model.CreateInstancesResult;
import com.amazonaws.services.lightsail.model.Operation;
import org.mockito.Mockito;
import software.amazon.cloudformation.proxy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.UUID;

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


        CreateInstancesResult mockResult = new CreateInstancesResult();
        mockResult.setOperations(new ArrayList<Operation>());

        Mockito.doReturn(mockResult).when(proxy).injectCredentialsAndInvoke(Mockito.any(CreateInstancesRequest.class), Mockito.any());

        final ResourceModel model = ResourceModel.builder().build();
        model.setBundleId("nano_2_0");
        model.setBlueprintId("amazon_linux");
        model.setAvailabilityZone("us-east-1a");

        final ResourceHandlerRequest<ResourceModel> request = ResourceHandlerRequest.<ResourceModel>builder()
                .desiredResourceState(model)
                .clientRequestToken(UUID.randomUUID().toString())
                .build();

        ProgressEvent<ResourceModel, CallbackContext> response = null;
        try {
            response = handler.handleRequest(proxy, request, null, logger);
        } catch(Exception e) {
            e.printStackTrace();
        }

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(OperationStatus.IN_PROGRESS);
        assertThat(response.getCallbackContext()).isNotNull();
        assertThat(response.getCallbackDelaySeconds()).isEqualTo(Constants.CALLBACK_DELAY_SECONDS);
        assertThat(response.getResourceModel()).isEqualTo(request.getDesiredResourceState());
        assertThat(response.getResourceModels()).isNull();
        assertThat(response.getMessage()).isNull();
        assertThat(response.getErrorCode()).isNull();
    }
}
