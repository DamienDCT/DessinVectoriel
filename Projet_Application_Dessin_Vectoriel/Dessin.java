import javax.swing.*;
import java.util.Stack;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*; 
import java.io.*;

class Dessin extends JPanel{
	// Attribut pour la fenêtre où est stockée le JPanel
	private Fenetre fenetre;

	// Attributs pour les couleurs de dessin
 	Color colorStroke;
 	Color colorBcg;
 	
	// Attribut pour l'épaisseur des formes
 	float stroke;

	// Attributs pour les boutons de sélections
	private JButton topleft;
	private JButton topright;
	private JButton bottomleft;
	private JButton bottomright;

	// Attribut pour le mode de dessin actuel
	private ModeDessin mode;

	// Attributs pour gérer les coordonnées de la souris
	int mouseX, mouseY;
	int mouseXend, mouseYend;

	// Attribut pour savoir si on est en train de drag notre souris
	private boolean isDragging;

	// Attribut pour stocker la liste de formes à dessiner
	public ArrayList<Forme> formes;

	public Stack<Forme> undo;

	Forme currentForme;		// Attribut pour stocker la forme en cours de dessin
	Forme selectedForme;	// Attribut pour stocker la forme sélectionnée
	Forme copiedForme;		// Attribut pour stocker la forme copiée

	// Attributs pour gérer les coordonnées d'un des boutons qu'on utilise pour redimensionner une forme
	int changeSizeX, changeSizeY;

 	Dessin(Color colorStroke, Color colorBcg, Fenetre f){
		// On initialise les variables 
 	 	this.colorStroke = colorStroke;
 	 	this.colorBcg = colorBcg;
		this.fenetre = f;
		this.currentForme = null;
		this.selectedForme = null;
		this.copiedForme = null;
		this.mode = ModeDessin.NOTHING;
		this.isDragging = false;
		this.formes = new ArrayList<Forme>();
		this.undo = new Stack<Forme>();
		this.stroke = 1.0f;		// Stroke vaut 1.0f pour avoir une épaisseur de 1 pixel par défaut

		// On créé les boutons de sélections
		topleft = new JButton();
		topright = new JButton();
		bottomleft = new JButton();
		bottomright = new JButton();

		// On initialise les variables pour les coordonnées du redimensionnement des formes
		changeSizeX = 0;
		changeSizeY = 0;

		// Méthode pour appliquer les événements aux quatre boutons de redimensionnement
		changeSize();

		// On rend invisible les boutons
		topleft.setVisible(false);
		topright.setVisible(false);
		bottomleft.setVisible(false);
		bottomright.setVisible(false);

		// Classe anonyme pour le drag & drop de la souris sur le JPanel
		this.addMouseMotionListener(new MouseMotionAdapter(){
			@Override
			// Si on est en train de drag la souris
			public void mouseDragged(MouseEvent e){
				// On stock les coordonnées actuelles
				mouseXend = e.getX();
				mouseYend = e.getY();
				// Si on est en train de dessiner une forme et qu'on a aucune forme sélectionnée
				if(currentForme != null && selectedForme == null)
				{
					// On modifie la taille de la forme en train d'être dessinée
					currentForme.setEndpoint(mouseX, mouseY, mouseXend, mouseYend);
				}
				// Si on ne dessine pas de forme mais qu'une forme est sélectionnée, et qu'on drag notre souris
				if(currentForme == null && selectedForme != null){
					// On déplace notre forme 
					selectedForme.move(mouseXend, mouseYend);
					// On affiche les boutons de sélection sur notre forme
					modeSelection(selectedForme);
				}
				afficheCoords(e);
			}

			@Override
			public void mouseMoved(MouseEvent e){
				afficheCoords(e);
			}
		});
		// Classe anonyme pour gérer le clic et le relâchement du clic de souris
		this.addMouseListener(new MouseAdapter(){
			@Override
			// Si on appuie sur la souris
			public void mousePressed(MouseEvent e){
				afficheCoords(e);
				// On met le booléen isDragging à true
				isDragging = true;
				// On récupère les coordonnées du premier clic
				mouseX = e.getX();
				mouseY = e.getY();
				// On créé une forme selon le mode sélectionné
				if(modeEquals(ModeDessin.RECTANGLE)){
					currentForme = new Rectangle(getColorStroke(), getColorBcg(),-1, -1, -1, -1, stroke);
				} else if(modeEquals(ModeDessin.ELLIPSE)){
					currentForme = new Ellipse(getColorStroke(), getColorBcg(),-1, -1, -1, -1, stroke);
				} else if(modeEquals(ModeDessin.TRIANGLE)){
					currentForme = new Triangle(getColorStroke(), getColorBcg(),-1, -1, -1, -1, stroke);
				} else if(modeEquals(ModeDessin.TRAIT)){
					currentForme = new Trait(getColorStroke(),-1, -1, -1, -1, stroke);
				// Si on est sur aucune forme et qu'on est en mode sélection
				} else if(modeEquals(ModeDessin.SELECT)){
					selectedForme = null;
					// On parcourt nos formes, et on regarde la forme 
					// la plus loin dans notre liste où notre clic de souris est dedans 
					// (signifiant la forme la plus récente)
					for(Forme f : formes){
						if(f.isSelected(mouseX, mouseY)){
							selectedForme = f;
						}
					}
					// On met les boutons de sélection sur notre forme
					modeSelection(selectedForme);
				// Si on est sur le mode de la gomme
				} else if(modeEquals(ModeDessin.ERASE)){
					selectedForme = null;
					// On regarde la forme sélectionnée par rapport à notre clic de souris
					for(Forme f : formes){
						if(f.isSelected(mouseX, mouseY)){
							selectedForme = f;
						}
					}
					// Si on en a sélectionné une, on la supprime de notre liste
					if(selectedForme != null){
						formes.remove(selectedForme);
						selectedForme = null;
					}
				}
			}
			@Override
			// Si on relâche notre clic de souris
			public void mouseReleased(MouseEvent e){
				afficheCoords(e);
				// Si on est en train de dessiner une forme et qu'on a pas déplacé notre souris une seule fois
				if(currentForme != null && currentForme.getterX() == -1){
					// On n'ajoute pas la forme à la liste
					currentForme = null;
					isDragging = false;
					return;
				}		
				// Si on est en train de drag la souris
				if(isDragging){
					// Si on est pas sur le mode "NOTHING"
					if(!modeEquals(ModeDessin.NOTHING)){
						// Et qu'on est en train de dessiner une forme
						if(currentForme != null){
							// On ajoute la forme à notre liste
							ajoutForme(currentForme);
						}
					}
					// On réinitialise les variables pour un prochain clic souris.
					currentForme = null;
					isDragging = false;
					mouseX = 0;
					mouseY = 0;
					mouseXend = 0; 
					mouseYend = 0;
				}
			}
		});
 	}
 	 
	// Méthode paintComponent qui prend en paramètre l'objet Graphique de notre JPanel
	// Cette méthode s'appelle automatiquement à chaque frame
	// Elle dessine toutes les formes, y compris celle en cours de dessin
  	public void paintComponent(Graphics g){
		// On remet le fond blanc sur le JPanel
    	g.setColor(Color.WHITE);
    	g.fillRect(0, 0, this.getWidth(),this.getHeight());

		// Pour chaque forme dans notre liste
		for(Forme f : this.formes){
			// On appelle la méthode paint de la forme
			f.paint(g);
		}
		// Si on est en train de dessiner une forme, on la dessine par dessus toutes les autres formes
		if(currentForme != null){
			currentForme.paint(g);
		}
  	}  

	// Méthode qui permet de changer la couleur de contour
	public void setColorStroke(Color c){
		this.colorStroke = c;
	}
	
	// Méthode qui permet de changer la couleur de fond
	public void setColorBcg(Color c){
		this.colorBcg = c;
	}

	// Méthode qui permet de récupérer la couleur de contour
	public Color getColorStroke(){
		return this.colorStroke;
	}

	// Méthode qui permet de récupérer la couleur de fond	
	public Color getColorBcg(){
		return this.colorBcg;
	}

	// Méthode qui permet de modifier le mode actuel
	public void setMode(ModeDessin m){
		this.mode = m;
	}

	// Méthode qui permet de récupérer le mode actuel
	public ModeDessin getMode(){
		return this.mode;
	}

	// Méthode qui permet de vérifier le mode actuel
	public boolean modeEquals(ModeDessin m){
		return mode == m;
	}
	
	// Méthode qui permet de modifier notre contour
	public void setStroke(float stroke){
		this.stroke = stroke;
	}

	// Méthode pour ajouter une forme dans notre liste de formes
	public void ajoutForme(Forme F){
		this.formes.add(F);
	}

	// Méthode qui permet de cacher les quatre boutons de sélections
	public void hiddenButtons(){
		// On rend les quatre boutons invisible, et on les remove du JPanel
		topleft.setVisible(false);
		topright.setVisible(false);
		bottomleft.setVisible(false);
		bottomright.setVisible(false);
		remove(topleft);
		remove(topright);
		remove(bottomleft);
		remove(bottomright);
	}

	// Méthode qui permet de placer les boutons de sélections sur la forme sélectionnée
	public void modeSelection(Forme f){
		// Si on a bien sélectionné une forme
		if(f != null){
			// Si la forme est un trait, on met que deux boutons 
			if(f instanceof Trait){
				topleft.setVisible(true);
				bottomright.setVisible(true);
				// On positionne les boutons sur le début et la fin du trait
				topleft.setBounds(f.getterX()-10, f.getterY()-10, 20,20);
				bottomright.setBounds(f.getterDX()-10, f.getterDY()-10, 20,20);
				add(topleft);
				add(bottomright);
				remove(topright);
				remove(bottomleft);
			// Sinon, c'est qu'on est sur une autre forme qu'un trait
			} else {
				// On rend visible les quatre boutons
				topleft.setVisible(true);
				topright.setVisible(true);
				bottomleft.setVisible(true);
				bottomright.setVisible(true);
				// On les positionnes aux quatre coins du rectangle qui contient la forme
				topleft.setBounds(f.getterX()-10, f.getterY()-10, 20,20);
				topright.setBounds(f.getterX() + f.getterDX()-10, f.getterY()-10, 20,20);
				bottomleft.setBounds(f.getterX()-10, f.getterY() + f.getterDY()-10, 20,20);
				bottomright.setBounds(f.getterX() + f.getterDX()-10, f.getterY() + f.getterDY()-10, 20,20);
				// On ajoute les quatre boutons à notre JPanel
				add(topleft);
				add(topright);
				add(bottomleft);
				add(bottomright);
			}
		} else {
			// Si on a pas sélectionné de forme, on supprime les boutons du JPanel
			hiddenButtons();
		}
	}
	
	// Méthode pour initialiser les actions des boutons pour redimensionner les formes
	private void changeSize(){
		// Si on clique sur le bouton en haut à gauche
		topleft.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				// On stock les valeurs du clic
				changeSizeX = e.getX();
				changeSizeY = e.getY();
			}
		});

		// Si on drag le bouton en haut à gauche
		topleft.addMouseMotionListener(new MouseMotionAdapter(){
			@Override
			public void mouseDragged(MouseEvent e){
				// Si on a une forme sélectionnée
				if(selectedForme != null){
					// On déplace le bouton à l'endroit de la souris
					topleft.setLocation(e.getX() - changeSizeX + topleft.getLocation().x, e.getY() - changeSizeY + topleft.getLocation().y);
					// On déplace le coin supérieur gauche de la forme à l'endroit du bouton
					selectedForme.setSelectedEndpoint(topleft.getLocation().x, topleft.getLocation().y, selectedForme.getterX(), selectedForme.getterY(), selectedForme.getterDX(),selectedForme.getterDY());
					// On resélectionne la forme en plaçant les boutons
					modeSelection(selectedForme);
				}
			}
		});

		// Si on clic sur le bouton en haut à droite de la sélection
		topright.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				// On stock les valeurs du clic
				changeSizeX = e.getX();
				changeSizeY = e.getY();
			}
		});

		topright.addMouseMotionListener(new MouseMotionAdapter(){
			@Override
			public void mouseDragged(MouseEvent e){
				// Si on a une forme sélectionnée
				if(selectedForme != null){
					// On déplace le bouton à l'endroit de la souris
					topright.setLocation(e.getX() - changeSizeX + topright.getLocation().x, e.getY() - changeSizeY + topright.getLocation().y);
					// On déplace le coin supérieur gauche de la forme à l'endroit du bouton
					selectedForme.setEndpoint(selectedForme.getterX(), topright.getLocation().y, topright.getLocation().x, selectedForme.getterY() + selectedForme.getterDY());
					// On resélectionne la forme en plaçant les boutons
					modeSelection(selectedForme);
				}
			}
		});
		
		// Si on clic sur le bouton en bas à gauche de la sélection
		bottomleft.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				// On stock les valeurs du clic
				changeSizeX = e.getX();
				changeSizeY = e.getY();
			}
		});

		bottomleft.addMouseMotionListener(new MouseMotionAdapter(){
			@Override
			public void mouseDragged(MouseEvent e){
				// Si on a une forme sélectionnée
				if(selectedForme != null){
					// On déplace le bouton à l'endroit de la souris
					bottomleft.setLocation(e.getX() - changeSizeX + bottomleft.getLocation().x, e.getY() - changeSizeY + bottomleft.getLocation().y);
					// On déplace le coin supérieur gauche de la forme à l'endroit du bouton
					selectedForme.setEndpoint(bottomleft.getLocation().x, selectedForme.getterY(), selectedForme.getterX() + selectedForme.getterDX(), bottomleft.getLocation().y);
					// On resélectionne la forme en plaçant les boutons
					modeSelection(selectedForme);
				}
			}
		});
		
		// Si on clic sur le bouton en bas à droite de la sélection
		bottomright.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				// On stock les valeurs du clic
				changeSizeX = e.getX();
				changeSizeY = e.getY();
			}
		});

		bottomright.addMouseMotionListener(new MouseMotionAdapter(){
			@Override
			public void mouseDragged(MouseEvent e){
				// Si on a une forme sélectionnée
				if(selectedForme != null){
					// On déplace le bouton à l'endroit de la souris
					bottomright.setLocation(e.getX() - changeSizeX + bottomright.getLocation().x, e.getY() - changeSizeY + bottomright.getLocation().y);
					// On déplace le coin supérieur gauche de la forme à l'endroit du bouton
					selectedForme.setEndpoint(selectedForme.getterX(), selectedForme.getterY(), bottomright.getLocation().x, bottomright.getLocation().y);
					// On resélectionne la forme en plaçant les boutons
					modeSelection(selectedForme);
				}
			}
		});
	}
	
	// Prend en paramètre un événement de souris
	private void afficheCoords(MouseEvent e){
		// Calcule et affiche le nouveau texte des coordonnées en bas de l'écran
		String coordX = String.valueOf((int) e.getPoint().getX());
		String coordY = String.valueOf((int) e.getPoint().getY());
		fenetre.textCoords.setText("(" + coordX + "," + coordY + ")");
	}
}
