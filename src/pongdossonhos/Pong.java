package pongdossonhos;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.gl2.GLUT;
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
    private GL2 gl;
    private GLUT glut;
    private Textura textura;
    private final int totalTextura = 1;
    private final String background = "imagens/background4k.jpg";
    
    private int filtro = GL2.GL_LINEAR; ////GL_NEAREST ou GL_LINEAR
    private int wrap = GL2.GL_CLAMP;  //GL.GL_REPEAT ou GL.GL_CLAMP
    private int modo = GL2.GL_DECAL; ////GL.GL_MODULATE ou GL.GL_DECAL ou GL.GL_BLEND
    
    private final float Z_FROM_BACKGROUNG = 20; 
    
    

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
        gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        
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
//        gl.glTranslatef(-20, 0, 0);
        desenhaQuadrilatero(Color.red, -25, -20, -65, -90, true);
        desenhaQuadrilatero(Color.red, 20, 25, -75, -90, true);
        desenhaQuadrilatero(Color.red, -20, 20, -80, -85, true);
        desenhaQuadrilatero(Color.white, -20, 20, -75, -80, true);
    }
    
    private void desenhaBola() {
        gl.glPushMatrix();
        gl.glColor3f(0f, 1f, 0f);
        gl.glTranslatef(0, 0, Z_FROM_BACKGROUNG);
        glut.glutSolidSphere(14, 40, 40);
        gl.glPopMatrix();
    }
    
    private void desenhaQuadrilatero(Color color, float x1, float x2, float y1, float y2, boolean borda) {
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(convertColor(color.getRed()), convertColor(color.getGreen()), convertColor(color.getBlue()));
        gl.glVertex3f(x1, y1, Z_FROM_BACKGROUNG);
        gl.glVertex3f(x2, y1, Z_FROM_BACKGROUNG);
        gl.glVertex3f(x2, y2, Z_FROM_BACKGROUNG);
        gl.glVertex3f(x1, y2, Z_FROM_BACKGROUNG);
        gl.glEnd();
        if(borda) desenhaBordaQuadrilatero(x1, x2, y1, y2);
    }
    
    private void desenhaBordaQuadrilatero(float x1, float x2, float y1, float y2) {
        gl.glLineWidth(2); // Define a largura da linha da borda
        gl.glBegin(GL2.GL_LINE_LOOP);
        gl.glColor3f(0f, 0f, 0f);
        gl.glVertex3f(x1, y1, Z_FROM_BACKGROUNG);
        gl.glVertex3f(x2, y1, Z_FROM_BACKGROUNG);
        gl.glVertex3f(x2, y2, Z_FROM_BACKGROUNG);
        gl.glVertex3f(x1, y2, Z_FROM_BACKGROUNG);
        gl.glEnd();
    }
    
    private float convertColor(int color) {
        return (float) color/255;
    }
}
