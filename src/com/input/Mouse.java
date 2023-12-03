
package com.input;

import com.graphics.Pong;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

public class Mouse implements MouseListener{
    
    private Pong pong;
    private final float margem = 10f;
    
    public Mouse (Pong pong) {
        this.pong = pong;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int botao = e.getButton();

        if(botao == MouseEvent.BUTTON1){
            System.out.println("Clique ESQ");
            System.out.println("X: " + e.getX() + " Y: " + e.getY());
            
           float xMouse =  ( (2 * pong.xMax * e.getX()) / pong.screenWidth) - pong.xMax;
           
           if(xMouse + (pong.larguraDaCama/2) <= pong.xMax && xMouse - (pong.larguraDaCama/2) >= pong.xMin) {
               pong.posXMinCama = xMouse - (pong.larguraDaCama/2);
               pong.posXMaxCama = pong.posXMinCama + pong.larguraDaCama;
           } else if(xMouse + (pong.larguraDaCama/2) > pong.xMax) {
               pong.posXMinCama = pong.xMax - pong.larguraDaCama - margem;
               pong.posXMaxCama = pong.xMax - margem;
           } else if(xMouse - (pong.larguraDaCama/2) < pong.xMin) {
               pong.posXMinCama = pong.xMin + margem;
               pong.posXMaxCama = pong.xMin + pong.larguraDaCama + margem;
           }
           
            
        }
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseEvent e) {
    }
    
}
