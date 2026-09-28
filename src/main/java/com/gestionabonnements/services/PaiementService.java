package main.java.com.gestionabonnements.services;

import main.java.com.gestionabonnements.DAO.AbonnementDAO;
import main.java.com.gestionabonnements.DAO.PaiementDAO;
import main.java.com.gestionabonnements.entites.Abonnement;
import main.java.com.gestionabonnements.entites.Paiement;
import main.java.com.gestionabonnements.entites.StatutPaiement;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PaiementService {
    private final PaiementDAO paiementDAO;
    private final AbonnementDAO abonnementDAO;

    public PaiementService(PaiementDAO paiementDAO, AbonnementDAO abonnementDAO) {
        this.paiementDAO = paiementDAO;
        this.abonnementDAO = abonnementDAO;
    }

    public PaiementDAO getPaiementDAO() {
        return paiementDAO;
    }

    public void enregistrerPaiement(String idPaiement, LocalDate datePaiement) {
        Paiement paiement = paiementDAO.findById(idPaiement)
                .orElseThrow(() -> new IllegalArgumentException("Aucun paiement trouvé avec l'id : " + idPaiement));

        if (datePaiement == null) {
            throw new IllegalArgumentException("La date de paiement est obligatoire.");
        }

        paiement.setDatePaiement(datePaiement);
        paiement.setStatut(datePaiement.isAfter(paiement.getDateEcheance())
                ? StatutPaiement.EN_RETARD
                : StatutPaiement.PAYE);

        paiementDAO.update(paiement);
    }

    public void modifierPaiement(String idPaiement, LocalDate dateEcheance, String typePaiement) {
        Paiement paiement = paiementDAO.findById(idPaiement)
                .orElseThrow(() -> new IllegalArgumentException("Aucun paiement trouvé avec l'id : " + idPaiement));

        if (dateEcheance == null) {
            throw new IllegalArgumentException("La date d'échéance est obligatoire.");
        }
        if (typePaiement == null || typePaiement.trim().isEmpty()) {
            throw new IllegalArgumentException("Le type de paiement ne peut pas être vide.");
        }

        paiement.setDateEcheance(dateEcheance);
        paiement.setTypePaiement(typePaiement);

        // Recalcule le statut si un règlement a déjà été enregistré,
        // en cohérence avec la règle de enregistrerPaiement (datePaiement <= dateEcheance => PAYE).
        if (paiement.getDatePaiement() != null) {
            paiement.setStatut(paiement.getDatePaiement().isAfter(dateEcheance)
                    ? StatutPaiement.EN_RETARD
                    : StatutPaiement.PAYE);
        }

        paiementDAO.update(paiement);
    }

    public void supprimerPaiement(String idPaiement) {
        boolean supprime = paiementDAO.delete(idPaiement);
        if (!supprime) {
            throw new IllegalArgumentException("Aucun paiement trouvé avec l'id : " + idPaiement);
        }
    }

    public double calculerMontantImpaye(String idAbonnement) {
        Abonnement abonnement = abonnementDAO.findById(idAbonnement)
                .orElseThrow(() -> new IllegalArgumentException("Aucun abonnement trouvé avec l'id : " + idAbonnement));

        List<Paiement> impayes = paiementDAO.findUnpaidByAbonnement(idAbonnement);

        return impayes.size() * abonnement.getMontantMensuel();
    }

    public double calculerSommePayee(String idAbonnement) {
        Abonnement abonnement = abonnementDAO.findById(idAbonnement)
                .orElseThrow(() -> new IllegalArgumentException("Aucun abonnement trouvé avec l'id : " + idAbonnement));

        long nombrePaiements = paiementDAO.findByAbonnement(idAbonnement).stream()
                .filter(p -> p.getStatut() == StatutPaiement.PAYE)
                .count();

        return nombrePaiements * abonnement.getMontantMensuel();
    }

    public List<Paiement> afficherDerniersPaiements() {
        return paiementDAO.findLastPayments();
    }

    public Map<YearMonth, Double> genererRapportMensuel() {
        return paiementDAO.findAll().stream()
                .filter(p -> p.getStatut() == StatutPaiement.PAYE)
                .collect(Collectors.groupingBy(
                        p -> YearMonth.from(p.getDatePaiement()),
                        Collectors.summingDouble(p -> montantAbonnement(p.getIdAbonnement()))
                ));
    }

    public Map<Integer, Double> genererRapportAnnuel() {
        return paiementDAO.findAll().stream()
                .filter(p -> p.getStatut() == StatutPaiement.PAYE)
                .collect(Collectors.groupingBy(
                        p -> p.getDatePaiement().getYear(),
                        Collectors.summingDouble(p -> montantAbonnement(p.getIdAbonnement()))
                ));
    }

    public Map<String, Double> genererRapportImpayes() {
        return paiementDAO.findAll().stream()
                .filter(p -> p.getStatut() == StatutPaiement.NON_PAYE || p.getStatut() == StatutPaiement.EN_RETARD)
                .collect(Collectors.groupingBy(
                        Paiement::getIdAbonnement,
                        Collectors.summingDouble(p -> montantAbonnement(p.getIdAbonnement()))
                ));
    }

    private double montantAbonnement(String idAbonnement) {
        return abonnementDAO.findById(idAbonnement)
                .map(Abonnement::getMontantMensuel)
                .orElse(0.0);
    }


}
