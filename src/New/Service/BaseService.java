package New.Service;

import java.sql.Connection;
import java.util.Scanner;

public abstract class BaseService {
    protected Connection connection;
    protected Scanner scanner;

    public BaseService(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }
}
