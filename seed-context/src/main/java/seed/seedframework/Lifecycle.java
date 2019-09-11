package seed.seedframework;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface Lifecycle {

    void start();

    void stop();

    boolean isRunning();

}