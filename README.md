# Smart-Lab-Inventory-Manager (GUI)

A desktop-based Java application built with **Swing** to help lab assistants and students manage electrical components (like Resistors, ICs, and Microcontrollers) efficiently.

## 🌟 Key Features
* **Interactive UI:** Built using Java Swing for a user-friendly experience.
* **CRUD Operations:** Add, Update, and Delete components from the inventory.
* **Smart Highlighting:** Automatically highlights rows in **Pink** when stock levels drop below 5 units.
* **Issuance Tracking:** Maintains a separate log of which student/project a component was issued to.
* **Dynamic Data Management:** Uses `ArrayList` for flexible data storage during the session.

## 🛠️ Tech Stack
* **Language:** Java
* **Framework:** Java Swing (GUI), AWT (Layouts)
* **Concepts:** Object-Oriented Programming (OOP), List Collections, Event Handling.

## 📸 Functionality
1. **Inventory Management:** Track ID, Name, Quantity, and Price.
2. **Real-time Updates:** "Last Updated" timestamps are generated automatically using `LocalDateTime`.
3. **Issue Records:** A separate pop-up window to view all transaction history.
