package com.graphics;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.gl2.GLUT;
import java.awt.Color;
import texture.Textura;

/**
 *
 * @author gabri
 */
public class Pong implements GLEventListener{
    
    private float xMin, xMax, yMin, yMax, zMin, zMax;
    private final float MAX_SRU = 100.0f;
    private final float MIN_SRU = -100.0f;
    private GL2 gl;
    private GLUT glut;
    private Textura textura;
    private final int totalTextura = 1;
    private final String background = "imagens/backgroundFHD.jpg";
    
    private int filtro = GL2.GL_NEAREST; ////GL_NEAREST ou GL_LINEAR
    private int wrap = GL2.GL_CLAMP;  //GL.GL_REPEAT ou GL.GL_CLAMP
    private int modo = GL2.GL_DECAL; ////GL.GL_MODULATE ou GL.GL_DECAL ou GL.GL_BLEND
    
    private final float DISTANCIA_Z_FUNDO = 20.0f;
    private float raio = 14f;
    private float numPontos = 60f;
    private float posicaoXBola = 0.0f;
    private float posicaoYBola = 0.0f;
    private float velocidadeDaBola = .2f; 
    
    private int toning = GL2.GL_SMOOTH;
    int i =0;
    
    

    @Override
    public void init(GLAutoDrawable drawable) {
        definirLimiteSRU();
        gl = drawable.getGL().getGL2();
        glut = new GLUT();
        gl.glEnable(GL.GL_DEPTH_TEST);
        textura = new Textura(totalTextura);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        System.out.println(i++);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        
        lithingScheme();
        turnLightOn();
        desenharFundo();
        desenhaCama();
        desenhaBola();
        
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity(); //lê a matriz identidade
        gl.glOrtho(xMin, xMax, yMin, yMax, zMin, zMax);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
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
        gl.glTexCoord2f(0, 0); gl.glVertex2f(-100, -100);
        gl.glTexCoord2f(1, 0); gl.glVertex2f(100, -100);   
        gl.glTexCoord2f(1, 1); gl.glVertex2f(100, 100);   
        gl.glTexCoord2f(0, 1); gl.glVertex2f(-100, 100);
        gl.glEnd();
        gl.glPopMatrix();
        
        textura.desabilitarTextura(gl, 0);
    }
    
    private void desenhaCama(){
        gl.glPushMatrix();
        desenhaQuadrilatero(Color.red, -25, -20, -65, -90, true);
        desenhaQuadrilatero(Color.red, 20, 25, -75, -90, true);
        desenhaQuadrilatero(Color.red, -20, 20, -80, -85, true);
        desenhaQuadrilatero(Color.white, -20, 20, -75, -80, true);
        gl.glPopMatrix();
    }
    
    private void desenhaBola() {
        gl.glPushMatrix();
        moverBola();
        
        gl.glColor3f(1f, 1f, 1f);
        glut.glutSolidSphere(14, 50, 50);
        gl.glPopMatrix();
        
    }
    
    private void desenhaQuadrilatero(Color color, float x1, float x2, float y1, float y2, boolean borda) {
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(convertColor(color.getRed()), convertColor(color.getGreen()), convertColor(color.getBlue()));
        gl.glVertex3f(x1, y1, DISTANCIA_Z_FUNDO);
        gl.glVertex3f(x2, y1, DISTANCIA_Z_FUNDO);
        gl.glVertex3f(x2, y2, DISTANCIA_Z_FUNDO);
        gl.glVertex3f(x1, y2, DISTANCIA_Z_FUNDO);
        gl.glEnd();
        if(borda) desenhaBordaQuadrilatero(x1, x2, y1, y2);
    }
    
    private void desenhaBordaQuadrilatero(float x1, float x2, float y1, float y2) {
        gl.glLineWidth(2); // Define a largura da linha da borda
        gl.glBegin(GL2.GL_LINE_LOOP);
        gl.glColor3f(0f, 0f, 0f);
        gl.glVertex3f(x1, y1, DISTANCIA_Z_FUNDO);
        gl.glVertex3f(x2, y1, DISTANCIA_Z_FUNDO);
        gl.glVertex3f(x2, y2, DISTANCIA_Z_FUNDO);
        gl.glVertex3f(x1, y2, DISTANCIA_Z_FUNDO);
        gl.glEnd();
    }
    
    private void moverBola() {
        posicaoXBola = velocidadeDaBola + posicaoXBola;
        posicaoYBola = velocidadeDaBola + posicaoYBola;
        gl.glTranslatef(posicaoXBola, posicaoYBola, DISTANCIA_Z_FUNDO);
    }
    
    public void lithingScheme(){
        float[] ambientLight = { 0.7f, 0.7f, 0.7f, 1f };  
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);  
		
        float difuseLight[] = {0.8f, 0.8f, 0.8f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, difuseLight, 0);
        
        float lightPosition[] = {-50.0f, 0.0f, 100.0f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);
    }

    
    public void turnLightOn() {
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);    
        gl.glShadeModel(toning);
    }

    public void turnLightOff() {
        gl.glDisable(GL2.GL_LIGHT0);
        gl.glDisable(GL2.GL_LIGHTING);
    }
    
    
    private float convertColor(int color) {
        return (float) color/255;
    }
}
