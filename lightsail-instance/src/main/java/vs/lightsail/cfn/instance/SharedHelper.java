package vs.lightsail.cfn.instance;

import com.amazonaws.services.lightsail.AmazonLightsail;
import com.amazonaws.services.lightsail.model.GetInstanceRequest;
import com.amazonaws.services.lightsail.model.GetInstanceResult;
import com.amazonaws.services.lightsail.model.NotFoundException;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;

public class SharedHelper {

    static boolean doesInstanceExist(
            final ResourceModel model,
            final AmazonWebServicesClientProxy proxy,
            final Logger logger,
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
        } catch(Exception ex) {
            logger.log("Caught Exceptionn: " + ex);
            return false;

        }

        return true;

    }
}
