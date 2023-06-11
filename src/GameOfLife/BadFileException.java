package GameOfLife;

/**
 * Exception, hogyha a beolvasandó txt szintaktiai hibás. Különösbben fontos szerepe nincs.
 */
public class BadFileException extends Exception{
    String msg;
    public BadFileException(String msg) {
        this.msg = msg;
    }
}
