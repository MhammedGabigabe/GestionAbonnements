package main.java.com.gestionabonnements.DAO;

import main.java.com.gestionabonnements.entites.Paiement;
import main.java.com.gestionabonnements.entites.StatutPaiement;

import java.util.*;
import java.util.stream.Collectors;

public class PaiementDAO {
    private final Map<String, Paiement> paiements = new HashMap<>();

    public Paiement create(Paiement paiement) {
        paiements.put(paiement.getIdPaiement(), paiement);
        return paiement;
    }

    public Optional<Paiement> findById(String idPaiement) {

        return Optional.ofNullable(paiements.get(idPaiement));
    }

    public List<Paiement> findByAbonnement(String idAbonnement) {
        return paiements.values().stream()
                .filter(p -> p.getIdAbonnement().equals(idAbonnement))
                .collect(Collectors.toList());
    }

    public List<Paiement> findAll() {
        return new ArrayList<>(paiements.values());
    }

    public boolean update(Paiement paiement) {
        if (!paiements.containsKey(paiement.getIdPaiement())) {
            return false;
        }
        paiements.put(paiement.getIdPaiement(), paiement);
        return true;
    }

    public boolean delete(String idPaiement) {
        return paiements.remove(idPaiement) != null;
    }

    public List<Paiement> findUnpaidByAbonnement(String idAbonnement) {
        return paiements.values().stream()
                .filter(p -> p.getIdAbonnement().equals(idAbonnement))
                .filter(p -> p.getStatut() == StatutPaiement.NON_PAYE
                        || p.getStatut() == StatutPaiement.EN_RETARD)
                .collect(Collectors.toList());
    }

    public List<Paiement> findLastPayments() {
        return paiements.values().stream()
                .sorted(Comparator.comparing(Paiement::getDateEcheance).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }
}
