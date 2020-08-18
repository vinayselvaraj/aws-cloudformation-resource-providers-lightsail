package vs.lightsail.cfn.instance;

import com.amazonaws.services.lightsail.model.CreateInstancesRequest;

import java.util.Collections;

public class Translator {

    static CreateInstancesRequest translateToCreateRequest(final ResourceModel model) {

        return new CreateInstancesRequest()
                .withAvailabilityZone(model.getAvailabilityZone())
                .withBlueprintId(model.getBlueprintId())
                .withInstanceNames(Collections.singletonList(model.getInstanceName()))
                .withAvailabilityZone(model.getAvailabilityZone())
                .withBundleId(model.getBundleId());
    }
}
