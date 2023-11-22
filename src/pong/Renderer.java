package pong;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Renderer {
    public static void main(String[] args) {
        // configurações do opengl (versão do opengl, e suas capacidades)
        GLProfile.initSingleton(); // inicialização do opengl;
        GLProfile profile = GLProfile.get(GLProfile.GL2); // perfil do opengl/versão
        
        GLCapabilities caps = new GLCapabilities(profile);
        GLWindow window = GLWindow.create(caps);
        
        Pong pong = new Pong();
        window.addGLEventListener(pong);
        
        // animação da janela, fica em loop, executando a todo momento o display;
        FPSAnimator animator = new FPSAnimator(window, 60);
        animator.start(); 
        
        //encerrar a aplicacao adequadamente
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyNotify(WindowEvent e) {
                animator.stop();
                System.exit(0);
            }
        }); 
        
        window.setFullscreen(true);
        window.setSize(800, 600);
        window.setResizable(false);
        int[] positions = getScreenCenterPosition(window);
        window.setPosition(positions[0], positions[1]);
        window.setVisible(true);
    }
    
    private static int[] getScreenCenterPosition(GLWindow window) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        int x = (int) ((dimension.getWidth() - window.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - window.getHeight()) / 2);
        int[] positions = {x, y};

        return positions;
    }
}
