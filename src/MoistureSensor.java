import org.firmata4j.I2CDevice;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.ssd1306.SSD1306;

import java.io.IOException;
import java.util.Timer;
public class MoistureSensor {

    static final int A1 = 15; // Moisture Sensor
    static final int D6 = 6; // Button
    static final int D7 = 7; // Water Pump
    static final byte I2CO = 0x3C; // Arduino Oled

    public static void main(String[] args) throws IOException, InterruptedException {

        // 1) Start board
        var myUSBPort = "COM3";
        var device = new FirmataDevice(myUSBPort);
        device.start();
        device.ensureInitializationIsDone();

        // 2a) Object for moisture sensor
        var myMoistureSensor = device.getPin(A1);

        // 2b) Object for button
        var myButton = device.getPin(D6);
        myButton.setMode(Pin.Mode.INPUT);

        // 2c) Object for pump
        var myWaterPump = device.getPin(D7);
        myWaterPump.setMode(Pin.Mode.OUTPUT);

        // 2d) Object for OLED
        I2CDevice i2cObject = device.getI2CDevice((byte) 0x3c);
        SSD1306 myOled = new SSD1306(i2cObject, SSD1306.Size.SSD1306_128_64);
        myOled.init();

        // 3) Timed Task
        var task = new PlantTask(myMoistureSensor,myOled,myWaterPump, myButton, new Timer());
        new Timer().schedule(task,0,1000);




    }// main
}// class