# GameRent 🎮

A game rental management system developed as a project for the **Object-Oriented Programming** course at **Universidade Federal do Cariri (UFCA)**.

## Features

| Feature | Description |
|---|---|
| Customer Registration | Register with name, CPF, email, and age |
| Customer Types | **Regular** (full price) and **Premium** (configurable percentage discount) |
| Game Registration | Register with name, platform, genre, daily rate, and age rating |
| Game Modalities | **Physical** (stock control) and **Digital** (simultaneous access control) |
| Age Rating | Age validation against game rating (All Ages, 10+, 12+, 14+, 16+, 18+) |
| Rental | Record linking customer and game with start date and return deadline |
| Availability Check | Stock query for physical games, available accesses for digital |
| Return | Record with date, item condition, and status |
| Fee Calculation | Based on days used × daily rate, with discount for Premium customers |
| Late Fee | Automatic addition when return exceeds deadline |
| Damage Fee | Additional fee applied when returning damaged physical games |
| Stock Control | Reserve and release units of physical games |
| Concurrent Access Control | Limit of active rentals per digital title |
| Loyalty Program | Points accumulated per timely return, free rental upon reaching the goal |
| Game Search | Filters by name, platform, genre, and age rating |
| Rental History | Complete customer history with status (active, returned, overdue) |
| Cash Register | Financial simulation with opening, transactions, change calculation, and closing |
| Top Rented Games Report | Ranking of most rented titles |
| Cash Closing Report | Balance with total income, expenses, and final amount |
| Multiple Platforms | PS5, PS4, Xbox Series X/S, Xbox One, Nintendo Switch, PC, Mobile (iOS/Android), PlayStation VR, Retro |

## Class Diagram

```
Jogo (abstract)
├── JogoFisico   (+ stock, damageLevel)
└── JogoDigital  (+ accessKey, sizeGB)

Cliente  ──────────>  Locacao  <──────────  Jogo
           (associates)             (associates)
```

- `Jogo` — abstract base class with name, platform, genre, daily rate, and age rating
- `JogoFisico` — extends `Jogo` adding stock control and damage level
- `JogoDigital` — extends `Jogo` adding access key and size in GB
- `Cliente` — represents the customer with name, CPF, email, age, and premium status
- `Locacao` — links customer and game with dates, values, and rental status

## Technologies

- **Java 21**
- **Paradigm:** Object-Oriented Programming
- **Supported IDEs:** Eclipse / IntelliJ IDEA

## Project Structure

```
GameRent/
├── src/
│   └── GameRent/
│       ├── Cliente.java
│       ├── Jogo.java
│       ├── JogoDigital.java
│       ├── JogoFisico.java
│       └── Locacao.java
├── .classpath
├── .project
└── GameRent.iml
```

## How to Run

### Via Eclipse
1. Import as existing project (`File → Import → General → Existing Projects`)
2. Compile and run

### Via terminal
```bash
javac -d bin src/GameRent/*.java
java -cp bin GameRent.<MainClass>
```

## Authors

- **Gildo Alves de Lima Junior**
- **Gilvan Alves Pastor Júnior**
- **Tomaz Ricarto de Sousa Santos**
- **Wesley Geilson Batista dos Santos**
- **William Severo Gomes**

## Course

- **Course:** Object-Oriented Programming
- **University:** Universidade Federal do Cariri (UFCA)
- **Professor:** Luana Batista da Cruz
- **Location:** Juazeiro do Norte – CE, 2026
