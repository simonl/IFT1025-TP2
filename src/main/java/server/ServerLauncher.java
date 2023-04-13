package server;

/**
 * La classe ServerLauncher permet d'exécuter le server.
 */
public class ServerLauncher {
    public final static int PORT = 1337;

    /**
     * La méthode main permet de lancer le serveur.
     * Elle vérifie également qu'il n'y a pas d'erreur lors de l'exécution.
     * @param args
     */
    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(PORT);
            System.out.println("Server is running...");
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}