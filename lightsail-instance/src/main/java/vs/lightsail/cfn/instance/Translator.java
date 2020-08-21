package vs.lightsail.cfn.instance;

import com.amazonaws.services.lightsail.model.CreateInstancesRequest;
import com.amazonaws.services.lightsail.model.Instance;

import java.util.Collections;

public class Translator {

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
