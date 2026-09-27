Feature: Tunnel d'achat SauceDemo
  En tant qu'utilisateur connecté, je veux pouvoir finaliser une commande
  afin de recevoir la confirmation d'achat.

  Background:
    Given l'utilisateur est connecté avec "standard_user" et "secret_sauce"
    And l'utilisateur a ajouté le premier produit au panier

  Scenario: Commande finalisée avec succès
    When l'utilisateur passe au checkout avec le prénom "Meher", le nom "Hammami" et le code postal "1000"
    And l'utilisateur finalise la commande
    Then le message de confirmation "Thank you for your order!" est affiché

  Scenario: Checkout refusé si le prénom est vide
    When l'utilisateur passe au checkout avec le prénom "", le nom "Hammami" et le code postal "1000"
    Then un message d'erreur du checkout contenant "First Name is required" est affiché

  Scenario: Checkout refusé si le code postal est vide
    When l'utilisateur passe au checkout avec le prénom "Meher", le nom "Hammami" et le code postal ""
    Then un message d'erreur du checkout contenant "Postal Code is required" est affiché
