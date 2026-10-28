# MiniBank — Complete Semester Project (Practicals 1–13)

A Java 17+ console banking application implementing the supplied `BankInfo.txt` practicals, organized into `model`, `util`, `service`, and `exception` packages.

## Requirements
- JDK 17 or newer (records and switch expressions require Java 16+; JDK 17 is recommended).
- No third-party dependencies.

## Build and run (Windows PowerShell)
```powershell
.uild.ps1
java -jar .\MiniBank.jar
```

## Build and run (Linux/macOS/Git Bash)
```bash
chmod +x build.sh
./build.sh
java -jar MiniBank.jar
```

Run the practical demonstrations:
```bash
java -jar MiniBank.jar --demo
```

## Main menu
1. Open account (Savings, Current, Fixed Deposit)
2. Deposit
3. Withdraw
4. Transfer
5. List accounts (sorted by balance)
6. Print account statement
7. Save accounts and generate end-of-day report
8. Exit

Accounts are saved to `data/accounts.ser`; transaction logs go to `logs/transactions.log`; reports go to `reports/end-of-day.txt`. These files are created on demand. Balances are stored as whole rupees (`long`).

## Project map
- `model`: account hierarchy, customer, interfaces, records, annotations.
- `util`: regex validation and reflection annotation validation.
- `service`: menu support, repository/store, persistence, reporting, transaction processing.
- `exception`: checked bank exception hierarchy.

## Notes
- On loading saved accounts, the account-number generator advances past the highest restored account number to avoid collisions in normal use.
- This is an educational console project, not production banking software. It has no authentication, database, encryption, or audit-grade guarantees.
