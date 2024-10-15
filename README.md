# Bank Management System

This project is a console-based bank management system implemented in Java. It simulates various banking operations, including managing banks, bank offices, ATMs, employees, and clients. Additionally, it supports business logic for loan applications, account management, and exporting/importing account data. 

## Features

### 1. Bank Management
- The system allows for the creation of multiple banks.
- Each bank has several associated offices, ATMs, and employees.
- Users can view detailed information about any bank, including its offices, employees, ATMs, and clients.

### 2. Client Management
- Clients can have multiple accounts in various banks.
- Clients can view all of their accounts in the system and export them to a text file.
- The system supports transferring accounts from one bank to another through file import/export functionality.

### 3. Loan Application System
- Clients can apply for loans from a selected bank based on specific criteria:
  - The bank is chosen based on factors like the number of ATMs, offices, employees, and interest rates.
  - The client selects a bank office that can process the loan and checks if sufficient funds are available.
  - A bank employee processes the loan application.
  - If the client does not have an account in the bank, one is automatically created.
- Loan approval is based on credit rating checks:
  - If the client's credit score is below 5000 and the bank's rating is above 50, the loan is denied.
  - Otherwise, the loan is processed via an ATM in the chosen bank office.

### 4. Exception Handling
- The system includes custom exceptions for scenarios like:
  - Loan application rejection due to insufficient credit score.
  - Missing payment accounts for clients applying for loans.
  - Invalid data entries (e.g., non-existent banks, users, or offices).
  
### 5. File Operations
- Users can export all their accounts for a specific bank into a `.txt` file.
- The system supports importing account data from a `.txt` file and transferring accounts between banks.

## Usage
1. **Bank Information**: View detailed information about a bank, including its offices, ATMs, employees, and clients.
2. **Client Information**: Retrieve all the details of a specific client, including their accounts and associated banks.
3. **Loan Application**: Apply for a loan, specifying the desired bank, loan amount, and duration.
4. **Export Accounts**: Export all user accounts for a specific bank to a `.txt` file.
5. **Import Accounts**: Import user accounts from a `.txt` file and transfer them to another bank.

## How to Run
1. Clone the repository.
2. Compile and run the `Main` class in your preferred Java IDE or using the command line.
3. Follow the console prompts to interact with the system.

## Future Enhancements
- Add a graphical user interface (GUI) for improved user experience.
- Implement additional banking operations, such as deposits and withdrawals.
