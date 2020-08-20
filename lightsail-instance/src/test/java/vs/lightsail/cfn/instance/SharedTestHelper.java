package vs.lightsail.cfn.instance;

import com.amazonaws.services.lightsail.model.GetInstanceRequest;
import com.amazonaws.services.lightsail.model.GetInstanceResult;
import com.amazonaws.services.lightsail.model.Instance;
import com.amazonaws.services.lightsail.model.ResourceLocation;
import org.mockito.Mockito;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;

public class SharedTestHelper {

    static void mockGetInstance(final ResourceModel model, final AmazonWebServicesClientProxy proxy) {
        GetInstanceResult mockResult = new GetInstanceResult();
        Instance mockInstance = new Instance();
        mockInstance.setBlueprintId(model.getBlueprintId());
        mockInstance.setBundleId(model.getBundleId());
        mockInstance.setLocation(new ResourceLocation().withAvailabilityZone(model.getAvailabilityZone()));
        mockInstance.setName(model.getInstanceName());
        mockResult.setInstance(mockInstance);
        Mockito.doReturn(mockResult).when(proxy).injectCredentialsAndInvoke(Mockito.any(GetInstanceRequest.class), Mockito.any());
    }

}
