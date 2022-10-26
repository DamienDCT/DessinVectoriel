import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.io.*;

public class Paint{
	Fenetre fenetre;	// Attribut pour stocker notre fenêtre d'application
	int w,h; // largeur et hauteur de cette fenêtre

	public Paint(int w, int h){
		this.w = w;		// Initialisation des variables 
		this.h = h;
		this.fenetre = new Fenetre(w,h);	// On créé notre nouvelle fenêtre 
	}

	public void run(){ 
		// Boucle principal de notre application
		while(true){
            try{
				// On met en veille le processus pendant 16ms pour avoir un rendu en 60 images par seconde
                Thread.sleep(16);
            }catch(InterruptedException e){
				// On affiche l'erreur au cas où la veille se passe mal
				e.printStackTrace();
				break;
			}
			// On réaffiche la fenêtre à chaque frame
			this.fenetre.repaint();
		}
	}

	public static void main(String[] argv){ 
		// On créé notre application de taille 1920x1080
		Paint application = new Paint(1920,1080);
		// On lance notre application
        application.run();
	}
}
