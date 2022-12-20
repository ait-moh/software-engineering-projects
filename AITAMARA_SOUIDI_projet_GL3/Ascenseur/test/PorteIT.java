/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Micho
 */
public class PorteIT {
    
     Controleur controleur;

    @Before
    public void setUp()
    {
       controleur = new Controleur();
        controleur.runControleur();
    }

    //------------------------ Test unitaire sur la porte
    @Test
    public void testPorte()
    {
        System.out.println("\n-> ouverture/fermeture d'une porte au 2eme l'etage\n");

        // Valider le comportement : la porte est bien fermee?
        assertFalse(controleur.portes[1].check_porteOuverte());
        assertEquals(controleur.etageArret,-1);
        System.out.println("1-- La "+controleur.portes[1]+" est fermee, et aucun signal d'arret.");


        // Simuler l'arr�t de l'ascenseur au 1er etage
        controleur.etageArret=2;

        // Attendre ouverture de la porte (1/1 delai)...
        try { Thread.sleep(500); }
        catch(InterruptedException e) { System.out.print("Erreur dans Thread.sleep\n"); }

        // Valider le comportement : la porte est ouverte?
        assertTrue(controleur.portes[1].check_porteOuverte());
        System.out.println("2-- La "+controleur.portes[1]+" est bien ouverte.");

        // Attendre fermeture de la porte (delai complet)...
        try { Thread.sleep(3000); }
        catch(InterruptedException e) { System.out.print("Erreur dans Thread.sleep\n"); }

        // Valider le comportement : la porte s'est refermee?
        assertFalse(controleur.portes[1].check_porteOuverte());
        System.out.println("3-- La "+controleur.portes[1]+" est refermee.");
    }

}
