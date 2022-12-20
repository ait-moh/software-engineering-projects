/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Vector;
import java.util.Random;
/**
 *
 * @author Micho
 */
public class Controleur
{
    // Nombre d'etages
    public static final int ETAGES = 3;

    // Nombre d'usagers
    public static final int USAGERS = 2;

    protected Ascenseur ascenseur;
    protected Porte portes[];

    public Direction appels[];	// NONE : aucun appel.  UP/DOWN : appel a l'etage [i] dans direction indiqu�e
    public boolean destinations[]; // true : destination voulue a l'etage [i]

    protected int usager_term = 0;

    protected int etageArret; // Correspond a l'eage de l'arret. '-1' est le signal de demarrage

    protected Vector<String> evenements;
    public  Usager usager0,usager1;

    protected Random rand;



    /** Main : methode appellee lors de l'execution. */
    public static void main (String[] args)
    {
        // Initialiser et demarrer le systeme
        (new Controleur()).runControleur();
    }


    /** Constructeur qui initialise la simulation. */
    public Controleur()
    {
        // CREER LES ATTRIBUTS
        destinations = new boolean[ETAGES];
        appels = new Direction[ETAGES];
        portes = new Porte[ETAGES];
        evenements = new Vector<String>();
        rand = new Random();

        // INITIALISATIONS 
        // Initialiser les attributs
        ascenseur =null; // aucun ascenceur encore...

        for(int i=0; i<ETAGES; i++)
        {
            appels[i] =Direction.NONE; 	// aucun appels
            destinations[i] =false;		// aucune destination
            portes[i] =null;			// aucune porte active
        }
        // Aucun arret signale au debut de la simulation 
        etageArret =-1;
    }


    /** Lancement de la simulation. */
    public void runControleur()
    {
        // CREATIONS OBJETS 
        System.out.print("\nDebut de la simulation\n");

        // Creer les portes (de 0 a 2 pour les etages 1 a 3)
        for (int i=0;i<ETAGES;i++)
        {
            portes[i] =new Porte(i+1,this); // (etage, Controleur)
            portes[i].start();
        }

        // Creer les usagers (partie du preambule)
      // (nom, etage, dest, simulateur)
        usager0=new Usager("0",2,3,this);
        usager0.start();
        usager1=new Usager("1",3,1,this);
        usager1.start();
        
        
        // Creer l'ascenseur  (partie du preambule) 
        ascenseur = new Ascenseur(1, this); // (etage, Controleur)
        ascenseur.start();

    }


    public void arretAscenseur(int etage)
    {
        this.etageArret =etage;
    }
    public boolean arretCetEtage(int etage)
    {
        return etageArret==etage ? true : false;
    }


    public void redemarreAscenceur()
    {
        this.etageArret =-1;
    }
    public boolean redemarrer()
    { if (etageArret==-1)
        return true;
    else
        return false;
    }



    public boolean porteOuverteAEtage(int etage)
    {
        if (portes[etage-1]==null)
            return  false;
        else
            return portes[etage-1].check_porteOuverte();
    }
    public boolean directionAscenseur(Direction dir)
    {
        if (ascenseur==null)
            return false;
        else
        {
            if (ascenseur.dir==dir)
                return  true;
            else
                return false;
        }
    }

    public void usagerArrive()
    {
        usager_term ++;

        if (usager_term ==USAGERS)
        {
            System.out.print("Fin de la simulation\n\n");
            System.exit(0);
        }
    }



    // fonction random qui donne une valeur parrport a la distraction de lusager
    public boolean distractionUsager()
    {
        return rand.nextBoolean();
    }





    /** Traitement des evenements */
    private void affichage()
    {
        System.out.print("Debut:\n\n");
        for(int i=0;i<evenements.size();i++)
            System.out.print((i+1) +".\t"+ evenements.get(i));
    }

    /** Traitement des evenements */
    public void ajouterEvenement(String evt)
    {
        evenements.add(evt);
        System.out.print(evenements.size() +".\t"+ evt);
        System.out.flush();
    }


}
