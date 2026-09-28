package main.java.com.gestionabonnements.services;

import main.java.com.gestionabonnements.DAO.AbonnementDAO;
import main.java.com.gestionabonnements.DAO.PaiementDAO;
import main.java.com.gestionabonnements.entites.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AbonnementService {
    private final AbonnementDAO abonnementDAO;
    private final PaiementDAO paiementDAO;

    public AbonnementService(AbonnementDAO abonnementDAO, PaiementDAO paiementDAO) {
        this.abonnementDAO = abonnementDAO;
        this.paiementDAO = paiementDAO;
    }

    public AbonnementDAO getAbonnementDAO() {
        return abonnementDAO;
    }

    public Abonnement creerAbonnementAvecEngagement(String nomService, double montantMensuel,
                                                    LocalDate dateDebut, LocalDate dateFin,
                                                    int dureeEngagementMois) {
        validerDonnees(nomService, montantMensuel, dateDebut, dateFin);
        Abonnement abonnement = new AbonnementAvecEngagement(nomService, montantMensuel, dateDebut, dateFin, dureeEngagementMois);
        return abonnementDAO.create(abonnement);
    }

    public Abonnement creerAbonnementSansEngagement(String nomService, double montantMensuel,
                                                    LocalDate dateDebut, LocalDate dateFin) {
        validerDonnees(nomService, montantMensuel, dateDebut, dateFin);
        Abonnement abonnement = new AbonnementSansEngagement(nomService, montantMensuel, dateDebut, dateFin);
        return abonnementDAO.create(abonnement);
    }

    public void modifierAbonnement(String id, String nomService, double montantMensuel,
                                   LocalDate dateDebut, LocalDate dateFin) {
        Abonnement abonnement = abonnementDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aucun abonnement trouvé avec l'id : " + id));

        validerDonnees(nomService, montantMensuel, dateDebut, dateFin);

        abonnement.setNomService(nomService);
        abonnement.setMontantMensuel(montantMensuel);
        abonnement.setDateDebut(dateDebut);
        abonnement.setDateFin(dateFin);

        abonnementDAO.update(abonnement);
    }

    public void supprimerAbonnement(String id) {
        boolean supprime = abonnementDAO.delete(id);
        if (!supprime) {
            throw new IllegalArgumentException("Aucun abonnement trouvé avec l'id : " + id);
        }
    }

    public void resilierAbonnement(String id) {
        Abonnement abonnement = abonnementDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aucun abonnement trouvé avec l'id : " + id));

        abonnement.setStatut(Statut.RESILIE);
        abonnementDAO.update(abonnement);
    }

    public List<Paiement> genererEcheances(String idAbonnement) {
        Abonnement abonnement = abonnementDAO.findById(idAbonnement)
                .orElseThrow(() -> new IllegalArgumentException("Aucun abonnement trouvé avec l'id : " + idAbonnement));

        LocalDate debut = abonnement.getDateDebut();
        LocalDate fin = abonnement.getDateFin();

        long nombreMois = ChronoUnit.MONTHS.between(debut, fin);

        List<Paiement> echeances = Stream.iterate(debut, date -> date.plusMonths(1))
                .limit(nombreMois)
                .map(dateEcheance -> new Paiement(
                        abonnement.getId(),
                        dateEcheance,
                        null,
                        "Mensuel",
                        StatutPaiement.NON_PAYE))
                .collect(Collectors.toList());

        echeances.forEach(paiementDAO::create);

        return echeances;
    }

    private void validerDonnees(String nomService, double montantMensuel, LocalDate dateDebut, LocalDate dateFin) {
        if (nomService == null || nomService.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du service ne peut pas être vide.");
        }
        if (montantMensuel <= 0) {
            throw new IllegalArgumentException("Le montant mensuel doit être supérieur à 0.");
        }
        if (dateDebut == null || dateFin == null) {
            throw new IllegalArgumentException("Les dates de début et de fin sont obligatoires.");
        }
        if (!dateFin.isAfter(dateDebut)) {
            throw new IllegalArgumentException("La date de fin doit être postérieure à la date de début.");
        }
    }
}
