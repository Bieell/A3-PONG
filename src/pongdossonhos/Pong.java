/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pongdossonhos;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import java.awt.Color;
import textura.Textura;

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
    private Textura textura;
    private int totalTextura = 1;
    private final String background = "imagens/background_pong4k.jpg";
    
    public int filtro = GL2.GL_LINEAR; ////GL_NEAREST ou GL_LINEAR
    public int wrap = GL2.GL_CLAMP;  //GL.GL_REPEAT ou GL.GL_CLAMP
    public int modo = GL2.GL_DECAL; ////GL.GL_MODULATE ou GL.GL_DECAL ou GL.GL_BLEND
    private final int limite = 1;
    
    

    @Override
    public void init(GLAutoDrawable drawable) {
        definirLimiteSRU();
        textura = new Textura(totalTextura);
        gl = drawable.getGL().getGL2();
        gl.glEnable(GL.GL_DEPTH_TEST);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        System.out.println("LOOP");
        
//        gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        
        desenharFundo();
        desenhaCama();
        
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        //obtem o contexto grafico Opengl
        gl = drawable.getGL().getGL2();
        //ativa a matriz de projeção
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity(); //lê a matriz identidade
        //projeção ortogonal (xMin, xMax, yMin, yMax, zMin, zMax)
        gl.glOrtho(-100, 100, -100, 100, -100, 100);
        //ativa a matriz de modelagem
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        System.out.println("Reshape: " + width + ", " + height);
    }
    
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }
    
    private void definirLimiteSRU() {
        xMax = yMax = zMax = MAX_SRU;
        xMin = yMin = zMin = MIN_SRU;
    }
    
    private void desenharFundo() {
        //não é geração de textura automática
        textura.setAutomatica(false);
        
        //configura os filtros
        textura.setFiltro(filtro);
        textura.setModo(modo);
        textura.setWrap(wrap);  
                
        //cria a textura indicando o local da imagem e o índice
        textura.gerarTextura(gl, background, 0);
        
        gl.glPushMatrix();
        gl.glBegin(GL2.GL_QUADS);   
        gl.glTexCoord3f(0, 0, 0); gl.glVertex3f(-100, -100, -10);
        gl.glTexCoord3f(1, 0, 0); gl.glVertex3f(100, -100, -10);   
        gl.glTexCoord3f(1, 1, 0); gl.glVertex3f(100, 100, -10);   
        gl.glTexCoord3f(0, 1, 0); gl.glVertex3f(-100, 100, -10);
        gl.glEnd();
        gl.glPopMatrix();
        
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
