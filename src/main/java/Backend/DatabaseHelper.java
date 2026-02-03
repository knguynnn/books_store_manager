
package Backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseHelper {

    // =========================
    // Thông tin cấu hình Database
    // Được đọc từ file db.properties
    // =========================
    private static String DB_URL;
    private static String USER;
    private static String PASS;

    /*
     * Khối static:
     * - Chạy 1 lần khi class được load
     * - Đọc thông tin database từ db.properties
     * - Giúp code chạy được trên cả Windows & Linux
     * - Không hard-code user/password
     */
    static {
        try (
                // Lấy file db.properties trong src/main/resources
                InputStream is = DatabaseHelper.class
                        .getClassLoader()
                        .getResourceAsStream("db.properties")
        ) {
            Properties props = new Properties();
            props.load(is);

            DB_URL = props.getProperty("db.url");
            USER   = props.getProperty("db.user");
            PASS   = props.getProperty("db.pass");

        } catch (Exception e) {
            System.out.println("❌ Không đọc được file db.properties");
            e.printStackTrace();
        }
    }

    /*
     * Hàm tạo kết nối Database
     * - Được gọi ở các lớp DAO
     * - Trả về Connection hoặc null nếu lỗi
     */
    public static Connection getConnection() {
        try {
            // Load JDBC Driver (an toàn cho mọi phiên bản Java)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Tạo và trả về kết nối
            return DriverManager.getConnection(DB_URL, USER, PASS);

        } catch (Exception e) {
            System.out.println("❌ Lỗi kết nối Database!");
            e.printStackTrace();
            return null;
        }
    }
}
