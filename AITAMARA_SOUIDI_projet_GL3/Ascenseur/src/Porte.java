/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Micho
 */
public class Porte extends Thread
{
   /** Etage a laquelle se situe la porte. */
    private int etagePorte;
    /** Indicateur decrivant si la porte est ouverte ou non. */
    public boolean porteOuverte;
    /** L'instance du Controleur qui gere le syteme. */
    private Controleur controleur;

    /** Constructeur */
    public Porte(int etage, Controleur controleur)
    {
        this.etagePorte =etage;
        this.porteOuverte =false;
        this.controleur = controleur;
    }

    @Override
    public String toString()
    {
        return "Porte[" +
                "etage=" + etagePorte +
                ", ouverture=" + porteOuverte +
                "]";
    }


    public void run()
    {
        while(true)
        {
            // [1] Attendre que l'ascenseur soit a l'arret a l'etage
            while (!controleur.arretCetEtage(etagePorte))
                try { Thread.sleep(200); } // Courte pause pour allouer le controle aux autres thread...
                catch(InterruptedException e) { System.out.print("Erreur dans Thread.sleep\n"); }

            // [2] Ouvrir la porte
            controleur.ajouterEvenement("* Porte etage "+etagePorte+":\t* ouverture\n");
            porteOuverte=true;

            // [3] Attendre avant de refermer
            try
            {
                Thread.sleep(3000);
            }
            catch(InterruptedException e) { System.out.print("Erreur dans Thread.sleep\n"); }

            // [4] Fermer la porte
            controleur.ajouterEvenement("* Porte etage "+etagePorte+":\t* fermeture\n");
            porteOuverte=false;

            // [5] Signaler a l'ascenseur de redemarrer
            controleur.redemarreAscenceur();
        }
    }

    public boolean check_porteOuverte()
    {
        return porteOuverte;
    }
}
