package main.java.com.gestionabonnements.presentation;

import main.java.com.gestionabonnements.DAO.AbonnementDAO;
import main.java.com.gestionabonnements.DAO.PaiementDAO;
import main.java.com.gestionabonnements.entites.Paiement;
import main.java.com.gestionabonnements.entites.StatutPaiement;
import main.java.com.gestionabonnements.services.AbonnementService;
import main.java.com.gestionabonnements.services.PaiementService;

import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        AbonnementDAO abonnementDAO = new AbonnementDAO();
        PaiementDAO paiementDAO = new PaiementDAO();

        AbonnementService abonnementService = new AbonnementService(abonnementDAO, paiementDAO);
        PaiementService paiementService = new PaiementService(paiementDAO, abonnementDAO);

        MenuPrincipal menu = new MenuPrincipal(abonnementService, paiementService);
        menu.demarrer();

    }
}
