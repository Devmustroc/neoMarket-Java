package com.nermarket.neomarket.repository;

import com.nermarket.neomarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Trouve un utilisateur par son email
     *
     * @param email l'email de l'utilisateur
     * @return un Optional contenant l'utilisateur s'il existe
     */
    Optional<User> findByEmail(String email);

    /**
     * Vérifie si un email existe déjà dans la base de données
     *
     * @param email l'email à vérifier
     * @return true si l'email existe déjà, false sinon
     */
    boolean existsByEmail(String email);

    /**
     * Trouve tous les utilisateurs avec un rôle spécifique
     *
     * @param role le rôle à rechercher
     * @return la liste des utilisateurs ayant ce rôle
     */
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
    List<User> findByRole(@Param("role") String role);

    /**
     * Trouve tous les utilisateurs dont le prénom ou le nom contient la chaîne de recherche
     *
     * @param searchTerm le terme de recherche
     * @return la liste des utilisateurs correspondants
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> searchByName(@Param("searchTerm") String searchTerm);

    /**
     * Trouve tous les utilisateurs qui ont activé l'authentification à deux facteurs
     *
     * @return la liste des utilisateurs avec 2FA activé
     */
    List<User> findByMfaEnabledTrue();

    /**
     * Trouve tous les utilisateurs qui ne sont pas encore activés
     *
     * @return la liste des utilisateurs non activés
     */
    List<User> findByEnabledFalse();

    /**
     * Compte le nombre d'utilisateurs inscrits après une date donnée
     *
     * @param date la date à partir de laquelle compter
     * @return le nombre d'utilisateurs inscrits après cette date
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :date")
    long countUsersRegisteredAfter(@Param("date") java.time.LocalDateTime date);
}