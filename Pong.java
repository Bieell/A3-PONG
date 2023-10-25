/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pongdossonhos;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import java.awt.Color;

/**
 *
 * @author gabri
 */
public class Pong implements GLEventListener{
    
    private float xMin, xMax, yMin, yMax, zMin, zMax;
    private final float MAX_SRU = 100;
    private final float MIN_SRU = -100;
    private float aspect;
    private GL2 gl;
    

    @Override
    public void init(GLAutoDrawable drawable) {
        definirLimiteSRU();
        
        
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        System.out.println("LOOP");
        gl = drawable.getGL().getGL2();
        gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        desenharFundo();
        desenhaCama();
        
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        //obtem o contexto grafico Opengl
        GL2 gl = drawable.getGL().getGL2();  
        
        //evita a divisão por zero
        if(height == 0) height = 1;
        //calcula a proporção da janela (aspect ratio) da nova janela
        float aspect = (float) width / height;
        
        //seta o viewport para abranger a janela inteira
        gl.glViewport(0, 0, width, height);
                
        //ativa a matriz de projeção
        gl.glMatrixMode(GL2.GL_PROJECTION);      
        gl.glLoadIdentity(); //lê a matriz identidade
        
        //Projeção ortogonal
        //true:   aspect >= 1 configura a altura de -1 para 1 : com largura maior
        //false:  aspect < 1 configura a largura de -1 para 1 : com altura maior
        if(width >= height)            
            gl.glOrtho(xMin * aspect, xMax * aspect, yMin, yMax, zMin, zMax);
        else        
            gl.glOrtho(xMin, xMax, yMin / aspect, yMax / aspect, zMin, zMax);
                
        //ativa a matriz de modelagem
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity(); //lê a matriz identidade
    }
    
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }
    
    private void definirLimiteSRU() {
        xMax = yMax = zMax = MAX_SRU;
        xMin = yMin = zMin = MIN_SRU;
    }
    
    private void desenharFundo() {
        desenhaQuadrilatero(Color.blue, xMin, xMin, yMin, yMin, true);
        
    }
    
    private void desenhaCama(){
        desenhaQuadrilatero(Color.red, -20, 20, -80, -85, true);
        desenhaQuadrilatero(Color.white, -20, 20, -75, -80, true);
        desenhaQuadrilatero(Color.red, -25, -20, -65, -90, true);
        desenhaQuadrilatero(Color.red, 20, 25, -75, -90, true);
        
    }
    
    private void desenhaQuadrilatero(Color color, float x1, float x2, float y1, float y2, boolean borda) {
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(convertColor(color.getRed()), convertColor(color.getGreen()), convertColor(color.getBlue()));
        gl.glVertex2f(x1, y1);
        gl.glVertex2f(x2, y1);
        gl.glVertex2f(x2, y2);
        gl.glVertex2f(x1, y2);
        gl.glEnd();
        if(borda) desenhaBorda(x1, x2, y1, y2);
    }
    
    private void desenhaBorda(float x1, float x2, float y1, float y2) {
        gl.glLineWidth(2); // Define a largura da linha da borda
        gl.glBegin(GL2.GL_LINE_LOOP); 
        gl.glColor3f(0f, 0f, 0f);
        gl.glVertex2f(x1, y1);
        gl.glVertex2f(x2, y1);
        gl.glVertex2f(x2, y2);
        gl.glVertex2f(x1, y2);
        gl.glEnd();
    }
    
    private float convertColor(int color) {
        return (float) color/255;
    }
}
