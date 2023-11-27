package com.input;

import com.graphics.Pong;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

public class Keyboard implements KeyListener{
    
    private final Pong pong;
    public Keyboard(Pong pong) {
        this.pong = pong;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if(pong.posXMinCama > pong.xMin) {
                    pong.posXMinCama -= 10;
                    pong.posXMaxCama -= 10;
                    
                }
                break;
            case KeyEvent.VK_RIGHT:
                if(pong.posXMaxCama < pong.xMax) {
                    pong.posXMinCama += 10;
                    pong.posXMaxCama +=10;
                }
                break;
                
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
}
