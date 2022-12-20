/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Micho
 */
public class Ascenseur extends Thread
{
    // L'etage courante
    public int etage;
    // La direction courante
    public Direction dir;
    private Controleur controleur;

    @Override
    public String toString() {
        return "Ascenseur{" +
                "etage=" + etage +
                '}';
    }

    public Ascenseur (int etage, Controleur controleur)
    {
        this.etage =etage;
        this.controleur =controleur;
        this.dir =Direction.NONE;
    }


    public void run()
    {
        while(true)
        {
            // Si l'ascenceur n'a aucune direction, prendre celle de l'appel a l'etage
            if (dir ==Direction.NONE && controleur.appels[etage-1] !=Direction.NONE)
                dir =controleur.appels[etage-1];

            //  Ouvrir si necessaire
            if( (controleur.appels[etage-1] !=Direction.NONE && dir ==controleur.appels[etage-1])
                    || controleur.destinations[etage-1])
            {
                // Signaler l'arret
                controleur.ajouterEvenement("+ Ascenseur:	\t+ arret a l'etage "+etage+"\n");
                controleur.arretAscenseur(etage);

                // Attendre la fermeture de la porte
                while (!controleur.redemarrer())
                    try { Thread.sleep(100); }
                    catch(InterruptedException e) { System.out.print("Erreur dans Thread.sleep\n"); }

                controleur.ajouterEvenement("+ Ascenseur: \t\t+ fin de l'arret\n");


                // Effacer appel ou destination pour l'etage courant
                if (controleur.appels[etage-1]==dir)
                    controleur.appels[etage-1]=Direction.NONE;
                controleur.destinations[etage-1]=false;
            }

            // Choisir la direction de l'ascenseur
            dir =choisirDirection();
            controleur.ajouterEvenement("+ Ascenseur:	\t+ direction: "+dir+"\n");

            // Changer d'etage
            if (dir == Direction.UP) // true ICI 
                etage++;
            else if (dir == Direction.DOWN)
                etage--;

            controleur.ajouterEvenement("+ Ascenseur:	\t+ etage: "+etage+"\n");

            // Renverser la direction
            if (etage == Controleur.ETAGES)
                dir = Direction.DOWN;
            else if (etage == 1)
                dir = Direction.UP;


            try { Thread.sleep(100); } // laisser la main au autre threads
            catch(InterruptedException e) { System.out.print("Erreur dans Thread.sleep\n"); }
        }
    }


 //la fonction retourne la prochaine direction que l'ascenseur doit prendre
 protected Direction choisirDirection()
    {
        Direction ret =Direction.NONE;

        if (dir==Direction.UP || dir==Direction.NONE)
        {
            if (appelVersLeBas(etage) || destAuDessus(etage))
                ret =Direction.UP;
            else if (appelVersLeBas(etage) || destEnDessous(etage))
                ret =Direction.DOWN;
        }
        else if (dir==Direction.DOWN)
        {
            if (appelVersLeHaut(etage) || destEnDessous(etage))
                ret =Direction.DOWN;
            else if (appelVersLeHaut(etage) || destAuDessus(etage))
                ret =Direction.UP;
        }

        return ret;
    }


    private boolean appelVersLeBas(int etageCourant)
    {
        boolean ret =false;
        for (int i=Controleur.ETAGES-1; i>=etageCourant; i--)
        {
            if (controleur.appels[i]!=Direction.NONE)
                ret =true;
        }

        return ret;
    }

    private boolean appelVersLeHaut(int etageCourant)
    {
        boolean ret =false;
        for (int i=0; i<etageCourant-1; i++)
        {
            if (controleur.appels[i]!=Direction.NONE)
                ret =true;
        }

        return ret;
    }


    private boolean destAuDessus(int etageCourant)
    {
        boolean ret =false;
        for (int i=Controleur.ETAGES-1; i>=etageCourant; i--)
        {
            if (controleur.destinations[i]==true)
                ret =true;
        }

        return ret;
    }

    private boolean destEnDessous(int etageCourant)
    {
        boolean ret =false;
        for (int i=0; i<etageCourant-1; i++)
        {
            if (controleur.destinations[i]==true)
                ret =true;
        }

        return ret;
    }

}
