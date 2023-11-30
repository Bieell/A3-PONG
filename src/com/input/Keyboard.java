package com.input;

import com.graphics.Pong;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

public class Keyboard implements KeyListener{
    
    private final Pong pong;
    private final float DISTANCIA_DE_MOVIMENTO = 10;
    public Keyboard(Pong pong) {
        this.pong = pong;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if(pong.posXMinCama > pong.xMin && Math.abs(pong.xMin) - Math.abs(pong.posXMinCama) > DISTANCIA_DE_MOVIMENTO) {
                    pong.posXMinCama -= DISTANCIA_DE_MOVIMENTO;
                    pong.posXMaxCama -= DISTANCIA_DE_MOVIMENTO;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if(pong.posXMaxCama < pong.xMax && pong.xMax - pong.posXMaxCama > DISTANCIA_DE_MOVIMENTO) {
                    pong.posXMinCama += DISTANCIA_DE_MOVIMENTO;
                    pong.posXMaxCama += DISTANCIA_DE_MOVIMENTO;
                }
                break;
                
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
}
