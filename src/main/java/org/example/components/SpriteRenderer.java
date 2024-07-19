package org.example.components;

import com.sun.security.jgss.GSSUtil;
import org.example.jade.Component;

public class SpriteRenderer extends Component {

    private boolean firstTime = false;

    @Override
    public void start() {
        System.out.println(this.getClass().getName() + " is starting...");
    }

    @Override
    public void update(double dt) {
        if (!firstTime) {
            System.out.println(this.getClass().getName() + " is updating.");
            this.firstTime = true;
        }
    }
}
