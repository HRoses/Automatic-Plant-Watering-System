import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
public class PlantTask extends TimerTask{
    private final Pin moistureSensor;
    private final Pin Button;
    private final Pin Pump;
    private final SSD1306 Oled;
    int counter = 0;
    public PlantTask(Pin Moist, SSD1306 Oled,Pin pump,Pin Button  , Timer timer) {
        this.moistureSensor = Moist;
        this.Button = Button;
        this.Pump = pump;
        this.Oled = Oled;
    }
    @Override
    public void run() {
        Oled.getCanvas().setTextsize(2);
        // Display On OLED:- Automatic Water Process Start
        if (counter ==0) {
            System.out.println("Automatic Water Process Start");
        Oled.getCanvas().write("Automatic\nWater\nProcess\nStart");
        Oled.display();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Oled.clear();
        }
        counter++;
        // value of the moisture sensor on Oled Display


        int moistureValue = (int) moistureSensor.getValue();
                    String moistureValueStr = String.valueOf(moistureValue);

        // Conditions to TURN ON PUMP AND TURN OFF PUMP.
        if (moistureValue > 660) {
            try {
                System.out.println("Soil is DRY, Water Needed. Pumping Water...");
                Pump.setValue(1);
                //Oled.getCanvas().drawString(0,0,"Pump ON"+" Moisture Value"+moistureValueStr);
                Oled.getCanvas().drawString(0,0,"Soil Dry \nPump ON ");
                Oled.display();
                Thread.sleep(1000);
            } catch (IOException e) {
                Oled.getCanvas().drawString(0,0,"ERROR\nPlease\nCheck");
                Oled.display();
                System.out.println("Error: Something wrong with Turning ON Pump");
            } catch (InterruptedException ignored) {}
        }

        else if (moistureValue<660) {
                try {
                    System.out.println("Soil Sufficiently Wet, No Water Needed.");
                    Pump.setValue(0);
                    Oled.getCanvas().drawString(0,0,"Soil Wet \nPump OFF");
                    Oled.display();
                } catch (IOException e) {
                    Oled.getCanvas().drawString(0,0,"ERROR\nPlease\nCheck");
                    Oled.display();
                    System.out.println("Error: Something wrong with Turning OFF Pump");
                }
        }
        // Stopping Procedure Using A BUTTON. PUMP OFF and OLED Display Clear.


        if (Button.getValue()==1) {
            Oled.clear(); // clear any previous display information
            System.out.println("Button Pressed: Automatic Water Process Stopped. ");
            Oled.getCanvas().drawString(0,0,"Automatic\nWater\nProcess\nStopped");
            Oled.display();
            try {
                Pump.setValue(0);
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                Oled.getCanvas().drawString(0,0,"ERROR\nPlease\nCheck");
                Oled.display();
                System.out.println("Error Stopping Pump");
            }
            Oled.clear(); // clear the current display WATERING STOPPED after 2 seconds of display on OLED.
            System.exit(0);

        }

    }
} // class ends
