package vs.lightsail.cfn.instance;

import com.amazonaws.services.lightsail.AmazonLightsail;
import com.amazonaws.services.lightsail.model.*;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;

import java.util.Collections;

public class SharedHelper {

    static boolean doesInstanceExist(
            final ResourceModel model,
            final AmazonWebServicesClientProxy proxy,
            final AmazonLightsail lightsailClient) {
        GetInstanceRequest getInstanceRequest = new GetInstanceRequest();
        getInstanceRequest.setInstanceName(model.getInstanceName());

        try {
            GetInstanceResult getInstanceResult =
                    proxy.injectCredentialsAndInvoke(getInstanceRequest, lightsailClient::getInstance);

            if(getInstanceResult == null || getInstanceResult.getInstance() == null) {
                return false;
            }

        } catch(NotFoundException e) {
            return false;
        }

        return true;

    }

    static CreateInstancesRequest translateToCreateRequest(final ResourceModel model) {

        return new CreateInstancesRequest()
                .withAvailabilityZone(model.getAvailabilityZone())
                .withBlueprintId(model.getBlueprintId())
                .withInstanceNames(Collections.singletonList(model.getInstanceName()))
                .withAvailabilityZone(model.getAvailabilityZone())
                .withUserData(model.getUserData())
                .withBundleId(model.getBundleId());
    }

    static ResourceModel createModelFromInstance(Instance instance) {
        ResourceModel model = new ResourceModel();
        model.setAvailabilityZone(instance.getLocation().getAvailabilityZone());
        model.setBlueprintId(instance.getBlueprintId());
        model.setBundleId(instance.getBundleId());
        model.setInstanceName(instance.getName());
        model.setUserData(null);
        return model;
    }
}
