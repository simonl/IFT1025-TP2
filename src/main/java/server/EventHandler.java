package server;

@FunctionalInterface
public interface EventHandler {
    void handle(Channel channel, String cmd, String arg);
}
