package com.input;

import com.graphics.Pong;
import com.graphics.Renderer;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import java.util.ArrayList;

public class Keyboard implements KeyListener{
    
    private final Pong pong;
    private final float DISTANCIA_DE_MOVIMENTO = 10;
    private final ArrayList<Short> currentPressedKey;
    public Keyboard(Pong pong) {
        this.pong = pong;
        this.currentPressedKey = new ArrayList<>();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if(pong.posXMinCama > pong.xMin && Math.abs(pong.xMin) - Math.abs(pong.posXMinCama) > DISTANCIA_DE_MOVIMENTO*pong.aspect) {
                    pong.posXMinCama -= DISTANCIA_DE_MOVIMENTO*pong.aspect;
                    pong.posXMaxCama -= DISTANCIA_DE_MOVIMENTO*pong.aspect;
                } else if(Math.abs(pong.xMin) - Math.abs(pong.posXMinCama) > 0 && Math.abs(pong.xMin) - Math.abs(pong.posXMinCama) < DISTANCIA_DE_MOVIMENTO*pong.aspect) {
                    float diferencaEntreCamaElimite = Math.abs(pong.xMin) - Math.abs(pong.posXMinCama)- 2;
                    pong.posXMinCama -= diferencaEntreCamaElimite;                    
                    pong.posXMaxCama -= diferencaEntreCamaElimite;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if(pong.posXMaxCama < pong.xMax && pong.xMax - pong.posXMaxCama > DISTANCIA_DE_MOVIMENTO*pong.aspect) {
                    pong.posXMinCama += DISTANCIA_DE_MOVIMENTO*pong.aspect;
                    pong.posXMaxCama += DISTANCIA_DE_MOVIMENTO*pong.aspect;
                } else if(pong.xMax - pong.posXMaxCama > 0 && pong.xMax - pong.posXMaxCama < DISTANCIA_DE_MOVIMENTO*pong.aspect) {
                    float diferencaEntreCamaElimite = pong.xMax - pong.posXMaxCama - 2;
                    pong.posXMinCama += diferencaEntreCamaElimite;
                    pong.posXMaxCama += diferencaEntreCamaElimite;
                }
                break;
            case KeyEvent.VK_ALT:
                currentPressedKey.add(KeyEvent.VK_ALT);
                break;
            case KeyEvent.VK_F4:
                if(currentPressedKey.contains(KeyEvent.VK_ALT)) {
                    Renderer.animator.stop();
                    System.exit(0);
                }
                break;
            case KeyEvent.VK_R:
                pong.zerarPontos();
                pong.resetarVidas();
                pong.resetarVelocidade();
                pong.resetarMovimento();
                pong.resetarCama();
                break;
            case KeyEvent.VK_ESCAPE:
                System.out.println("ESC: " + e.getKeyCode());
                if(!currentPressedKey.contains(KeyEvent.VK_ESCAPE)) {
                    pong.pausarJogo();
                    currentPressedKey.add(KeyEvent.VK_ESCAPE);
                } else {
                    pong.despausarJogo();
                    currentPressedKey.remove(currentPressedKey.indexOf(KeyEvent.VK_ESCAPE));
                }
                break;
            case KeyEvent.VK_ENTER:
                if(pong.exibirMenu) {
                    pong.exibirMenu = false;
                }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(currentPressedKey.contains(e.getKeyCode())) {
            if(e.getKeyCode() != KeyEvent.VK_ESCAPE) {
                currentPressedKey.remove(currentPressedKey.indexOf(e.getKeyCode()));
            }
        }
    }
    
}
