package com.graphics;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.gl2.GLUT;
import java.awt.Color;
import com.texture.Textura;

/**
 *
 * @author gabri
 */
public class Pong implements GLEventListener{
    
    public float xMin, xMax, yMin, yMax, zMin, zMax;
    private float aspect;
    private float screenWidth;
    private float screenHeight;
    public final float MAX_SRU = 100.0f;
    public final float MIN_SRU = -100.0f;
    private GL2 gl;
    private GLUT glut;
    private Textura textura;
    private final int totalTextura = 1;
    private final String background = "imagens/backgroundHD.jpg";
    
    private final int filtro = GL2.GL_NEAREST; ////GL_NEAREST ou GL_LINEAR
    private final int wrap = GL2.GL_CLAMP;  //GL.GL_REPEAT ou GL.GL_CLAMP
    private final int modo = GL2.GL_DECAL; ////GL.GL_MODULATE ou GL.GL_DECAL ou GL.GL_BLEND
    
    private final float DISTANCIA_Z_FUNDO = 20.0f;
    private final float raio = 16f;
    private final float posXBolaInit = -40.0f;
    private final float posYBolaInit = 80.0f;
    private final float velocidadeInicial = 3f;
    private float posicaoXBola;
    private float posicaoYBola;
    private float velocidadeXDaBola; 
    private float velocidadeYDaBola; 
    public final float larguraDaCama = 65f;
    public final float alturaCama = 14f;
    public float posXMinCama = -25f;
    private final float posYMinCama = -90f;
    public float posXMaxCama = posXMinCama + larguraDaCama;
    private final float posYMaxCama = posYMinCama + alturaCama;
    private final float diferencaAlturaCama = (Math.abs(posYMinCama) - Math.abs(posYMaxCama))/2;
    
    private final int toning = GL2.GL_SMOOTH;
    int i =0;

    @Override
    public void init(GLAutoDrawable drawable) {
        posicaoXBola = posXBolaInit;
        posicaoYBola = posYBolaInit;
        velocidadeXDaBola = velocidadeInicial;
        velocidadeYDaBola = velocidadeInicial;
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
        
        configurarIluminacao();
        ligarLuz();
        desenharFundo();
        desenhaBola();
        desenhaCama();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        if(height == 0) height = 1;
        screenWidth = width;
        screenHeight = height;
        aspect = (float) width / height;
        
        definirLimiteSRU();
        
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);      
        gl.glLoadIdentity(); //lê a matriz identidade
        gl.glOrtho(xMin, xMax, yMin, yMax, zMin, zMax);
        
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
    
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }
    
    private void definirLimiteSRU() {
        if(screenWidth >= screenHeight)  {
            xMin = MIN_SRU * aspect;
            xMax = MAX_SRU * aspect;
            yMin = zMin = MIN_SRU;
            yMax = zMax = MAX_SRU;
        } else {
            yMax = MAX_SRU/aspect;
            yMin = MIN_SRU/aspect;
            xMax = zMax = MAX_SRU;
            xMin = zMin = MIN_SRU;
        }
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
        gl.glTexCoord2f(0, 0); gl.glVertex2f(xMin, yMin);
        gl.glTexCoord2f(1, 0); gl.glVertex2f(xMax, yMin);   
        gl.glTexCoord2f(1, 1); gl.glVertex2f(xMax, yMax);   
        gl.glTexCoord2f(0, 1); gl.glVertex2f(xMin, yMax);
        gl.glEnd();
        gl.glPopMatrix();
        
        textura.desabilitarTextura(gl, 0);
    }
    
    private void desenhaCama(){
        gl.glPushMatrix();
//        desenhaQuadrilatero(Color.red, posXMinCama, posXMinCama + 5, posYMaxCama + 10, posYMinCama, true);
//        desenhaQuadrilatero(Color.red, posXMaxCama - 5, posXMaxCama, posYMaxCama, posYMinCama, true);
        desenhaQuadrilatero(Color.red, posXMinCama, posXMaxCama, posYMaxCama - diferencaAlturaCama, posYMinCama, true);
        desenhaQuadrilatero(Color.white, posXMinCama, posXMaxCama, posYMaxCama, posYMinCama + diferencaAlturaCama , true);
        gl.glPopMatrix();
    }
    
    private void desenhaBola() {
        gl.glPushMatrix();
        moverBola();
        gl.glColor3f(1f, 1f, 1f);
        glut.glutSolidSphere(raio, 50, 50);
        gl.glPopMatrix();
        desenhaBordaBola();
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
        posicaoXBola += velocidadeXDaBola;
        posicaoYBola += velocidadeYDaBola;
        gl.glTranslatef(posicaoXBola, posicaoYBola, DISTANCIA_Z_FUNDO);
        
        colisaoComMargens();
        colisaoComCama();
    }
    
    private void colisaoComMargens() {
        if(posicaoXBola + raio >= xMax || posicaoXBola - raio <= xMin){
            if(Math.abs(posicaoXBola - raio) - Math.abs(xMin) > Math.abs(velocidadeXDaBola)) {
                posicaoXBola += Math.abs(posicaoXBola - raio) - Math.abs(xMin);
            }
            if(posicaoXBola + raio - xMax > Math.abs(velocidadeXDaBola)) {
                posicaoXBola -= (posicaoXBola + raio - xMax);
            }
            velocidadeXDaBola = -velocidadeXDaBola;
        }
        if(posicaoYBola + raio >= yMax) velocidadeYDaBola = -velocidadeYDaBola;        
        if(posicaoYBola <= yMin) resetarMovimento();
    }
    
    private void colisaoComCama() {
        if(posicaoYBola - raio <= posYMaxCama && posicaoYBola - raio >= posYMaxCama - Math.abs(velocidadeYDaBola)) {
            if(posicaoXBola - (raio/2) <= posXMaxCama && posicaoXBola + (raio/2) >= posXMinCama){
                velocidadeYDaBola = -velocidadeYDaBola;
            } else if(posicaoXBola - raio <= posXMaxCama && posicaoXBola + raio >= posXMinCama) {
                if(velocidadeXDaBola > 0 && posicaoXBola + raio >= posXMaxCama) {
                    velocidadeYDaBola = -velocidadeYDaBola;
                } else if(velocidadeXDaBola < 0 && posicaoXBola - raio <= posXMinCama){
                    velocidadeYDaBola = -velocidadeYDaBola;
                } else {
                    velocidadeYDaBola = -velocidadeYDaBola;
                    velocidadeXDaBola = -velocidadeXDaBola;
                }
            }
        } else if(posicaoYBola - raio < posYMaxCama - Math.abs(velocidadeYDaBola)) {
            if(posicaoXBola - raio <= posXMaxCama && posicaoXBola - raio >= posXMaxCama - Math.abs(velocidadeYDaBola) && posicaoYBola - (raio/3) >= posYMaxCama){
                velocidadeYDaBola = -velocidadeYDaBola;
                velocidadeXDaBola = -velocidadeXDaBola;
            }else if(posicaoXBola + raio >= posXMinCama && posicaoXBola + raio <= posXMinCama + Math.abs(velocidadeYDaBola) && posicaoYBola - (raio/3) >= posYMaxCama) {
                velocidadeYDaBola = -velocidadeYDaBola;
                velocidadeXDaBola = -velocidadeXDaBola;
            } else if(posicaoXBola - raio <= posXMaxCama && posicaoXBola - raio >= posXMaxCama - Math.abs(velocidadeYDaBola)){
                velocidadeXDaBola = -velocidadeXDaBola;
            } else if(posicaoXBola + raio >= posXMinCama && posicaoXBola + raio <= posXMinCama + Math.abs(velocidadeYDaBola)) {
                velocidadeXDaBola = -velocidadeXDaBola;
            }
        }
    }
    
    private void resetarMovimento() {
        posicaoXBola = posXBolaInit;
        posicaoYBola = posYBolaInit;
        velocidadeXDaBola = velocidadeInicial;
        velocidadeYDaBola = velocidadeInicial;
    }
    public void configurarIluminacao(){
        float[] ambientLight = { 0.7f, 0.7f, 0.7f, 1f };  
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);  
		
        float difuseLight[] = {0.8f, 0.8f, 0.8f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, difuseLight, 0);
        
        float lightPosition[] = {-50.0f, 0.0f, 100.0f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);
    }
    
    public void ligarLuz() {
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);    
        gl.glShadeModel(toning);
    }

    public void desligarLuz() {
        gl.glDisable(GL2.GL_LIGHT0);
        gl.glDisable(GL2.GL_LIGHTING);
    }
    
    
    private float convertColor(int color) {
        return (float) color/255;
    }
    
    private void desenhaBordaBola() {
        int numSegments = 100;
        float theta = (float) (2.0 * Math.PI / numSegments);
        
        gl.glColor3f(0.0f, 0.0f, 0.0f);
        gl.glLineWidth(3);// Cor da borda (preto)
        gl.glBegin(GL2.GL_LINE_LOOP);
        for (int i = 0; i <= numSegments; i++) {
            float x = posicaoXBola + (float) (raio * Math.cos(i * theta));
            float y = posicaoYBola + (float) (raio * Math.sin(i * theta));
            gl.glVertex3f(x, y, DISTANCIA_Z_FUNDO);
        }
        gl.glEnd();
        
    }
}
