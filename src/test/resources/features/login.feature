Feature: Authentification sur SauceDemo
  En tant qu'utilisateur, je veux pouvoir me connecter à SauceDemo
  afin d'accéder au catalogue de produits.

  Background:
    Given l'utilisateur est sur la page de login SauceDemo

  Scenario: Connexion réussie avec un utilisateur valide
    When l'utilisateur se connecte avec "standard_user" et "secret_sauce"
    Then l'utilisateur est redirigé vers la page produits

  Scenario Outline: Connexion refusée avec des identifiants invalides
    When l'utilisateur se connecte avec "<username>" et "<password>"
    Then un message d'erreur contenant "<message>" est affiché

    Examples:
      | username        | password       | message                             |
      | standard_user   | wrong_password | Username and password do not match  |
      | ghost_user      | secret_sauce   | Username and password do not match  |
      | locked_out_user | secret_sauce   | locked out                           |

  Scenario: Déconnexion depuis le menu hamburger
    Given l'utilisateur est connecté avec "standard_user" et "secret_sauce"
    When l'utilisateur se déconnecte
    Then l'utilisateur revient sur la page de login
