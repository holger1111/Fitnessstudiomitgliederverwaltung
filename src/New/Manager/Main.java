package New.Manager;

import java.sql.Connection;
import java.util.Scanner;
import New.Service.*;

public class Main {
    public static void main(String[] args) {
        Connection connection = null;

        Scanner scanner = new Scanner(System.in);

        MitgliederService mitgliederService = new MitgliederService(connection, scanner);
        VerkaufService verkaufService = new VerkaufService(connection, scanner);
        KursService kursService = new KursService(connection, scanner);
        VertragService vertragService = new VertragService(connection, scanner);
        ÜbersichtService übersichtService = new ÜbersichtService(connection, scanner);
        AdminService adminService = new AdminService(connection, scanner);

        HauptmenüService hauptmenüService = new HauptmenüService(
            connection, scanner,
            mitgliederService,
            verkaufService,
            kursService,
            vertragService,
            übersichtService,
            adminService
        );

        hauptmenüService.start();
        scanner.close();
    }
}
