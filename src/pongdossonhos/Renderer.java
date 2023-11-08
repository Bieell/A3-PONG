package pongdossonhos;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

public class Renderer {
    public static void main(String[] args) {
        
        // configurações do opengl (versão do opengl, e suas capacidades)
        GLProfile.initSingleton(); // inicialização do opengl;
        GLProfile profile = GLProfile.get(GLProfile.GL2); // perfil do opengl/versão
        
        GLCapabilities caps = new GLCapabilities(profile);
        GLWindow window = GLWindow.create(caps);
        
        Pong pong = new Pong();
        window.setResizable(false);
        
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
        
//        window.setFullscreen(true);
        window.setSize(600, 600);
        window.setVisible(true);
        
    }
}
