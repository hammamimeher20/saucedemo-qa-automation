# SauceDemo QA Automation

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Selenium](https://img.shields.io/badge/Selenium-4.24-43B02A?logo=selenium)
![TestNG](https://img.shields.io/badge/TestNG-7.10-blue)
![Cucumber](https://img.shields.io/badge/Cucumber-7.18-23D96C?logo=cucumber)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven)
[![CI](https://github.com/<TON-USER>/saucedemo-qa-automation/actions/workflows/ci.yml/badge.svg)](https://github.com/<TON-USER>/saucedemo-qa-automation/actions/workflows/ci.yml)

Suite de tests automatisés pour [SauceDemo](https://www.saucedemo.com), construite avec
**Selenium 4 + Java 17 + TestNG + Page Object Model**, enrichie de reporting,
CI/CD, logging, exécution distribuée, data-driven testing, traçabilité et BDD (Cucumber).

## Stack technique
- Selenium WebDriver 4.24 + WebDriverManager
- TestNG 7.10 (classique) et Cucumber 7.18 + cucumber-testng (BDD)
- ExtentReports 5.1 (rapport HTML)
- SLF4J + Logback (logs console + fichier)
- Docker / Selenium Grid (exécution distribuée)
- GitHub Actions (CI/CD)
- Maven

## Architecture

```
src/main/java/com/saucedemo/
├── pages/       -> Page Objects (Login, Products, ProductDetail, Cart, Checkout x3)
└── utils/       -> DriverFactory (ThreadLocal + Grid), ConfigReader, ScreenshotUtils, ExtentManager

src/test/java/com/saucedemo/
├── base/        -> BaseTest (setUp/tearDown communs, @Listeners)
├── listeners/   -> TestListener (pont TestNG <-> ExtentReports)
├── tests/       -> 9 classes de test, 44 cas au total (dont 2 data-driven)
└── bdd/
    ├── context/ -> TestContext (état partagé entre steps, via PicoContainer)
    ├── hooks/   -> Hooks (cycle de vie driver par scénario)
    ├── steps/   -> LoginSteps, CheckoutSteps
    └── runners/ -> CucumberTestRunner (AbstractTestNGCucumberTests)

src/test/resources/features/  -> login.feature, checkout.feature (Gherkin)
docs/traceability-matrix.xlsx -> Matrice de traçabilité exigences <-> cas de test
docker-compose.yml             -> Selenium Grid (Chrome + Firefox)
.github/workflows/ci.yml       -> Pipeline CI (smoke + régression cross-browser)
```

## Répartition par priorité (groupes TestNG)

| Groupe      | Objectif                              | Nb de cas |
|-------------|-----------------------------------------|-----------|
| smoke       | Parcours critiques, exécution rapide    | 12        |
| sanity      | Vérifications rapides post-déploiement  | 10        |
| regression  | Couverture complète                     | 44 (superset) |

## Exécution — suite TestNG classique

```bash
mvn clean test                          # suite complète (testng.xml)
mvn test -Dgroups=smoke
mvn test -Dgroups=sanity
mvn test -Dgroups=regression
mvn test -Dbrowser=firefox              # override du navigateur (chrome par défaut)
mvn test -Dbrowser=chrome-headless      # mode headless (CI)
```

## Exécution — scénarios Cucumber (BDD)

```bash
mvn test -DsuiteXmlFile=cucumber-suite.xml
```

Rapport généré sous `test-output/cucumber-reports/cucumber.html`.
Les `.feature` sont en français (lisibles par un non-développeur), les step
definitions réutilisent les mêmes Page Objects que la suite TestNG classique
— aucune logique Selenium dupliquée.

## Exécution distribuée (Selenium Grid / Docker)

```bash
docker compose up -d
mvn test -Dgrid.url=http://localhost:4444/wd/hub -Dgroups=smoke
docker compose down
```

VNC de debug disponible sur `http://localhost:7900` (Chrome) et `:7901` (Firefox),
mot de passe `secret`.

## CI/CD

`.github/workflows/ci.yml` : les tests `smoke` tournent en headless sur chaque
push/PR ; la `regression` s'exécute en matrice Chrome/Firefox sur `main` et
chaque nuit à 02h00 UTC. Le rapport ExtentReports est publié comme artefact
téléchargeable sur chaque run.

## Reporting

Un rapport HTML **ExtentReports** est généré automatiquement sous
`test-output/extent-reports/SauceDemo_Report_<timestamp>.html`, via
`TestListener` (branché sur `BaseTest` avec `@Listeners`) :
- Un `ExtentTest` par méthode, catégorisé par groupe (`smoke`/`sanity`/`regression`)
- Capture d'écran **base64 automatique** embarquée en cas d'échec
- `ThreadLocal<ExtentTest>` compatible avec `parallel="tests"`

## Logging

Chaque action Selenium (clic, saisie, lecture de texte) est journalisée via
SLF4J/Logback dans `BasePage` — console + fichier `test-output/logs/execution.log`.
Les mots de passe sont automatiquement masqués dans les logs.

## Data-Driven Testing

`LoginDataProviderTests` illustre l'usage de `@DataProvider` TestNG : 6 combinaisons
de login invalide et 3 comptes valides sont rejoués par la même méthode de test,
au lieu d'une méthode dédiée par cas.

## Traçabilité

`docs/traceability-matrix.xlsx` relie chaque exigence fonctionnelle (REQ-xx) à son
cas de test (TC-xx) et sa méthode Java, avec une feuille de synthèse calculée par
formules (taux d'automatisation, répartition par priorité).

## Comptes de test couverts

`standard_user`, `locked_out_user`, `problem_user`, `performance_glitch_user`,
`error_user`, `visual_user`.

## Pistes d'extension
- Exécution Edge/Safari en plus de Chrome/Firefox
- Allure en complément d'ExtentReports (timeline, tendances multi-run)
- Scénarios Cucumber supplémentaires (Panier, Tri, Navigation)
