import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;

interface IntConsumer {
    void accept(int value);
}

public class TestApplication {

    public static void main(String[] args) {
        TalkingClock talkingClock = new TalkingClock(1000, true);
        talkingClock.start();



        // keep program running
        JOptionPane.showMessageDialog(null, "Quit Program?");
        System.exit(0);
    }

}

class TalkingClock {
    private final int interval;
    private final boolean beep;

    public TalkingClock(int interval, boolean beep) {
        this.interval = interval;
        this.beep = beep;
    }

    public void start() {
        var listener = new TimePrinter(){
            @Override
            public void actionPerformed(ActionEvent e) {
                super.actionPerformed(e);
            }
        };
        var timer = new Timer(interval, listener);
        timer.start();
    }

    private class TimePrinter implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Current Time is " + Instant.ofEpochMilli(e.getWhen()));
            if (TalkingClock.this.beep) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
}