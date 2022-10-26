import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Stack;

public class Fenetre extends JFrame{

	// Attributs pour les boutons de notre applications
	private JButton boutonEllipse;
	private JButton boutonRectangle;
	private JButton boutonTriangle;
	private JButton boutonTrait;
	private JButton boutonSelect;
	private JButton boutonCouleurBcg;
	private JButton boutonCouleurStroke;
	private JButton boutonChooseColor;
	private JButton boutonGomme;
	private JButton boutonUndo;
	private JButton boutonRedo;
	private JButton boutonCopy;
	private JButton boutonPaste;

	// Attributs pour le menu en haut de notre application
	private JMenuItem ouvrir;
	private JMenuItem enregistrer;
	private JMenuItem nouveau;
	private JMenuBar menuBar;
	private JMenu menu;

	// Attributs pour la taille de l'épaisseur du trait
	private SpinnerModel listeValue;
	private JSpinner rayonCercle;

	// Attribut pour gérer quelle couleur à modifier si on modifie la couleur
	private Couleur colorToEdit;

	public JTextField textCoords;

	// Variable qui contient là où les formes seront stockés, ainsi que le JPanel où elles seront dessinées
	private Dessin p;

	public Fenetre(int w, int h){ 
		super("Paint (Sans nom)");		// Constructeur du JFrame pour mettre le titre de la fenêtre
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// Fermer la fenêter si on appuie sur la croix
		this.setSize(w,h);		// On définit la taille de la fenêtre
		this.setResizable(true);	// On désactive le redimensionnement de la fenêtre
		colorToEdit = Couleur.MAIN;		// On initialise la couleur sélectionnée par défaut
		JPanel main = new JPanel();		// On créé un JPanel qui contiendra notre JToolBar et notre Dessin
		main.setLayout(new BorderLayout());		// On définit notre layout 

		menuBar = new JMenuBar();		// On définit notre menu en haut de la page
		menu = new JMenu("Fichier");    // On créé les boutons associés dans notre menu
		ouvrir = new JMenuItem("Ouvrir");
		nouveau = new JMenuItem("Nouveau");
		enregistrer = new JMenuItem("Enregistrer sous");

		// On ajoute les éléments dans notre JMenuBar
		menu.add(nouveau);
		menu.add(ouvrir);
		menu.add(enregistrer);
		menuBar.add(menu);

		// On créé notre JToolBar 
		JToolBar boutonPane = new JToolBar();
		boutonPane.setFloatable(true);

		// Création des boutons
		this.boutonEllipse = new JButton(new ImageIcon("cercle.png"));
		this.boutonEllipse.setBackground(Color.WHITE);
		this.boutonRectangle = new JButton(new ImageIcon("rectangle.png"));
		this.boutonRectangle.setBackground(Color.WHITE);
		this.boutonTriangle = new JButton(new ImageIcon("triangle.png"));
		this.boutonTriangle.setBackground(Color.WHITE);
		this.boutonTrait = new JButton(new ImageIcon("trait.png"));
		this.boutonTrait.setBackground(Color.WHITE);
		this.boutonSelect = new JButton(new ImageIcon("select.png"));
		this.boutonSelect.setBackground(Color.WHITE);
		this.boutonGomme = new JButton(new ImageIcon("gomme.png"));
		this.boutonGomme.setBackground(Color.WHITE);
		this.boutonUndo = new JButton(new ImageIcon("undo.png"));
		this.boutonUndo.setBackground(Color.WHITE);
		this.boutonRedo = new JButton(new ImageIcon("redo.png"));
		this.boutonRedo.setBackground(Color.WHITE);
		this.boutonCopy = new JButton(new ImageIcon("copy.png"));
		this.boutonCopy.setBackground(Color.WHITE);
		this.boutonPaste = new JButton(new ImageIcon("paste.png"));
		this.boutonPaste.setBackground(Color.WHITE);
		this.boutonCouleurBcg = new JButton(new ImageIcon("transparent.png"));
		boutonCouleurBcg.setBackground(Color.WHITE);
		this.boutonCouleurStroke = new JButton(new ImageIcon("transparent.png"));
		boutonCouleurStroke.setBackground(Color.BLACK);
		this.boutonChooseColor = new JButton(new ImageIcon("colorchooser.png"));
		this.boutonChooseColor.setBackground(Color.WHITE);

		// Zone de texte au survol des boutons
		this.boutonCouleurBcg.setToolTipText("Couleur de fond");
		this.boutonCouleurStroke.setToolTipText("Couleur de contour");
		this.boutonCopy.setToolTipText("Copier (Ctrl+C)");
		this.boutonPaste.setToolTipText("Coller (Ctrl+V)");
		this.boutonUndo.setToolTipText("Annuler (Ctrl+Z)");
		this.boutonRedo.setToolTipText("R\u00e9p\u00e9ter (Ctrl+Y)");

		// Création des actions des boutons
		setActions();

		// Gestion du JSpinner
		listeValue = new SpinnerNumberModel(1,1,20,1);
		rayonCercle = new JSpinner(listeValue);

		// Classe anonyme pour ajouter un événement quand on change de valeur dans notre JSpinner
		rayonCercle.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				// On récupère la valeur dans notre JSpinner
				int n = (Integer)rayonCercle.getValue();
				// On modifie l'épaisseur du trait de notre dessin
				p.setStroke((float)n);
				// On modifie l'épaisseur du trait de la forme sélectionnée si elle existe
				if(p.selectedForme != null){
					p.selectedForme.stroke = (float)n;
				}
			}
		});

		// On ajoute le JSpinner dans un JPanel et on met le JPanel dans la JToolBar
		JPanel spinner = new JPanel();
		spinner.setLayout(new BorderLayout());
		spinner.add(rayonCercle,BorderLayout.WEST);
		
		// Ajout des boutons dans la JToolBar
		boutonPane.add(this.boutonEllipse);
		boutonPane.add(this.boutonRectangle);
		boutonPane.add(this.boutonTriangle);
		boutonPane.add(this.boutonTrait);
		boutonPane.addSeparator();
		boutonPane.add(this.boutonSelect);
		boutonPane.add(this.boutonGomme);
		boutonPane.addSeparator();
		boutonPane.addSeparator();
		boutonPane.add(this.boutonCouleurStroke);
		boutonPane.add(this.boutonCouleurBcg);
		boutonPane.addSeparator();
		boutonPane.add(this.boutonChooseColor);
		boutonPane.addSeparator();
		boutonPane.addSeparator();
		boutonPane.add(this.boutonUndo);
		boutonPane.add(this.boutonRedo);
		boutonPane.addSeparator();
		boutonPane.add(this.boutonCopy);
		boutonPane.add(this.boutonPaste);
		boutonPane.addSeparator();
		boutonPane.addSeparator();
		boutonPane.addSeparator();
		boutonPane.add(spinner);
		main.add(boutonPane,BorderLayout.NORTH);

		// Panneau de dessin
		this.p = new Dessin(Color.BLACK, Color.WHITE,this);

		// Création des raccourcis claviers pour le copier coller
		this.p.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK),"Copier");
		this.p.getActionMap().put("Copier", new Copier());

		this.p.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK),"Coller");
		this.p.getActionMap().put("Coller", new Coller());

		this.p.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z,ActionEvent.CTRL_MASK),"Undo");
		this.p.getActionMap().put("Undo", new Undo());

		this.p.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Y,ActionEvent.CTRL_MASK),"Redo");
		this.p.getActionMap().put("Redo", new Redo());

		// On ajoute notre dessin au milieu de notre fenêtre
		main.add(p, BorderLayout.CENTER);

		// JTextField à la place d'un JLabel car le JLabel.setText refresh les boutons de sélections
		textCoords = new JTextField("(0,0)",10);
		// On change la couleur de texte quand le textfield est disable
		textCoords.setDisabledTextColor(Color.BLACK);
		// On change la couleur de fond
		textCoords.setBackground(null);
		// On change les propriétés du JTextField
		textCoords.setOpaque(false); 
		textCoords.setEnabled(false);
		textCoords.setBorder(null);
		// On créé le jpanel du footer de la page
		JPanel text = new JPanel(new BorderLayout());
		text.setBackground(null);
		text.add(textCoords,BorderLayout.WEST);
		textCoords.setPreferredSize(new Dimension(2,20));
		main.add(text,BorderLayout.SOUTH);

		// Ajout d'un écouteur de composant
		addComponentListener(new ComponentAdapter(){  
			@Override
			// Si on resize le composant (ici la fenêtre)
			public void componentResized(ComponentEvent evt) {
				// On resélectionne la fenêtre (car cela provoque un repaint)
				if(p.selectedForme != null){
					p.modeSelection(p.selectedForme);	
				}
			}
		});
		// On ajoute le JMenuBar à notre fenêtre
		this.setJMenuBar(menuBar);
		// On change le JPanel de notre fenêtre par celui que nous avons créé
		this.setContentPane(main);
		// On centre la fenêtre à l'écran
		this.setLocationRelativeTo(null);
		// On met la fenêtre en plein écran
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		// On rend la fenêtre visible
		this.setVisible(true);

	} 

	private void setActions(){ 
		// Gestion des événements cliquables grâce à des classes anonymes
		this.boutonEllipse.addActionListener(new ActionListener(){			
			@Override
			public void actionPerformed(ActionEvent e){
				// On déselectionne la forme
				p.selectedForme = null;
				// On change le mode de dessin
				p.setMode(ModeDessin.ELLIPSE);
				// On cache les boutons de sélection
				p.hiddenButtons();
			}
		});
		this.boutonRectangle.addActionListener(new ActionListener(){			
			@Override
			public void actionPerformed(ActionEvent e){
				// On déselectionne la forme
				p.selectedForme = null;
				// On change le mode de dessin
				p.setMode(ModeDessin.RECTANGLE);
				// On cache les boutons de sélection
				p.hiddenButtons();
			}
		});
		this.boutonTriangle.addActionListener(new ActionListener(){			
			@Override
			public void actionPerformed(ActionEvent e){
				// On déselectionne la forme
				p.selectedForme = null;
				// On change le mode de dessin
				p.setMode(ModeDessin.TRIANGLE);
				// On cache les boutons de sélection
				p.hiddenButtons();
			}
		});
		this.boutonTrait.addActionListener(new ActionListener(){			
			@Override
			public void actionPerformed(ActionEvent e){
				// On déselectionne la forme
				p.selectedForme = null;
				// On change le mode de dessin
				p.setMode(ModeDessin.TRAIT);
				// On cache les boutons de sélection
				p.hiddenButtons();
			}
		});
		this.boutonSelect.addActionListener(new ActionListener(){			
			@Override
			public void actionPerformed(ActionEvent e){
				// On change le mode de dessin
				p.setMode(ModeDessin.SELECT);
			}
		});
		this.boutonGomme.addActionListener(new ActionListener(){			
			@Override
			public void actionPerformed(ActionEvent e){
				// On déselectionne la forme
				p.selectedForme = null;
				// On change le mode de dessin
				p.setMode(ModeDessin.ERASE);
				// On cache les boutons de sélection
				p.hiddenButtons();
			}
		});

		this.menu.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				// On cache les boutons de sélection
				p.hiddenButtons();
			}
		});


		this.nouveau.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				// On cache les boutons de sélection
				p.hiddenButtons();
				// Si on a au moins une forme dans notre liste
				if(p.formes.size() > 0){
					// On ouvre une boîte de dialogue 
					int choix = JOptionPane.showConfirmDialog(null, "Voulez-vous enregistrer le dessin courant ?", "Nouveau fichier", JOptionPane.YES_NO_CANCEL_OPTION);
					switch(choix){
						// Si on appuie sur le bouton "Oui"
						case JOptionPane.YES_OPTION:
							// On enregistre le fichier
							enregistrerSous();
							// On remet à zéro les formes
							p.formes = new ArrayList<Forme>();
							// On change le nom de la fenêtre
							setTitle("Paint (Sans nom)");
							break;
						// Si on appuie sur le bouton "Non"
						case JOptionPane.NO_OPTION:	
							// On remet à zéro les formes
							p.formes = new ArrayList<Forme>();
							// On change le titre de la fenêtre
							setTitle("Paint (Sans nom)");
							break;
						default:
							// Par défaut, on ne fait rien
							return;
					}

				}
			}
		});

		// Classe anonyme pour ouvrir un fichier
		this.ouvrir.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				// On cache les boutons de sélection
				p.hiddenButtons();
				// On créé un sélecteur de fichier
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				// On change la liste des fichiers filtrés par les fichiers .paint
				jfc.addChoosableFileFilter(new FileNameExtensionFilter("Paint", "paint"));
				// On modifie les propriétés du JFileChooser
				jfc.setAcceptAllFileFilterUsed(false);
				jfc.setMultiSelectionEnabled(false);

				// On ouvre le JFileChooser
				int r = jfc.showOpenDialog(null);

				// Si on appuie sur "Open" dans le JFileChooser
				if(r == JFileChooser.APPROVE_OPTION){
					try{
						// On ouvre le fichier obtenu grâce au jfilechooser
						FileInputStream ouvrir = new FileInputStream(jfc.getSelectedFile().getAbsolutePath());
						// On créé un flux d'entrée d'objets qu'on va lire dans notre fichier
						ObjectInputStream inject = new ObjectInputStream(ouvrir);

						boolean eof = false;

						// On vide la liste de formes stockées dans notre dessin
						p.formes = new ArrayList<Forme>();
						do{
							try{
								// On lit un objet dans le fichier
								Object o = inject.readObject();
								// On l'ajoute à notre liste de formes stockées
								p.formes.add((Forme)o);
							} catch(EOFException exception){
								// Si on arrive à la fin de fichier, on arrête la boucle
								eof = true;
							} catch(ClassNotFoundException exception){
								// Si on a pas trouvé de classes correspondant à la forme lue, fin du programme
								exception.printStackTrace();
							}
							// Lecture des objets tant qu'on est pas à la fin du fichier
						} while(!eof);

						// On ferme les flux
						inject.close();
						ouvrir.close();
						// On change le titre de la fenêtre pour avoir "Paint (FILENAME)"
						setTitle("Paint (" + jfc.getSelectedFile().getName() + ")");
					} catch(FileNotFoundException exception){
						// Si on trouve pas le fichier, erreur
						exception.printStackTrace();
					} catch(IOException exception){
						// Si on arrive pas à ouvrir le fichier, erreur
						exception.printStackTrace();
					}
				}
				
			}
		});

		this.enregistrer.addActionListener(new ActionListener(){			
			@Override
			public void actionPerformed(ActionEvent e){
				// On cache les boutons de sélection
				p.hiddenButtons();
				// On enregistre le fichier
				enregistrerSous();
			}
		});

		this.boutonCouleurBcg.addActionListener(new ActionListener(){			
			@Override
			public void actionPerformed(ActionEvent e){
				// Si on appuie sur le bouton de la couleur de fond, on change la couleur modifiable par la couleur principale
				colorToEdit = Couleur.MAIN;
			}
		});	
		this.boutonCouleurStroke.addActionListener(new ActionListener(){			
			@Override
			public void actionPerformed(ActionEvent e){
				// Si on appuie sur le bouton de la couleur de contour, on change la couleur modifiable par la couleur secondaire
				colorToEdit = Couleur.SECONDARY;
			}
		});

		// Classe anonyme pour gérer le changement de couleurs
		this.boutonChooseColor.addActionListener(new ActionListener(){			
			@Override
			public void actionPerformed(ActionEvent e){
				// On créé un nouveau colorchooser
				JColorChooser changementCouleur = new JColorChooser();
				// Variable de retour de notre dialogue de JColorChooser
				Color c = null;
				// Si on a sélectionné la couleur principale
				if(colorToEdit == Couleur.MAIN){
					// On ouvre la fenêtre de dialogue avec comme couleur par défaut, la couleur principale
 					c = JColorChooser.showDialog(null, "Changer la couleur", p.colorBcg);
				// Sinon, on est sur la couleur secondaire
				} else {
					// On ouvre la fenêtre de dialogue avec comme couleur par défaut, la couleur secondaire
					c = JColorChooser.showDialog(null, "Changer la couleur", p.colorStroke);
				}
				// Si on a bien sélectionné une couleur dans notre JColorChooser
				if(c != null){
					// Si on a comme couleur sélectionnée la couleur principale
					if(colorToEdit == Couleur.MAIN){
						// On change la couleur du bouton de la couleur principale
						boutonCouleurBcg.setBackground(c);
						// On change la couleur de fond dans le JPanel pour les prochaines formes
						p.setColorBcg(c);
						// Si la forme sélectionnée existe, on change sa couleur de fond
						if(p.selectedForme != null){
							p.selectedForme.colorBcg = c;
						}
					// Sinon, on est sur la couleur secondaire
					} else {
						// On change la couleur de fond du bouton de la couleur secondaire
						boutonCouleurStroke.setBackground(c);
						// On change la couleur de contour dans le JPanel pour les prochaines formes
						p.setColorStroke(c);
						// Si la forme sélectionnée existe, on change sa couleur de contour
						if(p.selectedForme != null){
							p.selectedForme.colorStroke = c;
						}
					}
				}
			}
		});	

		this.boutonUndo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				p.hiddenButtons();
				undo();
			}
		});

		this.boutonRedo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				p.hiddenButtons();
				redo();
			}
		});

		this.boutonCopy.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				p.modeSelection(p.selectedForme);
				copy();
			}
		});

		this.boutonPaste.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				p.modeSelection(p.selectedForme);
				paste();
			}
		});
	}

	private void undo(){
		// Si on a des formes de dessinées
		if(p.formes.size() > 0){
			// On récupère la dernière forme dessinée
			Forme f = p.formes.get(p.formes.size()-1);
			// On la retire de la liste de formes à dessiner
			p.formes.remove(f);
			// On l'ajoute à la pile mémoire
			p.undo.push(f);
		}
	}

	private void redo(){
		// Si on a des éléments dans la pile
		if(!p.undo.empty()){
			// On récupère la forme en sommet de pile
			Forme f = p.undo.pop();
			// On l'ajoute dans notre liste de formes
			p.formes.add(f);
		}
	}

	private void copy(){
		// Une fois l'action réalisée, on copie la forme dans un buffer
		if(p.selectedForme != null){
			p.copiedForme = p.selectedForme;
		}
	}

	private void paste(){
		// Une fois l'action réalisée, si la forme copiée existe, il faut créé une nouvelle forme. 
		if(p.copiedForme != null){
			Forme f = null;
			Forme copiedForme = p.copiedForme;
			// Pour créer la nouvelle forme, il faut pouvoir savoir de quel type de forme il s'agit.
			if(p.copiedForme instanceof Rectangle){
				f = new Rectangle(copiedForme.colorStroke, copiedForme.colorBcg, copiedForme.x+25, copiedForme.y+25, copiedForme.dx, copiedForme.dy, copiedForme.stroke);
			} else if(p.copiedForme instanceof Triangle){
				f = new Triangle(copiedForme.colorStroke, copiedForme.colorBcg, copiedForme.x+25, copiedForme.y+25, copiedForme.dx+25, copiedForme.dy+25, copiedForme.stroke);
			} else if(p.copiedForme instanceof Trait){
				f = new Trait(copiedForme.colorStroke, copiedForme.x+25, copiedForme.y+25, copiedForme.dx+25, copiedForme.dy+25, copiedForme.stroke);
			} else if(p.copiedForme instanceof Ellipse){
				f = new Ellipse(copiedForme.colorStroke, copiedForme.colorBcg, copiedForme.x+25, copiedForme.y+25,copiedForme.dy+25, copiedForme.dx+25, copiedForme.stroke);
			}
			// Si on a récupéré une forme, on l'ajoute dans notre liste de formes et on positionne la sélection dessus
			if(f != null)
			{
				p.formes.add(f);
				p.setMode(ModeDessin.SELECT);
				p.modeSelection(f);
			}
		}
	}

	private void enregistrerSous(){
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		// On change la liste des fichiers filtrés par les fichiers .paint
		jfc.addChoosableFileFilter(new FileNameExtensionFilter("Paint", "paint"));
		// On modifie les propriétés du JFileChooser
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.setMultiSelectionEnabled(false);

		// On ouvre le JFileChooser
		int r = jfc.showOpenDialog(null);

		// Si on appuie sur "Open" dans le JFileChooser
		if(r == JFileChooser.APPROVE_OPTION){
			try{
				// On ouvre le fichier obtenu grâce au jfilechooser
				FileOutputStream ouvrir = new FileOutputStream(jfc.getSelectedFile().getAbsolutePath());
				// On créé un flux d'entrée d'objets qu'on va enregistrer dans notre fichier
				ObjectOutputStream eject = new ObjectOutputStream(ouvrir);

				// Pour chaque forme de notre dessin, on les écrit dans notre fichier
				for(Forme f : p.formes){
					eject.writeObject(f);
				}							

				// On change le titre de la fenêtre pour avoir "Paint (FILENAME)"
				eject.close();
				ouvrir.close();
				// On modifie
				setTitle("Paint (" + jfc.getSelectedFile().getName() + ")");
			} catch(FileNotFoundException exception){
				// Si on trouve pas le fichier, erreur
				exception.printStackTrace();
			} catch(IOException exception){
				// Si on arrive pas à ouvrir le fichier, erreur
				exception.printStackTrace();
			}
		}
	}

	// Classe interne pour gérer l'action "Copier" 
	class Copier extends AbstractAction{
		public Copier(){
			super("Copier"); // On appelle le constructeur de AbstractAction pour avoir un nom à notre action
		}	
		public void actionPerformed(ActionEvent e){
			copy();
		}
	}

	// Classe interne pour gérer l'action "Coller"
	class Coller extends AbstractAction{
		public Coller(){
			super("Coller"); // On appelle le constructeur de AbstractAction pour avoir un nom à notre action
		}	
		public void actionPerformed(ActionEvent e){
			paste();
		}
	}

	// Classe interne pour gérer l'action "Undo" 
	class Undo extends AbstractAction{
		public Undo(){
			super("Undo"); // On appelle le constructeur de AbstractAction pour avoir un nom à notre action
		}	
		public void actionPerformed(ActionEvent e){
			undo();
		}
	}

	// Classe interne pour gérer l'action "Redo" 
	class Redo extends AbstractAction{
		public Redo(){
			super("Redo"); // On appelle le constructeur de AbstractAction pour avoir un nom à notre action
		}	
		public void actionPerformed(ActionEvent e){
			redo();
		}
	}

}
