package se233.demo4.game;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GameExceptionHandler {
    // ใช้ Logger เดียวกันกับระบบหลัก
    private static final Logger logger = GameLogger.getLogger();

    // เมธอดสำหรับจัดการข้อผิดพลาด พร้อมทั้งบันทึกไปยัง Logger
    public static void handleException(String message, Exception e) {
        // บันทึกข้อผิดพลาดด้วย Logger
        logger.log(Level.SEVERE, message, e);


        System.err.println(message + ": " + e.getMessage());

        }
}
