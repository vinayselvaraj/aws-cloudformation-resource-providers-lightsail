package vs.lightsail.cfn.instance;

import software.amazon.cloudformation.proxy.StdCallbackContext;

@lombok.Getter
@lombok.Setter
@lombok.ToString
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor@lombok.EqualsAndHashCode(callSuper = true)
public class CallbackContext extends StdCallbackContext {
    private Integer stabilizationRetriesRemaining;
    private String status;
}
