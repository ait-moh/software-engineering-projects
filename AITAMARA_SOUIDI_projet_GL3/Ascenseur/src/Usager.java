/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Micho
 */
public class Usager extends Thread
{
    
    private String nom;
    private int etage;
    private int dest;
    private Direction dir;
    //Temps de distraction en millisecondes
    private static final  long delai =3000;
    // L'instance du Controleur qui gere le syteme.f
    private Controleur controleur;

    public Usager(String nom, int etage, int dest, Controleur controleur)
    {
        this.nom =nom;
        this.etage =etage;
        this.dest =dest;
        if (dest > etage) {
            this.dir = Direction.UP;
        }
        else 
            this.dir=Direction.DOWN;

        this.controleur =controleur;
    }


    public String getNom() {
        return nom;
    }

    public int getEtage() {
        return etage;
    }

    public int getDest() {
        return dest;
    }

    public Direction getDir() {
        return dir;
    }

    @Override
    public String toString() {
        return "Usager{" +
                "nom='" + nom + '\'' +
                ", etage=" + etage +
                ", dest=" + dest +
                ", dir=" + dir +
                '}';
    }

    public void run()
    {
        // Tant que la porte n'est pas ouverte et la direction incorrecte
        while(!controleur.porteOuverteAEtage(etage) || !controleur.directionAscenseur(dir))
        {
            //  Attente ou d'appel...
            while(!controleur.porteOuverteAEtage(etage) || !controleur.directionAscenseur(dir))
            {
                // Si aucun appel n'est signale, l'usager effectue l'appel 
                if (controleur.appels[etage-1]==Direction.NONE)
                {
                    controleur.ajouterEvenement("# Usager["+nom+"]: \t\t# effectue l'appel "+etage+"-"+dir+"\n");
                    controleur.appels[etage-1]=dir;
                }

                try { Thread.sleep(1); } // laisser la main au autre threads
                catch(InterruptedException e) { System.out.print("Erreur dans Thread.sleep\n"); }
            }

            //  Dans le cas ou l'usager est distrait (attente supplementaire)
            if(controleur.distractionUsager() && delai > 0)
                try
                {
                    controleur.ajouterEvenement("# Usager["+nom+"]: \t\t# est distrait...\n");
                    Thread.sleep(delai);
                }
                catch(InterruptedException e) { System.out.print("Erreur dans Thread.sleep\n"); }

        } //Si la porte est toujours ouverte, l'usager entre enfin!

        // L'usager entre dans l'ascenseur 
        controleur.ajouterEvenement("# Usager["+nom+"]: \t\t# entre dans l'ascenseur\n");

        //L'usager signale sa destination
        controleur.ajouterEvenement("# Usager["+nom+"]: \t\t# entre la destination "+dest+"\n");
        controleur.destinations[dest-1]=true;

        //  L'usager attend que la porte s'ouvre a destination et il descend
        while (!controleur.porteOuverteAEtage(dest))
            try { Thread.sleep(100); } // laisser la main au autre threads
            catch(InterruptedException e) { System.out.print("Erreur dans Thread.sleep\n"); }

        // L'usager est arrive a destination
        controleur.ajouterEvenement("# Usager["+nom+"]: \t\t# destination atteinte\n");

        // L'usager a termine 
        controleur.usagerArrive();
    }
}
