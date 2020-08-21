package vs.lightsail.cfn.instance;

public class Constants {
    public static final String CREATE_OPERATION = "CreateInstance";
    public static final String STATUS_CREATION_PENDING = "CreationPending";
    public static final String STATUS_DELETE_PENDING = "DeletePending";
    public static final int NUMBER_OF_STATUS_POLL_RETRIES = 120;
    public static final String INSTANCE_STATE_RUNNING = "running";
    public static final String CREATE_TIMED_OUT_MESSAGE = "Timed out waiting for Lightsail instance to create.";
    public static final int CALLBACK_DELAY_SECONDS = 1;
}
