package main.java.com.gestionabonnements.presentation;

import main.java.com.gestionabonnements.entites.Abonnement;
import main.java.com.gestionabonnements.entites.Paiement;
import main.java.com.gestionabonnements.services.AbonnementService;
import main.java.com.gestionabonnements.services.PaiementService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MenuPrincipal {
    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Scanner scanner = new Scanner(System.in);
    private final AbonnementService abonnementService;
    private final PaiementService paiementService;

    public MenuPrincipal(AbonnementService abonnementService, PaiementService paiementService) {
        this.abonnementService = abonnementService;
        this.paiementService = paiementService;
    }

    public void demarrer() {
        boolean continuer = true;
        while (continuer) {
            afficherMenu();
            int choix = lireEntier("Votre choix : ");

            try {
                switch (choix) {
                    case 1: creerAbonnement(); break;
                    case 2: modifierAbonnement(); break;
                    case 3: supprimerAbonnement(); break;
                    case 4: listerAbonnements(); break;
                    case 5: afficherPaiementsAbonnement(); break;
                    case 6: enregistrerPaiement(); break;
                    case 7: modifierPaiement(); break;
                    case 8: supprimerPaiement(); break;
                    case 9: afficherPaiementsManques(); break;
                    case 10: afficherSommePayee(); break;
                    case 11: afficherDerniersPaiements(); break;
                    case 12: genererEcheances(); break;
                    case 13: genererRapports(); break;
                    case 0: continuer = false; System.out.println("Au revoir !"); break;
                    default: System.out.println("Choix invalide.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Erreur inattendue : " + e.getMessage());
            }
        }
    }

    private void afficherMenu() {
        System.out.println("\n===== Gestion des Abonnements =====");
        System.out.println("1.  Créer un abonnement");
        System.out.println("2.  Modifier un abonnement");
        System.out.println("3.  Supprimer un abonnement");
        System.out.println("4.  Consulter la liste des abonnements");
        System.out.println("5.  Afficher les paiements d'un abonnement");
        System.out.println("6.  Enregistrer un paiement");
        System.out.println("7.  Modifier un paiement");
        System.out.println("8.  Supprimer un paiement");
        System.out.println("9.  Consulter les paiements manqués (montant impayé)");
        System.out.println("10. Afficher la somme payée d'un abonnement");
        System.out.println("11. Afficher les 5 derniers paiements");
        System.out.println("12. Générer les échéances d'un abonnement");
        System.out.println("13. Générer les rapports financiers");
        System.out.println("0.  Quitter");
    }

    private void creerAbonnement() {
        System.out.println("Type d'abonnement : 1) Avec engagement  2) Sans engagement");
        int type = lireEntier("Choix : ");

        String nomService = lireTexte("Nom du service : ");
        double montant = lireDouble("Montant mensuel : ");
        LocalDate dateDebut = lireDate("Date de début (dd/MM/yyyy) : ");
        LocalDate dateFin = lireDate("Date de fin (dd/MM/yyyy) : ");

        Abonnement abonnement;
        if (type == 1) {
            int duree = lireEntier("Durée d'engagement (mois) : ");
            abonnement = abonnementService.creerAbonnementAvecEngagement(nomService, montant, dateDebut, dateFin, duree);
        } else {
            abonnement = abonnementService.creerAbonnementSansEngagement(nomService, montant, dateDebut, dateFin);
        }

        System.out.println("Abonnement créé : " + abonnement);
    }

    private void modifierAbonnement() {
        String id = lireTexte("Id de l'abonnement : ");
        String nomService = lireTexte("Nouveau nom du service : ");
        double montant = lireDouble("Nouveau montant mensuel : ");
        LocalDate dateDebut = lireDate("Nouvelle date de début (dd/MM/yyyy) : ");
        LocalDate dateFin = lireDate("Nouvelle date de fin (dd/MM/yyyy) : ");

        abonnementService.modifierAbonnement(id, nomService, montant, dateDebut, dateFin);
        System.out.println("Abonnement modifié avec succès.");
    }

    private void supprimerAbonnement() {
        String id = lireTexte("Id de l'abonnement à supprimer : ");
        abonnementService.supprimerAbonnement(id);
        System.out.println("Abonnement supprimé avec succès.");
    }

    private void listerAbonnements() {
        List<Abonnement> abonnements = abonnementService.getAbonnementDAO().findAll();
        if (abonnements.isEmpty()) {
            System.out.println("Aucun abonnement enregistré.");
        } else {
            abonnements.forEach(System.out::println);
        }
    }

    private void genererEcheances() {
        String id = lireTexte("Id de l'abonnement : ");
        List<Paiement> echeances = abonnementService.genererEcheances(id);
        System.out.println(echeances.size() + " échéance(s) générée(s) :");
        echeances.forEach(System.out::println);
    }

    private void afficherPaiementsAbonnement() {
        String idAbonnement = lireTexte("Id de l'abonnement : ");
        List<Paiement> paiements = paiementService.getPaiementDAO().findByAbonnement(idAbonnement);
        if (paiements.isEmpty()) {
            System.out.println("Aucun paiement trouvé pour cet abonnement.");
        } else {
            paiements.forEach(System.out::println);
        }
    }

    private void enregistrerPaiement() {
        String idPaiement = lireTexte("Id du paiement : ");
        LocalDate datePaiement = lireDate("Date de paiement (dd/MM/yyyy) : ");
        paiementService.enregistrerPaiement(idPaiement, datePaiement);
        System.out.println("Paiement enregistré avec succès.");
    }

    private void modifierPaiement() {
        String idPaiement = lireTexte("Id du paiement : ");
        LocalDate dateEcheance = lireDate("Nouvelle date d'échéance (dd/MM/yyyy) : ");
        String typePaiement = lireTexte("Nouveau type de paiement : ");
        paiementService.modifierPaiement(idPaiement, dateEcheance, typePaiement);
        System.out.println("Paiement modifié avec succès.");
    }

    private void supprimerPaiement() {
        String idPaiement = lireTexte("Id du paiement à supprimer : ");
        paiementService.supprimerPaiement(idPaiement);
        System.out.println("Paiement supprimé avec succès.");
    }

    private void afficherPaiementsManques() {
        String idAbonnement = lireTexte("Id de l'abonnement : ");
        double montantImpaye = paiementService.calculerMontantImpaye(idAbonnement);
        System.out.println("Montant total impayé : " + montantImpaye);
    }

    private void afficherSommePayee() {
        String idAbonnement = lireTexte("Id de l'abonnement : ");
        double sommePayee = paiementService.calculerSommePayee(idAbonnement);
        System.out.println("Somme totale payée : " + sommePayee);
    }

    private void afficherDerniersPaiements() {
        List<Paiement> derniers = paiementService.afficherDerniersPaiements();
        if (derniers.isEmpty()) {
            System.out.println("Aucun paiement enregistré.");
        } else {
            derniers.forEach(System.out::println);
        }
    }

    private void genererRapports() {
        System.out.println("Type de rapport : 1) Mensuel  2) Annuel  3) Impayés");
        int type = lireEntier("Choix : ");

        switch (type) {
            case 1:
                Map<YearMonth, Double> rapportMensuel = paiementService.genererRapportMensuel();
                rapportMensuel.forEach((mois, montant) -> System.out.println(mois + " : " + montant));
                break;
            case 2:
                Map<Integer, Double> rapportAnnuel = paiementService.genererRapportAnnuel();
                rapportAnnuel.forEach((annee, montant) -> System.out.println(annee + " : " + montant));
                break;
            case 3:
                Map<String, Double> rapportImpayes = paiementService.genererRapportImpayes();
                rapportImpayes.forEach((idAbonnement, montant) -> System.out.println(idAbonnement + " : " + montant));
                break;
            default:
                System.out.println("Type de rapport invalide.");
        }
    }

    private int lireEntier(String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            System.out.print("Veuillez entrer un nombre entier valide : ");
            scanner.next();
        }
        int valeur = scanner.nextInt();
        scanner.nextLine();
        return valeur;
    }

    private double lireDouble(String message) {
        System.out.print(message);
        while (!scanner.hasNextDouble()) {
            System.out.print("Veuillez entrer un nombre valide : ");
            scanner.next();
        }
        double valeur = scanner.nextDouble();
        scanner.nextLine();
        return valeur;
    }

    private String lireTexte(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    private LocalDate lireDate(String message) {
        while (true) {
            System.out.print(message);
            String saisie = scanner.nextLine();
            try {
                return LocalDate.parse(saisie, FORMAT_DATE);
            } catch (DateTimeParseException e) {
                System.out.println("Format de date invalide, exemple attendu : 25/12/2026");
            }
        }
    }
}
