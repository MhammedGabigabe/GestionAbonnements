package main.java.com.gestionabonnements.DAO;

import main.java.com.gestionabonnements.entites.Abonnement;
import main.java.com.gestionabonnements.entites.Statut;

import java.util.*;
import java.util.stream.Collectors;

public class AbonnementDAO {
    private final Map<String, Abonnement> abonnements = new HashMap<>();

    public Abonnement create(Abonnement abonnement) {
        abonnements.put(abonnement.getId(), abonnement);
        return abonnement;
    }

    public Optional<Abonnement> findById(String id) {
        return Optional.ofNullable(abonnements.get(id));
    }

    public List<Abonnement> findAll() {
        return new ArrayList<>(abonnements.values());
    }

    public boolean update(Abonnement abonnement) {
        if (!abonnements.containsKey(abonnement.getId())) {
            return false;
        }
        abonnements.put(abonnement.getId(), abonnement);
        return true;
    }

    public boolean delete(String id) {
        return abonnements.remove(id) != null;
    }

    public List<Abonnement> findActiveSubscriptions() {
        return abonnements.values().stream()
                .filter(a -> a.getStatut() == Statut.ACTIVE)
                .collect(Collectors.toList());
    }
}
