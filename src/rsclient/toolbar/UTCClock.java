/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsclient.toolbar;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.TimerTask;
import java.util.Timer;
import java.time.ZoneId;
import javax.swing.JLabel;

/**
 *
 * @author Adam
 */
class UTCClock {
    
    public UTCClock(JLabel clockLabel) {
        final JLabel label = clockLabel;
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("kk:mm:ss VV").withZone(ZoneId.of("UTC"));
        Timer t;
        t = new Timer(true);
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {

                label.setText(LocalTime.now(ZoneId.of("UTC")).format(formatter));
            }
        }, 0, 1000);
    }
    
}
