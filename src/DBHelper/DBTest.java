package DBHelper;


import java.util.ArrayList;

public class DBTest {
    public static void main(String[] args) {
        try {
            Reservation reservation = new Reservation();
            ArrayList<ArrayList<Object>> results = reservation.select(null, null, null, null, null);

            System.out.println("✅ Connected to embedded DB.");
            System.out.println("🔍 Reservation table contents:");

            for (ArrayList<Object> row : results) {
                System.out.println(row);
            }

            if (results.isEmpty()) {
                System.out.println("⚠️ No data found in Reservation table.");
            }

        } catch (Exception e) {
            System.out.println("❌ DB connection or query failed:");
            e.printStackTrace();
        }
    }
}
