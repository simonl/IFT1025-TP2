package server;

public interface Channel {
    <T> T read() throws Exception;

    <T> void write(T o) throws Exception;
}
